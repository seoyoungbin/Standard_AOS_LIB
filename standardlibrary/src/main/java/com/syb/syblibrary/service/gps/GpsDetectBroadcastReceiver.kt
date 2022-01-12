package com.syb.syblibrary.service.gps

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location


class GpsDetectBroadcastReceiver : BroadcastReceiver
{

    var gpsDetectInterface: GpsDetectInterface? = null

    constructor(gpsDetectInterface: GpsDetectInterface)
    {
        this.gpsDetectInterface = gpsDetectInterface
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            intent?.getBundleExtra(GpsDetectService.INTENT_EXTRA_LOCATION)?.let { bundle ->
                bundle?.getParcelable<Location>(GpsDetectService.INTENT_BUNDLE_LOCATION)?.let { location ->
                        gpsDetectInterface?.locationUpdate(location)
                    }
            }
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    /**
     * 데이터 전달 해제
     */
    fun release()
    {
        gpsDetectInterface = null
    }
}