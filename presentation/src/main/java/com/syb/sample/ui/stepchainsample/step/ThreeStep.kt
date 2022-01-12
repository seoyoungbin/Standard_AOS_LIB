package com.syb.sample.ui.stepchainsample.step

import com.syb.syblibrary.common.stepchain.BaseStepChain
import com.syb.syblibrary.common.stepchain.StepChain
import com.syb.syblibrary.util.log.YLog

class ThreeStep : BaseStepChain()
{
    override fun start() {
        YLog.i("ThreeStep Start!!")
        nextStep()
    }

    override fun nextStep() {
        val next = nextStepChain != null
        stepManager?.stepResult(this, next)
        if(next) {
            context?.let { context ->
                stepManager?.let { stepManager ->
                    nextStepChain?.stepStart(context, stepManager)
                }
            }
        }
    }

    override fun setNextStep(nextStepChain: StepChain) {
        this.nextStepChain = nextStepChain
    }

    override fun getResult(): Any = "ThreeStepResult"
}