ALTER TABLE key_result
    add column if not exists commit_goal  double precision,
    add column if not exists target_goal  double precision;
