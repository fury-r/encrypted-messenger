package com.fury.messenger.data.db

import com.services.Message.MessageType

class ChatV1 {
       var id:String

       var sender:String

       var reciever:String

       var message:String

       var messageId:String

       var contentType:String

        var  isDelivered:Boolean

       var isSeen:Boolean
       var type:MessageType



    constructor(
        id: Int,
        sender:String,
        reciever:String,
        messageId:String,
        message:String,
        contentType:String,
        isDelivered:Boolean,
        isSeen:Boolean,
        type: MessageType= MessageType.INSERT){
        this.id= id.toString()
        this.sender=sender
        this.reciever=reciever
        this.messageId=messageId
        this.message=message
        this.isDelivered=isDelivered
        this.isSeen=isSeen
        this.contentType=contentType
        this.type=type

    }

}