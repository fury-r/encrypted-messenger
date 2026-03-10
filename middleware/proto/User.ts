// Original file: ../protobuf/service/user.proto

import type { Timestamp as _google_protobuf_Timestamp, Timestamp__Output as _google_protobuf_Timestamp__Output } from './google/protobuf/Timestamp';

export interface User {
  'username'?: (string);
  'password'?: (string);
  'email'?: (string);
  'phoneNumber'?: (string);
  'countryCode'?: (string);
  'updatedAt'?: (_google_protobuf_Timestamp | null);
  'uuid'?: (string);
  'token'?: (string);
  'pubKey'?: (string);
  'blockedUsers'?: (string)[];
  'status'?: (string);
  'image'?: (string);
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
  'status'?: (string);
  'image'?: (string);
}
