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
}
