alter table key_result
drop column if exists expectedEvolution,
    drop column if exists unit,
    drop column if exists basicValue,
    drop column if exists targetValue,
    add column if not exists created_on timestamp,
    add column if not exists baseline float,
    add column if not exists stretchGoal float,
    add column if not exists unit varchar(30),
    add column if not exists commitZone varchar(1024),
    add column if not exists targetZone varchar(1024),
    add column if not exists stretchGoal varchar(1024);

DO $$
DECLARE
r record;
BEGIN
FOR r IN SELECT * FROM key_result
         WHERE key_result.created_on IS NULL
             LOOP
UPDATE key_result k
SET created_on = k.objective.createdOn
WHERE k.id = r.id;

END LOOP;
END$$;

alter table key_result
    ALTER column created_on SET NOT NULL;
