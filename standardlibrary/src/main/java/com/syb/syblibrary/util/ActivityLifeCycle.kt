package com.syb.syblibrary.util

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.syb.syblibrary.BaseApplication

/**
 * 액티비티 life cycle 관리 class
 */
class ActivityLifeCycle : ActivityLifecycleCallbacks {

    private var mAppStatus = AppStatus.BACKGROUND

    private var activity: Activity? = null

    // running activity count
    private var running = 0
    // 앱 background 진입 시간
    private var backgroundTime = 0L
    // 앱이 background에서 대기한 시간
    private var waitingInBackgroundTime = 0L
    // 노티피케이션  클릭 시 또는 Deep link 시 설정 가능
    private var externalEventLink = false

    /**
     * 앱 액티비티 life cycle  listener
     */
    interface AppStatusListener {
        fun returnedToForeground(returnedToForegroundTime: Long, externalEventLink: Boolean)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        this.activity = activity
    }

    override fun onActivityStarted(activity: Activity) {
        this.activity = activity

        if (++running == 1) {
            mAppStatus = AppStatus.RETURNED_TO_FOREGROUND
            waitingInBackgroundTime = System.currentTimeMillis() - getBackgroundTime()

            val libApplication = BaseApplication.baseApplication
            libApplication?.returnedToForeground(waitingInBackgroundTime, externalEventLink)

            if (activity is AppStatusListener)
                activity.returnedToForeground(waitingInBackgroundTime, externalEventLink)

        } else if (running > 1) {
            // 2 or more running activities,
            // should be foreground already.
            mAppStatus = AppStatus.FOREGROUND
        }

        externalEventLink = false
    }

    /**
     * Stop 상태 :  다른 액티비티가 올라와 가려짐 , onDestroy 호출 , 홈버튼 클릭
     * 현재 foreground 액티비티 Stop 시 호출
     *
     * @param activity Stop 액티비티
     */
    override fun onActivityStopped(activity: Activity) {
        if (--running == 0) {
            // no active activity
            // app goes to background
            mAppStatus = AppStatus.BACKGROUND
            backgroundTime = System.currentTimeMillis()
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (running == 0)
            this.activity = null
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    /**
     * 노티피케이션 클릭으로 앱 시작인지를  설정
     * true 값으로 설정 시  AppStatusListener 를 implements 한
     * Activity class 에  전달
     *
     * @param externalEventLink true : 노티클릭 or DeepLink
     */
    fun setExternalEventLink(externalEventLink: Boolean) {
        this.externalEventLink = externalEventLink
    }

    /**
     * @return 앱 background 진입 시간
     */
    fun getBackgroundTime() = backgroundTime

    /**
     * @return 앱이 background 에 대기한 시간
     */
    private fun getAppWaitingInBackgroundTime() = waitingInBackgroundTime

    /**
     * @return 현재  foreground Activity
     */
    fun getActivity() = activity

    /**
     * @return 현재 AppStatus
     */
    fun getAppStatus() = mAppStatus

    /**
     * @return 앱 foreground 상태
     */
    fun isForeground() = mAppStatus.ordinal > AppStatus.BACKGROUND.ordinal

    enum class AppStatus {
        /**
         * foreground 액티비티 0
         */
        BACKGROUND,

        /**
         * 앱 background 에서 foreground or 앱 시작 시
         */
        RETURNED_TO_FOREGROUND,

        /**
         * 액티비티  foreground
         */
        FOREGROUND
    }
}