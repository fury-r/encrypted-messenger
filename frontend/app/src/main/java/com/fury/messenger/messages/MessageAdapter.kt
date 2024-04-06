package com.fury.messenger.messages

import ChatsByDate
import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fury.messenger.R
import com.fury.messenger.crypto.Crypto
import com.fury.messenger.helper.audio.AudioPlayer
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.utils.formatMilliSeconds
import com.google.android.material.slider.Slider
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
        val slider=itemView.findViewById<Slider>(R.id.playSlider)

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
        val slider=itemView.findViewById<Slider>(R.id.playSlider)
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
                LayoutInflater.from(context).inflate(R.layout.receive_audio, parent, false)
            ReceiveAudioViewHolder(view)
        }
    };
    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList.data[position]
        var file:File?=null
        var playing=false
        fun getDuration(view: View) {
            val duration=view.findViewById<TextView>(R.id.duration)
            val playSlider=view.findViewById<Slider>(R.id.playSlider)
            Log.d("here", (currentMessage.contentType==ContentType.Audio.name).toString())
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
                    Log.d("ddd mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)",
                        mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?:""
                    )
                    this@MessageAdapter.context.runCatching{



                        mmr.setDataSource(filePath)
                        val ms =
                            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                        mmr.release()
                        if (ms != null) {
                            duration.text =  formatMilliSeconds(ms.toLong())
                        }
                        if (ms != null) {
                            playSlider.valueTo=ms.toFloat()
                        }

                    }
                }
            }
        }


        fun audioPlayback(it:View){
            scope.launch {
                val slider=it.findViewById<Slider>(R.id.playSlider)
                if(!playing){


                    playing=true
                    if (file != null) {
                        player.playFile(file!!,fun(){

                            this@MessageAdapter.context.runCatching{
                                it.setBackgroundResource(R.drawable.play_circle)

                            }

                        })

                        this@MessageAdapter.context.runCatching{
                            it.setBackgroundResource(R.drawable.baseline_pause_circle_24)

                        }
                    }
                }
                else{
                    playing=false
                    player.stop()
                    this@MessageAdapter.context.runCatching {
                        it.setBackgroundResource(R.drawable.play_circle)
                        slider.visibility=View.GONE

                    }
                }


            }
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
                Log.d("Key MessageAdapter r",  currentMessage.message)
                Log.d("Key MessageAdapter r key", recipientKey.toString())

                viewHolder.receiveMessage.text=Crypto.decryptAESMessage(currentMessage.message, recipientKey)
                Log.d(",esss0",currentMessage.toString())
                viewHolder.time.text= currentMessage.createdAt?.toLocalTime()?.format(DateTimeFormatter.ofPattern("hh:mm:a"))
                    ?: ""

//                if(currentMessage.createdAt==null ){
//                    viewHolder.time.text = OffsetDateTime.now()?.format(DateTimeFormatter.ofPattern("hh:mm:a")) ?: ""
//
//                }else{
//                    viewHolder.time.text =
//                        currentMessage.createdAt?.format(DateTimeFormatter.ofPattern("hh:mm:a")) ?: ""
//                }


            }

            SentAudioViewHolder::class.java -> {
                val viewHolder = holder as SentAudioViewHolder
                viewHolder.playButton.tag=currentMessage.messageId
                viewHolder.time.text= currentMessage.createdAt?.toLocalTime()?.format(DateTimeFormatter.ofPattern("hh:mm:a"))
                    ?:""

                viewHolder.status.text =
                    if (currentMessage.isSeen) "Read" else if (currentMessage.isDelivered) "Delivered" else "Sent"
                viewHolder.time.text =
                    currentMessage.createdAt?.format(DateTimeFormatter.ofPattern("hh:mm:a")) ?: ""

                    getDuration(viewHolder.itemView)

                viewHolder.playButton.setBackgroundResource(R.drawable.play_circle)


                viewHolder.playButton.setOnClickListener{
                    audioPlayback(it)
                }
                viewHolder.slider.setLabelFormatter{
                    formatMilliSeconds(it.toLong())

                }
            }
            else -> {
                val viewHolder = holder as ReceiveAudioViewHolder
                viewHolder.time.text= currentMessage.createdAt?.toLocalTime()?.format(DateTimeFormatter.ofPattern("hh:mm:a"))
                    ?:""
                viewHolder.playButton.tag=currentMessage.messageId
                    getDuration(viewHolder.itemView)

                viewHolder.playButton.setBackgroundResource(R.drawable.play_circle)

                viewHolder.playButton.setOnClickListener{
                    audioPlayback(it)
                }
                viewHolder.slider.setLabelFormatter{
                     formatMilliSeconds(it.toLong())

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return messageList.data.size
    }

}