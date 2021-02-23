package com.natife.streaming.ui.tournament

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.Tournament
import com.natife.streaming.data.match.Match
import com.natife.streaming.data.search.SearchResult
import com.natife.streaming.datasource.MatchParams
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.MatchUseCase
import com.natife.streaming.usecase.SaveDeleteFavoriteUseCase
import com.natife.streaming.usecase.TournamentUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

class TournamentViewModel(
    private val sportId:Int,
    private val tournamentId:Int,
    private val router: Router,
    private val tournamentUseCase: TournamentUseCase,
    private val matchUseCase: MatchUseCase,
    private val saveDeleteFavoriteUseCase: SaveDeleteFavoriteUseCase
) : BaseViewModel() {

    private var process: Job? = null
    private val _tournament = MutableLiveData<Tournament>()
    private val _list = MutableLiveData<List<Match>>()
    val list: LiveData<List<Match>> = _list
    val tournament: LiveData<Tournament> = _tournament
    private var params = MatchParams(
        date = null,
        pageSize = 60,
        sportId = null,
        subOnly = false,
        additionalId = null
    )

    private val mutex = Mutex()
    private var isLoading = AtomicBoolean(false)
     fun loadList() {
        launch {
            mutex.withLock {
                Timber.e("JHDIDND ${System.currentTimeMillis()} load ${list.value?.size}")
                if (isLoading.get()) {
                    return@launch
                }

            }

            isLoading.set(true)

            val dataSource = matchUseCase.load(MatchUseCase.Type.TOURNAMENT)

            _list.value = dataSource

            Timber.e("JHDIDND 2 ${System.currentTimeMillis()} loaded ${list.value?.size}")
            isLoading.set(false)

        }
    }

    init {
        params = params.copy(additionalId = tournamentId,sportId = sportId)

        launch {
            _tournament.value = tournamentUseCase.execute(sportId,tournamentId)
        }
        launch {
            matchUseCase.prepare(params)
        }
        launch {
            matchUseCase.prepare(params)
            withContext(Dispatchers.IO) {
                loadList()
            }

        }

    }

    fun onScoreClicked() {
        router.toast("Счет")
    }

    fun addToFavorite(tournament: Tournament) {
        launch {
            if (tournament.isFavorite){
                saveDeleteFavoriteUseCase.executeDelete(sportId=sportId,id = tournamentId,type = SearchResult.Type.TOURNAMENT)
            }else{
                saveDeleteFavoriteUseCase.executeSave(sportId=sportId,id = tournamentId,type = SearchResult.Type.TOURNAMENT)
            }
            _tournament.value = tournament.copy(isFavorite = !tournament.isFavorite)
        }


    }
}