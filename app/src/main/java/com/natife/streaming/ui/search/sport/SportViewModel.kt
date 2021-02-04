package com.natife.streaming.ui.search.sport

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.Sport
import com.natife.streaming.preferenses.SearchPrefs
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.GetSportUseCase

abstract class SportViewModel: BaseViewModel() {
    abstract fun select(position: Int)

    abstract val list: LiveData<List<Sport>>
}
class SportViewModelImpl(private val sportUseCase: GetSportUseCase,private val searchPrefs: SearchPrefs,private val router: Router): SportViewModel() {
    override fun select(position: Int) {
        if (position>0){
            list.value?.get(position-1)?.id?.let { searchPrefs.saveSport(it) }
        }else{
            searchPrefs.saveSport(-1)
        }

        router.navigateUp()
    }

    override val list = MutableLiveData<List<Sport>>()

    init {
        launch {
            list.value = sportUseCase.execute()

        }
    }
}