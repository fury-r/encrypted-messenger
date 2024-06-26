// Code generated by protoc-gen-go-grpc. DO NOT EDIT.
// versions:
// - protoc-gen-go-grpc v1.3.0
// - protoc             v3.12.4
// source: test.proto

package pb

import (
	context "context"
	grpc "google.golang.org/grpc"
	codes "google.golang.org/grpc/codes"
	status "google.golang.org/grpc/status"
)

// This is a compile-time assertion to ensure that this generated file
// is compatible with the grpc package it is being compiled against.
// Requires gRPC-Go v1.32.0 or later.
const _ = grpc.SupportPackageIsVersion7

const (
	TestMiddleware_Ping_FullMethodName = "/com.service.TestMiddleware/Ping"
)

// TestMiddlewareClient is the client API for TestMiddleware service.
//
// For semantics around ctx use and closing/ending streaming RPCs, please refer to https://pkg.go.dev/google.golang.org/grpc/?tab=doc#ClientConn.NewStream.
type TestMiddlewareClient interface {
	Ping(ctx context.Context, in *PingRequest, opts ...grpc.CallOption) (*PingResponse, error)
}

type testMiddlewareClient struct {
	cc grpc.ClientConnInterface
}

func NewTestMiddlewareClient(cc grpc.ClientConnInterface) TestMiddlewareClient {
	return &testMiddlewareClient{cc}
}

func (c *testMiddlewareClient) Ping(ctx context.Context, in *PingRequest, opts ...grpc.CallOption) (*PingResponse, error) {
	out := new(PingResponse)
	err := c.cc.Invoke(ctx, TestMiddleware_Ping_FullMethodName, in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

// TestMiddlewareServer is the server API for TestMiddleware service.
// All implementations must embed UnimplementedTestMiddlewareServer
// for forward compatibility
type TestMiddlewareServer interface {
	Ping(context.Context, *PingRequest) (*PingResponse, error)
	mustEmbedUnimplementedTestMiddlewareServer()
}

// UnimplementedTestMiddlewareServer must be embedded to have forward compatible implementations.
type UnimplementedTestMiddlewareServer struct {
}

func (UnimplementedTestMiddlewareServer) Ping(context.Context, *PingRequest) (*PingResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method Ping not implemented")
}
func (UnimplementedTestMiddlewareServer) mustEmbedUnimplementedTestMiddlewareServer() {}

// UnsafeTestMiddlewareServer may be embedded to opt out of forward compatibility for this service.
// Use of this interface is not recommended, as added methods to TestMiddlewareServer will
// result in compilation errors.
type UnsafeTestMiddlewareServer interface {
	mustEmbedUnimplementedTestMiddlewareServer()
}

func RegisterTestMiddlewareServer(s grpc.ServiceRegistrar, srv TestMiddlewareServer) {
	s.RegisterService(&TestMiddleware_ServiceDesc, srv)
}

func _TestMiddleware_Ping_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(PingRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(TestMiddlewareServer).Ping(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: TestMiddleware_Ping_FullMethodName,
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(TestMiddlewareServer).Ping(ctx, req.(*PingRequest))
	}
	return interceptor(ctx, in, info, handler)
}

// TestMiddleware_ServiceDesc is the grpc.ServiceDesc for TestMiddleware service.
// It's only intended for direct use with grpc.RegisterService,
// and not to be introspected or modified (even as a copy)
var TestMiddleware_ServiceDesc = grpc.ServiceDesc{
	ServiceName: "com.service.TestMiddleware",
	HandlerType: (*TestMiddlewareServer)(nil),
	Methods: []grpc.MethodDesc{
		{
			MethodName: "Ping",
			Handler:    _TestMiddleware_Ping_Handler,
		},
	},
	Streams:  []grpc.StreamDesc{},
	Metadata: "test.proto",
}
