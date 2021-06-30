package com.natife.streaming.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
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
import timber.log.Timber

abstract class PlayerViewModel : BaseViewModel() {
    abstract fun showMatchInfo()
    abstract fun showVideo()
    abstract fun showMatches()
    abstract fun onMatchClicked(match: Match)
    abstract fun play(it: Episode, playlist: List<Episode>? = null)
    abstract fun toNextEpisode()
    abstract fun isLastEpisode(): Boolean
    abstract fun openVideoQualityMenu()
    abstract fun changeVideoQuality(videoQuality: String)
    abstract fun onBackClicked()

    abstract val videoLiveData: LiveData<List<Pair<String, Long>>>
    abstract val matchInfoLiveData: LiveData<Match>
    abstract val sourceLiveData: LiveData<Map<String, List<Episode>>>
    abstract val currentEpisode: LiveData<Episode>

    //    abstract val currentPlaylist: LiveData<List<Episode>>
    abstract val videoQualityListLiveData: LiveData<List<String>>
    abstract val initBottomBarData: LiveData<PlayerBottomBarSetup>
    abstract var currentWindow: Int
}

class PlayerViewModelImpl(
    private val setup: PlayerSetup,
    private val router: Router
) : PlayerViewModel() {

    override val videoLiveData = MutableLiveData<List<Pair<String, Long>>>()
    override val matchInfoLiveData = MutableLiveData<Match>()
    override val sourceLiveData = MutableLiveData<Map<String, List<Episode>>>()
    override val currentEpisode = MutableLiveData<Episode>()
    private val currentPlaylist = MutableLiveData<List<Episode>>()
    override val videoQualityListLiveData = MutableLiveData<List<String>>()
    override val initBottomBarData = MutableLiveData<PlayerBottomBarSetup>()
    override var currentWindow: Int = 0
        set(value) {
            field = if (value >= 0) value else 0
        }

    init {
        initBottomBarData.value = setup.toInitBottomData()
        currentPlaylist.value =
            setup.currentPlaylist?.sortedWith(compareBy({ it.half }, { it.start }))

        sourceLiveData.value = setup.playlist
        videoLiveData.value = setup.video?.filter { it.abc == "0" }
            ?.groupBy { it.quality }!!["720"]/*maxByOrNull { it.key.toInt() }*/?.map { it.url to it.duration }
//        currentEpisode.value = setup.currentEpisode ?: setup.currentPlaylist?.sortedWith(compareBy({ it.half }, { it.start }))?.get(0)
        currentEpisode.value = setup.toInitBottomData()?.playlist?.get(0)
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

    override fun play(it: Episode, playlist: List<Episode>?) {
        Timber.tag("TAG").d(Gson().toJson(it))
        currentEpisode.value = it
//        currentPlaylist.value = playlist
    }

    override fun toNextEpisode() {
        currentPlaylist.value?.let {
            val currentIndex = it.indexOf(currentEpisode.value)
            if (it.size > currentIndex + 1) {
                currentEpisode.value = it[currentIndex + 1]
            }
        }
    }

    override fun isLastEpisode(): Boolean {
        currentPlaylist.value?.let {
            val currentIndex = it.indexOf(currentEpisode.value)
            if (it.size > currentIndex + 1) {
                return false
            }
        }
        return true
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
}

