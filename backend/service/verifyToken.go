package service

import (
	"fmt"

	"example.com/messenger/firebase"
	"example.com/messenger/pb"
	"example.com/messenger/utils"
	"golang.org/x/net/context"
)

func VerifyTokenService(ctx context.Context, req *pb.AuthRequest) (*pb.AuthResponse, error) {
	app := firebase.InitFirebase()
	firestore, err := app.Firestore(ctx)
	auth, _ := app.Auth(ctx)
	if err != nil {
		panic(err)
	}
	fmt.Println(req.GetToken())

	email, err := utils.ValidateToken(req.GetToken())
	if err != nil {
		x := "Invalid Token 1"
		return &pb.AuthResponse{
			Error: &x,
		}, nil
	}
	docs, _ := firestore.Collection("user").Where("email", "==", email).Limit(1).Documents(ctx).GetAll()
	if docs == nil {
		x := "Invalid Token 2"
		return &pb.AuthResponse{
			Error: &x,
		}, nil
	}

	var data pb.User

	for _, doc := range docs {
		doc.DataTo(&data)
		token, err := auth.CustomToken(ctx, data.GetEmail())
		if err != nil {
			panic(err)
		}
		IsVerified := true
		email := data.Email
		id := doc.Ref.ID
		fmt.Printf("Valid token")
		return &pb.AuthResponse{
			Token:      &token,
			IsVerified: &IsVerified,
			User: &pb.User{
				Email:       email,
				PhoneNumber: data.GetPhoneNumber(),
				Uuid:        id,
				PubKey:      data.PubKey,
			},
		}, nil
	}
	x := "Invalid Token 3"
	return &pb.AuthResponse{
		Error: &x,
	}, nil
}
