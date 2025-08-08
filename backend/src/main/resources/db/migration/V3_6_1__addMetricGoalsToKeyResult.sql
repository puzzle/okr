ALTER TABLE key_result
    ADD COLUMN IF NOT EXISTS commit_goal DOUBLE PRECISION,
    add COLUMN IF NOT EXISTS target_goal DOUBLE PRECISION;

UPDATE key_result
SET commit_goal = ROUND(((COALESCE(stretch_goal, 0) - COALESCE(baseline, 0)) * 0.3 + COALESCE(baseline, 0))::numeric, 2),
    target_goal = ROUND(((COALESCE(stretch_goal, 0) - COALESCE(baseline, 0)) * 0.7 + COALESCE(baseline, 0))::numeric, 2)
WHERE commit_goal IS NULL
  AND target_goal IS NULL;
