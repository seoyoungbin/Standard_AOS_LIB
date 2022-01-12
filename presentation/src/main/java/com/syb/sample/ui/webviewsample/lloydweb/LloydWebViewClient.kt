package com.syb.sample.ui.view.lloydweb

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.webkit.WebView
import com.syb.syblibrary.ui.view.web.BaseWebView
import com.syb.syblibrary.ui.view.web.BaseWebViewClient
import com.syb.syblibrary.util.log.YLog
import com.syb.sample.ui.view.lloydweb.uri.ChainUriResolver
import java.net.URLDecoder

class LloydWebViewClient : BaseWebViewClient {

    private var mChainUriResolver: ChainUriResolver
    private var mListener: LloydWebViewListener

    constructor(context: Context, webView: LloydWebView, listener: LloydWebViewListener) {
        mChainUriResolver = ChainUriResolver(context, webView, listener)
        mListener = listener
    }

    override fun handleOverrideUrlLoading(
        webView: WebView?,
        uri: Uri?,
        scheme: String,
        host: String,
        path: String
    ): Boolean {
        try {
            val url = uri.toString()

            YLog.i("SCHEMEURL ==> ${URLDecoder.decode(url)}")
            // 간편인증앱 예외처리.
            if (url.startsWith("about:blank")) {
                (webView!! as LloydWebView).loadUrl(E.MAIN_URL)
                return true
            }

            return mChainUriResolver.startUriVisitor(uri!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        mListener.onPageStart(view, url)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        if(view is BaseWebView) {
            YLog.i("LloydWebView searchCookie => " + view.searchCookie("JSESSIONID"))
            YLog.i("LloydWebView getForwardUrl => " + view.getForwardUrl(1))
            YLog.i("LloydWebView getBackForwardUri => " + view.getBackForwardUri(1))
            view.saveSharedCookie("JSESSIONID")
            YLog.i("LloydWebView getSharedCookie => " + view.getSharedCookie("JSESSIONID"))
        }
        mListener.onPageFinish(view, url)
    }
}