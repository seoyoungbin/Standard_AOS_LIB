package com.syb.sample.ui.daggersample.di.builder

import com.syb.sample.ui.daggersample.DaggerSampleActivityModule
import com.syb.sample.ui.daggersample.DaggerSampleActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder
{
    @ContributesAndroidInjector(modules = [DaggerSampleActivityModule::class])
    abstract fun bindDaggerSampleActivity(): DaggerSampleActivity
}