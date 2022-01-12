package com.syb.sample.ui.basesample

import android.Manifest
import android.os.Bundle
import android.widget.Button
import com.syb.syblibrary.data.local.pref.BasePreferenceHelper
import com.syb.syblibrary.ui.base.view.BaseActivity
import com.syb.sample.R

class BaseSampleActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_sample)
        setUp()
    }

    override fun setUp() {

        findViewById<Button>(R.id.showLoading_btn).setOnClickListener {
            showLoading(true)
        }

        findViewById<Button>(R.id.hideLoading_btn).setOnClickListener {
            hideLoading()
        }

        findViewById<Button>(R.id.showDialog_btn).setOnClickListener {
            showDialog("테스트", false).show()
        }

        findViewById<Button>(R.id.showMessage_btn).setOnClickListener {
            showMessage("테스트")
        }

        findViewById<Button>(R.id.showLog_btn).setOnClickListener {
            showLogInfo("테스트")
        }

        findViewById<Button>(R.id.isNetConntected_btn).setOnClickListener {
            showLogInfo("네트워크 테스트 => ${isNetConnected()}")
        }

        findViewById<Button>(R.id.showPermissionFragment_btn).setOnClickListener {
            val permission = arrayOf(Manifest.permission.CAMERA)
            confirmPermissions(permission, "테스트입니다.")
        }

        findViewById<Button>(R.id.showFragment_btn).setOnClickListener {
            showBaseTestFragment()
        }

    }

    override fun getPreferenceHelper(): BasePreferenceHelper? = null

    fun showBaseTestFragment()
    {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.container,
            BaseSampleFragment()
        )
        transaction.commit()
    }

}