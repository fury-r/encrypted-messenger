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

func VerifyTokenService(ctx context.Context, req *pb.AuthRequest) (*pb.AuthResponse, error) {
	app := firebase.InitFirebase()

	firestore, err := app.Firestore(ctx)
	auth, _ := app.Auth(ctx)
	if err != nil {
		return nil, status.Errorf(codes.Internal, "Internal server error")
	}
	x := "Invalid Token"

	number, err := utils.GetTokenFromMetaDataAndValidate(ctx)
	if err != nil {
		log.Default().Println("Error", err)
		return &pb.AuthResponse{
			Error: &x,
		}, nil
	}
	docs, _ := firestore.Collection("user").Where("phoneNumber", "==", *number).Limit(1).Documents(ctx).GetAll()
	if docs == nil {
		log.Default().Println("Error", err)

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
		log.Default().Println("Valid token")
		return &pb.AuthResponse{
			Token:      &token,
			IsVerified: &IsVerified,
			User: &pb.User{
				Email:        email,
				PhoneNumber:  data.GetPhoneNumber(),
				Uuid:         id,
				PubKey:       data.PubKey,
				BlockedUsers: data.BlockedUsers,
			},
		}, nil
	}
	x = "Invalid Token 3"
	return &pb.AuthResponse{
		Error: &x,
	}, nil
}

func GetUserService(ctx context.Context, req *pb.UserRequest) (*pb.UserResponse, error) {
	app, err := firebase.InitFirebase().Firestore(ctx)
	if err != nil {
		panic(err)
	}
	if len(req.PhoneNumber) == 0 {
		log.Default().Println("Empty Request", req)
		return nil, status.Errorf(codes.InvalidArgument, "Empty Request")
	}
	val, err := utils.GetTokenFromMetaDataAndValidate(ctx)
	if err != nil {
		return nil, err
	}
	log.Default().Println("here", req)
	var data pb.User
	docs, err := app.Collection("user").Where("phoneNumber", "==", *val).Documents(ctx).GetAll()

	for _, doc := range docs {
		doc.DataTo(&data)

		return &pb.UserResponse{
			PubKey:       *data.PubKey,
			PhoneNumber:  req.PhoneNumber,
			BlockedUsers: data.BlockedUsers,
		}, nil

	}
	return &pb.UserResponse{}, nil
}
