create sequence if not exists sequence_key_result;
create sequence if not exists sequence_check_in;
create sequence if not exists sequence_objective;
create sequence if not exists sequence_person;
create sequence if not exists sequence_quarter;
create sequence if not exists sequence_team;
create sequence if not exists sequence_alignment;

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
SELECT TQ.TEAM_ID          AS "TEAM_ID",
       TQ.NAME             AS "TEAM_NAME",
       TQ.QUATER_ID        AS "QUARTER_ID",
       TQ.LABEL            AS "QUARTER_LABEL",
       COALESCE(O.ID, -1)  AS "OBJECTIVE_ID",
       O.TITLE             AS "OBJECTIVE_TITLE",
       O.STATE             AS "OBJECTIVE_STATE",
       COALESCE(KR.ID, -1) AS "KEY_RESULT_ID",
       KR.TITLE            AS "KEY_RESULT_TITLE",
       KR.KEY_RESULT_TYPE  AS "KEY_RESULT_TYPE",
       KR.UNIT,
       KR.BASELINE,
       KR.STRETCH_GOAL,
       KR.COMMIT_ZONE,
       KR.TARGET_ZONE,
       KR.STRETCH_ZONE,
       COALESCE(C.ID, -1)  AS "CHECK_IN_ID",
       C.VALUE_METRIC      AS "CHECK_IN_VALUE",
       C.ZONE              AS "CHECK_IN_ZONE",
       C.CONFIDENCE,
       C.CREATED_ON
FROM (SELECT T.ID AS TEAM_ID, T.NAME, Q.ID AS QUATER_ID, Q.LABEL
      FROM TEAM T,
           QUARTER Q) TQ
         LEFT JOIN OBJECTIVE O ON TQ.TEAM_ID = O.TEAM_ID AND TQ.QUATER_ID = O.QUARTER_ID
         LEFT JOIN KEY_RESULT KR ON O.ID = KR.OBJECTIVE_ID
         LEFT JOIN CHECK_IN C ON KR.ID = C.KEY_RESULT_ID AND C.MODIFIED_ON = (SELECT MAX(CC.MODIFIED_ON)
                                                                              FROM CHECK_IN CC
                                                                              WHERE CC.KEY_RESULT_ID = C.KEY_RESULT_ID);
create table if not exists alignment
(
    id                   bigint       not null,
    aligned_objective_id bigint       not null,
    alignment_type       varchar(255) not null,
    target_key_result_id bigint,
    target_objective_id  bigint,
    primary key (id),
    constraint fk_alignment_aligned_objective
        foreign key (aligned_objective_id) references objective,
    constraint fk_alignment_target_key_result
        foreign key (target_key_result_id) references key_result,
    constraint fk_alignment_target_objective
        foreign key (target_objective_id) references objective
);