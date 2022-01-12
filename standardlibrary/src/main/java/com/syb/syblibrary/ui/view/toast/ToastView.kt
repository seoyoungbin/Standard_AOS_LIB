package com.syb.syblibrary.ui.view.toast

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.widget.Toast
import com.syb.syblibrary.R

class ToastView(var context: Context) : Toast(context) {

    companion object {
        var mToastView: Toast? = null
        private var mHandler = Handler()
        private var isFinish = false
        private val FINISH_LIMIT_TIME = 3000L//ms

        /**
         * 토스트 팝업 띄우기!!
         * @param context 컨텍스트
         * @param message 메시지 문자열
         * @param duration 메세지 노출 시간
         */
        @JvmStatic
        fun showMessage(context: Context, message: String, duration: Int)
        {
            mToastView?.cancel()
            mToastView = makeText(context, message, duration)
            mToastView?.show()
        }


        /**
         * 토스트 팝업 띄우기!!
         * @param context 컨텍스트
         * @param message 메시지 ID 값
         * @param duration 메세지 노출 시간
         */
        @JvmStatic
        fun showMessage(context: Context, message: Int, duration: Int)
        {
            mToastView?.cancel()
            mToastView = makeText(context, message, duration)
            mToastView?.show()
        }

        /**
         * 앱 종료 백버튼 토스트 팝업 기능
         * @param activity 해당 액티비티
         */
        @JvmStatic
        fun backPressToast(activity: Activity)
        {
            if (!isFinish) {
                showMessage(activity, R.string.message_toast_notice_finish, FINISH_LIMIT_TIME.toInt())
                isFinish = true
                mHandler.postDelayed(object : Runnable {
                    override fun run() {
                        isFinish = false
                    }
                }, FINISH_LIMIT_TIME)
            } else {
                mToastView?.cancel()
                activity.finish()
            }
        }

        /**
         * 토스트 팝업 종료!!
         */
        @JvmStatic
        fun onDestroy() {
            isFinish = false
            mHandler.removeMessages(0)
        }

    }

}