CREATE  TABLE unit
(
    id            BIGINT                NOT NULL,
    version       INTEGER               NOT NULL,
    unit_name      TEXT                  NOT NULL,
    is_default      boolean                  NOT NULL,
    created_by_id     BIGINT,
    PRIMARY KEY (id)
);

CREATE SEQUENCE sequence_unit START 1;
insert into unit (id, version, unit_name, created_by_id, is_default)
values (nextval('sequence_unit'), 0, 'PERCENT', 1, true),
       (nextval('sequence_unit'), 0, 'NUMBER', 1, true),
       (nextval('sequence_unit'), 0, 'CHF', 1, true),
       (nextval('sequence_unit'), 0, 'EUR', 1, true),
       (nextval('sequence_unit'), 0, 'FTE', 1, true),
       (nextval('sequence_unit'), 0, 'UNKNOWN', 1, false);


DO
$$
    BEGIN
        IF EXISTS(SELECT *
                  FROM information_schema.columns
                  WHERE table_name = 'key_result'
                    and column_name = 'unit')
        THEN
            ALTER TABLE key_result
                add COLUMN unit_id bigint;

            UPDATE key_result
            SET unit_id = (
                SELECT id
                FROM unit
                WHERE unit_name = COALESCE(
                        (SELECT unit_name FROM unit WHERE unit_name = unit),
                        'UNKNOWN')
            )
            where key_result_type = 'metric';


        END IF;
    END
$$;

