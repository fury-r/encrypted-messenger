package com.fury.messenger.ui.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fury.messenger.R
import com.fury.messenger.Validater.VerifyTokenActivity
import com.fury.messenger.main.MainActivity
import com.fury.messenger.manageBuilder.ManageChanelBuilder
import com.fury.messenger.manageBuilder.createAuthenticationStub
import com.fury.messenger.otp.OtpActivity
import com.fury.messenger.signup.SignupActivity
import com.fury.messenger.utils.Constants
import com.fury.messenger.utils.TokenManager
import com.google.firebase.auth.FirebaseAuth
import com.services.Login.LoginRequest
import io.grpc.ManagedChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var editPhoneNumber: EditText
    private lateinit var loginBtn: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var signupBtn: Button
    private lateinit var channel: ManagedChannel
    private var hasReadContactPermission: Boolean = false
    private var hasStoragePermission: Boolean = false
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    lateinit var message: String
    private var scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Log.d("send to backend", "")
        channel = ManageChanelBuilder.channel
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
                hasStoragePermission =
                    permission[Manifest.permission.MANAGE_EXTERNAL_STORAGE] ?: hasStoragePermission
                hasReadContactPermission =
                    permission[Manifest.permission.READ_CONTACTS] ?: hasReadContactPermission

            }
        requestPermission()
        val tokenManager = TokenManager(this)
        val token = tokenManager.getToken()
        val client = createAuthenticationStub(tokenManager.getToken())
        Log.d("here sss",token.toString())
        if (token!=null) {
            startActivity(Intent(this,VerifyTokenActivity::class.java))
            finish()

        }



        editPhoneNumber = findViewById(R.id.phoneNumber)
        signupBtn = findViewById(R.id.signupBtn)
        loginBtn = findViewById(R.id.loginBtn)


        loginBtn.setOnClickListener {

            loginBtn.isEnabled = false
            try {
                var number = editPhoneNumber.text.toString()
                val countryCode = number.let { Constants.getCountryCodeFromPhone(it) }
                number = number.replace("+$countryCode", "")
                val request =
                    LoginRequest.newBuilder().setPhoneNumber(number).setCountryCode(countryCode)
                       .build();

                scope.launch {
                    val response =(async {  client.login(request) }).await();
                   runOnUiThread {  Toast.makeText(
                       this@LoginActivity,
                       if (response.hasError()) response.error + "." + response.message else (response.message),
                       Toast.LENGTH_SHORT
                   ).show() }

                    if (!response.hasError()) {
                        runOnUiThread{
                            val intent = Intent(this@LoginActivity, OtpActivity::class.java)
                            intent.putExtra("phoneNumber", number)

                            startActivity(intent)
                        }
                    }
                }

            } catch (e: Exception) {
                Log.d("Error", e.message.toString())
            }
            loginBtn.isEnabled = true

        }
        signupBtn.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
        }

    }


    private fun requestPermission() {
        Log.d("ask permission", "")
        hasReadContactPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
        hasStoragePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val permissionRequest: MutableList<String> = ArrayList()
        if (!hasStoragePermission) {
            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissionRequest.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
        }
        if (!hasReadContactPermission) {
            permissionRequest.add(Manifest.permission.READ_CONTACTS)
        }
        permissionLauncher.launch(permissionRequest.toTypedArray())
    }

    private fun loginFirebase(email: String, password: String) {
        Log.d("myTag", "$email $password")
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    finish()

                    startActivity(intent)

                } else {
                    Toast.makeText(this@LoginActivity, "Invalid Credentials", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
}

