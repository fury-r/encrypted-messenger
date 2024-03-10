package com.fury.messenger.kafka
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.rabbitmq.client.AMQP
import org.apache.kafka.clients.consumer.ConsumerRecords
import com.rabbitmq.client.Channel
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope

class ConsumerThread(
    private val threadName: String,
    private  val channel:Channel,
                       private  val handlerFunc:(records: Any,dbHelper: SQLiteOpenHelper)->Any,
                       private   val dbHelper: SQLiteOpenHelper

                      ):Thread(threadName) {




    override fun run() {

        Log.d("THREAD","starting listener in thread")
        val consumer=object :DefaultConsumer(channel){
            override fun handleDelivery(
                consumerTag: String?,
                envelope: Envelope?,
                properties: AMQP.BasicProperties?,
                body: ByteArray?
            ) {
               val message=body?.let{
                   String(it, charset("UTF-8"))
               }
                if (message != null) {
                    handlerFunc(message,dbHelper)
                }
            }
        }
    try{

    }catch (e:Exception){
        e.message?.let { Log.d("RabbitMq Error", it) }
    }

    }

}