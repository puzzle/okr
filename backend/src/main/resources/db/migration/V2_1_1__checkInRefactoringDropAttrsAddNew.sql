ALTER TABLE measure
    RENAME value TO value_metric;
ALTER TABLE measure
    ALTER COLUMN value_metric DROP NOT NULL,
ALTER
COLUMN measure_date DROP
NOT NULL,
    ALTER
COLUMN change_info DROP
NOT NULL,
    ALTER
COLUMN change_info TYPE VARCHAR(4096),
    ADD COLUMN confidence    INTEGER,
    ADD COLUMN check_in_type VARCHAR(255),
    ADD COLUMN zone TEXT;
ALTER TABLE measure
    RENAME measure_date TO modified_on;

UPDATE measure
SET confidence = 5;
UPDATE measure
SET check_in_type = 'metric';

ALTER TABLE "public"."measure"
    RENAME TO "check_in";
ALTER TABLE "public"."sequence_measure"
    RENAME TO "sequence_check_in";

DROP VIEW IF EXISTS OVERVIEW;
CREATE VIEW OVERVIEW AS
select t.id                as "team_id",
       t.name              as "team_name",
       coalesce(o.id, -1)  as "objective_id",
       o.title             as "objective_title",
       o.state             as "objective_state",
       q.id                as "quarter_id",
       q.label             as "quarter_label",
       coalesce(kr.id, -1) as "key_result_id",
       kr.title            as "key_result_title",
       kr.unit,
       kr.baseline,
       kr.stretch_goal,
       kr.commit_zone,
       kr.target_zone,
       kr.stretch_zone,
       coalesce(c.id, -1)  as "check_in_id",
       c.value_metric      as "check_in_value",
       c.zone              as "check_in_zone",
       c.confidence,
       c.created_on
FROM TEAM T
         LEFT JOIN OBJECTIVE O ON T.ID = O.TEAM_ID
         LEFT JOIN QUARTER Q ON O.QUARTER_ID = Q.ID
         LEFT JOIN KEY_RESULT KR ON O.ID = KR.OBJECTIVE_ID
         LEFT JOIN check_in C ON KR.ID = C.KEY_RESULT_ID AND C.modified_on = (SELECT MAX(CC.modified_on)
                                                                              FROM check_in CC
                                                                              WHERE CC.KEY_RESULT_ID = C.KEY_RESULT_ID);