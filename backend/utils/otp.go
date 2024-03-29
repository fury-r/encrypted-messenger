package utils

import (
	"fmt"
	"math/rand"
	"strconv"
)

func GenerateOtp() string {
	min := 100000
	max := 999999

	randomNumber := rand.Intn(max - min)

	randomNumber += min

	fmt.Println("Otp generated ", randomNumber)
	return strconv.Itoa(randomNumber)
}
