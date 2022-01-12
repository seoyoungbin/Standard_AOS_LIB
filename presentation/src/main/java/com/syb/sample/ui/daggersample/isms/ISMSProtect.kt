package com.syb.sample.ui.daggersample.isms

import java.io.File

class ISMSProtect
{
    // 루팅 체크 로직
    fun rootingInspect(): Boolean {
        val arrayOfString = arrayOf(
            "/system/bin/su"
            , "/system/xbin/su"
            , "/system/app/SuperUser.apk"
            , "/system/bin/.ext/.su"
            , "/system/usr/su-backup"
            , "/system/sd/xbin/su"
            , "/system/bin/failsafe/su"
            , "/system/su"
            , "/data/local/su"
            , "/data/data/com.noshufou.android.su"
            , "/data/local/xbin/su"
            , "/data/local/bin/su"
            , "/sbin/su"
            , "/su/bin/su"
        )

        var i = 0

        while (true) {
            if (i >= arrayOfString.size)
                return false
            if (File(arrayOfString[i]).exists()) {
                return true
            }
            i++
        }
    }
}