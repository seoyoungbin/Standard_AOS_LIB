package com.syb.syblibrary.ui.base.databinding.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import com.syb.syblibrary.R
import com.syb.syblibrary.ui.base.databinding.fade
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

object ViewBindingAdapters {

    @JvmStatic
    @BindingAdapter("fadeView")
    fun fadeView(view: View, show: Boolean) {
        view.fade(show)
    }

    @JvmStatic
    @BindingAdapter("android:visibility")
    fun setVisibility(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("showLongMessage", "callback", requireAll = false)
    fun showLongMessage(
        view: View,
        text: String?,
        callback: BaseTransientBottomBar.BaseCallback<Snackbar>? = null
    ) {
        text?.let {
            val snackbar = Snackbar.make(view, it, Snackbar.LENGTH_LONG)
            callback?.let { snackbar.addCallback(callback) }
            snackbar.show()
        }
    }

    @JvmStatic
    @BindingAdapter("loadUrl")
    fun loadUrl(imageView: ImageView, url: String?) {
        url?.let {
            Glide.with(imageView.context)
                .load(it)
                .apply(RequestOptions.noTransformation())
                .into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("onDelayClick")
    fun onDelayClick(view: View, listener: View.OnClickListener) {
        RxView.clicks(view)
            .throttleFirst(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread()) // 버튼 중복클릭 방지
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t ->
                listener.onClick(view)
            })
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageUri(view: ImageView, imageUri: String?) {
        if (imageUri == null) {
            view.setImageURI(null)
        } else {
            view.setImageURI(Uri.parse(imageUri))
        }
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageUri(view: ImageView, imageUri: Uri?) {
        view.setImageURI(imageUri)
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageDrawable(
        view: ImageView,
        drawable: Drawable?
    ) {
        view.setImageDrawable(drawable)
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageResource(imageView: ImageView, resource: Int) {
        if(resource == -1)
            return
        imageView.setImageResource(resource)
    }

    @JvmStatic
    @BindingAdapter("targetFocus")
    fun targerFocus(editText: EditText, isFocusing: Boolean)
    {
        if(isFocusing) {
            editText.requestFocus()
            val imm = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            editText.post {
                imm.showSoftInput(editText, 0)
            }

            val shakeAnim = AnimationUtils.loadAnimation(editText.context, R.anim.shake_anim)
            editText.startAnimation(shakeAnim)
        }
    }

    @JvmStatic
    @BindingAdapter("selection")
    fun selection(editText: EditText, position: Int)
    {
        editText.setSelection(position)
    }


}
