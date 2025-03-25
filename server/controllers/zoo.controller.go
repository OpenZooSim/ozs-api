package controllers

import (
	"encoding/json"
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/snowlynxsoftware/ozs-api/server/middleware"
	"github.com/snowlynxsoftware/ozs-api/server/services"
	"github.com/snowlynxsoftware/ozs-api/server/util"
)

type ZooController struct {
	authMiddleware *middleware.AuthMiddleware
	zooService     *services.ZooService
}

func NewZooController(authMiddleware *middleware.AuthMiddleware, zooService *services.ZooService) *ZooController {
	return &ZooController{
		authMiddleware: authMiddleware,
		zooService:     zooService,
	}
}

func (s *ZooController) MapController() *chi.Mux {
	r := chi.NewRouter()
	r.Get("/self", s.getMyZoo)
	return r
}

func (c *ZooController) getMyZoo(w http.ResponseWriter, r *http.Request) {

	userContext, err := c.authMiddleware.Authorize(r, nil)
	if err != nil {
		util.LogErrorWithStackTrace(err)
		http.Error(w, "an error occurred when verifying auth status", http.StatusUnauthorized)
		return
	}

	zoo, err := c.zooService.GetZooById(userContext.Id)
	if err != nil {
		util.LogErrorWithStackTrace(err)
		http.Error(w, "an error occurred when getting zoo", http.StatusInternalServerError)
		return
	}

	returnStr, err := json.Marshal(zoo)
	if err != nil {
		http.Error(w, "failed to create response", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write(returnStr)

}
