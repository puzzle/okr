-- flyway user for pitc
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
create user pitc_fly with encrypted password 'pwd';
create schema if not exists okr_pitc;
grant create on schema okr_pitc to pitc_fly; -- "create" permissions on schema okr_pitc
grant usage on schema okr_pitc to pitc_fly; -- "usage" permissions on schema okr_pitc

-- grants the highest possible level of access to all objects (tables, sequences) within the okr_pitc schema to the user account pitc_fly.
-- pitc_fly will have complete access to all objects (tables, sequences) within the okr_pitc schema and can perform any operation on those objects,
-- including:
-- Creating new tables
-- Modifying existing tables (e.g., adding or dropping columns)
-- Dropping tables
-- Executing queries against tables
grant all privileges on all tables in schema okr_pitc to pitc_fly;
grant all privileges on all sequences in schema okr_pitc to pitc_fly;


-- flyway user for acme (has access (read, write, truncate etc.) to schema okr_acme)
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
create user acme_fly with encrypted password 'pwd';
create schema if not exists okr_acme;
grant create on schema okr_acme to acme_fly;
grant usage on schema okr_acme to acme_fly;
grant all privileges on all tables in schema okr_acme to acme_fly;
grant all privileges on all sequences in schema okr_acme to acme_fly;


-- app user for no specific tenant (has no access to okr_pitc/okr_acme schemas)
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
create user bootstrap_app with encrypted password 'pwd';
grant usage on schema public to bootstrap_app;


-- pitc application user
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
create user pitc_app with encrypted password 'pwd';
grant usage on schema okr_pitc to pitc_app;

-- grant select/insert/update/delete to user pitc_app in schema okr_pitc
alter default privileges for user pitc_fly in schema okr_pitc
    grant select, insert, update, delete on tables to pitc_app;

alter default privileges for user pitc_fly in schema okr_pitc
    grant usage, select on sequences to pitc_app;


-- acme application user
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
create user acme_app with encrypted password 'pwd';
grant usage on schema okr_acme to acme_app;
alter default privileges for user acme_fly in schema okr_acme
    grant select, insert, update, delete on tables to acme_app;

alter default privileges for user acme_fly in schema okr_acme
    grant usage, select on sequences to acme_app;
