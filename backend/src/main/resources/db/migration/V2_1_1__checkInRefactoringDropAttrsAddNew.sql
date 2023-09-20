ALTER TABLE MEASURE RENAME VALUE TO VALUE_METRIC;

ALTER TABLE MEASURE
    ALTER COLUMN VALUE_METRIC DROP NOT NULL,
    ALTER COLUMN MEASURE_DATE DROP NOT NULL,
    ALTER COLUMN CHANGE_INFO DROP NOT NULL,
    ALTER COLUMN CHANGE_INFO TYPE VARCHAR(4096),
    ADD COLUMN CONFIDENCE    INTEGER,
    ADD COLUMN CHECK_IN_TYPE VARCHAR(255),
    ADD COLUMN ZONE TEXT;

ALTER TABLE MEASURE RENAME MEASURE_DATE TO MODIFIED_ON;

UPDATE MEASURE SET CONFIDENCE = 5;
UPDATE MEASURE SET CHECK_IN_TYPE = 'metric';

ALTER TABLE "public"."measure" RENAME TO "check_in";
ALTER TABLE "public"."sequence_measure" RENAME TO "sequence_check_in";

DROP VIEW IF EXISTS OVERVIEW;
CREATE VIEW OVERVIEW AS
SELECT t.id                AS "team_id",
       t.name              AS "team_name",
       coalesce(o.id, -1)  AS "objective_id",
       o.title             AS "objective_title",
       o.state             AS "objective_state",
       q.id                AS "quarter_id",
       q.label             AS "quarter_label",
       coalesce(kr.id, -1) AS "key_result_id",
       kr.title            AS "key_result_title",
       kr.unit,
       kr.baseline,
       kr.stretch_goal,
       kr.commit_zone,
       kr.target_zone,
       kr.stretch_zone,
       coalesce(c.id, -1)  AS "check_in_id",
       c.value_metric      AS "check_in_value",
       c.zone              AS "check_in_zone",
       c.confidence,
       c.created_on
FROM TEAM T
         LEFT JOIN OBJECTIVE O ON T.ID = O.TEAM_ID
         LEFT JOIN QUARTER Q ON O.QUARTER_ID = Q.ID
         LEFT JOIN KEY_RESULT KR ON O.ID = KR.OBJECTIVE_ID
         LEFT JOIN CHECK_IN C ON KR.ID = C.KEY_RESULT_ID AND C.MODIFIED_ON = (SELECT MAX(CC.MODIFIED_ON)
                                                                              FROM CHECK_IN CC
                                                                              WHERE CC.KEY_RESULT_ID = C.KEY_RESULT_ID);