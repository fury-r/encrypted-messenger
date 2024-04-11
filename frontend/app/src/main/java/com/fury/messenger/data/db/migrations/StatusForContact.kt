package com.fury.messenger.data.db.migrations
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val ADD_STATUS_CONTACTS: Migration = object : Migration(1, 2) {


    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER Table contacts ADD COLUMN status VARCHAR NULL")
    }
}