package com.natife.streaming.db.entity

import androidx.room.Entity
import com.natife.streaming.data.dto.country.CountryDTO


@Entity(primaryKeys = ["id"])
data class PreferencesTournament(
    val id: Int,
    val country: CountryDTO,
    val gender: Int,
    val nameEng: String,
    val nameRus: String,
    val sport: Int,
    val teamType: Int,
    val tournamentType: Int
)

@Entity(primaryKeys = ["id"])
data class PreferencesSport(
    val id: Int,
    val name: String,
    val lexic: Int
)