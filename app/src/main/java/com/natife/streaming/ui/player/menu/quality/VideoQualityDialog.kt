package com.natife.streaming.ui.player.menu.quality

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.natife.streaming.R
import com.natife.streaming.base.BaseDialog
import com.natife.streaming.base.EmptyViewModel
import com.natife.streaming.ui.player.PlayerFragment.Companion.VIDEO_QUALITY
import kotlinx.android.synthetic.main.dialog_video_quality.*

class VideoQualityDialog : BaseDialog<EmptyViewModel>() {
    override fun getLayoutRes(): Int = R.layout.dialog_video_quality
//    override fun getParameters(): ParametersDefinition = {
//        parametersOf(VideoQualityDialogArgs.fromBundle(requireArguments()))
//    }

    private val videoQualityAdapter by lazy {
        VideoQualityAdapter {
            onQualityClicked(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_App_Dialog_FullScreen)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val params: VideoQualityParams = arguments?.let {
            it.get(VIDEO_QUALITY)
        } as VideoQualityParams
        rvQuality.adapter = videoQualityAdapter
        videoQualityAdapter.submitList(params.videoQualityList)
    }


    private fun onQualityClicked(quality: String) {
        setFragmentResult(KEY_QUALITY, bundleOf(KEY_QUALITY to quality))
        dismiss()
    }

    companion object {
        const val KEY_QUALITY = "key_quality"
    }
}

