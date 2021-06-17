package com.natife.streaming.usecase

import com.natife.streaming.api.MainApi
import com.natife.streaming.data.request.VideoRequest

interface GetLiveVideoUseCase {
    suspend fun execute(matchId: Int, sportId: Int): List<String>
}

class GetLiveVideoUseCaseImpl(private val api: MainApi) : GetLiveVideoUseCase {
    override suspend fun execute(matchId: Int, sportId: Int): List<String> {
        val video: List<String> = api.getVideoStream(VideoRequest(matchId, sportId))

        // TODO Надо понять что приходит и сделать правельный возврат
        return video
    }

}