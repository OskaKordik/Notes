package com.natife.streaming.ui.live

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.addCallback
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
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

    private val playbackStateListener: Player.EventListener = playbackStateListener()
    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var mediaSource: MediaSource? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvWatchLive.setOnClickListener {
            start_group.visibility = View.GONE
            initializePlayer()
            live_video_view.visibility = View.VISIBLE

        }
        tvWatchFromStart.setOnClickListener {
            start_group.visibility = View.GONE
            live_video_view.visibility = View.VISIBLE
            initializePlayer()
        }

        tvContinueWatch.setOnClickListener {
            start_group.visibility = View.GONE
            live_video_view.visibility = View.VISIBLE
            initializePlayer()
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
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {
//        val uri = Uri.parse("https://moctobpltc-i.akamaihd.net/hls/live/571329/eight/playlist.m3u8")
//        val mediaSource = buildMediaSource(uri)

        player?.release()
        player = SimpleExoPlayer.Builder(requireContext()).build()
        player?.addListener(playbackStateListener)
        mediaSource?.let {
            player?.setMediaSource(it)
        }

        live_video_view.player = player

        player?.seekTo(currentWindow, playbackPosition)
        player?.playWhenReady = playWhenReady
        player?.prepare()
    }

//    private fun buildMediaSource(uri: Uri): MediaSource {
//        val dataSourceFactory: DefaultDataSourceFactory =
//            DefaultDataSourceFactory(requireContext(), "exoplayer-codelab")
//        return HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
//    }
}

private fun playbackStateListener() = object : Player.EventListener {
    override fun onPlaybackStateChanged(playbackState: Int) {
        val stateString: String = when (playbackState) {
            ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE      -"
            ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING -"
            ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY     -"
            ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED     -"
            else -> "UNKNOWN_STATE             -"
        }
//        Timber.tag("LiveDialog").d("changed state to $stateString")
    }
}