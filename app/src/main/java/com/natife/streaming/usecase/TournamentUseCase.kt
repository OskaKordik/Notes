package com.natife.streaming.usecase

import com.natife.streaming.API_TOURNAMENT
import com.natife.streaming.api.MainApi
import com.natife.streaming.data.Tournament
import com.natife.streaming.data.request.BaseRequest
import com.natife.streaming.data.request.TournamentProfileRequest
import com.natife.streaming.utils.ImageUrlBuilder

interface TournamentUseCase {

    suspend fun execute(sportId:Int,tournamentId:Int): Tournament
}

class TournamentUseCaseImpl(private val api: MainApi): TournamentUseCase {
    override suspend fun execute(sportId:Int,tournamentId:Int): Tournament {
        val tournament = api.getTournamentProfile(BaseRequest(
            procedure = API_TOURNAMENT,
            params = TournamentProfileRequest(
                sportId = sportId,
                tournamentId = tournamentId
            )
        ))
        return Tournament(
            title = tournament.nameRus,//TODO multilang
            icon = ImageUrlBuilder.getUrl(sportId,ImageUrlBuilder.Companion.Type.TOURNAMENT,tournamentId),
            placeholder = ImageUrlBuilder.getPlaceholder(sportId,ImageUrlBuilder.Companion.Type.TOURNAMENT),
            isFavorite = tournament.isFavorite
        )
    }
}