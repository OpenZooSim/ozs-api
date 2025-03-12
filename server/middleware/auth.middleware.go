package middleware

import (
	"errors"
	"net/http"

	"github.com/snowlynxsoftware/ozs-api/server/database/repositories"
	"github.com/snowlynxsoftware/ozs-api/server/services"
	"github.com/snowlynxsoftware/ozs-api/server/util"
)

type AuthMiddleware struct {
	UserService        *services.UserService
	UserTypeRepository *repositories.UserTypeRepository
}

func NewAuthMiddleware(userService *services.UserService, userTypeRepo *repositories.UserTypeRepository) *AuthMiddleware {
	return &AuthMiddleware{
		UserService:        userService,
		UserTypeRepository: userTypeRepo,
	}
}

// If a request is authorized, it will return this context to the controller
// so that information from the user can be used as an immutable object.
type AuthorizedUserContext struct {
	Id       int    `json:"id"`
	Email    string `json:"email"`
	Username string `json:"username"`
}

func (m *AuthMiddleware) Authorize(r *http.Request, requiredUserTypeKeys []string) (*AuthorizedUserContext, error) {

	cookie, err := r.Cookie("access_token")
	if err != nil {
		util.LogErrorWithStackTrace(err)
		return nil, errors.New("access token not found in request")
	}

	userId, err := m.UserService.TokenService.ValidateToken(&cookie.Value)
	if err != nil {
		util.LogErrorWithStackTrace(err)
		return nil, err
	}

	userEntity, err := m.UserService.GetUserById(*userId)
	if err != nil {
		util.LogErrorWithStackTrace(err)
		return nil, err
	}

	if userEntity.IsArchived {
		return nil, errors.New("user is archived")
	}

	if !userEntity.IsVerified {
		return nil, errors.New("user is not verified")
	}

	if userEntity.IsBanned {
		return nil, errors.New("user is banned. Reason - " + *userEntity.BanReason)
	}

	userTypeEntity, err := m.UserTypeRepository.GetUserTypeById(*userEntity.UserTypeID)
	if err != nil {
		util.LogErrorWithStackTrace(err)
		return nil, err
	}

	if requiredUserTypeKeys != nil {
		isUserTypeAllowed := false
		for _, key := range requiredUserTypeKeys {
			if userTypeEntity.Key == key {
				isUserTypeAllowed = true
				break
			}
		}
		if !isUserTypeAllowed {
			return nil, errors.New("forbidden - user type is not allowed")
		}
	}

	return &AuthorizedUserContext{
		Id:       int(userEntity.ID),
		Email:    userEntity.Email,
		Username: userEntity.DisplayName,
	}, nil

}
