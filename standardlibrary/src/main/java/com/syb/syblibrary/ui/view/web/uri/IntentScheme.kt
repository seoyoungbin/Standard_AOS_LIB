package com.syb.syblibrary.ui.view.web.uri

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.NonNull
import com.syb.syblibrary.ui.view.web.impl.UriAccept

class IntentScheme : UriAccept {

    companion object {
        const val INTENT_SCHEME = "intent"
    }

    override fun accept(visitor: UriVisitor): Boolean {
        val context = visitor.context
        val scheme = visitor.getScheme()
        val url = visitor.getUrl()
        return schemeIntent(context, url, scheme)
    }

    private fun schemeIntent(@NonNull context: Context, @NonNull url: String, @NonNull scheme: String): Boolean
    {
        try{
            if(!scheme.equals(INTENT_SCHEME))
                return false

            var intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
            if(context.packageManager.resolveActivity(intent, 0) == null)
            {
                if(intent.`package` != null)
                {
                    intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=${intent.`package`}")
                    )
                    context.startActivity(intent)
                }
            }
            else
            {
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(intent.dataString)).apply {
                    addCategory(Intent.CATEGORY_BROWSABLE)
                    component = null
                }
                context.startActivity(intent)
            }
        }catch (e: Exception)
        {
            e.printStackTrace()
        }

        return true
    }
}