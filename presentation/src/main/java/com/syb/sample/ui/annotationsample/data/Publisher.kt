package com.syb.sample.ui.annotationsample.data

import com.syb.annotations.datavalidation.DataValidation
import com.syb.annotations.datavalidation.MaxLength


@DataValidation
data class Publisher(
    @MaxLength(10, "publisher name is too long")
    val name: String
)