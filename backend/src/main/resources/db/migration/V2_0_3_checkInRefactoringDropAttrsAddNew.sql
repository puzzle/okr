alter table measure rename value to value_metric;
alter table measure
    alter column value_metric drop not null,
    alter column measure_date drop not null,
    alter column change_info drop not null,
    alter column change_info TYPE varchar(4096),
    add column if not exists confidence    integer,
    add column if not exists check_in_type varchar(255),
    add column if not exists value_ordinal text;
alter table measure rename measure_date to modified_on;

DO
$$
    DECLARE
        r record;
    BEGIN
        FOR r in SELECT * FROM measure
            LOOP
                UPDATE measure m SET confidence = 5 WHERE confidence IS NULL AND m.id = r.id;
                UPDATE measure m SET check_in_type = 'metric' WHERE m.id = r.id;
            end loop;
    end
$$;

DO
$$
    BEGIN
        IF EXISTS(SELECT *
                  FROM information_schema.columns WHERE table_name = 'measure')
        THEN
            ALTER TABLE "public"."measure" RENAME TO "check_in";
            ALTER TABLE "public"."sequence_measure" RENAME TO "sequence_check_in";
        END IF;
    END
$$;
END TRANSACTION;




