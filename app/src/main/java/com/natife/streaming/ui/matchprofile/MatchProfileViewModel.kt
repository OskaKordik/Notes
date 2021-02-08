package com.natife.streaming.ui.matchprofile

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.Video
import com.natife.streaming.data.match.Match
import com.natife.streaming.data.matchprofile.Episode
import com.natife.streaming.data.matchprofile.MatchInfo
import com.natife.streaming.data.matchprofile.Player
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import timber.log.Timber

abstract class MatchProfileViewModel : BaseViewModel() {
    abstract fun back()
    abstract fun goToSettings()

    abstract val team1: LiveData<List<Player>>
    abstract val team2: LiveData<List<Player>>
    abstract val info: LiveData<MatchInfo>
    abstract val title: LiveData<String>
    abstract val league: LiveData<String>
    abstract val episodes: LiveData<List<Episode>>
    abstract val fullVideoDuration: LiveData<Long>

    abstract fun goals()
    abstract fun review()
    abstract fun ballInPlay()
    abstract fun fullMatch()
    abstract fun player(player: Player)
}

class MatchProfileViewModelImpl(
    private val sport: Int,
    private val matchId: Int,
    private val matchProfileUseCase: MatchProfileUseCase,
    private val matchInfoUseCase: MatchInfoUseCase,
    private val router: Router,
    private val videoUseCase: VideoUseCase,
    private val playerUseCase: PlayerActionUseCase,
    private val getThumbnailUseCase: GetThumbnailUseCase
) : MatchProfileViewModel() {
    override fun back() {
        router.navigateUp()
    }
    private var match: Match?= null
    private var videos: List<Video>?= null
    override fun goToSettings() {
        if (match!=null && !videos.isNullOrEmpty()){
            router.navigate(MatchProfileFragmentDirections.actionMatchProfileFragmentToMatchSettingsFragment(sportId = match!!.sportId,match = match!!.copy(id= matchId),videos = videos!!.toTypedArray()))
        }
    }

    override val team1 = MutableLiveData<List<Player>>()
    override val team2 = MutableLiveData<List<Player>>()
    override val info = MutableLiveData<MatchInfo>()
    override val title = MutableLiveData<String>()
    override val league = MutableLiveData<String>()
    override val episodes = MutableLiveData<List<Episode>>()
    override val fullVideoDuration = MutableLiveData<Long>()
    private var matchInfo : MatchInfo? = null

    private var playerJob: Job? = null
    override fun goals() {
        matchInfo?.let {
            episodes.value = it.goals
        }
    }

    override fun review() {
        matchInfo?.let {
            episodes.value = it.highlights
        }
    }

    override fun ballInPlay() {
        matchInfo?.let {
            episodes.value = it.ballInPlay
        }
    }

    override fun fullMatch() {
        match?.let {
            episodes.value = listOf(Episode(
                start = 0,
                end = -1,
                half = -1,
                title = "${it.info}",
                image = it.image,
                placeholder = it.placeholder
            ))
        }

    }

    override fun player(player: Player) {
        playerJob?.cancel()
        playerJob = launch {
          withContext(Dispatchers.IO){
               val episs = playerUseCase.execute(matchId,sport,player.id)
              withContext(Dispatchers.Main){
                  episodes.value = episs
              }

            }
        }

    }

    init {
        launch {

            val match = matchInfoUseCase.execute(sportId = sport, matchId=matchId)
            Timber.e("IJDOIJDOI $match")
        title.value =  "${match.team1.name} ${match.team1.score}:${match.team2.score} ${match.team2.name}"
        league.value = match.info
        }
        launch {
            matchInfo = matchProfileUseCase.getMatchInfo(matchId, sport)
            Timber.e(matchInfo.toString())
            info.value = matchInfo
            team1.value = matchInfo?.players1
            team2.value = matchInfo?.players2
            episodes.value = matchInfo?.highlights
        }
        launch {
            val video = videoUseCase.execute(matchId,sport)
            videos = video
            Timber.e("juidfdnffd ${video.groupBy { it.quality }.values.toList().map { it.map { "${it.name} ${it.period} ${it.quality}"}}}")
            fullVideoDuration.value = video.groupBy { it.quality }.values.toList()[0].map { it.duration }.sum()
        }


    }


}