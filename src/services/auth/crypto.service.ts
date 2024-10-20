import { singleton } from "tsyringe";
import { hash, compare } from "bcrypt";
import { createHash } from "node:crypto";

@singleton()
export class CryptoService {
    private readonly _saltRounds = 13;

    constructor() {}

    // Method to hash a password
    public async Hash(
        password: string,
        pepper: string,
    ): Promise<string | null> {
        try {
            const checksum = this._encode(password, pepper);
            return hash(checksum, this._saltRounds);
        } catch (error: any) {
            console.error(error.message);
            return null;
        }
    }

    // Method to verify a password against a hash
    public async Verify(password: string, hash: string): Promise<boolean> {
        try {
            const value = await compare(password, hash);
            if (value) {
                return value;
            } else {
                throw new Error("Password could not be verified!");
            }
        } catch (error: any) {
            console.error(error.message);
            return false;
        }
    }

    private _encode(password: string, pepper: string): string {
        const rawString = password + pepper;
        const hash = createHash("sha256");
        hash.update(rawString);
        return hash.digest("hex");
    }
}
