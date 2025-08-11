ALTER TABLE key_result
    ADD COLUMN IF NOT EXISTS wording_commit_value DOUBLE PRECISION,
    add COLUMN IF NOT EXISTS wording_target_value DOUBLE PRECISION;

UPDATE key_result
SET wording_commit_value = ROUND(((COALESCE(stretch_goal, 0) - COALESCE(baseline, 0)) * 0.3 + COALESCE(baseline, 0))::numeric, 2),
    wording_target_value = ROUND(((COALESCE(stretch_goal, 0) - COALESCE(baseline, 0)) * 0.7 + COALESCE(baseline, 0))::numeric, 2)
WHERE wording_commit_value IS NULL
  AND wording_target_value IS NULL;
