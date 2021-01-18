package com.natife.streaming.usecase

import com.natife.streaming.preferenses.SettingsPrefs

interface SaveTournamentUseCase {
    fun execute(tournamentId: Int)
}

class SaveTournamentUseCaseImpl(private val settingsPrefs: SettingsPrefs): SaveTournamentUseCase {
    override fun execute(tournamentId: Int) {
        settingsPrefs.saveTournament(tournamentId)
    }
}