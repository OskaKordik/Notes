package com.natife.streaming.custom


import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SeekBar
import com.google.android.exoplayer2.SimpleExoPlayer
import com.natife.streaming.R
import com.natife.streaming.data.matchprofile.Episode
import com.natife.streaming.data.player.PlayerBottomBarSetup
import com.natife.streaming.databinding.ViewPlayerBottomBarBinding
import com.natife.streaming.ext.dp
import com.natife.streaming.ui.player.PlayerViewModel
import kotlinx.android.synthetic.main.view_player_bottom_bar.view.*
import kotlin.math.ceil


class PlayerBottomBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var _binding: ViewPlayerBottomBarBinding? = null
    private val binding get() = _binding!!
    private val sliderIds = mutableMapOf<Int, Episode>()
    private lateinit var viewModel: PlayerViewModel


    init {
        inflate(context, R.layout.view_player_bottom_bar, this)
        _binding = ViewPlayerBottomBarBinding.inflate(LayoutInflater.from(context))
    }

    fun initVideoUrl(
        video: PlayerBottomBarSetup,
        w: Int,
        simpleExoPlayer: SimpleExoPlayer?,
        viewModel: PlayerViewModel,
    ) {
        sliders_place.removeAllViews()
        //paddings
        val padding = when (video.playlist.size) {
            in 0..3 -> 25
            in 4..10 -> 7
            in 10..20 -> 3
            else -> 1
        }
        //Width SeekBar
        val widthSeekBar = when (video.playlist.size) {
            in 0..20 -> {
                ceil(w.toDouble() / video.playlist.size.toDouble()).toInt()
            }
            in 20..40 -> {
                ceil((w - (video.playlist.size)).toDouble() / video.playlist.size.toDouble()).toInt()
            }
            else -> {
                ceil((w - (video.playlist.size + 20)).toDouble() / video.playlist.size.toDouble()).toInt()
            }
        }

        video.playlist.forEachIndexed { index, episode ->
            sliderIds[index] = episode.copy(
                endMs = episode.endMs,
                startMs = episode.startMs
            )
            val lp = when (index) {
                0 -> {
                    LinearLayout.LayoutParams(
                        widthSeekBar,
                        4.dp
                    ).apply {
                        setMargins(padding, 0, padding, 0)
                        gravity = Gravity.CENTER
                    }
                }
                else -> {
                    LinearLayout.LayoutParams(
                        widthSeekBar,
                        4.dp
                    ).apply {
                        setMargins(0, 0, padding, 0)
                        gravity = Gravity.CENTER
                    }
                }
            }
            sliders_place.addView(
                initSliderBars(
                    index,
                    episode,
                    simpleExoPlayer,
                    viewModel,
                    video.playlist.size
                ), lp
            )
        }
    }

    fun updatePosition(half: Int, time: Long?) {
//        Timber.tag("TAG").d("${(half)} ---${(time)} ---${(time)?.toDate()?.toDisplay3("ru")}")
        val sliderIdForUpdate = sliderIds.filterValues {
            it.half == half && time in it.startMs..it.endMs
        }.keys.firstOrNull()
        sliderIdForUpdate?.let {
            findViewById<SeekBar>(sliderIdForUpdate)?.let { curentSeekBar ->
                curentSeekBar.progress =
                    sliderIds[sliderIdForUpdate]?.startMs?.toInt()?.let { it1 ->
                        time?.toInt()?.minus(
                            it1
                        )
                    } ?: 0
                viewModel.setCurrentSeekBarId(curentSeekBar.id)
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initSliderBars(
        index: Int,
        episode: Episode,
        simpleExoPlayer: SimpleExoPlayer?,
        viewModel: PlayerViewModel,
        size: Int
    ): SeekBar {
        this.viewModel = viewModel
        if (index == 0) viewModel.play(episode)
        return SeekBar(context).apply {
            setPadding(0, 0, 0, 0)
            contentDescription = ""
            id = index
            viewModel.setCurrentSeekBarId(index)
            thumb = if (size > 10) resources.getDrawable(
                R.drawable.player_seekbar_thumb_small_new,
                null
            ) else resources.getDrawable(R.drawable.player_seekbar_thumb_new, null)
            progressDrawable = resources.getDrawable(R.drawable.player_progress, null)
            progress = 0
            max = (episode.endMs - episode.startMs).toInt()

            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progressMs: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
//                        Timber.tag("TAG")
//                            .d("${(episode.half)} -----------${episode.startMs} + $progressMs")
                        simpleExoPlayer?.seekTo(episode.half, (episode.startMs + progressMs))
                        viewModel.currentWindow = episode.half
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
//                    if (simpleExoPlayer?.isPlaying != false) simpleExoPlayer?.pause()
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                    if (simpleExoPlayer?.isPlaying != true) simpleExoPlayer?.play()
                }
            })
        }
    }

    fun nextEpisode(half: Int, time: Long?) {
        if (time == null) return
        val a = sliderIds.mapValues {
            it.value.half
        }.values.toSet()


        val nextEpisodeForUpdate: Episode? = a.toList().mapNotNull { h ->
            when {
                h == half -> {
                    sliderIds
                        .filterValues { episode ->
                            h == episode.half && episode.startMs > time
                        }
                        .values.firstOrNull()
                }
                h > half -> {
                    sliderIds
                        .filterValues { episode ->
                            h == episode.half && episode.startMs > 0
                        }
                        .values
                        .sortedWith(compareBy({ it.half }, { it.startMs }))
                        .firstOrNull()
                }
                else -> null
            }
        }.firstOrNull()
        if (nextEpisodeForUpdate != null) {
            viewModel.play(nextEpisodeForUpdate)
        }
    }
}