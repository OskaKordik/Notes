package com.natife.streaming.usecase

import android.content.Context
import com.natife.streaming.API_MATCH_INFO
import com.natife.streaming.API_MATCH_POPUP
import com.natife.streaming.API_TRANSLATE
import com.natife.streaming.R
import com.natife.streaming.api.MainApi
import com.natife.streaming.data.matchprofile.MatchInfo
import com.natife.streaming.data.matchprofile.Player
import com.natife.streaming.data.matchprofile.toEpisode
import com.natife.streaming.data.request.*
import com.natife.streaming.usecase.MatchProfileUseCase.Companion.getPath
import com.natife.streaming.utils.ImageUrlBuilder
import java.util.*

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

class MatchProfileUseCaseImpl(
    private val api: MainApi,
    private val context: Context
) : MatchProfileUseCase {
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
                    context.resources.getString(R.string.lang).toLowerCase(Locale.ROOT),
                    listOf(
                        infoDto.data?.lexics?.ballInPlay ?: 0,
                        infoDto.data?.lexics?.fullGame ?: 0,
                        infoDto.data?.lexics?.goals ?: 0,
                        infoDto.data?.lexics?.highlights ?: 0,
                        infoDto.data?.lexics?.interview ?: 0,
                        infoDto.data?.lexics?.players ?: 0
                    ) ?: emptyList<Int>()
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

        return MatchInfo(
            translates = MatchInfo.Translates(
                ballInPlayTranslate = translates[infoDto.data?.lexics?.ballInPlay.toString()]?.text
                    ?: "",
                fullGameTranslate = translates[infoDto.data?.lexics?.fullGame.toString()]?.text
                    ?: "",
                goalsTranslate = translates[infoDto.data?.lexics?.goals.toString()]?.text ?: "",
                highlightsTranslate = translates[infoDto.data?.lexics?.highlights.toString()]?.text
                    ?: "",
                interviewTranslate = translates[infoDto.data?.lexics?.interview.toString()]?.text
                    ?: "",
                playersTranslate = translates[infoDto.data?.lexics?.players.toString()]?.text
                    ?: "",
            ),
            ballInPlay = infoDto.data?.ballInPlay?.data?.map {
                it?.toEpisode()!!.copy(
                    image = image,
                    placeholder = ImageUrlBuilder.getPlaceholder(
                        sportId,
                        ImageUrlBuilder.Companion.Type.TOURNAMENT
                    ),
                    title = when (context.resources.getString(R.string.lang)) {
                        "en", "EN" -> "${match.team1.nameEng} - ${match.team2.nameEng}" ?: ""
                        "ru", "RU" -> "${match.team1.nameRus} - ${match.team2.nameRus}" ?: ""
                        else -> "${match.team1.nameEng} - ${match.team2.nameEng}" ?: ""
                    }
                )
            }?.sortedBy { it.start } ?: emptyList(),
            ballInPlayDuration = infoDto.data?.ballInPlay?.dur ?: 0,
            highlights = infoDto.data?.highlights?.data?.map {
                it?.toEpisode().copy(
                    image = image,
                    placeholder = ImageUrlBuilder.getPlaceholder(
                        sportId,
                        ImageUrlBuilder.Companion.Type.TOURNAMENT
                    ),
                    title = when (context.resources.getString(R.string.lang)) {
                        "en", "EN" -> "${match.team1.nameEng} - ${match.team2.nameEng}" ?: ""
                        "ru", "RU" -> "${match.team1.nameRus} - ${match.team2.nameRus}" ?: ""
                        else -> "${match.team1.nameEng} - ${match.team2.nameEng}" ?: ""
                    }
//                    title = "${match.team1.nameRus} - ${match.team2.nameRus}"
                )
            }?.sortedBy { it.start } ?: emptyList(),
            highlightsDuration = infoDto.data?.highlights?.dur ?: 0,
            goals = infoDto.data?.goals?.data?.map {
                it.toEpisode().copy(
                    image = image, placeholder = ImageUrlBuilder.getPlaceholder(
                        sportId,
                        ImageUrlBuilder.Companion.Type.TOURNAMENT
                    ),
                    title = when (context.resources.getString(R.string.lang)) {
                        "en", "EN" -> "${match.team1.nameEng} - ${match.team2.nameEng}" ?: ""
                        "ru", "RU" -> "${match.team1.nameRus} - ${match.team2.nameRus}" ?: ""
                        else -> "${match.team1.nameEng} - ${match.team2.nameEng}" ?: ""
                    }
//                    title = "${match.team1.nameRus} - ${match.team2.nameRus}"
                )
            }?.sortedBy { it.start } ?: emptyList(),
            goalsDuration = infoDto.data?.goals?.dur ?: 0,
            players1 = infoDto.data?.players1?.map {
                Player(
                    id = it.id,
                    team = 1,
                    name = when (context.resources.getString(R.string.lang)) {
                        "en", "EN" -> it.name_eng ?: ""
                        "ru", "RU" -> it.name_rus ?: ""
                        else -> it.name_eng ?: ""
                    },
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
            } ?: emptyList(),
            players2 = infoDto.data?.players2?.map {
                Player(
                    id = it.id,
                    team = 2,
                    name = when (context.resources.getString(R.string.lang)) {
                        "en", "EN" -> it.name_eng ?: ""
                        "ru", "RU" -> it.name_rus ?: ""
                        else -> it.name_eng ?: ""
                    },
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
            } ?: emptyList()
        )

    }


}