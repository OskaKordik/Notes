package com.natife.streaming.api

import com.natife.streaming.data.dto.LoginDTO
import com.natife.streaming.data.dto.VideoDTO
import com.natife.streaming.data.dto.account.AccountDTO
import com.natife.streaming.data.dto.actions.ActionsDTO
import com.natife.streaming.data.dto.match.MatchesDTO
import com.natife.streaming.data.dto.matchprofile.MatchInfoDTO
import com.natife.streaming.data.dto.matchprofile.MatchInfoDataDTO
import com.natife.streaming.data.dto.matchprofile.MatchProfileDTO
import com.natife.streaming.data.dto.sports.SportsDTO
import com.natife.streaming.data.dto.tournament.TournamentListDTO
import com.natife.streaming.data.dto.translate.TranslatesDTO
import com.natife.streaming.data.request.*
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

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

    @POST("data/{sport}")
    suspend fun getMatchInfo(@Body body: BaseRequest<BaseParams>,@Path("sport") sport: String): MatchInfoDTO

    @POST("videoapi")
    suspend fun getVideoFile(@Body body: VideoRequest): VideoDTO
    //@POST("video/stream")
    //suspend fun getVideoStream(@Body body: BaseRequest<VideoRequest>): ???

    @POST("data/{sport}")
    suspend fun getActions(@Body body: BaseRequest<BaseParams>,@Path("sport") sport: String): ActionsDTO
}