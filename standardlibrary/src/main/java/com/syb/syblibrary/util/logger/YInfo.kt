package com.syb.syblibrary.util.log

import android.util.Log
import com.syb.syblibrary.util.log.YLogEnvironment.SHOW_LOG_CATEGORY

class YInfo : YLogBase(), YLogInterface {

    private val preFix = "[INFOMATION] : "

    override fun settingDefaultTag(defaultTag: String) {
        this.defaultTag = defaultTag
    }

    override fun settingShowElement(showElement: Int) {
        this.showElement = showElement
    }

    override fun showLog(message: String) {
        log(defaultTag, message)
    }

    override fun showLog(tag: String, message: String) {
        log(tag, message)
    }

    override fun showLog(message: String, err: Throwable) {
        log(defaultTag, message, err)
    }

    override fun showLog(tag: String, message: String, err: Throwable) {
        log(tag, message, err)
    }

    private fun log(tag: String, message: String) {
        var message: String? = message
        if (isShowInfo(SHOW_LOG_CATEGORY))
            message = preFix + buildLogMessage(message)
        Log.i(tag, message?: "")
    }

    private fun log(
        tag: String,
        message: String,
        tr: Throwable
    ) {
        var message: String? = message
        if (isShowInfo(SHOW_LOG_CATEGORY))
            message = preFix + buildLogMessage(message)
        Log.i(tag, message, tr)
    }

}