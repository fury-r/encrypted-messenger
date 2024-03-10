package com.fury.messenger.kafka

import org.apache.kafka.common.serialization.StringDeserializer

object KafkaConstants {
    val CONSUMER_PROPS =
        mapOf(
            "bootstrap.servers" to "localhost:9092",
            "auto.offset.reset" to "earliest",
            "key.deserializer" to StringDeserializer::class.java,
            "value.deserializer" to StringDeserializer::class.java,
            "group.id" to "someGroup",
            "security.protocol" to "PLAINTEXT"
        )

}