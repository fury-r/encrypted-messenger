import type * as grpc from '@grpc/grpc-js';
import type { MessageTypeDefinition } from '@grpc/proto-loader';

import type { MiddlewareClient as _com_middleware_MiddlewareClient, MiddlewareDefinition as _com_middleware_MiddlewareDefinition } from './com/middleware/Middleware';

type SubtypeConstructor<Constructor extends new (...args: any) => any, Subtype> = {
  new(...args: ConstructorParameters<Constructor>): Subtype;
};

export interface ProtoGrpcType {
  com: {
    middleware: {
      Middleware: SubtypeConstructor<typeof grpc.Client, _com_middleware_MiddlewareClient> & { service: _com_middleware_MiddlewareDefinition }
      PingRequest: MessageTypeDefinition
      PingResponse: MessageTypeDefinition
    }
  }
}

