package com.syb.syblibrary.ui.base.view

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.webkit.CookieSyncManager
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.syb.syblibrary.data.local.pref.BasePreferenceHelper
import com.syb.syblibrary.ui.view.permission.PermissionFragment
import com.syb.syblibrary.ui.view.popupdialog.CustomPopupDialog
import com.syb.syblibrary.ui.view.progressdialog.CustomProgressDialog
import com.syb.syblibrary.ui.view.toast.ToastView
import com.syb.syblibrary.ui.view.web.BaseWebChromeClient
import com.syb.syblibrary.ui.view.web.BaseWebView
import com.syb.syblibrary.ui.view.web.util.FileChooser
import com.syb.syblibrary.util.AppUtil
import com.syb.syblibrary.util.log.YLog
import java.lang.Exception

abstract class BaseActivity : AppCompatActivity(), BaseView, BaseFragment.Callback {

    private var progressDialog: CustomProgressDialog? = null
    protected var baseWebChromeClient: BaseWebChromeClient? = null
    protected var mWebView: BaseWebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun showLoading(isCancel: Boolean) {
        try {
            if (progressDialog != null && progressDialog!!.isShowing)
                return
            progressDialog = CustomProgressDialog(this).apply {
                setCancelable(isCancel)
                setCanceledOnTouchOutside(isCancel)
                show()
            }
        } catch (e: Exception) {

        }
    }

    override fun hideLoading() {
        try {
            progressDialog?.cancel()
            progressDialog?.dismiss()
            progressDialog = null
        } catch (e: Exception) {
            progressDialog?.cancel()
            progressDialog?.dismiss()
            progressDialog = null
        }
    }


    override fun showDialog(content: String, isCancel: Boolean): CustomPopupDialog =
        CustomPopupDialog(this).apply {
            contentText.set(content)
            setCancelable(isCancel)
            setCanceledOnTouchOutside(isCancel)
        }

    override fun showDialog(content: Int, isCancel: Boolean): CustomPopupDialog =
        CustomPopupDialog(this).apply {
            contentText.set(getString(content))
            setCancelable(isCancel)
            setCanceledOnTouchOutside(isCancel)
        }

    override fun confirmPermissions(permissions: Array<String>, message: String): Boolean {
        val deniedSize = PermissionFragment.deniedPermissionsSize(this, permissions)
        if (deniedSize > 0)
        {
            val permissionFragment = PermissionFragment.newInstance(permissions, message)
            val fragmentManager = supportFragmentManager
            fragmentManager?.beginTransaction()?.apply {
                add(permissionFragment, null)
                commit()
            }
            return false
        }
        return true
    }

    override fun showMessage(message: String) {
        ToastView.showMessage(this, message, Toast.LENGTH_LONG)
    }

    override fun showMessage(@StringRes resId: Int) {
        ToastView.showMessage(this, resId, Toast.LENGTH_LONG)
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun isNetConnected(): Boolean = AppUtil.isNetConnected(this)

    override fun showLogInfo(message: String) {
        YLog.i(message)
    }

    override fun showLogError(message: String) {
        YLog.e(message)
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
        progressDialog?.cancel()
        progressDialog?.dismiss()
        mWebView?.destroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            FileChooser.REQUEST_FILE_CHOOSE_V11, FileChooser.REQUEST_FILE_CHOOSE_V21 -> webViewFileChoose(requestCode, resultCode, data)
        }
    }

    /**
     * 웹뷰 파일 찾기에서 파일 선택 후 발생 이벤트
     * @param requestCode 응답코드
     * @param resultCode 결과코드
     * @param data 선택 파일 데이터
     */
    private fun webViewFileChoose(requestCode: Int, resultCode: Int, data: Intent?)
    {
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

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {

    }

}