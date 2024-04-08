// Original file: ../protobuf/service/otp.proto

import type { currentUser as _currentUser, currentUser__Output as _currentUser__Output } from './currentUser';

export interface OtpResponse {
  'message'?: (_currentUser | null);
  'error'?: (string);
  '_message'?: "message";
  '_error'?: "error";
}

export interface OtpResponse__Output {
  'message'?: (_currentUser__Output);
  'error'?: (string);
}
