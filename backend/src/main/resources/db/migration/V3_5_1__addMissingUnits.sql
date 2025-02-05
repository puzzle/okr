UPDATE key_result
SET unit_id = 2
WHERE unit_id IS NULL
  AND key_result_type = 'metric';