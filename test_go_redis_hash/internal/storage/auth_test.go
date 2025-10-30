package storage

import (
	"context"
	"fmt"
	"testing"

	"github.com/dxps/redis_playground/test_go_redis_hash/internal/domain/auth"
	"github.com/go-redis/redis/v8"
	"github.com/stretchr/testify/assert"
)

var rds *redis.Client

func init() {
	rds = redis.NewClient(&redis.Options{
		Addr:     "localhost:6379",
		Password: "",
	})
}

func newToken(tokenID string) auth.Token {
	return auth.Token{
		ID: tokenID,
		User: auth.User{
			ID:   "user-1",
			Name: "Robert De Niro",
			Permissions: []auth.Permission{
				auth.PermissionRead,
				auth.PermissionWrite,
			},
		},
	}
}

func TestAuth_Create(t *testing.T) {
	storage := NewAuth(rds)

	assert.NoError(t, storage.Create(context.Background(), newToken("d3d9446802a44259755d38e6d163e820")))
}

func TestAuth_Find_Error(t *testing.T) {
	storage := NewAuth(rds)

	assert.NoError(t, storage.Create(context.Background(), newToken("98f13708210194c475687be6106a3b84")))

	token, err := storage.Find(context.Background(), "unknown-token-id")
	assert.Nil(t, token)
	assert.Error(t, fmt.Errorf("find: not found"), err)
}

func TestAuth_Find_Success(t *testing.T) {
	storage := NewAuth(rds)

	assert.NoError(t, storage.Create(context.Background(), newToken("34173cb38f07f89ddbebc2ac9128303f")))

	token, err := storage.Find(context.Background(), "34173cb38f07f89ddbebc2ac9128303f")
	assert.NoError(t, err)
	assert.IsType(t, &auth.Token{}, token)
}
