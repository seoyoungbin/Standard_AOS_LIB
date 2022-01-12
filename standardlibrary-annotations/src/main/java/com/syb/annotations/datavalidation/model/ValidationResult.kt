package com.syb.annotations.datavalidation.model

data class ValidationResult(
    var isValid: Boolean = true,
    val invalidFieldNameAndTags: MutableList<FieldNameAndTag> = mutableListOf()
)