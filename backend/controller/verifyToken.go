package controller

import (
	"fmt"

	"example.com/messenger/firebase"
	"example.com/messenger/pb"
	"example.com/messenger/utils"
	"golang.org/x/net/context"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

func VerifyTokenService(ctx context.Context, req *pb.AuthRequest) (*pb.AuthResponse, error) {
	app := firebase.InitFirebase()

	firestore, err := app.Firestore(ctx)
	auth, _ := app.Auth(ctx)
	if err != nil {
		return nil, status.Errorf(codes.Unauthenticated, "Authorization token not provided")
	}

	email, err := utils.GetTokenFromMetaDataAndValidate(ctx)
	if err != nil {

		fmt.Println("Invalid token")
		return nil, err
	}
	docs, _ := firestore.Collection("user").Where("email", "==", *email).Limit(1).Documents(ctx).GetAll()
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

func GetUserService(ctx context.Context, req *pb.UserRequest) (*pb.UserResponse, error) {
	app, err := firebase.InitFirebase().Firestore(ctx)
	if err != nil {
		panic(err)
	}

	_, err = utils.GetTokenFromMetaDataAndValidate(ctx)
	if err != nil {
		return nil, err
	}

	docs := app.Collection("user").Where("phoneNumber", "==", req.PhoneNumber).Documents(ctx)

	for {
		doc, err := docs.Next()
		if err != nil {
			return nil, err
		}

		data := doc.Data()
		pubKey, ok := data["pubKey"]
		key := ""
		if ok == true {
			key = pubKey.(string)
		}
		return &pb.UserResponse{
			PubKey:      key,
			PhoneNumber: req.PhoneNumber,
		}, nil
	}

}
