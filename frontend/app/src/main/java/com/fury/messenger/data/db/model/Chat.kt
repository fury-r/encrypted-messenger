package com.fury.messenger.data.db.model

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



//
@Entity(tableName = "chats",indices = [Index(value = ["messageId"], unique = true)])
data class Chat(
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

){
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null
}

@Dao
interface ChatsDao {
    @Query("SELECT * FROM chats")
    fun getAll(): List<Chat>
    @Query("SELECT COUNT(*) FROM chats")
    fun count(): Int
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
    @Query("SELECT * FROM chats WHERE receiver = :number  or sender=:number  Order by createdAt DESC")
    fun loadChatsByNumber(number: String): List<Chat>

    @Query("SELECT COUNT(*) FROM chats WHERE receiver = :number  or sender=:number  Order by createdAt")
    fun countChatsByNumber(number: String): Int
    @Insert
    fun insertAll(vararg contact: Chat)

    @Update
    fun update(chat: Chat)
    @Query("Update chats set isSeen=true where isSeen=false")
    fun markAsRead()

    @Delete
    fun delete(cha: Chat)
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