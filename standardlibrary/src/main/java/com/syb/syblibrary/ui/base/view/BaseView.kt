package com.syb.syblibrary.ui.base.view

import androidx.annotation.StringRes
import com.syb.syblibrary.ui.view.popupdialog.CustomPopupDialog
import org.jetbrains.annotations.NotNull

interface BaseView
{
    fun showLoading(isCancel: Boolean)

    fun hideLoading()

    fun showDialog(content: String, @NotNull isCancel: Boolean): CustomPopupDialog?

    fun showDialog(@StringRes content: Int, @NotNull isCancel: Boolean): CustomPopupDialog?

    fun confirmPermissions(permissions: Array<String>, message: String): Boolean

    fun showMessage(message: String)

    fun showMessage(@StringRes resId: Int)

    fun isNetConnected(): Boolean?

    fun showLogInfo(message: String)

    fun showLogError(message: String)

}