package com.syb.syblibrary.ui.view.web.uri

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Browser
import androidx.annotation.NonNull
import com.syb.syblibrary.ui.view.popupdialog.CustomPopupDialog
import com.syb.syblibrary.ui.view.web.impl.UriAccept

class PayScheme : UriAccept{

    private var marketUri: String
    private var marketMsg: String
    private var scheme: String

    constructor(@NonNull marketUri: String, @NonNull marketMsg: String, @NonNull scheme: String)
    {
        this.marketUri = marketUri
        this.marketMsg = marketMsg
        this.scheme = scheme
    }

    override fun accept(visitor: UriVisitor): Boolean {
        val context = visitor.context
        val scheme = visitor.getScheme()
        val url = visitor.getUrl()
        return handle(context, url, scheme)
    }

    private fun handle(@NonNull context: Context, @NonNull url: String, @NonNull scheme: String): Boolean
    {
        if(!this.scheme.equals(scheme))
            return false

        try {
            createIntent(context, Uri.parse(url))
        }catch (e: Exception)
        {
            e.printStackTrace()
            val activity = if(context is Activity) context else null
            activity?.let {
                CustomPopupDialog(it).apply {
                    contentText.set(marketMsg)
                    onConfirmClick = {
                        createIntent(context, Uri.parse(marketUri))
                    }
                    show()
                }
            }
        }
        return true
    }

    private fun createIntent(@NonNull context: Context, @NonNull uri: Uri)
    {
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            addCategory(Intent.CATEGORY_BROWSABLE)
            putExtra(Browser.EXTRA_APPLICATION_ID, context.packageName)
        }
        context.startActivity(intent)
    }
}