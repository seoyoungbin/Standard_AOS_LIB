package com.syb.syblibrary.service.fingerpush

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.syb.syblibrary.util.LibConstants

class FingerPushNotificationReceiver : BroadcastReceiver()
{
    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            val pushMessageData = intent?.getSerializableExtra(LibConstants.EXTRA_FINGER_PUSH_DATA) as FingerPushMessageData
            val intentActivity = intent?.getSerializableExtra(LibConstants.EXTRA_FINGER_PUSH_INTENT_ACTIVITY) as Class<Activity>

            val intent = Intent(context, intentActivity).apply {
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                if (!pushMessageData.weblink.isNullOrEmpty())
                    putExtra(LibConstants.EXTRA_FINGER_PUSH_WEB_URL, pushMessageData.weblink)
                if (!pushMessageData.inApp.isNullOrEmpty())
                    putExtra(LibConstants.EXTRA_FINGER_PUSH_INAPP, pushMessageData.inApp)
                action = Intent.ACTION_VIEW
            }
            context?.startActivity(intent)
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
    }
}