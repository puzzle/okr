ALTER SEQUENCE sequence_team RESTART WITH 200;
ALTER SEQUENCE sequence_person RESTART WITH 200;
ALTER SEQUENCE sequence_quarter RESTART WITH 200;
ALTER SEQUENCE sequence_objective RESTART WITH 200;
ALTER SEQUENCE sequence_key_result RESTART WITH 200;
ALTER SEQUENCE sequence_measure RESTART WITH 200;

insert into team (id, name)
values (1, 'Puzzle ITC'),
       (2, 'Team 2'),
       (3, 'Team 3');


insert into person (id, email, firstname, lastname, username)
values (1, 'wunderland@puzzle.ch', 'Alice', 'Wunderland', 'alice'),
       (2, 'baumeister@puzzle.ch', 'Bob', 'Baumeister', 'bob'),
       (3, 'peterson@puzzle.ch', 'Findus', 'Peterson', 'findus'),
       (4, 'egiman@puzzle.ch', 'Paco', 'Egiman', 'paco'),
       (5, 'papierer@puzzle.ch', 'Robin', 'Papierer', 'robin'),
       (100, 'peggimann@puzzle.ch', 'Paco', 'Eggimann', 'peggimann');


insert into quarter (id, label)
values (1, 'GJ 22/23-Q3'),
       (2, 'GJ 22/23-Q2'),
       (3, 'GJ 22/23-Q1'),
       (100, 'GJ 22/23-Q4'),
       (101, 'GJ 23/24-Q1'),
       (102, 'GJ 21/22-Q4');


insert into objective (id, modified_on, description, progress, title, owner_id, quarter_id, team_id)
values (1, '2022-07-01 00:00:00.000000', 'Puzzle ITC will motivierte und glückliche Members.', 43,
        'Motivierte und glückliche Members', 1, 3, 1),
       (2, '2019-01-01 00:00:00.000000', 'This is the description of Objective 2', 5, 'Objective 2', 2, 3, 2),
       (3, '2020-01-01 00:00:00.000000', 'This is the description of Objective 3', 0, 'Objective 3', 4, 3, 3),
       (4, '2022-10-01 00:00:00.000000', 'Puzzle ITC will motivierte und glückliche Members.', 73,
        'Motivierte und glückliche Members', 1, 2, 1),
       (5, '2023-01-01 00:00:00.000000', 'Puzzle ITC will motivierte und glückliche Members.', 65,
        'Motivierte und glückliche Members', 1, 1, 1),
       (100, '2023-04-24 11:22:05.862100', '', null, 'Testq', 100, 100, 1),
       (102, '2023-04-24 11:23:49.081449', 'test', null, 'Mit diesem Objectiv können wir den scroll and back testen.',
        100, 100, 2),
       (103, '2023-04-24 11:24:02.300549', 'adsf', null,
        'Dies ist der Titel des Objectives, dieser Titel ist absichtlich mühsam lange zum lesen in einem kleinen Feld.',
        100, 100, 2),
       (101, '2023-04-24 11:23:23.577257', 'Eine kleine Beschreibung', 0,
        'Ein weiteres Objective zum messen ob ein Nullemblem auch rot eingefärbt wird', 100, 100, 1);



insert into key_result (id, basis_value, modified_on, description, expected_evolution, target_value, title, unit,
                        created_by_id, objective_id, owner_id)
values (1, 0, '2022-08-01 00:00:00.000000',
        'Auf allen drei Stockwerke sollten 4 verschiedene Schokoladenarten angeboten werden.', 1, 4,
        'Reichlich Schokolade', 2, 1, 1, 5),
       (2, 0, '2022-08-01 00:00:00.000000', 'In jedem Monat sollten 2 Apéros für alle Members durchgeführt werden.', 2,
        2, 'Regelmässige Apéros', 2, 1, 1, 1),
       (3, 0, '2022-08-01 00:00:00.000000', 'Die Members sollen spannende und interessante Arbeit erhalten.', 2, 100,
        'Spannende Arbeit', 0, 1, 1, 3),
       (5, 0, '2022-01-01 00:00:00.000000',
        'This is the description of Keyresult 5: Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry''s standard dummy text ever since the 1500s',
        2, 100, 'Keyresult 5', 2, 2, 2, 1),
       (6, 0, '2022-01-01 00:00:00.000000',
        'This is the description of Keyresult 6: Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry''s standard dummy text ever since the 1500s',
        2, 100, 'Keyresult 6', 2, 2, 2, 1),
       (7, 0, '2022-10-01 00:00:00.000000',
        'Auf allen drei Stockwerke sollten 4 verschiedene Schokoladenarten angeboten werden.', 1, 4,
        'Reichlich Schokolade', 2, 1, 4, 5),
       (8, 0, '2022-10-01 00:00:00.000000', 'In jedem Monat sollten 2 Apéros für alle Members durchgeführt werden.', 2,
        2, 'Regelmässige Apéros', 2, 1, 4, 1),
       (9, 0, '2022-10-01 00:00:00.000000', 'Die Members sollen spannende und interessante Arbeit erhalten.', 2, 100,
        'Spannende Arbeit', 0, 1, 4, 3),
       (11, 0, '2023-01-01 00:00:00.000000',
        'Auf allen drei Stockwerke sollten 4 verschiedene Schokoladenarten angeboten werden.', 1, 4,
        'Reichlich Schokolade', 2, 1, 5, 5),
       (12, 0, '2023-01-01 00:00:00.000000', 'In jedem Monat sollten 2 Apéros für alle Members durchgeführt werden.', 2,
        2, 'Regelmässige Apéros', 2, 1, 5, 1),
       (13, 0, '2023-01-01 00:00:00.000000', 'Die Members sollen spannende und interessante Arbeit erhalten.', 2, 100,
        'Spannende Arbeit', 0, 1, 5, 3),
       (100, null, '2023-04-24 11:26:04.757432', 'asdf', 4, 10, 'Keyresult Titel zum testen des einfärbens', 0, 100,
        101, 100),
       (101, 0, '2023-04-24 11:26:27.098339', '', 0, 100, 'Ein weiteres Keyresult ', 0, 100, 101, 100);


insert into measure (id, change_info, created_on, initiatives, measure_date, value, created_by_id, key_result_id)
values (1, 'Bei dieser Messung standen 2 Schokolandenarten auf allen Stockwerken bereit.', '2022-08-01 00:00:00.000000',
        'Mehr vom einfachen Milch- und Nuss-Schokolade einkaufen.', '2022-09-01 00:00:00.000000', 2, 1, 1),
       (2, 'Leider fand in diesem Monat kein Apéro statt.', '2022-08-01 00:00:00.000000',
        'Sofort die Termine für den nächsten Monat bestimmen.', '2022-09-03 00:00:00.000000', 0, 1, 2),
       (3, 'Ein grossteil der Members haben uns ihre Einschätzung gemeldet.', '2022-08-01 00:00:00.000000',
        'Vielleicht sollten wir  die Umfrage per E-Mail versenden.', '2022-09-05 00:00:00.000000', 80, 4, 3),
       (5, 'Uns sind drei Schokoladenarten ausgegangen.', '2022-10-01 00:00:00.000000',
        'Wir müssen regelmässig einkaufen gehen.', '2022-10-02 00:00:00.000000', 1, 1, 7),
       (6,
        'Wir hatten zumindest ein Apéro durchgeführt. Das zweite Apéro musste wegen Terminkonflikte abgesagt werden.',
        '2022-10-01 00:00:00.000000', 'Termine mit den Bereichen koordinieren.', '2022-10-03 00:00:00.000000', 1, 1, 8),
       (7, 'Fast alle Members haben uns ihre Einschätzung gemeldet.', '2022-10-01 00:00:00.000000',
        'Brauchen nichts zu ändern.', '2022-10-03 00:00:00.000000', 70, 4, 9),
       (9, 'Uns ist schon wieder eine Schokoladenart ausgegangen.', '2022-10-01 00:00:00.000000',
        'Wir müssen wirklich regelmässig einkaufen gehen!', '2022-11-02 00:00:00.000000', 3, 1, 7),
       (10, 'Ja zum Jahresende haben wir es geschafft.', '2022-10-01 00:00:00.000000',
        'Aktuell keine Massnahmen nötig.', '2022-12-02 00:00:00.000000', 4, 1, 7),
       (11, 'Das neue Jahr fängt ja gut an: keine Schokolade!', '2023-01-01 00:00:00.000000',
        'Wir sollten das Jahr 2023 auslasen.', '2023-01-02 00:00:00.000000', 0, 1, 11),
       (12, 'Wir haben sogar 3 Apéros gemacht.', '2022-08-01 00:00:00.000000', 'Weiter so.',
        '2023-01-03 00:00:00.000000', 3, 1, 12),
       (13, 'Fast alle Members haben uns ihre Feedback gemeldet.', '2022-08-01 00:00:00.000000', 'Keine Massnahmen',
        '2023-01-03 00:00:00.000000', 95, 4, 13),
       (15, '', '2022-08-01 00:00:00.000000', 'Initiatives einer Messung', '2023-02-04 00:00:00.000000', 10, 5, 6),
       (16, '', '2022-08-01 00:00:00.000000', 'Initiatives einer Messung', '2023-01-04 00:00:00.000000', 1, 5, 6),
       (100, 'Nullmessung', '2023-04-24 11:26:51.731592', '', '2023-04-24 00:00:00.000000', 9.999, 100, 100);
