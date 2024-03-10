package com.fury.messenger.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context):SQLiteOpenHelper(context,"messenger.db",null,1) {
   private var db:SQLiteDatabase?=null
    override fun onCreate(db: SQLiteDatabase) {
        this.db=db
        db.execSQL(DBUser.SQL_CREATE_QUERY)
        db.execSQL(DBMessage.SQL_CREATE_QUERY)

        //TODO: Triggers for Update data
//        db.execSQL("CREATE TRIGGER update_timestamp\n" +
//                "AFTER UPDATE ON ${DBUser.TableInfo.TABLE_NAME}\n" +
//                "FOR EACH ROW\n" +
//                "BEGIN\n" +
//                "    UPDATE ${DBUser.TableInfo}\n" +
//                "    SET updated_at = CURRENT_TIMESTAMP\n" +
//                "    WHERE id = NEW.id;\n" +
//                "END")
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
      ///  db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "chat .db"
    }

    fun executeQuery(query:String): Unit? {
        return db?.execSQL(query)
    }
}

