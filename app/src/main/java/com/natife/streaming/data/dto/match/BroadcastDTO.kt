package com.natife.streaming.data.dto.match

data class BroadcastDTO(
    val access: Boolean,
    val date: String,
    val has_video: Boolean,
    val id: Int,
    val live: Boolean,
    val sport: Int,
    val storage: Boolean,
    val sub: Boolean,
    val team1: Team1DTO,
    val team2: Team2DTO,
    val tournament: TournamentDTO
)