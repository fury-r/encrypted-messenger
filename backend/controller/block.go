package controller

import (
	"context"

	"example.com/messenger/firebase"
	"example.com/messenger/pb"
	"example.com/messenger/utils"
	"google.golang.org/api/iterator"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

func BlockUserController(ctx context.Context, req *pb.BlockRequest) (*pb.BlockResponse, error) {

	val, err := utils.GetTokenFromMetaDataAndValidate(ctx)
	if err != nil {
		return nil, err
	}

	app, err := firebase.InitFirebase().Firestore(ctx)
	if err != nil {
		panic(err)
	}

	docs := app.Collection("user").Where("phoneNumber", "==", val).Documents(ctx)
	defer docs.Stop()
	for {
		doc, err := docs.Next()
		if err == iterator.Done {
			break
		}
		if err != nil {
			return nil, err
		}
		data := doc.Data()
		var list []string = data["blocked_users"].([]string)
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
		app.Collection("user").Doc(doc.Ref.ID).Set(ctx, map[string]interface{}{
			"blocked_users": list,
		})
		return &pb.BlockResponse{
			BlockedUsers: list,
		}, nil
	}
	return nil, status.Errorf(codes.Internal, "Cannot access db")
}
