package com.natife.streaming.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.natife.streaming.*
import com.natife.streaming.api.MainApi
import com.natife.streaming.data.match.Match
import com.natife.streaming.data.match.Team
import com.natife.streaming.data.match.Tournament
import com.natife.streaming.data.request.*
import com.natife.streaming.datasource.MatchDataSource
import com.natife.streaming.datasource.MatchDataSourceFactory
import com.natife.streaming.datasource.MatchParams
import com.natife.streaming.ext.toRequest
import com.natife.streaming.mock.MockMatchRepository
import com.natife.streaming.utils.ImageUrlBuilder
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.util.*

interface MatchUseCase {
    suspend fun prepare(params: MatchParams)
    suspend fun load(type: Type = Type.SIMPLE): List<Match>
    fun executeFlow(pageSize: Int = 60): Flow<PagingData<Match>>
    enum class Type{
        SIMPLE,
        TOURNAMENT
    }
}

class MatchUseCaseImpl(
    private val matchDataSourceFactory: MatchDataSourceFactory,
    private val api: MainApi
) : MatchUseCase {

    private var list: List<Match> = listOf()
    private var gotEnd = false
    private var page = 0
    private var requestParams: MatchParams? = null

    override suspend fun prepare(
        params: MatchParams
    ) {
        if (params != requestParams) {
            requestParams = params
            page = 0
            gotEnd = false
            list = listOf()
        }
        Timber.e("prepared")
    }

    override suspend fun load(type: MatchUseCase.Type ): List<Match> {


        if (!gotEnd) {
            val mPrams = MatchesRequest(
                date = requestParams?.date ,
                limit = requestParams?.pageSize ?: 60,
                offset = (requestParams?.pageSize ?: 60) * page,
                sport = requestParams?.sportId,
                subOnly = requestParams?.subOnly ?: false,
                tournamentId = requestParams?.tournamentId
            )

            val matches = api.getMatches(
                BaseRequest(
                    procedure = when(type){
                        MatchUseCase.Type.SIMPLE->API_MATCHES
                        MatchUseCase.Type.TOURNAMENT -> API_TOURNAMENT_MATCHES
                    } ,
                    params = mPrams
                )
            )
            val sports = api.getSports(
                BaseRequest(
                    procedure = API_SPORTS,
                    params = EmptyRequest()
                )
            )
            val sportTranslate = api.getTranslate(
                BaseRequest(
                    procedure = API_TRANSLATE,
                    TranslateRequest(
                        language = "ru", //todo remove hardcode
                        params = sports.map { it.lexic }
                    )
                )
            )

            val previews = if(!matches.videoContent.broadcast.isNullOrEmpty()){
                api.getMatchPreview(body = PreviewRequest().apply {
                    matches.videoContent.broadcast?.map{PreviewRequestItem(it.id,it.sport!!)}?.let {
                        addAll(
                            it
                        )
                    }
                })
            }else{null}

        val data = matches.videoContent.broadcast?.map { match ->
                coroutineScope {
                    val profile = async {

                            api.getMatchProfile(
                                BaseRequest(
                                    procedure = API_MATCH_PROFILE, params = MatchProfileRequest(
                                        sportId = match.sport ?: requestParams?.sportId?:0,
                                        tournament = match.tournament?.id ?: requestParams?.tournamentId?:0
                                    )
                                )
                            )


                    }

                    val image = if (!match.hasVideo){
                        ImageUrlBuilder.getUrl(
                            match.sport?: requestParams?.sportId?:0,
                            ImageUrlBuilder.Companion.Type.TOURNAMENT, match.tournament?.id?:-1
                        )
                    }else{
                        previews?.find { it.matchId == match.id }?.preview ?: ImageUrlBuilder.getUrl(
                            match.sport ?: requestParams?.sportId?:0,
                            ImageUrlBuilder.Companion.Type.TOURNAMENT, match.tournament?.id?:-1
                        )
                    }

                    Match(
                        id = match.id,
                        sportId = match.sport?: requestParams?.sportId?:0,
                        sportName = sportTranslate[sports.find { it.id == match.sport }?.lexic.toString()]?.text  ?: "",
                        date = match.date,
                        tournament = Tournament(
                            match.tournament?.id?:-1,
                            match.tournament?.nameRus?:""
                        ),// todo multilang
                        team1 = Team(
                            match.team1.id,
                            match.team1.nameRus,
                            score = match.team1.score
                        ),
                        team2 = Team(
                            match.team2.id,
                            match.team2.nameRus,
                            score = match.team2.score
                        ),
                        info = "${profile.await()?.country?.name_rus} ${profile.await()?.nameRus}",
                        access = match.access,
                        hasVideo = match.hasVideo,
                        image = image,
                        placeholder = ImageUrlBuilder.getPlaceholder(
                            match.sport?: requestParams?.sportId?:0,
                            ImageUrlBuilder.Companion.Type.TOURNAMENT
                        ),
                        live = match.live,
                        storage = match.storage,
                        subscribed = match.sub
                    )
                }
            }

            val newList = mutableListOf<Match>()
            newList.addAll(this.list)
            newList.addAll(data ?: emptyList())
            this.list = newList

            page++
        }

        return list

    }



    @Deprecated("Use method above")
    override fun executeFlow(pageSize: Int): Flow<PagingData<Match>> {
        return Pager(PagingConfig(pageSize = pageSize)) {
            matchDataSourceFactory.invoke()
        }.flow
    }

}

