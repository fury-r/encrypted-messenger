import type { MessageTypeDefinition } from '@grpc/proto-loader';


type SubtypeConstructor<Constructor extends new (...args: any) => any, Subtype> = {
  new(...args: ConstructorParameters<Constructor>): Subtype;
};

export interface ProtoGrpcType {
  OtpRequest: MessageTypeDefinition
  OtpResponse: MessageTypeDefinition
  currentUser: MessageTypeDefinition
}

