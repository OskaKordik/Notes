package com.natife.streaming.data.matchprofile

import com.natife.streaming.data.dto.matchprofile.MatchInfoEpisodeDTO

data class Episode(
    val end: Long,
    val half: Long,
    val start: Long
)
fun MatchInfoEpisodeDTO.toEpisode():Episode{
    return Episode(
        end = this.e,
        half = this.h,
        start = this.s
    )
}