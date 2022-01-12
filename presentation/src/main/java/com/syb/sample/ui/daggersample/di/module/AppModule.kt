package com.syb.sample.ui.daggersample.di.module

import android.app.Application
import android.content.Context
import com.syb.syblibrary.di.ApplicationContext
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class AppModule
{
    @Provides
    @ApplicationContext
    internal fun provideContext(application: Application): Context = application

    @Provides
    internal fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

}