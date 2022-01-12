package com.syb.domain.usecase

import com.syb.domain.Schedulers

abstract class InterfaceUseCase<in Params, Result> constructor(private val schedulers: Schedulers) {

    abstract fun build(params: Params?)

    fun execute(params: Params? = null){
        build(params)
    }
}
