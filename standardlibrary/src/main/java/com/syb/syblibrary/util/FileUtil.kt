package com.syb.syblibrary.util

import android.content.ContentUris
import android.content.Context
import android.content.pm.ApplicationInfo
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import com.syb.syblibrary.util.log.YLog
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

object FileUtil {

    private const val BUFFER_SIZE = 1024 * 2

    /**
     * 파일 실제 경로 가져오기
     * @param context 컨텍스트
     * @param uri 파일 URL
     * @return 파일 실제 경로
     */
    fun getRealPathFromURI(context: Context, uri: Uri): String? { // DocumentProvider
        try {
            if (DocumentsContract.isDocumentUri(context, uri)) { // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    return if ("primary".equals(type, ignoreCase = true)) { (Environment.getExternalStorageDirectory().toString() + "/" + split[1])
                    } else {
                        val SDcardpath = getRemovableSDCardPath(context).split("/Android").toTypedArray()[0]
                        SDcardpath + "/" + split[1]
                    }
                } else if (isDownloadsDocument(uri)) {
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), id.toLong())
                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    if ("image" == type)
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    else if ("video" == type)
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    else if ("audio" == type)
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])
                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }
            }
            else if ("content".equals(uri.scheme, ignoreCase = true))
                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)
            else if ("file".equals(uri.scheme, ignoreCase = true))
                return uri.path
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
        return null
    }


    fun getRemovableSDCardPath(context: Context?): String {
        val storages: Array<File?> = ContextCompat.getExternalFilesDirs(context!!, null)
        return if (storages.size > 1 && storages[0] != null && storages[1] != null) storages[1].toString() else ""
    }


    fun getDataColumn(
        context: Context, uri: Uri?,
        selection: String?, selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(
                uri!!, projection,
                selection, selectionArgs, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index: Int = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            if (cursor != null) cursor.close()
        }
        return null
    }


    fun isExternalStorageDocument(uri: Uri): Boolean = "com.android.externalstorage.documents" == uri.authority


    fun isDownloadsDocument(uri: Uri): Boolean = "com.android.providers.downloads.documents" == uri.authority


    fun isMediaDocument(uri: Uri): Boolean = "com.android.providers.media.documents" == uri.authority


    fun isGooglePhotosUri(uri: Uri): Boolean = "com.google.android.apps.photos.content" == uri.authority

    /**
     * 업로드 이미지 이름 가져오기
     * @return 업로드 이미지 이름
     */
    fun getJPGFileName(): String {
        val date = Date()
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(date)
        val imageFileName = "Photo_" + timeStamp + ".jpg"
        return imageFileName
    }

    /**
     * 폴더 삭제
     * @param path 폴더 경로
     */
    fun folderDelete(path: String) {
        try {
            var folder = File(path)
            val childFileList = folder.listFiles()

            if (folder.exists()) {
                for (f in childFileList) {
                    if (f.isDirectory)
                        folderDelete(f.absolutePath)
                    else
                        f.delete()
                }
            }
            folder.delete()
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    /**
     * 파일 삭제
     * @param file 삭제할 파일
     */
    fun fileDelete(file: File)
    {
        if(file.exists()) {
            val fileName = file.name
            if(file.delete())
                YLog.i("File: $fileName 파일 삭제 성공!!")
            else
                YLog.i("File: $fileName 파일 삭제 실패!!")
        }
    }

    /**
     * 파일 존재 여부 확인
     * @param path 파일 경로
     * @return 파일 존재 여부
     */
    fun isFileExists(path: String): Boolean
    {
        val file = File(path)
        return file.exists()
    }

    /**
     * 로그 파일 출력
     * @param context 컨텍스트
     */
    fun createLogFile(context: Context)
    {

        try {
            val isDebuggable = (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
            if(isDebuggable) {
                val path = "${context.getExternalFilesDir("LogFile")}${File.separator}Log_${System.currentTimeMillis()}.txt"
                Runtime.getRuntime().exec(arrayOf("logcat", "-d", "-f", path))
                YLog.i("LogFile Create => Path : ${path}")
            }
        }catch (e: IOException)
        {
            e.printStackTrace()
        }
    }

    /**
     * 파일 용량 반환
     * @param path 파일 경로
     * @return 파일 용량
     */
    fun getFileLength(path: String): Long
    {
        var value = 0.toLong()
        val file = File(path)
        if(file.exists())
            value = file.length()
        return value
    }

    /**
     * 압축파일 풀기
     * @param zipPath 압축파일
     * @param targetDir 압축 풀 경로
     */
    fun unZip(zipPath: String, targetDir: String) {
        var fis: FileInputStream? = null
        var zis: ZipInputStream? = null
        var zentry: ZipEntry? = null
        try {
            fis = FileInputStream(File(zipPath))
            zis = ZipInputStream(fis)

            zentry = zis.nextEntry
            while (zentry != null) {
                var fileNameToUnzip = zentry.name
                var targetFile = File(targetDir, fileNameToUnzip)

                if (zentry.isDirectory) // directory인 경우
                {
                    var path = File(targetFile.absolutePath)
                    path.mkdirs()
                } else { // file인 경우
                    var path = File(targetFile.parent)
                    path.mkdirs()
                    unzipEntry(zis, targetFile)
                }
                zentry = zis.nextEntry
            }
        } finally {
            zis?.close()
            fis?.close()
        }
    }

    /**
     * 파일로 변환 작업
     * @param targetFile 변환할 파일
     */
    fun unzipEntry(zis: ZipInputStream, targetFile: File): File {
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(targetFile)

            var buffer = ByteArray(BUFFER_SIZE)
            var len = zis.read(buffer)
            while (len != -1) {
                fos.write(buffer, 0, len)
                len = zis.read(buffer)
            }
        } finally {
            fos?.close()
        }
        return targetFile
    }

    /**
     * 파일 Copy 작업
     * @param from Copy 할 파일
     * @param to Copy 후 덮어쓰일 파일
     */
    fun fileCopy(from: File, to: File) {
        var fis: FileInputStream? = null
        var fos: FileOutputStream? = null
        var inChannel: FileChannel? = null
        var outChannel: FileChannel? = null
        try {
            fis = FileInputStream(from)
            fos = FileOutputStream(to)
            inChannel = fis.channel
            outChannel = fos.channel

            inChannel.transferTo(0, inChannel.size(), outChannel)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            outChannel?.close()
            inChannel?.close()
            fis?.close()
            fos?.close()
        }
    }
}