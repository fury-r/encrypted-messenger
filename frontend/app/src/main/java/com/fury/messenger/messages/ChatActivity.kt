package com.fury.messenger.messages

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fury.messenger.R
import com.fury.messenger.data.db.DBMessage
import com.fury.messenger.data.db.DBMessage.eraseDB_BUFFER
import com.fury.messenger.data.db.DBMessage.getDB_BUFFER
import com.fury.messenger.data.db.DBMessage.initiateHandShake
import com.fury.messenger.data.db.DBMessage.listeners
import com.fury.messenger.data.db.DBMessage.presenfunc
import com.fury.messenger.data.db.DBMessage.sendMessage
import com.fury.messenger.data.db.DBUser
import com.fury.messenger.data.db.DbConnect
import com.fury.messenger.data.db.model.Chat
import com.fury.messenger.data.db.model.Contact
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.helper.user.CurrentUser.convertStringToKeyFactory
import com.fury.messenger.manageBuilder.createAuthenticationStub
import com.fury.messenger.rsa.RSA
import com.google.firebase.auth.FirebaseAuth
import com.services.Message
import com.services.ServicesGrpc.ServicesBlockingStub
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime
import javax.crypto.SecretKey

class ChatActivity : AppCompatActivity() {
    private lateinit var messageRecyleView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendBtn: ImageView
    private lateinit var title: TextView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Chat?>
    private var recipientDetails: Contact? = null
    private var scope = CoroutineScope(Dispatchers.Main)
    private var receiverRoom: String? = null
    private var senderRoom: String? = null
    private  lateinit var client:ServicesBlockingStub;
    private lateinit var  progressBar: ProgressBar


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#322F2F")))
        this.supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.message_titlebar)
        val phoneNumber = intent.getStringExtra("phoneNumber")
        val Contact = intent.getStringExtra("Contact")

        val receiverUid = intent.getStringExtra("uid")
        val uri = intent.getStringExtra("uri")
        Log.d("uri", uri.toString())

         client = createAuthenticationStub(CurrentUser.getToken())

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid


        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid
        messageRecyleView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messagebox)
        title = findViewById(R.id.username)
        sendBtn = findViewById(R.id.sendBtn)
        progressBar=findViewById(R.id.progressBar)
        messageList = ArrayList()
        title.text = Contact!!
        messageRecyleView = findViewById(R.id.chatRecyclerView)
        messageAdapter = MessageAdapter(this, messageList, receiverUid)
        listeners =
            @SuppressLint("NotifyDataSetChanged")
            fun(callback: (messages: ArrayList<Chat?>, recipient: String?) -> ArrayList<Chat?>) {

                this.messageAdapter.messageList = callback(messageList, phoneNumber)
                runOnUiThread {
                    messageAdapter.notifyDataSetChanged()
                }
            }
        messageRecyleView.layoutManager = LinearLayoutManager(this)
        messageRecyleView.adapter = messageAdapter
        this.progressBar.isVisible=true

        scope.launch {


            withContext(Dispatchers.IO) {
                Log.d("getMessageByTableName",recipientDetails.toString())

                try{
                    val recipientDetails = DBUser.getDataByPhoneNumber(phoneNumber!!)
                    Log.d("getMessageByTableName",recipientDetails.toString())

                    this@ChatActivity.setRecipientDetails(recipientDetails)
                    Log.d("getMessageByTableName","set $recipientDetails")
                    Log.d("getMessageByTableName","fetchMessages")

                    val messages = (async { DBMessage.getMessageByTableName(
                        this@ChatActivity, recipientDetails.phoneNumber
                    ) }).await()
                    Log.d("getMessageByTableName","got Messages $messages")

                    val hasUnread = messages.find { value -> value?.isSeen == false }
                    if (hasUnread != null) {
                        DBMessage.sendSeenEvent(recipientDetails.phoneNumber)


                    }
                    Log.d("messages",messages.size.toString())
                    setMessages(ArrayList(messages))

                }catch (err:Error){
                    Log.d("Error",err.toString())
                }finally {
                 runOnUiThread {
                     this@ChatActivity.progressBar.isVisible=false
                 }

                }
            }
        }
        scope.launch {
            withContext(Dispatchers.IO){
                this@ChatActivity.startListener()

            }
        }
        messageBox.setOnClickListener{

          scope.launch {
           if(recipientDetails!=null){
               Log.d("Create Event",Message.EventType.TYPE_UPDATE.toString())
               val event= Message.Event.newBuilder().setReciever(recipientDetails!!.phoneNumber).setType(Message.EventType.TYPE_UPDATE).build()
               client.send(event)
           }
          }



        }

        sendBtn.setOnClickListener {
            val messageText = messageBox.text.toString()
            scope.launch {
                withContext(Dispatchers.IO) {
                    recipientDetails?.let { it1 -> Log.d("recipientDetails", it1.phoneNumber) }
                    recipientDetails?.pubKey.let { it1 ->
                        if (it1 != null) {
                            Log.d("key", it1)
                        }
                    }
                    var encryptKey: SecretKey? = recipientDetails?.key?.let { it1 ->
                        RSA.convertAESstringToKey(
                            it1
                        )
                    }



                    Log.d("encryptKey 1", encryptKey.toString())
                    // if publicKey is not present then perform handshake
                    if (encryptKey == null) {
                        Log.d("encryptKey 2", encryptKey.toString())

                        encryptKey=(async { initiateHandShake(recipientDetails!!) }).await()

                    }
                    Log.d("encryptKey 3", encryptKey.toString())

                    val id = RSA.encryptMessage(
                        DbConnect.getDatabase().chatsDao()
                            .loadChatsByNumber(phoneNumber!!).size.toString(),
                        convertStringToKeyFactory(recipientDetails!!.pubKey!!, 0)
                    )
                    val encryptedMessage = RSA.encryptMessage(
                        messageText, convertStringToKeyFactory(recipientDetails!!.pubKey!!,2)
                    )
                    val chat = setMessageClass(id, messageText) as Chat
                    Log.d("testtt",chat.message)


                    sendMessage(this@ChatActivity,chat, recipientDetails!!,encryptKey)

                }
            }
            messageBox.setText("")

        }

    }


    override fun onDestroy() {
        super.onDestroy()
        listeners = presenfunc
    }

    suspend fun startListener() {
        scope.run {
            while (true) {
                while (getDB_BUFFER().size > 0) {
                    getDB_BUFFER().forEach { value -> addMessage(value) }
                }
                delay(1000)

                eraseDB_BUFFER()
            }

        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setMessages(messages: ArrayList<Chat?>) {
        this.messageAdapter.messageList = messages
        this.messageList = messages
        runOnUiThread {
            messageAdapter.notifyDataSetChanged()
            messageRecyleView.scrollToPosition(this.messageList.size - 1)

        }
    }

    private fun setRecipientDetails(details: Contact) {
        this.recipientDetails = details
    }

    private fun addMessage(message: Chat?) {
        Log.d("this.messageList", this.messageList.toString())
//        if (this.messageList.size > 0) {
//            this.messageList.add(this.messageList.size, message)
//
//        } else {
            this.messageList.add(message)
//        }
        Log.d("message",this.messageList.toString())
        this.messageAdapter.messageList = this.messageList
        runOnUiThread {
            messageAdapter.notifyDataSetChanged()
            messageRecyleView.scrollToPosition(this.messageList.size - 1)

        }
    }

    private fun setMessageClass(id: String, message: String): Chat? {

        Log.d("setMessageClass",message)
        val chat = CurrentUser.getCurrentUserPhoneNumber()?.let {
            recipientDetails?.let { it1 ->
                Chat(
                    0,
                    it,
                    it1.phoneNumber,
                    message,
                    id,
                    "text",
                    isDelivered = false,
                    isSeen = false,
                    Message.MessageType.INSERT.toString(),
                    OffsetDateTime.now(),
                    OffsetDateTime.now()
                )
            }
        }
        if (chat != null) {
            this.addMessage(chat)
        }
        return chat

    }



}