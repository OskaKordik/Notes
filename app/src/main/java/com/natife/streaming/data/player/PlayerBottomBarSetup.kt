package com.natife.streaming.data.player

import com.google.gson.Gson
import com.natife.streaming.data.matchprofile.Episode
import timber.log.Timber

data class PlayerBottomBarSetup(
    val playlist: List<Episode> = listOf(),
    val additionallyPlaylist: List<Episode>? = null,
)

fun PlayerSetup.toInitBottomData(): PlayerBottomBarSetup? {
    return when {
        currentPlaylist != null -> {
            Timber.tag("TAG").d(Gson().toJson(currentPlaylist))
            Timber.tag("TAG")
                .d(currentPlaylist.sortedWith(compareBy({ it.half }, { it.startMs })).map {
                    it.copy(
                        endMs = it.endMs * 1000,
                        startMs = it.startMs * 1000,
                        half = it.half - 1
                    )
                }.toString())
            PlayerBottomBarSetup(
                playlist = currentPlaylist.sortedWith(compareBy({ it.half }, { it.startMs })).map {
                    it.copy(
                        endMs = it.endMs * 1000,
                        startMs = it.startMs * 1000,
                        half = it.half - 1
                    )
                }
            )
        }
        currentEpisode != null -> {
            val timeList = this.video
                ?.filter { it.abc == "0" }
                ?.groupBy { it.quality }!!["720"]
                ?.mapIndexed { index, video ->
                    Timber.tag("TAG").d(video.toString())
                    Episode(
                        title = this.currentEpisode.title,
                        endMs = video.duration,
                        half = index,
                        startMs = 0,
                        image = this.currentEpisode.image,
                        placeholder = this.currentEpisode.placeholder
                    )
                }
            Timber.tag("TAG").d(timeList.toString())
            PlayerBottomBarSetup(
                playlist = timeList ?: listOf(),
                additionallyPlaylist = this.playlist.flatMap {
                    it.value
                }
            )
        }
        else -> null
    }
}

