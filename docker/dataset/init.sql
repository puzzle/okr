-- pitc
create user pitc with encrypted password 'pwd';
create schema if not exists okr_pitc;
grant create on schema okr_pitc to pitc;
grant all privileges on all tables in schema okr_pitc to pitc;
grant all privileges on all sequences in schema okr_pitc to pitc;
grant usage on schema okr_pitc to pitc;

-- acme
create user acme with encrypted password 'pwd';
create schema if not exists okr_acme;
grant create on schema okr_acme to acme;
grant all privileges on all tables in schema okr_acme to acme;
grant all privileges on all sequences in schema okr_acme to acme;
grant usage on schema okr_acme to acme;