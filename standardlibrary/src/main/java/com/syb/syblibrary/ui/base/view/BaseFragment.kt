package com.syb.syblibrary.ui.base.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.webkit.CookieSyncManager
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment
import com.syb.syblibrary.data.local.pref.BasePreferenceHelper
import com.syb.syblibrary.ui.view.popupdialog.CustomPopupDialog
import com.syb.syblibrary.ui.view.web.BaseWebChromeClient
import com.syb.syblibrary.ui.view.web.BaseWebView
import com.syb.syblibrary.ui.view.web.util.FileChooser

abstract class BaseFragment : Fragment(), BaseView
{
    protected var mWebView: BaseWebView? = null
    protected var baseWebChromeClient: BaseWebChromeClient? = null
    private var activity: BaseActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            activity = context
            activity?.onFragmentAttached()
        }
    }

    override fun showLoading(isCancel: Boolean) {
        activity?.showLoading(isCancel)
    }

    override fun hideLoading() {
        activity?.hideLoading()
    }

    override fun showMessage(message: String) {
        activity?.showMessage(message)
    }

    override fun showMessage(resId: Int) {
        activity?.showMessage(resId)
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun isNetConnected(): Boolean? =
        activity?.isNetConnected()

    override fun showLogInfo(message: String) {
        activity?.showLogInfo(message)
    }

    override fun showLogError(message: String) {
        activity?.showLogError(message)
    }

    override fun showDialog(content: String, isCancel: Boolean): CustomPopupDialog? =
        activity?.showDialog(content, isCancel)

    override fun showDialog(content: Int, isCancel: Boolean): CustomPopupDialog? =
        activity?.showDialog(content, isCancel)

    override fun confirmPermissions(permissions: Array<String>, message: String): Boolean = activity?.confirmPermissions(permissions, message)?: false

    override fun onDetach() {
        activity = null
        super.onDetach()
    }

    override fun onResume() {
        super.onResume()
        mWebView?.onResume()
        cookieSyncStart()
    }

    override fun onPause() {
        super.onPause()
        mWebView?.onPause()
        cookieSyncStop()
    }

    override fun onDestroy() {
        mWebView?.destroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FileChooser.REQUEST_FILE_CHOOSE_V11, FileChooser.REQUEST_FILE_CHOOSE_V21 -> webViewFileChoose(requestCode, resultCode, data)
        }
    }

    /**
     * 웹뷰 파일 찾기에서 파일 선택 후 발생 이벤트
     * @param requestCode 응답코드
     * @param resultCode 결과코드
     * @param data 선택 파일 데이터
     */
    private fun webViewFileChoose(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            FileChooser.REQUEST_FILE_CHOOSE_V11 -> baseWebChromeClient?.getFileChooser()?.resolveFileChooseV11(resultCode, data)
            FileChooser.REQUEST_FILE_CHOOSE_V21 -> baseWebChromeClient?.getFileChooser()?.resolveFileChooseV21(resultCode, data)
        }
    }

    /**
     * 쿠키 동기화 시작
     */
    fun cookieSyncStart() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            CookieSyncManager.getInstance().startSync()
    }

    /**
     * 쿠키 동기화 종료
     */
    fun cookieSyncStop() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            CookieSyncManager.getInstance().stopSync()
    }

    protected abstract fun setUp()

    abstract fun getPreferenceHelper(): BasePreferenceHelper?

    interface Callback {
        fun onFragmentAttached()

        fun onFragmentDetached(tag: String)
    }

}