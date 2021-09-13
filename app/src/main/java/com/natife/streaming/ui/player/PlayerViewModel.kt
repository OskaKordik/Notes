package com.natife.streaming.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.VIDEO_1080
import com.natife.streaming.VIDEO_480
import com.natife.streaming.VIDEO_720
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.matchprofile.Episode
import com.natife.streaming.data.player.PlayerBottomBarSetup
import com.natife.streaming.data.player.PlayerSetup
import com.natife.streaming.data.player.toInitBottomData
import com.natife.streaming.ext.Event
import com.natife.streaming.router.Router
import com.natife.streaming.ui.player.menu.quality.VideoQualityParams
import timber.log.Timber

abstract class PlayerViewModel : BaseViewModel() {
    abstract fun play(it: Episode, playlist: List<Episode>? = null)
    abstract fun openVideoQualityMenu()
    abstract fun changeVideoQuality(videoQuality: String, currentPosition: Long)
    abstract fun onBackClicked()
    abstract fun setCurrentSeekBarId(id: Int)
    abstract fun updatePlayList(list: List<Episode>, buttonText: String)
    abstract fun getStartMsPreviewEpisode(): Long

    abstract val videoLiveData: LiveData<Event<List<Pair<String, Long>>>>
    abstract val matchInfoLiveData: LiveData<String>
    abstract val videoDuration: LiveData<Long>
    abstract val sourceLiveData: LiveData<Map<String, List<Episode>>>
    abstract val currentEpisode: LiveData<Episode>
    abstract val viewedEpisode: List<Episode>
    abstract val videoQualityListLiveData: LiveData<List<String>>
    abstract val initBottomBarData: LiveData<PlayerBottomBarSetup?>
    abstract var currentWindow: Int
    abstract val currentSeekBarId: LiveData<Int>
    abstract var isNewEpisodeStarted: Boolean
    abstract val videoQualityParams: LiveData<VideoQualityParams>
}

class PlayerViewModelImpl(
    private val setup: PlayerSetup,
    private val router: Router
) : PlayerViewModel() {

    override val videoLiveData = MutableLiveData<Event<List<Pair<String, Long>>>>()
    override val matchInfoLiveData = MutableLiveData<String>()
    override val sourceLiveData = MutableLiveData<Map<String, List<Episode>>>()
    override val currentEpisode = MutableLiveData<Episode>()
    override val viewedEpisode = arrayListOf<Episode>()
    override val videoDuration = MutableLiveData<Long>()
    override val videoQualityListLiveData = MutableLiveData<List<String>>()
    override val initBottomBarData = MutableLiveData<PlayerBottomBarSetup?>()
    override var currentWindow: Int = 0
        set(value) {
            field = if (value >= 0) value else 0
        }
    override val currentSeekBarId = MutableLiveData<Int>()
    override var isNewEpisodeStarted: Boolean = false
    override val videoQualityParams = MutableLiveData<VideoQualityParams>()

    init {
        initBottomBarData.value = setup.toInitBottomData()
        sourceLiveData.value = setup.playlist
        videoLiveData.value = setup.video?.filter { it.abc == "0" }
            ?.groupBy { it.quality }!!["720"]/*maxByOrNull { it.key.toInt() }*/?.map { it.url to it.duration }
            ?.let { Event(it) }
        matchInfoLiveData.value = setup.startTitle
        videoDuration.value = setup.videoDuration
    }

    override fun play(it: Episode, playlist: List<Episode>?) {
        currentEpisode.value.let {
            if (it != null) {
                viewedEpisode.add(it)
            }
        }
        currentEpisode.value = it
    }

    override fun getStartMsPreviewEpisode(): Long {
        currentEpisode.value = viewedEpisode.last()
        return viewedEpisode.last().startMs
    }

    override fun openVideoQualityMenu() {
        videoQualityParams.value = VideoQualityParams(
            setup
                .video
                ?.sortedByDescending { it.quality.toInt() }
                ?.map { it.quality }
                ?.distinct() ?: listOf(
                VIDEO_1080,
                VIDEO_720,
                VIDEO_480
            )
        )
    }

    override fun changeVideoQuality(videoQuality: String, currentPosition: Long) {
        videoLiveData.value = setup
            .video
            ?.filter { it.abc == "0" }
            ?.groupBy { it.quality }!![videoQuality]
            ?.map { it.url to it.duration }?.let { Event(it) }
        currentEpisode.postValue(currentEpisode.value?.copy( startMs = currentPosition))
    }

    override fun onBackClicked() {
        router.navigateUp()
    }

    override fun setCurrentSeekBarId(id: Int) {
        currentSeekBarId.value = id
    }

    override fun updatePlayList(list: List<Episode>, buttonText: String) {
        initBottomBarData.value = PlayerBottomBarSetup(
            playlist = list
                .sortedWith(compareBy({ it.half }, { it.startMs }))
                .map {
                    it.copy(
                        endMs = it.endMs * 1000,
                        startMs = it.startMs * 1000,
                        half = it.half - 1
                    )
                }
        )
        matchInfoLiveData.value = setup.titlesForButtons[buttonText]
    }

}

