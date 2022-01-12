package com.syb.syblibrary.ui.view.web.uri

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.NonNull
import com.syb.syblibrary.ui.view.web.impl.UriAccept

class MarketScheme : UriAccept {

    companion object {
        const val MARKET_SCHEME = "market"
    }

    override fun accept(visitor: UriVisitor): Boolean {
        val context = visitor.context
        val scheme = visitor.getScheme()
        val url = visitor.getUrl()
        return schemeMarket(context, url, scheme)
    }

    private fun schemeMarket(@NonNull context: Context, @NonNull url: String, @NonNull scheme: String): Boolean
    {
        try{
            if(!scheme.equals(MARKET_SCHEME))
                return false

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }catch (e: Exception)
        {
            e.printStackTrace()
        }

        return true
    }
}