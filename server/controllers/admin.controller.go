package controllers

import (
	"encoding/json"
	"fmt"
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/snowlynxsoftware/ozs-api/server/middleware"
	"github.com/snowlynxsoftware/ozs-api/server/services"
)

type AdminController struct {
	authMiddleware *middleware.AuthMiddleware
	adminService *services.AdminService
}

func NewHAdminController(authMiddleware *middleware.AuthMiddleware, adminService *services.AdminService) *AdminController {
	return &AdminController{
		authMiddleware: authMiddleware,
		adminService: adminService,
	}
}

func (s *AdminController) MapController() *chi.Mux {
	r := chi.NewRouter()
	r.Get("/", func(w http.ResponseWriter, r *http.Request) {
		w.Write([]byte("hello, world! zzz"))
	})
	return r
}

func (c *AdminController) banUser(w http.ResponseWriter, r *http.Request) {

	userContext, err := c.authMiddleware.Authorize(r, nil)
	if err != nil {
		fmt.Println(err.Error())
		http.Error(w, "an error occurred when verifying auth status", http.StatusUnauthorized)
		return
	}

	returnStr, err := json.Marshal(userContext)
	if err != nil {
		http.Error(w, "failed to create response", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write(returnStr)

}
