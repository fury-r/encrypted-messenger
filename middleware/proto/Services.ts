// Original file: ../protobuf/service/service.proto

import type * as grpc from '@grpc/grpc-js'
import type { MethodDefinition } from '@grpc/proto-loader'
import type { AuthRequest as _AuthRequest, AuthRequest__Output as _AuthRequest__Output } from './AuthRequest';
import type { AuthResponse as _AuthResponse, AuthResponse__Output as _AuthResponse__Output } from './AuthResponse';
import type { ContactsList as _ContactsList, ContactsList__Output as _ContactsList__Output } from './ContactsList';
import type { Event as _Event, Event__Output as _Event__Output } from './Event';
import type { LoginRequest as _LoginRequest, LoginRequest__Output as _LoginRequest__Output } from './LoginRequest';
import type { LoginResponse as _LoginResponse, LoginResponse__Output as _LoginResponse__Output } from './LoginResponse';
import type { MessageUpdateRequest as _MessageUpdateRequest, MessageUpdateRequest__Output as _MessageUpdateRequest__Output } from './MessageUpdateRequest';
import type { MessageUpdateResponse as _MessageUpdateResponse, MessageUpdateResponse__Output as _MessageUpdateResponse__Output } from './MessageUpdateResponse';
import type { OtpRequest as _OtpRequest, OtpRequest__Output as _OtpRequest__Output } from './OtpRequest';
import type { OtpResponse as _OtpResponse, OtpResponse__Output as _OtpResponse__Output } from './OtpResponse';
import type { RegisterRequest as _RegisterRequest, RegisterRequest__Output as _RegisterRequest__Output } from './RegisterRequest';
import type { RegisterResponse as _RegisterResponse, RegisterResponse__Output as _RegisterResponse__Output } from './RegisterResponse';
import type { User as _User, User__Output as _User__Output } from './User';
import type { UserRequest as _UserRequest, UserRequest__Output as _UserRequest__Output } from './UserRequest';
import type { UserResponse as _UserResponse, UserResponse__Output as _UserResponse__Output } from './UserResponse';

export interface ServicesClient extends grpc.Client {
  Login(argument: _LoginRequest, metadata: grpc.Metadata, options: grpc.CallOptions, callback: grpc.requestCallback<_LoginResponse__Output>): grpc.ClientUnaryCall;
  Login(argument: _LoginRequest, metadata: grpc.Metadata, callback: grpc.requestCallback<_LoginResponse__Output>): grpc.ClientUnaryCall;
  Login(argument: _LoginRequest, options: grpc.CallOptions, callback: grpc.requestCallback<_LoginResponse__Output>): grpc.ClientUnaryCall;
  Login(argument: _LoginRequest, callback: grpc.requestCallback<_LoginResponse__Output>): grpc.ClientUnaryCall;
  login(argument: _LoginRequest, metadata: grpc.Metadata, options: grpc.CallOptions, callback: grpc.requestCallback<_LoginResponse__Output>): grpc.ClientUnaryCall;
  login(argument: _LoginRequest, metadata: grpc.Metadata, callback: grpc.requestCallback<_LoginResponse__Output>): grpc.ClientUnaryCall;
  login(argument: _LoginRequest, options: grpc.CallOptions, callback: grpc.requestCallback<_LoginResponse__Output>): grpc.ClientUnaryCall;
  login(argument: _LoginRequest, callback: grpc.requestCallback<_LoginResponse__Output>): grpc.ClientUnaryCall;
  
  Otp(argument: _OtpRequest, metadata: grpc.Metadata, options: grpc.CallOptions, callback: grpc.requestCallback<_OtpResponse__Output>): grpc.ClientUnaryCall;
  Otp(argument: _OtpRequest, metadata: grpc.Metadata, callback: grpc.requestCallback<_OtpResponse__Output>): grpc.ClientUnaryCall;
  Otp(argument: _OtpRequest, options: grpc.CallOptions, callback: grpc.requestCallback<_OtpResponse__Output>): grpc.ClientUnaryCall;
  Otp(argument: _OtpRequest, callback: grpc.requestCallback<_OtpResponse__Output>): grpc.ClientUnaryCall;
  otp(argument: _OtpRequest, metadata: grpc.Metadata, options: grpc.CallOptions, callback: grpc.requestCallback<_OtpResponse__Output>): grpc.ClientUnaryCall;
  otp(argument: _OtpRequest, metadata: grpc.Metadata, callback: grpc.requestCallback<_OtpResponse__Output>): grpc.ClientUnaryCall;
  otp(argument: _OtpRequest, options: grpc.CallOptions, callback: grpc.requestCallback<_OtpResponse__Output>): grpc.ClientUnaryCall;
  otp(argument: _OtpRequest, callback: grpc.requestCallback<_OtpResponse__Output>): grpc.ClientUnaryCall;
  
  Register(argument: _RegisterRequest, metadata: grpc.Metadata, options: grpc.CallOptions, callback: grpc.requestCallback<_RegisterResponse__Output>): grpc.ClientUnaryCall;
  Register(argument: _RegisterRequest, metadata: grpc.Metadata, callback: grpc.requestCallback<_RegisterResponse__Output>): grpc.ClientUnaryCall;
  Register(argument: _RegisterRequest, options: grpc.CallOptions, callback: grpc.requestCallback<_RegisterResponse__Output>): grpc.ClientUnaryCall;
  Register(argument: _RegisterRequest, callback: grpc.requestCallback<_RegisterResponse__Output>): grpc.ClientUnaryCall;
  register(argument: _RegisterRequest, metadata: grpc.Metadata, options: grpc.CallOptions, callback: grpc.requestCallback<_RegisterResponse__Output>): grpc.ClientUnaryCall;
  register(argument: _RegisterRequest, metadata: grpc.Metadata, callback: grpc.requestCallback<_RegisterResponse__Output>): grpc.ClientUnaryCall;
  register(argument: _RegisterRequest, options: grpc.CallOptions, callback: grpc.requestCallback<_RegisterResponse__Output>): grpc.ClientUnaryCall;
  register(argument: _RegisterRequest, callback: grpc.requestCallback<_RegisterResponse__Output>): grpc.ClientUnaryCall;
  
  Send(argument: _Event, metadata: grpc.Metadata, options: grpc.CallOptions, callback: grpc.requestCallback<_Event__Output>): grpc.ClientUnaryCall;
  Send(argument: _Event, metadata: grpc.Metadata, callback: grpc.requestCallback<_Event__Output>): grpc.ClientUnaryCall;
  Send(argument: _Event, options: grpc.CallOptions, callback: grpc.requestCallback<_Event__Output>): grpc.ClientUnaryCall;
  Send(argument: _Event, callback: grpc.requestCallback<_Event__Output>): grpc.ClientUnaryCall;
  send(argument: _Event, metadata: grpc.Metadata, options: grpc.CallOptions, callback: grpc.requestCallback<_Event__Output>): grpc.ClientUnaryCall;
  send(argument: _Event, metadata: grpc.Metadata, callback: grpc.requestCallback<_Event__Output>): grpc.ClientUnaryCall;
  send(argument: _Event, options: grpc.CallOptions, callback: grpc.requestCallback<_Event__Output>): grpc.ClientUnaryCall;
  send(argument: _Event, callback: grpc.requestCallback<_Event__Output>): grpc.ClientUnaryCall;
  
  ValidateContacts(argument: _ContactsList, metadata: grpc.Metadata, options: grpc.CallOptions, callback: grpc.requestCallback<_ContactsList__Output>): grpc.ClientUnaryCall;
  ValidateContacts(argument: _ContactsList, metadata: grpc.Metadata, callback: grpc.requestCallback<_ContactsList__Output>): grpc.ClientUnaryCall;
  ValidateContacts(argument: _ContactsList, options: grpc.CallOptions, callback: grpc.requestCallback<_ContactsList__Output>): grpc.ClientUnaryCall;
  ValidateContacts(argument: _ContactsList, callback: grpc.requestCallback<_ContactsList__Output>): grpc.ClientUnaryCall;
  validateContacts(argument: _ContactsList, metadata: grpc.Metadata, options: grpc.CallOptions, callback: grpc.requestCallback<_ContactsList__Output>): grpc.ClientUnaryCall;
  validateContacts(argument: _ContactsList, metadata: grpc.Metadata, callback: grpc.requestCallback<_ContactsList__Output>): grpc.ClientUnaryCall;
  validateContacts(argument: _ContactsList, options: grpc.CallOptions, callback: grpc.requestCallback<_ContactsList__Output>): grpc.ClientUnaryCall;
  validateContacts(argument: _ContactsList, callback: grpc.requestCallback<_ContactsList__Output>): grpc.ClientUnaryCall;
  
  VerifyToken(argument: _AuthRequest, metadata: grpc.Metadata, options: grpc.CallOptions, callback: grpc.requestCallback<_AuthResponse__Output>): grpc.ClientUnaryCall;
  VerifyToken(argument: _AuthRequest, metadata: grpc.Metadata, callback: grpc.requestCallback<_AuthResponse__Output>): grpc.ClientUnaryCall;
  VerifyToken(argument: _AuthRequest, options: grpc.CallOptions, callback: grpc.requestCallback<_AuthResponse__Output>): grpc.ClientUnaryCall;
  VerifyToken(argument: _AuthRequest, callback: grpc.requestCallback<_AuthResponse__Output>): grpc.ClientUnaryCall;
  verifyToken(argument: _AuthRequest, metadata: grpc.Metadata, options: grpc.CallOptions, callback: grpc.requestCallback<_AuthResponse__Output>): grpc.ClientUnaryCall;
  verifyToken(argument: _AuthRequest, metadata: grpc.Metadata, callback: grpc.requestCallback<_AuthResponse__Output>): grpc.ClientUnaryCall;
  verifyToken(argument: _AuthRequest, options: grpc.CallOptions, callback: grpc.requestCallback<_AuthResponse__Output>): grpc.ClientUnaryCall;
  verifyToken(argument: _AuthRequest, callback: grpc.requestCallback<_AuthResponse__Output>): grpc.ClientUnaryCall;
  
  getUser(argument: _UserRequest, metadata: grpc.Metadata, options: grpc.CallOptions, callback: grpc.requestCallback<_UserResponse__Output>): grpc.ClientUnaryCall;
  getUser(argument: _UserRequest, metadata: grpc.Metadata, callback: grpc.requestCallback<_UserResponse__Output>): grpc.ClientUnaryCall;
  getUser(argument: _UserRequest, options: grpc.CallOptions, callback: grpc.requestCallback<_UserResponse__Output>): grpc.ClientUnaryCall;
  getUser(argument: _UserRequest, callback: grpc.requestCallback<_UserResponse__Output>): grpc.ClientUnaryCall;
  getUser(argument: _UserRequest, metadata: grpc.Metadata, options: grpc.CallOptions, callback: grpc.requestCallback<_UserResponse__Output>): grpc.ClientUnaryCall;
  getUser(argument: _UserRequest, metadata: grpc.Metadata, callback: grpc.requestCallback<_UserResponse__Output>): grpc.ClientUnaryCall;
  getUser(argument: _UserRequest, options: grpc.CallOptions, callback: grpc.requestCallback<_UserResponse__Output>): grpc.ClientUnaryCall;
  getUser(argument: _UserRequest, callback: grpc.requestCallback<_UserResponse__Output>): grpc.ClientUnaryCall;
  
  handShakeRequest(argument: _Event, metadata: grpc.Metadata, options: grpc.CallOptions, callback: grpc.requestCallback<_Event__Output>): grpc.ClientUnaryCall;
  handShakeRequest(argument: _Event, metadata: grpc.Metadata, callback: grpc.requestCallback<_Event__Output>): grpc.ClientUnaryCall;
  handShakeRequest(argument: _Event, options: grpc.CallOptions, callback: grpc.requestCallback<_Event__Output>): grpc.ClientUnaryCall;
  handShakeRequest(argument: _Event, callback: grpc.requestCallback<_Event__Output>): grpc.ClientUnaryCall;
  handShakeRequest(argument: _Event, metadata: grpc.Metadata, options: grpc.CallOptions, callback: grpc.requestCallback<_Event__Output>): grpc.ClientUnaryCall;
  handShakeRequest(argument: _Event, metadata: grpc.Metadata, callback: grpc.requestCallback<_Event__Output>): grpc.ClientUnaryCall;
  handShakeRequest(argument: _Event, options: grpc.CallOptions, callback: grpc.requestCallback<_Event__Output>): grpc.ClientUnaryCall;
  handShakeRequest(argument: _Event, callback: grpc.requestCallback<_Event__Output>): grpc.ClientUnaryCall;
  
  messageUpdate(argument: _MessageUpdateRequest, metadata: grpc.Metadata, options: grpc.CallOptions, callback: grpc.requestCallback<_MessageUpdateResponse__Output>): grpc.ClientUnaryCall;
  messageUpdate(argument: _MessageUpdateRequest, metadata: grpc.Metadata, callback: grpc.requestCallback<_MessageUpdateResponse__Output>): grpc.ClientUnaryCall;
  messageUpdate(argument: _MessageUpdateRequest, options: grpc.CallOptions, callback: grpc.requestCallback<_MessageUpdateResponse__Output>): grpc.ClientUnaryCall;
  messageUpdate(argument: _MessageUpdateRequest, callback: grpc.requestCallback<_MessageUpdateResponse__Output>): grpc.ClientUnaryCall;
  messageUpdate(argument: _MessageUpdateRequest, metadata: grpc.Metadata, options: grpc.CallOptions, callback: grpc.requestCallback<_MessageUpdateResponse__Output>): grpc.ClientUnaryCall;
  messageUpdate(argument: _MessageUpdateRequest, metadata: grpc.Metadata, callback: grpc.requestCallback<_MessageUpdateResponse__Output>): grpc.ClientUnaryCall;
  messageUpdate(argument: _MessageUpdateRequest, options: grpc.CallOptions, callback: grpc.requestCallback<_MessageUpdateResponse__Output>): grpc.ClientUnaryCall;
  messageUpdate(argument: _MessageUpdateRequest, callback: grpc.requestCallback<_MessageUpdateResponse__Output>): grpc.ClientUnaryCall;
  
  savePubKey(argument: _User, metadata: grpc.Metadata, options: grpc.CallOptions, callback: grpc.requestCallback<_User__Output>): grpc.ClientUnaryCall;
  savePubKey(argument: _User, metadata: grpc.Metadata, callback: grpc.requestCallback<_User__Output>): grpc.ClientUnaryCall;
  savePubKey(argument: _User, options: grpc.CallOptions, callback: grpc.requestCallback<_User__Output>): grpc.ClientUnaryCall;
  savePubKey(argument: _User, callback: grpc.requestCallback<_User__Output>): grpc.ClientUnaryCall;
  savePubKey(argument: _User, metadata: grpc.Metadata, options: grpc.CallOptions, callback: grpc.requestCallback<_User__Output>): grpc.ClientUnaryCall;
  savePubKey(argument: _User, metadata: grpc.Metadata, callback: grpc.requestCallback<_User__Output>): grpc.ClientUnaryCall;
  savePubKey(argument: _User, options: grpc.CallOptions, callback: grpc.requestCallback<_User__Output>): grpc.ClientUnaryCall;
  savePubKey(argument: _User, callback: grpc.requestCallback<_User__Output>): grpc.ClientUnaryCall;
  
}

export interface ServicesHandlers extends grpc.UntypedServiceImplementation {
  Login: grpc.handleUnaryCall<_LoginRequest__Output, _LoginResponse>;
  
  Otp: grpc.handleUnaryCall<_OtpRequest__Output, _OtpResponse>;
  
  Register: grpc.handleUnaryCall<_RegisterRequest__Output, _RegisterResponse>;
  
  Send: grpc.handleUnaryCall<_Event__Output, _Event>;
  
  ValidateContacts: grpc.handleUnaryCall<_ContactsList__Output, _ContactsList>;
  
  VerifyToken: grpc.handleUnaryCall<_AuthRequest__Output, _AuthResponse>;
  
  getUser: grpc.handleUnaryCall<_UserRequest__Output, _UserResponse>;
  
  handShakeRequest: grpc.handleUnaryCall<_Event__Output, _Event>;
  
  messageUpdate: grpc.handleUnaryCall<_MessageUpdateRequest__Output, _MessageUpdateResponse>;
  
  savePubKey: grpc.handleUnaryCall<_User__Output, _User>;
  
}

export interface ServicesDefinition extends grpc.ServiceDefinition {
  Login: MethodDefinition<_LoginRequest, _LoginResponse, _LoginRequest__Output, _LoginResponse__Output>
  Otp: MethodDefinition<_OtpRequest, _OtpResponse, _OtpRequest__Output, _OtpResponse__Output>
  Register: MethodDefinition<_RegisterRequest, _RegisterResponse, _RegisterRequest__Output, _RegisterResponse__Output>
  Send: MethodDefinition<_Event, _Event, _Event__Output, _Event__Output>
  ValidateContacts: MethodDefinition<_ContactsList, _ContactsList, _ContactsList__Output, _ContactsList__Output>
  VerifyToken: MethodDefinition<_AuthRequest, _AuthResponse, _AuthRequest__Output, _AuthResponse__Output>
  getUser: MethodDefinition<_UserRequest, _UserResponse, _UserRequest__Output, _UserResponse__Output>
  handShakeRequest: MethodDefinition<_Event, _Event, _Event__Output, _Event__Output>
  messageUpdate: MethodDefinition<_MessageUpdateRequest, _MessageUpdateResponse, _MessageUpdateRequest__Output, _MessageUpdateResponse__Output>
  savePubKey: MethodDefinition<_User, _User, _User__Output, _User__Output>
}
