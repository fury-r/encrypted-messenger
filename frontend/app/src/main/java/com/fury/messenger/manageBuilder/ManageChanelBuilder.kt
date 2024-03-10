package com.fury.messenger.manageBuilder

import com.services.ServicesGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder

object ManageChanelBuilder {

      var channel:ManagedChannel= ManagedChannelBuilder.forAddress("192.168.0.104", 8081).usePlaintext().build()

      var client:ServicesGrpc.ServicesBlockingStub= ServicesGrpc.newBlockingStub(channel);



}