package com.syb.sample.ui.view.lloydweb

import android.webkit.WebView
import androidx.annotation.NonNull
import com.syb.syblibrary.ui.base.view.BaseActivity
import com.syb.syblibrary.ui.view.web.BaseWebChromeClient

class LloydWebViewChromeClient : BaseWebChromeClient {

    private var mListener: LloydWebViewListener ?= null
    private var activity: BaseActivity? = null

    constructor(@NonNull webView: LloydWebView, @NonNull activity: BaseActivity, listener: LloydWebViewListener) : super(webView, activity) {
        this.activity = activity
        mListener = listener
    }

    override fun onProgressChanged(webView: WebView?, newProgress: Int) {
        super.onProgressChanged(webView, newProgress)
        mListener?.onProgressChanged(webView!!, newProgress)
    }

}