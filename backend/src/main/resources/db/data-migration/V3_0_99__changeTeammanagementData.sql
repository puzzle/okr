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

INSERT INTO team (id, name, version)
VALUES (10, CONCAT('just_', current_schema), 1);

DO $$
    BEGIN
        IF current_schema = 'okr_pitc' THEN
            INSERT INTO team (id, name, version)
            VALUES (11, 'Mid', 1),
                    (12, 'Ruby', 1),
                   (13, 'Mobility', 1),
                   (14, 'sys', 1);
            INSERT INTO person (id, email, firstname, lastname, is_okr_champion, version)
            VALUES
                (100, 'bbt@bbt.com', 'Ashleigh', 'Russell', false, 1),
                (101, 'bl@bl.com', 'Esha', 'Harris', false, 1),
                (102, 'bl@mid.com', 'BL', 'Mid', false, 1),
                (103, 'bl@mob.com', 'BL', 'Mobility', false, 1),
                (104, 'bl@ruby.com', 'BL', 'Ruby', false, 1),
                (105, 'bl@sys.com', 'BL', 'Sys', false, 1),
                (106, 'gl@gl.com', 'Jaya', 'Norris', true, 1),
                (107, 'member@member.com', 'Abraham', 'Woodard', false, 1);

            insert into person_team (id, person_id, team_id, is_team_admin, version)
            values (9, 100, 4, true,1 ), --BBT
                   (10, 101, 10, true,1 ),-- just acme
                   (11, 102, 11, true,1 ), -- MID
                   (12, 103, 12, true,1 ), -- Mobility
                   (13, 104, 13, true,1 ), -- Ruby
                   (14, 105, 14, true,1 ), -- Sys
                   (15, 106, 5, true,1 ), --Puzzle ITC
                   (16, 107, 5, false,1 ), --Puzzle ITC, member
                   (17, 107, 4, false,1 ); --bbt, member


        END IF;
    END $$;

DO $$
    BEGIN
        IF current_schema = 'okr_acme' THEN
            INSERT INTO person (id, email, firstname, lastname, is_okr_champion, version)
            VALUES
                (100, 'bl@acme.com', 'Esha', 'Harris', false, 1),
                (101, 'gl@acme.com', 'Jaya', 'Norris', true, 1),
                (102, 'member@acme.com', 'Abraham', 'Woodard', false, 1);

            insert into person_team (id, person_id, team_id, is_team_admin, version)
            values (9, 100, 10, true,1 ), --BBT
                   (10, 101, 5, true,1 ),-- Puzzle ITC
                   (11, 102, 5, false,1 ), -- Puzzle ITC, member
                   (12, 102, 10, false,1 ); -- BBT, member

        END IF;
    END $$;



