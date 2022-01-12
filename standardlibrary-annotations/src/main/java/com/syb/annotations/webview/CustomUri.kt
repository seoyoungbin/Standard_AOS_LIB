package com.syb.annotations.webview

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CustomUri(
    val scheme: String = ""
)
