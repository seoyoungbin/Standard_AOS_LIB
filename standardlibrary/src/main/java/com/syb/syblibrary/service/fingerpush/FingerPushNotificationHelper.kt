package com.syb.syblibrary.service.fingerpush

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.NonNull
import com.syb.syblibrary.util.AppUtil
import com.syb.syblibrary.util.LibConstants
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

class FingerPushNotificationHelper {

    /**
     * 핑거푸시 메시지 데이터 파싱
     * @param bundle FingerPush Bundle 값
     * @return 핑거푸시 메시지 데이터
     */
    fun getPushMessageData(bundle: Bundle?): FingerPushMessageData {
        val pushMessageData = FingerPushMessageData()
        bundle?.let {
            with(pushMessageData) {
                this.appTitle = it.getString("data.appTitle") ?: ""
                this.badge = it.getString("data.badge")?.toInt() ?: 0
                try {
                    this.code = URLDecoder.decode(it.getString("data.code"), "UTF-8")
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }
                this.img = it.getString("data.img")?.toInt() ?: 0
                this.imgUrl = it.getString("data.imgUrl") ?: ""
                this.labelCode = it.getString("data.labelCode") ?: ""
                this.message = it.getString("data.message") ?: ""
                this.msgTag = it.getString("data.msgTag") ?: ""
                this.time = System.currentTimeMillis()
                this.title = it.getString("data.title") ?: ""
                this.weblink = it.getString("data.weblink") ?: ""
                this.inApp = it.getString("data.InApp") ?: "Y"
            }
        }
        return pushMessageData
    }

    /**
     * Notification 생성
     * @param context 컨텍스
     * @param pushMessageData 푸시 데이터
     * @param intentActivity 이동 액티비티
     * @return PendingIntent 값
     */
    fun <E: Activity> getPendingIntent(context: Context, @NonNull pushMessageData: FingerPushMessageData, intentActivity: Class<E>): PendingIntent
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            val badgeIntent = Intent("android.intent.action.BADGE_COUNT_UPDATE").apply {
                putExtra("badge_count", pushMessageData.badge)
                putExtra("badge_count_package_name", context.packageName)
                putExtra(
                    "badge_count_class_name",
                    AppUtil.getLauncherClassName(context.applicationContext)
                )
            }
            context.sendBroadcast(badgeIntent)
        }

        val intent = Intent(context, FingerPushNotificationReceiver::class.java).apply {
            putExtra(LibConstants.EXTRA_FINGER_PUSH_DATA, pushMessageData)
            putExtra(LibConstants.EXTRA_FINGER_PUSH_INTENT_ACTIVITY, intentActivity)
        }

        val pi = PendingIntent.getBroadcast(
            context,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        return pi
    }

}