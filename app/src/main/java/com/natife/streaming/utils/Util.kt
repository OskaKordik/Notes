package com.natife.streaming.utils

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever


object Util {
    @Throws(Throwable::class)
    fun getThumbnailFromVideo(videoPath: String?,time: Long): Bitmap? {
        var bitmap: Bitmap? = null
        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        try {
            mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(videoPath, HashMap())
            bitmap = mediaMetadataRetriever.getFrameAtTime(time)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Throwable("Exception in getThumbnailFromVideo(String videoPath)" + e.message)
        } finally {
            mediaMetadataRetriever?.release()
        }
        return bitmap
    }
}