package com.natife.streaming.api

import com.natife.streaming.data.dto.LoginDTO
import com.natife.streaming.data.dto.account.AccountDTO
import com.natife.streaming.data.request.BaseRequest
import com.natife.streaming.data.request.EmptyRequest
import com.natife.streaming.data.request.RequestLogin
import retrofit2.http.Body
import retrofit2.http.POST

interface MainApi {

    @POST("data")
    suspend fun login(@Body body: BaseRequest<RequestLogin>): LoginDTO
    @POST("data")
    suspend fun getProfile(@Body body: BaseRequest<EmptyRequest>): AccountDTO
}