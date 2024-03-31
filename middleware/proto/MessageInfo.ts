// Original file: ../protobuf/service/message.proto

import type { ContentType as _ContentType, ContentType__Output as _ContentType__Output } from './ContentType';

export interface MessageInfo {
  'sender'?: (string);
  'messageId'?: (string);
  'text'?: (string);
  'reciever'?: (string);
  'contentType'?: (_ContentType);
  'timestamp'?: (string);
  'deliverStatus'?: (boolean);
  'readStatus'?: (boolean);
}

export interface MessageInfo__Output {
  'sender'?: (string);
  'messageId'?: (string);
  'text'?: (string);
  'reciever'?: (string);
  'contentType'?: (_ContentType__Output);
  'timestamp'?: (string);
  'deliverStatus'?: (boolean);
  'readStatus'?: (boolean);
}
