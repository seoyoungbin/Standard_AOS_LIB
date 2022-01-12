package com.syb.syblibrary.ui.view.web.impl

import android.webkit.WebView
import androidx.annotation.NonNull
import androidx.annotation.Nullable

/**
 * 웹뷰에서 쿠키 이빈트를 받기 위한 interface
 */
interface CookieEventSearcherListener
{
    fun getCookieSearcherKey(): String

    fun cookieDelivery(@NonNull webView: WebView, @Nullable cookie: String?, @NonNull searchKey: String)
}