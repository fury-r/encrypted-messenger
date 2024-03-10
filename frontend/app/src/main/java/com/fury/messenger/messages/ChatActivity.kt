package com.fury.messenger.messages

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fury.messenger.R
import com.fury.messenger.TripleDES
import com.fury.messenger.data.db.Chat
import com.fury.messenger.data.db.DBHelper
import com.fury.messenger.data.db.DBMessage
import com.fury.messenger.data.db.DBMessage.eraseDB_BUFFER
import com.fury.messenger.data.db.DBMessage.getDB_BUFFER
import com.fury.messenger.data.db.DBMessage.getMessageByTableName
import com.fury.messenger.data.db.DBMessage.listeners
import com.fury.messenger.data.db.DBMessage.presenfunc
import com.fury.messenger.data.db.DBMessage.sendSeenEvent
import com.fury.messenger.data.db.DBUser
import com.fury.messenger.data.helper.contact.Contact
import com.fury.messenger.data.helper.user.CurrentUser
import com.fury.messenger.data.helper.user.CurrentUser.convertStringToKeyFactory
import com.fury.messenger.manageBuilder.ManageChanelBuilder
import com.fury.messenger.rsa.RSA.decryptMessage
import com.fury.messenger.rsa.RSA.encryptMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.services.Message
import com.services.Message.Event
import com.services.Message.MessageInfo
import com.services.Message.MessageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.PublicKey
import kotlin.concurrent.thread

class ChatActivity : AppCompatActivity() {
    private lateinit var messageRecyleView:RecyclerView 
    private lateinit var messageBox: EditText
    private lateinit var sendBtn: ImageView
    private lateinit var title: TextView
    private lateinit var messageAdapter:MessageAdapter
    private lateinit var messageList:ArrayList<Chat>
    private var  recipientDetails:Contact?=null
    private   var scope= CoroutineScope(Dispatchers.Main)
     var receiverRoom:String?=null
      var senderRoom:String?=null



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#322F2F")))
        this.supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM;
        supportActionBar?.setDisplayShowCustomEnabled(true);
        supportActionBar?.setCustomView(R.layout.message_titlebar);
        val phoneNumber=intent.getStringExtra("phoneNumber")
        val name=intent.getStringExtra("name")

        val receiverUid=intent.getStringExtra("uid")
        val uri =intent.getStringExtra("uri")
        Log.d("uri",uri.toString())

        val channel=ManageChanelBuilder.channel
        val client=ManageChanelBuilder.client

        val senderUid=FirebaseAuth.getInstance().currentUser?.uid
        val dbHelper=DBHelper(this)


        senderRoom=receiverUid+senderUid
        receiverRoom=senderUid+receiverUid
        messageRecyleView=findViewById(R.id.chatRecyclerView)
        messageBox=findViewById(R.id.messagebox)
        title=findViewById(R.id.username)
        sendBtn=findViewById(R.id.sendBtn)
        messageList=ArrayList()
        title.text = name!!
        messageRecyleView=findViewById(R.id.chatRecyclerView)
        messageAdapter=MessageAdapter(this,messageList,receiverUid)
        DBMessage.listeners=fun(callback:(messages:ArrayList<Chat>,recipient:String?)->ArrayList<Chat>){

            this. messageAdapter.messageList= callback(messageList,phoneNumber)
            runOnUiThread {
                messageAdapter.notifyDataSetChanged()
            }
        }
        messageRecyleView.layoutManager= LinearLayoutManager(this)
        messageRecyleView.adapter=messageAdapter
        scope.launch {

            withContext(Dispatchers.IO){
                var recipientDetails=   DBUser.getDataByPhoneNumber(dbHelper,phoneNumber!!)

                if (recipientDetails != null) {
                    this@ChatActivity.setRecipientDetails(recipientDetails)
                }
                val messages=
                    recipientDetails?.getPhoneNumber()?.let { getMessageByTableName(it,dbHelper) }
                if (messages != null) {
                    val hasUnread=messages.find { value->value.isSeen==false }
                    if(hasUnread!=null && recipientDetails != null){
                            sendSeenEvent(recipientDetails.getPhoneNumber())


                    }
                    setMessages(messages)
                }
            }
        }

        sendBtn.setOnClickListener {
        val messageText=messageBox.text.toString()
        scope.launch {
                recipientDetails?.getPubKey()?.let { it1 -> Log.d("key", it1) }
                val encryptedMessage=encryptMessage(messageText,
                    recipientDetails?.getPubKey()?.let { it1 -> convertStringToKeyFactory(it1,2) } as PublicKey
                )

                val dbHelper=DBHelper(this@ChatActivity)

                val  query="Select * FROM table_${recipientDetails!!.getPhoneNumber()}"
                val db=dbHelper.readableDatabase
                val result=db.rawQuery(query,null)
                //INFO: Hashing the Id so that it creates a new string which can act as an unique ID
                val id=CurrentUser.getPublicKey()?.let { it1 ->
                    encryptMessage(result.count.toString(),it1)}
                val myEncryptMessage= CurrentUser.getPublicKey()?.let { it1 ->
                    encryptMessage(messageText,it1)}


            if ( id!=null) {
                setMessageClass(id,messageText)
            }



            if (myEncryptMessage != null) {
                com.fury.messenger.data.db.DBMessage.insertMessage(dbHelper, "${recipientDetails!!.getPhoneNumber()}",
                    myEncryptMessage,
                    CurrentUser.getPhoneNumber()!!,
                    recipientDetails!!.getPhoneNumber()!!, messageId = id)
            }



                val message=MessageInfo.newBuilder().setMessageId(id).setText(encryptedMessage).setSender(CurrentUser.getPhoneNumber()).setReciever(recipientDetails!!.getPhoneNumber()).setContentType("text").build()
                val chatRequestBuilder=MessageRequest.newBuilder().setMessage(message).setType(Message.MessageType.INSERT).build()
                //TODO: FIX Enum issue
               val event=Event.newBuilder().setType(Message.EventType.MESSAGE).setMessage(chatRequestBuilder).setToken(CurrentUser.getToken()).build()



            client.send(event)


        }
        messageBox.setText("")

    }


    }


    override fun onDestroy() {
        super.onDestroy()
        listeners=presenfunc
    }
    suspend fun startListener(){
        scope.run {
            while (true){
                while (getDB_BUFFER().size>0){
                    getDB_BUFFER().forEach{value->addMessage(value)}
                }
                delay(1000)

                eraseDB_BUFFER()
            }

        }
    }
      private fun setMessages(messages:ArrayList<Chat>){
          this. messageAdapter.messageList=messages
          this.messageList=messages
          runOnUiThread {
              messageAdapter.notifyDataSetChanged()
              messageRecyleView.scrollToPosition(this.messageList.size-1)

          }
      }
    private fun setRecipientDetails(details: Contact){
        this.recipientDetails=details
    }
    private  fun addMessage(message:Chat){
        this.messageList.add(message)
        messageAdapter.messageList=messageList
        runOnUiThread {
            messageAdapter.notifyDataSetChanged()
            messageRecyleView.scrollToPosition(this.messageList.size-1)

        }
    }

    private  fun setMessageClass(id:String, message: String){
        val chat= CurrentUser.getPhoneNumber()?.let { it1 ->
            Chat(id,
                it1, recipientDetails!!.getPhoneNumber(),id,message,"text",false,false)
        }
        if (chat != null) {
            this.addMessage(chat)
        }
    }
}