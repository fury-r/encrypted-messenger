// Original file: ../protobuf/service/message.proto


export interface MessageInfo {
  'sender'?: (string);
  'messageId'?: (string);
  'text'?: (string);
  'reciever'?: (string);
  'contentType'?: (string);
  'timestamp'?: (string);
  'deliverStatus'?: (boolean);
  'readStatus'?: (boolean);
}

export interface MessageInfo__Output {
  'sender'?: (string);
  'messageId'?: (string);
  'text'?: (string);
  'reciever'?: (string);
  'contentType'?: (string);
  'timestamp'?: (string);
  'deliverStatus'?: (boolean);
  'readStatus'?: (boolean);
}
