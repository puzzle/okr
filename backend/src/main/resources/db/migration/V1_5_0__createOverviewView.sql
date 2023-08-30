DROP VIEW IF EXISTS OVERVIEW;
CREATE VIEW OVERVIEW AS
SELECT T.ID                AS "TEAM_ID",
       T.NAME              AS "TEAM_NAME",
       COALESCE(O.ID, -1)  AS "OBJECTIVE_ID",
       O.TITLE             AS "OBJECTIVE_TITLE",
       O.PROGRESS,
       Q.ID                AS "QUARTER_ID",
       Q.LABEL             AS "QUARTER_LABEL",
       COALESCE(KR.ID, -1) AS "KEY_RESULT_ID",
       KR.TITLE            AS "KEY_RESULT_TITLE",
       KR.UNIT,
       KR.BASIS_VALUE,
       KR.TARGET_VALUE,
       COALESCE(M.ID, -1)  AS "MEASURE_ID",
       M."value"           AS "MEASURE_VALUE",
       M.MEASURE_DATE,
       M.CREATED_ON
FROM TEAM T
         LEFT JOIN OBJECTIVE O ON T.ID = O.TEAM_ID
         LEFT JOIN QUARTER Q ON O.QUARTER_ID = Q.ID
         LEFT JOIN KEY_RESULT KR ON O.ID = KR.OBJECTIVE_ID
         LEFT JOIN MEASURE M ON KR.ID = M.KEY_RESULT_ID AND M.MEASURE_DATE = (SELECT MAX(MM.MEASURE_DATE)
                                                                              FROM MEASURE MM
                                                                              WHERE MM.KEY_RESULT_ID = M.KEY_RESULT_ID)
;