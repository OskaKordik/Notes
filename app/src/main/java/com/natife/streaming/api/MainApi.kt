package com.natife.streaming.api

import com.natife.streaming.data.dto.*
import com.natife.streaming.data.dto.account.AccountDTO
import com.natife.streaming.data.dto.actions.ActionsDTO
import com.natife.streaming.data.dto.favorites.FavoritesDTO
import com.natife.streaming.data.dto.favorites.savedelete.SaveDeleteDTO
import com.natife.streaming.data.dto.match.BroadcastDTO
import com.natife.streaming.data.dto.match.MatchesDTO
import com.natife.streaming.data.dto.matchprofile.MatchInfoDTO
import com.natife.streaming.data.dto.matchprofile.MatchProfileDTO
import com.natife.streaming.data.dto.matchprofile.ProfileColorDTO
import com.natife.streaming.data.dto.player.PlayerDTO
import com.natife.streaming.data.dto.register.RegisterDTO
import com.natife.streaming.data.dto.register.RequestRegister
import com.natife.streaming.data.dto.search.SearchResultDTO
import com.natife.streaming.data.dto.sports.SportsDTO
import com.natife.streaming.data.dto.team.TeamDTO
import com.natife.streaming.data.dto.tournament.TournamentListDTO
import com.natife.streaming.data.dto.tournamentprofile.TournamentProfileDTO
import com.natife.streaming.data.dto.translate.TranslatesDTO
import com.natife.streaming.data.dto.videoPosition.GetVideoPositionRequest
import com.natife.streaming.data.dto.videoPosition.StoreVideoPositionRequest
import com.natife.streaming.data.dto.videoPosition.StoreVideoPositionResponse
import com.natife.streaming.data.request.*
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url

interface MainApi {

    @POST("data")
    suspend fun login(@Body body: BaseRequest<RequestLogin>): LoginDTO

    @POST("data")
    suspend fun makeRegister(@Body body: BaseRequest<RequestRegister>): RegisterDTO

    @POST("data")
    suspend fun getProfile(@Body body: BaseRequest<EmptyRequest>): AccountDTO

    @POST("data")
    suspend fun getMatches(@Body body: BaseRequest<MatchRequest>): MatchesDTO

    @POST("data")
    suspend fun getSports(@Body body: BaseRequest<EmptyRequest>): SportsDTO

    @POST("data")
    suspend fun getMatchProfile(@Body body: BaseRequest<MatchProfileRequest>): MatchProfileDTO

    @POST("data")
    suspend fun getTournamentList(@Body body: BaseRequest<TournamentsRequest>): TournamentListDTO

    @POST("data")
    suspend fun getTranslate(@Body body: BaseRequest<TranslateRequest>): TranslatesDTO //new

    @POST
    suspend fun getProfileColor(
        @Url url: String = "https://api-staging.instat.tv/profile/color",
        @Body body: ProfileColorRequest
    ): ProfileColorDTO //new

    @POST("data")
    suspend fun saveUserLiveMatchSecond(@Body body: BaseRequest<StoreVideoPositionRequest>): StoreVideoPositionResponse // new

    @POST("data")
    suspend fun getUserLiveMatchSecond(@Body body: BaseRequest<GetVideoPositionRequest>): StoreVideoPositionResponse // new


    @POST("data/{sport}")
    suspend fun getMatchInfo(
        @Body body: BaseRequest<BaseParams>,
        @Path("sport") sport: String
    ): MatchInfoDTO

    @POST("videoapi")
    suspend fun getVideoFile(@Body body: VideoRequest): VideoDTO

    @POST("video/stream")
    suspend fun getVideoStream(@Body body: VideoRequest): List<String> // todo https://api.instat.tv/video/stream

    @POST("data/{sport}")
    suspend fun getActions(
        @Body body: BaseRequest<BaseParams>,
        @Path("sport") sport: String
    ): ActionsDTO

    @POST("data")
    suspend fun getTournamentProfile(@Body body: BaseRequest<TournamentProfileRequest>): TournamentProfileDTO

    @POST("data")
    suspend fun search(@Body body: BaseRequest<SearchRequest>): SearchResultDTO

    @POST("data")
    suspend fun getMatchInfoGlobal(@Body body: BaseRequest<MatchInfo>): BroadcastDTO

    @POST
    suspend fun getMatchPreview(
        @Url url: String = "https://api-staging.instat.tv/videoapi/preview",
        @Body body: PreviewRequest
    ): PreviewDTO

    @POST("data")
    suspend fun getSecond(@Body body: BaseRequest<MatchInfo>): SecondDTO

    @POST("data/{sport}")
    suspend fun getPlayerActions(
        @Body body: BaseRequest<PlayerActionsRequest>,
        @Path("sport") sport: String
    ): PlayerEpisodesDTO

    @POST("data")
    suspend fun getPlayerInfo(@Body body: BaseRequest<PlayerInfoRequest>): PlayerDTO

    @POST("data")
    suspend fun getSaveDeleteFavorite(@Body body: BaseRequest<SaveDeleteFavoriteRequest>): SaveDeleteDTO

    @POST("data")
    suspend fun getFavorite(@Body body: BaseRequest<EmptyRequest>): FavoritesDTO

    @POST("data")
    suspend fun getTeamInfo(@Body body: BaseRequest<TeamRequest>): TeamDTO
}