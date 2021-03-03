package com.natife.streaming.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.match.Match
import com.natife.streaming.data.matchprofile.Episode
import com.natife.streaming.data.player.PlayerSetup

abstract class PlayerViewModel : BaseViewModel() {
    abstract fun showMatchInfo()
    abstract fun showVideo()
    abstract fun showMatches()
    abstract fun onMatchClicked(match: Match)
    abstract fun play(it: Episode, playlist: MutableList<Episode>? = null)
    abstract fun toNextEpisode()
    abstract fun isLastEpisode():Boolean

    abstract val videoLiveData: LiveData<List<Pair<String,Long>>>
    abstract val matchInfoLiveData: LiveData<Match>
    abstract val sourceLiveData: LiveData<Map<String,List<Episode>>>
    abstract val currentEpisode: LiveData<Episode>
    abstract val currentPlaylist: LiveData<List<Episode>>
}

class PlayerViewModelImpl(
    private val setup: PlayerSetup
) : PlayerViewModel() {

    override val videoLiveData = MutableLiveData<List<Pair<String,Long>>>()
    override val matchInfoLiveData = MutableLiveData<Match>()
    override val sourceLiveData = MutableLiveData<Map<String,List<Episode>>>()
    override val currentEpisode = MutableLiveData<Episode>()
    override val currentPlaylist = MutableLiveData<List<Episode>>()


    init {

        sourceLiveData.value = setup.playlist
        videoLiveData.value = setup.video?.groupBy { it.quality }!!["720"]/*maxByOrNull { it.key.toInt() }*/?.map { it.url to it.duration }
        currentEpisode.value = setup.currentEpisode ?: setup.currentPlaylist?.get(0)
        matchInfoLiveData.value = setup.match
        currentPlaylist.value = setup.currentPlaylist

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

    override fun play(it: Episode, playlist: MutableList<Episode>? ) {
        currentEpisode.value = it
        currentPlaylist.value = playlist
    }

    override fun toNextEpisode() {
        currentPlaylist.value?.let {
           val currentIndex =  it.indexOf(currentEpisode.value)
            if (it.size> currentIndex+1){
                currentEpisode.value = it[currentIndex+1]
            }
        }
    }

    override fun isLastEpisode(): Boolean {
        currentPlaylist.value?.let {
            val currentIndex =  it.indexOf(currentEpisode.value)
            if (it.size> currentIndex+1){
                return false
            }
        }
        return true
    }
}