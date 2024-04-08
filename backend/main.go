package main

import (
	"context"
	"fmt"
	"log"
	"net"
	"net/http"

	service "example.com/messenger/controller"
	"example.com/messenger/pb"
	"google.golang.org/grpc"
	"google.golang.org/grpc/codes"
	status "google.golang.org/grpc/status"
)

func Alive(w http.ResponseWriter, _ *http.Request) {
	log.Default().Println("Server Alive")
	w.Write([]byte("Server Alive"))
}

type Server struct {
	pb.ServicesServer
}

func (s *Server) Login(ctx context.Context, req *pb.LoginRequest) (*pb.LoginResponse, error) {
	message, err := service.LoginService(ctx, req)
	if err != nil {
		return nil, status.Error(codes.Internal, err.Error())
	}

	return message, nil

}

func (s *Server) Register(ctx context.Context, req *pb.RegisterRequest) (*pb.RegisterResponse, error) {
	log.Default().Println((req.String()))
	message, err := service.RegisterService(ctx, req)
	log.Default().Println(message)
	if err != nil {
		return nil, status.Error(codes.Internal, err.Error())
	}

	return &pb.RegisterResponse{
		Message: message.Message,
		Error:   message.Error,
	}, nil
}

func (s *Server) ValidateContacts(ctx context.Context, req *pb.ContactsList) (*pb.ContactsList, error) {
	message, err := service.ContactService(ctx, req)
	if err != nil {
		return nil, status.Error(codes.Internal, err.Error())
	}

	return message, nil

}

func (s *Server) VerifyToken(ctx context.Context, req *pb.AuthRequest) (*pb.AuthResponse, error) {
	message, err := service.VerifyTokenService(ctx, req)
	if err != nil {
		return nil, status.Error(codes.Internal, err.Error())
	}

	return message, nil

}

func (s *Server) SavePubKey(ctx context.Context, req *pb.User) (*pb.User, error) {
	message, err := service.SavePubKeyService(ctx, req)
	if err != nil {
		return nil, status.Error(codes.Internal, err.Error())
	}

	return message, nil

}

func (s *Server) Otp(ctx context.Context, req *pb.OtpRequest) (*pb.AuthResponse, error) {
	message, err := service.OtpService(ctx, req)
	if err != nil {
		return nil, status.Error(codes.Internal, err.Error())
	}

	return message, nil

}

func (s *Server) GetUser(ctx context.Context, req *pb.UserRequest) (*pb.UserResponse, error) {
	message, err := service.GetUserService(ctx, req)
	if err != nil {
		return nil, status.Error(codes.Internal, err.Error())
	}
	return message, nil

}

func (s *Server) BlockUser(ctx context.Context, req *pb.BlockRequest) (*pb.BlockResponse, error) {

	message, err := service.BlockUserController(ctx, req)
	return message, err
}

func main() {
	// mux := http.NewServeMux()

	// mux.Handle("/test", http.HandlerFunc(Alive))
	listener, err := net.Listen("tcp", ":8082")
	fmt.Print("Starting up backend\n")

	if err != nil {

		panic(err)
	}

	s := grpc.NewServer()
	pb.RegisterServicesServer(s, &Server{})
	log.Default().Println("Server running on PORT 8082")

	if err := s.Serve(listener); err != nil {
		panic(err)
	}
	log.Default().Println("Server running on PORT 8082")

}
