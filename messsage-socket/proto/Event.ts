// Original file: ../protobuf/service/message.proto

import type { EventType as _EventType, EventType__Output as _EventType__Output } from './EventType';

export interface Event {
  'type'?: (_EventType);
  'message'?: (string);
  'exchange'?: (string);
  'sender'?: (string);
  'reciever'?: (string);
}

export interface Event__Output {
  'type'?: (_EventType__Output);
  'message'?: (string);
  'exchange'?: (string);
  'sender'?: (string);
  'reciever'?: (string);
}
