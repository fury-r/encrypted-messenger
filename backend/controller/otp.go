package controller

import (
	"fmt"
	"log"

	"example.com/messenger/firebase"
	"example.com/messenger/pb"
	"example.com/messenger/utils"
	"golang.org/x/net/context"
)

func OtpService(ctx context.Context, req *pb.OtpRequest) (*pb.AuthResponse, error) {
	fmt.Print(req.Otp, "validating otp ", req.PhoneNumber)
	app := firebase.InitFirebase()
	firestore, err := app.Firestore(ctx)
	auth, _ := app.Auth(ctx)
	if err != nil {
		panic(err)
	}

	docs, _ := firestore.Collection("user").Where("phoneNumber", "==", req.GetPhoneNumber()).Where("otp", "==", req.GetOtp()).Limit(1).Documents(ctx).GetAll()
	if docs == nil {
		errMessage := "Invalid Otp.Please try again."
		return &pb.AuthResponse{
			Error: &errMessage,
		}, nil
	}

	var data pb.User

	for _, doc := range docs {
		doc.DataTo(&data)
		email := data.Email
		log.Default().Println("generating token " + data.PhoneNumber)
		user, err := auth.GetUserByEmail(ctx, *email)
		if err != nil {
			log.Default().Println(err)
			panic(err)
		}

		log.Default().Println(user.UID)

		token, err := utils.CreateToken(data.PhoneNumber)
		if err != nil {
			log.Default().Println(err)
			errMessage := "Invalid Otp.Please try again."
			return &pb.AuthResponse{
				Error: &errMessage,
			}, nil
		}
		_, e := utils.ValidateToken(token)

		if e != nil {
			log.Default().Println(err)
			errMessage := "Invalid Otp.Please try again."
			return &pb.AuthResponse{
				Error: &errMessage,
			}, nil
		}
		id := doc.Ref.ID
		email = data.Email

		return &pb.AuthResponse{
			User: &pb.User{
				Email:        email,
				PhoneNumber:  data.GetPhoneNumber(),
				Uuid:         id,
				BlockedUsers: data.GetBlockedUsers(),
			},
			Token: &token,
		}, nil
	}
	errMessage := "Invalid Otp.Please try again."
	return &pb.AuthResponse{
		Error: &errMessage,
	}, nil
}
