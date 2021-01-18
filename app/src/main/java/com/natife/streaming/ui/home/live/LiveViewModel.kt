package com.natife.streaming.ui.home.live

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.LiveType
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.GetLiveUseCase
import com.natife.streaming.usecase.SaveLiveUseCase

abstract class LiveViewModel : BaseViewModel() {
    abstract val list: LiveData<List<String>>
    abstract fun saveType(selected: Int)
}

class LiveViewModelImpl(
    private val getLiveUseCase: GetLiveUseCase,
    private val saveLiveUseCase: SaveLiveUseCase,
    private val router: Router
): LiveViewModel() {
    override val list = MutableLiveData<List<String>>()
    override fun saveType(selected: Int) {

        saveLiveUseCase.execute(LiveType.values()[selected])
    }

    init {
        launch {
            val live = getLiveUseCase.execute()
            list.value = live
        }
    }

}