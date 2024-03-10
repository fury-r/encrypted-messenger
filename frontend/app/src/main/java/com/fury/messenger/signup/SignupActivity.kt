package com.fury.messenger.signup



import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.fury.messenger.databinding.ActivityLoginBinding

import com.fury.messenger.R
import com.fury.messenger.main.MainActivity
import com.fury.messenger.main.User
import com.fury.messenger.manageBuilder.ManageChanelBuilder
import com.fury.messenger.otp.OtpActivity
import com.fury.messenger.utils.Constants
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.protobuf.BlockingService

import com.services.Register.RegisterRequest
import com.services.ServicesGrpc
import io.grpc.ManagedChannel

class SignupActivity: AppCompatActivity() {

    private lateinit var edtPhoneNumber: EditText
    private lateinit var edtusername: EditText
    private  lateinit var editEmail:EditText
    private lateinit var edtPassword: EditText
    private lateinit var loginBtn: Button
    private lateinit var signupBtn: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var channel:ManagedChannel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        mAuth = FirebaseAuth.getInstance()
        edtPhoneNumber= findViewById(R.id.phoneNumber)
        edtusername = findViewById(R.id.username)
        editEmail=findViewById(R.id.email)
        edtPassword = findViewById(R.id.password)
        signupBtn = findViewById(R.id.signupBtn)
        channel=ManageChanelBuilder.channel
        val client=ManageChanelBuilder.client
        signupBtn.setOnClickListener {
            signupBtn.setEnabled(false)
//            val email = edtEmail.text.toString()
//            val username = edtusername.text.toString()
//            val password = edtPassword.text.toString()
//            sign(email, password,username)
            var number=edtPhoneNumber.text.toString()
            val countryCode= number?.let { Constants.getCountryCodeFromPhone(it) }
            if(countryCode!=null) {
                number = number.replace("+$countryCode", "")
            }
            val request=RegisterRequest.newBuilder().setEmail(editEmail.text.toString()).setPassword( edtPassword.text.toString()).setPhoneNumber(number).setCountryCode(countryCode).setUsername( edtusername.text.toString()).build()
            val response=client.register(request)

            Toast.makeText(this,if (response.hasError() )response.error+"."+response.message else (response.message),Toast.LENGTH_SHORT).show()
            Log.d("response",response.message+"1111")
            if(!response.hasError()){
                val intent=Intent(this,OtpActivity::class.java)
                intent.putExtra("phoneNumber",number)
                intent.putExtra("email",editEmail.text.toString())

                startActivity(intent)
            }



            signupBtn.setEnabled(true)




        }
    }

    private fun sign(email: String, password: String,username:String) {
        Log.d("myTag",email+" "+password)
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                Log.d("Mytag", task.isSuccessful.toString())
                Log.d("Check",task.exception.toString())
                if (task.isSuccessful) {
                    addUserToFirestore(username,email,mAuth.currentUser?.uid!!)
                    val intent = Intent(this, MainActivity::class.java)
                    finish()

                    startActivity(intent)

                } else {
                    print(task)
                    Toast.makeText(this, "Some error occured", Toast.LENGTH_SHORT).show()

                }


            }
    }
    private fun addUserToFirestore(username: String,email: String,uid:String){
        Log.d("database",username+" "+email+" "+uid)
//
//    dbRef=FirebaseDatabase.getInstance().getReference()
//        dbRef.child("user").child(uid).setValue(User(username,email,uid))
    }
}
