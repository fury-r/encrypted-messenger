//Generated by the protocol buffer compiler. DO NOT EDIT!
// source: message.proto

package com.services;

@kotlin.jvm.JvmSynthetic
inline fun messageResponse(block: com.services.MessageResponseKt.Dsl.() -> kotlin.Unit): com.services.Message.MessageResponse =
  com.services.MessageResponseKt.Dsl._create(com.services.Message.MessageResponse.newBuilder()).apply { block() }._build()
object MessageResponseKt {
  @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
  @com.google.protobuf.kotlin.ProtoDslMarker
  class Dsl private constructor(
    private val _builder: com.services.Message.MessageResponse.Builder
  ) {
    companion object {
      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _create(builder: com.services.Message.MessageResponse.Builder): Dsl = Dsl(builder)
    }

    @kotlin.jvm.JvmSynthetic
    @kotlin.PublishedApi
    internal fun _build(): com.services.Message.MessageResponse = _builder.build()

    /**
     * <code>.MessageInfo message = 1;</code>
     */
    var message: com.services.Message.MessageInfo
      @JvmName("getMessage")
      get() = _builder.getMessage()
      @JvmName("setMessage")
      set(value) {
        _builder.setMessage(value)
      }
    /**
     * <code>.MessageInfo message = 1;</code>
     */
    fun clearMessage() {
      _builder.clearMessage()
    }
    /**
     * <code>.MessageInfo message = 1;</code>
     * @return Whether the message field is set.
     */
    fun hasMessage(): kotlin.Boolean {
      return _builder.hasMessage()
    }

    /**
     * <code>.MessageType type = 2;</code>
     */
    var type: com.services.Message.MessageType
      @JvmName("getType")
      get() = _builder.getType()
      @JvmName("setType")
      set(value) {
        _builder.setType(value)
      }
    /**
     * <code>.MessageType type = 2;</code>
     */
    fun clearType() {
      _builder.clearType()
    }
  }
}
@kotlin.jvm.JvmSynthetic
inline fun com.services.Message.MessageResponse.copy(block: com.services.MessageResponseKt.Dsl.() -> kotlin.Unit): com.services.Message.MessageResponse =
  com.services.MessageResponseKt.Dsl._create(this.toBuilder()).apply { block() }._build()
