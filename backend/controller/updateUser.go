package controller

import (
	"context"
	"log"

	"cloud.google.com/go/firestore"
	"example.com/messenger/firebase"
	"example.com/messenger/pb"
	"example.com/messenger/utils"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

func UpdateUserService(ctx context.Context, req *pb.User) (*pb.User, error) {

	app, _ := firebase.InitFirebase().Firestore(ctx)
	number, err := utils.GetTokenFromMetaDataAndValidate(ctx)
	if err != nil {
		log.Default().Println("Validation failed", err)
		return nil, err
	}
	docs, err := app.Collection("user").Where("phoneNumber", "==", *number).Documents(ctx).GetAll()
	var data pb.User
	for _, doc := range docs {
		updateData := map[string]interface{}{}
		doc.DataTo(&data)

		if data.Username != req.Username {
			updateData["username"] = req.Username
		}
		if data.Image != req.Image {
			updateData["image"] = req.Image

		}
		_, err := app.Collection("user").Doc(doc.Ref.ID).Set(ctx, updateData, firestore.MergeAll)

		if err != nil {
			log.Default().Println("Failed to save")
			return nil, status.Error(codes.Internal, "Failed to update.")
		}

	}

	return req, nil

}
