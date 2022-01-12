package com.syb.sample.ui.view.lloydweb.uri

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import com.syb.syblibrary.ui.view.web.uri.UriVisitor
import java.net.URISyntaxException

class IntentUriResolver : UriResolver {
    private val INTENT_SCHEME = "intent:"
    private val MARKET_SCHEME = "market:"
    private var mContext: Context? = null

    companion object {
        @JvmStatic
        val TAG = "IntentUriResolver"
    }

    constructor(view: Context) {
        mContext = view
    }

    override fun accept(visitor: UriVisitor): Boolean {
        val url = visitor.getUrl()
        val webView = visitor.webView
        return handle(webView, url)
    }

    private fun handle(webView: WebView, url: String): Boolean {
        try {
            if (url.startsWith(INTENT_SCHEME)) {
                val intent: Intent
                try {
                    intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                } catch (e: URISyntaxException) {
                    // invalid intent uri. ignore..
                    return false
                }


                // 처리 가능한 액티비티가 있으면 호출
                mContext?.let { context ->
                    intent.resolveActivity(context.packageManager)?.let {
                        context.startActivity(intent)
                        return true
                    }
                }

                // 없으면 마켓으로 이동
                return if (!intent.getPackage().isNullOrEmpty()) {
                    startGooglePlay(mContext!!, intent.getPackage()!!)
                } else false
            } else if (url.startsWith(MARKET_SCHEME)) {
                mContext?.let {
                    startExternalActivity(it, Uri.parse(url))
                }
                return true
            }
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
        return false

    }

    fun startGooglePlay(context: Context, packageName: String): Boolean =
        startExternalActivity(context, Uri.parse("market://details?id=$packageName"))

    fun startExternalActivity(context: Context, uri: Uri): Boolean {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        if (intent.resolveActivity(context.packageManager) == null)
            return false
        context.startActivity(intent)
        return true
    }

    override fun getId(): String = TAG

}