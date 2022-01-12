package com.syb.compiler

import javax.annotation.processing.AbstractProcessor
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.tools.Diagnostic

abstract class BaseProcessor : AbstractProcessor()
{
    /**
     * Annotation Type 검증
     * @param kind Annotation 타입
     * @param elements Annotation class 배열
     * @return Annotation Type 검증 여부
     */
    protected fun checkElementType(kind: ElementKind, elements: Set<Element>): Boolean {
        if (elements.isEmpty()) return false

        elements.forEach {
            if (it.kind != kind) {
                printMessage(
                        Diagnostic.Kind.ERROR, "Only ${kind.name} Are Supported", it
                )
                return false
            }
        }
        return true
    }

    /**
     * 메세지 출력!
     * @param kind 메세지 타입
     * @param message 해당 메세지 문구
     * @param element 메세지 출력 Class
     */
    protected fun printMessage(kind: Diagnostic.Kind, message: String, element: Element) {
        processingEnv.messager.printMessage(kind, message, element)
    }

}