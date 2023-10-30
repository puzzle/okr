alter table quarter
    add column if not exists start_date date,
    add column if not exists end_date date;

DO $$
DECLARE
    r record;
    year_quarter text;
    years text;
    first_year text;
    last_year text;
    quarter_label text;
    formatted_start_date text;
    formatted_end_date text;
    new_start_date date;
    new_end_date date;
BEGIN
    FOR r IN SELECT * FROM quarter
             WHERE quarter.start_date IS NULL
               AND quarter.end_date IS NULL
        LOOP
            year_quarter :=  split_part(r.label, ' ', 2);
            years :=  split_part(year_quarter, '-', 1);
            first_year :=  split_part(years, '/', 1);
            last_year :=  split_part(years, '/', 2);
            quarter_label :=  split_part(year_quarter, '-', 2);

            SELECT
                CASE
                    WHEN quarter_label = 'Q1' THEN CONCAT(first_year, '-07-01')
                    WHEN quarter_label = 'Q2' THEN CONCAT(first_year, '-10-01')
                    WHEN quarter_label = 'Q3' THEN CONCAT(last_year, '-01-01')
                    WHEN quarter_label = 'Q4' THEN CONCAT(last_year, '-04-01')
                    END
            INTO formatted_start_date
            FROM quarter;

            RAISE NOTICE 'Start date: %', formatted_start_date;

            SELECT
                CASE
                    WHEN quarter_label = 'Q1' THEN CONCAT(first_year, '-09-30')
                    WHEN quarter_label = 'Q2' THEN CONCAT(first_year, '-12-31')
                    WHEN quarter_label = 'Q3' THEN CONCAT(last_year, '-03-31 ')
                    WHEN quarter_label = 'Q4' THEN CONCAT(last_year, '-06-30 ')
                    END
            INTO formatted_end_date
            FROM quarter;

            new_start_date :=  TO_DATE(formatted_start_date, 'YY-MM-DD');
            new_end_date :=  TO_DATE(formatted_end_date, 'YY-MM-DD');

            UPDATE quarter q
            SET start_date = new_start_date,
                end_date = new_end_date
            WHERE q.id = r.id;

        END LOOP;
END$$;

alter table quarter
    ALTER column start_date SET NOT NULL,
    ALTER column end_date SET NOT NULL;

ALTER SEQUENCE sequence_team RESTART WITH 500;
ALTER SEQUENCE sequence_person RESTART WITH 500;
ALTER SEQUENCE sequence_quarter RESTART WITH 500;
ALTER SEQUENCE sequence_objective RESTART WITH 500;
ALTER SEQUENCE sequence_key_result RESTART WITH 500;
ALTER SEQUENCE sequence_measure RESTART WITH 500;