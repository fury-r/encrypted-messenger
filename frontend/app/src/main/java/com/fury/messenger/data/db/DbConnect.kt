package com.fury.messenger.data.db
import android.content.Context
import androidx.room.Room
import com.fury.messenger.helper.user.AppDatabase
//import org.jetbrains.exposed.sql.Database

//fun connectToDb(){
//    Database.connect("jdbc:sqlite:main.db", driver = "org.sqlite.JDBC")
//
//}



    object DbConnect {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context?=null): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            if(context==null && INSTANCE==null){
                throw  Error("Db connect not initialized")
            }

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context!!.applicationContext,
                    AppDatabase::class.java,
                    "main.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
