package com.fury.messenger.helper.threads

import com.fury.messenger.kafka.ConsumerThread

object Threads {

    var MessageListenerThread: ConsumerThread? = null

    //TODO: Testing is required
//    suspend fun startEventListener() {
//        val scope = CoroutineScope(Dispatchers.Main)
//        withContext(Dispatchers.IO) {
//
//
//            val rabbitMq = RabbitMQ()
//            val channel: Channel?
//
//            val queueName = CurrentUser.phoneNumber
//            if (queueName != null) {
//                channel = rabbitMq.getChannel(queueName)
//
//
//                val consumer = object : DefaultConsumer(channel) {
//
//                    override fun handleDelivery(
//                        consumerTag: String?,
//                        envelope: Envelope?,
//                        properties: AMQP.BasicProperties?,
//                        body: ByteArray?
//                    ) {
//
//                        val message = body?.let {
//                            String(it, charset("UTF-8"))
//                        }
//                        if (message != null) {
//                            val decryptMessage =
//                                Crypto.decryptMessage(message, CurrentUser.getPrivateKey())
////                                val routingKey = envelope!!.routingKey
////                                val contentType = properties!!.contentType
//                            val deliveryTag = envelope?.deliveryTag
//                            val messageBuilder = Message.Event.newBuilder()
//                            JsonFormat.parser().merge(decryptMessage, messageBuilder)
//                            val event = messageBuilder.build()
//
//                            if (deliveryTag != null) {
//                                channel.basicAck(deliveryTag, false)
//                            }
//
//                            if (envelope != null) {
//                                if (!envelope.isRedeliver) {
//                                    val messageObj = event.message.message
//
//                                    //TODO: SQL Triggers
//                                    scope.launch {
//                                        DBMessage.messageThreadHandler(
//                                            event.message
//                                        )
//                                    }
//                                    when (event.type) {
//                                        Message.EventType.MESSAGE -> {
//
//                                            when (event.message.type) {
//                                                Message.MessageType.INSERT -> {
//                                                    val request =
//                                                        Message.MessageRequest.newBuilder()
//                                                            .setType(Message.MessageType.UPDATE)
//                                                    val messageInfo =
//                                                        Message.MessageInfo.newBuilder()
//                                                            .setMessageId(messageObj.messageId)
//                                                            .setSender(messageObj.reciever)
//                                                            .setReciever(messageObj.sender)
//                                                            .setDeliverStatus(true).build()
//                                                    val eventBuild = Message.Event.newBuilder()
//                                                        .setMessage(
//                                                            request.setMessage(messageInfo).build()
//                                                        ).setType(Message.EventType.MESSAGE).build()
//                                                    val client =
//                                                        createAuthenticationStub(CurrentUser.getToken())
//                                                    client.send(eventBuild)
//                                                }
//
//                                                else -> {
//                                                    Log.d(
//                                                        "Messenger logs",
//                                                        "Message type case not handle yet for reply."
//                                                    )
//
//                                                }
//                                            }
//
//                                        }
//
//                                        Message.EventType.HANDSHAKE -> {
//
//                                            scope.launch {
//                                                DBMessage.saveHandshake(
//                                                    event.exchange
//                                                )
//                                            }
//
//                                        }
//
//                                        else -> {
//                                            Log.d(
//                                                "Messenger logs",
//                                                "Event type case not handle yet."
//                                            )
//                                        }
//                                    }
//                                    //                                if(event.type==Message.EventType.MESSAGE ){
//                                    //                                    if(event.message.type==Message.MessageType.INSERT){
//                                    //                                        val request=Message.MessageRequest.newBuilder().setType(Message.MessageType.UPDATE)
//                                    //                                        val messageInfo=Message.MessageInfo.newBuilder().setMessageId(messageObj.messageId).setSender(messageObj.reciever).setReciever(messageObj.sender).setDeliverStatus(true).build()
//                                    //                                        val event= Message.Event.newBuilder().setMessage(request.setMessage(messageInfo).build()).setType(Message.EventType.MESSAGE).build()
//                                    //                                        val client= createAuthenticationStub(CurrentUser.getToken())
//                                    //                                        client.send(event)
//                                    //                                    }
//                                    //
//                                    //                                }
//                                    //
//                                    //                                else if(event.type===Message.EventType.HANDSHAKE){
//                                    //
//                                    //                                    scope.launch{
//                                    //                                        DBMessage.saveHandshake(
//                                    //                                            event.exchange
//                                    //                                        )
//                                    //                                    }
//                                    //                                }
//
//                                }
//                            }
//                        }
//                    }
//                }
//                CurrentUser.getCurrentUserPhoneNumber()?.let { channel.basicConsume(it, consumer) }
//
//            }
//
//        }
//
//    }

}