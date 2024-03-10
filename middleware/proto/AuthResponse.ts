// Original file: ../protobuf/service/auth.proto

import type { User as _User, User__Output as _User__Output } from './User';

export interface AuthResponse {
  'token'?: (string);
  'isVerified'?: (boolean);
  'error'?: (string);
  'user'?: (_User | null);
  '_token'?: "token";
  '_isVerified'?: "isVerified";
  '_error'?: "error";
  '_user'?: "user";
}

export interface AuthResponse__Output {
  'token'?: (string);
  'isVerified'?: (boolean);
  'error'?: (string);
  'user'?: (_User__Output);
}
