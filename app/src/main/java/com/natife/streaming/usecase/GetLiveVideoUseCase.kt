package com.natife.streaming.usecase

import android.util.Log
import com.natife.streaming.api.MainApi
import com.natife.streaming.data.request.VideoRequest

interface GetLiveVideoUseCase {
    suspend fun execute(matchId: Int, sportId: Int): List<String>
}

class GetLiveVideoUseCaseImpl(private val api: MainApi) : GetLiveVideoUseCase {
    override suspend fun execute(matchId: Int, sportId: Int): List<String> {
        val response = api.getVideoStream(VideoRequest(matchId, sportId))
        Log.d("MyLog", "File: ${response.body()?.byteStream()}")

        // TODO Надо понять что приходит и сделать правельный возврат
        return listOf()
    }

}