package com.syb.syblibrary.ui.base.popupdialog

import android.app.Dialog
import android.content.Context
import android.view.MotionEvent
import android.view.View

abstract class BasePopupDialog : Dialog
{
    private var touchLocate = 0f
    private var yLocate = 0f

    constructor(context: Context): super(context)

    /**
     * 터치 이동 후 팝업 종료!!!
     * @param touchArea 터치 영역
     * @param layout 이동 Layout
     * @param isUpDirect layout 이동 방향
     */
    protected fun setTouchDismiss(touchArea: View, layout: View, isUpDirect: Boolean)
    {
        touchArea.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                event?.let {
                    when (it.getActionMasked()) {
                        MotionEvent.ACTION_DOWN -> touchLocate = it.y
                        MotionEvent.ACTION_MOVE -> {
                            yLocate = touchLocate - it.y
                            if ((yLocate > 0 && isUpDirect) || (yLocate < 0 && !isUpDirect))
                                layout.scrollTo(0, yLocate.toInt())
                        }
                        MotionEvent.ACTION_UP -> {
                            val layoutHeight = layout.height
                            val movePercent = (Math.abs(yLocate) / layoutHeight) * 100

                            if(70 < movePercent) {
                                if ((yLocate > 0 && isUpDirect) || (yLocate < 0 && !isUpDirect))
                                    dismiss()
                            }
                            else
                                layout.scrollTo(0, 0)
                        }
                    }
                }
                return true
            }
        })
    }
}