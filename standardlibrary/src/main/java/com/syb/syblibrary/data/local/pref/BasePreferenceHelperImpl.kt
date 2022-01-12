package com.syb.syblibrary.data.local.pref

import android.content.Context

abstract class BasePreferenceHelperImpl(context: Context, private val appPrefFileName: String) : BasePreferenceHelper
{
    companion object {
        /**
         * PREF_IS_PERMISSION_CONFIRM 앱 권한 확인 여부
         * PREF_IS_AUTO_LOGIN AUTO 로그인 설정
         * PREF_IS_PUSH_NOTICE 푸시 알림 안내 확인 여부
         * PREF_IS_PUSH_INIT 푸시 알림 초기 세팅 설정 여부
         * PREF_IS_PUSH_ALIVE 푸시 알림 활성화 여부
         * PREF_IS_PUSH_IDENTITY 푸시 식별자 등록 여부
         * PREF_APP_SERVER_TYPE_ORDINAL API, 웹뷰 등등 서버 타입 지정
         */
        private const val PREF_IS_PERMISSION_CONFIRM = "PREF_IS_PERMISSION_CONFIRM"
        private const val PREF_IS_AUTO_LOGIN = "PREF_IS_AUTO_LOGIN"
        private const val PREF_IS_PUSH_NOTICE = "PREF_IS_PUSH_NOTICE"
        private const val PREF_IS_PUSH_INIT = "PREF_IS_PUSH_INIT"
        private const val PREF_IS_PUSH_ALIVE = "PREF_IS_PUSH_ALIVE"
        private const val PREF_IS_PUSH_IDENTITY = "PREF_IS_PUSH_IDENTITY"
        private const val PREF_APP_SERVER_TYPE_ORDINAL = "PREF_APP_SERVER_TYPE_ORDINAL"
    }

    protected val mPrefs = context.getSharedPreferences(appPrefFileName, Context.MODE_PRIVATE)
    protected val edit = mPrefs.edit()

    override fun setIsPermissionConfirm(isConfirm: Boolean) {
        edit.putBoolean(PREF_IS_PERMISSION_CONFIRM, isConfirm)
        edit.commit()
    }

    override fun getIsPermissionConfirm(): Boolean = mPrefs.getBoolean(PREF_IS_PERMISSION_CONFIRM, false)

    override fun setIsAutoLogin(isAutoLogin: Boolean) {
        edit.putBoolean(PREF_IS_AUTO_LOGIN, isAutoLogin)
        edit.commit()
    }

    override fun getIsAutoLogin(): Boolean = mPrefs.getBoolean(PREF_IS_AUTO_LOGIN, false)

    override fun setIsPushNotice(isNotice: Boolean) {
        edit.putBoolean(PREF_IS_PUSH_NOTICE, isNotice)
        edit.commit()
    }

    override fun getIsPushNotice(): Boolean = mPrefs.getBoolean(PREF_IS_PUSH_NOTICE, false)

    override fun setIsPushInit(isInit: Boolean) {
        edit.putBoolean(PREF_IS_PUSH_INIT, isInit)
        edit.commit()
    }

    override fun getIsPushInit(): Boolean = mPrefs.getBoolean(PREF_IS_PUSH_INIT, false)

    override fun setIsPushAlive(isAlive: Boolean) {
        edit.putBoolean(PREF_IS_PUSH_ALIVE, isAlive)
        edit.commit()
    }

    override fun getIsPushAlive(): Boolean = mPrefs.getBoolean(PREF_IS_PUSH_ALIVE, false)

    override fun setIsPushIdentity(isSet: Boolean) {
        edit.putBoolean(PREF_IS_PUSH_IDENTITY, isSet)
        edit.commit()
    }

    override fun getIsPushIdentity(): Boolean = mPrefs.getBoolean(PREF_IS_PUSH_IDENTITY, false)

    override fun setAppServerTypeOrdinal(ordinal: Int) {
        edit.putInt(PREF_APP_SERVER_TYPE_ORDINAL, ordinal)
        edit.commit()
    }

    override fun getAppServerTypeOrdinal(): Int = mPrefs.getInt(PREF_APP_SERVER_TYPE_ORDINAL, -1)

}