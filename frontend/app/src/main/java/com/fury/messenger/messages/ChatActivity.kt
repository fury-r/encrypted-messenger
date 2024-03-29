package com.fury.messenger.messages

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Button
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
import com.fury.messenger.crypto.Crypto
import com.fury.messenger.crypto.Crypto.convertAESstringToKey
import com.fury.messenger.crypto.Crypto.encryptAudio
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
import com.fury.messenger.helper.audio.AudioPlayer
import com.fury.messenger.helper.audio.AudioRecorder
import com.fury.messenger.helper.user.AppDatabase
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.helper.user.CurrentUser.convertStringToKeyFactory
import com.fury.messenger.manageBuilder.createAuthenticationStub
import com.google.firebase.auth.FirebaseAuth
import com.services.Message
import com.services.ServicesGrpc.ServicesBlockingStub
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDateTime
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
    private  lateinit var db:AppDatabase
    private  lateinit  var micButton:Button
    private  lateinit var directory: File
    private  var audioMessageFileName:String?=null
    private  val recorder by lazy {
        AudioRecorder(applicationContext)
    }
    private  val player by  lazy {
        AudioPlayer(applicationContext)
    }

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
        val key = intent.getStringExtra("key")

        directory=File(applicationContext.filesDir,"audio")
        if(!directory.exists()){
            directory.mkdir()
        }

         client = createAuthenticationStub(CurrentUser.getToken())

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        db=DbConnect.getDatabase(this)


        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid
        messageRecyleView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messagebox)
        title = findViewById(R.id.username)
        sendBtn = findViewById(R.id.sendBtn)
        progressBar=findViewById(R.id.progressBar)
        micButton=findViewById(R.id.micButton)
        messageList = ArrayList()
        title.text = Contact!!
        messageRecyleView = findViewById(R.id.chatRecyclerView)
        messageAdapter = MessageAdapter(this, messageList, receiverUid, convertAESstringToKey(key))

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

                try{
                    val recipientDetails= DBUser.getDataByPhoneNumber(phoneNumber!!)
                    this@ChatActivity.setRecipientDetails(recipientDetails)


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
                sendProcessMessage(messageText,recipientDetails!!,Message.ContentType.Text)
            }
            messageBox.setText("")

        }
        this.micButton.setOnClickListener{
            recorder.start(File(directory, (recipientDetails?.phoneNumber ?: "") +" "+LocalDateTime.now()))

        }
            this.micButton.setOnTouchListener(
            object: View.OnTouchListener{

                override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                    audioMessageFileName=(recipientDetails?.phoneNumber ?: "") +" "+LocalDateTime.now()

                    if(p1!=null){
                        when(p1.action){
                            MotionEvent.ACTION_DOWN->{
                                recorder.start(File(directory, (recipientDetails?.phoneNumber ?: "") +" "+LocalDateTime.now()))


                            }
                            MotionEvent.ACTION_UP->{
                                val key:SecretKey
                                if(recipientDetails?.key ==null){
                                     key= recipientDetails?.let { initiateHandShake(it) }!!
                                }
                                else{

                                    key= convertAESstringToKey(recipientDetails!!.key)!!
                                }
                                recorder.stop()
                                if(audioMessageFileName!=null){
                                    scope.launch {
                                        sendProcessMessage(encryptAudio(directory.path+"/"+audioMessageFileName,
                                            convertAESstringToKey(recipientDetails?.key)!!
                                        ),  recipientDetails!!,Message.ContentType.Audio)
                                     this@ChatActivity.audioMessageFileName=null
                                    }
                                }
                                p0?.performClick()
                            }
                        }
                    }

                    return  true

                }

            })

    }


    override fun onDestroy() {
        super.onDestroy()
        listeners = presenfunc
    }

    private suspend fun startListener() {
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.user_menu, menu)
        com.fury.messenger.helper.ui.Menu.onPrepareOptionsMenu(menu,recipientDetails!!,this, hideDelete = true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {


            when (item.itemId) {


                R.id.pin -> {

                    scope.launch {
                        val contact=db.contactDao().findByNumber(recipientDetails!!.phoneNumber)
                        if (contact.isPinned == true) {
                            contact.isPinned = false
                            db.contactDao().update(contact)
                            runOnUiThread {
                                item.title = "Pin"
                            }
                        } else {
                            contact.isPinned = true
                            db.contactDao().update(contact)
                            runOnUiThread {
                                item.title = "Un Pin"
                            }
                        }

                    }

                }

                R.id.block -> {

                    scope.launch {
                        DBMessage.blockUser(recipientDetails!!.phoneNumber)
                    }

                }
            }

        return super.onContextItemSelected(item)
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



    suspend fun sendProcessMessage(messageText:String, recipientDetails:Contact,messageType: Message.ContentType){
        withContext(Dispatchers.IO) {
            recipientDetails?.let { it1 -> Log.d("recipientDetails", it1.phoneNumber) }
            recipientDetails?.pubKey.let { it1 ->
                if (it1 != null) {
                    Log.d("key", it1)
                }
            }
            var encryptKey: SecretKey? = recipientDetails?.key?.let { it1 ->
                Crypto.convertAESstringToKey(
                    it1
                )
            }



            // if publicKey is not present then perform handshake
            if (encryptKey == null) {

                encryptKey=(async { initiateHandShake(recipientDetails) }).await()

            }

            val id = Crypto.encryptMessage(
                DbConnect.getDatabase().chatsDao()
                    .loadChatsByNumber(recipientDetails.phoneNumber).size.toString(),
                convertStringToKeyFactory(recipientDetails.pubKey!!, 0)
            )
            val encryptedMessage = Crypto.encryptMessage(
                messageText, convertStringToKeyFactory(recipientDetails.pubKey!!,2)
            )
            val chat = setMessageClass(id, messageText) as Chat
            Log.d("testtt",chat.message)


            sendMessage(this@ChatActivity,chat, recipientDetails,encryptKey,messageType)

        }
    }


}