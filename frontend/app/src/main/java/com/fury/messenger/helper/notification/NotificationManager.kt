package com.fury.messenger.helper.notification

import android.content.Context
import androidx.core.app.NotificationCompat


object NotificationManager {

    fun generateNotification(senderName: String, text: String, context: Context) {
        var notification = NotificationCompat.Builder(context, "MESSENGER_CHANNEL")
            .setContentTitle(senderName)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle()).build()

//                .bigText(emailObject.getSubjectAndSnippet()))
//            .setLargeIcon(emailObject.getSenderAvatar())

    }

}