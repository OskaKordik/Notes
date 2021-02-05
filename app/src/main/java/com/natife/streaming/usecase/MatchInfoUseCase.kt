package com.natife.streaming.usecase

import com.natife.streaming.API_MATCH_INFO
import com.natife.streaming.API_MATCH_PROFILE
import com.natife.streaming.API_SPORTS
import com.natife.streaming.API_TRANSLATE
import com.natife.streaming.api.MainApi
import com.natife.streaming.data.match.Match
import com.natife.streaming.data.match.Team
import com.natife.streaming.data.match.Tournament
import com.natife.streaming.data.request.*
import com.natife.streaming.utils.ImageUrlBuilder
import kotlinx.coroutines.async

interface MatchInfoUseCase {
    suspend fun execute(sportId: Int, matchId: Int): Match
}
class MatchInfoUseCaseImpl(private val api: MainApi): MatchInfoUseCase{
    override suspend fun execute(sportId: Int, matchId: Int): Match {
       val match = api.getMatchInfoGlobal(BaseRequest(
           procedure = API_MATCH_INFO,
           params = MatchInfo(sportId = sportId, matchId=matchId)
       ))

        val sports = api.getSports(
            BaseRequest(
                procedure = API_SPORTS,
                params = EmptyRequest()
            )
        )
        val sportTranslate = api.getTranslate(
            BaseRequest(
                procedure = API_TRANSLATE,
                TranslateRequest(
                    language = "ru", //todo remove hardcode
                    params = sports.map { it.lexic }
                )
            )
        )
        val profile =  api.getMatchProfile(
                BaseRequest(
                    procedure = API_MATCH_PROFILE, params = MatchProfileRequest(
                        sportId = match.sport ?: sportId,
                        tournament = match.tournament?.id ?:0
                    )
                )
            )



        return Match(
            id = match.id,
            sportId = match.sport?: sportId?:0,
            sportName = sportTranslate[sports.find { it.id == match.sport }?.lexic.toString()]?.text  ?: "",
            date = match.date,
            tournament = Tournament(
                match.tournament?.id?:-1,
                match.tournament?.nameRus?:""
            ),// todo multilang
            team1 = Team(
                match.team1.id,
                match.team1.nameRus,
                score = match.team1.score
            ),
            team2 = Team(
                match.team2.id,
                match.team2.nameRus,
                score = match.team2.score
            ),
            info = "${profile?.country?.name_rus} ${profile.nameRus}",
            access = match.access,
            hasVideo = match.hasVideo,
            image = ImageUrlBuilder.getUrl(
                match.sport?: sportId?:0,
                ImageUrlBuilder.Companion.Type.TOURNAMENT, match.tournament?.id?:-1
            ),
            placeholder = ImageUrlBuilder.getPlaceholder(
                match.sport?: sportId?:0,
                ImageUrlBuilder.Companion.Type.TOURNAMENT
            ),
            live = match.live,
            storage = match.storage,
            subscribed = match.sub
        )
    }

}