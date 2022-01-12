package com.syb.sample.ui.view.lloydweb.uri

import android.content.Context
import android.net.Uri
import android.webkit.WebView
import com.syb.compiler.customuri.generated.bind
import com.syb.compiler.customuri.generated.getScheme
import com.syb.annotations.webview.CustomUri
import com.syb.annotations.webview.HostInject
import com.syb.syblibrary.ui.view.web.uri.UriVisitor
import com.syb.sample.ui.view.lloydweb.E
import com.syb.sample.ui.view.lloydweb.LloydWebViewListener
import java.net.URLDecoder

@CustomUri("lloyd")
class LoginUriResolver : UriResolver
{

    @HostInject
    lateinit var logout: String

    private var mContext: Context
    private var listener: LloydWebViewListener

    companion object {
        @JvmStatic
        val TAG = "LoginUriResolver"
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
            if (uri.scheme.equals(getScheme())) {
                if (uri.authority.equals(logout))
                {
                    val redirectUrl = getSubStringParameter(urlDecode, E.REDIRECT_URL_PARAMETER)
                    if(!redirectUrl.isNullOrEmpty())
                        webView.loadUrl(E.baseUrl + redirectUrl)
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

    /**
     * redirectUrl 파라미터 값 가져오기
     * @param url 파싱할 URL
     * @param key 파싱할 KEY 값
     * @return 해당 key 파라미터 값
     */
    fun getSubStringParameter(url: String, key: String): String =
            try {
                val redirectUrlKey = key + "="
                val searchIndex = url.indexOf(redirectUrlKey)
                var redirectUrlParam = ""
                if (searchIndex > -1) {
                    val startIndex = searchIndex + redirectUrlKey.length
                    redirectUrlParam = url.substring(startIndex, url.length)
                }
                redirectUrlParam
            }catch (e: Exception)
            {
                e.printStackTrace()
                ""
            }

}