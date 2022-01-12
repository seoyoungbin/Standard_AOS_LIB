package com.syb.sample.service.fingerpush

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.fingerpush.android.FingerNotification
import com.fingerpush.android.FingerPushFcmListener
import com.syb.syblibrary.service.fingerpush.FingerPushNotificationHelper
import com.syb.sample.R
import com.syb.sample.ui.main.MainActivity

class PushService : FingerPushFcmListener() {

    protected var notiChannelName = "LibChannelName"
    protected var notiChannelId = "LibChannelId"
    @DrawableRes
    protected var notiIconRes: Int = R.mipmap.ic_launcher
    @DrawableRes
    protected var notiLargeIconRes: Int = R.mipmap.ic_launcher
    @ColorInt
    protected var notiColor: Int = Color.rgb(0, 114, 162)
    @ColorInt
    protected var notiLight: Int? = null
    protected var notiVibrate: LongArray = longArrayOf(0, 500, 600, 1000)

    private var fingerPushNotificationHelper: FingerPushNotificationHelper = FingerPushNotificationHelper()

    override fun onMessage(context: Context?, bundle: Bundle?) {
        context?.let {
            bundle?.let {
                val fingerPushMessageData = fingerPushNotificationHelper.getPushMessageData(bundle)
                val pi = fingerPushNotificationHelper.getPendingIntent(context, fingerPushMessageData, MainActivity::class.java)

                FingerNotification(this).apply {
                    setNotificationIdentifier(System.currentTimeMillis().toInt())
                    setIcon(notiIconRes)
                    setLargeIcon(BitmapFactory.decodeResource(resources, notiLargeIconRes))
                    setVibrate(notiVibrate)
                    setLights(notiLight ?: getColor(R.color.color_white), 500, 500)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        setColor(notiColor)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        createChannel(notiChannelName, notiChannelId)
                    showNotification(bundle, pi)
                }
            }
        }
    }

}