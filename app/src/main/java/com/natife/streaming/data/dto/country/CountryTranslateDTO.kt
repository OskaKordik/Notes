package com.natife.streaming.data.dto.country

import java.util.*


data class CountryTranslateDTO(
    val id: Int,
    val name: String,
)

fun CountryDTO.toCountryTranslateDTO(lang: String): CountryTranslateDTO {
    return CountryTranslateDTO(
        id = this.id,
        name = when (lang) {
            "en" -> this.nameEng ?:""
            "ru" -> this.nameRus ?:""
            else -> this.nameEng ?:""
        }
    )
}