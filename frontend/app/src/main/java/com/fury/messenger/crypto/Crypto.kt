package com.fury.messenger.crypto

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.helper.user.CurrentUser.keyToString
import com.fury.messenger.manageBuilder.createAuthenticationStub
import com.fury.messenger.utils.Constants.APP_NAME
import com.fury.messenger.utils.Constants.byteArrayToString
import com.fury.messenger.utils.Constants.stringToByteArray
import com.fury.messenger.utils.TokenManager
import com.services.UserOuterClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.Key
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESedeKeySpec
import javax.crypto.spec.SecretKeySpec


object Crypto {
    //RSA/ECB/PKCS1Padding
    private lateinit var keyPairGen: KeyPairGenerator

    private lateinit var keyPair: KeyPair;
    private lateinit var privateKey: PrivateKey;
    private lateinit var publicKey: PublicKey;
    private lateinit var sign: Signature;
    private var cipher: Cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
    private lateinit var tokenManager: TokenManager
    private var scope = CoroutineScope(Dispatchers.IO)
    private  var ALGORITHM="DESede"
    private fun generateEncryptionKeys(): List<Key> {
        keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048)
        keyPair = keyPairGen.generateKeyPair()
        publicKey = keyPair.public
        privateKey = keyPair.private
        return listOf(keyPair.private, keyPair.public)
    }

    fun getAES(): SecretKey {
//        val keyGen = KeyGenerator.getInstance("AES")
//        keyGen.init(256)
//
//        return keyGen.generateKey();

        val keyData = byteArrayOf(
            0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
            0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10,
            0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18
        )
        val keySpec=DESedeKeySpec(keyData)
        val keyFactory=SecretKeyFactory.getInstance(ALGORITHM)
        return  keyFactory.generateSecret(keySpec)


    }

    fun initRSA(ctx: Context) {


        this.tokenManager = TokenManager(ctx)

        val hasPublicKey = tokenManager.getPublicKey()
        val hasPrivateKey = tokenManager.getPrivateKey()

        if (hasPrivateKey != null) {
            Log.d("Messenger-x", "has saved key $hasPrivateKey")
            privateKey = hasPrivateKey as PrivateKey
            publicKey = hasPublicKey as PublicKey

        } else {
            Log.d("Messenger-x", " does not have saved key")
            generateEncryptionKeys()
            val privateKeyString = keyToString(privateKey.encoded)
            val publicKeyString = keyToString(publicKey.encoded)

            this.tokenManager.saveKeys("${APP_NAME}_PRIV_KEY", privateKeyString)
            Log.d("Messenger-x", " saving key in firestore")
            this.tokenManager.saveKeys("${APP_NAME}_PUB_KEY", publicKeyString)


            // ⚠️WARNING: For testing.Need to be removed private key later

            submitPublicKey(publicKeyString, privateKeyString)

            Log.d("Messenger-x", " setting keys to the user")


        }
        CurrentUser.setPublicKey(publicKey)
        CurrentUser.setPrivateKey(privateKey)
        Log.d("asKey", hasPublicKey.toString())

        sign = Signature.getInstance("SHA256withRSA")
        //this.cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        //   runTest(publicKey, privateKey)

    }


    //TODO: Fix encryption and decryption.Currently decryption does not show the original data
    fun encryptMessage(message: String, publicKey: Key?): String {
        val messageBytes: ByteArray = stringToByteArray(message)
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val encryptedBytes = cipher.doFinal(messageBytes)
        return Base64.getEncoder().encodeToString(encryptedBytes)

    }

    @SuppressLint("GetInstance")
    fun encryptAESMessage(message: String, key: SecretKey?): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encryptedBytes = cipher.doFinal(stringToByteArray(message))
        return Base64.getEncoder().encodeToString(encryptedBytes);

    }
    @SuppressLint("GetInstance")
    fun decryptAESMessage(message: String, key: SecretKey?): String {

        return try {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, key)
            val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(String(message.toByteArray())))
            byteArrayToString(decryptedBytes)
        }catch (e:Exception){
            e.printStackTrace()
            "Error"
        }


    }

    fun decryptMessage(message: String, privateKey: PrivateKey? = null): String {
        if (privateKey == null) {
             error("Private key missing")
        }
        Log.d("thread-messenger", CurrentUser.getPrivateKey().toString())
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey)
            val encryptedBytes = Base64.getDecoder().decode(message)
            val decryptedBytes = cipher.doFinal(encryptedBytes)
            return String(decryptedBytes, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("decrypt error", "")
        }
        return "" + ""
    }



    fun runAESTest(key: SecretKey,value: String?=null){
        val test=value?:"Hello World!"
        val keyString= convertAESKeyToString(key)


        val encryptText= encryptAESMessage(test, convertAESstringToKey(keyString))
        val decryptText= decryptAESMessage(encryptText, convertAESstringToKey(keyString))

        Log.d("runAESTest", "$test $encryptText $decryptText")
    }

    fun submitPublicKey(key: String, key2: String = "") {
        val client = createAuthenticationStub(CurrentUser.getToken())
        val request =
            UserOuterClass.User.newBuilder().setPhoneNumber(CurrentUser.getCurrentUserPhoneNumber())
                .setPubKey(
                    key
                ).build()
        client.savePubKey(request)
    }

    fun convertAESKeyToString(key: SecretKey): String {
        return Base64.getEncoder().encodeToString(key.encoded)
    }

    fun convertAESstringToKey(key: String?): SecretKey? {

        if (key?.length== 0) {
            return null
        }
//        val decodedBytes = Base64.getDecoder().decode(key)
//        return SecretKeySpec(decodedBytes, "AES")
        val decodedKey = Base64.getDecoder().decode(key)
        return SecretKeySpec(decodedKey, 0, decodedKey.size, ALGORITHM)
    }


    fun encryptAudio(filePath:String,key:SecretKey):String{
        val fileInput = FileInputStream(filePath)
        val audioData = ByteArray(File(filePath).length().toInt())
        fileInput.read(audioData)
        fileInput.close()


        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val  file=File(filePath)
        val encryptedData = cipher.doFinal(audioData)
        file.delete()

        return Base64.getEncoder().encodeToString(encryptedData)

    }
    fun decryptAudio(data:String,key:SecretKey,path:String): File {
        val audioData=Base64.getDecoder().decode(data)
        val cipher=Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE,key)
        val decrypted=cipher.doFinal(audioData)
        val file=File(path)
        val outputStream=FileOutputStream(path)





        outputStream.write(decrypted)
        outputStream.close()
        return file
    }



    fun runAESTestCrypto(){

        val test= getAES()
        runAESTest(test)
    }
}