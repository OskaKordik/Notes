package com.natife.streaming.ui.player

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.leanback.widget.BrowseFrameLayout.OnChildFocusListener
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Renderer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ClippingMediaSource
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.dp
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ext.toDisplayTime
import com.natife.streaming.ui.player.menu.quality.VideoQualityDialog
import kotlinx.android.synthetic.main.custom_playback_control.*
import kotlinx.android.synthetic.main.fragment_player.*
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

    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null
    private var start = 0L
    private var end = 0L
    private var playWhenReady = false
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    var animation: ValueAnimator? = null
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
//        exo_play.setOnKeyListener { v, keyCode, event ->
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


        progress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                if (fromUser) {
                    simpleExoPlayer?.seekTo((start + progress) * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                handler.removeCallbacks(timerRunnable)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                handler.postDelayed(timerRunnable, 1000)
            }

        })


        var isShow = false
        parentLayout.onChildFocusListener = object : OnChildFocusListener {
            override fun onRequestFocusInDescendants(
                direction: Int,
                previouslyFocusedRect: Rect?
            ): Boolean {
                return false
            }

            override fun onRequestChildFocus(child: View?, focused: View?) {
                try {
                    if (child?.id == recyclerViewVideos.id) {
                        if (!isShow) {
                            animation?.cancel()
                            animation = getShowAnimation()
                            animation!!.start()
                            isShow = true
                        }
                        playerView.controllerShowTimeoutMs = -1
                    } else {
                        if (isShow) {
                            animation?.cancel()
                            animation = getHideAnimation()
                            animation!!.start()
                            isShow = false
                        }
                        playerView.controllerShowTimeoutMs = 5000
                    }
                } catch (e: Exception) {

                }
            }

        }
    }

    fun getShowAnimation(): ValueAnimator {

        return ValueAnimator.ofInt(
            recyclerViewVideos.height,
            requireActivity().windowManager.defaultDisplay.height / 2
        ).apply {
            addUpdateListener { animator ->
                val lp = recyclerViewVideos.layoutParams
                recyclerViewVideos.layoutParams =
                    lp.apply { height = animator.animatedValue as Int }
            }
            duration = 500
        }

    }

    fun getHideAnimation(): ValueAnimator {
        return ValueAnimator.ofInt(recyclerViewVideos.height, 80.dp).apply {
            addUpdateListener { animator ->
                val lp = recyclerViewVideos.layoutParams
                recyclerViewVideos.layoutParams =
                    lp.apply { height = animator.animatedValue as Int }
            }
            duration = 500
        }

    }


    @SuppressLint("RestrictedApi")
    private fun subscribeViewModels() {
        subscribe(viewModel.videoLiveData) { videoUrl ->
            initializePlayer(videoUrl)

            subscribe(viewModel.currentEpisode) {
                if (it.start >= 0 && it.end > 0) {
                    groupFragments.isVisible = true
                    groupFull.isVisible = false
                    start = it.start
                    end = it.end
                    currentWindow = it.half
                    val max = (it.end - it.start)
                    progress.max = max.toInt()
                    duration.text = max.toDisplayTime()
                    simpleExoPlayer?.seekTo((it.half - 1).toInt(), it.start * 1000)
                    handler.removeCallbacks(timerRunnable)
                    handler.postDelayed(timerRunnable, 1000)
                } else {
                    groupFragments.isVisible = false
                    groupFull.isVisible = true
                    //  val max = simpleExoPlayer!!.duration
                    start = it.start
                    end = it.end


                    // progress.max = max.toInt()
                    simpleExoPlayer?.seekTo((it.half).toInt(), it.start * 1000)
                    handler.removeCallbacks(timerRunnable)
                    handler.postDelayed(timerRunnable, 1000)
                }

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
            smallGameTitle.text = matchInfo.info
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
        smallGameTitle.isVisible = show
        bigGameTitle.isVisible = show
        recyclerViewVideos.isVisible = show
    }

    private fun showControls(show: Boolean) {
        progress.isVisible = show
        duration.isVisible = show
        position.isVisible = show
        exo_play.isVisible = show
        exo_pause.isVisible = show
        menuPlayer.isVisible = show
    }

    // Timer for episode seekbar
    private val timerRunnable: Runnable = object : Runnable {
        override fun run() {
            if (end > 0) {
                progress.progress =
                    ((simpleExoPlayer?.contentPosition?.div(1000))?.minus(start))?.toInt() ?: 0
                position.text =
                    (((simpleExoPlayer?.contentPosition?.div(1000))?.minus(start))
                        ?: 0).toDisplayTime()
                if (simpleExoPlayer?.contentPosition?.div(1000)!! >= end) {
                    simpleExoPlayer?.playWhenReady = false
                    viewModel.toNextEpisode()
                } else {
                    handler.postDelayed(this, 1000)
                }
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
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        simpleExoPlayer?.videoScalingMode = Renderer.VIDEO_SCALING_MODE_SCALE_TO_FIT
        simpleExoPlayer?.playWhenReady = playWhenReady

        simpleExoPlayer?.addListener(object : Player.EventListener {
            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                if (playWhenReady && simpleExoPlayer!!.currentPosition / 1000 >= end) {
                    if (viewModel.isLastEpisode() && end > 0) {
                        simpleExoPlayer!!.seekTo(start * 1000)
                    }
                    handler.removeCallbacks(timerRunnable)
                    handler.postDelayed(timerRunnable, 1000)
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
            currentWindow = simpleExoPlayer?.currentWindowIndex!!
            simpleExoPlayer?.release()
            simpleExoPlayer = null
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(PlayerFragmentArgs.fromBundle(requireArguments()))
    }
}
