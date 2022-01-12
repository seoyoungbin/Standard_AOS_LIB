package com.syb.syblibrary.util

import android.util.Base64
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

object EncodingUtil {

    /**
     * MD5 암호화
     * @param text 텍스트
     * @return 암호화 된 텍스트
     */
    fun encryptionMD5(text: String): String
    {
        var MD5: String? = ""
        MD5 = try {
            val md = MessageDigest.getInstance("MD5")
            md.update(text.toByteArray())
            val byteData = md.digest()
            val sb = StringBuffer()
            for (i in byteData.indices) {
                sb.append(Integer.toString((byteData[i] and 0xff.toByte()) + 0x100, 16).substring(1))
            }
            sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            ""
        }
        return MD5
    }

    /**
     * SHA-256 암호화
     * @param text 암호화할 텍스트
     * @return 암호화된 텍스트
     */
    fun encryptionSHA256(text: String): String
    {
        var SHA: String? = ""
        SHA = try {
            val sh = MessageDigest.getInstance("SHA-256")
            sh.update(text.toByteArray())
            val byteData = sh.digest()
            val sb = StringBuffer()
            for (i in byteData.indices) {
                sb.append(Integer.toString((byteData[i] and 0xff.toByte()) + 0x100, 16).substring(1))
            }
            sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            ""
        }
        return SHA
    }

    /**
     * SHA-256 암호화(Base64)
     * @param text 암호화할 텍스트
     * @return 암호화된 텍스트
     */
    fun encryptionSHA256Base64(text: String): String
    {
        var SHA: String? = ""
        try {
            val sh = MessageDigest.getInstance("SHA-256")
            sh.update(text.toByteArray())
            val byteData = sh.digest()
            SHA = Base64.encodeToString(byteData, Base64.DEFAULT)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return SHA!!
    }

    /**
     * Base64 인코딩
     * @param text 인코딩할 텍스트
     * @return 인코딩된 텍스트
     */
    fun base64Encode(text: String): String = Base64.encodeToString(text.toByteArray(), 0)

    /**
     * Base64 디코딩
     * @param text 디코딩할 텍스트
     * @return 디코딩된 텍스트
     */
    fun base64Decode(text: String): String = String(Base64.decode(text, 0))

    /**
     * URL 인코딩
     * @param text 인코딩할 텍스트
     * @param charset
     * @return 인코딩된 텍스트
     */
    fun urlEncode(text: String, charset: String): String =
        try {
            URLEncoder.encode(text, charset)
        }catch (e: UnsupportedEncodingException)
        {
            e.printStackTrace()
            ""
        }

    /**
     * URL 디코딩
     * @param text 디코딩할 텍스트
     * @param charset
     * @return 디코딩된 텍스트
     */
    fun urlDecode(text: String, charset: String): String =
        try {
            URLDecoder.decode(text, charset)
        }catch (e: UnsupportedEncodingException)
        {
            e.printStackTrace()
            ""
        }

}