package utils

import (
	"context"
	"fmt"
	"os"
	"strings"
	"time"

	"github.com/dgrijalva/jwt-go"
	"github.com/joho/godotenv"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/metadata"
	"google.golang.org/grpc/status"
)

func CreateToken(id string) (string, error) {
	token := jwt.New(jwt.SigningMethodHS256)
	godotenv.Load()
	secretKey := os.Getenv("JWT_SECRET_KEY")
	if len(secretKey) == 0 {
		panic("JWT_SECRET_KEY Required in .env")
	}
	claims := token.Claims.(jwt.MapClaims)
	claims["user_id"] = id
	claims["exp"] = time.Now().Add(time.Hour * 24).Unix()

	tokenString, err := token.SignedString([]byte(secretKey))
	if err != nil {
		return "", fmt.Errorf("failed to sign the token: %v", err)
	}

	return tokenString, nil
}

type CustomClaims struct {
	UserID string `json:"user_id"`
	jwt.StandardClaims
}

func ValidateToken(tokenString string) (string, error) {
	godotenv.Load()
	secretKey := os.Getenv("JWT_SECRET_KEY")
	if len(secretKey) == 0 {
		panic("JWT_SECRET_KEY Required in .env")
	}
	token, err := jwt.ParseWithClaims(tokenString, &CustomClaims{}, func(token *jwt.Token) (interface{}, error) {
		return []byte(secretKey), nil
	})

	if err != nil {
		if ve, ok := err.(*jwt.ValidationError); ok {
			if ve.Errors&jwt.ValidationErrorMalformed != 0 {
				return "", fmt.Errorf("token is malformed")
			} else if ve.Errors&(jwt.ValidationErrorExpired|jwt.ValidationErrorNotValidYet) != 0 {
				return "", fmt.Errorf("token has expired or is not valid yet")
			} else {
				return "", fmt.Errorf("token validation error: %v", err)
			}
		}
		return "", fmt.Errorf("failed to parse the token: %v", err)
	}

	// Check if the token is valid
	if !token.Valid {
		return "", fmt.Errorf("invalid token")
	}

	// Extract custom claims
	claims, ok := token.Claims.(*CustomClaims)
	if !ok {
		return "", fmt.Errorf("failed to extract custom claims")
	}

	// Check expiration time
	if !claims.VerifyExpiresAt(time.Now().Unix(), true) {
		return "", fmt.Errorf("token has expired")
	}

	return claims.UserID, nil
}

func GetTokenFromMetaDataAndValidate(ctx context.Context) (*string, error) {
	md, ok := metadata.FromIncomingContext(ctx)

	if !ok {
		return nil, status.Errorf(codes.Unauthenticated, "Metadata not found")
	}
	authorization := md.Get("authorization")
	if len(authorization) == 0 {
		return nil, status.Errorf(codes.Unauthenticated, "Authorization token not provided")
	}
	token := strings.Split(authorization[0], " ")
	if len(token) == 1 {
		return nil, status.Errorf(codes.Unauthenticated, "Authorization token not provided")

	}
	data, err := ValidateToken(token[1])
	if err != nil {
		return nil, status.Errorf(codes.Unauthenticated, "Authorization token not provided")

	}
	fmt.Println("GetTokenFromMetaDataAndValidate", token, data)
	return &data, nil

}
