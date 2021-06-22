package com.natife.streaming.ui.player.menu.quality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel

abstract class VideoQualityViewModel : BaseViewModel() {
    abstract val videoQualityListLiveData: LiveData<List<String>>
}

class VideoQualityViewModelImpl(
    private val videoQualityParams: VideoQualityParams
) : VideoQualityViewModel() {

    override val videoQualityListLiveData = MutableLiveData<List<String>>()

    init {
        videoQualityListLiveData.value = videoQualityParams.videoQualityList
    }
}
