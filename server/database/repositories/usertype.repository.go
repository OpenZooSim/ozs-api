package repositories

import (
	"time"

	"github.com/snowlynxsoftware/ozs-api/server/database"
)

type UserTypeEntity struct {
	ID            int64      `json:"id" db:"id"`
	CreatedAt     time.Time  `json:"created_at" db:"created_at"`
	ModifiedAt    *time.Time `json:"modified_at" db:"modified_at"`
	IsArchived    bool       `json:"is_archived" db:"is_archived"`
	Name         string     `json:"name" db:"name"`
	Key      string     `json:"key" db:"key"`
}

type UserTypeRepository struct {
	DB *database.AppDataSource
}

func NewUserTypeRepository(db *database.AppDataSource) *UserTypeRepository {
	return &UserTypeRepository{
		DB: db,
	}
}

func (r *UserTypeRepository) GetUserTypeById(id int) (*UserTypeEntity, error) {
	userType := &UserTypeEntity{}
	sql := `SELECT
		*
	FROM user_types
	WHERE id = $1`
	err := r.DB.DB.Get(userType, sql, id)
	if err != nil {
		return nil, err
	}
	return userType, nil
}

func (r *UserTypeRepository) GetUserTypeByKey(key string) (*UserTypeEntity, error) {
	userType := &UserTypeEntity{}
	sql := `SELECT
		*
	FROM user_types
	WHERE key = $1`
	err := r.DB.DB.Get(userType, sql, key)
	if err != nil {
		return nil, err
	}
	return userType, nil
}
