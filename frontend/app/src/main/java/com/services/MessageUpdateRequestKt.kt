//Generated by the protocol buffer compiler. DO NOT EDIT!
// source: message.proto

package com.services;

@kotlin.jvm.JvmSynthetic
inline fun messageUpdateRequest(block: com.services.MessageUpdateRequestKt.Dsl.() -> kotlin.Unit): com.services.Message.MessageUpdateRequest =
  com.services.MessageUpdateRequestKt.Dsl._create(com.services.Message.MessageUpdateRequest.newBuilder()).apply { block() }._build()
object MessageUpdateRequestKt {
  @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
  @com.google.protobuf.kotlin.ProtoDslMarker
  class Dsl private constructor(
    private val _builder: com.services.Message.MessageUpdateRequest.Builder
  ) {
    companion object {
      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _create(builder: com.services.Message.MessageUpdateRequest.Builder): Dsl = Dsl(builder)
    }

    @kotlin.jvm.JvmSynthetic
    @kotlin.PublishedApi
    internal fun _build(): com.services.Message.MessageUpdateRequest = _builder.build()

    /**
     * <code>string sender = 1;</code>
     */
    var sender: kotlin.String
      @JvmName("getSender")
      get() = _builder.getSender()
      @JvmName("setSender")
      set(value) {
        _builder.setSender(value)
      }
    /**
     * <code>string sender = 1;</code>
     */
    fun clearSender() {
      _builder.clearSender()
    }

    /**
     * <code>string reciever = 2;</code>
     */
    var reciever: kotlin.String
      @JvmName("getReciever")
      get() = _builder.getReciever()
      @JvmName("setReciever")
      set(value) {
        _builder.setReciever(value)
      }
    /**
     * <code>string reciever = 2;</code>
     */
    fun clearReciever() {
      _builder.clearReciever()
    }

    /**
     * <code>string messageId = 3;</code>
     */
    var messageId: kotlin.String
      @JvmName("getMessageId")
      get() = _builder.getMessageId()
      @JvmName("setMessageId")
      set(value) {
        _builder.setMessageId(value)
      }
    /**
     * <code>string messageId = 3;</code>
     */
    fun clearMessageId() {
      _builder.clearMessageId()
    }

    /**
     * <code>bool deliverStatus = 4;</code>
     */
    var deliverStatus: kotlin.Boolean
      @JvmName("getDeliverStatus")
      get() = _builder.getDeliverStatus()
      @JvmName("setDeliverStatus")
      set(value) {
        _builder.setDeliverStatus(value)
      }
    /**
     * <code>bool deliverStatus = 4;</code>
     */
    fun clearDeliverStatus() {
      _builder.clearDeliverStatus()
    }

    /**
     * <code>bool readStatus = 5;</code>
     */
    var readStatus: kotlin.Boolean
      @JvmName("getReadStatus")
      get() = _builder.getReadStatus()
      @JvmName("setReadStatus")
      set(value) {
        _builder.setReadStatus(value)
      }
    /**
     * <code>bool readStatus = 5;</code>
     */
    fun clearReadStatus() {
      _builder.clearReadStatus()
    }

    /**
     * <code>string token = 6;</code>
     */
    var token: kotlin.String
      @JvmName("getToken")
      get() = _builder.getToken()
      @JvmName("setToken")
      set(value) {
        _builder.setToken(value)
      }
    /**
     * <code>string token = 6;</code>
     */
    fun clearToken() {
      _builder.clearToken()
    }
  }
}
@kotlin.jvm.JvmSynthetic
inline fun com.services.Message.MessageUpdateRequest.copy(block: com.services.MessageUpdateRequestKt.Dsl.() -> kotlin.Unit): com.services.Message.MessageUpdateRequest =
  com.services.MessageUpdateRequestKt.Dsl._create(this.toBuilder()).apply { block() }._build()
