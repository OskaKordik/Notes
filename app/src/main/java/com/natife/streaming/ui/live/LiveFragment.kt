package com.natife.streaming.ui.live

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_live.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import com.google.android.exoplayer2.source.MediaSource
import com.natife.streaming.ext.subscribe


class LiveFragment : BaseFragment<LiveViewModel>() {
    override fun getLayoutRes() = R.layout.fragment_live

    override fun getParameters(): ParametersDefinition = {
        parametersOf(LiveFragmentArgs.fromBundle(requireArguments()))
    }

    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var mediaSource: MediaSource? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        live_button.setOnClickListener {
            start_group.visibility = View.GONE
            live_video_view.visibility = View.VISIBLE
            releasePlayer()

        }
        from_the_start_button.setOnClickListener {
            start_group.visibility = View.GONE
            live_video_view.visibility = View.VISIBLE
            releasePlayer()
        }

        continue_with_button.setOnClickListener {
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