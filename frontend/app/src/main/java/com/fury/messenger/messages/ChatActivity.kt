package com.fury.messenger.messages

import ChatsByDate
import ParentAdapter
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
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fury.messenger.R
import com.fury.messenger.crypto.Crypto
import com.fury.messenger.crypto.Crypto.convertAESstringToKey
import com.fury.messenger.crypto.Crypto.decryptAESMessage
import com.fury.messenger.crypto.Crypto.runAESTest
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
import com.fury.messenger.helper.audio.AudioRecorder
import com.fury.messenger.helper.user.AppDatabase
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.helper.user.CurrentUser.convertStringToKeyFactory
import com.fury.messenger.manageBuilder.createAuthenticationStub
import com.google.firebase.auth.FirebaseAuth
import com.services.Message
import com.services.Message.ContentType
import com.services.ServicesGrpc.ServicesBlockingStub
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import javax.crypto.SecretKey

class ChatActivity : AppCompatActivity() {
    private lateinit var messageRecyleView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendBtn: ImageView
    private lateinit var title: TextView
    private lateinit var messageAdapter: ParentAdapter
    private lateinit var messageList: ArrayList<Chat>
    private var recipientDetails: Contact? = null
    private var scope = CoroutineScope(Dispatchers.Main)
    private var receiverRoom: String? = null
    private var senderRoom: String? = null
    private lateinit var status:TextView
    private lateinit var client: ServicesBlockingStub
    private lateinit var progressBar: ProgressBar
    private lateinit var db: AppDatabase
    private lateinit var micButton: Button
    private lateinit var directory: File
    private lateinit var search: SearchView
    private lateinit var phoneNumber: String
    private  var selected:String?=null

    private var audioMessageFileName: String? = null
    private val recorder by lazy {
        AudioRecorder(applicationContext)
    }
    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#322F2F")))
        this.supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.message_titlebar)
        phoneNumber = intent.getStringExtra("phoneNumber") ?: ""
        val contact = intent.getStringExtra("Contact")

        val receiverUid = intent.getStringExtra("uid")
//        val uri = intent.getStringExtra("uri")
        val key = intent.getStringExtra("key")

        directory = File(applicationContext.filesDir, "audio")
        if (!directory.exists()) {
            directory.mkdir()
        }

        client = createAuthenticationStub(CurrentUser.getToken())

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        db = DbConnect.getDatabase(this)


        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid
        messageRecyleView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messagebox)
        title = findViewById(R.id.username)
        sendBtn = findViewById(R.id.sendBtn)
        progressBar = findViewById(R.id.progressBar)
        micButton = findViewById(R.id.micButton)
        search = findViewById(R.id.searchView)
        status=findViewById(R.id.status)
        status.text=""
        messageList = ArrayList()
        title.text = contact!!
        messageRecyleView = findViewById(R.id.chatRecyclerView)
        if (key != null) {
            Log.d("key", key)
            convertAESstringToKey(key)?.let { runAESTest(it) }
        }
        messageAdapter = ParentAdapter(
            formatMessagesByDate(messageList),
            this,
            receiverUid,
            convertAESstringToKey(key),fun(selected:String?){
                this.selected=selected
            }
        )

        listeners =
            @SuppressLint("NotifyDataSetChanged")
            fun(callback: (messages: ArrayList<Chat>, recipient: String?) -> ArrayList<Chat>) {

                val message = callback(messageList, phoneNumber)
                runOnUiThread {
                    this.messageList.addAll(message)
                    this.messageAdapter.messageList=formatMessagesByDate(this.messageList)
                    messageAdapter.notifyDataSetChanged()

                    messageRecyleView.smoothScrollToPosition(messageAdapter.itemCount -1)

                }
            }


        messageRecyleView.layoutManager = LinearLayoutManager(this)
        messageRecyleView.adapter = messageAdapter


        scope.launch {


            withContext(Dispatchers.IO) {
                runOnUiThread{
                    this@ChatActivity.progressBar.isVisible = true
                }

                try {
                    val recipientDetails = DBUser.getDataByPhoneNumber(phoneNumber)
                    this@ChatActivity.setRecipientDetails(recipientDetails)


                    val messages = (async {
                        DBMessage.getMessageByTableName(
                            this@ChatActivity, recipientDetails.phoneNumber
                        )
                    }).await()
                    Log.d("getMessageByTableName", "got Messages $messages")

                    val hasUnread = messages.find { value -> value?.isSeen == false }
                    if (hasUnread != null) {
                        DBMessage.sendSeenEvent(recipientDetails.phoneNumber)


                    }

                    setMessages(messages as ArrayList<Chat>)

                } catch (err: Error) {
                    Log.d("Error", err.toString())
                } finally {
                    runOnUiThread {
                        this@ChatActivity.progressBar.isVisible = false
                    }

                }
            }
        }

        scope.launch {
            withContext(Dispatchers.IO) {
                this@ChatActivity.startListener()

            }
        }
        messageBox.setOnClickListener {

            scope.launch {
                if (recipientDetails != null) {
                    Log.d("Create Event", Message.EventType.TYPE_UPDATE.toString())
                    val event =
                        Message.Event.newBuilder().setReciever(recipientDetails!!.phoneNumber)
                            .setType(Message.EventType.TYPE_UPDATE).build()
                    client.send(event)
                }
            }


        }
        scope.launch {
            CurrentUser.subscribeToMessageQueue(this@ChatActivity)

        }
        sendBtn.setOnClickListener {
            val messageText = messageBox.text.toString()
            scope.launch {
                sendProcessMessage(messageText, recipientDetails!!, ContentType.Text)
            }
            messageBox.setText("")

        }
        this.micButton.setOnClickListener {
            recorder.start(
                File(
                    directory,
                    (recipientDetails?.phoneNumber ?: "") + " " + LocalDateTime.now()
                )
            )

        }
        this.micButton.setBackgroundResource(R.drawable.mic_on)
        this.micButton.setOnTouchListener(
            object : View.OnTouchListener {

                override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {

                    if (p1 != null) {
                        when (p1.action) {
                            MotionEvent.ACTION_DOWN -> {
                                audioMessageFileName = (recipientDetails?.phoneNumber
                                    ?: "") + "-" + LocalDateTime.now() + ".mp3"
                                messageBox.hint="Voice Message"
                                recorder.start(File(directory, audioMessageFileName))

                                this@ChatActivity.micButton.setBackgroundResource(R.drawable.mic_off)

                            }

                            MotionEvent.ACTION_UP -> {
                                messageBox.hint="Type a Message"


                                recorder.stop()
                                if (audioMessageFileName != null) {
                                    scope.launch {
                                        sendProcessMessage(
                                            directory.path + "/" + audioMessageFileName, recipientDetails!!, ContentType.Audio
                                        )
                                        this@ChatActivity.audioMessageFileName = null
                                    }
                                }
                                this@ChatActivity.micButton.setBackgroundResource(R.drawable.mic_on)

                                p0?.performClick()
                            }
                        }
                    }

                    return true

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
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (menu != null &&recipientDetails!=null) {
            com.fury.messenger.helper.ui.Menu.onPrepareOptionsMenu(
                menu,
                recipientDetails!!,
                this,
                hideDelete = true
            )
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("ddd", "DDDDd")


        when (item.itemId) {


            R.id.pin -> {

                scope.launch {
                    val contact = db.contactDao().findByNumber(recipientDetails!!.phoneNumber)
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
                    DBMessage.blockUser(phoneNumber)
                }

            }

            R.id.searchItem -> {
                search.visibility = View.VISIBLE

            }
        }

        return super.onOptionsItemSelected(item)
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val selectedItem = this.messageList.find { it.messageId==this.selected }

        if (selectedItem != null) {

            when (item.itemId) {
                R.id.deleteItem -> {
                    scope.launch {
                        this@ChatActivity.db.chatsDao()
                            .delete(selectedItem)

                    }
                    this.messageList.remove(selectedItem)
                    this.messageAdapter.messageList=formatMessagesByDate(this.messageList)
                    this.messageAdapter.notifyDataSetChanged()
                    Toast.makeText(
                        this,
                        "Deleted Message",
                        Toast.LENGTH_SHORT
                    ).show()
                    return true

                }


            }
        }
        return super.onContextItemSelected(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setMessages(messages: ArrayList<Chat>) {


        this.messageList = messages
        this.messageAdapter.messageList = formatMessagesByDate(messages)
        runOnUiThread {

            messageAdapter.notifyDataSetChanged()
        if(this.messageList.size>0){
            messageRecyleView.smoothScrollToPosition(messageAdapter.itemCount -1)
        }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setRecipientDetails(details: Contact) {
        this.recipientDetails = details
        runOnUiThread{
            this.status.text= details.typeTime?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy:hh:mm:a")) ?: ""

            if(details.typeTime!=null){
                if(Period.between(LocalDate.now(), details.typeTime!!.toLocalDate()).days==-1){
                    this.status.text="Last seen yesterday ${details.typeTime!!.format(DateTimeFormatter.ofPattern("hh:mm:a"))}"

                }
                else  if(details.typeTime!!.toLocalDate().isEqual(LocalDate.now())){
                    this.status.text="Last seen today ${details.typeTime!!.format(DateTimeFormatter.ofPattern("hh:mm:a"))}"

                }
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addMessage(message: Chat) {
        Log.d("this.messageList", this.messageList.toString())

        this.messageList.add(message)
        this.messageAdapter.messageList = formatMessagesByDate(messageList)
        runOnUiThread {
            messageAdapter.notifyDataSetChanged()
            messageRecyleView.smoothScrollToPosition(this.messageAdapter.itemCount - 1)

        }
    }

    private fun setMessageClass(id: String, message: String,contentType:ContentType): Chat? {

        Log.d("setMessageClass", message)
        val chat = CurrentUser.getCurrentUserPhoneNumber()?.let {
            recipientDetails?.let { it1 ->
                Chat(
                    it,
                    it1.phoneNumber,
                    message,
                    id,
                    contentType.name,
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


    @SuppressLint("NotifyDataSetChanged")
    suspend fun sendProcessMessage(
        messageText: String,
        recipientDetails: Contact,
        messageType: ContentType
    ) {
        withContext(Dispatchers.IO) {
            recipientDetails.let { it1 -> Log.d("recipientDetails", it1.phoneNumber) }
            recipientDetails.pubKey.let { it1 ->
                if (it1 != null) {
                    Log.d("key", it1)
                }
            }
            var encryptKey: SecretKey? = recipientDetails.key?.let { it1 ->
                convertAESstringToKey(
                    it1
                )
            }


            // if symmetric is not present then perform handshake
            if (encryptKey == null) {
                Log.d("HandShake", "Initiate Handshake")
                val handShakeEncryptKey = async { initiateHandShake(recipientDetails, messageText) }
                encryptKey = handShakeEncryptKey.await()

                runOnUiThread {
                    messageAdapter.recipientKey = encryptKey

                    this@ChatActivity.messageAdapter.notifyDataSetChanged()
                }

            }

            val id = Crypto.encryptMessage(
                DbConnect.getDatabase().chatsDao()
                    .loadChatsByNumber(recipientDetails.phoneNumber).size.toString(),
                convertStringToKeyFactory(recipientDetails.pubKey!!, 0)
            )
            val encryptedMessage:String = if(messageType==ContentType.Audio){
                Crypto.encryptAudio(
                    messageText, encryptKey
                )
            } else if(messageType==ContentType.Text){
                Crypto.encryptAESMessage(
                    messageText, encryptKey
                )
            } else{
                ""
            }


            Log.d("encrypt", encryptedMessage)
            Log.d("decrypt", decryptAESMessage(encryptedMessage, encryptKey))
            val chat = setMessageClass(id, encryptedMessage, messageType) as Chat


            sendMessage(this@ChatActivity, chat, recipientDetails, encryptKey, messageType)

        }
    }

    private fun formatMessagesByDate(messages: ArrayList<Chat>): ArrayList<ChatsByDate> {
        val dateByMessage = HashMap<LocalDate, ArrayList<Chat>>()

        messages.forEach {
            val date = it.createdAt!!.toLocalDate()
            if (!dateByMessage.containsKey(date)) {
                dateByMessage[date] = arrayListOf()
            }

            dateByMessage[date]!!.add(it)

        }
        return dateByMessage.entries.map {
            ChatsByDate(it.key, it.value)
        } as ArrayList<ChatsByDate>
    }

}