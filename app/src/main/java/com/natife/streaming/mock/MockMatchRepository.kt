package com.natife.streaming.mock

import com.natife.streaming.data.api.request.GetMatchInfoRequest
import com.natife.streaming.data.dto.MatchInfoDTO
import com.natife.streaming.data.match.Match
import com.natife.streaming.data.match.Team
import com.natife.streaming.data.match.Tournament
import com.natife.streaming.datasource.MatchParams
import kotlinx.coroutines.delay
import kotlin.random.Random

class MockMatchRepository {
    var requestParams: MatchParams? = null

    fun prepare(requestParams: MatchParams) {
        this.requestParams = requestParams
    }
    suspend fun getMatches(
        date: String,
        limit: Int,
        offset: Int,
        sportId: Int?,
        subOnly: Boolean,
        tournamentId: Int?
    ): List<Match> {
        delay(500)
        val resp = mutableListOf<Match>()
        for (i in 0..60) {
            resp.add(
                Match(
                    Random(5).nextInt(),
                    Random(50).nextInt(),
                    "basketball",
                    "25.01.2020 25:47",
                    Tournament(
                        id = Random(100).nextInt(),
                        nameRus = "Молодежная хоккейная лига",
                        nameEng = "Молодежная хоккейная лига"
                    ),
                    team1 = Team(
                        id = Random(5).nextInt(),
                        nameRus = "МХК Динамо Мск",
                        nameEng = "МХК Динамо Мск",
                        shortNameRus = "МХК Динамо Мск",
                        shortNameEng = "МХК Динамо Мск",
                        score = 0
                    ),
                    team2 = Team(
                        id = Random(5).nextInt(),
                        nameRus = "МХК Динамо Мск",
                        nameEng = "МХК Динамо Мск",
                        shortNameRus = "МХК Динамо Мск",
                        shortNameEng = "МХК Динамо Мск",
                        score = 0
                    ),
                    "Россия Суперлига 34 тур",
                    true,
                    true,
                    true,
                    true,
                    true,
                    "https://instatscout.com/images/tournaments/180/39.png"
                )
            )
        }
        return resp
    }

    suspend fun getMatchInfo(request: GetMatchInfoRequest): MatchInfoDTO {
        return MatchInfoDTO(
            tournament = Tournament(
                id = 0,
                nameRus = "Россия. Суперлига 1",
                nameEng = "Россия. Суперлига 1"
            ),
            date = "25.01.2020 25:47",
            hasVideo = true,
            live = true,
            storage = true,
            team1 = Team(
                id = Random(5).nextInt(),
                nameRus = "МХК Динамо Мск",
                nameEng = "МХК Динамо Мск",
                shortNameRus = "МХК Динамо Мск",
                shortNameEng = "МХК Динамо Мск",
                score = 0
            ),
            team2 = Team(
                id = Random(5).nextInt(),
                nameRus = "МХК Динамо Мск",
                nameEng = "МХК Динамо Мск",
                shortNameRus = "МХК Динамо Мск",
                shortNameEng = "МХК Динамо Мск",
                score = 0
            ),
            sub = true,
            access = true
        )
    }

}