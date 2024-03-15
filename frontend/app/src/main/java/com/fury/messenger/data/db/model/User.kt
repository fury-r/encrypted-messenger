package com.fury.messenger.data.db.model

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

data class Contact(      val id:String,
                      val name:String?=null,
                      val phoneNumber:String="",
                      val image:String?=null,
                      val pubKey:String?=null,
                      val chatPublickey:String?=null,
                      val chatPrivateKey:String?=null,
                      val countryCode:String?=null,
                      val isVerified:Boolean=false,
                      val uuid:String?=null,
val createdAt:LocalDateTime?=null)
object ContactsModel:IntIdTable() {
    val name=varchar("name",50)
    val countryCode=varchar("countryCode",50).nullable()
    val phoneNumber=varchar("phoneNumber",50)
    val image=varchar("image",999999999).nullable()
    val pubKey=varchar("pubKey",100).nullable()
    val chatPublickey=varchar("chatPubkey",100).nullable()
    val chatPrivateKey=varchar("chatPrivKey",100).nullable()
    val isVerified=bool("isVerified")
    val uuid=varchar("uuid",100)
    val createdAt=datetime("createdAt").defaultExpression(CurrentDateTime)
    val updatedAt=datetime("updatedAt").defaultExpression(CurrentDateTime)
}