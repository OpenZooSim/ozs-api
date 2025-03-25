package models

type ZooCreateDTO struct {
	Name        string  `json:"name"`
	Description *string `json:"description"`
	UserID      *int    `json:"user_id"`
	ShardID     *int    `json:"shard_id"`
}
