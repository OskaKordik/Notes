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
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*

abstract class HomeViewModel : BaseViewModel() {
    abstract fun showScoreDialog()
    abstract fun showSportDialog()
    abstract fun showTourneyDialog(items: List<Match>)
    abstract fun showLiveDialog()
    abstract fun subOnlyChange()
    abstract fun loadList()
    abstract fun toCalendar()

    abstract val list: LiveData<List<Match>>
    abstract val subOnly: LiveData<Boolean>
    abstract val date: LiveData<Date>
}

class HomeViewModelImpl(
    private val matchUseCase: MatchUseCase,
    private val router: Router,
    private val settingsPrefs: SettingsPrefs
) : HomeViewModel() {


    override val list = MutableLiveData<List<Match>>()
    override val subOnly = MutableLiveData<Boolean>()
    override val date = MutableLiveData<Date>()


    private var process: Job? = null

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

    override fun subOnlyChange() {
        val sub = settingsPrefs.getSubOnly()
        settingsPrefs.saveSubOnly(!sub)
        subOnly.value = !sub
    }

    override fun loadList() {
        process?.cancel()
        process = launch {
            val data = matchUseCase.load()
            list.value = data
        }
    }

    override fun toCalendar() {
        router.navigate(R.id.action_homeFragment_to_calendarFragment)
    }


    private var params = MatchParams(
        date = Date().toRequest(),
        pageSize = 60,
        sportId = null,
        subOnly = false,
        tournamentId = null
    )

    private fun prepareAndLoad() {
        launch {
            withContext(Dispatchers.IO) {
                matchUseCase.prepare(params)
                loadList()
            }

        }
    }

    init {

        val sport = settingsPrefs.getSport()
        val tournament = settingsPrefs.getTournament()
        val live = settingsPrefs.getLive()
        val subOnly = settingsPrefs.getSubOnly()
        val date = settingsPrefs.getDate()

        if (date == null){
            this.date.value = Date()
        }else{
            this.date.value = Date(date)
        }

        this.subOnly.value = subOnly

        params = params.copy(sportId = sport, tournamentId = tournament, subOnly = subOnly, date = (this.date.value ?: Date()).toRequest())
        launchCatching {
            withContext(Dispatchers.IO) {
                collect(settingsPrefs.getDateFlow()) {
                    it?.let {
                        params = params.copy(
                            date =Date(it).toRequest()
                        )

                        prepareAndLoad()
                    }
                }
            }
        }

        launchCatching {
            withContext(Dispatchers.IO) {
                collect(settingsPrefs.getSportFlow()) {
                    params = params.copy(
                        sportId = it
                    )
                    prepareAndLoad()
                }
            }
        }
        launchCatching {
            withContext(Dispatchers.IO) {
                collect(settingsPrefs.getTournamentFlow()) {
                    params = params.copy(
                        tournamentId = it
                    )
                    prepareAndLoad()
                }
            }
        }
        launchCatching {
            withContext(Dispatchers.IO) {
                collect(settingsPrefs.getSubOnlyFlow()) {
                    params = params.copy(
                        subOnly = it
                    )
                    prepareAndLoad()
                }
            }
        }




        launch {
            matchUseCase.prepare(params)
            withContext(Dispatchers.IO) {
                loadList()
            }

        }
    }


}