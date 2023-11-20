create sequence if not exists sequence_action;
ALTER SEQUENCE sequence_action RESTART WITH 1000;
create table if not exists action
(
    id            bigint        not null primary key,
    version       int           not null,
    action        varchar(4096) not null,
    priority      int           not null,
    is_checked    boolean       not null,

    key_result_id bigint        not null
        constraint fk_action_key_result
            references key_result
);

create index if not exists idx_action_key_result
    on action (key_result_id);
