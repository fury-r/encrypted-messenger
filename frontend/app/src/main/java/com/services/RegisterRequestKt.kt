//Generated by the protocol buffer compiler. DO NOT EDIT!
// source: register.proto

package com.services;

@kotlin.jvm.JvmSynthetic
inline fun registerRequest(block: com.services.RegisterRequestKt.Dsl.() -> kotlin.Unit): com.services.Register.RegisterRequest =
  com.services.RegisterRequestKt.Dsl._create(com.services.Register.RegisterRequest.newBuilder()).apply { block() }._build()
object RegisterRequestKt {
  @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
  @com.google.protobuf.kotlin.ProtoDslMarker
  class Dsl private constructor(
    private val _builder: com.services.Register.RegisterRequest.Builder
  ) {
    companion object {
      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _create(builder: com.services.Register.RegisterRequest.Builder): Dsl = Dsl(builder)
    }

    @kotlin.jvm.JvmSynthetic
    @kotlin.PublishedApi
    internal fun _build(): com.services.Register.RegisterRequest = _builder.build()

    /**
     * <code>string phoneNumber = 1;</code>
     */
    var phoneNumber: kotlin.String
      @JvmName("getPhoneNumber")
      get() = _builder.getPhoneNumber()
      @JvmName("setPhoneNumber")
      set(value) {
        _builder.setPhoneNumber(value)
      }
    /**
     * <code>string phoneNumber = 1;</code>
     */
    fun clearPhoneNumber() {
      _builder.clearPhoneNumber()
    }

    /**
     * <code>string username = 2;</code>
     */
    var username: kotlin.String
      @JvmName("getUsername")
      get() = _builder.getUsername()
      @JvmName("setUsername")
      set(value) {
        _builder.setUsername(value)
      }
    /**
     * <code>string username = 2;</code>
     */
    fun clearUsername() {
      _builder.clearUsername()
    }

    /**
     * <code>string email = 3;</code>
     */
    var email: kotlin.String
      @JvmName("getEmail")
      get() = _builder.getEmail()
      @JvmName("setEmail")
      set(value) {
        _builder.setEmail(value)
      }
    /**
     * <code>string email = 3;</code>
     */
    fun clearEmail() {
      _builder.clearEmail()
    }

    /**
     * <code>string password = 4;</code>
     */
    var password: kotlin.String
      @JvmName("getPassword")
      get() = _builder.getPassword()
      @JvmName("setPassword")
      set(value) {
        _builder.setPassword(value)
      }
    /**
     * <code>string password = 4;</code>
     */
    fun clearPassword() {
      _builder.clearPassword()
    }

    /**
     * <code>optional string countryCode = 5;</code>
     */
    var countryCode: kotlin.String
      @JvmName("getCountryCode")
      get() = _builder.getCountryCode()
      @JvmName("setCountryCode")
      set(value) {
        _builder.setCountryCode(value)
      }
    /**
     * <code>optional string countryCode = 5;</code>
     */
    fun clearCountryCode() {
      _builder.clearCountryCode()
    }
    /**
     * <code>optional string countryCode = 5;</code>
     * @return Whether the countryCode field is set.
     */
    fun hasCountryCode(): kotlin.Boolean {
      return _builder.hasCountryCode()
    }
  }
}
@kotlin.jvm.JvmSynthetic
inline fun com.services.Register.RegisterRequest.copy(block: com.services.RegisterRequestKt.Dsl.() -> kotlin.Unit): com.services.Register.RegisterRequest =
  com.services.RegisterRequestKt.Dsl._create(this.toBuilder()).apply { block() }._build()
