// Original file: ../protobuf/service/hello.proto

import type { Enum as _Enum, Enum__Output as _Enum__Output } from './Enum';

export interface HelloRequest {
  'name'?: (string);
  'enum'?: (_Enum);
}

export interface HelloRequest__Output {
  'name'?: (string);
  'enum'?: (_Enum__Output);
}
