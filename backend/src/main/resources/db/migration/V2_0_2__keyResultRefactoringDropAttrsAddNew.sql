DO $$
    BEGIN
        IF EXISTS(SELECT *
                  FROM information_schema.columns
                  WHERE table_name='key_result' and column_name='basis_value')
        THEN
            ALTER TABLE "public"."key_result" RENAME COLUMN "basis_value" TO "baseline";
            ALTER TABLE "public"."key_result" RENAME COLUMN "target_value" TO "stretch_goal";
        END IF;
    END $$;

alter table key_result
    drop column if exists expectedEvolution,
    drop column if exists unit,
    drop column if exists expected_evolution,
    alter column stretch_goal drop not null,
    add column if not exists key_result_type varchar(255),
    add column if not exists created_on timestamp,
    add column if not exists unit varchar(30),
    add column if not exists commit_zone varchar(1024),
    add column if not exists target_zone varchar(1024),
    add column if not exists stretch_zone varchar(1024);

DO $$
DECLARE
r record;
BEGIN
FOR r IN SELECT * FROM key_result
         WHERE key_result.created_on IS NULL
             LOOP
UPDATE key_result k
SET created_on = k.modified_on
WHERE k.id = r.id;
UPDATE key_result k
    SET key_result_type = 'metric'
    where k.id = r.id;

END LOOP;
END$$;

alter table key_result
    ALTER column created_on SET NOT NULL;
