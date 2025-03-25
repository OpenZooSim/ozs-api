package repositories

import (
	"time"

	"github.com/snowlynxsoftware/ozs-api/server/database"
)

type ShardEntity struct {
	ID          int64      `json:"id" db:"id"`
	CreatedAt   time.Time  `json:"created_at" db:"created_at"`
	ModifiedAt  *time.Time `json:"modified_at" db:"modified_at"`
	IsArchived  bool       `json:"is_archived" db:"is_archived"`
	Name        string     `json:"name" db:"name"`
	Description string     `json:"description" db:"description"`
}

type ShardRepository struct {
	DB *database.AppDataSource
}

func NewShardRepository(db *database.AppDataSource) *ShardRepository {
	return &ShardRepository{
		DB: db,
	}
}

func (r *ShardRepository) GetShardById(id int) (*ShardEntity, error) {
	shard := &ShardEntity{}
	sql := `SELECT
		*
	FROM shards
	WHERE id = $1`
	err := r.DB.DB.Get(shard, sql, id)
	if err != nil {
		return nil, err
	}
	return shard, nil
}

func (r *ShardRepository) GetAllShards(isArchived bool) (*[]ShardEntity, error) {
	shards := []ShardEntity{}
	sql := `SELECT
		*
	FROM shards
	WHERE is_archived = $1`
	err := r.DB.DB.Select(&shards, sql, isArchived)
	if err != nil {
		return nil, err
	}
	return &shards, nil
}
