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
    ADD COLUMN value_ordinal TEXT;
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

DROP VIEW overview;
CREATE VIEW overview AS
SELECT t.id                as "team_id",
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
       c."value_metric"           as "check_in_value_metric",
       c."value_ordinal" as "check_in_value_ordinal",
       c."confidence",
       c.created_on
FROM team t
         LEFT JOIN objective o ON t.id = o.team_id
         LEFT JOIN quarter q ON o.quarter_id = q.id
         LEFT JOIN key_result kr ON o.id = kr.objective_id
         LEFT JOIN "check_in" c ON kr.id = c.key_result_id AND c.modified_on = (SELECT MAX(cc.modified_on)
                                                                              FROM "check_in" cc
                                                                              WHERE cc.key_result_id = c.key_result_id)





