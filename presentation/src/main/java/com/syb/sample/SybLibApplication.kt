package com.syb.sample

import com.syb.syblibrary.BaseDaggerApplication
import com.syb.sample.ui.daggersample.di.component.DaggerAppComponent
import dagger.android.HasAndroidInjector

class SybLibApplication : BaseDaggerApplication(), HasAndroidInjector
{

    companion object {

        @JvmStatic
        lateinit var instance: SybLibApplication

    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)

    }

    override fun returnedToForeground(returnedToForegroundTime: Long, externalEventLink: Boolean) {

    }
}