package com.natife.streaming.usecase

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.natife.streaming.api.MainApi
import com.natife.streaming.api.exception.ApiException
import com.natife.streaming.data.request.VideoRequest
import com.natife.streaming.preferenses.AuthPrefs
import com.natife.streaming.utils.VideoHeaderUpdater
import timber.log.Timber
import java.lang.StringBuilder

interface GetLiveVideoUseCase {
    suspend fun execute(matchId: Int, sportId: Int): MediaSource
}

class GetLiveVideoUseCaseImpl(
    private val context: Context,
    private val api: MainApi,
    private val authPrefs: AuthPrefs,
    private val videoHeaderUpdater: VideoHeaderUpdater
) : GetLiveVideoUseCase {
    override suspend fun execute(matchId: Int, sportId: Int): MediaSource {
//        val response = api.getVideoStream(VideoRequest(matchId, sportId))
//        Log.d("GetLiveVideoUseCase", "File: ${response.body()?.byteStream()}")
        val accessToken = authPrefs.getAuthToken() ?: ""
//        Timber.tag("GetLiveVideoUseCase").d("accessToken=$accessToken")

        val urlLive = StringBuilder()
        urlLive.append("https://api.instat.tv/video/stream/")
        urlLive.append(sportId)
        urlLive.append("/")
        urlLive.append(matchId)
        urlLive.append(".m3u8")

        val authHeaders: HashMap<String, String> = HashMap()
        authHeaders["Cookie"] = accessToken

//        val uri: Uri = Uri.parse(urlLive.toString())
        // test URI
        val uri: Uri = Uri.parse("https://moctobpltc-i.akamaihd.net/hls/live/571329/eight/playlist.m3u8")

        val mediaDataSourceFactory = DefaultHttpDataSourceFactory(
            Util.getUserAgent(context, "InstatTV"),
            1000,
            1000,
            true
        )
        mediaDataSourceFactory.defaultRequestProperties.set(authHeaders)


        return try {
            videoHeaderUpdater.start(mediaDataSourceFactory)
            HlsMediaSource.Factory(mediaDataSourceFactory)
//            .setLoadErrorHandlingPolicy(NoStuckPolicy())
                .setAllowChunklessPreparation(true)
                .createMediaSource(uri)
        } catch (e: ApiException) {
            videoHeaderUpdater.stop()
            throw Exception(e)
        }
    }

}