//Generated by the protocol buffer compiler. DO NOT EDIT!
// source: middleware/middleware.proto

package com.fury.messenger.middlewaregrpc;

@kotlin.jvm.JvmSynthetic
inline fun pingResponse(block: com.fury.messenger.middlewaregrpc.PingResponseKt.Dsl.() -> kotlin.Unit): com.fury.messenger.middlewaregrpc.PingResponse =
  com.fury.messenger.middlewaregrpc.PingResponseKt.Dsl._create(com.fury.messenger.middlewaregrpc.PingResponse.newBuilder()).apply { block() }._build()
object PingResponseKt {
  @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
  @com.google.protobuf.kotlin.ProtoDslMarker
  class Dsl private constructor(
    private val _builder: com.fury.messenger.middlewaregrpc.PingResponse.Builder
  ) {
    companion object {
      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _create(builder: com.fury.messenger.middlewaregrpc.PingResponse.Builder): Dsl = Dsl(builder)
    }

    @kotlin.jvm.JvmSynthetic
    @kotlin.PublishedApi
    internal fun _build(): com.fury.messenger.middlewaregrpc.PingResponse = _builder.build()

    /**
     * <code>string message = 1;</code>
     */
    var message: kotlin.String
      @JvmName("getMessage")
      get() = _builder.getMessage()
      @JvmName("setMessage")
      set(value) {
        _builder.setMessage(value)
      }
    /**
     * <code>string message = 1;</code>
     */
    fun clearMessage() {
      _builder.clearMessage()
    }
  }
}
@kotlin.jvm.JvmSynthetic
inline fun com.fury.messenger.middlewaregrpc.PingResponse.copy(block: com.fury.messenger.middlewaregrpc.PingResponseKt.Dsl.() -> kotlin.Unit): com.fury.messenger.middlewaregrpc.PingResponse =
  com.fury.messenger.middlewaregrpc.PingResponseKt.Dsl._create(this.toBuilder()).apply { block() }._build()
