package com.natife.streaming.ui.matchprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.Video
import com.natife.streaming.data.match.Match
import com.natife.streaming.data.matchprofile.Episode
import com.natife.streaming.data.matchprofile.MatchInfo
import com.natife.streaming.data.matchprofile.Player
import com.natife.streaming.data.player.PlayerSetup
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.MatchInfoUseCase
import com.natife.streaming.usecase.MatchProfileUseCase
import com.natife.streaming.usecase.PlayerActionUseCase
import com.natife.streaming.usecase.VideoUseCase
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
    abstract fun play(episode: Episode?= null, playList: List<Episode>? = null)
}

class MatchProfileViewModelImpl(
    private val sport: Int,
    private val matchId: Int,
    private val matchProfileUseCase: MatchProfileUseCase,
    private val matchInfoUseCase: MatchInfoUseCase,
    private val router: Router,
    private val videoUseCase: VideoUseCase,
    private val playerUseCase: PlayerActionUseCase,
) : MatchProfileViewModel() {
    override fun back() {
        router.navigateUp()
    }

    private var match: Match? = null
    private var videos: List<Video>? = null
    override fun goToSettings() {

        Timber.e("$match $videos")
        if (match != null && !videos.isNullOrEmpty()) {
            router.navigate(
                MatchProfileFragmentDirections.actionMatchProfileFragmentToMatchSettingsFragment(
                    sportId = match!!.sportId,
                    match = match!!.copy(id = matchId),
                    videos = videos!!.toTypedArray()
                )
            )
        }
    }

    override val team1 = MutableLiveData<List<Player>>()
    override val team2 = MutableLiveData<List<Player>>()
    override val info = MutableLiveData<MatchInfo>()
    override val title = MutableLiveData<String>()
    override val league = MutableLiveData<String>()
    override val episodes = MutableLiveData<List<Episode>>()
    override val fullVideoDuration = MutableLiveData<Long>()
    private var matchInfo: MatchInfo? = null

    private var playerJob: Job? = null
    override fun goals() {
        matchInfo?.let {
            if (!it.goals.isNullOrEmpty()){
                play(playList = it.goals)
            }

            //episodes.value = it.goals
        }
    }

    override fun review() {
        matchInfo?.let {
            //episodes.value = it.highlights
            if (!it.highlights.isNullOrEmpty()){
                play(playList = it.highlights)
            }

        }
    }

    override fun ballInPlay() {
        matchInfo?.let {
            //episodes.value = it.ballInPlay
            if (!it.ballInPlay.isNullOrEmpty()){
                play(playList = it.ballInPlay)
            }


        }
    }

    override fun fullMatch() {
        match?.let {
            play(episode = Episode(
                start = 0,
                end = -1,
                half = 0,
                title = "${it.info}",
                image = it.image,
                placeholder = it.placeholder
            ))
//            episodes.value = listOf(
//                Episode(
//                    start = 0,
//                    end = -1,
//                    half = 1,
//                    title = "${it.info}",
//                    image = it.image,
//                    placeholder = it.placeholder
//                )
//            )
        }

    }

    override fun player(player: Player) {
        playerJob?.cancel()
        playerJob = launch {
            withContext(Dispatchers.IO) {
                val episs = playerUseCase.execute(matchId, sport, player.id)
                withContext(Dispatchers.Main) {
                    episodes.value = episs
                    play(playList = episs)
                }

            }
        }
    }

    override fun play(episode: Episode?, playList: List<Episode>?) {
        router.navigate(
            MatchProfileFragmentDirections.actionMatchProfileFragmentToPlayerFragment(
                PlayerSetup(
                    playlist =
                    mutableMapOf<String, List<Episode>>(
                        matchInfo!!.translates.ballInPlayTranslate to matchInfo!!.ballInPlay,
                        matchInfo!!.translates.highlightsTranslate to matchInfo!!.highlights,
                        matchInfo!!.translates.goalsTranslate to matchInfo!!.goals,
                        matchInfo!!.translates.fullGameTranslate to listOf(
                            Episode(
                                start = 0,
                                end = -1,
                                half = 0,
                                title = "${match?.info}",
                                image = match?.image ?: "",
                                placeholder = match?.placeholder?: ""
                            )
                        )
                    ),
                    video = videos,
                    currentEpisode = episode,
                    currentPlaylist = playList,
                    match = match
                )
            )
        )
    }

    init {
        launch {

            match = matchInfoUseCase.execute(sportId = sport, matchId = matchId)

            title.value =
                "${match!!.team1.name} ${match!!.team1.score}:${match!!.team2.score} ${match!!.team2.name}"
            league.value = match!!.info
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
            val video = videoUseCase.execute(matchId, sport)
            videos = video
            Timber.e("KMOKFMOFKF ${video.filter { it.abc == "0" }.groupBy { it.quality }?.entries?.maxByOrNull { it.key.toInt() }}")
            Timber.e("KMOKFMOFKF ${video.filter { it.abc == "0" }.groupBy { it.quality }?.entries?.maxByOrNull { it.key.toInt() }!!.value.map { (it.duration/1000) }.sum()}")
            var a = mutableListOf<Video>()
            a.addAll(videos!!)
            val b = a.filter { it.abc == "0" }.groupBy { it.quality }?.entries?.maxByOrNull { it.key.toInt() }!!.value.map { (it.duration/1000) }.sum()


            fullVideoDuration.value = video.filter { it.abc == "0" }.groupBy { it.quality }?.entries?.maxByOrNull { it.key.toInt() }!!.value.map { (it.duration/1000) }.sum()
        }
    }


}