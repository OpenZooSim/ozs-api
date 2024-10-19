import { NextFunction, Request, Response } from "express";
import { V1BaseController } from "./v1base.controller";
import { singleton } from "tsyringe";

@singleton()
export class V1HealthController extends V1BaseController {
    constructor() {
        super();
        this._buildController();
    }

    private _getHealthCheck(_req: Request, res: Response, _next: NextFunction) {
        res.send("ok");
    }

    private _buildController() {
        this._router.get("/", this._getHealthCheck.bind(this));
    }
}
