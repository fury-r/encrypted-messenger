package controller

import (
	"fmt"

	"time"

	"cloud.google.com/go/firestore"
	"example.com/messenger/firebase"
	"example.com/messenger/pb"
	"example.com/messenger/utils"
	"golang.org/x/net/context"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

func LoginService(ctx context.Context, req *pb.LoginRequest) (*pb.LoginResponse, error) {
	fmt.Println("Login")

	app, firebaseerr := firebase.InitFirebase().Firestore(ctx)
	if firebaseerr != nil {
		fmt.Println("Error", firebaseerr)
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
	docs, _ := app.Collection("user").Where("phoneNumber", "==", req.GetPhoneNumber()).Documents(ctx).GetAll()
	for _, doc := range docs {
		updateData := map[string]interface{}{
			"otp":       utils.GenerateOtp(),
			"updatedAt": time.Now(),
		}

		_, err := app.Collection("user").Doc(doc.Ref.ID).Set(
			ctx,
			updateData,
			firestore.MergeAll,
		)
		if err != nil {
			fmt.Println("Error", firebaseerr)
			return nil, status.Error(codes.Internal, "Internal server Error")
		}
	}
	fmt.Sprintln("OTP has been sent ", req.GetPhoneNumber())
	return &pb.LoginResponse{
		Message: "OTP has been sent " + req.GetPhoneNumber(), Error: nil,
	}, nil
}
