package com.fury.messenger.messages

import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Message {
    var message:String?=null
    var senderId:String?=null
    var datetime: Long? =null


    constructor(message:String?,senderId:String?,datetime: Long?){
        this.message=message
        this.senderId=senderId
        this.datetime=datetime
    }
    constructor(){}
}