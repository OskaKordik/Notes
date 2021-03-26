package com.natife.streaming.data.dto

import com.google.gson.annotations.SerializedName

data class VideoDTOItem(
    val abc: String,
    @SerializedName("abc_type")
    val abcType: String?,
    val checksum: Long,
    val duration: Long,
    val folder: String,
    val fps: Int,
    @SerializedName("match_id")
    val matchId: Int,
    val name: String,
    val period: Int,
    val quality: String,
    @SerializedName("server_id")
    val serverId: Int,
    val size: Long,
    @SerializedName("start_ms")
    val startMs: Long,
    val url: String,
    @SerializedName("url_root")
    val urlRoot: String,
    @SerializedName("video_type")
    val videoType: String
)
