package com.syb.syblibrary.service.gps

import android.os.Binder

class GpsDetectBinder(var gpsDetectService: GpsDetectService) : Binder()
{
    fun getService() = gpsDetectService
}