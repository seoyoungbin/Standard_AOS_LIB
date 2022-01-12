package com.syb.sample.ui.gpsdetectsample

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.syb.syblibrary.service.gps.GpsDetectInterface
import com.syb.syblibrary.service.gps.GpsDetectService
import com.syb.syblibrary.util.log.YLog
import com.syb.sample.R

class GpsDetectSampleActivity : AppCompatActivity() {

    var gpsDetectService: GpsDetectService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gps_detect_sample)
        gpsDetectService = GpsDetectService()
        gpsDetectService?.startService(this, object : GpsDetectInterface {
            override fun locationUpdate(location: Location) {
                YLog.i("Location Update Data => ${location}")
            }
        })
    }

    override fun onResume() {
        super.onResume()
        gpsDetectService?.registerGpsReceiver()
    }

    override fun onPause() {
        gpsDetectService?.unRegisterGpsReceiver()
        super.onPause()
    }

    override fun onDestroy() {
        gpsDetectService?.stopService()
        super.onDestroy()
    }
}