package com.syb.syblibrary.ui.view.blur

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import com.syb.syblibrary.R


class BlurView : View {

    companion object {
        private var RENDERING_COUNT = 0
        private var BLUR_IMPL = 0
        private val STOP_EXCEPTION = StopException()
    }

    private var mTopRound = 0F
    private var mBottomRound = 0F
    private var mDownsampleFactor: Float = 4F // default 4
    private var mOverlayColor: Int = -0x55000001 // default #aaffffff
    private var mBlurRadius: Float = 40F
    private var mBlurImpl: BlurImpl? = null
    private var mDirty = false
    private var mBitmapToBlur: Bitmap? = null
    private var mBlurredBitmap: Bitmap? = null
    private var mBlurringCanvas: Canvas? = null
    private var mIsRendering = false
    private var mPaint: Paint? = null
    private val mRectSrc = Rect()
    private val mRectDst = RectF()
    private var mDecorView: View? = null
    private var mDifferentRoot = false
    private var isTranslateEnable = false
    private var isBlurRefresh = true
    private var isBlurCaptrue = false
    private var fixBitmp: Bitmap? = null

    constructor(context: Context) : super(context) {
        initialzeView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialzeView(context, attrs)
    }

    private fun initialzeView(context: Context, attrs: AttributeSet?) {

        mBlurImpl = getBlurImpl()

        val a = context.obtainStyledAttributes(attrs, R.styleable.BlurView)
        mBlurRadius = a.getFloat(R.styleable.BlurView_radius, mBlurRadius)
        mDownsampleFactor = a.getFloat(R.styleable.BlurView_downsampleFactor, mDownsampleFactor)
        mOverlayColor = a.getColor(R.styleable.BlurView_overlayColor, mOverlayColor)
        mTopRound = a.getFloat(R.styleable.BlurView_topRound, mTopRound)
        mBottomRound = a.getFloat(R.styleable.BlurView_bottomRound, mBottomRound)
        isBlurCaptrue = a.getBoolean(R.styleable.BlurView_isBlurCapture, isBlurCaptrue)

        val fixBitmapDrawable = a.getDrawable(R.styleable.BlurView_fixDrawable)
        if(fixBitmapDrawable != null)
            fixBitmp = (fixBitmapDrawable as BitmapDrawable).bitmap
        a.recycle()

        mPaint = Paint()

    }

    private fun getBlurImpl(): BlurImpl {
        if (BLUR_IMPL == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                try {
                    val impl = AndroidStockBlurImpl()
                    val bmp = Bitmap.createBitmap(4, 4, Bitmap.Config.ARGB_8888)
                    impl.prepare(context, bmp, 4F)
                    impl.release()
                    bmp.recycle()
                    BLUR_IMPL = 2
                } catch (e: Throwable) {
                }
            }
        }
        if (BLUR_IMPL == 0) {
            try {
                javaClass.classLoader!!.loadClass("androidx.renderscript.RenderScript")
                val impl = AndroidXBlurImpl()
                val bmp = Bitmap.createBitmap(4, 4, Bitmap.Config.ARGB_8888)
                impl.prepare(context, bmp, 4F)
                impl.release()
                bmp.recycle()
                BLUR_IMPL = 1
            } catch (e: Throwable) {
            }
        }
        if (BLUR_IMPL == 0)
            BLUR_IMPL = -1
        return when (BLUR_IMPL) {
            1 -> AndroidXBlurImpl()
            2 -> AndroidStockBlurImpl()
            else -> EmptyBlurImpl()
        }
    }

    fun setBlurCapture(isBlurCaptrue: Boolean) {
        this.isBlurCaptrue = isBlurCaptrue
        isBlurRefresh = true
    }

    fun setFixBitmap(bitmap: Bitmap?)
    {
        fixBitmp = bitmap
        blurRefresh()
    }

    fun setBlurRadius(radius: Float) {
        if (mBlurRadius != radius) {
            mBlurRadius = radius
            mDirty = true
            invalidate()
        }
    }

    fun setDownsampleFactor(factor: Float) {
        require(factor > 0) { "Downsample factor must be greater than 0." }
        if (mDownsampleFactor != factor) {
            mDownsampleFactor = factor
            mDirty = true // may also change blur radius
            releaseBitmap()
            invalidate()
        }
    }

    fun setOverlayColor(color: Int) {
        if (mOverlayColor != color) {
            mOverlayColor = color
            invalidate()
        }
    }

    private fun releaseBitmap() {
        if (mBitmapToBlur != null) {
            mBitmapToBlur?.recycle()
            mBitmapToBlur = null
        }
        if (mBlurredBitmap != null) {
            mBlurredBitmap?.recycle()
            mBlurredBitmap = null
        }
    }

    protected fun release() {
        releaseBitmap()
        mBlurImpl?.release()
    }

    protected fun prepare(): Boolean {
        if (mBlurRadius == 0f) {
            release()
            return false
        }
        var downsampleFactor = mDownsampleFactor
        var radius = mBlurRadius / downsampleFactor
        if (radius > 25) {
            downsampleFactor = downsampleFactor * radius / 25
            radius = 25f
        }
        val width = width
        val height = height
        var scaledWidth = Math.max(1, (width / downsampleFactor).toInt())
        var scaledHeight = Math.max(1, (height / downsampleFactor).toInt())
        var dirty = mDirty
        if (mBlurringCanvas == null || mBlurredBitmap == null || mBlurredBitmap!!.width != scaledWidth || mBlurredBitmap!!.height != scaledHeight) {
            dirty = true
            releaseBitmap()
            var r = false
            try {
                if(fixBitmp != null)
                    fixBitmp = Bitmap.createScaledBitmap(fixBitmp!!, width, height, false)
                mBitmapToBlur = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)
                if (mBitmapToBlur == null)
                    return false
                mBlurringCanvas = Canvas(mBitmapToBlur!!)
                mBlurredBitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)
                if (mBlurredBitmap == null)
                    return false
                r = true
            } catch (e: Exception) {

            } catch (e: OutOfMemoryError) {

            } finally {
                if (!r) {
                    release()
                    return false
                }
            }
        }
        if (dirty) {
            mDirty = if (mBlurImpl!!.prepare(context, mBitmapToBlur!!, radius))
                false
            else
                return false
        }
        return true
    }

    protected fun blur(bitmapToBlur: Bitmap?, blurredBitmap: Bitmap?) {
        mBlurImpl?.blur(bitmapToBlur!!, blurredBitmap!!)
    }

    private val preDrawListener = ViewTreeObserver.OnPreDrawListener {
        blurRefresh()
        true
    }

    fun blurRefresh()
    {
        if(isBlurRefresh) {
            val locations = IntArray(2)
            var oldBmp = mBlurredBitmap
            val decor = mDecorView
            if (decor != null && isShown && prepare()) {
                val redrawBitmap = mBlurredBitmap != oldBmp

                decor.getLocationOnScreen(locations)
                var x = -locations[0]
                var y = -locations[1]

                getLocationOnScreen(locations)
                x += locations[0]
                y += locations[1]

                mBitmapToBlur?.eraseColor(mOverlayColor and 0xffffff)

                val rc = mBlurringCanvas?.save()
                mIsRendering = true
                RENDERING_COUNT++

                try {
                    mBlurringCanvas?.scale(
                        1f * mBitmapToBlur!!.width / width,
                        1f * mBitmapToBlur!!.height / height
                    )
                    if (fixBitmp == null) {
                        if(isTranslateEnable)
                            mBlurringCanvas?.translate(-x.toFloat(), -y.toFloat())
                        if (decor.background != null)
                            decor.background.draw(mBlurringCanvas!!)
                        decor.draw(mBlurringCanvas)
                    } else
                        mBlurringCanvas?.drawBitmap(fixBitmp!!, 0F, 0F, null)

                } catch (e: Exception) {

                } catch (e: StopException) {

                } finally {
                    mIsRendering = false
                    RENDERING_COUNT--
                    mBlurringCanvas?.restoreToCount(rc!!)
                }

                blur(mBitmapToBlur, mBlurredBitmap)

                if (redrawBitmap || mDifferentRoot)
                    invalidate()

                if (isBlurCaptrue)
                    isBlurRefresh = false

            }
        }
    }

    fun getActivityDecorView(): View? {
        var ctx = context
        var i = 0
        while (i < 4 && ctx != null && ctx !is Activity && ctx is ContextWrapper) {
            ctx = ctx.baseContext
            i++
        }
        return if (ctx is Activity)
            ctx.window.decorView
        else
            null
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mDecorView = getActivityDecorView()
        if (mDecorView != null) {
            mDecorView?.viewTreeObserver?.addOnPreDrawListener(preDrawListener)
            mDifferentRoot = mDecorView?.rootView !== rootView
            if (mDifferentRoot)
                mDecorView?.postInvalidate()
        } else
            mDifferentRoot = false
    }

    override fun onDetachedFromWindow() {
        mDecorView?.viewTreeObserver?.removeOnPreDrawListener(preDrawListener)
        release()
        super.onDetachedFromWindow()
    }

    override fun draw(canvas: Canvas) {
        if (mIsRendering)
            throw STOP_EXCEPTION
        else if (RENDERING_COUNT > 0) {

        } else
            super.draw(canvas)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBlurredBitmap(canvas, mBlurredBitmap, mOverlayColor)
    }

    protected fun drawBlurredBitmap(canvas: Canvas, blurredBitmap: Bitmap?, overlayColor: Int) {
        val corners = floatArrayOf(
            mTopRound, mTopRound,
            mTopRound, mTopRound,
            mBottomRound, mBottomRound,
            mBottomRound, mBottomRound
        )
        val path = Path()
        if (blurredBitmap != null) {
            mRectSrc.right = blurredBitmap.width
            mRectSrc.bottom = blurredBitmap.height
            mRectDst.right = width.toFloat()
            mRectDst.bottom = height.toFloat()

            path.addRoundRect(mRectDst, corners, Path.Direction.CW)
            canvas.clipPath(path)
            canvas.drawBitmap(blurredBitmap, mRectSrc, mRectDst, null)
        }


        mPaint?.let {
            it.color = overlayColor
            canvas.drawRect(mRectDst, it)
            canvas.drawPath(path, it)
        }
    }

}
