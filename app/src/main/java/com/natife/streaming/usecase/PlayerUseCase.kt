package com.natife.streaming.usecase

import com.natife.streaming.API_PLAYER_INFO
import com.natife.streaming.api.MainApi
import com.natife.streaming.data.matchprofile.Player
import com.natife.streaming.data.request.BaseRequest
import com.natife.streaming.data.request.PlayerInfoRequest
import com.natife.streaming.preferenses.MatchSettingsPrefs
import com.natife.streaming.preferenses.SettingsPrefs
import com.natife.streaming.utils.ImageUrlBuilder

interface PlayerUseCase {
    suspend fun execute(sportId: Int, playerId:Int): Player
}

class PlayerUseCaseImpl(private val api: MainApi, private val settingsPrefs: SettingsPrefs): PlayerUseCase{
    override suspend fun execute(sportId: Int, playerId: Int): Player {
        val player =  api.getPlayerInfo(BaseRequest(procedure = API_PLAYER_INFO, params = PlayerInfoRequest(sportId, playerId)))
        return Player(
            id = player.id,
            image = ImageUrlBuilder.getUrl(
                sportId,
                ImageUrlBuilder.Companion.Type.PLAYER,
                player.id ?: -1
            ),
            imagePlaceholder = ImageUrlBuilder.getPlaceholder(sportId, ImageUrlBuilder.Companion.Type.PLAYER),
            name = if (settingsPrefs.getLanguage().contains("ru",true)){
                "${player.firstnameRus} ${player.lastnameRus}"
            }else{
                "${player.firstnameEng} ${player.lastnameEng}"
            },
            number = "0",
            team = 0,
            isFavorite = player.isFavorite

        )
    }

}