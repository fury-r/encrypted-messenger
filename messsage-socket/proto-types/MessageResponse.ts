// Original file: ../protobuf/service/message.proto

import type { MessageInfo as _MessageInfo, MessageInfo__Output as _MessageInfo__Output } from './MessageInfo';
import type { MessageType as _MessageType, MessageType__Output as _MessageType__Output } from './MessageType';

export interface MessageResponse {
  'message'?: (_MessageInfo | null);
  'type'?: (_MessageType);
}

export interface MessageResponse__Output {
  'message'?: (_MessageInfo__Output);
  'type'?: (_MessageType__Output);
}
