SET REFERENTIAL_INTEGRITY FALSE;

truncate table check_in;
truncate table key_result;
truncate table objective;
truncate table person;
truncate table quarter;
truncate table team;

SET REFERENTIAL_INTEGRITY TRUE;

ALTER SEQUENCE sequence_team RESTART WITH 200;
ALTER SEQUENCE sequence_person RESTART WITH 200;
ALTER SEQUENCE sequence_quarter RESTART WITH 200;
ALTER SEQUENCE sequence_objective RESTART WITH 200;
ALTER SEQUENCE sequence_key_result RESTART WITH 200;
ALTER SEQUENCE sequence_check_in RESTART WITH 200;

insert into person (id, email, firstname, lastname, username)
values (1, 'peggimann@puzzle.ch', 'Paco', 'Eggimann', 'peggimann'),
       (11, 'wunderland@puzzle.ch', 'Alice', 'Wunderland', 'alice'),
       (21, 'baumeister@puzzle.ch', 'Bob', 'Baumeister', 'bob'),
       (31, 'peterson@puzzle.ch', 'Findus', 'Peterson', 'findus'),
       (41, 'egiman@puzzle.ch', 'Paco', 'Egiman', 'paco'),
       (51, 'papierer@puzzle.ch', 'Robin', 'Papierer', 'robin');

insert into quarter (id, label, start_date, end_date)
values (1, 'GJ 22/23-Q4', '2023-04-01', '2023-06-30'),
       (2, 'GJ 23/24-Q1', '2023-07-01', '2023-09-30'),
       (3, 'GJ 22/23-Q3', '2023-01-01', '2023-03-31'),
       (4, 'GJ 22/23-Q2', '2022-10-01', '2022-12-31'),
       (5, 'GJ 22/23-Q1', '2022-07-01', '2022-09-30'),
       (6, 'GJ 21/22-Q4', '2022-04-01', '2022-06-30'),
       (7, 'GJ 23/24-Q2', '2023-10-01', '2023-12-31');

insert into team (id, name)
values (4, '/BBT'),
       (8, 'we are cube.³'),
       (5, 'Puzzle ITC'),
       (6, 'LoremIpsum');

insert into public.objective (id, description, modified_on, progress, title, created_by_id, quarter_id, team_id, state,
                              modified_by_id, created_on)
values (4, '', '2023-07-25 08:17:51.309958', 66, 'Build a company culture that kills the competition.', 1, 2, 5,
        'ONGOING', null, '2023-07-25 08:17:51.309958'),
       (3,
        'Die Konkurenz nimmt stark zu, um weiterhin einen angenehmen Markt bearbeiten zu können, wollen wir die Kundenzufriedenheit steigern. ',
        '2023-07-25 08:13:48.768262', 84, 'Wir wollen die Kundenzufriedenheit steigern', 1, 2, 5, 'ONGOING', null,
        '2023-07-25 08:13:48.768262'),
       (6, '', '2023-07-25 08:26:46.982010', 25,
        'Als BBT wollen wir den Arbeitsalltag der Members von Puzzle ITC erleichtern.', 1, 2, 4, 'ONGOING', null,
        '2023-07-25 08:26:46.982010'),
       (5, 'Damit wir nicht alle anderen Entwickler stören wollen wir so leise wie möglich arbeiten',
        '2023-07-25 08:20:36.894258', 65, 'Wir wollen das leiseste Team bei Puzzle sein', 1, 2, 4, 'ONGOING', null,
        '2023-07-25 08:20:36.894258'),
       (9, '', '2023-07-25 08:39:45.752126', 88,
        'At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.',
        1, 2, 6, 'ONGOING', null, '2023-07-25 08:39:45.752126'),
       (10, '', '2023-07-25 08:39:45.772126', 88,
        'should not appear on staging, no sea takimata sanctus est Lorem ipsum dolor sit amet.', 1, 2, 6, 'ONGOING',
        null, '2023-07-25 08:39:45.772126'),
       (8, '', '2023-07-25 08:39:28.175703', 40,
        'consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua',
        1, 2, 6, 'ONGOING', null, '2023-07-25 08:39:28.175703');

insert into key_result (id, baseline, description, modified_on, stretch_goal, title, created_by_id,
                        objective_id, owner_id, key_result_type, created_on, unit, commit_zone, target_zone, stretch_zone)
values  (10, 465, '', '2023-07-25 08:23:02.273028', 60, 'Im Durchschnitt soll die Lautstärke 60dB nicht überschreiten', 1, 5, 1, 'metric', '2023-07-25 08:23:02.273028', 'PT', null, null, null),
    (8, 213425, '', '2023-07-25 08:19:44.351252', 80, 'High employee satisfaction scores (80%+) throughout the year.', 1, 4, 1, 'metric', '2023-07-25 08:19:44.351252', 'PT', null, null, null),
    (7, 84, '', '2023-07-25 08:19:13.569300', 4, 'Monthly town halls between our people and leadership teams over the next four months.', 1, 4, 1, 'metric', '2023-07-25 08:19:13.569300', 'PT', null, null, null),
    (6, 5, '', '2023-07-25 08:18:44.087674', 1, 'New structure that rewards funny guys and innovation before the end of Q1. ', 1, 4, 1, 'metric', '2023-07-25 08:18:44.087674', 'PT', null, null, null),
    (15, 0, 'asf', '2023-07-25 08:40:49.412684', 1, ' Lorem ipsum dolor sit amet', 1, 9, 1, 'metric', '2023-07-25 08:40:49.412684', 'PT', null, null, null),
    (5, null, '', '2023-07-25 08:16:24.466383', null, 'Kundenzufriedenheitsumfrage soll mindestens einmal pro 2 Wochen durchgeführt werden. ', 1, 3, 1, 'ordinal', '2023-07-25 08:16:24.466383', 'null', 'Fisch', 'Hai', 'MEG'),
    (12, 0, '', '2023-07-25 08:28:45.110759', 80, 'Wir wollen bereits nach Q1  rund 80% des Redesigns vom OKR-Tool abgeschlossen haben. ', 1, 6, 1, 'metric', '2023-07-25 08:28:45.110759', 'PT', null, null, null),
    (4, null, '', '2023-07-25 08:15:18.244565', null, 'Antwortzeit für Supportanfragen um 33% verkürzen.', 1, 3, 1, 'ordinal', '2023-07-25 08:15:18.244565', 'null', 'Pflanze', 'Baum', 'Wald'),
    (13, 43, '', '2023-07-25 08:29:57.709926', 15, 'Jedes Woche wird ein Kuchen vom BBT für Puzzle Organisiert', 1, 6, 1, 'metric', '2023-07-25 08:29:57.709926', 'PT', null, null, null),
    (3, 0, '', '2023-07-25 08:14:31.964115', 25, 'Steigern der URS um 25%', 1, 3, 1, 'metric', '2023-07-25 08:14:31.964115', 'PT', null, null, null),
    (14, 0, '', '2023-07-25 08:31:39.249943', 20, 'Das BBT hilft den Membern 20% mehr beim Töggelen', 1, 6, 1, 'metric', '2023-07-25 08:31:39.249943', 'PT', null, null, null),
    (16, 200, '', '2023-07-25 08:41:24.592007', 1, 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.', 1, 9, 1, 'metric', '2023-07-25 08:41:24.592007', 'PT', null, null, null),
    (19, 50, 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lore', '2023-07-25 08:42:56.407125', 1, 'nsetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At ', 1, 8, 1, 'metric', '2023-07-25 08:42:56.407125', 'PT', null, null, null),
    (17, 525, 'asdf', '2023-07-25 08:41:52.844903', 20000000, 'vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lore', 1, 9, 1, 'metric', '2023-07-25 08:41:52.844903', 'PT', null, null, null),
    (9, 100, '', '2023-07-25 08:48:45.825328', 80, 'Die Member des BBT reduzieren Ihre Lautstärke um 20%', 1, 5, 1, 'metric', '2023-07-25 08:48:45.825328', 'PT', null, null, null),
    (18, 0, 'consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lore', '2023-07-25 08:42:24.779721', 1, 'Lorem', 1, 8, 1, 'metric', '2023-07-25 08:42:24.779721', 'PT', null, null, null);

insert into check_in (id, change_info, created_on, initiatives, modified_on, value_metric, created_by_id, key_result_id,
                      confidence, check_in_type, value_ordinal)
values (1,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam',
        '2023-07-25 08:44:13.865976', '', '2023-07-24 22:00:00.000000', 77, 1, 8, 5, 'metric', null),
       (2,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:44:35.998776', '', '2023-07-25 22:00:00.000000', 89, 1, 8, 5, 'metric', null),
       (3,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:44:54.832400', '', '2023-07-24 22:00:00.000000', 1, 1, 7, 5, 'metric', null),
       (4,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:45:07.911215',
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 22:00:00.000000', 7, 1, 7, 5, 'metric', null),
       (5,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:45:25.583267',
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-24 22:00:00.000000', 3, 1, 6, 5, 'metric', null),
       (6,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:45:42.707340',
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-24 22:00:00.000000', 3, 1, 5, 5, 'metric', null),
       (7,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:45:57.304875', '', '2023-07-25 22:00:00.000000', 2, 1, 5, 5, 'metric', null),
       (8,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:46:21.358930',
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-24 22:00:00.000000', 70, 1, 4, 5, 'metric', null),
       (9,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:46:39.204525',
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-24 22:00:00.000000', 16, 1, 3, 5, 'metric', null),
       (10,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:47:01.649202',
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-24 22:00:00.000000', 10, 1, 14, 5, 'metric', null),
       (11,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:47:22.341767', 'Mehr Kuchen', '2023-07-24 22:00:00.000000', 1, 1, 13, 5, 'metric', null),
       (12,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:47:38.435770',
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-24 22:00:00.000000', 20, 1, 12, 5, 'metric', null),
       (14,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:48:08.440951',
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-24 22:00:00.000000', 50, 1, 10, 5, 'metric', null),
       (15,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:48:21.822399',
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-24 22:00:00.000000', 81, 1, 9, 5, 'metric', null),
       (16,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:49:11.811674', '', '2023-07-24 22:00:00.000000', 15000, 1, 17, 5, 'metric', null),
       (17,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:49:32.030171',
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-24 22:00:00.000000', 66.7, 1, 16, 5, 'metric', null),
       (18,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:49:56.975649',
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-24 22:00:00.000000', 99, 1, 15, 5, 'metric', null),
       (19,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:50:19.024254',
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-24 22:00:00.000000', 35, 1, 19, 5, 'metric', null),
       (20,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:50:44.059020',
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-24 22:00:00.000000', 0.5, 1, 18, 5, 'metric', null);


