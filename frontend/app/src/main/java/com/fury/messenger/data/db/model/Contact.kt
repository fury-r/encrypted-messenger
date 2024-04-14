package com.fury.messenger.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import java.time.OffsetDateTime


@Entity(tableName = "contacts",indices = [Index(value = ["phoneNumber"], unique = true)])
data class  Contact   (
    @PrimaryKey(autoGenerate = true) var id: Int=1,
    @ColumnInfo(name="name") var name: String? = null,
    @ColumnInfo(name="phoneNumber") var phoneNumber: String = "",
    @ColumnInfo(name="image") var image: String? = null,
    @ColumnInfo(name="status") var status: String? = null,
    @ColumnInfo(name="pubKey") var pubKey: String? = null,
    @ColumnInfo(name="key") var key: String? = null,
    @ColumnInfo(name="countryCode") var countryCode: String? = null,
    @ColumnInfo(name="isVerified") var isVerified: Boolean = false,
    @ColumnInfo(name="uuid") var uuid: String? = null,
    @ColumnInfo(name="typeTime") var typeTime: OffsetDateTime? = null,
    @ColumnInfo(name="isPinned") var isPinned: Boolean? = null,
    @ColumnInfo(name="createdAt") var createdAt: OffsetDateTime?=null,
    @ColumnInfo(name="updatedAt") var updatedAt: OffsetDateTime?=null
    )

@Dao
interface  ContactsDao{
    @Query("SELECT * FROM contacts")
    fun getAll(): List<Contact>

    @Query("SELECT * FROM contacts WHERE id IN (:contactsIds)")
    fun loadAllByIds(contactsIds: IntArray): List<Contact>
    @Query("SELECT * FROM contacts WHERE isVerified= :verified")
    fun loadAllVerified(verified:Int=1): List<Contact>

    @Query("SELECT * FROM contacts WHERE phoneNumber LIKE :number  " +
            "LIMIT 1")
    fun findByNumber(number: String): Contact
    @Query("SELECT * FROM contacts WHERE pubKey LIKE :publicKey  " +
            "LIMIT 1")
    fun findByPublicKey(publicKey: String): Contact
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg contact: Contact)
    @Update
    fun update( contact: Contact)
    @Update(onConflict=OnConflictStrategy.REPLACE)
    fun updateAll( vararg contact: Contact)
    @Delete
    fun delete(contact: Contact)
}

