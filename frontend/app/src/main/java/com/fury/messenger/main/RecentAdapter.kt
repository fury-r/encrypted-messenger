package com.fury.messenger.main

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fury.messenger.R
import com.fury.messenger.data.db.model.Contact
import com.fury.messenger.messages.ChatActivity
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import java.util.*
import kotlin.collections.ArrayList

class RecentAdapter(val context: Context, var userList:ArrayList<Contact>): RecyclerView.Adapter<RecentAdapter.RecentViewHolder>() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var storageRef: StorageReference
    private  var uri: String? =null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        val view:View= LayoutInflater.from(context).inflate(R.layout.recent_users,parent,false)
        return RecentViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        val currentUser=userList[position]
        Log.d("username",currentUser.name.toString())
        holder.textName.text=currentUser.name
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
            intent.putExtra("Contact",currentUser.name)
            intent.putExtra("phoneNumber",currentUser.phoneNumber)
//            intent.putExtra("uri",url)
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return userList.size
    }
    class RecentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textName=itemView.findViewById<TextView>(R.id.txtName)
        val imageView=itemView.findViewById< ImageView>(R.id.profilePicture)
    }


}