import type * as grpc from '@grpc/grpc-js';
import type { MessageTypeDefinition } from '@grpc/proto-loader';


type SubtypeConstructor<Constructor extends new (...args: any) => any, Subtype> = {
  new(...args: ConstructorParameters<Constructor>): Subtype;
};

export interface ProtoGrpcType {
  BlockRequest: MessageTypeDefinition
  BlockResponse: MessageTypeDefinition
  User: MessageTypeDefinition
  google: {
    protobuf: {
      Timestamp: MessageTypeDefinition
    }
  }
}

dUsers'?: (string)[];
  '_username'?: "username";
  '_password'?: "password";
  '_email'?: "email";
  '_countryCode'?: "countryCode";
  '_updatedAt'?: "updatedAt";
  '_pubKey'?: "pubKey";
}

export interface User__Output {
  'username'?: (string);
  'password'?: (string);
  'email'?: (string);
  'phoneNumber'?: (string);
  'countryCode'?: (string);
  'updatedAt'?: (_google_protobuf_Timestamp__Output);
  'uuid'?: (string);
  'token'?: (string);
  'pubKey'?: (string);
  'blockedUsers'?: (string)[];
}
