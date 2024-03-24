package com.fury.messenger.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import java.time.OffsetDateTime

//data class Contact(
//    val id: String,
//    val name: String? = null,
//    val phoneNumber: String = "",
//    val image: String? = null,
//    val pubKey: String? = null,
//    val chatPublickey: String? = null,
//    val chatPrivateKey: String? = null,
//    val countryCode: String? = null,
//    val isVerified: Boolean = false,
//    val uuid: String? = null,
//    val createdAt: LocalDateTime? = null
//)

//object ContactsModel : IntIdTable() {
//    val name = varchar("name", 50)
//    val countryCode = varchar("countryCode", 50).nullable()
//    val phoneNumber = varchar("phoneNumber", 50)
//    val image = varchar("image", 999999999).nullable()
//    val pubKey = varchar("pubKey", 100).nullable()
//    val chatPublickey = varchar("chatPubkey", 100).nullable()
//    val chatPrivateKey = varchar("chatPrivKey", 100).nullable()
//    val isVerified = bool("isVerified")
//    val uuid = varchar("uuid", 100)
//    val createdAt = datetime("createdAt").defaultExpression(CurrentDateTime)
//    val updatedAt = datetime("updatedAt").defaultExpression(CurrentDateTime)
//}


@Entity(tableName = "contacts")
data class  Contact   (
    @PrimaryKey(autoGenerate = true) var id: Int=1,
    @ColumnInfo(name="name") var name: String? = null,
    @ColumnInfo(name="phoneNumber") var phoneNumber: String = "",
    @ColumnInfo(name="image") var image: String? = null,
    @ColumnInfo(name="pubKey") var pubKey: String? = null,
    @ColumnInfo(name="key") var key: String? = null,
    @ColumnInfo(name="countryCode") var countryCode: String? = null,
    @ColumnInfo(name="isVerified") var isVerified: Boolean = false,
    @ColumnInfo(name="uuid") var uuid: String? = null,
    @ColumnInfo(name="typeTime") var typeTime: OffsetDateTime? = null,
    @ColumnInfo(name="createdAt") var createdAt: OffsetDateTime?=null,
    @ColumnInfo(name="updatedAt") var updatedAt: OffsetDateTime?=null
    )

@Dao
interface  ContactsDao{
    @Query("SELECT * FROM contacts")
    fun getAll(): List<Contact>

    @Query("SELECT * FROM contacts WHERE id IN (:contactsIds)")
    fun loadAllByIds(contactsIds: IntArray): List<Contact>
    @Query("SELECT * FROM contacts WHERE isVerified=1")
    fun loadAllVerified(): List<Contact>

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
    @Delete
    fun delete(contact: Contact)
}

