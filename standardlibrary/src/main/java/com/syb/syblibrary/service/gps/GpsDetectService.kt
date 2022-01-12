package com.syb.syblibrary.service.gps

import android.Manifest
import android.app.Service
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.syb.syblibrary.util.log.YLog

class GpsDetectService : Service(), LocationListener {

    companion object {
        const val INTENT_ACTION_GPS_LOCATION_UPDATE = "INTENT_ACTION_GPS_LOCATION_UPDATE"
        const val INTENT_BUNDLE_LOCATION = "INTENT_BUNDLE_LOCATION"
        const val INTENT_EXTRA_LOCATION = "INTENT_EXTRA_LOCATION"
    }

    val minUpdateTime = (1000 * 10 * 1).toLong() // 10초 단위 갱신
    val minUpdateDistance = 5.0f // 5m 단위 갱신

    private var mContext: Context? = null
    private var locationManager: LocationManager? = null
    private var gpsDetectInterface: GpsDetectInterface? = null
    private var gpsDetectBroadcastReceiver: GpsDetectBroadcastReceiver? = null
    private var localBroadcastManager: LocalBroadcastManager? = null
    private var isCallStopService = false
    var mBinder: GpsDetectBinder = GpsDetectBinder(this)
    var mService: GpsDetectService? = null
    var isService = false

    private var serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val mb = p1 as GpsDetectBinder
            mService = mb.getService()
            isService = true
            if(isCallStopService)
                stopService()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isService = false
        }
    }

    override fun onBind(intent: Intent?): IBinder? = mBinder

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onCreate() {
        super.onCreate()
        initLocation()
    }

    /**
     * 서비스 실행
     * @param context 컨텍스트
     * @param gpsDetectInterface Gps 감지 인터페이스
     */
    fun startService(context: Context, gpsDetectInterface: GpsDetectInterface)
    {
        mContext = context
        this.gpsDetectInterface = gpsDetectInterface
        localBroadcastManager = LocalBroadcastManager.getInstance(context)
        registerGpsReceiver()
        val intent = Intent(context, GpsDetectService::class.java)
        mContext?.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }


    /**
     * 서비스 종료
     */
    fun stopService()
    {
        isCallStopService = true
        gpsDetectInterface = null
        gpsDetectBroadcastReceiver?.release()
        unRegisterGpsReceiver()
        locationManager?.removeUpdates(this)
        if(isService)
        {
            mContext?.unbindService(serviceConnection)
            isService = false
        }
    }

    /**
     * GpsReceiver 등록!!
     */
    fun registerGpsReceiver() {
        gpsDetectInterface?.let {
            if (gpsDetectBroadcastReceiver == null)
                gpsDetectBroadcastReceiver = GpsDetectBroadcastReceiver(it)
        }
        gpsDetectBroadcastReceiver?.let {
            localBroadcastManager?.registerReceiver(it, IntentFilter(INTENT_ACTION_GPS_LOCATION_UPDATE))
        }
    }

    /**
     * GpsReceiver 해제!!
     */
    fun unRegisterGpsReceiver()
    {
        gpsDetectBroadcastReceiver?.let {
            localBroadcastManager?.unregisterReceiver(it)
        }
    }

    /**
     * 초기 위치정보 가져오기
     */
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun initLocation() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    return
            }

            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val isGpsOn = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false // 센서
            val isNetOn = locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ?: false // WIFI
            val isPassOn = locationManager?.isProviderEnabled(LocationManager.PASSIVE_PROVIDER) ?: false // 기지국

            if (!isGpsOn && !isNetOn && !isPassOn)
                return

            if (isGpsOn) {
                locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, minUpdateTime, minUpdateDistance, this)
                val location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                location?.let {
                    sendLocation(it)
                    YLog.i("GpsDetectService initLocation(isGpsOn) => ${it}")
                }
            }
            if (isNetOn) {
                locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minUpdateTime, minUpdateDistance, this)
                val location = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                location?.let {
                    sendLocation(it)
                    YLog.i("GpsDetectService initLocation(isNetOn) => ${it}")
                }
            }
            if (isPassOn) {
                locationManager?.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, minUpdateTime, minUpdateDistance, this)
                val location = locationManager?.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
                location?.let {
                    sendLocation(it)
                    YLog.i("GpsDetectService initLocation(isPassOn) => ${it}")
                }
            }
        }catch (e: Exception)
        {
            e.printStackTrace()
        }

    }

    override fun onLocationChanged(location: Location) {
        location?.let {
            sendLocation(it)
            YLog.i("GpsDetectService onLocationChanged => ${it}")
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    /**
     * Location 데이터 전달
     * @param location 위치정보
     */
    private fun sendLocation(location: Location)
    {
        val intent = Intent(INTENT_ACTION_GPS_LOCATION_UPDATE).apply {
            val bundle = Bundle().apply {
                putParcelable(INTENT_BUNDLE_LOCATION, location)
            }
            putExtra(INTENT_EXTRA_LOCATION, bundle)
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

}