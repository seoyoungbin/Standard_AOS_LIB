package com.syb.syblibrary.common.stepchain

import android.content.Context
import androidx.annotation.NonNull

abstract class StepManager
{
    protected var context: Context
    internal var currentStepChain: StepChain? = null

    constructor(@NonNull context: Context)
    {
        this.context = context
    }

    abstract fun goStep()

    abstract fun stepResult(stepChain: BaseStepChain, next: Boolean)
}