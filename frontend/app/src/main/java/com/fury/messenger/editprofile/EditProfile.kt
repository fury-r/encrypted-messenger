package com.fury.messenger.editprofile

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.fury.messenger.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class EditProfile : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 71
    private lateinit var auth: FirebaseAuth
    private lateinit var uploadBtn: Button
    private lateinit var textField: TextView

    private val pickImage: Int = 100
    lateinit var imageView: ImageView
    private var imageUri: Uri? = null
    private lateinit var storageRef: StorageReference
    private lateinit var saveButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile)
        supportActionBar?.hide()
        imageView = findViewById(R.id.profilePicture)
        textField = findViewById(R.id.username)
        saveButton = findViewById(R.id.saveEdit)
        textField.text = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        storageRef = FirebaseStorage.getInstance().reference
        getImage()
        uploadBtn = findViewById(R.id.uploadBtn)
        uploadBtn.setOnClickListener {


            val res = getContent.launch("image/*")
            Log.d(
                "messenger", res.toString()
            )
        }

        saveButton.setOnClickListener {


        }
    }

    private fun getImage() {
        storageRef?.child("profile/" + FirebaseAuth.getInstance().currentUser?.uid.toString())?.downloadUrl?.addOnSuccessListener { uri: Uri ->
            Glide.with(getApplicationContext())
                .load(uri.toString())
                .into(imageView);

        }

    }

    private fun saveImage() {
        val images =
            storageRef.child("profile/${FirebaseAuth.getInstance().currentUser?.uid.toString()}");
        val image = storageRef.child("${FirebaseAuth.getInstance().currentUser?.uid.toString()}");


    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        val ref =
            storageRef?.child("profile/" + FirebaseAuth.getInstance().currentUser?.uid.toString())
        ref?.putFile(uri!!)
        Toast.makeText(this, "Profile Picture Updated", Toast.LENGTH_SHORT).show()
        getImage()
    }

}