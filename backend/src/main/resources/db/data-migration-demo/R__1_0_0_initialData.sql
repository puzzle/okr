truncate table alignment cascade;
truncate table action cascade;
truncate table check_in cascade;
truncate table key_result cascade;
truncate table completed cascade;
truncate table objective cascade;
truncate table person cascade;
truncate table quarter cascade;
truncate table team_organisation;
truncate table organisation cascade;
truncate table team cascade;

insert into person (id, email, firstname, lastname, username, version)
values (1, 'peggimann@puzzle.ch', 'Paco', 'Eggimann', 'peggimann', 1),
       (11, 'wunderland@puzzle.ch', 'Alice', 'Wunderland', 'alice', 1),
       (21, 'baumeister@puzzle.ch', 'Bob', 'Baumeister', 'bob', 1),
       (31, 'peterson@puzzle.ch', 'Findus', 'Peterson', 'findus', 1),
       (41, 'egiman@puzzle.ch', 'Paco', 'Egiman', 'paco', 1),
       (51, 'papierer@puzzle.ch', 'Robin', 'Papierer', 'robin', 1);

DO
$$
    DECLARE
        quarter_label text;
        first_date    date;
        last_date     date;
    BEGIN
        -- last quarter
        select concat('GJ ', to_char(date_trunc('quarter', now())::date - 1, 'yyyy-q')) into quarter_label;
        select date_trunc('quarter', date_trunc('quarter', now())::date - 1):: date into first_date;
        select date_trunc('quarter', now())::date - 1 into last_date;
        insert into quarter (id, label, start_date, end_date)
        values (1, quarter_label, first_date, last_date);

        -- current quarter
        select concat('GJ ', to_char(date_trunc('quarter', now()), 'yyyy-q')) into quarter_label;
        select date_trunc('quarter', now()):: date into first_date;
        select date_trunc('quarter', date_trunc('quarter', now())::date + 93)::date - 1 into last_date;
        insert into quarter (id, label, start_date, end_date)
        values (2, quarter_label, first_date, last_date);

        -- next quarter, archive, backlog
        select concat('GJ ', to_char(date_trunc('quarter', now())::date + 93, 'yyyy-q')) into quarter_label;
        select date_trunc('quarter', date_trunc('quarter', now())::date + 93)::date into first_date;
        select date_trunc('quarter', date_trunc('quarter', now())::date + 186)::date - 1 into last_date;
        insert into quarter (id, label, start_date, end_date)
        values (3, quarter_label, first_date, last_date),
               (998, 'Archive', null, null),
               (999, 'Backlog', null, null);
    END
$$;

insert into team (id, name, version)
values (4, '/BBT', 1),
       (5, 'Puzzle ITC', 1),
       (6, 'LoremIpsum', 1),
       (8, 'we are cube.³', 1);

insert into organisation (id, org_name, state, version)
values (1, 'org_bl', 'ACTIVE', 1),
       (2, 'org_de', 'ACTIVE', 1),
       (3, 'org_gl', 'ACTIVE', 1),
       (4, 'org_pl', 'ACTIVE', 1),
       (5, 'org_pv', 'ACTIVE', 1),
       (6, 'org_ux', 'ACTIVE', 1),
       (7, 'org_zh', 'ACTIVE', 1),
       (8, 'org_sys', 'ACTIVE', 1),
       (9, 'org_azubi', 'ACTIVE', 1),
       (10, 'org_de_gl', 'ACTIVE', 1),
       (11, 'org_de_gs', 'ACTIVE', 1),
       (12, 'org_devtre', 'ACTIVE', 1),
       (13, 'org_racoon', 'ACTIVE', 1),
       (14, 'org_tqm_qm', 'ACTIVE', 1),
       (15, 'org_devruby', 'ACTIVE', 1),
       (16, 'org_midcicd', 'ACTIVE', 1),
       (17, 'org_verkauf', 'ACTIVE', 1),
       (18, 'org_finanzen', 'ACTIVE', 1),
       (19, 'org_mobility', 'ACTIVE', 1),
       (20, 'org_personal', 'ACTIVE', 1),
       (21, 'org_security', 'ACTIVE', 1),
       (22, 'org_marketing', 'ACTIVE', 1),
       (23, 'org_openshift', 'ACTIVE', 1),
       (24, 'org_ausbildung', 'ACTIVE', 1),
       (25, 'org_backoffice', 'ACTIVE', 1),
       (26, 'org_branch_sec', 'ACTIVE', 1),
       (27, 'org_standort_zh', 'ACTIVE', 1),
       (28, 'org_midcontainer', 'ACTIVE', 1);

insert into team_organisation (team_id, organisation_id)
values (5, 3),
       (8, 6),
       (4, 9),
       (6, 19),
       (8, 19);

insert into objective (id, description, modified_on, title, created_by_id, quarter_id, team_id, state, modified_by_id,
                       created_on, version)
values (4, '', '2023-07-25 08:17:51.309958', 'Build a company culture that kills the competition.', 1, 2, 5, 'ONGOING',
        1, '2023-07-25 08:17:51.309958', 1),
       (3,
        'Die Konkurenz nimmt stark zu, um weiterhin einen angenehmen Markt bearbeiten zu können, wollen wir die Kundenzufriedenheit steigern. ',
        '2023-07-25 08:13:48.768262', 'Wir wollen die Kundenzufriedenheit steigern', 1, 2, 5, 'ONGOING', 1,
        '2023-07-25 08:13:48.768262', 1),
       (6, '', '2023-07-25 08:26:46.982010',
        'Als BBT wollen wir den Arbeitsalltag der Members von Puzzle ITC erleichtern.', 1, 2, 4, 'ONGOING', 1,
        '2023-07-25 08:26:46.982010', 1),
       (5, 'Damit wir nicht alle anderen Entwickler stören wollen wir so leise wie möglich arbeiten',
        '2023-07-25 08:20:36.894258', 'Wir wollen das leiseste Team bei Puzzle sein', 1, 2, 4, 'NOTSUCCESSFUL', 1,
        '2023-07-25 08:20:36.894258', 1),
       (9, '', '2023-07-25 08:39:45.752126',
        'At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.',
        1, 2, 6, 'NOTSUCCESSFUL', 1, '2023-07-25 08:39:45.752126', 1),
       (10, '', '2023-07-25 08:39:45.772126',
        'should not appear on staging, no sea takimata sanctus est Lorem ipsum dolor sit amet.', 1, 2, 6, 'SUCCESSFUL',
        1, '2023-07-25 08:39:45.772126', 1),
       (8, '', '2023-07-25 08:39:28.175703',
        'consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua',
        1, 2, 6, 'NOTSUCCESSFUL', 1, '2023-07-25 08:39:28.175703', 1);

insert into completed (id, version, objective_id, comment)
values (1, 1, 5, 'Not successful because there were many events this month'),
       (2, 1, 9, 'Was not successful because we were too slow'),
       (3, 1, 8, 'Sadly we had not enough members to complete this objective'),
       (4, 1, 10, 'Objective could be completed fast and easy');

insert into key_result (id, baseline, description, modified_on, stretch_goal, title, created_by_id, objective_id,
                        owner_id, unit, key_result_type, created_on, commit_zone, target_zone, stretch_zone, version)
values (3, 0, '', '2023-07-25 08:14:31.964115', 25, 'Steigern der URS um 25%', 1, 3, 1, 'PERCENT', 'metric',
        '2023-07-25 08:14:31.964115', null, null, null, 1),
       (4, 100, '', '2023-07-25 08:15:18.244565', 67, 'Antwortzeit für Supportanfragen um 33% verkürzen.', 1, 3, 1,
        'PERCENT', 'metric', '2023-07-25 08:15:18.244565', null, null, null, 1),
       (5, null, '', '2023-07-25 08:16:24.466383', 1,
        'Kundenzufriedenheitsumfrage soll mindestens einmal pro 2 Wochen durchgeführt werden. ', 11, 4, 1, 'NUMBER',
        'metric', '2023-07-25 08:16:24.466383', null, null, null, 1),
       (6, null, '', '2023-07-25 08:18:44.087674', 1,
        'New structure that rewards funny guys and innovation before the end of Q1. ', 11, 4, 1, 'NUMBER', 'metric',
        '2023-07-25 08:18:44.087674', null, null, null, 1),
       (7, null, '', '2023-07-25 08:19:13.569300', 4,
        'Monthly town halls between our people and leadership teams over the next four months.', 21, 4, 1, 'NUMBER',
        'metric', '2023-07-25 08:18:44.087674', null, null, null, 1),
       (8, null, '', '2023-07-25 08:19:44.351252', 80,
        'High employee satisfaction scores (80%+) throughout the year.', 11, 5, 1, 'PERCENT', 'metric',
        '2023-07-25 08:18:44.087674', null, null, null, 1),
       (10, null, '', '2023-07-25 08:23:02.273028', 60,
        'Im Durchschnitt soll die Lautstärke 60dB nicht überschreiten', 21, 5, 1, 'NUMBER', 'metric',
        '2023-07-25 08:18:44.087674', null, null, null, 1),
       (11, null, '', '2023-07-25 08:24:10.972984', 2,
        'Mindestens 2 Komplimente für eine angenehme Arbeitslautstärke soll dem Team zugesprochen werden.', 21, 6, 1,
        'NUMBER', 'metric', '2023-07-25 08:18:44.087674', null, null, null, 1),
       (12, 0, '', '2023-07-25 08:28:45.110759', 80,
        'Wir wollen bereits nach Q1 rund 80% des Redesigns vom OKR-Tool abgeschlossen haben. ', 1, 6, 1, 'PERCENT',
        'metric', '2023-07-25 08:18:44.087674', null, null, null, 1),
       (13, null, '', '2023-07-25 08:29:57.709926', 15, 'Jedes Woche wird ein Kuchen vom BBT für Puzzle Organisiert',
        21, 8, 1, 'NUMBER', 'metric', '2023-07-25 08:18:44.087674', null, null, null, 1),
       (14, 0, '', '2023-07-25 08:31:39.249943', 20, 'Das BBT hilft den Membern 20% mehr beim Töggelen', 1, 8, 1,
        'PERCENT', 'metric', '2023-07-25 08:18:44.087674', null, null, null, 1);

insert into check_in (id, change_info, created_on, initiatives, modified_on, value_metric, created_by_id, key_result_id,
                      confidence, check_in_type, zone, version)
values (1,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam',
        '2023-07-25 08:44:13.865976', '', '2023-07-24 22:00:00.000000', 77, 1, 8, null, 'metric', null, 1),
       (2,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:44:35.998776', '', '2023-07-25 22:00:00.000000', 89, 1, 8, null, 'metric', null, 1),
       (3,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:44:54.832400', '', '2023-07-24 22:00:00.000000', 1, 1, 7, null, 'metric', null, 1),
       (4,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:45:07.911215',
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 22:00:00.000000', 7, 1, 7, null, 'metric', null, 1),
       (5,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:45:25.583267',
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-24 22:00:00.000000', 3, 1, 6, null, 'metric', null, 1),
       (6,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:45:42.707340',
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-24 22:00:00.000000', 3, 1, 5, null, 'metric', null, 1),
       (7,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:45:57.304875', '', '2023-07-25 22:00:00.000000', 2, 1, 5, null, 'metric', null, 1),
       (8,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:46:21.358930',
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-24 22:00:00.000000', 70, 1, 4, null, 'metric', null, 1),
       (9,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:46:39.204525',
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-24 22:00:00.000000', 16, 1, 3, null, 'metric', null, 1),
       (10,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:47:01.649202',
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-24 22:00:00.000000', 10, 1, 10, null, 'metric', null, 1),
       (11,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:48:21.822399',
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-24 22:00:00.000000', 81, 1, 10, null, 'metric', null, 1),
       (12,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:47:38.435770',
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-24 22:00:00.000000', 20, 1, 10, null, 'metric', null, 1),
       (13,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:47:55.811768',
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-24 22:00:00.000000', 1, 1, 11, null, 'metric', null, 1),
       (14,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:48:08.440951',
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-24 22:00:00.000000', 50, 1, 12, null, 'metric', null, 1),
       (15,
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-25 08:47:22.341767',
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
        '2023-07-24 22:00:00.000000', 1, 1, 13, null, 'metric', null, 1);
