import type * as grpc from '@grpc/grpc-js';
import type { EnumTypeDefinition, MessageTypeDefinition } from '@grpc/proto-loader';


type SubtypeConstructor<Constructor extends new (...args: any) => any, Subtype> = {
  new(...args: ConstructorParameters<Constructor>): Subtype;
};

export interface ProtoGrpcType {
  ContentType: EnumTypeDefinition
  Event: MessageTypeDefinition
  EventType: EnumTypeDefinition
  KeyExchange: MessageTypeDefinition
  MessageInfo: MessageTypeDefinition
  MessageRequest: MessageTypeDefinition
  MessageResponse: MessageTypeDefinition
  MessageType: EnumTypeDefinition
  MessageUpdateRequest: MessageTypeDefinition
  MessageUpdateResponse: MessageTypeDefinition
}

