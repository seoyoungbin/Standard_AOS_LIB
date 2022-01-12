package com.syb.annotations.datavalidation

/**
 * String field maximum length constraint
 */
@Target(AnnotationTarget.FIELD)
annotation class MaxLength(
    val length: Int,
    val tag: String
)
