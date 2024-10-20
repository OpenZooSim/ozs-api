import { singleton } from "tsyringe";
import { UserRepository } from "../../database/repositories/user.repository";
import { UserEntity } from "../../database/entities/user.entity";
import { CryptoService } from "./crypto.service";

@singleton()
export class UserRegistrationService {
    constructor(
        private readonly _userRepo: UserRepository,
        private readonly _cryptoService: CryptoService,
    ) {}

    public async RegisterNewUser(
        email: string,
        password: string,
        displayName: string,
    ): Promise<boolean> {
        const existingUser = await this._userRepo.GetUserByEmail(
            email.toLowerCase(),
        );
        if (existingUser) {
            throw new Error("A user with this email already exists.");
        }

        // Hash the password
        const hashedPassword = await this._cryptoService.Hash(
            password,
            "SuperS3cretToken!",
        );

        if (!hashedPassword) {
            return false;
        }

        // Create new user entity
        const newUser = new UserEntity();
        newUser.email = email.toLowerCase();
        newUser.passwordHash = hashedPassword;
        newUser.displayName = displayName;

        // Save the user in the database
        const savedUser = await this._userRepo.CreateUser(newUser);

        // Create a verification token
        // const token = sign({ id: savedUser.id }, "your_secret_key", {
        //     expiresIn: "1h",
        // });

        console.log({
            savedUser,
        });

        // Send verification email using SendGrid
        //await this._sendGridService.sendVerificationEmail(email, token);

        return true;
    }
}
