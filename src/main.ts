import "reflect-metadata";
import { container } from "tsyringe";
import { AppServer } from "./app.server";
import { AddressInfo } from "net";

const bootstrap = async () => {
    process.on("unhandledRejection", (reason, promise) => {
        console.error("Unhandled Rejection at:", promise, "reason:", reason);
    });

    process.on("uncaughtException", (error) => {
        console.error("Uncaught Exception thrown:", error);
    });

    const appServer = container.resolve(AppServer);
    const httpServer = await appServer.Start();
    const addr = httpServer.address() as AddressInfo;
    console.log(`Server is listening on port: [${addr.port}]`);
};

bootstrap().catch((error) => {
    console.error(error.message);
    process.exit(1);
});
