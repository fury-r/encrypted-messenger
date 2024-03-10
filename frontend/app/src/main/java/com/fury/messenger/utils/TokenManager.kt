package com.fury.messenger.utils

import android.content.Context
import android.util.Log
import com.fury.messenger.data.helper.user.CurrentUser.convertStringToKeyFactory
import com.fury.messenger.data.helper.user.CurrentUser.keyToString
import com.fury.messenger.utils.Constants.APP_NAME
import com.fury.messenger.utils.Constants.PREFS_TOKEN
import com.fury.messenger.utils.Constants.USER_TOKEN
import java.io.Serializable
import java.security.Key
import java.security.PrivateKey
import java.security.PublicKey
import javax.inject.Inject

class TokenManager @Inject constructor( context: Context) {
    private  var prefs=context.getSharedPreferences(PREFS_TOKEN,Context.MODE_PRIVATE)

    fun getToken():String?{
        return  prefs.getString(USER_TOKEN,null)

    }

    fun setToken(token:String){
        val editor=prefs.edit()
        editor.putString(USER_TOKEN,token)
        editor.apply()
    }
    fun deleteToken(){
        val editor=prefs.edit()
        editor.remove(USER_TOKEN)
        editor.apply()
    }
    fun deleteKey(){
        val editor=prefs.edit()
        editor.remove(APP_NAME+"_PUB_KEY")
        editor.remove(APP_NAME+"_PRIV_KEY")

        editor.apply()
    }
    fun getPrivateKey(): Key? {
       val key= prefs.getString(APP_NAME+"_PRIV_KEY",null)
        if(key!=null){
            Log.d("Messenger-x  privkey",key)
            return convertStringToKeyFactory(key,1)

        }
        return  null
    }
    fun getPublicKey(requireString:Boolean?=false): Serializable? {
        val key= prefs.getString(APP_NAME+"_PUB_KEY",null)
        if(requireString == true){
            return key
        }
        if(key!=null){
            Log.d("Messenger-x pub-key",key)

            return convertStringToKeyFactory(key,2)


        }
        return  null
    }

    fun privateKeyToString(privateKey: PrivateKey): String {
        val privateKeyBytes = privateKey.encoded
        return android.util.Base64.encodeToString(privateKeyBytes, android.util.Base64.NO_WRAP)
    }

    fun publicKeyToString(publicKey: PublicKey): String {
        val publicKeyBytes = publicKey.encoded
        return android.util.Base64.encodeToString(publicKeyBytes, android.util.Base64.NO_WRAP)
    }

    fun saveKeys(id:String,key:String){
        val editor=prefs.edit()
        Log.d("asKey", "saving keys $id $key")
        editor.putString(id,key)
        editor.apply()
    }
}