package com.syb.sample.ui.stepchainsample.step

import android.content.Context
import com.syb.syblibrary.common.stepchain.BaseStepChain
import com.syb.syblibrary.common.stepchain.StepManager
import com.syb.syblibrary.util.log.YLog

class AppStepManager : StepManager {

    private var stepFinishCallback: StepFinishCallback
    private var oneStep: OneStep
    private var twoStep: TwoStep
    private var threeStep: ThreeStep

    constructor(context: Context, callback: StepFinishCallback): super(context)
    {
        stepFinishCallback = callback
        oneStep = OneStep()
        twoStep = TwoStep()
        threeStep = ThreeStep()

        oneStep.setNextStep(twoStep)
        twoStep.setNextStep(threeStep)

    }

    override fun goStep() {
        oneStep.stepStart(context, this)
    }

    override fun stepResult(stepChain: BaseStepChain, next: Boolean) {
        if(stepChain is OneStep)
            YLog.i("OneStep stepResult => ${stepChain.getResult()}")

        if(stepChain is TwoStep)
            YLog.i("TwoStep stepResult => ${stepChain.getResult()}")

        if(stepChain is ThreeStep)
            YLog.i("ThreeStep stepResult => ${stepChain.getResult()}")

        if(!next)
            stepFinishCallback?.stepFinish(this)
    }

    /**
     * AppStepManager callBack listener
     */
    interface StepFinishCallback {
        fun stepFinish(appInitStepManager: AppStepManager)
    }
}