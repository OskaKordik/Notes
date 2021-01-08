package com.natife.streaming.data.match

import com.google.gson.annotations.SerializedName

data class Tournament(
    @SerializedName("id") val id: Int,
    @SerializedName("name_rus") val nameRus: String,
    @SerializedName("name_eng") val nameEng: String
)

