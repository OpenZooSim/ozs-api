import { singleton } from "tsyringe";
import express from "express";
import { Server } from "http";
import { EnvService } from "./services/env.service";
import { Database } from "./database/database";

@singleton()
export class AppServer {
    private _app: express.Application;

    constructor(private readonly _envService: EnvService) {
        this._app = express();
    }

    public async Start(): Promise<Server> {
        console.log(this._envService.DBConnectionString);

        // Initialize DB Connection
        await Database.InitializeDBConnection();

        return this._app.listen(3000);
    }
}
