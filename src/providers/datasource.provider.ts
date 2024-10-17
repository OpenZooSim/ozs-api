import { container } from "tsyringe";
import { DataSource } from "typeorm";
import { EnvService } from "../services/env.service";

const _envService = container.resolve(EnvService);

export default new DataSource({
    type: "postgres",
    url: _envService.DBConnectionString,
    entities: [],
});
