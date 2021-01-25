package com.natife.streaming.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.LiveType
import com.natife.streaming.data.match.Match
import com.natife.streaming.datasource.MatchParams
import com.natife.streaming.ext.fromResponse
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
    abstract val list: LiveData<List<Match>>
    abstract val subOnly: LiveData<Boolean>
}

class HomeViewModelImpl(
    private val matchUseCase: MatchUseCase,
    private val router: Router,
    private val settingsPrefs: SettingsPrefs
) : HomeViewModel() {


    override val list = MutableLiveData<List<Match>>()
    override val subOnly = MutableLiveData<Boolean>()

    private var process: Job? = null
    var live = LiveType.NONE

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
            val filtered = filterLive(data)
            list.value = filtered
        }
    }

    private fun filterLive(data: List<Match>): List<Match> {
        return when (live) {
            LiveType.LIVE -> data.filter { it.live }
            LiveType.SOON -> data.filter { Date().time - it.date.fromResponse().time in 0..1000 * 60 * 60 }
            LiveType.FINISHED -> data.filter { Date().time - it.date.fromResponse().time < 0 || it.storage || it.hasVideo }
            else -> data
        }
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
        live = settingsPrefs.getLive() ?: LiveType.NONE
        val subOnly = settingsPrefs.getSubOnly()

        this.subOnly.value = subOnly

        params = params.copy(sportId = sport, tournamentId = tournament, subOnly = subOnly)

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
                collect(settingsPrefs.getLiveFlow()) {
                    list.value?.let {
                        list.value = filterLive(it)
                    }
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



        Timber.e("after collect ")
    }


}