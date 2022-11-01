ALTER SEQUENCE sequence_team RESTART WITH 1000;

insert into team(id, name)
values (1, 'Team 1'),
       (2, 'Team 2'),
       (3, 'Team 3');

insert into objectives(id, title, owner_id, team_id, quarter_id,
                       description, progress, createdBy, createdOn)
VALUES (1, 'Objective', 1, 1, 1, 'Some Description', 5, 'marc', current_timestamp)