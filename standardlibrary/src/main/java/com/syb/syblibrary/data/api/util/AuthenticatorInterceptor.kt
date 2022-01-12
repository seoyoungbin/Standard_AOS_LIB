package com.syb.syblibrary.data.api.util

import io.reactivex.exceptions.Exceptions
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthenticatorInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val currentRequest = chain.request()

        val newRequest = currentRequest.newBuilder()
                .header("Content-Type", "application/json")
                .build()

        try {
            return chain.proceed(newRequest)
        } catch (e: IOException) {
            throw Exceptions.propagate(e)
        }
    }
}