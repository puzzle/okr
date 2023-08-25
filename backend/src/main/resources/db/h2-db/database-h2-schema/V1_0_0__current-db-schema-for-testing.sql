create sequence if not exists sequence_key_result;
create sequence if not exists sequence_measure;
create sequence if not exists sequence_objective;
create sequence if not exists sequence_person;
create sequence if not exists sequence_quarter;
create sequence if not exists sequence_team;
create table if not exists flyway_schema_history
(
    installed_rank integer not null,
    version varchar(50),
    description varchar(200) not null,
    type varchar(20) not null,
    script varchar(1000) not null,
    checksum integer,
    installed_by varchar(100) not null,
    installed_on timestamp default now() not null,
    execution_time integer not null,
    success boolean not null,
    constraint flyway_schema_history_pk
        primary key (installed_rank)
);


create index if not exists flyway_schema_history_s_idx
    on flyway_schema_history (success);

create table if not exists person
(
    id bigint not null,
    email varchar(250) not null,
    firstname varchar(50) not null,
    lastname varchar(50) not null,
    username varchar(20) not null,
    primary key (id),
    constraint uk_n0i6d7rc1hqkjivk494g8j2qd
        unique (username)
);


create table if not exists quarter
(
    id bigint not null,
    label varchar(255) not null,
    primary key (id),
    constraint uk_dgtrbsqpu1pdfxob0kkw6y44a
        unique (label)
);


create table if not exists team
(
    id bigint not null,
    name varchar(250) not null,
    primary key (id)
);


create table if not exists objective
(
    id bigint not null,
    description varchar(4096),
    modified_on timestamp not null,
    progress bigint,
    title varchar(250) not null,
    created_by_id bigint not null,
    quarter_id bigint not null,
    team_id bigint not null,
    state text not null,
    modified_by_id bigint,
    created_on timestamp not null,
    primary key (id),
    constraint fkei78u3nle5h56t0duejtav8t5
        foreign key (created_by_id) references person,
    constraint fkh6d9qidc5dtgvdv7y3muxoabd
        foreign key (quarter_id) references quarter,
    constraint fk8h2m4kk8wt96ran9rgxn9n3to
        foreign key (team_id) references team
);


create index if not exists idx_objective_title
    on objective (title);

create table if not exists measure
(
    id bigint not null,
    change_info varchar(255) not null,
    created_on timestamp not null,
    initiatives varchar(4096),
    measure_date timestamp not null,
    "value" double precision not null,
    created_by_id bigint not null,
    key_result_id bigint not null,
    primary key (id)
);


create table if not exists key_result
(
    id bigint not null,
    basis_value double precision,
    description varchar(4096),
    expected_evolution integer not null,
    modified_on timestamp not null,
    target_value double precision not null,
    title varchar(250),
    unit integer not null,
    created_by_id bigint not null,
    objective_id bigint not null,
    owner_id bigint not null,
    primary key (id),
    constraint fk4ba6rgbr8mrkc8vvyqd5il4v9
        foreign key (created_by_id) references person,
    constraint fkrvcqyntd3p3kj8i7n0kuwbmqk
        foreign key (objective_id) references objective,
    constraint fkrk74v7vu0tugx9tbpeiotgw9b
        foreign key (owner_id) references person
);

