package com.syb.syblibrary.ui.base.viewpager

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.viewpager.widget.ViewPager

abstract class BaseViewPager : ViewPager {

    var isPagingEnabled = true

    constructor(context: Context) : super(context) {
        initialzeView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialzeView()
    }

    private fun initialzeView() {

    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean = isPagingEnabled && super.onInterceptTouchEvent(event)


    override fun onTouchEvent(event: MotionEvent?): Boolean =
        try {
            isPagingEnabled && super.onTouchEvent(event)
        }catch (e: IllegalArgumentException)
        {
            e.printStackTrace()
            false
        }

    override fun canScroll(v: View?, checkV: Boolean, dx: Int, x: Int, y: Int): Boolean = isPagingEnabled && super.canScroll(v, checkV, dx, x, y)

}