// Generated by the protocol buffer compiler. DO NOT EDIT!
// source: otp.proto

// Generated files should ignore deprecation warnings
@file:Suppress("DEPRECATION")
package com.services;

@kotlin.jvm.JvmName("-initializeotpRequest")
public inline fun otpRequest(block: com.services.OtpRequestKt.Dsl.() -> kotlin.Unit): com.services.Otp.OtpRequest =
  com.services.OtpRequestKt.Dsl._create(com.services.Otp.OtpRequest.newBuilder()).apply { block() }._build()
/**
 * Protobuf type `OtpRequest`
 */
public object OtpRequestKt {
  @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
  @com.google.protobuf.kotlin.ProtoDslMarker
  public class Dsl private constructor(
    private val _builder: com.services.Otp.OtpRequest.Builder
  ) {
    public companion object {
      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _create(builder: com.services.Otp.OtpRequest.Builder): Dsl = Dsl(builder)
    }

    @kotlin.jvm.JvmSynthetic
    @kotlin.PublishedApi
    internal fun _build(): com.services.Otp.OtpRequest = _builder.build()

    /**
     * `string otp = 1;`
     */
    public var otp: kotlin.String
      @JvmName("getOtp")
      get() = _builder.getOtp()
      @JvmName("setOtp")
      set(value) {
        _builder.setOtp(value)
      }
    /**
     * `string otp = 1;`
     */
    public fun clearOtp() {
      _builder.clearOtp()
    }

    /**
     * `optional string email = 2;`
     */
    public var email: kotlin.String
      @JvmName("getEmail")
      get() = _builder.getEmail()
      @JvmName("setEmail")
      set(value) {
        _builder.setEmail(value)
      }
    /**
     * `optional string email = 2;`
     */
    public fun clearEmail() {
      _builder.clearEmail()
    }
    /**
     * `optional string email = 2;`
     * @return Whether the email field is set.
     */
    public fun hasEmail(): kotlin.Boolean {
      return _builder.hasEmail()
    }

    /**
     * `optional string phoneNumber = 3;`
     */
    public var phoneNumber: kotlin.String
      @JvmName("getPhoneNumber")
      get() = _builder.getPhoneNumber()
      @JvmName("setPhoneNumber")
      set(value) {
        _builder.setPhoneNumber(value)
      }
    /**
     * `optional string phoneNumber = 3;`
     */
    public fun clearPhoneNumber() {
      _builder.clearPhoneNumber()
    }
    /**
     * `optional string phoneNumber = 3;`
     * @return Whether the phoneNumber field is set.
     */
    public fun hasPhoneNumber(): kotlin.Boolean {
      return _builder.hasPhoneNumber()
    }
  }
}
@kotlin.jvm.JvmSynthetic
public inline fun com.services.Otp.OtpRequest.copy(block: `com.services`.OtpRequestKt.Dsl.() -> kotlin.Unit): com.services.Otp.OtpRequest =
  `com.services`.OtpRequestKt.Dsl._create(this.toBuilder()).apply { block() }._build()

