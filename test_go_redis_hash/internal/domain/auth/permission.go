package auth

type Permission string

const (
	PermissionRead  Permission = "Read"
	PermissionWrite Permission = "Write"
)
