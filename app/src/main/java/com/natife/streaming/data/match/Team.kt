package com.natife.streaming.data.match

import com.google.gson.annotations.SerializedName

data class Team(
    @SerializedName("id") val id: Int,
    @SerializedName("name_rus") val nameRus: String,
    @SerializedName("name_eng") val nameEng: String,
    @SerializedName("short_name_rus") val shortNameRus: String?,
    @SerializedName("short_name_eng") val shortNameEng: String?,
    @SerializedName("score") val score: Int
)