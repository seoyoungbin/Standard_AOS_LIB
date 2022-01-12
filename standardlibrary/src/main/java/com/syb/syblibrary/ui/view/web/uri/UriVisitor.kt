package com.syb.syblibrary.ui.view.web.uri

import android.content.Context
import android.net.Uri
import android.webkit.WebView
import androidx.annotation.NonNull
import com.syb.syblibrary.ui.view.web.impl.UriAccept

class UriVisitor
{
    private var uriAcceptList: ArrayList<UriAccept> = ArrayList()

    private var uri: Uri? = null
    var context: Context
    var webView: WebView

    constructor(@NonNull context: Context, @NonNull webView: WebView)
    {
        this.context = context
        this.webView = webView
    }

    /**
     * @param uri 처리할 Uri
     * @return 해당 Uri 가 처리되었으면 true return
     */
    fun visit(@NonNull uri: Uri): Boolean
    {
        this.uri = uri
        var result = false
        for (uriAccept in uriAcceptList) {
            result = uriAccept.accept(this)
            if (result)
                break
        }
        return result
    }

    /**
     * Uri 에 반응할 UriAccept 등록
     * @param uriAccept
     */
    fun addUriAccept(@NonNull uriAccept: UriAccept) {
        uriAcceptList.add(uriAccept)
    }

    /**
     * @return uri 스키마 반환
     */
    fun getScheme(): String = uri?.scheme?: ""

    /**
     * @return uri  path 반환
     */
    fun getPath(): String = uri?.path?: ""

    /**
     * @return uri 쿼리스트링 반환
     */
    fun getQueryString(): String = uri?.query?: ""

    /**
     * @return uri full url 반환
     */
    fun getUrl(): String = uri.toString()

}