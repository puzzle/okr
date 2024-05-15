DO
$$
    BEGIN
        IF EXISTS(SELECT *
                  FROM information_schema.columns
                  WHERE table_name = 'key_result'
                    and column_name = 'basis_value')
        THEN
            ALTER TABLE "key_result"
                RENAME COLUMN "basis_value" TO "baseline";
            ALTER TABLE "key_result"
                RENAME COLUMN "target_value" TO "stretch_goal";
            ALTER TABLE key_result RENAME unit to unit_old;
            Alter TABLE key_result ADD COLUMN if not exists unit varchar(30);

            UPDATE key_result
            SET unit = CASE
                           WHEN key_result.unit_old = 0 THEN 'PERCENT'
                           WHEN key_result.unit_old = 1 THEN 'CHF'
                           WHEN key_result.unit_old = 2 THEN 'NUMBER'
                           ELSE 'Unknown'
                END ;
        END IF;
    END
$$;

alter table key_result
    drop column if exists expectedEvolution,
    drop column if exists unit_old,
    drop column if exists expected_evolution,
    alter column stretch_goal drop not null,
    alter column modified_on drop not null,
    add column if not exists key_result_type varchar(255),
    add column if not exists created_on      timestamp,
    add column if not exists commit_zone     varchar(1024),
    add column if not exists target_zone     varchar(1024),
    add column if not exists stretch_zone    varchar(1024);

DO
$$
    DECLARE
        r record;
    BEGIN
        FOR r IN SELECT *
                 FROM key_result
                 WHERE key_result.created_on IS NULL
            LOOP
                UPDATE key_result k
                SET created_on = k.modified_on
                WHERE k.id = r.id;
                UPDATE key_result k
                SET key_result_type = 'metric'
                where k.id = r.id;

            END LOOP;
    END
$$;

alter table key_result
    ALTER column created_on SET NOT NULL;
