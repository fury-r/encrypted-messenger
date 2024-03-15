package com.fury.messenger.data.helper.contact

import android.annotation.SuppressLint
import android.content.Context
import android.provider.ContactsContract
import android.util.Log

import com.fury.messenger.data.db.model.Contact
import com.fury.messenger.data.db.model.ContactsModel
import com.fury.messenger.data.db.model.ContactsModel.chatPrivateKey
import com.fury.messenger.data.db.model.ContactsModel.chatPublickey
import com.fury.messenger.data.db.model.ContactsModel.countryCode
import com.fury.messenger.data.db.model.ContactsModel.createdAt
import com.fury.messenger.data.db.model.ContactsModel.id
import com.fury.messenger.data.db.model.ContactsModel.image
import com.fury.messenger.data.db.model.ContactsModel.isVerified
import com.fury.messenger.data.db.model.ContactsModel.name
import com.fury.messenger.data.db.model.ContactsModel.phoneNumber
import com.fury.messenger.data.db.model.ContactsModel.pubKey
import com.fury.messenger.data.db.model.ContactsModel.uuid
import com.fury.messenger.data.helper.mutex.MutexLock

import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class Contacts (private var classType: Context){
    private  var contactList:ArrayList<Contact>?=null

    private fun setContactList(contacts:ArrayList<Contact>){
        contactList=contacts

    }
    @SuppressLint("InlinedApi", "Range", "SuspiciousIndentation")
    suspend  fun listAllContacts(): ArrayList<Contact> {
       return withContext(Dispatchers.IO) {

            val users = ContactsModel.selectAll().where { isVerified eq true }.map {
                Contact(
                    it[id].toString(),
                    it[name],
                    it[phoneNumber],
                    it[image],
                    it[pubKey],
                    it[chatPublickey],
                    it[chatPrivateKey],
                    it[countryCode],
                    it[isVerified],
                    it[uuid],
                    it[createdAt]
                )
            }
            setContactList(ArrayList(users))
            return@withContext ArrayList(users)
        }

    }
    @SuppressLint("Range")
    suspend fun validateContacts(contacts:ArrayList<Contact>?){

        withContext(Dispatchers.IO){
            val contactList: ArrayList<Contact>? = if(contacts!=null) this@Contacts.contactList else contacts
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
                                val phoneNumValue = phoneNumberValue.getString(phoneNumberValue.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER))
                                val hasNumber=contactList?.find{
                                        contact -> contact.phoneNumber==phoneNumValue.split(" ").joinToString("")
                                }
                                if(hasNumber==null) {
                                    val contact = Contact(id, name, phoneNumValue.split(" ").joinToString(""), image)
                                    contactList?.add(contact)
                                }
                            }
                            phoneNumberValue.close()

                        }

                    }

                }
                phones.close()
            }

            if (contactList != null) {
                setContactList(contactList)
                updateDb(contactList)
                Log.d("list length",contactList.count().toString())

            }
        }
    }
    private fun updateDb(validContacts: ArrayList<Contact>) {
        while(MutexLock.getDbLock()){

        }

        MutexLock.setDbLock(true)
        transaction {
            SchemaUtils.create(ContactsModel)

            validContacts.forEach { contact ->
                run {
                    val index =
                        this@Contacts.contactList?.indexOfFirst { it.phoneNumber == contact.phoneNumber }
                    if (index != null) {
                        this@Contacts.contactList?.get(index)?.pubKey
                            ?.let { it -> Log.d(index.let {  this@Contacts.contactList?.get(it)?.name }, it) }
                    }
                    if (index != null &&  this@Contacts.contactList?.get(index)?.pubKey?.replace("-----BEGIN PUBLIC KEY-----", "")
                            ?.replace("-----END PUBLIC KEY-----", "")
                            ?.replace("\n", "")!= contact.pubKey?.replace("-----BEGIN PRIVATE KEY-----", "")
                            ?.replace("-----END PRIVATE KEY-----", "")
                                ?.replace("\n", "")
                    ) {
                        ContactsModel.insert {
                            it[name]=contact.name!!
                            it[phoneNumber]=contact.phoneNumber
                            it[pubKey]= contact.pubKey
                            it[uuid]=contact.uuid!!
                            it[isVerified]=true
                        }

                    }



                }
            }

        }



    }


    fun getAllVerifiedContacts():ArrayList<Contact>{

        return  ArrayList(ContactsModel.selectAll().where{ContactsModel.isVerified eq true}.map {
            Contact(it[id].toString(),it[name],it[phoneNumber],it[image], it[pubKey],it[chatPublickey],it[chatPrivateKey],it[countryCode],it[isVerified],
                it[uuid],it[createdAt]
            )
        })
    }
    }


