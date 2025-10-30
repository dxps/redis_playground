package auth

type User struct {
	ID          string
	Name        string
	Permissions []Permission
}
