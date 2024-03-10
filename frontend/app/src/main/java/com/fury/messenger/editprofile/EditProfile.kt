package com.fury.messenger.editprofile

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.fury.messenger.R
import com.google.android.gms.common.internal.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class EditProfile :AppCompatActivity(){
    private val PICK_IMAGE_REQUEST = 71
    private lateinit var auth: FirebaseAuth
    private lateinit var  uploadBtn:Button
    private lateinit var  textField: TextView

    private val pickImage:Int? = 100
    lateinit var imageView: ImageView
    private var imageUri: Uri? = null
    private lateinit var storageRef: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile)
        supportActionBar?.hide()
        imageView=findViewById(R.id.profilePicture)
        textField=findViewById(R.id.usename)
        textField.text = FirebaseAuth.getInstance().currentUser?.getDisplayName().toString()
        storageRef = FirebaseStorage.getInstance().reference
        getImage()
        uploadBtn=findViewById(R.id.uploadBtn)
        uploadBtn.setOnClickListener{


            getContent.launch("image/*")
        }



    }
    private fun getImage(){
       storageRef?.child("profile/" + FirebaseAuth.getInstance().currentUser?.uid.toString())?.downloadUrl?.addOnSuccessListener { uri:Uri->
            Glide.with(getApplicationContext())
                .load(uri.toString())
                .into(imageView);

         }

        }
    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        val ref = storageRef?.child("profile/" + FirebaseAuth.getInstance().currentUser?.uid.toString())
              ref?.putFile(uri!!)
        Toast.makeText(this, "Profile Picture Updated", Toast.LENGTH_SHORT).show()
        getImage()
    }

}