package com.fury.messenger.main

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fury.messenger.R
import com.fury.messenger.TripleDES
import com.fury.messenger.helper.contact.ContactChats
import com.fury.messenger.helper.ui.Menu
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.messages.ChatActivity
import com.fury.messenger.rsa.RSA.decryptMessage
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class UserAdapter(val context: Context, var userList: ArrayList<ContactChats>, setSelected: (Int?) -> Unit, private var view:Boolean=false) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var storageRef: StorageReference
    private var ctx=context
    private var uri: String? = null
    val decipher = TripleDES
    private var setSelected=setSelected
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)

        return UserViewHolder(view)
    }
  
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.textName.text = currentUser.contact.name
        // saving position to handle operation in menuSelect
        holder.lastMessage.tag=position
        if(view) {
            holder.count.text=""
            holder.datetime.text=""
        }else{
            if (currentUser.messageCount > 0) {
                holder.count.text = currentUser.messageCount.toString()

            } else {
                holder.count.visibility = View.GONE
            }
            if (currentUser.latestMessage != null) {
                holder.lastMessage.text = decryptMessage(currentUser.latestMessage.message, CurrentUser.getPrivateKey())
                if(currentUser.latestMessage.sender!=CurrentUser.phoneNumber){
                    holder.lastMessage.setTypeface(null, Typeface.BOLD);
                }
                holder.datetime.text= currentUser.latestMessage.createdAt?.format(DateTimeFormatter.ofPattern("hh:mm:a"))
                    ?: ""

            }
            else{
                holder.datetime.text=OffsetDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:a"))
            }
        }



//        getLastText(currentUser.uid,holder)
        if(!view){

            holder.itemView.setOnClickListener {
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("Contact", currentUser.contact.name)
                intent.putExtra("phoneNumber", currentUser.contact.phoneNumber)
                intent.putExtra("key", currentUser.contact.key)

                context.startActivity(intent)
            }

        }



    }


    override fun getItemCount(): Int {
        return userList.size
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
            Menu.onPrepareOptionsMenu(menu,userList[lastMessage.tag as Int ],context)


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