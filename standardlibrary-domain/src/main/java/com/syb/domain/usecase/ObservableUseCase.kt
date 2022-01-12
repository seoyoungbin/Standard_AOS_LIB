package com.syb.domain.usecase

import com.syb.domain.Schedulers
import io.reactivex.Observable

abstract class ObservableUseCase<in Params, Result> constructor(private val schedulers: Schedulers) {

    abstract fun build(params: Params?): Observable<Result>

    fun execute(params: Params? = null): Observable<Result> {
        return build(params)
                .subscribeOn(schedulers.subscribeOn)
                .observeOn(schedulers.observeOn, true)
    }

}
