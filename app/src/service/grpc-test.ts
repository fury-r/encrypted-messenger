// import { grpc } from "@improbable-eng/grpc-web";
// import {
//   PingRequest,
//   MiddlewareClient,
// } from "../protobuf/middleware/middleware_grpc_web_pb";

// const client = new MiddlewareClient("http://localhost:8081", null, null);

// export const ping = (message, callback) => {
//   const request = new PingRequest();
//   request.setMessage(message);

//   grpc.invoke(client.ping, {
//     request,
//     host: "",
//     onMessage: (response) => {
//       callback(null, response);
//     },
//     onEnd: (status, statusMessage) => {
//       if (status !== grpc.Code.OK) {
//         callback(new Error(`Error: ${statusMessage}`), null);
//       }
//     },
//   });
// };
