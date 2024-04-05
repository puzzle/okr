DROP VIEW IF EXISTS alignment_view;
CREATE VIEW alignment_view AS
SELECT
    concat(oa.id, coalesce(a.target_objective_id, a.target_key_result_id),'S','objective',a.alignment_type) as unique_id,
    oa.id as id,
    oa.title as title,
    ott.id as team_id,
    ott.name as team_name,
    oa.quarter_id as quarter_id,
    oa.state as state,
    'objective' as object_type,
    'source' as connection_item,
    coalesce(a.target_objective_id, a.target_key_result_id) as ref_id,
    a.alignment_type as ref_type
FROM alignment a
    LEFT JOIN objective oa ON oa.id = a.aligned_objective_id
    LEFT JOIN team ott ON ott.id = oa.team_id

UNION

SELECT
    concat(ot.id, a.aligned_objective_id,'T','objective','objective') as unique_id,
    ot.id as id,
    ot.title as title,
    ott.id as team_id,
    ott.name as team_name,
    ot.quarter_id as quarter_id,
    ot.state as state,
    'objective' as object_type,
    'target' as connection_item,
    a.aligned_objective_id as ref_id,
    'objective' as ref_type
FROM alignment a
         LEFT JOIN objective ot ON ot.id = a.target_objective_id
         LEFT JOIN team ott ON ott.id = ot.team_id
where alignment_type = 'objective'

UNION

SELECT
    concat(krt.id, a.aligned_objective_id,'T','keyResult','keyResult') as unique_id,
    krt.id as id,
    krt.title as title,
    ott.id as team_id,
    ott.name as team_name,
    o.quarter_id as quarter_id,
    null as state,
    'keyResult' as object_type,
    'target' as connection_item,
    a.aligned_objective_id as ref_id,
    'objective' as ref_type
FROM alignment a
         LEFT JOIN key_result krt ON krt.id = a.target_key_result_id
         LEFT JOIN objective o ON o.id = krt.objective_id
         LEFT JOIN team ott ON ott.id = o.team_id
where alignment_type = 'keyResult';