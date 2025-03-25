package services

import (
	"github.com/snowlynxsoftware/ozs-api/server/database/repositories"
	"github.com/snowlynxsoftware/ozs-api/server/models"
)

type ZooService struct {
	zooRepository *repositories.ZooRepository
}

func NewZooService(zooRepository *repositories.ZooRepository) *ZooService {
	return &ZooService{zooRepository: zooRepository}
}

func (s *ZooService) GetZooById(id int) (*repositories.ZooEntity, error) {
	return s.zooRepository.GetZooById(id)
}

func (s *ZooService) CreateNewZoo(dto *models.ZooCreateDTO) (*repositories.ZooEntity, error) {
	return s.zooRepository.CreateNewZoo(dto)
}
