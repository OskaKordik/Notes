package com.natife.streaming.data.player

import com.natife.streaming.data.matchprofile.Episode

data class PlayerBottomBarSetup(
    val playlist: List<Episode> = listOf(),
    val additionallyPlaylist: List<Episode>? = null,
)

fun PlayerSetup.toInitBottomData(): PlayerBottomBarSetup? {
    var endTimeEpisode = 0L
    var startTimeEpisode = 0L
    return when {
        currentPlaylist != null -> {
//            Timber.tag("TAG").d(Gson().toJson(currentPlaylist))
//            Timber.tag("TAG")
//                .d(currentPlaylist.sortedWith(compareBy({ it.half }, { it.start })).map {
//                    it.copy(end = it.end * 1000, start = it.start * 1000, half = it.half-1)
//                }.toString())
            PlayerBottomBarSetup(
                playlist = currentPlaylist.sortedWith(compareBy({ it.half }, { it.start })).map {
                    it.copy(end = it.end * 1000, start = it.start * 1000, half = it.half - 1)
                }
            )
        }
        currentEpisode != null -> {
            val timeList = this.video
                ?.filter { it.abc == "0" }
                ?.groupBy { it.quality }!!["720"]
                ?.map { video ->
//                Timber.tag("TAG").d(video.toString())
                    startTimeEpisode += endTimeEpisode
                    endTimeEpisode += (video.duration)
                    Episode(
                        title = this.currentEpisode.title,
                        end = endTimeEpisode - startTimeEpisode,
                        half = (video.period) - 1,
                        start = 0,
                        image = this.currentEpisode.image,
                        placeholder = this.currentEpisode.placeholder
                    )
                }
//            Timber.tag("TAG").d(timeList.toString())
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

