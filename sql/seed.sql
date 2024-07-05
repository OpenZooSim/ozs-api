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
-- to function correctly.
-- ############################

-- // USER TYPES
INSERT INTO public.user_types (id, name, key)
VALUES (1, 'Admin', 'ut_admin')
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.user_types (id, name, key)
VALUES (2, 'Mod', 'ut_mod')
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.user_types (id, name, key)
VALUES (3, 'Player', 'ut_player')
ON CONFLICT (id) DO NOTHING;

-- // USERS
INSERT INTO public.users (id, email, username, user_type_id)
VALUES (1, 'snowlynxsoftware@gmail.com', 'MorpheusZero', 1)
ON CONFLICT (id) DO NOTHING;

-- // SHARDS
INSERT INTO public.shards (id, name, description)
VALUES (1, 'Hydrogen', 'The main persistent shard that all players start on')
ON CONFLICT (id) DO NOTHING;