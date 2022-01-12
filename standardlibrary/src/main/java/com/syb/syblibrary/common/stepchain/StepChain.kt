package com.syb.syblibrary.common.stepchain

import android.content.Context
import androidx.annotation.NonNull

interface StepChain {
    fun setNextStep(nextStepChain: StepChain)
    fun stepStart(@NonNull context: Context, stepManager: StepManager)
    fun getResult(): Any
}