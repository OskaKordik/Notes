package com.natife.streaming.data.request

import com.google.gson.annotations.SerializedName

data class RequestLogin(
    @SerializedName("_p_email")
    val email: String,
    @SerializedName("_p_password")
    val password: String
)
