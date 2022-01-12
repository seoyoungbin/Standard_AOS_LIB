package com.syb.annotations.buildinject

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class BuildInject(
        val release: String = "",
        val debug: String = ""
)
