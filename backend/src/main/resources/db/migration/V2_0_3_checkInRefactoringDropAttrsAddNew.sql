alter table measure
    alter column value drop not null,
    add column if not exists confidence    integer,
    add column if not exists check_in_type varchar(255),
    add column if not exists value text;
alter table measure rename measure_date to modified_on;

DO
$$
    DECLARE
        r record;
    BEGIN
        FOR r in SELECT * FROM measure
            LOOP
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




