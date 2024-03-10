// Original file: ../protobuf/service/message.proto

export const MessageType = {
  INSERT: 0,
  UPDATE: 1,
} as const;

export type MessageType =
  | 'INSERT'
  | 0
  | 'UPDATE'
  | 1

export type MessageType__Output = typeof MessageType[keyof typeof MessageType]
