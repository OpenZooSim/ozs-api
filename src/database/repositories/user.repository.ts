import { singleton } from "tsyringe";
import { Repository } from "typeorm";
import { UserEntity } from "../entities/user.entity";

@singleton()
export class UserRepository extends Repository<UserEntity> {
    public async GetUserById(id: number): Promise<UserEntity | null> {
        return this.findOneBy({ id });
    }

    public async GetUserByEmail(email: string): Promise<UserEntity | null> {
        return this.findOneBy({ email });
    }

    public async CreateUser(
        userData: Partial<UserEntity>,
    ): Promise<UserEntity> {
        return this.save(this.create(userData));
    }

    public async UpdateUser(
        id: number,
        userData: Partial<UserEntity>,
    ): Promise<UserEntity | null> {
        await this.update(id, userData);
        return this.findOneBy({ id });
    }
}
