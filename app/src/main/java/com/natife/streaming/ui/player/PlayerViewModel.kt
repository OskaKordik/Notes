package com.natife.streaming.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.match.Match
import com.natife.streaming.usecase.GetVideosUseCase
import com.natife.streaming.usecase.MatchUseCase
import timber.log.Timber

abstract class PlayerViewModel : BaseViewModel() {
    abstract fun showMatchData()
    abstract fun showVideo()
    abstract fun showMatches()

    abstract val videoLiveData: LiveData<String>
    abstract val matchLiveData: LiveData<Match>
    abstract val matchesLiveData: LiveData<PagingData<Match>>
}

class PlayerViewModelImpl(
    //private val match: Match,
    private val getVideosUseCase: GetVideosUseCase,
    private val matchUseCase: MatchUseCase
) : PlayerViewModel() {

    override val videoLiveData = MutableLiveData<String>()
    override val matchLiveData = MutableLiveData<Match>()
    override val matchesLiveData = MutableLiveData<PagingData<Match>>()

    init {
        showVideo()
        showMatches()
    }

    override fun showVideo() {
        launch {
            val videos = getVideosUseCase.getVideos(matchId = 1, sportId = 1)
            videoLiveData.postValue(videos.first().videoUrl)
        }
    }

    override fun showMatchData() {

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
}