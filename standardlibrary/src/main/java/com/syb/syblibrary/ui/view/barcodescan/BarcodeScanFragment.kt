package com.syb.syblibrary.ui.view.barcodescan

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import com.google.zxing.Result
import com.syb.syblibrary.BaseApplication
import com.syb.syblibrary.R
import com.syb.syblibrary.databinding.FragmentBarcodeScanBinding
import com.syb.syblibrary.util.log.YLog
import me.dm7.barcodescanner.zxing.ZXingScannerView


class BarcodeScanFragment : DialogFragment, ZXingScannerView.ResultHandler {

    companion object {
        const val FLASH_STATE = "FLASH_STATE"
    }

    var binder: FragmentBarcodeScanBinding? = null
    private var mScannerView: ZXingScannerView? = null
    var mFlash: MutableLiveData<Boolean> = MutableLiveData(false)
    var barcodeScanListener: BarcodeScanListener? = null

    constructor(barcodeScanListener: BarcodeScanListener): super()
    {
        this.barcodeScanListener = barcodeScanListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_barcode_scan, container, false)
        binder?.fragment = this
        binder?.lifecycleOwner = this
        mScannerView = ZXingScannerView(BaseApplication.baseApplication)
        binder?.contentFrame?.addView(mScannerView)
        return binder?.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun onResume() {
        super.onResume()
        mScannerView?.let {
            it.setResultHandler(this)
            it.setAspectTolerance(0.2f)
            it.startCamera()
            mFlash?.value?.let { f ->
                it.flash = f
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mScannerView?.stopCamera()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mFlash?.value?.let {
            outState.putBoolean(FLASH_STATE, it)
        }
    }

    override fun handleResult(rawResult: Result?) {
        YLog.i("Barcode Scan => Content: ${rawResult?.text}, Format: ${rawResult?.barcodeFormat.toString()}")
        if(rawResult?.text.isNullOrEmpty()) {
            val handler = Handler()
            handler.postDelayed({ mScannerView?.resumeCameraPreview(this) }, 2000)
        }
        else {
            rawResult?.text?.let {
                barcodeScanListener?.scanSuccess(it)
                dismiss()
            }
        }
    }

    /**
     * 프래시 On/Off
     */
    fun toggleFlash() {
        mFlash?.value?.let {
            val value = !it
            mFlash.value = value
            mScannerView?.flash = value
        }
    }


}