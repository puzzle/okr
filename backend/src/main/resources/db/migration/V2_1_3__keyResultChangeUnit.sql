UPDATE key_result
    SET unit = null
    WHERE key_result_type like 'ordinal';

UPDATE key_result
    SET unit = 'CHF'
    WHERE id in (14, 15, 16, 17, 18, 19);

UPDATE key_result
SET unit = 'PERCENT'
WHERE id in (31, 33, 10, 9, 11, 12);

UPDATE key_result
SET unit = 'FTE'
WHERE id in (23, 24, 25, 3, 13, 20);

UPDATE key_result
SET unit = 'NUMBER'
WHERE id in (22, 4, 5, 6, 7, 8);