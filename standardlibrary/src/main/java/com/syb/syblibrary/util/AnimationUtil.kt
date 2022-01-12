package com.syb.syblibrary.util

import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation

object AnimationUtil {

    /**
     * View 회전 후 위로 바라보게 하는 애니메이션
     * @param view 애니메이션 Target View
     */
    fun rotateArrowUpAnimation(view: View) {
        val animation = RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        animation.duration = 200
        animation.fillAfter = true
        view.startAnimation(animation)
    }

    /**
     * View 회전 후 아래로 바라보게 하는 애니메이션
     * @param view 애니메이션 Target View
     */
    fun rotateArrowDownAnimation(view: View) {
        val animation = RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        animation.duration = 200
        animation.fillAfter = true
        view.startAnimation(animation)
    }

    /**
     * View 오른쪽 왼쪽 왕복 애니메이션
     * @param view 애니메이션 Target View
     */
    fun transRightAnimation(view: View)
    {
        var animation = TranslateAnimation(0f, -20f, 0f, 0f)
        animation.duration = 300
        animation.repeatCount = Animation.INFINITE
        animation.repeatMode = Animation.REVERSE
        view.startAnimation(animation)
    }

    // 화살표 오른쪽 왕복 애니메이션
    /**
     * View 왼쪽 오른쪽 애니메이션
     * @param view 애니메이션 Target View
     */
    fun transLeftAnimation(view: View)
    {
        var animation = TranslateAnimation(0f, 20f, 0f, 0f)
        animation.duration = 300
        animation.repeatCount = Animation.INFINITE
        animation.repeatMode = Animation.REVERSE
        view.startAnimation(animation)
    }


}