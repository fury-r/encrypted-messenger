package com.fury.messenger;

import android.util.Base64
import android.util.Log
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import javax.crypto.spec.SecretKeySpec;

object TripleDES {
    //    public static String ALGO = "DESede/CBC/PKCS7Padding";
    var ALGO:String? = "DESede/ECB/PKCS7Padding"

    @Throws(Exception::class)
    fun _encrypt(message: String, secretKey: String): String {

        val cipher = Cipher.getInstance(ALGO)
        cipher.init(Cipher.ENCRYPT_MODE, getSecreteKey(secretKey))
        val plainTextBytes = message.toByteArray(charset("UTF-8"))
        val buf = cipher.doFinal(plainTextBytes)
        val base64Bytes =
            Base64.encode(buf, Base64.DEFAULT)
        Log.d("encrypt1",String(base64Bytes)+" "+secretKey)

        return String(base64Bytes)
    }

    @Throws(Exception::class)
    fun _decrypt(encryptedText: String, secretKey: String): String {
         val message = Base64.decode(encryptedText.toByteArray(), Base64.DEFAULT)
        val decipher = Cipher.getInstance(ALGO)
        decipher.init(Cipher.DECRYPT_MODE, getSecreteKey(secretKey))
        Log.d("encrypt2",message.toString()+" "+secretKey)
        val plainText = decipher.doFinal(message)
        return String(plainText,  charset("UTF-8"))
    }


    @Throws(Exception::class)
    fun getSecreteKey(secretKey: String): SecretKey {
        val md = MessageDigest.getInstance("SHA-1")
        val digestOfPassword =
            md.digest(secretKey.toByteArray(charset("utf-8")))
        val keyBytes = Arrays.copyOf(digestOfPassword, 24)
        return SecretKeySpec(keyBytes, "DESede")
    }
}