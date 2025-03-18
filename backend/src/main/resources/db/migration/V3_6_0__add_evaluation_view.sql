DROP VIEW IF EXISTS EVALUATION_VIEW;
CREATE VIEW EVALUATION_VIEW AS
WITH team_quarters AS (
    SELECT
        t.id AS team_id,
        t.name AS team_name,
        q.id AS quarter_id,
        q.label AS quarter_label
    FROM team t
             CROSS JOIN quarter q
),
     objectives AS (
         SELECT
             o.team_id,
             o.quarter_id,
             COUNT(DISTINCT o.id) AS objective_amount,
             COUNT(*) FILTER (WHERE o.state IN ('SUCCESSFUL', 'NOTSUCCESSFUL')) AS completed_objectives_amount,
             COUNT(*) FILTER (WHERE o.state = 'SUCCESSFUL') AS successfully_completed_objectives_amount
         FROM OBJECTIVE o
         GROUP BY o.team_id, o.quarter_id
     ),
     latest_check_ins AS (
         SELECT DISTINCT ON (ci.key_result_id)
             ci.key_result_id,
             ci.value_metric,
             ci.zone,
             kr.key_result_type,
             kr.stretch_goal,
             kr.baseline,
             sub_o.team_id,
             sub_o.quarter_id
         FROM check_in ci
                  RIght JOIN key_result kr ON ci.key_result_id = kr.id
                  INNER JOIN objective sub_o ON kr.objective_id = sub_o.id
         ORDER BY ci.key_result_id, ci.modified_on DESC
     ),
     key_result_counts AS (
         SELECT
             team_id,
             quarter_id,
             COUNT(*) FILTER (WHERE key_result_type = 'ordinal') AS amount_key_results_ordinal,
             COUNT(*) FILTER (WHERE key_result_type = 'metric') AS amount_key_results_metric,
             COUNT(*) FILTER (
                 WHERE (key_result_type = 'ordinal' AND zone IN ('TARGET', 'STRETCH'))
                     OR (key_result_type = 'metric' AND (value_metric - baseline) / NULLIF(stretch_goal - baseline, 0)>= 0.7)
                 ) AS amount_key_results_in_target_or_stretch,
             COUNT(*) FILTER (
                 WHERE (key_result_type = 'ordinal' AND zone = 'FAIL')
                     OR (key_result_type = 'metric' AND (value_metric - baseline) / NULLIF(stretch_goal - baseline, 0) < 0.3)
                 ) AS amount_key_results_in_fail,
             COUNT(*) FILTER (
                 WHERE (key_result_type = 'ordinal' AND zone = 'COMMIT')
                     OR (key_result_type = 'metric' AND (value_metric - baseline) / NULLIF(stretch_goal - baseline, 0) BETWEEN 0.3 AND 0.7)
                 ) AS amount_key_results_in_commit,
             COUNT(*) FILTER (
                 WHERE (key_result_type = 'ordinal' AND zone = 'TARGET')
                     OR (key_result_type = 'metric' AND (value_metric - baseline) / NULLIF(stretch_goal - baseline, 0) BETWEEN 0.7 AND 1)
                 ) AS amount_key_results_in_target,
             COUNT(*) FILTER (
                 WHERE (key_result_type = 'ordinal' AND zone = 'STRETCH')
                     OR (key_result_type = 'metric' AND ((value_metric - baseline) / NULLIF(stretch_goal - baseline, 0)) >= 1)
                 ) AS amount_key_results_in_stretch
         FROM latest_check_ins
         GROUP BY team_id, quarter_id
     )
SELECT
    tq.team_id,
    tq.team_name,
    tq.quarter_id,
    tq.quarter_label,
    COALESCE(o.objective_amount, 0) AS objective_amount,
    COALESCE(o.completed_objectives_amount, 0) AS completed_objectives_amount,
    COALESCE(o.successfully_completed_objectives_amount, 0) AS successfully_completed_objectives_amount,
    COALESCE(kr.amount_key_results_ordinal, 0) AS amount_key_results_ordinal,
    COALESCE(kr.amount_key_results_metric, 0) AS amount_key_results_metric,
    COALESCE(kr.amount_key_results_in_target_or_stretch, 0) AS amount_key_results_in_target_or_stretch,
    COALESCE(kr.amount_key_results_in_fail, 0) AS amount_key_results_in_fail,
    COALESCE(kr.amount_key_results_in_commit, 0) AS amount_key_results_in_commit,
    COALESCE(kr.amount_key_results_in_target, 0) AS amount_key_results_in_target,
    COALESCE(kr.amount_key_results_in_stretch, 0) AS amount_key_results_in_stretch
FROM team_quarters tq
         LEFT JOIN objectives o
                   ON tq.team_id = o.team_id
                       AND tq.quarter_id = o.quarter_id
         LEFT JOIN key_result_counts kr
                   ON tq.team_id = kr.team_id
                       AND tq.quarter_id = kr.quarter_id
order by tq.team_id, tq.quarter_id;