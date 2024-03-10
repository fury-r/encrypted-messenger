package com.fury.messenger.otp
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.fury.messenger.R
import com.fury.messenger.data.helper.user.CurrentUser
import com.fury.messenger.main.MainActivity
import com.fury.messenger.manageBuilder.ManageChanelBuilder
import com.fury.messenger.utils.TokenManager
import com.services.Otp.OtpRequest
import com.services.ServicesGrpc
import io.grpc.ManagedChannel

class OtpActivity:AppCompatActivity() {
private lateinit var otp:String
private  lateinit var channel:ManagedChannel
private  lateinit var client:ServicesGrpc.ServicesBlockingStub
private lateinit var otp1:EditText;
    private lateinit var otp2:EditText;
    private lateinit var otp3:EditText;
    private lateinit var otp4:EditText;
    private lateinit var otp5:EditText;
    private lateinit var otp6:EditText;
    private  lateinit var otpBtn:Button
    @SuppressLint("SuspiciousIndentation")
    override  fun onCreate(savedInstanceState:Bundle?){
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_otp)
        channel=ManageChanelBuilder.channel

        val client=ManageChanelBuilder.client
        otpBtn=findViewById(R.id.otpBtn)
        otp1=findViewById(R.id.otp1)
        otp2=findViewById(R.id.otp2)
        otp3=findViewById(R.id.otp3)
        otp4=findViewById(R.id.otp4)
        otp5=findViewById(R.id.otp5)
        otp6=findViewById(R.id.otp6)
        //TODO: otp textView shift
//        otp1.addTextChangedListener(TextWatcher(){
//
//        })

    otpBtn.setOnClickListener(){
        otpBtn.isEnabled = false
        val request=OtpRequest.newBuilder().setOtp(
            otp1.text.toString()+otp2.text.toString()+otp3.text.toString()+otp4.text.toString()+otp5.text.toString()+otp6.text.toString()

        ).setPhoneNumber(intent.getStringExtra("phoneNumber")).build()

      val response=  client.otp(request)
        Toast.makeText(this,if (response.hasError() )response.error+"."+response.message.phoneNumber else (response.message.phoneNumber),Toast.LENGTH_SHORT).show()

        if(!response.hasError()){
            intent.getStringExtra("phoneNumber")?.let { it1 -> Log.d("Saving token", it1) }
            val tokenManager=TokenManager(this)
            tokenManager.setToken(response.message.token)
            CurrentUser.setToken(response.message.token)
            CurrentUser.setPhoneNumber(response.message.phoneNumber)
            CurrentUser.setEmail(response.message.email)
            if(response.message.uuid!=null && response.message.uuid.isNotEmpty()) {
                CurrentUser.setUUID(response.message.uuid)
            }
            val intent=Intent(this,MainActivity::class.java)

            startActivity(intent)
            finish()

        }
        otpBtn.isEnabled = true

    }



}

}