import { singleton } from "tsyringe";
import express from "express";
import { Server } from "http";

@singleton()
export class AppServer {
    private _app: express.Application;

    constructor() {
        this._app = express();
    }

    public async Start(): Promise<Server> {
        return this._app.listen(3000);
    }
}
