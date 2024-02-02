insert into alignment (id, aligned_objective_id, alignment_type, target_key_result_id, target_objective_id, version)
values  (1, 4, 'objective', null, 6, 1),
        (2, 3, 'objective', null, 6, 1),
        (3, 8, 'objective', null, 3, 1),
        (4, 9, 'keyResult', 8, null, 1),
        (5, 10, 'keyResult', 5, null, 1),
        (6, 5, 'keyResult', 4, null, 1),
        (7, 6, 'keyResult', 3, null, 1);

DROP VIEW IF EXISTS ALIGNMENT_VIEW;
CREATE VIEW ALIGNMENT_VIEW AS
SELECT
    a.id AS alignment_id,
    oa.id AS aligned_objective_id,
    oa.title AS aligned_objective_title,
    oat.id AS aligned_objective_team_id,
    oat.name AS aligned_objective_team_name,
    oa.quarter_id as aligned_objective_quarter_id,
    a.alignment_type as alignment_type,
    ot.id AS target_objective_id,
    ot.title AS target_objective_title,
    ott.name AS target_objective_team_name,
    krt.id AS target_key_result_id,
    krt.title AS target_key_result_title,
    krtot.name AS target_key_result_team_name
FROM alignment a
         JOIN objective oa ON oa.id = a.aligned_objective_id
         JOIN team oat ON oat.id = oa.team_id
         LEFT JOIN objective ot ON ot.id = a.target_objective_id
         LEFT JOIN team ott ON ott.id = ot.team_id
         LEFT JOIN key_result krt ON krt.id = a.target_key_result_id
         LEFT JOIN objective krto ON krto.id = krt.objective_id
         LEFT JOIN team krtot ON krtot.id = krto.team_id;