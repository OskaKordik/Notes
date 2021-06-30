package com.natife.streaming.data.matchprofile

import android.os.Parcelable
import com.natife.streaming.data.dto.matchprofile.MatchInfoEpisodeDTO
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Episode(
    val title: String = "",
    val end: Long,
    val half: Int,
    val start: Long,
    val image: String = "",
    val placeholder: String = ""
): Parcelable

fun MatchInfoEpisodeDTO.toEpisode():Episode{
    return Episode(
        end = this.e,
        half = this.h.toInt(),
        start = this.s
    )
}