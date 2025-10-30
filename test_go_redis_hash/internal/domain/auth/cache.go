package auth

func CacheHashKey(tokenID string) string {
	return "app:auth:" + tokenID
}

func CacheHashField() string {
	return "token"
}
