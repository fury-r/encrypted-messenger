package com.fury.messenger.main

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fury.messenger.R
import com.fury.messenger.helper.contact.ContactChats
import com.fury.messenger.helper.ui.Menu
import com.fury.messenger.messages.ChatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

class PinnedUserAdapter(val context: Context, var userList:ArrayList<ContactChats>, setSelected: (Int?) -> Unit): RecyclerView.Adapter<PinnedUserAdapter.RecentViewHolder>() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var storageRef: StorageReference
    private  var uri: String? =null
    private var setSelected=setSelected

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        val view:View= LayoutInflater.from(context).inflate(R.layout.recent_users,parent,false)
        return RecentViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        val currentUser=userList[position]
        Log.d("username",currentUser.contact.name.toString())
        holder.textName.text=currentUser.contact.name
        // saving position to handle operation in menuSelect
        holder.textName.tag=currentUser.contact.name

        //storageRef = FirebaseStorage.getInstance().reference
        val url=""
//        storageRef?.child("profile"+currentUser.getPhoneNumber())?.downloadUrl?.addOnSuccessListener { url: Uri ->
//            uri=url.toString()
//
//            Glide.with(context.applicationContext)
//                .load(uri)
//                .into(holder.imageView);
//
//        }
        //holder.profilePicture.src=currentUser.profile
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("Contact",currentUser.contact.name)
            intent.putExtra("phoneNumber",currentUser.contact.phoneNumber)
//            intent.putExtra("uri",url)
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return userList.size
    }
 inner   class RecentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnCreateContextMenuListener,View.OnLongClickListener{
        val textName=itemView.findViewById<TextView>(R.id.txtName)
        val imageView=itemView.findViewById< ImageView>(R.id.profilePicture)
        init {
            itemView.setOnCreateContextMenuListener(this)
            itemView.setOnLongClickListener(this)

        }
        override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
            val inflater = MenuInflater(v.context)

            inflater.inflate(R.menu.user_menu, menu)
            Menu.onPrepareOptionsMenu(menu,userList[textName.tag as Int ],context)


        }

        override fun onLongClick(v: View?): Boolean {
            v?.let{
                setSelected(textName.tag as Int )
                itemView.showContextMenu()


                return  true
            }
            return false
        }
    }


}