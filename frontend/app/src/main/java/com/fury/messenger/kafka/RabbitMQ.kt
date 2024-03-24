package com.fury.messenger.kafka

import android.util.Log
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


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
        factory.username = ConfigConstants.USERNAME;
        factory.password = ConfigConstants.PASSWORD;
        factory.virtualHost = ConfigConstants.VIRTUAL_HOST;
        factory.host = ConfigConstants.HOSTNAME;
        factory.port = ConfigConstants.PORT;
        this.createConnection()
        if (conn != null) {
            setIsConnectionInitialized(true)
        }
        this.createChannel()
        if (channel != null) {
            setIsChannelInitialized(true)
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