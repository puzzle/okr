truncate table alignment cascade;
truncate table action cascade;
truncate table check_in cascade;
truncate table key_result cascade;
truncate table completed cascade;
truncate table objective cascade;
truncate table person cascade;
truncate table quarter cascade;
truncate table team cascade;
truncate table person_team cascade;
truncate table unit cascade;

-- fill quarter with dummy labels. correct labels are updated via callback sql scripts
insert into quarter (id, label)
values (1, '1'), -- last past quarter
       (2, '2'),
       (3, '3'),
       (4, '4'),
       (5, '5'),
       (6, '6'), -- first past quarter
       (7, '7'), -- current quarter
       (8, '8'), -- future quarter
       (999, 'Backlog');

insert into person (id, email, first_name, last_name, version, okr_champion)
values (1, 'peggimann@puzzle.ch', 'Paco', 'Eggimann', 1, TRUE),
       (2, 'leimgruber@puzzle.ch', 'Philipp', 'Leimgruber', 1, TRUE),
       (3, 'brantschen@puzzle.ch', 'Jean-Claude', 'Brantschen', 1, TRUE),
       (4, 'endtner@puzzle.ch', 'Janik', 'Endtner', 1, TRUE),
       (11, 'wunderland@puzzle.ch', 'Alice', 'Wunderland', 1, FALSE),
       (21, 'baumeister@puzzle.ch', 'Bob', 'Baumeister', 1, FALSE),
       (31, 'peterson@puzzle.ch', 'Findus', 'Peterson', 1, FALSE),
       (51, 'papierer@puzzle.ch', 'Robin', 'Papierer', 1, FALSE),
       (61, 'gl@gl.com', 'gl', 'gl', 1, TRUE);

insert into team (id, name, version)
values (4, '/bbt', 1),
       (5, 'Puzzle ITC', 1),
       (6, '/mid', 1),
       (7, '/mobility', 1);

insert into unit (id, version, unit_name, created_by_id, is_default)
values (1, 0, 'PROZENT', 1, true),
       (2, 0, 'ZAHL', 1,true),
       (3, 0, 'CHF', 1, true),
       (4, 0, 'EUR', 1, true),
       (5, 0, 'FTE', 1, true),
       (6, 0, 'UNKNOWN', 1, false),
       (100, 0, 'TO_BE_UPDATED', 61, false),
       (101, 0, 'TO_BE_DELETED', 61, false);

insert into person_team (id, version, person_id, team_id, team_admin)
        -- peggimann@puzzle.ch
VALUES (1, 1, 1, 4, TRUE),
       (2, 1, 1, 5, FALSE),
       (3, 1, 1, 7, FALSE),
       -- wunderland@puzzle.ch
       (4, 1, 11, 6, FALSE),
       -- baumeister@puzzle.ch
       (5, 1, 21, 6, FALSE),
       -- peterson@puzzle.ch
       (6, 1, 31, 7, TRUE),
       -- papierer@puzzle.ch
       (8, 1, 51, 6, TRUE),
       -- gl@gl.ch
       (9, 1, 61, 7, TRUE),
       (10, 1, 61, 5, FALSE),
        -- leimgruber@puzzle.ch
       (11, 1, 2, 5, FALSE),
       (12, 1, 2, 7, TRUE),
       -- brantschen@puzzle.ch
       (13, 1, 3, 5, FALSE),
       (14, 1, 3, 7, FALSE),
       -- endtner@puzzle.ch
       (15, 1, 4, 5, FALSE),
       (16, 1, 4, 7, FALSE);
;

insert into objective (id, description, modified_on, title, created_by_id, quarter_id, team_id, state, modified_by_id,
                       created_on, version)
values (4, 'Wir wollen als Unternehmen so sein das wir die besten Kunden, Mitarbeiter und Freunde anlocken.', '2023-07-25 08:17:51.309958', 'Wir haben eine Unternehmenskultur, welche die Konkurrenz aussticht.', 1, 7, 5, 'SUCCESSFUL',
        1, '2023-07-25 08:17:51.309958', 1),
       (3,
        'Die Konkurrenz nimmt stark zu, um weiterhin einen angenehmen Markt bearbeiten zu können, wollen wir die Kundenzufriedenheit steigern. ',
        '2023-07-25 08:13:48.768262', 'Wir wollen die Kundenzufriedenheit steigern', 1, 7, 5, 'ONGOING', 1,
        '2023-07-25 08:13:48.768262', 1),
       (6, '', '2023-07-25 08:26:46.982010',
        'Als BBT wollen wir den Arbeitsalltag der Members von Puzzle ITC erleichtern.', 1, 7, 4, 'ONGOING', 1,
        '2023-07-25 08:26:46.982010', 1),
       (9, 'Arbeiten mit Cloud-Technologien ist ein wichtiger Fokus, bei dem wir noch mehr Wissen haben sollten.', '2023-07-25 08:39:45.752126',
        'Wir bauen das Cloud-Knowledge aus',
        1, 7, 7, 'ONGOING', 1, '2023-07-25 08:39:45.752126', 1),
       (8, '', '2023-07-25 08:39:28.175703',
        'Wir verbessern die Dev Experience für die Benutzung unserer Plattformen',
        1, 7, 6, 'DRAFT', 1, '2023-07-25 08:39:28.175703', 1),
       (10, '', '2023-07-25 08:39:45.752126',
        'Wir haben die passenden Projekte und prüfen unser Angebot',
        1, 7, 7, 'ONGOING', 1, '2023-07-25 08:39:45.752126', 1);

insert into completed (id, version, objective_id, comment)
values (1, 1, 4, 'Hat wunderbar geklappt, soll so weitergeführt werden.');

insert into key_result (id, baseline, description, modified_on, stretch_goal, title, created_by_id, objective_id,
                        owner_id, unit_id, key_result_type, created_on, commit_zone, target_zone, stretch_zone, version)
values (3, 0, '', '2023-07-25 08:14:31.964115', 25, 'Steigern der URS um 25%', 1, 3, 1, 1, 'metric',
        '2023-07-25 08:14:31.964115', null, null, null, 1),
       (4, 100, '', '2023-07-25 08:15:18.244565', 67, 'Antwortzeit für Supportanfragen um 33% verkürzen.', 1, 3, 1,
        1, 'metric', '2023-07-25 08:15:18.244565', null, null, null, 1),
       (5, null, '', '2023-07-25 08:16:24.466383', 1.5,
        'Verbessere das Ranking auf Kununu um 1.5 Punkte', 11, 4, 1, 2,
        'metric', '2023-07-25 08:16:24.466383', null, null, null, 1),
       (7, null, '', '2023-07-25 08:19:13.569300', 4,
        'Monatliche Town Halls die nächsten vier Monaten', 21, 4, 1, 2,
        'metric', '2023-07-25 08:18:44.087674', null, null, null, 1),
       (11, null, '', '2023-07-25 08:24:10.972984', 2,
        'Mindestens 2 Komplimente für eine angenehme Arbeitslautstärke soll dem Team zugesprochen werden.', 21, 6, 1,
        2, 'metric', '2023-07-25 08:18:44.087674', null, null, null, 1),
       (12, 0, '', '2023-07-25 08:28:45.110759', 80,
        'Wir wollen bereits nach Q1 rund 80% des Redesigns vom OKR-Tool abgeschlossen haben. ', 1, 6, 1, 1,
        'metric', '2023-07-25 08:18:44.087674', null, null, null, 1),
       (13, null, '', '2023-07-25 08:29:57.709926', 15, 'Jede Woche wird ein Kuchen vom BBT für Puzzle organisiert',
        21, 6, 1, 2, 'metric', '2023-07-25 08:18:44.087674', null, null, null, 1),
       (14, null, '', '2023-07-25 08:31:39.249943', 100, 'Anzahl der Mitarbeiter mit Cloud-Zertifizierungen auf über 70% steigern', 1, 9, 1,
        1, 'metric', '2023-07-25 08:18:44.087674', null, null, null, 1),
       (15, 9, '', '2023-07-25 08:31:39.249943', null, 'Wir setzen ein /mobility Cloud-Best-Practices-Wiki auf', 1, 9, 1,
        null, 'ordinal', '2023-07-25 08:18:44.087674', 'Das Wiki wurde erstellt und die Best-Practices darin dokumentiert', 'Das Wiki wird aktiv benutzt und gepflegt', 'Das Wiki wird aktiv von /mobility gepflegt und benutzt. Auch andere Teams beteiligen sich am Wiki.', 1),
       (16, null, '', '2023-07-25 08:31:39.249943', null, 'Wir haben eine klare Strategie für die Weiterentwicklung von Lagoon erarbeitet', 1, 10, 1,
        null, 'ordinal', '2023-07-25 08:18:44.087674', 'Es liegen Entwürfe für verschiedene Strategien vor', 'Eine Strategie wurde ausgewählt, überarbeitet und definiert.', 'Die Strategie wurde verabschiedet.', 1),
       (17, 0, '', '2023-07-25 08:31:39.249943', 5, 'Wir erstellen neue PoCs für Produkte um unser Portfolio zu erweitern', 1, 10, 1,
        2, 'metric', '2023-07-25 08:18:44.087674', null, null, null, 1);

insert into check_in (id, change_info, created_on, initiatives, modified_on, value_metric, created_by_id, key_result_id,
                      confidence, check_in_type, zone, version)
values (5,
        'Durch mehr Snacks und Mate konnten die Bewertungen gesteigert werden.',
        '2023-07-25 08:45:25.583267',
        '',
        '2023-07-24 22:00:00.000000', 1.1, 1, 5, null, 'metric', null, 1),
       (7,
        'Das vierte Town Hall durchgeführt',
        '2023-07-25 08:45:57.304875', '', '2023-07-25 22:00:00.000000', 4, 1, 7, null, 'metric', null, 1),
       (8,
        'Check-In mit dem Support-Team',
        '2023-07-25 08:46:21.358930',
        'Keine',
        '2023-07-24 22:00:00.000000', 70, 1, 4, 9, 'metric', null, 1),
       (9,
        'Check-In mit dem Team',
        '2023-07-25 08:46:39.204525',
        'Mehr Feedback bei den Kunden einholen',
        '2023-07-24 22:00:00.000000', 16, 1, 3, 6, 'metric', null, 1),
       (10,
        'Ein Kompliment von Lukas erhalten :)',
        '2023-07-25 08:47:55.811768',
        'Lautstärke beibehalten, aktiver nach Komplimenten fischen',
        '2023-07-24 22:00:00.000000', 1, 1, 11, null, 'metric', null, 1),
       (11,
        'Ein Grossteil der Änderungen sind umgesetzt',
        '2023-07-25 08:48:08.440951',
        'Weiter machen wie bisher',
        '2023-07-24 22:00:00.000000', 71, 1, 12, 8, 'metric', null, 1),
       (12,
        'Nach genauer Überprüfung stellt sich heraus das niemand im BBT gut backen kann',
        '2023-07-25 08:47:22.341767',
        'Kochkurs organisieren',
        '2023-07-24 22:00:00.000000', 1, 1, 13, 2, 'metric', null, 1),
       (13,
        'Jonas und Michu haben ein CNF Zertifikat abgeschlossen.',
        '2023-07-25 08:47:22.341767',
        '',
        '2023-07-24 22:00:00.000000', 56, 1, 14, 7, 'metric', null, 1),
       (14,
        'Giannin hat ein Kubernetes Zertifikat erfolgreich abgeschlossen.',
        '2023-07-27 08:47:22.341767',
        '',
        '2023-07-24 22:10:00.000000', 72, 1, 14, 9, 'metric', null, 1),
        (15, 'Das Wiki wurde erstellt und die bekannte Best-Practices nachgetragen.','2023-07-27 08:47:22.341767',
         'Das Wiki untern den Members bekannter machen.',
         '2023-07-24 22:10:00.000000', null, 1, 15, 7, 'ordinal', 'COMMIT', 1),
        (16, 'Strategie ist dokumentiert und abgelegt.','2023-07-27 08:47:22.341767',
           'Wird im nächsten Meeting am 7. April besprochen',
           '2023-07-24 22:10:00.000000', null, 1, 16, 7, 'ordinal', 'COMMIT', 1),
        (17, 'Bis jetzt wurde noch kein PoC erstellt, da keine Members Zeit hatte.','2023-07-27 08:47:22.341767',
        'Zeit reservieren',
        '2023-07-24 22:10:00.000000', 0, 1, 17, 7, 'metric', null, 1);
