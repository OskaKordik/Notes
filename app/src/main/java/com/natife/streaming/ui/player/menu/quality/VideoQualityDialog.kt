package com.natife.streaming.ui.player.menu.quality

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.natife.streaming.R
import com.natife.streaming.base.BaseDialog
import com.natife.streaming.ext.subscribe
import kotlinx.android.synthetic.main.dialog_video_quality.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class VideoQualityDialog : BaseDialog<VideoQualityViewModel>() {
    override fun getLayoutRes(): Int = R.layout.dialog_video_quality
    override fun getParameters(): ParametersDefinition = {
        parametersOf(VideoQualityDialogArgs.fromBundle(requireArguments()))
    }

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
        setupUI()
    }

    private fun setupUI() {
        subscribe(viewModel.videoQualityListLiveData) {
            videoQualityAdapter.submitList(it)
        }

        rvQuality.adapter = videoQualityAdapter
    }

    private fun onQualityClicked(quality: String) {
        setFragmentResult(KEY_QUALITY, bundleOf(KEY_QUALITY to quality))
        dismiss()
    }

    companion object {
        const val KEY_QUALITY = "key_quality"
    }
}

