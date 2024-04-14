package com.fury.messenger.signup


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fury.messenger.R
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.manageBuilder.ManageChanelBuilder
import com.fury.messenger.manageBuilder.createAuthenticationStub
import com.fury.messenger.otp.OtpActivity
import com.fury.messenger.utils.Constants
import com.services.Register
import io.grpc.ManagedChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {

    private lateinit var edtPhoneNumber: EditText
    private lateinit var edtusername: EditText
    private lateinit var editEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var loginBtn: Button
    private lateinit var signupBtn: Button

    private lateinit var channel: ManagedChannel
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        edtPhoneNumber = findViewById(R.id.phoneNumber)
        edtusername = findViewById(R.id.username)
        editEmail = findViewById(R.id.email)
        edtPassword = findViewById(R.id.password)
        signupBtn = findViewById(R.id.submitBtn)
        channel = ManageChanelBuilder.channel
        val client = createAuthenticationStub(CurrentUser.getToken())
        signupBtn.setOnClickListener {
            scope.launch {
                Log.d("Register", "register user")
                runOnUiThread {
                    signupBtn.isEnabled = false
                }

                var number = edtPhoneNumber.text.toString()
                val countryCode = number?.let { Constants.getCountryCodeFromPhone(it) }
                if (countryCode != null) {
                    number = number.replace("+$countryCode", "")
                }
                val request =
                    Register.RegisterRequest.newBuilder().setEmail(editEmail.text.toString())
                        .setPassword(edtPassword.text.toString()).setPhoneNumber(number)
                        .setCountryCode(countryCode).setUsername(edtusername.text.toString())
                        .build()
                val response = (async { client.register(request) }).await()

                runOnUiThread{
                    Toast.makeText(
                        this@SignupActivity,
                        if (response.hasError()) response.error + "." + response.message else (response.message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (!response.hasError()) {
                 runOnUiThread {

                     val intent = Intent(this@SignupActivity, OtpActivity::class.java)
                     intent.putExtra("phoneNumber", number)
                     intent.putExtra("email", editEmail.text.toString())

                     startActivity(intent)
                 }
                }



                runOnUiThread {
                    signupBtn.isEnabled = true
                }
            }


        }
    }



}
