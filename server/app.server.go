package server

import (
	"log"
	"net/http"

	"github.com/go-chi/chi/v5"
	mid "github.com/go-chi/chi/v5/middleware"
	"github.com/rs/zerolog"
	"github.com/snowlynxsoftware/ozs-api/config"
	"github.com/snowlynxsoftware/ozs-api/server/controllers"
	"github.com/snowlynxsoftware/ozs-api/server/database"
	"github.com/snowlynxsoftware/ozs-api/server/database/repositories"
	"github.com/snowlynxsoftware/ozs-api/server/middleware"
	"github.com/snowlynxsoftware/ozs-api/server/services"
	"github.com/snowlynxsoftware/ozs-api/server/util"
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

	// Setup Logger
	util.SetupZeroLogger(zerolog.DebugLevel)
	util.LogDebug("Starting server...")

	// Connect to DB
	s.DB = database.NewAppDataSource()
	s.DB.Connect(s.AppConfig.DBConnectionString)

	// Configure Repositories
	userTypeRepository := repositories.NewUserTypeRepository(s.DB)
	userRepository := repositories.NewUserRepository(s.DB)
	userLoginHistoryRepository := repositories.NewUserLoginHistoryRepository(s.DB)
	shardRepository := repositories.NewShardRepository(s.DB)
	zooRepository := repositories.NewZooRepository(s.DB)

	// Configure Services
	emailService := services.NewEmailService(s.AppConfig.SendgridAPIKey)
	cryptoService := services.NewCryptoService(s.AppConfig.HashPepper)
	tokenService := services.NewTokenService(s.AppConfig.JWTSecret)
	authService := services.NewAuthService(userRepository, userLoginHistoryRepository, tokenService, cryptoService, emailService)
	userService := services.NewUserService(userRepository, tokenService)
	shardService := services.NewShardService(shardRepository)
	zooService := services.NewZooService(zooRepository)

	// Configure Middleware
	authMiddleware := middleware.NewAuthMiddleware(userService, userTypeRepository)

	// Configure Controllers
	s.Router.Mount("/health", controllers.NewHealthController().MapController())
	s.Router.Mount("/settings", controllers.NewSettingsController().MapController())
	s.Router.Mount("/users", controllers.NewUserController(userService).MapController())
	s.Router.Mount("/auth", controllers.NewAuthController(authMiddleware, userService, authService).MapController())
	s.Router.Mount("/shards", controllers.NewShardController(shardService).MapController())
	s.Router.Mount("/zoos", controllers.NewZooController(authMiddleware, zooService).MapController())

	// Catch-all route
	s.Router.Get("/*", func(w http.ResponseWriter, r *http.Request) {
		util.LogWarning("Route not found: " + r.URL.Path)
		w.Write([]byte("404 route not found"))
		http.NotFound(w, r)
	})

	util.LogInfo("Server starting on port " + s.AppConfig.AppPort)
	log.Fatal(http.ListenAndServe(":"+s.AppConfig.AppPort, s.Router))
}
