import type * as grpc from '@grpc/grpc-js';
import type { MessageTypeDefinition } from '@grpc/proto-loader';

import type { TestMiddlewareClient as _com_service_TestMiddlewareClient, TestMiddlewareDefinition as _com_service_TestMiddlewareDefinition } from './com/service/TestMiddleware';

type SubtypeConstructor<Constructor extends new (...args: any) => any, Subtype> = {
  new(...args: ConstructorParameters<Constructor>): Subtype;
};

export interface ProtoGrpcType {
  com: {
    service: {
      PingRequest: MessageTypeDefinition
      PingResponse: MessageTypeDefinition
      TestMiddleware: SubtypeConstructor<typeof grpc.Client, _com_service_TestMiddlewareClient> & { service: _com_service_TestMiddlewareDefinition }
    }
  }
}

