package controller

import (
	"context"
	"fmt"
	"log"
	"time"

	"cloud.google.com/go/firestore"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"

	"example.com/messenger/firebase"
	"example.com/messenger/pb"
	"example.com/messenger/utils"
)

func RegenerateOtpService(ctx context.Context, req *pb.ReSendOtpRequest) (*pb.ReSendOtpRequest, error) {
	log.Default().Println("Regenerate Otp started")
	app, initerr := firebase.InitFirebase().Firestore(ctx)
	if initerr != nil {
		return nil, status.Error(codes.Internal, "Connection to db failed")
	}

	docs, _ := app.Collection("user").Where("phoneNumber", "==", req.GetPhoneNumber()).Documents(ctx).GetAll()
	for _, doc := range docs {
		// add logic to send otp  to user device via SMS
		otp := utils.GenerateOtp()
		updateData := map[string]interface{}{
			"otp":       otp,
			"otpUsed":   false,
			"otpTime":   time.Now(),
			"updatedAt": time.Now(),
		}

		_, err := app.Collection("user").Doc(doc.Ref.ID).Set(
			ctx,
			updateData,
			firestore.MergeAll,
		)
		if err != nil {
			return nil, status.Error(codes.Internal, "Internal server Error")
		}
		fmt.Sprintln("OTP has been sent ", req.GetPhoneNumber())

	}
	log.Default().Println("Regenerate Otp finished")

	return req, nil
}
