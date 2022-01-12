package com.syb.syblibrary.ui.view.web.impl

import android.webkit.WebView

interface BaseWebViewListener {

    fun onPageStart(view: WebView?, url: String?)

    fun onPageFinish(view: WebView?, url: String?)

    fun onProgressChanged(webView: WebView, progress: Int)

    fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String)

    fun webViewScrollChange(l: Int, t: Int, oldl: Int, oldt: Int)

}