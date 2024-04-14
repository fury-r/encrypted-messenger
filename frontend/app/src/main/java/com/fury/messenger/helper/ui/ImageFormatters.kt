package com.fury.messenger.helper.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream

fun base64StringToImage(base64String: String): Bitmap? {
    val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

 fun convertImageToBase64String(uri: Uri,ctx:Context): String? {
    val inputStream = ctx.contentResolver.openInputStream(uri)
    val options = BitmapFactory.Options()
    options.inSampleSize = 2



    val byteArrayOutputStream = ByteArrayOutputStream()
    BitmapFactory.decodeStream(inputStream, null, options)
        ?.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
    inputStream?.close()

    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)

}
