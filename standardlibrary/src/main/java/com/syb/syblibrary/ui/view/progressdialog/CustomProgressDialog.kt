package com.syb.syblibrary.ui.view.progressdialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.syb.syblibrary.R

class CustomProgressDialog : Dialog
{
    constructor(context: Context): super(context)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.progress_dialog)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}