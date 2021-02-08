package com.natife.streaming.data.dto.player

import com.google.gson.annotations.SerializedName

data class PlayerDTO(
    @SerializedName("c_gender")
    val gender: Int,
    @SerializedName("club_team")
    val clubTeam: ClubTeamDTO,
    @SerializedName("firstname_eng")
    val firstnameEng: String,
    @SerializedName("firstname_rus")
    val firstnameRus: String,
    val height: Int,
    val id: Int,
    @SerializedName("is_favorite")
    val isFavorite: Boolean,
    @SerializedName("is_gk")
    val isGk: Boolean,
    @SerializedName("lastname_eng")
    val lastnameEng: String,
    @SerializedName("lastname_rus")
    val lastnameRus: String,
    @SerializedName("nickname_eng")
    val nicknameEng: String,
    @SerializedName("nickname_rus")
    val nicknameRus: String,
    val weight: Int
)