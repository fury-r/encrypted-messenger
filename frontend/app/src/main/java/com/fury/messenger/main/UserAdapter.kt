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
import com.fury.messenger.TripleDES
import com.fury.messenger.data.db.model.Contact
import com.fury.messenger.messages.ChatActivity
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import java.util.*
import kotlin.collections.ArrayList

class UserAdapter(val context: Context, var userList:ArrayList<Contact>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var storageRef: StorageReference
    private  var uri: String? =null
    val decipher=TripleDES
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
       val view:View= LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser=userList[position]
        holder.textName.text=currentUser.name
//        getLastText(currentUser.uid,holder)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            Log.d(currentUser.name+"xzzzz1",currentUser.phoneNumber+""+currentUser.pubKey)
            intent.putExtra("Contact",currentUser.name)
            intent.putExtra("phoneNumber",currentUser.phoneNumber)
            intent.putExtra("chatPrivateKey",currentUser.chatPrivateKey)
            intent.putExtra("chatPublicKey",currentUser.chatPublickey)

            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return userList.size
    }
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var lastMessage=itemView.findViewById<TextView>(R.id.lastMessage)
        val textName=itemView.findViewById<TextView>(R.id.txtName)
        val imageView=itemView.findViewById<ImageView>(R.id.profilePicture)
        var datetime=itemView.findViewById<TextView>(R.id.datetime)
    }

//    private fun getLastText(uid:String? ,holder: UserViewHolder){
//        val myuid:String=FirebaseAuth.getInstance()?.currentUser?.uid!!
//        dbRef= FirebaseDatabase.getInstance().getReference()
//        dbRef.child("chats").child(myuid+uid).child("messages").limitToLast(1)
//            .addValueEventListener(object : ValueEventListener{
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    for (message in snapshot.children){
//                        val text=message.getValue(Message::class.java)
//                        if(text?.senderId==myuid){
//                            holder.lastMessage.text="You:"+decipher._decrypt(text?.message!!,uid+FirebaseAuth.getInstance().currentUser?.uid!!)
//
//                           var time= Date(text.datetime!!)
//                            val formatter = SimpleDateFormat("MM-dd-yyyy")
//                            val timeformat=SimpleDateFormat("hh:mm")
//                            val date=formatter.format(time)
//                            val currentDate=formatter.format(Date())
//                            storageRef = FirebaseStorage.getInstance().reference
//
//                            storageRef?.child("profile"+uid.toString())?.downloadUrl?.addOnSuccessListener { url: Uri ->
//
//                                uri=url.toString()
//                                Glide.with(context.applicationContext)
//                                    .load(uri)
//                                    .into(holder.imageView);
//
//                            }
//
//                            if (date!=currentDate){
//                                        holder.datetime.text=date.toString()
//
//                                }
//                            else{
//                                holder.datetime.text=timeformat.format(time).toString()
//                            }
//
//                        }
//                        else{
//                            holder.lastMessage.text=decipher._decrypt(text?.message!!,FirebaseAuth.getInstance().currentUser?.uid!!+uid)
//
//                        }
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//
//            })
//    }

}