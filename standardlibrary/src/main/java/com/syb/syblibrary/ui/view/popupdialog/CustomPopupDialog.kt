package com.syb.syblibrary.ui.view.popupdialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.syb.syblibrary.R
import com.syb.syblibrary.databinding.PopupDialogBinding
import com.syb.syblibrary.ui.base.databinding.lazyThreadSafetyNone
import com.syb.syblibrary.util.DeviceUtil


class CustomPopupDialog : Dialog {


    private val binder by lazyThreadSafetyNone<PopupDialogBinding> {
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.popup_dialog, null, false)
    }

    private val viewModel by lazyThreadSafetyNone {
        this
    }

    val titleText: ObservableField<String> = ObservableField(context.getString(R.string.message_alert))
    val contentText: ObservableField<String> = ObservableField("")
    val confirmText: ObservableField<String> = ObservableField(context.getString(R.string.message_confirm))
    val cancelText: ObservableField<String> = ObservableField(context.getString(R.string.message_cancel))
    var onConfirmClick: (() -> Unit)? = null
    var onCancelClick: (() -> Unit)? = null
    val confirmBtnVisible: ObservableBoolean = ObservableBoolean(true)
    val cancelBtnVisible: ObservableBoolean = ObservableBoolean(true)
    val editTextContent: ObservableField<String> = ObservableField("")
    val editTextVisible: ObservableBoolean = ObservableBoolean(false)

    constructor(context: Context): super(context)
    {
        initialzeView()
    }

    private fun initialzeView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)

        setContentView(binder.getRoot())
        binder.dialog = viewModel

        window?.attributes?.let {
            it.windowAnimations = R.style.AnimationPopupStyle
            it.copyFrom(it)
            it.width = (DeviceUtil.getDeviceWidth(context) / 1.3).toInt()
            it.height = (DeviceUtil.getDeviceHeight(context) / 2.5).toInt()
            window?.attributes = it
        }
    }

    /**
     * 확인버튼 클릭 이벤트
     */
    fun onConfirmClick() {
        onConfirmClick?.invoke()
        dismiss()
    }

    /**
     * 취소버튼 클릭 이벤트
     */
    fun onCancelClick() {
        onCancelClick?.invoke()
        dismiss()
    }

    /**
     * 텍스트 입력 결과값
     */
    fun getEditTextContent() = editTextContent.get()


}