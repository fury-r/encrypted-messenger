package com.fury.messenger.helper.ui

import android.content.Context
import android.util.Log
import android.view.Menu
import com.fury.messenger.R
import com.fury.messenger.data.db.model.Contact
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.main.MainActivity
import com.fury.messenger.messages.ChatActivity

object Menu {

     fun onPrepareOptionsMenu(menu: Menu, contact: Contact, context: Context,hideDelete:Boolean=false): Boolean {

        val deleteMenuItem = menu.findItem(R.id.deleteItem)
        val blockMenuItem = menu.findItem(R.id.block)
        val pinMenuItem = menu.findItem(R.id.pin)
         val searchMenuItem = menu.findItem(R.id.searchItem)

        if (context.classLoader !== MainActivity::class &&  deleteMenuItem!=null || hideDelete) {
            deleteMenuItem.isVisible = false
        }
         Log.d("ddd",context.classLoader.toString())
         if (context.classLoader === ChatActivity::class ) {
             searchMenuItem.isVisible=true
         }
         if (!contact.isVerified){
             blockMenuItem.isEnabled=false
             pinMenuItem.isEnabled=false

         }
         if (CurrentUser.isBlocked(contact.phoneNumber)) {

             blockMenuItem.title = "UnBlock"
         }
         if (contact.isPinned == true) {

             pinMenuItem.title = "UnPin"

         }
         return true

    }

}