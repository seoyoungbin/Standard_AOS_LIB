package com.syb.sample.ui.daggersample

import android.app.Application
import android.content.Context
import com.syb.syblibrary.ui.base.view.BaseView
import com.syb.syblibrary.ui.base.viewmodel.BaseViewModel

class DaggerSampleViewModel<V: BaseView>(
    context: Context
) : BaseViewModel<V>(context.applicationContext as Application)
{

}