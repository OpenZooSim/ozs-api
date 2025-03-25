package controllers

import (
	"encoding/json"
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/snowlynxsoftware/ozs-api/server/services"
	"github.com/snowlynxsoftware/ozs-api/server/util"
)

type ShardController struct {
	shardService *services.ShardService
}

func NewShardController(shardService *services.ShardService) *ShardController {
	return &ShardController{
		shardService: shardService,
	}
}

func (s *ShardController) MapController() *chi.Mux {
	r := chi.NewRouter()
	r.Get("/", func(w http.ResponseWriter, r *http.Request) {

		allShards, err := s.shardService.GetAllShards(false)
		if err != nil {
			util.LogErrorWithStackTrace(err)
			http.Error(w, "failed to get all shards", http.StatusInternalServerError)
			return
		}

		returnStr, err := json.Marshal(allShards)
		if err != nil {
			http.Error(w, "failed to create response", http.StatusInternalServerError)
			return
		}

		w.Write(returnStr)
	})
	return r
}
