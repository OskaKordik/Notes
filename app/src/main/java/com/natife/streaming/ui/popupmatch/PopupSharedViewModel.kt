package com.natife.streaming.ui.popupmatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.match.Match
import com.natife.streaming.data.matchprofile.Episode
import com.natife.streaming.data.matchprofile.MatchInfo
import com.natife.streaming.data.matchprofile.Player
import com.natife.streaming.ext.Event
import com.natife.streaming.usecase.PlayerActionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.get

class PopupSharedViewModel : BaseViewModel(), KoinComponent {
    private var playerJob: Job? = null
    private val playerUseCase: PlayerActionUseCase = get()

    var matchId: Int = -1
    var sportId: Int = -1

    private val _match = MutableLiveData<Match>()
    val match: LiveData<Match> = _match

    private val _matchInfo = MutableLiveData<MatchInfo>()
    val matchInfo: LiveData<MatchInfo> = _matchInfo

    private val _fullVideoDuration = MutableLiveData<Long>()
    val fullVideoDuration: LiveData<Long> = _fullVideoDuration

    private val _episodes = MutableLiveData<List<Episode>>()
    val episodes: LiveData<List<Episode>> = _episodes

    private val _team1 = MutableLiveData<List<Player>>()
    val team1: LiveData<List<Player>> = _team1

    private val _team2 = MutableLiveData<List<Player>>()
    val team2: LiveData<List<Player>> = _team2

    private val _playEpisode = MutableLiveData<Event<Episode?>>()
    val playEpisode: LiveData<Event<Episode?>> = _playEpisode

    private val _playPlayList = MutableLiveData<Event<List<Episode>?>>()
    val playPlayList: LiveData<Event<List<Episode>?>> = _playPlayList

    fun setMatch(math: Match) {
        _match.value = math
    }

    fun setMatchInfo(mathInfo: MatchInfo) {
        _matchInfo.value = mathInfo
    }

    fun setFullVideoDuration(fullVideoDuration: Long) {
        _fullVideoDuration.value = fullVideoDuration
    }

    fun setEpisodes(episodes: List<Episode>) {
        _episodes.value = episodes
    }

    fun setTeam1(team1: List<Player>) {
        _team1.value = team1
    }

    fun setTeam2(team2: List<Player>) {
        _team2.value = team2
    }

    fun goals() {
        matchInfo?.let {
            if (!it.value?.goals.isNullOrEmpty()) {
                _playPlayList.value = Event(it.value?.goals)
            }
        }
    }

    fun review() {
        matchInfo?.let {
            //episodes.value = it.highlights
            if (!it.value?.highlights.isNullOrEmpty()) {
                _playPlayList.value = Event(it.value?.highlights)
            }

        }
    }

    fun ballInPlay() {
        matchInfo?.let {
            //episodes.value = it.ballInPlay
            if (!it.value?.ballInPlay.isNullOrEmpty()) {
                _playPlayList.value = Event(it.value?.ballInPlay)
            }


        }
    }

    fun fullMatch() {
        _match?.let {
            _playEpisode.value = Event(
                Episode(
                    start = 0,
                    end = -1,
                    half = 0,
                    title = "${it.value?.info}",
                    image = it.value?.image ?: "",
                    placeholder = it.value?.placeholder ?: ""
                )
            )
        }

    }

    fun playEpisode(episod: Episode) {
        _playEpisode.value = Event(episod)
    }

    fun player(player: Player) {
        if (matchId == -1 || sportId == -1) return
        playerJob?.cancel()
        playerJob = launch {
            withContext(Dispatchers.IO) {
                val episods = playerUseCase.execute(matchId, sportId, player.id)
                withContext(Dispatchers.Main) {
                    _playPlayList.value = Event(episods)
                }
            }
        }
    }
}