// Original file: ../protobuf/service/login.proto


export interface LoginRequest {
  'phoneNumber'?: (string);
  'password'?: (string);
  'countryCode'?: (string);
  '_countryCode'?: "countryCode";
}

export interface LoginRequest__Output {
  'phoneNumber'?: (string);
  'password'?: (string);
  'countryCode'?: (string);
}
