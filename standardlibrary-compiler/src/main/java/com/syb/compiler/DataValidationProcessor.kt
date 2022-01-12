package com.syb.compiler

import com.google.auto.service.AutoService
import com.syb.annotations.datavalidation.*
import com.syb.annotations.datavalidation.model.FieldNameAndTag
import com.syb.annotations.datavalidation.model.ValidationResult
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.asTypeName
import java.io.File
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Name
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
class DataValidationProcessor : BaseProcessor() {

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
        val fileBuilder =
            FileSpec.builder("com.syb.compiler.datavalidation.generated", "DataValidationExtension")
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(
            DataValidation::class.java.name,
            Nested::class.java.name,
            NotNull::class.java.name,
            MinLength::class.java.name,
            MaxLength::class.java.name,
            MinValue::class.java.name,
            MaxValue::class.java.name,
            Regex::class.java.name
        )
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    //called twice or more
    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment
    ): Boolean {

        val classElements = roundEnv.getElementsAnnotatedWith(DataValidation::class.java)

        if (!checkElementType(ElementKind.CLASS, classElements)) return false

        classElements.forEach { fileBuilder.addFunction(makeValidateFunction(it)) }

        fileBuilder.addImport(FieldNameAndTag::class.java, "")
        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
        fileBuilder.build().writeTo(File(kaptKotlinGeneratedDir))
        return true
    }

    /**
     * 데이터 유효성 검증 함수 생성
     * @param classElement Annotation Class
     * @return 데이터 유효성 검증 함수 Builder
     */
    private fun makeValidateFunction(classElement: Element): FunSpec {
        val validateFunSpec = FunSpec.builder("validate")
            .receiver(classElement.asType().asTypeName())
            .returns(ValidationResult::class)
            .addStatement("val result = %T()", ValidationResult::class.java)

        val fieldElement = classElement.enclosedElements
        fieldElement.forEach {
            val nonNull = it.getAnnotation(NotNull::class.java)
            val minLength = it.getAnnotation(MinLength::class.java)
            val maxLength = it.getAnnotation(MaxLength::class.java)
            val minValue = it.getAnnotation(MinValue::class.java)
            val maxValue = it.getAnnotation(MaxValue::class.java)
            val regex = it.getAnnotation(Regex::class.java)
            val nested = it.getAnnotation(Nested::class.java)

            nonNull?.let { anno ->
                validateFunSpec.addComment("NonNull Check")
                validateFunSpec.beginControlFlow("if(${it.simpleName} == null)")
                validateFunSpec.addStatement("result.isValid = false")
                validateFunSpec.addStatement("result.invalidFieldNameAndTags.add(${createFieldNameAndTag(it.simpleName, anno.tag)})")
                validateFunSpec.endControlFlow()
            }

            minLength?.let { anno ->
                validateFunSpec.addComment("Minimum Length Check")
                validateFunSpec.beginControlFlow("if(${it.simpleName} == null || ${it.simpleName}.length < ${anno.length})")
                validateFunSpec.addStatement("result.isValid = false")
                validateFunSpec.addStatement("result.invalidFieldNameAndTags.add(${createFieldNameAndTag(it.simpleName, anno.tag)})")
                validateFunSpec.endControlFlow()
            }

            maxLength?.let { anno ->
                validateFunSpec.addComment("Maximum Length Check")
                validateFunSpec.beginControlFlow("if(${it.simpleName} == null || ${it.simpleName}.length > ${anno.length})")
                validateFunSpec.addStatement("result.isValid = false")
                validateFunSpec.addStatement("result.invalidFieldNameAndTags.add(${createFieldNameAndTag(it.simpleName, anno.tag)})")
                validateFunSpec.endControlFlow()
            }

            minValue?.let { anno ->
                validateFunSpec.addComment("Minimum Value Check")
                validateFunSpec.beginControlFlow("if(${it.simpleName} == null || ${it.simpleName}.toLong() < ${anno.value})")
                validateFunSpec.addStatement("result.isValid = false")
                validateFunSpec.addStatement("result.invalidFieldNameAndTags.add(${createFieldNameAndTag(it.simpleName, anno.tag)})")
                validateFunSpec.endControlFlow()
            }

            maxValue?.let { anno ->
                validateFunSpec.addComment("Minimum Value Check")
                validateFunSpec.beginControlFlow("if(${it.simpleName} == null || ${it.simpleName}.toLong() > ${anno.value})")
                validateFunSpec.addStatement("result.isValid = false")
                validateFunSpec.addStatement("result.invalidFieldNameAndTags.add(${createFieldNameAndTag(it.simpleName, anno.tag)})")
                validateFunSpec.endControlFlow()
            }

            regex?.let { anno ->
                validateFunSpec.addComment("Regex Match Check")
                validateFunSpec.beginControlFlow("if(${it.simpleName} == null || !%S.toRegex().matches(${it.simpleName}))", anno.regex)
                validateFunSpec.addStatement("result.isValid = false")
                validateFunSpec.addStatement("result.invalidFieldNameAndTags.add(${createFieldNameAndTag(it.simpleName, anno.tag)})")
                validateFunSpec.endControlFlow()
            }

            nested?.let { _ ->
                validateFunSpec.addComment("Nested Check")
                validateFunSpec.beginControlFlow("if(${it.simpleName} != null)")
                validateFunSpec.addStatement("val nestedValidation = ${it.simpleName}.validate()")
                validateFunSpec.beginControlFlow("if(!nestedValidation.isValid)")
                validateFunSpec.addStatement("result.isValid = false")
                validateFunSpec.addStatement("result.invalidFieldNameAndTags.addAll(nestedValidation.invalidFieldNameAndTags)")
                validateFunSpec.endControlFlow()
                validateFunSpec.endControlFlow()
            }
        }

        return validateFunSpec.addStatement("return result").build()
    }

    /**
     * FieldNameAndTag Class 생성
     * @param fieldName field 이름
     * @param tag 태그명
     * @return FieldNameAndTag Class 생성 코드
     */
    fun createFieldNameAndTag(fieldName: Name, tag: String): String {
        return "FieldNameAndTag(\"$fieldName\", \"$tag\")"
    }

}