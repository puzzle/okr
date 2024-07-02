DO
$$
    DECLARE
        r                    record;
        year_quarter         text;
        years                text;
        first_year           text;
        last_year            text;
        quarter_label        text;
        quarter_digit        text;
        start_month_day      text;
        end_month_day        text;
        formatted_start_date text;
        formatted_end_date   text;
        new_start_date       date;
        new_end_date         date;
        year_offset          integer;

    BEGIN
        FOR r IN SELECT *
                 FROM quarter
                 WHERE quarter.start_date IS NULL
                   AND quarter.end_date IS NULL
            LOOP
                CONTINUE WHEN substr(r.label, 1, 2) != 'GJ';

                year_quarter := split_part(r.label, ' ', 2);
                years := split_part(year_quarter, '-', 1);
                first_year := split_part(years, '/', 1);
                last_year := split_part(years, '/', 2);
                quarter_label := split_part(year_quarter, '-', 2);
                quarter_digit := substr(quarter_label, 2, 1);

                SELECT qm.start_month_day
                INTO start_month_day
                FROM quarter_meta qm
                WHERE qm.quarter = CAST(quarter_digit AS integer);

                SELECT qm.start_year_offset
                INTO year_offset
                FROM quarter_meta qm
                WHERE qm.quarter = CAST(quarter_digit AS integer);

                SELECT CONCAT(CAST(first_year AS integer) - year_offset, start_month_day)
                INTO formatted_start_date
                FROM quarter;

                --RAISE NOTICE 'Start date: %', formatted_start_date;

                SELECT qm.end_month_day
                INTO end_month_day
                FROM quarter_meta qm
                WHERE qm.quarter = CAST(quarter_digit AS integer);

                SELECT CONCAT(CAST(first_year AS integer) - year_offset, end_month_day)
                INTO formatted_end_date
                FROM quarter;

                --RAISE NOTICE 'End date  : %', formatted_end_date;

                new_start_date := TO_DATE(formatted_start_date, 'YY-MM-DD');
                new_end_date := TO_DATE(formatted_end_date, 'YY-MM-DD');

                UPDATE quarter q
                SET start_date = new_start_date,
                    end_date   = new_end_date
                WHERE q.id = r.id;

            END LOOP;
    END
$$;
