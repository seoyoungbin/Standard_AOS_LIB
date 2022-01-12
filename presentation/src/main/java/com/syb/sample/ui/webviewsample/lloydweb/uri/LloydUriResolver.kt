package com.syb.sample.ui.view.lloydweb.uri

import android.content.Context
import android.net.Uri
import android.webkit.WebView
import com.syb.compiler.customuri.generated.bind
import com.syb.compiler.customuri.generated.getScheme
import com.syb.annotations.webview.CustomUri
import com.syb.annotations.webview.HostInject
import com.syb.sample.ui.view.lloydweb.LloydWebViewListener
import com.syb.syblibrary.ui.view.web.uri.UriVisitor
import java.net.URLDecoder

@CustomUri("lloyd")
class LloydUriResolver : UriResolver
{
    @HostInject
    lateinit var closePopup: String

    @HostInject
    lateinit var slideClose: String

    @HostInject
    lateinit var buy: String

    @HostInject
    lateinit var present: String

    @HostInject
    lateinit var cart: String

    private var mContext: Context
    private var listener: LloydWebViewListener

    companion object {
        @JvmStatic
        val TAG = "LloydUriResolver"
    }

    constructor(context: Context, listener: LloydWebViewListener)
    {
        this.mContext = context
        this.listener = listener
        bind(this)
    }

    override fun accept(visitor: UriVisitor): Boolean {
        val url = visitor.getUrl()
        val webView = visitor.webView
        return handle(webView, url)
    }

    private fun handle(webView: WebView, url: String): Boolean
    {
        try {
            val urlDecode = URLDecoder.decode(url)
            var uri = Uri.parse(urlDecode)
            if (uri.scheme.equals(getScheme()))
            {
                if (uri.authority.equals(closePopup) || uri.authority.equals(slideClose))
                {
                    listener.closePopup()
                    return true
                }
                else if(uri.authority.equals(buy) || uri.authority.equals(present) || uri.authority.equals(cart))
                {
                    listener.moveMainWebView(uri)
                    return true
                }
            }
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
        return false
    }


    override fun getId(): String = TAG
}