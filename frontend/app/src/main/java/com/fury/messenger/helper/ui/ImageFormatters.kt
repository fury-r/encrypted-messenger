package com.fury.messenger.helper.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

 fun base64StringToImage(base64String: String): Bitmap? {
    val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}