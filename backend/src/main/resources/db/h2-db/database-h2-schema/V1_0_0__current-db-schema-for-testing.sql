create sequence if not exists sequence_key_result;
create sequence if not exists sequence_check_in;
create sequence if not exists sequence_objective;
create sequence if not exists sequence_person;
create sequence if not exists sequence_quarter;
create sequence if not exists sequence_team;
create sequence if not exists sequence_alignment;
create sequence if not exists sequence_completed;
create sequence if not exists sequence_organisation;
create sequence if not exists sequence_action;
CREATE SEQUENCE if not exists sequence_unit;



CREATE  TABLE unit
(
    id            BIGINT                NOT NULL,
    version       INTEGER               NOT NULL,
    unit_name      TEXT                  NOT NULL,
    created_by_id     BIGINT,
    is_default     boolean               NOT NULL,
    is_deleted     boolean  default false NOT NULL,

    PRIMARY KEY (id)
);

create table if not exists person
(
    id        bigint       not null,
    version   int          not null,
    email     varchar(250) not null,
    first_name varchar(50)  not null,
    last_name  varchar(50)  not null,
    okr_champion BOOLEAN DEFAULT FALSE,
    is_deleted     boolean  default false NOT NULL,
    primary key (id),
    constraint uk_person_email
        unique (email)
);


create table if not exists quarter
(
    id         bigint       not null,
    label      varchar(255) not null,
    start_date date,
    end_date   date,
    primary key (id),
    constraint uk_quarter_label
        unique (label)
);


create table if not exists team
(
    id      bigint       not null,
    version int          not null,
    name    varchar(250) not null,
    is_deleted     boolean  default false NOT NULL,
    primary key (id)
);


create table if not exists objective
(
    id             bigint       not null,
    version        int          not null,
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
    is_deleted     boolean  default false NOT NULL,
    primary key (id),
    constraint fk_objective_created_by_person
        foreign key (created_by_id) references person,
    constraint fk_objective_quarter
        foreign key (quarter_id) references quarter,
    constraint fk_objective_team
        foreign key (team_id) references team
);

create index if not exists idx_objective_created_by_person
    on objective (created_by_id);
create index if not exists idx_objective_quarter
    on objective (quarter_id);
create index if not exists idx_objective_team
    on objective (team_id);
create index if not exists idx_objective_title
    on objective (title);

create table if not exists key_result
(
    id              bigint    not null,
    version         int       not null,
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
    unit_id         bigint,
    commit_zone     varchar(1024),
    target_zone     varchar(1024),
    stretch_zone    varchar(1024),
    is_deleted     boolean  default false NOT NULL,
    primary key (id),
    constraint fk4ba6rgbr8mrkc8vvyqd5il4v9
        foreign key (created_by_id) references person,
    constraint fkrvcqyntd3p3kj8i7n0kuwbmqk
        foreign key (objective_id) references objective,
    constraint fkrk74v7vu0tugx9tbpeiotgw9b
        foreign key (owner_id) references person,
    constraint fk_key_result_unit
        foreign key (unit_id) references unit
);

create index if not exists idx_key_result_owner_person
    on key_result (owner_id);
create index if not exists idx_key_result_created_by_person
    on key_result (created_by_id);
create index if not exists idx_key_result_objective
    on key_result (objective_id);

create table if not exists check_in
(
    id            bigint    not null,
    version       int       not null,
    change_info   varchar(4096),
    created_on    timestamp not null,
    initiatives   varchar(4096),
    modified_on   timestamp,
    value_metric  double precision,
    created_by_id bigint    not null,
    key_result_id bigint    not null,
    confidence    integer,
    check_in_type varchar(255),
    zone          text,
    is_deleted     boolean  default false NOT NULL,
    primary key (id),
    constraint fk_check_in_key_result
        foreign key (key_result_id) references key_result
);

create index if not exists idx_check_in_key_result
    on check_in (key_result_id);

create table if not exists completed
(
    id           bigint not null primary key,
    version      int    not null,
    objective_id bigint not null
        constraint fk_completed_objective
            references objective,
    comment      varchar(4096)
);

create table action
(
    id            bigint        not null
        primary key,
    version       int           not null,
    action_point        varchar(4096) not null,
    priority      integer       not null,
    checked    boolean       not null,
    is_deleted     boolean  default false NOT NULL,
    key_result_id bigint        not null
        constraint fk_completed_key_result
            references key_result
);

create index if not exists idx_completed_objective
    on completed (objective_id);

DROP VIEW IF EXISTS OVERVIEW;
CREATE VIEW OVERVIEW AS
SELECT tq.team_id          AS "team_id",
       tq.team_version     AS "team_version",
       tq.name             AS "team_name",
       tq.quater_id        AS "quarter_id",
       tq.label            AS "quarter_label",
       coalesce(o.id, -1)  AS "objective_id",
       o.title             AS "objective_title",
       o.state             AS "objective_state",
       o.created_on        AS "objective_created_on",
       coalesce(kr.id, -1) AS "key_result_id",
       kr.title            AS "key_result_title",
       kr.key_result_type  AS "key_result_type",
       U.unit_name as "unit",
       kr.baseline,
       kr.stretch_goal,
       kr.commit_zone,
       kr.target_zone,
       kr.stretch_zone,
       coalesce(c.id, -1)  AS "check_in_id",
       c.value_metric      AS "check_in_value",
       c.zone              AS "check_in_zone",
       c.confidence,
       c.created_on        AS "check_in_created_on"
FROM (select t.id as team_id, t.version as team_version, t.name, q.id as quater_id, q.label
      from team t,
           quarter q) tq
         LEFT JOIN OBJECTIVE O ON TQ.TEAM_ID = O.TEAM_ID AND TQ.QUATER_ID = O.QUARTER_ID
         LEFT JOIN KEY_RESULT KR ON O.ID = KR.OBJECTIVE_ID
         LEFT JOIN unit U ON U.ID = KR.unit_id
         LEFT JOIN CHECK_IN C ON KR.ID = C.KEY_RESULT_ID AND C.MODIFIED_ON = (SELECT MAX(CC.MODIFIED_ON)
                                                                              FROM CHECK_IN CC
                                                                              WHERE CC.KEY_RESULT_ID = C.KEY_RESULT_ID AND  CC.is_deleted = false)
WHERE KR.is_deleted = false;


create table if not exists alignment
(
    id                   bigint       not null,
    version              int          not null,
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

DROP VIEW IF EXISTS ALIGNMENT_SELECTION;
CREATE VIEW ALIGNMENT_SELECTION AS
SELECT O.ID                AS "OBJECTIVE_ID",
       O.TITLE             AS "OBJECTIVE_TITLE",
       T.ID                AS "TEAM_ID",
       T.NAME              AS "TEAM_NAME",
       Q.ID                AS "QUARTER_ID",
       Q.LABEL             AS "QUARTER_LABEL",
       COALESCE(KR.ID, -1) AS "KEY_RESULT_ID",
       KR.TITLE            AS "KEY_RESULT_TITLE"
FROM OBJECTIVE O
         LEFT JOIN TEAM T ON O.TEAM_ID = T.ID
         LEFT JOIN QUARTER Q ON O.QUARTER_ID = Q.ID
         LEFT JOIN KEY_RESULT KR ON O.ID = KR.OBJECTIVE_ID;

create table if not exists organisation
(
    id       bigint       not null,
    version  int          not null,
    org_name varchar(255) not null,
    state    text         not null,
    primary key (id)
);

create table if not exists team_organisation
(
    team_id         bigint not null,
    organisation_id bigint not null,
    primary key (organisation_id, team_id),
    constraint fk_team_organisation_organisation
        foreign key (organisation_id) references organisation,
    constraint fk_team_organisation_team
        foreign key (team_id) references team
);

CREATE SEQUENCE IF NOT EXISTS sequence_team;

CREATE TABLE person_team
(
    team_admin BOOLEAN DEFAULT FALSE NOT NULL,
    version       INTEGER               NOT NULL,
    id            BIGINT                NOT NULL,
    person_id     BIGINT,
    team_id       BIGINT,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS person_team
    ADD CONSTRAINT FK_person_team_team FOREIGN KEY (team_id) REFERENCES team;
ALTER TABLE IF EXISTS person_team
    ADD CONSTRAINT FK_person_team_person FOREIGN KEY (person_id) REFERENCES person;

CREATE SEQUENCE IF NOT EXISTS sequence_person_team;
