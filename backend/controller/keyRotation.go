package controller

import (
	"context"
	"log"
	"time"

	"cloud.google.com/go/firestore"
	"example.com/messenger/firebase"
	"example.com/messenger/pb"
	"example.com/messenger/utils"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

func RotateKeyService(ctx context.Context, req *pb.KeyRotationRequest) (*pb.KeyRotationResponse, error) {
	if len(req.GetPubKey()) == 0 {
		return nil, status.Error(codes.InvalidArgument, "Public key is required")
	}

	app, err := firebase.InitFirebase().Firestore(ctx)
	if err != nil {
		return nil, status.Error(codes.Internal, "Internal server error")
	}

	number, err := utils.GetTokenFromMetaDataAndValidate(ctx)
	if err != nil {
		return nil, err
	}

	docs, err := app.Collection("user").Where("phoneNumber", "==", *number).Limit(1).Documents(ctx).GetAll()
	if err != nil {
		return nil, status.Error(codes.Internal, "Failed to load user")
	}
	if len(docs) == 0 {
		return nil, status.Error(codes.NotFound, "User not found")
	}

	doc := docs[0]
	currentVersion := getKeyVersion(doc.Data()["keyVersion"])
	rotatedAt := time.Now().UTC().Format(time.RFC3339)
	updateData := map[string]interface{}{
		"pubKey":       req.GetPubKey(),
		"keyVersion":   currentVersion + 1,
		"keyRotatedAt": rotatedAt,
		"updatedAt":    time.Now(),
	}

	if _, err := app.Collection("user").Doc(doc.Ref.ID).Set(ctx, updateData, firestore.MergeAll); err != nil {
		log.Default().Println("Failed to rotate key", err)
		return nil, status.Error(codes.Internal, "Failed to rotate key")
	}

	return &pb.KeyRotationResponse{
		Rotated:    true,
		PubKey:     req.GetPubKey(),
		KeyVersion: currentVersion + 1,
		RotatedAt:  rotatedAt,
		Message:    "Key rotation completed",
	}, nil
}

func getKeyVersion(value interface{}) int32 {
	switch typed := value.(type) {
	case int32:
		return typed
	case int64:
		return int32(typed)
	case float64:
		return int32(typed)
	default:
		return 0
	}
}
