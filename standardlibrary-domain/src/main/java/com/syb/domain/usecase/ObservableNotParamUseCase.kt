package com.syb.domain.usecase

import com.syb.domain.Schedulers
import io.reactivex.Observable

abstract class ObservableNotParamUseCase<Result> constructor(private val schedulers: Schedulers) {

    abstract fun build(): Observable<Result>

    fun execute(): Observable<Result> {
        return build()
                .subscribeOn(schedulers.subscribeOn)
                .observeOn(schedulers.observeOn, true)
    }

}
