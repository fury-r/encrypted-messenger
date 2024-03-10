package com.fury.messenger.rsa
import android.content.Context
import android.util.Log
import com.fury.messenger.data.helper.user.CurrentUser
import com.fury.messenger.data.helper.user.CurrentUser.keyToString
import com.fury.messenger.manageBuilder.ManageChanelBuilder
import com.fury.messenger.utils.Constants.APP_NAME
import com.fury.messenger.utils.Constants.byteArrayToString
import com.fury.messenger.utils.Constants.stringToByteArray
import com.fury.messenger.utils.TokenManager
import com.services.UserOuterClass.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.charset.Charset
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import java.util.Base64
import javax.crypto.Cipher




object RSA {
    //RSA/ECB/PKCS1Padding
    private lateinit var keyPairGen:KeyPairGenerator

     private lateinit var keyPair:KeyPair;
     private  lateinit var privateKey: PrivateKey;
     private  lateinit var publicKey:PublicKey;
     private  lateinit var  sign:Signature;
    private   var cipher:Cipher=Cipher.getInstance("RSA/ECB/PKCS1Padding")
    private  lateinit var tokenManager: TokenManager
    private  var scope= CoroutineScope(Dispatchers.IO)
    fun initRSA(ctx:Context) {
        keyPairGen=KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048)
        keyPair=keyPairGen.generateKeyPair()

        this.tokenManager= TokenManager(ctx)

        val hasPublicKey=tokenManager.getPublicKey()
        val hasPrivateKey=tokenManager.getPrivateKey()

        if(hasPrivateKey!=null){
            Log.d("Messenger-x", "has saved key $hasPrivateKey")
            privateKey=hasPrivateKey as PrivateKey
            publicKey=hasPublicKey as PublicKey

        }
        else{
            Log.d("Messenger-x"," does not have saved key")

            privateKey=keyPair.private
            publicKey=keyPair.public
            val privateKeyString=keyToString(keyPair.private.encoded)
            val publicKeyString=keyToString(keyPair.public.encoded)

            this.tokenManager.saveKeys("${APP_NAME}_PRIV_KEY",privateKeyString)
            Log.d("Messenger-x"," saving key in firestore")
            this.tokenManager.saveKeys("${APP_NAME}_PUB_KEY",publicKeyString)


            // ⚠️WARNING: For testing.Need to be removed private key later

            submitPublicKey(publicKeyString,privateKeyString)

            Log.d("Messenger-x"," setting keys to the user")





        }
        CurrentUser.setPublicKey(publicKey)
        CurrentUser.setPrivateKey(privateKey)
        Log.d("asKey",hasPublicKey.toString())

        sign=Signature.getInstance("SHA256withRSA")
        //this.cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
     //   runTest(publicKey, privateKey)

    }


    //TODO: Fix encryption and decryption.Currently decryption does not show the original data
    fun encryptMessage(message: String, publicKey: PublicKey): String {
        val messageBytes: ByteArray = stringToByteArray(message)
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val encryptedBytes = cipher.doFinal(messageBytes)
        return  Base64.getEncoder().encodeToString(encryptedBytes)

    }


    fun decryptMessage(message: String,privateKey:PrivateKey?=null):String?{
        Log.d("thread-messenger",CurrentUser.getPrivateKey().toString())
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        try{
            cipher.init(Cipher.DECRYPT_MODE,  CurrentUser.getPrivateKey())
            val encryptedBytes = Base64.getDecoder().decode(message)
            val decryptedBytes = cipher.doFinal(encryptedBytes)
//        val decipherText:ByteArray=cipher.doFinal( stringToByteArray(message));
            return String(decryptedBytes)
        }
        catch (e:Exception){
            e.printStackTrace()
            Log.d("decrypt error","")
        }
    return  "" +
            ""
    }


    private fun runTest(publicKey: PublicKey, privateKey: PrivateKey){
        val TEST="Hello world"
        var pubKey=keyToString(publicKey.encoded)
        Log.d(" pub key",publicKey.toString())
        Log.d("String pub key",pubKey)
        val pubKey_1= CurrentUser.convertStringToKeyFactory(pubKey,2) as PublicKey
        Log.d(" pub key",pubKey_1.toString())
        var privKey=keyToString(privateKey.encoded)


        Log.d(" pub key",publicKey.toString())
        Log.d("String pub key",privKey)
        val privKey_1= CurrentUser.convertStringToKeyFactory(privKey,1) as PrivateKey
        Log.d(" pub key",pubKey_1.toString())
        var stringConvEncrypt= encryptMessage(TEST,pubKey_1)
        var stringConvDecrypt= decryptMessage(TEST,privKey_1)
        val encrypt= encryptMessage(TEST,publicKey)
        val decrypt= decryptMessage(TEST,privateKey)
        if(TEST==stringConvEncrypt){
            Log.d("Valid-x",stringConvEncrypt+" normal $stringConvDecrypt")
        }
        else{
            Log.d("in-Valid-x",stringConvEncrypt+" normal $stringConvDecrypt")
        }
        if(TEST==decrypt){
            Log.d("Valid-x", "$encrypt normal $decrypt")
        }
        else{
            Log.d("in-Valid-x", "$encrypt normal $decrypt")

        }
        if(pubKey_1==publicKey){
            Log.d("valid-x key", (keyToString(pubKey_1.encoded)== keyToString(publicKey.encoded)).toString())
        }
    }
    fun submitPublicKey(key: String,key2:String=""){
        val client=ManageChanelBuilder.client
        // ⚠️WARNING: For testing.Need to be removed private key later

        if(key2!=null){
            val request=User.newBuilder().setPhoneNumber(CurrentUser.getPhoneNumber()).setPubKey(
                key).setPrivKey(key2).setToken(CurrentUser.getToken()).build()
            val response=client.savePubKey(request)
        }
    }

}