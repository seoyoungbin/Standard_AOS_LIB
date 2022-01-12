package com.syb.sample.ui.view.lloydweb

import android.net.Uri
import com.syb.syblibrary.ui.view.web.impl.BaseWebViewListener

interface LloydWebViewListener: BaseWebViewListener
{

    fun moveMainWebView(uri: Uri)

    fun pushSetting(adpush: String, pbpId: String)

    fun closePopup()

}