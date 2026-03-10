// Original file: ../protobuf/service/message.proto

import type { ContentType as _ContentType, ContentType__Output as _ContentType__Output } from './ContentType';

export interface RequestDownload {
  'contentType'?: (_ContentType);
  'cloudId'?: (string);
}

export interface RequestDownload__Output {
  'contentType'?: (_ContentType__Output);
  'cloudId'?: (string);
}
