package com.natife.streaming.data.matchprofile

import androidx.constraintlayout.widget.Placeholder
import com.natife.streaming.data.dto.matchprofile.MatchInfoEpisodeDTO

data class Episode(
    val title: String = "",
    val end: Long,
    val half: Long,
    val start: Long,
    val image: String = "",
    val placeholder: String = ""
)
fun MatchInfoEpisodeDTO.toEpisode():Episode{
    return Episode(
        end = this.e,
        half = this.h,
        start = this.s
    )
}