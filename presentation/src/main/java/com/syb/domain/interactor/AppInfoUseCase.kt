package com.syb.domain.interactor

import com.syb.domain.Schedulers
import com.syb.domain.entity.AppInfo
import com.syb.domain.gateway.MainGateway
import com.syb.domain.usecase.ObservableNotParamUseCase
import io.reactivex.Observable

class AppInfoUseCase(schedulers: Schedulers, private val mainGateway: MainGateway): ObservableNotParamUseCase<AppInfo>(schedulers) {

    override fun build(): Observable<AppInfo> = mainGateway.appInfo()
}