import dataSource from "../providers/datasource.provider";

export class Database {
    public static async InitializeDBConnection(): Promise<void> {
        try {
            const ds = await dataSource.initialize();
            if (ds.isInitialized) {
                console.log("Database is connected!");
            } else {
                console.error(
                    "An error occurred when attempting to initialize the database and the operation could not be completed. Please check your connection string options...",
                );
                process.exit(1);
            }
        } catch (error: any) {
            console.error("Error during database initialization: ", error);
            process.exit(1);
        }
    }
}
