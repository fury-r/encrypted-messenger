package com.fury.messenger.messages

import ChatsByDate
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.media.MediaMetadataRetriever
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.fury.messenger.R
import com.fury.messenger.crypto.Crypto
import com.fury.messenger.data.db.model.Chat
import com.fury.messenger.helper.audio.AudioPlayer
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.utils.formatMilliSeconds
import com.google.android.material.slider.Slider
import com.services.Message.ContentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.time.format.DateTimeFormatter
import java.util.Timer
import java.util.TimerTask
import javax.crypto.SecretKey

class MessageAdapter(
    val context: Context,
    var messageList: ChatsByDate,
    uid: String?,
    var recipientKey: SecretKey?,
    val setSelect: (String?) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val player by lazy {
        AudioPlayer(context)
    }
    private val hostActivity = context as? Activity
    private val uid2 = uid

    private fun runOnUiThread(action: () -> Unit) {
        hostActivity?.runOnUiThread(action) ?: action()
    }

    inner class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnCreateContextMenuListener, View.OnClickListener {
        val sentMessage = itemView.findViewById<TextView>(R.id.sentMessage)
        val status = itemView.findViewById<TextView>(R.id.status)
        val time = itemView.findViewById<TextView>(R.id.time)
        override fun onCreateContextMenu(
            menu: ContextMenu,
            v: View,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            val inflater = MenuInflater(v.context)
            inflater.inflate(R.menu.chat_menu, menu)
        }

        override fun onClick(v: View?) {
            v?.let {
                setSelect(sentMessage.tag as String)
                itemView.showContextMenu()
            }
        }
    }

    inner class SentAudioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnCreateContextMenuListener, View.OnClickListener {
        val playButton = itemView.findViewById<Button>(R.id.play)
        val downloadButton = itemView.findViewById<Button>(R.id.downloadButton)
        val audioTitle = itemView.findViewById<TextView>(R.id.audioTitle)
        val audioHint = itemView.findViewById<TextView>(R.id.audioHint)
        val status = itemView.findViewById<TextView>(R.id.status)
        val time = itemView.findViewById<TextView>(R.id.time)
        val slider = itemView.findViewById<Slider>(R.id.playSlider)
        val duration = itemView.findViewById<TextView>(R.id.duration)

        override fun onCreateContextMenu(
            menu: ContextMenu,
            v: View,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            val inflater = MenuInflater(v.context)
            inflater.inflate(R.menu.chat_menu, menu)
        }

        override fun onClick(v: View?) {
            v?.let {
                setSelect(playButton.tag as String)
                itemView.showContextMenu()
            }
        }
    }

    inner class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnCreateContextMenuListener, View.OnClickListener {
        val receiveMessage = itemView.findViewById<TextView>(R.id.receiveMessage)
        val time = itemView.findViewById<TextView>(R.id.time)
        override fun onCreateContextMenu(
            menu: ContextMenu,
            v: View,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            val inflater = MenuInflater(v.context)
            inflater.inflate(R.menu.chat_menu, menu)
        }

        override fun onClick(v: View?) {
            v?.let {
                setSelect(receiveMessage.tag as String)
                itemView.showContextMenu()
            }
        }
    }

    inner class ReceiveAudioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnCreateContextMenuListener, View.OnClickListener {
        val playButton = itemView.findViewById<Button>(R.id.play)
        val downloadButton = itemView.findViewById<Button>(R.id.downloadButton)
        val audioTitle = itemView.findViewById<TextView>(R.id.audioTitle)
        val audioHint = itemView.findViewById<TextView>(R.id.audioHint)
        val time = itemView.findViewById<TextView>(R.id.time)
        val duration = itemView.findViewById<TextView>(R.id.duration)
        val slider = itemView.findViewById<Slider>(R.id.playSlider)

        override fun onCreateContextMenu(
            menu: ContextMenu,
            v: View,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            val inflater = MenuInflater(v.context)
            inflater.inflate(R.menu.chat_menu, menu)
        }

        override fun onClick(v: View?) {
            v?.let {
                setSelect(playButton.tag as String)
                itemView.showContextMenu()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList.data[position]
        return if (CurrentUser.getCurrentUserPhoneNumber() == currentMessage.sender) {
            if (currentMessage.contentType == ContentType.Audio.toString()) 2 else 0
        } else {
            if (currentMessage.contentType == ContentType.Audio.toString()) 3 else 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
            ReceiveViewHolder(view)
        } else if (viewType == 0) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            SentViewHolder(view)
        } else if (viewType == 2) {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.sent_audio, parent, false)
            SentAudioViewHolder(view)
        } else {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.receive_audio, parent, false)
            ReceiveAudioViewHolder(view)
        }
    }

    private fun audioDirectory(): File {
        val audioFolder = File(context.filesDir, "audio")
        if (!audioFolder.exists()) {
            audioFolder.mkdirs()
        }
        return audioFolder
    }

    private fun audioCacheFile(currentMessage: Chat): File {
        val sanitizedMessageId = currentMessage.messageId.replace(Regex("[^A-Za-z0-9._-]"), "_")
        return File(audioDirectory(), "$sanitizedMessageId.mp3")
    }

    private fun resolveLocalAudio(currentMessage: Chat): File? {
        val directFile = File(currentMessage.message)
        if (directFile.exists()) {
            return directFile
        }
        val cachedFile = audioCacheFile(currentMessage)
        return if (cachedFile.exists()) {
            currentMessage.message = cachedFile.absolutePath
            cachedFile
        } else {
            null
        }
    }

    private fun updateDuration(view: View, timer: Timer) {
        val durationTextView = view.findViewById<TextView>(R.id.duration)
        val slider = view.findViewById<Slider>(R.id.playSlider)
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (player.isPlaying()) {
                    val position = player.getPosition()
                    if (durationTextView.tag != null) {
                        val duration = ((durationTextView.tag as? String) ?: "0").toInt() - position
                        view.context.runCatching {
                            durationTextView.text = formatMilliSeconds(duration.toLong())
                            slider.value = position.toFloat()
                        }
                    }
                }
            }
        }, 0, 1000)
    }

    private fun populateDuration(view: View, currentMessage: Chat) {
        val localFile = resolveLocalAudio(currentMessage) ?: return
        val playSlider = view.findViewById<Slider>(R.id.playSlider)
        val durationTextView = view.findViewById<TextView>(R.id.duration)
        scope.launch {
            val mmr = MediaMetadataRetriever()
            context.runCatching {
                mmr.setDataSource(localFile.absolutePath)
                val milliseconds =
                    mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                mmr.release()
                if (milliseconds != null) {
                    runOnUiThread {
                        durationTextView.text = formatMilliSeconds(milliseconds.toLong())
                        durationTextView.tag = milliseconds
                        playSlider.valueTo = milliseconds.toFloat()
                    }
                }
            }
        }
    }

    private fun setAudioMessageState(
        currentMessage: Chat,
        playButton: Button,
        downloadButton: Button,
        slider: Slider,
        duration: TextView,
        audioTitle: TextView,
        audioHint: TextView
    ) {
        val hasLocalAudio = resolveLocalAudio(currentMessage) != null
        playButton.visibility = if (hasLocalAudio) View.VISIBLE else View.GONE
        slider.visibility = if (hasLocalAudio) View.VISIBLE else View.GONE
        downloadButton.visibility = if (hasLocalAudio) View.GONE else View.VISIBLE
        playButton.setBackgroundResource(R.drawable.play_circle)
        if (hasLocalAudio) {
            audioTitle.text = context.getString(R.string.voice_note_ready_title)
            audioHint.text = context.getString(R.string.voice_note_ready_hint)
        } else {
            audioTitle.text = context.getString(R.string.encrypted_voice_note)
            audioHint.text = context.getString(R.string.download_voice_message_hint)
            duration.tag = null
            duration.text = context.getString(R.string.voice_note_not_downloaded)
            slider.value = 0f
        }
    }

    private fun downloadAudioMessage(
        containerView: View,
        currentMessage: Chat,
        playButton: Button,
        downloadButton: Button,
        slider: Slider,
        duration: TextView,
        audioTitle: TextView,
        audioHint: TextView
    ) {
        val decryptKey = recipientKey
        if (decryptKey == null) {
            Toast.makeText(context, R.string.voice_message_download_failed, Toast.LENGTH_SHORT).show()
            return
        }

        downloadButton.isEnabled = false
        downloadButton.text = context.getString(R.string.downloading_voice_message)
        scope.launch {
            val audioFile = runCatching {
                val cachedFile = audioCacheFile(currentMessage)
                Crypto.decryptAudio(currentMessage.message, decryptKey, cachedFile.absolutePath)
            }.getOrNull()

            runOnUiThread {
                downloadButton.isEnabled = true
                downloadButton.text = context.getString(R.string.download_voice_message)
                if (audioFile != null) {
                    currentMessage.message = audioFile.absolutePath
                    setAudioMessageState(
                        currentMessage,
                        playButton,
                        downloadButton,
                        slider,
                        duration,
                        audioTitle,
                        audioHint
                    )
                    populateDuration(containerView, currentMessage)
                } else {
                    Toast.makeText(
                        context,
                        R.string.voice_message_download_failed,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun formatProgressLabel(position: Float, duration: TextView): String {
        val formattedString = formatMilliSeconds(position.toLong())
        player.seekTo(position.toInt())
        duration.text = formattedString
        return formattedString
    }

    private fun audioPlayback(view: View, currentMessage: Chat) {
        val slider = view.findViewById<Slider>(R.id.playSlider)
        val button = view.findViewById<Button>(R.id.play)
        val localFile = resolveLocalAudio(currentMessage)
        if (localFile == null) {
            Toast.makeText(context, R.string.voice_message_not_ready, Toast.LENGTH_SHORT).show()
            return
        }

        scope.launch {
            if (!player.isPlaying()) {
                if (!player.init) {
                    val timer = Timer()
                    player.playFile(localFile, 0, {
                        context.runCatching {
                            updateDuration(view, timer)
                        }
                    }, {
                        runOnUiThread {
                            button.setBackgroundResource(R.drawable.play_circle)
                            slider.value = 0f
                            timer.cancel()
                        }
                    })
                } else {
                    player.resume()
                }
                runOnUiThread {
                    button.setBackgroundResource(R.drawable.baseline_pause_circle_24)
                }
            } else {
                player.pause()
                runOnUiThread {
                    button.setBackgroundResource(R.drawable.play_circle)
                }
            }
        }
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList.data[position]

        when (holder.javaClass) {
            SentViewHolder::class.java -> {
                val viewHolder = holder as SentViewHolder
                viewHolder.sentMessage.text =
                    Crypto.decryptAESMessage(currentMessage.message, recipientKey)
                viewHolder.sentMessage.tag = currentMessage.messageId
                viewHolder.status.text =
                    if (currentMessage.isSeen) "Read" else if (currentMessage.isDelivered) "Delivered" else "Sent"
                viewHolder.time.text =
                    currentMessage.createdAt?.format(DateTimeFormatter.ofPattern("hh:mm:a")) ?: ""
            }

            ReceiveViewHolder::class.java -> {
                val viewHolder = holder as ReceiveViewHolder
                viewHolder.receiveMessage.tag = currentMessage.messageId
                viewHolder.receiveMessage.text =
                    Crypto.decryptAESMessage(currentMessage.message, recipientKey)
                viewHolder.time.text = currentMessage.createdAt?.toLocalTime()
                    ?.format(DateTimeFormatter.ofPattern("hh:mm:a"))
                    ?: ""
            }

            SentAudioViewHolder::class.java -> {
                val viewHolder = holder as SentAudioViewHolder
                viewHolder.playButton.tag = currentMessage.messageId
                viewHolder.status.text =
                    if (currentMessage.isSeen) "Read" else if (currentMessage.isDelivered) "Delivered" else "Sent"
                viewHolder.time.text =
                    currentMessage.createdAt?.format(DateTimeFormatter.ofPattern("hh:mm:a")) ?: ""

                setAudioMessageState(
                    currentMessage,
                    viewHolder.playButton,
                    viewHolder.downloadButton,
                    viewHolder.slider,
                    viewHolder.duration,
                    viewHolder.audioTitle,
                    viewHolder.audioHint
                )
                populateDuration(viewHolder.itemView, currentMessage)

                viewHolder.playButton.setOnClickListener {
                    audioPlayback(viewHolder.itemView, currentMessage)
                }
                viewHolder.downloadButton.setOnClickListener {
                    downloadAudioMessage(
                        viewHolder.itemView,
                        currentMessage,
                        viewHolder.playButton,
                        viewHolder.downloadButton,
                        viewHolder.slider,
                        viewHolder.duration,
                        viewHolder.audioTitle,
                        viewHolder.audioHint
                    )
                }
                viewHolder.slider.setLabelFormatter {
                    formatProgressLabel(it, viewHolder.duration)
                }
            }

            else -> {
                val viewHolder = holder as ReceiveAudioViewHolder
                viewHolder.time.text = currentMessage.createdAt?.toLocalTime()
                    ?.format(DateTimeFormatter.ofPattern("hh:mm:a"))
                    ?: ""
                viewHolder.playButton.tag = currentMessage.messageId

                setAudioMessageState(
                    currentMessage,
                    viewHolder.playButton,
                    viewHolder.downloadButton,
                    viewHolder.slider,
                    viewHolder.duration,
                    viewHolder.audioTitle,
                    viewHolder.audioHint
                )
                populateDuration(viewHolder.itemView, currentMessage)

                viewHolder.playButton.setOnClickListener {
                    audioPlayback(viewHolder.itemView, currentMessage)
                }
                viewHolder.downloadButton.setOnClickListener {
                    downloadAudioMessage(
                        viewHolder.itemView,
                        currentMessage,
                        viewHolder.playButton,
                        viewHolder.downloadButton,
                        viewHolder.slider,
                        viewHolder.duration,
                        viewHolder.audioTitle,
                        viewHolder.audioHint
                    )
                }
                viewHolder.slider.setLabelFormatter {
                    formatProgressLabel(it, viewHolder.duration)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return messageList.data.size
    }
}
