alter table objective
    add column if not exists state text,
    add column if not exists created_on timestamp;

DO $$
BEGIN
    IF EXISTS(SELECT *
              FROM information_schema.columns
              WHERE table_name='objective' and column_name='owner_id')
    THEN
        ALTER TABLE "public"."objective" RENAME COLUMN "owner_id" TO "created_by_id";
    END IF;
END $$;

DO $$
DECLARE
    r record;
BEGIN
    FOR r IN SELECT * FROM objective
             WHERE objective.state IS NULL
               AND objective.created_on IS NULL
        LOOP
            UPDATE objective o
            SET state = 'ONGOING',
                created_on = modified_on
            WHERE o.id = r.id;

        END LOOP;
END$$;

alter table objective
    ALTER column state SET NOT NULL,
    ALTER column created_on SET NOT NULL;