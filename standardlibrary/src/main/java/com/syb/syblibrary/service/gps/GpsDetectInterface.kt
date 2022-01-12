package com.syb.syblibrary.service.gps

import android.location.Location

interface GpsDetectInterface {

    fun locationUpdate(location: Location)

}