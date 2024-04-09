package com.services;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.56.0)",
    comments = "Source: service.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ServicesGrpc {

  private ServicesGrpc() {}

  public static final String SERVICE_NAME = "Services";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.services.Login.LoginRequest,
      com.services.Login.LoginResponse> getLoginMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Login",
      requestType = com.services.Login.LoginRequest.class,
      responseType = com.services.Login.LoginResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.services.Login.LoginRequest,
      com.services.Login.LoginResponse> getLoginMethod() {
    io.grpc.MethodDescriptor<com.services.Login.LoginRequest, com.services.Login.LoginResponse> getLoginMethod;
    if ((getLoginMethod = ServicesGrpc.getLoginMethod) == null) {
      synchronized (ServicesGrpc.class) {
        if ((getLoginMethod = ServicesGrpc.getLoginMethod) == null) {
          ServicesGrpc.getLoginMethod = getLoginMethod =
              io.grpc.MethodDescriptor.<com.services.Login.LoginRequest, com.services.Login.LoginResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Login"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.services.Login.LoginRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.services.Login.LoginResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ServicesMethodDescriptorSupplier("Login"))
              .build();
        }
      }
    }
    return getLoginMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.services.Register.RegisterRequest,
      com.services.Register.RegisterResponse> getRegisterMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Register",
      requestType = com.services.Register.RegisterRequest.class,
      responseType = com.services.Register.RegisterResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.services.Register.RegisterRequest,
      com.services.Register.RegisterResponse> getRegisterMethod() {
    io.grpc.MethodDescriptor<com.services.Register.RegisterRequest, com.services.Register.RegisterResponse> getRegisterMethod;
    if ((getRegisterMethod = ServicesGrpc.getRegisterMethod) == null) {
      synchronized (ServicesGrpc.class) {
        if ((getRegisterMethod = ServicesGrpc.getRegisterMethod) == null) {
          ServicesGrpc.getRegisterMethod = getRegisterMethod =
              io.grpc.MethodDescriptor.<com.services.Register.RegisterRequest, com.services.Register.RegisterResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Register"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.services.Register.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.services.Register.RegisterResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ServicesMethodDescriptorSupplier("Register"))
              .build();
        }
      }
    }
    return getRegisterMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.services.Otp.OtpRequest,
      com.services.Auth.AuthResponse> getOtpMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Otp",
      requestType = com.services.Otp.OtpRequest.class,
      responseType = com.services.Auth.AuthResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.services.Otp.OtpRequest,
      com.services.Auth.AuthResponse> getOtpMethod() {
    io.grpc.MethodDescriptor<com.services.Otp.OtpRequest, com.services.Auth.AuthResponse> getOtpMethod;
    if ((getOtpMethod = ServicesGrpc.getOtpMethod) == null) {
      synchronized (ServicesGrpc.class) {
        if ((getOtpMethod = ServicesGrpc.getOtpMethod) == null) {
          ServicesGrpc.getOtpMethod = getOtpMethod =
              io.grpc.MethodDescriptor.<com.services.Otp.OtpRequest, com.services.Auth.AuthResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Otp"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.services.Otp.OtpRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.services.Auth.AuthResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ServicesMethodDescriptorSupplier("Otp"))
              .build();
        }
      }
    }
    return getOtpMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.services.ContactOuterClass.ContactsList,
      com.services.ContactOuterClass.ContactsList> getValidateContactsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ValidateContacts",
      requestType = com.services.ContactOuterClass.ContactsList.class,
      responseType = com.services.ContactOuterClass.ContactsList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.services.ContactOuterClass.ContactsList,
      com.services.ContactOuterClass.ContactsList> getValidateContactsMethod() {
    io.grpc.MethodDescriptor<com.services.ContactOuterClass.ContactsList, com.services.ContactOuterClass.ContactsList> getValidateContactsMethod;
    if ((getValidateContactsMethod = ServicesGrpc.getValidateContactsMethod) == null) {
      synchronized (ServicesGrpc.class) {
        if ((getValidateContactsMethod = ServicesGrpc.getValidateContactsMethod) == null) {
          ServicesGrpc.getValidateContactsMethod = getValidateContactsMethod =
              io.grpc.MethodDescriptor.<com.services.ContactOuterClass.ContactsList, com.services.ContactOuterClass.ContactsList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ValidateContacts"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.services.ContactOuterClass.ContactsList.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.services.ContactOuterClass.ContactsList.getDefaultInstance()))
              .setSchemaDescriptor(new ServicesMethodDescriptorSupplier("ValidateContacts"))
              .build();
        }
      }
    }
    return getValidateContactsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.services.Message.Event,
      com.services.Message.Event> getSendMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Send",
      requestType = com.services.Message.Event.class,
      responseType = com.services.Message.Event.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.services.Message.Event,
      com.services.Message.Event> getSendMethod() {
    io.grpc.MethodDescriptor<com.services.Message.Event, com.services.Message.Event> getSendMethod;
    if ((getSendMethod = ServicesGrpc.getSendMethod) == null) {
      synchronized (ServicesGrpc.class) {
        if ((getSendMethod = ServicesGrpc.getSendMethod) == null) {
          ServicesGrpc.getSendMethod = getSendMethod =
              io.grpc.MethodDescriptor.<com.services.Message.Event, com.services.Message.Event>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Send"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.services.Message.Event.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.services.Message.Event.getDefaultInstance()))
              .setSchemaDescriptor(new ServicesMethodDescriptorSupplier("Send"))
              .build();
        }
      }
    }
    return getSendMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.services.Message.MessageUpdateRequest,
      com.services.Message.MessageUpdateResponse> getMessageUpdateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "messageUpdate",
      requestType = com.services.Message.MessageUpdateRequest.class,
      responseType = com.services.Message.MessageUpdateResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.services.Message.MessageUpdateRequest,
      com.services.Message.MessageUpdateResponse> getMessageUpdateMethod() {
    io.grpc.MethodDescriptor<com.services.Message.MessageUpdateRequest, com.services.Message.MessageUpdateResponse> getMessageUpdateMethod;
    if ((getMessageUpdateMethod = ServicesGrpc.getMessageUpdateMethod) == null) {
      synchronized (ServicesGrpc.class) {
        if ((getMessageUpdateMethod = ServicesGrpc.getMessageUpdateMethod) == null) {
          ServicesGrpc.getMessageUpdateMethod = getMessageUpdateMethod =
              io.grpc.MethodDescriptor.<com.services.Message.MessageUpdateRequest, com.services.Message.MessageUpdateResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "messageUpdate"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.services.Message.MessageUpdateRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.services.Message.MessageUpdateResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ServicesMethodDescriptorSupplier("messageUpdate"))
              .build();
        }
      }
    }
    return getMessageUpdateMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.services.Auth.AuthRequest,
      com.services.Auth.AuthResponse> getVerifyTokenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "VerifyToken",
      requestType = com.services.Auth.AuthRequest.class,
      responseType = com.services.Auth.AuthResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.services.Auth.AuthRequest,
      com.services.Auth.AuthResponse> getVerifyTokenMethod() {
    io.grpc.MethodDescriptor<com.services.Auth.AuthRequest, com.services.Auth.AuthResponse> getVerifyTokenMethod;
    if ((getVerifyTokenMethod = ServicesGrpc.getVerifyTokenMethod) == null) {
      synchronized (ServicesGrpc.class) {
        if ((getVerifyTokenMethod = ServicesGrpc.getVerifyTokenMethod) == null) {
          ServicesGrpc.getVerifyTokenMethod = getVerifyTokenMethod =
              io.grpc.MethodDescriptor.<com.services.Auth.AuthRequest, com.services.Auth.AuthResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "VerifyToken"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.services.Auth.AuthRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.services.Auth.AuthResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ServicesMethodDescriptorSupplier("VerifyToken"))
              .build();
        }
      }
    }
    return getVerifyTokenMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.services.UserOuterClass.User,
      com.services.UserOuterClass.User> getSavePubKeyMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "savePubKey",
      requestType = com.services.UserOuterClass.User.class,
      responseType = com.services.UserOuterClass.User.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.services.UserOuterClass.User,
      com.services.UserOuterClass.User> getSavePubKeyMethod() {
    io.grpc.MethodDescriptor<com.services.UserOuterClass.User, com.services.UserOuterClass.User> getSavePubKeyMethod;
    if ((getSavePubKeyMethod = ServicesGrpc.getSavePubKeyMethod) == null) {
      synchronized (ServicesGrpc.class) {
        if ((getSavePubKeyMethod = ServicesGrpc.getSavePubKeyMethod) == null) {
          ServicesGrpc.getSavePubKeyMethod = getSavePubKeyMethod =
              io.grpc.MethodDescriptor.<com.services.UserOuterClass.User, com.services.UserOuterClass.User>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "savePubKey"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.services.UserOuterClass.User.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.services.UserOuterClass.User.getDefaultInstance()))
              .setSchemaDescriptor(new ServicesMethodDescriptorSupplier("savePubKey"))
              .build();
        }
      }
    }
    return getSavePubKeyMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.services.Message.Event,
      com.services.Message.Event> getHandShakeRequestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "handShakeRequest",
      requestType = com.services.Message.Event.class,
      responseType = com.services.Message.Event.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.services.Message.Event,
      com.services.Message.Event> getHandShakeRequestMethod() {
    io.grpc.MethodDescriptor<com.services.Message.Event, com.services.Message.Event> getHandShakeRequestMethod;
    if ((getHandShakeRequestMethod = ServicesGrpc.getHandShakeRequestMethod) == null) {
      synchronized (ServicesGrpc.class) {
        if ((getHandShakeRequestMethod = ServicesGrpc.getHandShakeRequestMethod) == null) {
          ServicesGrpc.getHandShakeRequestMethod = getHandShakeRequestMethod =
              io.grpc.MethodDescriptor.<com.services.Message.Event, com.services.Message.Event>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "handShakeRequest"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.services.Message.Event.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.services.Message.Event.getDefaultInstance()))
              .setSchemaDescriptor(new ServicesMethodDescriptorSupplier("handShakeRequest"))
              .build();
        }
      }
    }
    return getHandShakeRequestMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.services.Service.UserRequest,
      com.services.Service.UserResponse> getGetUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getUser",
      requestType = com.services.Service.UserRequest.class,
      responseType = com.services.Service.UserResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.services.Service.UserRequest,
      com.services.Service.UserResponse> getGetUserMethod() {
    io.grpc.MethodDescriptor<com.services.Service.UserRequest, com.services.Service.UserResponse> getGetUserMethod;
    if ((getGetUserMethod = ServicesGrpc.getGetUserMethod) == null) {
      synchronized (ServicesGrpc.class) {
        if ((getGetUserMethod = ServicesGrpc.getGetUserMethod) == null) {
          ServicesGrpc.getGetUserMethod = getGetUserMethod =
              io.grpc.MethodDescriptor.<com.services.Service.UserRequest, com.services.Service.UserResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.services.Service.UserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.services.Service.UserResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ServicesMethodDescriptorSupplier("getUser"))
              .build();
        }
      }
    }
    return getGetUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.services.UserOuterClass.BlockRequest,
      com.services.UserOuterClass.BlockResponse> getBlockUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "blockUser",
      requestType = com.services.UserOuterClass.BlockRequest.class,
      responseType = com.services.UserOuterClass.BlockResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.services.UserOuterClass.BlockRequest,
      com.services.UserOuterClass.BlockResponse> getBlockUserMethod() {
    io.grpc.MethodDescriptor<com.services.UserOuterClass.BlockRequest, com.services.UserOuterClass.BlockResponse> getBlockUserMethod;
    if ((getBlockUserMethod = ServicesGrpc.getBlockUserMethod) == null) {
      synchronized (ServicesGrpc.class) {
        if ((getBlockUserMethod = ServicesGrpc.getBlockUserMethod) == null) {
          ServicesGrpc.getBlockUserMethod = getBlockUserMethod =
              io.grpc.MethodDescriptor.<com.services.UserOuterClass.BlockRequest, com.services.UserOuterClass.BlockResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "blockUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.services.UserOuterClass.BlockRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.services.UserOuterClass.BlockResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ServicesMethodDescriptorSupplier("blockUser"))
              .build();
        }
      }
    }
    return getBlockUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.services.UserOuterClass.User,
      com.services.UserOuterClass.User> getUpdateUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "updateUser",
      requestType = com.services.UserOuterClass.User.class,
      responseType = com.services.UserOuterClass.User.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.services.UserOuterClass.User,
      com.services.UserOuterClass.User> getUpdateUserMethod() {
    io.grpc.MethodDescriptor<com.services.UserOuterClass.User, com.services.UserOuterClass.User> getUpdateUserMethod;
    if ((getUpdateUserMethod = ServicesGrpc.getUpdateUserMethod) == null) {
      synchronized (ServicesGrpc.class) {
        if ((getUpdateUserMethod = ServicesGrpc.getUpdateUserMethod) == null) {
          ServicesGrpc.getUpdateUserMethod = getUpdateUserMethod =
              io.grpc.MethodDescriptor.<com.services.UserOuterClass.User, com.services.UserOuterClass.User>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "updateUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.services.UserOuterClass.User.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.services.UserOuterClass.User.getDefaultInstance()))
              .setSchemaDescriptor(new ServicesMethodDescriptorSupplier("updateUser"))
              .build();
        }
      }
    }
    return getUpdateUserMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ServicesStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ServicesStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ServicesStub>() {
        @java.lang.Override
        public ServicesStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ServicesStub(channel, callOptions);
        }
      };
    return ServicesStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ServicesBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ServicesBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ServicesBlockingStub>() {
        @java.lang.Override
        public ServicesBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ServicesBlockingStub(channel, callOptions);
        }
      };
    return ServicesBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ServicesFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ServicesFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ServicesFutureStub>() {
        @java.lang.Override
        public ServicesFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ServicesFutureStub(channel, callOptions);
        }
      };
    return ServicesFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void login(com.services.Login.LoginRequest request,
        io.grpc.stub.StreamObserver<com.services.Login.LoginResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getLoginMethod(), responseObserver);
    }

    /**
     */
    default void register(com.services.Register.RegisterRequest request,
        io.grpc.stub.StreamObserver<com.services.Register.RegisterResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRegisterMethod(), responseObserver);
    }

    /**
     */
    default void otp(com.services.Otp.OtpRequest request,
        io.grpc.stub.StreamObserver<com.services.Auth.AuthResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getOtpMethod(), responseObserver);
    }

    /**
     */
    default void validateContacts(com.services.ContactOuterClass.ContactsList request,
        io.grpc.stub.StreamObserver<com.services.ContactOuterClass.ContactsList> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getValidateContactsMethod(), responseObserver);
    }

    /**
     */
    default void send(com.services.Message.Event request,
        io.grpc.stub.StreamObserver<com.services.Message.Event> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSendMethod(), responseObserver);
    }

    /**
     */
    default void messageUpdate(com.services.Message.MessageUpdateRequest request,
        io.grpc.stub.StreamObserver<com.services.Message.MessageUpdateResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getMessageUpdateMethod(), responseObserver);
    }

    /**
     */
    default void verifyToken(com.services.Auth.AuthRequest request,
        io.grpc.stub.StreamObserver<com.services.Auth.AuthResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getVerifyTokenMethod(), responseObserver);
    }

    /**
     */
    default void savePubKey(com.services.UserOuterClass.User request,
        io.grpc.stub.StreamObserver<com.services.UserOuterClass.User> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSavePubKeyMethod(), responseObserver);
    }

    /**
     */
    default void handShakeRequest(com.services.Message.Event request,
        io.grpc.stub.StreamObserver<com.services.Message.Event> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getHandShakeRequestMethod(), responseObserver);
    }

    /**
     */
    default void getUser(com.services.Service.UserRequest request,
        io.grpc.stub.StreamObserver<com.services.Service.UserResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetUserMethod(), responseObserver);
    }

    /**
     */
    default void blockUser(com.services.UserOuterClass.BlockRequest request,
        io.grpc.stub.StreamObserver<com.services.UserOuterClass.BlockResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getBlockUserMethod(), responseObserver);
    }

    /**
     */
    default void updateUser(com.services.UserOuterClass.User request,
        io.grpc.stub.StreamObserver<com.services.UserOuterClass.User> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateUserMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service Services.
   */
  public static abstract class ServicesImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ServicesGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service Services.
   */
  public static final class ServicesStub
      extends io.grpc.stub.AbstractAsyncStub<ServicesStub> {
    private ServicesStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServicesStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ServicesStub(channel, callOptions);
    }

    /**
     */
    public void login(com.services.Login.LoginRequest request,
        io.grpc.stub.StreamObserver<com.services.Login.LoginResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getLoginMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void register(com.services.Register.RegisterRequest request,
        io.grpc.stub.StreamObserver<com.services.Register.RegisterResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRegisterMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void otp(com.services.Otp.OtpRequest request,
        io.grpc.stub.StreamObserver<com.services.Auth.AuthResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getOtpMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void validateContacts(com.services.ContactOuterClass.ContactsList request,
        io.grpc.stub.StreamObserver<com.services.ContactOuterClass.ContactsList> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getValidateContactsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void send(com.services.Message.Event request,
        io.grpc.stub.StreamObserver<com.services.Message.Event> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSendMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void messageUpdate(com.services.Message.MessageUpdateRequest request,
        io.grpc.stub.StreamObserver<com.services.Message.MessageUpdateResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getMessageUpdateMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void verifyToken(com.services.Auth.AuthRequest request,
        io.grpc.stub.StreamObserver<com.services.Auth.AuthResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getVerifyTokenMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void savePubKey(com.services.UserOuterClass.User request,
        io.grpc.stub.StreamObserver<com.services.UserOuterClass.User> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSavePubKeyMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void handShakeRequest(com.services.Message.Event request,
        io.grpc.stub.StreamObserver<com.services.Message.Event> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getHandShakeRequestMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getUser(com.services.Service.UserRequest request,
        io.grpc.stub.StreamObserver<com.services.Service.UserResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetUserMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void blockUser(com.services.UserOuterClass.BlockRequest request,
        io.grpc.stub.StreamObserver<com.services.UserOuterClass.BlockResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getBlockUserMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateUser(com.services.UserOuterClass.User request,
        io.grpc.stub.StreamObserver<com.services.UserOuterClass.User> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateUserMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service Services.
   */
  public static final class ServicesBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ServicesBlockingStub> {
    private ServicesBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServicesBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ServicesBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.services.Login.LoginResponse login(com.services.Login.LoginRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getLoginMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.services.Register.RegisterResponse register(com.services.Register.RegisterRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRegisterMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.services.Auth.AuthResponse otp(com.services.Otp.OtpRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getOtpMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.services.ContactOuterClass.ContactsList validateContacts(com.services.ContactOuterClass.ContactsList request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getValidateContactsMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.services.Message.Event send(com.services.Message.Event request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSendMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.services.Message.MessageUpdateResponse messageUpdate(com.services.Message.MessageUpdateRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getMessageUpdateMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.services.Auth.AuthResponse verifyToken(com.services.Auth.AuthRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getVerifyTokenMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.services.UserOuterClass.User savePubKey(com.services.UserOuterClass.User request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSavePubKeyMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.services.Message.Event handShakeRequest(com.services.Message.Event request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getHandShakeRequestMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.services.Service.UserResponse getUser(com.services.Service.UserRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.services.UserOuterClass.BlockResponse blockUser(com.services.UserOuterClass.BlockRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getBlockUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.services.UserOuterClass.User updateUser(com.services.UserOuterClass.User request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateUserMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service Services.
   */
  public static final class ServicesFutureStub
      extends io.grpc.stub.AbstractFutureStub<ServicesFutureStub> {
    private ServicesFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServicesFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ServicesFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.services.Login.LoginResponse> login(
        com.services.Login.LoginRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getLoginMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.services.Register.RegisterResponse> register(
        com.services.Register.RegisterRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRegisterMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.services.Auth.AuthResponse> otp(
        com.services.Otp.OtpRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getOtpMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.services.ContactOuterClass.ContactsList> validateContacts(
        com.services.ContactOuterClass.ContactsList request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getValidateContactsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.services.Message.Event> send(
        com.services.Message.Event request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSendMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.services.Message.MessageUpdateResponse> messageUpdate(
        com.services.Message.MessageUpdateRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getMessageUpdateMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.services.Auth.AuthResponse> verifyToken(
        com.services.Auth.AuthRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getVerifyTokenMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.services.UserOuterClass.User> savePubKey(
        com.services.UserOuterClass.User request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSavePubKeyMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.services.Message.Event> handShakeRequest(
        com.services.Message.Event request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getHandShakeRequestMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.services.Service.UserResponse> getUser(
        com.services.Service.UserRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetUserMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.services.UserOuterClass.BlockResponse> blockUser(
        com.services.UserOuterClass.BlockRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getBlockUserMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.services.UserOuterClass.User> updateUser(
        com.services.UserOuterClass.User request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateUserMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_LOGIN = 0;
  private static final int METHODID_REGISTER = 1;
  private static final int METHODID_OTP = 2;
  private static final int METHODID_VALIDATE_CONTACTS = 3;
  private static final int METHODID_SEND = 4;
  private static final int METHODID_MESSAGE_UPDATE = 5;
  private static final int METHODID_VERIFY_TOKEN = 6;
  private static final int METHODID_SAVE_PUB_KEY = 7;
  private static final int METHODID_HAND_SHAKE_REQUEST = 8;
  private static final int METHODID_GET_USER = 9;
  private static final int METHODID_BLOCK_USER = 10;
  private static final int METHODID_UPDATE_USER = 11;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LOGIN:
          serviceImpl.login((com.services.Login.LoginRequest) request,
              (io.grpc.stub.StreamObserver<com.services.Login.LoginResponse>) responseObserver);
          break;
        case METHODID_REGISTER:
          serviceImpl.register((com.services.Register.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<com.services.Register.RegisterResponse>) responseObserver);
          break;
        case METHODID_OTP:
          serviceImpl.otp((com.services.Otp.OtpRequest) request,
              (io.grpc.stub.StreamObserver<com.services.Auth.AuthResponse>) responseObserver);
          break;
        case METHODID_VALIDATE_CONTACTS:
          serviceImpl.validateContacts((com.services.ContactOuterClass.ContactsList) request,
              (io.grpc.stub.StreamObserver<com.services.ContactOuterClass.ContactsList>) responseObserver);
          break;
        case METHODID_SEND:
          serviceImpl.send((com.services.Message.Event) request,
              (io.grpc.stub.StreamObserver<com.services.Message.Event>) responseObserver);
          break;
        case METHODID_MESSAGE_UPDATE:
          serviceImpl.messageUpdate((com.services.Message.MessageUpdateRequest) request,
              (io.grpc.stub.StreamObserver<com.services.Message.MessageUpdateResponse>) responseObserver);
          break;
        case METHODID_VERIFY_TOKEN:
          serviceImpl.verifyToken((com.services.Auth.AuthRequest) request,
              (io.grpc.stub.StreamObserver<com.services.Auth.AuthResponse>) responseObserver);
          break;
        case METHODID_SAVE_PUB_KEY:
          serviceImpl.savePubKey((com.services.UserOuterClass.User) request,
              (io.grpc.stub.StreamObserver<com.services.UserOuterClass.User>) responseObserver);
          break;
        case METHODID_HAND_SHAKE_REQUEST:
          serviceImpl.handShakeRequest((com.services.Message.Event) request,
              (io.grpc.stub.StreamObserver<com.services.Message.Event>) responseObserver);
          break;
        case METHODID_GET_USER:
          serviceImpl.getUser((com.services.Service.UserRequest) request,
              (io.grpc.stub.StreamObserver<com.services.Service.UserResponse>) responseObserver);
          break;
        case METHODID_BLOCK_USER:
          serviceImpl.blockUser((com.services.UserOuterClass.BlockRequest) request,
              (io.grpc.stub.StreamObserver<com.services.UserOuterClass.BlockResponse>) responseObserver);
          break;
        case METHODID_UPDATE_USER:
          serviceImpl.updateUser((com.services.UserOuterClass.User) request,
              (io.grpc.stub.StreamObserver<com.services.UserOuterClass.User>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getLoginMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.services.Login.LoginRequest,
              com.services.Login.LoginResponse>(
                service, METHODID_LOGIN)))
        .addMethod(
          getRegisterMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.services.Register.RegisterRequest,
              com.services.Register.RegisterResponse>(
                service, METHODID_REGISTER)))
        .addMethod(
          getOtpMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.services.Otp.OtpRequest,
              com.services.Auth.AuthResponse>(
                service, METHODID_OTP)))
        .addMethod(
          getValidateContactsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.services.ContactOuterClass.ContactsList,
              com.services.ContactOuterClass.ContactsList>(
                service, METHODID_VALIDATE_CONTACTS)))
        .addMethod(
          getSendMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.services.Message.Event,
              com.services.Message.Event>(
                service, METHODID_SEND)))
        .addMethod(
          getMessageUpdateMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.services.Message.MessageUpdateRequest,
              com.services.Message.MessageUpdateResponse>(
                service, METHODID_MESSAGE_UPDATE)))
        .addMethod(
          getVerifyTokenMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.services.Auth.AuthRequest,
              com.services.Auth.AuthResponse>(
                service, METHODID_VERIFY_TOKEN)))
        .addMethod(
          getSavePubKeyMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.services.UserOuterClass.User,
              com.services.UserOuterClass.User>(
                service, METHODID_SAVE_PUB_KEY)))
        .addMethod(
          getHandShakeRequestMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.services.Message.Event,
              com.services.Message.Event>(
                service, METHODID_HAND_SHAKE_REQUEST)))
        .addMethod(
          getGetUserMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.services.Service.UserRequest,
              com.services.Service.UserResponse>(
                service, METHODID_GET_USER)))
        .addMethod(
          getBlockUserMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.services.UserOuterClass.BlockRequest,
              com.services.UserOuterClass.BlockResponse>(
                service, METHODID_BLOCK_USER)))
        .addMethod(
          getUpdateUserMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.services.UserOuterClass.User,
              com.services.UserOuterClass.User>(
                service, METHODID_UPDATE_USER)))
        .build();
  }

  private static abstract class ServicesBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ServicesBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.services.Service.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Services");
    }
  }

  private static final class ServicesFileDescriptorSupplier
      extends ServicesBaseDescriptorSupplier {
    ServicesFileDescriptorSupplier() {}
  }

  private static final class ServicesMethodDescriptorSupplier
      extends ServicesBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ServicesMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ServicesGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ServicesFileDescriptorSupplier())
              .addMethod(getLoginMethod())
              .addMethod(getRegisterMethod())
              .addMethod(getOtpMethod())
              .addMethod(getValidateContactsMethod())
              .addMethod(getSendMethod())
              .addMethod(getMessageUpdateMethod())
              .addMethod(getVerifyTokenMethod())
              .addMethod(getSavePubKeyMethod())
              .addMethod(getHandShakeRequestMethod())
              .addMethod(getGetUserMethod())
              .addMethod(getBlockUserMethod())
              .addMethod(getUpdateUserMethod())
              .build();
        }
      }
    }
    return result;
  }
}
