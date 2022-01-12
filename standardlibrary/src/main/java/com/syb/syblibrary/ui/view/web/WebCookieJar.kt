package com.syb.syblibrary.ui.view.web

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import android.webkit.CookieManager
import java.util.*
import kotlin.collections.ArrayList

class WebCookieJar : CookieJar
{

    private val cookieManager: CookieManager = CookieManager.getInstance()

    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
        val urlString = url.toString()

        cookies.forEach {
            cookieManager.setCookie(urlString, it.toString())
        }
    }

    override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
        val urlString = url.toString()
        val cookieString = cookieManager.getCookie(urlString)

        if(!cookieString.isNullOrEmpty())
        {
            val cookieHeaders = cookieString.split(":")
            val cookies = ArrayList<Cookie>(cookieHeaders.size)

            cookieHeaders.forEach { sCookie ->
                Cookie.parse(url, sCookie)?.let { cookie ->
                    cookies.add(cookie)
                }
            }
            return cookies
        }

        return Collections.emptyList()
    }
}