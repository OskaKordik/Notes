package com.natife.streaming.api.interceptor

import com.natife.streaming.TOKEN_FIELD_NAME
import com.natife.streaming.preferenses.AuthPrefs
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.KoinComponent
import timber.log.Timber

class AuthInterceptor(private val authPrefs: AuthPrefs) : Interceptor, KoinComponent {
    override fun intercept(chain: Interceptor.Chain): Response {
        synchronized(this) {

            val original = chain.request()
            val builder = original.newBuilder()

            authPrefs.getAuthToken()?.let { token ->
                builder.addHeader(TOKEN_FIELD_NAME, token)
            }
            val response = chain.proceed( builder.build())
             response.header(TOKEN_FIELD_NAME)?.let{
                 Timber.e("token $it")
                 authPrefs.saveAuthToken(it)
             }

            return response

        }
    }
}