package com.natife.streaming.ui.popupmatch.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.R
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
import com.natife.streaming.usecase.VideoUseCase

abstract class PopupVideoViewModel : BaseViewModel() {
    abstract val team1: LiveData<List<Player>>
    abstract val team2: LiveData<List<Player>>
    abstract val info: LiveData<MatchInfo>
    abstract val episodes: LiveData<List<Episode>>
    abstract val fullVideoDuration: LiveData<Long>
    abstract val match: LiveData<Match>
    abstract fun play(episode: Episode? = null, playList: List<Episode>? = null)
    abstract fun onStatisticClicked()
    abstract fun onFinishClicked()
}

class PopupVideoViewModelImpl(
    private val sport: Int,
    private val matchId: Int,
    private val matchProfileUseCase: MatchProfileUseCase,
    private val matchInfoUseCase: MatchInfoUseCase,
    private val router: Router,
    private val videoUseCase: VideoUseCase,
) : PopupVideoViewModel() {
    private var _match: Match? = null
    override val match = MutableLiveData<Match>()
    private var videos: List<Video>? = null
    override val team1 = MutableLiveData<List<Player>>()
    override val team2 = MutableLiveData<List<Player>>()
    override val info = MutableLiveData<MatchInfo>()
    override val episodes = MutableLiveData<List<Episode>>()
    override val fullVideoDuration = MutableLiveData<Long>()
    private var matchInfo: MatchInfo? = null

    override fun play(episode: Episode?, playList: List<Episode>?) {
        matchInfo?.let {
            var endTimeEpisode = 0L
            var startTimeEpisode = 0L
            val timeList = videos
                ?.filter { it.abc == "0" }
                ?.groupBy { it.quality }!!["720"]
                ?.map { video ->
//                Timber.tag("TAG").d(video.toString())
                    startTimeEpisode += endTimeEpisode
                    endTimeEpisode += (video.duration)
                    Episode(
                        title = "${_match?.info}",
                        endMs = (endTimeEpisode - startTimeEpisode) / 1000,
                        half = (video.period),
                        startMs = 0,
                        image = _match?.image ?: "",
                        placeholder = _match?.placeholder ?: ""
                    )
                }
            router.navigate(
                PopupVideoFragmentDirections.actionGlobalPlayerFragment(
                    PlayerSetup(
                        playlist =
                        mutableMapOf<String, List<Episode>>(
                            it.translates.ballInPlayTranslate to it.ballInPlay,
                            it.translates.highlightsTranslate to it.highlights,
                            it.translates.goalsTranslate to it.goals,
                            it.translates.fullGameTranslate to (timeList ?: listOf())
                        ),
//                            it.translates.fullGameTranslate to listOf(
//                                Episode(
//                                    startMs = 0,
//                                    endMs = -1,
//                                    half = 0,
//                                    title = "${_match?.info}",
//                                    image = _match?.image ?: "",
//                                    placeholder = _match?.placeholder ?: ""
//                                )
//                            )
//                        ),
                        video = videos,
                        currentEpisode = episode,
                        currentPlaylist = playList,
                        match = _match
                    )
                )
            )
        }
    }

    override fun onStatisticClicked() {
        val direction =
            PopupVideoFragmentDirections.actionPopupVideoFragmentToPopupStatisticsFragment(
                sportId = sport,
                matchId = matchId
            )
        router.navigate(direction)
    }

    override fun onFinishClicked() {
        router.navigate(R.id.action_global_nav_main)
    }

    init {
        launch {
            _match = matchInfoUseCase.execute(sportId = sport, matchId = matchId)
            match.value = _match
        }
        launch {
            matchInfo = matchProfileUseCase.getMatchInfo(matchId, sport)
            info.value = matchInfo
            team1.value = matchInfo?.players1
            team2.value = matchInfo?.players2
            episodes.value = matchInfo?.highlights
        }
        launch {
            val video = videoUseCase.execute(matchId, sport)
            videos = video
            fullVideoDuration.value = video.filter { it.abc == "0" }
                .groupBy { it.quality }?.entries?.maxByOrNull { it.key.toInt() }!!.value.map { (it.duration / 1000) }
                .sum()
        }
    }


}