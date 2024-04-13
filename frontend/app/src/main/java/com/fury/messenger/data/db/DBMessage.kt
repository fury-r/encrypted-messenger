package com.fury.messenger.data.db

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.fury.messenger.crypto.Crypto
import com.fury.messenger.crypto.Crypto.convertAESKeyToString
import com.fury.messenger.crypto.Crypto.decryptAESMessage
import com.fury.messenger.crypto.Crypto.encryptAESMessage
import com.fury.messenger.crypto.Crypto.encryptMessage
import com.fury.messenger.crypto.Crypto.runAESTest
import com.fury.messenger.data.db.DbConnect.getDatabase
import com.fury.messenger.data.db.model.Chat
import com.fury.messenger.data.db.model.Contact
import com.fury.messenger.helper.mutex.MutexLock
import com.fury.messenger.helper.notification.Notifications
import com.fury.messenger.helper.user.AppDatabase
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.manageBuilder.createAuthenticationStub
import com.google.protobuf.util.JsonFormat
import com.services.Message
import com.services.Message.ContentType
import com.services.Message.Event
import com.services.Message.KeyExchange
import com.services.Message.MessageInfo
import com.services.Message.MessageRequest
import com.services.Message.MessageType
import com.services.UserOuterClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime
import java.util.function.Supplier
import java.util.stream.Collectors
import javax.crypto.SecretKey


object DBMessage {
    val functions = arrayOf(::setMessages)
    private var scope = CoroutineScope(Dispatchers.Main)
    var presenfunc =
        fun(callback: (messages: ArrayList<Chat>, recipient: String?) -> ArrayList<Chat>) {
        }
    var listeners: ((ArrayList<Chat>, String?) -> ArrayList<Chat>) -> Unit =
        presenfunc
    private val DB_BUFFER: ArrayList<Chat> = arrayListOf<Chat>()


    private fun setMessages(messages: ArrayList<Chat>) {

    }

    fun getDB_BUFFER(): ArrayList<Chat> {
        return this.DB_BUFFER
    }

    fun eraseDB_BUFFER() {
        this.DB_BUFFER.clear()
    }

    private fun addAllData(messages: ArrayList<Chat>, recipient: String?): ArrayList<Chat> {
        messages.addAll(getDB_BUFFER())
        return messages
    }

    private fun notifyAllSubscribers() {
        this.listeners(this::addAllData)
        eraseDB_BUFFER()
    }


    // save handshake request in db
    fun saveHandshake(ctx: Context, message: KeyExchange) {

        val db = Room.databaseBuilder(
            ctx,
            AppDatabase::class.java, "main.db"
        ).build()
        val contact = db.contactDao().findByNumber(message.reciever)
        contact.key = message.key
        db.contactDao().update(contact)

    }

    suspend fun messageThreadHandler(ctx: Context, data: MessageRequest): Any {

        if (data.type == MessageType.INSERT) {

            val chat = Chat(
                sender =   data.message.sender,
                receiver =data.message.reciever,
                message = data.message.text,
                messageId =  data.message.messageId,
                contentType =  data.message.contentType.name,
                isDelivered = false,
                isSeen = false,
                type= MessageType.INSERT.name,
                createdAt =  OffsetDateTime.now(),
                updatedAt = OffsetDateTime.now()
            )
            Notifications.generateNotification(chat,ctx)

            insertMessage(ctx, chat)
            this.DB_BUFFER.add(chat)

        } else if (data.type == MessageType.UPDATE) {
            if (data.message.messageId.isEmpty()) {
                markAllAsRead(ctx, data.message.sender)


            } else {
                updateData(
                    ctx,
                    data.message.sender,
                    data.message.reciever,
                    data.message.contentType,
                    true,
                    data.message.readStatus,
                    data.message.messageId
                )

            }

        }
        notifyAllSubscribers()
        return 1
    }


    private fun markAllAsRead(ctx: Context, receiver: String) {

        val db = Room.databaseBuilder(
            ctx,
            AppDatabase::class.java, "main.db"
        ).build()

        db.chatsDao().markAllAsReadAndDelivered(true, true, null, receiver)

    }

    private fun insertMessage(ctx: Context, chat: Chat) {

     try{
         MutexLock.setDbLock(true)
         val db = Room.databaseBuilder(
             ctx,
             AppDatabase::class.java, "main.db"
         ).build()

         db.chatsDao().insertAll(chat)
     }catch (e:Exception){
         e.printStackTrace()
         Log.d("gfgdgfdgdfgdfg",chat.toString())
     }
    }

    @SuppressLint("SuspiciousIndentation")
    private suspend fun updateData(
        ctx: Context,
        sender: String,
        receiver: String,
        contentType: ContentType? = ContentType.Text,
        deliveryStatus: Boolean? = false,
        readStatus: Boolean? = false,
        messageId: String
    ) {
        withContext(Dispatchers.IO) {
            while (MutexLock.getDbLock()) {
                delay(1000)
            }
            val db = Room.databaseBuilder(
                ctx,
                AppDatabase::class.java, "main.db"
            ).build()
            MutexLock.setDbLock(true)



            if (deliveryStatus != null && readStatus != null) {
                db.chatsDao().markAllAsReadAndDelivered(deliveryStatus, readStatus, messageId)

            }

        }
    }

    @SuppressLint("Range")
    fun getMessageByTableName(
        ctx: Context,
        reciever: String
    ): List<Chat?> {
        val messageList: ArrayList<Chat> = arrayListOf<Chat>()
        Log.d("Messages-z", "start thread")
        val db = Room.databaseBuilder(
            ctx,
            AppDatabase::class.java, "main.db"
        ).build()


        return ArrayList(db.chatsDao().loadChatsByNumber(reciever))

    }

    fun initiateHandShake(recipientDetails: Contact, message: String? = null): SecretKey {
        Log.d("Initiate handshake", "Generate Symmetric key")

        val client = createAuthenticationStub(CurrentUser.getToken())

        // generate new private and public keys
        val encryptKey = Crypto.getAES()
        if (message != null) {
            runAESTest(encryptKey, message)
        }
        val contact =
            getDatabase().contactDao().findByNumber(recipientDetails.phoneNumber)
        contact.key = convertAESKeyToString(encryptKey)
        getDatabase().contactDao().update(contact)

        val recipientPublicKey =
            CurrentUser.convertStringToKeyFactory(recipientDetails.pubKey!!, 0)
        val message = KeyExchange.newBuilder()
            .setSender(CurrentUser.getCurrentUserPhoneNumber())
            .setReciever(recipientDetails.phoneNumber).setKey(
                contact.key!!
            ).build()

        val event = Event.newBuilder().setType(Message.EventType.HANDSHAKE)
            .setExchange(encryptMessage(message.toString(), recipientPublicKey))
            .setReciever(recipientDetails.phoneNumber).build()
        client.send(event)
        return encryptKey
    }

    fun sendMessage(
        ctx: Context,
        chat: Chat,
        reciever: Contact,
        encryptKey: SecretKey,
        messageType: ContentType
    ) {
        val client = createAuthenticationStub(CurrentUser.getToken())


        val messageInfo =
            MessageInfo.newBuilder().setMessageId(chat.messageId).setText(chat.message)
                .setSender(CurrentUser.getCurrentUserPhoneNumber())
                .setReciever(reciever.phoneNumber).setContentType(messageType)
                .build()

        this.insertMessage(
            ctx,
            chat
        )

        val jsonPrinter = JsonFormat.printer().includingDefaultValueFields()

        val messageRequestBuilder = MessageRequest.newBuilder().setType(MessageType.INSERT).setMessage(messageInfo)
           .build()

        val event =
            Event.newBuilder().setType(Message.EventType.MESSAGE).setReciever(
                reciever.phoneNumber
            ).setMessage(
                encryptAESMessage(
                    jsonPrinter.print(messageRequestBuilder),
                    encryptKey
                )
            ).build()

        val decryptObj=  decryptAESMessage(
            event.message,
            encryptKey
        )
        Log.d("message--x",decryptObj.toString())
        var messageR=MessageRequest.newBuilder()
        JsonFormat.parser().merge(decryptObj,messageR)

        val message= decryptAESMessage(messageR.build().message.text,encryptKey)
        Log.d("message--x",message)


        Log.d("Sensssd", jsonPrinter.print(event))

        client.send(event)


    }

    suspend fun sendSeenEvent(recipientNumber: String) {
        withContext(Dispatchers.IO) {
            val message = MessageInfo.newBuilder().setReciever(recipientNumber)
                .setSender(CurrentUser.getCurrentUserPhoneNumber()).setReadStatus(true).build()
            val messageRequest =
                MessageRequest.newBuilder().setMessage(message).setType(Message.MessageType.UPDATE)
                    .build()
            val database = getDatabase()
            val event =
                Event.newBuilder().setMessage(
                    encryptAESMessage(
                        messageRequest.toString(),
                        Crypto.convertAESstringToKey(
                            database.contactDao().findByNumber(recipientNumber).key!!
                        )
                    )
                ).setType(Message.EventType.MESSAGE)
                    .build()

            val client = createAuthenticationStub(CurrentUser.getToken())
            client.send(event)

        }

    }


    suspend fun blockUser(phoneNumber: String) {
        val isBlocked = CurrentUser.isBlocked(phoneNumber)
        val client = createAuthenticationStub(CurrentUser.getToken())

        val request = UserOuterClass.BlockRequest.newBuilder()
            .setNumber(phoneNumber)
        if (isBlocked) {
            request.setBlock(false)
        } else {
            request.setBlock(true)
        }

        try {
            val response = client.blockUser(request.build())
            val arrayList = response.blockedUsersList.stream().collect(
                Collectors.toCollection(
                    Supplier { ArrayList() })
            )
            CurrentUser.setBlockedUser(arrayList as ArrayList<String>)
        } catch (e: Error) {
            Log.d("Error while calling block User", e.toString())

        }
    }
}

