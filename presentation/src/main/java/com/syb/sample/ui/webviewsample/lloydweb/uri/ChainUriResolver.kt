package com.syb.sample.ui.view.lloydweb.uri

import android.content.Context
import android.net.Uri
import android.webkit.WebView
import com.syb.syblibrary.ui.view.web.impl.UriAccept
import com.syb.syblibrary.ui.view.web.uri.IntentScheme
import com.syb.syblibrary.ui.view.web.uri.MarketScheme
import com.syb.syblibrary.ui.view.web.uri.PayScheme
import com.syb.syblibrary.ui.view.web.uri.UriVisitor
import com.syb.sample.ui.view.lloydweb.LloydWebViewListener


class ChainUriResolver: UriAccept
{
    private var mContext: Context
    private var uriVisitor: UriVisitor
    private var listener: LloydWebViewListener

    constructor(context: Context, webView: WebView, listener: LloydWebViewListener)
    {
        this.listener = listener
        mContext = context
        uriVisitor = UriVisitor(context, webView)
        create()
    }

    override fun accept(visitor: UriVisitor): Boolean {
        try {
            uriVisitor?.let {
                val uri = Uri.parse(it.getUrl())
                return it.visit(uri)
            }
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
        return false
    }

    fun create()
    {
        // 결제 스키마 추가
        val xPayMsg = "uplus ecredit 설치가 필요합니다.\n확인버튼을 누르시면 구글플레이로 이동합니다"
        val xPayUri = "market://details?id=kr.co.uplus.ecredit"
        val xPayScheme = "smartxpay-transfer"
        val xpayScheme = PayScheme(xPayUri, xPayMsg, xPayScheme)

        val ispMsg = "ISP 앱 설치가 필요합니다.\n확인버튼을 누르시면 구글플레이로 이동합니다"
        val ispUri = "market://details?id=kvp.jjy.MispAndroid320"
        val ispScheme = "ispmobile"
        val ispPayScheme = PayScheme(ispUri, ispMsg, ispScheme)

        val payNowMsg = "페이나우 앱 설치가 필요합니다.\n확인버튼을 누르시면 구글플레이로 이동합니다"
        val payNowUri = "market://details?id=com.lguplus.paynow"
        val payNowScheme = "lguthepay-xpay"
        val payNowPayScheme = PayScheme(payNowUri, payNowMsg, payNowScheme)

        val payNow2Msg = "페이나우 앱 설치가 필요합니다.\n확인버튼을 누르시면 구글플레이로 이동합니다"
        val payNow2Uri = "market://details?id=com.lguplus.paynow"
        val payNow2Scheme = "lguthepay"
        val payNow2PayScheme = PayScheme(payNow2Uri, payNow2Msg, payNow2Scheme)

        uriVisitor.addUriAccept(xpayScheme)
        uriVisitor.addUriAccept(ispPayScheme)
        uriVisitor.addUriAccept(payNowPayScheme)
        uriVisitor.addUriAccept(payNow2PayScheme)
        uriVisitor.addUriAccept(IntentScheme())
        uriVisitor.addUriAccept(MarketScheme())
//        uriVisitor.addUriAccept(IntentUriResolver(mContext))
        uriVisitor.addUriAccept(LoginUriResolver(mContext, listener))
        uriVisitor.addUriAccept(SettingUriResolver(mContext, listener))
        uriVisitor.addUriAccept(LloydUriResolver(mContext, listener))

    }

    fun startUriVisitor(uri: Uri): Boolean = uriVisitor?.let { it.visit(uri) }


}