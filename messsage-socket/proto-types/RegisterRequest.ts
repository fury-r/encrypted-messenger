// Original file: ../protobuf/service/register.proto


export interface RegisterRequest {
  'phoneNumber'?: (string);
  'username'?: (string);
  'email'?: (string);
  'password'?: (string);
  'countryCode'?: (string);
  '_countryCode'?: "countryCode";
}

export interface RegisterRequest__Output {
  'phoneNumber'?: (string);
  'username'?: (string);
  'email'?: (string);
  'password'?: (string);
  'countryCode'?: (string);
}
