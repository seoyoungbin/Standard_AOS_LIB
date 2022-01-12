package com.syb.sample.ui.view.lloydweb.uri

import com.syb.syblibrary.ui.view.web.impl.UriAccept


interface UriResolver : UriAccept
{
    fun getId(): String
}