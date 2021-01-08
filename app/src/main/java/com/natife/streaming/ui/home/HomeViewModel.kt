package com.natife.streaming.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.match.Match
import com.natife.streaming.usecase.MatchUseCase
import timber.log.Timber

abstract class HomeViewModel : BaseViewModel() {
    abstract fun showScoreDialog()
    abstract fun showSportDialog()
    abstract fun showTourneyDialog(items: List<Match>)
    abstract fun showLiveDialog()

    abstract val list: LiveData<PagingData<Match>>
}

class HomeViewModelImpl(matchUseCase: MatchUseCase) : HomeViewModel() {
    override fun showScoreDialog() {
        router?.navigate(R.id.action_homeFragment_to_scoreDialog)
    }

    override fun showSportDialog() {
        router?.navigate(R.id.action_homeFragment_to_sportDialog)
    }

    override fun showTourneyDialog(items: List<Match>) {

        router?.navigate(HomeFragmentDirections.actionHomeFragmentToTournamentDialog(items.map { it.tournament }
            .toSet().toTypedArray()))
    }

    override fun showLiveDialog() {
        router?.navigate(R.id.action_homeFragment_to_liveDialog)
    }

    override val list = MutableLiveData<PagingData<Match>>()

    init {
        launch {
            matchUseCase.execute(date = "mkfmkfmk")
            collectCatching(matchUseCase.executeFlow().cachedIn(viewModelScope)) {
                Timber.e("collect $it")
                it.map { it.tournament.name }
                list.postValue(it)
            }
        }


        Timber.e("after collect ")
    }


}