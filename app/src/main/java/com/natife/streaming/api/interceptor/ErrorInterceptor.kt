package com.natife.streaming.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class ErrorInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val resp = chain.proceed(builder.build())

        //TODO Handle http errors

        return resp
    }
}