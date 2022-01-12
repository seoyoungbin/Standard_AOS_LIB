package com.syb.syblibrary.util.log

interface YLogInterface {

    fun settingDefaultTag(defaultTag: String)

    fun settingShowElement(showElement: Int)

    fun showLog(message: String)

    fun showLog(tag: String, message: String)

    fun showLog(message: String, err: Throwable)

    fun showLog(tag: String, message: String, err: Throwable)

}