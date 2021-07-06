package com.natife.streaming.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.VIDEO_1080
import com.natife.streaming.VIDEO_480
import com.natife.streaming.VIDEO_720
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.match.Match
import com.natife.streaming.data.matchprofile.Episode
import com.natife.streaming.data.player.PlayerBottomBarSetup
import com.natife.streaming.data.player.PlayerSetup
import com.natife.streaming.data.player.toInitBottomData
import com.natife.streaming.router.Router
import com.natife.streaming.ui.player.menu.quality.VideoQualityParams

abstract class PlayerViewModel : BaseViewModel() {
    abstract fun play(it: Episode, playlist: List<Episode>? = null)
    abstract fun openVideoQualityMenu()
    abstract fun changeVideoQuality(videoQuality: String)
    abstract fun onBackClicked()
    abstract fun setCurrentSeekBarId(id: Int)
    abstract fun updatePlayList(list: List<Episode>)

    abstract val videoLiveData: LiveData<List<Pair<String, Long>>>
    abstract val matchInfoLiveData: LiveData<Match>
    abstract val sourceLiveData: LiveData<Map<String, List<Episode>>>
    abstract val currentEpisode: LiveData<Episode>
    abstract val videoQualityListLiveData: LiveData<List<String>>
    abstract val initBottomBarData: LiveData<PlayerBottomBarSetup>
    abstract var currentWindow: Int
    abstract val currentSeekBarId: LiveData<Int>
}

class PlayerViewModelImpl(
    private val setup: PlayerSetup,
    private val router: Router
) : PlayerViewModel() {

    override val videoLiveData = MutableLiveData<List<Pair<String, Long>>>()
    override val matchInfoLiveData = MutableLiveData<Match>()
    override val sourceLiveData = MutableLiveData<Map<String, List<Episode>>>()
    override val currentEpisode = MutableLiveData<Episode>()
    override val videoQualityListLiveData = MutableLiveData<List<String>>()
    override val initBottomBarData = MutableLiveData<PlayerBottomBarSetup>()
    override var currentWindow: Int = 0
        set(value) {
            field = if (value >= 0) value else 0
        }
    override val currentSeekBarId = MutableLiveData<Int>()

    init {
        initBottomBarData.value = setup.toInitBottomData()
        sourceLiveData.value = setup.playlist
        videoLiveData.value = setup.video?.filter { it.abc == "0" }
            ?.groupBy { it.quality }!!["720"]/*maxByOrNull { it.key.toInt() }*/?.map { it.url to it.duration }
        matchInfoLiveData.value = setup.match

    }

    override fun play(it: Episode, playlist: List<Episode>?) {
        currentEpisode.value = it
    }

    override fun openVideoQualityMenu() {
        val videoQualityParams = VideoQualityParams(
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
        router.navigate(
            PlayerFragmentDirections.actionPlayerFragmentToVideoQualityDialog(
                videoQualityParams
            )
        )
    }

    override fun changeVideoQuality(videoQuality: String) {
        videoLiveData.value = setup
            .video
            ?.filter { it.abc == "0" }
            ?.groupBy { it.quality }!![videoQuality]
            ?.map { it.url to it.duration }
    }

    override fun onBackClicked() {
        router.navigateUp()
    }

    override fun setCurrentSeekBarId(id: Int) {
        currentSeekBarId.value = id
    }

    override fun updatePlayList(list: List<Episode>) {
        initBottomBarData.value = PlayerBottomBarSetup(
            playlist = list.sortedWith(compareBy({ it.half }, { it.startMs })).map {
                it.copy(
                    endMs = it.endMs * 1000,
                    startMs = it.startMs * 1000,
                    half = it.half
                )
            }
        )
//        Timber.tag("TAG").d("---${initBottomBarData.value}---")
    }

}

