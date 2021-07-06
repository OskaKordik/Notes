package com.natife.streaming.ui.matchprofile
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.natife.streaming.base.BaseViewModel
//import com.natife.streaming.data.Second
//import com.natife.streaming.data.Video
//import com.natife.streaming.data.match.Match
//import com.natife.streaming.data.matchprofile.Episode
//import com.natife.streaming.data.matchprofile.MatchInfo
//import com.natife.streaming.data.player.PlayerSetup
//
//import com.natife.streaming.router.Router
//import com.natife.streaming.usecase.MatchProfileUseCase
//import com.natife.streaming.usecase.SecondUseCase
//
//abstract class WatchViewModel : BaseViewModel() {
//    abstract fun back()
//
//    abstract val title: LiveData<String>
//    abstract val second: LiveData<Int>
//    abstract val startFrom: LiveData<Pair<Long, Second>>
//    abstract fun toPlayer(episode: Episode)
//}
//
//class WatchViewModelImpl(
//    private val match: Match,
//    private val videos: Array<Video>,
//    private val router: Router,
//    private val secondUseCase: SecondUseCase,
//    private val matchProfileUseCase: MatchProfileUseCase
//) : WatchViewModel() {
//    override fun back() {
//        router.navigateUp()
//    }
//    private var matchInfo: MatchInfo? = null
//    override val title = MutableLiveData<String>()
//    override val second = MutableLiveData<Int>()
//    override val startFrom = MutableLiveData<Pair<Long, Second>>()
//    override fun toPlayer( episode: Episode) {
//        matchInfo?.let{
//            router.navigate(
//                WatchFragmentDirections.actionWatchFragmentToPlayerFragment(
//                    PlayerSetup(
//                        playlist =
//                        mutableMapOf<String, List<Episode>>(
//                            matchInfo!!.translates.ballInPlayTranslate to matchInfo!!.ballInPlay,
//                            matchInfo!!.translates.highlightsTranslate to matchInfo!!.highlights,
//                            matchInfo!!.translates.goalsTranslate to matchInfo!!.goals
//                        ),
//                        video = videos.toList(),
//                        currentEpisode = episode,
//                        match = match
//                    )
//                )
//            )
//        }
//
//    }
//
//    init {
//        title.value = "${match.team1.name} \u2014 ${match.team2.name}"
//        launch {
//            matchInfo = matchProfileUseCase.getMatchInfo(match.id, match.sportId)
//            secondUseCase.execute(matchId = match.id, sportId = match.sportId)?.let { second ->
//                startFrom.value = ((videos.groupBy { it.quality }.values.toList()[0].filter { it.period < second.half }
//                        .map { it.duration }.sum() + second.second) to second)
//            }
//
//        }
//    }
//
//}