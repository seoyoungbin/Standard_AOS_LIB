package com.syb.syblibrary.ui.view.underline

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.syb.syblibrary.R
import com.syb.syblibrary.ui.view.autoscale.AutoScaleTextView

class UnderlineTextView : AutoScaleTextView {

    private var underlineColor = 0
    private var underlineWidth = 0

    private var paint: Paint? = null

    private var width = 0F
    private var height = 0F

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

        underlineColor = Color.TRANSPARENT
        underlineWidth = 0

        val a = context.obtainStyledAttributes(attrs, R.styleable.UnderlineTextView)
        underlineColor = a.getColor(R.styleable.UnderlineTextView_underlineColor, Color.TRANSPARENT)
        underlineWidth = a.getDimensionPixelSize(R.styleable.UnderlineTextView_underlineWidth, 0)
        a.recycle()

        paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isUnderlineText = true
            style = Paint.Style.STROKE
            color = underlineColor
            strokeWidth = underlineWidth.toFloat()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        width = w.toFloat()
        height = h.toFloat()

    }

    override fun onDraw(canvas: Canvas?) {
        paint?.let {
            canvas?.drawLine(0F, height, width, height, it)
        }
        super.onDraw(canvas)
    }
}