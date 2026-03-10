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

func UpdateEcommerceIntegrationService(ctx context.Context, req *pb.EcommerceIntegrationRequest) (*pb.EcommerceIntegrationResponse, error) {
	if req.GetEnabled() && len(req.GetMerchantId()) == 0 {
		return nil, status.Error(codes.InvalidArgument, "Merchant ID is required when enabling integration")
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

	updateData := map[string]interface{}{
		"ecommerceEnabled":    req.GetEnabled(),
		"ecommerceMerchantId": req.GetMerchantId(),
		"ecommerceCatalogUrl": req.GetCatalogUrl(),
		"updatedAt":           time.Now(),
	}

	if _, err := app.Collection("user").Doc(docs[0].Ref.ID).Set(ctx, updateData, firestore.MergeAll); err != nil {
		log.Default().Println("Failed to update ecommerce integration", err)
		return nil, status.Error(codes.Internal, "Failed to update ecommerce integration")
	}

	return &pb.EcommerceIntegrationResponse{
		Enabled:    req.GetEnabled(),
		MerchantId: req.GetMerchantId(),
		CatalogUrl: req.GetCatalogUrl(),
		Message:    "Ecommerce integration updated",
	}, nil
}
