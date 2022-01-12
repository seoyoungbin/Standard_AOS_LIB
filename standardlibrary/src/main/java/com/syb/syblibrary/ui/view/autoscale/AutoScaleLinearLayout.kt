package com.syb.syblibrary.ui.view.autoscale

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.syb.syblibrary.util.DeviceUtil

class AutoScaleLinearLayout : LinearLayout {

    constructor(context: Context) : super(context) {
        initialzeView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialzeView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialzeView(context)
    }

    private fun initialzeView(context: Context) {

        var heightSize = height.toFloat()
        heightSize *= DeviceUtil.getDeviceHeightMultiple(context)
        val param = LayoutParams(width, heightSize.toInt())
        layoutParams = param
    }

}