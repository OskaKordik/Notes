package com.natife.streaming.usecase

import com.natife.streaming.API_TOURNAMENT_LIST
import com.natife.streaming.api.MainApi
import com.natife.streaming.data.dto.tournament.TournamentListDTO
import com.natife.streaming.data.match.Tournament
import com.natife.streaming.data.request.BaseRequest
import com.natife.streaming.data.request.TournamentsRequest
import com.natife.streaming.db.LocalSqlDataSourse
import com.natife.streaming.db.entity.PreferencesTournament

//new
interface GetTournamentUseCase {
    @Deprecated("Old")
    suspend fun execute(sportId: Int?): List<Tournament>

    suspend fun execute(): TournamentListDTO
    suspend fun getAllUserPreferencesInTournament(): List<PreferencesTournament>?
}

class GetTournamentUseCaseImpl(
    private val api: MainApi,
    private val localSqlDataSourse: LocalSqlDataSourse
) : GetTournamentUseCase {
    @Deprecated("don't use")
    override suspend fun execute(sportId: Int?): List<Tournament> {
        val list = api.getTournamentList(
            BaseRequest(
                procedure = API_TOURNAMENT_LIST,
                params = TournamentsRequest(sportId)
            )
        )
        return list.map {
            Tournament(it.id, it.nameRus)// todo multilang
        }
    }

    override suspend fun execute() = api.getTournamentList(
        BaseRequest(
            procedure = API_TOURNAMENT_LIST,
            params = TournamentsRequest()
        )
    )

    override suspend fun getAllUserPreferencesInTournament(): List<PreferencesTournament>? =
        localSqlDataSourse.getPreferencesTournament()
}