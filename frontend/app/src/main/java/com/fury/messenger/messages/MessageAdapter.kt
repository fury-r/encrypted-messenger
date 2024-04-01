package com.fury.messenger.messages

import ChatsByDate
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fury.messenger.R
import com.fury.messenger.crypto.Crypto
import com.fury.messenger.helper.audio.AudioPlayer
import com.fury.messenger.helper.user.CurrentUser
import com.services.Message.ContentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.time.format.DateTimeFormatter
import javax.crypto.SecretKey


class MessageAdapter(
    val context: Context, var messageList: ChatsByDate, uid: String?, var recipientKey: SecretKey?,  val setSelect: (String?) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private  val  scope= CoroutineScope(Dispatchers.IO)
    private var setSelected=setSelect
    private val player by lazy {
        AudioPlayer(context)
    }
    val ITEM_RECV = 1
    val ITEM_SENT = 2
    val uid2 = uid

    inner class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnCreateContextMenuListener,View.OnClickListener {
        val sentMessage = itemView.findViewById<TextView>(R.id.sentMessage)
        val status = itemView.findViewById<TextView>(R.id.status)
        val time = itemView.findViewById<TextView>(R.id.time)
        override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
            val inflater = MenuInflater(v.context)
            inflater.inflate(R.menu.chat_menu, menu)
        }
        override fun onClick(v: View?) {
            v?.let{
                setSelected(sentMessage.tag as String )
                itemView.showContextMenu()


            }
        }
    }

    inner class SentAudioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnCreateContextMenuListener,View.OnClickListener {
        val playButton = itemView.findViewById<Button>(R.id.play)
        val status = itemView.findViewById<TextView>(R.id.status)
        val time = itemView.findViewById<TextView>(R.id.time)
        var duration = itemView.findViewById<TextView>(R.id.duration)
        override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
            val inflater = MenuInflater(v.context)
            inflater.inflate(R.menu.chat_menu, menu)
        }
        override fun onClick(v: View?) {
            v?.let{
                setSelected(playButton.tag as String )
                itemView.showContextMenu()


            }
        }
    }

    inner class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnCreateContextMenuListener ,View.OnClickListener{
        val receiveMessage = itemView.findViewById<TextView>(R.id.receiveMessage)

        val time = itemView.findViewById<TextView>(R.id.time)
        override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
            val inflater = MenuInflater(v.context)
            inflater.inflate(R.menu.chat_menu, menu)
        }
        override fun onClick(v: View?) {
            v?.let{
                setSelected(receiveMessage.tag as String )
                itemView.showContextMenu()


            }
        }
    }

    inner  class ReceiveAudioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnCreateContextMenuListener,View.OnClickListener{
        val playButton: Button = itemView.findViewById(R.id.play)
        val time: TextView = itemView.findViewById(R.id.time)
        val duration = itemView.findViewById<TextView>(R.id.duration)

        override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
            val inflater = MenuInflater(v.context)
            inflater.inflate(R.menu.chat_menu, menu)

        }

        override fun onClick(v: View) {
                setSelected(playButton.tag as String )
                itemView.showContextMenu()



        }

    }

    override fun getItemViewType(position: Int): Int {

        val currentMessage = messageList.data[position]
        Log.d("ss",currentMessage.contentType+" "+ ContentType.Audio.toString())
        return if (CurrentUser.getCurrentUserPhoneNumber() == currentMessage.sender) {
            if (currentMessage.contentType == ContentType.Audio.toString()) {
                2
            } else {
                0
            }
        } else {
            if (currentMessage.contentType == ContentType.Audio.toString()) {
                3
            } else {
                1
            }
        }
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == 1) {
            //receive text
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
            ReceiveViewHolder(view)
        } else if (viewType == 0) {
            //sent text
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            SentViewHolder(view)
        } else if (viewType == 2) {
            //sent text

            val view: View =
                LayoutInflater.from(context).inflate(R.layout.sent_audio, parent, false)
            SentAudioViewHolder(view)

        } else {
            //receive audio
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.sent_audio, parent, false)
            ReceiveAudioViewHolder(view)
        }
    };
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList.data[position]
        var file:File?=null
        var duration:String?=null
        if (currentMessage.contentType==ContentType.Audio.name){
            scope.launch{
                val filePath="${context.filesDir}/audio/${currentMessage.sender}-${currentMessage.createdAt.toString().replace("+","-").replace(":","-")}.mp3"
                val message = currentMessage.message
                file = (async{recipientKey?.let {
                    Crypto.decryptAudio(
                        message, it, filePath
                    )
                }}).await()
                val uri = Uri.parse(filePath)
                val mmr = MediaMetadataRetriever()
                mmr.setDataSource(context, uri)
                duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            }
        }
        val touchListener= View.OnTouchListener { p0, p1 ->
            if (p1 != null) {
                when (p1.action) {
                    MotionEvent.ACTION_DOWN -> {
                        scope.launch {

                            if (file != null) {
                                player.playFile(file!!)
                            }


                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        player.stop()
                        p0?.performClick()
                    }
                }
            }

            true
        }

        when (holder.javaClass) {
            SentViewHolder::class.java -> {
                val viewHolder = holder as SentViewHolder
                viewHolder.sentMessage.text =
                    Crypto.decryptAESMessage(currentMessage.message, recipientKey)
                Log.d("Key MessageAdapter",  currentMessage.message.toString())
                viewHolder.sentMessage.tag=currentMessage.messageId
                //TODO: Add color to the text
                viewHolder.status.text =
                    if (currentMessage.isSeen) "Read" else if (currentMessage.isDelivered) "Delivered" else "Sent"
                viewHolder.time.text =
                    currentMessage.createdAt?.format(DateTimeFormatter.ofPattern("hh:mm:a")) ?: ""

            }
            ReceiveViewHolder::class.java -> {
                val viewHolder = holder as ReceiveViewHolder
                viewHolder.receiveMessage.tag=currentMessage.messageId

                viewHolder.receiveMessage.text =
                    Crypto.decryptAESMessage(currentMessage.message, recipientKey)

                viewHolder.time.text =
                    currentMessage.createdAt?.format(DateTimeFormatter.ofPattern("hh:mm:a")) ?: ""

            }
            SentAudioViewHolder::class.java -> {
                val viewHolder = holder as SentAudioViewHolder
                viewHolder.playButton.tag=currentMessage.messageId

                viewHolder.status.text =
                    if (currentMessage.isSeen) "Read" else if (currentMessage.isDelivered) "Delivered" else "Sent"
                viewHolder.time.text =
                    currentMessage.createdAt?.format(DateTimeFormatter.ofPattern("hh:mm:a")) ?: ""

                if(file!=null){
                    viewHolder.duration.text=duration
                }


                viewHolder.playButton.setOnTouchListener(touchListener)

            }
            else -> {
                val viewHolder = holder as ReceiveAudioViewHolder
                viewHolder.playButton.tag=currentMessage.messageId
                if(file!=null){
                    viewHolder.duration.text=duration
                }
                viewHolder.playButton.setOnTouchListener(touchListener)
            }
        }
    }

    override fun getItemCount(): Int {
        return messageList.data.size
    }

}