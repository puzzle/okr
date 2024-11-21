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

create table if not exists person
(
    id        bigint       not null,
    version   int          not null,
    email     varchar(250) not null,
    firstname varchar(50)  not null,
    lastname  varchar(50)  not null,
    is_okr_champion BOOLEAN DEFAULT FALSE,
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
    action        varchar(4096) not null,
    priority      integer       not null,
    is_checked    boolean       not null,
    key_result_id bigint        not null
        constraint fk_completed_key_result
            references key_result
);

create index if not exists idx_completed_objective
    on completed (objective_id);

DROP VIEW IF EXISTS OVERVIEW;
CREATE VIEW OVERVIEW AS
SELECT TQ.TEAM_ID          AS "TEAM_ID",
       TQ.TEAM_VERSION     AS "TEAM_VERSION",
       TQ.NAME             AS "TEAM_NAME",
       TQ.QUARTER_ID       AS "QUARTER_ID",
       TQ.LABEL            AS "QUARTER_LABEL",
       COALESCE(O.ID, -1)  AS "OBJECTIVE_ID",
       O.TITLE             AS "OBJECTIVE_TITLE",
       O.STATE             AS "OBJECTIVE_STATE",
       O.CREATED_ON        AS "OBJECTIVE_CREATED_ON",
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
       C.CREATED_ON        AS "CHECK_IN_CREATED_ON"
FROM (SELECT T.ID AS TEAM_ID, T.VERSION AS TEAM_VERSION, T.NAME, Q.ID AS QUARTER_ID, Q.LABEL
      FROM TEAM T,
           QUARTER Q) TQ
         LEFT JOIN OBJECTIVE O ON TQ.TEAM_ID = O.TEAM_ID AND TQ.QUARTER_ID = O.QUARTER_ID
         LEFT JOIN KEY_RESULT KR ON O.ID = KR.OBJECTIVE_ID
         LEFT JOIN CHECK_IN C ON KR.ID = C.KEY_RESULT_ID AND C.MODIFIED_ON = (SELECT MAX(CC.MODIFIED_ON)
                                                                              FROM CHECK_IN CC
                                                                              WHERE CC.KEY_RESULT_ID = C.KEY_RESULT_ID);
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
    is_team_admin BOOLEAN DEFAULT FALSE NOT NULL,
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

DROP VIEW IF EXISTS ALIGNMENT_VIEW;
CREATE VIEW ALIGNMENT_VIEW AS
SELECT
    CONCAT(OA.ID, COALESCE(A.TARGET_OBJECTIVE_ID, A.TARGET_KEY_RESULT_ID),'S','objective',A.ALIGNMENT_TYPE) AS UNIQUE_ID,
    OA.ID AS ID,
    OA.TITLE AS TITLE,
    OTT.ID AS TEAM_ID,
    OTT.NAME AS TEAM_NAME,
    OA.QUARTER_ID AS QUARTER_ID,
    OA.STATE AS STATE,
    'objective' AS OBJECT_TYPE,
    'source' AS CONNECTION_ITEM,
    COALESCE(A.TARGET_OBJECTIVE_ID, A.TARGET_KEY_RESULT_ID) AS REF_ID,
    A.ALIGNMENT_TYPE AS REF_TYPE
FROM ALIGNMENT A
         LEFT JOIN OBJECTIVE OA ON OA.ID = A.ALIGNED_OBJECTIVE_ID
         LEFT JOIN TEAM OTT ON OTT.ID = OA.TEAM_ID
UNION
SELECT
    CONCAT(OT.ID, A.ALIGNED_OBJECTIVE_ID,'T','objective','objective') AS UNIQUE_ID,
    OT.ID AS ID,
    OT.TITLE AS TITLE,
    OTT.ID AS TEAM_ID,
    OTT.NAME AS TEAM_NAME,
    OT.QUARTER_ID AS QUARTER_ID,
    OT.STATE AS STATE,
    'objective' AS OBJECT_TYPE,
    'target' AS CONNECTION_ITEM,
    A.ALIGNED_OBJECTIVE_ID AS REF_ID,
    'objective' AS REF_TYPE
FROM ALIGNMENT A
         LEFT JOIN OBJECTIVE OT ON OT.ID = A.TARGET_OBJECTIVE_ID
         LEFT JOIN TEAM OTT ON OTT.ID = OT.TEAM_ID
WHERE ALIGNMENT_TYPE = 'objective'
UNION
SELECT
    CONCAT(KRT.ID, A.ALIGNED_OBJECTIVE_ID,'T','keyResult','keyResult') AS UNIQUE_ID,
    KRT.ID AS ID,
    KRT.TITLE AS TITLE,
    OTT.ID AS TEAM_ID,
    OTT.NAME AS TEAM_NAME,
    O.QUARTER_ID AS QUARTER_ID,
    NULL AS STATE,
    'keyResult' AS OBJECT_TYPE,
    'target' AS CONNECTION_ITEM,
    A.ALIGNED_OBJECTIVE_ID AS REF_ID,
    'objective' AS REF_TYPE
FROM ALIGNMENT A
         LEFT JOIN KEY_RESULT KRT ON KRT.ID = A.TARGET_KEY_RESULT_ID
         LEFT JOIN OBJECTIVE O ON O.ID = KRT.OBJECTIVE_ID
         LEFT JOIN TEAM OTT ON OTT.ID = O.TEAM_ID
WHERE ALIGNMENT_TYPE = 'keyResult';