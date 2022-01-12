package com.syb.sample.ui.daggersample

import android.os.Bundle
import com.syb.syblibrary.data.local.pref.BasePreferenceHelper
import com.syb.syblibrary.ui.base.view.BaseDaggerActivity
import com.syb.sample.R
import com.syb.sample.ui.daggersample.isms.ISMSProtect
import javax.inject.Inject

class DaggerSampleActivity : BaseDaggerActivity() {

    @Inject
    lateinit var ismsProtect: ISMSProtect

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dagger_sample)
        setUp()
    }

    override fun setUp() {
        showLogInfo("ISMS Rooting Check => ${ismsProtect.rootingInspect()}")
    }

    override fun getPreferenceHelper(): BasePreferenceHelper? = null
}