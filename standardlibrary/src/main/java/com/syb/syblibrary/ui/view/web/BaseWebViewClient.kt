package com.syb.syblibrary.ui.view.web

import android.annotation.TargetApi
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.view.View
import android.webkit.*
import androidx.annotation.RequiresApi
import com.syb.syblibrary.R
import com.syb.syblibrary.ui.base.databinding.adapter.ViewBindingAdapters
import com.syb.syblibrary.ui.view.popupdialog.CustomPopupDialog
import com.syb.syblibrary.ui.view.web.util.CookieHelper
import com.syb.syblibrary.util.log.YLog

abstract class BaseWebViewClient : WebViewClient(){

    companion object {
        private const val SCHEME_INTENT_ACTION_DIAL = "tel:"
        private const val SCHEME_INTENT_ACTION_SENDTO = "mailto:"
        private const val SCHEME_INTENT_ACTION_SMS = "sms:"
    }

    protected abstract fun handleOverrideUrlLoading(webView: WebView?, uri: Uri?, scheme: String, host: String, path: String): Boolean

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean = parseUrl(view, Uri.parse(url))

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean = parseUrl(view, request?.url)

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        if(view is BaseWebView)
        {
            val baseWebView: BaseWebView = view
            baseWebView.baseWebViewListener?.onPageStart(view, url?: "")
            baseWebView.hProgressBar?.visibility = View.VISIBLE
            baseWebView.cProgressBar?.let {
                ViewBindingAdapters.fadeView(it, true)
            }
        }
        super.onPageStarted(view, url, favicon)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        CookieHelper.cookieSync()

        if(view is BaseWebView) {
            val baseWebView: BaseWebView = view

            baseWebView.baseWebViewListener?.onPageFinish(view, url?: "")
            baseWebView.cProgressBar?.let {
                ViewBindingAdapters.fadeView(it, false)
            }

            baseWebView.cookieDelivery(url?: "")

            val path = Uri.parse(url).path
            path?.let {
                val historyClearList = baseWebView.getHistoryClear()
                for (clearPath in historyClearList) {
                    if (it.equals(clearPath)) {
                        view.clearHistory()
                        break
                    }
                }
            }
        }
        super.onPageFinished(view, url)
    }

    override fun onReceivedError(
        view: WebView?,
        errorCode: Int,
        description: String?,
        failingUrl: String?
    ) {
        if(view is BaseWebView) {
            val baseWebView: BaseWebView = view
            baseWebView.baseWebViewListener?.onReceivedError(view, errorCode, description?: "", failingUrl?: "")
        }
        super.onReceivedError(view, errorCode, description, failingUrl)
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        error?.let {
            this.onReceivedError(view, it.errorCode, it.description.toString(), request?.url?.toString()?:"")
        }
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        try {
            view?.context?.let {
                CustomPopupDialog(it).apply {
                    contentText.set(it.getString(R.string.message_error_ssl_cert_invalid))
                    onConfirmClick = {
                        handler?.proceed()
                    }
                    onCancelClick = {
                        handler?.cancel()
                    }
                    setCancelable(false)
                    setCanceledOnTouchOutside(false)
                    show()
                }
            }
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
        YLog.e("WebView SslError!!")
    }

    /**
     * 웹뷰로 부터 들어오는 URL 파싱
     * @param webView 웹뷰
     * @param uri URL
     * @return URL 실행 여부
     */
    fun parseUrl(webView: WebView?, uri: Uri?): Boolean
    {
        try {
            val url = uri.toString()
            val path = uri?.path?: ""
            val host = uri?.host?: ""
            val query = uri?.query?: ""
            val scheme = uri?.scheme?: ""

            val context = webView?.context

            if (url != null && url.startsWith(SCHEME_INTENT_ACTION_DIAL)) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse(url)).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context?.startActivity(intent)
                return true
            } else if (url != null && url.startsWith(SCHEME_INTENT_ACTION_SENDTO)) {
                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(SCHEME_INTENT_ACTION_SENDTO + url.substring(7))).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context?.startActivity(intent)
                return true
            }
            else if(url != null && url.startsWith(SCHEME_INTENT_ACTION_SMS)) {
                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(url)).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context?.startActivity(intent)
            }
            return handleOverrideUrlLoading(webView, uri, scheme, host, path)
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
        return false
    }

    override fun onScaleChanged(view: WebView?, oldScale: Float, newScale: Float) {
        super.onScaleChanged(view, oldScale, newScale)
        if(newScale - oldScale > 7)
            view?.setInitialScale((oldScale / newScale * 100).toInt())
    }

}