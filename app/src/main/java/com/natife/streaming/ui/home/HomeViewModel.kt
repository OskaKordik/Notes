package com.natife.streaming.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.match.Match
import com.natife.streaming.usecase.MatchUseCase
import timber.log.Timber

abstract class HomeViewModel : BaseViewModel() {
    abstract val list: LiveData<PagingData<Match>>
}

class HomeViewModelImpl(matchUseCase: MatchUseCase) : HomeViewModel() {
    override val list = MutableLiveData<PagingData<Match>>()
    init {
        launch {
            matchUseCase.execute(date = "mkfmkfmk")
            collectCatching(matchUseCase.executeFlow().cachedIn(viewModelScope)){
                Timber.e("collect $it")
                list.postValue(it)
            }
        }


        Timber.e("after collect ")
    }


}