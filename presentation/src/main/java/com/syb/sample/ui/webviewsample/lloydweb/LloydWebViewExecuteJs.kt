package com.syb.sample.ui.view.lloydweb

import com.syb.syblibrary.ui.view.web.BaseWebViewExecuteJs

class LloydWebViewExecuteJs(private val lloydWebView: LloydWebView): BaseWebViewExecuteJs(lloydWebView) {

    /**
     * 주소록 세팅
     * @param telNm 주소록 이름
     * @param telNo 주소록 번호
     */
    fun fnCallAddress(telNm: String, telNo: String)
    {
        quickCallJs("fnCallAddress", *arrayOf(telNm, telNo))
    }

    /**
     * 푸시 활성화 세팅
     * @param adPush 푸시 ON/OFF 여부
     * @param pbpId 고객 ID
     * @param settingCheck 푸시 세팅 여부
     */
    @Synchronized
    fun fnPushCheck(adPush: String, pbpId: String, settingCheck: Boolean)
    {
        quickCallJs("fnPushCheck", *arrayOf(adPush, pbpId, settingCheck.toString()))
    }

}