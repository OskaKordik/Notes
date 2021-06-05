package com.natife.streaming.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.match.Match
import com.natife.streaming.datasource.MatchParams
import com.natife.streaming.db.LocalSqlDataSourse
import com.natife.streaming.ext.toRequest
import com.natife.streaming.preferenses.SettingsPrefs
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.MatchUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

abstract class HomeViewModel : BaseViewModel() {
    abstract fun loadList()
    abstract fun toMatchProfile(match: Match)

    abstract val listTournament: LiveData<List<TournamentItem>>
}

class HomeViewModelImpl(
    private val matchUseCase: MatchUseCase,
    private val router: Router,
    private val settingsPrefs: SettingsPrefs,
    private val localSqlDataSourse: LocalSqlDataSourse
) : HomeViewModel() {
    override val listTournament = MutableLiveData<List<TournamentItem>>()
    private val list = MutableLiveData<List<Match>>()
    private val showScore = MutableLiveData<Boolean>()
    private val date = MutableLiveData<Date>()
    private val mutex = Mutex()
    private var isLoading = AtomicBoolean(false)

    override fun loadList() {
        launch {
            mutex.withLock {
                if (isLoading.get()) return@launch
            }
            isLoading.set(true)
            matchUseCase.load()
            isLoading.set(false)
        }
    }

//    private fun filterLive(data: List<Match>): List<Match> {
//
//        return when (live) {
//            LiveType.LIVE -> data.filter { it.live }
//            LiveType.SOON -> data.filter { it.date.fromResponse().time - Date().time in 0..(1000 * 60 * 60) }
//            LiveType.FINISHED -> data.filter { Date().time - it.date.fromResponse().time > 0 || it.storage || it.hasVideo }
//            else -> data
//        }
//    }

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

    private fun setListTournament(listMatch: List<Match>) {
        if (listMatch.isEmpty()) return
        var tempId: Int? = null
        var tempName: String = ""
        var tempMatchList = mutableListOf<Match>()
        val resultTournamentList = mutableListOf<TournamentItem>()
        listMatch.forEach {
            //TODO Проверка с предпочтениями
            when {
                tempId == null -> {
                    if (listMatch.size > 1) {
                        tempId = it.tournament.id
                        tempName = it.tournament.name
                        tempMatchList.add(it)
                    } else {
                        resultTournamentList.add(
                            TournamentItem.RegularTournamentItem(
                                tournamentId = listMatch.first().tournament.id,
                                programName = listMatch.first().tournament.name,
                                match = listOf(listMatch.first())
                            )
                        )
                    }
                }
                tempId == it.tournament.id -> {
                    tempMatchList.add(it)
                }
                tempId != it.tournament.id -> {
                    resultTournamentList.add(
                        TournamentItem.RegularTournamentItem(
                            tournamentId = tempId,
                            programName = tempName,
                            match = tempMatchList
                        )
                    )
                    tempId = it.tournament.id
                    tempName = it.tournament.name
                    tempMatchList = mutableListOf<Match>()
                    tempMatchList.add(it)
                }
            }
        }
        Timber.tag("TAG").d(Gson().toJson(resultTournamentList))
        listTournament.value = resultTournamentList
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
        val date = settingsPrefs.getDate()
        if (date == null) {
            this.date.value = Date()
        } else {
            this.date.value = Date(date)
        }
        params = params.copy(
            sportId = null, //sport,
            additionalId = null, //tournament,
            subOnly = false, //показать только купленные матчи или же все
            date = (this.date.value ?: Date()).toRequest()
        )
        launch {
            matchUseCase.prepare(params)
        }
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
                collect(localSqlDataSourse.getGlobalSettingsFlow()) { globalSetings ->
                    showScore.value = globalSetings?.showScore
                    list.value?.let { matchList ->
                        matchList.map { match ->
                            match.copy(isShoveScore = globalSetings?.showScore ?: false)
                        }.let {
                            list.value = it
                            setListTournament(it)
                        }
                    }
                }
            }
        }
        launchCatching {
            collect(matchUseCase.list) { matchList ->
                matchList.map { match ->
                    match.copy(isShoveScore = showScore.value ?: false)
                }.let {
                    list.value = it
                    setListTournament(it)
                }
            }
        }
    }
}