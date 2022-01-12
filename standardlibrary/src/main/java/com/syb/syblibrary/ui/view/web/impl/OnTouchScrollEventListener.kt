package com.syb.syblibrary.ui.view.web.impl

import android.webkit.WebView
import androidx.annotation.NonNull

interface OnTouchScrollEventListener
{
    fun webViewOnTouchScrollDown(@NonNull webView: WebView, touchY: Float, webViewScrollMove: Boolean)

    fun webViewOnTouchScrollUp(@NonNull webView: WebView, touchY: Float, webViewScrollMove: Boolean)
}