package com.syb.sample.ui.webviewsample.lloydweb

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.webkit.JavascriptInterface
import com.syb.sample.ui.view.lloydweb.LloydWebView

class LloydJsInterface
{
    private var mContext: Context?= null
    private var lloydWebView: LloydWebView? = null
    private var handler: Handler = Handler()
    private var arJewelryMediaPlayer: MediaPlayer? = null


    constructor(mContext: Context, lloydWebView: LloydWebView)
    {
        this.mContext = mContext
        this.lloydWebView = lloydWebView
    }

    @JavascriptInterface
    fun AppInnerSoundStop()
    {
        handler.post {
            arJewelryMediaPlayer?.stop()
            arJewelryMediaPlayer?.release()
            arJewelryMediaPlayer = null
        }
    }

    @JavascriptInterface
    fun AppInnerLayerPopup(isShow: String)
    {
        handler.post {
            val isLayerPopupShow = isShow?.toBoolean()
            lloydWebView?.setSwipeRefreshBlock(isLayerPopupShow)
        }
    }

}