package com.syb.sample.ui.daggersample.di.component

import android.app.Application
import com.syb.sample.SybLibApplication
import com.syb.sample.ui.daggersample.di.builder.ActivityBuilder
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class,
    ActivityBuilder::class])
interface AppComponent
{
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: SybLibApplication)

}