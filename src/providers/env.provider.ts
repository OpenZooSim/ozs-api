import { config } from "dotenv";

export interface IRawEnv {
    DB_CONNECTION_STRING: string;
}

export class EnvProvider {
    public static LoadRawEnv(): IRawEnv {
        // This is only used for local development, but it will load the contents of the .env file.
        // In all other cases, variables are loaded into ENV vars by the host system.
        config();

        // Validate ENV vars before allowing the application to start.
        let missingVars = "";

        const dbConnectionString = process.env.DB_CONNECTION_STRING;
        if (!dbConnectionString) {
            missingVars += "DB_CONNECTION_STRING ";
        }

        if (missingVars) {
            console.error(
                `The following ENV vars are required and were not set: [${missingVars.trim()}]`,
            );
            process.exit(1);
        }

        return {
            DB_CONNECTION_STRING: (dbConnectionString as string).toString(),
        };
    }
}
