package com.fury.messenger.otp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.fury.messenger.R
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.main.MainActivity
import com.fury.messenger.manageBuilder.ManageChanelBuilder
import com.fury.messenger.manageBuilder.createAuthenticationStub
import com.services.Otp.OtpRequest
import com.services.Otp.ReSendOtpRequest
import io.grpc.ManagedChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class OtpActivity : AppCompatActivity() {
    private lateinit var channel: ManagedChannel
    private lateinit var regOtpBtn: Button
    private lateinit var countDown: TextView
    private lateinit var countDownTimer:CountDownTimer
    private lateinit var otpBtn: Button
    private lateinit var otpInput: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        channel = ManageChanelBuilder.channel
        val scope = CoroutineScope(Dispatchers.IO)

        val client = createAuthenticationStub(CurrentUser.getToken())
        otpBtn = findViewById(R.id.otpBtn)
        regOtpBtn = findViewById(R.id.renegerateOtp)
        countDown=findViewById(R.id.durationTime)
        countDownTimer=object :CountDownTimer(300000,1000){
            override fun onTick(p0: Long) {
                val minutes = p0 / 60000
                val seconds = (p0 % 60000) / 1000
                val timeLeftFormatted = String.format("%02d:%02d", minutes, seconds)
                    countDown.text = "Time Left: $timeLeftFormatted"            }

            override fun onFinish() {
               regOtpBtn.isEnabled=true
            }


        }
        countDownTimer.start()
        val phoneNumber=intent.getStringExtra("phoneNumber")
        otpInput = findViewById(R.id.otp)
        otpBtn.setOnClickListener {
            otpBtn.isEnabled = false
            val request = OtpRequest.newBuilder().setOtp(
                otpInput.text.toString()

            ).setPhoneNumber(phoneNumber).build()
            scope.launch {
                val response = (async { client.otp(request) }).await()


                if (!response.hasError()) {
                    CurrentUser.saveUserDetails(this@OtpActivity, response.token, response)

                    runOnUiThread {
                        val intent = Intent(this@OtpActivity, MainActivity::class.java)
                        phoneNumber?.let { it1 -> Log.d("Saving token", it1) }

                        startActivity(intent)
                    }
                }
              runOnUiThread {   otpBtn.isEnabled = true }


            }

        }

        regOtpBtn.setOnClickListener{
            val request=ReSendOtpRequest.newBuilder().setPhoneNumber(phoneNumber).build()
            regOtpBtn.isEnabled=false

            scope.launch {
                try {
                    client.regenerateOtp(request)
                    runOnUiThread{

                        countDownTimer.start()

                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }



        }

    }

}