package com.syb.syblibrary.ui.view.web.util

import android.content.Context
import android.os.Build
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import androidx.annotation.NonNull

object CookieHelper
{

    private var cookieManager = CookieManager.getInstance()

    /**
     * 쿠키값 반환!!
     * @param domain url
     * @return 도메인에 해당한 쿠키를 map 으로 반환
     */
    fun getAppCookie(@NonNull domain: String): HashMap<String, String>
    {
        val cookiesMap = HashMap<String, String>()
        val cookies = cookieManager.getCookie(domain)

        try{
            val cookie = cookies.split(";")
            for(kv in cookie)
            {
                val key = kv.split("=".toRegex()).toTypedArray()[0]
                val lastIdx = kv.indexOf("=") + 1
                if (lastIdx <= kv.length)
                    cookiesMap[key.trim()] = kv.substring(lastIdx)
            }
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
        return cookiesMap
    }

    /**
     * 모든 쿠키 삭제!!
     */
    fun removeAllCookie() { cookieManager.removeAllCookie() }

    /**
     * 세션 쿠키 삭제!!
     */
    fun removeSessionCookie()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            cookieManager.removeSessionCookies(null)
        else
        {
            cookieManager.removeExpiredCookie()
            cookieManager.removeSessionCookie()
        }
    }

    /**
     * 쿠키 값 세팅
     * @param context 컨텍스트
     * @param domain 쿠키값 세팅 domain
     * @param cookies 쿠키
     */
    fun setCustomCookie(@NonNull context: Context, @NonNull domain: String, @NonNull cookies: Array<String>)
    {
        try{
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            {
                CookieSyncManager.createInstance(context)
                for(cookie in cookies)
                    cookieManager.setCookie(domain, cookie)
                cookieSync()
            }
            else {
                for(cookie in cookies)
                    cookieManager.setCookie(domain, cookie)
                cookieSync()
            }
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    /**
     * 쿠키 동기화 처리!!
     */
    fun cookieSync()
    {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            CookieSyncManager.getInstance().sync()
        else
            CookieManager.getInstance().flush()
    }
}