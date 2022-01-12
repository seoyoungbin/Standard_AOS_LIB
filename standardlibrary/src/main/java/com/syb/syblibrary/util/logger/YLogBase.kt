package com.syb.syblibrary.util.log

import android.util.Log
import androidx.annotation.NonNull
import com.syb.syblibrary.util.log.YLogEnvironment.SHOW_CLASS_INFO
import com.syb.syblibrary.util.log.YLogEnvironment.SHOW_FILE_INFO
import com.syb.syblibrary.util.log.YLogEnvironment.SHOW_LINE_INFO
import com.syb.syblibrary.util.log.YLogEnvironment.SHOW_METHOD_INFO

abstract class YLogBase {

    protected var showElement: Int = YLogEnvironment.DEFAULT_ELEMENT
    protected var defaultTag: String = YLogEnvironment.DEFAULT_TAG

    protected fun isShowInfo(showEnElement: Int): Boolean {
        return showElement and showEnElement == showEnElement
    }

    protected fun buildLogMessage(message: String?): String? {
        val ste = Thread.currentThread().stackTrace[6]
        val sb = StringBuilder()
        if (isShowInfo(SHOW_CLASS_INFO or SHOW_METHOD_INFO))
            sb.append(" - ").append(parseClassName(ste.className)).append(".").append(ste.methodName)
        else if (isShowInfo(SHOW_CLASS_INFO))
            sb.append(" - ").append(parseClassName(ste.className))
        else if (isShowInfo(SHOW_METHOD_INFO))
            sb.append(" - ").append(ste.methodName)
        if (isShowInfo(SHOW_FILE_INFO or SHOW_LINE_INFO))
            sb.append(" (").append(ste.fileName).append(":").append(ste.lineNumber).append(")")
        else if (isShowInfo(SHOW_FILE_INFO))
            sb.append(" (").append(ste.fileName).append(")")
        else if (isShowInfo(SHOW_FILE_INFO))
            sb.append(" (").append(ste.lineNumber).append(")")
        sb.append(" ").append(message)
        return sb.toString()
    }

    private fun parseClassName(@NonNull className: String): String? {
        val tmpStr = className.split("\\.").toTypedArray()
        if (tmpStr.size < 1)
            Log.println(Log.ASSERT, YLogEnvironment.DEFAULT_TAG, "ClassName Parse Error")
        return tmpStr[tmpStr.size - 1]
    }

}