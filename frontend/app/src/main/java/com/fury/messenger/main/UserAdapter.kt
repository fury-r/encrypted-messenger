package com.fury.messenger.main

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.CountDownTimer
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fury.messenger.R
import com.fury.messenger.crypto.Crypto
import com.fury.messenger.helper.contact.ContactChats
import com.fury.messenger.helper.ui.Menu
import com.fury.messenger.helper.ui.base64StringToImage
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.messages.ChatActivity
import com.fury.messenger.utils.ChatActivityIntentProps
import com.services.Message.ContentType
import java.time.Duration
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class UserAdapter(val context: Context, var userList: ArrayList<ContactChats>, setSelected: (Int?) -> Unit, private var view:Boolean=false) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var setSelected=setSelected
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)

        return UserViewHolder(view)
    }
  
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.textName.text = currentUser.contact.name?:currentUser.contact.phoneNumber
        // saving position to handle operation in menuSelect
        holder.lastMessage.tag=position
        if(currentUser.contact.image?.isNotEmpty() == true){
            holder.imageView.setImageBitmap(base64StringToImage(currentUser.contact.image?:""))

        }
        if(view) {
            holder.count.text=""

            holder.datetime.text=""
            holder.lastMessage.text=currentUser.contact.status
            holder.itemView.isEnabled=currentUser.contact.isVerified

        }else{
            if (currentUser.messageCount > 0) {
                holder.count.text = currentUser.messageCount.toString()

            } else {
                holder.count.visibility = View.GONE
            }
            if (currentUser.latestMessage != null) {



               if(currentUser.contact.typeTime!=null) {
                    val diffInSeconds=Duration.between(currentUser.contact.typeTime,OffsetDateTime.now()).seconds.toInt()
                    if(diffInSeconds<10){
                        holder.lastMessage.text = "Typing..."
                        holder.lastMessage.setTypeface(null, Typeface.BOLD);

                        val  countDownTimer=object : CountDownTimer((diffInSeconds*1000).toLong(),1000){
                            override fun onTick(p0: Long) {

                            }

                            override fun onFinish() {
                                this@UserAdapter.setLastMessage(currentUser,holder)
                            }


                        }
                        countDownTimer.start()
                    }
                }
                else{
                    this@UserAdapter.setLastMessage(currentUser,holder)

                }

                holder.datetime.text= currentUser.latestMessage!!.createdAt?.format(DateTimeFormatter.ofPattern("hh:mm:a"))
                    ?: ""

            }
            else{
                holder.datetime.text=OffsetDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:a"))
            }
        }




            holder.itemView.setOnClickListener {
                val intent = Intent(context, ChatActivity::class.java)
                ChatActivityIntentProps(intent,currentUser.contact)
                context.startActivity(intent)
            }





    }


    override fun getItemCount(): Int {
        return userList.size
    }
    fun setLastMessage(currentUser:ContactChats,holder:UserViewHolder){
        var message:String=""
        if(currentUser.latestMessage!!.contentType==ContentType.Text.name){
            message= Crypto.decryptAESMessage(
                currentUser.latestMessage!!.message,
                Crypto.convertAESstringToKey(currentUser.contact.key)
            )

        }else if(currentUser.latestMessage!!.contentType==ContentType.Audio.name) {
            message="Voice Message"

        }
        if(currentUser.latestMessage!!.sender!= CurrentUser.phoneNumber){
            holder.lastMessage.setTypeface(null, Typeface.BOLD);
        } else {
            message="You:${message}"
        }
        holder.lastMessage.text = message
    }

  inner  class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnCreateContextMenuListener,View.OnLongClickListener {
        var lastMessage: TextView = itemView.findViewById(R.id.lastMessage)
        val textName: TextView = itemView.findViewById(R.id.txtName)
        val imageView:ImageView = itemView.findViewById(R.id.profilePicture)
        var datetime: TextView = itemView.findViewById(R.id.datetime)
        var count: TextView = itemView.findViewById(R.id.count)
        init {
            itemView.setOnCreateContextMenuListener(this)
            itemView.setOnLongClickListener(this)

        }
      override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
          val inflater = MenuInflater(v.context)
          inflater.inflate(R.menu.user_menu, menu)
            Menu.onPrepareOptionsMenu(menu,userList[lastMessage.tag as Int ].contact,context)


      }

      override fun onLongClick(v: View?): Boolean {
        v?.let{
            setSelected(lastMessage.tag as Int )
            itemView.showContextMenu()


            return  true
        }
          return false
      }

  }


}