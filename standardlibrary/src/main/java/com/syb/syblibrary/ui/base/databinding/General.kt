package com.syb.syblibrary.ui.base.databinding

fun <T> lazyThreadSafetyNone(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)