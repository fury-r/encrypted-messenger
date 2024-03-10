package com.fury.messenger.data.db


import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.provider.ContactsContract
import android.util.Log
import com.fury.messenger.data.helper.contact.Contact
import com.fury.messenger.data.helper.mutex.MutexLock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

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
        const val TABLE_NAME="User"
        const val COLUMN_ITEM_NUMBER="number"
        const val COLUMN_ITEM_COUNTRY_CODE="country_code"
        const val  COLUMN_ITEM_NAME="name"
        const val COLUMN_ITEM_BIO="bio"
        const val COLUMN_ITEM_PUB_KEY="pub_key"
        const val COLUMN_ITEM_PROFILE_PIC="profile_pic"
        const val COLUMN_ITEM_IS_VERIFIED="is_verified"


    }
    private fun insertData(
        COLUMN_ITEM_NUMBER:String,COLUMN_ITEM_NAME:String?="",COLUMN_ITEM_BIO:String?="",
        COLUMN_ITEM_PROFILE_PIC:String?="",COLUMN_ITEM_IS_VERIFIED:Boolean?=false
    ):String{
        return "("+
                "${COLUMN_ITEM_NUMBER.toString()?:" " },"+
                "${COLUMN_ITEM_NAME.toString()?:"z "},"+
                "${COLUMN_ITEM_BIO.toString()?:" z"},"+
                "${COLUMN_ITEM_PROFILE_PIC.toString()?:" z"},"+
                "$COLUMN_ITEM_IS_VERIFIED )"

    }

    @SuppressLint("Range")
 suspend   fun multipleInsert(data:ArrayList<Contact>, dbHelper: SQLiteOpenHelper) {
    withContext(Dispatchers.IO){
        while (MutexLock.getDbLock()){
          delay(1000)
        }
        MutexLock.setDbLock(true)
        val db=dbHelper.writableDatabase
        db.beginTransaction()
        try{
            val columns = arrayOf<String>(TableInfo.COLUMN_ITEM_NUMBER)
            val selection: String = TableInfo.COLUMN_ITEM_NUMBER + " =?"
            val limit = "1"
            for( contact in data.filter { contact->
                contact.getPhoneNumber()!=null
            }){
                val selectionArgs = arrayOf<String>(contact.getPhoneNumber())

                val contentValues=ContentValues()
                contentValues.put(TableInfo.COLUMN_ITEM_NUMBER,contact.getPhoneNumber())
                contentValues.put(TableInfo.COLUMN_ITEM_COUNTRY_CODE,contact.getCountryCode().toString()?:"")


                contentValues.put(TableInfo.COLUMN_ITEM_BIO,"")
                contentValues.put(TableInfo.COLUMN_ITEM_PROFILE_PIC,contact.getImage().toString()?:"")
                contentValues.put(TableInfo.COLUMN_ITEM_PUB_KEY,contact.getPubKey()?:"")

                contentValues.put(TableInfo.COLUMN_ITEM_IS_VERIFIED,contact.getIsVerified()
                )
                val updatedRows = db.update(
                    TableInfo.TABLE_NAME,
                    contentValues,
                    selection,
                    selectionArgs
                )

                if (updatedRows == 0) {
                    // No rows updated, perform an insert
                    contentValues.put(TableInfo.COLUMN_ITEM_NAME, contact.getName())
                    db.insert(TableInfo.TABLE_NAME, null, contentValues)
                }


            }
            db.setTransactionSuccessful()
        }catch (e:Exception){
            e.printStackTrace()

        }

        finally {
            db.endTransaction()
            MutexLock.setDbLock(false)

        }
        db.close()

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
   suspend  fun getData(dbHelper: SQLiteOpenHelper):ArrayList<Contact>{
        val contactList: ArrayList<Contact> = arrayListOf<Contact>()
        while (MutexLock.getDbLock()){
            delay(1000)
        }
        MutexLock.setDbLock(true)
        val db=dbHelper.readableDatabase

        val  query="Select * FROM ${TableInfo.TABLE_NAME}"
        val result=db.rawQuery(query,null)
        if(result.moveToFirst()){
            do{
                val contact=Contact(result.getString(result.getColumnIndex(BaseColumns._ID)),result.getString(result.getColumnIndex(TableInfo.COLUMN_ITEM_NAME)),result.getString(result.getColumnIndex(TableInfo.COLUMN_ITEM_NUMBER)),"")
                contact.setCountryCode(result.getString(result.getColumnIndex(TableInfo.COLUMN_ITEM_COUNTRY_CODE)))
                contact.setIsVerified(result.getString(result.getColumnIndex(TableInfo.COLUMN_ITEM_IS_VERIFIED))=="1")
                contactList.add(contact)
            }while (result.moveToNext())
        }
        result.close()
        db.close()
        MutexLock.setDbLock(false)
        return  contactList
    }
    @SuppressLint("Range")
    fun getDataByPubKey(dbHelper: SQLiteOpenHelper, pubKey:String):Contact?{
        val db=dbHelper.readableDatabase

        val cursor=db.query(TableInfo.TABLE_NAME, arrayOf(TableInfo.COLUMN_ITEM_PUB_KEY), TableInfo.COLUMN_ITEM_PUB_KEY+"=?",
            arrayOf(pubKey),null,null,null,"1"
        )

        if(cursor.count>0){
            while (cursor.moveToNext()){
                val contact=Contact(cursor.getString(cursor.getColumnIndex(BaseColumns._ID)),cursor.getString(cursor.getColumnIndex(TableInfo.COLUMN_ITEM_NAME)),cursor.getString(cursor.getColumnIndex(TableInfo.COLUMN_ITEM_NUMBER)),"")
                contact.setCountryCode(cursor.getString(cursor.getColumnIndex(TableInfo.COLUMN_ITEM_COUNTRY_CODE)))
                contact.setIsVerified(cursor.getString(cursor.getColumnIndex(TableInfo.COLUMN_ITEM_IS_VERIFIED)).toBoolean())
                return contact
            }
        }
        cursor.close()

        db.close()

    return null
    }
    @SuppressLint("Range")
    suspend fun  getDataByPhoneNumber(dbHelper: SQLiteOpenHelper, phoneNumber:String):Contact?{

        val db=dbHelper.readableDatabase
        val selectionArgs = arrayOf<String>(phoneNumber)

        val columns = arrayOf<String>(TableInfo.COLUMN_ITEM_NUMBER)
        val selection: String = TableInfo.COLUMN_ITEM_NUMBER + " =?"
        val  query="Select * FROM ${TableInfo.TABLE_NAME} where ${TableInfo.COLUMN_ITEM_NUMBER}=${phoneNumber}"
        val cursor=db.rawQuery(query,null)
//        val cursor:Cursor =  db.query(TableInfo.TABLE_NAME, columns, selection, selectionArgs, null, null, null, "1");

        Log.d("found",cursor.count.toString())

        if(cursor.count>0){
            while (cursor.moveToNext()){


                Log.d("IndexedDB",cursor.getString(cursor.getColumnIndex(TableInfo.COLUMN_ITEM_PUB_KEY)))
                val contact=Contact(cursor.getString(cursor.getColumnIndex(BaseColumns._ID)),cursor.getString(cursor.getColumnIndex(TableInfo.COLUMN_ITEM_NAME)),cursor.getString(cursor.getColumnIndex(TableInfo.COLUMN_ITEM_NUMBER)),"",cursor.getString(cursor.getColumnIndex(TableInfo.COLUMN_ITEM_PUB_KEY)))
                contact.setCountryCode(cursor.getString(cursor.getColumnIndex(TableInfo.COLUMN_ITEM_COUNTRY_CODE)))
                contact.setIsVerified(cursor.getString(cursor.getColumnIndex(TableInfo.COLUMN_ITEM_IS_VERIFIED)).toBoolean())
                return contact
            }
        }
        cursor.close()
        db.close()

        return null
    }
}
