package com.natife.streaming.mapper

import com.natife.streaming.data.dto.MatchInfoDTO
import com.natife.streaming.data.match.Tournament
import com.natife.streaming.data.model.MatchInfo

fun MatchInfoDTO.toMatchInfoItem(): MatchInfo {
    return MatchInfo(
        tournamentNameRus = tournament?.nameRus ?: "",
        tournamentNameEng = tournament?.nameEng ?: "",
        teamName1 = team1?.shortNameRus ?: team1?.nameRus ?: "",
        teamName2 = team2?.shortNameRus ?: team2?.nameRus ?: ""
    )
}