package com.fury.messenger.helper.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.fury.messenger.R
import com.fury.messenger.crypto.Crypto
import com.fury.messenger.data.db.DbConnect
import com.fury.messenger.data.db.model.Chat
import com.services.Message.ContentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


object Notifications {
    val channelId = "e_message_notification_channel"
    const val notificationId = 1
    fun generateNotification(chat: Chat, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val db= DbConnect.getDatabase(context)
            val contact=db.contactDao().findByNumber(chat.sender)
            val notificationBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_person_white_24dp)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(NotificationCompat.BigTextStyle().bigText(chat.message))
                .setCategory(NotificationCompat.CATEGORY_MESSAGE).setAutoCancel(true)
                .setContentTitle(contact.name).setContentText(if( chat.contentType==ContentType.Audio.name) "Voice Message" else Crypto.decryptAESMessage(chat.message,Crypto.convertAESstringToKey(contact.key))).setStyle(NotificationCompat.BigTextStyle()).build()

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channel = NotificationChannel(
                channelId,
                "E-Message Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)

            notificationManager.notify(notificationId, notificationBuilder)

        }



    }

}