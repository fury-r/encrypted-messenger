// Original file: ../protobuf/service/message.proto

export const EventType = {
  MESSAGE: 0,
  HANDSHAKE: 1,
  DETAILS_UPDATE: 2,
  TYPE_UPDATE: 3,
  STATUS_UPDATE: 4,
} as const;

export type EventType =
  | 'MESSAGE'
  | 0
  | 'HANDSHAKE'
  | 1
  | 'DETAILS_UPDATE'
  | 2
  | 'TYPE_UPDATE'
  | 3
  | 'STATUS_UPDATE'
  | 4

export type EventType__Output = typeof EventType[keyof typeof EventType]
