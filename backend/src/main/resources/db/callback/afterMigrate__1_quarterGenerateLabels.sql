-- Config quarter meta data
CREATE TABLE IF NOT EXISTS quarter_meta
(
    id                integer,
    start_month       integer,
    end_month         integer,
    quarter           integer,
    start_year_offset integer,
    start_month_day   text,
    end_month_day     text
);

-- puzzle year: starts in july
INSERT INTO quarter_meta(id, start_month, end_month, quarter, start_year_offset, start_month_day, end_month_day)
VALUES (1, 1, 3, 3, -1, '-01-01', '-03-31');
INSERT INTO quarter_meta(id, start_month, end_month, quarter, start_year_offset, start_month_day, end_month_day)
VALUES (2, 4, 6, 4, -1, '-04-01', '-06-30');
INSERT INTO quarter_meta(id, start_month, end_month, quarter, start_year_offset, start_month_day, end_month_day)
VALUES (3, 7, 9, 1, 0, '-07-01', '-09-30');
INSERT INTO quarter_meta(id, start_month, end_month, quarter, start_year_offset, start_month_day, end_month_day)
VALUES (4, 10, 12, 2, 0, '-10-01', '-12-31');

-- standard year: starts in january
/*
INSERT INTO quarter_meta(id, start_month, end_month, quarter, start_year_offset, start_month_day, end_month_day)
VALUES (1, 1, 3, 1, 0, '-01-01', '-03-31');
INSERT INTO quarter_meta(id, start_month, end_month, quarter, start_year_offset, start_month_day, end_month_day)
VALUES (2, 4, 6, 2, 0, '-04-01', '-06-30');
INSERT INTO quarter_meta(id, start_month, end_month, quarter, start_year_offset, start_month_day, end_month_day)
VALUES (3, 7, 9, 3, 0, '-07-01', '-09-30');
INSERT INTO quarter_meta(id, start_month, end_month, quarter, start_year_offset, start_month_day, end_month_day)
VALUES (4, 10, 12, 4, 0, '-10-01', '-12-31');
*/

-- "empty" label and start/end date of quarter table
DELETE FROM quarter WHERE id IN (9, 10);
UPDATE quarter SET start_date = null;
UPDATE quarter SET end_date = null;
UPDATE quarter q SET label = 'label-' || q.id WHERE id < 999;

-- utility functions
CREATE OR REPLACE FUNCTION next_quarter(current_quarter integer)
    RETURNS integer
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (current_quarter = 4) THEN
        RETURN 1;
    ELSE
        RETURN current_quarter + 1;
    END IF;
END;
$$;

CREATE OR REPLACE FUNCTION previous_quarter(current_quarter integer)
    RETURNS integer
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (current_quarter = 1) THEN
        RETURN 4;
    ELSE
        RETURN current_quarter -1;
    END IF;
END;
$$;

CREATE OR REPLACE FUNCTION start_year(current_quarter integer)
    RETURNS integer
    LANGUAGE plpgsql
AS
$$
DECLARE
    current_year integer;
    start_year_offset integer;
BEGIN
    SELECT date_part('year', (SELECT current_timestamp)) INTO current_year;
    SELECT qm.start_year_offset from quarter_meta qm where qm.quarter = current_quarter INTO start_year_offset;
    RETURN current_year + start_year_offset;
END;
$$;

CREATE OR REPLACE FUNCTION end_year(start_year integer)
    RETURNS text
    LANGUAGE plpgsql
AS
$$
DECLARE
    year_offset_count integer;
BEGIN
    SELECT count(qm.start_year_offset) FROM quarter_meta qm where qm.start_year_offset = 0 INTO year_offset_count;

    -- if all quarters are in the same year
    IF (year_offset_count = 4) THEN
        RETURN start_year;
    ELSE
        RETURN start_year + 1;
    END IF;
END;
$$;

CREATE OR REPLACE FUNCTION year_of_next_quarter_without_offset_correction(current_year integer, current_quarter integer)
    RETURNS integer
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (current_quarter = 4) THEN
        RETURN current_year + 1;
    ELSE
        RETURN current_year;
    END IF;
END;
$$;

CREATE OR REPLACE FUNCTION year_of_previous_quarter_without_offset_correction(current_year integer, current_quarter integer)
    RETURNS integer
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (current_quarter = 1) THEN
        return current_year - 1;
    ELSE
        return current_year;
    END IF;
END;
$$;

CREATE OR REPLACE FUNCTION format_year(year integer)
    RETURNS text
    LANGUAGE plpgsql
AS
$$
BEGIN
    RETURN substr(cast(year AS text),3,4);
END;
$$;

CREATE OR REPLACE FUNCTION format_label(start_year integer, end_year integer, quarter integer)
    RETURNS text
    LANGUAGE plpgsql
AS
$$
DECLARE
    start_year_label text;
    end_year_label text;
BEGIN
    SELECT format_year(start_year) INTO start_year_label;
    SELECT format_year(end_year) INTO end_year_label;
    RETURN FORMAT('GJ %s/%s-Q%s', start_year_label, end_year_label, quarter);
END;
$$;

CREATE OR REPLACE FUNCTION create_future_quarter_label(quarter integer, start_year integer, quarter_db_id integer)
    RETURNS text
    LANGUAGE plpgsql
AS
$$
DECLARE
    next_quarter integer;
    end_year integer;
    quarter_label text;
BEGIN
    SELECT next_quarter(quarter) INTO next_quarter;
    SELECT end_year(start_year) INTO end_year;
    SELECT format_label(start_year, end_year, next_quarter) INTO quarter_label;
    UPDATE quarter SET label = quarter_label WHERE id = quarter_db_id;
    return quarter_label;
END;
$$;

CREATE OR REPLACE FUNCTION create_current_quarter_label(quarter integer, start_year integer, quarter_db_id integer)
    RETURNS text
    LANGUAGE plpgsql
AS
$$
DECLARE
    end_year integer;
    quarter_label text;
BEGIN
    SELECT end_year(start_year) INTO end_year;
    SELECT format_label(start_year, end_year, quarter) INTO quarter_label;
    UPDATE quarter SET label = quarter_label WHERE id = quarter_db_id;
    return quarter_label;
END;
$$;

CREATE OR REPLACE FUNCTION create_past_quarter_label(quarter integer, start_year integer, quarter_db_id integer)
    RETURNS RECORD
    LANGUAGE plpgsql
AS
$$
DECLARE
    previous_quarter integer;
    end_year integer;
    quarter_label text;
    r RECORD;
BEGIN
    SELECT previous_quarter(quarter) INTO previous_quarter;
    SELECT end_year(start_year) INTO end_year;
    SELECT format_label(start_year, end_year,previous_quarter) INTO quarter_label;
    UPDATE quarter SET label = quarter_label WHERE id = quarter_db_id;

    -- several return values as record
    SELECT previous_quarter AS quarter, start_year AS year, quarter_label AS label INTO r;
    RETURN r;
END;
$$;

-- create quarter labels
DO
$$
    DECLARE
        current_month integer;
        current_year integer;
        future_quarter_year integer;
        past_quarter_year integer;
        current_quarter integer;
        past_quarter integer;
        quarter_label text;
        r record;
    BEGIN
        SELECT date_part('month', (SELECT current_timestamp)) INTO current_month;
        SELECT quarter INTO current_quarter FROM quarter_meta qm WHERE current_month >= qm.start_month AND current_month <= qm.end_month;
        SELECT start_year(current_quarter) INTO current_year;

        -- next (future) quarter
        future_quarter_year = year_of_next_quarter_without_offset_correction(current_year, current_quarter);
        SELECT create_future_quarter_label(current_quarter, future_quarter_year, 1) INTO quarter_label;
        --RAISE NOTICE '%', quarter_label;

        -- current quarter
        SELECT create_current_quarter_label(current_quarter, current_year, 2) INTO quarter_label;
        --RAISE NOTICE '% *', quarter_label;

        -- previous (past) quarters
        past_quarter = current_quarter;
        past_quarter_year = current_year;

        FOR quarter_id IN 3..8 LOOP
                past_quarter_year = year_of_previous_quarter_without_offset_correction(past_quarter_year, past_quarter);
                SELECT quarter, year, label FROM create_past_quarter_label(past_quarter, past_quarter_year, quarter_id) AS (quarter integer, year integer, label text) INTO r;
                --RAISE NOTICE '%', r.label;
                past_quarter = r.quarter;
                past_quarter_year = r.year;
            END LOOP;
    END
$$;

-- cleanup
DROP FUNCTION IF EXISTS create_past_quarter_label;
DROP FUNCTION IF EXISTS create_future_quarter_label;
DROP FUNCTION IF EXISTS create_current_quarter_label;
DROP FUNCTION IF EXISTS start_year;
DROP FUNCTION IF EXISTS end_year;
DROP FUNCTION IF EXISTS format_label;
DROP FUNCTION IF EXISTS format_year;
DROP FUNCTION IF EXISTS next_quarter;
DROP FUNCTION IF EXISTS previous_quarter;
DROP FUNCTION IF EXISTS year_of_next_quarter_without_offset_correction;
DROP FUNCTION IF EXISTS year_of_previous_quarter_without_offset_correction;