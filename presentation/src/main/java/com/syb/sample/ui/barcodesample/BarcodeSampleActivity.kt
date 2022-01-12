package com.syb.sample.ui.barcodesample

import android.os.Bundle
import android.widget.Button
import com.syb.syblibrary.data.local.pref.BasePreferenceHelper
import com.syb.syblibrary.ui.base.view.BaseActivity
import com.syb.syblibrary.ui.view.barcodescan.BarcodeScanFragment
import com.syb.syblibrary.ui.view.barcodescan.BarcodeScanListener
import com.syb.syblibrary.util.log.YLog
import com.syb.sample.R

class BarcodeSampleActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_sample)
        setUp()
    }

    override fun setUp() {

        findViewById<Button>(R.id.showBarcodeScan_btn).setOnClickListener {
            if (confirmPermissions((arrayOf(android.Manifest.permission.CAMERA)), "테스트입니다.")) {
                BarcodeScanFragment(object : BarcodeScanListener {
                    override fun scanSuccess(scanData: String) {
                        YLog.i("Barcode Scan Success Data => ${scanData}")
                    }
                }).show(supportFragmentManager, null)
            }
        }

    }

    override fun getPreferenceHelper(): BasePreferenceHelper? = null
}