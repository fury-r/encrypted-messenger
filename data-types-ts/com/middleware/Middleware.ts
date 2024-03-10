// Original file: protobuf/middleware/middleware.proto

import type * as grpc from '@grpc/grpc-js'
import type { MethodDefinition } from '@grpc/proto-loader'
import type { PingRequest as _com_middleware_PingRequest, PingRequest__Output as _com_middleware_PingRequest__Output } from '../../com/middleware/PingRequest';
import type { PingResponse as _com_middleware_PingResponse, PingResponse__Output as _com_middleware_PingResponse__Output } from '../../com/middleware/PingResponse';

export interface MiddlewareClient extends grpc.Client {
  Ping(argument: _com_middleware_PingRequest, metadata: grpc.Metadata, options: grpc.CallOptions, callback: grpc.requestCallback<_com_middleware_PingResponse__Output>): grpc.ClientUnaryCall;
  Ping(argument: _com_middleware_PingRequest, metadata: grpc.Metadata, callback: grpc.requestCallback<_com_middleware_PingResponse__Output>): grpc.ClientUnaryCall;
  Ping(argument: _com_middleware_PingRequest, options: grpc.CallOptions, callback: grpc.requestCallback<_com_middleware_PingResponse__Output>): grpc.ClientUnaryCall;
  Ping(argument: _com_middleware_PingRequest, callback: grpc.requestCallback<_com_middleware_PingResponse__Output>): grpc.ClientUnaryCall;
  ping(argument: _com_middleware_PingRequest, metadata: grpc.Metadata, options: grpc.CallOptions, callback: grpc.requestCallback<_com_middleware_PingResponse__Output>): grpc.ClientUnaryCall;
  ping(argument: _com_middleware_PingRequest, metadata: grpc.Metadata, callback: grpc.requestCallback<_com_middleware_PingResponse__Output>): grpc.ClientUnaryCall;
  ping(argument: _com_middleware_PingRequest, options: grpc.CallOptions, callback: grpc.requestCallback<_com_middleware_PingResponse__Output>): grpc.ClientUnaryCall;
  ping(argument: _com_middleware_PingRequest, callback: grpc.requestCallback<_com_middleware_PingResponse__Output>): grpc.ClientUnaryCall;
  
}

export interface MiddlewareHandlers extends grpc.UntypedServiceImplementation {
  Ping: grpc.handleUnaryCall<_com_middleware_PingRequest__Output, _com_middleware_PingResponse>;
  
}

export interface MiddlewareDefinition extends grpc.ServiceDefinition {
  Ping: MethodDefinition<_com_middleware_PingRequest, _com_middleware_PingResponse, _com_middleware_PingRequest__Output, _com_middleware_PingResponse__Output>
}
