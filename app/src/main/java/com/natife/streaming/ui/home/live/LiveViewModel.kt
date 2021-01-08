package com.natife.streaming.ui.home.live

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.usecase.GetLiveUseCase
import com.natife.streaming.usecase.SaveLiveUseCase

abstract class LiveViewModel : BaseViewModel() {
    abstract val list: LiveData<List<String>>
}

class LiveViewModelImpl(
    private val getLiveUseCase: GetLiveUseCase,
    private val saveLiveUseCase: SaveLiveUseCase): LiveViewModel() {
    override val list = MutableLiveData<List<String>>()

    init {
        launch {
            val live = getLiveUseCase.execute()
            list.value = live
        }
    }

}