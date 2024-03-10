// Original file: ../protobuf/service/message.proto

import type { EventType as _EventType, EventType__Output as _EventType__Output } from './EventType';
import type { MessageRequest as _MessageRequest, MessageRequest__Output as _MessageRequest__Output } from './MessageRequest';

export interface Event {
  'type'?: (_EventType);
  'token'?: (string);
  'message'?: (_MessageRequest | null);
}

export interface Event__Output {
  'type'?: (_EventType__Output);
  'token'?: (string);
  'message'?: (_MessageRequest__Output);
}
