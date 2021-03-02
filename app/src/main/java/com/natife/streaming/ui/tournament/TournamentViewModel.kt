package com.natife.streaming.ui.tournament

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.Tournament
import com.natife.streaming.data.match.Match
import com.natife.streaming.data.match.Team
import com.natife.streaming.data.matchprofile.Player
import com.natife.streaming.data.search.SearchResult
import com.natife.streaming.datasource.MatchParams
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

class TournamentViewModel(
    private val sportId:Int,
    private val additionalId:Int,
    private val type: SearchResult.Type,
    private val router: Router,
    private val tournamentUseCase: TournamentUseCase,
    private val teamUseCase: TeamUseCase,
    private val playerUseCase: PlayerUseCase,
    private val matchUseCase: ProfileUseCase,
    private val saveDeleteFavoriteUseCase: SaveDeleteFavoriteUseCase
) : BaseViewModel() {

    private val _tournament = MutableLiveData<Tournament>()
    private val _team = MutableLiveData<SearchResult>()
    private val _player = MutableLiveData<Player>()
    private val _list = MutableLiveData<List<Match>>()
    val list: LiveData<List<Match>> = _list
    val tournament: LiveData<Tournament> = _tournament
    val team: LiveData<SearchResult> = _team
    val player: LiveData<Player> = _player
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

            when(type){
                SearchResult.Type.PLAYER ->matchUseCase.load(ProfileUseCase.Type.PLAYER)
                SearchResult.Type.TEAM -> matchUseCase.load(ProfileUseCase.Type.TEAM)
                SearchResult.Type.TOURNAMENT ->matchUseCase.load(ProfileUseCase.Type.TOURNAMENT)
                SearchResult.Type.NON -> matchUseCase.load(ProfileUseCase.Type.TOURNAMENT)
            }


            Timber.e("JHDIDND 2 ${System.currentTimeMillis()} loaded ${list.value?.size}")
            isLoading.set(false)

        }
    }

    init {

        launchCatching {
            collect(matchUseCase.list){
                _list.value = it
            }
        }
        params = params.copy(additionalId = additionalId,sportId = sportId)
        Timber.e("komdkmfkdfmlfkmdlkfmf $type")
        when(type){
            SearchResult.Type.PLAYER ->launch {
                _player.value = playerUseCase.execute(sportId,additionalId)
            }
            SearchResult.Type.TEAM ->launch {
                _team.value = teamUseCase.execute(sportId,additionalId)
            }
            SearchResult.Type.TOURNAMENT ->launch {
               _tournament.value = tournamentUseCase.execute(sportId,additionalId)
            }
            SearchResult.Type.NON -> launch { cancel() }
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
                saveDeleteFavoriteUseCase.executeDelete(sportId=sportId,id = additionalId,type = SearchResult.Type.TOURNAMENT)
            }else{
                saveDeleteFavoriteUseCase.executeSave(sportId=sportId,id = additionalId,type = SearchResult.Type.TOURNAMENT)
            }
            _tournament.value = tournament.copy(isFavorite = !tournament.isFavorite)
        }


    }
    fun addToFavorite(player: Player) {
        launch {
            if (player.isFavorite){
                saveDeleteFavoriteUseCase.executeDelete(sportId=sportId,id = additionalId,type = SearchResult.Type.PLAYER)
            }else{
                saveDeleteFavoriteUseCase.executeSave(sportId=sportId,id = additionalId,type = SearchResult.Type.PLAYER)
            }
            _player.value = player.copy(isFavorite = !player.isFavorite)
        }


    }
    fun addToFavorite(team: SearchResult) {
        launch {
            if (team.isFavorite){
                saveDeleteFavoriteUseCase.executeDelete(sportId=sportId,id = additionalId,type = SearchResult.Type.TEAM)
            }else{
                saveDeleteFavoriteUseCase.executeSave(sportId=sportId,id = additionalId,type = SearchResult.Type.TEAM)
            }
            _team.value = team.copy(isFavorite = !team.isFavorite)
        }


    }

    fun toProfile(match: Match) {
        router.navigate(TournamentFragmentDirections.actionTournamentFragmentToMatchProfileFragment(match.id,match.sportId))
    }
}