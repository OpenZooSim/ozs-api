package main

import (
	_ "github.com/joho/godotenv/autoload"
	_ "github.com/lib/pq"
	"github.com/snowlynxsoftware/ozs-api/cmd"
)

func main() {
	err := cmd.NewCommandService().ParseCommand()
	if err != nil {
		panic(err)
	}
}
