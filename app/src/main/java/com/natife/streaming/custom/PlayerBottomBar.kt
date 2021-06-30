package com.natife.streaming.custom


import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setMargins
import com.google.android.exoplayer2.SimpleExoPlayer
import com.natife.streaming.R
import com.natife.streaming.data.matchprofile.Episode
import com.natife.streaming.data.player.PlayerBottomBarSetup
import com.natife.streaming.databinding.ViewPlayerBottomBarBinding
import com.natife.streaming.ext.dp
import com.natife.streaming.ext.toDate
import com.natife.streaming.ext.toDisplay3
import com.natife.streaming.ui.player.PlayerViewModel
import kotlinx.android.synthetic.main.view_player_bottom_bar.view.*
import timber.log.Timber


class PlayerBottomBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var _binding: ViewPlayerBottomBarBinding? = null
    private val binding get() = _binding!!
    private val sliderIds = mutableMapOf<Int, Episode>()


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
        sliders_plase.removeAllViews()
        video.playlist.forEachIndexed { index, episode ->
            sliderIds[index] = episode.copy(
                end = if (episode.half != -1) episode.end * 1000 else episode.end,
                start = if (episode.half != -1) episode.start * 1000 else episode.start
            )
            val lp = LinearLayout.LayoutParams(
                w / video.playlist.size,
                4.dp
            ) //ViewGroup.LayoutParams.WRAP_CONTENT
            lp.setMargins(0)
            lp.gravity = Gravity.CENTER

            sliders_plase.addView(
                initSliderBars(
                    index,
                    episode,
                    w / video.playlist.size,
                    video.playlist.size,
                    simpleExoPlayer,
                    viewModel
                ), lp
            )
        }
    }

    fun updatePosition(half: Int, i: Long?) {
        Timber.tag("TAG").d("${(half)} ---${(i)} ---${(i)?.toDate()?.toDisplay3("ru")}")
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initSliderBars(
        index: Int,
        episode: Episode,
        w: Int,
        size: Int,
        simpleExoPlayer: SimpleExoPlayer?,
        viewModel: PlayerViewModel
    ): SeekBar {
        return SeekBar(context).apply {
            setPadding((w * 0.05).toInt().dp, 0, (w * 0.05).toInt().dp, 0)
            contentDescription = ""
            id = index
            thumb = resources.getDrawable(R.drawable.player_seekbar_thumb_new, null)
            progressDrawable = resources.getDrawable(R.drawable.player_progress, null)
            progress = 0
            max = (episode.end - episode.start).toInt()
//            nextFocusLeftId = when (index) {
//                size - 1 -> -1
//                else -> index + 1
//            }
//            nextFocusRightId = when (index) {
//                size - 1 -> -1
//                else -> index - 1
//            }


            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
//                         if (episode.half == -1) {
////                             Timber.tag("TAG").d("${(start + progress)} ---${((start + progress)).toDate().toDisplay3("ru")}")
//                             simpleExoPlayer?.seekTo(((episode.start + progress)))
//                         }
//                        else {
//                             Timber.tag("TAG").d("${(start + progress)*1000} ---${((start + progress)*1000).toDate().toDisplay3("ru")}")
                        simpleExoPlayer?.seekTo(episode.half, (episode.start + progress))
                        viewModel.currentWindow = episode.half
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
//                    simpleExoPlayer?.pause()
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                    simpleExoPlayer?.play()
                }
            })


//            addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
//                override fun onStartTrackingTouch(slider: Slider) {
////                    binding.previewFrameLayout.visibility = View.VISIBLE
////                    binding.imageView23.setImageResource(R.drawable.ic_basketball_new)
//                }
//
//                override fun onStopTrackingTouch(slider: Slider) {
////                    binding.previewFrameLayout.visibility = View.GONE
////                    binding.imageView23.setImageResource(R.drawable.ic_footbool_new)
//                }
//            })

//            this.addOnChangeListener { rangeSlider, value, fromUser ->
//                if (fromUser) {
//                    rangeSlider.rootView.preloadImage.bindFlagImage(25)
//                    rangeSlider.rootView.preloadImage.bindPreloadFromUrl(url,value.toLong())
//                    invalidate()

//                    Glide.with(context).load(url).apply(RequestOptions().frame(1000*(value.toLong())))
//                        .into(rangeSlider.rootView.preloadImage)

//                    Glide.with(context).asBitmap().load(url.toUri().pathSegments.last()).apply(RequestOptions().frame(1000*(value.toLong())))
//                        .into(rangeSlider.rootView.preloadImage)
//                    invalidate()

//                    var microSecond = (value.toLong())* 1000
//                    var options =  RequestOptions().frame(microSecond).override(35.dp, 35.dp)
//                    options.isMemoryCacheable
//                    var thumb=Glide.with(context).load(url).apply(RequestOptions().frame(microSecond).override(35.dp))
//                    Glide.with(context).load(url)
//                        .thumbnail(thumb)
//                        .apply(options)
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .into(rangeSlider.rootView.preloadImage)

//
//                }
//            }
        }
    }
}