package com.syb.syblibrary.ui.view.autoscale

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatEditText
import com.syb.syblibrary.util.DeviceUtil

class AutoScaleEditText : AppCompatEditText {

    var mTextSize = 33F // 초기 세팅 10SP

    constructor(context: Context) : super(context) {
        initialzeView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialzeView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialzeView(context)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        setDisplayTextSize()
    }

    private fun initialzeView(context: Context) {

        mTextSize = textSize
        setDisplayTextSize()

    }

    /**
     * 화면에 맞게 텍스트 사이즈 변경
     */
    private fun setDisplayTextSize()
    {
        var textSize = DeviceUtil.pxToSp(context, mTextSize)
        textSize *= DeviceUtil.getDeviceHeightMultiple(context)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
    }

}