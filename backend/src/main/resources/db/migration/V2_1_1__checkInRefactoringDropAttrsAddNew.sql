ALTER TABLE measure
    RENAME value TO value_metric;
ALTER TABLE measure
    ALTER COLUMN value_metric DROP NOT NULL,
    ALTER COLUMN measure_date DROP NOT NULL,
    ALTER COLUMN change_info DROP NOT NULL,
    ALTER COLUMN change_info TYPE VARCHAR(4096),
    ADD COLUMN confidence    INTEGER,
    ADD COLUMN check_in_type VARCHAR(255),
    ADD COLUMN value_ordinal TEXT;
ALTER TABLE measure
    RENAME measure_date TO modified_on;


UPDATE measure SET confidence = 5;
update measure SET check_in_type = 'metric';

ALTER TABLE "public"."measure"
    RENAME TO "check_in";
ALTER TABLE "public"."sequence_measure"
    RENAME TO "sequence_check_in";
END TRANSACTION;




