package com.natife.streaming.data.dto

data class VideoDTOItem(
    val abc: String,
    val abc_type: String,
    val checksum: Long,
    val duration: Long,
    val folder: String,
    val fps: Int,
    val match_id: Int,
    val name: String,
    val period: Int,
    val quality: String,
    val server_id: Int,
    val size: Long,
    val start_ms: Long,
    val url: String,
    val url_root: String,
    val video_type: String
)