package com.syb.domain.entity

data class AppInfo(
    var verinfo: String = "",
    // C: 선택 / R: 필수
    var uptforce: String = "",
    var splash: String = ""
): BaseEntity()