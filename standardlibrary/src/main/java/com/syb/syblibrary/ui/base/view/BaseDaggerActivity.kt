package com.syb.syblibrary.ui.base.view

import android.os.Bundle
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

abstract class BaseDaggerActivity: BaseActivity(), HasAndroidInjector
{

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = fragmentInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        performDI()
        super.onCreate(savedInstanceState)
    }

    fun performDI() {
        AndroidInjection.inject(this)
    }
}