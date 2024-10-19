import { Router } from "express";

export class V1BaseController {
    public _router: Router;

    constructor() {
        this._router = Router();
    }
}
