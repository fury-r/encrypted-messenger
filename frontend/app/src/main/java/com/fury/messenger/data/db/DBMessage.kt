package com.fury.messenger.data.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import com.fury.messenger.data.db.model.Chat
import com.fury.messenger.data.db.model.Chats
import com.fury.messenger.data.db.model.ContactsModel

import com.fury.messenger.data.helper.mutex.MutexLock
import com.fury.messenger.data.helper.user.CurrentUser
import com.fury.messenger.manageBuilder.createAuthenticationStub
import com.fury.messenger.rsa.RSA.decryptMessage
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
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.security.Key
import java.security.PrivateKey


object DBMessage {
    val SQL_CREATE_QUERY=createTableQuery("Messages")
    val functions = arrayOf(::setMessages)
    private   var scope= CoroutineScope(Dispatchers.Main)
    var presenfunc=fun(callback:(messages:ArrayList<Chat?>, recipient:String?)->ArrayList<Chat?>){

    }
    var listeners: (callback:(messages:ArrayList<Chat?>, recipient:String?)->ArrayList<Chat?>)->Unit =presenfunc
    private val DB_BUFFER:ArrayList<Chat?> = arrayListOf<Chat?>()
      const val SQL_DELETE_QUERY="DROP TABLE IF EXISTS ${TableInfo.TABLE_NAME}"
    object  TableInfo:BaseColumns{
        const val TABLE_NAME="Messages"
        const val COLUMN_ITEM_SENDER="sender"
        const val  COLUMN_ITEM_RECEIVER="receiver"
        const val COLUMN_ITEM_CONTENT_TYPE="content_type"
        const val COLUMN_ITEM_MESSAGE="message"
        const val COLUMN_ITEM_SEEN="seen"
        const val COLUM_ITEM_DELIVERED="is_delivered"
        const val COLUMN_ITEM_MESSAGE_ID="message_id"
    }
    private fun setMessages(messages:ArrayList<Chat>){

    }
    fun getDB_BUFFER(): ArrayList<Chat?> {
        return  this.DB_BUFFER
    }

    fun eraseDB_BUFFER(){
        this.DB_BUFFER.clear()
    }
    private fun addAllData(messages: ArrayList<Chat?>, recipient:String?): ArrayList<Chat?> {
        messages.addAll(getDB_BUFFER())
        return messages
    }
    private fun notifyAllSubscribers(){
        this.listeners(this::addAllData)
        eraseDB_BUFFER()
    }
    private fun createTableQuery(tableName: String):String{
     return   "CREATE TABLE IF NOT EXISTS ${tableName} ("+
                "${BaseColumns._ID} INTEGER  PRIMARY KEY,"+
                "${TableInfo.COLUMN_ITEM_SENDER} VARCHAR ,"+

                "${TableInfo.COLUMN_ITEM_RECEIVER} VARCHAR ,"+
                "${TableInfo.COLUMN_ITEM_MESSAGE_ID} VARCHAR  UNIQUE,"+

                "${TableInfo.COLUMN_ITEM_CONTENT_TYPE} VARCHAR ,"+
                "${TableInfo.COLUMN_ITEM_MESSAGE} VARCHAR ,"+
                "${TableInfo.COLUM_ITEM_DELIVERED} BOOL ,"+
                "${TableInfo.COLUMN_ITEM_SEEN} BOOL )"
    }

        private fun hasMessage(id:String, tableName:String, dbHelper: SQLiteOpenHelper):Boolean{
        val db=dbHelper.writableDatabase

        val cursor: Cursor =  db.query(tableName,
            arrayOf(BaseColumns._ID), BaseColumns._ID + " =?", arrayOf(id), null, null, null, "1");
        return cursor.count>0

    }


    // save handshake request in db
     fun saveHandshake(message:KeyExchange){
        transaction {
            ContactsModel.update ({ ContactsModel.phoneNumber eq message.sender }){
                it[chatPrivateKey]= decryptMessage(message.privateKey,CurrentUser.getPrivateKey())
                it[chatPublickey]= decryptMessage(message.publicKey,CurrentUser.getPrivateKey())

            }
        }
    }

    suspend fun messageThreadHandler(data: MessageRequest):Any {
        val privateKey=CurrentUser.getPrivateKey()!!
       if(data.type==com.services.Message.MessageType.INSERT){
           val chat=Chat(1,
               data.message.sender, data.message.reciever,
               data.message.messageId,  decryptMessage(data.message.text,privateKey)!!,data.message.contentType, isDelivered = false, isSeen = false,MessageType.INSERT,null)
           this.DB_BUFFER.add( chat)
            insertMessage(chat)


        }
        else if(data.type==MessageType.UPDATE){
            if(data.message.messageId.isEmpty()){
                markAllAsRead(data.message.sender)


            }else{
                updateData(data.message.sender,data.message.text,data.message.sender,data.message.reciever,data.message.contentType,true,data.message.readStatus,data.message.messageId)

            }

        }
        notifyAllSubscribers()
       return  1
    }


    private  fun markAllAsRead(receiver: String){
       transaction {
           Chats.update({(Chats.receiver eq receiver)  and (Chats.isSeen eq false)}){
               it[isSeen]=true
           }
       }



    }
    fun insertMessage( chat:Chat){

        MutexLock.setDbLock(true)
        val contentValue=ContentValues()


transaction {
    Chats.insert {
        it[sender]=chat.sender
        it[receiver]=chat.receiver
        it[message]=chat.message
        it[messageId]=chat.messageId
        it[contentType]=chat.contentType
        it[isSeen]=chat.isSeen
        it[isDelivered]=chat.isDelivered
        it[type]=chat.type.toString()
    }
}


    }

    @SuppressLint("SuspiciousIndentation")
    private  suspend fun  updateData(tableName:String, message:String, sender:String, receiver:String, contentType:String?="text", deliveryStatus:Boolean?=false, readStatus:Boolean?=false, messageId:String){
        withContext(Dispatchers.IO){
            while (MutexLock.getDbLock()){
                delay(1000)
            }
            MutexLock.setDbLock(true)

            transaction {
                Chats.update({ Chats.messageId eq messageId}){
                    it[isDelivered]= isDelivered
                    it[isSeen]= isSeen
                }
            }


        }
    }
    @SuppressLint("Range")
    fun getMessageByTableName(tableName: String, dbHelper: SQLiteOpenHelper,privateKey: Key): List<Chat?> {
        val messageList: ArrayList<Chat> = arrayListOf<Chat>()
        val db=dbHelper.readableDatabase
        Log.d("Messages-z","start thread")

       val messages= Chats.selectAll().where { Chats.receiver eq tableName }.orderBy(Chats.createdAt to SortOrder.DESC).map {
            (it[Chats.type] as? MessageType)?.let { it1 ->
                decryptMessage(it[Chats.message],privateKey as PrivateKey)?.let { it2 ->
                    Chat(
                        it[Chats.id].value,
                        it[Chats.sender],
                        it[Chats.receiver],
                        it[Chats.messageId],
                        it2,
                        it[Chats.contentType],
                        it[Chats.isDelivered],
                        it[Chats.isSeen],
                        it1,
                        it[Chats.createdAt]


                    )
                }
            }
        }

        return  messages

    }
    suspend fun  sendSeenEvent(recipientNumber:String){
    withContext(Dispatchers.IO){
        val message=MessageInfo.newBuilder().setReciever(recipientNumber).setSender(CurrentUser.getCurrentUserPhoneNumber()).setReadStatus(true).build()
        val messageRequest=MessageRequest.newBuilder().setMessage(message).setType(Message.MessageType.UPDATE).build()
        val event=Event.newBuilder().setMessage(messageRequest).setType(Message.EventType.MESSAGE).build()

        val client= createAuthenticationStub(CurrentUser.getToken())
        client.send(event)

    }
    }
}

