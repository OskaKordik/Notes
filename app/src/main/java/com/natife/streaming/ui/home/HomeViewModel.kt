package com.natife.streaming.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

abstract class HomeViewModel : BaseViewModel() {
    abstract fun showScoreDialog()
    abstract fun showSportDialog()
    abstract fun showTourneyDialog(items: List<Match>)
    abstract fun showLiveDialog()
    abstract fun subOnlyChange()
    abstract fun loadList()

    //    abstract fun toCalendar()
//    abstract fun nextDay()
//    abstract fun previousDay()
    abstract fun toMatchProfile(match: Match)

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
    private var dataSource: List<Match> = listOf()

    private var process: Job? = null
    var live = LiveType.ALL

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

    private val mutex = Mutex()
    private var isLoading = AtomicBoolean(false)

    override fun loadList() {
        launch {
            mutex.withLock {
                if (isLoading.get()) {
                    return@launch
                }
            }

            isLoading.set(true)

           matchUseCase.load()

            isLoading.set(false)

        }
    }

    private fun filterLive(data: List<Match>): List<Match> {

        return when (live) {
            LiveType.LIVE -> data.filter { it.live }
            LiveType.SOON -> data.filter { it.date.fromResponse().time - Date().time in 0..(1000 * 60 * 60) }
            LiveType.FINISHED -> data.filter { Date().time - it.date.fromResponse().time > 0 || it.storage || it.hasVideo }
            else -> data
        }
    }

//    override fun toCalendar() {
//        router.navigate(R.id.action_homeFragment_to_calendarFragment)
//    }
//
//    override fun nextDay() {
//        val calendar = Calendar.getInstance()
//        calendar.time = settingsPrefs.getDate()?.toDate() ?: Date()
//        calendar.add(Calendar.DAY_OF_YEAR, 1)
//        settingsPrefs.saveDate(calendar.time.time)
//    }
//
//    override fun previousDay() {
//        val calendar = Calendar.getInstance()
//        calendar.time = settingsPrefs.getDate()?.toDate() ?: Date()
//        calendar.add(Calendar.DAY_OF_YEAR, -1)
//        settingsPrefs.saveDate(calendar.time.time)
//    }

    override fun toMatchProfile(match: Match) {
        if (match.hasVideo) {
            router.navigate(
                HomeFragmentDirections.actionHomeFragmentToMatchProfileFragment(
                    sportId = match.sportId,
                    matchId = match.id
                )
            )
        }

    }


    private var params = MatchParams(
        date = Date().toRequest(),
        pageSize = 60,
        sportId = null,
        subOnly = false,
        additionalId = null
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

        launchCatching {
            collect(matchUseCase.list){
                dataSource = it
                val filtered = filterLive(dataSource)
                Timber.e("HKUHIUH ${list.value}")
                Timber.e("HKUHIUH1 ${filtered}")
                list.value = filtered
            }
        }
        val sport = settingsPrefs.getSport()
        val tournament = settingsPrefs.getTournament()
        live = settingsPrefs.getLive() ?: LiveType.ALL
        val subOnly = settingsPrefs.getSubOnly()
        val date = settingsPrefs.getDate()

        if (date == null) {
            this.date.value = Date()
        } else {
            this.date.value = Date(date)
        }

        this.subOnly.value = subOnly

        params = params.copy(
            sportId = sport,
            additionalId = tournament,
            subOnly = subOnly,
            date = (this.date.value ?: Date()).toRequest()
        )
        launchCatching {
            withContext(Dispatchers.IO) {
                collect(settingsPrefs.getDateFlow()) {
                    it?.let {
                        params = params.copy(
                            date = Date(it).toRequest()
                        )
                        this@HomeViewModelImpl.date.value = Date(it)
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
                collect(settingsPrefs.getLiveFlow()) {
                    live = it ?: LiveType.ALL
                    list.value?.let {
                        list.value = filterLive(dataSource)
                    }
                }
            }
        }
        launchCatching {
            withContext(Dispatchers.IO) {
                collect(settingsPrefs.getTournamentFlow()) {
                    params = params.copy(
                        additionalId = it
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