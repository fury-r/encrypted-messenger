package com.fury.messenger.Validater

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.fury.messenger.R
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.main.MainActivity
import com.fury.messenger.manageBuilder.ManageChanelBuilder
import com.fury.messenger.manageBuilder.createAuthenticationStub
import com.fury.messenger.ui.login.LoginActivity
import com.fury.messenger.utils.TokenManager
import com.google.firebase.auth.FirebaseAuth
import com.services.Auth.AuthRequest
import io.grpc.ManagedChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VerifyTokenActivity : AppCompatActivity() {
    private lateinit var editPhoneNumber: EditText
    private lateinit var loginBtn: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var signupBtn: Button
    private lateinit var channel: ManagedChannel
    private var hasReadContactPermission: Boolean = false
    private lateinit var progressBar: ProgressBar
    private lateinit var loaderText: TextView
    private var hasStoragePermission: Boolean = false
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    lateinit var message: String
    private var scope = CoroutineScope(Dispatchers.Main)

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        Log.d("send to backend", "")
        channel = ManageChanelBuilder.channel
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
                hasStoragePermission =
                    permission[Manifest.permission.MANAGE_EXTERNAL_STORAGE] ?: hasStoragePermission
                hasReadContactPermission =
                    permission[Manifest.permission.READ_CONTACTS] ?: hasReadContactPermission

            }
        loaderText = findViewById(R.id.loaderText)
        progressBar = findViewById(R.id.progressBar)

        val tokenManager = TokenManager(this)
        val token = tokenManager.getToken().toString()
        val client = createAuthenticationStub(tokenManager.getToken())
        Log.d("setting user details", token)

        if (token.isNotEmpty()) {


            val ctx = this
            var intent: Intent
            scope.launch {

                withContext(Dispatchers.IO) {
                    val request = AuthRequest.newBuilder().build()

                    val response = client.verifyToken(request)
                    Log.d("setting user details", response.token)
                    val tokenManager = TokenManager(this@VerifyTokenActivity)

                    intent = if (response.hasError()) {
                        tokenManager.deleteToken()
                        Intent(ctx, LoginActivity::class.java)


                    } else {
                        CurrentUser.saveUserDetails(this@VerifyTokenActivity, token, response)
                        Intent(ctx, MainActivity::class.java)
                    }
                    startActivity(intent)

                }


            }


        }


    }


}

