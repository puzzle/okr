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
     kr_latest_check_in AS (
         SELECT DISTINCT ON (kr.id)
             kr.id as key_result_id,
             ci.value_metric,
             COALESCE(((value_metric - baseline) / NULLIF(stretch_goal - baseline, 0)),0) as progress,
             ci.zone,
             kr.key_result_type,
             kr.stretch_goal,
             kr.baseline,
             sub_o.team_id,
             sub_o.quarter_id
         FROM key_result kr
                  LEFT JOIN CHECK_IN ci ON KR.ID = ci.KEY_RESULT_ID AND ci.MODIFIED_ON = (SELECT MAX(CC.MODIFIED_ON)
                                                                                          FROM CHECK_IN CC
                                                                                          WHERE CC.KEY_RESULT_ID = ci.KEY_RESULT_ID)
                  INNER JOIN objective sub_o ON kr.objective_id = sub_o.id
         ORDER BY kr.id
     ),
     key_result_counts AS (
         SELECT
             team_id,
             quarter_id,
             COUNT(*) AS key_result_amount,
             COUNT(*) FILTER (WHERE key_result_type = 'ordinal') AS key_results_ordinal_amount,
             COUNT(*) FILTER (WHERE key_result_type = 'metric') AS key_results_metric_amount,
             COUNT(*) FILTER (
                 WHERE (key_result_type = 'ordinal' AND zone IN ('TARGET', 'STRETCH'))
                     OR (key_result_type = 'metric' AND progress >= 0.7)
                 ) AS key_results_in_target_or_stretch_amount,
             COUNT(*) FILTER (
                 WHERE (key_result_type = 'ordinal' AND zone = 'FAIL')
                     OR (key_result_type = 'metric' AND progress > 0 AND progress < 0.3)
                 ) AS key_results_in_fail_amount,
             COUNT(*) FILTER (
                 WHERE (key_result_type = 'ordinal' AND zone = 'COMMIT')
                     OR (key_result_type = 'metric' AND progress >= 0.3 AND progress < 0.7)
                 ) AS key_results_in_commit_amount,
             COUNT(*) FILTER (
                 WHERE (key_result_type = 'ordinal' AND zone = 'TARGET')
                     OR (key_result_type = 'metric' AND progress >= 0.7 AND progress < 1)
                 ) AS key_results_in_target_amount,
             COUNT(*) FILTER (
                 WHERE (key_result_type = 'ordinal' AND zone = 'STRETCH')
                     OR (key_result_type = 'metric' AND progress >= 1)
                 ) AS key_results_in_stretch_amount
         FROM kr_latest_check_in
         GROUP BY team_id, quarter_id
     )
SELECT
    tq.team_id,
    tq.team_name,
    tq.quarter_id,
    tq.quarter_label,
    COALESCE(o.objective_amount, 0) AS objective_amount,
    COALESCE(o.completed_objectives_amount , 0) as completed_objectives_amount,
    COALESCE(o.successfully_completed_objectives_amount, 0) as successfully_completed_objectives_amount,
    COALESCE(kr.key_result_amount, 0) as key_result_amount,
    COALESCE(kr.key_results_ordinal_amount, 0) as key_results_ordinal_amount,
    COALESCE(kr.key_results_metric_amount, 0) as key_results_metric_amount,
    COALESCE(kr.key_results_in_target_or_stretch_amount, 0) as key_results_in_target_or_stretch_amount,
    COALESCE(kr.key_results_in_fail_amount, 0) as key_results_in_fail_amount,
    COALESCE(kr.key_results_in_commit_amount, 0) as key_results_in_commit_amount,
    COALESCE(kr.key_results_in_target_amount, 0) as key_results_in_target_amount,
    COALESCE(kr.key_results_in_stretch_amount, 0) as key_results_in_stretch_amount
FROM team_quarters tq
         LEFT JOIN objectives o
                   ON tq.team_id = o.team_id
                       AND tq.quarter_id = o.quarter_id
         LEFT JOIN key_result_counts kr
                   ON tq.team_id = kr.team_id
                       AND tq.quarter_id = kr.quarter_id
order by tq.team_id, tq.quarter_id;