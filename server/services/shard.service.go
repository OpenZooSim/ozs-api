package services

import (
	"github.com/snowlynxsoftware/ozs-api/server/database/repositories"
)

type ShardService struct {
	shardRepository *repositories.ShardRepository
}

func NewShardService(shardRepository *repositories.ShardRepository) *ShardService {
	return &ShardService{shardRepository: shardRepository}
}

func (s *ShardService) GetShardById(id int) (*repositories.ShardEntity, error) {
	return s.shardRepository.GetShardById(id)
}

func (s *ShardService) GetAllShards(isArchived bool) (*[]repositories.ShardEntity, error) {
	return s.shardRepository.GetAllShards(isArchived)
}
