package com.natife.streaming.data.dto.favorites.savedelete

import com.google.gson.annotations.SerializedName
import com.natife.streaming.data.dto.tournament.CountryDTO

data class InfoDTO(
    val country: CountryDTO,
    @SerializedName("name_eng")
    val nameEng: String,
    @SerializedName("name_rus")
    val nameRus: String
)