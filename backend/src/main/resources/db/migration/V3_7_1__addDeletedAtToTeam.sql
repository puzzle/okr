ALTER TABLE team
    ADD COLUMN status TEXT default 'ACTIVE',
    ADD COLUMN marked_as_archived_at TIMESTAMP;

CREATE OR REPLACE FUNCTION prevent_archived_team_update()
    RETURNS TRIGGER AS $$
BEGIN
    IF OLD.status = 'ARCHIVED' THEN

        IF NEW.status != 'ACTIVE' THEN
            RAISE EXCEPTION 'DB Write-Lock Active: Cannot modify an archived team (ID: %).', OLD.id;
        END IF;

    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER enforce_archived_team_lock
BEFORE UPDATE ON team
FOR EACH ROW
EXECUTE FUNCTION prevent_archived_team_update();

DROP VIEW IF EXISTS OVERVIEW RESTRICT ;
CREATE VIEW OVERVIEW AS
SELECT tq.team_id                    AS "team_id",
       tq.team_version               AS "team_version",
       tq.name                       AS "team_name",
       tq.team_marked_as_archived_at AS "team_marked_as_archived_at",
       tq.quater_id                  AS "quarter_id",
       tq.label                      AS "quarter_label",
       coalesce(o.id, -1)            AS "objective_id",
       o.title                       AS "objective_title",
       o.state                       AS "objective_state",
       o.created_on                  AS "objective_created_on",
       coalesce(kr.id, -1)           AS "key_result_id",
       kr.title                      AS "key_result_title",
       kr.key_result_type            AS "key_result_type",
       kr.baseline,
       kr.commit_value,
       kr.target_value,
       kr.stretch_goal,
       kr.commit_zone,
       kr.target_zone,
       kr.stretch_zone,
       coalesce(c.id, -1)  AS "check_in_id",
       c.value_metric      AS "check_in_value",
       c.zone              AS "check_in_zone",
       c.confidence,
       c.created_on        AS "check_in_created_on"
FROM (select t.id as team_id, t.version as team_version, t.name, t.marked_as_archived_at as team_marked_as_archived_at ,q.id as quater_id, q.label
      from team t,
           quarter q) tq
         LEFT JOIN OBJECTIVE O ON TQ.TEAM_ID = O.TEAM_ID AND TQ.QUATER_ID = O.QUARTER_ID
         LEFT JOIN KEY_RESULT KR ON O.ID = KR.OBJECTIVE_ID
         LEFT JOIN CHECK_IN C ON KR.ID = C.KEY_RESULT_ID AND C.MODIFIED_ON = (SELECT MAX(CC.MODIFIED_ON)
                                                                              FROM CHECK_IN CC
                                                                              WHERE CC.KEY_RESULT_ID = C.KEY_RESULT_ID);