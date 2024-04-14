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
import com.fury.messenger.helper.contact.Contacts
import com.fury.messenger.helper.mutex.MutexLock
import com.fury.messenger.helper.notification.Notifications
import com.fury.messenger.helper.user.AppDatabase
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.manageBuilder.createAuthenticationStub
import com.google.protobuf.util.JsonFormat
import com.services.ContactOuterClass
import com.services.Message
import com.services.Message.ContentType
import com.services.Message.Event
import com.services.Message.EventType
import com.services.Message.KeyExchange
import com.services.Message.MessageInfo
import com.services.Message.MessageRequest
import com.services.Message.MessageType
import com.services.UserOuterClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime
import java.util.function.Supplier
import java.util.stream.Collectors
import javax.crypto.SecretKey



class UserEvent(var  eventType:EventType, var message: Chat?)
object DBMessage {
    val functions = arrayOf(::setMessages)
    private var scope = CoroutineScope(Dispatchers.Main)
    var presenfunc =
        fun(_: (messages: ArrayList<UserEvent>, recipient: String?) -> ArrayList<UserEvent>) {
        }
    var listeners: ((ArrayList<UserEvent>, String?) -> ArrayList<UserEvent>) -> Unit =
        presenfunc

    private val DB_BUFFER: ArrayList<UserEvent> = arrayListOf<UserEvent>()


    private fun setMessages(messages: ArrayList<Chat>) {

    }

    fun getDB_BUFFER(): ArrayList<UserEvent> {
        return this.DB_BUFFER
    }

    fun eraseDB_BUFFER() {
        this.DB_BUFFER.clear()
    }

    private fun addAllData(messages: ArrayList<UserEvent>, recipient: String?): ArrayList<UserEvent> {
        messages.addAll(getDB_BUFFER())
        return messages
    }

    private fun notifyAllSubscribers() {
        this.listeners(this::addAllData)
        eraseDB_BUFFER()
    }


    // save handshake request in db
    private fun saveHandshake(ctx: Context, message: KeyExchange) {


        val db = Room.databaseBuilder(
            ctx,
            AppDatabase::class.java, "main.db"
        ).build()
        val contact = db.contactDao().findByNumber(message.reciever)
        if (contact != null) {
            contact.key = message.key
            db.contactDao().update(contact)
        } else {
            scope.launch {
                val client = createAuthenticationStub(CurrentUser.getToken())
                val contact =
                    ContactOuterClass.Contact.newBuilder().setPhoneNumber(message.sender).build()

                val request = ContactOuterClass.ContactsList.newBuilder()
                request.addContacts(contact)
                val response = client.validateContacts(request.build())

                Contacts(ctx).addToContactList(response)
            }
        }


    }

    private suspend fun messageThreadHandler(ctx: Context, data: MessageRequest): Any {

        if (data.type == MessageType.INSERT) {

            val chat = Chat(
                sender = data.message.sender,
                receiver = data.message.reciever,
                message = data.message.text,
                messageId = data.message.messageId,
                contentType = data.message.contentType.name,
                isDelivered = false,
                isSeen = false,
                type = MessageType.INSERT.name,
                createdAt = OffsetDateTime.now(),
                updatedAt = OffsetDateTime.now()
            )
            Notifications.generateNotification(chat, ctx)

            insertMessage(ctx, chat)
            this.DB_BUFFER.add(UserEvent(EventType.MESSAGE,chat))

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

        try {
            MutexLock.setDbLock(true)
            val db = Room.databaseBuilder(
                ctx,
                AppDatabase::class.java, "main.db"
            ).build()

            db.chatsDao().insertAll(chat)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("gfgdgfdgdfgdfg", chat.toString())
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

        val event = Event.newBuilder().setType(EventType.HANDSHAKE)
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

        val messageRequestBuilder =
            MessageRequest.newBuilder().setType(MessageType.INSERT).setMessage(messageInfo)
                .build()

        val event =
            Event.newBuilder().setType(EventType.MESSAGE).setReciever(
                reciever.phoneNumber
            ).setMessage(
                encryptAESMessage(
                    jsonPrinter.print(messageRequestBuilder),
                    encryptKey
                )
            ).build()

        val decryptObj = decryptAESMessage(
            event.message,
            encryptKey
        )
        Log.d("message--x", decryptObj.toString())
        var messageR = MessageRequest.newBuilder()
        JsonFormat.parser().merge(decryptObj, messageR)

        val message = decryptAESMessage(messageR.build().message.text, encryptKey)
        Log.d("message--x", message)


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
                ).setType(EventType.MESSAGE)
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

    suspend fun processMessageAndSave(body: ByteArray, ctx: Context) {
        val message = String(body, charset("UTF-8"))
        Log.d("here", message)
        val decryptMessage =
            Crypto.decryptMessage(message, CurrentUser.getPrivateKey())

        val messageBuilder = Message.Event.newBuilder()
        JsonFormat.parser().merge(message, messageBuilder)
        val event = messageBuilder.build()
        val db = getDatabase(ctx)




        if (!CurrentUser.isBlocked(event.reciever)) {
            //TODO: SQL Triggers

            val key = Crypto.convertAESstringToKey(
                db.contactDao().findByNumber(event.reciever).key!!
            )

            when (event.type) {
                EventType.MESSAGE -> {
                    val messageBuilder =
                        Message.MessageRequest.newBuilder()
                    val contact =
                        db.contactDao().findByNumber(event.reciever)
                    Log.d(
                        "message", Crypto.decryptAESMessage(
                            event.message,
                            Crypto.convertAESstringToKey(contact.key)
                        )
                    )
                    JsonFormat.parser().merge(
                        Crypto.decryptAESMessage(
                            event.message,
                            Crypto.convertAESstringToKey(contact.key)
                        ), messageBuilder
                    )

                    val messageObj = messageBuilder.build()
                    Log.d(
                        "message", Crypto.decryptAESMessage(
                            event.message,
                            Crypto.convertAESstringToKey(contact.key)
                        )
                    )


                    when (messageObj.type) {
                        Message.MessageType.INSERT -> {
                            DBMessage.messageThreadHandler(
                                ctx,
                                messageObj
                            )
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
                                    Crypto.encryptAESMessage(
                                        request.setMessage(
                                            messageInfo
                                        ).toString(), key
                                    )
                                ).setType(EventType.MESSAGE)
                                .build()
                            val client =
                                createAuthenticationStub(CurrentUser.getToken())
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

                EventType.HANDSHAKE -> {
                    // TODO handle handhsake request even though previously handshake was done
                    val messageBuilder =
                        Message.KeyExchange.newBuilder()
                    val decryptMessage = Crypto.decryptMessage(
                        event.message,
                        CurrentUser.getPrivateKey()
                    )

                    JsonFormat.parser()
                        .merge(decryptMessage, messageBuilder)
                    val messageObj = messageBuilder.build()
                    saveHandshake(
                        ctx,
                        messageObj
                    )


                }

                EventType.TYPE_UPDATE -> {
                    val contact =
                        db.contactDao().findByNumber(event.sender)
                    contact.typeTime = OffsetDateTime.now()
                    db.contactDao().update(contact)
                    this.DB_BUFFER.add(UserEvent(EventType.HANDSHAKE,
                        Chat(sender = event.sender, receiver = "", contentType = ContentType.Text.name, isDelivered = false, isSeen = false, message = "", messageId = "", createdAt = contact.typeTime)))

                }

                else -> {
                    Log.d(
                        "Messenger: Event handler",
                        "Event type case not handle yet."
                    )
                }
            }


        }

    }

}

