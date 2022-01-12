package com.syb.syblibrary.service.fingerpush

enum class FingerPushCode(val code: String) {
    SUCCESS_A("200"), // 성공
    SUCCESS_B("201"), // 성공
    ERROR_A("403"), // App_key, secret 오류, 권한없음
    ERROR_B("404"), // 조회 대상 없음, 조회 결과 없음
    ERROR_C("500"), // 처리 중 에러
    ERROR_D("503"), // 필수 값 없음
    ERROR_E("504") // 이미 등록된 토큰
}