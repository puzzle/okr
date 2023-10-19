create sequence if not exists sequence_completed;

create table if not exists completed
(
    id                   bigint       not null primary key,
    objective_id bigint       not null
        constraint fk_completed_objective
            references objective,
    comment       varchar(4096)
);
