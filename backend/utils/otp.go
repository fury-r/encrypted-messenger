package utils

import (
	"fmt"
	"math/rand"
	"strconv"
)

func GenerateOtp() string {

	var otp string

	for i := 0; i < 6; i++ {
		otp = otp + strconv.Itoa(rand.Intn(6))
	}
	fmt.Println("Otp generated ", otp)
	return otp
}
