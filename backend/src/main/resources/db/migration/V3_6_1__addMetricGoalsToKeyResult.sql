ALTER TABLE key_result
    ADD COLUMN IF NOT EXISTS commit_value DOUBLE PRECISION,
    ADD COLUMN IF NOT EXISTS target_value DOUBLE PRECISION;

UPDATE key_result
SET commit_value = ROUND(((COALESCE(stretch_goal, 0) - COALESCE(baseline, 0)) * 0.3 + COALESCE(baseline, 0))::numeric, 2),
    target_value = ROUND(((COALESCE(stretch_goal, 0) - COALESCE(baseline, 0)) * 0.7 + COALESCE(baseline, 0))::numeric, 2)
WHERE commit_value IS NULL
  AND target_value IS NULL;
