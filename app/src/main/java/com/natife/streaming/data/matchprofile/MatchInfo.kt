package com.natife.streaming.data.matchprofile

import com.google.gson.annotations.SerializedName
import com.natife.streaming.data.dto.matchprofile.BallInPlayDTO
import com.natife.streaming.data.dto.matchprofile.GoalsDTO
import com.natife.streaming.data.dto.matchprofile.PlayersDTO

data class MatchInfo(
    val translates:Translates,
    val highlightsDuration: Long,
    val highlights: List<Episode>,
    val ballInPlayDuration: Long,
    val ballInPlay: List<Episode>,
    val goalsDuration: Long,
    val goals: List<Episode>,
    val players1: List<Player>,
    val players2: List<Player>
){
    data class Translates(val ballInPlayTranslate: String,
                          val fullGameTranslate: String,
                          val goalsTranslate: String,
                          val highlightsTranslate: String,
                          val interviewTranslate: String,
                          val playersTranslate: String,)
}
