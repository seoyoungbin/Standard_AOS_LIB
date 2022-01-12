package com.syb.syblibrary.ui.view.web.util

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.syb.syblibrary.util.FileUtil
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FileChooser {

    companion object{
        const val REQUEST_FILE_CHOOSE_V11 = 10
        const val REQUEST_FILE_CHOOSE_V21 = 11
    }

    private var activity: AppCompatActivity
    var valueCallbackV11: ValueCallback<Uri>? = null
    var valueCallbackV21: ValueCallback<Array<Uri>>? = null

    var cameraPhotoFile: File? = null
    var cameraPhotoPath: String? = null
    var isPhotoMultiSelect = false

    constructor(@NonNull activity: AppCompatActivity)
    {
        this.activity = activity
    }

    /**
     * 사용자가 이미지 선택할 수 있도록 실행!
     * For Android 3.0+
     * @param requestCode 요청 코드
     * @param fileType 파일 유형
     * @param fileName 파일 이름
     */
    fun imageChooser(requestCode: Int, @NonNull fileType: Array<String>, @NonNull fileName: String)
    {
        var camIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val deviceCamera = camIntent?.resolveActivity(activity.packageManager) != null
        if (deviceCamera) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile(fileName)
                photoFile.deleteOnExit()
                camIntent?.putExtra("PhotoPath", cameraPhotoPath)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (photoFile != null) {
                val providerURI: Uri = FileProvider.getUriForFile(activity, activity.packageName + ".fileprovider", photoFile)
                cameraPhotoPath = providerURI.toString()
                cameraPhotoFile = photoFile
                camIntent?.putExtra(MediaStore.EXTRA_OUTPUT, providerURI)
            } else
                camIntent = null

        }
        startImageChooser(camIntent, requestCode, fileType)
    }

    /**
     * 이미지 선택 사용자 인터페이스 실행!
     * @param intent 이미지 캡쳐 화면
     * @param requestCode 요청 코드
     * @param mimeTypes
     */
    private fun startImageChooser(@Nullable intent: Intent?, requestCode: Int, @Nullable mimeTypes: Array<String>?)
    {
        val selectionIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, isPhotoMultiSelect)
            if(mimeTypes != null && mimeTypes.size > 0)
            {
                if(mimeTypes.size > 1)
                    putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                else
                    setType(mimeTypes[0])
            }
        }

        val intents: Array<Intent>
        if(intent != null)
            intents = arrayOf(intent)
        else
            intents = arrayOf(selectionIntent)

        val chooserIntent = Intent(Intent.ACTION_CHOOSER).apply {
            putExtra(Intent.EXTRA_INTENT, selectionIntent)
            putExtra(Intent.EXTRA_TITLE, "")
            putExtra(Intent.EXTRA_INITIAL_INTENTS, intents)
        }

        activity.startActivityForResult(chooserIntent, requestCode)
    }

    /**
     * 사용자가 선택한 이미지 웹뷰에 넘기기!
     * @param resultCode 사진 선택시 넘어온 결과코드
     * @param data 사진 선택 데이터
     */
    fun resolveFileChooseV11(resultCode: Int, data: Intent?)
    {
        if(resultCode == Activity.RESULT_OK)
        {
            val callback = valueCallbackV11
            val uri = data?.data?: null
            callback?.onReceiveValue(uri)
        }
        else
            valueCallbackV11?.onReceiveValue(null)

        photoFileValid()
        valueCallbackV11 = null
        cameraPhotoPath = null
        cameraPhotoFile = null
    }

    /**
     * 사용자가 선택한 이미지 웹뷰에 넘기기!
     * @param resultCode 사진 선택시 넘어온 결과코드
     * @param data 사진 선택 데이터
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("unchecked")
    fun resolveFileChooseV21(resultCode: Int, @Nullable data: Intent?)
    {
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.clipData != null) {
                    val count = data.clipData?.itemCount?: 0
                    val uris = arrayOf<Uri>()
                    for (i in 0 until count) {
                        data.clipData?.getItemAt(i)?.uri?.let {
                            uris[i] = it
                        }
                    }
                    val callback: ValueCallback<Array<Uri>>? = valueCallbackV21
                    callback?.onReceiveValue(uris)
                } else if (!data.dataString.isNullOrEmpty()) {
                    val callback: ValueCallback<Array<Uri>>? = valueCallbackV21
                    callback?.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data))
                } else {
                    if (cameraPhotoPath != null) {
                        val uri = Uri.parse(cameraPhotoPath)
                        val callback = valueCallbackV21
                        callback?.onReceiveValue(arrayOf(uri))
                    }
                }
            } else {
                if (cameraPhotoPath != null) {
                    val uri = Uri.parse(cameraPhotoPath)
                    val callback = valueCallbackV21
                    callback?.onReceiveValue(arrayOf(uri))
                }
            }
        } else
            valueCallbackV21?.onReceiveValue(null)

        photoFileValid()
        valueCallbackV21 = null
        cameraPhotoPath = null
        cameraPhotoFile = null
    }

    /**
     * 캡처 이미지 임시 파일 만드는 작업
     * @param fileName 파일 이름
     * @return 캡쳐 이미지 임시 파일
     */
    fun createImageFile(@Nullable fileName: String): File
    {
        val name = if(fileName == null) "jpg_" else fileName
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(Date())
        val imageFileName = name + timeStamp + "_"
        return File.createTempFile(imageFileName, ".jpg", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES))
    }

    /**
     * 캡쳐 파일 유효성 검사!!
     */
    private fun photoFileValid() {
        try {
            // 공백 이미지 파일 삭제!!
            cameraPhotoFile?.let {
                if (it != null && it.exists() && it.length() == 0L)
                    FileUtil.fileDelete(it)
            }
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
    }


}