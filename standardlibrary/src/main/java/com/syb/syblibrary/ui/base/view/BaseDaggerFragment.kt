package com.syb.syblibrary.ui.base.view

import android.content.Context
import dagger.android.support.AndroidSupportInjection

abstract class BaseDaggerFragment : BaseFragment()
{
    fun performDependencyInjection() = AndroidSupportInjection.inject(this)

    override fun onAttach(context: Context) {
        performDependencyInjection()
        super.onAttach(context)
    }

}