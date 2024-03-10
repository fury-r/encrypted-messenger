package main

import (
	"context"
	"fmt"
	"log"
	"net"
	"net/http"

	"example.com/messenger/pb"
	"example.com/messenger/service"
	"google.golang.org/grpc"
	"google.golang.org/grpc/codes"
	status "google.golang.org/grpc/status"
)

func Alive(w http.ResponseWriter, _ *http.Request) {
	fmt.Println("Server Alive\n")
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
	fmt.Println((req.String()))
	message, err := service.RegisterService(ctx, req)
	fmt.Println(message)
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
func (s *Server) Otp(ctx context.Context, req *pb.OtpRequest) (*pb.OtpResponse, error) {
	message, err := service.OtpService(ctx, req)
	if err != nil {
		return nil, status.Error(codes.Internal, err.Error())
	}

	return message, nil

}
func main() {
	// log.Fatalf("1x")
	listener, err := net.Listen("tcp", ":8082")
	fmt.Print("Starting up backend\n")

	if err != nil {

		panic(err)
	}
	mux := http.NewServeMux()
	// log.Fatalf("1")

	mux.Handle("/test", http.HandlerFunc(Alive))
	s := grpc.NewServer()
	pb.RegisterServicesServer(s, &Server{})
	// log.Fatalf("2")
	fmt.Println("Server running on PORT 8082 \n")

	if err := s.Serve(listener); err != nil {
		log.Fatalf("failed to server %v", err)
		panic("Exit")
	}
	fmt.Println("Server running on PORT 8082 \n")

}
