package com.natife.streaming.ui.live

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.source.MediaSource
import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.GetLiveVideoUseCase
import com.natife.streaming.usecase.GetUserLiveMatchSecond
import com.natife.streaming.usecase.SaveUserLiveMatchSecond
import com.natife.streaming.utils.VideoHeaderUpdater
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class LiveViewModel : BaseViewModel() {
    abstract val mediaSourceLiveData: LiveData<MediaSource>
    abstract val currentPositionLiveData: LiveData<Int>
    abstract val returnToLiveEvent: LiveData<Boolean>
    abstract fun onFinishClicked()
    abstract fun saveCurrentPosition(position: Long)
    abstract fun returnToLive()
}

class LiveViewModelImpl(
    private val matchId: Int,
    private val sportId: Int,
    private val title: String,
    private val router: Router,
    private val getLiveVideoUseCase: GetLiveVideoUseCase,
    private val videoHeaderUpdater: VideoHeaderUpdater,
    private val saveUserLiveMatchSecond: SaveUserLiveMatchSecond,
    private val getUserLiveMatchSecond: GetUserLiveMatchSecond
) : LiveViewModel() {
    override val mediaSourceLiveData = MutableLiveData<MediaSource>()
    override val currentPositionLiveData = MutableLiveData<Int>()
    override val returnToLiveEvent = MutableLiveData<Boolean>(false)

    override fun onFinishClicked() {
        router.navigate(R.id.action_global_nav_main)
    }

    init {
        launch {
            val mediaSource = getLiveVideoUseCase.execute(matchId, sportId)
            mediaSourceLiveData.postValue(mediaSource)

            val currentPosition = getUserLiveMatchSecond.execute(sportId, matchId).second
            currentPositionLiveData.postValue(currentPosition)
        }
    }

    override fun saveCurrentPosition(position: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            saveUserLiveMatchSecond.execute(
                _sportId = sportId,
                _matchId = matchId,
                _half = 1,
                _second = position.toInt()
            )
        }
    }

    override fun returnToLive() {
        returnToLiveEvent.postValue(true)
    }

    override fun onCleared() {
        videoHeaderUpdater.stop()
        super.onCleared()
    }
}