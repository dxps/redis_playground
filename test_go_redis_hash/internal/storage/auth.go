package storage

import (
	"context"
	"fmt"
	"time"

	"github.com/dxps/redis_playground/test_go_redis_hash/internal/domain/auth"
	"github.com/go-redis/redis/v8"
)

type Auth struct {
	rds *redis.Client
}

func NewAuth(rds *redis.Client) Auth {
	return Auth{rds: rds}
}

func (a Auth) Create(ctx context.Context, token auth.Token) error {
	if _, err := a.rds.HSetNX(ctx, auth.CacheHashKey(token.ID), auth.CacheHashField(), &token).Result(); err != nil {
		return fmt.Errorf("create: redis error: %w", err)
	}
	a.rds.Expire(ctx, auth.CacheHashKey(token.ID), time.Minute)

	return nil
}

func (a Auth) Find(ctx context.Context, tokenID string) (*auth.Token, error) {
	result, err := a.rds.HGet(ctx, auth.CacheHashKey(tokenID), auth.CacheHashField()).Result()
	if err != nil && err != redis.Nil {
		return nil, fmt.Errorf("find: redis error: %w", err)
	}
	if result == "" {
		return nil, fmt.Errorf("find: not found")
	}

	token := &auth.Token{}
	if err := token.UnmarshalBinary([]byte(result)); err != nil {
		return nil, fmt.Errorf("find: unmarshal error: %w", err)
	}

	return token, nil
}

func (a Auth) Update(ctx context.Context, token auth.Token) error {
	// Find token:     a.rds.HGet()
	// Override token: a.rds.HSet()

	return nil
}

func (a Auth) Delete(ctx context.Context, tokenID string) error {
	// Find token:   a.rds.HGet()
	// Delete token: a.rds.Del()

	return nil
}
