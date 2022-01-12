package com.syb.compiler

import com.google.auto.service.AutoService
import com.syb.annotations.webview.CustomUri
import com.syb.annotations.webview.HostInject
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.asTypeName
import java.io.File
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
class CustomUriProcessor : BaseProcessor() {

    var schemeArray: HashMap<String, ArrayList<String>> = HashMap()

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
        val fileBuilder =
            FileSpec.builder("com.syb.compiler.customuri.generated", "CustomUriExtension")
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(
                CustomUri::class.java.name,
                HostInject::class.java.name
        )
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun process(
            annotations: MutableSet<out TypeElement>?,
            roundEnv: RoundEnvironment
    ): Boolean {

        val classElements = roundEnv.getElementsAnnotatedWith(CustomUri::class.java)

        if (!checkElementType(ElementKind.CLASS, classElements)) return false

        classElements.forEach {classElement ->

            dataValidation(classElement)
            fileBuilder.addFunction(makeGetSchemeFunction(classElement))
            fileBuilder.addFunction(makeBindFunction(classElement))

        }

        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
        fileBuilder.build().writeTo(File(kaptKotlinGeneratedDir))
        return true

    }

    /**
     * 데이터 유효성 검사!!
     * @param classElement Annotation Class
     */
    private fun dataValidation(classElement: Element)
    {
        val customScheme = classElement.getAnnotation(CustomUri::class.java).scheme
        if(customScheme.isNullOrEmpty())
            printMessage(Diagnostic.Kind.ERROR, "scheme 값이 비어있습니다. scheme 값은 필수 값입니다.", classElement)

        val enclosedElements = classElement.enclosedElements
        val hostArray = schemeArray[customScheme]
        if(hostArray.isNullOrEmpty())
            schemeArray.put(customScheme, ArrayList())

        enclosedElements.forEach { e ->
            val customHost = e.getAnnotation(HostInject::class.java)
            customHost?.let {
                val host = e.simpleName.toString()
                val findIndex = schemeArray[customScheme]?.indexOf(host)?: -1
                if(findIndex == -1)
                    schemeArray[customScheme]?.add(host)
                else
                    printMessage(Diagnostic.Kind.ERROR, "${customScheme}://${schemeArray[customScheme]?.get(findIndex)} 값이 중복되었습니다.", e)
            }
        }

    }

    /**
     * Field Variable bind 함수 생성!
     * @param classElement Annotation Class
     * @return bind 함수 Builder
     */
    private fun makeBindFunction(classElement: Element): FunSpec {
        val funcBuilder = FunSpec.builder("bind")
                .receiver(classElement.asType().asTypeName())
                .addModifiers(KModifier.PUBLIC)
                .addParameter("parent", classElement.asType().asTypeName())

        classElement.enclosedElements.forEach { e ->
            val customHost = e.getAnnotation(HostInject::class.java)
            customHost?.let {
                funcBuilder.addStatement("parent.%L = %S", e.simpleName, e.simpleName)
            }
        }
        return funcBuilder.build()
    }

    /**
     * Scheme 반환 함수 생성
     * @param classElement Annotation Class
     * @return Scheme 반환 함수 Builder
     */
    private fun makeGetSchemeFunction(classElement: Element): FunSpec {
        val customScheme = classElement.getAnnotation(CustomUri::class.java).scheme
        val funcBuilder = FunSpec.builder("getScheme")
                .receiver(classElement.asType().asTypeName())
                .addModifiers(KModifier.PUBLIC)
                .returns(String::class)

        return funcBuilder.addStatement("return %S", customScheme).build()
    }


}