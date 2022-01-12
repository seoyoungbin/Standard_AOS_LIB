package com.syb.syblibrary.ui.base.databinding

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListenerAdapter

fun View.fade(show: Boolean) {

    ViewCompat.animate(this).cancel()

    if (show) {
        if (!isShown) {
            visibility = View.VISIBLE
            alpha = 0f
            ViewCompat.animate(this).alpha(1f).start()
        }
    } else {
        if (isShown) {
            alpha = 1f
            ViewCompat.animate(this)
                    .alpha(0f)
                    .setListener(object : ViewPropertyAnimatorListenerAdapter() {
                        override fun onAnimationEnd(view: View?) {
                            ViewCompat.animate(view!!).setListener(null)
                            view?.visibility = View.GONE
                        }
                    })
                    .start()
        }
    }
}
