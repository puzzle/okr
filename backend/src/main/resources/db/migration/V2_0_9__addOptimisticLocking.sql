alter table alignment
    add column if not exists version int;
alter table check_in
    add column if not exists version int;
alter table key_result
    add column if not exists version int;
alter table completed
    add column if not exists version int;
alter table objective
    add column if not exists version int;
alter table organisation
    add column if not exists version int;
alter table person
    add column if not exists version int;
alter table team
    add column if not exists version int;

update alignment
set version = 1
where version is null;
update check_in
set version = 1
where version is null;
update key_result
set version = 1
where version is null;
update completed
set version = 1
where version is null;
update objective
set version = 1
where version is null;
update organisation
set version = 1
where version is null;
update person
set version = 1
where version is null;
update team
set version = 1
where version is null;

alter table alignment
    alter column version set not null;
alter table check_in
    alter column version set not null;
alter table key_result
    alter column version set not null;
alter table completed
    alter column version set not null;
alter table objective
    alter column version set not null;
alter table organisation
    alter column version set not null;
alter table person
    alter column version set not null;
alter table team
    alter column version set not null;
