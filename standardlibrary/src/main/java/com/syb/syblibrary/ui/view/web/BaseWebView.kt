package com.syb.syblibrary.ui.view.web

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.webkit.CookieManager
import android.webkit.DownloadListener
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.syb.syblibrary.BuildConfig
import com.syb.syblibrary.common.filedownload.FileDownload
import com.syb.syblibrary.ui.base.viewpager.BaseViewPager
import com.syb.syblibrary.ui.view.web.impl.*
import com.syb.syblibrary.ui.view.web.util.CookieHelper
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

abstract class BaseWebView : WebView, DownloadListener {

    companion object{
        const val SHARED_COOKIE_FILE = "SHARED_COOKIE_FILE"
    }

    private var cookieEventSearcherListenerList = ArrayList<CookieEventSearcherListener>()
    private var historyClearList = ArrayList<String>()
    private var webViewScrollMove = false
    private var scaleTouchSlop = 0
    private var cookiesMap: HashMap<String, String> = HashMap()
    private var beforeTouchY = 0f
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    set(value) {
        field = value
        field?.setOnRefreshListener {
            field?.let{
                it.isRefreshing = false
                swipeRefreshListener?.swipeRefresh(it)
            }
        }
    }
    var baseWebViewListener: BaseWebViewListener? = null
    var cProgressBar: ProgressBar? = null
    var hProgressBar: ProgressBar? = null
    var onTouchScrollEventListener: OnTouchScrollEventListener? = null
    var swipeRefreshListener: SwipeRefreshListener? = null
    var baseViewPager: BaseViewPager? = null

    constructor(context: Context) : super(context)
    {
        initialzeView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialzeView(context)
    }

    @SuppressLint("SetJavaScriptEnabled", "SdCardPath")
    private fun initialzeView(context: Context) {

        scaleTouchSlop = ViewConfiguration.get(context).scaledTouchSlop

        with(settings)
        {
            javaScriptEnabled = true
            loadsImagesAutomatically = true
            loadWithOverviewMode = true
            useWideViewPort = true
            allowFileAccess = true
            domStorageEnabled = true
            databaseEnabled = true
            setSupportMultipleWindows(false)
            javaScriptCanOpenWindowsAutomatically = true
            textZoom = 100
            cacheMode = WebSettings.LOAD_NO_CACHE
        }

        isHorizontalScrollBarEnabled = false
        isVerticalScrollBarEnabled = false
        setVerticalScrollbarOverlay(true)
        setHorizontalScrollbarOverlay(true)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        else
            settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)
            cookieManager.setAcceptThirdPartyCookies(this, true)
        }

        if (BuildConfig.DEBUG) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                setWebContentsDebuggingEnabled(true)
        }

        setDownloadListener(this)

    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(left, top, oldl, oldt)
        webViewScrollMove = true
        swipeRefreshLayout?.isEnabled = (top == 0)
        baseWebViewListener?.webViewScrollChange(left, top, oldl, oldt)
    }

    override fun onOverScrolled(scrollX: Int, scrollY: Int, clampedX: Boolean, clampedY: Boolean) {
        baseViewPager?.isPagingEnabled = true
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        requestDisallowInterceptTouchEvent(true)
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                beforeTouchY = event.y
                baseViewPager?.isPagingEnabled = false
            }
            MotionEvent.ACTION_MOVE -> {
                val eventY = event.y
                if (Math.abs(eventY - beforeTouchY) > scaleTouchSlop) {
                    if (beforeTouchY > event.y)
                        onTouchScrollEventListener?.webViewOnTouchScrollDown(this, eventY, webViewScrollMove)
                    else if (beforeTouchY < event.y)
                        onTouchScrollEventListener?.webViewOnTouchScrollUp(this, eventY, webViewScrollMove)
                }
            }
            MotionEvent.ACTION_UP -> {
                beforeTouchY = 0f
                webViewScrollMove = false
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDownloadStart(url: String?, userAgent: String?, contentDisposition: String?, mimetype: String?, contentLength: Long) {
        FileDownload(context).downloadStart(url, userAgent, contentDisposition, mimetype)
    }

    /**
     * History ?????? URL ??????
     * @param historyArray History ?????? URL???
     */
    fun setHistoryClear(@Nullable historyArray: Array<String>)
    {
        if(historyArray == null)
            historyClearList.clear()
        else
        {
            Collections.addAll(historyClearList, *historyArray)
            val distinctData = HashSet(historyClearList)
            historyClearList = ArrayList(distinctData)
        }
    }

    /**
     * History ?????? URL ????????????!!
     * @return History ?????? URL
     */
    fun getHistoryClear(): ArrayList<String>
    {
        return historyClearList
    }

    /**
     * ?????? ?????? ????????? ????????? ??????
     * @param event ?????? ?????? ????????? ?????????
     */
    fun addCookieEvent(@NonNull event: CookieEventSearcherListener)
    {
        cookieEventSearcherListenerList.add(event)
        val distinctData = HashSet(cookieEventSearcherListenerList)
        cookieEventSearcherListenerList = ArrayList(distinctData)
    }

    /**
     * ?????? ?????? ????????? ????????? ??????!
     * @param event ?????? ?????? ????????? ?????????
     */
    fun removeCookieEvent(@NonNull event: CookieEventSearcherListener)
    {
        cookieEventSearcherListenerList.remove(event)
    }

    /**
     * ?????? ?????? ????????? ???????????? ?????? ??? ??????
     * @param url ?????? ??? ????????? URL
     */
    internal fun cookieDelivery(@Nullable url: String)
    {
        if (url == null)
            return

        cookiesMap = CookieHelper.getAppCookie(url)

        cookieEventSearcherListenerList.forEach { listener ->
            val key = listener.getCookieSearcherKey()

            key?.let {
                var value = cookiesMap.get(it.trim())
                value?.let { v ->
                    listener.cookieDelivery(this, v.trim(), key)
                }
            }
        }
    }

    /**
     * ?????? KEY, VALUE ??? ?????? ?????? ??????
     * @param key ?????? ??? ???
     * @return ?????? ?????? ??????
     */
    fun saveSharedCookie(@NonNull key: String): Boolean
    {
        val value = cookiesMap.get(key)?: ""
        if(!value.isNullOrEmpty()) {
            val pref = context.getSharedPreferences(SHARED_COOKIE_FILE, MODE_PRIVATE)
            pref.edit().putString(key, value).apply()
            return true
        }
        return false
    }

    /**
     * ?????? ????????? ???????????? ?????? ?????? ??? ??????
     * @param key ?????? ??? ???
     * @return ?????? VALUE
     */
    fun getSharedCookie(@NonNull key: String): String
    {
        val pref = context.getSharedPreferences(SHARED_COOKIE_FILE, MODE_PRIVATE)
        return pref.getString(key, "")?: ""
    }

    /**
     * ?????? ????????? ???????????? ?????? ?????? ??????
     * @param key ?????? ??? ???
     */
    fun removeSharedCookie(@NonNull key: String)
    {
        val pref = context.getSharedPreferences(SHARED_COOKIE_FILE, MODE_PRIVATE)
        val editor = pref.edit()
        editor.remove(key)
        editor.apply()
    }

    /**
     * ?????? ?????? ????????? ???????????? ??? ??? ????????? URL ??????
     * @param index ????????? ?????????
     * @return ?????? URL
     */
    fun getForwardUrl(index: Int): String
    {
        try {
            val uri = getBackForwardUri(index)
            val scheme = uri?.scheme
            if(scheme.equals("http") || scheme.equals("https"))
                return uri.toString()
        }catch (e: NullPointerException)
        {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * ?????? URL ???????????? index ????????? ?????? ????????? URL ??????
     * @param index ????????? ?????????
     * @return ?????? URL
     */
    fun getBackForwardUri(index: Int): Uri?
    {
        try {
            val historyList = copyBackForwardList()

            val forwardTargetUrl = historyList.getItemAtIndex(historyList.currentIndex + index).url
            return Uri.parse(forwardTargetUrl)
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
        return null
    }

    /**
     * ?????? Value ??? ??????
     * @param cookieKey ????????? key ???
     * @return ????????? ?????? value ???
     */
    fun searchCookie(@NonNull cookieKey: String) = cookiesMap.get(cookieKey)

    /**
     * ?????? UserAgent ??????
     * @param linkedHashMap UserAgent ?????????
     */
    fun setCustomUserAgent(@NonNull linkedHashMap: LinkedHashMap<String, String>)
    {
        var userAgentString = settings.userAgentString
        if(userAgentString.lastIndexOf(";") < 0)
            userAgentString += ";"
        if(linkedHashMap.size > 0)
        {
            val sb = StringBuilder()
            val keys = linkedHashMap.keys.iterator()
            while (keys.hasNext()) {
                val key = keys.next()
                sb.append(key).append("=").append(linkedHashMap[key])
                if (keys.hasNext())
                    sb.append(";")
            }
            settings.userAgentString = userAgentString + sb.toString()
        }
    }
}