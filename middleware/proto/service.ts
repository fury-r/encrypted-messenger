import type * as grpc from '@grpc/grpc-js';
import type { EnumTypeDefinition, MessageTypeDefinition } from '@grpc/proto-loader';

import type { ServicesClient as _ServicesClient, ServicesDefinition as _ServicesDefinition } from './Services';

type SubtypeConstructor<Constructor extends new (...args: any) => any, Subtype> = {
  new(...args: ConstructorParameters<Constructor>): Subtype;
};

export interface ProtoGrpcType {
  AuthRequest: MessageTypeDefinition
  AuthResponse: MessageTypeDefinition
  BlockRequest: MessageTypeDefinition
  BlockResponse: MessageTypeDefinition
  Contact: MessageTypeDefinition
  ContactsList: MessageTypeDefinition
  ContentType: EnumTypeDefinition
  Event: MessageTypeDefinition
  EventType: EnumTypeDefinition
  KeyExchange: MessageTypeDefinition
  LoginRequest: MessageTypeDefinition
  LoginResponse: MessageTypeDefinition
  MessageInfo: MessageTypeDefinition
  MessageRequest: MessageTypeDefinition
  MessageResponse: MessageTypeDefinition
  MessageType: EnumTypeDefinition
  MessageUpdateRequest: MessageTypeDefinition
  MessageUpdateResponse: MessageTypeDefinition
  OtpRequest: MessageTypeDefinition
  ReSendOtpRequest: MessageTypeDefinition
  RegisterRequest: MessageTypeDefinition
  RegisterResponse: MessageTypeDefinition
  Services: SubtypeConstructor<typeof grpc.Client, _ServicesClient> & { service: _ServicesDefinition }
  User: MessageTypeDefinition
  UserRequest: MessageTypeDefinition
  UserResponse: MessageTypeDefinition
  google: {
    protobuf: {
      Timestamp: MessageTypeDefinition
    }
  }
}

