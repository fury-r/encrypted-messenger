// Original file: ../protobuf/service/service.proto

import type * as grpc from "@grpc/grpc-js";
import type { MethodDefinition } from "@grpc/proto-loader";
import type {
  LoginRequest as _LoginRequest,
  LoginRequest__Output as _LoginRequest__Output,
} from "../../LoginRequest";
import type {
  LoginResponse as _LoginResponse,
  LoginResponse__Output as _LoginResponse__Output,
} from "../../LoginResponse";
import type {
  OtpRequest as _OtpRequest,
  OtpRequest__Output as _OtpRequest__Output,
} from "../../OtpRequest";
import type {
  OtpResponse as _OtpResponse,
  OtpResponse__Output as _OtpResponse__Output,
} from "../../OtpResponse";
import type {
  RegisterRequest as _RegisterRequest,
  RegisterRequest__Output as _RegisterRequest__Output,
} from "../../RegisterRequest";
import type {
  RegisterResponse as _RegisterResponse,
  RegisterResponse__Output as _RegisterResponse__Output,
} from "../../RegisterResponse";

export interface ServicesClient extends grpc.Client {
  Login(
    argument: _LoginRequest,
    metadata: grpc.Metadata,
    options: grpc.CallOptions,
    callback: grpc.requestCallback<_LoginResponse__Output>
  ): grpc.ClientUnaryCall;
  Login(
    argument: _LoginRequest,
    metadata: grpc.Metadata,
    callback: grpc.requestCallback<_LoginResponse__Output>
  ): grpc.ClientUnaryCall;
  Login(
    argument: _LoginRequest,
    options: grpc.CallOptions,
    callback: grpc.requestCallback<_LoginResponse__Output>
  ): grpc.ClientUnaryCall;
  Login(
    argument: _LoginRequest,
    callback: grpc.requestCallback<_LoginResponse__Output>
  ): grpc.ClientUnaryCall;
  login(
    argument: _LoginRequest,
    metadata: grpc.Metadata,
    options: grpc.CallOptions,
    callback: grpc.requestCallback<_LoginResponse__Output>
  ): grpc.ClientUnaryCall;
  login(
    argument: _LoginRequest,
    metadata: grpc.Metadata,
    callback: grpc.requestCallback<_LoginResponse__Output>
  ): grpc.ClientUnaryCall;
  login(
    argument: _LoginRequest,
    options: grpc.CallOptions,
    callback: grpc.requestCallback<_LoginResponse__Output>
  ): grpc.ClientUnaryCall;
  login(
    argument: _LoginRequest,
    callback: grpc.requestCallback<_LoginResponse__Output>
  ): grpc.ClientUnaryCall;

  Otp(
    argument: _OtpRequest,
    metadata: grpc.Metadata,
    options: grpc.CallOptions,
    callback: grpc.requestCallback<_OtpResponse__Output>
  ): grpc.ClientUnaryCall;
  Otp(
    argument: _OtpRequest,
    metadata: grpc.Metadata,
    callback: grpc.requestCallback<_OtpResponse__Output>
  ): grpc.ClientUnaryCall;
  Otp(
    argument: _OtpRequest,
    options: grpc.CallOptions,
    callback: grpc.requestCallback<_OtpResponse__Output>
  ): grpc.ClientUnaryCall;
  Otp(
    argument: _OtpRequest,
    callback: grpc.requestCallback<_OtpResponse__Output>
  ): grpc.ClientUnaryCall;
  otp(
    argument: _OtpRequest,
    metadata: grpc.Metadata,
    options: grpc.CallOptions,
    callback: grpc.requestCallback<_OtpResponse__Output>
  ): grpc.ClientUnaryCall;
  otp(
    argument: _OtpRequest,
    metadata: grpc.Metadata,
    callback: grpc.requestCallback<_OtpResponse__Output>
  ): grpc.ClientUnaryCall;
  otp(
    argument: _OtpRequest,
    options: grpc.CallOptions,
    callback: grpc.requestCallback<_OtpResponse__Output>
  ): grpc.ClientUnaryCall;
  otp(
    argument: _OtpRequest,
    callback: grpc.requestCallback<_OtpResponse__Output>
  ): grpc.ClientUnaryCall;

  Register(
    argument: _RegisterRequest,
    metadata: grpc.Metadata,
    options: grpc.CallOptions,
    callback: grpc.requestCallback<_RegisterResponse__Output>
  ): grpc.ClientUnaryCall;
  Register(
    argument: _RegisterRequest,
    metadata: grpc.Metadata,
    callback: grpc.requestCallback<_RegisterResponse__Output>
  ): grpc.ClientUnaryCall;
  Register(
    argument: _RegisterRequest,
    options: grpc.CallOptions,
    callback: grpc.requestCallback<_RegisterResponse__Output>
  ): grpc.ClientUnaryCall;
  Register(
    argument: _RegisterRequest,
    callback: grpc.requestCallback<_RegisterResponse__Output>
  ): grpc.ClientUnaryCall;
  register(
    argument: _RegisterRequest,
    metadata: grpc.Metadata,
    options: grpc.CallOptions,
    callback: grpc.requestCallback<_RegisterResponse__Output>
  ): grpc.ClientUnaryCall;
  register(
    argument: _RegisterRequest,
    metadata: grpc.Metadata,
    callback: grpc.requestCallback<_RegisterResponse__Output>
  ): grpc.ClientUnaryCall;
  register(
    argument: _RegisterRequest,
    options: grpc.CallOptions,
    callback: grpc.requestCallback<_RegisterResponse__Output>
  ): grpc.ClientUnaryCall;
  register(
    argument: _RegisterRequest,
    callback: grpc.requestCallback<_RegisterResponse__Output>
  ): grpc.ClientUnaryCall;
}

export interface ServicesHandlers extends grpc.UntypedServiceImplementation {
  Login: grpc.handleUnaryCall<_LoginRequest__Output, _LoginResponse>;

  Otp: grpc.handleUnaryCall<_OtpRequest__Output, _OtpResponse>;

  Register: grpc.handleUnaryCall<_RegisterRequest__Output, _RegisterResponse>;
}

export interface ServicesDefinition extends grpc.ServiceDefinition {
  Login: MethodDefinition<
    _LoginRequest,
    _LoginResponse,
    _LoginRequest__Output,
    _LoginResponse__Output
  >;
  Otp: MethodDefinition<
    _OtpRequest,
    _OtpResponse,
    _OtpRequest__Output,
    _OtpResponse__Output
  >;
  Register: MethodDefinition<
    _RegisterRequest,
    _RegisterResponse,
    _RegisterRequest__Output,
    _RegisterResponse__Output
  >;
}
