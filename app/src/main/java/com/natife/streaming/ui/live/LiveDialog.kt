package com.natife.streaming.ui.live

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.util.Util
import com.natife.streaming.R
import com.natife.streaming.base.BaseDialog
import com.natife.streaming.ext.subscribe
import kotlinx.android.synthetic.main.dialog_live.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvWatchLive.setOnClickListener {
            start_group.visibility = View.GONE
            live_video_view.visibility = View.VISIBLE
            releasePlayer()

        }
        tvWatchFromStart.setOnClickListener {
            start_group.visibility = View.GONE
            live_video_view.visibility = View.VISIBLE
            releasePlayer()
        }

        tvContinueWatch.setOnClickListener {
            start_group.visibility = View.GONE
            live_video_view.visibility = View.VISIBLE
            releasePlayer()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.onFinishClicked()
        }

        subscribe(viewModel.mediaSourceLiveData) {
            mediaSource = it
            initializePlayer()
        }

        tvTitle.text = arguments?.getString("title")
    }

    private fun initializePlayer() {
        mediaSource?.let {
            player = SimpleExoPlayer.Builder(requireContext()).build()
            live_video_view.player = player

            player!!.playWhenReady = playWhenReady
            player!!.seekTo(currentWindow, playbackPosition)
            player!!.prepare(it)
        }
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


    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT < 24 || player == null) {
            initializePlayer()
        }
    }
}