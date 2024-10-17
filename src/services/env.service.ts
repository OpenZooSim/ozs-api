import { singleton } from "tsyringe";
import { EnvProvider, IRawEnv } from "../providers/env.provider";

@singleton()
export class EnvService {
    private readonly _rawEnv: IRawEnv;

    constructor() {
        this._rawEnv = EnvProvider.LoadRawEnv();
    }

    /**
     * The database connection string.
     */
    public get DBConnectionString(): string {
        return this._rawEnv.DB_CONNECTION_STRING;
    }
}
