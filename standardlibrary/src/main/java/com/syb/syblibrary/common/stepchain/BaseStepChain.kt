package com.syb.syblibrary.common.stepchain

import android.content.Context

abstract class BaseStepChain : StepChain {
    protected var context: Context? = null
    protected var stepManager: StepManager? = null
    protected var nextStepChain: StepChain? = null

    override fun stepStart(context: Context, stepManager: StepManager) {
        this.context = context
        this.stepManager = stepManager
        this.stepManager?.currentStepChain = this
        start()
    }

    protected abstract fun start()

    protected abstract fun nextStep()
}