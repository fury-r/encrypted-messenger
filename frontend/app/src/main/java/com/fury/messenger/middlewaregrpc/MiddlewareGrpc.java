// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: middleware/middleware.proto

package com.fury.messenger.middlewaregrpc;

public final class MiddlewareGrpc {
  private MiddlewareGrpc() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_middleware_PingRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_middleware_PingRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_middleware_PingResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_middleware_PingResponse_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\033middleware/middleware.proto\022\016com.middl" +
      "eware\"\036\n\013PingRequest\022\017\n\007message\030\001 \001(\t\"\037\n" +
      "\014PingResponse\022\017\n\007message\030\001 \001(\t2O\n\nMiddle" +
      "ware\022A\n\004Ping\022\033.com.middleware.PingReques" +
      "t\032\034.com.middleware.PingResponseB5\n!com.f" +
      "ury.messenger.middlewaregrpcB\016Middleware" +
      "GrpcP\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_com_middleware_PingRequest_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_middleware_PingRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_middleware_PingRequest_descriptor,
        new java.lang.String[] { "Message", });
    internal_static_com_middleware_PingResponse_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_com_middleware_PingResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_middleware_PingResponse_descriptor,
        new java.lang.String[] { "Message", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
