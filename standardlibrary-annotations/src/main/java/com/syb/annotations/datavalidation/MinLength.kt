package com.syb.annotations.datavalidation

/**
 * String field minimum length constraint
 */
@Target(AnnotationTarget.FIELD)
annotation class MinLength(
    val length: Int,
    val tag: String
)
