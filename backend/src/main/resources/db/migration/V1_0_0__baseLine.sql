create sequence if not exists sequence_key_result;

create sequence if not exists sequence_measure;

create sequence if not exists sequence_objective;

create sequence if not exists sequence_person;

create sequence if not exists sequence_quarter;

create sequence if not exists sequence_team;

create table if not exists person
(
    id        bigint       not null
        primary key,
    email     varchar(250) not null,
    firstname varchar(50)  not null,
    lastname  varchar(50)  not null,
    username  varchar(20)  not null
        constraint uk_n0i6d7rc1hqkjivk494g8j2qd
            unique
);

create table if not exists quarter
(
    id    bigint       not null
        primary key,
    label varchar(255) not null
        constraint uk_dgtrbsqpu1pdfxob0kkw6y44a
            unique
);

create table if not exists team
(
    id   bigint       not null
        primary key,
    name varchar(250) not null
);

create table if not exists objective
(
    id          bigint       not null
        primary key,
    description varchar(4096),
    modified_on timestamp    not null,
    progress    bigint,
    title       varchar(250) not null,
    owner_id    bigint       not null
        constraint fkei78u3nle5h56t0duejtav8t5
            references person,
    quarter_id  bigint       not null
        constraint fkh6d9qidc5dtgvdv7y3muxoabd
            references quarter,
    team_id     bigint       not null
        constraint fk8h2m4kk8wt96ran9rgxn9n3to
            references team
);

create table if not exists measure
(
    id            bigint           not null primary key,
    change_info   varchar(255)     not null,
    created_on    timestamp        not null,
    initiatives   varchar(4096),
    measure_date  timestamp        not null,
    "value"       double precision not null,
    created_by_id bigint           not null,
    key_result_id bigint           not null
);

create table if not exists key_result
(
    id                 bigint           not null
        primary key,
    basis_value        double precision,
    description        varchar(4096),
    expected_evolution integer          not null,
    modified_on        timestamp        not null,
    target_value       double precision not null,
    title              varchar(250),
    unit               integer          not null,
    created_by_id      bigint           not null
        constraint fk4ba6rgbr8mrkc8vvyqd5il4v9
            references person,
    objective_id       bigint           not null
        constraint fkrvcqyntd3p3kj8i7n0kuwbmqk
            references objective,
    owner_id           bigint           not null
        constraint fkrk74v7vu0tugx9tbpeiotgw9b
            references person
);

create table if not exists measure
(
    id            bigint           not null
        primary key,
    change_info   varchar(255)     not null,
    created_on    timestamp        not null,
    initiatives   varchar(4096),
    measure_date  timestamp        not null,
    "value"       double precision not null,
    created_by_id bigint           not null
        constraint fk1ceh0ql85vexss212h0jlrctd
            references person,
    key_result_id bigint           not null
        constraint fkoiu1l4cagu6bsdwyfy90v4tvn
            references key_result
);

create index if not exists idx_objective_title
    on objective (title);

ALTER SEQUENCE sequence_team RESTART WITH 500;
ALTER SEQUENCE sequence_person RESTART WITH 500;
ALTER SEQUENCE sequence_quarter RESTART WITH 500;
ALTER SEQUENCE sequence_objective RESTART WITH 500;
ALTER SEQUENCE sequence_key_result RESTART WITH 500;
ALTER SEQUENCE sequence_measure RESTART WITH 500;
