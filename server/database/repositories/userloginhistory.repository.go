package repositories

import (
	"time"

	"github.com/snowlynxsoftware/ozs-api/server/database"
)

type UserLoginHistoryEntity struct {
	ID         int64      `json:"id" db:"id"`
	CreatedAt  time.Time  `json:"created_at" db:"created_at"`
	ModifiedAt *time.Time `json:"modified_at" db:"modified_at"`
	IsArchived bool       `json:"is_archived" db:"is_archived"`
	UserId     string     `json:"user_id" db:"user_id"`
}

type UserLoginHistoryRepository struct {
	DB *database.AppDataSource
}

func NewUserLoginHistoryRepository(db *database.AppDataSource) *UserLoginHistoryRepository {
	return &UserLoginHistoryRepository{
		DB: db,
	}
}

func (r *UserLoginHistoryRepository) CreateLoginHistoryForUser(userId *int) error {
	sql := `INSERT INTO user_login_history (user_id)
    VALUES ($1)
    RETURNING id;`
	row := r.DB.DB.QueryRow(sql, userId)
	var insertedId int
	err := row.Scan(&insertedId)
	if err != nil {
		return err
	}

	return nil
}
