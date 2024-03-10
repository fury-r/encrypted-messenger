package service

import (
	"context"
	"fmt"
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

	email, err := utils.ValidateToken(req.GetToken())
	if err != nil {
		x := "Invalid Token"
		fmt.Println(x)
	}
	fmt.Println("email " + email)
	docs, _ := app.Collection("user").Where("email", "==", email).Documents(ctx).GetAll()

	for _, doc := range docs {
		fmt.Println("updating firestore")
		updateData := map[string]interface{}{
			"pubKey": req.GetPubKey(),
			// ⚠️WARNING: For testing need to be removed later

			"privKey": req.GetPrivKey(),

			"updatedAt": time.Now(),
		}

		_, err := app.Collection("user").Doc(doc.Ref.ID).Set(ctx, updateData, firestore.MergeAll)
		if err != nil {
			fmt.Println("Firebase update error")
		}
	}
	return req, err
}
