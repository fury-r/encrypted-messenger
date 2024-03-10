// Original file: ../protobuf/service/message.proto


export interface MessageUpdateRequest {
  'sender'?: (string);
  'reciever'?: (string);
  'messageId'?: (string);
  'deliverStatus'?: (boolean);
  'readStatus'?: (boolean);
  'token'?: (string);
}

export interface MessageUpdateRequest__Output {
  'sender'?: (string);
  'reciever'?: (string);
  'messageId'?: (string);
  'deliverStatus'?: (boolean);
  'readStatus'?: (boolean);
  'token'?: (string);
}
