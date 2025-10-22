UPDATE key_result
SET
    target_value = ROUND((baseline + (stretch_goal - baseline) * 0.7)::numeric, 2),
    commit_value = ROUND((baseline + (stretch_goal - baseline) * 0.3)::numeric, 2)
WHERE baseline IS NOT NULL
  AND stretch_goal IS NOT NULL
  AND target_value is NULL
  AND commit_value is NULL;
