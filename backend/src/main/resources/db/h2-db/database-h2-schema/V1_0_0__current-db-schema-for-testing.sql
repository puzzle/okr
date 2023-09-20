create sequence if not exists sequence_key_result;
create sequence if not exists sequence_check_in;
create sequence if not exists sequence_objective;
create sequence if not exists sequence_person;
create sequence if not exists sequence_quarter;
create sequence if not exists sequence_team;
create table if not exists flyway_schema_history
(
    installed_rank integer                 not null,
    version        varchar(50),
    description    varchar(200)            not null,
    type           varchar(20)             not null,
    script         varchar(1000)           not null,
    checksum       integer,
    installed_by   varchar(100)            not null,
    installed_on   timestamp default now() not null,
    execution_time integer                 not null,
    success        boolean                 not null,
    constraint flyway_schema_history_pk
        primary key (installed_rank)
);


create index if not exists flyway_schema_history_s_idx
    on flyway_schema_history (success);

create table if not exists person
(
    id        bigint       not null,
    email     varchar(250) not null,
    firstname varchar(50)  not null,
    lastname  varchar(50)  not null,
    username  varchar(20)  not null,
    primary key (id),
    constraint uk_n0i6d7rc1hqkjivk494g8j2qd
        unique (username)
);


create table if not exists quarter
(
    id         bigint       not null,
    label      varchar(255) not null,
    start_date date         not null,
    end_date   date         not null,
    primary key (id),
    constraint uk_dgtrbsqpu1pdfxob0kkw6y44a
        unique (label)
);


create table if not exists team
(
    id   bigint       not null,
    name varchar(250) not null,
    primary key (id)
);


create table if not exists objective
(
    id             bigint       not null,
    description    varchar(4096),
    modified_on    timestamp    not null,
    progress       bigint,
    title          varchar(250) not null,
    created_by_id  bigint       not null,
    quarter_id     bigint       not null,
    team_id        bigint       not null,
    state          text         not null,
    modified_by_id bigint,
    created_on     timestamp    not null,
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

create table if not exists check_in
(
    id            bigint    not null
        constraint measure_pkey
            primary key,
    change_info   varchar(4096),
    created_on    timestamp not null,
    initiatives   varchar(4096),
    modified_on   timestamp,
    value_metric  double precision,
    created_by_id bigint    not null,
    key_result_id bigint    not null,
    confidence    integer,
    check_in_type varchar(255),
    zone          text
);

create table if not exists key_result
(
    id              bigint    not null,
    baseline        double precision,
    description     varchar(4096),
    modified_on     timestamp,
    stretch_goal    double precision,
    title           varchar(250),
    created_by_id   bigint    not null,
    objective_id    bigint    not null,
    owner_id        bigint    not null,
    key_result_type varchar(255),
    created_on      timestamp not null,
    unit            varchar(30),
    commit_zone     varchar(1024),
    target_zone     varchar(1024),
    stretch_zone    varchar(1024),

    primary key (id),
    constraint fk4ba6rgbr8mrkc8vvyqd5il4v9
        foreign key (created_by_id) references person,
    constraint fkrvcqyntd3p3kj8i7n0kuwbmqk
        foreign key (objective_id) references objective,
    constraint fkrk74v7vu0tugx9tbpeiotgw9b
        foreign key (owner_id) references person
);

DROP VIEW IF EXISTS OVERVIEW;
CREATE VIEW OVERVIEW AS
select t.id                as "team_id",
       t.name              as "team_name",
       coalesce(o.id, -1)  as "objective_id",
       o.title             as "objective_title",
       o.state             as "objective_state",
       q.id                as "quarter_id",
       q.label             as "quarter_label",
       coalesce(kr.id, -1) as "key_result_id",
       kr.title            as "key_result_title",
       kr.unit,
       kr.baseline,
       kr.stretch_goal,
       kr.commit_zone,
       kr.target_zone,
       kr.stretch_zone,
       coalesce(c.id, -1)  as "check_in_id",
       c.value_metric      as "check_in_value",
       c.zone              as "check_in_zone",
       c.confidence,
       c.created_on
FROM TEAM T
         LEFT JOIN OBJECTIVE O ON T.ID = O.TEAM_ID
         LEFT JOIN QUARTER Q ON O.QUARTER_ID = Q.ID
         LEFT JOIN KEY_RESULT KR ON O.ID = KR.OBJECTIVE_ID
         LEFT JOIN check_in C ON KR.ID = C.KEY_RESULT_ID AND C.modified_on = (SELECT MAX(CC.modified_on)
                                                                              FROM check_in CC
                                                                              WHERE CC.KEY_RESULT_ID = C.KEY_RESULT_ID)
;