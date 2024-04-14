package com.fury.messenger.helper.user

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fury.messenger.crypto.Crypto
import com.fury.messenger.data.db.DBMessage
import com.fury.messenger.data.db.helper.DateTypeConverters
import com.fury.messenger.data.db.model.Chat
import com.fury.messenger.data.db.model.ChatsDao
import com.fury.messenger.data.db.model.Contact
import com.fury.messenger.data.db.model.ContactsDao
import com.fury.messenger.helper.socket.SocketHandler
import com.fury.messenger.messagebroker.ConsumerThread
import com.fury.messenger.utils.TokenManager
import com.services.Auth.AuthResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.Key
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import java.util.function.Supplier
import java.util.stream.Collectors

object CurrentUser {

    var phoneNumber: String? = null
    private var uuid: String? = null

    private var phoneCountryCode: String? = null
    private var token: String = ""
    private var email: String? = null
    private var username: String? = ""
    private var privateKey: PrivateKey? = null
    private var publicKey: PublicKey? = null
    private var MessageThread: ConsumerThread? = null
    private var blockedUsers: ArrayList<String> = arrayListOf()
    private var NotificationThread: ConsumerThread? = null
    private var image: String? = null
    private var status: String? = null

    fun keyToString(key: ByteArray): String {
        return Base64.getEncoder().encodeToString(key)
    }

    fun getImage():String?{
        return  image
    }
    fun getStatus():String?{
        return  status
    }
    fun setBlockedUser(blockedUsers: ArrayList<String>) {
        this.blockedUsers = blockedUsers
    }
    fun setStatus(status: String) {
        this.status = status
    }


    fun setImage(image: String) {

        this.image = image
    }


    fun getPhoneCountryCode(): String? {
        return phoneCountryCode
    }

    fun getUsername(): String? {
        return username
    }

    fun getCurrentUserPhoneNumber(): String? {
        return phoneNumber
    }

    fun isBlocked(number: String): Boolean {
        return this.blockedUsers?.contains(number) ?: false
    }

    fun getToken(): String {
        return token
    }

    fun getPublicKey(): PublicKey? {
        return publicKey
    }

    fun getPrivateKey(): PrivateKey? {
        return privateKey
    }

    fun setUsername(username: String? = "") {
        CurrentUser.username = username
    }

    fun getUUID(): String? {
        return uuid
    }

    fun setUUID(uuid: String? = "") {
        CurrentUser.uuid = uuid
    }

    fun setPhoneCountryCode(phoneCountryCode: String? = "") {
        CurrentUser.phoneCountryCode = phoneCountryCode
    }

    fun setToken(token: String = "") {
        CurrentUser.token = token
    }

    fun setCurrentUserPhoneNumber(phoneNumber: String?) {
        CurrentUser.phoneNumber = phoneNumber
        CurrentUser.phoneNumber?.let { Log.d("setting phone Number", it) }

    }

    fun setEmail(email: String? = "") {
        CurrentUser.email = email
    }

    fun setPublicKey(value: Any) {
        Log.d("Thread-Messenger key", "setting key $value")
        publicKey = if (value::class == String::class) {
            convertStringToKeyFactory(value as String, 2) as PublicKey
        } else {
            value as PublicKey
        }

    }

    fun setPrivateKey(value: Any) {
        privateKey = if (value::class == String::class) {
            convertStringToKeyFactory(value as String, 1) as PrivateKey
        } else {
            value as PrivateKey
        }
        Log.d(
            "thread-messenger set private  key as ",
            privateKey.toString() + " from " + value
        )

    }

    fun saveUserDetails(ctx: Context, token: String, response: AuthResponse) {
        this.setToken(token)
        val tokenManager = TokenManager(ctx)
        tokenManager.setToken(token)
        this.setCurrentUserPhoneNumber(response.user.phoneNumber)
        this.image=response.user.image
        this.status=response.user.status
        this.username=response.user.username

        if (response.user.blockedUsersList.isNotEmpty()) {
            val arrayList = response.user.blockedUsersList.stream().collect(
                Collectors.toCollection(
                    Supplier { ArrayList() })
            )
            this.setBlockedUser(arrayList as ArrayList<String>)

        }


        val publicKey = tokenManager.getPublicKey(true)

        this.setEmail(response.user.email)
        if (response.user.pubKey != publicKey && response.user.pubKey.isNotEmpty()) {
            if (publicKey == null) {
                Crypto.initRSA(ctx)

            } else {

                Crypto.submitPublicKey(publicKey as String)
            }


        }
        Crypto.initRSA(ctx)

    }
    fun convertStringToKeyFactory(key: String, type: Int): Key? {


        try {

            val keyBytes = Base64.getDecoder().decode(key)


            return if (type == 1) {
                val keySpec = PKCS8EncodedKeySpec(keyBytes)

                val keyFactory = KeyFactory.getInstance("RSA")
                keyFactory.generatePrivate(keySpec)
            } else {

                val keySpec = X509EncodedKeySpec(keyBytes)
                val keyFactory = KeyFactory.getInstance("RSA")
                keyFactory.generatePublic(keySpec)

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }



    suspend fun subscribeToMessageQueue(ctx: Context) {
        val scope = CoroutineScope(Dispatchers.IO)
        withContext(Dispatchers.Main) {
            val socket = (async { SocketHandler.setSocket() }).await()

            socket?.emit("message")
            socket?.on("message") { args ->
                scope.launch {
                    args.map {
                        if (it::class.java == ByteArray::class.java) {
                            Log.d("processMessage", it.toString())
                            DBMessage.processMessageAndSave(it as ByteArray, ctx)
                        }
                    }
                }

            } ?: ""
            socket?.connect()
        }
    }

    fun killMessageThread() {
        MessageThread!!.join()
    }

}


@Database(entities = [Contact::class, Chat::class], version = 2)
@TypeConverters(*[DateTypeConverters::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactsDao
    abstract fun chatsDao(): ChatsDao

}