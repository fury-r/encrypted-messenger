package controller

import (
	"context"
	"log"
	"time"

	"cloud.google.com/go/firestore"
	"example.com/messenger/firebase"
	"example.com/messenger/pb"
	"example.com/messenger/utils"
)

func SavePubKeyService(ctx context.Context, req *pb.User) (*pb.User, error) {
	app, err := firebase.InitFirebase().Firestore(ctx)
	if err != nil {
		return nil, err
	}

	email, err := utils.GetTokenFromMetaDataAndValidate(ctx)
	if err != nil {
		return nil, err
	}
	docs, _ := app.Collection("user").Where("email", "==", *email).Documents(ctx).GetAll()

	for _, doc := range docs {
		log.Default().Println("updating firestore")
		updateData := map[string]interface{}{
			"pubKey": req.GetPubKey(),

			"updatedAt": time.Now(),
		}

		_, err := app.Collection("user").Doc(doc.Ref.ID).Set(ctx, updateData, firestore.MergeAll)
		if err != nil {
			log.Default().Println("Firebase update error")
		}
	}
	return req, err
}
