package com.fury.messenger.otp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.fury.messenger.R
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.main.MainActivity
import com.fury.messenger.manageBuilder.ManageChanelBuilder
import com.fury.messenger.manageBuilder.createAuthenticationStub
import com.services.Otp.OtpRequest
import io.grpc.ManagedChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class OtpActivity : AppCompatActivity() {
    private lateinit var channel: ManagedChannel

    private lateinit var otpBtn: Button
    private lateinit var otpInput: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        channel = ManageChanelBuilder.channel
        val scope = CoroutineScope(Dispatchers.IO)

        val client = createAuthenticationStub(CurrentUser.getToken())
        otpBtn = findViewById(R.id.otpBtn)



        otpInput = findViewById(R.id.otp)
        otpBtn.setOnClickListener {
            otpBtn.isEnabled = false
            val request = OtpRequest.newBuilder().setOtp(
                otpInput.text.toString()

            ).setPhoneNumber(intent.getStringExtra("phoneNumber")).build()
            scope.launch {
                val response = (async { client.otp(request) }).await()


                if (!response.hasError()) {
                    Log.d("ssss",response.toString())
                    CurrentUser.saveUserDetails(this@OtpActivity, response.token, response)

                    runOnUiThread {
                        val intent = Intent(this@OtpActivity, MainActivity::class.java)
                        intent.getStringExtra("phoneNumber")?.let { it1 -> Log.d("Saving token", it1) }

                        startActivity(intent)
                    }
                }
              runOnUiThread {   otpBtn.isEnabled = true }


            }

        }


    }

}