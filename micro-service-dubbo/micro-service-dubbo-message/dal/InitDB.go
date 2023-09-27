package main

import (
	"github.com/acmestack/gorm-plus/gplus"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
	"gorm.io/gorm/logger"
	"log"
	"time"
)

type User struct {
	ID        int64
	Username  string
	Password  string
	Address   string
	Age       int
	Phone     string
	Score     int
	Dept      string
	CreatedAt time.Time
	UpdatedAt time.Time
}

var gormDb *gorm.DB

func init() {
	dsn := "root:root@tcp(124.222.147.194:3306)/db1?charset=utf8mb4&parseTime=True&loc=Local"
	var err error
	gormDb, err = gorm.Open(mysql.Open(dsn), &gorm.Config{

		Logger: logger.Default.LogMode(logger.Info),
	})
	if err != nil {
		log.Println(err)
	}

	// 初始化gplus
	gplus.Init(gormDb)
}

func main() {
	users, resultDb := gplus.SelectList[User](nil)
	log.Println("error:", resultDb.Error)
	log.Println("RowsAffected:", resultDb.RowsAffected)
	for _, user := range users {
		log.Println("user:", user)
	}
}
