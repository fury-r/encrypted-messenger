import * as grpc from "@grpc/grpc-js";
import * as protoLoader from "@grpc/proto-loader";
import { ServicesClient } from "../../proto/Services";

export class GrpcClientService {
  private path = "";

  private packageDefintion: protoLoader.PackageDefinition;
  private grpcService: any;
  private client: ServicesClient;
  private token: string | null | undefined;
  private options: any;
  constructor(url?: string) {
    this.options = {
      keepCase: true,
      longs: String,
      enums: String,
      defaults: true,
      oneofs: true,
    };
    this.path = "../protobuf/service/service.proto";
    this.packageDefintion = protoLoader.loadSync(this.path, this.options);
    // const options: grpc.ChannelOptions = {
    //   interceptors: [this.addAuthorizationInterceptor.bind(this)],
    // };
    this.grpcService = grpc.loadPackageDefinition(
      this.packageDefintion
    ).Services;

    this.client = new this.grpcService(
      url || "localhost:8082",
      grpc.credentials.createInsecure()
      // options
    );
  }

  getClient() {
    return this.client;
  }
}
