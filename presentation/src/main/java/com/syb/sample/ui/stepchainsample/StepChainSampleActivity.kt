package com.syb.sample.ui.stepchainsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.syb.syblibrary.util.log.YLog
import com.syb.sample.R
import com.syb.sample.ui.stepchainsample.step.AppStepManager

class StepChainSampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_chain_sample)

        val appStepManager =
            AppStepManager(
                this,
                object :
                    AppStepManager.StepFinishCallback {
                    override fun stepFinish(appInitStepManager: AppStepManager) {
                        YLog.i("AppStepManager stepFinish")
                    }
                })
        appStepManager.goStep()
    }
}