package com.syb.syblibrary.ui.view.web

import android.net.Uri
import java.net.URLEncoder

object WebUtil {

    /**
     * 파라미터 공백문자 치환!
     * @param s 검증 문자열
     * @return 치환 후 문자열
     */
    fun paramSubstitution(s: String?): String =
        if (s == null)
            ""
        else
            s.replace(" ", "+")
                .replace("_", "/")
                .replace("-", "+")

    /**
     * Url 파라미터 데이터로 변환
     * @param url 타켓 URL
     * @return 파라미터 데이터
     */
    fun urlParamToData(url: String): LinkedHashMap<String, String>
    {
        val params: LinkedHashMap<String, String> = LinkedHashMap()
        try {
            val uri = Uri.parse(url)
            val paramNames = uri.queryParameterNames
            for (p in paramNames) {
                uri.getQueryParameter(p)?.let {
                    params.put(p, it)
                }
            }
            return params
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
        return params
    }

    /**
     * Url 파라미터 데이터로 변환
     * @param url 타켓 URL
     * @return 파라미터 데이터
     */
    fun urlParamToString(url: String): String
    {
        val params: LinkedHashMap<String, String> = LinkedHashMap()
        try {
            val uri = Uri.parse(url)
            val paramNames = uri.queryParameterNames
            for (p in paramNames) {
                uri.getQueryParameter(p)?.let {
                    params.put(p, it)
                }
            }
            return dataToUriParam(params)
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
        return ""
    }


    /**
     * 데이터 Uri 파라미터로 변환
     * @param data 데이터
     * @return Uri 파라미터
     */
    fun dataToUriParam(data: LinkedHashMap<String, String>): String
    {
        val init = "?"
        var param = init
        data.forEach {
            param += if(param.equals(init)) "${it.key}=${it.value}" else "&${it.key}=${it.value}"
        }
        if (param.equals(init))
            param = ""
        return param
    }

    /**
     * 데이터 Uri 파라미터로 변환
     * @param data 데이터
     * @return Uri 파라미터
     */
    fun dataToPostUriParam(data: LinkedHashMap<String, String>): ByteArray
    {
        val enc = "UTF-8"
        var s = ""
        data.forEach {
            s += "${if(s.isEmpty())"" else "&"}${it.key}=${URLEncoder.encode(it.value, enc)}"
        }
        return s.toByteArray()
    }

}