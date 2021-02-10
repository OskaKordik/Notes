package com.natife.streaming.usecase

import com.natife.streaming.API_MATCH_INFO
import com.natife.streaming.API_MATCH_POPUP
import com.natife.streaming.API_TRANSLATE
import com.natife.streaming.api.MainApi
import com.natife.streaming.data.matchprofile.MatchInfo
import com.natife.streaming.data.matchprofile.Player
import com.natife.streaming.data.matchprofile.toEpisode
import com.natife.streaming.data.request.*
import com.natife.streaming.usecase.MatchProfileUseCase.Companion.getPath
import com.natife.streaming.utils.ImageUrlBuilder
import timber.log.Timber

interface MatchProfileUseCase {
    suspend fun getMatchInfo(matchId: Int, sportId: Int): MatchInfo

    companion object {
        fun getPath(sportId: Int): String {
            return when (sportId) {
                1 -> "football"
                2 -> "hockey"
                3 -> "basketball"
                else -> ""
            }
        }
    }
}

class MatchProfileUseCaseImpl(private val api: MainApi) : MatchProfileUseCase {
    override suspend fun getMatchInfo(matchId: Int, sportId: Int): MatchInfo {
        val infoDto = api.getMatchInfo(
            BaseRequest(
                procedure = API_MATCH_POPUP,
                params = MatchInfoRequest(matchId),
            ),
            getPath(sportId)
        )
        val match = api.getMatchInfoGlobal(
            BaseRequest(
                procedure = API_MATCH_INFO,
                params = MatchInfo(sportId, matchId)
            )
        )
        val translates = api.getTranslate(
            BaseRequest(
                procedure = API_TRANSLATE,
                params = TranslateRequest(
                    "ru", listOf(
                        infoDto.data.lexics.ballInPlay,
                        infoDto.data.lexics.fullGame,
                        infoDto.data.lexics.goals,
                        infoDto.data.lexics.highlights,
                        infoDto.data.lexics.interview,
                        infoDto.data.lexics.players
                    )
                )
            )
        )
        val previews = api.getMatchPreview(body = PreviewRequest().apply {
            add(PreviewRequestItem(matchId, sportId))
        })

        val image = if (previews.isNotEmpty()) {
            previews[0].preview
        } else {
            ImageUrlBuilder.getUrl(
                sportId,
                ImageUrlBuilder.Companion.Type.TOURNAMENT, match.tournament?.id ?: -1
            )
        }

        Timber.e("Translate $translates")
        return MatchInfo(
            translates = MatchInfo.Translates(
                ballInPlayTranslate = translates.get(infoDto.data.lexics.ballInPlay.toString())?.text
                    ?: "",
                fullGameTranslate = translates.get(infoDto.data.lexics.fullGame.toString())?.text
                    ?: "",
                goalsTranslate = translates.get(infoDto.data.lexics.goals.toString())?.text ?: "",
                highlightsTranslate = translates.get(infoDto.data.lexics.highlights.toString())?.text
                    ?: "",
                interviewTranslate = translates.get(infoDto.data.lexics.interview.toString())?.text
                    ?: "",
                playersTranslate = translates.get(infoDto.data.lexics.players.toString())?.text
                    ?: "",
            ),
            ballInPlay = infoDto.data.ballInPlay.data.map {
                it.toEpisode().copy(
                    image = image,
                    placeholder = ImageUrlBuilder.getPlaceholder(
                        sportId,
                        ImageUrlBuilder.Companion.Type.TOURNAMENT
                    ),
                    title = "${match.team1.nameRus} - ${match.team2.nameRus}"
                )
            },
            ballInPlayDuration = infoDto.data.ballInPlay.dur,
            highlights = infoDto.data.highlights.data.map {
                it.toEpisode().copy(
                    image = image, placeholder = ImageUrlBuilder.getPlaceholder(
                        sportId,
                        ImageUrlBuilder.Companion.Type.TOURNAMENT
                    ),
                    title = "${match.team1.nameRus} - ${match.team2.nameRus}"
                )
            },
            highlightsDuration = infoDto.data.highlights.dur,
            goals = infoDto.data.goals.data.map {
                it.toEpisode().copy(
                    image = image, placeholder = ImageUrlBuilder.getPlaceholder(
                        sportId,
                        ImageUrlBuilder.Companion.Type.TOURNAMENT
                    ),
                    title = "${match.team1.nameRus} - ${match.team2.nameRus}"
                )
            },
            goalsDuration = infoDto.data.goals.dur,
            players1 = infoDto.data.players1.map {
                Player(
                    id = it.id,
                    team = 1,
                    name = it.name_rus,//TODO Multilang
                    image = ImageUrlBuilder.getUrl(
                        sportId,
                        ImageUrlBuilder.Companion.Type.PLAYER,
                        it.id
                    ),
                    imagePlaceholder = ImageUrlBuilder.getPlaceholder(
                        sportId,
                        ImageUrlBuilder.Companion.Type.PLAYER
                    ),
                    number = it.num
                )
            },
            players2 = infoDto.data.players2.map {
                Player(
                    id = it.id,
                    team = 2,
                    name = it.name_rus,//TODO Multilang
                    image = ImageUrlBuilder.getUrl(
                        sportId,
                        ImageUrlBuilder.Companion.Type.PLAYER,
                        it.id
                    ),
                    imagePlaceholder = ImageUrlBuilder.getPlaceholder(
                        sportId,
                        ImageUrlBuilder.Companion.Type.PLAYER
                    ),
                    number = it.num
                )
            }
        )

    }


}