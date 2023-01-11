ALTER SEQUENCE sequence_team RESTART WITH 1000;
ALTER SEQUENCE sequence_person RESTART WITH 1000;
ALTER SEQUENCE sequence_quarter RESTART WITH 1000;
ALTER SEQUENCE sequence_objective RESTART WITH 1000;
ALTER SEQUENCE sequence_key_result RESTART WITH 1000;
ALTER SEQUENCE sequence_measure RESTART WITH 1000;

insert into team(id, name) values
        (1, 'Puzzle ITC'),
        (2, 'Team 2'),
        (3, 'Team 3');

insert into person(id, username, firstname, lastname, email) values
        (1, 'alice', 'Alice', 'Wunderland', 'wunderland@puzzle.ch'),
        (2, 'bob', 'Bob', 'Baumeister', 'baumeister@puzzle.ch'),
        (3, 'findus', 'Findus', 'Peterson', 'peterson@puzzle.ch'),
        (4, 'paco', 'Paco', 'Egiman', 'egiman@puzzle.ch'),
        (5, 'robin', 'Robin', 'Papierer', 'papierer@puzzle.ch');

insert into quarter(id, label) values
        (1, 'GJ 22/23-Q3'),
        (2, 'GJ 22/23-Q2'),
        (3, 'GJ 22/23-Q1');

--  quartal GJ 22/23-Q1 (1.7.2022 - 30.9.2022)
insert into objective(id, title, owner_id, team_id, quarter_id, progress, created_on, description) values
        (1, 'Motivierte und glückliche Members', 1, 1, 1, '61', '2022-07-01', 'Puzzle ITC will motivierte und glückliche Members.'),
        (2, 'Objective 2', 2, 2, 2, '0', '2019-01-01', 'This is the description of Objective 2'),
        (3, 'Objective 3', 4, 3, 3, '0', '2020-01-01', 'This is the description of Objective 3');

insert into key_result(id, objective_id, owner_id, expected_evolution, unit, basis_value, target_value, created_by_id, created_on, title, description) values
        (1, 1, 5, 1, 2, 0, 4, 1, '2022-08-01', 'Reichlich Schokolade', 'Auf allen drei Stockwerke sollten 4 verschiedene Schokoladenarten angeboten werden.'),
        (2, 1, 1, 2, 2, 0, 2, 1, '2022-08-01', 'Regelmässige Apéros', 'In jedem Monat sollten 2 Apéros für alle Members durchgeführt werden.'),
        (3, 1, 3, 2, 0, 0, 100, 1, '2022-08-01', 'Spannende Arbeit', 'Die Members sollen spannende und interessante Arbeit erhalten.'),
        (4, 1, 2, 2, 3, 0, 1, 1, '2022-08-01', 'Dienstag: Cordon bleu', 'Am Dienstag bietet der Mattenhof immer Keller Cordon bleu an.'),
        (5, 2, 1, 2, 2, 0, 100, 2, '2022-01-01', 'Keyresult 5', 'This is the description of Keyresult 5: Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry''s standard dummy text ever since the 1500s'),
        (6, 2, 1, 2, 2, 0, 100, 2, '2022-01-01', 'Keyresult 6', 'This is the description of Keyresult 6: Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry''s standard dummy text ever since the 1500s');

insert into measure(id, key_result_id, measure_date, value, created_by_id, created_on, change_info, initiatives) values
        (1, 1,'2022-09-01', 2, 1, '2022-08-01', 'Bei dieser Messung standen 2 Schokolandenarten auf allen Stockwerken bereit.', 'Mehr vom einfachen Milch- und Nuss-Schokolade einkaufen.'),
        (2, 2,'2022-09-03', 0, 1, '2022-08-01', 'Leider fand in diesem Monat kein Apéro statt.', 'Sofort die Termine für den nächsten Monat bestimmen.'),
        (3, 3,'2022-09-05', 80, 4, '2022-08-01', 'Ein grossteil der Members haben uns ihre Einschätzung gemeldet.', 'Vielleicht sollten wir  die Umfrage per E-Mail versenden.'),
        (4, 4,'2022-09-05', 1, 5, '2022-08-01', 'Der Mattenhof Keller hat es erfüllt.', 'Wir überwachen die Menükarte zur Sicherheit');

--  quartal GJ 22/23-Q2 (1.10.2022 - 31.12.2022)
insert into objective(id, title, owner_id, team_id, quarter_id, progress, created_on, description) values
        (4, 'Motivierte und glückliche Members', 1, 1, 1, '61', '2022-10-01', 'Puzzle ITC will motivierte und glückliche Members.');

insert into key_result(id, objective_id, owner_id, expected_evolution, unit, basis_value, target_value, created_by_id, created_on, title, description) values
        (7, 4, 5, 1, 2, 0, 4, 1, '2022-10-01', 'Reichlich Schokolade', 'Auf allen drei Stockwerke sollten 4 verschiedene Schokoladenarten angeboten werden.'),
        (8, 4, 1, 2, 2, 0, 2, 1, '2022-10-01', 'Regelmässige Apéros', 'In jedem Monat sollten 2 Apéros für alle Members durchgeführt werden.'),
        (9, 4, 3, 2, 0, 0, 100, 1, '2022-10-01', 'Spannende Arbeit', 'Die Members sollen spannende und interessante Arbeit erhalten.'),
        (10, 4, 2, 2, 3, 0, 1, 1, '2022-10-01', 'Dienstag: Cordon bleu', 'Am Dienstag bietet der Mattenhof immer Keller Cordon bleu an.');

insert into measure(id, key_result_id, measure_date, value, created_by_id, created_on, change_info, initiatives) values
        (5, 7,'2022-10-02', 1, 1, '2022-10-01', 'Uns sind drei Schokoladenarten ausgegangen.', 'Wir müssen regelmässig einkaufen gehen.'),
        (6, 8,'2022-10-03', 1, 1, '2022-10-01', 'Wir hatten zumindest ein Apéro durchgeführt. Das zweite Apéro musste wegen Terminkonflikte abgesagt werden.', 'Termine mit den Bereichen koordinieren.'),
        (7, 9,'2022-10-03', 70, 4, '2022-10-01', 'Fast alle Members haben uns ihre Einschätzung gemeldet.', 'Brauchen nichts zu ändern.'),
        (8, 10,'2022-10-04', 1, 5, '2022-10-01', 'Es gab immer Cordon bleu am Dienstag', 'Aktuell kein Handlungsbedarf'),
        (9, 7,'2022-11-02', 3, 1, '2022-10-01', 'Uns ist schon wieder eine Schokoladenart ausgegangen.', 'Wir müssen wirklich regelmässig einkaufen gehen!'),
        (10, 7,'2022-12-02', 4, 1, '2022-10-01', 'Ja zum Jahresende haben wir es geschafft.', 'Aktuell keine Massnahmen nötig.');

--  quartal GJ 22/23-Q3 (1.1.2023 - 31.3.2023)
insert into objective(id, title, owner_id, team_id, quarter_id, progress, created_on, description) values
        (5, 'Motivierte und glückliche Members', 1, 1, 1, '61', '2023-01-01', 'Puzzle ITC will motivierte und glückliche Members.');

insert into key_result(id, objective_id, owner_id, expected_evolution, unit, basis_value, target_value, created_by_id, created_on, title, description) values
        (11, 5, 5, 1, 2, 0, 4, 1, '2023-01-01', 'Reichlich Schokolade', 'Auf allen drei Stockwerke sollten 4 verschiedene Schokoladenarten angeboten werden.'),
        (12, 5, 1, 2, 2, 0, 2, 1, '2023-01-01', 'Regelmässige Apéros', 'In jedem Monat sollten 2 Apéros für alle Members durchgeführt werden.'),
        (13, 5, 3, 2, 0, 0, 100, 1, '2023-01-01', 'Spannende Arbeit', 'Die Members sollen spannende und interessante Arbeit erhalten.'),
        (14, 5, 2, 2, 3, 0, 1, 1, '2023-01-01', 'Dienstag: Cordon bleu', 'Am Dienstag bietet der Mattenhof immer Keller Cordon bleu an.');

insert into measure(id, key_result_id, measure_date, value, created_by_id, created_on, change_info, initiatives) values
        (11, 11,'2023-01-02', 0, 1, '2023-01-01', 'Das neue Jahr fängt ja gut an: keine Schokolade!', 'Wir sollten das Jahr 2023 auslasen.'),
        (12, 12,'2023-01-03', 3, 1, '2022-08-01', 'Wir haben sogar 3 Apéros gemacht.', 'Weiter so.'),
        (13, 13,'2023-01-03', 95, 4, '2022-08-01', 'Fast alle Members haben uns ihre Feedback gemeldet.', 'Keine Massnahmen'),
        (14, 14,'2023-01-04', 1, 5, '2022-08-01', 'Mattenhof Keller hat versagt: Ein Dienstag ohne Cordon bleu.', 'Zur Sicherheit 10 auf Reserve haben.');
