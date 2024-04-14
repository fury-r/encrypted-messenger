package com.fury.messenger.helper.contact


import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import com.fury.messenger.data.db.DbConnect
import com.fury.messenger.data.db.model.Chat
import com.fury.messenger.data.db.model.Contact
import com.fury.messenger.helper.ui.convertImageToBase64String
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.manageBuilder.createAuthenticationStub
import com.services.ContactOuterClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Contacts(private var ctx: Context) {
    private var contactList: ArrayList<Contact>? = null

    private fun setContactList(contacts: ArrayList<Contact>) {
        this.contactList = contacts

    }

    suspend fun addToContactList(contactlistRequest: ContactOuterClass.ContactsList) {
        val contacts = contactlistRequest.contactsList.map {
            Contact(
                0,
                it.name,
                it.phoneNumber,
                "",
                "",
                it.pubKey,
                "",
                it.countryCode,
                it.isVerified
            )
        } as ArrayList<Contact>
        this.contactList?.addAll(contacts)

        updateDb(contacts)
    }

    suspend fun listAllContacts(): ArrayList<Contact> {
        return withContext(Dispatchers.IO) {
            val db by lazy { DbConnect.getDatabase() }

            val contacts = db.contactDao().loadAllVerified()



            setContactList(ArrayList(contacts) as ArrayList<Contact>)
            return@withContext ArrayList(contacts) as ArrayList<Contact>
        }

    }

    @SuppressLint("Range")
    suspend fun getContactsFromPhone() {
        while (CurrentUser.phoneNumber.isNullOrEmpty()) {

        }

        withContext(Dispatchers.IO) {
            val contactList: ArrayList<Contact> = arrayListOf()
            val phones = ctx.contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, "display_name ASC"
            )
            if (phones != null && phones.count > 0) {
                while (phones.moveToNext()) {
                    val id = phones.getString(phones.getColumnIndex(ContactsContract.Contacts._ID))
                    val name =
                        phones.getString(phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val phoneNumber =
                        phones.getString(phones.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                            .toInt()
                    Log.d("ddd", phones.toString())
                    val imageindex = phones.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)
                    val image = if (imageindex == -1) null else phones.getString(imageindex)
                        ?.let { it -> convertImageToBase64String(Uri.parse(it), ctx) }

                    if (phoneNumber > 0) {
                        val phoneNumberValue = ctx.contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            arrayOf(id),
                            null
                        )

                        if (phoneNumberValue != null && phoneNumberValue.count > 0) {
                            while (phoneNumberValue.moveToNext()) {
                                val phoneNumValue = phoneNumberValue.getString(
                                    phoneNumberValue.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER
                                    )
                                )
                                val hasNumber = contactList?.find { contact ->
                                    contact.phoneNumber == phoneNumValue.split(" ").joinToString("")
                                }
                                if (hasNumber == null) {
                                    val contact = Contact(
                                        id.toInt(),
                                        name,
                                        phoneNumValue.split(" ").joinToString(""),
                                        image
                                    )
                                    if (!contact.phoneNumber.contains(CurrentUser.phoneNumber!!)) {
                                        contactList.add(contact)
                                    }
                                }
                            }
                            DbConnect.getDatabase(ctx).contactDao()
                                .insertAll(*contactList.toTypedArray())
                            phoneNumberValue.close()

                        }

                    }

                }
                phones.close()
            }

            setContactList(contactList)
            updateDb(contactList)
            Log.d("list length", contactList.count().toString())

        }
    }

    suspend fun validateContacts() {
        withContext(Dispatchers.IO) {
            if (this@Contacts.contactList != null) {
                val client = createAuthenticationStub(CurrentUser.getToken())
                val request = ContactOuterClass.ContactsList.newBuilder()
                Log.d("validating", contactList.toString())

                this@Contacts.contactList?.forEachIndexed { index, contact ->
                    run {

                        val countryCode = contact.countryCode

                        if (!contact.phoneNumber.contains(CurrentUser.phoneNumber!!)) {
                            if (countryCode != null) {
                                val subRequest =
                                    ContactOuterClass.Contact.newBuilder().setId(index.toString())
                                        .setPhoneNumber(contact.phoneNumber).setName(contact.name)
                                        .setIsVerified(contact.isVerified)
                                        .setCountryCode(countryCode).build()
                                request.addContacts(subRequest)

                            } else {
                                val subRequest =
                                    ContactOuterClass.Contact.newBuilder().setId(index.toString())
                                        .setPhoneNumber(contact.phoneNumber).setName(contact.name)
                                        .setIsVerified(contact.isVerified).build()
                                request.addContacts(subRequest)
                            }
                        }
                    }
                }
                val response = client.validateContacts(request.build())


                Log.d("response for contacts", response.contactsList.size.toString())
                if (response.contactsList.size > 0) {
                    updateDb(response.contactsList.map {
                        Contact(
                            0,
                            it.name,
                            it.phoneNumber,
                            "",
                            "",
                            it.pubKey,
                            "",
                            it.countryCode,
                            it.isVerified
                        )
                    } as ArrayList<Contact>)

                }
            }
        }
    }

    private suspend fun updateDb(validContacts: ArrayList<Contact>) {
        withContext(Dispatchers.IO) {

            val db = DbConnect.getDatabase()


            val contacts: ArrayList<Contact> = arrayListOf<Contact>()
            Log.d("Valid contacts", validContacts.size.toString())
            validContacts.forEach { contact ->
                run {
                    val contactSaved = db.contactDao().findByNumber(contact.phoneNumber)


                    if (contact.isVerified) {

                        contacts.add(
                            Contact(
                                name = contact.name,
                                phoneNumber = contact.phoneNumber,
                                key = contactSaved.key,
                                image = contact.image,
                                status = contact.status,
                                countryCode = contact.countryCode,
                                uuid = contact.uuid,
                                pubKey = contact.pubKey,
                                isVerified = contact.isVerified
                            )
                        )

                    }

                }


            }
            Log.d("Update", contacts.size.toString())

            db.contactDao().updateAll(*contacts.toTypedArray())


        }
    }


    suspend fun getAllVerifiedContacts(): ArrayList<Contact> {
        val contacts: ArrayList<Contact>
        withContext(Dispatchers.IO) {
            val db = DbConnect.getDatabase()
            contacts = db.contactDao().loadAllVerified() as ArrayList<Contact>


        }
        return contacts
    }

    suspend fun getAllContacts(): ArrayList<Contact> {
        val contacts: ArrayList<Contact>
        withContext(Dispatchers.IO) {
            val db = DbConnect.getDatabase()
            contacts = db.contactDao().getAll() as ArrayList<Contact>


        }
        return contacts
    }

}


class ContactChats(
    val contact: Contact,
    var messageCount: Int,
    var latestMessage: Chat? = null
)