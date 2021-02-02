package com.natife.streaming.data.match

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Match(
    val id: Int,
    val sportId: Int,
    val sportName: String,
    val date: String,
    val tournament: Tournament,
    val team1: Team,
    val team2: Team,
    val info: String,
    val hasVideo: Boolean,
    val live: Boolean,
    val storage: Boolean,
    val subscribed: Boolean,
    val access: Boolean,
    val image: String,
    val placeholder: String
): Parcelable

