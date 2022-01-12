package com.syb.syblibrary.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.Settings

object IntentUtil {

    /**
     * 디바이스에 설치된 앱 실행
     * case1. 설치된 경우 해당 앱 실행
     * case2. 미설치된 경우 마켓으로 이동
     * @param context 컨텍스트
     * @param packgeName 실행할 앱 패키지명
     */
    fun startAnotherAppIntent(context: Context, packgeName: String)
    {
        try {
            val intent = context.packageManager.getLaunchIntentForPackage(packgeName)
            if(intent == null)
            {
                val marketIntent = Intent(Intent.ACTION_VIEW).apply {
                    setData(Uri.parse("market://details?id=$packgeName"))
                }
                context.startActivity(marketIntent)
            }
            else
                context.startActivity(intent)
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    /**
     * 플레이 스토어 마켓으로 이동
     * @param context 컨텍스트
     * @param packgeName 패키지명
     */
    fun startMarketIntent(context: Context, packgeName: String)
    {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setData(Uri.parse("market://details?id=$packgeName"))
        }
        context.startActivity(intent)
    }

    /**
     * 세팅 화면으로 이동
     * @param context 컨텍스트
     * @param packageName 패키지명
     */
    fun startSettingIntent(context: Context, packageName: String)
    {
        val intent = Intent().apply {
            setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            addCategory(Intent.CATEGORY_DEFAULT)
            setData(Uri.parse("package:$packageName"))
        }
        context.startActivity(intent)
    }

    /**
     * 외뷰 웹뷰 호출
     * @param context 컨텍스트
     * @param link 호출할 링크
     */
    fun startExternalWebViewIntent(context: Context, link: String)
    {
        try {
            val uri = Uri.parse(link)
            if(uri.isHierarchical)
            {
                val intent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(intent)
            }
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    /**
     * 사진 촬영 호출
     * @param activity 액티비티
     * @param requestCode 요청 코드
     */
    fun startDispatchTakePicture(activity: Activity, requestCode: Int)
    {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).let { takePictureIntent ->
            takePictureIntent.resolveActivity(activity.packageManager)?.let {
                activity.startActivityForResult(takePictureIntent, requestCode)
            }
        }
    }

    /**
     * 갤러리 호출
     * @param activity 액티비티
     * @param requestCode 요청 코드
     */
    fun startTakePictureFromGallery(activity: Activity, requestCode: Int) {
        activity.startActivityForResult(
            Intent.createChooser(
                Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                "Choose an image"
            ), requestCode)
    }


    /**
     * 주소록 호출
     * @param activity 액티비티
     * @param requestCode 요청 코드
     */
    fun startContactsContractIntent(activity: Activity?, requestCode: Int)
    {
        val intent = Intent(Intent.ACTION_PICK).apply {
            setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        }
        activity?.startActivityForResult(intent, requestCode)
    }

    /**
     * 인스타그램 이미지 공유
     * @param context 컨텍스트
     * @param imgUri 이미지 URI
     */
    fun startShareInstagram(context: Context, imgUri: Uri?)
    {
        try{
            val instagramPackageName = "com.instagram.android"
            val cIntent = context.packageManager.getLaunchIntentForPackage(instagramPackageName)
            if(cIntent == null)
            {
                val marketIntent = Intent(Intent.ACTION_VIEW).apply {
                    setData(Uri.parse("market://details?id=$instagramPackageName"))
                }
                context.startActivity(marketIntent)
            }
            else {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    setType("image/*")
                    putExtra(Intent.EXTRA_STREAM, imgUri)
                    setPackage(instagramPackageName)
                }
                context.startActivity(intent)
            }
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    /**
     * 푸시 알림 세팅 이동!!
     * @param activity 해당 액티비티
     * @param requestCode 요청 코드
     */
    fun startNotifiactionSetting(activity: Activity, requestCode: Int) {
        try {
            val intent = Intent()
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
                }

                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                    intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                    intent.putExtra("app_package", activity.packageName)
                    intent.putExtra("app_uid", activity.applicationInfo?.uid)
                }

                else -> {
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    intent.addCategory(Intent.CATEGORY_DEFAULT)
                    intent.data = Uri.parse("package:" + activity.packageName)
                }
            }
            activity.startActivityForResult(intent, requestCode)
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

}