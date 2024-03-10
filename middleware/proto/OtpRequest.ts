// Original file: ../protobuf/service/otp.proto


export interface OtpRequest {
  'otp'?: (string);
  'email'?: (string);
  'phoneNumber'?: (string);
  '_email'?: "email";
  '_phoneNumber'?: "phoneNumber";
}

export interface OtpRequest__Output {
  'otp'?: (string);
  'email'?: (string);
  'phoneNumber'?: (string);
}
