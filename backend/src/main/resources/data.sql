ALTER SEQUENCE sequence_team RESTART WITH 1000;
ALTER SEQUENCE sequence_person RESTART WITH 1000;
ALTER SEQUENCE sequence_quarter RESTART WITH 1000;
ALTER SEQUENCE sequence_objective RESTART WITH 1000;
ALTER SEQUENCE sequence_key_result RESTART WITH 1000;
ALTER SEQUENCE sequence_measure RESTART WITH 1000;

insert into team(id, name) values
        (1, 'Team 1'),
        (2, 'Team 2'),
        (3, 'Team 3');

insert into person(id, username, firstname, lastname, email) values
        (1, 'alice', 'Alice', 'Wunderland', 'wunderland@puzzle.ch'),
        (2, 'bob', 'Bob', 'Baumeister', 'baumeister@puzzle.ch'),
        (3, 'findus', 'Findus', 'Peterson', 'peterson@puzzle.ch'),
        (4, 'paco', 'Paco', 'Egiman', 'egiman@puzzle.ch'),
        (5, 'robin', 'Robin', 'Papierer', 'papierer@puzzle.ch');

insert into quarter(id, year, number) values
        (1, '2022', 3),
        (2, '2021', 4),
        (3, '2020', 1);

insert into objective(id, title, owner_id, team_id, quarter_id, description, progress, created_on) values
        (1, 'Objective 1', 1, 1, 1, 'This is the description of Objective 1', '20.0', '2022-01-01'),
        (2, 'Objective 2', 2, 2, 2, 'This is the description of Objective 2', '60.0', '2019-01-01'),
        (3, 'Objective 3', 4, 3, 3, 'This is the description of Objective 3', '80.0', '2020-01-01');

insert into key_result(id, objective_id, title, description, owner_id, quarter_id, expected_evolution, unit, basis_value, target_value, created_by_id, created_on) values
        (1, 1, 'Keyresult 1', 'This is the description of Keyresult 1', 1, 1, 0, 0, 0, 100, 1, '2022-01-01'),
        (2, 1, 'Keyresult 2', 'This is the description of Keyresult 2', 1, 1, 1, 1, 10, 50, 1, '2022-01-01'),
        (3, 1, 'Keyresult 3', 'This is the description of Keyresult 3', 1, 1, 2, 2, 0, 100, 1, '2022-01-01'),
        (4, 1, 'Keyresult 4', 'This is the description of Keyresult 4', 1, 1, 2, 3, 0, 1, 1, '2022-01-01'),
        (5, 2, 'Keyresult 5', 'This is the description of Keyresult 5', 1, 1, 2, 2, 0, 100, 2, '2022-01-01'),
        (6, 2, 'Keyresult 6', 'This is the description of Keyresult 6', 1, 1, 2, 2, 0, 100, 2, '2022-01-01');

insert into measure(id, key_result_id, value, change_info, initiatives, created_by_id, created_on) values
        (1, 1, 60, '', '', 1, '2022-01-01'),
        (2, 1, 30, '', '', 2, '2021-01-01'),
        (3, 2, 10, '', '', 1, '2022-01-01'),
        (4, 2, 15, '', '', 1, '2023-01-01'),
        (5, 3, 85, '', '', 4, '2022-01-01'),
        (6, 4, 90, '', '', 5, '2022-01-01');
