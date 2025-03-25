package repositories

import (
	"time"

	"github.com/snowlynxsoftware/ozs-api/server/database"
	"github.com/snowlynxsoftware/ozs-api/server/models"
)

type ZooEntity struct {
	ID           int64      `json:"id" db:"id"`
	CreatedAt    time.Time  `json:"created_at" db:"created_at"`
	ModifiedAt   *time.Time `json:"modified_at" db:"modified_at"`
	IsArchived   bool       `json:"is_archived" db:"is_archived"`
	Name         string     `json:"name" db:"name"`
	Description  *string    `json:"description" db:"description"`
	BannerImgURL *string    `json:"banner_img_url" db:"banner_img_url"`
	CurrentMoney float64    `json:"current_money" db:"current_money"`
	UserID       *int       `json:"user_id" db:"user_id"`
	ShardID      *int       `json:"shard_id" db:"shard_id"`
}

type ZooRepository struct {
	DB *database.AppDataSource
}

func NewZooRepository(db *database.AppDataSource) *ZooRepository {
	return &ZooRepository{
		DB: db,
	}
}

func (r *ZooRepository) GetZooById(id int) (*ZooEntity, error) {
	zoo := &ZooEntity{}
	sql := `SELECT
		*
	FROM zoos
	WHERE id = $1 AND is_archived = false`
	err := r.DB.DB.Get(zoo, sql, id)
	if err != nil {
		return nil, err
	}
	return zoo, nil
}

func (r *ZooRepository) GetZoosByUserId(userId int) ([]*ZooEntity, error) {
	zoos := []*ZooEntity{}
	sql := `SELECT
		*
	FROM zoos
	WHERE user_id = $1 AND is_archived = false`
	err := r.DB.DB.Select(&zoos, sql, userId)
	if err != nil {
		return nil, err
	}
	return zoos, nil
}

func (r *ZooRepository) CreateNewZoo(dto *models.ZooCreateDTO) (*ZooEntity, error) {
	sql := `INSERT INTO zoos (name, description, user_id, shard_id)
	VALUES ($1, $2, $3, $4, $5)
	RETURNING id;`
	row := r.DB.DB.QueryRow(sql, dto.Name, dto.Description, dto.UserID, dto.ShardID)
	var insertedId int
	err := row.Scan(&insertedId)
	if err != nil {
		return nil, err
	}

	zoo, err := r.GetZooById(insertedId)
	if err != nil {
		return nil, err
	}

	return zoo, nil
}

func (r *ZooRepository) UpdateZoo(id int, dto *models.ZooCreateDTO) (*ZooEntity, error) {
	sql := `UPDATE zoos 
	SET 
		name = $1, 
		description = $2, 
		banner_img_url = $3
	WHERE id = $4 AND is_archived = false;`

	_, err := r.DB.DB.Exec(sql, dto.Name, dto.Description, id)
	if err != nil {
		return nil, err
	}

	return r.GetZooById(id)
}

func (r *ZooRepository) ArchiveZoo(id int) (bool, error) {
	sql := `UPDATE zoos 
	SET 
		is_archived = true
	WHERE id = $1;`

	_, err := r.DB.DB.Exec(sql, id)
	if err != nil {
		return false, err
	}

	return true, nil
}

func (r *ZooRepository) UpdateZooMoney(id int, amount float64) (bool, error) {
	sql := `UPDATE zoos 
	SET 
		current_money = $1
	WHERE id = $2 AND is_archived = false;`

	_, err := r.DB.DB.Exec(sql, amount, id)
	if err != nil {
		return false, err
	}

	return true, nil
}
