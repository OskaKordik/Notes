package com.natife.streaming.ui.player

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Renderer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ui.home.MatchAdapter
import kotlinx.android.synthetic.main.custom_playback_control.*
import kotlinx.android.synthetic.main.fragment_player.*


class PlayerFragment : BaseFragment<PlayerViewModel>() {
    override fun getLayoutRes(): Int = R.layout.fragment_player

    private var simpleExoPlayer: SimpleExoPlayer? = null

    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0

    private val matchAdapter by lazy {
        MatchAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetBehavior = BottomSheetBehavior.from(recyclerViewVideos)

        subscribe(viewModel.videoLiveData) { videoUrl ->
            initializePlayer(videoUrl)
        }

        subscribe(viewModel.matchesLiveData) { match ->
            matchAdapter.submitData(this.lifecycle, match)
        }

        with(recyclerViewVideos) {
            layoutManager = LinearLayoutManager(requireContext(),  RecyclerView.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = matchAdapter
        }

        //Focus for progress
        exo_progress.setOnFocusChangeListener { v, _ ->
            v.nextFocusDownId = R.id.recyclerViewVideos
            v.nextFocusUpId = R.id.menuPlayer
            if (v.isFocused) {
                playerView.controllerShowTimeoutMs = 5000
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        //For hiding video list
        bottomSheetHelperView.setOnFocusChangeListener { v, isFocused ->
            if (isFocused) {
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
                exo_progress.requestFocus()
                v.isFocusable = false
            }
        }

        //Focus for list of videos
        recyclerViewVideos.setOnFocusChangeListener { v, _ ->
            if (v.isFocused) {
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                recyclerViewVideos.getChildAt(0).requestFocus()
                playerView.controllerShowTimeoutMs = 0
                bottomSheetHelperView.isFocusable = true
            } else {
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    private fun showContent(show: Boolean) {
        smallGameTitle.isVisible = show
        bigGameTitle.isVisible = show
        recyclerViewVideos.isVisible = show
    }

    private fun initializePlayer(videoUrl: String) {
        if (simpleExoPlayer != null) return

        simpleExoPlayer = SimpleExoPlayer.Builder(requireContext()).build()

        val mediaItem = MediaItem.fromUri(videoUrl)
        simpleExoPlayer?.setMediaItem(mediaItem)

        playerView.player = simpleExoPlayer

        playerView.setControllerVisibilityListener { visibility ->
            if (visibility == View.VISIBLE) {
                showContent(true)
            } else if (visibility == View.GONE) {
                showContent(false)
                //bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        simpleExoPlayer?.videoScalingMode = Renderer.VIDEO_SCALING_MODE_SCALE_TO_FIT
        simpleExoPlayer?.playWhenReady = playWhenReady
        simpleExoPlayer?.seekTo(currentWindow, playbackPosition)
        simpleExoPlayer?.prepare()

        playerView.hideController()
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer?.pause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
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
}