package com.natife.streaming.api

import com.natife.streaming.data.dto.LoginDTO
import com.natife.streaming.data.dto.account.AccountDTO
import com.natife.streaming.data.dto.match.MatchesDTO
import com.natife.streaming.data.dto.match.TournamentDTO
import com.natife.streaming.data.dto.matchprofile.MatchProfileDTO
import com.natife.streaming.data.dto.sports.SportsDTO
import com.natife.streaming.data.dto.tournament.TournamentListDTO
import com.natife.streaming.data.dto.tournamentprofile.TournamentProfileDTO
import com.natife.streaming.data.dto.translate.TranslatesDTO
import com.natife.streaming.data.request.*
import retrofit2.http.Body
import retrofit2.http.POST

interface MainApi {

    @POST("data")
    suspend fun login(@Body body: BaseRequest<RequestLogin>): LoginDTO
    @POST("data")
    suspend fun getProfile(@Body body: BaseRequest<EmptyRequest>): AccountDTO
    @POST("data")
    suspend fun getMatches(@Body body: BaseRequest<MatchesRequest>): MatchesDTO
    @POST("data")
    suspend fun getSports(@Body body: BaseRequest<EmptyRequest>): SportsDTO
    @POST("data")
    suspend fun getMatchProfile(@Body body: BaseRequest<MatchProfileRequest>): MatchProfileDTO
    @POST("data")
    suspend fun getTournamentList(@Body body: BaseRequest<TournamentsRequest>): TournamentListDTO
    @POST("data")
    suspend fun getTranslate(@Body body: BaseRequest<TranslateRequest>): TranslatesDTO
    @POST("data")
    suspend fun getTournamentProfile(@Body body: BaseRequest<TournamentProfileRequest>): TournamentProfileDTO
}