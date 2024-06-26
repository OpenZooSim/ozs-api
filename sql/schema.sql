-- ############################
-- OpenZooSim Official Schema
--
-- https://openzoosim.com
--
-- Licensed Under GNU GPLv3.
-- Copyright 2024. OpenZooSim. All Rights Reserved.
-- 
-- This will allow you to create a new (empty) 
-- database from scratch to get started 
-- with OpenZooSim. This creates the most current
-- version of the schema (without any migrations).
-- ############################

CREATE TABLE IF NOT EXISTS "banned_ips" (
  "id" INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "created_at" timestamp DEFAULT (now()),
  "modified_at" timestamp,
  "is_archived" boolean DEFAULT false,
  "ip" text UNIQUE NOT NULL,
  "description" text
);

CREATE TABLE IF NOT EXISTS "user_types" (
  "id" INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "created_at" timestamp DEFAULT (now()),
  "modified_at" timestamp,
  "is_archived" boolean DEFAULT false,
  "name" text NOT NULL,
  "key" text UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS "users" (
  "id" INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "created_at" timestamp DEFAULT (now()),
  "modified_at" timestamp,
  "is_archived" bool DEFAULT false,
  "email" text UNIQUE NOT NULL,
  "username" text NOT NULL,
  "is_verified" boolean DEFAULT false,
  "user_type_id" integer,
  "password_hash" text,
  "last_login" timestamp,
  "failed_login_attempts" integer DEFAULT 0,
  "is_banned" boolean DEFAULT false,
  "ban_reason" text
);

CREATE TABLE IF NOT EXISTS "user_login_history" (
  "id" INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "created_at" timestamp DEFAULT (now()),
  "modified_at" timestamp,
  "is_archived" boolean DEFAULT false,
  "user_id" integer
);

CREATE TABLE IF NOT EXISTS "user_sessions" (
  "id" INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "created_at" timestamp DEFAULT (now()),
  "modified_at" timestamp,
  "is_archived" bool DEFAULT false,
  "user_id" INTEGER,
  "ip" text NOT NULL,
  "device_thumbprint" text NOT NULL,
  "session_token" text NOT NULL,
  "expires_at" timestamp
);

CREATE TABLE IF NOT EXISTS "user_password_resets" (
  "id" INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "created_at" timestamp DEFAULT (now()),
  "modified_at" timestamp,
  "is_archived" boolean DEFAULT false,
  "user_id" INTEGER,
  "token" text UNIQUE NOT NULL,
  "expires_at" timestamp
);

CREATE TABLE IF NOT EXISTS "shards" (
  "id" INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "created_at" timestamp DEFAULT (now()),
  "modified_at" timestamp,
  "is_archived" bool DEFAULT false,
  "name" text NOT NULL,
  "description" text NOT NULL
);

ALTER TABLE "users" ADD FOREIGN KEY ("user_type_id") REFERENCES "user_types" ("id");

ALTER TABLE "user_login_history" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "user_sessions" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "user_password_resets" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");
