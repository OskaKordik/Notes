package com.natife.streaming.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.match.Match
import com.natife.streaming.datasource.MatchParams
import com.natife.streaming.ext.toRequest
import com.natife.streaming.preferenses.SettingsPrefs
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.MatchUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*

abstract class HomeViewModel : BaseViewModel() {
    abstract fun showScoreDialog()
    abstract fun showSportDialog()
    abstract fun showTourneyDialog(items: List<Match>)
    abstract fun showLiveDialog()
    abstract fun loadList()
    abstract val list: LiveData<List<Match>>
}

class HomeViewModelImpl(private val matchUseCase: MatchUseCase,private val router: Router, private val settingsPrefs: SettingsPrefs) : HomeViewModel() {
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



    override val list = MutableLiveData<List<Match>>()
    override fun loadList() {
        launch {
            val data = matchUseCase.load()
            list.value = data
        }
    }


    private var params = MatchParams(
        date = Date().toRequest(),
        pageSize = 60,
        sportId = null,
        subOnly = false,
        tournamentId = null
    )
    init {

        launch{
            /*.combine(settingsPrefs.getTournamentFlow()){sport,tournament->

            }*/
            withContext(Dispatchers.IO){
                collect(settingsPrefs.getSportFlow()){
                    params= params.copy(
                        sportId = it
                    )
                    Timber.e("kkdjfdffd $params")
                    launch {
                        withContext(Dispatchers.IO){
                            matchUseCase.prepare(params)
                            loadList()
                        }

                    }
                }
            }

        }
        launch {
            matchUseCase.prepare(params)
            withContext(Dispatchers.IO){

                loadList()
                /*collectCatching(matchUseCase.executeFlow().cachedIn(viewModelScope)) {
                    Timber.e("collect ${it}")

                    list.postValue(it)
                }*/
            }

        }



        Timber.e("after collect ")
    }


}