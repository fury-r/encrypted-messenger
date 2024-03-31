package utils

import (
	"math/rand"
	"time"
)

const (
	uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
	lowercaseLetters = "abcdefghijklmnopqrstuvwxyz"
	digits           = "0123456789"
	specialChars     = "!@#$%^&*()-_=+[]{}|;:,.<>?/~`"
)

func GenerateComplexPassword(length int) string {
	rand.Seed(time.Now().UnixNano())

	allChars := uppercaseLetters + lowercaseLetters + digits + specialChars

	password := make([]byte, length)
	for i := range password {
		password[i] = allChars[rand.Intn(len(allChars))]
	}

	return string(password)
}
