// Original file: ../protobuf/service/test.proto

import type * as grpc from '@grpc/grpc-js'
import type { MethodDefinition } from '@grpc/proto-loader'
import type { PingRequest as _com_service_PingRequest, PingRequest__Output as _com_service_PingRequest__Output } from '../../com/service/PingRequest';
import type { PingResponse as _com_service_PingResponse, PingResponse__Output as _com_service_PingResponse__Output } from '../../com/service/PingResponse';

export interface TestMiddlewareClient extends grpc.Client {
  Ping(argument: _com_service_PingRequest, metadata: grpc.Metadata, options: grpc.CallOptions, callback: grpc.requestCallback<_com_service_PingResponse__Output>): grpc.ClientUnaryCall;
  Ping(argument: _com_service_PingRequest, metadata: grpc.Metadata, callback: grpc.requestCallback<_com_service_PingResponse__Output>): grpc.ClientUnaryCall;
  Ping(argument: _com_service_PingRequest, options: grpc.CallOptions, callback: grpc.requestCallback<_com_service_PingResponse__Output>): grpc.ClientUnaryCall;
  Ping(argument: _com_service_PingRequest, callback: grpc.requestCallback<_com_service_PingResponse__Output>): grpc.ClientUnaryCall;
  ping(argument: _com_service_PingRequest, metadata: grpc.Metadata, options: grpc.CallOptions, callback: grpc.requestCallback<_com_service_PingResponse__Output>): grpc.ClientUnaryCall;
  ping(argument: _com_service_PingRequest, metadata: grpc.Metadata, callback: grpc.requestCallback<_com_service_PingResponse__Output>): grpc.ClientUnaryCall;
  ping(argument: _com_service_PingRequest, options: grpc.CallOptions, callback: grpc.requestCallback<_com_service_PingResponse__Output>): grpc.ClientUnaryCall;
  ping(argument: _com_service_PingRequest, callback: grpc.requestCallback<_com_service_PingResponse__Output>): grpc.ClientUnaryCall;
  
}

export interface TestMiddlewareHandlers extends grpc.UntypedServiceImplementation {
  Ping: grpc.handleUnaryCall<_com_service_PingRequest__Output, _com_service_PingResponse>;
  
}

export interface TestMiddlewareDefinition extends grpc.ServiceDefinition {
  Ping: MethodDefinition<_com_service_PingRequest, _com_service_PingResponse, _com_service_PingRequest__Output, _com_service_PingResponse__Output>
}
