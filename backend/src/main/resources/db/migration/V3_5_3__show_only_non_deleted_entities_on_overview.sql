
DROP VIEW IF EXISTS OVERVIEW;
CREATE VIEW OVERVIEW AS
SELECT tq.team_id          AS "team_id",
       tq.team_version     AS "team_version",
       tq.name             AS "team_name",
       tq.quater_id        AS "quarter_id",
       tq.label            AS "quarter_label",
       coalesce(o.id, -1)  AS "objective_id",
       o.title             AS "objective_title",
       o.state             AS "objective_state",
       o.created_on        AS "objective_created_on",
       coalesce(kr.id, -1) AS "key_result_id",
       kr.title            AS "key_result_title",
       kr.key_result_type  AS "key_result_type",
       U.unit_name as "unit",
       kr.baseline,
       kr.stretch_goal,
       kr.commit_zone,
       kr.target_zone,
       kr.stretch_zone,
       coalesce(c.id, -1)  AS "check_in_id",
       c.value_metric      AS "check_in_value",
       c.zone              AS "check_in_zone",
       c.confidence,
       c.created_on        AS "check_in_created_on"
FROM (select t.id as team_id, t.version as team_version, t.name, q.id as quater_id, q.label
      from team t,
           quarter Q where T.is_deleted is not true) tq
         LEFT JOIN OBJECTIVE O ON TQ.TEAM_ID = O.TEAM_ID AND TQ.QUATER_ID = O.QUARTER_ID
         LEFT JOIN KEY_RESULT KR ON O.ID = KR.OBJECTIVE_ID
         LEFT JOIN unit U ON U.ID = KR.unit_id
         LEFT JOIN CHECK_IN C ON KR.ID = C.KEY_RESULT_ID AND C.MODIFIED_ON = (SELECT MAX(CC.MODIFIED_ON)
                                                                              FROM CHECK_IN CC
                                                                              WHERE CC.KEY_RESULT_ID = C.KEY_RESULT_ID AND  CC.is_deleted is not true)
    WHERE KR.is_deleted is not true
    AND O.is_deleted = false;

drop view if exists alignment_selection;
create view alignment_selection as
select o.id                as "objective_id",
       o.title             as "objective_title",
       t.id                as "team_id",
       t.name              as "team_name",
       q.id                as "quarter_id",
       q.label             as "quarter_label",
       coalesce(kr.id, -1) as "key_result_id",
       kr.title            as "key_result_title"
from objective o
         left join team t on o.team_id = t.id
         left join quarter q on o.quarter_id = q.id
         left join key_result kr on o.id = kr.objective_id
WHERE T.is_deleted is not true
  and KR.is_deleted is not true
  and O.is_deleted is not true;

