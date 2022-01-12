package com.syb.sample.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.syb.syblibrary.ui.view.toast.ToastView
import com.syb.sample.R
import com.syb.sample.SybLibApplication
import com.syb.sample.data.pref.PreferenceHelper
import com.syb.sample.data.pref.PreferenceHelperImpl
import com.syb.sample.ui.annotationsample.AnnotationSampleActivity
import com.syb.sample.ui.barcodesample.BarcodeSampleActivity
import com.syb.sample.ui.basesample.BaseSampleActivity
import com.syb.sample.ui.daggersample.DaggerSampleActivity
import com.syb.sample.ui.gpsdetectsample.GpsDetectSampleActivity
import com.syb.sample.ui.stepchainsample.StepChainSampleActivity
import com.syb.sample.ui.viewsample.ViewSampleActivity
import com.syb.sample.ui.webviewsample.WebViewSampleActivity
import com.syb.sample.util.AppConstants

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var preferenceHelper: PreferenceHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferenceHelper = PreferenceHelperImpl(SybLibApplication.instance, AppConstants.APP_PREF_FILE_NAME)

        setUp()
    }

    fun setUp()
    {
        findViewById<Button>(R.id.base_sample_btn).setOnClickListener(this)
        findViewById<Button>(R.id.view_sample_btn).setOnClickListener(this)
        findViewById<Button>(R.id.annotation_sample_btn).setOnClickListener(this)
        findViewById<Button>(R.id.webview_sample_btn).setOnClickListener(this)
        findViewById<Button>(R.id.dagger_sample_btn).setOnClickListener(this)
        findViewById<Button>(R.id.barcode_sample_btn).setOnClickListener(this)
        findViewById<Button>(R.id.gps_detect_sample_btn).setOnClickListener(this)
        findViewById<Button>(R.id.stepchain_sample_btn).setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        var intent: Intent? = when(view?.id)
        {
            R.id.base_sample_btn -> Intent(this, BaseSampleActivity::class.java)
            R.id.view_sample_btn -> Intent(this, ViewSampleActivity::class.java)
            R.id.annotation_sample_btn -> Intent(this, AnnotationSampleActivity::class.java)
            R.id.webview_sample_btn -> Intent(this, WebViewSampleActivity::class.java)
            R.id.dagger_sample_btn -> Intent(this, DaggerSampleActivity::class.java)
            R.id.barcode_sample_btn -> Intent(this, BarcodeSampleActivity::class.java)
            R.id.gps_detect_sample_btn -> Intent(this, GpsDetectSampleActivity::class.java)
            R.id.stepchain_sample_btn -> Intent(this, StepChainSampleActivity::class.java)
            else -> null
        }
        startActivity(intent)
    }

    override fun onBackPressed() {
        ToastView.backPressToast(this)
    }

    override fun onDestroy() {
        ToastView.onDestroy()
        super.onDestroy()
    }
}