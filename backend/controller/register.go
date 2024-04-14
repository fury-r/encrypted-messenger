package controller

import (
	"log"

	"example.com/messenger/firebase"
	"example.com/messenger/pb"
	"example.com/messenger/utils"
	"golang.org/x/net/context"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

func RegisterService(ctx context.Context, req *pb.RegisterRequest) (*pb.RegisterResponse, error) {
	log.Default().Println("Register")
	log.Default().Println(req.GetEmail()+" "+req.String(), req.PhoneNumber)
	firestore, initerr := firebase.InitFirebase().Firestore(ctx)
	log.Default().Println(initerr)
	auth, _ := firebase.InitFirebase().Auth(ctx)

	phoneNumber, _ := firestore.Collection("user").Where("phoneNumber", "==", req.PhoneNumber).Documents(ctx).GetAll()

	if len(phoneNumber) > 0 {
		log.Default().Println("User Exist")

		errorMessage := req.GetPhoneNumber() + " in use"
		return &pb.RegisterResponse{
			Message: "Use a different phoneNumber.",
			Error:   &errorMessage,
		}, nil
	}
	phoneNumberCheck, _ := firestore.Collection("user").Where("phoneNumber", "==", req.GetPhoneNumber()).Documents(ctx).GetAll()
	if len(phoneNumberCheck) > 0 {
		log.Default().Println("User Exist")

		errorMessage := req.GetPhoneNumber() + " in use"
		return &pb.RegisterResponse{
			Message: "Use a different Number.",
			Error:   &errorMessage,
		}, nil
	}

	var password string = utils.GenerateComplexPassword(10)
	firestore.Collection("user").NewDoc().Set(ctx, map[string]interface{}{
		"username":     req.GetUsername(),
		"countryCode":  req.CountryCode,
		"phoneNumber":  req.GetPhoneNumber(),
		"email":        req.GetEmail(),
		"password":     password,
		"image":        "",
		"status":       "",
		"blockedUsers": []interface{}{},
	})

	firebase.CreateUser(ctx, auth, req.GetEmail(), password)
	log.Default().Println("Saved and otp generated")
	_, err := RegenerateOtpService(ctx, &pb.ReSendOtpRequest{PhoneNumber: &req.PhoneNumber})
	if err != nil {
		return nil, status.Error(codes.Internal, "Failed to generate Otp")
	}
	return &pb.RegisterResponse{
		Message: "Otp sent",
	}, nil
}
