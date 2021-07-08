package com.natife.streaming.ui.player

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.SeekBar
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.leanback.widget.BrowseFrameLayout
import androidx.transition.AutoTransition
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ClippingMediaSource
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.material.button.MaterialButton
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.*
import com.natife.streaming.ui.player.menu.quality.VideoQualityDialog
import kotlinx.android.synthetic.main.custom_playback_control.*
import kotlinx.android.synthetic.main.fragment_player.*
import kotlinx.android.synthetic.main.view_player_bottom_bar.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf


class PlayerFragment : BaseFragment<PlayerViewModel>() {
    override fun getLayoutRes(): Int = R.layout.fragment_player

    private var simpleExoPlayer: SimpleExoPlayer? = null
    private var start = 0L
    private var end = 0L
    private var playWhenReady = false
    private var playbackPosition: Long = 0
    private val handler = Handler(Looper.getMainLooper())
    private var seekBarState: SeekBarType = SeekBarType.BIG


    override fun onResume() {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sightOfBottom = view.findViewById<MaterialButton>(R.id.sight_of_bottom)
        val exoPlay = view.findViewById<MaterialButton>(R.id.exo_play)
        val exoPause = view.findViewById<MaterialButton>(R.id.exo_pause)
        val exoRew = view.findViewById<MaterialButton>(R.id.exo_rew)
        val exoIntervalRewind30 = view.findViewById<MaterialButton>(R.id.exo_interval_rewind_30)
        val exoIntervalRewind5 = view.findViewById<MaterialButton>(R.id.exo_interval_rewind_5)
        val exoIntervalForward5 = view.findViewById<MaterialButton>(R.id.exo_interval_forward_5)
        val exoIntervalForward30 = view.findViewById<MaterialButton>(R.id.exo_interval_forward_30)
        val exoFfwd = view.findViewById<MaterialButton>(R.id.exo_ffwd)
        val menuPlayer = view.findViewById<MaterialButton>(R.id.menuPlayer)

        subscribeViewModels()
        menuPlayer.setOnClickListener {
            viewModel.openVideoQualityMenu()
        }

        setFragmentResultListener(VideoQualityDialog.KEY_QUALITY) { _, bundle ->
            bundle.getString(VideoQualityDialog.KEY_QUALITY)?.let { videoQuality ->
                viewModel.changeVideoQuality(videoQuality)
            }
        }
        subscribe(viewModel.videoQualityParams) {
            val d = VideoQualityDialog().apply {
                arguments = bundleOf(VIDEO_QUALITY to it)
            }
            d.show(parentFragmentManager, DIALOG_QUALITY)
        }

        subscribe(viewModel.currentSeekBarId) { id ->
            sightOfBottom?.let { imageView ->
                imageView.nextFocusDownId = id
                imageView.nextFocusLeftId = id
                imageView.nextFocusRightId = id
            }

            if (seekBarState == SeekBarType.BIG) {
                view.findViewById<SeekBar>(id)?.let { seekBar ->
                    seekBar.nextFocusDownId = sightOfBottom.id
                    seekBar.nextFocusUpId = sightOfBottom.id
                }

                exoPlay?.let { button ->
                    button.nextFocusDownId = sightOfBottom.id
                    button.nextFocusUpId = sightOfBottom.id
                }
                exoPause?.let { button ->
                    button.nextFocusDownId = sightOfBottom.id
                    button.nextFocusUpId = sightOfBottom.id
                }
                exoRew?.let { button ->
                    button.nextFocusDownId = sightOfBottom.id
                    button.nextFocusUpId = sightOfBottom.id
                    button.nextFocusLeftId = button.id
                }
                exoIntervalRewind30?.let { button ->
                    button.nextFocusDownId = sightOfBottom.id
                    button.nextFocusUpId = sightOfBottom.id
                }
                exoIntervalRewind5?.let { button ->
                    button.nextFocusDownId = sightOfBottom.id
                    button.nextFocusUpId = sightOfBottom.id
                }
                exoIntervalForward5?.let { button ->
                    button.nextFocusDownId = sightOfBottom.id
                    button.nextFocusUpId = sightOfBottom.id
                }
                exoIntervalForward30?.let { button ->
                    button.nextFocusDownId = sightOfBottom.id
                    button.nextFocusUpId = sightOfBottom.id
                }
                exoFfwd?.let { button ->
                    button.nextFocusDownId = sightOfBottom.id
                    button.nextFocusUpId = sightOfBottom.id
                    button.nextFocusRightId = button.id
                }
            } else {
                view.findViewById<SeekBar>(id)?.let { seekBar ->
                    seekBar.nextFocusDownId = if (exoPlay.isVisible) exoPlay.id else exoPause.id
                    seekBar.nextFocusUpId = if (exoPlay.isVisible) exoPlay.id else exoPause.id
                }

                menuPlayer?.let { imageView ->
                    imageView.nextFocusDownId = View.NO_ID
                    imageView.nextFocusUpId = id
                }

                exoPlay?.let { button ->
                    button.nextFocusDownId = View.NO_ID
                    button.nextFocusUpId = id
                }
                exoPause?.let { button ->
                    button.nextFocusDownId = View.NO_ID
                    button.nextFocusUpId = id
                }
                exoRew?.let { button ->
                    button.nextFocusDownId = View.NO_ID
                    button.nextFocusUpId = id
                    button.nextFocusLeftId = View.NO_ID
                }
                exoIntervalRewind30?.let { button ->
                    button.nextFocusDownId = View.NO_ID
                    button.nextFocusUpId = id
                }
                exoIntervalRewind5?.let { button ->
                    button.nextFocusDownId = View.NO_ID
                    button.nextFocusUpId = id
                }
                exoIntervalForward5?.let { button ->
                    button.nextFocusDownId = View.NO_ID
                    button.nextFocusUpId = id
                }
                exoIntervalForward30?.let { button ->
                    button.nextFocusDownId = View.NO_ID
                    button.nextFocusUpId = id
                }
                exoFfwd?.let { button ->
                    button.nextFocusDownId = View.NO_ID
                    button.nextFocusUpId = id
                    button.nextFocusRightId = id
                }
            }
        }

        sight_of_bottom.setOnClickListener {
            //трансформируем
            seekBarState = SeekBarType.SMALL
            val set = ConstraintSet()
            set.clone(custom_control_layout)
            changeToSmollCustomControlLayout(set)
            set.applyTo(custom_control_layout)
            val autoTransition = AutoTransition()
            autoTransition.duration = 100
            autoTransition.addListener(object : Transition.TransitionListener {
                override fun onTransitionStart(transition: Transition) {
                    //уменьшаем кнопки
                    exo_play.apply {
                        iconSize = 27.dp
                    }
                    exo_pause.apply {
                        iconSize = 27.dp
                    }
                    exo_rew.apply {
                        iconSize = 27.dp
                    }
                    exo_interval_rewind_30.apply {
                        iconSize = 27.dp
                    }
                    exo_interval_rewind_5.apply {
                        iconSize = 27.dp
                    }
                    exo_interval_forward_5.apply {
                        iconSize = 27.dp
                    }
                    exo_interval_forward_30.apply {
                        iconSize = 27.dp
                    }
                    exo_ffwd.apply {
                        iconSize = 27.dp
                    }
                }

                override fun onTransitionEnd(transition: Transition) {
                    bottom_button_line_layout.visibility = View.VISIBLE
                    bigGameTitle.visibility = View.VISIBLE
                    sight_of_bottom.visibility = View.GONE
                    menuPlayer.visibility = View.VISIBLE
                    if (exo_play.isVisible) exo_play.requestFocus() else exo_pause.requestFocus()
                }

                override fun onTransitionCancel(transition: Transition) {}
                override fun onTransitionPause(transition: Transition) {}
                override fun onTransitionResume(transition: Transition) {}
            })
            TransitionManager.beginDelayedTransition(
                custom_control_layout,
                autoTransition
            )
        }

        sliders_place_layout.onFocusSearchListener =
            BrowseFrameLayout.OnFocusSearchListener { focused, direction ->
                when (direction) {
                    33 -> {//top
                        seekBarState = SeekBarType.BIG
                        TransformCustomControlLayoutToBigState()
                        return@OnFocusSearchListener null
                    }
                    else -> return@OnFocusSearchListener null
                }
            }
    }

    private fun TransformCustomControlLayoutToBigState() {
        //трансформируем
        val set = ConstraintSet()
        set.clone(custom_control_layout)
        changeToBigCustomControlLayout(set)
        set.applyTo(custom_control_layout)
        val autoTransition = AutoTransition()
        autoTransition.duration = 100
        autoTransition.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {
                //уменьшаем кнопки
                exo_play.apply {
                    iconSize = 62.dp
                }
                exo_pause.apply {
                    iconSize = 62.dp
                }
                exo_rew.apply {
                    iconSize = 62.dp
                }
                exo_interval_rewind_30.apply {
                    iconSize = 62.dp
                }
                exo_interval_rewind_5.apply {
                    iconSize = 62.dp
                }
                exo_interval_forward_5.apply {
                    iconSize = 62.dp
                }
                exo_interval_forward_30.apply {
                    iconSize = 62.dp
                }
                exo_ffwd.apply {
                    iconSize = 62.dp
                }
            }

            override fun onTransitionEnd(transition: Transition) {
                bottom_button_line_layout.visibility = View.GONE
                bigGameTitle.visibility = View.GONE
                sight_of_bottom.visibility = View.VISIBLE
                menuPlayer.visibility = View.GONE
                if (exo_play.isVisible) exo_play.requestFocus() else exo_pause.requestFocus()
            }

            override fun onTransitionCancel(transition: Transition) {}
            override fun onTransitionPause(transition: Transition) {}
            override fun onTransitionResume(transition: Transition) {}
        })
        TransitionManager.beginDelayedTransition(
            custom_control_layout,
            autoTransition
        )
    }

    private fun changeToBigCustomControlLayout(set: ConstraintSet) {
        // увеличиваем меню
        set.clear(R.id.player_bottom_bar, ConstraintSet.BOTTOM)
        set.connect(
            R.id.player_bottom_bar,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM,
            48.dp
        )
        //переносим кнопки
        set.clear(R.id.exo_play, ConstraintSet.TOP)
        set.clear(R.id.exo_play, ConstraintSet.BOTTOM)
        set.connect(
            R.id.exo_play,
            ConstraintSet.TOP,
            R.id.center_max_control_panel_guideline,
            ConstraintSet.TOP,
            0.dp
        )
        set.connect(
            R.id.exo_play,
            ConstraintSet.BOTTOM,
            R.id.center_max_control_panel_guideline,
            ConstraintSet.BOTTOM,
            0.dp
        )
        set.clear(R.id.exo_pause, ConstraintSet.TOP)
        set.clear(R.id.exo_pause, ConstraintSet.BOTTOM)
        set.connect(
            R.id.exo_pause,
            ConstraintSet.TOP,
            R.id.center_max_control_panel_guideline,
            ConstraintSet.TOP,
            0.dp
        )
        set.connect(
            R.id.exo_pause,
            ConstraintSet.BOTTOM,
            R.id.center_max_control_panel_guideline,
            ConstraintSet.BOTTOM,
            0.dp
        )
        // переносим боковые привязки
        set.clear(R.id.exo_rew, ConstraintSet.START)
        set.connect(
            R.id.exo_rew,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START,
            0.dp
        )
        set.clear(R.id.exo_ffwd, ConstraintSet.END)
        set.connect(
            R.id.exo_ffwd,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END,
            0.dp
        )
    }


    private fun changeToSmollCustomControlLayout(set: ConstraintSet) {
        // увеличиваем меню
        set.clear(R.id.player_bottom_bar, ConstraintSet.BOTTOM)
        set.connect(
            R.id.player_bottom_bar,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM,
            200.dp
        )
        //переносим кнопки
        set.clear(R.id.exo_play, ConstraintSet.TOP)
        set.clear(R.id.exo_play, ConstraintSet.BOTTOM)
        set.connect(
            R.id.exo_play,
            ConstraintSet.TOP,
            R.id.top_mini_control_panel_guideline,
            ConstraintSet.TOP,
            0.dp
        )
        set.connect(
            R.id.exo_play,
            ConstraintSet.BOTTOM,
            R.id.top_mini_control_panel_guideline,
            ConstraintSet.BOTTOM,
            0.dp
        )
        set.clear(R.id.exo_pause, ConstraintSet.TOP)
        set.clear(R.id.exo_pause, ConstraintSet.BOTTOM)
        set.connect(
            R.id.exo_pause,
            ConstraintSet.TOP,
            R.id.top_mini_control_panel_guideline,
            ConstraintSet.TOP,
            0.dp
        )
        set.connect(
            R.id.exo_pause,
            ConstraintSet.BOTTOM,
            R.id.top_mini_control_panel_guideline,
            ConstraintSet.BOTTOM,
            0.dp
        )
        // переносим боковые привязки
        set.clear(R.id.exo_rew, ConstraintSet.START)
        set.connect(
            R.id.exo_rew,
            ConstraintSet.START,
            R.id.left_mini_control_panel_guideline,
            ConstraintSet.START,
            0.dp
        )
        set.clear(R.id.exo_ffwd, ConstraintSet.END)
        set.connect(
            R.id.exo_ffwd,
            ConstraintSet.END,
            R.id.right_mini_control_panel_guideline,
            ConstraintSet.END,
            0.dp
        )
    }

    @SuppressLint("RestrictedApi")
    private fun subscribeViewModels() {

        subscribeEvent(viewModel.videoLiveData) { videoUrl ->
            initializePlayer(videoUrl)

            subscribe(viewModel.initBottomBarData) {
//                Timber.tag("TAG").d(Gson().toJson(it))
                if (it != null) {
                    player_bottom_bar.initVideoUrl(
                        it,
                        getScreenWidth(requireActivity()) - 100.dp, // плавающий отступ в макете для компенсации округления при выборе длинны
                        simpleExoPlayer,
                        viewModel
                    )
                }
            }



            subscribe(viewModel.currentEpisode) {
                start = it.startMs
                end = it.endMs
                viewModel.currentWindow = it.half
                try {
                    simpleExoPlayer?.seekTo(it.half, it.startMs)
                } catch (e: IllegalSeekPositionException) {
//                    Timber.tag("TAG").d("PlaerFragment --------  /n------${Gson().toJson(it)}")
                    releasePlayer()
                    viewModel.onBackClicked()
                }
                //update SeekBar
                player_bottom_bar.updatePosition(
                    simpleExoPlayer?.currentWindowIndex ?: viewModel.currentWindow,
                    simpleExoPlayer?.contentPosition
                )
                handler.removeCallbacks(timerRunnable)
                handler.postDelayed(timerRunnable, 500)

                simpleExoPlayer?.playWhenReady = true
                viewModel.isNewEpisodeStarted = true
            }

        }
        // bottom buttons
        subscribe(viewModel.sourceLiveData) { source ->
            bottom_button_line_layout.initButtons(source, viewModel)
        }

        subscribe(viewModel.matchInfoLiveData) { info ->
            bigGameTitle.text = info
        }

        exo_interval_rewind_30.setOnClickListener {
            simpleExoPlayer?.currentPosition?.minus(30000)
                ?.let { it1 -> simpleExoPlayer?.seekTo(it1) }
        }
        exo_interval_rewind_5.setOnClickListener {
            simpleExoPlayer?.currentPosition?.minus(5000)
                ?.let { it1 -> simpleExoPlayer?.seekTo(it1) }
        }
        exo_interval_forward_30.setOnClickListener {
            simpleExoPlayer?.currentPosition?.plus(30000)
                ?.let { it1 -> simpleExoPlayer?.seekTo(it1) }
        }
        exo_interval_forward_5.setOnClickListener {
            simpleExoPlayer?.currentPosition?.plus(5000)
                ?.let { it1 -> simpleExoPlayer?.seekTo(it1) }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (playerView.isControllerVisible) playerView.hideController() else {
                viewModel.onBackClicked()
            }
        }
    }

    // Timer for episode seekbar
    private val timerRunnable: Runnable = object : Runnable {
        override fun run() {
            if (end > 0 && simpleExoPlayer?.contentPosition!! >= end) {
                simpleExoPlayer?.playWhenReady = false
                // next episode
                player_bottom_bar.nextEpisode(
                    simpleExoPlayer?.currentWindowIndex ?: viewModel.currentWindow,
                    simpleExoPlayer?.contentPosition
                )
            } else {
                player_bottom_bar.updatePosition(
                    simpleExoPlayer?.currentWindowIndex ?: viewModel.currentWindow,
                    simpleExoPlayer?.contentPosition
                )
                handler.postDelayed(this, 500)
            }
        }

    }

    private fun initializePlayer(list: List<Pair<String, Long>>) {
        if (simpleExoPlayer != null) {
            simpleExoPlayer?.release()
            simpleExoPlayer = null
        }

        simpleExoPlayer = SimpleExoPlayer.Builder(requireContext()).build()
        //hack for full playlist duration
        val concatenatedSource = ConcatenatingMediaSource()
        list.forEach {
            concatenatedSource.addMediaSource(
                ClippingMediaSource(
                    ProgressiveMediaSource.Factory(
                        DefaultDataSourceFactory(
                            requireContext()
                        )
                    ).createMediaSource(
                        MediaItem.fromUri(it.first)
                    ), it.second * 1000
                )
            )
        }

        simpleExoPlayer?.setMediaSource(concatenatedSource)


        playerView.player = simpleExoPlayer
        playerView.controllerAutoShow = false
        playerView.setControllerVisibilityListener { visibility ->
            if (visibility == View.VISIBLE) {

            } else if (visibility == View.GONE) {
//                showContent(false)
//                //трансформируем
//                val set = ConstraintSet()
//                set.clone(custom_control_layout)
//                changeToBigCustomControlLayout(set)
//                TransitionManager.beginDelayedTransition(custom_control_layout)
//                set.applyTo(custom_control_layout)
                TransformCustomControlLayoutToBigState()
            }
        }

        simpleExoPlayer?.videoScalingMode = Renderer.VIDEO_SCALING_MODE_SCALE_TO_FIT
        simpleExoPlayer?.playWhenReady = playWhenReady

        simpleExoPlayer?.addListener(object : Player.EventListener {
            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                if (playWhenReady && simpleExoPlayer!!.currentPosition >= end) {
                    handler.removeCallbacks(timerRunnable)
                    handler.postDelayed(timerRunnable, 500)
                }
                super.onPlayWhenReadyChanged(playWhenReady, reason)
            }

//            override fun onPlayerError(error: ExoPlaybackException) {
//                //TODO убрать
//                if (error.type == ExoPlaybackException.TYPE_SOURCE) {
//                    val cause = error.sourceException
//                    Timber.tag("TAG")
//                        .d("onPlayerError ---ExoPlaybackException.TYPE_SOURCE ---${cause}")
//                    viewModel.onBackClicked()
//                }
//            }
        })
        playerView.setShowMultiWindowTimeBar(true)
        simpleExoPlayer?.prepare()

        playerView.hideController()
    }


    override fun onPause() {
        super.onPause()
        simpleExoPlayer?.pause()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(timerRunnable)
        simpleExoPlayer?.stop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        if (simpleExoPlayer != null) {
            playWhenReady = simpleExoPlayer?.playWhenReady!!
            playbackPosition = simpleExoPlayer?.currentPosition!!
            viewModel.currentWindow = simpleExoPlayer?.currentWindowIndex!!
            simpleExoPlayer?.release()
            simpleExoPlayer = null
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(PlayerFragmentArgs.fromBundle(requireArguments()))
    }

    private fun getScreenWidth(activity: Activity): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity.windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }
    }

//    override fun onError(throwable: Throwable) {
//        when (throwable) {
//            is ExoPlaybackException -> {viewModel.onBackClicked()}
//            is IllegalSeekPositionException -> {viewModel.onBackClicked()}
//            else -> super.onError(throwable)
//        }
//    }

    companion object {
        enum class SeekBarType {
            BIG,
            SMALL,
        }

        const val VIDEO_QUALITY = "videoQualityParams"
        const val DIALOG_QUALITY = "dialogQuality"
    }
}
