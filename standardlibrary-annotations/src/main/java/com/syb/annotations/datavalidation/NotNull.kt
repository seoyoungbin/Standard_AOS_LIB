package com.syb.annotations.datavalidation

/**
 * Not null constraint
 */
@Target(AnnotationTarget.FIELD)
annotation class NotNull(
    val tag: String
)
