package com.fury.messenger.kafka

import android.content.Context
import android.util.Log
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.time.Duration
import java.util.Properties

class Kafka {

    private  lateinit var context:Context
    private lateinit var consumer:Consumer<String,String>;
    private  var polling:Duration=Duration.ofSeconds(1);
    constructor(ctx:Context,topic:Any?, polling:Long?){
        this.context=ctx
        if(topic!=null && polling!=null){
            this.createConsumer()
            this.subScribe(topic)
            this.setPolling(polling)

        }
    }

    private fun createConsumer():Consumer<String,String>{
        val properties=Properties()

        try {
            this.consumer= KafkaConsumer(KafkaConstants.CONSUMER_PROPS)

        }catch (e:Exception){
            Log.d("kafka error",e.message.toString())

        }
        return this.consumer
    }

    fun subScribe(topic:Any): Consumer<String, String> {
        if(topic::class.java==String::class.java){
            this.consumer?.subscribe(listOf(topic) as List<String>)

        }
        else if (List::class.java==topic::class.java){
            this.consumer?.subscribe(topic as List<String>)

        }
        return this.consumer
    }
    fun  setPolling(time:Long){
        this.polling= Duration.ofSeconds(time)
    }

    fun startPolling(): ConsumerRecords<String, String>? {
        return this.consumer.poll(this.polling)
    }


}