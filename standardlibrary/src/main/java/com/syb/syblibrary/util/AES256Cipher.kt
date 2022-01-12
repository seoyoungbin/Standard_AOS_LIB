package com.syb.syblibrary.util

import android.util.Base64
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AES256Cipher {

    var ENCRYPTION_KEY = "ENCRYPTION_KEY"

    var ivBytes =
        byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)


    @Throws(
        java.io.UnsupportedEncodingException::class,
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    fun AES_Encode(str: String, key: String): String =
        try {
            val textBytes = str.toByteArray(charset("UTF-8"))
            val ivSpec = IvParameterSpec(ivBytes)
            val newKey = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")
            var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec)

            Base64.encodeToString(cipher.doFinal(textBytes), 0)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }

    @Throws(
        java.io.UnsupportedEncodingException::class,
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    fun AES_Decode(str: String, key: String): String =
        try {
            val textBytes = Base64.decode(str, 0)
            val ivSpec = IvParameterSpec(ivBytes)
            val newKey = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec)
            String(cipher.doFinal(textBytes), charset("UTF-8"))
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }

}