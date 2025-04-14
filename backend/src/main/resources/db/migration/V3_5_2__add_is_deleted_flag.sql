ALTER TABLE team
    add COLUMN is_deleted boolean not null default false;

ALTER TABLE objective
    add COLUMN is_deleted boolean not null default false;

ALTER TABLE key_result
    add COLUMN is_deleted boolean not null default false;


ALTER TABLE check_in
    add COLUMN is_deleted boolean not null default false;


ALTER TABLE action
    add COLUMN is_deleted boolean not null default false;

ALTER TABLE unit
    add COLUMN is_deleted boolean not null default false;

ALTER TABLE person
    add COLUMN is_deleted boolean not null default false;