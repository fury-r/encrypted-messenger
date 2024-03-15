package controller

import (
	"fmt"

	"example.com/messenger/firebase"
	"example.com/messenger/pb"
	"example.com/messenger/utils"
	"golang.org/x/net/context"
)

func RegisterService(ctx context.Context, req *pb.RegisterRequest) (*pb.RegisterResponse, error) {
	fmt.Println("Register")
	fmt.Println(req.GetEmail()+" "+req.String(), req.PhoneNumber)
	firestore, initerr := firebase.InitFirebase().Firestore(ctx)
	fmt.Println(initerr)
	auth, _ := firebase.InitFirebase().Auth(ctx)

	emailCheck, _ := firestore.Collection("user").Where("email", "==", req.GetEmail()).Documents(ctx).GetAll()

	if len(emailCheck) > 0 {
		fmt.Println("User Exist")

		errorMessage := req.GetEmail() + " in use"
		return &pb.RegisterResponse{
			Message: "Use a different email.",
			Error:   &errorMessage,
		}, nil
	}
	phoneNumberCheck, _ := firestore.Collection("user").Where("phoneNumber", "==", req.GetPhoneNumber()).Documents(ctx).GetAll()
	if len(phoneNumberCheck) > 0 {
		fmt.Println("User Exist")

		errorMessage := req.GetPhoneNumber() + " in use"
		return &pb.RegisterResponse{
			Message: "Use a different Number.",
			Error:   &errorMessage,
		}, nil
	}

	var password string = utils.GenerateComplexPassword(10)
	firestore.Collection("user").NewDoc().Set(ctx, map[string]interface{}{
		"username":    req.GetUsername(),
		"countryCode": req.CountryCode,
		"phoneNumber": req.GetPhoneNumber(),
		"email":       req.GetEmail(),
		"password":    password,
		"otp":         utils.GenerateOtp(),
	})

	firebase.CreateUser(ctx, auth, req.GetEmail(), password)
	fmt.Println("Saved and otp generated")
	return &pb.RegisterResponse{
		Message: "Otp sent",
	}, nil
}
