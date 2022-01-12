package com.syb.syblibrary.data.local.pref

interface BasePreferenceHelper
{
    fun setIsPermissionConfirm(isConfirm: Boolean)

    fun getIsPermissionConfirm(): Boolean

    fun setIsAutoLogin(isAutoLogin: Boolean)

    fun getIsAutoLogin(): Boolean

    fun setIsPushNotice(isNotice: Boolean)

    fun getIsPushNotice(): Boolean

    fun setIsPushInit(isInit: Boolean)

    fun getIsPushInit(): Boolean

    fun setIsPushAlive(isAlive: Boolean)

    fun getIsPushAlive(): Boolean

    fun setIsPushIdentity(isSet: Boolean)

    fun getIsPushIdentity(): Boolean

    fun setAppServerTypeOrdinal(ordinal: Int)

    fun getAppServerTypeOrdinal(): Int
}