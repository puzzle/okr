create sequence if not exists sequence_organisation INCREMENT 50;
Alter SEQUENCE sequence_organisation RESTART WITH 1000;

create table if not exists organisation
(
    id       bigint       not null,
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

create index if not exists idx_team_organisation_team
    on team_organisation (team_id);

DO
$$
    BEGIN
        IF NOT EXISTS(SELECT * FROM organisation)
        THEN
            insert into organisation (id, org_name, state)
            values (1, 'org_bl', 'ACTIVE'),
                   (2, 'org_de', 'ACTIVE'),
                   (3, 'org_gl', 'ACTIVE'),
                   (4, 'org_pl', 'ACTIVE'),
                   (5, 'org_pv', 'ACTIVE'),
                   (6, 'org_ux', 'ACTIVE'),
                   (7, 'org_zh', 'ACTIVE'),
                   (8, 'org_sys', 'ACTIVE'),
                   (9, 'org_azubi', 'ACTIVE'),
                   (10, 'org_de_gl', 'ACTIVE'),
                   (11, 'org_de_gs', 'ACTIVE'),
                   (12, 'org_devtre', 'ACTIVE'),
                   (13, 'org_racoon', 'ACTIVE'),
                   (14, 'org_tqm_qm', 'ACTIVE'),
                   (15, 'org_devruby', 'ACTIVE'),
                   (16, 'org_midcicd', 'ACTIVE'),
                   (17, 'org_verkauf', 'ACTIVE'),
                   (18, 'org_finanzen', 'ACTIVE'),
                   (19, 'org_mobility', 'ACTIVE'),
                   (20, 'org_personal', 'ACTIVE'),
                   (21, 'org_security', 'ACTIVE'),
                   (22, 'org_marketing', 'ACTIVE'),
                   (23, 'org_openshift', 'ACTIVE'),
                   (24, 'org_ausbildung', 'ACTIVE'),
                   (25, 'org_backoffice', 'ACTIVE'),
                   (26, 'org_branch_sec', 'ACTIVE'),
                   (27, 'org_standort_zh', 'ACTIVE'),
                   (28, 'org_midcontainer', 'ACTIVE');

        END IF;
        IF NOT EXISTS(SELECT * FROM team_organisation)
        THEN
            insert into team_organisation (team_id, organisation_id)
            select t.id, 3
            from team t;
        END IF;
    END;
$$;
