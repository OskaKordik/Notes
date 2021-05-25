package com.natife.streaming.db.entity

import androidx.room.Entity
import com.natife.streaming.data.Sport
import com.natife.streaming.data.dto.country.CountryDTO
import com.natife.streaming.data.dto.sports.SportTranslateDTO
import com.natife.streaming.data.dto.tournament.TournamentTranslateDTO


@Entity(primaryKeys = ["id","sport","tournamentType"])
data class PreferencesTournament(
    val id: Int,
    val name: String,
    val sport: Int,
    val tournamentType: Int
)


fun TournamentTranslateDTO.toPreferencesTournament(): PreferencesTournament =
    PreferencesTournament(
        id = this.id,
        name = this.name,
        sport = this.sport,
        tournamentType = this.tournamentType
    )



@Entity(primaryKeys = ["id"])
data class PreferencesSport(
    val id: Int,
    val name: String,
    val lexic: Int
)

fun SportTranslateDTO.toPreferencesSport(): PreferencesSport =
    PreferencesSport(
        id = this.id,
        name = this.name,
        lexic = this.lexic
    )