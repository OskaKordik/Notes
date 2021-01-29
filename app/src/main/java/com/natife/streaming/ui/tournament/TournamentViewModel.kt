package com.natife.streaming.ui.tournament

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.Tournament
import com.natife.streaming.data.match.Match
import com.natife.streaming.datasource.MatchParams
import com.natife.streaming.ext.toRequest
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.MatchUseCase
import com.natife.streaming.usecase.TournamentUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import java.util.*

class TournamentViewModel(
    private val sportId:Int,
    private val tournamentId:Int,
    private val router: Router,
    private val tournamentUseCase: TournamentUseCase,
    private val matchUseCase: MatchUseCase
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
        tournamentId = null
    )
    fun loadList() {
        process?.cancel()
        process = launch {
            val dataSource = matchUseCase.load(MatchUseCase.Type.TOURNAMENT)

            _list.value = dataSource
        }
    }

    init {
        params = params.copy(tournamentId = tournamentId,sportId = sportId)

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

    fun addToFavorite() {
        router.toast("Добавить в избранное")
    }
}