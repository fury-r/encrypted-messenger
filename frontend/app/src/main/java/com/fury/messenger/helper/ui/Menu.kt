package com.fury.messenger.helper.ui

import android.content.Context
import android.view.Menu
import com.fury.messenger.R
import com.fury.messenger.helper.contact.ContactChats
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.main.MainActivity

object Menu {

     fun onPrepareOptionsMenu(menu: Menu,contact:ContactChats,context: Context): Boolean {

        val deleteMenuItem = menu.findItem(R.id.deleteItem)
        val blockMenuItem = menu.findItem(R.id.block)
        val pinMenuItem = menu.findItem(R.id.pin)

        if (context.classLoader !== MainActivity::class &&  deleteMenuItem!=null) {
            deleteMenuItem.isVisible = false
        }
         if (!contact.contact.isVerified){
             blockMenuItem.isEnabled=false
             pinMenuItem.isEnabled=false

         }
         if (CurrentUser.isBlocked(contact.contact.phoneNumber)) {

             blockMenuItem.title = "UnBlock"
         }
         if (contact.contact.isPinned == true) {

             pinMenuItem.title = "UnPin"

         }
         return true

    }

}