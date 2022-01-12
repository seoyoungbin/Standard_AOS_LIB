package com.syb.syblibrary.data.api.util

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.net.HttpURLConnection

class RetryAfterInterceptor : Interceptor {

    private val logger = HttpLoggingInterceptor.Logger.DEFAULT

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        var response = chain.proceed(request)

        if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
            val retryAfter = parseRetryAfter(response)
            retryAfter?.let {
                logger.log("Retrying after $it seconds...")
                Thread.sleep((it * 1000).toLong())
                response = chain.proceed(request)
            }
        }

        return response
    }

    private fun parseRetryAfter(response: Response): Int? {
        val retryAfter = response.header("Retry-After")
        return retryAfter?.toInt()
    }
}