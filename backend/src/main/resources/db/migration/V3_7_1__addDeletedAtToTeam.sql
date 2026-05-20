ALTER TABLE team
ADD COLUMN status text default 'ACTIVE',
ADD COLUMN marked_as_archived_at timestamp;
