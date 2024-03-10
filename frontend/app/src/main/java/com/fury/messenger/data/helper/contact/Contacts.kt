package com.fury.messenger.data.helper.contact

import android.annotation.SuppressLint
import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import com.fury.messenger.data.db.DBHelper
import com.fury.messenger.data.db.DBMessage
import com.fury.messenger.data.db.DBUser
import com.fury.messenger.data.helper.mutex.MutexLock
import com.fury.messenger.data.helper.user.CurrentUser

import com.fury.messenger.manageBuilder.ManageChanelBuilder
import com.services.ContactOuterClass
import com.services.ContactOuterClass.ContactsList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext

class Contacts {
    private var classType:Context;
    private  var contactList:ArrayList<Contact>?=null
    private var db:DBHelper

    constructor(classType:Context){
        this.classType=classType
        this.db= DBHelper(this.classType)

    }

    private fun setContactList(contacts:ArrayList<Contact>){
        contactList=contacts

    }
    @SuppressLint("InlinedApi", "Range", "SuspiciousIndentation")
    public suspend  fun listAllContacts(){
        withContext(Dispatchers.IO){
            val contactList: ArrayList<Contact> = ArrayList<Contact>()
            val phones = classType.contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, "display_name ASC")
            if (phones != null && phones.count>0) {
                while(phones.moveToNext()){
                    val id=phones.getString(phones.getColumnIndex(ContactsContract.Contacts._ID))
                    val name=phones.getString(phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val phoneNumber=phones.getString(phones.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)).toInt()
                    val image=phones.getString(phones.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))

                    if(phoneNumber>0){
                        val phoneNumberValue=classType.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            arrayOf(id),null
                        )
                        if( phoneNumberValue!=null &&phoneNumberValue.count>0){
                            while (phoneNumberValue.moveToNext()){
                                val phoneNumValue = phoneNumberValue.getString(phoneNumberValue.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                val hasNumber=contactList.find{
                                        contact -> contact.getPhoneNumber()==phoneNumValue.split(" ").joinToString("")
                                }
                                if(hasNumber==null) {
                                    val contact = Contact(id, name, phoneNumValue.split(" ").joinToString(""), image)
                                    contactList.add(contact)
                                }
                            }
                            phoneNumberValue.close()

                        }

                    }

                }
                phones.close()
            }
            Log.d("list length",contactList.count().toString())
            setContactList(contactList)
        }
    }

    suspend fun validateContacts(){
     withContext(Dispatchers.IO){
         if(contactList!=null){
             val channel=ManageChanelBuilder.channel
             val client=ManageChanelBuilder.client
             val request=ContactsList.newBuilder()

             contactList?.forEachIndexed { index, contact ->
                 run {

                     val countryCode=contact.getCountryCode()
                     if(countryCode!=null){
                         var subRequest =ContactOuterClass.Contact.newBuilder().setId(index.toString())
                             .setPhoneNumber(contact.getPhoneNumber()).setName(contact.getName())
                             .setIsVerified(contact.getIsVerified()).setCountryCode(countryCode).build()
                         request.addContacts( subRequest)

                     }else{
                         var subRequest =ContactOuterClass.Contact.newBuilder().setId(index.toString())
                             .setPhoneNumber(contact.getPhoneNumber()).setName(contact.getName())
                             .setIsVerified(contact.getIsVerified()).build()
                         request.addContacts(subRequest)
                     }
                 }
             }
             val response= client.validateContacts(request.build())



             updateDb(response.contactsList)
         }
     }
    }
    private suspend fun updateDb(validContacts: MutableList<ContactOuterClass.Contact>) {
        val dbHelper=DBHelper(this.classType)
        while(MutexLock.getDbLock()){

        }
        val db=dbHelper.writableDatabase

        MutexLock.setDbLock(true)
        validContacts.forEach { contact ->
            run {
                val index =
                    this.contactList?.indexOfFirst { it.getPhoneNumber() == contact.phoneNumber }
                if (index != null) {
                    this.contactList?.get(index)?.getPubKey()
                        ?.let { Log.d(index?.let { this.contactList?.get(it)?.getName() }, it) }
                }
                if (index != null && this.contactList?.get(index)?.getPubKey()?.replace("-----BEGIN PRIVATE KEY-----", "")
                        ?.replace("-----END PRIVATE KEY-----", "")
                        ?.replace("\n", "")!=contact.pubKey.replace("-----BEGIN PRIVATE KEY-----", "")
                        ?.replace("-----END PRIVATE KEY-----", "")
                        ?.replace("\n", "")) {

                    this.contactList?.get(index)?.setIsVerified(true)


                    this.contactList?.get(index)?.setName(contact.name)
                    this.contactList?.get(index)?.getPhoneNumber()

                    this.contactList?.get(index)?.setPubKey(contact.pubKey.replace("-----BEGIN PRIVATE KEY-----", "")
                        ?.replace("-----END PRIVATE KEY-----", "")
                        ?.replace("\n", ""))
                    this.contactList?.get(index)?.setUUID(contact.uuid)
                   db.execSQL(DBMessage.createTableQuery("table_${contact.phoneNumber}"))

                }



            }
        }
        MutexLock.setDbLock(false)

        runBlocking {
          DBUser.multipleInsert(contactList!!,dbHelper)
          dbHelper.close()

      }



    }
    }


