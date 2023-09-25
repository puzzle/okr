create sequence if not exists sequence_alignment;

create table if not exists alignment
(
    id                   bigint       not null primary key,
    aligned_objective_id bigint       not null
        constraint fk_alignment_aligned_objective
            references objective,
    alignment_type       varchar(255) not null,
    target_key_result_id bigint
        constraint fk_alignment_target_key_result
            references key_result,
    target_objective_id  bigint
        constraint fk_alignment_target_objective
            references objective
);

create index if not exists idx_alignment_aligned_objective
    on alignment (aligned_objective_id);

create index if not exists idx_alignment_target_key_result
    on alignment (target_key_result_id);

create index if not exists idx_alignment_target_objective
    on alignment (target_objective_id);

alter table alignment
    owner to "user";
