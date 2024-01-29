insert into alignment (id, aligned_objective_id, alignment_type, target_key_result_id, target_objective_id, version)
values (1, 4, 'objective', null, 6, 1),
(2, 3, 'objective', null, 5, 1),
(3, 10, 'objective', null, 8, 1),
(4, 9, 'keyResult', 8, null, 1);

DROP VIEW IF EXISTS ALIGNMENT_GET;
CREATE VIEW ALIGNMENT_GET AS
SELECT o.id, o.title, o.team_id, o.quarter_id, a.alignment_type, a.aligned_objective_id, a.target_key_result_id, a.target_objective_id
FROM objective o
         JOIN alignment a ON o.id IN (a.aligned_objective_id, a.target_objective_id)

UNION

SELECT kr.id, kr.title, o.team_id, o.quarter_id, a.alignment_type, a.aligned_objective_id, a.target_key_result_id, a.target_objective_id
FROM key_result kr
         JOIN alignment a ON kr.id = a.target_key_result_id
         JOIN objective o ON kr.objective_id = o.id;