package com.fury.messenger.main

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fury.messenger.R
import com.fury.messenger.TripleDES
import com.fury.messenger.helper.contact.ContactChats
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.messages.ChatActivity
import com.fury.messenger.rsa.RSA.decryptMessage
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class UserAdapter(val context: Context, var userList: ArrayList<ContactChats>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var storageRef: StorageReference
    private var uri: String? = null
    val decipher = TripleDES
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.textName.text = currentUser.contact.name
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


//        getLastText(currentUser.uid,holder)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("Contact", currentUser.contact.name)
            intent.putExtra("phoneNumber", currentUser.contact.phoneNumber)
            intent.putExtra("key", currentUser.contact.key)

            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var lastMessage = itemView.findViewById<TextView>(R.id.lastMessage)
        val textName = itemView.findViewById<TextView>(R.id.txtName)
        val imageView = itemView.findViewById<ImageView>(R.id.profilePicture)
        var datetime = itemView.findViewById<TextView>(R.id.datetime)
        var count = itemView.findViewById<TextView>(R.id.count)

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