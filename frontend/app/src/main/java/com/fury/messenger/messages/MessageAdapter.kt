package com.fury.messenger.messages

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fury.messenger.R
import com.fury.messenger.crypto.Crypto
import com.fury.messenger.data.db.model.Chat
import com.fury.messenger.helper.audio.AudioPlayer
import com.fury.messenger.helper.user.CurrentUser
import com.services.Message.ContentType
import java.time.format.DateTimeFormatter
import javax.crypto.SecretKey

class MessageAdapter(
    val context: Context, var messageList: List<Chat?>, uid: String?, var reciepentKey: SecretKey?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val player by lazy {
        AudioPlayer(context)
    }
    val ITEM_RECV = 1
    val ITEM_SENT = 2
    val uid2 = uid

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.sentMessage)
        val status = itemView.findViewById<TextView>(R.id.status)
        val time = itemView.findViewById<TextView>(R.id.time)

    }

    class SentAudioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playButton = itemView.findViewById<Button>(R.id.play)
        val status = itemView.findViewById<TextView>(R.id.status)
        val time = itemView.findViewById<TextView>(R.id.time)

    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMessage = itemView.findViewById<TextView>(R.id.receiveMessage)
        val time = itemView.findViewById<TextView>(R.id.time)

    }

    class ReceiveAudioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playButton = itemView.findViewById<Button>(R.id.play)


        val time = itemView.findViewById<TextView>(R.id.time)

    }

    override fun getItemViewType(position: Int): Int {

        val currentMessage = messageList[position]
        if (currentMessage != null) {
            return if (CurrentUser.getCurrentUserPhoneNumber() == currentMessage.sender) {
                if ((currentMessage.contentType as ContentType) == ContentType.Audio) {
                    0
                } else {
                    2
                }
            } else {
                if ((currentMessage.contentType as ContentType) == ContentType.Audio) {
                    1
                } else {
                    3
                }
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
        val currentMessage = messageList[position]
        val touchListener= object :View.OnTouchListener {

            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {

                if (p1 != null) {
                    when (p1.action) {
                        MotionEvent.ACTION_DOWN -> {
                            val message = currentMessage!!.message
                            val file = reciepentKey?.let {
                                Crypto.decryptAudio(
                                    message, it
                                )
                            }
                            if (file != null) {
                                player.playFile(file)
                            }


                        }

                        MotionEvent.ACTION_UP -> {
                            player.stop()
                            p0?.performClick()
                        }
                    }
                }

                return true

            }

        }
        if (currentMessage != null) {
            if (holder.javaClass == SentViewHolder::class.java) {
                val viewHolder = holder as SentViewHolder
                viewHolder.sentMessage.text =
                    Crypto.decryptMessage(currentMessage.message, CurrentUser.getPrivateKey())
                //TODO: Add color to the text
                viewHolder.status.text =
                    if (currentMessage.isSeen) "Read" else if (currentMessage.isDelivered) "Delivered" else "Sent"
                viewHolder.time.text =
                    currentMessage.createdAt?.format(DateTimeFormatter.ofPattern("hh:mm:a")) ?: ""

            } else if (holder.javaClass == ReceiveViewHolder::class.java) {
                val viewHolder = holder as ReceiveViewHolder

                viewHolder.receiveMessage.text =
                    Crypto.decryptMessage(currentMessage.message, CurrentUser.getPrivateKey())

                viewHolder.time.text =
                    currentMessage.createdAt?.format(DateTimeFormatter.ofPattern("hh:mm:a")) ?: ""

            } else if (holder.javaClass == SentAudioViewHolder::class.java) {
                val viewHolder = holder as SentAudioViewHolder

                viewHolder.status.text =
                    if (currentMessage.isSeen) "Read" else if (currentMessage.isDelivered) "Delivered" else "Sent"
                viewHolder.time.text =
                    currentMessage.createdAt?.format(DateTimeFormatter.ofPattern("hh:mm:a")) ?: ""


                viewHolder.playButton.setOnTouchListener(touchListener)

            } else {
                val viewHolder = holder as ReceiveAudioViewHolder

                viewHolder.playButton.setOnTouchListener(touchListener)
            }
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

}