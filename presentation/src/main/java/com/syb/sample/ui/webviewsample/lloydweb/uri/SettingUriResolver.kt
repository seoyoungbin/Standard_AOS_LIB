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
import com.syb.syblibrary.ui.view.web.WebUtil
import java.net.URLDecoder

@CustomUri("lloyd")
class SettingUriResolver : UriResolver
{

    @HostInject
    lateinit var setting: String

    private var mContext: Context
    private var listener: LloydWebViewListener

    companion object {
        @JvmStatic
        val TAG = "SettingUriResolver"
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
            if(uri.scheme.equals(getScheme()) && uri.authority.equals(setting))
            {
                val adPush = uri.getQueryParameter(E.ADPUSH_PARAMETER)
                val pbpId = WebUtil.paramSubstitution(uri.getQueryParameter(E.PBPID_PARAMETER))
                if(!adPush.isNullOrEmpty() && !pbpId.isNullOrEmpty())
                    listener?.pushSetting(adPush, pbpId)
                return true
            }
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
        return false
    }


    override fun getId(): String = TAG
}