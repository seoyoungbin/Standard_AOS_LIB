package com.syb.domain.gateway

import com.syb.domain.entity.AppInfo
import io.reactivex.Observable

interface MainGateway {

    fun appInfo(): Observable<AppInfo>

}