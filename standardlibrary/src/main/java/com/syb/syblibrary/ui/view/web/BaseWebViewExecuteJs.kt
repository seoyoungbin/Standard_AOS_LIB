package com.syb.syblibrary.ui.view.web

import android.os.Build
import android.webkit.ValueCallback
import android.webkit.WebView
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

abstract class BaseWebViewExecuteJs(@NonNull private val webView: WebView) {

    /**
     * 자바스크립트 실행
     * @param js 자바스크립트 함수
     */
    fun loadJs(js: String) { loadJs(js, null) }

    /**
     * 자바스크립트 실행
     * @param js 자바스크립트 함수
     * @param callback 콜백 파라미터
     */
    fun loadJs(js: String, callback: ValueCallback<String>?) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            evaluateJs(js, callback)
        else
            webView.loadUrl(js)
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun evaluateJs(js: String, callback: ValueCallback<String>?)
    {
        webView.evaluateJavascript(js, { value -> callback?.onReceiveValue(value) })
    }

    /**
     * 자바스크립트 호출
     * @param method 함수명
     */
    fun quickCallJs(method: String) {
        this.quickCallJs(method, null)
    }

    /**
     * 자바스크립트 호출
     * @param method 함수명
     * @param params 파라미터 배열
     */
    fun quickCallJs(method: String, vararg params: String) {
        quickCallJs(method, null, *params)
    }

    /**
     * 자바스크립트 호출
     * @param method 함수명
     * @param callback 콜백 파라미터
     * @param params 파라미터 배열
     */
    fun quickCallJs(method: String, callback: ValueCallback<String>?, vararg params: String)
    {
        val sb = StringBuilder()
        sb.append("javascript:").append(method)
        if (params == null || params.size == 0)
            sb.append("()")
        else
            sb.append("(").append(concat(*params)).append(")")
        loadJs(sb.toString(), callback)
    }

    /**
     * 자바스크립트 호출 함수 제작
     * @param params 파라미터 배열
     * @return 자바스크립트 호출 함수
     */
    fun concat(vararg params: String): String {
        val mStringBuilder = StringBuilder()
        for (i in 0 until params.size) {
            val param = params[i]
            if (!isJson(param))
                mStringBuilder.append("\"").append(param).append("\"")
            else
                mStringBuilder.append(param)
            if (i != params.size - 1)
                mStringBuilder.append(" , ")
        }
        return mStringBuilder.toString()
    }

    /**
     * JSON 여부 확인
     * @param target 검사할 문자열
     * @return JSON 여부
     */
    fun isJson(target: String): Boolean {
        if (target.isNullOrEmpty())
            return false
        var tag = try {
            if (target.startsWith("["))
                JSONArray(target)
            else
                JSONObject(target)
            true
        } catch (ignore: JSONException) {
            false
        }
        return tag
    }

}