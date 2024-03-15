// Generated by the protocol buffer compiler. DO NOT EDIT!
// source: message.proto

// Generated files should ignore deprecation warnings
@file:Suppress("DEPRECATION")
package com.services;

@kotlin.jvm.JvmName("-initializeevent")
public inline fun event(block: com.services.EventKt.Dsl.() -> kotlin.Unit): com.services.Message.Event =
  com.services.EventKt.Dsl._create(com.services.Message.Event.newBuilder()).apply { block() }._build()
/**
 * Protobuf type `Event`
 */
public object EventKt {
  @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
  @com.google.protobuf.kotlin.ProtoDslMarker
  public class Dsl private constructor(
    private val _builder: com.services.Message.Event.Builder
  ) {
    public companion object {
      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _create(builder: com.services.Message.Event.Builder): Dsl = Dsl(builder)
    }

    @kotlin.jvm.JvmSynthetic
    @kotlin.PublishedApi
    internal fun _build(): com.services.Message.Event = _builder.build()

    /**
     * `.EventType type = 1;`
     */
    public var type: com.services.Message.EventType
      @JvmName("getType")
      get() = _builder.getType()
      @JvmName("setType")
      set(value) {
        _builder.setType(value)
      }
    public var typeValue: kotlin.Int
      @JvmName("getTypeValue")
      get() = _builder.getTypeValue()
      @JvmName("setTypeValue")
      set(value) {
        _builder.setTypeValue(value)
      }
    /**
     * `.EventType type = 1;`
     */
    public fun clearType() {
      _builder.clearType()
    }

    /**
     * `string token = 2;`
     */
    public var token: kotlin.String
      @JvmName("getToken")
      get() = _builder.getToken()
      @JvmName("setToken")
      set(value) {
        _builder.setToken(value)
      }
    /**
     * `string token = 2;`
     */
    public fun clearToken() {
      _builder.clearToken()
    }

    /**
     * `.MessageRequest message = 3;`
     */
    public var message: com.services.Message.MessageRequest
      @JvmName("getMessage")
      get() = _builder.getMessage()
      @JvmName("setMessage")
      set(value) {
        _builder.setMessage(value)
      }
    /**
     * `.MessageRequest message = 3;`
     */
    public fun clearMessage() {
      _builder.clearMessage()
    }
    /**
     * `.MessageRequest message = 3;`
     * @return Whether the message field is set.
     */
    public fun hasMessage(): kotlin.Boolean {
      return _builder.hasMessage()
    }

    /**
     * `.PubKeyExchange exchange = 4;`
     */
    public var exchange: com.services.Message.PubKeyExchange
      @JvmName("getExchange")
      get() = _builder.getExchange()
      @JvmName("setExchange")
      set(value) {
        _builder.setExchange(value)
      }
    /**
     * `.PubKeyExchange exchange = 4;`
     */
    public fun clearExchange() {
      _builder.clearExchange()
    }
    /**
     * `.PubKeyExchange exchange = 4;`
     * @return Whether the exchange field is set.
     */
    public fun hasExchange(): kotlin.Boolean {
      return _builder.hasExchange()
    }
    public val bodyCase: com.services.Message.Event.BodyCase
      @JvmName("getBodyCase")
      get() = _builder.getBodyCase()

    public fun clearBody() {
      _builder.clearBody()
    }
  }
}
@kotlin.jvm.JvmSynthetic
public inline fun com.services.Message.Event.copy(block: `com.services`.EventKt.Dsl.() -> kotlin.Unit): com.services.Message.Event =
  `com.services`.EventKt.Dsl._create(this.toBuilder()).apply { block() }._build()

public val com.services.Message.EventOrBuilder.messageOrNull: com.services.Message.MessageRequest?
  get() = if (hasMessage()) getMessage() else null

public val com.services.Message.EventOrBuilder.exchangeOrNull: com.services.Message.PubKeyExchange?
  get() = if (hasExchange()) getExchange() else null

