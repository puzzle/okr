create sequence if not exists sequence_action;

create table if not exists action
(
    id                   bigint       not null primary key,
    action varchar(4096) not null,
    priority int not null,
    is_checked boolean not null,

    key_result_id bigint           not null
    constraint fk_completed_key_result
    references key_result
);
