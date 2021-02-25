package com.natife.streaming.usecase

import com.natife.streaming.API_TEAM_INFO
import com.natife.streaming.api.MainApi
import com.natife.streaming.data.request.BaseRequest
import com.natife.streaming.data.request.TeamRequest
import com.natife.streaming.data.search.SearchResult
import com.natife.streaming.preferenses.SettingsPrefs
import com.natife.streaming.utils.ImageUrlBuilder

interface TeamUseCase {
    suspend fun execute(sportId: Int, teamId:Int):  SearchResult
}

class TeamUseCaseImpl(private val api: MainApi, private val settingsPrefs: SettingsPrefs): TeamUseCase {
    override suspend fun execute(sportId: Int, teamId: Int):  SearchResult {
       val team = api.getTeamInfo(BaseRequest(procedure = API_TEAM_INFO, params = TeamRequest(sportId,teamId)))
        return SearchResult(id = team.id,
            image = ImageUrlBuilder.getUrl(
                sportId,
                ImageUrlBuilder.Companion.Type.TEAM,
                team.id ?: -1
            ),
            placeholder = ImageUrlBuilder.getPlaceholder(sportId, ImageUrlBuilder.Companion.Type.TEAM),
            name = if (settingsPrefs.getLanguage().contains("ru",true)){
                team.nameRus
            }else{
                team.nameEng
            },
            gender = 0,
            sport = sportId,
            type = SearchResult.Type.TEAM,
        isFavorite = team.isFavorite)
    }

}