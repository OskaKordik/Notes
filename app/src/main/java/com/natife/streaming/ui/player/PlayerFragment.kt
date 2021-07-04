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
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.leanback.widget.BrowseFrameLayout
import androidx.transition.AutoTransition
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Renderer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ClippingMediaSource
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.material.button.MaterialButton
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.dp
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ext.toDisplayTime
import com.natife.streaming.ui.player.menu.quality.VideoQualityDialog
import kotlinx.android.synthetic.main.custom_playback_control.*
import kotlinx.android.synthetic.main.fragment_player.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.android.synthetic.main.view_player_bottom_bar.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf


class PlayerFragment : BaseFragment<PlayerViewModel>() {
    override fun getLayoutRes(): Int = R.layout.fragment_player

    private var simpleExoPlayer: SimpleExoPlayer? = null

    private val adapter: BottomPlaylistAdapter by lazy {
        BottomPlaylistAdapter() { episode, playlist ->
            viewModel.play(episode, playlist)
        }
    }

    //    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null
    private var start = 0L
    private var end = 0L
    private var playWhenReady = false
    private var playbackPosition: Long = 0

    //    var animation: ValueAnimator? = null
    private val handler = Handler(Looper.getMainLooper())


    override fun onResume() {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeViewModels()
        recyclerViewVideos.adapter = adapter

        menuPlayer.setOnClickListener {
            viewModel.openVideoQualityMenu()
        }

        setFragmentResultListener(VideoQualityDialog.KEY_QUALITY) { _, bundle ->
            bundle.getString(VideoQualityDialog.KEY_QUALITY)?.let { videoQuality ->
                viewModel.changeVideoQuality(videoQuality)
            }
        }
        subscribe(viewModel.currentSeekBarId) { id ->
            view.findViewById<MaterialButton>(R.id.exo_play)?.let { button ->
                button.nextFocusDownId = id
                button.nextFocusUpId = id
            }
            view.findViewById<MaterialButton>(R.id.exo_pause)?.let { button ->
                button.nextFocusDownId = id
                button.nextFocusUpId = id
            }
            view.findViewById<MaterialButton>(R.id.exo_rew)?.let { button ->
                button.nextFocusDownId = id
                button.nextFocusUpId = id
            }
            view.findViewById<MaterialButton>(R.id.exo_interval_rewind_30)?.let { button ->
                button.nextFocusDownId = id
                button.nextFocusUpId = id
            }
            view.findViewById<MaterialButton>(R.id.exo_interval_rewind_5)?.let { button ->
                button.nextFocusDownId = id
                button.nextFocusUpId = id
            }
            view.findViewById<MaterialButton>(R.id.exo_interval_forward_5)?.let { button ->
                button.nextFocusDownId = id
                button.nextFocusUpId = id
            }
            view.findViewById<MaterialButton>(R.id.exo_interval_forward_30)?.let { button ->
                button.nextFocusDownId = id
                button.nextFocusUpId = id
            }
            view.findViewById<MaterialButton>(R.id.exo_ffwd)?.let { button ->
                button.nextFocusDownId = id
                button.nextFocusUpId = id
            }
        }
//
//        //Focus for progress
//        progress.setOnFocusChangeListener { v, _ ->
//            v.nextFocusDownId = R.id.recyclerViewVideos
//            v.nextFocusUpId = R.id.menuPlayer
//            if (v.isFocused) {
//                //Timeout for hiding video player controls
//                playerView.controllerShowTimeoutMs = 5000
//                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
//            }
//        }
//        //Focus for progress full match
//        exo_progress.setOnFocusChangeListener { v, _ ->
//            v.nextFocusDownId = R.id.recyclerViewVideos
//            v.nextFocusUpId = R.id.menuPlayer
//            if (v.isFocused) {
//                //Timeout for hiding video player controls
//                playerView.controllerShowTimeoutMs = 5000
//
//                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
//            }
//        }
//
//
//        sliders_place.setOnKeyListener { v, keyCode, event ->
//            when (keyCode) {
//                KeyEvent.KEYCODE_DPAD_DOWN -> {
//                    menuPlayer.visibility = View.GONE
//                    menuPlayer.requestFocus()
//                    return@setOnKeyListener true
//                }
//                else -> return@setOnKeyListener false
//            }
//            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
//                if (end >= 0) {
//                    progress.requestFocus()
//                } else {
//                    exo_progress.requestFocus()
//                }
//                return@setOnKeyListener true
//            }
//            return@setOnKeyListener false
//
//            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
//                if (end >= 0) {
//                    progress.requestFocus()
//                } else {
//                    exo_progress.requestFocus()
//                }
//                return@setOnKeyListener true
//            }
//
//            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
//                if (end >= 0) {
//                    progress.requestFocus()
//                } else {
//                    exo_progress.requestFocus()
//                }
//                return@setOnKeyListener true
//            }
//            return@setOnKeyListener false
//        }
//
//        exo_pause.setOnKeyListener { v, keyCode, event ->
//            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
//                if (end >= 0) {
//                    progress.requestFocus()
//                } else {
//                    exo_progress.requestFocus()
//                }
//                return@setOnKeyListener true
//            }
//            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
//                if (end >= 0) {
//                    progress.requestFocus()
//                } else {
//                    exo_progress.requestFocus()
//                }
//                return@setOnKeyListener true
//            }
//            return@setOnKeyListener false
//        }


//        progress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(
//                seekBar: SeekBar?,
//                progress: Int,
//                fromUser: Boolean
//            ) {
//                if (fromUser) {
//                    simpleExoPlayer?.seekTo((start + progress) * 1000)
//                }
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar?) {
////                handler.removeCallbacks(timerRunnable)
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {
////                handler.postDelayed(timerRunnable, 1000)
//            }
//
//        })

//        var isShow = false
//        parentLayout.onChildFocusListener = object : OnChildFocusListener {
//            override fun onRequestFocusInDescendants(
//                direction: Int,
//                previouslyFocusedRect: Rect?
//            ): Boolean {
//                return false
//            }
//
//            override fun onRequestChildFocus(child: View?, focused: View?) {
//                try {
//                    if (child?.id == recyclerViewVideos.id) {
//
//                        if (!isShow) {
//                            animation?.cancel()
//                            animation = getShowAnimation()
//                            animation!!.start()
//                            isShow = true
//                        }
//                        playerView.controllerShowTimeoutMs = -1
//                    } else {
//                        if (isShow) {
//                            animation?.cancel()
//                            animation = getHideAnimation()
//                            animation!!.start()
//                            isShow = false
//                        }
//                        playerView.controllerShowTimeoutMs = 5000
//                    }
//                } catch (e: Exception) {
//
//                }
//            }
//
//        }

        sliders_place_layout.onFocusSearchListener =
            BrowseFrameLayout.OnFocusSearchListener { focused, direction ->
                when (direction) {
                    130 -> { //низ
                        //трансформируем
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
                        return@OnFocusSearchListener null
                    }
                    33 -> { //top
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


//    fun getShowAnimation(): ValueAnimator {
//
//        return ValueAnimator.ofInt(
//            recyclerViewVideos.height,
//            requireActivity().windowManager.defaultDisplay.height / 2
//        ).apply {
//            addUpdateListener { animator ->
//                val lp = recyclerViewVideos.layoutParams
//                recyclerViewVideos.layoutParams =
//                    lp.apply { height = animator.animatedValue as Int }
//            }
//            duration = 500
//        }
//
//    }
//
//    fun getHideAnimation(): ValueAnimator {
//        return ValueAnimator.ofInt(recyclerViewVideos.height, 80.dp).apply {
//            addUpdateListener { animator ->
//                val lp = recyclerViewVideos.layoutParams
//                recyclerViewVideos.layoutParams =
//                    lp.apply { height = animator.animatedValue as Int }
//            }
//            duration = 500
//        }
//
//    }


    @SuppressLint("RestrictedApi")
    private fun subscribeViewModels() {

        subscribe(viewModel.videoLiveData) { videoUrl ->
            initializePlayer(videoUrl)

            subscribe(viewModel.initBottomBarData) {
//                Timber.tag("TAG").d(Gson().toJson(it))
                player_bottom_bar.initVideoUrl(
                    it,
                    getScreenWidth(requireActivity()) - 100.dp, // плавающий отступ в макете для компенсации округления при выборе длинны
                    simpleExoPlayer,
                    viewModel
                )
                simpleExoPlayer?.playWhenReady = true
            }



            subscribe(viewModel.currentEpisode) {
//                if (it.start >= 0 && it.end > 0) {
//                    groupFragments.isVisible = true
                groupFull.isVisible = true
                start = it.start
                end = it.end
                viewModel.currentWindow = it.half
                val max = (it.end - it.start)
                duration.text = (max / 1000).toDisplayTime()
                simpleExoPlayer?.seekTo(it.half, it.start)

                //update SeekBar
                player_bottom_bar.updatePosition(
                    simpleExoPlayer?.currentWindowIndex ?: viewModel.currentWindow,
                    simpleExoPlayer?.contentPosition
                )
                player_bottom_bar.updatePosition(
                    viewModel.currentWindow,
                    simpleExoPlayer?.contentPosition
                )
                handler.removeCallbacks(timerRunnable)
                handler.postDelayed(timerRunnable, 500)

                simpleExoPlayer?.playWhenReady = true
            }

        }
        // bottom playlist
        subscribe(viewModel.sourceLiveData) { source ->

            adapter.submitList(source.toList())
//                playlistsContainer.removeAllViews()
//                source.toList().forEach { pair ->
//                    val title = TextView(context).apply {
//                        layoutParams = ViewGroup.LayoutParams(
//                            ViewGroup.LayoutParams.WRAP_CONTENT,
//                            ViewGroup.LayoutParams.WRAP_CONTENT
//                        )
//                        setTextColor(Color.WHITE)
//                        text = pair.first
//                    }
//                    context?.let {
//                        val recycler = HorizontalGridView(it).apply {
//                            layoutParams = ViewGroup.LayoutParams(
//                                ViewGroup.LayoutParams.MATCH_PARENT,
//                                160.dp
//                            )
//                           // layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//
//                            focusScrollStrategy = BaseGridView.FOCUS_SCROLL_ITEM
//
//                            adapter = PlaylistAdapter() {
//                                viewModel.play(it, (adapter as PlaylistAdapter).currentList)
//                            }.apply {
//                                submitList(pair.second.sortedBy { it.start })
//                            }
//                            isFocusable = true
//
//
//                        }
//
//                        playlistsContainer.addView(title)
//                        playlistsContainer.addView(recycler)
//                    }
//                }
        }

        subscribe(viewModel.matchInfoLiveData) { matchInfo ->
            //todo
//            smallGameTitle.text = matchInfo.info
            bigGameTitle.text = "${matchInfo.team1.name} : ${matchInfo.team2.name}"
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

    private fun showContent(show: Boolean) {

        //todo
//        smallGameTitle.isVisible = show
//        bigGameTitle.isVisible = show
//        recyclerViewVideos.isVisible = show
    }

//    private fun showControls(show: Boolean) {
////        progress.isVisible = show
////        duration.isVisible = show
////        position.isVisible = show
//        exo_play.isVisible = show
//        exo_pause.isVisible = show
//        menuPlayer.isVisible = show
//    }

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

                // todo нет обновления общего временни когда мы сменли вручную секбаром
                position.text =
                    (((simpleExoPlayer?.contentPosition?.div(1000))?.minus(start))
                        ?: 0).toDisplayTime()
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

        playerView.setControllerVisibilityListener { visibility ->
            if (visibility == View.VISIBLE) {
                showContent(true)
            } else if (visibility == View.GONE) {
                showContent(false)
//                //трансформируем
//                val set = ConstraintSet()
//                set.clone(custom_control_layout)
//                changeToBigCustomControlLayout(set)
//                TransitionManager.beginDelayedTransition(custom_control_layout)
//                set.applyTo(custom_control_layout)
                TransformCustomControlLayoutToBigState()


//                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        simpleExoPlayer?.videoScalingMode = Renderer.VIDEO_SCALING_MODE_SCALE_TO_FIT
        simpleExoPlayer?.playWhenReady = playWhenReady

        simpleExoPlayer?.addListener(object : Player.EventListener {
            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                if (playWhenReady && simpleExoPlayer!!.currentPosition >= end) {
//                    if (viewModel.isLastEpisode() && end > 0) { //TODO
//                        simpleExoPlayer!!.seekTo(start * 1000)
//                    }

                    handler.removeCallbacks(timerRunnable)
                    handler.postDelayed(timerRunnable, 500)
                }
                super.onPlayWhenReadyChanged(playWhenReady, reason)
            }
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
}
