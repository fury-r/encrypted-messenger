package com.fury.messenger.utils

import com.google.i18n.phonenumbers.PhoneNumberUtil

object Constants {

    const val  API="http://192.168.0.103"
    const val  PREFS_TOKEN="MESSENGER_TOKEN_X"
    const val  USER_TOKEN="USER_TOKEN"
    const val APP_NAME="MESSENGER_X"


    fun getCountryCodeFromPhone(phoneNumber:String): String {
        val phoneInstance= PhoneNumberUtil.getInstance()
        try {
            val phoneNumber = phoneInstance.parse(phoneNumber, null)
            return (phoneNumber?.countryCode?.toString()?:phoneNumber) as String
        }catch (_ : Exception) {
        }
        return phoneNumber

    }
    fun byteArrayToString(byteArray: ByteArray): String {
        return String(byteArray, Charsets.UTF_8)
    }
    fun stringToByteArray(string: String): ByteArray {
        return string.toByteArray()
    }
}