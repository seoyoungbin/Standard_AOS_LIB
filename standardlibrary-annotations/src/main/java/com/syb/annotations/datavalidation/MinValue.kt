package com.syb.annotations.datavalidation

/**
 * Number minimum value
 */
@Target(AnnotationTarget.FIELD)
annotation class MinValue(
    val value: Long,
    val tag: String
)
