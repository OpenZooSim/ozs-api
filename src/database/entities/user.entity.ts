import { Column, Entity } from "typeorm";
import { BaseEntity } from "./base.entity";

@Entity({
    name: "users",
})
export class UserEntity extends BaseEntity {
    @Column({
        unique: true,
    })
    email!: string;

    @Column()
    displayName!: string;

    @Column()
    passwordHash!: string;

    @Column({
        default: false,
    })
    emailVerified!: boolean;

    @Column({
        default: "",
    })
    avatarURL!: string;

    @Column({
        nullable: true,
        default: null,
    })
    lastLoginDate!: Date;

    @Column({
        default: true,
    })
    allowMarketingEmails!: boolean;
}
