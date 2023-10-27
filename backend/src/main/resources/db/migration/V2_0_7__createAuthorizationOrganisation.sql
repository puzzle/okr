create sequence if not exists sequence_organisation;

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
