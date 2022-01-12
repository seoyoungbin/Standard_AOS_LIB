package com.syb.syblibrary.common.filedownload

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.widget.Toast
import com.syb.syblibrary.R
import com.syb.syblibrary.ui.view.toast.ToastView
import com.syb.syblibrary.util.AppUtil


class DownloadBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            if (context != null && intent != null) {
                val action = intent.action
                if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                    val query = DownloadManager.Query()
                    query.setFilterById(intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0))
                    val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val cursor: Cursor = manager.query(query)
                    if (cursor.moveToFirst()) {
                        if (cursor.getCount() > 0) {
                            val status: Int = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                            val download_id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)

                            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                if (AppUtil.isAppRunning(context))
                                    ToastView.showMessage(context, R.string.message_file_download_success, Toast.LENGTH_LONG)
                            } else if (status == DownloadManager.STATUS_FAILED) {
                                if(AppUtil.isAppRunning(context))
                                    ToastView.showMessage(context, R.string.message_file_download_fail, Toast.LENGTH_LONG)
                            }
                        }
                    }
                    cursor.close()
                }
            }
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
    }
}