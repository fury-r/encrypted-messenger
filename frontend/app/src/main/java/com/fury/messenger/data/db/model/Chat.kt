package com.fury.messenger.data.db.model

//import org.jetbrains.exposed.dao.id.IntIdTable
//import org.jetbrains.exposed.sql.javatime.CurrentDateTime
//import org.jetbrains.exposed.sql.javatime.datetime
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import java.time.OffsetDateTime

//data class Chat(
//    val id: Int,
//    val sender: String,
//    val receiver: String,
//    val message: String,
//    val messageId: String,
//    val contentType: String,
//    val isDelivered: Boolean,
//    var isSeen: Boolean,
//    val type: Message.MessageType,
//    val createdAt: LocalDateTime?
//
//)

//object Chats : IntIdTable("Chats") {
//    val sender = varchar("sender", 50)
//    val receiver = varchar("receiver", 50)
//    val receiver_id = integer("receiver_id").references(ContactsModel.id)
//    val messageId = varchar("messageId", 50).uniqueIndex()
//    val message = varchar("message", 999999999)
//    val type = varchar("type", 100)
//    val contentType = varchar("contentType", 100)
//    val isDelivered = bool("isDelivered")
//    val isSeen = bool("isSeen")
//    val createdAt = datetime("createdAt").defaultExpression(CurrentDateTime)
//    val updatedAt = datetime("updatedAt").defaultExpression(CurrentDateTime)
//
//    init {
//        index(true, messageId)
//    }
//}

@Entity(tableName = "chats", indices = [Index(value = ["messageId"], unique = true)])
data class Chat(
    @PrimaryKey(autoGenerate = true) var id: Int = 1,
    @ColumnInfo(name = "sender") var sender: String,
    @ColumnInfo(name = "receiver") var receiver: String,
    @ColumnInfo(name = "message") var message: String,
    @ColumnInfo(name = "messageId") var messageId: String,
    @ColumnInfo(name = "contentType") var contentType: String,
    @ColumnInfo(name = "isDelivered") var isDelivered: Boolean,
    @ColumnInfo(name = "isSeen") var isSeen: Boolean,
    @ColumnInfo(name = "type") var type: String? = null,
    @ColumnInfo(name = "createdAt") var createdAt: OffsetDateTime? = null,
    @ColumnInfo(name = "updatedAt") var updatedAt: OffsetDateTime? = null

)

@Dao
interface ChatsDao {
    @Query("SELECT * FROM chats")
    fun getAll(): List<Chat>

    @Query("SELECT * FROM chats WHERE id IN (:chatIds)")
    fun loadAllByIds(chatIds: IntArray): List<Chat>

    @Query(
        "SELECT * FROM chats WHERE receiver LIKE :receiver  " +
                "LIMIT 1"
    )
    fun findByReceiver(receiver: String): Chat

    @Query("SELECT COUNT(id) FROM chats WHERE receiver LIKE :receiver and isSeen!=1 and sender=:receiver  Order by createdAt  ")
    fun findByReceiversUnreadMessage(receiver: String): Int
    @Query("SELECT * FROM chats WHERE receiver LIKE :receiver or  sender LIKE :receiver Order by createdAt  DESC Limit 1")
    fun findLastMessageByReciever(receiver: String): Chat
    @Query("SELECT * FROM chats WHERE receiver = :number  or sender=:number  Order by createdAt")
    fun loadChatsByNumber(number: String): List<Chat>


    @Insert
    fun insertAll(vararg contact: Chat)

    @Update
    fun update(chat: Chat)

    @Delete
    fun delete(contact: Chat)
    @Query("Delete from  chats where receiver= :number or sender= :number")
    fun deleteAllChats(number: String)
    @Query(" Update chats set isDelivered= :isDelivered , isSeen= :isSeen where messageId= :messageId  or receiver= :receiver")
    fun markAllAsReadAndDelivered(
        isDelivered: Boolean,
        isSeen: Boolean,
        messageId: String?,
        receiver: String? = null
    )
}