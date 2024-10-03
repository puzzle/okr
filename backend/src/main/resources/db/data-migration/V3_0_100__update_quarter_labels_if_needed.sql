DO
$$
    DECLARE
        last_date date;
    BEGIN
        select max(start_date) from quarter INTO last_date;

        if (last_date < TO_DATE('2024-07-01', 'YY-MM-DD')) then
            raise notice 'update all quarters%', '';

            UPDATE quarter SET start_date = null;
            UPDATE quarter SET end_date = null;
            UPDATE quarter q SET label = 'label-' || q.id WHERE id < 999;

            UPDATE quarter SET label = 'GJ 24/25-Q1', start_date = '2024-07-01', end_date = '2024-09-30' WHERE id = 1;
            UPDATE quarter SET label = 'GJ 23/24-Q4', start_date = '2024-04-01', end_date = '2024-06-30' WHERE id = 2;
            UPDATE quarter SET label = 'GJ 23/24-Q3', start_date = '2024-01-01', end_date = '2024-03-31' WHERE id = 3;
            UPDATE quarter SET label = 'GJ 23/24-Q2', start_date = '2023-10-01', end_date = '2023-12-31' WHERE id = 4;
            UPDATE quarter SET label = 'GJ 23/24-Q1', start_date = '2023-07-01', end_date = '2023-09-30' WHERE id = 5;
            UPDATE quarter SET label = 'GJ 22/23-Q4', start_date = '2023-04-01', end_date = '2023-06-30' WHERE id = 6;
            UPDATE quarter SET label = 'GJ 22/23-Q3', start_date = '2023-01-01', end_date = '2023-03-31' WHERE id = 7;
            UPDATE quarter SET label = 'GJ 22/23-Q2', start_date = '2022-10-01', end_date = '2022-12-31' WHERE id = 8;
        else
            raise notice 'quarters are up to date';
        end if;
    END
$$;




