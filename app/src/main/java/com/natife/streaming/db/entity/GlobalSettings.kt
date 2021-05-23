package com.natife.streaming.db.entity

import androidx.room.Entity


@Entity(primaryKeys = ["authEmail"])
data class GlobalSettings(
    val authEmail: String,
    val showScore: Boolean,
    val lang: Lang
)

enum class Lang(code: String) {
    RU("ru"), EN("en")
}
