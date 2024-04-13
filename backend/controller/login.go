package controller

import (
	"log"

	"example.com/messenger/firebase"
	"example.com/messenger/pb"
	"golang.org/x/net/context"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

func LoginService(ctx context.Context, req *pb.LoginRequest) (*pb.LoginResponse, error) {
	log.Default().Println("Login")

	app, firebaseerr := firebase.InitFirebase().Firestore(ctx)
	if firebaseerr != nil {
		log.Default().Println("Error", firebaseerr)
		return nil, status.Error(codes.Internal, "Internal server Error")
	}
	userCheck := app.Collection("user").Where("phoneNumber", "==", req.GetPhoneNumber()).Documents(ctx)
	if userCheck == nil {
		errorMessage := "User not found."
		return &pb.LoginResponse{
			Message: "Account not found with this Number.",
			Error:   &errorMessage,
		}, nil
	}
	_, err := RegenerateOtpService(ctx, &pb.ReSendOtpRequest{PhoneNumber: &req.PhoneNumber})
	if err != nil {
		return nil, status.Error(codes.Internal, "Failed to generate Otp")
	}
	return &pb.LoginResponse{
		Message: "OTP has been sent " + req.GetPhoneNumber(), Error: nil,
	}, nil
}
