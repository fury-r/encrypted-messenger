package com.fury.messenger.helper.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.fury.messenger.R
import com.fury.messenger.crypto.Crypto
import com.fury.messenger.data.db.DbConnect
import com.fury.messenger.data.db.model.Chat
import com.fury.messenger.messages.ChatActivity
import com.fury.messenger.utils.ChatActivityIntentProps
import com.services.Message.ContentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object Notifications {
    private const val channelId = "e_message_notification_channel"
    private const val notificationId = 1
    fun generateNotification(chat: Chat, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val db= DbConnect.getDatabase(context)
            val contact=db.contactDao().findByNumber(chat.sender)
            val intent = Intent(context, ChatActivity::class.java)
            ChatActivityIntentProps(intent,contact)
            val msg=if( chat.contentType==ContentType.Audio.name) "Voice Message" else Crypto.decryptAESMessage(chat.message,Crypto.convertAESstringToKey(contact.key))
            var notificationBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT))
                .setCategory(NotificationCompat.CATEGORY_MESSAGE).setAutoCancel(true)
                .setContentTitle(contact.name)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
              .setContentText(msg.substring(0,20)+(if( msg.length>20) "..." else ""))
            if(msg.length>20){
                notificationBuilder= notificationBuilder.setStyle(NotificationCompat.BigTextStyle()
                    .bigText(msg))
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channel = NotificationChannel(
                channelId,
                "E-Message Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)

            notificationManager.notify(SimpleDateFormat("ddMMmmss",Locale.US).format(Date()).toInt(), notificationBuilder.build())

        }



    }

}