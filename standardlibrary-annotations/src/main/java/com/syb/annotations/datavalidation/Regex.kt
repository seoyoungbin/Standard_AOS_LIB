package com.syb.annotations.datavalidation

/**
 * String field regex match constraint
 */
@Target(AnnotationTarget.FIELD)
annotation class Regex(
    val regex: String,
    val tag: String
)
