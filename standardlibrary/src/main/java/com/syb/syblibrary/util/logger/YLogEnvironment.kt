package com.syb.syblibrary.util.log

import android.util.Log

object YLogEnvironment {

    const val SHOW_FILE_INFO = 0x1
    const val SHOW_CLASS_INFO = 0x2
    const val SHOW_METHOD_INFO = 0x4
    const val SHOW_LINE_INFO = 0x8
    const val SHOW_LOG_CATEGORY = 0x10

    var DEFAULT_ELEMENT = SHOW_CLASS_INFO or SHOW_FILE_INFO or SHOW_METHOD_INFO or
            SHOW_LINE_INFO or SHOW_LOG_CATEGORY

    const val DEFAULT_TAG = "SLog"

    init {
        Log.i(DEFAULT_TAG, GenerateLogEnvironmentReport())
    }

    private fun GenerateLogEnvironmentReport(): String {
        val sb = StringBuilder()
        sb.append("*************** SLog Configuration Info ***************")
        sb.append("\n\t Log Category : ")
        sb.append(if (isShowInfo(SHOW_LOG_CATEGORY)) "yes" else "no")
        sb.append("\n\t File Information : ")
        sb.append(if (isShowInfo(SHOW_FILE_INFO)) "yes" else "no")
        sb.append("\n\t Class Information : ")
        sb.append(if (isShowInfo(SHOW_CLASS_INFO)) "yes" else "no")
        sb.append("\n\t Document Line Information : ")
        sb.append(if (isShowInfo(SHOW_LINE_INFO)) "yes" else "no")
        sb.append("\n\t Method Information : ")
        sb.append(if (isShowInfo(SHOW_METHOD_INFO)) "yes" else "no")
        sb.append("\n****************************************************")
        return sb.toString()
    }

    private fun isShowInfo(showEnElement: Int): Boolean {
        return DEFAULT_ELEMENT and showEnElement == showEnElement
    }

}