package com.natife.streaming.ui.player

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.match.Match
import com.natife.streaming.data.matchprofile.Episode
import com.natife.streaming.data.model.MatchInfo
import com.natife.streaming.data.player.PlayerSetup
import com.natife.streaming.usecase.GetMatchInfoUseCase
import com.natife.streaming.usecase.GetVideosUseCase
import com.natife.streaming.usecase.MatchUseCase
import timber.log.Timber

abstract class PlayerViewModel : BaseViewModel() {
    abstract fun showMatchInfo()
    abstract fun showVideo()
    abstract fun showMatches()
    abstract fun onMatchClicked(match: Match)
    abstract fun play(it: Episode)

    abstract val videoLiveData: LiveData<List<String>>
    abstract val matchInfoLiveData: LiveData<Match>
    abstract val sourceLiveData: LiveData<Map<String,List<Episode>>>
    abstract val currentEpisode: LiveData<Episode>
}

class PlayerViewModelImpl(
    private val setup: PlayerSetup
) : PlayerViewModel() {

    override val videoLiveData = MutableLiveData<List<String>>()
    override val matchInfoLiveData = MutableLiveData<Match>()
    override val sourceLiveData = MutableLiveData<Map<String,List<Episode>>>()
    override val currentEpisode = MutableLiveData<Episode>()

    init {

        sourceLiveData.value = setup.playlist
        videoLiveData.value = setup.video?.groupBy { it.quality }?.entries?.maxByOrNull { it.key.toInt() }!!.value.map { it.url }
        currentEpisode.value = setup.currentEpisode
        matchInfoLiveData.value = setup.match

        showVideo()
        showMatches()
        showMatchInfo()
    }

    override fun showVideo() {
        launch {
           // val videos = getVideosUseCase.getVideos(matchId = 1, sportId = 1)
          // videoLiveData.postValue(videos.first().videoUrl)
        }
    }

    override fun showMatchInfo() {
        launch {
          //  val matchInfo = getMatchInfoUseCase.getMatchInfo(sportId = 1, matchId = 1)
           // matchInfoLiveData.postValue(matchInfo)
        }
    }

    override fun showMatches() {

    }

    override fun onMatchClicked(match: Match) {
        launch {
         //   val videos = getVideosUseCase.getVideos(matchId = match.id, sportId = match.sportId)
           // videoLiveData.postValue(videos.first().videoUrl)
        }
    }

    override fun play(it: Episode) {
        currentEpisode.value = it
    }
}