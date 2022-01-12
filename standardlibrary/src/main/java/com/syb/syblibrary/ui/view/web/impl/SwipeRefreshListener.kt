package com.syb.syblibrary.ui.view.web.impl

import androidx.annotation.NonNull
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * 웹뷰에서  swipeRefresh 발생 시 listener
 */
interface SwipeRefreshListener
{
    fun swipeRefresh(@NonNull swipeRefreshLayout: SwipeRefreshLayout)
}