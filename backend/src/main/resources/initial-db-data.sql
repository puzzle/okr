ALTER SEQUENCE sequence_team RESTART WITH 1000;

insert into team(id, name) values
    (1, 'Team 1'),
    (2, 'Team 2'),
    (3, 'Team 3');