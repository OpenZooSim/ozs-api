-- ############################
-- OpenZooSim Seed Data
--
-- https://openzoosim.com
--
-- Licensed Under GNU GPLv3.
-- Copyright 2024. OpenZooSim. All Rights Reserved.
-- 
-- This will create some initial data in the DB
-- such as an initial admin user (you must reset the password),
-- types, and other data that are necessary for the application
-- to function correctly. All other Seed data should be handled by the application.
-- ############################

-- // USER TYPES
INSERT INTO public.user_types (name, key)
VALUES ('Admin', 'ut_admin')
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.user_types (name, key)
VALUES ('Mod', 'ut_mod')
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.user_types (name, key)
VALUES ('Player', 'ut_player')
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.user_types (name, key)
VALUES ('System', 'ut_system')
ON CONFLICT (id) DO NOTHING;

-- // USERS
INSERT INTO public.users (email, username, user_type_id)
VALUES ('snowlynxsoftware@gmail.com', 'MorpheusZero', 1)
ON CONFLICT (id) DO NOTHING;

-- // SHARDS
INSERT INTO public.shards (name, description)
VALUES ('Hydrogen', 'The main persistent shard that all players start on')
ON CONFLICT (id) DO NOTHING;