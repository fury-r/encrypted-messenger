package com.fury.messenger.helper.socket

import android.util.Log
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.kafka.ConfigConstants
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.Polling
import io.socket.engineio.client.transports.WebSocket

object SocketHandler {
    private lateinit var mSocket:Socket


    @Synchronized
    fun setSocket(): Socket? {
        try{
            val options = IO.Options()
            options.transports = arrayOf(Polling.NAME, WebSocket.NAME)
            options.extraHeaders = mutableMapOf("authorization" to listOf( CurrentUser.getToken()) )
            mSocket= IO.socket(ConfigConstants.SOCKET_IP,options)
            establishConnection()
            return mSocket
        }catch (e:Exception){
            Log.e("Socket Init FAILED",e.toString())
        }
        return null
    }

    @Synchronized
    fun getSocket(): Socket {
        return this.mSocket
    }
    @Synchronized
    fun establishConnection() {
        mSocket.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }
}