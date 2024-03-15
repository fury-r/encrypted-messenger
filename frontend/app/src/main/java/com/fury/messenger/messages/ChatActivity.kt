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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fury.messenger.R
import com.fury.messenger.data.db.DBHelper
import com.fury.messenger.data.db.DBMessage.eraseDB_BUFFER
import com.fury.messenger.data.db.DBMessage.getDB_BUFFER
import com.fury.messenger.data.db.DBMessage.getMessageByTableName
import com.fury.messenger.data.db.DBMessage.insertMessage
import com.fury.messenger.data.db.DBMessage.listeners
import com.fury.messenger.data.db.DBMessage.presenfunc
import com.fury.messenger.data.db.DBMessage.sendSeenEvent
import com.fury.messenger.data.db.DBUser
import com.fury.messenger.data.db.DBUser.getPublicKeyForUser
import com.fury.messenger.data.db.model.Chat
import com.fury.messenger.data.db.model.Chats
import com.fury.messenger.data.db.model.Contact
import com.fury.messenger.data.db.model.ContactsModel
import com.fury.messenger.data.helper.user.CurrentUser
import com.fury.messenger.data.helper.user.CurrentUser.convertStringToKeyFactory
import com.fury.messenger.data.helper.user.CurrentUser.keyToString
import com.fury.messenger.manageBuilder.createAuthenticationStub
import com.fury.messenger.rsa.RSA.encryptMessage
import com.fury.messenger.rsa.RSA.generateEncryptionKeys
import com.google.firebase.auth.FirebaseAuth
import com.services.Message
import com.services.Message.Event
import com.services.Message.MessageInfo
import com.services.Message.MessageRequest
import com.services.Message.PubKeyExchange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.security.Key
import java.time.LocalDateTime

class ChatActivity : AppCompatActivity() {
    private lateinit var messageRecyleView:RecyclerView 
    private lateinit var messageBox: EditText
    private lateinit var sendBtn: ImageView
    private lateinit var title: TextView
    private lateinit var messageAdapter:MessageAdapter
    private lateinit var messageList:ArrayList<Chat?>
    private var  recipientDetails: Contact?=null
    private   var scope= CoroutineScope(Dispatchers.Main)
    private var receiverRoom:String?=null
    private var senderRoom:String?=null



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#322F2F")))
        this.supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.message_titlebar)
        val phoneNumber=intent.getStringExtra("phoneNumber")
        val Contact=intent.getStringExtra("Contact")

        val receiverUid=intent.getStringExtra("uid")
        val uri =intent.getStringExtra("uri")
        Log.d("uri",uri.toString())

        val client= createAuthenticationStub(CurrentUser.getToken())

        val senderUid=FirebaseAuth.getInstance().currentUser?.uid
        val dbHelper=DBHelper(this)


        senderRoom=receiverUid+senderUid
        receiverRoom=senderUid+receiverUid
        messageRecyleView=findViewById(R.id.chatRecyclerView)
        messageBox=findViewById(R.id.messagebox)
        title=findViewById(R.id.username)
        sendBtn=findViewById(R.id.sendBtn)
        messageList=ArrayList()
        title.text = Contact!!
        messageRecyleView=findViewById(R.id.chatRecyclerView)
        messageAdapter=MessageAdapter(this,messageList,receiverUid)
        listeners=fun(callback:(messages:ArrayList<Chat?>,recipient:String?)->ArrayList<Chat?>){

            this. messageAdapter.messageList= callback(messageList,phoneNumber)
            runOnUiThread {
                messageAdapter.notifyDataSetChanged()
            }
        }
        messageRecyleView.layoutManager= LinearLayoutManager(this)
        messageRecyleView.adapter=messageAdapter
        scope.launch {

            withContext(Dispatchers.IO){
                val recipientDetails=   DBUser.getDataByPhoneNumber(dbHelper,phoneNumber!!)

                this@ChatActivity.setRecipientDetails(recipientDetails)
                val messages=
                    recipientDetails.chatPrivateKey?.let { convertStringToKeyFactory(it,1) }?.let {
                        getMessageByTableName(recipientDetails.phoneNumber,dbHelper,
                            it
                        )
                    }
                if (messages != null) {
                    val hasUnread=messages.find { value->value?.isSeen==false }
                    if(hasUnread!=null){
                            sendSeenEvent(recipientDetails.phoneNumber)


                    }
                    setMessages(ArrayList(messages))
                }
            }
        }

        sendBtn.setOnClickListener {
        val messageText=messageBox.text.toString()
        scope.launch {
                recipientDetails?.pubKey.let { it1 ->
                    if (it1 != null) {
                        Log.d("key", it1)
                    }
                }
            var publicKey: Key? = getPublicKeyForUser(recipientDetails!!.phoneNumber)?.let { it1 ->
                   convertStringToKeyFactory(
                       it1,0)
               }

               transaction {

                   val id= encryptMessage(Chats.selectAll().count().toString(),
                       convertStringToKeyFactory(recipientDetails!!.pubKey!!,0)
                   )
                   // if publicKey is not present then perform handshake
                   if(publicKey==null){
                        // generate new private and public keys
                       val keys=generateEncryptionKeys()
                       ContactsModel.update({ ContactsModel.phoneNumber eq recipientDetails!!.phoneNumber}){
                           it[chatPrivateKey]= keyToString(keys[0].encoded)
                           it[chatPublickey]= keyToString( keys[1].encoded)

                       }
                       publicKey=keys[1]
                       val recipientPublicKey= convertStringToKeyFactory(recipientDetails!!.pubKey!!,0)
                       val message=PubKeyExchange.newBuilder().setSender(CurrentUser.getPhoneNumber()).setReciever(recipientDetails!!.phoneNumber).setPublicKey(
                           encryptMessage(keyToString(keys[0].encoded),recipientPublicKey)
                       ).setPrivateKey( encryptMessage(keyToString(keys[1].encoded),recipientPublicKey)).build()

                       val event=Event.newBuilder().setType(Message.EventType.HANDSHAKE).setExchange(message).build()
                       client.send(event)

                   }

                   val encryptedMessage=encryptMessage(messageText,
                       publicKey
                   )
                   val chat=  setMessageClass(id,messageText) as Chat
                   insertMessage(dbHelper,

                       Chat(chat.id,chat.sender,chat.receiver,encryptedMessage,chat.messageId,chat.contentType,chat.isSeen,chat.isDelivered,chat.type ,chat.createdAt))
                   val message=MessageInfo.newBuilder().setMessageId(id).setText(encryptedMessage).setSender(CurrentUser.getPhoneNumber()).setReciever(recipientDetails!!.phoneNumber).setContentType("text").build()
                   val chatRequestBuilder=MessageRequest.newBuilder().setMessage(message).setType(Message.MessageType.INSERT).build()
                   //TODO: FIX Enum issue
                   val event=Event.newBuilder().setType(Message.EventType.MESSAGE).setMessage(chatRequestBuilder).build()



                   client.send(event)

               }
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
      private fun setMessages(messages:ArrayList<Chat?>){
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
    private  fun addMessage(message:Chat?){
        this.messageList.add(this.messageList.size-1,message)
        messageAdapter.messageList=messageList
        runOnUiThread {
            messageAdapter.notifyDataSetChanged()
            messageRecyleView.scrollToPosition(this.messageList.size-1)

        }
    }

    private  fun setMessageClass(id:String, message: String): Chat? {


        val chat=     CurrentUser.getPhoneNumber()?.let {
            recipientDetails?.let { it1 ->
                Chat(0, it, it1.phoneNumber,message,id,"text", isDelivered = false, isSeen = false,Message.MessageType.INSERT,
                    LocalDateTime.now())
            }
        }
        if (chat != null) {
            this.addMessage(chat)
        }
        return  chat

    }
}