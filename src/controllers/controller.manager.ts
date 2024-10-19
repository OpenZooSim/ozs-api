import { Router } from "express";
import { container } from "tsyringe";
import { V1HealthController } from "./v1/v1health.controller";

export class ControllerManager {
    public static InitializeAppControllers() {
        const mainController = Router();

        mainController.use("/v1", this._initializeV1Controllers());

        return mainController;
    }

    private static _initializeV1Controllers(): Router {
        const v1Controller = Router();

        const v1HealthController = container.resolve(V1HealthController);

        v1Controller.use("/health", v1HealthController._router);

        return v1Controller;
    }
}
