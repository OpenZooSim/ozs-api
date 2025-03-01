package services

import (
	"github.com/snowlynxsoftware/ozs-api/server/database/repositories"
)

type AdminService struct {
	userRepository *repositories.UserRepository
}

func NewAdminService(userRepository *repositories.UserRepository) *AdminService {
	return &AdminService{userRepository: userRepository}
}

func (s *AdminService) BanUserById(id int, reason string) (bool, error) {
	return s.userRepository.BanUserByIdWithReason(&id, reason)
}

func (s *AdminService) UnbanUserById(id int) (bool, error) {
	return s.userRepository.UnbanUserById(&id)
}

func (s *AdminService) SetCanUseAPIKeysForUser(id int, value bool) (bool, error) {
	return s.userRepository.SetCanUseAPIKeysByUserId(&id, value)
}

func (s *AdminService) SetUserTypeForUser(userId int, typeId int) (bool, error) {
	return s.userRepository.SetUserTypeId(&userId, &typeId)
}