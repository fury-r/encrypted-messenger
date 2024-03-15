import * as grpc from "@grpc/grpc-js";
import * as protoLoader from "@grpc/proto-loader";
import { ServicesClient } from "../proto/Services";

export class GrpcClientService {
  private path = "";

  private packageDefintion: protoLoader.PackageDefinition;
  private grpcService: any;
  private client: ServicesClient;
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

    this.grpcService = grpc.loadPackageDefinition(
      this.packageDefintion
    ).Services;

    this.client = new this.grpcService(
      url || "localhost:8082",
      grpc.credentials.createInsecure()
    );
  }

  getClient() {
    return this.client;
  }
}
