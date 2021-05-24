package com.natife.streaming.usecase

import com.natife.streaming.data.dto.tournament.TournamentTranslateDTO
import com.natife.streaming.db.LocalSqlDataSourse
import com.natife.streaming.preferenses.SettingsPrefs

interface SaveTournamentUseCase {
    @Deprecated("don't use")
    fun execute(tournamentId: Int)

    suspend fun execute(tournament: TournamentTranslateDTO, isCheck: Boolean)
}
class SaveTournamentUseCaseImpl(
    private val settingsPrefs: SettingsPrefs,
    private val localSqlDataSourse: LocalSqlDataSourse
    ): SaveTournamentUseCase {

    @Deprecated("don't use")
    override fun execute(tournamentId: Int) {
        settingsPrefs.saveTournament(tournamentId)
    }

    override suspend fun execute(tournament: TournamentTranslateDTO, isCheck: Boolean) {
        if(isCheck) localSqlDataSourse.setPreferencesTournament(tournament) else  localSqlDataSourse.deletePreferencesTournament(tournament)
    }
}