package com.services

import com.services.ServicesGrpc.getServiceDescriptor
import io.grpc.CallOptions
import io.grpc.CallOptions.DEFAULT
import io.grpc.Channel
import io.grpc.Metadata
import io.grpc.MethodDescriptor
import io.grpc.ServerServiceDefinition
import io.grpc.ServerServiceDefinition.builder
import io.grpc.ServiceDescriptor
import io.grpc.Status
import io.grpc.Status.UNIMPLEMENTED
import io.grpc.StatusException
import io.grpc.kotlin.AbstractCoroutineServerImpl
import io.grpc.kotlin.AbstractCoroutineStub
import io.grpc.kotlin.ClientCalls
import io.grpc.kotlin.ClientCalls.unaryRpc
import io.grpc.kotlin.ServerCalls
import io.grpc.kotlin.ServerCalls.unaryServerMethodDefinition
import io.grpc.kotlin.StubFor
import kotlin.String
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * Holder for Kotlin coroutine-based client and server APIs for Services.
 */
public object ServicesGrpcKt {
  public const val SERVICE_NAME: String = ServicesGrpc.SERVICE_NAME

  @JvmStatic
  public val serviceDescriptor: ServiceDescriptor
    get() = ServicesGrpc.getServiceDescriptor()

  public val loginMethod: MethodDescriptor<Login.LoginRequest, Login.LoginResponse>
    @JvmStatic
    get() = ServicesGrpc.getLoginMethod()

  public val registerMethod: MethodDescriptor<Register.RegisterRequest, Register.RegisterResponse>
    @JvmStatic
    get() = ServicesGrpc.getRegisterMethod()

  public val otpMethod: MethodDescriptor<Otp.OtpRequest, Auth.AuthResponse>
    @JvmStatic
    get() = ServicesGrpc.getOtpMethod()

  public val validateContactsMethod:
      MethodDescriptor<ContactOuterClass.ContactsList, ContactOuterClass.ContactsList>
    @JvmStatic
    get() = ServicesGrpc.getValidateContactsMethod()

  public val sendMethod: MethodDescriptor<Message.Event, Message.Event>
    @JvmStatic
    get() = ServicesGrpc.getSendMethod()

  public val messageUpdateMethod:
      MethodDescriptor<Message.MessageUpdateRequest, Message.MessageUpdateResponse>
    @JvmStatic
    get() = ServicesGrpc.getMessageUpdateMethod()

  public val verifyTokenMethod: MethodDescriptor<Auth.AuthRequest, Auth.AuthResponse>
    @JvmStatic
    get() = ServicesGrpc.getVerifyTokenMethod()

  public val savePubKeyMethod: MethodDescriptor<UserOuterClass.User, UserOuterClass.User>
    @JvmStatic
    get() = ServicesGrpc.getSavePubKeyMethod()

  public val handShakeRequestMethod: MethodDescriptor<Message.Event, Message.Event>
    @JvmStatic
    get() = ServicesGrpc.getHandShakeRequestMethod()

  public val getUserMethod: MethodDescriptor<Service.UserRequest, Service.UserResponse>
    @JvmStatic
    get() = ServicesGrpc.getGetUserMethod()

  public val blockUserMethod:
      MethodDescriptor<UserOuterClass.BlockRequest, UserOuterClass.BlockResponse>
    @JvmStatic
    get() = ServicesGrpc.getBlockUserMethod()

  /**
   * A stub for issuing RPCs to a(n) Services service as suspending coroutines.
   */
  @StubFor(ServicesGrpc::class)
  public class ServicesCoroutineStub @JvmOverloads constructor(
    channel: Channel,
    callOptions: CallOptions = DEFAULT,
  ) : AbstractCoroutineStub<ServicesCoroutineStub>(channel, callOptions) {
    public override fun build(channel: Channel, callOptions: CallOptions): ServicesCoroutineStub =
        ServicesCoroutineStub(channel, callOptions)

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun login(request: Login.LoginRequest, headers: Metadata = Metadata()):
        Login.LoginResponse = unaryRpc(
      channel,
      ServicesGrpc.getLoginMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun register(request: Register.RegisterRequest, headers: Metadata = Metadata()):
        Register.RegisterResponse = unaryRpc(
      channel,
      ServicesGrpc.getRegisterMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun otp(request: Otp.OtpRequest, headers: Metadata = Metadata()):
        Auth.AuthResponse = unaryRpc(
      channel,
      ServicesGrpc.getOtpMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun validateContacts(request: ContactOuterClass.ContactsList, headers: Metadata =
        Metadata()): ContactOuterClass.ContactsList = unaryRpc(
      channel,
      ServicesGrpc.getValidateContactsMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun send(request: Message.Event, headers: Metadata = Metadata()): Message.Event =
        unaryRpc(
      channel,
      ServicesGrpc.getSendMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun messageUpdate(request: Message.MessageUpdateRequest, headers: Metadata =
        Metadata()): Message.MessageUpdateResponse = unaryRpc(
      channel,
      ServicesGrpc.getMessageUpdateMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun verifyToken(request: Auth.AuthRequest, headers: Metadata = Metadata()):
        Auth.AuthResponse = unaryRpc(
      channel,
      ServicesGrpc.getVerifyTokenMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun savePubKey(request: UserOuterClass.User, headers: Metadata = Metadata()):
        UserOuterClass.User = unaryRpc(
      channel,
      ServicesGrpc.getSavePubKeyMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun handShakeRequest(request: Message.Event, headers: Metadata = Metadata()):
        Message.Event = unaryRpc(
      channel,
      ServicesGrpc.getHandShakeRequestMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun getUser(request: Service.UserRequest, headers: Metadata = Metadata()):
        Service.UserResponse = unaryRpc(
      channel,
      ServicesGrpc.getGetUserMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun blockUser(request: UserOuterClass.BlockRequest, headers: Metadata =
        Metadata()): UserOuterClass.BlockResponse = unaryRpc(
      channel,
      ServicesGrpc.getBlockUserMethod(),
      request,
      callOptions,
      headers
    )
  }

  /**
   * Skeletal implementation of the Services service based on Kotlin coroutines.
   */
  public abstract class ServicesCoroutineImplBase(
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
  ) : AbstractCoroutineServerImpl(coroutineContext) {
    /**
     * Returns the response to an RPC for Services.Login.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun login(request: Login.LoginRequest): Login.LoginResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method Services.Login is unimplemented"))

    /**
     * Returns the response to an RPC for Services.Register.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun register(request: Register.RegisterRequest): Register.RegisterResponse =
        throw
        StatusException(UNIMPLEMENTED.withDescription("Method Services.Register is unimplemented"))

    /**
     * Returns the response to an RPC for Services.Otp.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun otp(request: Otp.OtpRequest): Auth.AuthResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method Services.Otp is unimplemented"))

    /**
     * Returns the response to an RPC for Services.ValidateContacts.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun validateContacts(request: ContactOuterClass.ContactsList):
        ContactOuterClass.ContactsList = throw
        StatusException(UNIMPLEMENTED.withDescription("Method Services.ValidateContacts is unimplemented"))

    /**
     * Returns the response to an RPC for Services.Send.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun send(request: Message.Event): Message.Event = throw
        StatusException(UNIMPLEMENTED.withDescription("Method Services.Send is unimplemented"))

    /**
     * Returns the response to an RPC for Services.messageUpdate.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun messageUpdate(request: Message.MessageUpdateRequest):
        Message.MessageUpdateResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method Services.messageUpdate is unimplemented"))

    /**
     * Returns the response to an RPC for Services.VerifyToken.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun verifyToken(request: Auth.AuthRequest): Auth.AuthResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method Services.VerifyToken is unimplemented"))

    /**
     * Returns the response to an RPC for Services.savePubKey.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun savePubKey(request: UserOuterClass.User): UserOuterClass.User = throw
        StatusException(UNIMPLEMENTED.withDescription("Method Services.savePubKey is unimplemented"))

    /**
     * Returns the response to an RPC for Services.handShakeRequest.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun handShakeRequest(request: Message.Event): Message.Event = throw
        StatusException(UNIMPLEMENTED.withDescription("Method Services.handShakeRequest is unimplemented"))

    /**
     * Returns the response to an RPC for Services.getUser.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun getUser(request: Service.UserRequest): Service.UserResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method Services.getUser is unimplemented"))

    /**
     * Returns the response to an RPC for Services.blockUser.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun blockUser(request: UserOuterClass.BlockRequest):
        UserOuterClass.BlockResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method Services.blockUser is unimplemented"))

    public final override fun bindService(): ServerServiceDefinition =
        builder(getServiceDescriptor())
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = ServicesGrpc.getLoginMethod(),
      implementation = ::login
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = ServicesGrpc.getRegisterMethod(),
      implementation = ::register
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = ServicesGrpc.getOtpMethod(),
      implementation = ::otp
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = ServicesGrpc.getValidateContactsMethod(),
      implementation = ::validateContacts
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = ServicesGrpc.getSendMethod(),
      implementation = ::send
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = ServicesGrpc.getMessageUpdateMethod(),
      implementation = ::messageUpdate
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = ServicesGrpc.getVerifyTokenMethod(),
      implementation = ::verifyToken
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = ServicesGrpc.getSavePubKeyMethod(),
      implementation = ::savePubKey
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = ServicesGrpc.getHandShakeRequestMethod(),
      implementation = ::handShakeRequest
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = ServicesGrpc.getGetUserMethod(),
      implementation = ::getUser
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = ServicesGrpc.getBlockUserMethod(),
      implementation = ::blockUser
    )).build()
  }
}
