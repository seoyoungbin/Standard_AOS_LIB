package com.syb.annotations.datavalidation

/**
 * Number maximum value
 */
@Target(AnnotationTarget.FIELD)
annotation class MaxValue(
    val value: Long,
    val tag: String
)
