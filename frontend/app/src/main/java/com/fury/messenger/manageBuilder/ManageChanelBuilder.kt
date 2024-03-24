package com.fury.messenger.manageBuilder

import com.fury.messenger.kafka.ConfigConstants
import com.services.ServicesGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata
import io.grpc.stub.MetadataUtils

object ManageChanelBuilder {
    var channel: ManagedChannel =
        ManagedChannelBuilder.forAddress(ConfigConstants.HOSTNAME, 8081).usePlaintext().build()
    val headers = Metadata()
    var client: ServicesGrpc.ServicesBlockingStub = ServicesGrpc.newBlockingStub(channel);


}


fun createAuthenticationStub(token: String?): ServicesGrpc.ServicesBlockingStub {
    if (token == null) {
        return ManageChanelBuilder.client
    }
    val headers = Metadata()

    headers.put(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER), "Bearer $token")

    return MetadataUtils.attachHeaders(ManageChanelBuilder.client, headers);
}