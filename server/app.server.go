package server

import (
	"fmt"
	"log"
	"net/http"

	"github.com/go-chi/chi/v5"
	mid "github.com/go-chi/chi/v5/middleware"
	"github.com/snowlynxsoftware/ozs-api/config"
	"github.com/snowlynxsoftware/ozs-api/server/controllers"
	"github.com/snowlynxsoftware/ozs-api/server/database"
	"github.com/snowlynxsoftware/ozs-api/server/database/repositories"
	"github.com/snowlynxsoftware/ozs-api/server/middleware"
	"github.com/snowlynxsoftware/ozs-api/server/services"
)

type AppServer struct {
	AppConfig *config.AppConfig
	Router    *chi.Mux
	DB        *database.AppDataSource
}

func NewAppServer() *AppServer {

	r := chi.NewRouter()
	r.Use(mid.Logger)

	return &AppServer{
		AppConfig: config.NewAppConfig(),
		Router:    r,
	}
}

func (s *AppServer) Start() {

	// Connect to DB
	s.DB = database.NewAppDataSource()
	s.DB.Connect(s.AppConfig.DBConnectionString)

	// Configure Repositories
	userRepository := repositories.NewUserRepository(s.DB)

	// Configure Services
	emailService := services.NewEmailService(s.AppConfig.SendgridAPIKey)
	cryptoService := services.NewCryptoService(s.AppConfig.HashPepper)
	tokenService := services.NewTokenService(s.AppConfig.JWTSecret)
	authService := services.NewAuthService(userRepository, tokenService, cryptoService, emailService)
	userService := services.NewUserService(userRepository, tokenService)

	// Configure Middleware
	authMiddleware := middleware.NewAuthMiddleware(userService)

	// Configure Controllers
	s.Router.Mount("/health", controllers.NewHealthController().MapController())
	s.Router.Mount("/settings", controllers.NewSettingsController().MapController())
	s.Router.Mount("/users", controllers.NewUserController(userService).MapController())
	s.Router.Mount("/auth", controllers.NewAuthController(authMiddleware, userService, authService).MapController())

	fmt.Printf("Server starting on port %s\n", s.AppConfig.AppPort)
	log.Fatal(http.ListenAndServe(":"+s.AppConfig.AppPort, s.Router))
}
