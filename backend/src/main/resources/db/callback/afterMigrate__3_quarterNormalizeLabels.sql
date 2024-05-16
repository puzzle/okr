DO
$$
    DECLARE
        r                 record;
        year_quarter      text;
        years             text;
        first_year        text;
        quarter_label     text;
        quarter_digit     text;
        year_offset_count integer;
        new_label         text;
    BEGIN
        FOR r IN SELECT *
                 FROM quarter
                 WHERE quarter.label LIKE 'GJ%'
            LOOP
                SELECT count(qm.start_year_offset)
                FROM quarter_meta qm
                WHERE qm.start_year_offset = 0
                INTO year_offset_count;

                -- if all quarters are in the same year
                IF (year_offset_count = 4) THEN
                    year_quarter := split_part(r.label, ' ', 2);
                    years := split_part(year_quarter, '-', 1);
                    first_year := split_part(years, '/', 1);
                    quarter_label := split_part(year_quarter, '-', 2);
                    quarter_digit := substr(quarter_label, 2, 1);

                    new_label = FORMAT('GJ 20%s-Q%s', first_year, quarter_digit);
                    --RAISE NOTICE 'process % -> %', r.label, new_label;

                    UPDATE quarter q
                    SET label = new_label
                    WHERE q.id = r.id;

                ELSE
                    --RAISE NOTICE 'skip %', r.label;
                END IF;
            END LOOP;
    END
$$;

-- cleanup
DROP TABLE IF EXISTS quarter_meta;