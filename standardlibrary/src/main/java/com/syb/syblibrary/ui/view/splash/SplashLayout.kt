package com.syb.syblibrary.ui.view.splash

import android.app.Activity
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.syb.syblibrary.R
import com.syb.syblibrary.databinding.SplashLayoutBinding
import com.syb.syblibrary.ui.base.databinding.lazyThreadSafetyNone

class SplashLayout : LinearLayout {

    companion object {
        const val SPLASH_SHOW_DELAY_TIME = 3000L
    }

    private val binder by lazyThreadSafetyNone<SplashLayoutBinding> {
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.splash_layout, this, true)
    }

    private val viewModel by lazyThreadSafetyNone {
        this
    }

    private var mActivity: Activity? = null
    private var loadingStartTime: Long = 0
    private val splashHandler = Handler()
    var splashLayoutVisible: ObservableBoolean = ObservableBoolean(true)
    var splashDefaultImgVisible: ObservableBoolean = ObservableBoolean(false)
    var splashServerImgVisible: ObservableBoolean = ObservableBoolean(false)

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

        val a = context.obtainStyledAttributes(attrs, R.styleable.SplashView)
        val defaultSplashDrawable = a.getDrawable(R.styleable.SplashView_defaultImage)
        defaultSplashDrawable?.let {
            val defaultSplash = (it as BitmapDrawable).bitmap
            binder.defaultImg.setImageBitmap(defaultSplash)
        }
        a.recycle()

        if(context is Activity)
            mActivity = context
        loadingStartTime = System.currentTimeMillis()
        binder.layout = viewModel
    }

    /**
     * 이미지 파일 띄우기
     * @param url 해당 이미지 URL
     */
    fun imgLoad(url: String) {

        if(url.isNullOrEmpty())
        {
            showDefaultSplash()
            return
        }

        Glide.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.NORMAL)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    showDefaultSplash()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    mActivity?.runOnUiThread {
                        if(!isHideSplash()) {
                            binder.imageServerLogo.setImageDrawable(resource)
                            splashServerImgVisible.set(true)
                        }
                    }
                    return false
                }
            }).submit()
    }

    /**
     * GIF 이미지 파일 띄우기
     * @param url 해당 이미지 URL
     */
    fun gifLoad(url: String) {

        if(url.isNullOrEmpty())
        {
            showDefaultSplash()
            return
        }

        Glide.with(context)
            .asGif()
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.NORMAL)
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    showDefaultSplash()
                    return false
                }

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    mActivity?.runOnUiThread {
                        if (!isHideSplash()) {
                            binder.imageServerLogo.setImageDrawable(resource)
                            resource?.start()
                            splashServerImgVisible.set(true)
                        }
                    }
                    return false
                }
            }).submit()
    }

    /**
     * 스플래시 종료!!
     */
    fun hideSplash() {
        if(loadingStartTime > 0) {
            val minimumSplashTime = SPLASH_SHOW_DELAY_TIME
            val currentTime = System.currentTimeMillis()
            val delayTime = currentTime - loadingStartTime
            if (delayTime < minimumSplashTime) {
                var r = Runnable { splashFinish() }
                splashHandler.postDelayed(r, minimumSplashTime - delayTime)
            } else
                splashFinish()
            loadingStartTime = 0
        }
    }

    /**
     * 디폴트 스플래시 이미지 띄우기
     */
    private fun showDefaultSplash()
    {
        mActivity?.runOnUiThread {
            splashDefaultImgVisible.set(true)
        }
    }

    /**
     * 스플래시 종료!!
     */
    private fun splashFinish()
    {
        mActivity?.runOnUiThread {
            splashLayoutVisible.set(false)
        }
    }


    /**
     * 스플레시 종료 여부 확인
     */
    fun isHideSplash(): Boolean = !splashLayoutVisible.get()

    /**
     * 스플래시 종료!!
     */
    fun onDestroy()
    {
        splashHandler?.removeMessages(0)
    }
}