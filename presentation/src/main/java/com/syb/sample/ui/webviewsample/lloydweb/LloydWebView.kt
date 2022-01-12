package com.syb.sample.ui.view.lloydweb

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.webkit.WebView
import androidx.databinding.ObservableBoolean
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.syb.syblibrary.ui.base.view.BaseActivity
import com.syb.syblibrary.ui.view.web.BaseWebView
import com.syb.syblibrary.ui.view.web.impl.CookieEventSearcherListener
import com.syb.syblibrary.ui.view.web.impl.SwipeRefreshListener
import com.syb.syblibrary.ui.view.web.util.CookieHelper
import com.syb.syblibrary.util.AppUtil
import com.syb.syblibrary.util.log.YLog
import com.syb.sample.ui.webviewsample.lloydweb.LloydJsInterface

class LloydWebView : BaseWebView, SwipeRefreshListener {

    companion object {
        const val LLOYD_JAVAINTERFACE_NAME = "lloyd"
    }

    private var isSwipeRefreshBlock = false
    internal var lloydWebViewChromeClient: LloydWebViewChromeClient? = null
    internal var lloydExecuteJs: LloydWebViewExecuteJs = LloydWebViewExecuteJs(this)

    constructor(context: Context) : super(context) {
        initialzeView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialzeView(context)
    }

    fun initialzeView(context: Context) {

        swipeRefreshListener = this

        val userAgent = LinkedHashMap<String, String>().apply {
            put("AP", "Y")
            put("DTYPE", "A")
            put("AppVersion", AppUtil.getAppVersionName(context))
        }

        CookieHelper.setCustomCookie(context, E.MAIN_URL, arrayOf("AppVersion=${AppUtil.getAppVersionName(context)}"))

        setCustomUserAgent(userAgent)

        setHistoryClear(arrayOf("/myPage/recentViewProduct"))

        addCookieEvent(object : CookieEventSearcherListener{
            override fun getCookieSearcherKey(): String {
                return "JSESSIONID"
            }

            override fun cookieDelivery(webView: WebView, cookie: String?, searchKey: String) {
                YLog.i("LloydWebView cookieDelivery Data => ${searchKey}:${cookie}")
            }
        })

        setWebContentsDebuggingEnabled(true)

    }

    /**
     * 로딩창 보여주면서 웹페이지 로드
     * @param observable 로딩창 VISIBLE Observable
     * @param url Load Url
     */
    internal fun showLoadingLoadUrl(observable: ObservableBoolean, url: String)
    {
        observable.set(true)
        loadUrl(url)
    }

    /**
     * 로딩창 보여주면서 웹페이지 로드
     * @param observable 로딩창 VISIBLE Observable
     * @param url Load Url
     * @param param Body Data
     */
    internal fun showLoadingLoadPostUrl(observable: ObservableBoolean, url: String, param: ByteArray)
    {
        observable.set(true)
        postUrl(url, param)
    }

    // 현재 페이지가 메인 페이지인지 체크
    internal fun isMainUrl(): Boolean {
        if (url.isNullOrEmpty())
            return false
        val uri = Uri.parse(url)
        val path = uri.path?.let { it } ?: ""
        return uri.isHierarchical && E.getMainAuthority().equals(uri.authority) && path.equals(E.MAIN_PATH)
    }

    /**
     * 웹뷰 초기 세팅!!
     * @param activity 해당 Activity
     * @param listener 웹뷰 리스너
     */
    internal fun setWebViewSetting(activity: BaseActivity, listener: LloydWebViewListener) {
        LloydWebViewClient(context, this, listener)?.let {
            webViewClient = it
        }
        LloydWebViewChromeClient(this, activity, listener)?.let {
            lloydWebViewChromeClient = it
            webChromeClient = it
        }
        LloydJsInterface(context, this)?.let {
            addJavascriptInterface(it, LLOYD_JAVAINTERFACE_NAME)
        }
    }

    /**
     * SwipeRefresh 활성화 세팅
     * @param isSwipeRefreshBlock SwipeRefresh Block 여부
     */
    internal fun setSwipeRefreshBlock(isSwipeRefreshBlock: Boolean)
    {
        this.isSwipeRefreshBlock = isSwipeRefreshBlock
        swipeRefreshLayout?.isEnabled = !isSwipeRefreshBlock
    }

    override fun swipeRefresh(p0: SwipeRefreshLayout) {
        reload()
    }

    override fun onScrollChanged(left: Int, top: Int, oldLeft: Int, oldTop: Int) {
        if(!isSwipeRefreshBlock)
            super.onScrollChanged(left, top, oldLeft, oldTop)
    }

}