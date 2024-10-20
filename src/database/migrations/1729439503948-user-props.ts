import { MigrationInterface, QueryRunner } from "typeorm";

export class UserProps1729439503948 implements MigrationInterface {
    name = 'UserProps1729439503948'

    public async up(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`ALTER TABLE "users" ADD "passwordHash" character varying NOT NULL`);
        await queryRunner.query(`ALTER TABLE "users" ADD "emailVerified" boolean NOT NULL DEFAULT false`);
        await queryRunner.query(`ALTER TABLE "users" ADD "avatarURL" character varying NOT NULL DEFAULT ''`);
        await queryRunner.query(`ALTER TABLE "users" ADD "lastLoginDate" TIMESTAMP`);
        await queryRunner.query(`ALTER TABLE "users" ADD "allowMarketingEmails" boolean NOT NULL DEFAULT true`);
    }

    public async down(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`ALTER TABLE "users" DROP COLUMN "allowMarketingEmails"`);
        await queryRunner.query(`ALTER TABLE "users" DROP COLUMN "lastLoginDate"`);
        await queryRunner.query(`ALTER TABLE "users" DROP COLUMN "avatarURL"`);
        await queryRunner.query(`ALTER TABLE "users" DROP COLUMN "emailVerified"`);
        await queryRunner.query(`ALTER TABLE "users" DROP COLUMN "passwordHash"`);
    }

}
