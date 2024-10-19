import { container } from "tsyringe";
import { DataSource } from "typeorm";
import { EnvService } from "../services/env.service";

const _envService = container.resolve(EnvService);

console.log(__dirname);

export default new DataSource({
    type: "postgres",
    url: _envService.DBConnectionString,
    migrations: [__dirname + "/../database/migrations/*.{ts,js}"],
    entities: [__dirname + "/../database/entities/*.{ts,js}"],
});
