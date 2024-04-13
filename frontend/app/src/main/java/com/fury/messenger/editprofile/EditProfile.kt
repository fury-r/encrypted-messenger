package com.fury.messenger.editprofile

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.fury.messenger.R
import com.fury.messenger.helper.ui.base64StringToImage
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.manageBuilder.createAuthenticationStub
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import com.services.UserOuterClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class EditProfile : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 71
    private lateinit var auth: FirebaseAuth
    private lateinit var uploadBtn: Button
    private lateinit var username: TextView
    private lateinit var statusField: TextView

    private val pickImage: Int = 100
    lateinit var imageView: ImageView
    private var imageUri: Uri? = null
    private lateinit var storageRef: StorageReference
    private lateinit var saveButton: Button
    private  var file: Uri?=null
    private  var scope=CoroutineScope(Dispatchers.IO)

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile)
        supportActionBar?.hide()
        imageView = findViewById(R.id.profilePicture)
        username = findViewById(R.id.username)
        saveButton = findViewById(R.id.saveEdit)
        statusField = findViewById(R.id.status)
        username.text = CurrentUser.getUsername()
        statusField.text = CurrentUser.getStatus()


        var image: String? =CurrentUser.getImage()
        if(image!=null){
            imageView.setImageBitmap( base64StringToImage(image) )

        }

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                image= convertImageToBase64String(uri)

                imageView.setImageBitmap(image?.let { it1 -> base64StringToImage(it1) })

                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
        getImage()
        uploadBtn = findViewById(R.id.uploadBtn)
        uploadBtn.setOnClickListener {// Registers a photo picker activity launcher in single-select mode.

            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }

        saveButton.setOnClickListener {

            val client= createAuthenticationStub(CurrentUser.getToken())
            Log.d("username", username.toString())
            val request= UserOuterClass.User.newBuilder().setUsername(username.text.toString()).setStatus(statusField.text.toString()).setImage(image).build()

            CurrentUser.setUsername(username.text.toString())
            CurrentUser.setStatus(statusField.text.toString())
            CurrentUser.setImage(request.image)

            scope.launch {
              runOnUiThread{
                  saveButton.isEnabled=false
              }
                client.updateUser(request)
                runOnUiThread {
                    saveButton.isEnabled = true
                }
            }

        }
    }

    //Move to
    private fun getImage() {
//        imageView.set

    }



    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->

        Toast.makeText(this, "Profile Picture Updated", Toast.LENGTH_SHORT).show()
        getImage()
    }
    private fun convertImageToBase64String(uri: Uri): String? {
        val inputStream = contentResolver.openInputStream(uri)
        val options = BitmapFactory.Options()
        options.inSampleSize = 2



        val byteArrayOutputStream = ByteArrayOutputStream()
        BitmapFactory.decodeStream(inputStream, null, options)
            ?.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        inputStream?.close()

        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)

    }


}