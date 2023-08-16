alter table quarter
    add column if not exists start_date timestamp,
    add column if not exists end_date timestamp;

-- TODO: Add start and end date to all quarters where start_date && end_date == null

alter table quarter
    ALTER column start_date SET NOT NULL,
    ALTER column end_date SET NOT NULL;