create sequence if not exists sequence_completed ;
alter sequence sequence_completed restart with 1000;

create table if not exists completed
(
    id           bigint not null primary key,
    objective_id bigint not null
        constraint fk_completed_objective
            references objective,
    comment      varchar(4096)
);

create index if not exists idx_completed_objective
    on completed (objective_id);
