package com.service;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.56.0)",
    comments = "Source: test.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class TestMiddlewareGrpc {

  private TestMiddlewareGrpc() {}

  public static final String SERVICE_NAME = "com.service.TestMiddleware";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.service.PingRequest,
      com.service.PingResponse> getPingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Ping",
      requestType = com.service.PingRequest.class,
      responseType = com.service.PingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.service.PingRequest,
      com.service.PingResponse> getPingMethod() {
    io.grpc.MethodDescriptor<com.service.PingRequest, com.service.PingResponse> getPingMethod;
    if ((getPingMethod = TestMiddlewareGrpc.getPingMethod) == null) {
      synchronized (TestMiddlewareGrpc.class) {
        if ((getPingMethod = TestMiddlewareGrpc.getPingMethod) == null) {
          TestMiddlewareGrpc.getPingMethod = getPingMethod =
              io.grpc.MethodDescriptor.<com.service.PingRequest, com.service.PingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Ping"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.service.PingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.service.PingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TestMiddlewareMethodDescriptorSupplier("Ping"))
              .build();
        }
      }
    }
    return getPingMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TestMiddlewareStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TestMiddlewareStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TestMiddlewareStub>() {
        @java.lang.Override
        public TestMiddlewareStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TestMiddlewareStub(channel, callOptions);
        }
      };
    return TestMiddlewareStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TestMiddlewareBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TestMiddlewareBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TestMiddlewareBlockingStub>() {
        @java.lang.Override
        public TestMiddlewareBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TestMiddlewareBlockingStub(channel, callOptions);
        }
      };
    return TestMiddlewareBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TestMiddlewareFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TestMiddlewareFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TestMiddlewareFutureStub>() {
        @java.lang.Override
        public TestMiddlewareFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TestMiddlewareFutureStub(channel, callOptions);
        }
      };
    return TestMiddlewareFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void ping(com.service.PingRequest request,
        io.grpc.stub.StreamObserver<com.service.PingResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPingMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service TestMiddleware.
   */
  public static abstract class TestMiddlewareImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return TestMiddlewareGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service TestMiddleware.
   */
  public static final class TestMiddlewareStub
      extends io.grpc.stub.AbstractAsyncStub<TestMiddlewareStub> {
    private TestMiddlewareStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TestMiddlewareStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TestMiddlewareStub(channel, callOptions);
    }

    /**
     */
    public void ping(com.service.PingRequest request,
        io.grpc.stub.StreamObserver<com.service.PingResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service TestMiddleware.
   */
  public static final class TestMiddlewareBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<TestMiddlewareBlockingStub> {
    private TestMiddlewareBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TestMiddlewareBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TestMiddlewareBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.service.PingResponse ping(com.service.PingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPingMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service TestMiddleware.
   */
  public static final class TestMiddlewareFutureStub
      extends io.grpc.stub.AbstractFutureStub<TestMiddlewareFutureStub> {
    private TestMiddlewareFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TestMiddlewareFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TestMiddlewareFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.service.PingResponse> ping(
        com.service.PingRequest request) {
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
          serviceImpl.ping((com.service.PingRequest) request,
              (io.grpc.stub.StreamObserver<com.service.PingResponse>) responseObserver);
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
              com.service.PingRequest,
              com.service.PingResponse>(
                service, METHODID_PING)))
        .build();
  }

  private static abstract class TestMiddlewareBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TestMiddlewareBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.service.TestMiddlewarec.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TestMiddleware");
    }
  }

  private static final class TestMiddlewareFileDescriptorSupplier
      extends TestMiddlewareBaseDescriptorSupplier {
    TestMiddlewareFileDescriptorSupplier() {}
  }

  private static final class TestMiddlewareMethodDescriptorSupplier
      extends TestMiddlewareBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    TestMiddlewareMethodDescriptorSupplier(String methodName) {
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
      synchronized (TestMiddlewareGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TestMiddlewareFileDescriptorSupplier())
              .addMethod(getPingMethod())
              .build();
        }
      }
    }
    return result;
  }
}
