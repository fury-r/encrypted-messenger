package controller

import (
	"context"
	"fmt"

	"cloud.google.com/go/firestore"
	"example.com/messenger/firebase"
	"example.com/messenger/pb"
	"example.com/messenger/utils"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

func BlockUserController(ctx context.Context, req *pb.BlockRequest) (*pb.BlockResponse, error) {
	fmt.Println("BlockUserController")
	val, err := utils.GetTokenFromMetaDataAndValidate(ctx)
	if err != nil {
		return nil, err
	}
	var data pb.User

	app, err := firebase.InitFirebase().Firestore(ctx)
	if err != nil {
		fmt.Println("db err", err)
		return &pb.BlockResponse{
			BlockedUsers: []string{},
		}, nil
	}
	docs, err := app.Collection("user").Where("phoneNumber", "==", *val).Documents(ctx).GetAll()
	if err != nil {
		fmt.Println(err)
		return &pb.BlockResponse{
			BlockedUsers: []string{},
		}, nil
	}
	for _, doc := range docs {
		doc.DataTo(&data)

		var list []string = data.BlockedUsers
		if req.Block == true {
			list = append(list, req.Number)

		} else {
			var new_list []string
			for _, s := range list {
				if s != req.Number {
					new_list = append(new_list, s)
				}
			}
			list = new_list
		}
		updatedData := map[string]interface{}{
			"blockedUsers": list,
		}
		_, err := app.Collection("user").Doc(doc.Ref.ID).Set(ctx, updatedData, firestore.MergeAll)
		if err != nil {
			fmt.Println(err)
			return &pb.BlockResponse{
				BlockedUsers: []string{},
			}, nil
		}
		return &pb.BlockResponse{
			BlockedUsers: list,
		}, nil
	}
	return nil, status.Errorf(codes.Internal, "Cannot access db")
}
