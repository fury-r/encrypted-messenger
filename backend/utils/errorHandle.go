package utils

func handleError(err error) bool {
	if err != nil {
		panic(err)
	}
	return false
}
