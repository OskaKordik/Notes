package com.natife.streaming.ui.search.type

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.preferenses.SearchPrefs
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.SearchTypeUseCase

abstract class TypeDialogViewModel: BaseViewModel() {
    abstract fun select(position: Int)

    abstract val list: LiveData<List<String>>
}

class TypeDialogViewModelImpl(private val typeUseCase: SearchTypeUseCase,private val searchPrefs: SearchPrefs,private val router: Router): TypeDialogViewModel() {
    override fun select(position: Int) {
        searchPrefs.saveType(position)
        router.navigateUp()
    }

    override val list = MutableLiveData<List<String>>()

    init {
        launch {
            list.value = typeUseCase.execute()
        }
    }
}