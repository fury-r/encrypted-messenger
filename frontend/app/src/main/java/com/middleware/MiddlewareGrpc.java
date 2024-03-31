package com.middleware;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.56.0)",
    comments = "Source: middleware/middleware.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class MiddlewareGrpc {

  private MiddlewareGrpc() {}

  public static final String SERVICE_NAME = "com.middleware.Middleware";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.middleware.PingRequest,
      com.middleware.PingResponse> getPingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Ping",
      requestType = com.middleware.PingRequest.class,
      responseType = com.middleware.PingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.middleware.PingRequest,
      com.middleware.PingResponse> getPingMethod() {
    io.grpc.MethodDescriptor<com.middleware.PingRequest, com.middleware.PingResponse> getPingMethod;
    if ((getPingMethod = MiddlewareGrpc.getPingMethod) == null) {
      synchronized (MiddlewareGrpc.class) {
        if ((getPingMethod = MiddlewareGrpc.getPingMethod) == null) {
          MiddlewareGrpc.getPingMethod = getPingMethod =
              io.grpc.MethodDescriptor.<com.middleware.PingRequest, com.middleware.PingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Ping"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.middleware.PingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.middleware.PingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MiddlewareMethodDescriptorSupplier("Ping"))
              .build();
        }
      }
    }
    return getPingMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MiddlewareStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MiddlewareStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MiddlewareStub>() {
        @java.lang.Override
        public MiddlewareStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MiddlewareStub(channel, callOptions);
        }
      };
    return MiddlewareStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MiddlewareBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MiddlewareBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MiddlewareBlockingStub>() {
        @java.lang.Override
        public MiddlewareBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MiddlewareBlockingStub(channel, callOptions);
        }
      };
    return MiddlewareBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MiddlewareFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MiddlewareFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MiddlewareFutureStub>() {
        @java.lang.Override
        public MiddlewareFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MiddlewareFutureStub(channel, callOptions);
        }
      };
    return MiddlewareFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void ping(com.middleware.PingRequest request,
        io.grpc.stub.StreamObserver<com.middleware.PingResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPingMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service Middleware.
   */
  public static abstract class MiddlewareImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return MiddlewareGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service Middleware.
   */
  public static final class MiddlewareStub
      extends io.grpc.stub.AbstractAsyncStub<MiddlewareStub> {
    private MiddlewareStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MiddlewareStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MiddlewareStub(channel, callOptions);
    }

    /**
     */
    public void ping(com.middleware.PingRequest request,
        io.grpc.stub.StreamObserver<com.middleware.PingResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service Middleware.
   */
  public static final class MiddlewareBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<MiddlewareBlockingStub> {
    private MiddlewareBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MiddlewareBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MiddlewareBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.middleware.PingResponse ping(com.middleware.PingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPingMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service Middleware.
   */
  public static final class MiddlewareFutureStub
      extends io.grpc.stub.AbstractFutureStub<MiddlewareFutureStub> {
    private MiddlewareFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MiddlewareFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MiddlewareFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.middleware.PingResponse> ping(
        com.middleware.PingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PING = 0;

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
        case METHODID_PING:
          serviceImpl.ping((com.middleware.PingRequest) request,
              (io.grpc.stub.StreamObserver<com.middleware.PingResponse>) responseObserver);
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
          getPingMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.middleware.PingRequest,
              com.middleware.PingResponse>(
                service, METHODID_PING)))
        .build();
  }

  private static abstract class MiddlewareBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MiddlewareBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.middleware.Middlewarec.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Middleware");
    }
  }

  private static final class MiddlewareFileDescriptorSupplier
      extends MiddlewareBaseDescriptorSupplier {
    MiddlewareFileDescriptorSupplier() {}
  }

  private static final class MiddlewareMethodDescriptorSupplier
      extends MiddlewareBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    MiddlewareMethodDescriptorSupplier(String methodName) {
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
      synchronized (MiddlewareGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MiddlewareFileDescriptorSupplier())
              .addMethod(getPingMethod())
              .build();
        }
      }
    }
    return result;
  }
}
