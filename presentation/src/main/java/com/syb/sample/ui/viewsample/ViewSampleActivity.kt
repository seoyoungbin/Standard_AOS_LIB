package com.syb.sample.ui.viewsample

import android.os.Bundle
import com.syb.syblibrary.data.local.pref.BasePreferenceHelper
import com.syb.syblibrary.ui.base.view.BaseActivity
import com.syb.syblibrary.ui.view.splash.SplashLayout
import com.syb.sample.R

class ViewSampleActivity : BaseActivity() {

    var splashLayout: SplashLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_sample)
        setUp()
    }

    override fun setUp()
    {
        splashLayout = findViewById(R.id.splash_layout)
        splashLayout?.imgLoad("https://www.elandretail.com/upload/20210607161020967_0.jpg")
        splashLayout?.hideSplash()
    }

    override fun getPreferenceHelper(): BasePreferenceHelper? = null

    override fun onDestroy() {
        splashLayout?.onDestroy()
        super.onDestroy()
    }
}