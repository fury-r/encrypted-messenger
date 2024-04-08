package com.fury.messenger.messagebroker

import android.util.Log
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


//Not used (logic moved to socket server)
class RabbitMQ {
    var factory = ConnectionFactory()
    private var conn: Connection? = null;
    private var channel: Channel? = null
    private var scope = CoroutineScope(Dispatchers.Main)
    public var isChannelInitialized = false
    public var isConnectionInitialized = false

    constructor() {
        setUp()


    }

    fun getConnection(): Connection {
        return this.conn!!
    }

    private fun setUp() {

        factory.virtualHost = ConfigConstants.VIRTUAL_HOST;
        factory.host = ConfigConstants.HOSTNAME;
        factory.port = ConfigConstants.PORT;
        try{
            this.createConnection()
            if (conn != null) {
                setIsConnectionInitialized(true)
            }
            this.createChannel()
            if (channel != null) {
                setIsChannelInitialized(true)
            }
            isConnectionInitialized=true
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    private fun createChannel() {

        channel = conn?.createChannel()


    }

    private fun createConnection() {

        conn = factory.newConnection()
    }

    fun getChannel(queueName: String): Channel {
        Log.d(
            " Thread-Messenger " + this.isChannelInitialized.toString(),
            "declare channel queue: $queueName"
        )

        this.channel?.queueDeclare(queueName, false, false, false, null)

        return this?.channel!!
    }

    private fun setIsChannelInitialized(value: Boolean) {
        this.isChannelInitialized = value
    }

    private fun setIsConnectionInitialized(value: Boolean) {
        this.isConnectionInitialized = value
    }

}