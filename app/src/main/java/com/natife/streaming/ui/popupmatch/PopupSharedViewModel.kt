package com.natife.streaming.ui.popupmatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.natife.streaming.data.match.Match
import com.natife.streaming.data.matchprofile.Episode
import com.natife.streaming.data.matchprofile.MatchInfo
import com.natife.streaming.data.matchprofile.Player

class PopupSharedViewModel : ViewModel() {
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
}