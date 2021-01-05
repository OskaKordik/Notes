package com.natife.streaming.usecase

import com.natife.streaming.data.Tournament

class TournamentUseCase {

    suspend fun execute(): Tournament {
        return Tournament(
            title = "Title",
            icon = null,
            isFavorite = true
        )
    }
}