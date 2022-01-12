package com.syb.sample.ui.view.lloydweb

import android.net.Uri
import com.syb.syblibrary.ui.view.web.WebUtil

object E
{
    var baseUrl = "https://m.lloydgift.com"
//    var baseUrl = "https://dev-www.latem.co.kr/support/notice/view/233"
//    var baseUrl = "https://m.naver.com"
    const val MAIN_PATH = "/main"
    const val SEARCH_PRODUCT_DETAIL_PATH = "/productDetail/searchProductDetail"
    const val SEARCH_AR_PRODUCT_PATH = "/arProduct/searchProductDetail"
    const val PRODUCT_ORDER_SHEET_PATH = "/order/orderSheet"
    const val PRESENT_ORDER_SHEET_PATH = "/present/presentOrderSheet"
    const val ORDER_CARTLIST_PATH = "/cart/cartList"

    val MAIN_URL = baseUrl

    const val PBPID_PARAMETER = "pbpid"
    const val REDIRECT_URL_PARAMETER = "redirecturl"
    const val ADPUSH_PARAMETER = "adpush"


    /**
     * 메인 authority 반환
     * @return 메인 authority
     */
    fun getMainAuthority(): String
    {
        val uri = Uri.parse(baseUrl)
        val authority = uri.authority?: ""
        return authority
    }

    /**
     * 제품 상세 페이지 URL 반환
     * @param customUrl 로이드 커스텀 URL
     * @return 제품 상세 페이지 URL
     */
    fun getSearchProductDetailUrl(customUrl: String): String
    {
        val param = WebUtil.urlParamToString(customUrl)
        return baseUrl + SEARCH_PRODUCT_DETAIL_PATH + param
    }

}