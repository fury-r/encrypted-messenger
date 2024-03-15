// Code generated by protoc-gen-go-grpc. DO NOT EDIT.
// versions:
// - protoc-gen-go-grpc v1.3.0
// - protoc             v3.12.4
// source: service.proto

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
	Services_Login_FullMethodName            = "/Services/Login"
	Services_Register_FullMethodName         = "/Services/Register"
	Services_Otp_FullMethodName              = "/Services/Otp"
	Services_ValidateContacts_FullMethodName = "/Services/ValidateContacts"
	Services_Send_FullMethodName             = "/Services/Send"
	Services_MessageUpdate_FullMethodName    = "/Services/messageUpdate"
	Services_VerifyToken_FullMethodName      = "/Services/VerifyToken"
	Services_SavePubKey_FullMethodName       = "/Services/savePubKey"
	Services_HandShakeRequest_FullMethodName = "/Services/handShakeRequest"
	Services_GetUser_FullMethodName          = "/Services/getUser"
)

// ServicesClient is the client API for Services service.
//
// For semantics around ctx use and closing/ending streaming RPCs, please refer to https://pkg.go.dev/google.golang.org/grpc/?tab=doc#ClientConn.NewStream.
type ServicesClient interface {
	Login(ctx context.Context, in *LoginRequest, opts ...grpc.CallOption) (*LoginResponse, error)
	Register(ctx context.Context, in *RegisterRequest, opts ...grpc.CallOption) (*RegisterResponse, error)
	Otp(ctx context.Context, in *OtpRequest, opts ...grpc.CallOption) (*OtpResponse, error)
	ValidateContacts(ctx context.Context, in *ContactsList, opts ...grpc.CallOption) (*ContactsList, error)
	Send(ctx context.Context, in *Event, opts ...grpc.CallOption) (*Event, error)
	MessageUpdate(ctx context.Context, in *MessageUpdateRequest, opts ...grpc.CallOption) (*MessageUpdateResponse, error)
	VerifyToken(ctx context.Context, in *AuthRequest, opts ...grpc.CallOption) (*AuthResponse, error)
	SavePubKey(ctx context.Context, in *User, opts ...grpc.CallOption) (*User, error)
	HandShakeRequest(ctx context.Context, in *Event, opts ...grpc.CallOption) (*Event, error)
	GetUser(ctx context.Context, in *UserRequest, opts ...grpc.CallOption) (*UserResponse, error)
}

type servicesClient struct {
	cc grpc.ClientConnInterface
}

func NewServicesClient(cc grpc.ClientConnInterface) ServicesClient {
	return &servicesClient{cc}
}

func (c *servicesClient) Login(ctx context.Context, in *LoginRequest, opts ...grpc.CallOption) (*LoginResponse, error) {
	out := new(LoginResponse)
	err := c.cc.Invoke(ctx, Services_Login_FullMethodName, in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *servicesClient) Register(ctx context.Context, in *RegisterRequest, opts ...grpc.CallOption) (*RegisterResponse, error) {
	out := new(RegisterResponse)
	err := c.cc.Invoke(ctx, Services_Register_FullMethodName, in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *servicesClient) Otp(ctx context.Context, in *OtpRequest, opts ...grpc.CallOption) (*OtpResponse, error) {
	out := new(OtpResponse)
	err := c.cc.Invoke(ctx, Services_Otp_FullMethodName, in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *servicesClient) ValidateContacts(ctx context.Context, in *ContactsList, opts ...grpc.CallOption) (*ContactsList, error) {
	out := new(ContactsList)
	err := c.cc.Invoke(ctx, Services_ValidateContacts_FullMethodName, in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *servicesClient) Send(ctx context.Context, in *Event, opts ...grpc.CallOption) (*Event, error) {
	out := new(Event)
	err := c.cc.Invoke(ctx, Services_Send_FullMethodName, in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *servicesClient) MessageUpdate(ctx context.Context, in *MessageUpdateRequest, opts ...grpc.CallOption) (*MessageUpdateResponse, error) {
	out := new(MessageUpdateResponse)
	err := c.cc.Invoke(ctx, Services_MessageUpdate_FullMethodName, in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *servicesClient) VerifyToken(ctx context.Context, in *AuthRequest, opts ...grpc.CallOption) (*AuthResponse, error) {
	out := new(AuthResponse)
	err := c.cc.Invoke(ctx, Services_VerifyToken_FullMethodName, in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *servicesClient) SavePubKey(ctx context.Context, in *User, opts ...grpc.CallOption) (*User, error) {
	out := new(User)
	err := c.cc.Invoke(ctx, Services_SavePubKey_FullMethodName, in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *servicesClient) HandShakeRequest(ctx context.Context, in *Event, opts ...grpc.CallOption) (*Event, error) {
	out := new(Event)
	err := c.cc.Invoke(ctx, Services_HandShakeRequest_FullMethodName, in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *servicesClient) GetUser(ctx context.Context, in *UserRequest, opts ...grpc.CallOption) (*UserResponse, error) {
	out := new(UserResponse)
	err := c.cc.Invoke(ctx, Services_GetUser_FullMethodName, in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

// ServicesServer is the server API for Services service.
// All implementations must embed UnimplementedServicesServer
// for forward compatibility
type ServicesServer interface {
	Login(context.Context, *LoginRequest) (*LoginResponse, error)
	Register(context.Context, *RegisterRequest) (*RegisterResponse, error)
	Otp(context.Context, *OtpRequest) (*OtpResponse, error)
	ValidateContacts(context.Context, *ContactsList) (*ContactsList, error)
	Send(context.Context, *Event) (*Event, error)
	MessageUpdate(context.Context, *MessageUpdateRequest) (*MessageUpdateResponse, error)
	VerifyToken(context.Context, *AuthRequest) (*AuthResponse, error)
	SavePubKey(context.Context, *User) (*User, error)
	HandShakeRequest(context.Context, *Event) (*Event, error)
	GetUser(context.Context, *UserRequest) (*UserResponse, error)
	mustEmbedUnimplementedServicesServer()
}

// UnimplementedServicesServer must be embedded to have forward compatible implementations.
type UnimplementedServicesServer struct {
}

func (UnimplementedServicesServer) Login(context.Context, *LoginRequest) (*LoginResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method Login not implemented")
}
func (UnimplementedServicesServer) Register(context.Context, *RegisterRequest) (*RegisterResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method Register not implemented")
}
func (UnimplementedServicesServer) Otp(context.Context, *OtpRequest) (*OtpResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method Otp not implemented")
}
func (UnimplementedServicesServer) ValidateContacts(context.Context, *ContactsList) (*ContactsList, error) {
	return nil, status.Errorf(codes.Unimplemented, "method ValidateContacts not implemented")
}
func (UnimplementedServicesServer) Send(context.Context, *Event) (*Event, error) {
	return nil, status.Errorf(codes.Unimplemented, "method Send not implemented")
}
func (UnimplementedServicesServer) MessageUpdate(context.Context, *MessageUpdateRequest) (*MessageUpdateResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method MessageUpdate not implemented")
}
func (UnimplementedServicesServer) VerifyToken(context.Context, *AuthRequest) (*AuthResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method VerifyToken not implemented")
}
func (UnimplementedServicesServer) SavePubKey(context.Context, *User) (*User, error) {
	return nil, status.Errorf(codes.Unimplemented, "method SavePubKey not implemented")
}
func (UnimplementedServicesServer) HandShakeRequest(context.Context, *Event) (*Event, error) {
	return nil, status.Errorf(codes.Unimplemented, "method HandShakeRequest not implemented")
}
func (UnimplementedServicesServer) GetUser(context.Context, *UserRequest) (*UserResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method GetUser not implemented")
}
func (UnimplementedServicesServer) mustEmbedUnimplementedServicesServer() {}

// UnsafeServicesServer may be embedded to opt out of forward compatibility for this service.
// Use of this interface is not recommended, as added methods to ServicesServer will
// result in compilation errors.
type UnsafeServicesServer interface {
	mustEmbedUnimplementedServicesServer()
}

func RegisterServicesServer(s grpc.ServiceRegistrar, srv ServicesServer) {
	s.RegisterService(&Services_ServiceDesc, srv)
}

func _Services_Login_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(LoginRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(ServicesServer).Login(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: Services_Login_FullMethodName,
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(ServicesServer).Login(ctx, req.(*LoginRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _Services_Register_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(RegisterRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(ServicesServer).Register(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: Services_Register_FullMethodName,
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(ServicesServer).Register(ctx, req.(*RegisterRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _Services_Otp_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(OtpRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(ServicesServer).Otp(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: Services_Otp_FullMethodName,
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(ServicesServer).Otp(ctx, req.(*OtpRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _Services_ValidateContacts_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(ContactsList)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(ServicesServer).ValidateContacts(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: Services_ValidateContacts_FullMethodName,
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(ServicesServer).ValidateContacts(ctx, req.(*ContactsList))
	}
	return interceptor(ctx, in, info, handler)
}

func _Services_Send_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(Event)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(ServicesServer).Send(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: Services_Send_FullMethodName,
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(ServicesServer).Send(ctx, req.(*Event))
	}
	return interceptor(ctx, in, info, handler)
}

func _Services_MessageUpdate_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(MessageUpdateRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(ServicesServer).MessageUpdate(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: Services_MessageUpdate_FullMethodName,
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(ServicesServer).MessageUpdate(ctx, req.(*MessageUpdateRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _Services_VerifyToken_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(AuthRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(ServicesServer).VerifyToken(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: Services_VerifyToken_FullMethodName,
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(ServicesServer).VerifyToken(ctx, req.(*AuthRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _Services_SavePubKey_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(User)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(ServicesServer).SavePubKey(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: Services_SavePubKey_FullMethodName,
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(ServicesServer).SavePubKey(ctx, req.(*User))
	}
	return interceptor(ctx, in, info, handler)
}

func _Services_HandShakeRequest_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(Event)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(ServicesServer).HandShakeRequest(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: Services_HandShakeRequest_FullMethodName,
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(ServicesServer).HandShakeRequest(ctx, req.(*Event))
	}
	return interceptor(ctx, in, info, handler)
}

func _Services_GetUser_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(UserRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(ServicesServer).GetUser(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: Services_GetUser_FullMethodName,
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(ServicesServer).GetUser(ctx, req.(*UserRequest))
	}
	return interceptor(ctx, in, info, handler)
}

// Services_ServiceDesc is the grpc.ServiceDesc for Services service.
// It's only intended for direct use with grpc.RegisterService,
// and not to be introspected or modified (even as a copy)
var Services_ServiceDesc = grpc.ServiceDesc{
	ServiceName: "Services",
	HandlerType: (*ServicesServer)(nil),
	Methods: []grpc.MethodDesc{
		{
			MethodName: "Login",
			Handler:    _Services_Login_Handler,
		},
		{
			MethodName: "Register",
			Handler:    _Services_Register_Handler,
		},
		{
			MethodName: "Otp",
			Handler:    _Services_Otp_Handler,
		},
		{
			MethodName: "ValidateContacts",
			Handler:    _Services_ValidateContacts_Handler,
		},
		{
			MethodName: "Send",
			Handler:    _Services_Send_Handler,
		},
		{
			MethodName: "messageUpdate",
			Handler:    _Services_MessageUpdate_Handler,
		},
		{
			MethodName: "VerifyToken",
			Handler:    _Services_VerifyToken_Handler,
		},
		{
			MethodName: "savePubKey",
			Handler:    _Services_SavePubKey_Handler,
		},
		{
			MethodName: "handShakeRequest",
			Handler:    _Services_HandShakeRequest_Handler,
		},
		{
			MethodName: "getUser",
			Handler:    _Services_GetUser_Handler,
		},
	},
	Streams:  []grpc.StreamDesc{},
	Metadata: "service.proto",
}
