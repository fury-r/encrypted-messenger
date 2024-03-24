// Original file: ../protobuf/service/middlware/message.proto


export interface MessageRequest {
  'fromPubKey'?: (string);
  'messageId'?: (string);
  'text'?: (string);
  'toPubKey'?: (string);
  'contentType'?: (string);
  'timestamp'?: (string);
}

export interface MessageRequest__Output {
  'fromPubKey'?: (string);
  'messageId'?: (string);
  'text'?: (string);
  'toPubKey'?: (string);
  'contentType'?: (string);
  'timestamp'?: (string);
}
