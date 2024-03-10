// Original file: ../protobuf/service/backend/auth.proto


export interface AuthResponse {
  'token'?: (string);
  'isVerified'?: (string);
  'error'?: (string);
  '_error'?: "error";
}

export interface AuthResponse__Output {
  'token'?: (string);
  'isVerified'?: (string);
  'error'?: (string);
}
