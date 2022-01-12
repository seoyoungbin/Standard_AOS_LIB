package com.syb.syblibrary.util.log

object YLog {

    private val error: YLogInterface = YError()
    private val debug: YLogInterface = YDebug()
    private val info: YLogInterface = YInfo()
    private val verbose: YLogInterface = YVerbose()
    private val warnning: YLogInterface = YWarning()
    private val assertlog: YLogInterface = YAssert()

    private val logList: Array<YLogInterface> = arrayOf(error, debug, info, verbose, warnning, assertlog)

    fun setShowElement(showElement: Int) {
        logList.forEach { t ->
            t.settingShowElement(showElement)
        }
    }

    fun setDefaultTag(tag: String) {
        logList.forEach { t ->
            t.settingDefaultTag(tag)
        }
    }

    fun ASSERT(message: String) {
        AssertLog(message)
    }

    fun ASSERT_FAIL(condition: Boolean, message: String) {
        if (condition)
            AssertLog(message)
    }

    fun ASSERT_NULL(obj: Any?, message: String) {
        if (obj == null)
            AssertLog(message)
    }

    private fun AssertLog(message: String) {
        assertlog.showLog(message)
    }

    fun e(tag: String, message: String) {
        error.showLog(tag, message)
    }

    fun e(message: String) {
        error.showLog(message)
    }

    fun e(tag: String, message: String, err: Throwable) {
        error.showLog(tag, message, err)
    }

    fun e(message: String, err: Throwable) {
        error.showLog(message, err)
    }

    fun d(tag: String, message: String) {
        debug.showLog(tag, message)
    }

    fun d(message: String) {
        debug.showLog(message)
    }

    fun d(tag: String, message: String, err: Throwable) {
        debug.showLog(tag, message, err)
    }

    fun d(message: String, err: Throwable) {
        debug.showLog(message, err)
    }


    fun i(tag: String, message: String) {
        info.showLog(tag, message)
    }


    fun i(message: String) {
        info.showLog(message)
    }

    fun i(tag: String, message: String, err: Throwable) {
        info.showLog(tag, message, err)
    }

    fun i(message: String, err: Throwable) {
        info.showLog(message, err)
    }


    fun v(tag: String, message: String) {
        verbose.showLog(tag, message)
    }

    fun v(message: String) {
        verbose.showLog(message)
    }


    fun v(tag: String, message: String, err: Throwable) {
        verbose.showLog(tag, message, err)
    }

    fun v(message: String, err: Throwable) {
        verbose.showLog(message, err)
    }


    fun w(tag: String, message: String) {
        warnning.showLog(tag, message)
    }

    fun w(message: String) {
        warnning.showLog(message)
    }


    fun w(tag: String, message: String, err: Throwable) {
        warnning.showLog(tag, message, err)
    }

    fun w(message: String, err: Throwable) {
        warnning.showLog(message, err)
    }
    
    
}