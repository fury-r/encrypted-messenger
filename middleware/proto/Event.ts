// Original file: ../protobuf/service/message.proto

import type { EventType as _EventType, EventType__Output as _EventType__Output } from './EventType';
import type { MessageRequest as _MessageRequest, MessageRequest__Output as _MessageRequest__Output } from './MessageRequest';
import type { KeyExchange as _KeyExchange, KeyExchange__Output as _KeyExchange__Output } from './KeyExchange';

export interface Event {
  'type'?: (_EventType);
  'message'?: (_MessageRequest | null);
  'exchange'?: (_KeyExchange | null);
  'body'?: "message"|"exchange";
}

export interface Event__Output {
  'type'?: (_EventType__Output);
  'message'?: (_MessageRequest__Output);
  'exchange'?: (_KeyExchange__Output);
}
