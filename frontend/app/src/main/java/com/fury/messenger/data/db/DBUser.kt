package com.fury.messenger.data.db


import android.annotation.SuppressLint
import android.content.Context
import com.fury.messenger.data.db.model.Contact
import com.fury.messenger.helper.contact.ContactChats
import com.fury.messenger.helper.contact.Contacts

//import com.fury.messenger.data.db.model.ContactsModel
//import org.jetbrains.exposed.sql.selectAll

object DBUser {

    fun getDataByPubKey(pubKey: String): Contact {
        val contact = DbConnect.getDatabase().contactDao().findByPublicKey(pubKey)

        return contact as Contact
    }

    @SuppressLint("Range")
    suspend fun getDataByPhoneNumber(phoneNumber: String): Contact {

        return DbConnect.getDatabase().contactDao().findByNumber(phoneNumber) as Contact
    }

    suspend fun getPublicKeyForUser(number: String): String? {
        var chatPublicKey: String? = null

        return getDataByPhoneNumber(number).pubKey
    }

    fun getAllLastMessagesForContact(
        ctx: Context,
        data: ArrayList<Contact>
    ): ArrayList<ContactChats> {
        val db = DbConnect.getDatabase(ctx)
        return data.map {
            val count = db.chatsDao().findByReceiversUnreadMessage(it.phoneNumber)
            val message = db.chatsDao().findLastMessageByReciever(it.phoneNumber)

            return@map ContactChats(it, count, message)
        } as ArrayList<ContactChats>

    }

    suspend fun getAllContactsWithMessages(
        ctx: Context,
        all: Boolean = false
    ): ArrayList<ContactChats> {
        val contacts = Contacts(ctx)



            contacts.getContactsFromPhone()
            contacts.validateContacts()

        return if (all) {
            val data = contacts.getAllContacts()

            getAllLastMessagesForContact(
                ctx,
                data
            )
        } else {
            val data = contacts.getAllVerifiedContacts()

            getAllLastMessagesForContact(
                ctx,
                data
            ).filter { it.latestMessage!=null  } as ArrayList<ContactChats>

        }


    }
}
