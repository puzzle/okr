-- table check_in
alter table check_in
    drop constraint if exists measure_pkey;

alter table check_in
    add constraint check_in_pkey
        primary key (id);

alter table check_in
    drop constraint if exists fk_check_in_key_result;

alter table check_in
    add constraint fk_check_in_key_result
        foreign key (key_result_id) references key_result;

create index if not exists idx_check_in_key_result
    on check_in (key_result_id);

-- table key_result
alter table key_result
    drop constraint if exists fk4ba6rgbr8mrkc8vvyqd5il4v9;
alter table key_result
    drop constraint if exists fkrvcqyntd3p3kj8i7n0kuwbmqk;
alter table key_result
    drop constraint if exists fkrk74v7vu0tugx9tbpeiotgw9b;
alter table key_result
    drop constraint if exists fk_key_result_owner_person;
alter table key_result
    drop constraint if exists fk_key_result_created_by_person;
alter table key_result
    drop constraint if exists fk_key_result_objective;

alter table key_result
    add constraint fk_key_result_owner_person
        foreign key (owner_id) references person;
alter table key_result
    add constraint fk_key_result_created_by_person
        foreign key (created_by_id) references person;
alter table key_result
    add constraint fk_key_result_objective
        foreign key (objective_id) references objective;

create index if not exists idx_key_result_owner_person
    on key_result (owner_id);
create index if not exists idx_key_result_created_by_person
    on key_result (created_by_id);
create index if not exists idx_key_result_objective
    on key_result (objective_id);

-- table objective
alter table objective
    drop constraint if exists fkei78u3nle5h56t0duejtav8t5;
alter table objective
    drop constraint if exists fkh6d9qidc5dtgvdv7y3muxoabd;
alter table objective
    drop constraint if exists fk8h2m4kk8wt96ran9rgxn9n3to;
alter table objective
    drop constraint if exists fk_objective_created_by_person;
alter table objective
    drop constraint if exists fk_objective_quarter;
alter table objective
    drop constraint if exists fk_objective_team;

alter table objective
    add constraint fk_objective_created_by_person
        foreign key (created_by_id) references person;
alter table objective
    add constraint fk_objective_quarter
        foreign key (quarter_id) references quarter;
alter table objective
    add constraint fk_objective_team
        foreign key (team_id) references team;

create index if not exists idx_objective_created_by_person
    on objective (created_by_id);
create index if not exists idx_objective_quarter
    on objective (quarter_id);
create index if not exists idx_objective_team
    on objective (team_id);

-- table person
alter table person
    drop constraint if exists uk_n0i6d7rc1hqkjivk494g8j2qd;
alter table person
    drop constraint if exists uk_person_username;

alter table person
    add constraint uk_person_username
        unique (username);

create unique index if not exists idx_person_username
    on person (username);

-- table quarter
alter table quarter
    drop constraint if exists uk_dgtrbsqpu1pdfxob0kkw6y44a;
alter table quarter
    drop constraint if exists uk_quarter_label;

alter table quarter
    add constraint uk_quarter_label
        unique (label);

create unique index if not exists idx_quarter_label
    on quarter (label);
