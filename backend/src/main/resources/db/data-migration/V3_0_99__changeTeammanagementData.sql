-- add gl@gl.com as a user. we set it as champion in dev application properties
/*
INSERT INTO person (id, email, firstname, lastname, version, is_okr_champion)
VALUES (61, 'gl@gl.com', 'Jaya', 'Norris', 1, FALSE);
*/
-- map existing users to teams
INSERT INTO person_team (id, version, person_id, team_id, is_team_admin)
        -- peggimann@puzzle.ch
VALUES (1, 1, 1, 4, TRUE),
       (2, 1, 1, 5, FALSE),
       -- wunderland@puzzle.ch
       (3, 1, 11, 6, FALSE),
       -- baumeister@puzzle.ch
       (4, 1, 21, 8, FALSE),
       -- peterson@puzzle.ch
       (5, 1, 31, 8, TRUE),
       -- egiman@puzzlech
       (6, 1, 41, 4, FALSE),
       -- papierer@puzzle.ch
       (7, 1, 51, 6, TRUE)
       -- gl@gl.ch
  --     (8, 1, 61, 5, TRUE),
  --     (9, 1, 61, 6, FALSE)
;

ALTER SEQUENCE sequence_person_team RESTART WITH 1000;