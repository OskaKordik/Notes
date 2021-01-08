package com.natife.streaming.usecase

import com.natife.streaming.data.match.Tournament

interface GetTournamentUseCase {
    suspend fun execute():List<Tournament>
}
class GetTournamentUseCaseImpl(): GetTournamentUseCase{
    override suspend fun execute():List<Tournament> {
        return listOf(Tournament(id = 1,"2020 Brawl For The Ball U16"),Tournament(id = 2,"2020 Brawl For The Ball U17"))
    }
}