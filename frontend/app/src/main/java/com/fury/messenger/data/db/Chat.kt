package com.fury.messenger.data.db

import com.services.ContactOuterClass.Contact
import com.services.Message
import com.services.Message.EventType
import com.services.Message.MessageType
import org.apache.kafka.common.protocol.types.Field.Bool

class Chat {
       var id:String

       var sender:String

       var reciever:String

       var message:String

       var messageId:String

       var contentType:String

        var  isDelivered:Boolean

       var isSeen:Boolean
       var type:MessageType



    constructor(id:String,sender:String,reciever:String,messageId:String,message:String,contentType:String,isDelivered:Boolean,isSeen:Boolean,type: Message.MessageType=Message.MessageType.INSERT){
        this.id    =id
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