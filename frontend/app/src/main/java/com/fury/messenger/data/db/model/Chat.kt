package com.fury.messenger.data.db.model

import com.services.Message
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

data class Chat(
    val id:Int,
    val sender:String,
    val receiver:String,
    val message:String,
    val messageId:String,
    val contentType:String,
    val  isDelivered:Boolean,
    var isSeen:Boolean,
    val type: Message.MessageType,
    val createdAt:LocalDateTime?

    )

object Chats :IntIdTable("Chats") {
    val sender = varchar("sender", 50)
    val receiver = varchar("receiver", 50)
    val receiver_id = integer("receiver_id").references(ContactsModel.id)
    val messageId = varchar("messageId", 50).uniqueIndex()
    val message = varchar("message", 999999999)
    val type = varchar("type", 100)
    val contentType = varchar("contentType", 100)
    val isDelivered = bool("isDelivered")
    val isSeen = bool("isSeen")
    val createdAt=datetime("createdAt").defaultExpression(CurrentDateTime)
    val updatedAt=datetime("updatedAt").defaultExpression(CurrentDateTime)
    init {
        index(true, messageId)
    }
}