package com.syb.syblibrary.util

import java.util.regex.Pattern

object RegexUtil
{
    /**
     * 핸드폰번호 패턴 확인
     * @param s 검증할 문자열
     * @return 패턴 매치여부
     */
    fun isPhoneNumber(s: String): Boolean
    {
        try {
            val regExp = "^01(?:0|1|2|[6-9])[.-]?(\\d{3,4})[.-]?(\\d{4})$"
            val pattern = Pattern.compile(regExp)
            val match = pattern.matcher(s)
            return if(match.find()) true else false
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 일반전화번호 패턴 확인
     * @param s 검증할 문자열
     * @return 패턴 매치여부
     */
    fun isRegularPhoneNumber(s: String): Boolean
    {
        try {
            val regExp = "^0(?:2|32)[.-]?(\\d{3,4})[.-]?(\\d{4})$"
            val pattern = Pattern.compile(regExp)
            val match = pattern.matcher(s)
            return if(match.find()) true else false
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
        return false
    }

}