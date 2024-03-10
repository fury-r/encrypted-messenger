package com.fury.messenger.data.helper.notification

import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService


object NotificationManager {

    fun generateNotification(senderName:String,text:String,context:Context){
        var notification = NotificationCompat.Builder(context, "MESSENGER_CHANNEL")
            .setContentTitle(senderName)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle()).build()

//                .bigText(emailObject.getSubjectAndSnippet()))
//            .setLargeIcon(emailObject.getSenderAvatar())

    }

}