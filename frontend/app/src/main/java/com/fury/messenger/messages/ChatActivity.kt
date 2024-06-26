package com.fury.messenger.messages

import ChatsByDate
import ParentAdapter
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
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
import com.fury.messenger.crypto.Crypto.convertAESKeyToString
import com.fury.messenger.crypto.Crypto.convertAESstringToKey
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
import com.fury.messenger.data.db.UserEvent
import com.fury.messenger.data.db.model.Chat
import com.fury.messenger.data.db.model.Contact
import com.fury.messenger.helper.audio.AudioRecorder
import com.fury.messenger.helper.ui.base64StringToImage
import com.fury.messenger.helper.user.AppDatabase
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.helper.user.CurrentUser.convertStringToKeyFactory
import com.fury.messenger.manageBuilder.createAuthenticationStub
import com.services.Message
import com.services.Message.ContentType
import com.services.Message.EventType
import com.services.ServicesGrpc.ServicesBlockingStub
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import javax.crypto.SecretKey
import kotlin.collections.set


class ChatActivity : AppCompatActivity() {
    private lateinit var messageRecyleView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendBtn: ImageView
    private lateinit var profilePicture: ImageView
    private var timer: Job? = null
    private var seconds: Int = 0

    private lateinit var title: TextView
    private lateinit var messageAdapter: ParentAdapter
    private lateinit var messageList: ArrayList<Chat>
    private var recipientDetails: Contact? = null
    private var scope = CoroutineScope(Dispatchers.Main)
    private var receiverRoom: String? = null
    private var senderRoom: String? = null
    private lateinit var status: TextView
    private lateinit var client: ServicesBlockingStub
    private lateinit var progressBar: ProgressBar
    private lateinit var db: AppDatabase
    private lateinit var micButton: Button
    private lateinit var directory: File
    private lateinit var search: SearchView
    private lateinit var phoneNumber: String
    private lateinit var key: String

    private var selected: String? = null

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
        val image = intent.getStringExtra("image") ?: ""
        client = createAuthenticationStub(CurrentUser.getToken())
        db = DbConnect.getDatabase(this)

        messageRecyleView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messagebox)
        title = findViewById(R.id.username)
        sendBtn = findViewById(R.id.sendBtn)
        profilePicture = findViewById(R.id.profilePicture)

        progressBar = findViewById(R.id.progressBar)
        micButton = findViewById(R.id.micButton)
        search = findViewById(R.id.searchView)
        status = findViewById(R.id.status)
        val contact = intent.getStringExtra("Contact")

        val receiverUid = intent.getStringExtra("uid")
//        val uri = intent.getStringExtra("uri")
        key = intent.getStringExtra("key") ?: ""

        directory = File(applicationContext.filesDir, "audio")
        if (!directory.exists()) {
            directory.mkdir()
        }


        if (image.isNotEmpty()) {
            profilePicture.setImageBitmap(base64StringToImage(image))
        }




        status.text = ""
        messageList = ArrayList()
        title.text = contact!!
        messageRecyleView = findViewById(R.id.chatRecyclerView)
        convertAESstringToKey(key)?.let { runAESTest(it) }
        messageAdapter = ParentAdapter(
            formatMessagesByDate(messageList),
            this,
            receiverUid,
            convertAESstringToKey(key), fun(selected: String?) {
                this.selected = selected
            }
        )

        listeners =
            @SuppressLint("NotifyDataSetChanged")
            fun(callback: (messages: ArrayList<UserEvent>, recipient: String?) -> ArrayList<UserEvent>) {
                val messageList = arrayListOf<UserEvent>()

                val message = callback(messageList, phoneNumber)
                runOnUiThread {
                    this.messageList.addAll(message.filter { it.eventType == EventType.MESSAGE } as ArrayList<Chat>)
                    this.messageAdapter.messageList = formatMessagesByDate(this.messageList)
                    messageAdapter.notifyDataSetChanged()

                    messageRecyleView.smoothScrollToPosition(messageAdapter.itemCount - 1)

                }
            }


        messageRecyleView.layoutManager = LinearLayoutManager(this)
        messageRecyleView.adapter = messageAdapter


        scope.launch {


            withContext(Dispatchers.IO) {
                runOnUiThread {
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
                    DbConnect.getDatabase(this@ChatActivity).chatsDao().markAsRead()
                    if (recipientDetails.key != null) {
                        key = recipientDetails.key!!
                    }

                    setMessages(messages.map {

                        if (it?.contentType == ContentType.Audio.name) {

                            val filePath = "${this@ChatActivity.filesDir}/audio/${it?.sender}-${
                                it.createdAt.toString().replace("+", "-").replace(":", "-")
                            }.mp3"
                            (async {
                                key.let { key ->
                                    Crypto.decryptAudio(
                                        it.message, convertAESstringToKey(key)!!, filePath
                                    )
                                }
                            }).await()
                            it.message = filePath

                        }
                        return@map it
                    } as ArrayList<Chat>)

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

        this.micButton.setOnTouchListener { p0, p1 ->
            if (p1 != null) {

                when (p1.action) {
                    MotionEvent.ACTION_DOWN -> {
                        audioMessageFileName = (recipientDetails?.phoneNumber
                            ?: "") + "-" + LocalDateTime.now() + ".mp3"
                        startTimer()
                        recorder.start(File(directory, audioMessageFileName))

                        this@ChatActivity.micButton.setBackgroundResource(R.drawable.mic_off)

                    }

                    MotionEvent.ACTION_UP -> {
                        recorder.stop()
                        stopTimer()
                        messageBox.hint = "Type a Message"



                        if (audioMessageFileName != null) {
                            scope.launch {
                                sendProcessMessage(
                                    directory.path + "/" + audioMessageFileName,
                                    recipientDetails!!,
                                    ContentType.Audio
                                )
                                this@ChatActivity.audioMessageFileName = null
                            }
                        }
                        this@ChatActivity.micButton.setBackgroundResource(R.drawable.mic_on)

                        p0?.performClick()
                    }
                }
            }

            true
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        listeners = presenfunc

        for (child in directory.listFiles()!!) child.delete()
        stopTimer()
    }

    private suspend fun startListener() {
        scope.run {
            while (true) {
                while (getDB_BUFFER().size > 0) {
                    val buffer = getDB_BUFFER()
                    buffer.filter { it.eventType === EventType.MESSAGE }
                        .forEach { value -> addMessage(value.message!!) }

                    val findTypeEvent = buffer.findLast { it.eventType === EventType.TYPE_UPDATE }
                    if (findTypeEvent != null) {
                        val diffInSeconds = Duration.between(
                            findTypeEvent.message?.createdAt,
                            OffsetDateTime.now()
                        ).seconds
                        if (diffInSeconds < 10) {
                            this@ChatActivity.status.text = "Typing..."
                            this@ChatActivity.status.setTypeface(null, Typeface.BOLD);
                            recipientDetails!!.typeTime = findTypeEvent.message?.createdAt
                            val countDownTimer =
                                object : CountDownTimer(diffInSeconds * 1000, 1000) {
                                    override fun onTick(p0: Long) {

                                    }

                                    override fun onFinish() {
                                        setLastSeen()
                                    }


                                }
                            countDownTimer.start()
                        }
                    }
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
        if (menu != null && recipientDetails != null) {
            com.fury.messenger.helper.ui.Menu.onPrepareOptionsMenu(
                menu,
                recipientDetails!!,
                this,
                hideDelete = true
            )
        }

        return super.onPrepareOptionsMenu(menu)
    }


    fun setLastSeen() {
        if (recipientDetails?.typeTime != null) {
            val diffBetWeenTime = Duration.between(
                this@ChatActivity.recipientDetails!!.typeTime,
                OffsetDateTime.now()
            )

            if (diffBetWeenTime.toDays()
                    .toInt() == 0 && this@ChatActivity.recipientDetails != null
            ) {
                status.text = this@ChatActivity.recipientDetails!!.typeTime?.let {
                    String.format(
                        "Last seen at %d",
                        it.format(DateTimeFormatter.ofPattern("hh:mm"))
                    )
                }

            } else {
                status.text = this@ChatActivity.recipientDetails!!.typeTime?.let {
                    String.format(
                        "Last seen on %d",
                        it.format(DateTimeFormatter.ofPattern("dd/MM/yyyy:hh:mm:ss"))
                    )
                }

            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


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
        val selectedItem = this.messageList.find { it.messageId == this.selected }

        if (selectedItem != null) {

            when (item.itemId) {
                R.id.deleteItem -> {
                    scope.launch {
                        this@ChatActivity.db.chatsDao()
                            .delete(selectedItem)

                    }
                    this.messageList.remove(selectedItem)
                    this.messageAdapter.messageList = formatMessagesByDate(this.messageList)
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
            if (this.messageList.size > 0) {
                messageRecyleView.smoothScrollToPosition(messageAdapter.itemCount - 1)
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setRecipientDetails(details: Contact) {
        this.recipientDetails = details
        runOnUiThread {
            this.status.text =
                details.typeTime?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy:hh:mm:a")) ?: ""

            if (details.typeTime != null) {
                if (Period.between(LocalDate.now(), details.typeTime!!.toLocalDate()).days == -1) {
                    this.status.text = "Last seen yesterday ${
                        details.typeTime!!.format(
                            DateTimeFormatter.ofPattern("hh:mm:a")
                        )
                    }"

                } else if (details.typeTime!!.toLocalDate().isEqual(LocalDate.now())) {
                    this.status.text =
                        "Last seen today ${details.typeTime!!.format(DateTimeFormatter.ofPattern("hh:mm:a"))}"

                }
            }
            setLastSeen()

        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addMessage(message: Chat) {

        this.messageList.add(message)
        this.messageAdapter.messageList = formatMessagesByDate(messageList)
        runOnUiThread {
            messageAdapter.notifyDataSetChanged()
            messageRecyleView.smoothScrollToPosition(this.messageAdapter.itemCount - 1)

        }
    }

    private fun setMessageClass(id: String, message: String, contentType: ContentType): Chat? {

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
                    this@ChatActivity.recipientDetails!!.key = convertAESKeyToString(encryptKey)

                    this@ChatActivity.messageAdapter.notifyDataSetChanged()
                }

            }

            val id = Crypto.encryptMessage(
                DbConnect.getDatabase().chatsDao()
                    .loadChatsByNumber(recipientDetails.phoneNumber).size.toString(),
                convertStringToKeyFactory(recipientDetails.pubKey!!, 0)
            )
            val encryptedMessage: String = if (messageType == ContentType.Audio) {
                Crypto.encryptAudio(
                    messageText, encryptKey
                )
            } else if (messageType == ContentType.Text) {
                Crypto.encryptAESMessage(
                    messageText, encryptKey
                )
            } else {
                ""
            }


//            Log.d("encrypt", encryptedMessage)
//            Log.d("decrypt", decryptAESMessage(encryptedMessage, encryptKey))
            val chat = setMessageClass(id, encryptedMessage, messageType) as Chat



            sendMessage(this@ChatActivity, chat, recipientDetails, encryptKey, messageType)
            if (ContentType.Audio == messageType) {
                chat.message = messageText
            }
            this@ChatActivity.addMessage(chat)
        }
    }

    private fun formatMessagesByDate(messages: ArrayList<Chat>): ArrayList<ChatsByDate?> {
        val dateByMessage = HashMap<LocalDate, ArrayList<Chat>>()

        messages.forEach {
            val date = it.createdAt!!.toLocalDate()
            if (!dateByMessage.containsKey(date)) {
                dateByMessage[date] = arrayListOf()
            }

            dateByMessage[date]!!.add(it)

        }
        val chats = (dateByMessage.entries.sortedBy { it.key }.map {
            ChatsByDate(
                it.key,
                arrayListOf(*it.value.sortedBy { value -> value.createdAt }.toTypedArray())
            )
        }.sortedBy { it.date }).toList().map {
            it
        }
        if (chats.isEmpty()) {
            return arrayListOf()
        }
        return chats as ArrayList<ChatsByDate?>
    }

    private fun startTimer() {
        timer = scope.launch {
            while (true) {
                delay(1000)
                this@ChatActivity.seconds += 1
                val minutes = seconds / 60
                val seconds = (seconds % 60)
                val timeLeftFormatted = String.format("%02d:%02d", minutes, seconds)
                runOnUiThread {

                    messageBox.hint = "Voice Message  $timeLeftFormatted"
                }
            }
        }
    }


    private fun stopTimer() {
        timer?.cancel()
        seconds = 0
    }


}