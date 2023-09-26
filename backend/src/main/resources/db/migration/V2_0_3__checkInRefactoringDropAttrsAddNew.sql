DO
$$
    BEGIN
        IF EXISTS(SELECT *
                  FROM information_schema.columns
                  WHERE table_name = 'measure'
                    and column_name = 'value')
        THEN
            ALTER TABLE MEASURE
                RENAME VALUE TO VALUE_METRIC;
        END IF;
    END
$$;

DO
$$
    BEGIN
        IF EXISTS(SELECT *
                  FROM information_schema.tables
                  WHERE table_name = 'measure')
        THEN
            ALTER TABLE MEASURE
                ALTER COLUMN VALUE_METRIC DROP NOT NULL,
                ALTER COLUMN MEASURE_DATE DROP NOT NULL,
                ALTER COLUMN CHANGE_INFO DROP NOT NULL,
                ALTER COLUMN CHANGE_INFO TYPE VARCHAR(4096),
                ADD COLUMN IF NOT EXISTS CONFIDENCE    INTEGER,
                ADD COLUMN IF NOT EXISTS CHECK_IN_TYPE VARCHAR(255),
                ADD COLUMN IF NOT EXISTS ZONE          TEXT;
        END IF;
    END
$$;

DO
$$
    BEGIN
        IF EXISTS(SELECT *
                  FROM information_schema.columns
                  WHERE table_name = 'measure'
                    and column_name = 'measure_date')
        THEN
            ALTER TABLE MEASURE
                RENAME MEASURE_DATE TO MODIFIED_ON;
        END IF;
    END
$$;

DO
$$
    BEGIN
        IF EXISTS(SELECT *
                  FROM information_schema.tables
                  WHERE table_name = 'measure')
        THEN
            ALTER TABLE measure
                RENAME TO check_in;
        END IF;
    END
$$;

DO
$$
    BEGIN
        IF EXISTS(SELECT *
                  FROM information_schema.sequences
                  WHERE sequence_name = 'sequence_measure')
        THEN
            ALTER TABLE sequence_measure
                RENAME TO sequence_check_in;
        END IF;
    END
$$;

UPDATE check_in
SET confidence    = 5,
    check_in_type = 'metric'
WHERE check_in_type IS NULL;

