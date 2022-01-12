package com.syb.domain.usecase

import com.syb.domain.Schedulers
import io.reactivex.Completable

abstract class CompletableUseCase internal constructor(private val schedulers: Schedulers) {

    abstract fun build(): Completable

    fun execute(): Completable {
        return build()
                .subscribeOn(schedulers.subscribeOn)
                .observeOn(schedulers.observeOn)
    }

}
