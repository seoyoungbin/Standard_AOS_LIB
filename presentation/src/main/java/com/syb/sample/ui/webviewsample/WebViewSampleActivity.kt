package com.syb.sample.ui.webviewsample

import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.ProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.syb.syblibrary.data.local.pref.BasePreferenceHelper
import com.syb.syblibrary.ui.base.view.BaseActivity
import com.syb.syblibrary.ui.view.web.impl.OnTouchScrollEventListener
import com.syb.syblibrary.ui.view.web.impl.SwipeRefreshListener
import com.syb.syblibrary.util.log.YLog
import com.syb.sample.R
import com.syb.sample.ui.view.lloydweb.E
import com.syb.sample.ui.view.lloydweb.LloydWebView
import com.syb.sample.ui.view.lloydweb.LloydWebViewListener
import org.json.JSONArray

class WebViewSampleActivity : BaseActivity(), LloydWebViewListener, OnTouchScrollEventListener, SwipeRefreshListener {

    private var lloydWebView: LloydWebView? = null
    private var swipeRefresh: SwipeRefreshLayout? = null
    private var loadingbar: ProgressBar? = null
    private var horizontal_loadingbar: ProgressBar? = null
    private var btn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view_sample)
        setUp()
    }

    override fun setUp() {
        lloydWebView = findViewById(R.id.lloydWebView)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        loadingbar = findViewById(R.id.loadingbar)
        horizontal_loadingbar = findViewById(R.id.horizontal_progressbar)
        btn = findViewById(R.id.btn)
        btn?.setOnClickListener {
            val telNo = JSONArray().apply {
                put("01000000000")
                put("01000000001")
            }.toString()
            lloydWebView?.lloydExecuteJs?.fnCallAddress("a", telNo)
        }
        webSetting()
    }

    override fun getPreferenceHelper(): BasePreferenceHelper? = null

    /**
     * 웹 초기화 작업
     */
    private fun webSetting() {

        // 웹뷰 SwipeRefresh 등록!
        lloydWebView?.swipeRefreshLayout = swipeRefresh
        lloydWebView?.baseWebViewListener = this
        lloydWebView?.onTouchScrollEventListener = this
        lloydWebView?.swipeRefreshListener = this

        lloydWebView?.cProgressBar = loadingbar
        lloydWebView?.hProgressBar = horizontal_loadingbar

        // 웹뷰 클라이언트 및 크롬 클라이언트 초기화
        lloydWebView?.setWebViewSetting(this, this)

        // 웨뷰 BaseWebViewActivity에 등록!!
        mWebView = lloydWebView

        baseWebChromeClient = lloydWebView?.lloydWebViewChromeClient

        // 웹뷰 실행
        lloydWebView?.loadUrl(E.MAIN_URL)

    }

    override fun onBackPressed() {
        if (lloydWebView?.canGoBack()?: false)
            lloydWebView?.goBack()
        else
            super.onBackPressed()
    }

    override fun onPageStart(view: WebView?, url: String?) {
        YLog.i("LloydWebView onPageStart")
    }

    override fun onPageFinish(view: WebView?, url: String?) {
        YLog.i("LloydWebView onPageFinish")
    }

    override fun onReceivedError(
        view: WebView,
        errorCode: Int,
        description: String,
        failingUrl: String
    ) {
        YLog.i("LloydWebView onReceivedError => $description")
    }

    override fun onProgressChanged(webView: WebView, progress: Int) {
        YLog.i("LloydWebView onProgressChanged => ${progress}")
    }

    override fun moveMainWebView(uri: Uri) {
        YLog.i("LloydWebView moveMainWebView")
    }

    override fun pushSetting(adpush: String, pbpId: String) {
        YLog.i("LloydWebView pushSetting => $adpush $pbpId")
    }

    override fun closePopup() {

    }

    override fun webViewScrollChange(l: Int, t: Int, oldl: Int, oldt: Int) {
        YLog.i("LloydWebView webViewScrollChange => $l $t $oldl $oldt")
    }

    override fun webViewOnTouchScrollDown(webView: WebView, touchY: Float, webViewScrollMove: Boolean)
    {
        YLog.i("LloydWebView webViewOnTouchScrollDown => $touchY $webViewScrollMove")
    }

    override fun webViewOnTouchScrollUp(webView: WebView, touchY: Float, webViewScrollMove: Boolean)
    {
        YLog.i("LloydWebView webViewOnTouchScrollUp => $touchY $webViewScrollMove")
    }

    override fun swipeRefresh(swipeRefreshLayout: SwipeRefreshLayout) {
        YLog.i("LloydWebView swipeRefresh")
        lloydWebView?.reload()
    }
}