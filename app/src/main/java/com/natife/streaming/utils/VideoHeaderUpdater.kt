package com.natife.streaming.utils

import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.natife.streaming.preferenses.AuthPrefs
import kotlinx.coroutines.*
import timber.log.Timber

private const val TIME_UPDATE_MS = 10000L

interface VideoHeaderUpdater {
    fun start(mediaDataSourceFactory: DefaultHttpDataSourceFactory)
    fun stop()
}

class VideoHeaderUpdaterImpl(
    private val authPrefs: AuthPrefs
) : VideoHeaderUpdater {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var videoHeaderJob: Job? = null

    override fun start(mediaDataSourceFactory: DefaultHttpDataSourceFactory) {
        if (videoHeaderJob == null) {
            videoHeaderJob = scope.launch {
                while (true) {
                    val authHeaders: HashMap<String, String> = HashMap()
                    authHeaders["Cookie"] = authPrefs.getAuthToken() ?: ""
                    mediaDataSourceFactory.defaultRequestProperties.clearAndSet(
                        authHeaders
                    )
//                    Timber.tag("VideoHeaderUpdater").d("authHeaders=$authHeaders")
                    delay(TIME_UPDATE_MS)
                }
            }
        }
    }

    override fun stop() {
        videoHeaderJob?.cancel()
        videoHeaderJob = null
    }
}