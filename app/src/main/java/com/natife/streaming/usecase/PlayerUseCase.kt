package com.natife.streaming.usecase

import android.content.Context
import com.natife.streaming.API_PLAYER_INFO
import com.natife.streaming.R
import com.natife.streaming.api.MainApi
import com.natife.streaming.data.dto.player.PlayerDTO
import com.natife.streaming.data.matchprofile.Player
import com.natife.streaming.data.request.BaseRequest
import com.natife.streaming.data.request.PlayerInfoRequest
import com.natife.streaming.utils.ImageUrlBuilder

interface PlayerUseCase {
    suspend fun execute(sportId: Int, playerId: Int): Player
}

class PlayerUseCaseImpl(
    private val api: MainApi,
    private val context: Context
) : PlayerUseCase {
    override suspend fun execute(sportId: Int, playerId: Int): Player {
        val player: PlayerDTO = api.getPlayerInfo(
            BaseRequest(
                procedure = API_PLAYER_INFO,
                params = PlayerInfoRequest(sportId, playerId)
            )
        )
        return Player(
            id = player.id,
            image = ImageUrlBuilder.getUrl(
                sportId,
                ImageUrlBuilder.Companion.Type.PLAYER,
                player.id ?: -1
            ),
            imagePlaceholder = ImageUrlBuilder.getPlaceholder(
                sportId,
                ImageUrlBuilder.Companion.Type.PLAYER
            ),
            name = when (context.resources.getString(R.string.lang)) {
                "en", "EN" -> "${player.firstNameEng} ${player.lastNameEng}" ?: ""
                "ru", "RU" -> "${player.firstNameRus} ${player.lastNameRus}" ?: ""
                else -> "${player.firstNameEng} ${player.lastNameEng}" ?: ""
            },
            nickname = when (context.resources.getString(R.string.lang)) {
                "en", "EN" -> "${player.nicknameEng}" ?: ""
                "ru", "RU" -> "${player.nicknameRus}" ?: ""
                else -> "${player.firstNameEng} ${player.lastNameEng}" ?: ""
            },
            number = "0",
            team = 0,
            isFavorite = player.isFavorite,
            countryId = player.country?.id ?: -1,
            countryName = when (context.resources.getString(R.string.lang)) {
                "en", "EN" -> player.country.nameEng ?: ""
                "ru", "RU" -> player.country.nameRus ?: ""
                else -> player.country.nameEng ?: ""
            }
        )
    }

}