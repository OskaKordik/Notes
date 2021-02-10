package com.natife.streaming.data.request

import com.google.gson.annotations.SerializedName

data class BaseRequest<T>(
    @SerializedName("proc")
    val procedure: String,
    val params: T
)
