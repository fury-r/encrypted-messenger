package com.fury.messenger.data.db


import android.annotation.SuppressLint
import android.content.Context
import android.provider.BaseColumns
import com.fury.messenger.data.db.model.Contact
import com.fury.messenger.helper.contact.ContactChats

//import com.fury.messenger.data.db.model.ContactsModel
//import org.jetbrains.exposed.sql.selectAll

object DBUser {

    const val SQL_CREATE_QUERY=
        "CREATE TABLE ${TableInfo.TABLE_NAME} ("+
                "${BaseColumns._ID} INTEGER   PRIMARY KEY AUTOINCREMENT  ,"+

                "${TableInfo.COLUMN_ITEM_NUMBER} VARCHAR ,"+
                "${TableInfo.COLUMN_ITEM_COUNTRY_CODE} VARCHAR ,"+
                "${TableInfo.COLUMN_ITEM_NAME} VARCHAR ,"+
                "${TableInfo.COLUMN_ITEM_BIO} VARCHAR ,"+
                "${TableInfo.COLUMN_ITEM_PROFILE_PIC} VARCHAR ,"+
                "${TableInfo.COLUMN_ITEM_PUB_KEY} VARCHAR ,"+
                "${TableInfo.COLUMN_ITEM_IS_VERIFIED} BOOLEAN ," +
                "updated_at TIMESTAMP  DEFAULT CURRENT_TIMESTAMP )"

    const val SQL_DELETE_QUERY="DROP TABLE IF EXISTS ${TableInfo.TABLE_NAME}"
    object   TableInfo:BaseColumns{
        const val TABLE_NAME="Contact"
        const val COLUMN_ITEM_NUMBER="number"
        const val COLUMN_ITEM_COUNTRY_CODE="country_code"
        const val  COLUMN_ITEM_NAME="Contact"
        const val COLUMN_ITEM_BIO="bio"
        const val COLUMN_ITEM_PUB_KEY="pub_key"
        const val COLUMN_ITEM_PROFILE_PIC="profile_pic"
        const val COLUMN_ITEM_IS_VERIFIED="is_verified"


    }

    @SuppressLint("Range")
    fun testData(dbHelper: DBHelper) {

    }
    @SuppressLint("Range")
//   suspend  fun getUserWithPhoneNumber(phoneNumber: String): Contact {
//            val contact=      ContactsModel.selectAll().where{ContactsModel.phoneNumber eq phoneNumber}.limit(0).map {
//                Contact(
//                    it[ContactsModel.id].toString(),
//                    it[ContactsModel.name],
//                    it[ContactsModel.phoneNumber],
//                    it[ContactsModel.image],
//                    it[ContactsModel.pubKey],
//                    it[ContactsModel.chatPublickey],
//                    it[ContactsModel.chatPrivateKey],
//                    it[ContactsModel.countryCode],
//                    it[ContactsModel.isVerified],
//                    it[ContactsModel.uuid]
//
//                )
//            }
//        return contact[0]
//    }
    fun getDataByPubKey( pubKey:String):Contact?{
        val contact=DbConnect.getDatabase().contactDao().findByPublicKey(pubKey)

        return contact as Contact
    }
    @SuppressLint("Range")
    suspend fun  getDataByPhoneNumber( phoneNumber:String): Contact {

    return DbConnect.getDatabase().contactDao().findByNumber(phoneNumber) as Contact
    }

    suspend fun  getPublicKeyForUser(number:String ): String? {
        var chatPublicKey: String? =null

        return  getDataByPhoneNumber(number).pubKey
    }

    fun getAllLastMessagesForContact(ctx:Context,data:ArrayList<Contact>): ArrayList<ContactChats> {
        val db=DbConnect.getDatabase(ctx)
        return data.map{
            val count=db.chatsDao().findByReceiversUnreadMessage(it.phoneNumber)
            val message=db.chatsDao().findLastMessageByReciever(it.phoneNumber)

            return@map  ContactChats(it,count,message)
        }  as ArrayList<ContactChats>

    }
}
