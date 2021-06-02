package com.natife.streaming.usecase

import com.natife.streaming.data.dto.tournament.TournamentTranslateDTO
import com.natife.streaming.db.LocalSqlDataSourse
import com.natife.streaming.db.entity.PreferencesTournament
import com.natife.streaming.preferenses.SettingsPrefs

interface SaveTournamentUseCase {
    @Deprecated("don't use")
    fun execute(tournamentId: Int)

    suspend fun setTournamentCheckUncheck(tournament: TournamentTranslateDTO, isCheck: Boolean)
    suspend fun saveTournamentList(list: List<PreferencesTournament>)

}
class SaveTournamentUseCaseImpl(
    private val settingsPrefs: SettingsPrefs,
    private val localSqlDataSourse: LocalSqlDataSourse
    ): SaveTournamentUseCase {

    @Deprecated("don't use")
    override fun execute(tournamentId: Int) {
        settingsPrefs.saveTournament(tournamentId)
    }

    override suspend fun setTournamentCheckUncheck(
        tournament: TournamentTranslateDTO,
        isCheck: Boolean
    ) {
        localSqlDataSourse.setCheckedTournament(tournament, isCheck)
    }

    override suspend fun saveTournamentList(list: List<PreferencesTournament>) {
        localSqlDataSourse.setlistPreferencesTournament(list)
    }
}