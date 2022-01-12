package com.syb.domain

import io.reactivex.Scheduler


interface Schedulers {

    val subscribeOn: Scheduler

    val observeOn: Scheduler

}
