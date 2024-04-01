package com.fury.messenger.helper.contact

//import com.fury.messenger.data.db.model.ContactsModel
//import com.fury.messenger.data.db.model.ContactsModel.chatPrivateKey
//import com.fury.messenger.data.db.model.ContactsModel.chatPublickey
//import com.fury.messenger.data.db.model.ContactsModel.countryCode
//import com.fury.messenger.data.db.model.ContactsModel.createdAt
//import com.fury.messenger.data.db.model.ContactsModel.id
//import com.fury.messenger.data.db.model.ContactsModel.image
//import com.fury.messenger.data.db.model.ContactsModel.isVerified
//import com.fury.messenger.data.db.model.ContactsModel.name
//import com.fury.messenger.data.db.model.ContactsModel.phoneNumber
//import com.fury.messenger.data.db.model.ContactsModel.pubKey
//import com.fury.messenger.data.db.model.ContactsModel.uuid
import android.annotation.SuppressLint
import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import com.fury.messenger.data.db.DbConnect
import com.fury.messenger.data.db.model.Chat
import com.fury.messenger.data.db.model.Contact
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.manageBuilder.createAuthenticationStub
import com.services.ContactOuterClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//import org.jetbrains.exposed.sql.selectAll

class Contacts(private var ctx: Context) {
    private var contactList: ArrayList<Contact>? = null

    private fun setContactList(contacts: ArrayList<Contact>) {
        this.contactList = contacts

    }

    suspend fun listAllContacts(): ArrayList<Contact> {
        return withContext(Dispatchers.IO) {
            val db  by lazy { DbConnect.getDatabase() }

            val contacts = db.contactDao().loadAllVerified()
//            val users = ContactsModel.selectAll().where { isVerified eq true }.map {
//                Contact(
//                    it[id].toString(),
//                    it[name],
//                    it[phoneNumber],
//                    it[image],
//                    it[pubKey],
//                    it[chatPublickey],
//                    it[chatPrivateKey],
//                    it[countryCode],
//                    it[isVerified],
//                    it[uuid],
//                    it[createdAt]
//                )
//            }


            setContactList(ArrayList(contacts) as ArrayList<Contact>)
            return@withContext  ArrayList(contacts) as ArrayList<Contact>
        }

    }

    @SuppressLint("Range")
    suspend fun getContactsFromPhone() {
        while (CurrentUser.phoneNumber.isNullOrEmpty()){

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
                    val image =
                        phones.getString(phones.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))

                    if (phoneNumber > 0) {
                        val phoneNumberValue = ctx.contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            arrayOf(id),
                            null
                        )
                        if(phoneNumber.toString()=="9527698053" || phoneNumber.toString()=="9158907407"){
                            Log.d("check", phoneNumber.toString()+""+CurrentUser.phoneNumber)

                        }
                        if (phoneNumberValue != null && phoneNumberValue.count > 0  ) {
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
                                        id.toInt(), name, phoneNumValue.split(" ").joinToString(""), image
                                    )
                                   if(contact.phoneNumber!=CurrentUser.phoneNumber){
                                       contactList.add(contact)
                                   }
                                }
                            }
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
    suspend fun validateContacts(){
        withContext(Dispatchers.IO){
            if(this@Contacts.contactList!=null){
                val client= createAuthenticationStub(CurrentUser.getToken())
                val request= ContactOuterClass.ContactsList.newBuilder()
                Log.d("validating", contactList.toString())

                this@Contacts.contactList?.forEachIndexed { index, contact ->
                    run {

                        val countryCode=contact.countryCode
                        if(contact.phoneNumber!=CurrentUser.phoneNumber){
                            if(countryCode!=null){
                                val subRequest =ContactOuterClass.Contact.newBuilder().setId(index.toString())
                                    .setPhoneNumber(contact.phoneNumber).setName(contact.name)
                                    .setIsVerified(contact.isVerified).setCountryCode(countryCode).build()
                                request.addContacts( subRequest)

                            }else{
                                val subRequest = ContactOuterClass.Contact.newBuilder().setId(index.toString())
                                    .setPhoneNumber(contact.phoneNumber).setName(contact.name)
                                    .setIsVerified(contact.isVerified).build()
                                request.addContacts(subRequest)
                            }
                        }
                    }
                }
                val response= client.validateContacts(request.build())


            Log.d("response for contacts" ,response.contactsList.size.toString())
                if (response.contactsList.size>0){
                    updateDb(response.contactsList.map {
                         Contact(0,it.name,it.phoneNumber,"",it.pubKey,"",it.countryCode,it.isVerified)
                    } as ArrayList<Contact>)

                }
            }
        }
    }

    private suspend fun updateDb(validContacts: ArrayList<Contact>) {
        withContext(Dispatchers.IO) {

            val db = DbConnect.getDatabase()

//        transaction {
//            SchemaUtils.create(ContactsModel)
            val contacts: ArrayList<Contact> = arrayListOf<Contact>()
            Log.d("Valid contacts",validContacts.size.toString())
            validContacts.forEach { contact ->
                run {
                    val index =
                        this@Contacts.contactList?.indexOfFirst { it.phoneNumber == contact.phoneNumber }
                    if (index != null) {
                        this@Contacts.contactList?.get(index)?.pubKey?.let { it ->
                            Log.d(
                                index.let { this@Contacts.contactList?.get(it)?.name }, it
                            )
                        }
                    }
                    if (index != null && this@Contacts.contactList?.get(index)?.pubKey?.replace(
                            "-----BEGIN PUBLIC KEY-----", ""
                        )?.replace("-----END PUBLIC KEY-----", "")?.replace(
                            "\n", ""
                        ) != contact.pubKey?.replace("-----BEGIN PRIVATE KEY-----", "")
                            ?.replace("-----END PRIVATE KEY-----", "")?.replace("\n", "")
                    ) {
//                        ContactsModel.insert {
//                            it[name] = contact.name!!
//                            it[phoneNumber] = contact.phoneNumber
//                            it[pubKey] = contact.pubKey
//                            it[uuid] = contact.uuid!!
//                            it[isVerified] = true
                        contacts.add(Contact(
                            name = contact.name,
                            phoneNumber = contact.phoneNumber,
                            key = contact.key,
                            countryCode = contact.countryCode,
                            uuid = contact.uuid,
                            pubKey = contact.pubKey,
                            isVerified = contact.isVerified
                        ) as Contact)

                    }

                }


            }
            Log.d("Insert contacts",contacts.size.toString())

            db.contactDao().insertAll(*contacts.toTypedArray())

//        }

        }
    }


  suspend  fun getAllVerifiedContacts(): ArrayList<Contact> {
      val contacts:ArrayList<Contact>
      withContext(Dispatchers.IO) {
          val db = DbConnect.getDatabase()
          contacts=db.contactDao().loadAllVerified()  as ArrayList<Contact>
          Log.d("contact size",contacts.size.toString())

          contacts.map {
              Log.d("contact",it.name.toString())
          }
          db.contactDao().getAll().map {
              Log.d("contact",it.name.toString())
          }
      }
      return  contacts
  }

//        ArrayList(ContactsModel.selectAll().where { ContactsModel.isVerified eq true }.map {
//            Contact(
//                it[id].toString(),
//                it[name],
//                it[phoneNumber],
//                it[image],
//                it[pubKey],
//                it[chatPublickey],
//                it[chatPrivateKey],
//                it[countryCode],
//                it[isVerified],
//                it[uuid],
//                it[createdAt]
//            )
//        })
//    }
}



class ContactChats (
    val contact:Contact,
    val messageCount:Int,
    val latestMessage: Chat?=null
)