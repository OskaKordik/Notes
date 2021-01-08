package com.natife.streaming.ui.player

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.match.Match
import com.natife.streaming.data.model.MatchInfo
import com.natife.streaming.usecase.GetMatchInfoUseCase
import com.natife.streaming.usecase.GetVideosUseCase
import com.natife.streaming.usecase.MatchUseCase
import timber.log.Timber

abstract class PlayerViewModel : BaseViewModel() {
    abstract fun showMatchInfo()
    abstract fun showVideo()
    abstract fun showMatches()
    abstract fun onMatchClicked(match: Match)

    abstract val videoLiveData: LiveData<String>
    abstract val matchInfoLiveData: LiveData<MatchInfo>
    abstract val matchesLiveData: LiveData<PagingData<Match>>
}

class PlayerViewModelImpl(
    //private val match: Match,
    private val getVideosUseCase: GetVideosUseCase,
    private val getMatchInfoUseCase: GetMatchInfoUseCase,
    private val matchUseCase: MatchUseCase
) : PlayerViewModel() {

    override val videoLiveData = MutableLiveData<String>()
    override val matchInfoLiveData = MutableLiveData<MatchInfo>()
    override val matchesLiveData = MutableLiveData<PagingData<Match>>()

    init {
        showVideo()
        showMatches()
        showMatchInfo()
    }

    override fun showVideo() {
        launch {
            val videos = getVideosUseCase.getVideos(matchId = 1, sportId = 1)
            videoLiveData.postValue(videos.first().videoUrl)
        }
    }

    override fun showMatchInfo() {
        launch {
            val matchInfo = getMatchInfoUseCase.getMatchInfo(sportId = 1, matchId = 1)
            matchInfoLiveData.postValue(matchInfo)
        }
    }

    override fun showMatches() {
        launch {
            matchUseCase.execute(date = "date")
            collectCatching(matchUseCase.executeFlow(5).cachedIn(viewModelScope)){ match ->
                Timber.e("collect $match")
                matchesLiveData.postValue(match)
            }
        }
    }

    override fun onMatchClicked(match: Match) {
        launch {
            val videos = getVideosUseCase.getVideos(matchId = match.id, sportId = match.sportId)
            videoLiveData.postValue(videos.first().videoUrl)
        }
    }
}