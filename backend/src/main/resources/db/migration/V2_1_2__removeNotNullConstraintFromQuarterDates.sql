alter table quarter
    alter column start_date drop not null,
    alter column end_date drop not null;

INSERT INTO quarter (id, label, start_date, end_date)
VALUES (999, 'Backlog', null, null);