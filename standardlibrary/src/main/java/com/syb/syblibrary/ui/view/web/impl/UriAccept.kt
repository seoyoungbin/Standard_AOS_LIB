package com.syb.syblibrary.ui.view.web.impl

import com.syb.syblibrary.ui.view.web.uri.UriVisitor

/**
 * Uri 에 반응할 interface
 */
interface UriAccept
{
    /**
     * visitor 를 통해서 Uri 정보를 확인
     * @param visitor
     * @return  Uri  처리 후 결과를 return
     */
    fun accept(visitor: UriVisitor): Boolean
}