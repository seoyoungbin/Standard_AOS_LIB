package com.syb.syblibrary.common.filedownload

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.syb.syblibrary.R
import com.syb.syblibrary.ui.view.permission.PermissionFragment
import com.syb.syblibrary.ui.view.toast.ToastView
import java.lang.Exception
import java.net.URLDecoder

class FileDownload {

    private var context: Context

    constructor(context: Context)
    {
        this.context = context
    }

    /**
     * 파일 다운로드 시작!!
     * @param url 다운로드 URL
     * @param userAgent userAgent
     * @param contentDisposition contentDisposition
     * @param mimetype mimetype
     */
    fun downloadStart(url: String?, userAgent: String?, contentDisposition: String?, mimetype: String?)
    {
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                val deniedSize = PermissionFragment.deniedPermissionsSize(context, permission)
                if (deniedSize > 0) {
                    val permissionFragment = PermissionFragment.newInstance(permission, context.getString(R.string.message_permission_file_download))
                    if(context is AppCompatActivity)
                    {
                        (context as AppCompatActivity).supportFragmentManager.beginTransaction().apply {
                            add(permissionFragment, "PermissionFragment")
                            commit()
                        }
                    }
                    return
                }
            }

            val request = DownloadManager.Request(Uri.parse(url))
            val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            var cd = URLDecoder.decode(contentDisposition, "UTF-8")
            var fileName = cd
            if (fileName != null && fileName.length > 0) {
                val idxFileName = fileName.indexOf("filename=");
                if (idxFileName > -1)
                    fileName = fileName.substring(idxFileName + 9).trim()

                if (fileName.endsWith(";"))
                    fileName = fileName.substring(0, fileName.length - 1)

                if (fileName.startsWith("\"") && fileName.endsWith("\""))
                    fileName = fileName.substring(1, fileName.length - 1)
            }
            request.apply {
                setMimeType(mimetype)
                addRequestHeader("User-Agent", userAgent)
                setDescription("File Download...")
                setAllowedOverMetered(true)
                setAllowedOverRoaming(true)
                setTitle(fileName)
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    setRequiresCharging(false)
                allowScanningByMediaScanner()
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            }
            dm.enqueue(request)
            ToastView.showMessage(context, R.string.message_file_download, Toast.LENGTH_LONG)

        }catch (e: Exception)
        {
            ToastView.showMessage(context, R.string.message_file_download_fail, Toast.LENGTH_LONG)
            e.printStackTrace()
        }
    }

}