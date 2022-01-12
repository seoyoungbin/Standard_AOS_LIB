package com.syb.syblibrary.util

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.util.Base64
import androidx.annotation.NonNull
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.syb.syblibrary.util.log.YLog
import java.io.File
import java.security.MessageDigest

object AppUtil {


    /**
     * 전화 기능 가능 여부
     * @param context 컨텍스
     */
    fun isCallAbleDevice(context: Context): Boolean {
        val pkManager = context.packageManager
        return pkManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)
    }

    /**
     * 앱 버전 코드 반환
     * @param context 컨텍스트
     */
    fun getAppVersionCode(context: Context): Int {
        try {
            val i = context.packageManager.getPackageInfo(context.packageName, 0)
            val versionCode = i.versionCode
            return versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return -1
    }

    /**
     * 앱 버전 이름 반환
     * @param context 컨텍스트
     */
    fun getAppVersionName(context: Context): String {
        try {
            val i = context.packageManager.getPackageInfo(context.packageName, 0)
            val versionName = i.versionName.toString()
            return versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * SDK 버전 반환
     */
    fun getOsApiLevel() = Build.VERSION.SDK_INT

    /**
     * 디바이스 모델명 반환
     */
    fun geDeviceModelName(): String = Build.MODEL

    /**
     * context에 따른 현재 클래스명 반환
     * @param context 컨텍스트
     */
    fun getLauncherClassName(@NonNull context: Context): String {
        var className = ""
        val pm = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val infoList = pm.queryIntentActivities(intent, 0)
        for (resolveInfo in infoList) {
            val pkgName = resolveInfo.activityInfo.applicationInfo.packageName
            if (pkgName.equals(context.packageName, ignoreCase = true))
                className = resolveInfo.activityInfo.name
        }
        return className
    }

    /**
     * 네트워크 연결여부 확인
     * @param context 컨텍스트
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isNetConnected(context: Context): Boolean {
        val isConnect: Boolean
        val connectivityManager = context.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            isConnect = true
            if (networkInfo.type == ConnectivityManager.TYPE_WIFI)
                YLog.i("WIFI 네트워크 연결")
            else if (networkInfo.type == ConnectivityManager.TYPE_MOBILE)
                YLog.i("모바일(3G/LTE) 네트워크에 연결됨")
        } else
            isConnect = false
        return isConnect
    }

    /**
     * 서비스 실행여부 확인
     * @param context 컨텍스트
     * @param className 서비스 클래스명
     * @return 서비스 실행여부
     */
    fun isServiceRunning(context: Context, className: String): Boolean
    {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (className == service.service.className)
                return true
        }
        return false
    }

    /**
     * 루팅 여부 확인
     * @return 루팅 여부
     */
    fun isRootingDevice(): Boolean {
        val buildTags = Build.TAGS
        if (buildTags != null && buildTags.contains("test-keys"))
            return true

        if (File("/system/app/Superuser.apk").exists())
            return true

        try {
            Runtime.getRuntime().exec("su")
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * 앱 빌드한 사이닝키 해시키 값 반환
     * @param context 컨텍스트
     * @return 앱 해시키
     */
    fun getAppHashKey(context: Context): String
    {
        try {
            val info = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = Base64.encodeToString(md.digest(), Base64.DEFAULT).trim()
                return hashKey
            }
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
        return ""
    }


    /**
     * 버전 업데이트 확인
     * @param context 컨텍스트
     * @param uVersion 업데이트 버전
     * @return 업데이트 여부
     */
    fun versionUpdate(context: Context, uVersion: String): Boolean {
        try {
            var cVersion = getAppVersionName(context)
            var cVersionDivide = cVersion.split(".")
            var uVersionDivide = uVersion.split(".")
            if(cVersionDivide.size != 0 && uVersionDivide.size != 0 && cVersionDivide.size == uVersionDivide.size) {
                for (i in uVersionDivide.indices) {
                    if (uVersionDivide[i].toInt() > cVersionDivide[i].toInt())
                        return true
                    else if (uVersionDivide[i].toInt() < cVersionDivide[i].toInt())
                        return false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 앱 설치 여부 확인
     * @param context 컨텍스트
     * @param packgeName 설치 여부 확인할 패키지명
     * @return 앱 설치 여부
     */
    fun isAppInstall(context: Context, packgeName: String): Boolean
    {
        try {
            val startLink = context.packageManager.getLaunchIntentForPackage(packgeName)
            return startLink != null
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 상태바 색깔 변경
     * @param activity 해당 화면
     * @param colorId color Resource ID
     */
    @TargetApi(LOLLIPOP)
    fun setStatusBarColor(activity: Activity, colorId: Int)
    {
        activity.window.statusBarColor = ContextCompat.getColor(activity, colorId)
    }

    /**
     * 상태바 색깔 변경
     * @param activity 해당 화면
     * @param colorString color String
     */
    @TargetApi(LOLLIPOP)
    fun setStatusBarColor(activity: Activity, colorString: String)
    {
        activity.window.statusBarColor = Color.parseColor(colorString)
    }

    /**
     * 스크린 화면 밝기 최대
     * @param activity 현재 액티비티
     */
    fun screenBrightnessMax(activity: Activity)
    {
        val layoutParams = activity.window.attributes
        layoutParams.screenBrightness = 1f
        activity.window.attributes = layoutParams
    }

    /**
     * 앱 재실행 작업
     * @param context 컨텍스트
     */
    fun restartApp(context: Context)
    {
        try {
            val packageManager = context.packageManager
            val intent = packageManager.getLaunchIntentForPackage(context.packageName)
            val componentName = intent?.component
            val mainIntent = Intent.makeRestartActivityTask(componentName)
            context.startActivity(mainIntent)
            System.exit(0)
        }catch (e: Exception)
        {
            e.printStackTrace()
        }

    }

    /**
     * 현재 앱 실행여부 확인
     * @param context 컨텍스트
     * @return 현재 앱 실행여부
     */
    fun isAppRunning(context: Context): Boolean
    {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val procInfos = activityManager.runningAppProcesses
        for(proc in procInfos)
        {
            if(proc.processName.equals(context.packageName))
                return true
        }
        return false
    }


}