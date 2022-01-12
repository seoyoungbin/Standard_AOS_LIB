package com.syb.sample.ui.basesample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.syb.syblibrary.data.local.pref.BasePreferenceHelper
import com.syb.syblibrary.ui.base.view.BaseFragment
import com.syb.sample.R

class BaseSampleFragment : BaseFragment() {

    var fv: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fv = inflater.inflate(R.layout.fragment_base_sample, container, false)
        setUp()
        return fv
    }

    override fun setUp() {
        fv?.findViewById<Button>(R.id.showLoading_btn)?.setOnClickListener {
            showLoading(true)
        }

        fv?.findViewById<Button>(R.id.hideLoading_btn)?.setOnClickListener {
            hideLoading()
        }

        fv?.findViewById<Button>(R.id.showDialog_btn)?.setOnClickListener {
            showDialog("테스트", false)?.show()
        }

        fv?.findViewById<Button>(R.id.showMessage_btn)?.setOnClickListener {
            showMessage("테스트")
        }

        fv?.findViewById<Button>(R.id.showLog_btn)?.setOnClickListener {
            showLogInfo("테스트")
        }

        fv?.findViewById<Button>(R.id.isNetConntected_btn)?.setOnClickListener {
            showLogInfo("네트워크 테스트 => ${isNetConnected()}")
        }
    }

    override fun getPreferenceHelper(): BasePreferenceHelper? = null
}