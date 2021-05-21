package com.natife.streaming.usecase

import com.natife.streaming.API_TOURNAMENT_LIST
import com.natife.streaming.api.MainApi
import com.natife.streaming.apiModule
import com.natife.streaming.data.dto.tournament.TournamentListDTO
import com.natife.streaming.data.match.Tournament
import com.natife.streaming.data.request.BaseRequest
import com.natife.streaming.data.request.TournamentsRequest
//new
interface GetTournamentUseCase {
    @Deprecated ("Old")
    suspend fun execute(sportId: Int?):List<Tournament>
    suspend fun execute():TournamentListDTO
}
class GetTournamentUseCaseImpl(private val api: MainApi): GetTournamentUseCase{
    @Deprecated ("Old")
    override suspend fun execute(sportId: Int?):List<Tournament> {
        val list =  api.getTournamentList(BaseRequest(procedure = API_TOURNAMENT_LIST,
        params = TournamentsRequest(sportId)
        ))
        return list.map{
            Tournament(it.id,it.nameRus)// todo multilang
        }
    }
//new
    override suspend fun execute()  =  api.getTournamentList(BaseRequest(procedure = API_TOURNAMENT_LIST,
            params = TournamentsRequest()))

}