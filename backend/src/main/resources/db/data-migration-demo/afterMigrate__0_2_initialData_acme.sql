DO
$$
    BEGIN
        RAISE NOTICE 'Adding demo data for PITC with the user %.', session_user;

            INSERT INTO okr_acme.person VALUES (1, 'peggimann@puzzle.ch', 'Paco', 'Eggimann', 1, true);
            INSERT INTO okr_acme.person VALUES (3, 'brantschen@puzzle.ch', 'Jean-Claude', 'Brantschen', 1, true);
            INSERT INTO okr_acme.person VALUES (4, 'endtner@puzzle.ch', 'Janik', 'Endtner', 1, true);
            INSERT INTO okr_acme.person VALUES (11, 'wunderland@puzzle.ch', 'Alice', 'Wunderland', 1, false);
            INSERT INTO okr_acme.person VALUES (21, 'baumeister@puzzle.ch', 'Bob', 'Baumeister', 1, false);
            INSERT INTO okr_acme.person VALUES (31, 'peterson@puzzle.ch', 'Findus', 'Peterson', 1, false);
            INSERT INTO okr_acme.person VALUES (51, 'papierer@puzzle.ch', 'Robin', 'Papierer', 1, false);
            INSERT INTO okr_acme.person VALUES (61, 'gl@gl.com', 'gl', 'gl', 1, true);
            INSERT INTO okr_acme.person VALUES (1003, 'gl@acme.com', 'Jaya', 'Norris', 1, true);
            INSERT INTO okr_acme.person VALUES (1005, 'ya.minder05@gmail.com', 'Yanick', 'Minder', 1, true);
            INSERT INTO okr_acme.person VALUES (1006, 'kathrin.beer@bls.ch', 'Kathrin', 'Beer', 0, false);
            INSERT INTO okr_acme.person VALUES (1007, 'tanja.bieri@bls.ch', 'Tanja', 'Bieri', 0, false);
            INSERT INTO okr_acme.person VALUES (1008, 'matthias.blum@five-e.ch', 'Matthias', 'Blum', 0, false);
            INSERT INTO okr_acme.person VALUES (1009, 'patrik.landolfo@five-e.ch', 'Pat', 'Landolfo', 0, false);
            INSERT INTO okr_acme.person VALUES (1010, 'nicole@zeilenwerk.ch', 'Nicole', 'Pauli', 0, false);
            INSERT INTO okr_acme.person VALUES (1011, 'dario@zeilenwerk.ch', 'Dario', 'Schwab', 0, false);
            INSERT INTO okr_acme.person
            VALUES (1004, 'philipp.leimgruber@protonmail.ch', 'Philipp', 'Leimgruber', 1, true);
            INSERT INTO okr_acme.person VALUES (2, 'leimgruber@puzzle.ch', 'Philipp', 'Leimgruber', 2, false);


            --
-- Data for Name: quarter; Type: TABLE DATA; Schema: okr_acme; Owner: -
--

            INSERT INTO okr_acme.quarter VALUES (999, 'Backlog', NULL, NULL);
            INSERT INTO okr_acme.quarter VALUES (8, 'GJ 25/26-Q1', '2025-07-01', '2025-09-30');
            INSERT INTO okr_acme.quarter VALUES (7, 'GJ 24/25-Q4', '2025-04-01', '2025-06-30');
            INSERT INTO okr_acme.quarter VALUES (6, 'GJ 24/25-Q3', '2025-01-01', '2025-03-31');
            INSERT INTO okr_acme.quarter VALUES (5, 'GJ 24/25-Q2', '2024-10-01', '2024-12-31');
            INSERT INTO okr_acme.quarter VALUES (4, 'GJ 24/25-Q1', '2024-07-01', '2024-09-30');
            INSERT INTO okr_acme.quarter VALUES (3, 'GJ 23/24-Q4', '2024-04-01', '2024-06-30');
            INSERT INTO okr_acme.quarter VALUES (2, 'GJ 23/24-Q3', '2024-01-01', '2024-03-31');
            INSERT INTO okr_acme.quarter VALUES (1, 'GJ 23/24-Q2', '2023-10-01', '2023-12-31');


            --
-- Data for Name: team; Type: TABLE DATA; Schema: okr_acme; Owner: -
--

            INSERT INTO okr_acme.team VALUES (4, '/BBT', 1);
            INSERT INTO okr_acme.team VALUES (5, 'Puzzle ITC', 1);
            INSERT INTO okr_acme.team VALUES (6, 'LoremIpsum', 1);
            INSERT INTO okr_acme.team VALUES (7, 'Mobility', 1);
            INSERT INTO okr_acme.team VALUES (8, 'we are cube.³', 1);
            INSERT INTO okr_acme.team VALUES (1009, 'BLS AG', 0);
            INSERT INTO okr_acme.team VALUES (1010, 'five-e business AG', 0);
            INSERT INTO okr_acme.team VALUES (1011, 'Zeilenwerk GmbH', 0);


            --
-- Data for Name: objective; Type: TABLE DATA; Schema: okr_acme; Owner: -
--

            INSERT INTO okr_acme.objective
            VALUES (4, '', '2023-07-25 08:17:51.309958', 'Build a company culture that kills the competition.', 1, 7, 5,
                    'ONGOING', 1, '2023-07-25 08:17:51.309958', 1);
            INSERT INTO okr_acme.objective
            VALUES (3,
                    'Die Konkurenz nimmt stark zu, um weiterhin einen angenehmen Markt bearbeiten zu können, wollen wir die Kundenzufriedenheit steigern. ',
                    '2023-07-25 08:13:48.768262', 'Wir wollen die Kundenzufriedenheit steigern', 1, 7, 5, 'ONGOING', 1,
                    '2023-07-25 08:13:48.768262', 1);
            INSERT INTO okr_acme.objective
            VALUES (6, '', '2023-07-25 08:26:46.98201',
                    'Als BBT wollen wir den Arbeitsalltag der Members von Puzzle ITC erleichtern.', 1, 7, 4, 'ONGOING',
                    1, '2023-07-25 08:26:46.98201', 1);
            INSERT INTO okr_acme.objective
            VALUES (5, 'Damit wir nicht alle anderen Entwickler stören wollen wir so leise wie möglich arbeiten',
                    '2023-07-25 08:20:36.894258', 'Wir wollen das leiseste Team bei Puzzle sein', 1, 7, 4,
                    'NOTSUCCESSFUL', 1, '2023-07-25 08:20:36.894258', 1);
            INSERT INTO okr_acme.objective
            VALUES (9, '', '2023-07-25 08:39:45.752126',
                    'At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.',
                    1, 7, 6, 'NOTSUCCESSFUL', 1, '2023-07-25 08:39:45.752126', 1);
            INSERT INTO okr_acme.objective
            VALUES (10, '', '2023-07-25 08:39:45.772126',
                    'should not appear on staging, no sea takimata sanctus est Lorem ipsum dolor sit amet.', 1, 7, 6,
                    'SUCCESSFUL', 1, '2023-07-25 08:39:45.772126', 1);
            INSERT INTO okr_acme.objective
            VALUES (8, '', '2023-07-25 08:39:28.175703',
                    'consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua',
                    1, 7, 6, 'NOTSUCCESSFUL', 1, '2023-07-25 08:39:28.175703', 1);


            --
-- Data for Name: key_result; Type: TABLE DATA; Schema: okr_acme; Owner: -
--

            INSERT INTO okr_acme.key_result
            VALUES (3, 0, '', '2023-07-25 08:14:31.964115', 25, 'Steigern der URS um 25%', 1, 3, 1, 'metric',
                    '2023-07-25 08:14:31.964115', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_acme.key_result
            VALUES (4, 100, '', '2023-07-25 08:15:18.244565', 67, 'Antwortzeit für Supportanfragen um 33% verkürzen.',
                    1, 3, 1, 'metric', '2023-07-25 08:15:18.244565', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_acme.key_result
            VALUES (5, NULL, '', '2023-07-25 08:16:24.466383', 1,
                    'Kundenzufriedenheitsumfrage soll mindestens einmal pro 2 Wochen durchgeführt werden. ', 11, 4, 1,
                    'metric', '2023-07-25 08:16:24.466383', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_acme.key_result
            VALUES (6, NULL, '', '2023-07-25 08:18:44.087674', 1,
                    'New structure that rewards funny guys and innovation before the end of Q1. ', 11, 4, 1, 'metric',
                    '2023-07-25 08:18:44.087674', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_acme.key_result
            VALUES (7, NULL, '', '2023-07-25 08:19:13.5693', 4,
                    'Monthly town halls between our people and leadership teams over the next four months.', 21, 4, 1,
                    'metric', '2023-07-25 08:18:44.087674', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_acme.key_result
            VALUES (8, NULL, '', '2023-07-25 08:19:44.351252', 80,
                    'High employee satisfaction scores (80%+) throughout the year.', 11, 5, 1, 'metric',
                    '2023-07-25 08:18:44.087674', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_acme.key_result
            VALUES (10, NULL, '', '2023-07-25 08:23:02.273028', 60,
                    'Im Durchschnitt soll die Lautstärke 60dB nicht überschreiten', 21, 5, 1, 'metric',
                    '2023-07-25 08:18:44.087674', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_acme.key_result
            VALUES (11, NULL, '', '2023-07-25 08:24:10.972984', 2,
                    'Mindestens 2 Komplimente für eine angenehme Arbeitslautstärke soll dem Team zugesprochen werden.',
                    21, 6, 1, 'metric', '2023-07-25 08:18:44.087674', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_acme.key_result
            VALUES (12, 0, '', '2023-07-25 08:28:45.110759', 80,
                    'Wir wollen bereits nach Q1 rund 80% des Redesigns vom OKR-Tool abgeschlossen haben. ', 1, 6, 1,
                    'metric', '2023-07-25 08:18:44.087674', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_acme.key_result
            VALUES (13, NULL, '', '2023-07-25 08:29:57.709926', 15,
                    'Jedes Woche wird ein Kuchen vom BBT für Puzzle Organisiert', 21, 8, 1, 'metric',
                    '2023-07-25 08:18:44.087674', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_acme.key_result
            VALUES (14, 0, '', '2023-07-25 08:31:39.249943', 20, 'Das BBT hilft den Membern 20% mehr beim Töggelen', 1,
                    8, 1, 'metric', '2023-07-25 08:18:44.087674', NULL, NULL, NULL, 1, 2);


            --
-- Data for Name: action; Type: TABLE DATA; Schema: okr_acme; Owner: -
--


--
-- Data for Name: alignment; Type: TABLE DATA; Schema: okr_acme; Owner: -
--


--
-- Data for Name: check_in; Type: TABLE DATA; Schema: okr_acme; Owner: -
--

            INSERT INTO okr_acme.check_in
            VALUES (1,
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam',
                    '2023-07-25 08:44:13.865976', '', '2023-07-24 22:00:00', 77, 1, 8, NULL, 'metric', NULL, 1);
            INSERT INTO okr_acme.check_in
            VALUES (2,
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
                    '2023-07-25 08:44:35.998776', '', '2023-07-25 22:00:00', 89, 1, 8, NULL, 'metric', NULL, 1);
            INSERT INTO okr_acme.check_in
            VALUES (3,
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
                    '2023-07-25 08:44:54.8324', '', '2023-07-24 22:00:00', 1, 1, 7, NULL, 'metric', NULL, 1);
            INSERT INTO okr_acme.check_in
            VALUES (4,
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
                    '2023-07-25 08:45:07.911215',
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
                    '2023-07-25 22:00:00', 7, 1, 7, NULL, 'metric', NULL, 1);
            INSERT INTO okr_acme.check_in
            VALUES (5,
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
                    '2023-07-25 08:45:25.583267',
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
                    '2023-07-24 22:00:00', 3, 1, 6, NULL, 'metric', NULL, 1);
            INSERT INTO okr_acme.check_in
            VALUES (6,
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
                    '2023-07-25 08:45:42.70734',
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
                    '2023-07-24 22:00:00', 3, 1, 5, NULL, 'metric', NULL, 1);
            INSERT INTO okr_acme.check_in
            VALUES (7,
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
                    '2023-07-25 08:45:57.304875', '', '2023-07-25 22:00:00', 2, 1, 5, NULL, 'metric', NULL, 1);
            INSERT INTO okr_acme.check_in
            VALUES (8,
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
                    '2023-07-25 08:46:21.35893',
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
                    '2023-07-24 22:00:00', 70, 1, 4, NULL, 'metric', NULL, 1);
            INSERT INTO okr_acme.check_in
            VALUES (9,
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
                    '2023-07-25 08:46:39.204525',
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
                    '2023-07-24 22:00:00', 16, 1, 3, NULL, 'metric', NULL, 1);
            INSERT INTO okr_acme.check_in
            VALUES (10,
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
                    '2023-07-25 08:47:01.649202',
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
                    '2023-07-24 22:00:00', 10, 1, 10, NULL, 'metric', NULL, 1);
            INSERT INTO okr_acme.check_in
            VALUES (11,
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
                    '2023-07-25 08:48:21.822399',
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
                    '2023-07-24 22:00:00', 81, 1, 10, NULL, 'metric', NULL, 1);
            INSERT INTO okr_acme.check_in
            VALUES (12,
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
                    '2023-07-25 08:47:38.43577',
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
                    '2023-07-24 22:00:00', 20, 1, 10, NULL, 'metric', NULL, 1);
            INSERT INTO okr_acme.check_in
            VALUES (13,
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
                    '2023-07-25 08:47:55.811768',
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
                    '2023-07-24 22:00:00', 1, 1, 11, NULL, 'metric', NULL, 1);
            INSERT INTO okr_acme.check_in
            VALUES (14,
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
                    '2023-07-25 08:48:08.440951',
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
                    '2023-07-24 22:00:00', 50, 1, 12, NULL, 'metric', NULL, 1);
            INSERT INTO okr_acme.check_in
            VALUES (15,
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
                    '2023-07-25 08:47:22.341767',
                    'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores ',
                    '2023-07-24 22:00:00', 1, 1, 13, NULL, 'metric', NULL, 1);


            --
-- Data for Name: completed; Type: TABLE DATA; Schema: okr_acme; Owner: -
--

            INSERT INTO okr_acme.completed VALUES (1, 5, 'Not successful because there were many events this month', 1);
            INSERT INTO okr_acme.completed VALUES (2, 9, 'Was not successful because we were too slow', 1);
            INSERT INTO okr_acme.completed
            VALUES (3, 8, 'Sadly we had not enough members to complete this objective', 1);
            INSERT INTO okr_acme.completed VALUES (4, 10, 'Objective could be completed fast and easy', 1);


            --
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: okr_acme; Owner: -
--


            --
-- Data for Name: person_team; Type: TABLE DATA; Schema: okr_acme; Owner: -
--

            INSERT INTO okr_acme.person_team VALUES (true, 1, 1, 1, 4);
            INSERT INTO okr_acme.person_team VALUES (false, 1, 2, 1, 5);
            INSERT INTO okr_acme.person_team VALUES (false, 1, 3, 1, 7);
            INSERT INTO okr_acme.person_team VALUES (false, 1, 4, 11, 6);
            INSERT INTO okr_acme.person_team VALUES (false, 1, 5, 21, 8);
            INSERT INTO okr_acme.person_team VALUES (true, 1, 6, 31, 8);
            INSERT INTO okr_acme.person_team VALUES (true, 1, 8, 51, 6);
            INSERT INTO okr_acme.person_team VALUES (true, 1, 9, 61, 5);
            INSERT INTO okr_acme.person_team VALUES (false, 1, 10, 61, 6);
            INSERT INTO okr_acme.person_team VALUES (false, 1, 13, 3, 5);
            INSERT INTO okr_acme.person_team VALUES (false, 1, 14, 3, 7);
            INSERT INTO okr_acme.person_team VALUES (false, 1, 15, 4, 5);
            INSERT INTO okr_acme.person_team VALUES (false, 1, 16, 4, 7);
            INSERT INTO okr_acme.person_team VALUES (true, 1, 21, 1008, 1010);
            INSERT INTO okr_acme.person_team VALUES (true, 1, 22, 1009, 1010);
            INSERT INTO okr_acme.person_team VALUES (true, 1, 24, 1010, 1011);
            INSERT INTO okr_acme.person_team VALUES (true, 1, 25, 1011, 1011);
            INSERT INTO okr_acme.person_team VALUES (true, 1, 18, 1006, 1009);
            INSERT INTO okr_acme.person_team VALUES (true, 1, 19, 1007, 1009);
            INSERT INTO okr_acme.person_team VALUES (true, 1, 26, 1004, 7);


            --
-- Data for Name: unit; Type: TABLE DATA; Schema: okr_acme; Owner: -
--

            INSERT INTO okr_acme.unit VALUES (1, 0, 'PROZENT', true, 1);
            INSERT INTO okr_acme.unit VALUES (2, 0, 'ZAHL', true, 1);
            INSERT INTO okr_acme.unit VALUES (3, 0, 'CHF', true, 1);
            INSERT INTO okr_acme.unit VALUES (4, 0, 'EUR', true, 1);
            INSERT INTO okr_acme.unit VALUES (5, 0, 'FTE', true, 1);
            INSERT INTO okr_acme.unit VALUES (6, 0, 'UNBEKANNT', false, 1);


        RAISE NOTICE 'Finished successfully!';
    END
$$;

