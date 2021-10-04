package com.natife.streaming.ui.live

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.transition.AutoTransition
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.util.Util
import com.natife.streaming.R
import com.natife.streaming.base.BaseDialog
import com.natife.streaming.ext.dp
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ext.toDisplayTime
import kotlinx.android.synthetic.main.custom_playback_control.*
import kotlinx.android.synthetic.main.dialog_live.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class LiveDialog : BaseDialog<LiveViewModel>() {
    override fun getLayoutRes(): Int = R.layout.dialog_live

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_App_Dialog_FullScreen)
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(LiveDialogArgs.fromBundle(requireArguments()))
    }

    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var mediaSource: MediaSource? = null
    private val handler = Handler(Looper.getMainLooper())


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvWatchLive.setOnClickListener {
            setPlayerPosition(WatchType.WATCH_LIVE)
            start_group.visibility = View.GONE
            live_video_view.visibility = View.VISIBLE
        }
        tvWatchFromStart.setOnClickListener {
            setPlayerPosition(WatchType.WATCH_FROM_START)
            start_group.visibility = View.GONE
            live_video_view.visibility = View.VISIBLE
        }

        tvContinueWatch.setOnClickListener {
            setPlayerPosition(WatchType.CONTINUE_WATCH)
            start_group.visibility = View.GONE
            live_video_view.visibility = View.VISIBLE
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.onFinishClicked()
        }

        subscribe(viewModel.mediaSourceLiveData) {
            mediaSource = it
            initializePlayer()
        }

        subscribe(viewModel.currentPositionLiveData) { seconds ->
            tvContinueWatch.text = getString(R.string.continue_with, seconds.toLong().toDisplayTime())
        }

        bottom_button_line_layout.initButtonLive(viewModel)

        subscribe(viewModel.returnToLiveEvent) {
            if (it) {
                playbackPosition = player?.duration ?: 0
                player?.seekTo(currentWindow, playbackPosition)
                playWhenReady = true
                player?.playWhenReady = playWhenReady
            }
        }

        arguments?.getString("title")?.let {
            tvTitle.text = it
            bigGameTitle.text = it
        }

        exo_interval_rewind_30.setOnClickListener {
            player?.currentPosition?.minus(30000)
                ?.let { it1 -> player?.seekTo(it1) }
        }
        exo_interval_rewind_5.setOnClickListener {
            player?.currentPosition?.minus(5000)
                ?.let { it1 -> player?.seekTo(it1) }
        }
        exo_interval_forward_30.setOnClickListener {
            player?.currentPosition?.plus(30000)
                ?.let { it1 -> player?.seekTo(it1) }
        }
        exo_interval_forward_5.setOnClickListener {
            player?.currentPosition?.plus(5000)
                ?.let { it1 -> player?.seekTo(it1) }
        }
//        exo_next_episode.setOnClickListener {
//            player?.seekToNext()
//        }
        exo_preview.isEnabled = false
        exo_next_episode.isEnabled = false
        menuPlayer.visibility = View.GONE

        sight_of_bottom.setOnClickListener {
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
                    exo_preview.apply {
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
                    exo_next_episode.apply {
                        iconSize = 27.dp
                    }
                }

                override fun onTransitionEnd(transition: Transition) {
                    bottom_button_line_layout.visibility = View.VISIBLE
                    bigGameTitle.visibility = View.VISIBLE
                    sight_of_bottom.visibility = View.GONE
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
                exo_preview.apply {
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
                exo_next_episode.apply {
                    iconSize = 62.dp
                }
            }

            override fun onTransitionEnd(transition: Transition) {
                bottom_button_line_layout.visibility = View.GONE
                bigGameTitle.visibility = View.GONE
                sight_of_bottom.visibility = View.VISIBLE
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

    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            player!!.release()
            player = null
        }
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        player?.stop()
        handler.removeCallbacks(timerRunnable)
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {
        player?.release()
        player = SimpleExoPlayer.Builder(requireContext()).build()
        mediaSource?.let { player?.setMediaSource(it) }
        player?.addListener(object : Player.EventListener {
            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                if (playWhenReady) {
                    handler.removeCallbacks(timerRunnable)
                    handler.postDelayed(timerRunnable, 10000)
                }
                super.onPlayWhenReadyChanged(playWhenReady, reason)
            }
        })

        live_video_view.player = player
        live_video_view.controllerAutoShow = false
        live_video_view.setControllerVisibilityListener { visibility ->
            if (visibility == View.VISIBLE) {
                TransformCustomControlLayoutToBigState()
            }
        }

        player?.seekTo(currentWindow, playbackPosition)
        player?.playWhenReady = playWhenReady
        player?.prepare()
    }

    private fun setPlayerPosition(watchType: WatchType) {
        playbackPosition = when (watchType) {
            WatchType.WATCH_LIVE -> player?.duration ?: 0
            WatchType.WATCH_FROM_START -> 0
            WatchType.CONTINUE_WATCH -> {
                ((viewModel.currentPositionLiveData.value ?: 0) * 1000).toLong()
            }
        }
        player?.seekTo(currentWindow, playbackPosition)
    }

    // Timer for saving current position
    private val timerRunnable: Runnable = object : Runnable {
        override fun run() {
            player?.currentPosition?.let { position ->
                viewModel.saveCurrentPosition(position.div(1000))
            }
            handler.postDelayed(this, 10000)
        }
    }
}