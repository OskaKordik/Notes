package com.natife.streaming.data.dto

import com.google.gson.annotations.SerializedName

data class VideoDTO(
    @SerializedName("name") val name: String?,
    @SerializedName("match_id") val matchId: Long?,
    @SerializedName("period") val period: Int?,
    @SerializedName("server_id") val serverId: Long?,
    @SerializedName("quality") val quality: String?,
    @SerializedName("folder") val folder: String?,
    @SerializedName("video_type") val videoType: String?,
    @SerializedName("abc") val abc: String?,
    @SerializedName("start_ms") val startMs: Long?,
    @SerializedName("checksum") val checkSum: Long?,
    @SerializedName("size") val size: Long?,
    @SerializedName("abc_type") val abcType: String?,
    @SerializedName("duration") val duration: Long?,
    @SerializedName("fps") val fps: Int?,
    @SerializedName("url_root") val urlRoot: String?,
    @SerializedName("url") val url: String?
)

