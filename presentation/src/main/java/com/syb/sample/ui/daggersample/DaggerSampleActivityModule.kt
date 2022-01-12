package com.syb.sample.ui.daggersample

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.syb.syblibrary.di.ApplicationContext
import com.syb.syblibrary.ui.base.view.BaseView
import com.syb.sample.ui.daggersample.isms.ISMSProtect
import dagger.Module
import dagger.Provides

@Module
class DaggerSampleActivityModule {


    @Provides
    internal fun provideViewModelFactory(
        @ApplicationContext context: Context
    ): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return when {

                    modelClass.isAssignableFrom(DaggerSampleViewModel::class.java) ->
                        DaggerSampleViewModel<BaseView>(
                            context
                        ) as T

                    else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            }
        }
    }

    @Provides
    internal fun provideIsmsProtect(): ISMSProtect = ISMSProtect()

}