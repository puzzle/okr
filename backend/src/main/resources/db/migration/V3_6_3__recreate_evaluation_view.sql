DROP VIEW IF EXISTS EVALUATION_VIEW;

CREATE VIEW EVALUATION_VIEW AS
SELECT
    o.id              AS objective_id,
    o.team_id,
    o.quarter_id,
    o.state           AS objective_state,
    kr.id             AS key_result_id,
    kr.key_result_type,
    kr.baseline,
    kr.commit_value,
    kr.target_value,
    kr.stretch_goal,
    ci.value_metric,
    ci.zone,
    ci.modified_on    AS latest_check_in_date
FROM objective o
         INNER JOIN key_result kr
                    ON kr.objective_id = o.id
         LEFT JOIN LATERAL (
    SELECT ci2.*
    FROM check_in ci2
    WHERE ci2.key_result_id = kr.id
    ORDER BY ci2.modified_on DESC
    LIMIT 1
    ) ci ON TRUE
WHERE o.state <> 'DRAFT';
