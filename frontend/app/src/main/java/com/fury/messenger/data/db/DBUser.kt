package com.fury.messenger.data.db


import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import com.fury.messenger.data.db.model.Contact
import com.fury.messenger.data.db.model.ContactsModel
import com.fury.messenger.data.helper.mutex.MutexLock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

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
 suspend   fun multipleInsert(data:ArrayList<Contact>, dbHelper: SQLiteOpenHelper) {
    withContext(Dispatchers.IO){
        while (MutexLock.getDbLock()){
          delay(1000)
        }
        MutexLock.setDbLock(true)
        val db=dbHelper.writableDatabase

        transaction {
            for( contact in data.filter {
                true
            }){
                ContactsModel.insert {

                    it[name]=contact.name!!
                    it[phoneNumber]=contact.phoneNumber
                    it[pubKey]= contact.pubKey
                    it[uuid]=contact.uuid!!
                    it[countryCode]=contact.countryCode
                    it[chatPrivateKey]=contact.chatPrivateKey
                    it[chatPublickey]=contact.chatPublickey
                    it[image]=contact.image
                    it[isVerified]=true
                }
            }
        }


    }
    }
    fun testData(dbHelper: DBHelper) {
        val contentValue=ContentValues()
        val db=dbHelper.writableDatabase
        Log.d("add","data")
        contentValue.put(TableInfo.COLUMN_ITEM_NUMBER," " )
        contentValue.put(TableInfo.COLUMN_ITEM_NAME,"")
        contentValue.put(TableInfo.COLUMN_ITEM_BIO,"")
        contentValue.put(TableInfo.COLUMN_ITEM_PROFILE_PIC,"")
        contentValue.put(TableInfo.COLUMN_ITEM_IS_VERIFIED,"")

        db.insert(TableInfo.TABLE_NAME,null,contentValue)
        db.close()
    }
    @SuppressLint("Range")
   suspend  fun getUserWithPhoneNumber(phoneNumber: String): Contact {
            val contact=ContactsModel.selectAll().where{ContactsModel.phoneNumber eq phoneNumber}.limit(0).map {
                Contact(
                    it[ContactsModel.id].toString(),
                    it[ContactsModel.name],
                    it[ContactsModel.phoneNumber],
                    it[ContactsModel.image],
                    it[ContactsModel.pubKey],
                    it[ContactsModel.chatPublickey],
                    it[ContactsModel.chatPrivateKey],
                    it[ContactsModel.countryCode],
                    it[ContactsModel.isVerified],
                    it[ContactsModel.uuid]

                )
            }
        return contact[0]
    }
    @SuppressLint("Range")
    fun getDataByPubKey( pubKey:String):Contact?{
        val contact=ContactsModel.selectAll().where{ContactsModel.pubKey eq pubKey}.limit(0).map {
            Contact(
                it[ContactsModel.id].toString(),
                it[ContactsModel.name],
                it[ContactsModel.phoneNumber],
                it[ContactsModel.image],
                it[ContactsModel.pubKey],
                it[ContactsModel.chatPublickey],
                it[ContactsModel.chatPrivateKey],
                it[ContactsModel.countryCode],
                it[ContactsModel.isVerified],
                it[ContactsModel.uuid]

            )
        }
        return contact[0]
    }
    @SuppressLint("Range")
    suspend fun  getDataByPhoneNumber(dbHelper: SQLiteOpenHelper, phoneNumber:String): Contact {

    return ContactsModel.selectAll().where{ContactsModel.phoneNumber eq phoneNumber}.limit(1).map { it }[0] as Contact
    }

    suspend fun  getPublicKeyForUser(number:String ): String? {
        var chatPublicKey: String? =null
         ContactsModel.selectAll().where{ ContactsModel.phoneNumber eq number }.map {
            chatPublicKey= it[ContactsModel.chatPublickey]
        }
        return  chatPublicKey
    }
}
