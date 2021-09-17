package com.natife.streaming.ui.live

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.source.MediaSource
import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.GetLiveVideoUseCase
import timber.log.Timber

abstract class LiveViewModel : BaseViewModel() {
    abstract val mediaSourceLiveData: LiveData<MediaSource>
    abstract fun onFinishClicked()
}

class LiveViewModelImpl(
    private val matchId: Int,
    private val sportId: Int,
    private val title: String,
    private val router: Router,
    private val getLiveVideoUseCase: GetLiveVideoUseCase
) : LiveViewModel() {
    override val mediaSourceLiveData = MutableLiveData<MediaSource>()

    override fun onFinishClicked() {
        router.navigate(R.id.action_global_nav_main)
    }

    init {
        launch {
            val mediaSource = getLiveVideoUseCase.execute(matchId, sportId)
            mediaSourceLiveData.postValue(mediaSource)
            Timber.tag("MediaSource").d("matchId=$matchId sportId=$sportId")
            Timber.tag("MediaSource").d(mediaSource.mediaItem.mediaId)
        }
    }

}