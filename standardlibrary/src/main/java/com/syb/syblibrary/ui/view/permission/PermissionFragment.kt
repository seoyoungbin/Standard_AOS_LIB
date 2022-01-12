package com.syb.syblibrary.ui.view.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.syb.syblibrary.R
import com.syb.syblibrary.ui.view.popupdialog.CustomPopupDialog
import java.util.*

class PermissionFragment : Fragment() {

    companion object {
        private const val TAG = "PermissionFragment"
        private const val PERMISSION = "PERMISSION"
        const val RATIONALE_MSG = "RATIONALE_MSG"
        private const val SETTING_ACTIVITY_REQUEST_CODE = 1
        private const val PERMISSION_REQUEST_CODE = 100
        private const val RESTART_FRAGMENT_DATA = "RESTART_FRAGMENT_DATA"

        fun newInstance(@NonNull perm: Array<String>, @NonNull rationalMsg: String): PermissionFragment
        {
            val bundle = Bundle().apply {
                putStringArray(PERMISSION, perm)
                putString(RATIONALE_MSG, rationalMsg)
            }
            val permFragment = PermissionFragment().apply {
                arguments = bundle
            }
            return permFragment
        }

        /**
         * 해당 권한들 거부한 권한 갯수 반환
         * @param context 컨텍스트
         * @param permissions 체크할 권한들
         * @return 거부한 권한 개수
         */
        fun deniedPermissionsSize(@NonNull context: Context, permissions: Array<String>): Int {
            val granted = PackageManager.PERMISSION_GRANTED
            var size = 0
            for (permission in permissions) {
                if (granted != ContextCompat.checkSelfPermission(context, permission))
                    size++
            }
            return size
        }
    }

    private var permissions: Array<String>? = null
    private var permRationaleMsg = ""
    private var permissionDeniedList = ArrayList<String>()
    private var settingPopup: CustomPopupDialog? = null

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(RESTART_FRAGMENT_DATA, true)
        super.onSaveInstanceState(outState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var restart_fragment = false
        savedInstanceState?.let {
            restart_fragment = it.getBoolean(RESTART_FRAGMENT_DATA)
        }

        val bundle = arguments
        // fragment 재시작 맟 bundle 값 존재 확인
        if(restart_fragment || bundle == null)
        {
            selfRemoveFragment()
            return
        }

        // 요청 퍼미션 유효성 체크
        permissions = bundle.getStringArray(PERMISSION)
        if(permissions == null || permissions?.size == 0)
        {
            selfRemoveFragment()
            return
        }

        // 해당 퍼미션 필요성 여부 메세지 등
        permRationaleMsg = bundle.getString(RATIONALE_MSG)?: ""

        /*
         요청한 퍼미션이 허용 상태인지 거부 상태인지 판별 후
         요청 또는 거부 중인 퍼미션이 존재하면 퍼미션 허용 팝업을 요청한다.
         그렇지 않으면 fragment 제거
         */
        permissions?.let {
            deniedPermissions(it)
        }
        if(permissionDeniedList.size > 0)
            requestPermission()
        else
            selfRemoveFragment()
    }

    /**
     * 사용자에게 권한 요청
     */
    fun requestPermission()
    {
        val permList = permissionDeniedList.toTypedArray()
        requestPermissions(permList, PERMISSION_REQUEST_CODE)
    }

    /**
     * 허용 거부 상태의 퍼미션 리스트 추가
     * @param permissions 허용 거부 퍼미션 리스트
     */
    fun deniedPermissions(permissions: Array<String>)
    {
        val granted = PackageManager.PERMISSION_GRANTED
        for (permission in permissions) {
            activity?.let {
                if (granted != ContextCompat.checkSelfPermission(it, permission))
                    permissionDeniedList.add(permission)
            }
        }
    }

    /**
     * 현재 프래그먼트 종료!
     */
    fun selfRemoveFragment()
    {
        val fmManager = activity?.supportFragmentManager
        val fmTransaction = fmManager?.beginTransaction()
        fmTransaction?.remove(this)
        fmTransaction?.commitAllowingStateLoss()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for(i in permissions.indices) {
            // 사용자 허용 퍼미션 제거
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                permissionDeniedList.remove(permissions[i])
        }

        // 사용자 거부 퍼미션 존재시
        var neverAskAgainCnt = 0
        if (permissionDeniedList.size > 0) {
            permissionDeniedList.forEach { permission ->
                if (activity is Activity) {
                    val rationale = ActivityCompat.shouldShowRequestPermissionRationale(activity as Activity, permission)
                    if (!rationale)
                        neverAskAgainCnt++
                }
            }

            if (neverAskAgainCnt == permissionDeniedList.size)
                showSettingDialog(permRationaleMsg, settingOnClickListener)
            else
                showSettingDialog(permRationaleMsg, retryPermissionRequestOnClickListener)
        } else
            selfRemoveFragment()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == SETTING_ACTIVITY_REQUEST_CODE)
            selfRemoveFragment()
    }

    /**
     * 권한 허용 요청 팝업 띄우기
     * @param message 전달 내용
     * @param listener 설정 클릭시 발생하는 이벤트
     */
    fun showSettingDialog(message: String, @Nullable listener: (() -> Unit)) {
        settingPopup?.let {
            if(it.isShowing)
                return
        }
        activity?.let {
            settingPopup = CustomPopupDialog(it).apply {
                contentText.set(message)
                confirmText.set(context.getString(R.string.message_setting))
                onConfirmClick = listener
                onCancelClick = {
                    this.cancel()
                    selfRemoveFragment()
                }
                show()
            }
        }
    }

    // 권한 재요청 이벤트 리스너
    val retryPermissionRequestOnClickListener = {
        requestPermission()
    }

    // 설정 화면으로 이동 이벤트 리스너
    val settingOnClickListener = {
        val intent = Intent().apply {
            setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            addCategory(Intent.CATEGORY_DEFAULT)
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            val uri = Uri.fromParts("package", activity?.packageName, null)
            setData(uri)
        }
        startActivityForResult(intent, SETTING_ACTIVITY_REQUEST_CODE)
    }

}