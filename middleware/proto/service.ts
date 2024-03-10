import type * as grpc from '@grpc/grpc-js';
import type { EnumTypeDefinition, MessageTypeDefinition } from '@grpc/proto-loader';

import type { ServicesClient as _ServicesClient, ServicesDefinition as _ServicesDefinition } from './Services';

type SubtypeConstructor<Constructor extends new (...args: any) => any, Subtype> = {
  new(...args: ConstructorParameters<Constructor>): Subtype;
};

export interface ProtoGrpcType {
  AuthRequest: MessageTypeDefinition
  AuthResponse: MessageTypeDefinition
  Contact: MessageTypeDefinition
  ContactsList: MessageTypeDefinition
  Event: MessageTypeDefinition
  EventType: EnumTypeDefinition
  LoginRequest: MessageTypeDefinition
  LoginResponse: MessageTypeDefinition
  MessageInfo: MessageTypeDefinition
  MessageRequest: MessageTypeDefinition
  MessageResponse: MessageTypeDefinition
  MessageType: EnumTypeDefinition
  MessageUpdateRequest: MessageTypeDefinition
  MessageUpdateResponse: MessageTypeDefinition
  OtpRequest: MessageTypeDefinition
  OtpResponse: MessageTypeDefinition
  PubKeyExchange: MessageTypeDefinition
  RegisterRequest: MessageTypeDefinition
  RegisterResponse: MessageTypeDefinition
  Services: SubtypeConstructor<typeof grpc.Client, _ServicesClient> & { service: _ServicesDefinition }
  User: MessageTypeDefinition
  currentUser: MessageTypeDefinition
  google: {
    protobuf: {
      Timestamp: MessageTypeDefinition
    }
  }
}

