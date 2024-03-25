package com.fury.messenger.helper.user

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fury.messenger.data.db.DBMessage
import com.fury.messenger.data.db.DbConnect.getDatabase
import com.fury.messenger.data.db.helper.DateTypeConverters
import com.fury.messenger.data.db.model.Chat
import com.fury.messenger.data.db.model.ChatsDao
import com.fury.messenger.data.db.model.Contact
import com.fury.messenger.data.db.model.ContactsDao
import com.fury.messenger.kafka.ConsumerThread
import com.fury.messenger.kafka.RabbitMQ
import com.fury.messenger.manageBuilder.createAuthenticationStub
import com.fury.messenger.rsa.RSA
import com.google.protobuf.util.JsonFormat
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Channel
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import com.services.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.Key
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.time.OffsetDateTime
import java.util.Base64

object CurrentUser {

    var phoneNumber: String? = null
    private var uuid: String? = null

    private var phoneCountryCode: String? = null
    private var token: String = ""
    private var email: String? = null
    private var username: String? = null
    private var privateKey: PrivateKey? = null
    private var publicKey: PublicKey? = null
    private var MessageThread: ConsumerThread? = null
    private var NotificationThread: ConsumerThread? = null

    fun keyToString(key: ByteArray): String {
        return Base64.getEncoder().encodeToString(key)
    }


    fun convertStringToKeyFactory(key: String, type: Int): Key? {


        try {

            val keyBytes = Base64.getDecoder().decode(key)


            return if (type == 1) {
                val keySpec = PKCS8EncodedKeySpec(keyBytes)

                val keyFactory = KeyFactory.getInstance("RSA")
                keyFactory.generatePrivate(keySpec)
            } else {

                val keySpec = X509EncodedKeySpec(keyBytes)
                val keyFactory = KeyFactory.getInstance("RSA")
                keyFactory.generatePublic(keySpec)

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getPhoneCountryCode(): String? {
        return phoneCountryCode
    }

    fun getUsername(): String? {
        return username
    }

    fun getCurrentUserPhoneNumber(): String? {
        return phoneNumber
    }

    fun getToken(): String? {
        return token
    }

    fun getPublicKey(): PublicKey? {
        return publicKey
    }

    fun getPrivateKey(): PrivateKey? {
        return privateKey
    }

    fun setUsername(username: String? = "") {
        CurrentUser.username = username
    }

    fun getUUID(): String? {
        return uuid
    }

    fun setUUID(uuid: String? = "") {
        CurrentUser.uuid = uuid
    }

    fun setPhoneCountryCode(phoneCountryCode: String? = "") {
        CurrentUser.phoneCountryCode = phoneCountryCode
    }

    fun setToken(token: String = "") {
        CurrentUser.token = token
    }

    fun setCurrentUserPhoneNumber(phoneNumber: String?) {
        CurrentUser.phoneNumber = phoneNumber
        CurrentUser.phoneNumber?.let { Log.d("setting phone Number", it) }

    }

    fun setEmail(email: String? = "") {
        CurrentUser.email = email
    }

    fun setPublicKey(value: Any) {
        Log.d("Thread-Messenger key", "setting key $value")
        publicKey = if (value::class == String::class) {
            convertStringToKeyFactory(value as String, 2) as PublicKey
        } else {
            value as PublicKey
        }

    }

    fun setPrivateKey(value: Any) {
        privateKey = if (value::class == String::class) {
            convertStringToKeyFactory(value as String, 1) as PrivateKey
        } else {
            value as PrivateKey
        }
        Log.d(
            "thread-messenger set private  key as ",
            privateKey.toString() + " from " + value
        )

    }

    suspend fun startMessageThread(ctx: Context, dbHelper: SQLiteOpenHelper) {
        val scope = CoroutineScope(Dispatchers.Main)
        withContext(Dispatchers.IO) {
            val database=getDatabase()

            if (MessageThread == null || (MessageThread?.isAlive == false)) {

                val rabbitMq = RabbitMQ()
                var channel: Channel? = null

                val queueName = phoneNumber
                if (queueName != null) {
                    channel = rabbitMq.getChannel(queueName)


                    val consumer = object : DefaultConsumer(channel) {

                        override fun handleDelivery(
                            consumerTag: String?,
                            envelope: Envelope?,
                            properties: AMQP.BasicProperties?,
                            body: ByteArray?
                        ) {

                            val message = body?.let {
                                String(it, charset("UTF-8"))
                            }
                            if (message != null) {
                                val decryptMessage =
                                    RSA.decryptMessage(message, getPrivateKey())
                                val routingKey = envelope!!.routingKey
                                val contentType = properties!!.contentType
                                val deliveryTag = envelope.deliveryTag
                                val messageBuilder = Message.Event.newBuilder()
                                JsonFormat.parser().merge(decryptMessage, messageBuilder)
                                val event = messageBuilder.build()
                                val  db= getDatabase(ctx)

                                channel.basicAck(deliveryTag, false)

                                if (!envelope.isRedeliver) {


                                    //TODO: SQL Triggers

                                    val key=RSA.convertAESstringToKey(database.contactDao().findByNumber(event.reciever).key!!)

                                    when (event.type) {
                                        Message.EventType.MESSAGE -> {
                                            val messageBuilder = Message.MessageRequest.newBuilder()
                                            val contact=db.contactDao().findByNumber(event.reciever)
                                            JsonFormat.parser().merge(RSA.decryptAESMessage(event.message,RSA.convertAESstringToKey(contact.key)), messageBuilder)
                                            val messageObj=messageBuilder.build()
                                            scope.launch {
                                                DBMessage.messageThreadHandler(ctx,
                                                    messageObj
                                                )
                                            }
                                            when (messageObj.type) {
                                                Message.MessageType.INSERT -> {
                                                    val request =
                                                        Message.MessageRequest.newBuilder()
                                                            .setType(Message.MessageType.UPDATE)
                                                    val messageInfo =
                                                        Message.MessageInfo.newBuilder()
                                                            .setMessageId(messageObj.message.messageId)
                                                            .setSender(messageObj.message.reciever)
                                                            .setReciever(messageObj.message.sender)
                                                            .setDeliverStatus(true).build()
                                                    val eventBuild = Message.Event.newBuilder()
                                                        .setMessage(
                                                            RSA.encryptAESMessage(request.setMessage(messageInfo).toString(),key)
                                                        ).setType(Message.EventType.MESSAGE).build()
                                                    val client =
                                                        createAuthenticationStub(getToken())
                                                    client.send(eventBuild)
                                                }

                                                else -> {
                                                    Log.d(
                                                        "Messenger logs",
                                                        "Message type case not handle yet for reply."
                                                    )

                                                }
                                            }

                                        }

                                        Message.EventType.HANDSHAKE -> {
                                            val messageBuilder = Message.KeyExchange.newBuilder()
                                            val decryptMessage = RSA.decryptMessage(event.message,
                                                privateKey
                                            )

                                            JsonFormat.parser().merge(decryptMessage,messageBuilder )
                                            val messageObj=messageBuilder.build()
                                            scope.launch {
                                                DBMessage.saveHandshake(ctx,
                                                    messageObj
                                                )
                                            }

                                        }
                                        Message.EventType.TYPE_UPDATE->{
                                            val contact=db.contactDao().findByNumber(event.sender)
                                            contact.typeTime=OffsetDateTime.now()
                                            db.contactDao().update(contact)
                                        }
                                        else -> {
                                            Log.d(
                                                "Messenger logs",
                                                "Event type case not handle yet."
                                            )
                                        }
                                    }
//                                if(event.type==Message.EventType.MESSAGE ){
//                                    if(event.message.type==Message.MessageType.INSERT){
//                                        val request=Message.MessageRequest.newBuilder().setType(Message.MessageType.UPDATE)
//                                        val messageInfo=Message.MessageInfo.newBuilder().setMessageId(messageObj.messageId).setSender(messageObj.reciever).setReciever(messageObj.sender).setDeliverStatus(true).build()
//                                        val event= Message.Event.newBuilder().setMessage(request.setMessage(messageInfo).build()).setType(Message.EventType.MESSAGE).build()
//                                        val client= createAuthenticationStub(CurrentUser.getToken())
//                                        client.send(event)
//                                    }
//
//                                }
//
//                                else if(event.type===Message.EventType.HANDSHAKE){
//
//                                    scope.launch{
//                                        DBMessage.saveHandshake(
//                                            event.exchange
//                                        )
//                                    }
//                                }

                                }
                            }
                        }
                    }
                    getCurrentUserPhoneNumber().let { channel.basicConsume(it, consumer) }

                }
            }

        }

    }


    fun killMessageThread() {
        MessageThread!!.stop()
    }

}


@Database(entities = [Contact::class,Chat::class], version = 1)
@TypeConverters(*[DateTypeConverters::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactsDao
    abstract fun chatsDao(): ChatsDao

}