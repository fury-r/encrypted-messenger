package controller

import (
	"fmt"

	"example.com/messenger/firebase"
	"example.com/messenger/pb"
	"example.com/messenger/utils"
	"golang.org/x/net/context"
)

func OtpService(ctx context.Context, req *pb.OtpRequest) (*pb.OtpResponse, error) {
	app := firebase.InitFirebase()
	firestore, err := app.Firestore(ctx)
	auth, _ := app.Auth(ctx)
	if err != nil {
		panic(err)
	}

	docs, _ := firestore.Collection("user").Where("phoneNumber", "==", req.GetPhoneNumber()).Where("otp", "==", req.GetOtp()).Limit(1).Documents(ctx).GetAll()
	if docs == nil {
		errMessage := "Invalid Otp.Please try again."
		return &pb.OtpResponse{
			Error: &errMessage,
		}, nil
	}

	var data pb.User

	for _, doc := range docs {
		doc.DataTo(&data)
		email := data.Email
		fmt.Println("generating token " + *email)
		user, err := auth.GetUserByEmail(ctx, *email)
		if err != nil {
			fmt.Println(err)
			panic(err)
		}

		fmt.Println(user.UID)
		if err != nil {
			panic(err)

		}

		token, err := utils.CreateToken(*email)
		if err != nil {
			fmt.Println(err)
			errMessage := "Invalid Otp.Please try again."
			return &pb.OtpResponse{
				Error: &errMessage,
			}, nil
		}
		_, e := utils.ValidateToken(token)
		if e != nil {
			fmt.Println(err)
			errMessage := "Invalid Otp.Please try again."
			return &pb.OtpResponse{
				Error: &errMessage,
			}, nil
		}
		id := doc.Ref.ID

		return &pb.OtpResponse{
			Message: &pb.CurrentUser{
				Token:       token,
				Email:       data.GetEmail(),
				PhoneNumber: data.GetPhoneNumber(),
				Uuid:        id,
			},
		}, nil
	}
	errMessage := "Invalid Otp.Please try again."
	return &pb.OtpResponse{
		Error: &errMessage,
	}, nil
}
