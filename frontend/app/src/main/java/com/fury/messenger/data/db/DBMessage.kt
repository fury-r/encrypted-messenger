package com.fury.messenger.data.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log

import com.fury.messenger.data.helper.mutex.MutexLock
import com.fury.messenger.data.helper.user.CurrentUser
import com.fury.messenger.manageBuilder.ManageChanelBuilder
import com.fury.messenger.rsa.RSA.decryptMessage
import com.services.Message
import com.services.Message.Event
import com.services.Message.MessageInfo
import com.services.Message.MessageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


object DBMessage {
    val SQL_CREATE_QUERY=createTableQuery("Messages")
    val functions = arrayOf(::setMessages)
    private   var scope= CoroutineScope(Dispatchers.Main)
    var presenfunc=fun(callback:(messages:ArrayList<Chat>, recipient:String?)->ArrayList<Chat>){

    }
    var listeners: (callback:(messages:ArrayList<Chat>, recipient:String?)->ArrayList<Chat>)->Unit =presenfunc
    private val DB_BUFFER:ArrayList<Chat> = arrayListOf<Chat>()
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
    fun getDB_BUFFER():ArrayList<Chat>{
        return  this.DB_BUFFER
    }

    fun eraseDB_BUFFER(){
        this.DB_BUFFER.clear()
    }
    private fun addAllData(messages: ArrayList<Chat>, recipient:String?): ArrayList<Chat> {
        messages.addAll(getDB_BUFFER())
        return messages
    }
    fun notifyAllSubscribers(){
        listeners(this::addAllData)
        eraseDB_BUFFER()
    }
    fun createTableQuery(tableName: String):String{
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



    suspend fun messageThreadHandler(data: MessageRequest, dbHelper: SQLiteOpenHelper):Any {
        val privateKey=CurrentUser.getPrivateKey()!!
       if(data.type==com.services.Message.MessageType.INSERT){
           this.DB_BUFFER.add( Chat("0",
               data.message.sender, data.message.reciever,
             data.message.messageId,  decryptMessage(data.message.text,privateKey)!!,data.message.contentType,false,false))
            insertMessage(dbHelper,data.message.sender,data.message.text,data.message.sender,data.message.reciever,data.message.contentType,true,false,data.message.messageId)


        }
        else if(data.type==com.services.Message.MessageType.UPDATE){
            if(data.message.messageId.isEmpty()){
                markAllAsRead(data.message.sender,dbHelper)


            }else{
                updateData(dbHelper,data.message.sender,data.message.text,data.message.sender,data.message.reciever,data.message.contentType,true,data.message.readStatus,data.message.messageId)

            }

        }
        notifyAllSubscribers()
       return  1
    }


    private  fun markAllAsRead(tableName: String, dbHelper: SQLiteOpenHelper){
        val db=dbHelper.writableDatabase
        db.beginTransaction()
        try {

            db.rawQuery("UPDATE table_$tableName  SET seen  = 1 " , null);

            this.listeners((fun(messages:ArrayList<Chat>, recipient:String?):ArrayList<Chat>{
                if (tableName.split("_")[1] ==recipient){
                    for(message in messages){
                        message.isSeen=true
                    }
                    if (recipient != null) {
                        scope.launch{
                            sendSeenEvent(recipient)
                        }
                    }
                }

                return messages
            }))

        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            db.endTransaction()
        }


    }
    fun insertMessage(dbHelper:SQLiteOpenHelper, tableName:String, message:String, sender:String, receiver:String, contentType:String?="text", deliveryStatus:Boolean?=false, readStatus:Boolean?=false, messageId:String?=null){

        MutexLock.setDbLock(true)
        val contentValue=ContentValues()
        val db=dbHelper.writableDatabase
        val  query="Select * FROM table_$tableName"
        val result=db.rawQuery(query,null)


        db.beginTransaction()
        try{
            contentValue.put(TableInfo.COLUMN_ITEM_MESSAGE,message)
            contentValue.put(TableInfo.COLUMN_ITEM_SENDER,sender)
            contentValue.put(TableInfo.COLUMN_ITEM_RECEIVER,receiver)
            contentValue.put(TableInfo.COLUMN_ITEM_MESSAGE_ID,messageId)

            contentValue.put(TableInfo.COLUMN_ITEM_CONTENT_TYPE,contentType)
            contentValue.put(TableInfo.COLUMN_ITEM_SEEN,readStatus)

            contentValue.put(TableInfo.COLUM_ITEM_DELIVERED,deliveryStatus)
            db.insert("table_$tableName",null,contentValue)
            db.setTransactionSuccessful()


        }
        finally {
            db.endTransaction()
            db.close()
            MutexLock.setDbLock(false)


        }

    }

    @SuppressLint("SuspiciousIndentation")
    private  suspend fun  updateData(dbHelper:SQLiteOpenHelper, tableName:String, message:String, sender:String, receiver:String, contentType:String?="text", deliveryStatus:Boolean?=false, readStatus:Boolean?=false, messageId:String){
        withContext(Dispatchers.IO){
            while (MutexLock.getDbLock()){
                delay(1000)
            }
            MutexLock.setDbLock(true)
            val db=dbHelper.writableDatabase
            db.beginTransaction()

            try{
                val contentValue= ContentValues()
                var id:String=messageId;


                    contentValue.put(TableInfo.COLUMN_ITEM_SEEN,readStatus)
                    contentValue.put(TableInfo.COLUMN_ITEM_SEEN,deliveryStatus)

                this@DBMessage.listeners((fun(messages:ArrayList<Chat>, recipient:String?):ArrayList<Chat>{
                    if (tableName.split("_")[1] ==recipient){
                        for(message in messages){
                            message.isSeen=true
                        }

                    }

                    return messages
                }))
               db.update("table_$tableName",contentValue,"${TableInfo.COLUMN_ITEM_MESSAGE_ID}=?" ,arrayOf(id))

                db.setTransactionSuccessful()
            }finally {
                db.endTransaction()
                MutexLock.setDbLock(false)

            }
        }
    }
    @SuppressLint("Range")
    fun getMessageByTableName(tableName: String, dbHelper: SQLiteOpenHelper): ArrayList<Chat> {
        val messageList: ArrayList<Chat> = arrayListOf<Chat>()
        val db=dbHelper.readableDatabase
        Log.d("Messages-z","start thread")

        val  query="Select * FROM table_$tableName"
        val result=db.rawQuery(query,null)
        if(result.moveToFirst()){
            do{
                val seen=result.getString(result.getColumnIndex(TableInfo.COLUMN_ITEM_SEEN))
                val deliver=result.getString(result.getColumnIndex(TableInfo.COLUM_ITEM_DELIVERED))

                val chat=Chat(result.getString(result.getColumnIndex(BaseColumns._ID)),result.getString(result.getColumnIndex(TableInfo.COLUMN_ITEM_SENDER)),result.getString(result.getColumnIndex(TableInfo.COLUMN_ITEM_RECEIVER)),result.getString(result.getColumnIndex(TableInfo.COLUMN_ITEM_MESSAGE_ID)),result.getString(result.getColumnIndex(TableInfo.COLUMN_ITEM_MESSAGE)),result.getString(result.getColumnIndex(TableInfo.COLUMN_ITEM_CONTENT_TYPE)),
                    deliver=="1",
                    seen=="1"
                )
                Log.d("thread-messenger","+" +
                        "message  "+result.getString(result.getColumnIndex(TableInfo.COLUMN_ITEM_SEEN)))
                chat.message=decryptMessage(chat.message)!!
                messageList.add(chat)
            }while (result.moveToNext())
        }
        return  messageList

    }
    suspend fun  sendSeenEvent(recipientNumber:String){
    withContext(Dispatchers.IO){
        val message=MessageInfo.newBuilder().setReciever(recipientNumber).setSender(CurrentUser.getPhoneNumber()).setReadStatus(true).build()
        val messageRequest=MessageRequest.newBuilder().setMessage(message).setType(Message.MessageType.UPDATE).build()
        val event=Event.newBuilder().setMessage(messageRequest).setType(Message.EventType.MESSAGE).setToken(CurrentUser.getToken()).build()

        val client=ManageChanelBuilder.client
        client.send(event)

    }
    }
}

