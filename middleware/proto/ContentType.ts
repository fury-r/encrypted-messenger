// Original file: ../protobuf/service/message.proto

export const ContentType = {
  Text: 0,
  Audio: 1,
} as const;

export type ContentType =
  | 'Text'
  | 0
  | 'Audio'
  | 1

export type ContentType__Output = typeof ContentType[keyof typeof ContentType]
