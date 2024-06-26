package controller

import (
	"fmt"
	"log"
	"time"

	"cloud.google.com/go/firestore"
	"example.com/messenger/firebase"
	"example.com/messenger/pb"
	"example.com/messenger/utils"
	"golang.org/x/net/context"
)

func OtpService(ctx context.Context, req *pb.OtpRequest) (*pb.AuthResponse, error) {
	fmt.Println(req.Otp, "validating otp ", req.PhoneNumber)
	firebase := firebase.InitFirebase()
	app, err := firebase.Firestore(ctx)
	auth, _ := firebase.Auth(ctx)
	if err != nil {
		panic(err)
	}

	docs, _ := app.Collection("user").Where("phoneNumber", "==", req.GetPhoneNumber()).Where("otp", "==", req.GetOtp()).Where("otpUsed", "==", false).Limit(1).Documents(ctx).GetAll()
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
		otpData := doc.Data()
		otpTime, ok := otpData["otpTime"].(string)
		updated := map[string]interface{}{}
		if ok {
			parseTime, err := time.Parse("January 2, 2006 at 3:04:05 PM MST-07:00", otpTime)
			if err == nil {
				currenTime := time.Now()
				if currenTime.Sub(parseTime).Minutes() > 5 {
					message := "otp Expired"
					return &pb.AuthResponse{Error: &message}, nil
				}
			}
			updated["otpUsed"] = true
			_, err = app.Collection("user").Doc(doc.Ref.ID).Set(ctx, updated, firestore.MergeAll)
			if err != nil {
				log.Default().Println(err)
			}

		}

		return &pb.AuthResponse{
			User: &pb.User{
				Email:        email,
				PhoneNumber:  data.GetPhoneNumber(),
				Uuid:         id,
				BlockedUsers: data.GetBlockedUsers(),
				Image:        data.Image,
				Status:       data.Status,
				Username:     data.Username,
			},
			Token: &token,
		}, nil
	}
	errMessage := "Invalid Otp.Please try again."
	return &pb.AuthResponse{
		Error: &errMessage,
	}, nil
}
