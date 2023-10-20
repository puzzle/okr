create table if not exists organisation
(
    id bigint not null,
    org_name varchar(255) not null,
    primary key (id)
    );

create table if not exists team_organisation
(
    team_id bigint not null,
    organisation_id bigint not null,
    constraint fkkya7xh83whc05etnjbndf9rkf
        foreign key (organisation_id) references organisation,
    constraint fk5wag4scnx05cf3cyq2eqd0xba
        foreign key (team_id) references team
);

