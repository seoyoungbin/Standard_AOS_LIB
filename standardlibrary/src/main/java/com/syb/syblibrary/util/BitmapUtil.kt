package com.syb.syblibrary.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.os.Environment.DIRECTORY_DCIM
import android.view.Display
import java.io.*


object BitmapUtil
{
    /**
     * ByteArray에서 Bitmap으로 변환!!
     * @param buffer 변환 데이터
     * @return Bitmap 데이터
     */
    fun bufferToBitmap(buffer: ByteArray): Bitmap?
    {
        try {
            val bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.size)
            return bitmap
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
        return null
    }

    /**
     * Bitmap에서 ByteArray으로 변환!!
     * @param bitmap 변환 데이터
     * @return ByteArray 데이터
     */
    fun bitmapToBuffer(bitmap: Bitmap): ByteArray?
    {
        try {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            return byteArray
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * Drawable에서 ByteArray으로 변환!!
     * @param drawable 변환 데이터
     * @return ByteArray 데이터
     */
    fun drawableToBuffer(drawable: Drawable): ByteArray?
    {
        val bitmap = (drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val bitmapdata = stream.toByteArray()
        return bitmapdata
    }

    /**
     * 화면 크기에 맞게 비트맵 사이즈 변경
     * @param activity 해당 실행 Activity
     * @param bitmap 해당 비트맵
     * @return 크기 변경된 비트맵
     */
    fun windowScaledResize(activity: Activity, bitmap: Bitmap): Bitmap?
    {
        try {
            // 화면 크기 구하기
            val display: Display = activity.windowManager.defaultDisplay
            val displayWidth = display.width
            val displayHeight = display.height

            // 화면 크기에 가장 근접하는 이미지의 리스케일 사이즈를 구한다.
            val widthScale = bitmap.width.toFloat() / displayWidth.toFloat()
            val heightScale = bitmap.height.toFloat() / displayHeight.toFloat()
            val scale = if (widthScale >= heightScale) widthScale else heightScale

            val resizeBitmap = Bitmap.createScaledBitmap(bitmap,(bitmap.width / scale).toInt(), (bitmap.height / scale).toInt(), true)

            var rotateBitmap = resizeBitmap
            if(resizeBitmap.width > resizeBitmap.height) {
                val matrix = Matrix()
                matrix.postRotate(90F)
                rotateBitmap = Bitmap.createBitmap(resizeBitmap, 0, 0, resizeBitmap.width, resizeBitmap.height, matrix, true)
            }


            return rotateBitmap
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 비트맵 상단 자르기!!
     * @param bitmap 원본 Bitmap
     * @param ratio 자를 비율
     * @return 수정 후 Bitmap
     */
    fun topCropBitmap(bitmap: Bitmap, ratio: Int): Bitmap?
    {
        try {
            val result = Bitmap.createBitmap(
                bitmap
                , 0
                , bitmap.height / ratio
                , bitmap.width
                , bitmap.height - (bitmap.height / ratio)
            )
            if (result != bitmap)
                bitmap.recycle()
            return result
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
        return null
    }

    /**
     * Bitmap PNG로 저장!!
     * 경로 => /storage/emulated/0/DCIM/앱 이름/파일명
     * @param context 컨텍스트
     * @param bitmap 저장할 Bitmap
     * @param name 파일명
     * @param folderName 폴더 이름
     */
    fun saveBitmapToPNG(context: Context, bitmap: Bitmap, name: String, folderName: String): File? {

        val dirPath = "${Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM).absolutePath}/${folderName}"
        val storage = File(dirPath)
        if(!storage.exists())
            storage.mkdirs()

        //저장할 파일 이름
        val fileName = "$name.png"

        //storage 에 파일 인스턴스를 생성합니다.
        val saveFile = File(storage, fileName)
        try {
            // 자동으로 빈 파일을 생성합니다.
            saveFile.createNewFile()

            // 파일을 쓸 수 있는 스트림을 준비합니다.
            val out = FileOutputStream(saveFile)

            // compress 함수를 사용해 스트림에 비트맵을 저장합니다.
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)

            // 스트림 사용후 닫아줍니다.
            out.close()

            val scanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(saveFile))
            context.sendBroadcast(scanIntent)
            return saveFile
        }
        catch (e: Exception)
        {
        }
        catch (e: FileNotFoundException) {

        } catch (e: IOException) {

        }
        return null
    }

}