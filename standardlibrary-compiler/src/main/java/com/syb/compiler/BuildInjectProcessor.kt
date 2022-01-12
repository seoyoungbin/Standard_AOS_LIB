package com.syb.compiler

import com.google.auto.service.AutoService
import com.syb.annotations.buildinject.BuildInject
import com.squareup.kotlinpoet.*
import java.io.File
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
class BuildInjectProcessor : BaseProcessor() {

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
        val fileBuilder =
            FileSpec.builder("com.syb.compiler.buildinject", "BuildInjection")
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(
                BuildInject::class.java.name
        )
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun process(
            annotations: MutableSet<out TypeElement>?,
            roundEnv: RoundEnvironment
    ): Boolean {

        val classElements = roundEnv.getElementsAnnotatedWith(BuildInject::class.java)

        if (!checkElementType(ElementKind.FIELD, classElements)) return false

        val typeSpec = TypeSpec.objectBuilder("BuildInjection")
        classElements.forEach { classElement ->

            val buildInject = classElement.getAnnotation(BuildInject::class.java)
            typeSpec.addFunction(
                    FunSpec.builder("bind")
                            .addModifiers(KModifier.PUBLIC)
                            .addParameter("parent", classElement.enclosingElement.asType().asTypeName())
                            .addParameter("buildType", String::class)
                            .beginControlFlow("if(buildType == \"release\")")
                            .addStatement("parent.%L = %S", classElement.simpleName, buildInject.release)
                            .endControlFlow()
                            .beginControlFlow("else")
                            .addStatement("parent.%L = %S", classElement.simpleName, buildInject.debug)
                            .endControlFlow()
                            .build())

        }

        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
        fileBuilder.addType(typeSpec.build())
        fileBuilder.build().writeTo(File(kaptKotlinGeneratedDir))
        return true

    }


}