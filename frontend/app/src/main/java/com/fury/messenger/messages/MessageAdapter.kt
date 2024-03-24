package com.fury.messenger.messages

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fury.messenger.R
import com.fury.messenger.data.db.model.Chat
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.rsa.RSA
import java.time.format.DateTimeFormatter

class MessageAdapter(val context: Context, var messageList: List<Chat?>, uid: String?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECV = 1
    val ITEM_SENT = 2
    val uid2 = uid

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.sentMessage)
        val status = itemView.findViewById<TextView>(R.id.status)
        val time = itemView.findViewById<TextView>(R.id.time)

    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMessage = itemView.findViewById<TextView>(R.id.receiveMessage)
        val time = itemView.findViewById<TextView>(R.id.time)

    }

    override fun getItemViewType(position: Int): Int {

        val currentMessage = messageList[position]
        return if (currentMessage != null && CurrentUser.getCurrentUserPhoneNumber() == currentMessage.sender) {
            0
        } else {
            1
        }
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            //recieve
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
            ReceiveViewHolder(view)
        } else {
            //sent
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            SentViewHolder(view)
        }
    }
;
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        Log.d("load message",currentMessage!!.message)

        if (currentMessage != null) {
            if (holder.javaClass == SentViewHolder::class.java) {
                val viewHolder = holder as SentViewHolder
                viewHolder.sentMessage.text = RSA.decryptMessage(currentMessage.message,CurrentUser.getPrivateKey())
                //TODO: Add color to the text
                viewHolder.status.text =
                    if (currentMessage.isSeen) "Read" else if (currentMessage.isDelivered) "Delivered" else "Sent"
                viewHolder.time.text =
                    currentMessage.createdAt?.format(DateTimeFormatter.ofPattern("hh:mm:a")) ?:""

            } else {
                val viewHolder = holder as ReceiveViewHolder

                viewHolder.receiveMessage.text = RSA.decryptMessage(currentMessage.message,CurrentUser.getPrivateKey())

                viewHolder.time.text =
                    currentMessage.createdAt?.format(DateTimeFormatter.ofPattern("hh:mm:a")) ?:""

            }
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

}