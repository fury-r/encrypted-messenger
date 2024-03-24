// Original file: ../protobuf/service/hello.proto

export const Enum = {
  A: 0,
  B: 1,
} as const;

export type Enum =
  | 'A'
  | 0
  | 'B'
  | 1

export type Enum__Output = typeof Enum[keyof typeof Enum]
