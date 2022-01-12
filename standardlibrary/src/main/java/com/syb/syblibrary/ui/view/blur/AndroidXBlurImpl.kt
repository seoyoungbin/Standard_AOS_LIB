package com.syb.syblibrary.ui.view.blur

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.*


class AndroidXBlurImpl : BlurImpl {

    companion object {
        var DEBUG: Boolean? = null
        fun isDebug(ctx: Context?): Boolean {
            if (DEBUG == null && ctx != null) {
                DEBUG = ctx.applicationInfo.flags and 2 != 0
            }
            return DEBUG == java.lang.Boolean.TRUE
        }
    }

    private var mRenderScript: RenderScript? = null
    private var mBlurScript: ScriptIntrinsicBlur? = null
    private var mBlurInput: Allocation? = null
    private var mBlurOutput: Allocation? = null

    override fun prepare(context: Context, buffer: Bitmap, radius: Float): Boolean {
        if (mRenderScript == null) {
            try {
                mRenderScript = RenderScript.create(context)
                mBlurScript = ScriptIntrinsicBlur.create(mRenderScript, Element.U8_4(mRenderScript))
            } catch (var5: RSRuntimeException) {
                if (isDebug(context)) {
                    throw var5
                }
                release()
                return false
            }
        }
        mBlurScript?.setRadius(radius)
        mBlurInput = Allocation.createFromBitmap(mRenderScript, buffer, Allocation.MipmapControl.MIPMAP_NONE, 1)
        mBlurOutput = Allocation.createTyped(mRenderScript, mBlurInput?.getType())
        return true
    }

    override fun release() {
        if (mBlurInput != null) {
            mBlurInput?.destroy()
            mBlurInput = null
        }
        if (mBlurOutput != null) {
            mBlurOutput?.destroy()
            mBlurOutput = null
        }
        if (mBlurScript != null) {
            mBlurScript?.destroy()
            mBlurScript = null
        }
        if (mRenderScript != null) {
            mRenderScript?.destroy()
            mRenderScript = null
        }
    }

    override fun blur(input: Bitmap, output: Bitmap) {
        mBlurInput?.copyFrom(input)
        mBlurScript?.setInput(mBlurInput)
        mBlurScript?.forEach(mBlurOutput)
        mBlurOutput?.copyTo(output)
    }

}
