package com.natife.streaming.data.matchprofile

import com.natife.streaming.data.dto.matchprofile.PlayersDTO

data class Player(
    val id: Int,
    val team: Int,
    val name: String,
    val image: String,
    val imagePlaceholder: String,
    val number: String,
    val isFavorite: Boolean = false
)

