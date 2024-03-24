package com.fury.messenger.data.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import androidx.room.Room
import com.fury.messenger.data.db.DbConnect.getDatabase
import com.fury.messenger.data.db.model.Chat
import com.fury.messenger.data.db.model.Contact
import com.fury.messenger.helper.mutex.MutexLock
import com.fury.messenger.helper.user.AppDatabase
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.manageBuilder.createAuthenticationStub
import com.fury.messenger.rsa.RSA
import com.fury.messenger.rsa.RSA.decryptMessage
import com.fury.messenger.rsa.RSA.encryptMessage
import com.services.Message
import com.services.Message.Event
import com.services.Message.KeyExchange
import com.services.Message.MessageInfo
import com.services.Message.MessageRequest
import com.services.Message.MessageType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.crypto.SecretKey


object DBMessage {
    val SQL_CREATE_QUERY = createTableQuery("Messages")
    val functions = arrayOf(::setMessages)
    private var scope = CoroutineScope(Dispatchers.Main)
    var presenfunc =
        fun(callback: (messages: ArrayList<Chat?>, recipient: String?) -> ArrayList<Chat?>) {

        }
    var listeners: (callback: (messages: ArrayList<Chat?>, recipient: String?) -> ArrayList<Chat?>) -> Unit =
        presenfunc
    private val DB_BUFFER: ArrayList<Chat?> = arrayListOf<Chat?>()
    const val SQL_DELETE_QUERY = "DROP TABLE IF EXISTS ${TableInfo.TABLE_NAME}"

    object TableInfo : BaseColumns {
        const val TABLE_NAME = "Messages"
        const val COLUMN_ITEM_SENDER = "sender"
        const val COLUMN_ITEM_RECEIVER = "receiver"
        const val COLUMN_ITEM_CONTENT_TYPE = "content_type"
        const val COLUMN_ITEM_MESSAGE = "message"
        const val COLUMN_ITEM_SEEN = "seen"
        const val COLUM_ITEM_DELIVERED = "is_delivered"
        const val COLUMN_ITEM_MESSAGE_ID = "message_id"
    }

    private fun setMessages(messages: ArrayList<Chat>) {

    }

    fun getDB_BUFFER(): ArrayList<Chat?> {
        return this.DB_BUFFER
    }

    fun eraseDB_BUFFER() {
        this.DB_BUFFER.clear()
    }

    private fun addAllData(messages: ArrayList<Chat?>, recipient: String?): ArrayList<Chat?> {
        messages.addAll(getDB_BUFFER())
        return messages
    }

    private fun notifyAllSubscribers() {
        this.listeners(this::addAllData)
        eraseDB_BUFFER()
    }

    private fun createTableQuery(tableName: String): String {
        return "CREATE TABLE IF NOT EXISTS ${tableName} (" +
                "${BaseColumns._ID} INTEGER  PRIMARY KEY," +
                "${TableInfo.COLUMN_ITEM_SENDER} VARCHAR ," +

                "${TableInfo.COLUMN_ITEM_RECEIVER} VARCHAR ," +
                "${TableInfo.COLUMN_ITEM_MESSAGE_ID} VARCHAR  UNIQUE," +

                "${TableInfo.COLUMN_ITEM_CONTENT_TYPE} VARCHAR ," +
                "${TableInfo.COLUMN_ITEM_MESSAGE} VARCHAR ," +
                "${TableInfo.COLUM_ITEM_DELIVERED} BOOL ," +
                "${TableInfo.COLUMN_ITEM_SEEN} BOOL )"
    }

    private fun hasMessage(id: String, tableName: String, dbHelper: SQLiteOpenHelper): Boolean {
        val db = dbHelper.writableDatabase

        val cursor: Cursor = db.query(
            tableName,
            arrayOf(BaseColumns._ID), BaseColumns._ID + " =?", arrayOf(id), null, null, null, "1"
        );
        return cursor.count > 0

    }


    // save handshake request in db
     fun saveHandshake(ctx:Context,message: KeyExchange) {

        val db = Room.databaseBuilder(
            ctx,
            AppDatabase::class.java, "main.db"
        ).build()
        val contact=db.contactDao().findByNumber(message.reciever)
        contact.key=message.key
        db.contactDao().update(contact)

    }

    suspend fun messageThreadHandler(ctx:Context,data: MessageRequest): Any {
        val privateKey = CurrentUser.getPrivateKey()!!
        if (data.type == com.services.Message.MessageType.INSERT) {
            val chat = Chat(
                1,
                data.message.sender,
                data.message.reciever,
                data.message.messageId,
                data.message.text,
                data.message.contentType,
                isDelivered = false,
                isSeen = false,
                MessageType.INSERT.toString(),
                null
            )

            insertMessage(ctx,chat)
            chat.message=decryptMessage(data.message.text, privateKey)!!
            this.DB_BUFFER.add(chat)

        } else if (data.type == MessageType.UPDATE) {
            if (data.message.messageId.isEmpty()) {
                markAllAsRead(ctx,data.message.sender)


            } else {
                updateData(ctx,
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


    private fun markAllAsRead(ctx: Context,receiver: String) {

       val db = Room.databaseBuilder(
           ctx,
           AppDatabase::class.java, "main.db"
       ).build()

       db.chatsDao().markAllAsReadAndDelivered(true,true ,null,receiver)

    }

    fun insertMessage(ctx: Context, chat: Chat) {

        MutexLock.setDbLock(true)
        val contentValue = ContentValues()
        val db = Room.databaseBuilder(
            ctx,
            AppDatabase::class.java, "main.db"
        ).build()


    db.chatsDao().insertAll(chat )
    }

    @SuppressLint("SuspiciousIndentation")
    private suspend fun updateData(
        ctx: Context,
        sender: String,
        receiver: String,
        contentType: String? = "text",
        deliveryStatus: Boolean?= false,
        readStatus: Boolean ?= false,
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
                    db.chatsDao().markAllAsReadAndDelivered(deliveryStatus, readStatus ,messageId)

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


        return  ArrayList(db.chatsDao().loadChatsByNumber(reciever))

    }
      fun initiateHandShake(recipientDetails:Contact): SecretKey {
        Log.d("Initiate handshake", "Generate Symmetric key")

        val client= createAuthenticationStub(CurrentUser.getToken())

        // generate new private and public keys
        val  encryptKey = RSA.getAES()!!
        val contact =
            DbConnect.getDatabase().contactDao().findByNumber(recipientDetails.phoneNumber!!)
        contact.key = RSA.convertAESKeyToString(encryptKey!!)
        DbConnect.getDatabase().contactDao().update(contact)

        val recipientPublicKey =
            CurrentUser.convertStringToKeyFactory(recipientDetails!!.pubKey!!, 0)
        val message = KeyExchange.newBuilder()
            .setSender(CurrentUser.getCurrentUserPhoneNumber())
            .setReciever(recipientDetails!!.phoneNumber).setKey(
                contact.key!!
            ).build()

        val event = Event.newBuilder().setType(Message.EventType.HANDSHAKE)
            .setExchange(encryptMessage(message.toString(), recipientPublicKey))
            .setReciever(recipientDetails!!.phoneNumber).build()
        client.send(event)
        return encryptKey
    }

     fun sendMessage(ctx: Context,chat:Chat,reciever:Contact,encryptKey:SecretKey){
        val client= createAuthenticationStub(CurrentUser.getToken())


        val message =
            MessageInfo.newBuilder().setMessageId(chat.messageId).setText(encryptMessage(chat.message,
                CurrentUser.convertStringToKeyFactory(reciever.pubKey!!,2)))
                .setSender(CurrentUser.getCurrentUserPhoneNumber())
                .setReciever(reciever.phoneNumber).setContentType("text")
                .build()
        chat.message= encryptMessage(chat.message,CurrentUser.getPublicKey())
        this.insertMessage(ctx,
            chat
        )

        Log.d("ee", message.toString())
        val chatRequestBuilder = MessageRequest.newBuilder().setMessage(message)
            .setType(MessageType.INSERT).build()
        //TODO: FIX Enum issue
        val event =
            Event.newBuilder().setType(Message.EventType.MESSAGE).setReciever(
                reciever.phoneNumber
            ).setMessage(
                RSA.encryptAESMessage(
                    chatRequestBuilder.toString(),
                    encryptKey
                )
            ).build()



        client.send(event)
    }

    suspend fun sendSeenEvent(recipientNumber: String) {
        withContext(Dispatchers.IO) {
            val message = MessageInfo.newBuilder().setReciever(recipientNumber)
                .setSender(CurrentUser.getCurrentUserPhoneNumber()).setReadStatus(true).build()
            val messageRequest =
                MessageRequest.newBuilder().setMessage(message).setType(Message.MessageType.UPDATE)
                    .build()
            val database=getDatabase()
            val event =
                Event.newBuilder().setMessage(RSA.encryptAESMessage(messageRequest.toString(),RSA.convertAESstringToKey(database.contactDao().findByNumber(recipientNumber).key!!))).setType(Message.EventType.MESSAGE)
                    .build()

            val client = createAuthenticationStub(CurrentUser.getToken())
            client.send(event)

        }
    }
}

