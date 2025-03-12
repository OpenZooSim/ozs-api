package controllers

import (
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/snowlynxsoftware/ozs-api/server/util"
)

type HealthController struct {
}

func NewHealthController() *HealthController {
	return &HealthController{}
}

func (s *HealthController) MapController() *chi.Mux {
	r := chi.NewRouter()
	r.Get("/", func(w http.ResponseWriter, r *http.Request) {
		util.LogInfo("health check is healthy")
		w.Write([]byte("ok"))
	})
	return r
}
