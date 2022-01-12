package com.syb.syblibrary

import androidx.multidex.MultiDexApplication
import com.syb.syblibrary.util.DeviceUtil

abstract class BaseApplication : MultiDexApplication() {

    companion object{
        var baseApplication: BaseApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        baseApplication = this

        DeviceUtil.create(this)
    }

    abstract fun returnedToForeground(returnedToForegroundTime: Long, externalEventLink: Boolean)

}