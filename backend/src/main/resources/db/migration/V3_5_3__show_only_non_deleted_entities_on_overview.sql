DO
$$
    BEGIN
        IF EXISTS(SELECT *
                  FROM information_schema.columns
                  WHERE table_name = 'key_result'
                    and column_name = 'unit')
        THEN
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
                       quarter q where t.is_deleted = false) tq
                     LEFT JOIN OBJECTIVE O ON TQ.TEAM_ID = O.TEAM_ID AND TQ.QUATER_ID = O.QUARTER_ID AND O.is_deleted = false
                     LEFT JOIN KEY_RESULT KR ON O.ID = KR.OBJECTIVE_ID AND KR.is_deleted = false
                     LEFT JOIN unit U ON U.ID = KR.unit_id and U.is_deleted = false
                     LEFT JOIN CHECK_IN C ON KR.ID = C.KEY_RESULT_ID AND C.MODIFIED_ON = (SELECT MAX(CC.MODIFIED_ON)
                                                                                          FROM CHECK_IN CC
                                                                                          WHERE CC.KEY_RESULT_ID = C.KEY_RESULT_ID and CC.is_deleted = false);




        END IF;
    END
$$;

