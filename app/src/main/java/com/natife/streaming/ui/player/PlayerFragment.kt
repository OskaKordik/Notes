package com.natife.streaming.ui.player

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.leanback.widget.BrowseFrameLayout
import androidx.leanback.widget.BrowseFrameLayout.OnChildFocusListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.util.Util
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.dp
import com.natife.streaming.ext.subscribe
import com.natife.streaming.ui.home.MatchAdapter
import com.natife.streaming.ui.matchprofile.MatchProfileFragmentArgs
import kotlinx.android.synthetic.main.custom_playback_control.*
import kotlinx.android.synthetic.main.fragment_player.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import timber.log.Timber


class PlayerFragment : BaseFragment<PlayerViewModel>() {
    override fun getLayoutRes(): Int = R.layout.fragment_player

    private var simpleExoPlayer: SimpleExoPlayer? = null

    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null

    private var playWhenReady = false
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    var animation: ValueAnimator? = null

//    private val matchAdapter by lazy {
//        MatchAdapter { match ->
//            match?.let { it -> viewModel.onMatchClicked(it) }
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeViewModels()

        //Focus for progress
        exo_progress.setOnFocusChangeListener { v, _ ->
            v.nextFocusDownId = R.id.recyclerViewVideos
            v.nextFocusUpId = R.id.menuPlayer
            if (v.isFocused) {
                //Timeout for hiding video player controls
                playerView.controllerShowTimeoutMs = 5000
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }



        var isShow = false
        parentLayout.onChildFocusListener = object :OnChildFocusListener{
            override fun onRequestFocusInDescendants(
                direction: Int,
                previouslyFocusedRect: Rect?
            ): Boolean {
                Timber.e(" jidjofjodifjofidfijdfoi ${direction}")
               return false
            }

            override fun onRequestChildFocus(child: View?, focused: View?) {
                try {
                    Timber.e(" jidjofjodifjofidfijdfoi ${resources.getResourceName(child!!.id)}")
                    if (child.id ==  recyclerViewVideos.id) {
                        if (!isShow){
                            animation?.cancel()
                            animation = getShowAnimation()
                            animation!!.start()
                            isShow = true
                        }

                    }
                    else{
                        if(isShow){

                        animation?.cancel()
                        animation = getHideAnimation()
                        animation!!.start()
                            isShow = false
                        }
                    }
                }catch (e:Exception){

                }
            }

        }
    }

    fun getShowAnimation():ValueAnimator{

        return ValueAnimator.ofInt(recyclerViewVideos.height, requireActivity().windowManager.defaultDisplay.height/2).apply {
            addUpdateListener { animator->
            val lp = recyclerViewVideos.layoutParams
            recyclerViewVideos.layoutParams = lp.apply { height = animator.animatedValue as Int}
        }
            duration = 500}

    }
    fun getHideAnimation():ValueAnimator{
        return ValueAnimator.ofInt(recyclerViewVideos.height,80.dp).apply {
            addUpdateListener { animator->
                val lp = recyclerViewVideos.layoutParams
                recyclerViewVideos.layoutParams = lp.apply { height = animator.animatedValue as Int}
            }
            duration = 500}

    }


    private fun subscribeViewModels() {
        subscribe(viewModel.videoLiveData) { videoUrl ->
            Timber.e("jodifjoifjdoif $videoUrl")
            initializePlayer(videoUrl)
            subscribe(viewModel.currentEpisode){
                Timber.e("jodifjoifjdoif $it")
                if (it.start >0){
                    simpleExoPlayer?.seekTo((it.half-1).toInt(),it.start*1000)
                }

                simpleExoPlayer?.playWhenReady = true
            }

        }

        subscribe(viewModel.sourceLiveData) { source ->
            playlistsContainer.removeAllViews()
            source.toList().forEach { pair ->
                val title = TextView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    setTextColor(Color.WHITE)
                    text = pair.first
                }
                context?.let {
                    val recycler = RecyclerView(it).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        adapter = PlaylistAdapter(){
                            viewModel.play(it)
                        }.apply {
                            submitList(pair.second.sortedBy { it.start })
                        }
                        isFocusable = true



                    }

                    playlistsContainer.addView(title)
                    playlistsContainer.addView(recycler)
                }
            }
        }

        subscribe(viewModel.matchInfoLiveData) { matchInfo ->
            smallGameTitle.text = matchInfo.info
            bigGameTitle.text = "${matchInfo.team1.name} : ${matchInfo.team2.name}"
        }
    }

    private fun showContent(show: Boolean) {
        smallGameTitle.isVisible = show
        bigGameTitle.isVisible = show
        recyclerViewVideos.isVisible = show
    }

    private fun showControls(show: Boolean) {
        exo_progress.isVisible = show
        exo_duration.isVisible = show
        exo_position.isVisible = show
        exo_play.isVisible = show
        exo_pause.isVisible = show
        menuPlayer.isVisible = show
    }

    private fun initializePlayer(list: List<String>) {
        if (simpleExoPlayer != null) {
            simpleExoPlayer?.release()
            simpleExoPlayer = null
        }

        simpleExoPlayer = SimpleExoPlayer.Builder(requireContext()).build()

        simpleExoPlayer?.setMediaItems(list.map { MediaItem.fromUri(it) })

        //recyclerViewVideos.scrollToPosition(0)

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
//        Timber.e("jodifjoifjdoif $videoUrl, $start")
//        simpleExoPlayer?.seekTo(start)
//        simpleExoPlayer?.addListener(object : Player.EventListener{
//            override fun onPlaybackStateChanged(state: Int) {
//                if (simpleExoPlayer?.currentPosition!! >end){
//                    simpleExoPlayer?.stop()
//                }
//                super.onPlaybackStateChanged(state)
//            }
//        })
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

    override fun getParameters(): ParametersDefinition = {
        parametersOf(PlayerFragmentArgs.fromBundle(requireArguments()))
    }
}