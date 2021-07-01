package com.natife.streaming.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.match.Match
import com.natife.streaming.data.search.SearchResult
import com.natife.streaming.datasource.MatchParams
import com.natife.streaming.db.LocalSqlDataSourse
import com.natife.streaming.db.entity.PreferencesTournament
import com.natife.streaming.ext.Event
import com.natife.streaming.ext.toRequest
import com.natife.streaming.preferenses.SettingsPrefs
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.FavoritesUseCase
import com.natife.streaming.usecase.MatchUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

abstract class HomeViewModel : BaseViewModel() {
    abstract fun loadList()
    abstract fun toMatchProfile(match: Match)

    abstract val listTournament: LiveData<List<TournamentItem>>
    abstract val isLoadData: LiveData<Event<Boolean>>
    abstract var load: Boolean
}

class HomeViewModelImpl(
    private val matchUseCase: MatchUseCase,
    private val router: Router,
    private val settingsPrefs: SettingsPrefs,
    private val localSqlDataSourse: LocalSqlDataSourse,
    private val favoritesUseCase: FavoritesUseCase,
) : HomeViewModel() {
    override val listTournament = MutableLiveData<List<TournamentItem>>()
    override val isLoadData = MutableLiveData(Event(false))
    override var load = true
    private val list = MutableLiveData<List<Match>>()
    private val showScore = MutableLiveData<Boolean>()
    private val prefInTournament = MutableLiveData<List<PreferencesTournament>>()
    private val date = MutableLiveData<Date>()
    private val mutex = Mutex()
    private var isLoading = AtomicBoolean(false)
    private var job: Job? = null

    init{
        load = true
    }

    override fun loadList() {
        job?.cancel()
        job = launch {
            isLoading.set(true)
            load = true
            matchUseCase.load()
            isLoading.set(false)
            load = false
        }
    }

    override fun toMatchProfile(match: Match) {
        when {
            (match.live && match.subscribed) -> {
                router.navigate(
                    HomeFragmentDirections.actionHomeFragmentToLiveFragment(
                        sportId = match.sportId,
                        matchId = match.id,
                        title = "${match.team1} - ${match.team2}"
                    )
                )
            }
            (match.hasVideo && match.subscribed) -> {
                router.navigate(
                    HomeFragmentDirections.actionHomeFragmentToPopupVideoFragment(
                        sportId = match.sportId,
                        matchId = match.id
                    )
                )
            }
            (match.live && !match.subscribed) -> {
                router.navigate(
                    HomeFragmentDirections.actionHomeFragmentToBillingFragment(
                        sportId = match.sportId,
                        matchId = match.id,
                        tournamentId = match.tournament.id,
                        tournamentTitle = match.tournament.name,
                        live = match.live,
                        team1 = match.team1.name,
                        team2 = match.team2.name
                    )
                )
            }
            (match.hasVideo && !match.subscribed) -> {
                router.navigate(
                    HomeFragmentDirections.actionHomeFragmentToBillingFragment(
                        sportId = match.sportId,
                        matchId = match.id,
                        tournamentId = match.tournament.id,
                        tournamentTitle = match.tournament.name,
                        live = match.live,
                        team1 = match.team1.name,
                        team2 = match.team2.name
                    )
                )
            }
            else -> return
        }
    }

    private fun setListTournament(listMatch: List<Match>) {
        launch {
            val res = hashMapOf<Int, List<Match>>()
            listMatch.forEach { it ->
                val isPreferred =
                    localSqlDataSourse.getPreferencesTournamentBySportIDPrefID(
                        it.sportId,
                        it.tournament.id
                    )?.isPreferred
                if (isPreferred == true) res[it.tournament.id] =
                    res[it.tournament.id]?.plus(it) ?: listOf(it)
            }
            listTournament.value = res.mapValues {
                TournamentItem.RegularTournamentItem(
                    tournamentId = it.key,
                    programName = it.value.first().tournament.name,
                    match = it.value
                )
            }.values.toList()
            isLoadData.value = Event(true)
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
        //tracking global settings
        launchCatching {
            withContext(Dispatchers.IO) {
                collect(localSqlDataSourse.getGlobalSettingsFlow()) { globalSetings ->
                    showScore.value = globalSetings?.showScore
                    list.value?.let { matchList ->
                        matchList.map { match ->
                            match.copy(isShowScore = globalSetings?.showScore ?: false)
                        }.let {
                            list.value = it
                            setListTournament(it)
                        }
                    }
                }
            }
        }
        //tracking preferences
        launchCatching {
            withContext(Dispatchers.IO) {
                collect(localSqlDataSourse.getPreferencesTournamentFlow()) {
//                    prefInTournament.value = listPrefTournament
                    list.value
                        ?.let { setListTournament(it) }
                }
            }
        }

        launchCatching {
            val favorites = favoritesUseCase.execute()
            val tournamentFavorites: List<SearchResult> =
                favorites.filter { it.type == SearchResult.Type.TOURNAMENT }
            val teamFavorites = favorites.filter { it.type == SearchResult.Type.TEAM }
            collect(matchUseCase.list) { matchList ->
                matchList.map { match ->
                    match.copy(
                        isShowScore = showScore.value ?: false,
                        isFavoriteTournament = tournamentFavorites.firstOrNull {
                            it.id == match.tournament.id
                        }?.isFavorite ?: false,
                        isFavoriteTeam1 = teamFavorites.firstOrNull {
                            it.id == match.team1.id
                        }?.isFavorite ?: false,
                        isFavoriteTeam2 = teamFavorites.firstOrNull {
                            it.id == match.team2.id
                        }?.isFavorite ?: false,
                    )
                }.let {
                    list.value = it
                    setListTournament(it)
                }
            }
        }
    }
}