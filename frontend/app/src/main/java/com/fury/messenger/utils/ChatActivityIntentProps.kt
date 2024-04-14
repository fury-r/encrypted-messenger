package com.fury.messenger.utils

import android.content.Intent
import android.util.Log
import com.fury.messenger.data.db.model.Contact

fun ChatActivityIntentProps(intent: Intent,contact:Contact){
    intent.putExtra("Contact", contact.name)
    intent.putExtra("phoneNumber", contact.phoneNumber)
    contact.key?.let { it1 -> Log.d("Dd", it1) }
    intent.putExtra("key", contact.key)
}