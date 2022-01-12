package com.syb.syblibrary.ui.view.web.fullscreen

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.core.content.ContextCompat

class FullScreenLayout : FrameLayout {

    constructor(context: Context) : super(context) {
        initialzeView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialzeView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialzeView(context, attrs)
    }

    private fun initialzeView(context: Context, attrs: AttributeSet?) {
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.black))
    }

    override fun onTouchEvent(evt: MotionEvent): Boolean {
        return true
    }

}