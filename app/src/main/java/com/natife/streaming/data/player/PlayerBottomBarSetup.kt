package com.natife.streaming.data.player

import com.natife.streaming.data.matchprofile.Episode
import timber.log.Timber

data class PlayerBottomBarSetup(
    val playlist: List<Episode> = listOf(),
    val additionallyPlaylist: List<Episode>? = null,
)

fun PlayerSetup.toInitBottomData(): PlayerBottomBarSetup? {
    return when {
        currentPlaylist != null -> {
            var currentHalf = 0
            var tempHalf = 0
            Timber.tag("TAG")
                .d(currentPlaylist.sortedWith(compareBy({ it.half }, { it.startMs })).map {
                    tempHalf = if (tempHalf == it.half - 1) tempHalf else {
                        currentHalf += 1
                        it.half - 1
                    }
                    it.copy(
                        endMs = it.endMs * 1000,
                        startMs = it.startMs * 1000,
                        half = currentHalf
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
                ?.sortedBy { it.period }
                ?.mapIndexed { index, video ->
//                    Timber.tag("TAG").d(video.toString())
                    Episode(
                        title = this.currentEpisode.title,
                        endMs = video.duration,
                        half = video.period - 1,//  TODO иногда приходят не правильные данные
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

