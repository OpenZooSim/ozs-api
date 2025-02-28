package services

import (
	"encoding/base64"
	"errors"
	"fmt"
	"strings"

	"github.com/snowlynxsoftware/ozs-api/server/database/repositories"
	"github.com/snowlynxsoftware/ozs-api/server/models"
)

type AuthService struct {
	UserRepository *repositories.UserRepository
	TokenService   *TokenService
	CryptoService  *CryptoService
	EmailService   *EmailService
}

func NewAuthService(
	userRepository *repositories.UserRepository,
	tokenService *TokenService,
	cryptoService *CryptoService,
	emailService *EmailService,
) *AuthService {
	return &AuthService{UserRepository: userRepository, TokenService: tokenService, CryptoService: cryptoService, EmailService: emailService}
}

func (s *AuthService) RegisterNewUser(dto *models.UserCreateDTO) (*repositories.UserEntity, error) {

	var _, err = s.UserRepository.GetUserByEmail(dto.Email)
	if err == nil {
		return nil, errors.New("a user already exists with the specified email")
	}

	hashedPassword, err := s.CryptoService.HashPassword(dto.Password)
	if err != nil {
		return nil, err
	}

	dto.Password = *hashedPassword

	newUser, err := s.UserRepository.CreateNewUser(dto)
	if err != nil {
		return nil, err
	}

	verificationToken, err := s.TokenService.GenerateVerificationToken(int(newUser.ID))
	if err != nil {
		return nil, err
	}

	var emailOptions = &EmailSendOptions{}
	emailOptions.FromEmail = "do-not-reply@openzoosim.com"
	emailOptions.ToEmail = newUser.Email
	emailOptions.Subject = "OpenZooSim - Verify Your Account"
	// TODO: Update this to use the correct URL
	emailOptions.HTMLContent = GetNewUserEmailTemplate("http://localhost:3000", *verificationToken)
	var isEmailSuccess = s.EmailService.SendEmail(emailOptions)
	if isEmailSuccess {
		return newUser, nil
	} else {
		return nil, errors.New("the user was created but the verification email failed to send")
	}
}

func (s *AuthService) SendLoginEmail(email string) (*repositories.UserEntity, error) {

	var user, err = s.UserRepository.GetUserByEmail(email)
	if err != nil {
		return nil, errors.New("user does not exist")
	}

	if user.IsBanned {
		return nil, errors.New("user is banned")
	}

	verificationToken, err := s.TokenService.GenerateLoginWithEmailToken(int(user.ID))
	if err != nil {
		return nil, err
	}

	var emailOptions = &EmailSendOptions{}
	emailOptions.FromEmail = "do-not-reply@openzoosim.com"
	emailOptions.ToEmail = user.Email
	emailOptions.Subject = "OpenZooSim - Login Email"
	// TODO: Update this to use the correct URL
	emailOptions.HTMLContent = GetLoginEmailTemplate("http://localhost:3000", *verificationToken)
	var isEmailSuccess = s.EmailService.SendEmail(emailOptions)
	if isEmailSuccess {
		return user, nil
	} else {
		return nil, errors.New("the login by email failed to send")
	}
}

func (s *AuthService) LoginWithEmailLink(userId *int) (*models.UserLoginResponseDTO, error) {

	accessToken, err := s.TokenService.GenerateAccessToken(*userId)
	if err != nil {
		fmt.Println(err.Error())
		return nil, errors.New("there was an issue trying to log this user in")
	}

	return &models.UserLoginResponseDTO{
		AccessToken:  *accessToken,
		RefreshToken: "",
	}, nil
}

func (s *AuthService) VerifyNewUser(verificationToken *string) (*int, error) {

	var userId, err = s.TokenService.ValidateToken(verificationToken)
	if err != nil {
		fmt.Println(err.Error())
		return nil, errors.New("the token could not be verified")
	}

	_, err = s.UserRepository.MarkUserVerified(userId)
	if err != nil {
		fmt.Println(err.Error())
		return nil, err
	}

	return userId, nil
}

func (s *AuthService) Login(authHeaderStr *string) (*models.UserLoginResponseDTO, error) {

	encodedCredentials := strings.TrimPrefix(*authHeaderStr, "Basic ")
	decodedCredentials, err := base64.StdEncoding.DecodeString(encodedCredentials)
	if err != nil {
		return nil, errors.New("failed to decode authorization header")
	}

	credentials := strings.SplitN(string(decodedCredentials), ":", 2)
	if len(credentials) != 2 {
		return nil, errors.New("invalid authorization header format")
	}

	email := credentials[0]
	password := credentials[1]

	user, err := s.UserRepository.GetUserByEmail(email)
	if err != nil {
		return nil, errors.New("there was an issue trying to log this user in")
	}

	isValid, err := s.CryptoService.ValidatePassword(password, *user.PasswordHash)
	if err != nil || !isValid {
		return nil, errors.New("there was an issue trying to log this user in")
	}

	accessToken, err := s.TokenService.GenerateAccessToken(int(user.ID))
	if err != nil {
		return nil, errors.New("there was an issue trying to log this user in")
	}

	return &models.UserLoginResponseDTO{
		AccessToken:  *accessToken,
		RefreshToken: "",
	}, nil
}
