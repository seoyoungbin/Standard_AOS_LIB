package com.syb.sample.ui.annotationsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.syb.annotations.buildinject.BuildInject
import com.syb.compiler.buildinject.BuildInjection
import com.syb.compiler.datavalidation.generated.validate
import com.syb.sample.BuildConfig
import com.syb.sample.R
import com.syb.sample.ui.annotationsample.data.Book
import com.syb.sample.ui.annotationsample.data.Publisher

class AnnotationSampleActivity : AppCompatActivity() {

    @BuildInject("release", "debug")
    lateinit var buildType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_annotation_sample)

        setUp()

    }

    fun setUp()
    {
        findViewById<Button>(R.id.btn).setOnClickListener {
            val book = Book(
                "힐링페이퍼 인재상", 0, "Thomas", "thomas@healingpaper/com",
                Publisher("높은 기준을 추구합니다. 소신있게 반대하고 헌신합니다. 틀릴 수도 있다고 생각합니다.")
            )

            val validationResult = book.validate()
            Log.v("Validation",
                StringBuilder()
                    .appendln("유효성: ${validationResult.isValid}")
                    .appendln("잘못된 필드: ${validationResult.invalidFieldNameAndTags.joinToString(", ", transform = {it.fieldName})}")
                    .appendln("메시지: ${validationResult.invalidFieldNameAndTags.joinToString(" & ", transform = { it.tag })}")
                    .toString()
            )
        }
        BuildInjection.bind(this, BuildConfig.BUILD_TYPE)

        Log.i("T", "buildType value => ${buildType}")
    }
}