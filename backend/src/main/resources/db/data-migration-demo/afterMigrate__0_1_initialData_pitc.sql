DO
$$
    BEGIN
        IF '${tenant}' = 'pitc' THEN
            RAISE NOTICE 'Adding demo data for PITC with the user %.', session_user;

            INSERT INTO okr_pitc.person VALUES (1, 'wunderland@puzzle.ch', 'Alice', 'Wunderland', 1, false);
            INSERT INTO okr_pitc.person VALUES (21, 'baumeister@puzzle.ch', 'Bob', 'Baumeister', 1, false);
            INSERT INTO okr_pitc.person VALUES (31, 'peterson@puzzle.ch', 'Findus', 'Peterson', 1, false);
            INSERT INTO okr_pitc.person VALUES (41, 'egiman@puzzle.ch', 'Paco', 'Egiman', 1, false);
            INSERT INTO okr_pitc.person VALUES (51, 'papierer@puzzle.ch', 'Robin', 'Papierer', 1, false);
            INSERT INTO okr_pitc.person VALUES (1000, 'gl@gl.com', 'Jaya', 'Norris', 1, true);
            INSERT INTO okr_pitc.person VALUES (1001, 'endtner@puzzle.ch', 'Janik', 'Endtner', 0, true);
            INSERT INTO okr_pitc.person VALUES (1002, 'leimgruber@puzzle.ch', 'Philipp', 'Leimgruber', 1, true);
            INSERT INTO okr_pitc.person VALUES (1003, 'brantschen@puzzle.ch', 'Jean-Claude', 'Brantschen', 1, true);
            INSERT INTO okr_pitc.person VALUES (1004, 'peggimann@puzzle.ch', 'Paco', 'Eggimann', 1, true);
            INSERT INTO okr_pitc.person VALUES (1005, 'minder@puzzle.ch', 'Yanick', 'Minder', 1, true);
            INSERT INTO okr_pitc.person VALUES (1006, 'gaechter@puzzle.ch', 'Christoph', 'Gaechter', 0, false);
            INSERT INTO okr_pitc.person VALUES (1007, 'egli@puzzle.ch', 'Marc', 'Egli', 1, true);

            -- keep sequence_person ahead of the hardcoded demo IDs above, otherwise real
            -- SSO-provisioned logins eventually collide with them (person_pkey violation)
            PERFORM setval('sequence_person', (SELECT MAX(id) FROM okr_pitc.person));


            --
    -- Data for Name: quarter; Type: TABLE DATA; Schema: okr_pitc; Owner: -
    --

            INSERT INTO okr_pitc.quarter VALUES (999, 'Backlog', NULL, NULL);
            INSERT INTO okr_pitc.quarter VALUES (8, 'GJ 25/26-Q1', '2025-07-01', '2025-09-30');
            INSERT INTO okr_pitc.quarter VALUES (7, 'GJ 24/25-Q4', '2025-04-01', '2025-06-30');
            INSERT INTO okr_pitc.quarter VALUES (6, 'GJ 24/25-Q3', '2025-01-01', '2025-03-31');
            INSERT INTO okr_pitc.quarter VALUES (5, 'GJ 24/25-Q2', '2024-10-01', '2024-12-31');
            INSERT INTO okr_pitc.quarter VALUES (4, 'GJ 24/25-Q1', '2024-07-01', '2024-09-30');
            INSERT INTO okr_pitc.quarter VALUES (3, 'GJ 23/24-Q4', '2024-04-01', '2024-06-30');
            INSERT INTO okr_pitc.quarter VALUES (2, 'GJ 23/24-Q3', '2024-01-01', '2024-03-31');
            INSERT INTO okr_pitc.quarter VALUES (1, 'GJ 23/24-Q2', '2023-10-01', '2023-12-31');


            --
    -- Data for Name: team; Type: TABLE DATA; Schema: okr_pitc; Owner: -
    --

            INSERT INTO okr_pitc.team VALUES (20, '/dev/tre', 2);
            INSERT INTO okr_pitc.team VALUES (26, '/hitobito', 2);
            INSERT INTO okr_pitc.team VALUES (21, '/mid', 2);
            INSERT INTO okr_pitc.team VALUES (15, '/mobility', 2);
            INSERT INTO okr_pitc.team VALUES (18, '/security', 2);
            INSERT INTO okr_pitc.team VALUES (24, '/ux', 2);
            INSERT INTO okr_pitc.team VALUES (23, '/zh', 2);
            INSERT INTO okr_pitc.team VALUES (25, 'MarCom', 2);
            INSERT INTO okr_pitc.team VALUES (14, 'Puzzle ITC', 2);


            --
    -- Data for Name: objective; Type: TABLE DATA; Schema: okr_pitc; Owner: -
    --

            INSERT INTO okr_pitc.objective
            VALUES (1122, 'test', '2024-07-02 11:46:42.259678', 'test', 1, 999, 24, 'DRAFT', NULL,
                    '2024-07-02 11:46:42.260734', 0);
            INSERT INTO okr_pitc.objective
            VALUES (65, 'Die absolute Verrechenbarkeit von /zh ist in jedem Monat auf über 70.0%',
                    '2023-06-26 10:53:50.28674', 'Wir erreichen eine hohe absolute Verrechenbarkeit', 1, 7, 23,
                    'ONGOING', NULL, '2023-06-26 10:53:50.28674', 1);
            INSERT INTO okr_pitc.objective
            VALUES (68, '', '2023-06-27 08:24:19.508338', 'Wir machen Puzzleness sichtbarer', 1, 7, 25, 'ONGOING', NULL,
                    '2023-06-27 08:24:19.508338', 1);
            INSERT INTO okr_pitc.objective
            VALUES (69, '', '2023-06-27 08:30:05.2284', 'Wir tragen technische Inhalte nach Aussen', 1, 7, 25,
                    'ONGOING', NULL, '2023-06-27 08:30:05.2284', 1);
            INSERT INTO okr_pitc.objective
            VALUES (42, '', '2024-05-29 05:34:41.579936',
                    'Wir arbeiten gerne bei Puzzle und schaffen ein positives internes Bild', 1, 7, 14, 'SUCCESSFUL',
                    16, '2023-06-14 09:11:09.406672', 2);
            INSERT INTO okr_pitc.objective
            VALUES (58, '', '2023-06-20 14:21:27.063055', 'Wir verzeichnen eine gesunde Wirtschaftlichkeit', 1, 7, 21,
                    'ONGOING', NULL, '2023-06-20 14:21:27.063055', 1);
            INSERT INTO okr_pitc.objective
            VALUES (60, '', '2023-06-20 13:36:22.441286',
                    'Wir fördern die Zufriedenheit im Team und verbessern die Kommunikationswahrnehmung', 1, 7, 21,
                    'ONGOING', NULL, '2023-06-20 13:36:22.441286', 1);
            INSERT INTO okr_pitc.objective
            VALUES (56, '', '2023-06-20 12:47:46.393088', 'Wir erreichen eine gesunde Wirtschaftlichkeit', 1, 7, 20,
                    'ONGOING', NULL, '2023-06-20 12:47:46.393088', 1);
            INSERT INTO okr_pitc.objective
            VALUES (44, '', '2024-05-29 05:35:38.61899',
                    'Die Marktopportunitäten sind etabliert und erreichen einen sichtbaren Fortschritt', 1, 7, 14,
                    'NOTSUCCESSFUL', 16, '2023-06-14 10:42:03.621643', 2);
            INSERT INTO okr_pitc.objective
            VALUES (39, '', '2023-06-09 09:28:58.865895', 'Wir erreichen eine gesunde Wirtschaftlichkeit', 1, 7, 15,
                    'ONGOING', NULL, '2023-06-09 09:28:58.865895', 1);
            INSERT INTO okr_pitc.objective
            VALUES (40, '', '2023-06-14 14:48:22.609337',
                    'Wir fördern die Happiness unserer Member und steigern die Identifikation mit /mobility', 1, 7, 15,
                    'ONGOING', NULL, '2023-06-14 14:48:22.609337', 1);
            INSERT INTO okr_pitc.objective
            VALUES (54, '', '2023-06-20 12:33:17.632481', 'Wir steigern die Wirtschaftlichkeit von WAC', 1, 7, 24,
                    'ONGOING', NULL, '2023-06-20 12:33:17.632481', 1);
            INSERT INTO okr_pitc.objective
            VALUES (66, '', '2023-06-26 07:47:59.28265',
                    'Wir optimieren unseren Newsletter, damit noch mehr Menschen von Puzzle erfahren', 1, 7, 25,
                    'ONGOING', NULL, '2023-06-26 07:47:59.28265', 1);
            INSERT INTO okr_pitc.objective
            VALUES (1118, 'Wir bleiben finanziell auf einem guten Level', '2024-06-25 15:15:28.096103', 'Nöötli 💸', 1,
                    6, 21, 'ONGOING', 40, '2024-06-18 13:13:13.63553', 4);
            INSERT INTO okr_pitc.objective
            VALUES (1119,
                    '/security bleibt intern, aber trägt zu den MOs Beratung, Digitale Lösungen und Platform Engineering bei.',
                    '2024-07-01 09:06:54.68063', 'Beitrag zu MOs aufgleisen und planen', 1, 6, 18, 'ONGOING', 27,
                    '2024-06-18 13:19:34.330846', 3);
            INSERT INTO okr_pitc.objective
            VALUES (1101, '', '2024-06-24 11:33:46.394682',
                    'Wir verankern unser Umweltengagement mittels Zertifizierung ISO 14001 nachhaltig  ', 1, 6, 26,
                    'ONGOING', 41, '2024-06-17 06:46:21.357351', 5);
            INSERT INTO okr_pitc.objective
            VALUES (1105, 'Themen: Angebot /dev/tre', '2024-06-24 11:39:28.950244',
                    'Wir schärfen und kommunizieren unser Angebot', 1, 6, 20, 'ONGOING', 4,
                    '2024-06-17 12:07:31.359535', 2);
            INSERT INTO okr_pitc.objective
            VALUES (1115,
                    'Wir erreichen im Team Stabilität binden neue Members ein und erhalten die Zufriedenheit der bestehenden. Gleichzeitig werden wir effizienter in der Art wie wir arbeiten. ',
                    '2024-06-24 11:11:58.715794', 'Wir sind ein stabiles, performantes und zufriedenes Team', 1, 6, 24,
                    'ONGOING', 3, '2024-06-18 11:44:10.717314', 2);
            INSERT INTO okr_pitc.objective
            VALUES (1091, '', '2024-06-11 12:51:36.310489',
                    'Die Marktopportunitäten für das neue Geschäftsjahr sind lanciert und die Basis ist gelegt.', 1, 6,
                    14, 'ONGOING', 16, '2024-06-04 08:16:49.746375', 3);
            INSERT INTO okr_pitc.objective
            VALUES (1095,
                    'Wie können wir Rentablitässteigerung messen? z.B. wenn eine weitere Kooperation zu Stande kommt mir ci/cd und wir einen Member zu einem höheren Stundensatz einsetzen können. Durch konsequentes Auftragsmangement können wir Kosten senken. ',
                    '2024-07-04 07:01:53.197695',
                    'Wir steigern unsere Rentabilität durch die Umsetzung gezielter Massnahmen in den strategischen Schwerpunkten gesundes Wachstum, Technologieshift und Auftragsmanagement. ',
                    1, 6, 15, 'ONGOING', 5, '2024-06-13 14:06:45.770843', 7);
            INSERT INTO okr_pitc.objective
            VALUES (1097, '', '2024-07-04 07:02:08.619208',
                    'Durch strategische Grundlagenarbeit zur Sales-Strategie Mobility (mit Yup) sowie zur MO Digitale Lösungen haben wir den Grundstein für neue lukrative Aufträge gelegt. ',
                    1, 6, 15, 'ONGOING', 5, '2024-06-13 14:07:05.750528', 3);
            INSERT INTO okr_pitc.objective
            VALUES (1103, '', '2024-06-24 11:33:31.394778', 'Wir bringen frischen Wind in unseren Online-Auftritt.', 1,
                    6, 25, 'ONGOING', 26, '2024-06-17 11:08:49.869808', 1);
            INSERT INTO okr_pitc.objective
            VALUES (1099, 'Jahresstrategie 2024', '2024-06-24 11:33:39.309648',
                    'Durch Kosteneinsparungen und Wachstum verbessern wir die Wirtschaftlichkeit.', 1, 6, 26, 'ONGOING',
                    41, '2024-06-17 06:46:02.057806', 10);
            INSERT INTO okr_pitc.objective
            VALUES (1109, '', '2024-06-17 23:33:29.260166', 'Auslastung & Sales', 1, 6, 23, 'DRAFT', NULL,
                    '2024-06-17 23:33:29.26126', 0);
            INSERT INTO okr_pitc.objective
            VALUES (1110, '', '2024-06-17 23:44:32.095018', 'Recruiting', 1, 6, 23, 'DRAFT', NULL,
                    '2024-06-17 23:44:32.09616', 0);
            INSERT INTO okr_pitc.objective
            VALUES (1130, '', '2024-07-05 09:53:29.956073', 'Tests
    ', 1, 5, 24, 'DRAFT', NULL, '2024-07-05 09:53:29.957079', 0);
            INSERT INTO okr_pitc.objective
            VALUES (51,
                    'Zwei Leute, die ihr Pensum anpsasen. Mind. eine neue Person, die startet. Mehr Diversity im Team. Mehrere direkt betroffene Kunden.',
                    '2023-06-20 12:24:44.61702', 'Embrace Change im Team', 1, 7, 23, 'ONGOING', NULL,
                    '2023-06-20 12:24:44.61702', 1);
            INSERT INTO okr_pitc.objective
            VALUES (59, 'Bewusstsein was wir machen', '2023-06-25 09:22:27.334997',
                    'Wir definieren und kommunizieren unser Dienstleistungs- und Auftragsportfolio', 1, 7, 20,
                    'ONGOING', NULL, '2023-06-25 09:22:27.334997', 1);
            INSERT INTO okr_pitc.objective
            VALUES (72, '', '2023-07-27 07:39:38.193792', 'Wir steigern die Kundenzufriedenheit', 1, 7, 26, 'ONGOING',
                    NULL, '2023-07-27 07:39:38.193792', 1);
            INSERT INTO okr_pitc.objective
            VALUES (57, '', '2023-06-20 14:08:48.956072',
                    'Wir pushen New Tech und machen uns auf dem Markt weiter bemerkbar', 1, 7, 21, 'ONGOING', NULL,
                    '2023-06-20 14:08:48.956072', 1);
            INSERT INTO okr_pitc.objective
            VALUES (70, '', '2023-07-27 06:23:08.507453', 'Wir tragen zum wirtschaftlichen Erfolg von Puzzle bei ', 1,
                    7, 26, 'ONGOING', NULL, '2023-07-27 06:23:08.507453', 1);
            INSERT INTO okr_pitc.objective
            VALUES (43, '', '2024-05-29 05:35:10.63066',
                    'Wir erreichen einen Quartalsgewinn und sind weiter verrechenbar gewachsen', 1, 7, 14, 'SUCCESSFUL',
                    16, '2023-06-20 14:04:58.817841', 2);
            INSERT INTO okr_pitc.objective
            VALUES (47, '', '2023-06-20 10:55:46.475554', 'Wir werden Data Analytics / ML / MLOps Angebote verkaufen.',
                    1, 7, 15, 'ONGOING', NULL, '2023-06-20 10:55:46.475554', 1);
            INSERT INTO okr_pitc.objective
            VALUES (61,
                    'Das Büro, die Grundlage der Zusammenarbeit ist eingerichtet. Kürzliche Abgänge müssen kompensiert, Neueinstellungen integriert werden. /racoon wurde mit devtre fusioniert. Die technische Ausrichtung ist unklar. Die Auftragslage ändert sich, neue Aufträge kommen dazu. Die Divisionrentabilität wird eingeführt… Unter all diesen Gesichtspunkten ist es immanent wichtig, dass Team “neu” aufzustellen, damit es nicht verläddert.',
                    '2023-06-25 09:25:50.371312',
                    'Wir formen und stabiliseren das Team devtre und fördern die Selbstorganisation', 1, 7, 20,
                    'ONGOING', NULL, '2023-06-25 09:25:50.371312', 1);
            INSERT INTO okr_pitc.objective
            VALUES (53, '', '2023-06-20 13:15:13.236034',
                    'Wir arbeiten gerne bei Puzzle und schaffen ein positives internes Bild', 1, 7, 24, 'ONGOING', NULL,
                    '2023-06-20 13:15:13.236034', 1);
            INSERT INTO okr_pitc.objective
            VALUES (49,
                    'Im Q1 werden für mehrere Devs im Team (Andres, David, Yelan) neue Projekte oder spannende Mandate fällig. Der Salesfokus liegt entsprechend im Dev-Bereich.',
                    '2023-06-20 12:23:16.695538', 'Spannende nächste Etappe für Devs', 1, 7, 23, 'ONGOING', NULL,
                    '2023-06-20 12:23:16.695538', 1);
            INSERT INTO okr_pitc.objective
            VALUES (55, '', '2023-06-20 12:34:11.120224', 'Harder, Better, Faster, Stronger', 1, 7, 24, 'ONGOING', NULL,
                    '2023-06-20 12:34:11.120224', 1);
            INSERT INTO okr_pitc.objective
            VALUES (1059, 'Strategie 2024', '2024-03-21 12:42:49.144084',
                    'Wir bringen Hitobito durch ein Techboard Review aufs nächste Level', 1, 999, 26, 'DRAFT', 41,
                    '2024-03-15 09:43:41.686021', 2);
            INSERT INTO okr_pitc.objective
            VALUES (1111, 'Angebot und Fokusthemen von /zh festlegen und pushen', '2024-06-25 08:51:50.06875',
                    'Portfolio Refinement ', 1, 6, 23, 'DRAFT', 33, '2024-06-17 23:49:20.383198', 1);
            INSERT INTO okr_pitc.objective
            VALUES (1104, '', '2024-06-24 11:33:35.217206', 'Wir finden unsere Puzzle-Botschafter:innen.', 1, 6, 25,
                    'ONGOING', 26, '2024-06-17 11:10:48.844395', 2);
            INSERT INTO okr_pitc.objective
            VALUES (1116, '', '2024-07-01 09:06:52.032503', 'Wir bringen die Security-Prozesse bei Puzzle weiter.', 1,
                    6, 18, 'ONGOING', 27, '2024-06-18 11:53:07.317901', 1);
            INSERT INTO okr_pitc.objective
            VALUES (1114,
                    'Wir sind auch diesen Monat kommunikativ am Markt aktiv. Wir ziehen unsere Social Media Kampagne weiter und Analysieren die Wirksamkeit der vergangenen Kampagne um Feinjustierungen vornehmen zu können. ',
                    '2024-06-24 11:11:55.08339', 'We are Top of Mind!', 1, 6, 24, 'ONGOING', 3,
                    '2024-06-18 11:43:10.532408', 2);
            INSERT INTO okr_pitc.objective
            VALUES (1106, 'Themen: Wirtschaftlichkeit /dev/tre', '2024-06-24 11:38:46.707782',
                    'Wir sind wirtschaftlich gesund aufgestellt', 1, 6, 20, 'ONGOING', 4, '2024-06-17 12:09:04.174114',
                    1);
            INSERT INTO okr_pitc.objective
            VALUES (1092, 'Wir machen erste Schritte in den MOALs Organisation der Zukunft und CRM Ablösung',
                    '2024-06-18 12:52:46.270659', 'Wir reaktivieren OrgDev und starten mit dem CRM Projekt.', 1, 6, 14,
                    'ONGOING', 24, '2024-06-04 09:10:55.060774', 3);
            INSERT INTO okr_pitc.objective
            VALUES (1112, 'Wir arbeiten mit Hochdruck an unserer Rentabilität', '2024-06-24 11:11:51.938688',
                    'Wir steigern die Wirtschaftlichkeit von WAC', 1, 6, 24, 'ONGOING', 3, '2024-06-18 11:41:43.89273',
                    1);
            INSERT INTO okr_pitc.objective
            VALUES (1120, 'Wir habens weiterhin lässig bei /mid', '2024-06-25 15:15:30.827695', '/mid-Groove 💃🕺', 1, 6,
                    21, 'ONGOING', 40, '2024-06-18 13:21:17.72944', 3);
            INSERT INTO okr_pitc.objective
            VALUES (1100, '', '2024-06-24 11:33:42.768632', 'Wir erhöhen die Kundenzufriedenheit', 1, 6, 26, 'ONGOING',
                    41, '2024-06-17 06:46:15.168927', 3);
            INSERT INTO okr_pitc.objective
            VALUES (1096, '', '2024-07-04 07:02:03.09142',
                    'Wir haben eine Vertiefung für die strategischen Schwerpunkte Beratung und Schulungsangebot ausgearbeitet sowie zwei ML Ops Labs erfolgreich durchgeführt. ',
                    1, 6, 15, 'ONGOING', 5, '2024-06-13 14:06:56.922593', 3);
            INSERT INTO okr_pitc.objective
            VALUES (1102, 'Themen: Struktur und Organisation /dev/tre', '2024-06-24 11:38:35.342783',
                    'Wir festigen unsere selbstorganisierte Teamarbeit', 1, 6, 20, 'ONGOING', 4,
                    '2024-06-17 10:18:55.315729', 1);
            INSERT INTO okr_pitc.objective
            VALUES (1098, 'Wir starten durch im Thema Platform Engineering', '2024-06-25 15:15:25.260628',
                    'Platform Engineering goes brrr 🚀', 1, 6, 21, 'ONGOING', 40, '2024-06-14 13:16:40.662286', 5);
            INSERT INTO okr_pitc.objective
            VALUES (1077, '', '2024-06-14 08:12:00.689109',
                    'Unsere Website ist bereit für das Go-Live am 1. Juli 2024. ', 1, 7, 25, 'SUCCESSFUL', 26,
                    '2024-03-19 11:52:41.575107', 3);
            INSERT INTO okr_pitc.objective
            VALUES (1076, '', '2024-06-14 05:14:04.031166', 'Wir erzielen Erfolge in den Marktopprtunitäten.', 1, 7, 14,
                    'NOTSUCCESSFUL', 16, '2024-03-19 11:22:46.963368', 4);
            INSERT INTO okr_pitc.objective
            VALUES (1083, '', '2024-06-26 19:32:58.124949', 'Unsere Rentabilität bleibt hoch 📈', 1, 7, 21, 'SUCCESSFUL',
                    40, '2024-03-19 13:12:57.95764', 8);
            INSERT INTO okr_pitc.objective
            VALUES (1056, 'Jahresstrategie 2024', '2024-07-24 06:09:12.064469', 'Wir erhöhen die Rentabilität', 1, 7,
                    26, 'NOTSUCCESSFUL', 1002, '2024-03-15 08:43:59.822526', 10);
            INSERT INTO okr_pitc.objective
            VALUES (1082, '', '2024-06-17 15:30:27.282231',
                    'We Are Cube ist im Spotlight - Wir erhöhen unsere Präsenz am Markt', 1, 7, 24, 'SUCCESSFUL', 36,
                    '2024-03-19 13:07:26.109211', 4);
            INSERT INTO okr_pitc.objective
            VALUES (1072,
                    'Wir wollen in anderen Projekten präsent sein und beraten/helfen um Security-Themen im Bewusstsein zu halten. Ebenso wollen wir über Newspost oder Techkafis sowie Trainings Security bei ganz Puzzle als Thema hochhalten.',
                    '2024-06-18 08:31:18.477839', '/security unterstützt den Rest von Puzzle', 1, 7, 18,
                    'NOTSUCCESSFUL', 27, '2024-03-19 08:44:09.365113', 2);
            INSERT INTO okr_pitc.objective
            VALUES (1074, '', '2024-06-17 22:36:34.016086', 'Teamanliegen adressiert, Teamentwicklung angestossen', 1,
                    7, 23, 'NOTSUCCESSFUL', 33, '2024-03-19 10:09:48.989542', 4);
            INSERT INTO okr_pitc.objective
            VALUES (1062, 'Themen: Team, Zusammenarbeit, Selbstorganisation, Bereichsstruktur',
                    '2024-08-12 06:42:48.511085',
                    'Wir haben uns in den Bereichen Selbstorganisation und Arbeit im Team weiterentwickelt', 1, 7, 20,
                    'SUCCESSFUL', 1002, '2024-03-18 15:53:39.625582', 7);
            INSERT INTO okr_pitc.objective
            VALUES (1058, '', '2024-06-07 11:39:03.580716',
                    'Wir verankern unser Umweltengagement mittels Zertifizierung ISO 14001 nachhaltig  ', 1, 7, 26,
                    'SUCCESSFUL', 41, '2024-03-15 09:22:25.329866', 3);
            INSERT INTO okr_pitc.objective
            VALUES (1050, '', '2024-06-13 06:34:31.759701',
                    'Wir haben mit den zusätzlichen Members volle Auftragsbücher für den Sommer.', 1, 7, 14,
                    'SUCCESSFUL', 16, '2024-03-04 15:12:46.099519', 7);
            INSERT INTO okr_pitc.objective
            VALUES (1080, 'Wir schärfen unser Portfolio und verbreite(r)n das Know-How.', '2024-08-16 13:02:03.02441',
                    '/mid journey continues 🚀', 1, 7, 21, 'SUCCESSFUL', 1002, '2024-03-19 12:04:33.346437', 9);
            INSERT INTO okr_pitc.objective
            VALUES (1064, 'Themen: Auslastung, Rentabilität', '2024-07-24 06:14:51.402763', 'Wir sind wirtschaftlich gesund aufgestellt
    ', 1, 7, 20, 'NOTSUCCESSFUL', 1002, '2024-03-18 15:56:49.027715', 5);
            INSERT INTO okr_pitc.objective
            VALUES (1054, '', '2024-06-17 15:52:55.871115',
                    'Wir haben die strategische Stossrichtung "Technologieshift" ausgearbeitet und vorhandene Opportunitäten umgesetzt.',
                    1, 7, 15, 'NOTSUCCESSFUL', 20, '2024-03-07 14:40:45.156034', 7);
            INSERT INTO okr_pitc.objective
            VALUES (1078, '', '2024-06-14 07:51:33.731057', 'Wir optimieren unsere Sponsoring- und Eventauftritte.', 1,
                    7, 25, 'SUCCESSFUL', 26, '2024-03-19 11:52:50.589197', 2);
            INSERT INTO okr_pitc.objective
            VALUES (1084, '', '2024-06-17 15:32:42.47863', 'We Are Calibrating', 1, 7, 24, 'SUCCESSFUL', 36,
                    '2024-03-19 13:14:17.045939', 2);
            INSERT INTO okr_pitc.objective
            VALUES (1075, '', '2024-06-17 22:37:58.553863',
                    'Planung für den Q1 aufgefüllt und neue Members rekrutiert.', 1, 7, 23, 'NOTSUCCESSFUL', 33,
                    '2024-03-19 10:12:31.793629', 3);
            INSERT INTO okr_pitc.objective
            VALUES (1081, 'Wir arbeiten daran, dass wir effizienter werden in unserem Daily Business.',
                    '2024-06-14 13:05:44.70548', 'Wir werden effizienter ⚙️', 1, 7, 21, 'SUCCESSFUL', 31,
                    '2024-03-19 13:04:25.07092', 4);
            INSERT INTO okr_pitc.objective
            VALUES (1071,
                    'Wir arbeiten strukturiert und geben Infos weiter, indem wir Infos in Gitlab erfassen. Was wir tun, richten wir an unserer Strategie und den Zielen aus, die wir uns gesetzt haben. Insbesondere wollen wir Puzzle für eine etwaige ISO27k1 Zertifizierung vorbereiten.',
                    '2024-06-18 08:32:00.523519',
                    'Unsere Tätigkeiten folgen der Strategie und bereiten uns auf ISO 27k1 vor', 1, 7, 18, 'SUCCESSFUL',
                    27, '2024-03-19 08:36:14.682794', 1);
            INSERT INTO okr_pitc.objective
            VALUES (1063, 'Themen: Inhalte, Marketing, Sales, Sponsoring, Events', '2024-08-05 21:14:10.641943',
                    'Wir schärfen «Digitale Lösungen» sowie unsere Events- und Sponsoring-Aktivitäten', 1, 7, 20,
                    'NOTSUCCESSFUL', 1001, '2024-03-18 15:56:23.573056', 7);
            INSERT INTO okr_pitc.objective
            VALUES (1079, '', '2024-06-17 15:29:57.731645', 'Wir steigern die Wirtschaftlichkeit von WAC', 1, 7, 24,
                    'NOTSUCCESSFUL', 36, '2024-03-19 11:57:49.718649', 6);
            INSERT INTO okr_pitc.objective
            VALUES (1073,
                    'Wir wollen so aufgestellt sein, dass auch bei Ferienabwesenheiten u.ä. die Tätigkeiten von /security weiterlaufen können.',
                    '2024-06-18 08:31:48.364712', 'Wir entwickeln /security weiter', 1, 7, 18, 'SUCCESSFUL', 27,
                    '2024-03-19 09:12:45.581936', 3);
            INSERT INTO okr_pitc.objective
            VALUES (1053, '', '2024-06-13 13:03:31.500959',
                    'Wir haben die /mobility Strategie kommuniziert, deren Umsetzung geplant und das Controlling aufgesetzt.',
                    1, 7, 15, 'SUCCESSFUL', 5, '2024-03-07 14:39:32.865826', 2);
            INSERT INTO okr_pitc.objective
            VALUES (1065, '', '2024-07-24 06:13:27.326914', 'Chole hole...', 1, 7, 23, 'SUCCESSFUL', 1002,
                    '2024-03-19 06:37:37.276552', 8);
            INSERT INTO okr_pitc.objective
            VALUES (1057, '', '2024-06-13 06:25:19.98919', 'Wir erhöhen die Kundenzufriedenheit', 1, 7, 26,
                    'SUCCESSFUL', 41, '2024-03-15 08:44:18.216665', 2);
            INSERT INTO okr_pitc.objective
            VALUES (1055, '', '2024-08-21 14:56:58.993085',
                    'Wir sind in den Bereichen BBT, /mobility und Anzahl FTE gut organisiert und haben die Basis für einen erfolgreichen Start in das neue Geschäftsjahr gelegt. ',
                    1, 7, 15, 'NOTSUCCESSFUL', 1002, '2024-03-07 14:43:59.102685', 8);
            INSERT INTO okr_pitc.objective
            VALUES (1051, '', '2024-06-13 06:33:24.611386',
                    'Wir bringen Nachhaltigkeit, Marketing und Strategie aufs nächste Level.', 1, 7, 14, 'SUCCESSFUL',
                    16, '2024-03-04 15:12:57.723467', 4);
            INSERT INTO okr_pitc.objective
            VALUES (1090, '', '2024-06-11 12:51:33.163952',
                    'Durch Kosteneinsparungen und Wachstum verbessern wir die Wirtschaftlichkeit.', 1, 6, 14, 'ONGOING',
                    16, '2024-06-04 08:16:36.494054', 2);


            --
    -- Data for Name: key_result; Type: TABLE DATA; Schema: okr_pitc; Owner: -
    --

            INSERT INTO okr_pitc.key_result
            VALUES (1227, NULL, '', NULL, NULL,
                    'Wir bringen die Auftragslage für Q1, GJ24/25 ans Trockene (Juli-Sep 2024)', 1, 1064, 1, 'ordinal',
                    '2024-03-19 13:26:40.990644', 'Aufträge identifiziert
    ', 'Offerten rausgelassen', 'Mind. 1 Vertrag abgeschlossen', 0, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1222, NULL, 'Inklusive Ownership der entsprechenden KR', '2024-03-19 13:57:07.99654', NULL,
                    'Team hilft mit OKR zu definieren für Q1 2024/2025', 1, 1081, 1, 'ordinal',
                    '2024-03-19 13:17:55.123749', '1 KR', '2 KR', '1 O inkl. min. 3 KR', 1, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1308, NULL, 'Gemessen wird die durchschnittliche Auslastung pro Monat.',
                    '2024-06-17 23:48:22.892735', NULL, 'Dev Praktikant/Junior angestellt und ausgelastet', 1, 1110, 1,
                    'ordinal', '2024-06-17 23:48:05.095926', 'Angestellt', 'Angestellt + 33% Auslastung im Q1',
                    'Angestellt + 66% Auslastung im Q1', 1, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1221, NULL, '', NULL, NULL, 'Wir haben einen Plan, wer wann warum an welchen Events teilnimmt', 1,
                    1063, 1, 'ordinal', '2024-03-19 13:17:36.759498', 'Workshop, Übersicht erstellen (/dev/tre-Wiki)
    ', 'In Workshop besprechen, Abgleich mit Markom,  Als Thema im Weekly etabliert
    ', 'Wir klären Sponsoring-Aktivitäten /dev/tre
    ', 0, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1223, NULL, '', '2024-04-22 09:43:59.407865', NULL,
                    'Wir steigern unsere Qualitätsstandards und Effizienz', 1, 1084, 1, 'ordinal',
                    '2024-03-19 13:18:37.434381', 'Knowledge Base Tool Frage ist geklärt',
                    'Knowledge Base PoC mit Grundstruktur ist erstellt', 'Members erfassen ihr Wissen ', 4, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1293, NULL, '', '2024-06-18 13:21:38.70695', NULL,
                    'Wir haben geklärt, an welchen Events im GJ 2024-25 /dev/tre einen Beitrag leistet', 1, 1105, 1,
                    'ordinal', '2024-06-17 12:08:29.409697',
                    'Wir erstellen einen Massnahmeplan zu d3 an Events. Wir estellen eine Arbeitsgruppe innerhalb d3 und klären die Verantwortlichkeit',
                    'Ein Beiträg an einem INTERNEN Events wurde geleistet.',
                    'Ein Beiträg an einem EXTERNEN Events wurde geleistet.', 2, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1280, NULL, '', NULL, NULL,
                    'Employer Branding - Wir erweitern die Jobsseite mit Videoinhalten.    ', 1, 1103, 1, 'ordinal',
                    '2024-06-17 11:10:03.541109',
                    'Wir erstellen ein Video-Konzept, für eine mögliche Ausschreibung und planen dies gemeinsam mit dem HR/Personalmarketing ein. ',
                    '1 Stellenausschreibung mit Videocontent ist online.  ',
                    '2 Stellenausschreibungen mit Videocontent ist online.', 0, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1294, NULL, '', '2024-06-19 07:25:28.760176', NULL,
                    'Wir haben ein Konzept, wie die absolute Verrechenbarkeit nachhaltig auf 75% steigern', 1, 1106, 1,
                    'ordinal', '2024-06-17 12:09:14.259086', 'Zieldefinition von Marcel aufnehmen
    Hypothesen sind geschrieben und validiert
    Hypothesen sind bewertet.
    Prüfen, ob die absolute Verrechenbarkeit wirklich der "Taktgeber" von d3 ist.', 'Massnahmekatalog ist basierend auf Hypothesen erarbeitet
    Massnahmen sind auf einen 2-3 Jahres Zeitplan gelegt
    Es ist definiert, wie die Verrechenbarkeit zukünftig in OKR geführt werden soll',
                    'Massnahmen und Roadmap sind mit GL-Body besprochen und validiert sowie dem LST vorgestellt', 4,
                    NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1234, NULL,
                    'Wir wollen entweder eine Praktikumsstelle oder eine Junior-Position besetzen und bis zum Quartalsende deren Auslastung und Rentabilität sicherstellen.',
                    '2024-03-25 11:48:39.045487', NULL,
                    'Praktikum oder Junior Position besetzt, Betreuungsstrukturen geklärt und kostendeckender Einsatz gesichert.',
                    1, 1075, 1, 'ordinal', '2024-03-25 11:47:27.808255',
                    'Praktikums- oder Juniorstelle besetzt (Vertrag unterzeichnet) und Betreuungsstrukturen geklärt',
                    'Commit plus Auslastung des neuen Members für GJ25/Q1 zu 75% definitiv',
                    'Target plus eine Verrechenbarkeit für GJ25/Q1 sichergestellt, welche die Kosten (Lohn, Betreuung, Onboarding) übersteigt (Kalkulation in den Check-ins ausgeführt)',
                    1, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1179, NULL, '', '2024-03-21 10:05:23.74659', NULL,
                    'Wir setzen unseren Fokus bei der Content Creation von Anfang an auf SEO. ', 1, 1077, 1, 'ordinal',
                    '2024-03-19 11:53:40.027952',
                    'Die duplizierten Meta-Titel und -Beschreibungen bei Blogpost und Referenzen sind korrigiert und bilden eine saubere Basis für den Wechsel in die neue Datenbank. ',
                    'Alle neuen Hauptseiten werden mit Yoast SEO auf orange (ok) oder grün (gut) in der SEO- und Lesbarkeitsanalyse gebracht. ',
                    'Die Keywords aus der SEO-Analyse sowie die Keywords der Divisions sind in den Content und das SEO-Tool eingeflossen.',
                    4, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1178, NULL, '', '2024-03-19 12:16:27.458864', NULL,
                    'Wir kennen den potentiellen Zielmarkt für Digitale Lösungen (individual Software-Projekte).', 1,
                    1051, 1, 'ordinal', '2024-03-07 10:00:18.741243',
                    'Eine erste Marktanalyse in Form eines Desk-Research ist erstellt und der Kerngruppe digitale Lösungen präsentiert.',
                    'Die Resultate der Desk-Research Analyse werden erweitert durch eine Telefonkampagne und so geprüft und erweitert.',
                    'Ein erstes Konzept für die Marktbearbeitung ist erstellt.', 4, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (110, 0, '', '2023-08-25 07:30:51.004594', 2, 'Wir finalisieren und publizieren unser Videokonzept',
                    1, 68, 1, 'metric', '2023-08-25 07:30:51.004594', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1142, NULL,
                    'Hinweis Target: darunter fällt auch, dass wir sagen, dass Joel und Pippo gar nicht verrechenbar sein müssen aufgrund meiner Vertretung. ',
                    '2024-03-19 12:21:17.500831', NULL,
                    'Wir haben einen Plan, wie wir uns längerfristig im Kernteam aufstellen wollen und entsprechende Massnahmen für die Umsetzung sind aufgegleist. ',
                    1, 1055, 1, 'ordinal', '2024-03-07 15:01:20.889002',
                    'Schwangerschaftsvertretung Nathalie (inkl. Unterstützung Pippo durch MarCom) sowie Verantwortlichkeit für den Innoprozess sind geklärt.  ',
                    'Langfristige Zusammenarbeit mit MarCom und Sales ist geklärt und allfälliger Wissenstransfer initiiert. Der Einsatz der verrechenbaren Pensen von jbl und ple für Q1 und Q2 GJ 24/25 ist geklärt. ',
                    'Wir haben Varianten definiert, wie wir uns ab Januar 2025 im Kernteam organisieren wollen. Dazu gehören die Aufteilung der verrechenbaren Pensen (PL Pensen) sowie mögliche neue Aufgaben von Nathalie. ',
                    4, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1285, NULL, '', '2024-06-18 12:29:44.298538', NULL,
                    'Wir gewinnen einen Indiv. Neukunden für eine Umsetzung im Jahr 2025', 1, 1099, 1, 'ordinal',
                    '2024-06-17 11:22:49.625032', 'Sales Kampagne erstellt,2 Angebote gestellt ', '1 Kunde gewonnen',
                    '2 Kunden gewonnen', 3, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1156, NULL, 'MVP ist erfolgreich implementiert und der SAC ist äusserst zufrieden ',
                    '2024-03-19 13:29:20.614635', NULL, 'Wir sind im Projekt SAC Features Ready per 30.6.2024', 1, 1057,
                    1, 'ordinal', '2024-03-15 09:55:07.848663',
                    'SAC ist der Ansicht, dass wir unser Bestes geben und ist zufrieden.',
                    'Projekt ist in time sowie in budget ', '-', 4, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1155, NULL, 'im Rahmen unserer Strategie bezüglich Hitobito haben wir immer noch einen entscheidenden schwarzen Fleck, die Qualität der Software. Hier sind einige Indizien:

        Die Wartungsaufwände sind deutlich höher als das Budget vorsieht.
        Es gibt viele Bugs
        Die Nacharbeitsaufwände bei Releasen sind hoch und werden meist auf eigene Kosten durchgeführt.
        Entwicklungen sind tendenziell aufwändig und werden oft von Kunden hinterfragt.
        Schätzungen weichen häufig stark von den tatsächlich geleisteten Stunden pro Ticket ab, was schwer zu verstehen ist und auch Kunden im Nachhinein schwer zu verrechnen ist.
        Die Software ist bereits 10 Jahre alt.
        Die Architektur und die Wartungsstruktur sind sehr komplex. -> evtl. Grund für die hohen Kosten
        Performance Probleme zb bei Exporten
        Wir sind stark von zwei Entwicklern abhängig, die Hitobito und die Umgebung mehr oder weniger kennen.
    ', '2024-03-19 13:22:17.347132', NULL, 'Wir steigern die Qualität von Hitobito', 1, 1059, 1, 'ordinal',
                    '2024-03-15 09:46:20.41382', 'Kick off Techbaord und PL durchgeführt ',
                    'Analyse druch Techboard ist durchgeführt ', 'Erste Masnsahmen sind eingeleitet ', 5, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1229, NULL, 'Coop-KR mit /mobilty', '2024-03-19 13:39:31.016217', NULL,
                    'Weitere /mobility Members sind auf GO im Einsatz.', 1, 1080, 1, 'ordinal',
                    '2024-03-19 13:39:04.029841', 'Angebotsstory in Kooperation mit CI/CD erarbeitet.',
                    'Mind. 1 zusätzlicher Member auf GO Auftrag fix eingeplant (Auftrag/Ressource ist vom Kunden bestellt)',
                    'Mind. 1 neuer Go Auftrag gewonnen.', 1, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1176, NULL, '', NULL, NULL, 'Wir sind zertifizierte Partner eines Hyperscalers und generieren Opportunities für dieses Geschäftsfeld.
    ', 1, 1076, 1, 'ordinal', '2024-03-19 11:25:34.256824', 'Wir generieren 3 Opportunities für AWS oder GCP in diesem Quartal.
    ', 'Wir gewinnen einen Auftrag, der Monthly Recurring Revenue bringt.
    ', 'Die Partnerzertifizierung für AWS oder GCP ist abgeschlossen.
    ', 0, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1305, NULL, '', NULL, NULL, 'Junior/Praktikant Betreuung und Planung fix', 1, 1109, 1, 'ordinal',
                    '2024-06-17 23:42:08.88023', 'Junior/Praktikant angestellt (Vertrag unterschrieben)',
                    '+ Betreuung sichergestellt und Planung zu 33% gesichert', '+ Planung zu 66% gesichert', 0, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1152, NULL, 'wir führen ein erfolgreiches Communitymeeting durch', '2024-05-13 09:30:37.840891',
                    NULL, 'das nächste Communitymeeting ist erfolgreich durchgeführt', 1, 1057, 1, 'ordinal',
                    '2024-03-15 08:44:18.222663', 'Agenda ist min. 2 Wochen vorher versendet',
                    'es melden sich 20 Teilnehmer an ', '> 20 Teilnehmer nehmen teil an Communitymeeting', 6, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1281, NULL, '', NULL, NULL, 'SEO - Wir erhöhen die organische Suchsichtbarkeit der neuen Website. ',
                    1, 1103, 1, 'ordinal', '2024-06-17 11:10:35.044833',
                    'Wir optimieren die Website-Inhalte und die Meta-Tags, um die Platzierung in den organischen Suchergebnissen zu verbessern. ',
                    'Die organische Suchsichtbarkeit der neuen Website ist um mindestens 10% im Vergleich zum Vorjahr gestiegen.  ',
                    'Die organische Suchsichtbarkeit der neuen Website ist um mindestens 20% im Vergleich zum Vorjahr gestiegen.',
                    0, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1245, NULL, '', '2024-06-04 10:04:42.439976', NULL,
                    'Der OrgDev Branch ist mit genügend und den passenden Members besetzt.', 1, 1092, 1, 'ordinal',
                    '2024-06-04 09:15:13.402123', 'Wir finden neue Branch Members.',
                    'Wir finden eine neue Branch Owner:in.',
                    'Alle Branch Rollen sind besetzt und Members, Division Owner, Member Coach und GL sind im Branch vertreten.',
                    4, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1198, NULL, '', NULL, NULL,
                    'Wir dokumentieren unsere Arbeit so, dass Tasks bei Bedarf übergeben werden können.', 1, 1073, 1,
                    'ordinal', '2024-03-19 12:30:29.823536', 'Alle Tasks sind als Issues erfasst und beschrieben.',
                    'Es gibt wöchentliche Updates zu den Tasks.',
                    'Die Updates sind so formuliert, dass ein anderes Teammitglied übernehmen kann.', 0, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1139, NULL, '', '2024-03-28 17:05:33.135138', NULL,
                    'Bis Ende Q4 GJ 23/24 ist die Lehrlingsausbildung im Java Bereich fürs neue Semester organisiert.',
                    1, 1055, 1, 'ordinal', '2024-03-07 14:51:27.444282',
                    'Betreuungspersonen und  Projekte fürs erste Quartal sind definiert.',
                    'Fürs erste Semester sind Projekte und Verantwortlichkeiten   definiert und Ressourcen verbindlich eingeplant.',
                    '4.Lj-Lernender ist auf Kundenprojekt für mind. 3 Monate eingeplant.', 6, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1309, NULL, '', NULL, NULL, 'Technologische Fokusthemen gesetzt', 1, 1111, 1, 'ordinal',
                    '2024-06-17 23:53:55.517276', 'Themen identifiziert', '+ ...', '+ ...', 0, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1250, NULL,
                    'Wir arbeiten weiter an der Basis der Marktopportunität mit dem Ziel mindestens eine der Partnerschaften zu den Hyperscalern abzuschliessen. Zudem haben wir einen Plan für die Vermarktung und Go-To-Market Aktivitäten bis Ende Geschäftsjahr.',
                    '2024-06-18 11:45:37.131116', NULL, 'MO Hyperscaler
    Wir kommunizieren eine Partnerschaft mit einem Hyperscaler.', 1, 1091, 1, 'ordinal', '2024-06-04 09:19:42.872314', '- Wir führen eine Liste der Opportunities bei AWS und GCP.
    - Wir prüfen und planen externe Cloud Lunches AWS und GCP.
    - Wir prüfen Teilnahme und Sponsoring-Möglichkeit am AWS Summit Zürich vom 4.9.24.', '- Wir drehen ein "Mate mit"-Hyperscaler Video.
    - Wir haben einen Vermarktungsplan und eine Go-To-Market Strategie mit Schwerpunkten bis Ende Geschäftsjahr.
    - Wir kommunizieren die AWS Partnerschaft am Puzzle up! 2024.', '- Wir schliessen Partnerschaft mit GCP ab.', 7, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1340, NULL, '', '2024-07-01 09:06:47.6577', NULL, 'MO Platform Engineering Beitrag definieren', 1,
                    1119, 1, 'ordinal', '2024-06-19 07:52:15.254026', 'Aktiver Austausch mit Lead/Kernteam ',
                    'Roadmap für /security-Beitrag ist definiert ', '/security ist bereit für Aufträge im MO ', 1,
                    NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1350, NULL, '', '2024-06-27 07:52:30.699336', NULL,
                    'Das CRM Projekt hat Fahrt aufgenommen und kommt voran.', 1, 1092, 1, 'ordinal',
                    '2024-06-27 07:50:59.301335', 'Das Projektteam ist definiert und eine erste Evaluation der möglichen Odoo SaaS Anbieter ist gemacht worden.
    ', 'Eine erste Installation unseres neuen CRM läuft und ein erstes Datenset aus Highrise wurde migriert.

    ', 'Die Anpassungen von PTime und anderen Umsystemen wurden für das Testsystem bereits testweise umgesetzt.

    ', 1, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1270, NULL,
                    'Unsere Wartungskosten sind nicht gedeckt. es wird deshalb ein Konzept erstellt und anschliessend neue Verträge SLA ausgearbeitet und verhandelt',
                    '2024-06-18 13:55:59.148846', NULL, 'wir erhöhen den Ertrag der Pauschalen (Betrieb, Wartung und Support)
    Jahresziel 2025 -> Rentabilität', 1, 1099, 1, 'ordinal', '2024-06-17 06:46:02.083382',
                    'Analyse der Umgebungs komplexität estellt ', 'Wartungskosten pro Kunde sind definiert ',
                    '1. Fassung SLA erarbeitet ', 4, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1218, NULL, '', NULL, NULL, 'Wir finalisieren unsere Bereichsstrategie', 1, 1084, 1, 'ordinal',
                    '2024-03-19 13:16:15.130716',
                    'Unsere Bereichsstrategie ist finalisiert und mit dem Team und unserem SUM Buddy besprochen',
                    'Bereichsstrategie ist mit Feedback von Team und GL erweitert und Massnahmeplan zur Umsetzung ist erstellt',
                    'Bereichsstrategie ist ready und einzelne Massnahmen wurden umgesetzt', 0, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1205, NULL,
                    'Ziel ist, /security als interne "Security-Consultants" zu etablieren, welche für Projekte angefragt werden können.',
                    '2024-03-19 13:15:14.194229', NULL,
                    'Wir unterstützen bestehende Kunden-Projekte mit Security Beratung.', 1, 1072, 1, 'ordinal',
                    '2024-03-19 12:49:22.575428', 'Wir haben bei drei Projekten unsere Unterstützung angeboten.',
                    'Wir durften bei einem Projekt (potenziell unverrechenbar) mithelfen und haben dadurch eine Kundenreferenz.',
                    'Wir konnten bei zwei Projekten eine Referenz erhalten oder verrechenbar bei einem Projekt mithelfen.',
                    2, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1207, NULL, 'Es werden nur Zertifizierungen gezählt, welche den Partnerstatus unterstützen.',
                    '2024-03-19 13:08:07.249113', NULL, 'Weitere Members von /mid erreichen eine Cloud-Zertifizierung',
                    1, 1080, 1, 'ordinal', '2024-03-19 13:05:46.580143', '+1', '+2', '+5', 1, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1224, NULL, '', NULL, NULL, 'Wir klären Sponsoring-Aktivitäten /dev/tre
    ', 1, 1063, 1, 'ordinal', '2024-03-19 13:23:26.253225', 'Ideen gesammelt, kategorisiert und eingeordet - Ideen im Team besprochen (Weeklys)
    ', 'Liste der Sponsoring-Aktivitäten finalisiert. Abgleich mit /ux, /mobiity und /ruby – Bereichtsübergreifende Zusammenfassung
    ', 'Kommunikation an LST und Markom
    ', 0, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1324, NULL,
                    '/ux ist für digitale Lösungen ein Zudiener (gemäss Absprache SUM) - als Puzzlestück helfen wir mit ein attraktives Angebot zu erstellen. ',
                    '2024-06-18 13:06:35.731107', NULL, 'Wir unterstützen die Initiative "Digitale Lösungen"', 1, 1112,
                    1, 'ordinal', '2024-06-18 13:03:03.257927', '- Wir schliessen die Marktanalyse Digitale Lösungen ab.
    - Wir sind an jedem Digitale Lösungen Meeting dabei und bringen unsere Sicht ein.',
                    'Gemeinsames Statement zusammen mit Dev-Bereichen erreicht. ',
                    'Artefakte Konzeption / UX sind formuliert. ', 2, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1292, NULL, 'Marktopportunitäten GJ 24/25 (Codi): https://codimd.puzzle.ch/0-eK0IubTRKlbTvSvAtwxQ',
                    '2024-06-18 12:43:29.167228', NULL,
                    'Wir klären in welchen Marktopportunitäten wir aktiv sein könnten', 1, 1105, 1, 'ordinal',
                    '2024-06-17 12:08:11.833769', '- Die MOs GJ 24/24 sind studiert und hinsichtlich Unterstützung durch d3 validiert
    ', '- Falls Unterstützung durch d3 möglich, ist diese Niedergeschriebnen,', '- Die d3-Mitwirkung an dem MOs ist kommuniziert an/ besprochen mit:
    - Sales (Yup)
    - GL Coach (Dänu)', 1, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1192, NULL,
                    'Ziel ist es, Awareness hoch zu halten, und den Devs potenziell wichtiges Wissen zu vermitteln. Das Stretch Goal würde das dann auch gegen aussen präsentieren, um für Puzzle Werbung zu machen.',
                    '2024-03-19 13:15:54.102868', NULL,
                    'Wir machen Puzzle sicherer, in dem wir Security-Wissen teilen.', 1, 1072, 1, 'ordinal',
                    '2024-03-19 12:20:00.545823',
                    'Wir haben ein Tech-Kafi oder einen Blogpost zu Security-Themen organisiert.',
                    'Wir haben zwei solche Aktivitäten organisiert.',
                    'Wir durften das bei einem Kunden oder an einem externen Event präsentieren.', 3, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1129, NULL, '', '2024-03-07 10:51:43.645495', NULL,
                    'Wir haben unser Puzzle OS erweitert und das interne Audit zur Umweltzertifizierung bestanden.', 1,
                    1051, 1, 'ordinal', '2024-03-07 09:58:46.642752', 'Draft des erweiterten Puzzle OS erstellt.',
                    'Internes Audit erfolgreich bestanden.', 'ISO14001 in ISO9001 vollständig integriert.', 3, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1279, NULL, '', NULL, NULL,
                    'Social Media – Wir erstellen einen Prompt für LinkedIn-Posts auf ChatGPT in unserem Tone of Voice.  ',
                    1, 1103, 1, 'ordinal', '2024-06-17 11:09:29.453559',
                    'Prompt konzipiert und neue ChatGPT-Instanz erstellt.  ',
                    'Die Anwendung des Prompts ist im Team verankert und bei Daily Tasks im Einsatz. ',
                    'Technische LinkedIn-Posts erreichen 10% mehr Impressions im Vergleich zum vorderen Quartal.', 0,
                    NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1138, NULL, 'Zusätzlich zu Go und Kafka ist geklärt, welche zusätzlichen Technologien bei /mobility gefördert und angeboten werden sollen.
    https://codimd.puzzle.ch/UwQ1DyuyQFSXqMoI-h-M4w?both', '2024-05-02 15:42:17.855525', NULL, 'Wir haben den Schwerpunkt für weitere Technologien definiert und Angebot ausgearbeitet.
    ', 1, 1054, 1, 'ordinal', '2024-03-07 14:50:34.285993',
                    'Eine Auswahl der Technologien ist mit dem Techboard getroffen und mit interessierte Members dafür gewonnen.',
                    'Die ausgewählten Technolgien sind am Markt validiert (mit unserem Sales und externen Spezialisten)',
                    'Eine Go To Market Strategie ist mit dem CTO definiert.', 9, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1140, NULL, 'https://codimd.puzzle.ch/GoNewTech
    Zur Zeit ist bereits Martin Käser auf GO mit CI/CD im Einsatz.', '2024-05-02 15:44:26.824731', NULL,
                    'Weitere /mobility Members sind auf GO im Einsatz.', 1, 1054, 1, 'ordinal',
                    '2024-03-07 14:54:19.127344', 'Angebotsstory in Kooperation mit CI/CD erarbeitet.',
                    'Mind. 1 zusätzlicher Member auf GO Auftrag fix eingeplant (Auftrag/Ressource ist  vom Kunden bestellt)',
                    'Mind. 1 neuer Go Auftrag gewonnen.', 8, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1247, NULL, '', '2024-06-18 12:53:45.322692', NULL,
                    'Die Rahmenbedingungen und Ziele für den nächsten Entwicklungsschritt unserer Organisation verabschiedet und die Erwartungen der Divisions an die Organisation der Zukunft sind abgeholt.',
                    1, 1092, 1, 'ordinal', '2024-06-04 09:16:14.499697',
                    'Die Rahmenbedingungen und Ziele sind definiert und sind durch die GL verabschiedet.', '80% der LST Members wurden zu den Erwartungen an die Organisation der Zukunft befragt und deren Rückmeldungen wurden ausgewertet.
    ',
                    'Aus den Auswertungen der Ziele und Erwartungen wurde ein erster Plan für Anpassungen an der Organisation von Puzzle erstellt.',
                    3, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1216, NULL, 'Wir stellen eine*n neuen Member ein', '2024-03-19 13:58:01.447212', NULL,
                    '/mid wächst', 1, 1083, 1, 'ordinal', '2024-03-19 13:14:15.54267', 'Stelle ausgeschrieben',
                    'Mindestens 1 Person auf der Shortlist', 'Unterschrift', 1, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1131, NULL, '', '2024-03-07 10:51:32.486278', NULL,
                    'Die Rollen und Tätigkeiten im Marketing und Business Development sind in den Prozessen verankert. ',
                    1, 1051, 1, 'ordinal', '2024-03-07 10:08:12.083562', 'Analyse und Auslegeordnung der nötigen Tätigkeiten und Aufgaben im Bereich Marketing und Business Development gegenüber P32&P14.
    Bedürfnisse der Divisions gesammelt und mit Auslegeordnung abgeglichen.', 'Wir haben die nötigen Aufgaben und Tätigkeiten in den Prozessen integriert.
    Vorhandene Lücken sind erkannt und es ist geklärt wer den Lead übernimmt diese Lücken zu füllen.',
                    'Wir haben alle Aufgaben einer konkreten Person bzw. einem Team zugeordnet und die Aufgaben werden entsprechend wahrgenommen',
                    3, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1132, NULL, '', '2024-03-08 08:52:31.520158', NULL,
                    'Wir überprüfen und erweitern den Strategie-Prozess und definieren die Marktopportunitäten für das neue Geschäftsjahr.',
                    1, 1051, 1, 'ordinal', '2024-03-07 10:12:58.772802',
                    'Wir haben die bestehenden Marktoppounitäten gereviewed und uns entschieden, welche weiterverfolgt werden.',
                    'Die neuen Marktopportunitäten für das Geschäftsjahr 2024/2025 sind erarbeitet.',
                    'Der Strategie ist  mit einer Mittelfristsicht erweitert und die Prozesserweiterung ist im Puzzle OS dokumentiert.',
                    4, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1215, NULL, '', NULL, NULL,
                    'Wir pimpen unser Visualisierungsworkshop-Angebot und führen einen Event mit zahlenden Teilnehmern durch',
                    1, 1082, 1, 'ordinal', '2024-03-19 13:13:59.057687',
                    'Unser überarbeitetes und verbessertes Angebot ist publiziert',
                    'Wir erreichen potenzielle Teilnehmer und haben für ein konkretes Datum mindestens 4 bestätigte Teilnehmer',
                    'Wir haben mindestens einen Visualiserungsworkshop mit 4 zahlenden Teilnehmer durchgeführt', 0,
                    NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1182, NULL, '', '2024-03-19 12:44:42.388359', NULL,
                    'Wir verabschieden ein Sponsoringkonzept, dass die Vielfältigkeit und Breite von Puzzle widerspiegelt und eine saubere Planung über das gesamte GJ sicherstellt.',
                    1, 1078, 1, 'ordinal', '2024-03-19 11:56:24.524661',
                    'Neues Vorgehen für Sponsorings ist definiert und am April-Monthly präsentiert.',
                    'Planungsmeeting mit DO und Sales hat stattgefunden.', 'Sponsoringplanung GJ 24/25 verabschiedet.',
                    1, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1283, NULL, '', '2024-06-18 12:29:17.811568', NULL,
                    'Wir wissen, wer für Talks zu welchen Themen zur Verfügung steht, haben diese Information dokumentiert und evaluieren Speaking Opportunities. ',
                    1, 1104, 1, 'ordinal', '2024-06-17 11:13:06.922508',
                    'Dokumentation von mindestens 2 Members und ihren spezifischen Themenbereichen für Talks.',
                    'Dokumentation von mindestens 4 Members und ihren spezifischen Themenbereichen für Talks.',
                    '2 Speaking Opportunities fürs GJ 24/25 ', 3, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1339, NULL, '', NULL, NULL, 'MO Digitale Lösungen Beitrag definieren', 1, 1119, 1, 'ordinal',
                    '2024-06-19 07:51:31.874605', 'Aktiver Austausch mit Lead/Kernteam ',
                    'Roadmap für /security-Beitrag ist definiert ', '/security ist bereit für Aufträge im MO ', 0,
                    NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1307, NULL, 'Gemessen wird die durchschnittliche Auslastung pro Monat.', NULL, NULL,
                    'System Engineer angestellt und ausgelastet', 1, 1110, 1, 'ordinal', '2024-06-17 23:47:03.817687',
                    'Angestellt', 'Angestellt + 33% Auslastung im Q1', 'Angestellt + 66% Auslastung im Q1', 0, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1263, NULL, '', '2024-07-04 07:13:46.516497', NULL,
                    'Durch die Marktvalidierung ist die strategische Stossrichtung New Tech geschärft', 1, 1095, 1,
                    'ordinal', '2024-06-13 14:28:33.091655',
                    'Marktanalyse für die neuen Technologien ist erfolgt und mit CSO und CTO validiert.',
                    'Die Short List der neuen Technologien ist priorisiert und die nächsten Schritte sind definiert.',
                    'In einer der drei höchst priorisierten Technologien ist ein Member in einem bezahlten Auftrag (ohne GO).',
                    3, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1208, NULL, '', NULL, NULL,
                    'Wir überarbeiten unseren Actionplan zur Steigerung unserer Rentabilität mit konkreten Massnahmen',
                    1, 1079, 1, 'ordinal', '2024-03-19 13:06:55.492661',
                    'Actionplan überarbeitet und mindestens 5 neue konkrete Massnahmen eruiert und mit SUM Buddy abgesprochen',
                    'Actionplan mit Inputs Sum Buddy überarbeitet und vom LST Team abgenommen',
                    'Feedback von LST Team eingearbeitet und erste Massnahmen umgesetzt', 0, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1254, NULL,
                    'Die Marktanalyse für die Digitalen Lösungen sind abgeschlossen und die Schlussforgerungen liegen vor. Wir führen einen Workshop durch, um Angebot und Schwerpunkte zum Thema Digitale Lösungen zu definieren. Ein Vermarktungsplan und eine Go-To-Market Strategie ist erarbeitet und hilft uns bei der weiteren Marktbearbeitung.',
                    '2024-06-20 06:28:33.545396', NULL, 'MO Digitale Lösungen
    Die Schlussfolgerungen aus der Marktanalyse sind vorhanden und die Schwerpunkte und das Angebot sind definiert.', 1,
                    1091, 1, 'ordinal', '2024-06-04 14:32:47.053686', '- Die Marktanalyse ist abgeschlossen und die Resultate aus der Kampagne von Carlos sind eingeflossen.
    - Die Schlussfolgerungen aus der Analyse sind vorhanden und wurden mit allen DO einer Software-Entwicklungsdivision diskutiert und abgenommen.',
                    '- Wir definieren ein Angebot und Schwerpunkte der Neuausrichtung für das Thema Digitale Lösungen.', '- Wir haben einen Vermarktungsplan und eine Go-To-Market Strategie für die Digitalen Lösungen.
    - Wir haben drei Leads für Digitale Lösungen.', 2, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (108, 38, '', '2023-07-05 08:14:03.764364', 40,
                    'Wir steigern die Öffnungsrate des Newsletters von 38% auf 40%', 1, 66, 1, 'metric',
                    '2023-07-05 08:14:03.764364', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1189, NULL, '', NULL, NULL, 'Puzzle hat einen Image Lifecycle Prozess.', 1, 1071, 1, 'ordinal',
                    '2024-03-19 12:11:24.226837',
                    'Es gibt automatisierte Alerts wenn Images altern, welche die Verantwortlichen informieren.',
                    'Es gibt einen Prozess, welcher Teil des PuzzleOS oder QM ist, um Images aktuell zu halten.',
                    'Es gibt keine Images mehr, welche über 3 Monate alt sind.', 0, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1190, NULL, '', '2024-03-19 13:04:58.461589', NULL,
                    'Wir haben eine Trainingskampagne vorbereitet, welche den Devs einen Mehrwert bietet.', 1, 1072, 1,
                    'ordinal', '2024-03-19 12:13:59.94989',
                    'In Zusammenarbeit mit Devs und/oder Sec Champions haben wir ein Thema definiert und dazu passende Materialien oder Ausbildner gefunden.',
                    'Wir haben eine Trainingskampagne zusammengestellt und mit den Security Champions validiert.',
                    'Die Kampagne ist lanciert.', 1, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1276, NULL, '', '2024-07-04 07:21:54.944384', NULL,
                    'MO AI & MLOps: Ein AI Use Case ist als MLOps Show Case umgesetzt', 1, 1096, 1, 'ordinal',
                    '2024-06-17 07:02:23.912373', 'Use Case ist umgesetzt', '+ Zwei Blogs sind publiziert',
                    '+ ein zahlender Kunde für den Use Case bzw. dessen Implementierung vorhanden', 1, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1273, NULL, '(Wir leisten die vordefnierten Stunden, aber der SAC bestimtm der Inhalt)

    MVP (Minimal viable product) MMP (Minimal Marketable product) ist erfolgreich implementiert und der SAC ist äusserst zufrieden ',
                    '2024-06-18 12:30:44.274657', NULL, 'Wir  sind mit dem SAC Portal 1.0 erfolgreich und halten Meilensteine ein
    ', 1, 1100, 1, 'ordinal', '2024-06-17 06:46:15.178652', 'MVP, Machbarkeit bestätigt ', 'MMP Feature complete ',
                    'Ende Nachkorrekturen ', 3, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1135, NULL, '', '2024-03-28 17:00:29.658597', NULL,
                    'Technologieshift Kafka: Wir haben Knowhow durch Weiterbildung bestehender Members und Neuanstellung soweit gesteigert, dass wir Kafka Aufträge anbieten können.',
                    1, 1054, 1, 'ordinal', '2024-03-07 14:45:33.292335',
                    'Strategieziel definiert, Stelleninserat aufgeschaltet, internes Team definiert.',
                    'Schulung für internes Team geplant, 1 Kafka Spezialist angestellt und Angebot auf Webseite publiziert.',
                    'Mind. 1 Auftrag für Kafka Team gewonnen', 3, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1316, NULL,
                    'Analyse der SoMe-Kampagne und Traffic Website von April-Juni - rollierend erweitert mit Erkenntnissen aus der neuen SoMe-Kampagne.',
                    '2024-06-18 12:47:59.661365', NULL,
                    'Wir wissen, wie wir unsere Kund:innen richtig ansprechen und welche Inhalte interessieren. ', 1,
                    1114, 1, 'ordinal', '2024-06-18 12:12:35.534198',
                    'Konzept Auswertung erstellt, Erkenntnisse gewonnen',
                    'Erkenntnisse sind im Team kommuniziert und diskutiert.',
                    'Wir wenden unser gewonnenes Wissen auf die neuen Artikel an. ', 1, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1184, NULL, '', '2024-05-16 06:38:13.198147', NULL,
                    'Wir setzen auf nachhaltige, lokale Give Aways bei unseren Eventauftritten. ', 1, 1078, 1,
                    'ordinal', '2024-03-19 11:57:43.952127',
                    'Wir erstellen ein Give Away-Konzept, das intern allen bekannt ist. ',
                    'Wir produzieren ein nachhaltiges Give Away für den Puzzle up! 2024.  ',
                    'Wir setzen das nachhaltige Give Away beim Puzzle up! interaktiv in Szene und motivieren die Teilnehmenden, sich mit der Nachhaltigkeit auseinander zu setzen.',
                    2, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1211, NULL,
                    'Wir wollen das aktuelle Marktangebot ausweiten und publizieren. Den Inhalt und die Form erarbeiten wir zusammen mit Markom.',
                    '2024-03-19 13:09:21.101073', NULL, 'Cloud-Marktangebot von /mid ausgeweitet und definiert', 1,
                    1080, 1, 'ordinal', '2024-03-19 13:08:31.245033', 'Inhalte für die neue Webseite ist erstellt',
                    'Bereit für Publishing auf der neuen Webseite', 'Inhalt ist auf der neuen Webseite publiziert', 4,
                    NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1209, NULL, 'Backlog ist ready für Auslastungslücken zu füllen', '2024-03-19 13:49:15.656512', NULL,
                    'OV & Rohrmax Backlogs sind bereinigt', 1, 1081, 1, 'ordinal', '2024-03-19 13:08:05.287415',
                    'Backlogs wurden aufgeräumt',
                    'Alle Tickets sind priorisiert, so dass sich die entsprechenden Members jeder Zeit ein Ticket schnappen können', 'Alle Tickets sind ready für Action
    (Kategorie, Definition of done, Aufwandschätzung)', 1, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1346, NULL, 'Der Durchschnitt des Ertrags aus Leistungsverrechnungen pro FTE pro Monat ist gleich gut wie vor einem Jahr.
    Als Basis dienen Zahlen vom Q1 GJ2023/2024 (Juli 2023 bis August 2023). Durchschnitt: TCHF 16.1', NULL, NULL,
                    'Der Ertrag pro FTE pro Monat bleibt gleich gut', 1, 1118, 1, 'ordinal',
                    '2024-06-25 15:01:03.794902', 'Der Ertrag ist <95% der Vergleichsperiode vor einem Jahr: TCHF 15.3',
                    'Der Ertrag ist gleich wie in der Vergleichsperiode vor einem Jahr: TCHF 16.1',
                    'Der Ertrag ist um 2.5% beser als in der Vergleichsperiode vor einem Jahr: TCHF 16.5', 0, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1334, NULL, 'Wichtig: Die Prozesse, welche am stärksten mit Risiken und Entwicklung zu tun haben.',
                    NULL, NULL, 'Sicherheitsthemen sind in nicht-P16 Prozessen überarbeitet.', 1, 1116, 1, 'ordinal',
                    '2024-06-18 13:53:04.102457', 'Problemstellen sind identifiziert.',
                    'Wichtige Prozesse sind überarbeitet.', 'Alle Prozesse sind überarbeitet.', 0, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1326, NULL, 'Pro Flavour (micro/standard/plus WIP) ist ein Blueprint am Entstehen, welcher alle Platform Engineering Pfeiler abdeckt:
    - Kubernetes
    - CI/CD
    - Observability
    - Security & Compliance
    (- Dev Experience)
    Die Flavours sollen die Ausbaustufen einer Platform darstellen.', '2024-06-25 14:31:04.035038', NULL,
                    'Modulare PE Blueprints sind in Arbeit 🙂', 1, 1098, 1, 'ordinal', '2024-06-18 13:10:45.416404', 'Die 3 Flavours haben einen passenden/kreativen Namen.

    Pro Flavour sind die Komponenten bestimmt.', 'Pro Flavour ist ein Draft eines modularen Blueprints erstellt.
    Die PO-Rolle ist besetzt.', 'Pro Flavour ist ein fertiger modularer Blueprint erstellt.', 10, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1146, NULL, '', '2024-03-21 12:52:17.969467', NULL,
                    'Ein Controlling System für das Tracken der strategischen Massnahmen ist implementiert.', 1, 1053,
                    1, 'ordinal', '2024-03-07 15:03:39.297069', 'Das Controlling System ist definiert.',
                    'Das Controlling System ist implementiert.',
                    'Alle strategischen Massnahmen werden bereits aktiv darin getrackt', 3, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1332, NULL,
                    'Im neuen Geschäftsjahr (2024/2025) halten x /mid-Members mindestens einen Talk. Dieses KR dient der Organisation dieser Talks.',
                    '2024-06-25 15:15:15.997245', NULL, '/mid-Talks sind geplant 🙂', 1, 1120, 1, 'ordinal',
                    '2024-06-18 13:29:32.281768',
                    'Die Themen und Speaker für mögliche Talks sind definiert und potenzielle Events aufgelistet.',
                    'Commit erreicht & vier Talks sind definiert (Thema, Speaker, Event), wovon zwei bereits fix eingeplant sind.',
                    'Alle Talks sind definiert, wovon vier bereits fix eingeplant sind und ein zusätzlicher Talk bereits durchgeführt wurde.',
                    5, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1188, NULL, 'Coop-KR mit /marcom', '2024-03-19 14:17:47.003644', NULL,
                    'Wir kommunizieren die /mid Journey intern und extern', 1, 1080, 1, 'ordinal',
                    '2024-03-19 12:07:53.421123', 'Es wurden drei Massnahmen aus dem Action-Plan umgesetzt',
                    'Es wurden alle Massnahmen aus dem Action-Plan umgesetzt',
                    'Es wurde mindestens ein neuer Lead generiert', 5, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1348, NULL, '', '2024-06-27 07:37:38.154735', NULL,
                    'In der Divisionsrentabilität haben wir die Zielwerte für Divisions definiert und eingeführt.', 1,
                    1090, 1, 'ordinal', '2024-06-04 09:09:17.565579',
                    'Die Jahreszielwerte sind definiert und eingeführt.',
                    'Die Quartalszielwerte sind definiert und eingeführt.',
                    'Die Quartalszielwerte werden von 70% der Divisions erreicht.', 4, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1278, NULL, 'Kleine Bubble, die wir beeinflussen können', '2024-06-18 12:50:23.599892', NULL,
                    'Unsere Wir vertiefen und etablieren die drei Fokusthemen “Ziele”, “Verantwortung” und “Beteiligung an Entscheiden”',
                    1, 1102, 1, 'ordinal', '2024-06-17 10:20:09.572591', 'Die 3 Fokusthemen wurden im Quartalsworkshop weiterentwickelt:
    - Selbstorganisation mit OrgDev, bzw. Puzzle abstimmen, bzw. darauf aufbauen, z.B.
    - Für die Fokusthemen etablierte Prozesse/ Modelle nehmen (Z.B. DACI für Entscheidungen)

     niedergeschrieben.', 'Die Regeln zu den 3 Fokusthemen sind erarbeitet und niedergeschrieben.

    Massnahmen und Messpunkte zu den 3 Fokusthemen sind definiert ',
                    'Die Resultate sind dem GL-Body und dem LST präsentiert', 6, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1187, NULL, '', NULL, NULL,
                    'Wir haben ein Monitoring in unserem Netzwerk, um potenzielle Bedrohungen zu erkennen.', 1, 1071, 1,
                    'ordinal', '2024-03-19 12:07:20.112061', 'Suricata od. equiv. loggt Alerts.',
                    'Wir haben einen Prozess, um diese Resultate zu verarbeiten.',
                    'Es gibt automatisierte Alerts und Management dieser Information.', 0, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1267, NULL, 'MO "Ditigale Lösungen: Codi https://codimd.puzzle.ch/fGy0RNSzTVqVlUz80dw_Mw
    ', '2024-07-04 07:24:45.374733', NULL,
                    'Unser (/mobility) Mitwirken und unsere Sales-Erwartungen zur Marktopportunität (MO) "Digitale Lösungen" sind formuliert und kommuniziert sowie mit den /dev- & /ux-Divisions abgeglichen.',
                    1, 1097, 1, 'ordinal', '2024-06-13 15:13:22.247492',
                    '/mobility-Mitwirken und -Sales-Erwartungen sind mit MO "Digitale Lösungen" abgeglichen und dokumentiert', '- /mobility-Mitwirken sind mit devtre, /ruby & /ux abgestimmt
    - allgemeine /dev- & /ux-Mitwirken sind formuliert
    - /mobility-Sales-Erwartungen sind mit /devtre, /ruby & /ux abgestimmt
    - allgemeine /dev- & /ux-Sales-Erwartungen sind mit /devtre, /ruby & /ux abgestimmt', 'Inputs und Sales-Erwartungen seitens /dev- /ux-Divisions sind:
    an Sales (Yup) kommuniziert & besprochen
    dem Kernteam MO "Digitale Lösungen" präsentiert
    ', 6, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1175, NULL, '', NULL, NULL, 'Rekrutierung: Anstellung eines Senior System Engineers', 1, 1075, 1,
                    'ordinal', '2024-03-19 10:17:19.40042', '2 Kandidaten absolvieren Tech-Interview',
                    'Anstellung und vorgängige Validierung durch Team',
                    'Anstellung und mind. 50% Auslastung im Forecast für GJ25-Q1', 0, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1213, NULL, 'Zu allen unseren Fokus Themen haben wir eine Special Intrest Group (SIG).',
                    '2024-03-19 13:56:09.2245', NULL, 'SIG haben sich etabliert', 1, 1081, 1, 'ordinal',
                    '2024-03-19 13:11:04.296225', 'Pro Fokus Thema ist eine SIG definiert',
                    'Pro SIG ist ein RocketChat-Channel definiert und die entsprechenden Members hinzugefügt. Die Rahmenbedingungen sind geklärt und akzeptiert.',
                    'SIGs verankert -> FIXME: besser schärfen', 1, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1226, NULL, '', NULL, NULL,
                    'Es ist geklärt, wie Weiterbildungen im Security-Bereich angegangen werden.', 1, 1073, 1, 'ordinal',
                    '2024-03-19 13:26:23.172564', 'Mark hat seine Wunschvorstellungen formuliert.',
                    'Mit den Member Coaches ist geklärt, was und wie das ablaufen soll.',
                    'Die Members konnten bereits ihre Wünsche äussern und einplanen.', 0, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1274, NULL,
                    'Unser Ziel ist es das Zertifikat ISO 14001 bis im Herbst zur nächsten Lieferantenberuteilung dre SBB vorweisen zu können',
                    '2024-06-17 06:48:26.640149', NULL,
                    'Wir bestehen das externe Audit vom 19.-20. August und erhalten die ISO 14001 Zertifizierung.', 1,
                    1101, 1, 'ordinal', '2024-06-17 06:46:21.36493', '-', 'Audit bestanden ', 'keine Abweichnung', 1,
                    NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1317, NULL,
                    'Wir sprechen Kund:innen anders über Puzzle und über WAC an. Die Puzzle-Website ist unsere "Feelgood"-Package (digitale Lösungen)-Kanal, we are cube ist stärker auf Consulting ausgerichtet. ',
                    '2024-07-08 08:31:48.917591', NULL,
                    'Wir kommunizieren gezielter mit UX Kunden über die WAC Website', 1, 1114, 1, 'ordinal',
                    '2024-06-18 12:15:09.571382', 'Konsens über Informationen auf wearecube.ch/puzzle.ch',
                    'Texte und Struktur steht, Illustrationen usw. sind erstellt.', 'wearecube.ch angepasst', 2, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1266, NULL, '', '2024-06-18 12:49:12.245197', NULL,
                    'Salesunterstützung für /mobility führt zu neuen Aufträgen', 1, 1097, 1, 'ordinal',
                    '2024-06-13 14:41:56.193694',
                    'Die Zusammenarbeit zwischen /sales und /mobility ist mit neuem CSO verifiziert und bei Bedarf angepasst.',
                    'Die koordinierte Salesunterstützung führt zu neuen Leads. Wir haben für alle /mobility Members verrechenbare Aufträge  im Q2.',
                    'Durch gezieltes strategisches Pricing gewinnen wir neue Aufträge (inkl. Rahmenverträge). Erreicht wenn Aufträge/ Rahmenverträge mit mind. CHF 300k Auftragsvolumen gewonnen.',
                    4, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1225, NULL, '', '2024-07-24 06:04:47.014479', NULL,
                    'Wir steigern und behalten die absolute Verrechenbarkeit auf X%', 1, 1064, 1, 'ordinal',
                    '2024-03-19 13:25:49.176129', 'A%', 'B%', 'C%', 1, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1333, NULL,
                    '"Näher an ISO 27001" heisst spezifisch noch ohne grössere Änderungen an der Puzze-Kultur; grosse Schritte hängen von GL-Entscheid ab.',
                    NULL, NULL, 'P16 ist so überarbeitet, dass er zur Strategie passt und näher an ISO27001 ist.', 1,
                    1116, 1, 'ordinal', '2024-06-18 13:50:23.658672',
                    'Probleme in P16 sind identifiziert und bestehende Tickets sind wieder in guten Zustand (oder geschlossen).',
                    'P16 ist grösstenteils überarbeitet, Rest ist in Tickets erfasst.',
                    'P16 ist komplett überarbeitet.', 0, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1321, NULL,
                    'Wir finden unseren "Way of working" und teilen unser Wissen im Team. Wir lernen im Prozess und minimieren Doppelspurigkeiten. ',
                    '2024-06-18 13:15:43.632085', NULL, 'Way of Working - Wir werden effizienter in unseren Prozessen',
                    1, 1115, 1, 'ordinal', '2024-06-18 12:35:20.441083', 'Knowledge Base etabliert (nextcloud live).
    Mindestens ein Beitrag pro Member.
    Zielliste für Inhalt erfasst. ',
                    'Migration vorhandene, verteilte (files.puzzle) Prozessdefinition oder Vorgehensansätze sammeln. ',
                    'Jeder Member hat seine Knowledge-Base-Insel zum pflegen. ', 2, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1329, NULL,
                    'Wir wollen mehr Support-Verträge abschliessen, sei dies OpenShift/Rancher oder Pipeline-Support.',
                    '2024-06-25 14:14:49.433052', NULL, 'Das Support Angebot weitet sich aus 💰', 1, 1118, 1, 'ordinal',
                    '2024-06-18 13:19:20.359097', '1 weiterer offeriert und SHKB abgeschlossen',
                    '1 weiterer neben SHKB', '2 weitere neben SHKB', 3, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1147, NULL, '', '2024-05-02 15:41:25.067141', NULL,
                    'Die strategischen Stossrichtungen mit Vertiefungsphasen sind mit einer Roadmap geplant.', 1, 1053,
                    1, 'ordinal', '2024-03-07 15:10:04.657755', 'Alle strategischen Stossrichtungen sind geplant',
                    'Im Mobcoeur Team abgestimmt', 'Mit CTO abgestimmt', 4, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1341, NULL, '', NULL, NULL, 'Cryptopus wird abgelöst.', 1, 1116, 1, 'ordinal',
                    '2024-06-19 07:53:06.96398', 'Wir haben einen Plan wie wir Cryptopus ablösen und was nötig ist. ',
                    'Wir haben die Alternative bereit, aber die Migration noch nicht durchgeführt. ',
                    'Migration erfolgreich durchgeführt. ', 0, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1206, NULL, '', NULL, NULL, 'Alle Cubies sind verrechenbar', 1, 1079, 1, 'ordinal',
                    '2024-03-19 13:03:22.84727', 'Alle Cubies sind auf Projekte offeriert',
                    'Mayra leistet mind. 20 verrechenbare Stunden und Berti ist auf einem bestätigten Auftrag platziert',
                    'Zusätzlich zum Target Ziel leistet Berti verrechenbare Stunden', 0, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1183, NULL, '', '2024-05-02 06:59:52.203704', NULL,
                    'Wir etablieren das Sponsoringmodell für den Puzzle up!.', 1, 1078, 1, 'ordinal',
                    '2024-03-19 11:57:13.313459', 'Wir haben eine Zusage für ein Sponsoringpackage.',
                    'Wir haben zwei Zusagen für ein Sponsoringpackage. ',
                    'Wir haben drei Zusagen für ein Sponsoringpackage.', 2, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1261, NULL,
                    'Die strategischen Massnahmen 3 ("konsequentes Auftrags-  & Change-Management anwenden") und 4 ("Den effektiven Aufwand Stunden für Pauschalaufträge reduzieren" werden umgesezt. Dass nicht alle Support und Wartungsaufträge neu geführt werden können, wird im Konzept berücksichtigt.',
                    '2024-07-18 09:37:41.94327', NULL,
                    'Durch konsequenteres Auftragsmanagement sind die Aufwände für Support- & Wartungsaufträge reduziert',
                    1, 1095, 1, 'ordinal', '2024-06-13 14:21:26.42174',
                    'Konzept ist erstellt für die beide strategischen Massnahmen 3 & 4, dokumentiert und die Auftragsverantwortlichen sind  geschult',
                    'Alle Support und Wartungsaufträge werden nach dem neuen Konzept geführt.',
                    'Eine Wirkung von -6 kCHF Mehraufwand im Quartal ist bereits erzielt (entspricht der Hälfte des Nice Case beide Massnahmen zusammengenommen)',
                    6, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1196, NULL, '', NULL, NULL, 'Guidelines für die Selbstorganisation sind erstellt', 1, 1062, 1,
                    'ordinal', '2024-03-19 12:29:06.193344', 'Workshop «Brainstorming Selbstorganisation 14.11.2023» verarbeitet
    ', 'Erstellen «Heatmap», Wo drückt uns der Schuh, wo gehen wir was an, Draft Guidelines vorhanden, Mit Manifest abgeglichen
    ', 'Guidelines sind vorhanden, im Team besprochen und dem GL-Coach vorgestellt
    ', 0, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1194, NULL, '', '2024-03-19 14:04:26.362443', NULL, 'Wir haben ein gemeinsames Verständnis der Arbeit im Team und legen 3 Fokusthemen fest
    ', 1, 1062, 1, 'ordinal', '2024-03-19 12:25:45.803587', 'Workshop, Verarbeitung «Retro 2024» ', 'Auslegeordnung vorhanden, 3 Fokusthemen definiert
    ', 'Massnahmen und Messpunkte zu den 3 Fokusthemen definiert', 1, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1217, NULL, '', '2024-03-19 13:21:04.213815', NULL, 'Wir weiten das 3rd-Level Angebot weiter aus',
                    1, 1083, 1, 'ordinal', '2024-03-19 13:15:31.926178', '1 Offerten raus', '1x Unterschrieben',
                    '2x Unterschrieben', 1, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1220, NULL, '', '2024-04-22 09:44:15.953885', NULL,
                    'Wir formulieren unsere Anforderung/Ansprüche bzgl. “Digitale Lösungen” ', 1, 1084, 1, 'ordinal',
                    '2024-03-19 13:17:27.386995',
                    'Workshop und Verständnis schaffen, Einordnung «Individual-Software-Entwicklung» in dieses Thema',
                    'Formulierung für /ux erstellen - Mit /devtre, /mobility & /ruby abgeglichen - Gemeinsame Anforderung formulieren',
                    'An Markom und im Leadership-Team präsentiert', 4, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1193, NULL, '', '2024-03-21 10:06:14.336769', NULL,
                    'Wir kommunizieren die /mid-Journey intern und extern. ', 1, 1078, 1, 'ordinal',
                    '2024-03-19 12:24:52.412284', 'Es wurden drei Massnahmen aus dem Action-Plan umgesetzt.',
                    'Es wurden alle Massnahmen aus dem Action-Plan umgesetzt.',
                    'Es wurde mindestens ein neuer Lead generiert.', 2, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1336, NULL, '', '2024-06-19 07:50:27.816055', NULL, 'MO Beratung Beitrag definieren', 1, 1119, 1,
                    'ordinal', '2024-06-18 14:03:06.481582', 'Aktiver Austausch mit Lead/Kernteam',
                    'Roadmap für /security-Beitrag ist definiert', '/security ist bereit für Aufträge im MO', 1, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1290, NULL, '- d3-Workshop 23.4.2024, Files: https://files.puzzle.ch/f/6357279
    - MO "Ditigale Lösungen: Codi https://codimd.puzzle.ch/fGy0RNSzTVqVlUz80dw_Mw', '2024-06-18 12:33:23.611667', NULL,
                    'Unser (d3) Mitwirken und unsere Sales-Erwartungen zur Marktopportunität (MO) "Digitale Lösungen" sind formuliert und kommuniziert sowie mit den /dev- & /ux-Divisions abgeglichen.',
                    1, 1105, 1, 'ordinal', '2024-06-17 12:07:45.504078', '- Verarbeitung Outcome d3-Workshop 23.4.2024
    - d3-Mitwirken und -Sales-Erwartungen sind mit  MO "Ditigale Lösungen" abgeglichen und dokumentiert', '- d3-Mitwirken sind mit /mobility, /ruby & /ux abgestimmt
    - allgemeine /dev- & /ux-Mitwirken sind formuliert
    - d3-Sales-Erwartungen sind mit /mobility, /ruby & /ux abgestimmt
    - allgemeine /dev- & /ux-Sales-Erwartungen sind mit /mobility, /ruby & /ux abgestimmt
    ', '- Inputs und Sales-Erwartungen seitens /dev- /ux-Divisions sind:
    - an Sales (Yup) kommuniziert & besprochen
    - dem Kernteam MO "Digitale Lösungen" präsentiert', 5, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1322, NULL,
                    'Wir ersetzen den Austritt von Julia (80%), den Austritt von Flavio (80%) und die Reduktion von Tobi (20%) nachhaltig und verrechenbar. ',
                    '2024-06-18 12:43:44.239125', NULL,
                    'Wir haben bei den verrechenbaren Rollen die Austritte ersetzt.', 1, 1112, 1, 'ordinal',
                    '2024-06-18 12:03:22.628908', 'Ausfälle sind ersetzt (d.h. Vertrag ist unterschrieben).',
                    'Mindestens 1 Member hat die Stelle bereits angetreten und arbeitet produktiv auf einem Mandat. ',
                    'Zwei Members sind verrechenbar angestellt. ', 1, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1150, NULL,
                    'Unsere Wartungskosten sind nicht gedeckt. es wird deshalb ein Konzept erstellt und anschliessend neue Verträge SLA ausgearbeitet und verhandelt',
                    '2024-03-19 12:57:54.651258', NULL, 'wir erhöhen den Ertrag der Pauschalen (Betrieb, Wartung und Support)
    Jahresziel 2024 -> Rentabilität', 1, 1056, 1, 'ordinal', '2024-03-15 08:43:59.844382', 'Analyse der Umgebungs komplexität estellt
    ', 'Wartungskosten pro Kunde sind definiert', '1. Fassung SLA erarbeitet ', 4, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1296, NULL, '', '2024-06-18 13:20:40.390659', NULL,
                    'Die /dev/tre-Members sind bis Ende 2024 eingeplant', 1, 1106, 1, 'ordinal',
                    '2024-06-17 12:09:42.258426', 'Alle d3-Members per Ende Juli bis Ende Oktober eingeplant',
                    'Alle d3-Members per Ende August bis Ende Jahr',
                    'Alle d3-Members per Ende August bis Ende Januar 2024', 2, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (116, 0, '', '2023-06-27 08:30:32.261977', 1,
                    'Wir führen mit jeder Division eine Kommunikationsplanung durch', 1, 69, 1, 'metric',
                    '2023-06-27 08:30:32.261977', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1262, NULL, 'Formulierung entspricht Company OKR', '2024-06-24 07:24:26.559405', NULL,
                    'MO Beratung: Wir starten mit der Marktopportunität Beratung und haben eine erste Auslegeordnung',
                    1, 1096, 1, 'ordinal', '2024-06-13 14:21:39.522447', 'Wir sind als Team organisiert und der Kick-Off zur Marktopportunität Beratung ist erfolgt.
    Eine erste Auslegeordung und ein mögliches Angebot für das Thema Beratung ist erstellt.', 'Wir haben eine Massnahmenplan und eine Roadmap für das weitere Vorgehen festgelegt.
    Für /mobility haben wir ein Konzept wie wir Members für Beratungseinsätze kurzfristig aus ihren Mandaten herausnehmen',
                    'Wir haben mit ersten möglichen Kunden unser Angebot verifiziert und Feedback eingeholt.', 1, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1153, NULL,
                    'Wir haben nun die Relases standartisiert und vesenden Releasenotes. Nun wollen wir das Angebot ausbauen und 4 Schulungen/Jahr anbieten',
                    '2024-03-19 13:19:57.167526', NULL,
                    'wir bieten Release Schulungen für unsere Kunden an-> Rentabilität, Verrechenbarkeit PL ', 1, 1056,
                    1, 'ordinal', '2024-03-15 09:02:44.952065', 'Intresse an Communitymeeting abgeholt ',
                    'Schulungskonzept ist vorhanden', 'Schulungseinladung ist bereits versendet ', 2, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1295, NULL, '', '2024-06-18 13:01:14.551329', NULL,
                    'Wir stellen einen zusätzlichen, geeigneten Software-Engineer an.', 1, 1106, 1, 'ordinal',
                    '2024-06-17 12:09:35.093312', 'Inserat ist finalisiert und freigeschaltet',
                    'Bewerbungen und Gespräche finden statt', 'Ein Vertrag ist abgeschlossen', 1, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1154, NULL,
                    'Unser Ziel ist es das Zertifikat ISO 14001 bis im Herbst zur nächsten Lieferantenberuteilung dre SBB vorweisen zu können',
                    '2024-03-19 14:27:39.958847', NULL,
                    'Wir haben unser Puzzle OS erweitert und das interne Audit vom 30. Mai  2024 zur Umweltzertifizierung bestanden.',
                    1, 1058, 1, 'ordinal', '2024-03-15 09:23:14.212613', 'Draft des erweiterten Puzzle OS erstellt. ',
                    'Internes Audit erfolgreich bestanden', 'ISO14001 in ISO9001 vollständig in Puzzle OS integriert. ',
                    5, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1185, NULL, '', NULL, NULL, 'Planung zu ISO 27k1 ist soweit, dass GL go/no-go entscheiden kann.', 1,
                    1071, 1, 'ordinal', '2024-03-19 12:01:06.519853', 'Notwendige Schritte sind dokumentiert.',
                    'Prozess ist aufgegleist und bei GL deponiert.', 'Zertifizierungsprozess ist angelaufen.', 0, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1288, NULL, '', '2024-06-18 12:55:17.449217', NULL,
                    'Wir haben einen Plan “Team-Aktivitäten GJ 24/25”', 1, 1102, 1, 'ordinal',
                    '2024-06-17 12:03:21.24326', 'Es ist definiert, was mit Team-Aktivitäten gemeint ist?
    Es ist definiert, welches die Ziele und Inhalte der Aktivitäten (sachlich, sozial, etc.)
    ', 'Eine erste Team-Aktivität hat stattgefunden.', 'Die Team-Aktivitäten fürs GJ 24/25 sind geplant', 3, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1186, NULL, '', NULL, NULL, 'Wir haben ein Bedrohungsmodell für Puzzle definiert.', 1, 1071, 1,
                    'ordinal', '2024-03-19 12:03:01.575285', 'Threat Model ist ad-hoc definiert.',
                    'Threat Model nach STRIDE definiert und in relevante Prozesse eingebettet.',
                    'Individuelle Models für ein oder mehrere Projekte zusammen mit den Devs entwickelt.', 0, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1128, NULL, '', '2024-03-19 10:35:44.31777', NULL,
                    'Wir haben zusätzliche verrechenbare Members gewonnen.', 1, 1050, 1, 'ordinal',
                    '2024-03-07 09:55:24.832574', 'Wir schreiben fünf neue Stellen aus.',
                    'Wir schreiben fünf neue Stellen aus und schliessen zwei Arbeitsverträge ab.',
                    'Wir schreiben fünf neue Stellen aus und schliessen vier Arbeitsverträge ab.', 3, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1268, NULL,
                    'Wir halten das monatliche Wartungsbduget von 63.5h (gemessen wird März, April und Mai 24)  ',
                    '2024-06-18 12:25:27.269657', NULL, 'wir halten uns an das monatliche Wartungsbudget', 1, 1099, 1,
                    'ordinal', '2024-06-17 06:46:02.080958', '1 Monat < 63.5 h ', ' 3 Monate < 63.5 h ',
                    '3 Monate < 60h ', 1, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1228, NULL,
                    'Wir suchen gezielt einen Software Engineer, den wir in Acrvis aufbauen können. Die Stufe muss mindestens Professional sein. Der Beschäftigungsgrad muss mindestens 80% sein.',
                    '2024-04-16 14:57:00.027618', NULL,
                    'Wir stellen eine zusätzlichen, geeignete Software entwickelndes Member an', 1, 1064, 1, 'ordinal',
                    '2024-03-19 13:27:12.483246', 'Ausschreiben', 'Gespräche führen', 'Eine Anstellungung', 2, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (49, 0, 'Commit (0.3): Pro Technologie ist ein Blogpost vorhanden welcher unsere Expertise zeigt; Target (0.7): Commit + Wir haben pro Technologie mindestens 3 Members mit Wissen und mindestens ein Kunde mit Interesse. Stretch (1.0) Wir haben in jeder Technologie bereits Dienstleistungsumsätze generiert. Stichdatum 15.9.
    ', '2023-06-14 10:49:06.237495', 1, 'New Tech: Puzzle ist für die gewählten Technologien am Markt bekannt ', 1, 44, 1,
                    'metric', '2023-06-14 10:49:06.237495', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1137, 0.4, 'Stretch Goal ist 9 "Daumen hoch" zu 1 "Daumen runter" mit neuen Feedback Prozess zu News (Target entspricht 0.75). Bei negativem Feedback wird aktiv beim Member nachgefragt, um Inputs zu holen.
    ', '2024-03-21 12:22:13.311461', 0.9,
                    'Die Kommunikation zur /mobility Strategie ist Puzzle weit erfolgt und wird von den Members gestützt (gemessen an Ratio "Daumen hoch" / "Daumen runter")',
                    1, 1053, 1, 'metric', '2024-03-07 14:47:43.267556', NULL, NULL, NULL, 3, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (82, 0, '0.3 (Commit Zone): Das Ziel wird 1x erreicht; 0.7 (Target Zone): Das Ziel wird 2x erreicht; 1.0 (Stretch Goal): Das Ziel wird 3x erreicht.

    Es wird davon ausgegangen, dass die 3rd lvl Verträge zur Erreichung der Kennzahl beitragen.',
                    '2023-07-10 09:34:18.882319', 1,
                    'Die absolute Verrechenbarkeit von /mid/container ist in jedem Monat mindestens 71.0%', 1, 58, 1,
                    'metric', '2023-07-10 09:34:18.882319', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (44, NULL, 'Commit (0.3): 1 Monat über 53%;  Target 0.7: 2 Monate über 53%; Stretch (1.0): 3 Monate über 53%. Stichtag 31.8 (Monatsabschlüsse Juni, Juli, August)
    ', '2023-07-22 05:10:55.954769', 53, 'Die absolute Verrechenbarkeit liegt in jedem Monat über 53%', 1, 43, 1, 'metric',
                    '2023-07-22 05:10:55.954769', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1174, 50, 'Massgeblich ist der Anteil verplanter Stunden relativ zur den planbaren Stunden auf der Ansicht https://time.puzzle.ch/plannings/departments/15/multi_employees
    (Baseline ist 50%)', '2024-04-23 16:37:44.652675', 90,
                    'Auftragsbücher füllen: GJ25-Q1 Auslastung im Forecast erreicht 90% der Gesamtkapazität der verrechenbaren Pensen',
                    1, 1075, 1, 'metric', '2024-03-19 10:14:03.608472', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (93, 0,
                    '0.3 (Commit Zone): Monatliche Happiness Umfrage ausgewertet; 0.7 (Target Zone): + Massnahmenpaket für''s nächste Quartal definiert; 1.0 (Stretch Goal): + Zufriedenheit in jedem Monat etwas höher als im Vormonat. ',
                    '2023-06-23 14:10:01.194713', 1,
                    'Grundlage & Massnahmen zur Förderung sowie Messung der Members-Zufriedenheit definiert', 1, 60, 1,
                    'metric', '2023-06-23 14:10:01.194713', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (37, 0,
                    '0.3 (Commit Zone): Zwei Erfolgsstories sind auf Linkedin gepostet. Die Erfahrung aus anderen Teams ist in unser Konzept eingeflossen. Die Meinung von 3 Kontakten (RTEs) zu unserem Konzept ist uns bekannt.  0.7 (Target Zone):  Die Meinung eines weiteren /mobility Kontakt ist bekannt;  1.0 (Strech Goal: die Meinung aller RTEs sowie aller /mobility Kunden ist uns bekannt. ',
                    '2023-06-20 13:25:35.616212', 1,
                    'Wir haben ein Konzept wie wir Members als Team im PV oder Projekt zum Einsatz bringen', 1, 40, 1,
                    'metric', '2023-06-20 13:25:35.616212', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1235, NULL, 'In Ausrichtigungsworkshops (ggf. verschiedene Workshops mit unterschiedlichen Interessengruppen) definieren, wie die mittelfristige Stossrichtung der Division /zh aussieht.

    Wichtig dabei ist die Positionierung im kompetitiven (Java-)Softwareengineering und im traditionellen Linux System Engineering (mittelfristig weniger VMs, mehr Cloud, mehr SaaS).',
                    '2024-04-23 15:42:06.014988', NULL, 'Divisionstrategie 2026 entworfen.', 1, 1074, 1, 'ordinal',
                    '2024-03-25 12:04:57.249721',
                    'Interessen des Teams in facilitierten Workshops (ggf. mehrere) erhoben und zusammen mit Team konsolidiert.',
                    'Commit plus Überführung der Ausrichtungsziele in eine Strategie, welche die Interessen von Puzzle, der Members und der Division zusammenbringt. Inkl. Massnahmenkatalog und grobem Ausbildungskonzept.',
                    'Target + Vernehmlassung und Approval der GL', 1, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1149, NULL,
                    'Wir halten das monatliche Wartungsbduget von 63.5h (gemessen wird März, April und Mai 24)  ',
                    '2024-03-19 11:56:27.871509', NULL, 'wir halten uns an das monatliche Wartungsbudget', 1, 1056, 1,
                    'ordinal', '2024-03-15 08:43:59.839807', '1 Monat < 63.5 h bedeutet Commit Zone ',
                    '3 Monate < 63.5 h erreicht bedeutet Target Zone, ', '3 Monate < 60h bedeutet Stretch', 2, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1243, NULL, '', '2024-06-18 08:57:21.501447', NULL, 'MO AI/ML
    Das Kernteam AI/ML hat sich gefunden und hat erste Resultate erzielt.', 1, 1091, 1, 'ordinal',
                    '2024-06-04 09:11:46.501437', 'Das AI/ML Kernteam hat aktiv Zeit in das Thema investiert.',
                    'Erweitertes Training (Kombi: ML für Engineers und ML Ops Grundlagen) mit acend und Uni Bern ist aufgesetzt und fertig vorbereitet.', 'Erster bezahlter Auftrag ist gewonnen.
    ', 4, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1204, NULL, '', NULL, NULL,
                    'Wir formulieren unsere Anforderung/ Ansprüche bzgl. “Digitale Lösungen” ', 1, 1063, 1, 'ordinal',
                    '2024-03-19 12:48:31.170053', 'Workshop, Verständnis «Digitale Lösungen» schaffen, Einodrnung «Individual-Software-Entwicklung» in dieses Thema
    ', 'Formulierung für /dev/tre erstellen - Mit /ux & /ruby abgeglichen - Gemeinsame Anforderung formulieren
    ', 'An Markom und im Leadeship-Team präsentiert
    ', 0, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1319, NULL,
                    'Wir hatten in der näheren Vergangenheit Änderungen in unserem kleinen Team. Daraus entstehende Unruhe möchten wir reduzieren ohne unsere Rentabilität zu torpedieren',
                    '2024-07-22 08:13:34.028516', NULL,
                    'Als Team wachsen wir zusammen, fühlen uns wohl und integrieren Neuzugänge', 1, 1115, 1, 'ordinal',
                    '2024-06-18 12:33:04.248237',
                    'Kickoff mit gemeinsamen Zielen  - Action Plan für Teamzufriedenheit erstellen. ',
                    'Priorisierung der Massnahmen und Umsetzung Top 1 Massnahmen',
                    'Priorisierung der Massnahmen und Umsetzung Top 2 Massnahmen', 5, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1251, NULL,
                    'Das Team für die Marktopportunität Beratung hat sich formiert und die ersten Aktivitäten werden angegangen. Eine erste Auslegeordnung und ein erstes Angebot ist vorhanden.',
                    '2024-06-18 09:03:46.94421', NULL, 'MO Beratung
    Wir starten mit der Marktopportunität Beratung und haben eine erste Auslegeordnung erstellt.', 1, 1091, 1, 'ordinal',
                    '2024-06-04 09:28:39.675608', '- Wir sind als Team organisiert und der Kick-Off zur Marktopportunität Beratung ist erfolgt.
    - Eine erste Auslegeordung und ein mögliches Angebot für das Thema Beratung ist erstellt.', '- Wir haben eine Massnahmenplan und eine Roadmap für das weitere Vorgehen festgelegt.
    - Wir haben mit ersten möglichen Kunden unser Angebot verifiziert und Feedback eingeholt.',
                    '- Wir haben einen Lead für einen Auftrag für das Thema Beratung gewonen.', 2, NULL);
            INSERT INTO okr_pitc.key_result
            VALUES (1141, 0,
                    'Das Gesamtpotential dieser Massnahme ist im Nice Case +30kCHF pro Quartal bis Ende 2025 (ggü. Q1_23/24) , das ergibt über die verbleibenden 7 Quartale gemittelt +4.285 kCHF pro Quartal als Target.  Der Stretch liegt somit bei 6121. Wichtig: Der Cash-in Muss noch nicht in diesem Quartal erfolgen.',
                    '2024-03-21 12:53:16.133836', 6121,
                    'Erste strategischen Massnahmen, die keine Vertiefung benötigen, sind mit finanzieller Wirkung umgesetzt',
                    1, 1053, 1, 'metric', '2024-03-07 14:59:39.632218', NULL, NULL, NULL, 4, 3);
            INSERT INTO okr_pitc.key_result
            VALUES (1259, 0, 'Vertragsunterzeichnung, nicht schon gestartet. ', '2024-06-18 12:24:29.410823', 1,
                    'Durch angemessene Rekrutierungsbemühungen erreicht /mobility bis Ende Q1 GJ 24/25 ein Nettowachstum von 0.8 verrechenbaren FTE. Ausgangswert: 30.06.2024',
                    1, 1095, 1, 'metric', '2024-06-13 14:11:25.238775', NULL, NULL, NULL, 3, 5);
            INSERT INTO okr_pitc.key_result
            VALUES (121, NULL, 'Commit (0.3): 1 Monat über 92%; Target 0.7: 2 Monate über 92%; Stretch (1.0): 3 Monate über 92%. Stichtag 31.8 (Monatsabschlüsse Juni, Juli, August)

    Messbarkeit: Durchschnittliche Verrechenbarkeit der laufenden Kundenauftrage (exkl. BWS) ',
                    '2023-08-21 10:53:58.212369', 92, 'Unsere Aufträge sind mit einer Verrechenbarkeit > 92% rentabel ',
                    1, 70, 1, 'metric', '2023-08-21 10:53:58.212369', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1343, 2, 'Wir haben anhand des Action Plans eine Go-to-Market Strategie für Platform Engineering erarbeitet.

    Commit: 5 Actions umgesetzt
    Target: 7 Actions umgesetzt', '2024-06-25 13:44:04.283298', 9, 'Go-to-Market Strategie erfolgreich erarbeitet (🙂)', 1,
                    1098, 1, 'metric', '2024-06-18 13:00:42.280522', NULL, NULL, NULL, 12, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1311, 0, 'Alle Action Items wurden erledigt.', '2024-06-18 11:45:52.522219', 7, 'MO Plattform Engineering
    Das go-to-market Konzept ist abgeschlossen und ein erster Platform MVP wurde intern aufgebaut.
    ', 1, 1091, 1, 'metric', '2024-06-04 09:18:13.055124', NULL, NULL, NULL, 3, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1177, 10000, '', '2024-05-02 09:15:47.724829', 160000, 'Realisierung von CHF 160''000.- Neugeschäft (Single Year Bookings) mit Red Hat Subscriptions.
    ', 1, 1076, 1, 'metric', '2024-03-19 11:27:13.028816', NULL, NULL, NULL, 6, 3);
            INSERT INTO okr_pitc.key_result
            VALUES (1306, 0, 'Volumen abgeschlossener Deals mit Neukunden, für die wir bisher nicht gearbeitet haben.',
                    NULL, 50000, 'New Business: Ein Neukunde', 1, 1109, 1, 'metric', '2024-06-17 23:44:05.106302', NULL,
                    NULL, NULL, 0, 3);
            INSERT INTO okr_pitc.key_result
            VALUES (122, NULL, 'Das Wartungs&Support Budget für Hitobito beträgt 111''800 CHF pro Kalenderjahr ',
                    '2023-09-11 12:31:31.609673', 111800, 'Wir halten das Wartungsbudget ein', 1, 70, 1, 'metric',
                    '2023-09-11 12:31:31.609673', NULL, NULL, NULL, 1, 3);
            INSERT INTO okr_pitc.key_result
            VALUES (1241, 4050000,
                    'Forecast Wert des geplanten Dienstleistungsumsatzes aus PTime für die Monate Okt-Jan (1.10.24-31.1.25) (Pauschalen, Partnerumsätze, Subs sind nicht enthalten). Commit MCHF 4.45, Target: MCHF 5.0, Stretch MCHF 5.4. Stichdatum: 16.09.24',
                    '2024-06-27 07:24:49.137156', 5400000,
                    'Wir erfreuen uns über volle Auftragsbücher bis über den Jahreswechsel hinaus.', 1, 1090, 1,
                    'metric', '2024-06-04 09:08:49.614671', NULL, NULL, NULL, 5, 3);
            INSERT INTO okr_pitc.key_result
            VALUES (1230, 800000, '', '2024-03-25 12:03:17.670101', 1200000, 'Wir haben einen guten Forecast für das Q1 2024/2025
    ', 1, 1083, 1, 'metric', '2024-03-19 13:16:22.381186', NULL, NULL, NULL, 3, 3);
            INSERT INTO okr_pitc.key_result
            VALUES (1214, 14, '', '2024-03-21 08:24:19.421798', 150,
                    'Auf unserer WAC Linkedin Seite erreichen wir 150 Linkedin Follower', 1, 1082, 1, 'metric',
                    '2024-03-19 13:11:38.120287', NULL, NULL, NULL, 2, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1349, 77.225,
                    'Kündigungen und Anstellungen die nach dem Quartal erfolgen werden bei der Messung auch miteinbezogen.',
                    '2024-06-27 07:40:36.966803', 80.225,
                    'Wir haben bei den verrechenbaren Rollen die Austritte ersetzt und zusätzliche Members gewonnen.',
                    1, 1090, 1, 'metric', '2024-06-04 09:16:36.160563', NULL, NULL, NULL, 3, 5);
            INSERT INTO okr_pitc.key_result
            VALUES (50, 0, 'Stichdatum 15.9.', '2023-06-20 06:21:05.06711', 100000,
                    'Operations: Wiederkehrende Erträge generiert durch neue Aufträge gemessen an den Pauschalen ', 1,
                    44, 1, 'metric', '2023-06-20 06:21:05.06711', NULL, NULL, NULL, 1, 3);
            INSERT INTO okr_pitc.key_result
            VALUES (120, 0, 'S: Bis zum Ende des laufenden Geschäftsjahres (31. Dezember 2023) streben wir an, den Umsatz zu halten oder leicht zu steigern im Vergleich zum Vorjahr
    M: Der Fortschritt wird anhand der monatlichen Umsatzzahlen und Vergleichswerte aus dem Vorjahr gemessen.
    A: Die geplante Umsatzsteigerung basiert auf den Kundenbudgets sowie den vorhanden Leads.
    R: Der Umsatz ist von grossr Bedeutung für die finanzielle Stabilität von Hitobito
    T:: Das Ziel soll bis zum 31. Dezember 2023 erreicht werden.', '2023-07-27 06:38:54.157882', 460000,
                    'Wir erreichen und steigern den Umsatz', 1, 70, 1, 'metric', '2023-07-27 06:38:54.157882', NULL,
                    NULL, NULL, 1, 3);
            INSERT INTO okr_pitc.key_result
            VALUES (43, 0, 'Ziewert von 4.5 bezieht sich auf FTE. Stichtag: 15.9.', '2023-06-14 09:31:27.101145', 4.5,
                    'Wir erhöhen die verrechenbaren Pensen um 5% (4.5 FTE)', 1, 43, 1, 'metric',
                    '2023-06-14 09:31:27.101145', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1163, 250000,
                    'Eingecheckt wird der kumulierte Durchnitt der monatlichen Umsätze (März-Mai, inkl. Sublieferanten) gemäss Divisionsrentabilitätsrechnung. Wenn der Durchschnitt am Schluss 296''296.- erreicht, ist das Stretch Ziel erreicht (3*296296=888888)',
                    '2024-07-24 06:11:32.932145', 296296,
                    'Wir erreichen einen Quartalsumsatz von CHF xxxxxx.- (durchschnittl. knapp TCHF xxx pro Mt.)', 1,
                    1065, 1, 'metric', '2024-03-19 06:37:37.288324', NULL, NULL, NULL, 4, 3);
            INSERT INTO okr_pitc.key_result
            VALUES (1312, 0,
                    'Bei einem Maximalumsatz von 1.115 Mio pro Jahr erreichen wir 300k pro Quartal. Mit der Kurzfristigkeit (Jahreswechsel, Consulting), schätzen wir optimistisch 80% bereits zu Anfang planbar im PTime zu haben (Target: 187''500, Commit: 137''500)',
                    '2024-07-11 08:22:53.420412', 250000,
                    'Wir freuen uns über volle Auftragsbücher bis über den Jahreswechsel hinaus', 1, 1112, 1, 'metric',
                    '2024-06-18 11:57:43.264306', NULL, NULL, NULL, 8, 3);
            INSERT INTO okr_pitc.key_result
            VALUES (1134, 2900000,
                    'Forecast Wert des geplanten Dienstleistungsumsatzes aus PTime für das Q1 2024/25 (Pauschalen, Partnerumsätze, Subs sind nicht enthalten). Commit MCHF 3.2, Target: MCHF 3.6, Stretch MCHF 3.9. Stichdatum: 19.06.24',
                    '2024-05-29 07:47:51.317739', 3900000,
                    'Dank randvollen Auftragsbüchern geniessen wir unsere Sommerferien.', 1, 1050, 1, 'metric',
                    '2024-03-07 10:14:49.928678', NULL, NULL, NULL, 6, 3);
            INSERT INTO okr_pitc.key_result
            VALUES (1191, 86609, 'PTIME > Aufträge > Umsatz Juli bis September', '2024-05-13 06:58:08.509889', 250000,
                    'Prognostizierter Umsatz im Q1 ist gesichert ', 1, 1079, 1, 'metric', '2024-03-19 12:16:38.579741',
                    NULL, NULL, NULL, 4, 3);
            INSERT INTO okr_pitc.key_result
            VALUES (1275, 0,
                    '1 weiterer /mobility Member ist entweder auf GO oder Delivery Pipeline/Kubernetes Auftrag im Einsatz als Target (= 0.8FTE). Als Stretch zweiter Member. Ein Auftrag zählt wenn für mindestens 3 Monate Dauer geplant.',
                    '2024-07-04 07:18:47.8253', 1.1,
                    'Mit CI/CD sind weitere Kooperationspotentiale identifiziert und ein weiterer /mobility Member im Einsatz',
                    1, 1095, 1, 'metric', '2024-06-13 14:33:34.242254', NULL, NULL, NULL, 7, 5);
            INSERT INTO okr_pitc.key_result
            VALUES (1328, 750000, 'Vergleich 2023.10 - 2024.01 Ergebnis:
      - Total TCHF 1197,
      - Durchschnittlich pro Monat TCHF 299

    Zielwert für 01.10.2024 - 31.01.2025 Ergebnis:
    Commit: TCHF 1050
    Target: TCHF 1200', '2024-07-09 08:14:29.145333', 1350000,
                    'Wir erfreuen uns über volle Auftragsbücher bis über den Jahreswechsel hinaus', 1, 1118, 1,
                    'metric', '2024-06-18 13:15:24.365383', NULL, NULL, NULL, 5, 3);
            INSERT INTO okr_pitc.key_result
            VALUES (1136, 0, '', '2024-03-19 12:14:22.338744', 1,
                    'Durch angemessene Rekrutierungsbemühungen erreicht /mobility bis Ende Q4 GJ 23/24 ein Nettowachstum von 0.8 verrechenbaren FTE (exkl. Kafkastelle). ',
                    1, 1055, 1, 'metric', '2024-03-07 14:47:21.249126', NULL, NULL, NULL, 7, 5);
            INSERT INTO okr_pitc.key_result
            VALUES (51, 0, 'Stichdatum 15.9.', '2023-07-11 03:49:27.256405', 104700,
                    'Subscriptions: Handelsgeschäft-Gewinn verdreifacht zum Vorjahresquartal', 1, 44, 1, 'metric',
                    '2023-07-11 03:49:27.256405', NULL, NULL, NULL, 1, 3);
            INSERT INTO okr_pitc.key_result
            VALUES (1144, 0, '', '2024-04-23 09:33:21.862302', 2.4,
                    'Durch koordinierte Aktivitäten mit dem Salesteam gewinnen wir neue Aufträge. ', 1, 1055, 1,
                    'metric', '2024-03-07 15:05:05.436977', NULL, NULL, NULL, 7, 5);
            INSERT INTO okr_pitc.key_result
            VALUES (32, NULL,
                    'Überlegungen: Ambitiöses (“stretch”) Jahresziel ist 17.0kCHF  (kein grosser strategischer Invest geplant; besser als die letzten drei Jahre, aber deutlich unter Spitzenjahr 19/20.) Unter Berücksichtigung, dass Q1 [alle Quartalswerte mit einem Monat “Lag”] ca 12% weniger Ist Stunden hat, und wir wieder das BBT Team betreuen ist ein Abschlag von 15% auf die 17kCHF eingerechnet --> 14.5kCHF',
                    '2023-06-07 07:45:08.822171', 14500,
                    'Der Mitarbeiterumsatz pro FTE beträgt in jedem Monat mehr als 14.5kCHF', 1, 39, 1, 'metric',
                    '2023-06-07 07:45:08.822171', NULL, NULL, NULL, 1, 3);
            INSERT INTO okr_pitc.key_result
            VALUES (69, 0, 'Ordinales Ziel:
    - Commit: DesignOps Workshop durchgeführt und Pain Points Members aufgenommen
    - Target: konkrete Massnahme im Team besprochen und festgehalten
    - Stretch: Zusätzlich zweite  Massnahmen besprochen und festgehalten', '2023-06-20 13:26:09.244617', 1,
                    'Wir legen den Grundstein für die weitere Entwicklung und Professionalisierung von WAC', 1, 55, 1,
                    'metric', '2023-06-20 13:26:09.244617', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (33, NULL, 'Überlegungen:  Ambitiöses (“stretch”) Jahresziel müsste 76% sein (kein grosser strategischer Invest geplant; besser als die letzten drei Jahre, aber deutlich unter Spitzenjahr 19/20, in den Monaten Jan-April hatten wir im Schnitt 75.5%). Für Q1 und Q2 haben wir die Lehrlinge, darum 3% Abschlag (ca. 0.6 FTE für Lehrlingsbetreuung) --> 73%
    ', '2023-06-01 14:06:27.14655', 73, 'Die absolute Verrechenbarkeit ist in jedem Monat über 73.0%', 1, 39, 1, 'metric',
                    '2023-06-01 14:06:27.14655', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (40, 0, 'Commit (0.3): zu 50% der internen Events (offiz. Fübi / Sommerinfo / Aareböötle usw.) gibt es einen Newsbeitrag mit Fotos;
    Target (0.7): zu 80% der internen Events (offiz. Fübi / Sommerinfo / Aareböötle usw.) gibt es einen Newsbeitrag mit Fotos;
    Stretch (1.0): Target + Es gibt mindestens 2 interne Events pro Monat.
    Stichtag 15.9.', '2023-06-14 09:20:07.357072', 1, 'Events & Ereignisse werden intern kommuniziert und zelebriert', 1,
                    42, 1, 'metric', '2023-06-14 09:20:07.357072', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (47, 0, 'Commit (0.3): Gemeinsames internes Verständnis was ein "Agiles Team" ist wurde geschafft, Sales Kampagne gestartet; Target (0.7): Sales Kampagne gestartet und zwei Leads vorhanden; Stretch (1.0): Sales Kampagne gestartet und ein Team gewonnen. Stichdatum 15.9.
    ', '2023-06-20 09:05:48.287785', 1,
                    'Agile Teams: Die Sales Kampagne ist gestartet und wir haben einen Auftrag für ein weiteres agiles Team gewonnen ',
                    1, 44, 1, 'metric', '2023-06-20 09:05:48.287785', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1210, 0, '', NULL, 12, 'Wir posten jede Woche mind. 1 Beitrag auf SoMe', 1, 1082, 1, 'metric',
                    '2024-03-19 13:08:06.359845', NULL, NULL, NULL, 0, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (99, 75,
                    'Die absolute Verrechenbarkeit von /zh ist in jedem Monat mindestens auf 70% (Commit) bzw. 75% (Target) bzw. 80% (Stretch)',
                    '2023-06-26 10:55:12.663594', 80, 'Absolute Verrechenbarkeit', 1, 65, 1, 'metric',
                    '2023-06-26 10:55:12.663594', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1180, 0, 'Commit: 80%
    Target: 90 %
    Stretch: 100 %', '2024-03-21 10:05:35.759224', 100, 'Alle Texte sind im Wordpress eingepflegt. ', 1, 1077, 1, 'metric',
                    '2024-03-19 11:54:13.839631', NULL, NULL, NULL, 5, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1233, 0, 'Jeder Input, jeder Wunsch, jeder Verbesserungsvorschlag aus den MAGs ist entweder erfüllt, adressiert oder, falls beides nicht möglich, mit Absender besprochen sein. In letzterem Fall muss Vorgehen definiert und dem Member kommuniziert sein.

    Liste von total 29 Inputs und Verbesserungsvorschlägen, die abgearbeitet wird (Zielerreichungsgrad linear)',
                    '2024-04-23 15:19:22.026003', 29, 'MAG-Inputs zu 100% verarbeitet', 1, 1074, 1, 'metric',
                    '2024-03-25 11:31:02.37229', NULL, NULL, NULL, 2, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (109, 1345, '', '2023-06-26 07:53:25.953862', 1371,
                    'Wir erhöhen die Anzahl Newsletter Abonennt*innen um 2%', 1, 66, 1, 'metric',
                    '2023-06-26 07:53:25.953862', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (87, 0, 'Commit:
    - Techmap devtre aktualisiert- Liste Technologie pro Auftrag aktualisiert und verabschiedet
    - Techradar devtre erstellt

    Target:
    - Gewichtung FE, BE & CI/CD visualisiert
    - Ermittelt mit welcher Technologie wir auf die Marktopportunität «New Tech» einzahlen wollen

    Stretch:
    - Statement geschrieben
    - Mit GL-Coach & CTO verabschiedet
     ', '2023-06-25 09:23:54.601536', 1, 'Wir kennen unsere technologiesche Ausrichtung', 1, 59, 1, 'metric',
                    '2023-06-25 09:23:54.601536', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (96, 0, 'Commit:
    - Inserat überarbeitet und publiziert
    Target:
     - mindestens 3 Gespräche durchgeführt
    Stretch:
    - 1 Vertrag unterschrieben', '2023-08-03 12:37:39.006376', 1,
                    'Wir schliessen einen neuen Arbeitsvertrag mit einer verrechenbaren Person ab 60-80% *', 1, 56, 1,
                    'metric', '2023-08-03 12:37:39.006376', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (46, 0, 'Commit (0.3): Divisionsrentabilität ist eingeführt;
    Target (0.7): Commit + Neue Zielwerte sind Ende Quartal vorhanden
    Stretch (1.0): Commit + Neue Zielwerte sind auf den zweiten Monat bereits vorhanden; Stichtag 15.9.',
                    '2023-06-14 09:40:09.588376', 1,
                    'Die Divisionsrentabilitätsrechnung ist eingeführt und Zielwerte sind definiert', 1, 43, 1,
                    'metric', '2023-06-14 09:40:09.588376', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1373, 0, '', '2024-07-05 10:02:11.702047', 8, 'yxvc', 1, 1130, 1, 'metric',
                    '2024-07-05 09:53:49.209378', NULL, NULL, NULL, 3, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (45, 2,
                    'Die FTE beziehen sich auf jene in der "Membersliste". Die Messungen erfolgen jeweils Mitte und Ende des Monats. Stichtag ist 15.9.',
                    '2023-06-14 09:36:51.701417', 1, 'Wir senken unsere durchschnittliche Auslastungslücke auf 1.0 FTE',
                    1, 43, 1, 'metric', '2023-06-14 09:36:51.701417', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (86, 0, 'Commit:
    - Etabilieren eines regelmässigen (alle 3 Wochen) devtre-Biers
    - Dabei Abwechslung reingebracht
    - Roadmap der Gefässe erstellt und auf übergelagerte Meetings abgestimmt (SUM, LST-WS, OKR-Meetings, …)

    Target:
    - 3 Teammeetings durchgeführt (bisher)
    - Erstmaliger Quartals-Teamworkshop durchgeführt

    Stretch:
    - Retro/ Umfrage durchgeführt und Verbesserungsmassnahmen definiert', '2023-06-25 09:30:14.721095', 1,
                    'Wir bauen die regelmässigen devtre Team-Gefässe aus und steigern deren inhaltliche Qualiät', 1, 61,
                    1, 'metric', '2023-06-25 09:30:14.721095', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (85, 0, '0.3 (Commit Zone): Wir identifizieren im Team Aspekte der Kommunikation, die als verbesserungswürdig wahrgenommen werden;
    0.7 (Target Zone): + Wir definieren & implementieren Massnahmen zur Verbesserung der Kommunikation;
    1.0 (Stretch Goal): + Wir evaluieren den Massnahmeneffekt und ziehen Schlüsse daraus.', '2023-06-20 13:31:07.956171', 1,
                    'Massnahmen zur Verbesserung der wahrgenommenen Kommunikation sind umgesetzt', 1, 60, 1, 'metric',
                    '2023-06-20 13:31:07.956171', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (106, 0, 'Commit:
    . Swisscom cADC erstellt, kommuniziert und 2x durchgeführt
    Target:
    - Mobiliar Gaia erstellt, kommuniziert und 2x durchgeführt
    - BUAR SAP HANA erstellt, kommuniziert und 1x durchgeführt
    Stretch:
    - Standard-Controlling-/ Reporting Vorlage vorhanden & dokumentiert
    - F&A-Team instruiert für Durchführung beim Monatsabschluss', '2023-06-25 09:19:59.841728', 1,
                    'Wir verbessern, vereinheitlichen und etablieren ein regelmässiges Controlling und Reporting', 1,
                    56, 1, 'metric', '2023-06-25 09:19:59.841728', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (105, 0,
                    '0.3 (Commit Zone): Wir organisieren ein Meeting mit den involvierten Members; 0.7 (Target Zone): + Wir machen eine Auslegeordnung als Basis für''s Q2; 1.0 (Stretch Goal): + Wir definieren erste Schritte und verteilen Tasks;',
                    '2023-08-21 13:07:46.146106', 1, 'Kubermatic Auslegeordnung gemacht', 1, 57, 1, 'metric',
                    '2023-08-21 13:07:46.146106', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (123, 0, 'Commit (0.3): Termine sind abgestimmt mit 2 Accounts ; Target (0.7): Termine sind abgestimmt mit 4 Accounts (1.0): Termine sind abgestimmt mit 6 Accounts; Stichtag 20.10.

    SAC
    die Mitte
    Wanderwege Schweiz
    Pfadi
    Jubla
    Blasmusik

    ', '2023-08-21 11:19:20.772977', 100, 'Accountgespräche sind geplant ', 1, 72, 1, 'metric',
                    '2023-08-21 11:19:20.772977', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1151, 0,
                    'Commit (0.3): Release ist deployed und kommuniziert; Target (0.7): weniger als 5 Bugs (1.0): : weniger als 2 Bugs; ',
                    '2024-03-19 13:23:15.946525', 1, 'Hitobito Release für Q4 (Q2''24)', 1, 1057, 1, 'metric',
                    '2024-03-15 08:44:18.222095', NULL, NULL, NULL, 2, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (58, 0, '0.3 (Commit Zone): Die Technologien für MLOps Beratung sind definiert.
    0.7 (Target Zone): Drei Angebote (inkl. MLOps Tech-Lab) sind indentifiziert und als Angebot beschrieben. Davon ist ein Angebot mit 3 Kunden validiert.
    1.0 (Strech Goal): Ein Kunde bestellt Dienstleistungen im Thema Data-Analytics.', '2023-06-20 12:46:14.198447', 1,
                    '3 Angebotsideen sind durch Einbezug der Members entworfen und können validiert werden.', 1, 47, 1,
                    'metric', '2023-06-20 12:46:14.198447', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (38, 0, '0.3 (Commit Zone): Mindestens 50% aller /mobility Members (atkuell 14 Members) nehmen durchschnittlich am monatlichen /mobility Tag teil.
    0.7 (Target Zone): Mindestens 65% aller /mobility Members (aktuell 18 Members) nehmen durchschnittlich am monatlichen /mobility Tag teil.
    1.0 (Strech Goal): Mindestens 80% aller /mobility Members (aktuell 22 Members) nehmen durchschnittlich am monatlichen /mobility Tag teil.',
                    '2023-06-20 13:12:14.921932', 1,
                    'Der /mobility Tag erhöht die Identifikation und den Zusammenhalt unter /mobility Members mit Puzzle & /mobility.',
                    1, 40, 1, 'metric', '2023-06-20 13:12:14.921932', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (71, 0, 'Ordinales Ziel
    Zielwert: 53%
    - Commit: 1 Monat über Zielwert
    - Target: 2 Monate über Zielwert
    - Stretch: 3 Monate über Zielwert', '2023-06-20 14:45:52.685949', 1,
                    'Wir halten die absolute Verrechenbarkeit in allen Monaten hoch', 1, 54, 1, 'metric',
                    '2023-06-20 14:45:52.685949', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (41, 0, 'Commit (0.3): Happiness Umfrage ist bei mind. 3 Divisions eingeführt und erste Auswertung ist vorhanden; Target (0.7): Happiness Umfrage ist bei mind. 7 der Divisions eingeführt; Stretch (1.0): Happiness Umfrage ist bei allen Divisions etabliert.
    Stichtag: 15.9.
    ', '2023-08-28 14:00:53.935079', 1,
                    'Die Happiness Umfrage ist in den Divisions eingeführt, ausgewertet und Zielwerte definiert.', 1,
                    42, 1, 'metric', '2023-08-28 14:00:53.935079', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (126, 0,
                    'Commit (0.3): Termin ist agestimmt mit Community; Target (0.7): Themen sind bereits klar (1.0): Einladungen sind versendet; Stichtag 20.9. ',
                    '2023-08-24 07:57:53.27343', 1, 'Es nehmen min. 15 Teilnehmer an Community teil', 1, 72, 1,
                    'metric', '2023-08-24 07:57:53.27343', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1271, 0,
                    'Commit (0.3): Release ist deployed und kommuniziert; Target (0.7): weniger als 5 Bugs (1.0): : weniger als 2 Bugs; ',
                    '2024-06-17 07:45:51.109092', 1, 'Hitobito Release für Q1 (Q3''24)', 1, 1100, 1, 'metric',
                    '2024-06-17 06:46:15.176579', NULL, NULL, NULL, 2, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1318, 0,
                    'Dokumentation von Anzahl Members, die regelmässig LinkedIn-Posts veröffentlichen sowie je ein publizierter Post.   ',
                    '2024-06-18 12:28:15.656737', 10,
                    'Wir wissen, wer gerne LinkedIn-Posts veröffentlicht, haben diese Information dokumentiert und bauen unsere Botschafter:innen zu spezifischen Themen auf. ',
                    1, 1104, 1, 'metric', '2024-06-17 11:12:14.190596', NULL, NULL, NULL, 3, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1310, 0, 'Messung ist kumuliert zu lesen (3*15%, 15% pro Monat)', NULL, 45, '15% Marge', 1, 1109, 1,
                    'metric', '2024-06-17 23:57:47.193358', NULL, NULL, NULL, 0, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1200, 0, 'Commit: 80%
    Target: 90 %
    Stretch: 100 %', '2024-04-02 07:10:53.41092', 100, 'Alle Bilder und Grafiken sin dim Wordpress eingepflegt. ', 1, 1077,
                    1, 'metric', '2024-03-19 12:41:20.415988', NULL, NULL, NULL, 2, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1277, 0, 'Lab mit Uni Bern: ML Ops for Engineers und ML Ops Grundlagen sind zwei neue Labs, die mit Acend aufgebaut werden sollen, diese werden aber noch nicht durchgeführt.
    Die erfolgreich durchgeführten Labs beziehen sich auf das PuzzleUp und den CH Open Workshop.',
                    '2024-07-04 07:23:47.19402', 3, 'MO AI & MLOps: 2 MLOps Labs sind durchgeführt ', 1, 1096, 1,
                    'metric', '2024-06-13 14:40:06.702244', NULL, NULL, NULL, 4, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (36, 0, '0.3 (Commit Zone): Monatliche Happiness Umfrage ausgewertet für die Baseline Bestimmung; 0.7 (Target Zone): Baseline und Zielwert Bestimmung + Massnahmenpaket aus letztjähriger Mitarbeiterbefragung für Mobility definiert; 1.0 (Stretch Goal): Baseline + Massnahmenpaket + 10% höhere Zufriedenheit als bei letztjähriger Mitarbeiterbefragung
    ', '2023-08-31 11:50:11.584735', 1,
                    'Die Happiness Baseline, Massnahmen und allfälliger Zielwert für die folgenden Quartale sind definiert',
                    1, 40, 1, 'metric', '2023-08-31 11:50:11.584735', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (35, 0,
                    'Wir stellen zwei neue Member (Engineers) an (und keine verlieren) mit durchschnittlich 80% Pensum, um unser Verhältnis verrechenbare/nicht verrechenbare Pensen zu verbessern.',
                    '2023-06-20 13:38:07.407737', 1.6, 'Netto-Wachstum um 1.6 verrechenbarer FTE', 1, 39, 1, 'metric',
                    '2023-06-20 13:38:07.407737', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (104, NULL, '', '2023-07-12 13:10:32.846345', 0, 'Wir erzielen ein Nettowachstum von 0', 1, 58, 1,
                    'metric', '2023-07-12 13:10:32.846345', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (113, 0, '', '2023-06-27 08:21:37.401184', 1,
                    'Wir platzieren den Newsletter prominent auf unserer Startseite puzzle.ch', 1, 66, 1, 'metric',
                    '2023-06-27 08:21:37.401184', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (80, 0, 'Zwei Leute, die ihr Pensum reduzieren (Tiago, Rémy). Mind. eine neue Person, die startet. Mehrere betroffene Kunden.
    // Commit: Kundenbeziehungen können fortgesetzt werden
    // Target: Pensumsreduktionen können bei allen betroffenen Kunden kompensiert werden // Stretch: Bei betroffenen Kunden ist zusätzliches Business möglich (duch zusätzliche Kapa nach Anstellung)',
                    '2023-06-20 13:22:15.926697', 1, 'Kundenbeziehungen und bestehendes Business erhalten', 1, 51, 1,
                    'metric', '2023-06-20 13:22:15.926697', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (72, 6, 'Zufriedenheit der Devs mit ihrer Auftrags-Perspektive auf Skala von 1-10.
    Commit: 6, Target: 8, Stretch: 9', '2023-08-28 11:14:55.983388', 10, 'Perspektive für Devs geschafft', 1, 49, 1,
                    'metric', '2023-08-28 11:14:55.983388', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1173, 0, 'Falls mehr als 3 Teamevents stattfinden, zählen die 3 meistbesuchten.
    Teilnehmerzahl wird kumuliert, deshalb ist das Stretchgoal 3*10=30', NULL, 30,
                    '3 Teamevents mit je 10 Teilnehmenden (es zählen auch team-externe Members)', 1, 1074, 1, 'metric',
                    '2024-03-19 10:12:14.287478', NULL, NULL, NULL, 0, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1197, 0, '', NULL, 1, 'Wir führen einen Team-Event durch
    ', 1, 1062, 1, 'metric', '2024-03-19 12:29:47.520198', NULL, NULL, NULL, 0, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1315, 12,
                    'In der Breite liegt die Wirkung - wir posten 15 Artikel auf Social Media (Target). Dazu gehören Blogposts (auf WAC, die wir streuen), Erfahrungsberichte von Konferenzen, Social Media Umfragen...',
                    '2024-07-11 08:24:41.063487', 16,
                    'Wir posten weiterhin jede Woche einmal relevanten Content auf SoMe.', 1, 1114, 1, 'metric',
                    '2024-06-18 12:08:14.003851', NULL, NULL, NULL, 3, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (125, NULL, 'Commit (0.3): 1 Monat über 55%; Target 0.7: 2 Monate über 55%; Stretch (1.0): 3 Monate über 55%. Stichtag 31.8 (Monatsabschlüsse Juni, Juli, August)

    Uner absoluter Verrechenbarkeit verstehen wir als Baisis die Verrechenbarkeit inkl die  inidirekte Stunden der Wartung( Budget) und  Lösungsbduget',
                    '2023-08-21 10:46:51.265759', 56, 'Die absolute Verrechenbarkeit liegt in jedem Monat über 55%', 1,
                    70, 1, 'metric', '2023-08-21 10:46:51.265759', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (48, 0, 'Commit (0.3): Umfrage bei Members durchgeführt, AWS Partnerschaft abgeschlossen und kommuniziert; Target (0.7): GCP Partnerschaft abgeschlossen und kommuniziert und 1 neuer Kunde gewonnen (AWS oder GCP);  Stretch (1.0): 3 neue Kunden gewonnen. Stichtdatum 15.9.
    ', '2023-06-14 10:46:24.350383', 1,
                    'Hyperscaler: Partnerschaften mit AWS und GCP sind abgeschlossen und wir gewinnen neue Kunden ', 1,
                    44, 1, 'metric', '2023-06-14 10:46:24.350383', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1331, 7, 'Zufriedenheit bleibt hoch. Gemessen wird die Zufriedenheit anhand der "Happiness Umfrage", die monatlich nach dem Teammeeting durchgeführt wird

    Commit: 7.75
    Target: 8

    Mit 🙂 gekennzeichnete Actions geben wir an unsere Members weiter.', '2024-06-25 15:07:08.653438', 8.5,
                    'Die Members bei /mid bleiben zufrieden.', 1, 1120, 1, 'metric', '2024-06-18 13:22:38.460131', NULL,
                    NULL, NULL, 15, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (68, 0, 'Ordinales Ziel:
    - Commit: Happinessumfrage ist etabliert
    - Target: Zielwerte für die Happinessumfrage sind definiert
    - Stretch: Für jedes Tactical ist die Happinessumfrage ausgefüllt und diskutiert', '2023-06-20 12:42:41.208914', 1,
                    'Wir steigern die WAC Memberszufriedenheit', 1, 53, 1, 'metric', '2023-06-20 12:42:41.208914', NULL,
                    NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (119, 0, '0.3 (Commit Zone): Baseline fürs nächste Quartal ist definiert (unter Berücksichtigung der Senioritätsstufen u.ä.); 0.7 (Target Zone): Baseline ist definiert + Massnahmenpaket zur Stundensatzerhöhung ist definiert; 1.0 (Stretch Goal): Baseline + Massnahmenpaket + Mittlerer Stundensatz zu aktuellem Wert um 2CHF erhöht auf 155CHF/h
    ', '2023-07-05 07:56:45.069795', 1, 'Durchschnittliche Stundensätze um 2CHF erhöhen', 1, 39, 1, 'metric',
                    '2023-07-05 07:56:45.069795', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (65, 0, 'Ordinales Ziel:
    - Commit: Wir haben die WAC Strategie mit Stakeholdern diskutiert
    - Target: die WAC Strategie ist finalisiert
    - Stretch: Die WAC Strategie ist publiziert', '2023-06-20 12:39:56.273089', 1,
                    'Wir finalisieren die Divisionsstrategie von We Are Cube', 1, 55, 1, 'metric',
                    '2023-06-20 12:39:56.273089', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (67, 0, 'Ordinales Ziel:
    - Commit: Zieldefinition WAC Teamkultur erfolgt
    - Target: erste Massnahme zur Steigerung definiert und umgesetzt
    - Stretch: zusätzliche zweite Massnahme zur Steigerung definiert und umgesetzt', '2023-07-06 13:51:08.637905', 1,
                    'Wir definieren die WAC Teamkultur mit dem Team und leiten konkrete Massnahmen ab um diese zu stärken',
                    1, 53, 1, 'metric', '2023-07-06 13:51:08.637905', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (117, 0, '', '2023-06-27 10:53:54.522734', 1,
                    'Wir erstellen ein Kanban-Board, das die Kommunikationsaktivitäten der Divisions für alle sichtbar macht ',
                    1, 69, 1, 'metric', '2023-06-27 10:53:54.522734', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1253, 0,
                    'Der übrige Betriebsaufwand (Miete, Ausgaben für Marketing- und Kommunikations-Aktionen, IT-Betrieb, Infrastruktur, Verwaltungsaufwand) konnte im Vergleich zum vorherigen Quartal um 3% reduziert werden.',
                    '2024-06-12 09:51:44.163327', 4.3, 'Der übrige Betriebsaufwand konnte um 3% reduziert werden.
    ', 1, 1090, 1, 'metric', '2024-06-04 13:21:10.641733', NULL, NULL, NULL, 3, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (118, 0, '', '2023-07-05 08:31:49.819983', 2,
                    'Wir produzieren und publizieren zwei Videos mit technischen Inhalten', 1, 69, 1, 'metric',
                    '2023-07-05 08:31:49.819983', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (77, -85, 'Pensumsverluste bei Sys Engineers (Tiago -65%, Rémy -20%) durch Anstellung eines neuen Sys Engineers eingrenzen/kompensieren
    Commit: -50%, Target: 0%, Stretch: 10%', '2023-06-20 13:13:02.914596', 0,
                    'Kompensation Pensumsverluste bei Sys Engineering', 1, 51, 1, 'metric',
                    '2023-06-20 13:13:02.914596', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (79, 0, '0.3 (Commit Zone): Wir haben unseren neuen OpenShift Staging Cluster aufgebaut;
    0.7 (Target Zone): + Wir setzen einen Cilium PoC um;
    1.0 (Stretch Goal): + Wir schreiben einen Blogpost über unsere Learnings;', '2023-08-21 13:07:56.14477', 1,
                    'Cilium PoC auf interner Infrastruktur umgesetzt', 1, 57, 1, 'metric', '2023-08-21 13:07:56.14477',
                    NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1314, 6, 'Wir sind mindestens an 6 Events vor Ort (Target) mit Networking und Aufbau von Wissen. ',
                    '2024-07-11 08:23:29.853144', 8,
                    'Wir sind an wichtigen Events am Networken und bilden uns an Konferenzen weiter. ', 1, 1114, 1,
                    'metric', '2024-06-18 12:04:20.142148', NULL, NULL, NULL, 4, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (127, 0,
                    'Commit (0.3): Inhalt ist bekannt ; Target (0.7): Release ist bereit  (1.0): Release sind deployed und kommuniziert; Stichtag 20.9. ',
                    '2023-08-24 07:58:01.986223', 1, 'Hitobito Release für Q1 (Q3''23)', 1, 72, 1, 'metric',
                    '2023-08-24 07:58:01.986223', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (73, 0, 'Ordinales Ziel
    - Commit: 1 Neukunde
    - Target: 2 (wovon 1 in Gesundheitsbereich)
    - Stretch: 3 (wovon 1 im Gesundheitsbereich)', '2023-06-20 12:53:02.495619', 1,
                    'WAC gewinnt zwei Neukunden (Ziel: Vertrag. Projekt muss noch nicht starten oder durchgeführt sein)',
                    1, 54, 1, 'metric', '2023-06-20 12:53:02.495619', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (89, 0, 'Commit:
    - Dienstleistungsangebot erstellt und im Wiki notiert
    - “Wie wir arbeiten" erstellt und im Wiki notiert

    Target:
    - Blog-Artikel zu Moser-Baer publiziert (Output)
    - Outcome!! Wie angekommen, wie messen?

    Stretch:
    - Eine Anfrage
    ', '2023-06-25 09:25:11.244925', 1, 'Es ist bekannt, dass devtre für Individual-Software-Entwicklung steht', 1, 59, 1,
                    'metric', '2023-06-25 09:25:11.244925', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (70, 0, 'Wir kommen mit aktiver Akquise an neue Aufträge und Kundenkontakte.
    // Commit: 10 qualifizierte Leads, mind. 5 davon mit Neukunden
    // Target: Akquise von zwei Mandaten oder einem Projekt
    // Stretch: Akquise eines grösseren Dev-Projekts (>200k), das die /zh-Devs substantiell auslastet',
                    '2023-06-20 13:24:12.689433', 1, 'Akqui-Sprint trägt Früchte', 1, 49, 1, 'metric',
                    '2023-06-20 13:24:12.689433', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (42, 0, 'Commit (0.3): Die Frage “Wie zufrieden bin ich mit meiner Arbeit” aus Happiness Umfrage ist als Grundlage für Konzept ausgewertet; Target (0.7): Commit + Konzept für stärkeres involvment der Members ist definiert; Stretch (1.0): Target + erste Massnahmen aus Konzept sind umgesetzt. Stichdatum 15.9.
    ', '2023-06-14 09:23:05.038915', 1,
                    'Members und ihre Wünsche werden in der Akquise mehr involviert und die Zufriedenheit mit der Arbeit gesteigert',
                    1, 42, 1, 'metric', '2023-06-14 09:23:05.038915', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1199, 0,
                    'Metrische Messung gemäss Action Plan Items - Wir betrachten unser Manifest in allem was wir tun und überlegen uns dabei, tun wir es im Namen/ Rahmen des Manifests? So wollen wir gewährleisten, dass das Manifest bewusst wahrgenommen wird und das dessen Inhalt keine "heisse Luft" ist.',
                    '2024-03-19 14:08:12.960427', 5, 'Wir verankern unser Manifest und leben danach', 1, 1062, 1,
                    'metric', '2024-03-19 12:33:46.200824', NULL, NULL, NULL, 2, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (78, 0, 'Ordinales Ziel:
    - Commit: Wir verlieren keine Members
    - Target: Wir decken alle neuen Mandate mit vorhandenen und geplanten Members ab
    - Stretch: Wir stellen ein zusätzlicher voll verrechenbarer Member an', '2023-06-20 13:14:57.587783', 1,
                    'Wir kümmern uns um unsere FTE-Auslastung', 1, 54, 1, 'metric', '2023-06-20 13:14:57.587783', NULL,
                    NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (115, 0, '', '2023-06-27 08:28:56.233567', 3,
                    'Wir planen 3 Puzzleness-Inhalte, die wir in passender Form (Video, Social Media, Blogpost) publizieren',
                    1, 68, 1, 'metric', '2023-06-27 08:28:56.233567', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (74, NULL, 'Überlegungen:
    - Ambitiöses (stretch) Jahresziel ist 75%.
    - Durchschnitt der letzten 5 Jahre (GJ 2018/19 bis GJ 2022/23): 68%
    - Höchstwert der letzten 5 Jahre GJ 2018/19): 74.4%
    - Durchschnitt GJ 2022/23 (Aug-Jun): 73.5%
    - Durschnitt März bis April 2023: 71.4%

    Für Q1, GJ 2023/24 sind viele Absenzen, Member-Gespräche, Onboardings, Offerings, Workshops und Rekrutierung geplant. Die 73% im Q1 GJ 2023/24 zu halten ist also schon ein Mega-Stretch.',
                    '2023-06-25 09:15:44.152678', 1, 'Die absolute Verrechenbarkeit ist in jedem Monat mindestens 73%',
                    1, 56, 1, 'metric', '2023-06-25 09:15:44.152678', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (114, 0, '2 interne Events pro Monat', '2023-07-05 08:23:35.390507', 6,
                    'Wir unterstützen die GL bei der internen Kommunikation von Events & Ereignissen', 1, 68, 1,
                    'metric', '2023-07-05 08:23:35.390507', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (94, 0, 'Commit:
    - Wir kennen den Auftragsplan bis Ende 2023 und haben eine Vision für Q1, 2024
    - Wir organisieren ein Coaching für den BL

    Target:
    - Wir klären die Frage mit welchem Instrument wir uns organisieren «Gurkensalat-Board"

    Stretch:
    - Report "Get things done" aus Gurkensalat als Beweis, dass Get Things Done besser geworden  ist (aber wie reporten??)

    ', '2023-06-25 09:31:56.561616', 1,
                    'Wir leben die Selbstorganisation bewusster hinsichtlich Prioritäten und Terminen und get things done',
                    1, 61, 1, 'metric', '2023-06-25 09:31:56.561616', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (81, 0, '0.3 (Commit Zone): Das Ziel wird 1x erreicht; 0.7 (Target Zone): Das Ziel wird 2x erreicht; 1.0 (Stretch Goal): Das Ziel wird 3x erreicht. Es wird davon ausgegangen, dass die 3rd lvl Verträge zur Erreichung der Kennzahl beitragen.

    Es wird davon ausgegangen, dass die 3rd lvl Verträge zur Erreichung der Kennzahl beitragen.',
                    '2023-07-10 09:34:09.465359', 1,
                    'Die absolute Verrechenbarkeit von /mid/cicd ist in jedem Monat mindestens 65.0%', 1, 58, 1,
                    'metric', '2023-07-10 09:34:09.465359', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (83, 0,
                    'Effiziente Einarbeitung Junior (Yelan) + Auftrag finden, welcher Tandem mit Junior ermöglicht // Commit: Junior kann 50% verrechenbar eingesetzt werden // Target: 75% Verrechenbarkeit // Stretch: Target + Auftrag mit Junior/Senior Pairing ermöglicht',
                    '2023-06-20 13:33:54.643812', 1, 'Pairing-Konstellation für Junior ermöglichen', 1, 49, 1, 'metric',
                    '2023-06-20 13:33:54.643812', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1161, 0, 'Der Ausblick auf das GJQ4 ist positiv. Wir wollen die zur Verfügung stehenden Ressourcen möglichst effektiv nutzen und durchschnittlich 16% Marge in den Monatsabschlüssen ausweisen.
    Der Messwert ist kumuliert zu lesen (3mal Erreichung des Stretch-(Monats-)Ziels -> 3*16%=48%)',
                    '2024-07-24 06:11:04.278362', 45, 'Durchschnittlich x% Marge in den Monatsabschlüssen (März-Mai)',
                    1, 1065, 1, 'metric', '2024-03-19 06:37:37.284726', NULL, NULL, NULL, 3, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1344, 2, 'Anhand des Action Plans definieren wir die interne Plattform.
    Mit 🙂 gekennzeichnete Actions geben wir an unsere Members weiter

    Commit: 4 Actions umgesetzt
    Target: 5 Actions umgesetzt', '2024-06-25 14:35:57.012724', 6,
                    'MVP ist erstellt, Stakeholder sind eingebunden und die Kommunikation ist sichergestellt (🙂)', 1,
                    1098, 1, 'metric', '2024-06-18 13:03:03.339119', NULL, NULL, NULL, 17, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (59, 0, '0.3 (Commit Zone): Ein Pool von mindestens 5 interessierten Members besteht
    0.7 (Target Zone): Das MLOps Tech-Lab wurde einmal durchgeführt und dabei einen weiteren Trainer eingeführt. Ein Blog Post zeigt unsere Entscheidung und Anwendung der gewählten Technologie auf.
    1.0 (Stretch Goal): Zusätzlich wurde ein zweites MLOps Tech-Lab durch 2. Trainer/in durchgeführt. Technologiehersteller spricht Budget für Kommunikation.
    ', '2023-06-20 12:53:24.885364', 1, 'Das MLOps Tech-Lab ist entwickelt und Kompetenzen aufgebaut. ', 1, 47, 1, 'metric',
                    '2023-06-20 12:53:24.885364', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (92, 0, 'Commit:
    - racoon: Dani, Mätthu integriert

    Target:
    - Integrationsplan für Clara erstellt
    - Integrationsplan für Simon erstellt

    Stretch:
    - Plan was Chrigu Lüthi ab Oktober: Aufträge, Technologien, Arbeitsweise
    - Plan für Beri ab Oktober: Volumen, Aufträge, Entwicklung

    ', '2023-08-26 13:37:28.708989', 1,
                    'Wir haben eine Vision für die neuen/ zurückkehrenden Members und integrieren diese', 1, 61, 1,
                    'metric', '2023-08-26 13:37:28.708989', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (57, 0, '0.3 (Commit Zone): Indikatoren sowie Mess- und Zielwerte für Beurteilung von Teamzusammenhalt sind definiert.
    - 0.7 (Target Zone): Zusätzlich ist eine zusammenhaltsfördernde Aktivität durchgeführt und gemäss festgelegten Indikatoren ausgewertet.
    - 1.0 (Strech Goal): Zusammenhaltsfördernde Aktivitäten sind etabliert und werden durch Initiative von Members iniziert und umgesetzt.',
                    '2023-06-20 13:10:44.568941', 1,
                    'Zusammenhaltsförderende Aktivitäten innerhalb /mobility oder Subteams unter Mitwirkung der Members durchgeführt.',
                    1, 40, 1, 'metric', '2023-06-20 13:10:44.568941', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1304, 50,
                    'Die Aufträge von Yelan, David und Dani laufen im Verlauf des Septembers potentiell aus. Wir wollen eine kontinierliche Auslastung sicherstellen. Gemessen wird nach Soll-Stunden gewichtete Auslastung',
                    NULL, 95, 'Kontinuierliche Auslastung der Dev-Pensen', 1, 1109, 1, 'metric',
                    '2024-06-17 23:39:05.681914', NULL, NULL, NULL, 0, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (76, 0, '0.3 (Commit Zone): Wir führen ein Dagger Kundenmapping durch und verfügen über eine Liste potenzieller Leads;
    0.7 (Target Zone): + Wir führen Gespräche mit 3 potenziellen Kunden und werten die Erkenntnisse aus;
    1.0 (Stretch Goal): + Wir können mit einem Kunden eine Dagger-Pipeline umsetzen;', '2023-06-20 14:12:57.449485', 1,
                    'Dagger-Marktresonanz ist untersucht', 1, 57, 1, 'metric', '2023-06-20 14:12:57.449485', NULL, NULL,
                    NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (107, NULL, '1=implementiert', '2023-06-26 07:49:09.175261', 1,
                    'Wir implementieren das neue Layout passend zur neuen Startseite von puzzle.ch', 1, 66, 1, 'metric',
                    '2023-06-26 07:49:09.175261', NULL, NULL, NULL, 1, 2);
            INSERT INTO okr_pitc.key_result
            VALUES (1212, 0, 'Messung Summe der Besuche gemäss Matomo im Q4 (1. April - Bis 30. Juni)',
                    '2024-05-13 14:00:31.302925', 2000,
                    'Wir erhöhen den Traffic in diesem Quartal auf der WAC Website auf 2000 Besuche', 1, 1082, 1,
                    'metric', '2024-03-19 13:10:29.866985', NULL, NULL, NULL, 5, 2);


            --
    -- Data for Name: action; Type: TABLE DATA; Schema: okr_pitc; Owner: -
    --

            INSERT INTO okr_pitc.action VALUES (1090, 1, 'Preisstruktur ist erstellt ', 0, false, 1153);
            INSERT INTO okr_pitc.action VALUES (1114, 1, 'Mit Arbeit an digitalen Lösungen prüfen', 2, false, 1199);
            INSERT INTO okr_pitc.action VALUES (1115, 2, 'Abstimmung mit ar und mw (25.3. geplant)', 0, true, 1144);
            INSERT INTO okr_pitc.action VALUES (1086, 1, 'Ableiten des Verteilschlüssel', 1, false, 1150);
            INSERT INTO okr_pitc.action VALUES (1087, 1, 'Analyse der Wartung der letzten 3 Jahre', 2, false, 1150);
            INSERT INTO okr_pitc.action VALUES (1088, 2, 'Inhalt Betrieb/Wartung und Support', 3, false, 1150);
            INSERT INTO okr_pitc.action VALUES (1110, 1, 'Event organisieren', 2, false, 1197);
            INSERT INTO okr_pitc.action VALUES (1111, 1, 'Mit Guidelines Selbstorganisation prüfen', 0, false, 1199);
            INSERT INTO okr_pitc.action
            VALUES (1089, 1, 'Wording und Vorgehen für Kundenkommunikation', 4, false, 1150);
            INSERT INTO okr_pitc.action VALUES (1091, 3, 'Analyse von Techboard 5 - 7PT', 0, false, 1155);
            INSERT INTO okr_pitc.action VALUES (1106, 2, 'Workshop, 23.4.2024', 0, true, 1194);
            INSERT INTO okr_pitc.action VALUES (1085, 2, 'Analyse der Umgebungs-Komplexität', 0, true, 1150);
            INSERT INTO okr_pitc.action VALUES (1107, 2, 'Workshop, 23.04.2024', 0, true, 1196);
            INSERT INTO okr_pitc.action
            VALUES (1101, 1, 'Open Space Leadership Monthly oder Kafi-Meeting für Members', 3, true, 1188);
            INSERT INTO okr_pitc.action VALUES (1098, 1, 'Imagine-Blogpost inkl. Social Media', 0, true, 1188);
            INSERT INTO okr_pitc.action VALUES (1100, 1, 'Direct Mailing an Email-Kontakte', 2, true, 1188);
            INSERT INTO okr_pitc.action VALUES (1112, 2, 'Mit Fokusthemen Teamarbeit prüfen', 1, true, 1199);
            INSERT INTO okr_pitc.action VALUES (1099, 1, 'Sponsoringauftritt KCD 13.06.2024', 1, true, 1188);
            INSERT INTO okr_pitc.action VALUES (1108, 2, 'Ideen sammeln', 0, true, 1197);
            INSERT INTO okr_pitc.action VALUES (1109, 2, 'Termin finden', 1, true, 1197);
            INSERT INTO okr_pitc.action VALUES (1117, 1, 'Im Leadership-Team kommunizieren', 3, false, 1199);
            INSERT INTO okr_pitc.action
            VALUES (1118, 1, 'Erklärungen für die Punkte hinter dem Manifest erstellt', 4, false, 1199);
            INSERT INTO okr_pitc.action VALUES (1123, 0, 'Review Saraina', 2, false, 1137);
            INSERT INTO okr_pitc.action VALUES (1124, 0, 'Publish', 3, false, 1137);
            INSERT INTO okr_pitc.action
            VALUES (1125, 0, 'Aktiv auf negativ Feedbacker zugehen und Meinung/Tipps abholen', 4, false, 1137);
            INSERT INTO okr_pitc.action
            VALUES (1142, 1, 'Neues regelmässiges Abstimmungsformat /mobility - Sales Team einführen', 1, true, 1144);
            INSERT INTO okr_pitc.action
            VALUES (1134, 1, 'Draft mit Nathi und Jöu besprechen (2.5. eingeplant)', 1, true, 1147);
            INSERT INTO okr_pitc.action
            VALUES (1126, 1, 'Mit Jöu Umsetzungsplanung machen (Termin 25.4.)', 0, true, 1141);
            INSERT INTO okr_pitc.action VALUES (1127, 1, 'Mit Jöu Zwischencheck machen', 1, true, 1141);
            INSERT INTO okr_pitc.action VALUES (1122, 1, 'Draften', 1, true, 1137);
            INSERT INTO okr_pitc.action VALUES (1140, 1, 'Validierung mit Sales extenr', 6, false, 1138);
            INSERT INTO okr_pitc.action
            VALUES (1148, 0, 'Weiterbildungsangebote prüfen & an Teampool kommunizieren', 2, false, 1135);
            INSERT INTO okr_pitc.action VALUES (1149, 0, 'Weiterbildung verbindlich planen', 3, false, 1135);
            INSERT INTO okr_pitc.action
            VALUES (1150, 0, 'Bewerbungsgespräche führen & neue Members anstellen', 4, false, 1135);
            INSERT INTO okr_pitc.action
            VALUES (1151, 0, 'Angebot auf Webseite mit Mobcoeur und Markom abstimmen', 5, false, 1135);
            INSERT INTO okr_pitc.action VALUES (1152, 0, 'Markom publiziert Angebot auf Webseite', 6, false, 1135);
            INSERT INTO okr_pitc.action
            VALUES (1158, 0, 'Ressourcen für Lehrlingsprojekt sind freigegeben und geplant', 5, false, 1139);
            INSERT INTO okr_pitc.action
            VALUES (1159, 0, 'Kundenprojekt für 4-Lehrjahr-Lernender ist gewonnen und Lernender eingeplant', 6, false,
                    1139);
            INSERT INTO okr_pitc.action VALUES (1164, 0, 'Diskussion Topics', 4, false, 1223);
            INSERT INTO okr_pitc.action VALUES (1146, 1, 'Stelleninserat aufschalten', 0, true, 1135);
            INSERT INTO okr_pitc.action
            VALUES (1153, 1, 'Stelleninserat für Betreuungsperson (Professional Java Dev) aufgeschaltet', 0, true,
                    1139);
            INSERT INTO okr_pitc.action
            VALUES (1165, 1, 'Marktvalidierung mit CTO und FI sowie Members Platzierung am 23.4.', 0, true, 1140);
            INSERT INTO okr_pitc.action
            VALUES (1141, 1, 'Vorgehen und Terminplanung mit CTO (Termin 27.3.)', 0, true, 1138);
            INSERT INTO okr_pitc.action
            VALUES (1135, 2, 'Shortliste Technologie mit potentiellen Members erstellen', 1, true, 1138);
            INSERT INTO okr_pitc.action VALUES (1121, 1, 'Abstimmung mit Saraine how to', 0, true, 1137);
            INSERT INTO okr_pitc.action VALUES (1136, 2, 'Durchsprache mit CTO und jbl, nb', 2, true, 1138);
            INSERT INTO okr_pitc.action VALUES (1131, 1, 'Mit Jölu weitere Tasks bestimmen', 2, true, 1141);
            INSERT INTO okr_pitc.action VALUES (1119, 2, 'Workshop, 23.04.2024', 0, true, 1204);
            INSERT INTO okr_pitc.action VALUES (1120, 2, 'Workshop, 23.04.2024', 0, true, 1221);
            INSERT INTO okr_pitc.action
            VALUES (1160, 2,
                    'Abgleich Dänu (Nextcloud Collections) - (ehem 23.04.2024) - zu verschieben auf Freigabe Collections',
                    0, true, 1223);
            INSERT INTO okr_pitc.action VALUES (1177, 1, 'Kickoff', 0, false, 1262);
            INSERT INTO okr_pitc.action VALUES (1138, 3, 'Validierung mit Techboard', 3, true, 1138);
            INSERT INTO okr_pitc.action
            VALUES (1105, 2, '1 Open Space Leadership Monthly oder Kaffi-Meeting für Members', 3, true, 1193);
            INSERT INTO okr_pitc.action VALUES (1128, 1, 'Draft erarbeiten ', 0, true, 1146);
            INSERT INTO okr_pitc.action VALUES (1129, 1, 'Draft besprechen mit jbl, nb (25.4. gebucht)', 1, true, 1146);
            INSERT INTO okr_pitc.action VALUES (1130, 1, 'Finalisieren und implementieren', 2, true, 1146);
            INSERT INTO okr_pitc.action VALUES (1133, 1, 'Draft erarbeiten', 0, true, 1147);
            INSERT INTO okr_pitc.action VALUES (1176, 0, 'Iwan bei Mobiliar einsetzen', 2, false, 1140);
            INSERT INTO okr_pitc.action VALUES (1132, 1, 'Stand Tasks tracken', 3, true, 1141);
            INSERT INTO okr_pitc.action VALUES (1171, 1, 'Neues Kanban Board implementiert', 2, true, 1144);
            INSERT INTO okr_pitc.action VALUES (1161, 1, 'Danach Entscheid Tool', 1, true, 1223);
            INSERT INTO okr_pitc.action
            VALUES (1154, 1, 'Interviews mit möglichen Kandidaten durchgeführt', 1, true, 1139);
            INSERT INTO okr_pitc.action
            VALUES (1156, 1, 'Lehrlingsprojekt mit BBT und LST abgesprochen und bestimmt', 3, true, 1139);
            INSERT INTO okr_pitc.action VALUES (1169, 1, 'Schulung mit 3-4 /mobility Members', 1, true, 1140);
            INSERT INTO okr_pitc.action VALUES (1170, 2, 'Angebots/Marketing Story erstellen', 3, true, 1140);
            INSERT INTO okr_pitc.action VALUES (1102, 3, '1 Imagine-Blogpost inkl. Social Media ', 0, true, 1193);
            INSERT INTO okr_pitc.action VALUES (1175, 1, 'Mit CTO Roadmap abstimmen', 2, true, 1147);
            INSERT INTO okr_pitc.action VALUES (1155, 1, 'Betreuungsperson angestellt', 2, true, 1139);
            INSERT INTO okr_pitc.action VALUES (1104, 3, '1 Direct Mailing an Email-Kontakte ', 2, true, 1193);
            INSERT INTO okr_pitc.action
            VALUES (1157, 1, 'Verantwortlichkeiten für Lehrlingsprojekt definiert', 4, true, 1139);
            INSERT INTO okr_pitc.action VALUES (1137, 3, 'Validierung mit Members', 4, true, 1138);
            INSERT INTO okr_pitc.action VALUES (1139, 2, 'Validierung mit Sales intern', 5, true, 1138);
            INSERT INTO okr_pitc.action VALUES (1147, 1, 'Interner Teampool bestimmen', 1, true, 1135);
            INSERT INTO okr_pitc.action VALUES (1178, 1, 'Angebotsentwurf erstellt', 1, false, 1262);
            INSERT INTO okr_pitc.action
            VALUES (1179, 1, 'Massnahmen & Roadmap für weiteres Vorgehen definiert', 2, false, 1262);
            INSERT INTO okr_pitc.action
            VALUES (1180, 0, '/mobility Konzept für Einsatzplanung & Kundenkommunikation', 3, false, 1262);
            INSERT INTO okr_pitc.action
            VALUES (1181, 0, 'Beratungsangebot mit potenziellen Kunden erstellt', 4, false, 1262);
            INSERT INTO okr_pitc.action
            VALUES (1182, 0, 'Kundenfeedback zu Beratungsangebot eingeholt', 5, false, 1262);
            INSERT INTO okr_pitc.action
            VALUES (1183, 1, 'Klärung Zusammenarbeit /sales & /mobility mit CSO', 0, false, 1266);
            INSERT INTO okr_pitc.action VALUES (1103, 3, 'Sponsoringauftritt KCD 13.06.2024 ', 1, true, 1193);
            INSERT INTO okr_pitc.action VALUES (1162, 1, 'Erstellen Grundstruktur', 2, true, 1223);
            INSERT INTO okr_pitc.action VALUES (1163, 1, 'Briefing / Vorstellung Members', 3, true, 1223);
            INSERT INTO okr_pitc.action
            VALUES (1184, 2,
                    'Im Thema "Delivery Pipeline / Kubernetes" sind Kooperationspotentiale mit CI/CD eruriert.', 3,
                    false, 1275);
            INSERT INTO okr_pitc.action VALUES (1196, 1, 'Der Go-to Market Plan steht', 0, false, 1311);
            INSERT INTO okr_pitc.action
            VALUES (1195, 1, 'Kundenumfrage ist ausgewertet und 3 potenzielle Massnahmen sind daraus abgeleitet', 1,
                    false, 1311);
            INSERT INTO okr_pitc.action
            VALUES (1197, 1, 'Landing-Page “Platform Engineering” auf neuer Webseite ist live', 2, false, 1311);
            INSERT INTO okr_pitc.action VALUES (1198, 1, 'Partner Programm Konzept steht', 3, false, 1311);
            INSERT INTO okr_pitc.action
            VALUES (1199, 1, 'Plattformengineering Chapter Schweiz wurde gegründet', 4, false, 1311);
            INSERT INTO okr_pitc.action
            VALUES (1200, 1,
                    '2 Leads für den Aufbau von Plattformen bei neuen Kunden (oder Kunden, bei denen wir seit > 3 Jahre kein Projekt umgesetzt haben)',
                    5, false, 1311);
            INSERT INTO okr_pitc.action
            VALUES (1201, 1, '2 Leads für die Optimierung von Plattformen bei bestehenden Kunden', 6, false, 1311);
            INSERT INTO okr_pitc.action VALUES (1205, 0, ' Ableiten des Verteilschlüssel', 1, false, 1270);
            INSERT INTO okr_pitc.action VALUES (1206, 0, ' Analyse der Wartung der letzten 3 Jahre', 2, false, 1270);
            INSERT INTO okr_pitc.action VALUES (1207, 0, 'Inhalt Betrieb/Wartung und Support', 3, false, 1270);
            INSERT INTO okr_pitc.action
            VALUES (1208, 0, 'Wording und Vorgehen für Kundenkommunikation', 4, false, 1270);
            INSERT INTO okr_pitc.action VALUES (1204, 1, 'Analyse der Umgebungs-Komplexität', 0, false, 1270);
            INSERT INTO okr_pitc.action
            VALUES (1209, 0,
                    'Sales Kampagne gestrartet auf Basis der marktanalyse https://www.srf.ch/kultur/gesellschaft-religion/schweizer-sportverbaende-immer-weniger-vereine-aber-immer-mehr-freiwilligenarbeit',
                    0, false, 1285);
            INSERT INTO okr_pitc.action VALUES (1239, 1, 'Partnerprogramm Konzept entwickelt.', 3, false, 1343);
            INSERT INTO okr_pitc.action
            VALUES (1240, 1, 'Platform Engineering Chapter Schweiz gegründet.', 4, false, 1343);
            INSERT INTO okr_pitc.action
            VALUES (1277, 1, 'Deep Dive Termin mit adi, anna und Chrigu aufsetzen', 0, true, 1275);
            INSERT INTO okr_pitc.action VALUES (1267, 1, 'Konzept erstellen', 2, false, 1261);
            INSERT INTO okr_pitc.action
            VALUES (1241, 2,
                    'Pro Bereich/Pfeiler mindestens 1 Ressource vorhanden, die nicht älter als 2 Jahre ist (Blogbeitrag, Referenzbericht, Video). 🙂',
                    5, false, 1343);
            INSERT INTO okr_pitc.action
            VALUES (1242, 4,
                    '2 Leads für den Aufbau von Plattformen bei neuen oder seit >3 Jahre inaktiven Kunden generiert.',
                    6, false, 1343);
            INSERT INTO okr_pitc.action
            VALUES (1243, 2, '2 Leads für die Optimierung von Plattformen bei bestehenden Kunden generiert.', 7, false,
                    1343);
            INSERT INTO okr_pitc.action VALUES (1244, 2, 'Neuer Deal >TCHF100 gewonnen.', 8, false, 1343);
            INSERT INTO okr_pitc.action VALUES (1224, 2, 'Wir arbeiten weiter gemeinsam in Projekten', 0, false, 1331);
            INSERT INTO okr_pitc.action VALUES (1225, 3, 'Wir organisieren Teamevents 🙂', 1, false, 1331);
            INSERT INTO okr_pitc.action
            VALUES (1226, 3, 'Die SIGs erfüllen ihren Zweck und machen Spass 🙂', 2, false, 1331);
            INSERT INTO okr_pitc.action VALUES (1268, 1, 'Feedback msc und jba einholen', 3, false, 1261);
            INSERT INTO okr_pitc.action VALUES (1274, 0, 'Marktvalidierung durchführen', 1, false, 1263);
            INSERT INTO okr_pitc.action VALUES (1275, 0, 'Priorisierte Shortlist ist erstellt', 2, false, 1263);
            INSERT INTO okr_pitc.action
            VALUES (1246, 1, 'Wir entwickeln unsere Prozesse und Kommunikation stetig weiter', 4, false, 1331);
            INSERT INTO okr_pitc.action
            VALUES (1245, 2, 'Die Vision bzgl. Platform Engineering / mid3.0 ist Allen klar', 3, false, 1331);
            INSERT INTO okr_pitc.action
            VALUES (1251, 1, 'Interne Stakeholder identifiziert und deren Bedürfnisse erfasst. 🙂', 0, false, 1344);
            INSERT INTO okr_pitc.action
            VALUES (1252, 5, 'Gemeinsame Definition der Workpackages für das nächste Quartal mit Stakeholdern. 🙂', 1,
                    false, 1344);
            INSERT INTO okr_pitc.action
            VALUES (1253, 3, 'Aufbau eines MVPs basierend auf OpenShift Developer Hub (Backstage).🙂', 2, false, 1344);
            INSERT INTO okr_pitc.action VALUES (1254, 3, 'Organisation eines PE Kafis. 🙂', 3, false, 1344);
            INSERT INTO okr_pitc.action
            VALUES (1255, 4, 'Produktion eines “Ein Mate mit…” Videos zum Thema PE.', 4, false, 1344);
            INSERT INTO okr_pitc.action
            VALUES (1256, 0, 'Einführung und Kommunikation einer einheitlichen PE-Lösung', 5, false, 1344);
            INSERT INTO okr_pitc.action
            VALUES (1236, 1, 'Kundenumfrage ausgewertet und 3 potenzielle Massnahmen abgeleitet.', 0, false, 1343);
            INSERT INTO okr_pitc.action VALUES (1237, 1, 'Go-to-Market Plan finalisiert.', 1, false, 1343);
            INSERT INTO okr_pitc.action
            VALUES (1238, 1, 'Landing-Page “Platform Engineering” auf neuer Webseite ist live.', 2, false, 1343);
            INSERT INTO okr_pitc.action VALUES (1276, 0, 'mit Ci/CD neuen Einsatz geplant', 3, false, 1263);
            INSERT INTO okr_pitc.action
            VALUES (1279, 0, 'Kick Off mit Raaflaub & Adi durchführen für Delivery Pipeline', 1, false, 1275);
            INSERT INTO okr_pitc.action VALUES (1280, 0, 'jbr dazu einladen', 2, false, 1275);
            INSERT INTO okr_pitc.action VALUES (1278, 1, 'Next Steps definieren', 4, false, 1275);
            INSERT INTO okr_pitc.action VALUES (1282, 0, 'Ende Juli Stand bei ii und msc abholen', 1, false, 1276);
            INSERT INTO okr_pitc.action VALUES (1283, 0, 'Blogs beauftragen', 2, false, 1276);
            INSERT INTO okr_pitc.action
            VALUES (1284, 0, 'bei Kanton Bern Implementierungssauftrag holen', 3, false, 1276);
            INSERT INTO okr_pitc.action VALUES (1285, 0, 'mit ar Vorgehen planen', 0, false, 1277);
            INSERT INTO okr_pitc.action VALUES (1286, 0, 'monatliche Abstimmung ar und dbi einführen', 2, false, 1277);
            INSERT INTO okr_pitc.action VALUES (1269, 2, 'Konzept finalisieren & Dokumentieren', 5, false, 1261);
            INSERT INTO okr_pitc.action VALUES (1270, 1, 'Abstimmungstermine planen', 0, true, 1261);
            INSERT INTO okr_pitc.action VALUES (1273, 1, 'Termine mit CSO und CTO geplant', 0, true, 1263);
            INSERT INTO okr_pitc.action VALUES (1288, 0, 'Codi Berti', 0, false, 1317);
            INSERT INTO okr_pitc.action VALUES (1289, 0, 'Simu Abgleich mit Pascou', 1, false, 1317);
            INSERT INTO okr_pitc.action VALUES (1290, 0, 'Draft im August - Spiegeln mit Team', 2, false, 1317);
            INSERT INTO okr_pitc.action VALUES (1281, 1, 'Termine planen', 0, true, 1276);
            INSERT INTO okr_pitc.action VALUES (1202, 2, 'Kommunikationsplanung weiterführen', 0, true, 1315);
            INSERT INTO okr_pitc.action
            VALUES (1266, 3, 'Kickoff durchführen mit msc und jbl für Zielklärung', 1, true, 1261);
            INSERT INTO okr_pitc.action VALUES (1291, 0, 'Challenging durch sfa und ar', 4, false, 1261);
            INSERT INTO okr_pitc.action VALUES (1271, 1, 'Massnahmen kommunzieren und schulen', 6, false, 1261);
            INSERT INTO okr_pitc.action VALUES (1272, 1, 'Massnahmen priorisiert umsetzen', 7, false, 1261);
            INSERT INTO okr_pitc.action VALUES (1287, 1, 'Vorgehen und Mitarbeit mit Mäge klären', 0, true, 1267);


            --
    -- Data for Name: alignment; Type: TABLE DATA; Schema: okr_pitc; Owner: -
    --


    --
    -- Data for Name: check_in; Type: TABLE DATA; Schema: okr_pitc; Owner: -
    --

            INSERT INTO okr_pitc.check_in
            VALUES (245,
                    'Das Konzept und das Drehbuch für das Video und den Blogartikel stehen und werden nun mit Markom abgestimmt. ',
                    '2023-08-28 08:00:55.25972', '', '2023-08-27 22:00:00', 0.35, 1, 37, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (136, 'Interviewfragen stehen bereit. Erste Termine mit RTEs sind vereinbart. ',
                    '2023-07-31 06:59:20.772247', '', '2023-07-30 22:00:00', 0.2, 1, 37, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (114,
                    'Bisher noch keine Aufträge. Vielversprechende Deals: Vontobel Volt, Peerdom DB, HRM DB Consulting',
                    '2023-07-24 11:41:54.179135',
                    'Dran bleiben mit hoher Prio. Wird während meinen Ferien durch Remo weitergetrieben (ich würde bei Fragen zur Verfügung stehen).',
                    '2023-07-23 22:00:00', 0.05, 1, 70, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (334,
                    'Mandat für David und Yelan bereits gestartet (Vertrag noch ausstehend). Carlo ok. Verbesserung bei Andres (Team-Projekt b. Centris). Zumindest kurzfristige Perspektive für Dani (Mobi).',
                    '2023-09-14 08:47:22.041523', 'Nachfolgeaufträge für Andres und Dani suchen (kurzfristig ok).',
                    '2023-09-13 22:00:00', 8, 1, 72, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (342,
                    'Massnahmenpaket in Form der aktuellen Teammeeting-Inhalte definiert und für das nächste Quartal geplant. Durchschnittliche Zufriedenheit der Members bei /mid jeden Monat gestiegen (Juni: 7.4, Juli: 7.6, August: 8)',
                    '2023-09-14 12:41:10.263166', '', '2023-09-13 22:00:00', 1, 1, 93, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (80, '76 Prozent', '2023-07-10 09:34:36.610649', '', '2023-07-09 22:00:00', 0.3, 1, 81, 5, 'metric',
                    NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1807, '', '2024-04-29 10:52:01.957026', '', '2024-04-29 10:52:01.957033', 530, 1, 1212, 5, 'metric',
                    NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1806, 'VisualisierungsPost auf WAC/Linkedin', '2024-04-29 10:50:10.464443', '',
                    '2024-04-29 10:50:10.46445', 4, 1, 1210, 9, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (183,
                    'Der Testrun hat mit Marc Schmid, Max, Adrian , Khoi und Pippo stattgefunden. Iwan hatte den Lead Rebecca hat unterstützt. Sie wird nun auch als 2. Trainerin den Lead am Puzzle Up übernehmen.',
                    '2023-08-16 19:09:21.463582', 'Iwan hat auf Anfrage von Mats ein Video zu ML erstellt.
    Das Grundgerüst des Labs mit Step by Step Anleitung steht nun muss aber mit dem Feedback aus dem Testrun verfeinert werden. Auch Infrastrukturthemen sind noch offen. ',
                    '2023-08-15 22:00:00', 0.4, 1, 59, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (238,
                    '3 Happiness-Umfragen durchgeführt und ausgewertet. Massnahmenpaket für das nächste Quartal in Erarbeitung.',
                    '2023-08-25 11:41:08.375944', '', '2023-08-24 22:00:00', 0.5, 1, 93, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (338, 'Umstellung auf Mecano mit Divisionsrentabilitätsrechnung. Messung nicht mehr möglich.',
                    '2023-09-14 12:33:22.313066', '', '2023-09-13 22:00:00', 0.7, 1, 81, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (266, 'Wie bisher', '2023-08-31 13:38:00.871096', '', '2023-08-30 22:00:00', 0.35, 1, 37, 5,
                    'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2149, '', '2024-06-17 15:51:39.940056', 'Anpassung Confidence Level', '2024-06-17 15:51:39.940059',
                    NULL, 1, 1135, 0, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1812, 'Keine Änderung', '2024-04-29 11:00:47.18347', '', '2024-04-29 11:00:47.183479', NULL, 1,
                    1220, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1808, '', '2024-04-29 10:52:55.373514', '', '2024-04-29 10:52:55.37352', 132, 1, 1214, 10, 'metric',
                    NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2283, '', '2024-07-16 06:45:50.937225', '', '2024-07-16 06:45:50.937227', NULL, 1, 1261, 6,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (349,
                    'Messung August: 82%. Ausblick: September tendentiell tiefer (wegen Neueintritten und ggf. Lücken)',
                    '2023-09-17 22:52:30.925267', 'Sales, Sales, Sales...', '2023-09-17 22:00:00', 82, 1, 99, 5,
                    'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (113,
                    'Immer noch Unsicherheit, ob Yelan überhaupt starten kann (Arbeitsbewilligung). Falls ja, haben wir mit Mobi Gaia Team einen vielversprechenden Ansatz mit Päscu Hurni vorbesprochen (Pairing mit David S., Einarbeitung Yelan gratis, dann verrechenbar).',
                    '2023-07-24 11:37:57.744395',
                    'Arbeitsbewilligung für Yelan erhalten (Verfahren immer noch hängig; Ausgang ungewiss). Wenn Ausgang positiv, dann Ausgangslage bei Mobi nutzen.',
                    '2023-07-23 22:00:00', 0.2, 1, 83, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (185, 'Members für Mobility Tag eingeladen. 1. Termin am 31.8.', '2023-08-17 09:35:58.180985', '',
                    '2023-08-16 22:00:00', 0, 1, 38, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2151, 'Anpassung Confidence Level', '2024-06-17 15:52:43.948429', '', '2024-06-17 15:52:43.948431',
                    NULL, 1, 1140, 0, 'ordinal', 'COMMIT', 2);
            INSERT INTO okr_pitc.check_in
            VALUES (1809, 'Aktuell noch keine Durchführung', '2024-04-29 10:53:40.077584', '',
                    '2024-04-29 10:53:40.07759', NULL, 1, 1215, 5, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2284, 'Austausch mit Alexandra von InnoArchitects zu Marktvalidierung mit KI',
                    '2024-07-16 06:46:38.033626', '', '2024-07-16 06:46:38.03363', NULL, 1, 1263, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1811, 'Keine Änderung', '2024-04-29 11:00:37.454923', '', '2024-04-29 11:00:37.454929', NULL, 1,
                    1218, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (319, 'Keine', '2023-09-12 06:40:43.592175', '', '2023-09-11 22:00:00', 0.5, 1, 58, 5, 'metric',
                    NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (112,
                    'Mündliche Zusage von Daniel Alder (70% Pensum). Er kann als Dev und Sys-ler eingesetzt werden und könnte die Pensumsreduktionen von Rémy und Tiago weitgehend kompensieren. Wert wird erst angepasst, wenn Vertrag unterschrieben vorliegt.',
                    '2023-07-24 11:32:40.671967',
                    'Vertrag mit Dani Alder ins Trockene bringen. Müsste bis Ende Juli möglich sein (mündl. zugesagt)',
                    '2023-07-23 22:00:00', -85, 1, 77, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (258,
                    'Mobi Mandat für David und Yelan sollte klappen. Carlo mit SAC zufrieden. Kurzfristig etwas Verbesserung bei Andres (Team-Projekt bei Centris bis im Okt, nicht mehr alleine). Ungelöst: Dani Alder (Leads: Mobi, BJUB)',
                    '2023-08-28 11:14:08.553093',
                    'Gutes Nachfolgemandat für Andres und Startmandat für Daniel Alder suchen, dann können wir auf 8 kommen.',
                    '2023-08-27 22:00:00', 7, 1, 72, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (339, 'Alles i.O.', '2023-09-14 12:33:47.645011', '', '2023-09-13 22:00:00', 0, 1, 104, 5, 'metric',
                    NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1810,
                    'Aktuell keine Änderung auf dem Papier (ich weiss nicht genau wo die Zahl herkommt)... In Fact ist ein bisschen was dazu gekommen.',
                    '2024-04-29 10:54:21.914254', '', '2024-04-29 10:54:21.91426', 92424, 1, 1191, 6, 'metric', NULL,
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1813, 'Abgleich mit Lukas durchgeführt - noch keine freigeschaltete Version von Collections. ',
                    '2024-04-29 11:03:03.330244', '', '2024-04-29 11:03:03.330249', NULL, 1, 1223, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (2285, '', '2024-07-16 06:47:38.62983', '', '2024-07-16 06:47:38.629832', 0, 1, 1275, 4, 'metric',
                    NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (239, 'Inputs weiter implementiert und Massnahmen umgesetzt.', '2023-08-25 11:42:36.056468', '',
                    '2023-08-24 22:00:00', 0.7, 1, 85, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2158,
                    'Stellenangebot ist draussen (für S.W.), wurde jedoch noch nicht definitiv angenommen, darum Verbleib auf "Fail" :-(',
                    '2024-06-17 22:26:06.389025', '', '2024-06-17 22:26:06.389027', NULL, 1, 1234, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (257,
                    'Yelan startet definitiv (alle Bewilligungen liegen vor). Einsatzmöglichkeite für Mobi (Gluon Migration) zusammen mit DSM ist offeriert. Kunde hat Commitment zu dieser Combo abgegeben.',
                    '2023-09-14 08:34:41.865426',
                    'Vertrag mit Mobi abschliessen. Fallback wäre das UZH Frontend-Dev-Mandat (Angebot eingereicht, Zuschlagsw''keit ~50%)',
                    '2023-08-27 22:00:00', 0.5, 1, 83, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (131,
                    'Die geplanten Arbeiten brauchen mehr Zeit als angenommen. Ein erster interner Testrun mit Adrian Bader und Khoi Tran sowie allenfalls einem nicht technischen Vertreter findet am 15.8 statt.',
                    '2023-07-27 15:58:02.525048', '', '2023-07-26 22:00:00', 0.25, 1, 59, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (207, 'Keine', '2023-08-24 08:04:15.45546', '', '2023-08-23 22:00:00', 0.4, 1, 59, 5, 'metric', NULL,
                    1);
            INSERT INTO okr_pitc.check_in
            VALUES (325, 'Zusage von Martin K.', '2023-09-13 06:13:08.720462', '', '2023-09-12 22:00:00', 0.2, 1, 35, 5,
                    'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1819, 'Es wurde eine zusätzliche Stelle ausgeschrieben. Total: 5', '2024-05-01 11:39:11.73286', '',
                    '2024-05-01 11:39:11.732877', NULL, 1, 1128, 7, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2286,
                    'Bei Kanton Bern vorgestellt. Ausstehend noch Rückmeldung vom Kanton zu Rahmenbedingungen der Data Goverance',
                    '2024-07-16 06:48:47.980441', '', '2024-07-16 06:48:47.980444', NULL, 1, 1276, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (2287, '', '2024-07-16 06:49:36.484655', '', '2024-07-16 06:49:36.484657', 0, 1, 1277, 4, 'metric',
                    NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (90, '0', '2023-07-12 13:11:24.906084', '', '2023-07-11 22:00:00', 0, 1, 105, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (89, '0', '2023-07-12 13:10:40.859889', '', '2023-07-11 22:00:00', 0, 1, 104, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (353,
                    'Am Techworkshop wurde ein OpenSpace zu PrivateGPT durchgeführt. Das Angebot dazu ist aber noch nicht erstellt.',
                    '2023-09-18 08:26:14.10592', '', '2023-09-17 22:00:00', 0.5, 1, 58, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (221, 'Alles i.O.', '2023-08-24 13:28:43.865556', '', '2023-08-23 22:00:00', 0, 1, 104, 5, 'metric',
                    NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1994,
                    'Notwendiges ist erfasst - Dokument muss noch ins Reine geschrieben werden, damit es vorgelegt werden kann.',
                    '2024-05-27 08:24:55.397411', '', '2024-05-27 08:24:55.397415', NULL, 1, 1185, 8, 'ordinal',
                    'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (326, 'Töfflitour-Event durchgeführt. Es war geil.', '2023-09-13 06:14:17.534125', '',
                    '2023-09-12 22:00:00', 0.3, 1, 57, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1995, '', '2024-05-27 08:25:18.347621', 'Phil nimmt das Thema nun auf.',
                    '2024-05-27 08:25:18.347624', NULL, 1, 1189, 6, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1820, '', '2024-05-01 12:05:01.754768', '', '2024-05-01 12:05:01.75479', 0, 1, 1136, 3, 'metric',
                    NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1640, 'Erste Termine sind aufgesetzt', '2024-04-04 14:00:16.579184', '',
                    '2024-04-04 14:00:16.579191', NULL, 1, 1142, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (220,
                    'Vertrag von Dani Alder wurde unterzeichnet. Er ist Dev+Sysler gleichzeitig. Angenommen, er ist halb/halb tätig, können wir mit ihm kalkulatorisch 70%/2=35% der verlorenen Sys-Pensen kompensieren.',
                    '2023-08-24 13:13:22.139287',
                    'Im Recruiting aktiv bleiben, sofern Bedarf vorhanden (im Moment eher on hold)',
                    '2023-08-23 22:00:00', -50, 1, 77, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (208, 'Keine', '2023-08-24 08:04:36.809448', '', '2023-08-23 22:00:00', 0.5, 1, 58, 5, 'metric',
                    NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (111,
                    'Einvernehmliche Lösung mit FZAG zu Ablösung von Tiago gefunden. Gleichzeitig müssen wir bei SNB temporäre Reduktion der Velocity in Kauf nehmen. Dies ist allerdings ein Budgetthema, das nicht direkt mit Pensumsreduktion von Tiago in Verbindung steht.',
                    '2023-07-24 11:28:57.986878', 'Vorgehen von SNB definitiv bestätigen lassen.',
                    '2023-07-23 22:00:00', 0.4, 1, 80, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (103, 'Technologien aktuell wie Lab (CML und DVC). Aktuelle Angebotsideen
    - MLOpsLab
    - Integration von ChatGPT bei Firmen / Suche über Unternehmensdaten mittels LLM (z.B. PrivateGPT)
    - MLOps Beratung', '2023-07-18 08:03:28.41304', '', '2023-07-17 22:00:00', 0.3, 1, 58, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1998, 'Noch keine News.', '2024-05-27 08:26:08.735639', '', '2024-05-27 08:26:08.735643', NULL, 1,
                    1190, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2165,
                    'Wir werden den Prozess nicht aufbauen, da die Resultate von Suricata nicht ausreichend verwaltbar sind.',
                    '2024-06-18 08:28:39.534999', '', '2024-06-18 08:28:39.535002', NULL, 1, 1187, 10, 'ordinal',
                    'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2090, '', '2024-06-11 09:19:01.147103', '', '2024-06-11 09:19:01.147107', NULL, 1, 1131, 1,
                    'ordinal', 'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2288, 'Kennenlernen mit Yup stattgefunden. Terminserie Abstimmung /mobility & Sales aufgesetzt',
                    '2024-07-16 06:50:22.584064', '', '2024-07-16 06:50:22.584067', NULL, 1, 1266, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (2289, 'Einladung von Berti für Workshop im August', '2024-07-16 06:51:07.119896', '',
                    '2024-07-16 06:51:07.119899', NULL, 1, 1267, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2208, 'keine Veränderung', '2024-07-02 08:38:28.416743', '', '2024-07-02 08:38:28.416747', 0, 1,
                    1315, 5, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (91, '0', '2023-07-12 13:11:35.251539', '', '2023-07-11 22:00:00', 0, 1, 79, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1631, 'Opportunitäten:
    - BAFU: Daten und Digitalisierung (PDD) (Einladungsverfahren)', '2024-04-03 18:06:38.667367', '',
                    '2024-04-03 18:06:38.667372', NULL, 1, 1176, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1632, 'Monat hat erst begonnen. Noch kein Umsatz.', '2024-04-03 18:09:27.097901', '',
                    '2024-04-03 18:09:46.510331', 0, 1, 1177, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1615, 'in Erarbeitung, Fokustage eingeführt, internes Audit findet am 30. Mai statt',
                    '2024-03-28 14:00:44.156854', '', '2024-03-28 14:00:44.156861', NULL, 1, 1129, 6, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1616, '', '2024-03-28 14:02:10.435636', '', '2024-03-28 14:02:32.571415', 2471130, 1, 1134, 6,
                    'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1817, 'Open Space am Leadership Monthly durchgeführt', '2024-04-29 15:23:22.379294', '',
                    '2024-04-29 15:23:22.379314', NULL, 1, 1188, 10, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2238, 'Austausch mit Members ist geplant', '2024-07-09 08:00:19.661192', '',
                    '2024-07-09 08:00:19.661194', NULL, 1, 1332, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2011, 'Give Away Konzept wird am Puzzle Inside Ende Juni vorgestellt
    Nachhaltiges Give Away Puzzle up (Druckvelo) in Produktion
    Interaktive Inszenierung am Puzzle up! sichergestellt (Rollup, Druckvelo)', '2024-05-28 07:30:59.102165', '',
                    '2024-05-28 07:30:59.102186', NULL, 1, 1184, 7, 'ordinal', 'STRETCH', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2009, '', '2024-05-28 07:25:52.894287', '', '2024-05-28 07:25:52.894308', NULL, 1, 1182, 8,
                    'ordinal', 'TARGET', 2);
            INSERT INTO okr_pitc.check_in
            VALUES (2017,
                    'Bedürfnisse Divisions wurden am Monthly abgeholt und wurden bereits besprochen. Integration und Leads noch offen.',
                    '2024-05-29 06:44:41.137811', '', '2024-05-29 06:44:41.137824', NULL, 1, 1131, 5, 'ordinal',
                    'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1636, 'Es wurden 4 neue Stellen ausgeschrieben.', '2024-04-04 06:34:40.792811', '',
                    '2024-04-04 06:34:40.792815', NULL, 1, 1128, 7, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2020, 'N/A', '2024-05-30 14:12:35.375084',
                    'Massnahmen und Messpunkte zu den 3 Fokusthemen definieren', '2024-05-30 14:12:35.375092', NULL, 1,
                    1194, 0, 'ordinal', 'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2019, '', '2024-05-30 04:52:24.495543', '', '2024-05-30 04:52:24.495552', 3318419, 1, 1134, 4,
                    'metric', NULL, 8);
            INSERT INTO okr_pitc.check_in
            VALUES (2010, '', '2024-05-28 07:28:22.010232', '', '2024-05-28 07:28:22.010254', NULL, 1, 1183, 7,
                    'ordinal', 'TARGET', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1638, '', '2024-04-04 12:58:06.394121', '2 Inserate erstellt, eines auf Webseite aufgeschaltet.',
                    '2024-04-04 12:58:06.394126', 0, 1, 1136, 4, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1639, '', '2024-04-04 12:58:26.014455', '', '2024-04-04 12:58:26.014459', NULL, 1, 1139, 5,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1664, '2 Interviews geplant, Abstimmungstermin mit POs für Lehrlingsprojekt definiert.',
                    '2024-04-11 12:51:48.759343', '', '2024-04-11 12:51:48.759348', NULL, 1, 1139, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1628,
                    'Sponsoring-Doku ist erstellt. Am 2. April werden im MarComSales-Weekly die möglichen Partner definiert. Anschliessend folgen die Anfragen via Partnermanager. ',
                    '2024-04-02 07:14:09.09109', '', '2024-04-02 07:14:09.091095', NULL, 1, 1183, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (2012, '', '2024-05-28 07:36:28.289317', '', '2024-05-28 07:36:28.289338', NULL, 1, 1193, 8,
                    'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2021, 'N/A', '2024-05-30 14:20:29.3606',
                    'Erarbeiten der Massnahmen. Weiterarbeit Hand in Hand mit KR ".... Arbeit im Team.... 3 Fokusthemen..."',
                    '2024-05-30 14:20:29.360611', NULL, 1, 1196, 0, 'ordinal', 'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2290,
                    'Austausch mit interessierten Members fand bilateral statt. Folgende Members starten im neuen Branch: Anna, Tru, Pippo, Odi, Bruno. Bruno möchte Branch Owner übernehmen.',
                    '2024-07-19 08:54:09.243354', '', '2024-07-19 13:17:04.361824', NULL, 1, 1245, 10, 'ordinal',
                    'TARGET', 3);
            INSERT INTO okr_pitc.check_in
            VALUES (1868, '- 75% durch Kündigung von Beat
    + 50% Neuanstellung G.B.', '2024-05-08 12:15:15.727991', '', '2024-05-08 12:15:55.488256', -0.25, 1, 1136, 3, 'metric',
                    NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1630, 'Austauschmeeting mit /mid am 4.4.2024 geplant.', '2024-04-02 07:17:47.394938', '',
                    '2024-04-02 07:17:47.394943', NULL, 1, 1193, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1825, 'Bisher gibt es eine Analyse von P32/P14 durch MarCom sowie eine Gegenüberstellung zu den 5P durch mich.

    Arbeitsstand Analyse: https://codimd.puzzle.ch/ZdHODTltQUSQPiu7AwR9EA', '2024-05-02 05:54:46.229356',
                    'Auslegeordnung mit Divisions planen', '2024-05-02 05:54:46.229371', NULL, 1, 1131, 3, 'ordinal',
                    'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1627, 'Anpassungen wurden mit DB besprochen, Ticket für April-Monthly wird noch erstellt.',
                    '2024-04-02 07:13:02.29328', '', '2024-04-02 07:13:02.293283', NULL, 1, 1182, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1879, 'Wir haben weitere Fokustage organisiert, damit wir grosse Schritte vorwärts kommen.',
                    '2024-05-13 04:39:33.661716', '', '2024-05-13 04:39:33.661722', NULL, 1, 1129, 6, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1841, 'Von Sales kommt noch wenig aktiv.', '2024-05-06 06:10:30.945989', '',
                    '2024-05-06 06:10:30.945996', 0, 1, 1144, 3, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1840, 'Keine Veränderung. Abstimmung mit Saraina heute', '2024-05-06 06:09:39.595429', '',
                    '2024-05-06 06:09:39.595435', 0, 1, 1137, 5, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1826, 'Wir kommen nicht wie geplant vorwärts, da wir zu wenig Zeit finden. ',
                    '2024-05-02 05:55:41.078133', 'Nicole Frey wird uns zusätzlich unterstützen',
                    '2024-05-02 05:55:41.078145', NULL, 1, 1129, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1675, 'keine Veränderung. SAC Verlängerung von Tobi steht an. Muss noch bestätigt werden',
                    '2024-04-15 08:00:52.834693', '', '2024-04-15 08:00:52.834697', 92424, 1, 1191, 6, 'metric', NULL,
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1663, 'Keine Änderung ggü. letzer Woche', '2024-04-10 15:04:44.78086', '',
                    '2024-04-10 15:04:44.780866', NULL, 1, 1142, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (110,
                    'Monatsabschluss Juni. Prognose für Juli ähnlich. Ausblick Aug+Sept noch mit Unsicherheiten behaftet.',
                    '2023-07-24 11:22:13.147836', 'Sales Kampagne', '2023-07-23 22:00:00', 75.6, 1, 99, 5, 'metric',
                    NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2211, 'keine Veränderung', '2024-07-02 08:39:31.990511', '', '2024-07-02 08:39:31.990515', NULL, 1,
                    1319, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2160, 'Absolut keine Zeit gehabt für dieses Thema.', '2024-06-17 22:29:17.47805',
                    'Wir nehmen es ins nächste Quartal mit...', '2024-06-17 22:29:17.478052', NULL, 1, 1235, 0,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1710, 'Consulting von Khôi bei HRM-Systems im Juni. Annahme 24h x CHF 90.- Mehrertrag = CHF 2160',
                    '2024-04-19 14:04:22.845342', '', '2024-04-19 14:04:22.845347', 2160, 1, 1141, 5, 'metric', NULL,
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1698, '3 spannende Professional Bewerbungen offen', '2024-04-17 07:10:09.113587', '',
                    '2024-04-17 07:10:09.113592', 0, 1, 1136, 6, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1629,
                    'Give Away-Konzept ist in Erarbeitung. Definition Give Away Puzzle Up! erfolgt heute im Rahmen eines Meetings.',
                    '2024-04-02 07:16:36.762408', '', '2024-04-02 07:16:36.762413', NULL, 1, 1184, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1673, 'in Arbeit', '2024-04-15 04:50:10.986944', '', '2024-04-15 04:50:10.986948', NULL, 1, 1129, 6,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1708, '', '2024-04-18 06:50:31.813922', '', '2024-04-18 06:50:31.813926', 2687767, 1, 1134, 5,
                    'metric', NULL, 2);
            INSERT INTO okr_pitc.check_in
            VALUES (1775, 'Keine Veränderung', '2024-04-29 05:49:33.507916', '', '2024-04-29 05:49:33.507924', NULL, 1,
                    1209, 5, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (102,
                    'Technologien MLflow und Kubeflow ausprobiert.Können nicht 1:1 verglichen werden. Für das Lab werden wir nun aber CML und DVC einsetzten. Erster Draft der Lab Struktur und Planung steht. Review der Arbeiten mit msc und rh erfolgt.',
                    '2023-07-18 07:56:21.157844', '', '2023-07-17 22:00:00', 0.2, 1, 59, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2089, '', '2024-06-10 14:15:25.552693', '', '2024-06-10 14:15:25.552698', 0, 1, 1144, 1, 'metric',
                    NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1869,
                    'Weiteres Kennenlernen für 29.5. abgemacht. Zudem Abstimmungstermin mit potenziellen Techleaders von /mobility',
                    '2024-05-08 13:55:14.347287', '', '2024-05-08 13:55:14.347294', NULL, 1, 1135, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1827, '- am Leadership-Team Workshop vom 30. April wurden die bestehenden und mögliche neue Marktopportunitäten besprochen
    - am Monthly Mai (27. Mai) werden wir die finale Entscheidung über die Marktopportunitäten für das GJ 24/25 treffen',
                    '2024-05-02 06:06:36.345525', '', '2024-05-15 11:16:45.064837', NULL, 1, 1132, 5, 'ordinal', 'FAIL',
                    2);
            INSERT INTO okr_pitc.check_in
            VALUES (2023,
                    'Abstimmung mit dbi: Inserat aufgeschaltet lassen, sonst Thema nicht aktivi weiter treiben bis Person für Themenlead gefunden ist.',
                    '2024-05-31 07:43:27.099065', '', '2024-05-31 07:43:27.099072', NULL, 1, 1135, 1, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1665, '2 Interviews fix geplant, weitere Bewerbungen in Pipeline', '2024-04-11 12:52:49.156715', '',
                    '2024-04-11 12:52:49.156719', 0, 1, 1136, 5, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1709, 'Keine zusätzliche Stellen ausgeschrieben.', '2024-04-18 08:17:14.157564', '',
                    '2024-04-18 08:17:14.157569', NULL, 1, 1128, 7, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2014,
                    '- am 6.6. werden der erweiterten Kerngruppe Digitale Lösungen die Resultate aus der Marktanalayse vorgestellt.',
                    '2024-05-28 08:42:40.521363', '', '2024-05-28 08:42:40.521385', NULL, 1, 1178, 3, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1674, '', '2024-04-15 04:51:08.497927', '', '2024-04-15 04:51:48.096285', 2491938, 1, 1134, 5,
                    'metric', NULL, 2);
            INSERT INTO okr_pitc.check_in
            VALUES (2022, 'N/A', '2024-05-30 14:33:48.01887', 'Gemäss Action Plan:', '2024-05-30 14:33:48.018878', 0, 1,
                    1197, 10, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2291, '', '2024-07-19 08:56:17.180664', '', '2024-07-19 08:56:34.53743', 2606285, 1, 1241, 4,
                    'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1880, '', '2024-05-13 04:41:42.101473', '', '2024-05-13 04:41:42.101535', 3038219, 1, 1134, 4,
                    'metric', NULL, 7);
            INSERT INTO okr_pitc.check_in
            VALUES (1881, 'Keine Veränderung von den Massnahmen. Durch Weggang Nadia ca 16kchF kostenreduktion',
                    '2024-05-13 06:10:36.825768', '', '2024-05-13 06:10:36.825774', 16000, 1, 1141, 10, 'metric', NULL,
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1842, 'Schwangerschaftsvertetung und Verantwortlichkeit Innoprozess ist gelärt',
                    '2024-05-06 06:11:57.354297', '', '2024-05-06 06:11:57.354308', NULL, 1, 1142, 4, 'ordinal',
                    'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1742, '5/29 Tasks erledigt', '2024-04-23 15:40:15.880817', '', '2024-04-23 15:40:15.880825', 5, 1,
                    1233, 6, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1652, '', '2024-04-08 08:34:58.62462', '', '2024-04-08 08:34:58.624624', NULL, 1, 1213, 6,
                    'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1777, 'Wird im Mai/Juni angegangen', '2024-04-29 05:52:48.398437', '', '2024-04-29 05:52:48.398443',
                    NULL, 1, 1222, 8, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (59, 'Yoga mit Pascal Simon', '2023-07-05 08:19:43.181628', '', '2023-07-04 22:00:00', 1, 1, 115, 5,
                    'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (60, 'Newsbeitrag PWorkshop, Hinweis an Simu Mario Kart-Event', '2023-07-05 08:22:10.928752', '',
                    '2023-07-04 22:00:00', 2, 1, 114, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (71, 'Resultate aus Happiness Umfrage noch nicht vorhanden für Auswertung.',
                    '2023-07-07 14:00:01.245679', '', '2023-07-06 22:00:00', 0, 1, 42, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (61, 'Kickoff hat stattgefunden', '2023-07-05 08:25:15.256532', '', '2023-07-04 22:00:00', 0.2, 1,
                    110, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (57, '1335', '2023-07-05 08:16:41.117668', '', '2023-07-04 22:00:00', 1345, 1, 109, 5, 'metric',
                    NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (63, 'Dagger-Video', '2023-07-05 08:30:48.809244', '', '2023-07-04 22:00:00', 1, 1, 118, 5, 'metric',
                    NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1751, 'unverändert', '2024-04-24 06:44:15.744511', '', '2024-04-24 06:44:15.744517', NULL, 1, 1135,
                    5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (56, '0', '2023-07-05 08:15:27.679992', 'Feedback Sebastian Preisner ausstehend',
                    '2023-07-04 22:00:00', 0, 1, 113, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (67,
                    'Termin mit Jürgen von UX im August aufgesetzt für Auswertung letzte Memberumfrage. Happiness Umfrage für Teams ist ready. Planung erfolgt. ',
                    '2023-07-05 08:48:04.819227', '', '2023-07-04 22:00:00', 0.1, 1, 36, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (70, 'Erste Messung', '2023-07-05 13:51:22.219061', '', '2023-07-04 22:00:00', 1.4, 1, 45, 5,
                    'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (54,
                    'Deep Dive AR und PLE für 22. August aufgesetzt. Challenging mit fse und mga aufgesetzt. Ferienbedingt läuft vorher nicht viel.',
                    '2023-07-05 10:37:43.175256', '', '2023-07-04 22:00:00', 0.1, 1, 119, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1759, 'Weggang Nadia Blatter ohne Ersetzung (Reduktion nicht verrechenbarer Pensen)',
                    '2024-04-25 07:58:44.581579', '', '2024-04-25 07:58:44.581586', 8000, 1, 1141, 10, 'metric', NULL,
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1717, 'Offerten draussen für Berti und Mayra (Mobi visualisierung). ', '2024-04-22 08:16:59.967317',
                    '', '2024-04-22 08:16:59.967322', NULL, 1, 1206, 5, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (73, 'Gemeinsames internes Verständnis geschafft, am Puzzle Workshop vorgestellt.',
                    '2023-07-09 06:05:24.161629', '', '2023-07-08 22:00:00', 0.15, 1, 47, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1743, 'Planung der Workshops für das Teammeeting vom 24.4. geplant.', '2024-04-23 16:18:05.020677',
                    '', '2024-04-23 16:18:05.020686', NULL, 1, 1235, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1702, 'Keine Veränderung', '2024-04-17 12:24:34.748258', '', '2024-04-17 12:24:34.748262', NULL, 1,
                    1207, 5, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1701, 'Blogpost, Direct Mailing & KCD Auftritt in Arbeit. Kommt gut.', '2024-04-17 12:23:46.166882',
                    '', '2024-04-17 12:23:46.166885', NULL, 1, 1188, 6, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1704, 'Keine Veränderung - Angebotsstory in Erarbeitung, Diskussion ist am 23.4. aufgesetzt.',
                    '2024-04-17 12:26:01.939347', '', '2024-04-17 12:26:01.939351', NULL, 1, 1229, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1778, 'Keine Veränderung - 2 zusätzliche interessierte Personen wurden abgelehnt.',
                    '2024-04-29 05:53:36.82726', '', '2024-04-29 05:53:36.827267', NULL, 1, 1216, 10, 'ordinal',
                    'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2015, '- am 6.6. findet der Puzzle Lunch mit AWS definitiv statt.
    - AWS und TD Synnex sind Partner am Puzzle up! vom Ende August.
    - das geplante Account Alignment mit GCP vom 27. Mai wurde leider verschoben (neuer Termin wird gesucht).',
                    '2024-05-28 08:44:21.592804', '', '2024-05-28 08:44:21.592827', NULL, 1, 1176, 3, 'ordinal', 'FAIL',
                    1);
            INSERT INTO okr_pitc.check_in
            VALUES (1828, '- die Marktanalyse ist noch in Erarbeitung. Aufgrund Ferienabwesenheiten und vielen Offerings fehlte die Zeit schneller vorwärts zu kommen.
    - Experten-Interview mit Gernot Hugl wird gepürft. Entscheid noch ausstehend.', '2024-05-02 06:10:11.428553', '',
                    '2024-05-02 06:10:11.428565', NULL, 1, 1178, 4, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2024, 'unvewrändert', '2024-05-31 07:44:32.891575', '', '2024-05-31 07:44:32.891582', -0.25, 1,
                    1136, 3, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1752, 'unverändert', '2024-04-24 06:44:59.667966', '', '2024-04-24 06:44:59.667972', 0, 1, 1136, 6,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1682, 'N/A', '2024-04-15 12:31:48.603428', '', '2024-04-15 12:31:48.603432', NULL, 1, 1194, 10,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1683, 'N/A', '2024-04-15 12:32:00.544092', '', '2024-04-15 12:32:00.544096', NULL, 1, 1196, 10,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1687, 'N/A', '2024-04-15 12:33:25.5621', '', '2024-04-15 12:37:06.040108', NULL, 1, 1221, 10,
                    'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2292,
                    'Kickoff findet erst Ende August statt. Daher wird das Target vermutlich erst im nächsten Quartal erreicht.',
                    '2024-07-19 13:16:02.539553', '', '2024-07-19 13:16:31.985709', NULL, 1, 1247, 1, 'ordinal', 'FAIL',
                    1);
            INSERT INTO okr_pitc.check_in
            VALUES (1843,
                    'In Arbeitsgruppe die Fokusthemen "Ziele", "Verantwortung", "Beteiligung an Entscheiden" untersucht, besprochen und weitere Schritte definiert (https://codimd.puzzle.ch/d3-selbstorganisation-methoden#)',
                    '2024-05-06 06:40:52.48897',
                    'Präsi zwecks Kenntnisnahme und Entscheid im Teammeeting, erarbeiten der Massnahmen',
                    '2024-05-06 06:40:52.488977', NULL, 1, 1194, 10, 'ordinal', 'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1719, 'Brandbook vom Mayra auf WAC und verlinkung auf Linkedin', '2024-04-22 08:21:58.315227', '',
                    '2024-04-22 08:21:58.315231', 3, 1, 1210, 9, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1718, 'Keine Veränderung seit letztem Checkin (Planung gemacht) ', '2024-04-22 08:18:05.493526', '',
                    '2024-04-22 08:18:05.49353', NULL, 1, 1208, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1882, 'Keine', '2024-05-13 06:11:23.752646', '', '2024-05-13 06:11:23.752653', NULL, 1, 1146, 10,
                    'ordinal', 'TARGET', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1703, 'In Arbeit', '2024-04-17 12:24:56.552453', '', '2024-04-17 12:24:56.552457', NULL, 1, 1211, 5,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1654, 'Planung gemacht', '2024-04-08 09:05:16.053632', '', '2024-04-08 09:05:16.053636', NULL, 1,
                    1188, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1772, 'Keine Veränderung', '2024-04-29 05:47:41.820626', '', '2024-04-29 05:47:41.820633', NULL, 1,
                    1207, 7, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1771, 'Läuft alles, kommt gut', '2024-04-29 05:47:05.65933', '', '2024-04-29 05:47:05.659336', NULL,
                    1, 1188, 10, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1745, 'März: CHF 289''513.- +++ Durchschnitt: CHF 289''513.-', '2024-04-23 16:23:40.289812',
                    'Weiter so...', '2024-04-23 16:23:40.289819', 289513, 1, 1163, 7, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (336, 'Neukundenanteil nicht erreicht, dafür viele Leads und mehrere Aufträge (z.T. kleine)
    Faktisch gewonnen: Mobi MongoDB, KaPo GR, SNB Ansible auf Windows, BKD.
    Zudem neue Leads: SIX Satellite, SwissLife DepTrack, FZAG Monitoring RFP, BLKB Java.', '2023-09-14 08:46:33.314305',
                    'Faktisch gewonnene Deals ins Trockene bringen. Nicht nachlassen bei Aquise. Angelschnur gespannt halten...',
                    '2023-09-13 22:00:00', 0.75, 1, 70, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (337, 'Umstellung auf Mecano mit Divisionsrentabilitätsrechnung. Messung nicht mehr möglich.',
                    '2023-09-14 12:33:10.990919', '', '2023-09-13 22:00:00', 0.3, 1, 82, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2016, 'Ypsomed neu dazu gekommen.', '2024-05-28 09:09:55.182605', '', '2024-05-28 09:09:55.182627',
                    61010, 1, 1177, 3, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2025, 'Projektentscheid: Entweder UniLu oder PTCS-Sheet, zudem OKR Tool',
                    '2024-05-31 07:49:24.186037', '', '2024-05-31 07:49:24.186044', NULL, 1, 1139, 6, 'ordinal',
                    'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1830, 'Bestellungen von SHKB und RhB (beide OpenShift).', '2024-05-02 06:22:40.443292', '',
                    '2024-05-02 06:22:40.443303', 56980, 1, 1177, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1754, 'Ticket fürs Leadership Monthly ist erstellt', '2024-04-24 08:03:21.696649', '',
                    '2024-04-24 08:05:44.626593', NULL, 1, 1182, 7, 'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1684, 'N/A', '2024-04-15 12:32:22.938782', '', '2024-04-15 12:32:22.938786', 0, 1, 1197, 5,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1765,
                    'Workshop durchgeführt, Verständnis geschafft, Ob "Digitale Lösungen" od "Indivisual Softwareentwicklung" spielt keine Rolle',
                    '2024-04-26 15:51:55.972257', 'Mit /ux, /mobility & /ruby abgleichen.',
                    '2024-05-06 06:55:55.686259', NULL, 1, 1204, 5, 'ordinal', 'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1764, '', '2024-04-26 15:50:15.245703', '', '2024-04-26 15:50:15.245709', 0, 1, 1199, 5, 'metric',
                    NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1720, 'Gemäss Batch auf Linkedin', '2024-04-22 09:33:19.140569', '', '2024-04-22 09:33:19.140589',
                    125, 1, 1214, 10, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1769, 'BAFU SAM kommt (Hupf & Mehmet eingeplant)', '2024-04-26 15:55:17.190451',
                    'Offerte Kanton Aargau bis Ende Mai erstellen', '2024-04-26 15:55:17.190457', NULL, 1, 1227, 10,
                    'ordinal', 'COMMIT', 2);
            INSERT INTO okr_pitc.check_in
            VALUES (1847, 'N/A', '2024-05-06 06:56:16.491582', 'Mit /ux, /mobility & /ruby abgleichen.',
                    '2024-05-06 06:56:16.491589', NULL, 1, 1204, 10, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1845, 'N/A', '2024-05-06 06:43:13.53317', 'siehe Action Plan', '2024-05-06 06:43:13.5332', 0, 1,
                    1197, 10, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1721, 'Aktueller Stand (Besuche im April) gemäss Matomo', '2024-04-22 09:35:14.919728', '',
                    '2024-04-22 09:35:14.91974', 430, 1, 1212, 5, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1725, 'Aktueller Stand unklar', '2024-04-22 09:44:44.341328', '', '2024-04-22 09:44:44.34133', NULL,
                    1, 1220, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1748, '246	386	Dani
    187	552	Carlo
    221	248	Tiago
    480	580	Remy
    542	552	Christian
    369	331	Nils
    248	552	Konstantin
    519	607	Urs
    318	497	Jürg
    204	386	David
    369	552	Yelan
    3703	5243	Total
    70.6275033377837 %
    ', '2024-04-23 16:36:55.7218', '', '2024-04-23 16:36:55.721808', 70.63, 1, 1174, 8, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1749, 'Bisher 1 Techinterview (L.S.)', '2024-04-23 16:38:32.688558', '',
                    '2024-04-23 16:38:32.688581', NULL, 1, 1175, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1773, 'In Arbeit - braucht mehr als angenommen.', '2024-04-29 05:48:10.545727', '',
                    '2024-04-29 05:48:10.545734', NULL, 1, 1211, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1655,
                    'Dävu Schneider involviert und seine Inputs aufgenommen. Wird am nächsten Austausch dabei sein.',
                    '2024-04-08 09:06:29.84513', '', '2024-04-08 09:06:29.845134', NULL, 1, 1229, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1671, 'noch offen ', '2024-04-11 13:36:50.896976', '', '2024-04-11 13:36:50.896981', NULL, 1, 1152,
                    5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1774, 'In Arbeit - erste Diskussion geführt und nächste Schritte definiert.',
                    '2024-04-29 05:49:19.318546', '', '2024-04-29 05:49:19.318552', NULL, 1, 1229, 4, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (2302, '', '2024-07-22 15:26:00.786357', '', '2024-07-22 15:26:00.78636', NULL, 1, 1261, 6,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1756, 'Feinheiten beim Give Away-Konzept werden ausgearbeitet. Anschliessend wird es dem Nachhaltigkeitsbranch präsentiert.
    Abklärungen des Give Awaysa für den Puzzle Up! laufen', '2024-04-24 08:05:16.209273', '', '2024-04-24 08:05:28.469931',
                    NULL, 1, 1184, 7, 'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1757, 'Open Space Leadership Monthly ist geplant für Ende April (abr)
    Blogpost in Erarbeitung
    Sponsoringauftritt KCD in Erarbeitung
    Direkt Mailing in Erarbeitung', '2024-04-24 08:07:47.887951', '', '2024-04-24 08:07:47.887958', NULL, 1, 1193, 8,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1915, 'Status quo', '2024-05-16 06:28:22.303261', '', '2024-05-16 06:28:33.020904', NULL, 1, 1182,
                    8, 'ordinal', 'COMMIT', 2);
            INSERT INTO okr_pitc.check_in
            VALUES (1832, 'Aktuell warten wir auf Feedback von den Angefragten.', '2024-05-02 07:14:33.548046', '',
                    '2024-05-02 07:14:33.548055', NULL, 1, 1183, 7, 'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1833, 'Status quo', '2024-05-02 07:16:41.090748', '', '2024-05-02 07:16:41.09076', NULL, 1, 1184, 7,
                    'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1755,
                    'Sponsoringanfragen an GCP, Acend, Red Hat und Isovalent sind raus. Rückmeldungen ausstehend.',
                    '2024-04-24 08:04:05.732178', '', '2024-04-24 08:06:30.65757', NULL, 1, 1183, 7, 'ordinal', 'FAIL',
                    1);
            INSERT INTO okr_pitc.check_in
            VALUES (1831, '', '2024-05-02 06:59:27.541742', '', '2024-05-02 06:59:27.54175', NULL, 1, 1182, 7,
                    'ordinal', 'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1689,
                    'Absolute Verrechnebarkeit Februar 2024: 62.4% // März sieht nach knapp über 70% aus, d.h. "Commit"',
                    '2024-04-15 12:35:51.975439', '', '2024-04-15 12:35:51.975443', NULL, 1, 1225, 5, 'ordinal',
                    'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1685, '', '2024-04-15 12:32:48.153975', '', '2024-04-15 12:32:48.153978', 0, 1, 1199, 7, 'metric',
                    NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1885, 'Von Sales kommt wenig aktiv.', '2024-05-13 06:21:59.698801', '',
                    '2024-05-13 06:21:59.698808', 0, 1, 1144, 2, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1738,
                    'Saraina stellt zu recht in Frage, ob die Kommunikation nur schriftlich erfolgen soll oder nicht auch mündlich. Aber dann später am Sommerinfo Anlass.',
                    '2024-04-23 09:26:24.925541', '', '2024-04-23 09:26:24.925559', 0, 1, 1137, 5, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1690, 'Identifizierte Aufträge: BAFU SAM, UniBern Studentenanmeldung, SAC, Kanton Aargau',
                    '2024-04-15 12:36:40.317677', '... weitere Aufträge identifizieren', '2024-04-16 14:46:23.855154',
                    NULL, 1, 1227, 10, 'ordinal', 'COMMIT', 2);
            INSERT INTO okr_pitc.check_in
            VALUES (1884, 'Gutes Angebot bei Mobi für ii gemacht. Beauftragung ausstehen.',
                    '2024-05-13 06:19:56.653737', '', '2024-05-13 06:19:56.653745', NULL, 1, 1140, 6, 'ordinal', 'FAIL',
                    1);
            INSERT INTO okr_pitc.check_in
            VALUES (1686, 'N/A', '2024-04-15 12:32:59.132224', '', '2024-04-15 12:32:59.132228', NULL, 1, 1204, 5,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1688, 'N/A', '2024-04-15 12:33:38.135283', '', '2024-04-15 12:33:38.135321', NULL, 1, 1224, 5,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1846, 'N/A', '2024-05-06 06:55:37.967358', 'siehe Action Plan', '2024-05-06 06:55:37.967366', 0, 1,
                    1199, 0, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1763, '', '2024-04-26 15:49:43.336755', '', '2024-04-26 15:49:43.336761', 0, 1, 1197, 10, 'metric',
                    NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1735,
                    'Geburtstagsparty dsm/sfa: 7 TeilnehmerInnen // Pingpong Indoor Masters: 7 TeilnehmerInnen. Kumuliert: 7+7=14',
                    '2024-04-23 09:06:16.447869', 'Monty Python Abend und Teamwanderung organisieren.',
                    '2024-04-23 09:06:16.447886', 14, 1, 1173, 8, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1750, 'Erstgespräche mit Interessenten sind geplant aber noch nicht durchgeführt.',
                    '2024-04-23 16:39:54.326483',
                    'Erstgespräche durchführen, ggf. Fübi-Treffen auf Terrasse durchführen, anschliessend Tech-Interviews...',
                    '2024-04-23 16:39:54.326491', NULL, 1, 1234, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1707, 'Schätzungen werden beim nächsten Grooming gemacht.', '2024-04-17 12:57:51.576227', '',
                    '2024-04-17 12:57:51.57623', NULL, 1, 1209, 5, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1776, 'Soweit gut.. Werden nach einem Monat eine Auslegeordnung machen.',
                    '2024-04-29 05:51:52.394618', '', '2024-04-29 05:51:52.394625', NULL, 1, 1213, 10, 'ordinal',
                    'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (115,
                    'Situation für David Simmen entschärft mit Mobi Gluon 2 Migration, die seinen Vorstellungen entspricht. Carlo mit SAC-Zusage ebenfalls gut mit spannender Büez eingedeckt.',
                    '2023-07-24 11:47:06.348672',
                    'Verbleibende Challenges: Andres (Varianten: Vontobel, PostAuto) und, falls er definitiv zusagt, ab Sept. Dani A. (BJUB, SAC, Swissmedic, Vontobel).',
                    '2023-07-23 22:00:00', 6, 1, 72, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1744, 'März: 12.1% +++ Kumuliert: 12.1%', '2024-04-23 16:20:07.126596',
                    'Gas geben im April und Mail...', '2024-04-23 16:20:07.126603', 12.1, 1, 1161, 7, 'metric', NULL,
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1909, 'Es wurde eine zusätzliche Stelle ausgeschrieben. Total: 6
    Es konnte ein Arbeitsvertrag abgeschlossen werden. Total: 1', '2024-05-15 09:25:56.824422', '',
                    '2024-05-15 09:26:10.165219', NULL, 1, 1128, 8, 'ordinal', 'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1916, '', '2024-05-16 06:28:58.659802', '', '2024-05-16 06:28:58.659813', NULL, 1, 1183, 7,
                    'ordinal', 'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1917,
                    'Richtlinien des Nachhaltigkeitsbranch bekannt, Give Away Konzept wird am Puzzle Inside präsentiert.',
                    '2024-05-16 06:30:05.150767', '', '2024-05-16 06:30:05.150774', NULL, 1, 1184, 7, 'ordinal',
                    'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1691, 'N/A', '2024-04-15 12:37:34.515178', '', '2024-04-15 12:37:34.515183', NULL, 1, 1228, 5,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1758, '', '2024-04-24 13:19:39.875461', '', '2024-04-24 13:19:39.875469', NULL, 1, 1138, 6,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2304, 'je auch zum Kick-off dazugenommen', '2024-07-22 15:27:19.984323', '',
                    '2024-07-22 15:27:19.984326', 0, 1, 1275, 4, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1848, 'N/A', '2024-05-06 06:57:04.801754', 'Übersicht erstellen, Besprechung in Weekly etablieren',
                    '2024-05-06 06:57:04.801765', NULL, 1, 1221, 10, 'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1837, 'Validierung mit Techboard erstellt. ', '2024-05-02 15:43:14.369094', '',
                    '2024-05-02 15:43:14.369102', NULL, 1, 1138, 7, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1887, 'Keine Änderung', '2024-05-13 06:58:20.005066', '', '2024-05-13 06:58:20.005074', 92424, 1,
                    1191, 6, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1886, 'Workshop im September mit OpenGIS', '2024-05-13 06:56:33.76382', '',
                    '2024-05-13 06:56:33.763827', NULL, 1, 1215, 10, 'ordinal', 'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2301, 'Am 29.7. Zweitgespräch, am 24.7. Erstgespräch', '2024-07-22 15:25:37.252868', '',
                    '2024-07-22 15:25:37.25287', -0.9, 1, 1259, 4, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1835, 'Controlling System ist implementiert. Einige Massnahmen schon getrackt.',
                    '2024-05-02 15:39:45.562491', '', '2024-05-02 15:40:08.620847', NULL, 1, 1146, 10, 'ordinal',
                    'TARGET', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2029, 'Übersicht erstellt, in Teammeeting besprochen, in Weekly integriert
    ', '2024-06-02 12:54:58.986993',
                    'Massnahmen: Events: Anwenden als fixer Bestandteil im Weekly / Sponsoring: T.B.D / Mit Markom besprechen',
                    '2024-06-02 12:55:11.072871', NULL, 1, 1221, 0, 'ordinal', 'COMMIT', 2);
            INSERT INTO okr_pitc.check_in
            VALUES (2061, 'Es konnte ein zusätzlicher Arbeitsvertrag abgeschlossen werden. Total: 2',
                    '2024-06-06 09:34:44.739921', '', '2024-06-06 09:34:44.739927', NULL, 1, 1128, 10, 'ordinal',
                    'TARGET', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2070, '', '2024-06-10 07:01:27.325076', '', '2024-06-10 07:01:27.325082', NULL, 1, 1185, 8,
                    'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2054, 'Es gibt noch einen Bug in den Blogposts bei der neuen Website. Daher Status Quo. ',
                    '2024-06-04 07:15:07.400095', '', '2024-06-04 07:15:07.400102', NULL, 1, 1179, 8, 'ordinal', 'FAIL',
                    1);
            INSERT INTO okr_pitc.check_in
            VALUES (2062, '65.5 h erreicht ', '2024-06-07 11:35:57.793436', '', '2024-06-07 11:35:57.793441', NULL, 1,
                    1149, 2, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1736, 'Marktvalidierung erfolgt, nächste Action Points geplant', '2024-04-23 09:11:17.333759', '',
                    '2024-04-23 09:11:42.184544', NULL, 1, 1140, 4, 'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2045,
                    'Sales intern Validierung aufgesetzt. Sales extern wird nicht durchgeführt. Dafür mit Yup zeitnah im Juli',
                    '2024-06-03 14:34:32.703275', '', '2024-06-03 14:34:32.703283', NULL, 1, 1138, 5, 'ordinal',
                    'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1737, '', '2024-04-23 09:24:09.465429', '', '2024-04-23 09:24:09.465447', NULL, 1, 1138, 6,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1890, 'On Track (Phippu)', '2024-05-13 07:07:24.071774', '', '2024-05-13 07:07:24.071782', 6, 1,
                    1210, 9, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1889, 'Keine Änderung', '2024-05-13 07:07:12.009153', '', '2024-05-13 07:07:12.009161', NULL, 1,
                    1208, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1928, '', '2024-05-21 11:32:37.474241', '', '2024-05-21 11:32:37.474245', 16000, 1, 1141, 10,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1888, 'Aktuell keine Änderung', '2024-05-13 07:06:58.659283', '', '2024-05-13 07:06:58.659293',
                    NULL, 1, 1206, 5, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2057, 'Planung wird im Leadership-Monthly im Juni verabschiedet.', '2024-06-04 07:19:08.712755', '',
                    '2024-06-04 07:19:08.712762', NULL, 1, 1182, 8, 'ordinal', 'TARGET', 2);
            INSERT INTO okr_pitc.check_in
            VALUES (1911, '- das Experten-Interview mit Gernot Hugl findet am 6.6. statt.
    - die Telefonkampagne mit Carlos wird durchgeführt und ist in Planung.', '2024-05-15 11:23:47.419965', '',
                    '2024-05-15 11:23:47.419971', NULL, 1, 1178, 6, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2056, '', '2024-06-04 07:16:11.869323', '', '2024-06-04 07:16:11.86933', 10, 1, 1200, 6, 'metric',
                    NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1741, '', '2024-04-23 09:29:12.731241', '', '2024-04-23 09:29:12.73126', 0, 1, 1144, 3, 'metric',
                    NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1929, '', '2024-05-21 11:32:55.676059', '', '2024-05-21 11:32:55.676062', NULL, 1, 1146, 10,
                    'ordinal', 'TARGET', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1952, 'April mit 74.8% knapp verfehlt', '2024-05-22 06:54:07.762879', '',
                    '2024-05-22 06:54:07.762883', NULL, 1, 1225, 0, 'ordinal', 'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1960, '', '2024-05-23 08:08:43.848296', '', '2024-05-23 08:09:11.395508', 3067433, 1, 1134, 3,
                    'metric', NULL, 8);
            INSERT INTO okr_pitc.check_in
            VALUES (2030, ' Ideen gesammelt, kategorisiert und eingeordet - Ideen im Team besprochen (Weeklys)',
                    '2024-06-02 12:56:06.448035',
                    'Liste der Sponsoring-Aktivitäten finalisiert. Abgleich mit /ux, /mobiity und /ruby – Bereichtsübergreifende Zusammenfassung & Kommunikation an LST und Markom ',
                    '2024-06-02 12:56:06.448041', NULL, 1, 1224, 0, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1838, 'Schulungstermin ist 3. Juli. Iwan bei Mobiliar angeboten', '2024-05-02 15:45:54.803324', '',
                    '2024-05-02 15:45:54.803333', NULL, 1, 1140, 6, 'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2309, 'Keine weiteren Tätigkeiten von /mobilty stattgefunden', '2024-07-22 15:32:39.983177', '',
                    '2024-07-22 15:32:39.983179', 0, 1, 1277, 4, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1854, 'Keine Änderung zur Vorwoche', '2024-05-06 07:23:07.659813', '', '2024-05-06 07:23:07.65982',
                    NULL, 1, 1206, 5, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1967, 'ERstes monatliches Strategie Controlling Meeting diese Woche', '2024-05-27 06:08:12.612474',
                    '', '2024-05-27 06:08:12.612478', NULL, 1, 1146, 10, 'ordinal', 'TARGET', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (264, 'Offizielle Zahl Marcel', '2023-09-06 06:00:56.98941', '', '2023-09-05 22:00:00', 79.8, 1, 33,
                    5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2152, 'April: 14.5% +++ Kumuliert: 12.1+14.5=26.6%', '2024-06-17 20:01:59.975727', '',
                    '2024-06-17 20:01:59.97573', 26.6, 1, 1161, 8, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1871, 'Direct Mailing in Arbeit, Sponsoringauftritt KCD ebenfalls in Planung',
                    '2024-05-08 17:32:25.972228', '', '2024-05-08 17:32:25.972235', NULL, 1, 1188, 10, 'ordinal',
                    'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (158, 'Inputs im nächsten August-Teammeeting umgesetzt', '2023-08-04 15:07:11.409739', '',
                    '2023-08-03 22:00:00', 0.4, 1, 85, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (194,
                    'Von Chrigel während Teammeeting angesprochen und Interesse geprüft. Thema wird in der /mid Week (November 23) vertieft angeschaut. Ziel ist ein PoC.',
                    '2023-08-21 12:40:47.05924', '', '2023-08-20 22:00:00', 0.7, 1, 105, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2058, 'Sponsorensuche ist abgeschlossen.', '2024-06-04 07:19:50.617535', '',
                    '2024-06-04 07:19:50.617542', NULL, 1, 1183, 7, 'ordinal', 'TARGET', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2107, '', '2024-06-13 06:07:40.903698', '', '2024-06-13 06:07:40.903701', 3440219, 1, 1134, 3,
                    'metric', NULL, 8);
            INSERT INTO okr_pitc.check_in
            VALUES (1912, '- Puzzle Cloud Lunch prov. für am 6.6. geplant ("Dev Experience auf AWS")
    - AWS wird prov. Partner am Puzzle up! 2024
    Opportunitäten:
    - BAFU: Daten und Digitalisierung (PDD) (Einladungsverfahren) (leider verloren)
    - SBB: Nachfolge Phil Matti, Senior System Engineer mit Fokus AWS', '2024-05-15 11:29:15.6891', '',
                    '2024-05-15 11:29:15.689106', NULL, 1, 1176, 4, 'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2115, '- diverse Leads aus Account Alignment mit GCP vom 13.06.24: Post am konkretesten',
                    '2024-06-14 05:13:49.833461', '', '2024-06-14 05:13:49.833463', NULL, 1, 1176, 0, 'ordinal',
                    'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1860, 'keine änderung', '2024-05-06 07:27:17.612715', '', '2024-05-06 07:27:17.612721', NULL, 1,
                    1218, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1894, 'Keine Änderung', '2024-05-13 07:10:05.534778', '', '2024-05-13 07:10:05.534785', NULL, 1,
                    1220, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2032,
                    'Girod versuchen in Peerdom zu platzieren, Für Hupf sieht es nach Überbuchung bis Ende Jahr aus. ',
                    '2024-06-02 12:59:27.99809',
                    'Peerdom fixieren, BKD-Arbeiten für zweite Jahreshälfte andiskutieren, Zusatzbudget für Bundesarchiv auftreiben (-> Meeting Ende Juli vorgesehen)',
                    '2024-06-02 12:59:27.998097', NULL, 1, 1227, 0, 'ordinal', 'COMMIT', 2);
            INSERT INTO okr_pitc.check_in
            VALUES (1862, 'Collections scheint sich als gut zu bestätigen und ist im Test',
                    '2024-05-06 07:28:00.264273', '', '2024-05-06 07:28:00.264281', NULL, 1, 1223, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1855, 'Keine Änderung seit letztem Checkin', '2024-05-06 07:23:24.108807', '',
                    '2024-05-06 07:23:24.108814', NULL, 1, 1208, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1966, '', '2024-05-27 06:06:32.924781', '', '2024-05-27 06:06:32.924785', NULL, 1, 1147, 10,
                    'ordinal', 'STRETCH', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2153, 'März: CHF 264''099.- +++ Durchschnitt: (289''513+264''099)/2 = CHF 276''806.-',
                    '2024-06-17 20:04:05.21133', '', '2024-06-17 20:04:05.211332', 276806, 1, 1163, 6, 'metric', NULL,
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (2154, 'Teamwanderung Aargau: 6. Kumuliert: 14+6 = 20', '2024-06-17 20:07:43.704734', '',
                    '2024-06-17 20:07:43.704737', 20, 1, 1173, 7, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2186, 'Noch keine Veränderung', '2024-06-27 07:07:13.792764', '', '2024-06-27 07:07:13.792771', 0,
                    1, 1311, 5, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2117, 'Sponsorensuche ist abgeschlossen.', '2024-06-14 07:47:24.629563', '',
                    '2024-06-14 07:47:24.629566', NULL, 1, 1183, 7, 'ordinal', 'TARGET', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1913, 'Keine Veränderung gegenüber letztem Check-in.', '2024-05-15 11:30:53.621996', '',
                    '2024-05-15 11:30:53.622003', 56980, 1, 1177, 4, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2185, 'Noch nichts passiert', '2024-06-27 07:06:40.989977', '', '2024-06-27 07:06:40.989984', NULL,
                    1, 1243, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1968, 'Weggang Nadia. Verkleinerung Kernteam', '2024-05-27 06:09:24.010891', '',
                    '2024-05-27 06:09:24.010895', 16000, 1, 1141, 10, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2041, 'Erstes monatliche Strategie-Controlling durchgeführt. Jede Massnahme aktuaisiert',
                    '2024-06-03 11:08:22.612112', '', '2024-06-03 11:08:22.612118', NULL, 1, 1146, 10, 'ordinal',
                    'STRETCH', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1856, 'Boom-Summit checking', '2024-05-06 07:23:40.130865', '', '2024-05-06 07:23:40.130878', 5, 1,
                    1210, 9, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2308, 'msc und ii sind dran', '2024-07-22 15:31:09.468235', '', '2024-07-22 15:31:09.468237', NULL,
                    1, 1276, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1861, 'keine änderung', '2024-05-06 07:27:27.499566', '', '2024-05-06 07:27:27.499575', NULL, 1,
                    1220, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1895, 'Nextcloud Collections eignet sich gut als Tool (noch auf der Test-Umgebung)',
                    '2024-05-13 07:10:40.114785', '', '2024-05-13 07:10:40.114792', NULL, 1, 1223, 5, 'ordinal',
                    'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1947, 'N/A', '2024-05-22 06:49:42.233736', 'siehe Action Plan', '2024-05-22 06:49:42.23374', 0, 1,
                    1197, 10, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2155, '100. Lunch Explorer Jubi: 11 Teilnehmende. Kumuliert: 20+11 = 31

    Die regulären Austragungen von LXZH und Chez Leu noch gar nicht eingerechnet...', '2024-06-17 20:14:00.221531', '',
                    '2024-06-17 20:14:00.221534', 31, 1, 1173, 10, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1969, '', '2024-05-27 06:34:47.77326', '', '2024-05-27 06:34:47.773263', NULL, 1, 1226, 4,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2252, '', '2024-07-11 06:30:16.003315', '', '2024-07-11 06:30:16.003318', 74.83, 1, 1349, 1,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2119, '', '2024-06-14 07:50:12.19861', '', '2024-06-14 07:50:12.198613', NULL, 1, 1193, 8,
                    'ordinal', 'STRETCH', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2109, '- die erste Marktanalyse ist erstellt und wurde der Kerngruppe Digitale Lösungen am 6. Juni vorgestellt.
    - die Telefonkampagne mit Carlos ist gestartet und die Resultate werden in die Analyse eingearbeitet.',
                    '2024-06-13 06:24:15.173703', '', '2024-06-13 06:24:15.173707', NULL, 1, 1178, 10, 'ordinal',
                    'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2202, '', '2024-07-02 07:55:32.302629', '', '2024-07-02 07:55:32.302633', NULL, 1, 1280, 5,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1914, 'Auslegeordnung abgeschlossen', '2024-05-15 11:44:25.327669',
                    'Auslegeordnung bzw. Bedürfnissklärung mit LST ist geplant am Mai Monthly',
                    '2024-05-15 11:44:25.327675', NULL, 1, 1131, 3, 'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2221, '', '2024-07-04 14:59:07.355657', '', '2024-07-04 14:59:07.355665', NULL, 1, 1263, 5,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2223, 'Nicht Deep Dive sondern Kickoff für Erwartungsmanagement', '2024-07-08 06:24:59.104764', '',
                    '2024-07-08 06:24:59.104768', 0, 1, 1275, 5, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2222, 'Erste interessierte Members gefunden', '2024-07-05 06:43:07.61485', '',
                    '2024-07-05 06:43:26.401117', NULL, 1, 1245, 10, 'ordinal', 'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1896, 'Fehlt noch Bestellung von Iwan', '2024-05-13 07:44:20.145975', '',
                    '2024-05-13 07:44:20.145982', NULL, 1, 1140, 6, 'ordinal', 'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2226, '', '2024-07-08 15:34:18.770505', '', '2024-07-08 15:34:18.770509', NULL, 1, 1276, 5,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1953, 'N/A', '2024-05-22 06:59:16.359499', 'Dran bleiben', '2024-05-22 06:59:16.359508', NULL, 1,
                    1227, 10, 'ordinal', 'COMMIT', 2);
            INSERT INTO okr_pitc.check_in
            VALUES (2220, '', '2024-07-04 14:52:43.325568', '', '2024-07-04 14:52:43.325571', NULL, 1, 1261, 6,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1857, 'Etwas tieferer Anstieg (rund 100 pro Woche)', '2024-05-06 07:24:51.472847', '',
                    '2024-05-06 07:24:51.472857', 603, 1, 1212, 5, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1954, 'N/A', '2024-05-22 06:59:48.621792', 'Ausschreiben', '2024-05-22 06:59:48.621796', NULL, 1,
                    1228, 0, 'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2188, '', '2024-06-27 07:24:02.000523', '', '2024-06-27 07:24:02.000529', 2295853, 1, 1241, 5,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2219, '', '2024-07-04 09:56:46.020096', '', '2024-07-04 09:56:46.020099', 2350345, 1, 1241, 5,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2042,
                    'Weiterhin von Mobiliar mündliche Zusage, Aber wir werden hingehalten mit unterschiedlichen Signalen...',
                    '2024-06-03 11:26:00.158599', '', '2024-06-03 14:33:28.725133', NULL, 1, 1140, 4, 'ordinal',
                    'COMMIT', 2);
            INSERT INTO okr_pitc.check_in
            VALUES (2099, 'OV Ok
    Rohrmax hat die Hälfte aller Tickets bearbeitet - Genügt zur Erreichung vom Target, da eine 100%ige Bearbeitung nicht Zielführend wäre.',
                    '2024-06-11 13:46:23.592146', '', '2024-06-11 13:46:23.592149', NULL, 1, 1209, 10, 'ordinal',
                    'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2234, 'Info hat stattgefunden', '2024-07-09 07:57:42.474927', '', '2024-07-09 07:57:42.474931',
                    NULL, 1, 1326, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2156, 'Alder Daniel	394	526
    Beltrame Carlo	324	432
    da Costa Cova Tiago	389	338
    Keil Rémy	751	790
    Kolinski Christian	733	752
    Kreienbühl Nils	665	451
    Kreutzer Konstantin	610	752
    Roesch Urs	776	827
    Schulthess Jürg	576	677
    Simmen David	381	526
    Tao Yelan	621	752
    Total	6220	6823

    0.911622453466217%', '2024-06-17 22:20:34.224095', '', '2024-06-17 22:20:34.224097', 91.16, 1, 1174, 10, 'metric', NULL,
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1955,
                    'Gleicher Stand wie letztes Mal. Aktuell in Abklärungen mit Pippo, was es noch braucht für den Target-Teil. ',
                    '2024-05-22 10:16:53.365567', '', '2024-05-22 10:16:53.365571', NULL, 1, 1142, 4, 'ordinal',
                    'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (92, '0', '2023-07-12 13:11:44.571004', '', '2023-07-11 22:00:00', 0, 1, 76, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2132, 'keine Veränderung', '2024-06-17 11:15:22.353501', '', '2024-06-17 15:28:06.880661', NULL, 1,
                    1206, 0, 'ordinal', 'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2254, '', '2024-07-11 07:17:15.463142', '', '2024-07-11 07:17:15.463144', NULL, 1, 1243, 5,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2250, '- Absprache zwischen ek und mw hat stattgefunden.
    - Kick-Off für MO Beratung mit passendem Teilnehmerkreis für nach den Sommerferien organisiert (7.8.24).
    - ek hat erste Ideen für marktorientieres Angebot zusammengetragen (welches auch Themen der Beratung enthält).',
                    '2024-07-10 19:47:07.360518', '', '2024-07-10 19:47:07.36052', NULL, 1, 1251, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1970, 'Thema konnte gefunden werden, Blogpost wird ausgearbeitet.', '2024-05-27 06:36:49.169421',
                    '', '2024-05-27 06:37:35.69721', NULL, 1, 1192, 8, 'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2203, '', '2024-07-02 07:55:55.180854', '', '2024-07-02 07:55:55.18086', 0, 1, 1318, 5, 'metric',
                    NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2168, 'Blogpost auf nächstes Quartal verschoben.', '2024-06-18 08:30:28.636099', '',
                    '2024-06-18 08:30:28.636101', NULL, 1, 1192, 0, 'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2166, 'Phil war krank, eventuell reicht es noch diesen Monat, aber das hat verzögert.',
                    '2024-06-18 08:29:10.765583', '', '2024-06-18 08:29:10.765585', NULL, 1, 1189, 0, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (2253, 'Projektteam definiert. Kickoff heute', '2024-07-11 07:16:48.765722', '',
                    '2024-07-11 07:16:48.765725', NULL, 1, 1350, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2167, 'Leider keine Referenz erhalten.', '2024-06-18 08:29:53.205623', '',
                    '2024-06-18 08:29:53.205625', NULL, 1, 1205, 0, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2227, 'Inserat angepasst und neu auf jobs.ch aufgeschaltet.', '2024-07-08 16:49:54.497816', '',
                    '2024-07-08 16:49:54.49782', -0.9, 1, 1259, 3, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2043, 'Validierung mit Sales intern aufgesetzt. Sales extern offen bzw. mit wem?',
                    '2024-06-03 11:30:51.988085',
                    'Wir haben 18 Validierungen mit Members. WEr fehlt noch? Stefan, Mättu Begert?',
                    '2024-06-03 11:30:51.988092', NULL, 1, 1138, 5, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1897, 'Validierung mit Members beauftragt', '2024-05-13 07:50:29.291311', 'Sales Termine erstellen',
                    '2024-05-13 07:50:29.291319', NULL, 1, 1138, 7, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2224,
                    'ar nochmals darauf aufmerksam gemacht, ein Status Update Meeting einzuführen mit dbi und mir. Und allenfalls auch Yup und jemand von /mid',
                    '2024-07-08 06:38:38.660902', '', '2024-07-08 06:38:38.660912', 0, 1, 1277, 5, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1898, 'Termin bei Sales Puzzle angefragt, Termin bei Dave Kilchenmann angefragt',
                    '2024-05-13 08:26:46.93511', '', '2024-05-13 08:26:46.935122', NULL, 1, 1138, 7, 'ordinal',
                    'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1859, 'Aktuell noch keine Durchführung', '2024-05-06 07:27:07.922212', '',
                    '2024-05-06 07:27:07.922218', NULL, 1, 1215, 5, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1858, '', '2024-05-06 07:26:50.632652', '', '2024-05-06 07:26:50.632659', 136, 1, 1214, 10,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2235, 'Umfrage läuft, erster Partner-Pitch geplant', '2024-07-09 07:58:13.365619', '',
                    '2024-07-09 07:58:13.365622', 0, 1, 1343, 5, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2236, 'Planung in Arbeit', '2024-07-09 07:58:43.996949', '', '2024-07-09 07:58:43.996951', 0, 1,
                    1344, 5, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2157,
                    'Neues Techinterview geführt (B.R.). Er hat Angebot erhalten und wurde durch Team validiert. Er hat das Angebot aber noch nicht angenommen.',
                    '2024-06-17 22:24:47.806719',
                    'Ob das Stellenangebot angenommen wird, entscheidet sich diese Woche und ist ziemlich offen.',
                    '2024-06-17 22:24:47.806721', NULL, 1, 1175, 5, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2255, '', '2024-07-11 07:17:57.44252', '', '2024-07-11 07:17:57.442522', 0, 1, 1311, 5, 'metric',
                    NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1793, '', '2024-04-29 08:55:31.926936', '', '2024-04-29 08:55:31.926942', NULL, 1, 1187, 5,
                    'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2112, 'Mobiliar dazu gekommen.', '2024-06-13 06:32:10.855283', '', '2024-06-13 06:32:10.855286',
                    72959, 1, 1177, 3, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2251, '- wir haben das weitere Vorgehen für die MO Digitale Lösungen besprechen und festgelegt. Terminfindung für Workshop läuft.
    - wir wollen wir Marktanalyse, welche aktuell noch läuft, mit weiteren Experten-Interviews mit Architekten von Kunden ergänzen (wo es terminlich noch reicht).',
                    '2024-07-10 19:49:27.073877', '', '2024-07-10 19:49:27.073879', NULL, 1, 1254, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1794, '', '2024-04-29 08:55:38.766838', '', '2024-04-29 08:55:38.766845', NULL, 1, 1189, 5,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1791, 'Arbeti daran ist fortgeschritten, aber noch nicht fertig.', '2024-04-29 08:55:12.311792', '',
                    '2024-04-29 08:55:12.311798', NULL, 1, 1185, 8, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2100, '', '2024-06-11 14:16:55.252106', '', '2024-06-11 14:16:55.252109', NULL, 1, 1186, 7,
                    'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1789, 'Nicht alle Tasks sind so beschrieben. ', '2024-04-29 08:54:13.948232', '',
                    '2024-04-29 08:54:13.948238', NULL, 1, 1198, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2228, 'Kickoff am 7.8.2024', '2024-07-08 16:50:53.073812', '', '2024-07-08 16:50:53.073816', NULL,
                    1, 1262, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2170, '', '2024-06-18 08:31:30.567695', '', '2024-06-18 08:31:30.567698', NULL, 1, 1226, 0,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2169, 'Thema ist gefunden und Materialien auch, aber nicht validiert.',
                    '2024-06-18 08:30:58.733322', '', '2024-06-18 08:31:06.302618', NULL, 1, 1190, 0, 'ordinal',
                    'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1795, '', '2024-04-29 08:55:44.035441', '', '2024-04-29 08:55:44.035448', NULL, 1, 1190, 5,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2225, 'Abstimmungstermin am 14.8. mit Mäge', '2024-07-08 06:40:35.820797', '',
                    '2024-07-08 06:40:35.820802', NULL, 1, 1267, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2079, '', '2024-06-10 13:36:18.418885', '', '2024-06-10 13:36:18.41889', NULL, 1, 1138, 5,
                    'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2124, 'Vale AWS cert.', '2024-06-14 12:52:33.992703', '', '2024-06-14 12:52:33.992706', NULL, 1,
                    1207, 10, 'ordinal', 'TARGET', 3);
            INSERT INTO okr_pitc.check_in
            VALUES (2125, 'OGZH Deal gewonnen', '2024-06-14 13:03:43.354514', '', '2024-06-14 13:04:05.835333', 968405,
                    1, 1230, 1, 'metric', NULL, 3);
            INSERT INTO okr_pitc.check_in
            VALUES (2127, 'unverändert', '2024-06-14 13:04:53.355808', '', '2024-06-14 13:04:53.355811', NULL, 1, 1216,
                    10, 'ordinal', 'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2159, '21/29 Tasks abgearbeitet', '2024-06-17 22:28:01.906772', '', '2024-06-17 22:28:01.906774',
                    22, 1, 1233, 8, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2192, '', '2024-06-27 09:29:03.710205', '', '2024-06-27 09:29:03.710214', 75.73, 1, 1349, 2,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2259, '', '2024-07-15 06:52:12.495851', '', '2024-07-15 06:52:12.495854', NULL, 1, 1334, 5,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2085, 'Teampool definiert. Jedoch zurückgestellt bis externer Spezialist gefunden.',
                    '2024-06-10 13:54:49.829232', '', '2024-06-10 13:54:49.829241', NULL, 1, 1135, 1, 'ordinal',
                    'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1790, 'Andere Themen waren bisher dringender.', '2024-04-29 08:54:44.391402', '',
                    '2024-04-29 08:54:44.391408', NULL, 1, 1226, 4, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2086, 'Confidence reduziert.', '2024-06-10 13:55:43.439313', '', '2024-06-10 13:55:43.439319',
                    -0.25, 1, 1136, 1, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1792, '', '2024-04-29 08:55:23.324522', '', '2024-04-29 08:55:23.324528', NULL, 1, 1186, 7,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1958, 'Betreuungsperson BBT für Basislehrjahr (SBB) und Java Bereich angestellt. Betreuung während pe''s Abwesenheit: js oder cg.
    Projekt UniLu', '2024-05-22 12:22:11.430781', '', '2024-05-22 12:22:11.430785', NULL, 1, 1139, 6, 'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1797,
                    'Mark konnte bei einigen Projekten aushelfen und fragen beantworten: Von JWTs über Keycloak zu Vulns. Tatsächlich auch verrechenbar bei Brixel. Referenz wird aber noch abgeklärt, drum mal nur Commit.',
                    '2024-04-29 08:57:24.374326', '', '2024-04-29 08:57:24.374333', NULL, 1, 1205, 9, 'ordinal',
                    'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2135,
                    'Grundstruktur auf POC-Blattform erstellt, Briefing / Vorstellung Members am letzten WAC-Morning. Aktuell noch keine Partizipation durch Members. ',
                    '2024-06-17 12:59:16.802215', '', '2024-06-17 12:59:16.802218', NULL, 1, 1223, 10, 'ordinal',
                    'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2191, 'Projektteam wird aktuell angefragt.', '2024-06-27 07:52:04.158235', '',
                    '2024-06-27 07:52:04.158245', NULL, 1, 1350, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2113,
                    'Auf Input von Saraina Plan geändert und nicht schriftliche Info, sondern mündliche Info am Puzzle Workshop mit Open Space für Feedback',
                    '2024-06-13 12:18:43.160035', '', '2024-06-13 12:18:43.160039', 0, 1, 1137, 0, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2082, '', '2024-06-10 13:38:17.191616', '', '2024-06-10 13:38:17.191634', NULL, 1, 1146, 10,
                    'ordinal', 'STRETCH', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2083, '', '2024-06-10 13:38:27.712735', '', '2024-06-10 13:38:27.71274', NULL, 1, 1147, 10,
                    'ordinal', 'STRETCH', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2114, '', '2024-06-13 12:19:37.515309', '', '2024-06-13 12:19:37.515323', 0, 1, 1144, 0, 'metric',
                    NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2080, 'Antwort von Mobi zu Iwan immer noch ausständig', '2024-06-10 13:37:36.170857', '',
                    '2024-06-10 13:37:36.170862', NULL, 1, 1140, 2, 'ordinal', 'COMMIT', 2);
            INSERT INTO okr_pitc.check_in
            VALUES (2081, '', '2024-06-10 13:38:06.38461', '', '2024-06-10 13:38:06.384616', 16000, 1, 1141, 10,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2126, 'Unverändert', '2024-06-14 13:04:39.476592', '', '2024-06-14 13:04:39.476595', NULL, 1, 1217,
                    4, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2088, 'Stand wie 3.6.', '2024-06-10 14:14:59.011153', '', '2024-06-10 14:14:59.011158', NULL, 1,
                    1142, 10, 'ordinal', 'STRETCH', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1981, 'Markom & Sales aus mobility Sicht Pensen und Zuständigkeiten geklärt',
                    '2024-05-27 07:18:46.860535', '', '2024-05-27 07:18:46.860538', NULL, 1, 1142, 4, 'ordinal',
                    'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (75, 'Bisher noch keine Blogposts zu Kubermatic, ML Themen und Cilium. Video zu Dagger erschienen.',
                    '2023-07-10 05:47:46.120715', '', '2023-07-09 22:00:00', 0, 1, 49, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2087, 'unverändert', '2024-06-10 13:56:29.119705', '', '2024-06-10 13:56:29.119716', NULL, 1, 1139,
                    6, 'ordinal', 'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (361, 'Konzept immer noch bei der GL', '2023-09-18 11:01:24.854381', '', '2023-09-17 22:00:00', 1, 1,
                    110, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (76, 'Wir sind auf Kurs. Erste News zu Events (Puzzle Workshop) erschienen',
                    '2023-07-10 05:49:20.120696', '', '2023-07-09 22:00:00', 0.1, 1, 40, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1796, '', '2024-04-29 08:55:48.606419', '', '2024-04-29 08:55:48.606426', NULL, 1, 1192, 5,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1980, '', '2024-05-27 07:03:18.116783', '', '2024-05-27 07:03:28.423602', NULL, 1, 1198, 7,
                    'ordinal', 'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2141, 'N/A', '2024-06-17 15:06:26.807345', 'In nächsten Zyklus nehmen',
                    '2024-06-17 15:06:26.807348', NULL, 1, 1221, 0, 'ordinal', 'COMMIT', 2);
            INSERT INTO okr_pitc.check_in
            VALUES (274, 'Inputs aus Durchsprache im Team konsolidiert.', '2023-09-06 05:57:19.014945',
                    'Finale Durchsprache am 13.9.', '2023-09-05 22:00:00', 0.65, 1, 36, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2139, 'N/A', '2024-06-17 15:05:03.591247', 'In nächsten Zyklus nehmen', '2024-06-17 15:05:03.59125',
                    1, 1, 1199, 0, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2140, 'N/A', '2024-06-17 15:06:07.946044', 'In nächsten Zyklus nehmen',
                    '2024-06-17 15:06:07.946047', NULL, 1, 1204, 0, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2137, 'N/A', '2024-06-17 15:03:10.777175', 'In nächsten Zyklus nehmen',
                    '2024-06-17 15:03:10.777178', NULL, 1, 1196, 0, 'ordinal', 'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2138, 'Termin 25. Juni 2024 (einfach ein Essen)', '2024-06-17 15:03:54.558573', '',
                    '2024-06-17 15:04:33.677994', 1, 1, 1197, 10, 'metric', NULL, 2);
            INSERT INTO okr_pitc.check_in
            VALUES (2136, 'N/A', '2024-06-17 15:02:38.168633', 'In nächsten Zyklus nehmen',
                    '2024-06-17 15:02:38.168636', NULL, 1, 1194, 0, 'ordinal', 'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2241, '', '2024-07-09 10:32:49.766128', '', '2024-07-09 10:32:49.76613', 2473356, 1, 1241, 5,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (74, 'Mir sind noch keine Auswertung bekannt.', '2023-07-09 06:06:40.661812', '',
                    '2023-07-08 22:00:00', 0, 1, 41, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2145, 'N/A', '2024-06-17 15:09:08.137627', 'In nächsten Zyklus verschoben',
                    '2024-06-17 15:09:08.137629', NULL, 1, 1228, 0, 'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2242, 'Noch nicht gestartet', '2024-07-09 10:33:49.788583', '', '2024-07-09 10:33:49.788586', NULL,
                    1, 1247, 3, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2143, 'Auch im Mai tiefer als 70%, da Eintritt und viel Sales-Aufwand',
                    '2024-06-17 15:08:01.503585', '', '2024-06-17 15:08:01.503588', NULL, 1, 1225, 0, 'ordinal', 'FAIL',
                    1);
            INSERT INTO okr_pitc.check_in
            VALUES (2144, 'N/A', '2024-06-17 15:08:45.036831',
                    'Einige Möglichkeiten aber noch keine konkrete Pläne. Muss weiterverfolgt werden',
                    '2024-06-17 15:08:45.036834', NULL, 1, 1227, 0, 'ordinal', 'COMMIT', 2);
            INSERT INTO okr_pitc.check_in
            VALUES (2142, 'N/A', '2024-06-17 15:06:54.928846', 'In nächsten Zyklus nehmen',
                    '2024-06-17 15:06:54.928849', NULL, 1, 1224, 0, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (68, 'Erste Happiness-Umfrage ausgewertet, zweite HU durchgeführt.', '2023-08-04 15:07:53.902649',
                    '', '2023-07-04 22:00:00', 0.2, 1, 93, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (69, 'Erste Inputs von Team erhalten (Teammeeting Juli)', '2023-08-04 15:06:12.18436', '',
                    '2023-07-04 22:00:00', 0.15, 1, 85, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (81, '68 Prozent', '2023-08-21 11:37:24.323393', '', '2023-07-10 22:00:00', 0, 1, 82, 5, 'metric',
                    NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (79, 'In Ausarbeitung', '2023-07-10 07:41:15.810373', '', '2023-07-09 22:00:00', 0, 1, 46, 5,
                    'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (105, 'weekly news neu jeweils mit Events, aufgrund der Sommerpause ist es etwas ruhiger',
                    '2023-07-18 15:02:10.781888', '', '2023-07-17 22:00:00', 2, 1, 114, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (104,
                    'Das Board ist erstellt und mit Sales besprochen. Es gibt noch einen Input betreffend der Events und dann können wir es dem bl-weekly präsentieren. ',
                    '2023-07-18 15:01:04.037399', '', '2023-07-17 22:00:00', 0.8, 1, 117, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (106, 'Post mit neuem Teambild und Hink zu unserer Kultur und den offenen Stellen',
                    '2023-07-18 15:02:59.58015', '', '2023-07-17 22:00:00', 2, 1, 115, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (101, '0', '2023-07-17 16:29:02.729704', 'Organisation der einzelnen Events ist den Subteams übertragen und erste Umsetzungsideen sind vorhanden.
    Teams und "Spielplan" für Mobility Töggeliturnier sind gemacht.', '2023-07-16 22:00:00', 0, 1, 57, 5, 'metric', NULL,
                    1);
            INSERT INTO okr_pitc.check_in
            VALUES (99, '0', '2023-07-17 15:59:57.416401',
                    'Aktuell eine spannende Bewerbung offem, nächste Woche 2. Gespräch geplant.', '2023-07-16 22:00:00',
                    0, 1, 35, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (229, 'End of Summerparty ZH', '2023-08-25 07:27:36.889448', '', '2023-08-24 22:00:00', 3, 1, 115, 5,
                    'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (362, 'Family Social Anlass', '2023-09-18 11:02:14.476301', '', '2023-09-17 22:00:00', 6, 1, 114, 5,
                    'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (109, '-3.2 Prozentpunkte gegenüber Vormonat', '2023-07-22 05:09:31.316639', '',
                    '2023-07-21 22:00:00', 50.2, 1, 44, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (78,
                    'Erste Messung im neuen Jahr. Erster RH Subs Deal bereits abgeschlossen (Renewal). Cililum Subs für Post in Aussicht (Offeriert, TCHF 250, 10%=25 k). ',
                    '2023-07-10 07:00:59.447002', '', '2023-07-09 22:00:00', 5000, 1, 51, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (107,
                    'Julian kommt nächste Woche dazu, die Änderungen vorzunehmen. Anschliessend versuchen wir mit einer Linkedin Kampagne, etwas mehr Abonennten zu genierieren. ',
                    '2023-07-18 15:04:28.896705', '', '2023-07-17 22:00:00', 0.5, 1, 113, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (100, '0', '2023-07-17 16:05:38.319316',
                    'Members am Mobility Meetup zur Mitwirkung aufgefordert und Verfügbarkeiten abgeklärt.',
                    '2023-07-16 22:00:00', 0, 1, 38, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (66, 'BBT Team noch nicht bei uns. Offizielle Zahl von Marcel', '2023-07-27 11:39:02.9258', '',
                    '2023-07-26 22:00:00', 17021, 1, 32, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (72, 'Eintritt Mirjam Thomet und kleine Pensumanpassungen.
    ', '2023-07-31 04:41:31.538874', '', '2023-07-08 22:00:00', 0.85, 1, 43, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (132,
                    'Angebot für MLOpsLab und MLOps Consulting in Erarbeitung. Für das MLOpsLab werden wir sofern wir Ressourcen von AWS benötigen ein Sandbox Funding beantragen. Dieses soll die Kosten für die AWS Ressourcen finanzieren.',
                    '2023-07-27 16:05:23.999901', '', '2023-07-26 22:00:00', 0.4, 1, 58, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (121, 'Da Jölu in den Ferien immer noch Stand wie beim letzten Checkin.',
                    '2023-07-27 11:14:26.078392', '', '2023-07-26 22:00:00', 0, 1, 38, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (126,
                    'Super Kandidat hat für ein 80% Pensum zugesagt (aber noch nicht unterschrieben). Simu hat gekündigt (60%) Pensum',
                    '2023-07-27 11:51:37.9145', '', '2023-07-26 22:00:00', 0.2, 1, 35, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (122, 'Da Jölu in den Ferien immer noch Stand wie beim letzten Checkin.',
                    '2023-07-27 11:15:06.108113', '', '2023-07-26 22:00:00', 0, 1, 57, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (116,
                    'Zweite Messung gegen Ende Juli. Cilium Subs Deal für Post ist gekommen. Deal Flughafen ZH in Ausssicht.',
                    '2023-07-24 12:04:06.940739', '', '2023-07-23 22:00:00', 28600, 1, 51, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (137,
                    'Strategie Draft wurde mit Stakeholdern diskutiert und nochmals von Simu und Berti überarbeitet',
                    '2023-07-31 07:02:50.571158', 'finalisierte Fassung erstellen', '2023-07-30 22:00:00', 0.3, 1, 65,
                    5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (135, 'DesignOps Worskhop durchgeführt und PainPoints aufgenommen', '2023-07-31 07:00:58.808657',
                    'konkrete Massnahmen im Team besprechen', '2023-07-30 22:00:00', 0.3, 1, 69, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (129, 'Erste Ideen für Massnahmen generiert und mit mga und sfe gechallenged ergänzt.',
                    '2023-07-27 11:20:08.214817',
                    'Massnahmen gilt es vertieft zu prüfen, zu priorisieren und auszuarbeiten', '2023-07-26 22:00:00',
                    0.2, 1, 119, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (275, 'Keine Veränderung seit letzter Messung', '2023-09-06 05:58:57.015258', '',
                    '2023-09-05 22:00:00', 0.6, 1, 119, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (65, 'Klar über Zielwert. Aber noch ohne BBT Team. Offizieller Wert von Marcel',
                    '2023-07-27 11:23:47.158342', '', '2023-07-26 22:00:00', 75, 1, 33, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (133, 'Keine Veränderung', '2023-07-31 04:37:55.882196', '', '2023-07-30 22:00:00', 1.4, 1, 45, 5,
                    'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (134, 'Festanstellung Paco und Florian, Austritt Aaron und Dominic, div. Pensumanpassungen',
                    '2023-07-31 04:40:22.296079', '', '2023-07-30 22:00:00', -0.35, 1, 43, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (120,
                    'Letztjährige Membersumfrage ausgewertet und Ansatzpunkte für Massnahmenpaket identifiziert. Bei Team Zoo neue Membersumfrage eingeführt',
                    '2023-07-27 11:11:09.869964', '', '2023-07-26 22:00:00', 0.2, 1, 36, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (177,
                    'Interview-Termine mit allen RTE stehen. Viedos werden wahrscheinlich in einem verpackt. --> Mehrere Erfolgsstories in einem verpackt',
                    '2023-08-16 10:45:18.61138', 'Termine mit Kontakten ausserhalb der SBB vereinbaren',
                    '2023-08-15 22:00:00', 0.25, 1, 37, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (230, 'Videokonzept ist bei der GL fürs Review und wird anschliessend publiziert',
                    '2023-08-25 07:30:23.407048', '', '2023-08-24 22:00:00', 1, 1, 110, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (160, 'Erste Messung sämtlicher Grundpauschalen Operations der Divisions (Monat Juni 2023).',
                    '2023-08-11 07:11:38.079814', '', '2023-08-10 22:00:00', 81481, 1, 50, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (161, 'RH Subs Deals bei Flughafen ZH und aity offeriert.', '2023-08-11 07:16:56.540463', '',
                    '2023-08-10 22:00:00', 37800, 1, 51, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (182, 'Mobility Event für September organisiert, Töggeliturnier initiert.',
                    '2023-08-16 16:00:01.473138', '', '2023-08-15 22:00:00', 0.2, 1, 57, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (172, 'Bisher noch keine Blogposts zu Kubermatic, ML Themen und Cilium. Video zu Dagger erschienen.',
                    '2023-08-14 07:14:25.016971', 'Auftrags-Reminder an Teams wurde versendet via Chat',
                    '2023-08-13 22:00:00', 0.1, 1, 49, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (162,
                    'Umfrage durchgeführt, Auswertung vorgenommen, die richtigen Schlüsse müssen noch gezogen werden. Kick-Off mit Marcom, Sales und weiteren Division in Planung. Prüfung Peerdom als Kunde für GCP Account. Nächster Touchpoint m. GCP Ende Sept. vorgesehen.',
                    '2023-08-17 11:27:31.031067',
                    'Da die Partnerschaft mit GCP wegen fehlender Zertifizierungen blockiert ist, müssen zeitnah die finanziellen und zeitl. Mittel für Members-Zertifizierungen geklärt werden.',
                    '2023-08-10 22:00:00', 0.1, 1, 48, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (150, 'Die weekly news machen gerade Sommerpause.', '2023-08-02 12:11:20.687644',
                    'Für das Züri Fübi & Montagsmaler nächsten Dienstag, habe ich Lukas Koller angefragt. Am #wecare  Schwumm nimmt unser Mats teil und eine Woche später ist das Aarebötle und der MarCom-Sales Teamevent, wo sich auch Gelegenheit bietet, etwas darüber zu erzählen. ',
                    '2023-08-01 22:00:00', 4, 1, 114, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (227,
                    'Alle Quartalsplanungen haben stattgefunden und die neuen Termine sind vereinbart fürs nächste Quartal',
                    '2023-08-25 07:25:09.260022', '', '2023-08-24 22:00:00', 1, 1, 116, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (171, 'Keine Veränderung, da keine Events', '2023-08-14 07:13:07.316482', '', '2023-08-13 22:00:00',
                    0.1, 1, 40, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (263, 'Offizielle Zahl von Marcel höher als selbsterrechnete.', '2023-09-06 06:00:31.018376',
                    'Keine', '2023-09-05 22:00:00', 14803, 1, 32, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (151, 'N/A', '2023-08-03 12:00:11.840016', 'Aktivitäten gemäss Key Results', '2023-08-02 22:00:00',
                    0, 1, 94, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (153, 'Dani Tschan und Mätthu Liechti in /dev/tre integriert', '2023-08-03 11:58:32.178331', 'N/A',
                    '2023-08-02 22:00:00', 0.3, 1, 92, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (178, 'Happiness Umfrage in allen /mobility Teams eingeführt. ', '2023-08-16 14:17:37.569162',
                    'Auswertung für die Baseline Bestimmung erfolgt Ende August und ist eingeplant.',
                    '2023-08-15 22:00:00', 0.25, 1, 36, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (155, 'N/A', '2023-08-03 12:00:48.489513', 'Aktivitäten gemäss Key Results', '2023-08-02 22:00:00',
                    0, 1, 87, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (154, 'N/A', '2023-08-03 11:59:43.210707', 'Aktivitäten gemäss Key Results', '2023-08-02 22:00:00',
                    0, 1, 89, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (152, 'N/A', '2023-08-03 12:00:22.388779', 'Aktivitäten gemäss Key Results', '2023-08-02 22:00:00',
                    0, 1, 86, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (156,
                    'Vertrag mit Simon Bühlmann abgeschlossen. Start als Software Engineer S4 zu 80% am 1. September 2023',
                    '2023-08-03 12:02:38.982369', 'Keine weiteren Aktivitäten', '2023-08-02 22:00:00', 1, 1, 96, 5,
                    'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (179,
                    'Admin Team beauftragt für die Berechnung der Baseline (pro Senioritätsstufe). Massnahmepaket konsolidiert',
                    '2023-08-16 14:19:57.439952', 'Einzelne Massnahmen werden noch weiter ausgearbeitet/geprüft.',
                    '2023-08-15 22:00:00', 0.25, 1, 119, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (163,
                    'kurzfristig nur 1.0 FTE, per nächsten Monat jedoch total 3 FTE (Anstellung Yelan und 2*0.5 freie Sys-Engineers in /zh)',
                    '2023-08-11 12:12:12.78986', '', '2023-08-10 22:00:00', 1, 1, 45, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (157, 'Wert Juni 2023 - + 3.9% im Vormonat, dennoch tiefer als Zielwert',
                    '2023-08-03 12:08:41.199292', 'N/A', '2023-08-02 22:00:00', 72.1, 1, 74, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (149, 'Eingeführt bei /sys, /ux, /mid, /mobility. Auswertung und Zielwerte noch nicht bekannt.',
                    '2023-07-31 11:29:04.558434', '', '2023-07-30 22:00:00', 0.2, 1, 41, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (256, 'Juli: 82%. Ausblick August: ähnlich wie Juli erwartet, dann ab Sept. tedentiell abnehmend',
                    '2023-09-17 22:51:48.612592', 'Sales, Sales, Sales...', '2023-08-27 22:00:00', 82, 1, 99, 5,
                    'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (184,
                    'Draft für Angebotstexte für die Website für MLOps Consulting und dem MLOps Lab sind erstellt. Validierung herausfordernd. Aktuell hat ein Gespräch mit der Mobi stattgefunden. Die sind aber schon sehr weit. Monitoring hat bei ihnen noch Potential.',
                    '2023-08-16 19:17:03.255698', '', '2023-08-15 22:00:00', 0.5, 1, 58, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (193, '71 Prozent', '2023-08-21 11:37:17.826866', '', '2023-08-20 22:00:00', 0.3, 1, 82, 5, 'metric',
                    NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (200,
                    'Haben bereits einige Interviews mit Personen im CI/CD Umfeld durchgeführt. Die Interviews sind dokumentiert und werden im September ausgewertet.',
                    '2023-08-24 07:17:49.905876', '', '2023-08-23 22:00:00', 0.5, 1, 76, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (192, '74 Prozent', '2023-08-21 11:36:47.945967', '', '2023-08-20 22:00:00', 0.7, 1, 81, 5, 'metric',
                    NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (186, 'Keine Änderung zum letzten Check-in. Spannende Bewerbungen offen.',
                    '2023-08-17 09:36:50.354004', '', '2023-08-16 22:00:00', 0.2, 1, 35, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (196, 'Task erledigt. ', '2023-08-23 10:00:01.257123',
                    'Allenfalls Linkedin Kampagne um neue Abonnent*innen zu gewinnen.', '2023-08-22 22:00:00', 1, 1,
                    113, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (197, 'Augustversand = 138 Empfänger*innen = -0.5%', '2023-08-23 10:03:32.160754',
                    'Straffe Adresskorrektur im Highrise. ', '2023-08-22 22:00:00', 1338, 1, 109, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (215,
                    'Prüfung Swiss Unihockey als Kunde für GCP angestossen. Interner Kick-Off mit Marcom+Sales und potentiell weiteren Divisions geplant (2. September).',
                    '2023-08-24 09:19:48.719295', '', '2023-08-23 22:00:00', 0.11, 1, 48, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (216, 'Stand 24.8.: 13 Anmeldungen', '2023-08-24 10:45:38.953258', '', '2023-08-23 22:00:00', 0, 1,
                    38, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (218, 'keine Veränderung', '2023-08-24 10:48:32.898036', '', '2023-08-23 22:00:00', 0.2, 1, 35, 5,
                    'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (213, 'Nächste Messung für Monat August wird im September folgen.', '2023-08-24 09:14:13.736541', '',
                    '2023-08-23 22:00:00', 81481, 1, 50, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (217,
                    'Zielwerte in Mobkernteam in Diskussion & Teamevent von Bandtis of the Latin Sea im Juli durchgeführt',
                    '2023-08-24 10:47:34.877917', '', '2023-08-23 22:00:00', 0.2, 1, 57, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (201, '+4.6 Prozentpunkte gegenüber Vormonat
    ', '2023-08-24 07:35:25.711195', '', '2023-08-23 22:00:00', 54.8, 1, 44, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (276, '13 Teilnehmende am 1. Mobility Day vom 31.8.', '2023-09-06 11:50:10.878996', '',
                    '2023-09-05 22:00:00', 0, 1, 38, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (202, 'kurz vor der Einführung', '2023-08-24 09:03:38.569888', '', '2023-08-23 22:00:00', 0.15, 1,
                    46, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (212,
                    'Deals von RhB, EveryWare und aity im August reingekommen. Leads: SHKB sowie erste Anfragen aus dem Rocky Linux Umfeld.',
                    '2023-08-24 09:11:50.799325', '', '2023-08-23 22:00:00', 66500, 1, 51, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (195, 'Öffnungsrate vom August NL 34.7%', '2023-08-23 09:59:14.336763',
                    'Sommerferien. Wir prüfen die Entwicklung im September. ', '2023-08-22 22:00:00', 35, 1, 108, 5,
                    'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (205,
                    'März Stundensätze pro Senioritätslevel sind ausgewertet. Können wir als Baseline so verwenden.',
                    '2023-08-24 07:59:48.585776',
                    '- Am 13.9. im mobcoeur weekly ist das KR traktandiert für Massnahmenpaket Bestimmung',
                    '2023-08-23 22:00:00', 0.5, 1, 119, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (203, 'Keine: Aber alles geplant, dass Target Zone erreicht werden kann',
                    '2023-08-24 07:51:30.423803', '- Task bei Jölu und Nathi zur Ablage ihrer Team Happiness
    - Termin für Massnahmenpaket definieren ist am 31.8.', '2023-08-23 22:00:00', 0.25, 1, 36, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (210,
                    'Freie Kapazität ist etwas gestiegen (PL Kapazität dazu gekommen). Ab September gibt es gegen 400% zusätzliche freie Kapazität bei unseren Members.',
                    '2023-08-24 09:03:08.346575', 'Etliche Leads und Anfragen werden derzeit geprüft.',
                    '2023-08-23 22:00:00', 1.5, 1, 45, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (209,
                    'Werte per 01.09.23: Festanstellung Simon Bühlmann, Tobias Stern, Daniel Alder, Yelan Tao, diverse Pensumsanpassungen, Austritt Simon Roth',
                    '2023-09-11 04:26:47.725138', '', '2023-08-23 22:00:00', 1.95, 1, 43, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (223,
                    'Meeting-Gefässe und -Zyklen /dev/tre überprüft, neu definiert und mit übergelagerten Firmen-Meetings und dem OKR-Prozess abgestimmt -> https://wiki.puzzle.ch/Puzzle/DevTreMeetings - Neue Termin-Serien erstellt',
                    '2023-08-24 19:39:19.425451', 'Meetings planen und durchführen', '2023-08-23 22:00:00', 0.3, 1, 86,
                    5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (259,
                    'Neuer Auftrag: BKS (sehr klein). Deals: Kapo GR, UZH Frontend Dev, Mobi Gluon, Peerdom DB, HRM DB Consulting, BJUB',
                    '2023-08-28 11:26:37.617476', 'Sales Prio bleibt hoch.', '2023-08-27 22:00:00', 0.3, 1, 70, 5,
                    'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (261,
                    'Aktuell ist nicht definiert, wie die Resultate aus den Happiness Umfragen der Divisions auf Stufe Unternehmen zusammengezogen und ausgewertet werden. Die Auswertung und Umsetzung erfolgt direkt auf Stufe Division.',
                    '2023-08-28 14:54:22.924565', 'Überprüfung des KR für die nächste OKR Iteration.',
                    '2023-08-27 22:00:00', 0.15, 1, 42, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (247, 'Dagger Video, Cilium Blogpost, ML Ops Video', '2023-08-28 08:37:14.263633', '',
                    '2023-08-27 22:00:00', 0.25, 1, 49, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (246, 'Aareböötle Post erschienen, Family Event hat stattgefunden', '2023-08-28 09:09:22.01945', '',
                    '2023-08-27 22:00:00', 0.7, 1, 40, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (228, 'Lunch Explorer Zürich auf LinkedIn', '2023-08-25 07:26:31.008175', '', '2023-08-24 22:00:00',
                    5, 1, 114, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (225, 'MLOps-Video', '2023-08-25 07:22:22.036945', '', '2023-08-24 22:00:00', 2, 1, 118, 5, 'metric',
                    NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (226, 'Das Board ist lanciert und kommuniziert', '2023-08-25 07:23:05.118068', '',
                    '2023-08-24 22:00:00', 1, 1, 117, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (286, 'Verbesserung Mobi- und Swisscom-Controlling', '2023-09-07 12:47:50.428141',
                    'Aktivitäten gemäss Key Results ', '2023-09-06 22:00:00', 0.4, 1, 106, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (265, 'Massnahmen konsolidiert, Baseline bestimmt. Muss im MobCeur noch abgenommen werden.',
                    '2023-08-31 12:47:32.562431',
                    'Zielwerte sind schwierig zu definieren. Braucht nochmals einen Effort.', '2023-08-30 22:00:00',
                    0.6, 1, 119, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (262, 'Baseline berechnet. Vorschlag für Zielwerte und Massnahmenpaket erstellt',
                    '2023-08-30 09:19:20.635794', '', '2023-08-29 22:00:00', 0.6, 1, 36, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (242,
                    'Draft "Arbeit im Team " und "Ambitionen" erstellt (https://wiki.puzzle.ch/Puzzle/DevTre), Arbeit an Moser-Baer-Blog-Artikel aufgegeist',
                    '2023-08-26 13:51:15.707019', 'WIKI-Doku über /dev/tre mit Team verifizieren und präszisieren
    Moser-Baer-Blog-Artikel ferstigstellen und publizieren', '2023-08-25 22:00:00', 0.3, 1, 89, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (240, 'Simon Bühlmann: Integration in SIARD: Wartung und Support, SAP HANA. Allefalls Mobi-Gaia Grundhaltung: Java-Projekte
    Chrigu Lüthi: Einsatz von Oktober bis Ende Jahr im Mobi-Gaia-Team-Auftrag. Mit Chrigu und Mobi abgesprochen.',
                    '2023-08-26 13:44:24.994595', 'Intetragion und Einführungsprogramm für Clara erstellen. SWOA eine Idee, aber Clara möchte zum Start eher Back- statt Frontend (Java)

    Beri: Arbeitsversuch am Laufen. Vorzu werden kleine Fortschritte erziehlt. Integration in Aufträge, noch offen.',
                    '2023-08-25 22:00:00', 0.7, 1, 92, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (241, 'Bisher keine Zeit für Arbeit an diesem Key Result', '2023-08-26 13:45:49.802595',
                    'Aktivitäten gemäss Key Results ', '2023-08-25 22:00:00', 0, 1, 94, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (244, 'Swisscom cADC erstellt, kommuniziert und 2x durchgeführt
    Mobiliar Gaia erstellt, kommuniziert und 2x durchgeführt', '2023-08-26 13:55:37.115452',
                    'Aktivitäten gemäss Key Results ', '2023-08-25 22:00:00', 0.3, 1, 106, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (243, 'Bisher keine Zeit für Arbeit an diesem Key Result', '2023-08-26 13:53:19.710419',
                    'Aktivitäten gemäss Key Results ', '2023-08-25 22:00:00', 0, 1, 87, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (234, 'Noch keine klarere Formulierung / definition - Strategie ist abgestimmt',
                    '2023-08-25 08:28:19.149925', 'Strategie konkretisieren (Draft SIH)', '2023-08-24 22:00:00', 0.3, 1,
                    65, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (224,
                    'Interne Version ist finalisiert und Rückmeldungen eingearbeitet. Erste Texte für externe Kommunikation sind vorhanden.',
                    '2023-08-25 06:05:25.976562',
                    'Finalisieren Texte und Bilder für öffentliche Kommunikation. Vorstellen am Weekly, start Sales Kampagne.',
                    '2023-08-24 22:00:00', 0.2, 1, 47, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (233,
                    'Fokussierung fehlt weiterhin - wir haben aber mittlerweile ein entsprechendes Projektbudget für das aufgleisen',
                    '2023-08-25 08:27:26.415947', 'Priorisieren findings vom Workshop und Ansatz definieren',
                    '2023-08-24 22:00:00', 0.3, 1, 69, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (304,
                    'Meinung von 6/7 RTEs ist bekannt. Zwei Linkedin-Beiträge sind für September geplant. Austausch mit Mäge geplant',
                    '2023-09-11 08:51:13.220694', '', '2023-09-10 22:00:00', 0.6, 1, 37, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (352, '2. Durchführung am Techworkshop durch Iwan erfolgt (Rebecca krank)',
                    '2023-09-18 08:15:14.050639', '', '2023-09-17 22:00:00', 0.5, 1, 59, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (309, 'keine Veränderung seit letztem Checkin', '2023-09-11 09:51:52.819603', '',
                    '2023-09-10 22:00:00', 2, 1, 71, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (363, 'Öffnungsrate vom NL August', '2023-09-18 11:03:32.544804', '', '2023-09-17 22:00:00', 36.1, 1,
                    108, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (290, 'Neue Messung für Monat August. Es wurden noch Aufträge angepasst/neu integriert.',
                    '2023-09-08 08:40:27.756837', '', '2023-09-07 22:00:00', 99466, 1, 50, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (364, 'NL-Abonennt*innen Stand 18.9. / Für Oktober SoMe-Kampagne geplant',
                    '2023-09-18 11:05:06.893316', '', '2023-09-17 22:00:00', 1338, 1, 109, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (289,
                    'Kick-Off zur Marktopportunität mit Marcom&Sales am 4. September durchgeführt. Klärung der Pendenzen und des weiteren Vorgehens. Google Billing ID von Peerdom an Google weitergeleitet zur Prüfung. Teilnahme an AWS Cloud Day vom 26. September sfa/mga.',
                    '2023-09-08 07:37:15.018857', '', '2023-09-07 22:00:00', 0.15, 1, 48, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (303,
                    'Mischwert: Wir haben bei Dagger und Cilium das Wissen bereits über mehrere Members verteilt. Ebenfalls bei ML Ops. Allerdings fehlt eine Komm zu Kubermatic',
                    '2023-09-11 06:49:06.903171', '', '2023-09-10 22:00:00', 0.5, 1, 49, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (288, 'Keine Veränderung. Das KR wird nicht mehr aktiv weiterverfolgt.',
                    '2023-09-08 07:34:06.978881', '', '2023-09-07 22:00:00', 0.15, 1, 42, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (279, 'N/A', '2023-09-07 09:52:54.567623', 'Aktivitäten gemäss Key Results', '2023-09-06 22:00:00',
                    0, 1, 94, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (281, 'N/A', '2023-09-07 09:55:09.234594',
                    'Aktivitäten gemäss Key Results. Wird sich gemäss neu definiertem Zyklus und den Terminen ins nächste Quartal verschieben',
                    '2023-09-06 22:00:00', 0.3, 1, 86, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (305, 'keine Veränderung', '2023-09-11 09:37:29.557502', 'siehe checkin vom 25.08.23',
                    '2023-09-10 22:00:00', 0.3, 1, 69, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (280, 'N/A', '2023-09-07 09:53:45.210247', 'Aktivitäten gemäss Key Results', '2023-09-06 22:00:00',
                    0.7, 1, 92, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (291, 'Freie Kapa durch Neuanstellungen massiv gestiegen.', '2023-09-11 04:22:33.202399',
                    'Wir starten einen Java-Sales-Sprint.', '2023-09-10 22:00:00', 3.7, 1, 45, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (292, 'Eintritt per 18.9.23 Clara Llorente Lemm', '2023-09-11 04:26:59.384089', '',
                    '2023-09-10 22:00:00', 2.95, 1, 43, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (283, 'N/A', '2023-09-07 12:37:15.078339', 'Der Moser-Baer-Blogartikel wurde leicht angepasst. Er wird mit technischer Ausrichtung als erster von etwa 5 Blog-Artikeln erscheinen und ist in Arbeit.
    Ausserhalb der OKRs wurde eine Referenzstory für BKD erstellt.
    Wie wir arbeiten -> "Arbeit im Team" und Dienstleistungsangebot -> "Ambitionen" sind skitzziert im Wiki dokumentiert: https://wiki.puzzle.ch/Puzzle/DevTre',
                    '2023-09-06 22:00:00', 0.5, 1, 89, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (282, 'Blogartikel ist in Arbeit und adaptiert. Neu teschnisch ausgerichet & erster von Artieklserie. Ausserhalb der OKRs wurde eine Referenzstory für BKD erstellt.  Arbeit im Team, Ambitionen sind skizziert: https://wiki.puzzle.ch/Puzzle/DevTre
    ', '2023-09-07 12:40:50.714529', 'Abschluss Blog-Artikel', '2023-09-06 22:00:00', 0.5, 1, 89, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (293, 'Kein Update, wir sind an der Finalisierung der Texte und Bilder.',
                    '2023-09-11 04:28:13.277612', '', '2023-09-10 22:00:00', 0.2, 1, 47, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (294, 'Kein Update', '2023-09-11 05:31:01.598835', '', '2023-09-10 22:00:00', 0.7, 1, 41, 5,
                    'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (335,
                    'Junior/Senior Pairing im Gluon2-Auftrag für Yelan erreicht (formelle Bestellung zwar noch aussthend). Verrechenbarkeit um die 75% für die nächsten Wochen erwartet. Intakte Chancen auf Zuschlag bei UZH (dann gegen 100% verrechenbar)',
                    '2023-09-14 08:38:06.315176',
                    'Vertrag mit Mobi erhalten und UZH Dev-Mandat gewinnen (ist nicht mehr in unserer Hand, dies zu erreichen)',
                    '2023-09-13 22:00:00', 0.8, 1, 83, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (332,
                    'Ergänzung des SNB-Teams durch CK aufgegleist (Kompensation TD). Hier ab 2024 Aussicht auf leicht höheres Volumen. Pensum bei FZAG kann bis Ende Jahr knapp gehalten werden (Fokus von TD), danach Ablösung von TD als Challenge.',
                    '2023-09-14 08:24:25.946787',
                    'Bereits im Herbst Planungssicherheit bei FZAG und SNB für 2024 anstreben.', '2023-09-13 22:00:00',
                    0.8, 1, 80, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (320, 'Überarbeitung des Labs nach dem Feedback aus dem Testrun', '2023-09-12 06:42:47.159152', '',
                    '2023-09-11 22:00:00', 0.5, 1, 59, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (333, 'Status quo (siehe Checkin vom 24.8.).', '2023-09-14 08:27:42.975607',
                    'Recruitingbemühungen im Sys Engineering werden aufgrund aktueller Nachfrage etwas reduziert, jedoch nicht eingestellt.',
                    '2023-09-13 22:00:00', -50, 1, 77, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (347,
                    '4 Zusammenhaltsfördernde Aktivitäten sind durchgeführt und werden bis am 22.9. im Bandits of the Latin Sea Team ausgewertet.',
                    '2023-09-15 09:43:20.349614', '', '2023-09-14 22:00:00', 0.7, 1, 57, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (345, 'Divisionsrentabilität wurde eingeführt und den Divisions vorgestellt',
                    '2023-09-15 08:39:55.94165', '', '2023-09-14 22:00:00', 0.3, 1, 46, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (350,
                    'Es ist uns nicht bei allen Technologien gelungen unsere Kompetenz direkt sichtbar zu machen (Kubermatic fehlt). Für Cilium und Dagger wurde aber klar mehr als nur das Minimalziel erreicht. Auch im Thema ML Ops sind wir unterwegs.',
                    '2023-09-18 05:35:53.68167', '', '2023-09-17 22:00:00', 0.5, 1, 49, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (351,
                    'Es wurde klar aktiver über die Puzzle internen Events kommuniziert und die Sichtbarkeit erhöht. Das KR wurde klar übertroffen (Stretch)',
                    '2023-09-18 05:37:35.629369', '', '2023-09-17 22:00:00', 0.9, 1, 40, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (327, 'keine Änderung', '2023-09-13 06:15:44.825369', '', '2023-09-12 22:00:00', 0, 1, 38, 5,
                    'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (321, 'Alles vorbereitet für Verabschiedung im Team', '2023-09-12 13:42:04.268342', '',
                    '2023-09-11 22:00:00', 0.69, 1, 36, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (322, 'Massnahmepaket ist definiert. Aber Baseline kann Senioritätslevel nicht sinnvoll abbilden',
                    '2023-09-12 14:44:09.000869', '', '2023-09-11 22:00:00', 0.4, 1, 119, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (331,
                    'Kommunikation (interner Newsbeitrag, extern Blogpost mit Video und eigene Landingpage) folgen anfangs Oktober, daher Commit Ziel knapp nicht erreicht.',
                    '2023-09-14 04:16:25.647593', '', '2023-09-13 22:00:00', 0.25, 1, 47, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (371,
                    'Der Blog + das Video werden noch im September veröffentlicht. Termin mit Mäge steht diese Woche an für den Austausch. Alle RTEs wurden befragt. Pippo und Jölu haben ihre Kunden befragt.',
                    '2023-09-18 11:59:24.678055', '', '2023-09-17 22:00:00', 1, 1, 37, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (382,
                    'Zusammenarbeit im MO Hyperscaler Team etabliert, Meeting zum Stand am 18.09.23, Terminreihe für zukünftige Check-ins, Kick-Off mit GCP in Vorbereitung, Klärung Rahmenbedingungen Members Zertifizierung und weitere Themen werden angegangen.',
                    '2023-09-18 15:31:34.411918', '', '2023-09-17 22:00:00', 0.2, 1, 48, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (381, 'Deal von Uni Bern im September. Weitere Deals offen. Zusätzlich zu letztem Checkin: MediData',
                    '2023-09-18 15:24:59.511698', '', '2023-09-17 22:00:00', 78500, 1, 51, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (379, '-6.1 Prozentpunkte gegenüber Vormonat', '2023-09-18 15:00:52.44142', '',
                    '2023-09-17 22:00:00', 48.7, 1, 44, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (348, 'Martin hat Vertrag unterschrieben', '2023-09-18 13:17:57.096117', '', '2023-09-17 22:00:00',
                    1, 1, 35, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (385, 'Keine Veränderung. Das KR wird nicht mehr aktiv weiterverfolgt.',
                    '2023-09-18 16:23:46.281168', '', '2023-09-17 22:00:00', 0.15, 1, 42, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (346, 'Für den 2. Mobility Tag vom 18.9 waren es wieder 13 Member', '2023-09-18 13:15:51.455503',
                    'Zielwerte wurden festgelegt, bevor die Verfügbarkeit der Members an den einzelnen Tagen bekannt war. Aufgrund der Verfügbarkeit hätte im als bestmöglichen Wert nur Commit Zone erreicht werden können.',
                    '2023-09-17 22:00:00', 0.2, 1, 38, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (384, 'Keine Veränderung gegenüber letzter Messung.', '2023-09-18 15:49:35.966723', '',
                    '2023-09-17 22:00:00', 99466, 1, 50, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (376, 'Vorgehen für Findung bzgl. "Technologischer Ausrichtung" im Teammeeting besprochen',
                    '2023-09-18 13:52:57.086474', 'Aktivitäten gemäss Key Results ', '2023-09-17 22:00:00', 0.2, 1, 87,
                    5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (323, 'Nicht offizielle Zahl von Marcel. Selbst aus PTime errechnet', '2023-09-18 13:10:13.461295',
                    '', '2023-09-17 22:00:00', 16240, 1, 32, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (380,
                    'Weitere Eintritte von Members vor Ende September sind keine mehr zu verzeichnen. Nach dem 1.10. sind es fünf Members, die starten. (2x /mid/container, 2x /mobility, 1x /dev/tre).',
                    '2023-09-18 15:08:36.56011', '', '2023-09-17 22:00:00', 2.95, 1, 43, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (324,
                    'js und pe bei Lehrlingen, jbr ganzer Monat in Ferien, ii und rhi für Marktopportunität. Nicht offizielle Zahl Marcel. Aus PTime gezogen',
                    '2023-09-18 13:11:19.576769', '', '2023-09-17 22:00:00', 71, 1, 33, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (377,
                    'Mobi- & Swisscom: Weitere Monatsrapporte erstellt, Controlling verbessert, Swisscom nur intern (will nicht für den Rapport bezahlen)',
                    '2023-09-18 13:56:34.391499', 'Massnahmen gemäss Key Results', '2023-09-17 22:00:00', 0.6, 1, 106,
                    5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (329, 'Massahmenpaket, Baseline, Zielwert definiert und abgenommen', '2023-09-18 13:01:28.018679',
                    '', '2023-09-17 22:00:00', 0.85, 1, 36, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (330, 'Baseline und Massnahmenpaket sind definiert und abgenommen', '2023-09-18 13:08:04.903086', '',
                    '2023-09-17 22:00:00', 0.85, 1, 119, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (378,
                    'Die freie Kapazität bei unseren Members ist im Vergleich zur letzten Messung nochmals etwas gestiegen (um 0.5 FTE).',
                    '2023-09-18 15:00:45.220682', '', '2023-09-17 22:00:00', 4.3, 1, 45, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (386, 'keine Veränderung seit 25.08.23', '2023-09-19 06:28:53.846642',
                    'werden wir im nächsten Quartal weiterverfolgen', '2023-09-18 22:00:00', 0.3, 1, 69, 5, 'metric',
                    NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (328, 'Erkenntnisse ausgewertet. Total mit 6 Kunden/Partner gesprochen', '2023-09-13 10:08:20.72237',
                    '', '2023-09-12 22:00:00', 0.7, 1, 76, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (343,
                    'Learnings: Nicht relevante Infos rausfiltern. Relevante Infos direkter und transparenter weiter geben. Die Members mitbestimmen lassen, welche Infos als relevante oder nicht relevant klassifiziert werden. Mitarbeit and Zielen motiviert.',
                    '2023-09-14 12:45:30.838305', '', '2023-09-13 22:00:00', 1, 1, 85, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (400, 'N/A', '2023-09-23 15:45:19.858172', 'Dito letzter Check-In', '2023-09-22 22:00:00', 0.6, 1,
                    106, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (401, 'Wert August 2023 -13.9% tiefer als im Vormonat - Durschntt Zyklus: 75.9',
                    '2023-09-23 15:52:10.948581', '', '2023-09-22 22:00:00', 70.9, 1, 74, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (394, 'N/A', '2023-09-23 15:37:56.241782', 'Dito letzter Check-In', '2023-09-22 22:00:00', 0.85, 1,
                    92, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (396, 'N/A', '2023-09-23 15:39:09.617811', 'Dito letzter Check-In', '2023-09-22 22:00:00', 0.6, 1,
                    86, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (398, 'N/A', '2023-09-23 15:40:56.73687', 'Dito letzter Check-In', '2023-09-22 22:00:00', 0.2, 1, 87,
                    5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (399, 'N/A', '2023-09-23 15:44:24.35659', 'N/A', '2023-09-22 22:00:00', 1, 1, 96, 5, 'metric', NULL,
                    1);
            INSERT INTO okr_pitc.check_in
            VALUES (395, 'N/A', '2023-09-23 15:38:37.24614', 'Dito letzter Check-In', '2023-09-22 22:00:00', 0, 1, 94,
                    5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2044,
                    '03.06.2024: Die Aufteilung ist soweit geklärt. Nathalie 20% Members Coaching und LST, 10% Innoprozess, 40% verrechenbar PL / SM',
                    '2024-06-03 12:46:13.276304', '', '2024-06-03 12:46:13.276312', NULL, 1, 1142, 4, 'ordinal',
                    'STRETCH', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2237, 'SHKB Unterschrieben', '2024-07-09 07:59:24.592818', '', '2024-07-09 07:59:24.592834', NULL,
                    1, 1329, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2131, 'keine Veränderung (Warten immer noch auf Bestellung Swiss Life)',
                    '2024-06-17 11:13:06.399309', '', '2024-06-17 11:13:06.399311', 96096, 1, 1191, 0, 'metric', NULL,
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (219,
                    'Bestätigung des weiteren Vorgehens von beiden Kunden erhalten. Gute Lösung gefunden, jedoch werden die reduzierten Pensen zumindest in den nächsten Monaten noch nicht kompensiert werden können.',
                    '2023-08-24 13:08:42.224997', '', '2023-08-23 22:00:00', 0.6, 1, 80, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1973,
                    'Das Ziel hat sich nun etwas geändert und haben ein Kostendacherhöhung erhalten. Diverse Massnahmen sind eingeleitet',
                    '2024-05-27 06:42:46.828046', '', '2024-05-27 06:42:46.82805', NULL, 1, 1156, 4, 'ordinal',
                    'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2267,
                    'Simone Götz hat letzten Dienstag den Vertrag unterschrieben und wird ab Oktober bei WAC arbeiten. Somit sind beide Abgänge ersetzt',
                    '2024-07-15 08:09:02.964815', '', '2024-07-15 08:09:02.964818', NULL, 1, 1322, 5, 'ordinal',
                    'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2269, 'noch keine Veränderung, Plan erstellt', '2024-07-15 08:10:00.625468', '',
                    '2024-07-15 08:10:00.625471', 0, 1, 1314, 5, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (344,
                    'Chrigu Schlatter hat die BAFU Pipeline in Dagger umgeschrieben (PoC). Learnings werden voraussichtlich im 3. Teil der Dagger-Blogbeitragsserie dargestellt.',
                    '2023-09-14 13:33:48.849038', '', '2023-09-13 22:00:00', 0.9, 1, 76, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1982, 'Tim war leider oft besetzt, weshalb sich hier nicht mehr viel bewegt hat.',
                    '2024-05-27 07:20:50.097011', '', '2024-05-27 07:20:50.097014', NULL, 1, 1187, 5, 'ordinal',
                    'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (143, 'aufgrund der Veränderung im Modus unseres Team Meetings, ist noch nichts passiert',
                    '2023-07-31 07:08:17.518371', 'Happiness Umfrage im neuen Modus etablieren', '2023-07-30 22:00:00',
                    0, 1, 68, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1804, 'Mobi-Mandat für Mayra hat sich leider nicht erfüllt - offerten sind weiterhin offen. ',
                    '2024-04-29 10:40:06.262937', '', '2024-04-29 10:40:06.262945', NULL, 1, 1206, 5, 'ordinal',
                    'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1805, 'Keine Veränderungen seit letztem Checkin', '2024-04-29 10:40:23.827253', '',
                    '2024-04-29 10:40:23.827259', NULL, 1, 1208, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2282, 'Interview 17.6 mit neuem Kandidaten', '2024-07-16 06:45:24.310831', '',
                    '2024-07-16 06:45:24.310833', -0.9, 1, 1259, 3, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2193, 'Der Prompt wird im Juli kreiert (nach Go-Live der neuen Website).',
                    '2024-07-01 04:24:51.954512', '', '2024-07-01 04:24:51.954516', NULL, 1, 1279, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1993, 'keine Veränderung', '2024-05-27 07:51:12.729168', '', '2024-05-27 07:51:12.729172', NULL, 1,
                    1220, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (314, 'SBVE hatten viele Nachbesserungen die nicht verrechent werden können',
                    '2023-09-14 11:01:01.558731', '', '2023-09-10 22:00:00', 90.8, 1, 121, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2003, 'keine Updates', '2024-05-27 10:03:50.471616', '', '2024-05-27 10:03:50.47162', NULL, 1, 1229,
                    6, 'ordinal', 'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1876, 'Im nächsten Team-Meeting Ideensammlung', '2024-05-08 17:39:46.179669', '',
                    '2024-05-08 17:39:46.179676', NULL, 1, 1222, 8, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1878, 'Wir warten immer noch auf Romeo.
    Medidata in Aussicht (3rd Level Plus).', '2024-05-08 17:41:08.824613', '', '2024-05-08 17:41:08.824621', NULL, 1, 1217,
                    5, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1877, 'Unverändert.', '2024-05-08 17:40:19.498087', '', '2024-05-08 17:40:19.498113', NULL, 1, 1216,
                    10, 'ordinal', 'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2004, 'Unverändert (Momentan keine Prio beim Mid-Lead)', '2024-05-27 10:04:34.137002', '',
                    '2024-05-27 10:04:34.137008', NULL, 1, 1211, 0, 'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2133,
                    'Actionplan mit Sum Buddy besprochen, am LST Monthly vom 24. Juni wird es dem LST Team vorgestellt',
                    '2024-06-17 11:29:13.877015', '', '2024-06-17 15:29:28.407631', NULL, 1, 1208, 0, 'ordinal',
                    'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2007, 'Struktur wird diese Woche abgeschlossen. erste Texte sind im Wordpress',
                    '2024-05-28 06:52:22.235734', '', '2024-05-28 06:52:22.235765', 8, 1, 1180, 7, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1992, 'Feedback von GL erhalten, nächste Woch werden wir es mit dem Team besprechen',
                    '2024-05-27 07:50:18.454862', '', '2024-05-27 07:50:18.454865', NULL, 1, 1218, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (2134, '', '2024-06-17 12:55:34.454306', '', '2024-06-17 15:30:11.695647', 1097, 1, 1212, 0,
                    'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1730, 'Vorbereitungen für die Aufräumaktion sind für diese Woche eingeplant. ',
                    '2024-04-23 06:27:18.98795', '', '2024-04-24 08:06:12.940862', NULL, 1, 1179, 8, 'ordinal', 'FAIL',
                    1);
            INSERT INTO okr_pitc.check_in
            VALUES (2122,
                    'Im Services&Solutions Part sind drei von fünf Themen abgebildet (und in Vernehmlassung). Der Rest wird momentan er-/überarbeitet. Ausser der Jobs-Seite sind die Texte überall enthalten und warten auf den Feinschliff.',
                    '2024-06-14 08:06:52.182021',
                    'Die Texte sind soweit möglich eingepflegt. Es gibt noch div. technische und strukturelle Tasks und ein Brocken Schreibarbeit - aber wir haben ja noch Zeit. ',
                    '2024-06-14 08:06:52.182023', 70, 1, 1180, 7, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2121,
                    'Im Services&Solutions Part sind drei von fünf Themen abgebildet (und in Vernehmlassung). Der Rest wird momentan er-/überarbeitet. Ausser der Jobs-Seite sind die Texte überall enthalten und warten auf den Feinschliff.',
                    '2024-06-14 08:06:52.16385',
                    'Die Texte sind soweit möglich eingepflegt. Es gibt noch div. technische und strukturelle Tasks und ein Brocken Schreibarbeit - aber wir haben ja noch Zeit. ',
                    '2024-06-14 08:06:52.163852', 70, 1, 1180, 7, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2120,
                    'Alle Bilder der bestehenden Seiten sind importiert und zugewiesen, die Visuals sind entsprechend den Seiten auch platziert. Bei einigen Lottie Animationen haben wir noch Darstellungsprobleme, diese werden asap behoben.',
                    '2024-06-14 08:01:25.765682',
                    'Nach Start Jup und Erhalt Föteli Puzzle Bern können die restlichen Fotos eingefügt werden. ',
                    '2024-06-14 08:01:25.765684', 80, 1, 1200, 9, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1734, 'ISO 9001 weisst doch einige Lücken auf die einen effizienten Fortschritt hindern....

    Die Hauptprozesse  Infrastruktur wurde mit den Zuständigen besprochen und werden erarbeitet ',
                    '2024-04-23 09:03:52.444103',
                    '- Zusätzliche Unterstützung durch Nicole für die Aufarbeitung der relevanten GEsetzte für Puzzle ',
                    '2024-04-23 09:03:52.44412', NULL, 1, 1154, 6, 'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2006,
                    'Die Migration der Blogpost sollte diese Woche abgeschlossen sein, dann legen wir mit dem SEO Thema los.',
                    '2024-05-28 06:51:33.02133', '', '2024-05-28 06:51:33.021356', NULL, 1, 1179, 8, 'ordinal', 'FAIL',
                    1);
            INSERT INTO okr_pitc.check_in
            VALUES (1672, 'wir sind auf einem guten Weg', '2024-04-11 13:37:29.314764',
                    'noch weitere Fokustage reservieren', '2024-04-23 09:01:25.845548', NULL, 1, 1154, 6, 'ordinal',
                    'FAIL', 2);
            INSERT INTO okr_pitc.check_in
            VALUES (140,
                    'Definitin WAC Teamkultur ist erfolgt und eine Massnahme wurde definiert und umgesetzt. Eine weitere wurde geplant',
                    '2023-07-31 07:05:49.890689', 'zweite Massnahme umsetzen', '2023-07-30 22:00:00', 0.7, 1, 67, 5,
                    'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (318, 'noch keines geplant ', '2023-09-11 13:08:32.159938', '', '2023-09-10 22:00:00', 0, 1, 123, 5,
                    'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (312, 'Von Budget von CHF 111 800 wurde bereits 78710 benutzt ', '2023-09-11 12:30:58.405421', '',
                    '2023-09-10 22:00:00', 78710, 1, 122, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1875, 'Backlogs wurden aufgeräumt, OV Backlog ist quasi leer.', '2024-05-08 17:37:34.533088', '',
                    '2024-05-08 17:37:34.533095', NULL, 1, 1209, 5, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2312, 'Weiter Status Quo, beinahe alle Subsiten sind nun erfasst und live.',
                    '2024-07-23 07:00:17.603397', '', '2024-07-23 07:00:17.603403', NULL, 1, 1281, 9, 'ordinal',
                    'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1873, 'Unverändert, in Arbeit', '2024-05-08 17:34:21.898721', '', '2024-05-08 17:34:21.898732',
                    NULL, 1, 1211, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2059, 'Nächste Woche Sponsoringauftritt KCD', '2024-06-04 07:24:25.957445', '',
                    '2024-06-04 07:24:25.957451', NULL, 1, 1193, 8, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2067, 'internes Audit war sehr zufriedenstellend, nur noch kleinere Anpassunge',
                    '2024-06-07 11:38:55.715456', '', '2024-06-07 11:38:55.715461', NULL, 1, 1154, 8, 'ordinal',
                    'STRETCH', 2);
            INSERT INTO okr_pitc.check_in
            VALUES (2071, 'keine Veränderung seit letztem Checkin', '2024-06-10 08:08:45.727186', 'Swiss Life Bestätigung einholen und planen
    Kt. Aargau follow up
    WTO Kt. Zug follow up
    WTO UVEK teilnehmen
    BAFU UX mit Oli ', '2024-06-10 08:08:45.727191', 96098, 1, 1191, 2, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1620, 'SoMe Plan erstellt. Fangen erst diese Woche an', '2024-04-02 06:42:07.976873', '',
                    '2024-04-02 06:42:07.976878', 0, 1, 1210, 5, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2194, 'Diese Woche werden alle Meta-Tags für das Go-Live der Website überprüft.',
                    '2024-07-01 04:26:25.028758', '', '2024-07-01 04:26:25.028762', NULL, 1, 1281, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (2063, 'wir sind noch nicht weiter', '2024-06-07 11:36:39.168141', '', '2024-06-07 11:36:39.168146',
                    NULL, 1, 1150, 2, 'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2240, 'noch keine Resultate', '2024-07-09 08:55:40.911738', '', '2024-07-09 08:55:40.911741', NULL,
                    1, 1346, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1823,
                    'Schulungstermin am 13. Mai für sje & jh. Anschliessend Teameinführung und Start mit bestehendem & freigegebenem Content',
                    '2024-05-01 13:45:08.419793', '', '2024-05-01 13:45:08.419813', 5, 1, 1180, 7, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1983, 'nächstes Relase ist in Planung ', '2024-05-27 07:46:56.77612', '',
                    '2024-05-27 07:46:56.776124', 0.3, 1, 1151, 4, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2248,
                    'Diesmal hat es super funktioniert und zum ersten mal ohne Bug seit 2 Jarhren, trotz den vielen Anpassungen durch den SAC,',
                    '2024-07-10 10:05:50.352918', 'Team loben :-) ', '2024-07-10 10:05:50.352922', 1, 1, 1271, 5,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1927, 'Unverändert (Momentan keine Prio beim Mid-Lead)', '2024-05-21 11:18:57.382291', '',
                    '2024-05-21 11:19:05.782297', NULL, 1, 1211, 0, 'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2204, '', '2024-07-02 08:34:35.186389', '', '2024-07-02 08:34:35.186394', 0, 1, 1312, 5, 'metric',
                    NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2206, 'Wir sind dran - Roadmap Meeting mit Yup, Mark und Andreas am 9. Juli angesetzt',
                    '2024-07-02 08:36:39.311004', '', '2024-07-02 08:36:39.311008', NULL, 1, 1324, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1667, 'viel zu hoch mit 104.92 Stunden', '2024-04-11 13:33:29.893202', 'Retro mit tel und Matthias',
                    '2024-04-11 13:33:29.893207', NULL, 1, 1149, 3, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1622, 'Termin für Workshop erstellt', '2024-04-02 06:44:38.066401', '',
                    '2024-04-02 06:44:38.066405', NULL, 1, 1215, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (315, 'Wartung und indirekter Aufwand war tief', '2023-09-11 12:39:17.831887', '',
                    '2023-09-10 22:00:00', 70.3, 1, 125, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2271, '', '2024-07-15 08:11:19.964563', '', '2024-07-15 08:11:19.964567', NULL, 1, 1316, 5,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2246, 'Ende August ist Stichtag für Inkasso, sidn aber nun auf einem guten Weg ',
                    '2024-07-10 08:23:01.940198', '', '2024-07-10 08:23:01.940201', NULL, 1, 1273, 7, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1676, 'neue Anfrage für den Visualisierungsworkshop', '2024-04-15 08:13:38.65907', '',
                    '2024-04-15 08:13:38.659074', NULL, 1, 1206, 5, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1621, '', '2024-04-02 06:43:51.067297', '', '2024-04-02 06:43:51.067302', 66, 1, 1214, 5, 'metric',
                    NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1747, 'Zwei Personen auf der Shortlist', '2024-04-23 16:31:31.645004', '',
                    '2024-04-23 16:31:31.645012', NULL, 1, 1216, 5, 'ordinal', 'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (316, 'Inhalt ist bekannt (?)', '2023-09-11 13:07:33.942933', '', '2023-09-10 22:00:00', 1, 1, 127,
                    5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2002, '', '2024-05-27 09:55:56.576042', '', '2024-05-27 11:22:37.259697', 918773, 1, 1230, 7,
                    'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2129, '', '2024-06-17 11:09:07.462709', '', '2024-06-17 11:09:07.462711', 157, 1, 1214, 10,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (189, 'Elternzugang mussten wir einiges nachbessern', '2023-08-21 10:54:23.969957', '',
                    '2023-07-16 22:00:00', 84, 1, 121, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1732, '- Mayra hat den ersten Entwurf für die Animationen gemacht, warten auf V2. Jürgen wird anschliessend die Animationen übernehmen
    - Briefing für Fotografin erfolgt in der KW12', '2024-04-23 06:28:48.639656', '', '2024-04-23 06:28:48.639673', 0, 1,
                    1200, 5, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1784, 'Es gibt nachwievor offenen Themen
    - Prozess Kommunikation wurde nun auch gebrieft und sie werden Anpassungen vornehmen, inkl. Leitbild ',
                    '2024-04-29 07:17:43.520013',
                    'Ziele in den Przessen werden schwierig sein zu definieren, bis jetzt haben wir noch keine ',
                    '2024-04-29 07:18:31.991626', NULL, 1, 1154, 4, 'ordinal', 'FAIL', 2);
            INSERT INTO okr_pitc.check_in
            VALUES (2128, 'Team informiert, Feedback eingeholt und Massnahme Plan (siehe O1:KR3) erstellt',
                    '2024-06-17 11:08:41.695573', '', '2024-06-17 15:32:33.149232', NULL, 1, 1218, 10, 'ordinal',
                    'TARGET', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1731,
                    'Wir planen in den nächsten Wochen den Schulungstermin und können dann den def. Startschuss festlegen.',
                    '2024-04-23 06:27:52.819785', '', '2024-04-23 06:27:52.819799', 0, 1, 1180, 5, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1986, 'keine Veränderung', '2024-05-27 07:47:24.783818', '', '2024-05-27 07:47:24.783823', NULL, 1,
                    1206, 3, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1680,
                    'Anfrage für Visualisierungsworkshop erhalten und wir werden bei CH-Open den Workshop eingeben',
                    '2024-04-15 08:17:22.650778', '', '2024-04-15 08:17:22.650782', NULL, 1, 1215, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1999, '', '2024-05-27 09:27:18.09449', '', '2024-05-27 09:27:18.094493', NULL, 1, 1188, 10,
                    'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1990, '', '2024-05-27 07:48:28.403061', '', '2024-05-27 07:48:28.403065', 153, 1, 1214, 10,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1679, '', '2024-04-15 08:15:26.207401', '', '2024-04-15 08:15:26.207405', 100, 1, 1214, 10,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2243, '100 h ', '2024-07-10 08:21:14.560577', '', '2024-07-10 08:21:14.560608', NULL, 1, 1268, 5,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (118, 'Die Auftragsverrechenbarkeit war im Juli sehr erfreulich', '2023-08-21 10:54:16.585038', '',
                    '2023-08-16 22:00:00', 99, 1, 121, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1617, '', '2024-04-02 06:40:45.711575', '', '2024-04-02 06:40:45.71158', 92424, 1, 1191, 6,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2066, '', '2024-06-07 11:38:21.098907',
                    'Feature ready ist nun gemäss Roadmap MMP auf Ende August geplant', '2024-06-07 11:38:21.098913',
                    NULL, 1, 1156, 4, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2313, 'Commit erreicht und im Redaktonsplan detailliert vermerkt.', '2024-07-23 09:40:15.872963',
                    '', '2024-07-23 09:40:15.87297', NULL, 1, 1283, 8, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1872, 'Bidu hat DevOps Engineer abgeschlossen', '2024-05-08 17:33:46.190554', '',
                    '2024-05-08 17:33:46.190561', NULL, 1, 1207, 7, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1822,
                    'Wir starten in diesen Tagen mit der Bereinigung der SEO Probleme. Die Blogpost Datenbank wurde ab 2020 bis heute bereits bereinigt. Nun gehen wir an die SEO Tasks.',
                    '2024-05-01 13:44:03.320744', '', '2024-05-01 13:44:03.320762', NULL, 1, 1179, 8, 'ordinal', 'FAIL',
                    1);
            INSERT INTO okr_pitc.check_in
            VALUES (2195, 'Grundgerüst erstellt, erste Namen vermerkt. Vorgehen Dokumentation wird noch entschieden. ',
                    '2024-07-01 04:35:49.578069', '', '2024-07-01 04:35:49.578073', NULL, 1, 1283, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (2207, 'noch keine Veränderung. Werden eine Roadmap erstellen', '2024-07-02 08:37:49.443678',
                    'Roadmap erstellen', '2024-07-02 08:37:49.443682', 0, 1, 1314, 5, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1926, 'RohrMax Backlog ist noch nicht priorisiert (dem Team fehlt die Zeit für Interne Themen)',
                    '2024-05-21 11:17:57.629582', '', '2024-05-21 11:17:57.629586', NULL, 1, 1209, 2, 'ordinal',
                    'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2110, 'Ziel hat sich geändert, aber würde dsagen wir haben mindestens Commit erreicht ',
                    '2024-06-13 06:24:33.106254', '', '2024-06-13 06:24:33.106258', NULL, 1, 1156, 4, 'ordinal',
                    'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (307, 'WAC Dinner durchgeführt', '2023-09-11 09:46:13.242651',
                    'die entschiedenen Aktivitäten weiterführen', '2023-09-10 22:00:00', 1, 1, 67, 5, 'metric', NULL,
                    1);
            INSERT INTO okr_pitc.check_in
            VALUES (237, 'Weiterhin keine Neukunden (Sales etwas tiefer am laufen)', '2023-08-25 08:40:42.399931',
                    'Sales, Sales, Sales', '2023-08-24 22:00:00', 0, 1, 73, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (206, 'noch nicht gestartet ', '2023-08-24 08:00:02.066646', '', '2023-08-23 22:00:00', 0, 1, 123, 5,
                    'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2077,
                    'Verständnis Division UX geschaffen und an Abstimmungsmeeting mit dev bereichen diskutiert. Werden aber erst im nächsten Cycle ein gemeinsames Statement erarbeiten',
                    '2024-06-10 08:14:09.813583', '', '2024-06-10 08:14:09.813589', NULL, 1, 1220, 5, 'ordinal',
                    'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2078, '', '2024-06-10 08:48:11.271499', '', '2024-06-10 08:48:11.271504', 1016, 1, 1212, 4,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (119, 'Von Budget von CHF 111 800 wurde bereits 56 610 benutzt ', '2023-07-27 07:37:35.860928', '',
                    '2023-07-26 22:00:00', 55190, 1, 122, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1624, 'Startet ab Ende April. Zur Zeit sind wir an der finalen Freigabe für die Designs. ',
                    '2024-04-02 07:10:38.401593', '', '2024-04-02 07:10:38.401598', NULL, 1, 1179, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1991, 'keine Veränderung', '2024-05-27 07:48:51.198159', '', '2024-05-27 07:48:51.198162', NULL, 1,
                    1215, 10, 'ordinal', 'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2075, 'keine Veränderung', '2024-06-10 08:11:56.873024', '', '2024-06-10 08:11:56.873029', NULL, 1,
                    1215, 10, 'ordinal', 'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1788,
                    'Es wurden diverse Massnahmen getroffen inklusive Kostendacherhöhung sowie ext. Unterstützung aufs Projekt nehmen',
                    '2024-04-29 07:24:58.282202', '', '2024-04-29 07:24:58.282208', NULL, 1, 1156, 4, 'ordinal',
                    'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2076, 'Diese Woche erhalten wir Feedback vom Team', '2024-06-10 08:12:48.379869', '',
                    '2024-06-10 08:12:48.379874', NULL, 1, 1218, 5, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1656, '', '2024-04-08 11:07:30.517459', '', '2024-04-08 11:07:30.517467', 464220, 1, 1230, 7,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1867, 'Es sind noch nicht alle Verlängerungen ab Juli unter Dach und Fach',
                    '2024-05-07 11:53:20.515084', '', '2024-05-07 11:53:20.515092', 831561, 1, 1230, 7, 'metric', NULL,
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (2310, 'Status Quo', '2024-07-23 06:57:59.58275', '', '2024-07-23 06:57:59.58276', NULL, 1, 1279, 5,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (232, 'Keine Anpassung seit letzter Messung', '2023-08-25 08:25:47.522134',
                    'Unklar, welche Massnahme umgesetzt wurde - evtl. sind wir mit wearemoving und dem neuen tactical bereits weiter. ',
                    '2023-08-24 22:00:00', 0.7, 1, 67, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2268, 'Roadmap erstellt, Market Research sind wir noch am finalisieren',
                    '2024-07-15 08:09:36.529244', '', '2024-07-15 08:09:36.529248', NULL, 1, 1324, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (188, 'Der Juli Umsatz ist unter Budget wegen vielen Ferienabwesenheiten ',
                    '2023-09-11 12:33:06.903323', '', '2023-08-16 22:00:00', 391049, 1, 120, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1648, 'läuft gut und wir haben mit den Post jetzt sicherlich auch mehr Sichtbarkeit',
                    '2024-04-08 07:29:17.778005', '', '2024-04-08 07:29:17.77801', 84, 1, 1214, 8, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (341, 'In /mid-Week wird ein PoC durchgeführt', '2023-09-14 12:35:35.076404', 'PoC durchführen',
                    '2023-09-13 22:00:00', 0.7, 1, 105, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2212, 'keine Veränderung', '2024-07-02 08:39:49.884998', '', '2024-07-02 08:39:49.885003', NULL, 1,
                    1321, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1782, 'getting there...', '2024-04-29 06:51:54.776068', '', '2024-04-29 06:51:54.776074', 771973, 1,
                    1230, 7, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2300, 'Einladung verschickt für Kickoff, Actionplan muss noch erstellt werden',
                    '2024-07-22 08:14:03.072382', '', '2024-07-22 08:14:03.072385', NULL, 1, 1319, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1935, '', '2024-05-21 11:45:43.755282', '', '2024-05-21 11:45:43.755286', 151, 1, 1214, 10,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2008, 'Wir sind dran. ', '2024-05-28 07:18:15.932529', '', '2024-05-28 07:18:15.93255', 8, 1, 1200,
                    6, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2244, 'noch nicht gestartet', '2024-07-10 08:21:46.413395', '', '2024-07-10 08:21:46.413421', NULL,
                    1, 1270, 5, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2270, '', '2024-07-15 08:10:16.974845', '', '2024-07-15 08:10:16.974847', 1, 1, 1315, 5, 'metric',
                    NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (148, 'keine Veränderung', '2023-07-31 07:16:20.786844', 'Sales, Sales, Sales',
                    '2023-07-30 22:00:00', 0, 1, 73, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (146, 'leider tiefe abs. Verrechenbarkeit im Juni wegen Ferienabwesenheiten und Krankheit',
                    '2023-07-31 07:12:10.906762', 'wir geben Gas im Juli und August', '2023-07-30 22:00:00', 0, 1, 71,
                    5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2033, '', '2024-06-03 07:00:10.113294', '', '2024-06-03 07:00:10.113301', 96096, 1, 1191, 3,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2232, 'Wir halten unsere Botschafter:innen im Redaktionsplan im Taiga fest.',
                    '2024-07-09 04:21:14.85107', '', '2024-07-09 04:21:14.851073', 0, 1, 1318, 8, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2293, 'keine Veränderung', '2024-07-22 08:07:57.203061', '', '2024-07-22 08:07:57.203064', 83001, 1,
                    1312, 5, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2184, 'SHKB hat endlich unterschrieben! ', '2024-06-26 19:31:41.426539', '',
                    '2024-06-26 19:32:41.358688', NULL, 1, 1217, 10, 'ordinal', 'TARGET', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2001, 'Call mit SHKB am 29.5. ob 3rd Level ja oder neien', '2024-05-27 09:30:50.607788', '',
                    '2024-05-27 09:30:50.607791', NULL, 1, 1217, 5, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2000, 'Keine Veränderungen', '2024-05-27 09:28:38.111606', '', '2024-05-27 09:29:29.929363', NULL,
                    1, 1207, 7, 'ordinal', 'COMMIT', 2);
            INSERT INTO okr_pitc.check_in
            VALUES (1647, 'Post und Kommentar über Unfold Event gemacht', '2024-04-08 07:28:42.358994',
                    'dem SoMe Plan folgen', '2024-04-08 07:28:42.359', 1, 1, 1210, 9, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2233, 'KicKoff im Juli mit HR.', '2024-07-09 07:05:57.484909', '', '2024-07-09 07:05:57.484911',
                    NULL, 1, 1280, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1733, 'hat nicht sauber funktioniert. Wir mussten 4 Umgebungen anschliessend nochmals Releasen',
                    '2024-04-23 09:00:53.167846',
                    'Umgang mit Sentry Issue im Team besprochen. Momentann sind wir bei 210',
                    '2024-04-23 09:00:53.167868', 0.2, 1, 1151, 3, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1985, 'zeitlich noch nicht dazu gekommen ', '2024-05-27 07:47:18.705226', '',
                    '2024-05-27 07:47:18.705229', NULL, 1, 1150, 1, 'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1984, 'keine Veränderung', '2024-05-27 07:47:04.277238', '', '2024-05-27 07:47:04.277242', 92424, 1,
                    1191, 4, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2277, 'Vorschlag Prompt in LKW 30, anschliessend Feinschliff mit Saraina und Vorstellung dem Team.',
                    '2024-07-16 05:04:36.357297', '', '2024-07-16 05:04:36.357299', NULL, 1, 1279, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (2095, 'Deal bei Mobi wird wahrscheinlich nicht zu Stande kommen, keine Einflussmöglichkeiten.',
                    '2024-06-11 13:06:02.651592', '', '2024-08-16 13:01:48.017208', NULL, 1, 1229, 0, 'ordinal',
                    'COMMIT', 2);
            INSERT INTO okr_pitc.check_in
            VALUES (1681,
                    'Bereichsstrategie ist niedergeschrieben und bei Simu zum überarbeiten. Nach den Ferien von Berti wird sie finalisiert',
                    '2024-04-15 08:19:02.013217', '', '2024-04-15 08:19:02.01322', NULL, 1, 1218, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (187, 'Von Budget von CHF 111 800 wurde bereits 71060 benutzt ', '2023-08-21 10:28:35.598462', '',
                    '2023-08-16 22:00:00', 71060, 1, 122, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1936, 'keine Veränderung seit letzten Check-in', '2024-05-21 11:46:14.397143', '',
                    '2024-05-21 11:46:14.397147', NULL, 1, 1215, 10, 'ordinal', 'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (340,
                    'Cilium ist als CNI auf LPG1-Cluster installiert, die Verbindungen werden mit Hubble analysiert.',
                    '2023-09-14 12:34:21.883211', 'PoC weiterführen', '2023-09-13 22:00:00', 0.7, 1, 79, 5, 'metric',
                    NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1874, 'Dave, Pippo und Dänu haben Möglichkeiten für Angebot besprochen.
    Kubernetes-nahe Entwicklung oder Go-Applikation.
    Wir tendieren zu Ersterem. Iwan im Vorstellungs-Prozess bei der Mobi.', '2024-05-08 17:35:40.666306', '',
                    '2024-05-13 07:27:32.602674', NULL, 1, 1229, 6, 'ordinal', 'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1781,
                    'Von der SHB warten wir auf eine Antwort. Weiter haben wir der RHB einen 3rd Level Light offeriert.',
                    '2024-04-29 06:49:47.60432', '', '2024-04-29 06:49:47.604327', NULL, 1, 1217, 5, 'ordinal',
                    'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (313, 'Sehr guter August Umsatz von CHF 52''022', '2023-09-11 12:32:56.31526', '',
                    '2023-09-10 22:00:00', 443070, 1, 120, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2245, 'Nein ', '2024-07-10 08:22:09.133271', '', '2024-07-10 08:22:09.133273', NULL, 1, 1285, 5,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1906,
                    'Wir warten auf das finale Go on WLY, damit wir mit den SEO Korrekturen auf der neuen Website beginnen können. ',
                    '2024-05-15 05:12:06.50115', '', '2024-05-15 05:12:06.501158', NULL, 1, 1179, 8, 'ordinal', 'FAIL',
                    1);
            INSERT INTO okr_pitc.check_in
            VALUES (1623, 'Termin und Roadmap für Bereichsstrategie ist erstellt', '2024-04-02 06:45:11.927657', '',
                    '2024-04-02 06:45:11.927662', NULL, 1, 1218, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2311, 'Termin mit Tru: Erste Stelle Kafka. Wir machen uns an ein Storyboard.',
                    '2024-07-23 06:58:37.050753', '', '2024-07-23 06:58:37.050762', NULL, 1, 1280, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (147, 'wir haben alle Members halten können', '2023-07-31 07:13:35.271844',
                    'Auslastung im Herbst von allen Members und neuen Members sicherstellen', '2023-07-30 22:00:00',
                    0.3, 1, 78, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1645,
                    'Neue Anfrage für Berti als Workshop Moderator. Muss aber zuerst noch geprüft und offeriert werden',
                    '2024-04-08 07:27:48.055745', '', '2024-04-08 07:27:48.055748', NULL, 1, 1206, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1644, 'keine Veränderung seit letztem Checkin', '2024-04-08 07:27:04.443764', 'Sales! Sales! Sales',
                    '2024-04-08 07:27:04.443768', 92424, 1, 1191, 6, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1646, 'keine Veränderung seit letztem Check-in', '2024-04-08 07:28:13.098649', '',
                    '2024-04-08 07:28:13.098654', NULL, 1, 1208, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2231, 'Wir verfolgen die Thematik weiter und suchen fleissig Speakers.',
                    '2024-07-09 04:17:20.214264', '', '2024-07-09 04:17:20.214267', NULL, 1, 1283, 8, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1908,
                    'Weitere Aufträge an Mayra haben sich durch die Schulung ergeben und können dann gleich im neuen Wordpress eingesetzt werden. Animationen werden getestet.',
                    '2024-05-15 05:13:41.734952', '', '2024-05-15 05:13:41.734959', 5, 1, 1200, 6, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (222, 'OpenShift Cluster LPG1 wurde erfolgreich aufgebaut. Cilium PoC gestartet.',
                    '2023-08-24 14:32:46.341097', '', '2023-08-23 22:00:00', 0.3, 1, 79, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2280,
                    'Die ersten Divisionsaustausche finden statt - Commit wird wahrscheinlich nächste Woche erreicht.',
                    '2024-07-16 05:24:13.400385', '', '2024-07-16 05:24:13.400387', NULL, 1, 1283, 8, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (2035, 'erarbeiten wir heute', '2024-06-03 07:00:54.792531', '', '2024-06-03 07:00:54.792538', NULL,
                    1, 1208, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2281,
                    'Wir verfolgen dies weiter im Juli und halten die Botschafter:innen und allfällige Themen/Posts fest.',
                    '2024-07-16 05:25:41.96709', '', '2024-07-16 05:25:41.967092', 2, 1, 1318, 8, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2295, 'keine Veränderung', '2024-07-22 08:10:08.574354', '', '2024-07-22 08:10:22.304798', NULL, 1,
                    1324, 5, 'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (231, 'Weiterhin keine Messung - unser Team-Meeting findet nur noch alle 6 Wochen statt',
                    '2023-08-25 08:24:23.026984', 'Alternative definieren - evtl. im Bila', '2023-08-24 22:00:00', 0, 1,
                    68, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2239, '', '2024-07-09 08:16:11.006017', '', '2024-07-09 08:16:11.006019', 751825, 1, 1328, 5,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2230,
                    'Go-Live am 4. Juli. SEO-Score liegt bei 83. Beim nächsten Check-in ziehen wir die ersten Vergleichszahlen (mind. 1 Woche live).',
                    '2024-07-09 04:04:20.292339', '', '2024-07-09 04:04:20.292352', NULL, 1, 1281, 9, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (2229, 'Status Quo und Problematik mit ChatGPT Firmenaccount.', '2024-07-09 03:58:37.116109', '',
                    '2024-07-09 03:58:37.116121', NULL, 1, 1279, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1937,
                    'Bereichsstrategie finalisiert und mit SUM Buddy besprochen. Warten auf Feedback von GL und dann besprechen wir es beim nächsten WAC Morning mit dem Team',
                    '2024-05-21 11:47:17.890421', '', '2024-05-21 11:47:17.890426', NULL, 1, 1218, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1834, 'Adi stellte imagine am monthly vor. ', '2024-05-02 07:17:31.554138', '',
                    '2024-05-02 07:17:31.554153', NULL, 1, 1193, 8, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2037, '', '2024-06-03 07:01:32.970878', '', '2024-06-03 07:01:32.970885', 155, 1, 1214, 10,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1905, 'wir haben nun alles das erste mal gemerget', '2024-05-13 09:31:54.085971', '',
                    '2024-05-13 09:31:54.085981', NULL, 1, 1154, 4, 'ordinal', 'COMMIT', 2);
            INSERT INTO okr_pitc.check_in
            VALUES (2210, 'keine Veränderung', '2024-07-02 08:39:14.512815', '', '2024-07-02 08:39:14.512819', NULL, 1,
                    1317, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1705, 'Bereits eine interessierte Person', '2024-04-17 12:32:35.369377', '',
                    '2024-04-17 12:32:35.369382', NULL, 1, 1216, 5, 'ordinal', 'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1907,
                    'Schulung absolviert. Diese und nächste Woche Fokus auf Seitenaufbau & Services & Solutions Inhalte.',
                    '2024-05-15 05:12:55.049747', '', '2024-05-15 05:12:55.049754', 5, 1, 1180, 7, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2094, 'Keine Veränderung, lassen es bleiben. Wird Platform Engineering Thema eingebettet.',
                    '2024-06-11 13:04:56.874009', '', '2024-06-11 13:04:56.874013', NULL, 1, 1211, 0, 'ordinal', 'FAIL',
                    1);
            INSERT INTO okr_pitc.check_in
            VALUES (311,
                    'sehr viele qualitative Leads (Visana, UZH, HRM Systems, FMH, Kanton Züri) aber noch keine fixe Bestätigun',
                    '2023-09-11 12:00:08.4229', 'Close the Sale!!!!', '2023-09-10 22:00:00', 0, 1, 73, 5, 'metric',
                    NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2294, 'keine Veränderung seit letztem Checkin', '2024-07-22 08:08:49.375259', '',
                    '2024-07-22 08:08:49.375261', NULL, 1, 1322, 5, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2092, 'Lead Frequentis aus /mid-Journey an Kubecon', '2024-06-11 13:02:50.063092', '',
                    '2024-06-11 13:02:50.063096', NULL, 1, 1188, 10, 'ordinal', 'STRETCH', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2034, 'keine Veränderung', '2024-06-03 07:00:36.952373', '', '2024-06-03 07:00:36.952381', NULL, 1,
                    1206, 3, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1700, 'SHKB 3rd Level Offerte pending', '2024-04-17 12:23:29.717497', '',
                    '2024-04-17 12:23:29.717501', NULL, 1, 1217, 5, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1900, 'Zeitlich nicht weiter gemacht ', '2024-05-13 09:28:18.124109', '',
                    '2024-05-13 09:28:18.124115', NULL, 1, 1150, 1, 'ordinal', 'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1899, 'Wir haben wieder über 123 Stunden ', '2024-05-13 09:27:47.180548', '',
                    '2024-05-13 09:27:47.180554', NULL, 1, 1149, 2, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2278, 'Termin mit Tru am 23.7.24', '2024-07-16 05:07:11.31255', '', '2024-07-16 05:07:11.312553',
                    NULL, 1, 1280, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1649, 'mit Team besprochen und Tobi H. ist jetzt an der Umsetzung', '2024-04-08 07:29:56.963986',
                    '', '2024-04-08 07:29:56.96399', NULL, 1, 1215, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2074, 'Linkedin Post zum Visualisierungworkshop', '2024-06-10 08:10:54.448674', '',
                    '2024-06-10 08:10:54.448679', 10, 1, 1210, 9, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1904, 'Neuer Prozess ist eigenführt sowie hat Andy Gurtner und Andy neu gestartet auf dem Projekt ',
                    '2024-05-13 09:31:14.674236', '', '2024-05-13 09:31:14.674243', NULL, 1, 1156, 4, 'ordinal',
                    'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1938, 'keine Veränderung seit letztem Checkin', '2024-05-21 11:48:05.899665', '',
                    '2024-05-21 11:48:05.89967', NULL, 1, 1220, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2038, '', '2024-06-03 07:02:00.217048', '', '2024-06-03 07:02:00.217055', 955, 1, 1212, 5, 'metric',
                    NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1625, 'Start wahrscheinlich ab Mai', '2024-04-02 07:12:22.265489', '', '2024-04-02 07:12:22.265495',
                    0, 1, 1180, 5, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2299, 'mit jenny besprochen. Workshop geplant', '2024-07-22 08:13:04.639875', '',
                    '2024-07-22 08:13:04.639878', NULL, 1, 1317, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (392, 'konnten leider keinen Neukunden gewinnen - haben aber sehr viele gute Leads am laufen',
                    '2023-09-19 06:35:43.192874', 'Leads im nächsten Quartal gewinnen', '2023-09-18 22:00:00', 0, 1, 73,
                    5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2098, 'Unverändert.', '2024-06-11 13:09:59.995098', '', '2024-06-11 13:09:59.995102', NULL, 1, 1216,
                    10, 'ordinal', 'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1783, 'Agenda ist versendet ', '2024-04-29 07:16:28.013982', '', '2024-04-29 07:16:28.013989', NULL,
                    1, 1152, 7, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2093, 'Vale dran, aber unsicher ob bis Ende Juni Prüfung abgelgt.', '2024-06-11 13:03:55.937198',
                    '', '2024-06-11 13:09:02.146284', NULL, 1, 1207, 5, 'ordinal', 'COMMIT', 3);
            INSERT INTO okr_pitc.check_in
            VALUES (388, 'keine Veränderung weil wir es erledigt haben', '2023-09-19 06:31:05.613322', 'keine',
                    '2023-09-18 22:00:00', 1, 1, 67, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1746, '', '2024-04-23 16:27:24.621936', '', '2024-04-23 16:27:24.621943', 766193, 1, 1230, 7,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (191, 'Wenig indirekter Aufwand wegen Ferienabwesenheiten', '2023-08-21 10:53:29.225721', '',
                    '2023-08-16 22:00:00', 64, 1, 125, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (236, 'TH Reduktion auf Ende November (-20%), Aktuell konnte die Auslastung gehalten werden. ',
                    '2023-08-25 08:39:57.50632', 'Auslastung Herbst sicherstellen', '2023-08-24 22:00:00', 0.3, 1, 78,
                    5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1934, 'Uphill Conf Post', '2024-05-21 11:44:30.235227', '', '2024-05-21 11:44:30.235231', 7, 1,
                    1210, 9, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2296, 'noch keine Veränderung (Front wurde abgesagt)', '2024-07-22 08:11:08.356937',
                    'in den Bilas mit den Members diskutieren an welche UX Veranstaltungen sie gehen',
                    '2024-07-22 08:11:08.356939', 0, 1, 1314, 4, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2247, 'nach wie vor in der Vorbereitung ', '2024-07-10 08:23:25.936929', '',
                    '2024-07-10 08:23:25.936932', NULL, 1, 1274, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1932, 'keine Veränderung', '2024-05-21 11:43:27.619602', '', '2024-05-21 11:43:27.619609', NULL, 1,
                    1206, 3, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1933,
                    'keine Veränderung seit letztem Check-in. Wir warten auf die finalisierung der Bereichsstrategie',
                    '2024-05-21 11:44:05.133703', '', '2024-05-21 11:44:05.133708', NULL, 1, 1208, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (2084, '', '2024-06-10 13:42:52.962147', '', '2024-06-11 13:48:40.370942', 947829, 1, 1230, 2,
                    'metric', NULL, 2);
            INSERT INTO okr_pitc.check_in
            VALUES (1669, 'noch nicht gestartet ', '2024-04-11 13:35:21.874421', '', '2024-04-11 13:35:21.874425', NULL,
                    1, 1153, 3, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1668, 'Erste Analyse gemacht und Ideen sind generiert', '2024-04-11 13:34:56.955409',
                    'wegen Daily Business und ISO 14001 ist meien Zuversicht sehr klein für dieses Quartal',
                    '2024-04-11 13:34:56.955413', NULL, 1, 1150, 1, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (235,
                    'Juli: 54%, August 68% - die absolute Verrechenbarkeit ist wegen Abwesenheiten, Ferien usw. starken Schwankungen unterworfen. Die Verrechenbarkeit selber ist mit 100% hoch',
                    '2023-08-25 08:38:13.554972', 'Überdenken, ob sich die Kennzahl eigenet (ob all den Schwankungen)',
                    '2023-08-24 22:00:00', 2, 1, 71, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1824, 'Die ersten 6 Grafiken sind bei Jürgen in Bearbeitung. Mayra wird nächste Woche den Rest fertigstellen. Die Animationen sind bis spätestens Ende Mai fertig.
    Fotograin: Zweite Fotografin angefragt für Büro- & Alltagssituationen', '2024-05-01 13:46:09.656711', '',
                    '2024-05-01 13:46:09.656729', 5, 1, 1200, 6, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2279, '', '2024-07-16 05:13:15.315452',
                    'Wir nutzen den Juli um weiterhin die Website Inhalte zu optimieren. Die organische Suchsichtbarkeit prüfen wir am 5. August (1. Monat live)',
                    '2024-07-16 05:13:15.315454', NULL, 1, 1281, 9, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1787, 'kein Fortschritt ', '2024-04-29 07:24:03.732261', '', '2024-04-29 07:24:03.732268', NULL, 1,
                    1153, 3, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2072, 'keine Veränderung', '2024-06-10 08:09:25.138813', '', '2024-06-10 08:09:25.13882', NULL, 1,
                    1206, 3, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2146, 'keine Veränderung', '2024-06-17 15:31:35.788883', '', '2024-06-17 15:31:55.221633', NULL, 1,
                    1220, 0, 'ordinal', 'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1987, 'keine Veränderung', '2024-05-27 07:47:39.032942', '', '2024-05-27 07:47:39.032945', NULL, 1,
                    1208, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1988, 'Tendenz Mai wieder nicht gut ', '2024-05-27 07:47:55.803936', '',
                    '2024-05-27 07:47:55.80394', NULL, 1, 1149, 2, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1989, 'Post Visualisierungsworkshop', '2024-05-27 07:47:58.044899', '',
                    '2024-05-27 07:47:58.044902', 8, 1, 1210, 9, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1618, 'Mögliche Aufträge für Mayra bei Mobi, für Berti evtl. punktuell bei BKD',
                    '2024-04-02 06:41:14.232472', '', '2024-04-02 06:41:14.232477', NULL, 1, 1206, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (117, 'Wir liegen 80% über dem Vorjahr in der selben Zeiteinheit (Juni 2022; CHF 197''678)',
                    '2023-07-27 06:31:23.894166', '', '2023-07-26 22:00:00', 357716, 1, 120, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2065, 'abgeschlossen, kein Release mehr ', '2024-06-07 11:37:35.726732', '',
                    '2024-06-07 11:37:35.726737', 0.3, 1, 1151, 4, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1626, 'Start im Mai', '2024-04-02 07:12:45.648744', '', '2024-04-02 07:12:45.648748', 0, 1, 1200, 5,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2298, 'mit Jenny aufgegleist. Konzept muss noch erstellt werden', '2024-07-22 08:12:26.329719', '',
                    '2024-07-22 08:12:26.329722', NULL, 1, 1316, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2073, 'Actionplan erarbeitet und wird am Dienstag mit SUM Buddy besprochen',
                    '2024-06-10 08:10:13.816945', '', '2024-06-10 08:10:13.81695', NULL, 1, 1208, 5, 'ordinal',
                    'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1650,
                    'Bereichsstrategie wurde von Berti und Simu besprochen und wird jetzt final niedergeschrieben',
                    '2024-04-08 07:30:34.802382', '', '2024-04-08 07:30:34.802386', NULL, 1, 1218, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1903, 'wir haben ein erfolgreicehs Communitymeeting durchgeführt mit 20 Personen',
                    '2024-05-13 09:30:09.624494', '', '2024-05-13 09:30:09.624501', NULL, 1, 1152, 7, 'ordinal',
                    'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2266, '', '2024-07-15 08:08:13.32055', '', '2024-07-15 08:08:13.320553', 83001, 1, 1312, 5,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1670, 'Noch kein Resultat, siehe auch Steerings Codi ', '2024-04-11 13:36:23.750029',
                    'Ext. Ressourcen auf Projekt nehmen', '2024-04-11 13:36:23.750051', NULL, 1, 1156, 4, 'ordinal',
                    'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (391, 'keine Veränderung seit letztem Checkin', '2023-09-19 06:34:39.473208',
                    'Auslastung sicherstellen', '2023-09-18 22:00:00', 0.3, 1, 78, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (204, 'Noch nicht gestartet ', '2023-08-24 07:57:06.571643', '', '2023-08-23 22:00:00', 0, 1, 126, 5,
                    'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1931,
                    'keine Veränderung im UX Umsatz selber (BKD und SAC Projekte laufen im PTime Forecast nicht über uns). SwissLife Bestätigung noch ausstehend und evtl. Polypoint',
                    '2024-05-21 11:42:52.040148', 'Polypoint Deal reinholen, WTO''s gewinnen',
                    '2024-05-21 11:42:52.040153', 92424, 1, 1191, 4, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2097, 'Lukas Koller hat quasi ein neues Company OKR definiert.
    Werden wir weiterverfolgen und als Vorschlag eingeben.', '2024-06-11 13:08:08.548484', '', '2024-06-11 13:08:30.608544',
                    NULL, 1, 1222, 10, 'ordinal', 'STRETCH', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (308, 'Happiness Umfrage im Bila integriert', '2023-09-11 09:47:34.417462',
                    'Umfrage regelmässig durchführen und dann Zielwerte festlegen', '2023-09-10 22:00:00', 0.3, 1, 68,
                    5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (310, 'Keine Veränderung seit letztem Checkin', '2023-09-11 11:46:26.7304',
                    'Auslastung Herbst sicherstellen', '2023-09-10 22:00:00', 0.3, 1, 78, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (389, 'Umfrage bei Bilas eingefügt', '2023-09-19 06:31:56.79753',
                    'nächstne Quartal noch die Zielwerte definieren', '2023-09-18 22:00:00', 0.3, 1, 68, 5, 'metric',
                    NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1972, 'Wir sind ready für Donnerstag :-) ', '2024-05-27 06:41:43.588057', '',
                    '2024-05-27 06:41:43.588061', NULL, 1, 1154, 8, 'ordinal', 'COMMIT', 2);
            INSERT INTO okr_pitc.check_in
            VALUES (2091, 'unverändert --> Warten auf SHKB', '2024-06-11 12:12:50.369652', '',
                    '2024-06-11 12:12:50.369655', NULL, 1, 1217, 5, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1786, 'kein Fortschritt', '2024-04-29 07:23:29.140324', '', '2024-04-29 07:23:42.423421', NULL, 1,
                    1150, 1, 'ordinal', 'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1785, 'bereits 100h', '2024-04-29 07:22:56.090236', '', '2024-04-29 07:22:56.09026', NULL, 1, 1149,
                    3, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2130, 'Hybrider Usability Test und Referenz Landmatrix', '2024-06-17 11:11:09.537321', '',
                    '2024-06-17 11:11:09.537324', 12, 1, 1210, 10, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1940, '', '2024-05-21 14:18:05.189451', '', '2024-05-21 14:18:05.189455', 826, 1, 1212, 5, 'metric',
                    NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (190, 'Ruby Upgrade und viele Lösungsstunden', '2023-08-21 10:53:36.260616', '',
                    '2023-07-16 22:00:00', 48, 1, 125, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2209, 'keine Veränderung', '2024-07-02 08:38:51.090563', '', '2024-07-02 08:38:51.090567', NULL, 1,
                    1316, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1901, 'zeitlich nicht weitergemacht ', '2024-05-13 09:28:40.142806', '',
                    '2024-05-13 09:28:40.142813', NULL, 1, 1153, 3, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1902, 'kein neues Relase geplant ', '2024-05-13 09:29:06.100737', '', '2024-05-13 09:29:06.100754',
                    0.25, 1, 1151, 3, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1678, 'SoMe Post von Flavio plus Kommentar bei Beitrag von Zeix', '2024-04-15 08:14:26.8989', '',
                    '2024-04-15 08:14:26.898904', 2, 1, 1210, 9, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1677, 'keine Veränderung seit letztem Checkin', '2024-04-15 08:13:59.193507', '',
                    '2024-04-15 08:13:59.193511', NULL, 1, 1208, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2123,
                    'Aufgrund des früheren Imports konnte die saubere Basis nicht vorher gemacht resp. übernommen werden. Wir optimieren nun direkt auf der neuen Seite. ',
                    '2024-06-14 08:08:50.168806',
                    'Die Target Zone erreichen wir aber erst nach Livegang, wenn die Seiten indexiert sind und wir uns dort den Fokus auf SEO setzen. ',
                    '2024-06-14 08:08:50.168808', NULL, 1, 1179, 8, 'ordinal', 'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2272, 'noch keine Veränderung', '2024-07-15 08:11:37.118753', '', '2024-07-15 08:11:37.118756',
                    NULL, 1, 1319, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2096,
                    'Erste Resultate in sig-gitops erreicht, braucht noch mehr Zeit, kann weiter genommen werden.',
                    '2024-06-11 13:07:13.773361', '', '2024-06-11 13:07:13.773365', NULL, 1, 1213, 10, 'ordinal',
                    'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2205,
                    'Pascou hat bereits unterschrieben, nächste Woche unterschreibt Simone, dann haben wir alle Ausfälle ersetzt',
                    '2024-07-02 08:35:59.452167', '', '2024-07-02 08:35:59.452174', NULL, 1, 1322, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1619, 'Termine und Roadmap erstellt', '2024-04-02 06:41:40.73829', '', '2024-04-02 06:41:40.738295',
                    NULL, 1, 1208, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2005, '', '2024-05-27 10:52:39.069966', '', '2024-05-27 10:52:39.06997', 893, 1, 1212, 5, 'metric',
                    NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2036, 'SoMe Post über Device Lab', '2024-06-03 07:01:20.027173', '', '2024-06-03 07:01:20.02718', 9,
                    1, 1210, 9, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2064, 'wir sind noch nicht weiter', '2024-06-07 11:37:01.6608', '', '2024-06-07 11:37:01.660806',
                    NULL, 1, 1153, 3, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2297, 'Post  Animationen Puzzle Website', '2024-07-22 08:11:34.525937', '',
                    '2024-07-22 08:11:34.525939', 2, 1, 1315, 5, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1651, 'Robin hat die AWS Zertifizierung abgeschlossen', '2024-04-08 08:16:19.799037', '',
                    '2024-04-08 08:16:19.799041', NULL, 1, 1207, 5, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (317, 'Datum steht noch nicht fest ', '2023-09-11 13:07:59.57807', '', '2023-09-10 22:00:00', 0, 1,
                    126, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1706, 'Noch nicht alle Verlängerungen abgeschlossen.', '2024-04-17 12:35:31.912394', '',
                    '2024-04-17 12:35:31.912398', 673053, 1, 1230, 7, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (390, 'September tiefe Auslastung', '2023-09-19 06:33:24.606374',
                    ' nehmen wir mit für das nächste Quartal und haben einen Actionplan erstellt',
                    '2023-09-18 22:00:00', 2, 1, 71, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1918, 'Blogpost Imagine wurde veröffentlicht (inkl. SoMe). ', '2024-05-16 06:42:11.832574', '',
                    '2024-05-16 06:42:11.83258', NULL, 1, 1193, 8, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (159,
                    'Dagger Kundenmapping durchgeführt und potenzial bewertet. Zusätzlich haben wir ein Fragenkatalog für die kommenden Gespräche mit potenziellen Kunden erstellt.',
                    '2023-08-09 14:47:24.030512', '', '2023-08-08 22:00:00', 0.3, 1, 76, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2069, 'Phil arbeitet daran.', '2024-06-10 07:01:12.662588', '', '2024-06-10 07:01:12.662593', NULL,
                    1, 1189, 7, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2068, 'Suricata läuft und ist brauchbar zumindest für forensische Auswertung.',
                    '2024-06-10 07:00:38.577713', '', '2024-06-10 07:00:38.577718', NULL, 1, 1187, 5, 'ordinal',
                    'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1666,
                    'Puzzle DE ist an spannendem Kandidaten mit Kafka Knowhow dran. Ev. könnte diese Person beim internen Knowhow Aufbau mithelfen.',
                    '2024-04-11 12:55:04.136988', '', '2024-04-11 12:55:04.136992', NULL, 1, 1135, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (64,
                    'Quartalsplanungen mit /sys, /mid, /zh, /mobility, /devtre, /bbt, /wac haben schon stattgefunden',
                    '2023-07-05 08:33:20.442628', '', '2023-07-04 22:00:00', 0.75, 1, 116, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2118, 'Give Away Konzept wird am Puzzle Inside Ende Juni vorgestellt
    Nachhaltiges Give Away Puzzle up (Druckvelo) in Produktion
    Interaktive Inszenierung am Puzzle up! sichergestellt (Rollup, Druckvelo)', '2024-06-14 07:47:41.767259', '',
                    '2024-06-14 07:47:41.767261', NULL, 1, 1184, 7, 'ordinal', 'STRETCH', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (62, 'Board ist erstellt. Wird am MarComSales-Weekly und dann am BL-Weekly präsentiert.',
                    '2023-07-05 08:30:10.93451', '', '2023-07-04 22:00:00', 0.75, 1, 117, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2108, '- die Marktopportunitäten für das GJ 24/25 sind erarbeitet und werden ab 1.7.24 parat sein.
    - der definitive Entscheid, welche verfolgt werden, wird am 24.6. am nächsten Monthly gefällt.
    - die Moals (Mittelfrist-Strategie-Sicht) sind erarbeitet und werden ab 1.7.24 umgesetzt.
    - der Prozess P11 wird mit den Moals ergänzt.', '2024-06-13 06:22:15.369725', '', '2024-06-13 06:22:15.369729', NULL, 1,
                    1132, 10, 'ordinal', 'STRETCH', 3);
            INSERT INTO okr_pitc.check_in
            VALUES (2150, 'Anpassung Confidence Level', '2024-06-17 15:52:28.071703', '', '2024-06-17 15:52:28.071705',
                    NULL, 1, 1138, 0, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2163, '', '2024-06-18 06:58:19.729863', '', '2024-06-18 06:58:19.729866', NULL, 1, 1205, 0,
                    'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2018, 'Keine Veränderungen.', '2024-05-29 06:51:30.866411', '', '2024-05-29 06:51:30.866437', NULL,
                    1, 1128, 8, 'ordinal', 'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1634, '- die Marktanalyse ist noch in Erarbeitung
    - Abklärungen Kampagne mit Carlos werden in Angriff genommen', '2024-04-03 18:12:57.457706', '',
                    '2024-04-03 18:12:57.45771', NULL, 1, 1178, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1699, '- 1. Interview mit möglicher BBT Betreuungsperson hat stattgefunden.
    - Abstimmung POs und BBT für Lehrlingsprojekt durchgeführt.
    - Bei BLS angefragt für Einsatz von 4. Lehrjahr Lehrling.', '2024-04-17 07:12:44.120024', '',
                    '2024-04-17 07:12:44.120028', NULL, 1, 1139, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1637, '', '2024-04-04 12:57:02.648214', '', '2024-04-04 12:57:02.648218', NULL, 1, 1135, 5,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2161, '', '2024-06-18 06:56:48.279135', '', '2024-06-18 06:56:48.279137', NULL, 1, 1198, 0,
                    'ordinal', 'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2183, '- die Marktanalyse ist noch in Arbeit.
    - am Puzzle Workshop wurde eine Working Session durchgeführt und mit interessierten Members an den Hypothesen gearbeitet.
    - die Telefonkampagne von Carlos ist aktuell am Laufen. Bereits resultierten daraus Leads für drei Meetings.',
                    '2024-06-26 08:30:39.47362', '', '2024-06-26 08:30:39.473632', NULL, 1, 1254, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1633, '- in GL erstes Review der MO und Ausblick neue MO vorgenommen
    - Frühlings-LST Workshop mit Daniel Osterwalder vorbesprochen', '2024-04-03 18:11:44.828317', '',
                    '2024-05-02 06:08:21.4669', NULL, 1, 1132, 8, 'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2164, 'Dokument mit Vorschlag wird heute der GL übergeben.', '2024-06-18 08:27:29.532426', '',
                    '2024-06-18 08:27:46.116351', NULL, 1, 1185, 10, 'ordinal', 'TARGET', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2148, 'Anpassung Confidence Level', '2024-06-17 15:50:20.735918', '', '2024-06-17 15:50:20.735921',
                    NULL, 1, 1139, 0, 'ordinal', 'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (302, 'Juli: -
    Aug: Aareböötle, Family, Sommerinfoanlass,
    Sept: TWS, Geburiapero, ', '2023-09-11 06:47:35.418407', '', '2023-09-10 22:00:00', 0.9, 1, 40, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (55, '38.17', '2023-07-05 08:14:35.435436', '', '2023-07-04 22:00:00', 38, 1, 108, 5, 'metric', NULL,
                    1);
            INSERT INTO okr_pitc.check_in
            VALUES (2182, '- wir haben am Puzzle Workshop 2024 eine Working Session angeboten und durchgeführt. Daraus entstand eine erste Auslegeordnung. An dieser wird nun weitergearbeitet.
    - als nächsten Schritt organisieren wir uns als Team.', '2024-06-26 08:21:16.947025', '', '2024-06-26 08:21:16.947035',
                    NULL, 1, 1251, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (278, 'Aussichtsreiche Bewerbung (nach 2. Gespräch & Techinterview) am Start',
                    '2023-09-06 12:56:34.29422', 'Im Mob Weekly entscheiden & ev. Angebot durch HR',
                    '2023-09-05 22:00:00', 0.2, 1, 35, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2116, 'Planung wird im Leadership-Monthly End Juni verabschiedet.', '2024-06-14 07:47:09.209577',
                    '', '2024-06-14 07:47:09.209579', NULL, 1, 1182, 8, 'ordinal', 'TARGET', 2);
            INSERT INTO okr_pitc.check_in
            VALUES (287,
                    'Deal von Centris im September. Etliche weitere Leads offen, in Bearbeitung oder bereits offeriert: Uni BE, SHKB, BLS, ETHZ, HPE, Centris, Honeywell, Flughafen ZH',
                    '2023-09-08 07:30:29.308576', '', '2023-09-07 22:00:00', 77800, 1, 51, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1829, 'Account Alignment mit GCP aufgesetzt am 27. Mai
    Account Alignment mit AWS wird aktuell aufgesetzt
    Zertifizierungen AWS -> erreicht
    Zertifizierungen GCP -> noch offen
    Opportunitäten:
    - BAFU: Daten und Digitalisierung (PDD) (Einladungsverfahren) (leider verloren)', '2024-05-02 06:16:25.161542', '',
                    '2024-05-02 06:24:23.08433', NULL, 1, 1176, 4, 'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (277,
                    'Indikatoren & Zielwerte sind definiert. Zusätzliche Frage in Membersumfrage der /mob Subteams ergänzt. Beurteilung & Auswertung von Teamzusammenhalt erfolgt spätestens ab Q2 in allen Subteams.',
                    '2023-09-06 11:54:19.033827', '', '2023-09-05 22:00:00', 0.3, 1, 57, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2190, 'Die Jahreszielwerte sind definiert.', '2024-06-27 07:42:30.475005', '',
                    '2024-06-27 07:42:30.475011', NULL, 1, 1348, 7, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1956, 'Gespräche für internen Teampool geführt. Abstimmung mit CTO geplant.',
                    '2024-05-22 12:17:03.057707', '', '2024-05-22 12:17:03.057711', NULL, 1, 1135, 3, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1971, '', '2024-05-27 06:37:07.339592', '', '2024-05-27 06:37:07.339598', NULL, 1, 1226, 5,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1821, '- Lehrlingsprojekte am LST monthly bestimmt: 1. Prio: UniLu, 2. Prio PCTS-Sheet
    - Interviews mit möglichen Betreuungspersonen geführt', '2024-05-01 12:10:52.831527', '', '2024-05-01 12:10:52.831546',
                    NULL, 1, 1139, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2258, 'Noch nicht vorangekommen.', '2024-07-15 06:52:06.75086', '', '2024-07-15 06:52:06.750862',
                    NULL, 1, 1333, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2111, '- am 6.6. wurde der Puzzle Lunch mit AWS durchgeführt.
    - das Account Alignment mit GCP findet am 13.6. statt.
    - weitere Leads sind in der Zwischenzeit nicht dazu gekommen. Wir verbleiben bei zwei Opportunities.',
                    '2024-06-13 06:26:33.725664', '', '2024-06-13 06:26:33.725667', NULL, 1, 1176, 0, 'ordinal', 'FAIL',
                    1);
            INSERT INTO okr_pitc.check_in
            VALUES (2256, 'Erfolgreiches Meeting mit /mid, gemeinsames Verständnis erreicht.',
                    '2024-07-15 06:44:12.603848',
                    'Mark trägt mal Fragen für eine Auslegeordnung bei den Kunden zusammen',
                    '2024-07-15 06:44:12.60385', NULL, 1, 1340, 9, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2055, '', '2024-06-04 07:16:03.254714', '', '2024-06-04 07:16:03.25472', 10, 1, 1180, 7, 'metric',
                    NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2147, 'Anpassung Confidence Level', '2024-06-17 15:49:57.855145', '', '2024-06-17 15:49:57.855147',
                    -0.25, 1, 1136, 0, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2162, 'Threatmodel ist nach STRIDE definiert, wir haben da ein paar Sachen zusammengetragen.',
                    '2024-06-18 06:57:33.766093', '', '2024-06-18 06:57:33.766096', NULL, 1, 1186, 7, 'ordinal',
                    'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1870, '- mündl. Zusage für Betreuungsperson.
    - Abstimmung mit UniLu für BBT Kundenprojekt
    - Betreuung im Q1 noch in Abklärung (Java & Basislehrjahr)', '2024-05-08 13:57:44.992228', '',
                    '2024-05-08 13:58:04.246167', NULL, 1, 1139, 6, 'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1753, '- Aussicht auf Kundeneinsatz für 4. Lehrjahr Lehrling bei BLS. Wird sich im Mai klären
    - Aussicht auf BBT Entwicklungsprojekt bei UniLu', '2024-04-24 06:46:33.759972', '', '2024-04-24 06:46:33.759979', NULL,
                    1, 1139, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1996, '', '2024-05-27 08:25:30.039261', '', '2024-05-27 08:25:30.039264', NULL, 1, 1186, 7,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1635, 'Arbeiten noch nicht gestartet.
    Bisher gibt es eine Analyse von P32/P14 durch MarCom.

    Arbeitsstand Analyse: https://codimd.puzzle.ch/ZdHODTltQUSQPiu7AwR9EA', '2024-04-04 06:13:52.940812',
                    'Abstimmung mit den relevanten Stellen', '2024-04-04 11:35:52.329911', NULL, 1, 1131, 3, 'ordinal',
                    'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1997, 'Leider keine Referenz.', '2024-05-27 08:25:56.23741', '', '2024-05-27 08:25:56.237413', NULL,
                    1, 1205, 9, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2013, '', '2024-05-28 08:41:15.008605', '- der Entscheid für die neuen Marktopportunitäten wird erst im Monthly Juni erfolgen.
    - die Moals (Mittelfristsicht) sind erarbeitet. Offen sind noch letzten quantitativen Ziele.',
                    '2024-05-28 09:10:25.191161', NULL, 1, 1132, 7, 'ordinal', 'FAIL', 3);
            INSERT INTO okr_pitc.check_in
            VALUES (2181, '- Vorbereitungsarbeiten KR Q1 im Gange
    - Treffen mit TD Synnex
    - Abklärungen Sponsoring und/oder Teilnahme AWS Summit
    - Arbeiten Abschluss AWS Partnerschaft', '2024-06-26 08:16:24.919815', '', '2024-06-26 08:16:24.919835', NULL, 1, 1250,
                    5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2189, 'Die erste Messung erfolgt mit dem Juli-Monatsabschluss im August.',
                    '2024-06-27 07:41:39.682035', '', '2024-06-27 07:41:39.682042', 0, 1, 1253, 7, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2257,
                    'Phil hat bei sich einen PoC aufgebaut und peilt nun an, das im Staging aufzubauen. Damit können wir dann effizient planen.',
                    '2024-07-15 06:51:45.153025', '', '2024-07-15 06:51:45.153028', NULL, 1, 1341, 7, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1910, '- Die Verteilung der MO in der GL ist erfolgt
    - Für die Mittelfristsicht der Strategie sollen Moals (mid-term goals) verwendet werden (Info und Entscheid am Monthly vom 27.05.)
    - die Inhalte der Moals werden aktuell ausgearbeitet', '2024-05-15 11:20:18.576311', '', '2024-05-15 11:20:18.576317',
                    NULL, 1, 1132, 8, 'ordinal', 'FAIL', 2);
            INSERT INTO okr_pitc.check_in
            VALUES (58, 'Entwurf steht, Feedbackrunde', '2023-07-05 08:17:54.963221', '', '2023-07-04 22:00:00', 0.5, 1,
                    107, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1957, 'unverändert', '2024-05-22 12:17:32.588671', '', '2024-05-22 12:17:32.588674', -0.25, 1, 1136,
                    3, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2249, '- AWS Opportunities, welche wir online einreichen werden oder bereits gemacht haben: Brixel, Helvetia. Für den Partner-Level fehlt uns noch eine. Wir prüfen eine interne Referenz.
    - Wir haben AWS Summit Zürich Sponsoring geprüft. Es ist mit TCHF 14 zu teuer. Wir werden als Teilnehmer dabei sein.
    - Für den Vermarktungsplan starten wir mit einem Codi werden auf Basis von weiter daran arbeiten.
    - die Mate-Video Planung wird in Angriff genommen.', '2024-07-10 19:41:06.33401', '', '2024-07-10 19:41:06.334012',
                    NULL, 1, 1250, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2046,
                    'Wir haben entschieden, die Kommunikation nur mündlich am Puzzle Workshop zu machen. Dafür zu einem Open Space für Kritik und Input einzuladen',
                    '2024-06-03 14:37:34.997244', '', '2024-06-03 14:37:34.997251', 0, 1, 1137, 0, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (383, 'Happiness Umfrage in GL durchgeführt.', '2023-09-18 15:46:00.92083', '',
                    '2023-09-17 22:00:00', 0.8, 1, 41, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1951, 'N/A', '2024-05-22 06:52:59.55829', 'Gemäss KR', '2024-05-22 06:52:59.558294', NULL, 1, 1224,
                    10, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2031, 'Mai sieht mit 65% wieder schlechter aus', '2024-06-02 12:57:43.261686',
                    'Wert überdenken. Massnahmen disktuieren, z.B. begrenzte Aktivitäten zu Ausbildung und Sales und diese über die Monate verteilen (nicht wirklich ein griffiger Ansatz)',
                    '2024-06-02 12:57:43.261694', NULL, 1, 1225, 0, 'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1948, 'N/A', '2024-05-22 06:50:38.884707', 'siehe Action Plan', '2024-05-22 06:50:38.884712', 0, 1,
                    1199, 0, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2303, 'Mit Innoarchitects Vorgehen und Tools besprochen', '2024-07-22 15:26:39.831665', '',
                    '2024-07-22 15:26:39.831668', NULL, 1, 1263, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1959, 'Draft erstellt, wir sind ready fürs interne Audit.', '2024-05-23 08:07:30.378125', '',
                    '2024-05-23 08:07:30.378128', NULL, 1, 1129, 8, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1946,
                    'Präsi Erkenntnisse und Empfehlung in Teammeeting 14.5.2024 zwecks Kenntnisnahme und Entscheids zur Weiterwarbei',
                    '2024-05-22 06:49:23.79964',
                    'Erarbeiten der Massnahmen. Weiterarbeit Hand in Hand mit KR ".... Arbeit im Team.... 3 Fokusthemen..."',
                    '2024-05-22 06:49:23.799644', NULL, 1, 1196, 10, 'ordinal', 'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1950, 'Übersicht erstellt, in Teammeeting besprochen, in Weekly integriert',
                    '2024-05-22 06:52:37.199279',
                    'Events: Anwenden als fixer Bestandteil im Weekly / Sponsoring: T.B.D',
                    '2024-05-22 06:52:37.199283', NULL, 1, 1221, 10, 'ordinal', 'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1780, '', '2024-04-29 06:15:36.573589', '', '2024-05-02 08:11:43.07251', 2998179, 1, 1134, 5,
                    'metric', NULL, 7);
            INSERT INTO okr_pitc.check_in
            VALUES (373, 'N/A', '2023-09-18 13:47:19.60709',
                    'Übernahme Key Result in Division OKRs GJ23/24, Q2 (nächster Zyklus)', '2023-09-17 22:00:00', 0, 1,
                    94, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (375, 'Blog-Artikel Lit (Moser-Baer) erstellt, BKD Referenzstory publiziert',
                    '2023-09-18 13:51:46.282099',
                    'Pendente Massnahmen in nächsten Zyklus übernehmen: Wie wir arbeiten -> "Arbeit im Team" und Dienstleistungsangebot -> "Ambitionen" sind skitzziert im Wiki dokumentiert: https://wiki.puzzle.ch/Puzzle/DevTre ',
                    '2023-09-17 22:00:00', 0.7, 1, 89, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2028,
                    'Internes Audit erfolgreich absolviert, ISO14001 in ISO9001 vollständig integriert, wir sind ready für die Zertifizierung.',
                    '2024-05-31 15:26:26.996174', '', '2024-05-31 15:26:26.99618', NULL, 1, 1129, 10, 'ordinal',
                    'STRETCH', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2026, 'Die im Workshop 23.4.3034 definierten Fokusthemen zu "Arbeit im Team", finden sich im Manifest wieder. Die Fokusthemen sind: Ziele, Verantwortung und Beteiligung an Entscheiden
    "', '2024-05-31 09:38:41.344382', 'Weiterarbeit gemäss Action Plan', '2024-05-31 09:38:41.34439', 1, 1, 1199, 0,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1949, 'N/A', '2024-05-22 06:50:59.056498', 'Mit /ux, /mobility & /ruby abgleichen.',
                    '2024-05-22 06:50:59.056503', NULL, 1, 1204, 10, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1761,
                    'Workshop durchgeführt. 3 Fokusthemen definiert: 1. Verantwortung, 2. Ziele, 3. Beteiligung an Entscheiden / Arbeitsgruppe erstellt, die das Thema weiter vertieft',
                    '2024-04-26 15:47:17.677396',
                    'In Arbeitsgruppe erarbeiten, wie die drei Fokusthemen umgesetzt werden, inkl. Massnahmen und Messpunkte.',
                    '2024-04-26 15:47:17.677402', NULL, 1, 1194, 10, 'ordinal', 'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1762, 'Workshop durchgeführt, Arbeitsgruppe erstellt, die das Thema weiterverfolgt',
                    '2024-04-26 15:49:11.083901',
                    'In Arbeitsgruppe anhand bestehender SO-Frameworks/ -Methoden erarbeiten, wie wir die SO in /dev/tre in Guidelines verankern wollen',
                    '2024-04-26 15:49:11.083908', NULL, 1, 1196, 10, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1849, 'N/A', '2024-05-06 06:57:27.164475', 'Gemäss KR', '2024-05-06 06:57:27.164483', NULL, 1, 1224,
                    10, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1853, 'Keine Änderung zur Vorwoche (updates ab nächster Woche)', '2024-05-06 07:22:53.790333', '',
                    '2024-05-06 07:22:53.79034', 92424, 1, 1191, 6, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1919, 'Auf Puzzle Workshop geplant und mit Saraina vorbesprochen', '2024-05-16 08:33:34.602015', '',
                    '2024-05-16 08:33:34.602022', 0, 1, 1137, 5, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1965,
                    'Offerings für Post health care und Eportrisikoversicherung sowie Entscheid Studis Applikation von UniBe offen.',
                    '2024-05-27 06:05:55.714304', '', '2024-05-27 07:20:40.43502', 0, 1, 1144, 1, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1945,
                    'Präsi Erkenntnisse und Empfehlung in Teammeeting 14.5.2024 zwecks Kenntnisnahme und Entscheids zur Weiterwarbeit',
                    '2024-05-22 06:48:35.024008', 'Erarbeiten der Massnahmen', '2024-05-22 06:48:35.024013', NULL, 1,
                    1194, 10, 'ordinal', 'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1844,
                    'In Arbeitsgruppe die Fokusthemen "Ziele", "Verantwortung", "Beteiligung an Entscheiden" untersucht, besprochen und weitere Schritte definiert (https://codimd.puzzle.ch/d3-selbstorganisation-methoden#)',
                    '2024-05-06 06:42:27.044714',
                    'Präsi zwecks Kenntnisnahme und Entscheid im Teammeeting, erarbeiten der Massnahmen - Weiterarbeit Hand in Hand mit KR ".... Arbeit im Team.... 3 Fokusthemen..."',
                    '2024-05-06 06:42:27.044744', NULL, 1, 1196, 10, 'ordinal', 'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1766, 'Workshop durchgeführt & Thema besprochen, Übersicht noch nicht erstellt',
                    '2024-04-26 15:52:44.07951', '', '2024-04-26 15:52:44.079517', NULL, 1, 1221, 10, 'ordinal', 'FAIL',
                    1);
            INSERT INTO okr_pitc.check_in
            VALUES (1836, 'Die Stossrichtungen sind geplant auf die nächsten Quartale', '2024-05-02 15:40:55.970244',
                    '', '2024-05-02 15:40:55.970252', NULL, 1, 1147, 10, 'ordinal', 'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (387,
                    'haben nochmals eine Session gemacht, jetzt müssen wir alles noch ins Reine schreiben und finalisieren',
                    '2023-09-19 06:30:07.038977', 'Berti macht den finalen Wurf im nächsten Quartal',
                    '2023-09-18 22:00:00', 0.3, 1, 65, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1770,
                    'Inserat geschrieben aber noch nicht publiziert. Erstgespräch mit Spontanbewerber durchgeführt, welcher passen könnte',
                    '2024-04-26 15:55:59.081149', '', '2024-04-26 15:55:59.081155', NULL, 1, 1228, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1722, 'Seite aktualisiert (neue Themen), eine Individuelle Offerte gestellt (OpenGIS). ',
                    '2024-04-22 09:37:40.488625', '', '2024-04-22 09:37:40.488627', NULL, 1, 1215, 5, 'ordinal',
                    'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1892, '', '2024-05-13 07:09:31.404304', '', '2024-05-13 07:09:31.404311', 143, 1, 1214, 10,
                    'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1963, 'Es fehlt noch die Validierung mit (einem) externen Spezialisten. ',
                    '2024-05-27 06:03:36.446751', '', '2024-05-27 06:03:36.446756', NULL, 1, 1138, 5, 'ordinal',
                    'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1964, 'Die Bestellung für Iwan sollte nächstens kommen laut PO', '2024-05-27 06:04:29.114008', '',
                    '2024-05-27 06:04:29.114011', NULL, 1, 1140, 7, 'ordinal', 'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (306, 'keine Veränderung seit letztem Checkin', '2023-09-11 09:39:24.473084',
                    'Slot mit Simu am Freitag, 15.09.23 um die Strategie zu finalisieren', '2023-09-10 22:00:00', 0.3,
                    1, 65, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (285, 'Wert Juli 2023 - +12.6% höher als im Vormonat', '2023-09-07 12:46:18.459843',
                    'Auslastung der verrechenbaren Pensen hoch halten. Wird mit Projektabschlüssen und Eintritten nicht erreichbar sein.',
                    '2023-09-06 22:00:00', 84.8, 1, 74, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1962, '', '2024-05-27 06:01:31.906465', '', '2024-05-27 06:01:31.906469', 0, 1, 1137, 5, 'metric',
                    NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1891, 'kleine Steigerung - leichter Rückgang (vorab 100 pro Woche)', '2024-05-13 07:08:49.934359',
                    '', '2024-05-13 07:08:49.934367', 662, 1, 1212, 5, 'metric', NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1852,
                    'Mit Spontanberwerber gesprochen. Inserat noch nicht geschaltet. Habe mich (Mäge) entschieden, später auszuschreiben, damit die Bewerbende mit Mätthu Liechti besprochen und geprüft werden können',
                    '2024-05-06 07:00:24.49276', 'Ausschreiben', '2024-05-06 07:00:34.93769', NULL, 1, 1228, 0,
                    'ordinal', 'FAIL', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1851, 'N/A', '2024-05-06 06:59:03.352468', 'Dran bleiben', '2024-05-06 06:59:03.352479', NULL, 1,
                    1227, 10, 'ordinal', 'COMMIT', 2);
            INSERT INTO okr_pitc.check_in
            VALUES (2305, 'Aufgrund von Ferienabwesenheiten noch keine Tätigkeiten', '2024-07-22 15:27:53.450101', '',
                    '2024-07-22 15:27:53.450103', NULL, 1, 1262, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (2307, 'Termin aufgesetzt mit Mäge', '2024-07-22 15:30:35.76184', '', '2024-07-22 15:30:35.761842',
                    NULL, 1, 1267, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1883, '', '2024-05-13 06:13:51.050279', 'Termin mit CTO aufgesetzt', '2024-05-13 06:13:51.050286',
                    NULL, 1, 1147, 10, 'ordinal', 'TARGET', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (372, 'Eintritt Clara, Arbeitsaufnahme bei SWOA', '2023-09-18 13:45:57.036041',
                    'N/A. Beri Status Quo', '2023-09-17 22:00:00', 0.85, 1, 92, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1930, '', '2024-05-21 11:34:09.998452', '', '2024-05-21 11:34:09.998456', 0, 1, 1144, 1, 'metric',
                    NULL, 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1723, 'Keine Änderung (Mail bei Simu)', '2024-04-22 09:38:20.287439', '',
                    '2024-04-22 09:38:20.287441', NULL, 1, 1218, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (397, 'N/A', '2023-09-23 15:40:01.955732', 'Dito letzter Check-In', '2023-09-22 22:00:00', 0.7, 1,
                    89, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (284, 'N/A', '2023-09-07 12:41:36.883236', 'Aktivitäten gemäss Key Results ', '2023-09-06 22:00:00',
                    0, 1, 87, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1893, 'keine Änderung', '2024-05-13 07:09:52.580267', '', '2024-05-13 07:09:52.580274', NULL, 1,
                    1218, 5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1767, 'N/A', '2024-04-26 15:53:16.020864', 'Gemäss KR', '2024-04-26 15:53:16.02087', NULL, 1, 1224,
                    5, 'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1740, '', '2024-04-23 09:27:33.038108', '', '2024-04-23 09:27:33.038126', NULL, 1, 1147, 5,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1739, '', '2024-04-23 09:27:05.172257', '', '2024-04-23 09:27:05.172273', NULL, 1, 1146, 5,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1850, 'April könnte 75% erreichen, der Quartalsdurchschnitt liegt aber tiefer',
                    '2024-05-06 06:58:25.821663', 'N/A', '2024-05-06 07:00:46.592448', NULL, 1, 1225, 0, 'ordinal',
                    'COMMIT', 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1779, 'in Arbeit', '2024-04-29 06:14:42.253346', '', '2024-04-29 06:14:42.253353', NULL, 1, 1129, 3,
                    'ordinal', 'FAIL', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (1939, 'Mit CTO abgestimmt', '2024-05-21 14:11:49.704266', '', '2024-05-21 14:11:49.70427', NULL, 1,
                    1147, 10, 'ordinal', 'STRETCH', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (374, 'Erstes Teammeeting nach neuer Struktur durchgeführt, Workshop vorbesprochen',
                    '2023-09-18 13:49:03.287967',
                    'Aktivitäten gemäss Key Results. Wird sich gemäss neu definiertem Zyklus und den Terminen ins nächste Quartal verschieben ',
                    '2023-09-17 22:00:00', 0.6, 1, 86, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (2306,
                    'ii für einen neuen Auftrag bei Mobi eingereicht. js bei SBB (ist aber nicht unbedingt durch neue Leads entstanden)',
                    '2024-07-22 15:30:00.715178', '', '2024-07-22 15:30:00.715181', NULL, 1, 1266, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1768, 'März ist schon besser als Februar. 75% aber noch nicht erreicht.',
                    '2024-04-26 15:53:55.480625', '', '2024-04-26 15:53:55.480643', NULL, 1, 1225, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (1724,
                    'Abgleich mit Lüku - Nextcloud Collections wird zum ausprobieren zur Verfügung gestellt, alternative Github-Wiki zu prüfen, Fallback Gitlab Wiki',
                    '2024-04-22 09:40:23.177101', '', '2024-04-22 09:40:23.177103', NULL, 1, 1223, 5, 'ordinal', 'FAIL',
                    0);
            INSERT INTO okr_pitc.check_in
            VALUES (2027, 'N/A', '2024-05-31 09:59:40.051622',
                    'Mit /ux, /mobility & /ruby abgleichen. An Markom und im LST kommunizieren',
                    '2024-05-31 09:59:40.05163', NULL, 1, 1204, 0, 'ordinal', 'COMMIT', 0);
            INSERT INTO okr_pitc.check_in
            VALUES (260, 'Bei 8 Divisions eingeführt', '2023-08-28 13:54:17.816622', '', '2023-08-27 22:00:00', 0.7, 1,
                    41, 5, 'metric', NULL, 1);
            INSERT INTO okr_pitc.check_in
            VALUES (1000, '', '2024-07-24 06:01:48.909213', '', '2024-07-24 06:01:48.90922', NULL, 1002, 1149, 0,
                    'ordinal', 'COMMIT', 0);


            --
    -- Data for Name: completed; Type: TABLE DATA; Schema: okr_pitc; Owner: -
    --

            INSERT INTO okr_pitc.completed VALUES (1010, 1063, NULL, 0);
            INSERT INTO okr_pitc.completed VALUES (1012, 1062, NULL, 0);
            INSERT INTO okr_pitc.completed VALUES (1013, 1080, NULL, 0);
            INSERT INTO okr_pitc.completed VALUES (1014, 1055, NULL, 0);
            INSERT INTO okr_pitc.completed VALUES (1069, 42, NULL, 0);
            INSERT INTO okr_pitc.completed VALUES (1070, 43, NULL, 0);
            INSERT INTO okr_pitc.completed VALUES (1071, 44, NULL, 0);
            INSERT INTO okr_pitc.completed VALUES (1072, 1058, NULL, 0);
            INSERT INTO okr_pitc.completed
            VALUES (1075, 1057,
                    'Bin hin und her ob erreicht oder nicht, aber seien wir mal Positiv :-) Eine Steigerung kontnen wir feststellen ',
                    0);
            INSERT INTO okr_pitc.completed VALUES (1077, 1051, NULL, 0);
            INSERT INTO okr_pitc.completed VALUES (1079, 1050, NULL, 0);
            INSERT INTO okr_pitc.completed
            VALUES (1080, 1053, 'Erstes KR Vorgehen gewechselt. Die anderen KRs ein bisschen zu wenig ambitioniert.',
                    0);
            INSERT INTO okr_pitc.completed VALUES (1083, 1076, NULL, 0);
            INSERT INTO okr_pitc.completed VALUES (1084, 1078, NULL, 0);
            INSERT INTO okr_pitc.completed VALUES (1085, 1077, NULL, 0);
            INSERT INTO okr_pitc.completed VALUES (1087, 1081, NULL, 0);
            INSERT INTO okr_pitc.completed VALUES (1095, 1079, NULL, 0);
            INSERT INTO okr_pitc.completed VALUES (1096, 1082, NULL, 0);
            INSERT INTO okr_pitc.completed VALUES (1097, 1084, NULL, 0);
            INSERT INTO okr_pitc.completed VALUES (1099, 1054, NULL, 0);
            INSERT INTO okr_pitc.completed
            VALUES (1104, 1074,
                    'Wir hatten keine Zeit für Strategiethemen. Auch blieben einige Teamanliegen noch unadressiert.',
                    0);
            INSERT INTO okr_pitc.completed
            VALUES (1105, 1075,
                    'Zwei Bewerber haben ein Jobangebot von uns, beide sind aber noch hängig. Bei zwei Zusagen wäre Objective erreicht. Aus heutiger Sicht muss es als nicht erreicht taxiert werden.',
                    0);
            INSERT INTO okr_pitc.completed VALUES (1106, 1072, NULL, 0);
            INSERT INTO okr_pitc.completed VALUES (1107, 1073, 'Unsere Zusammenarbeit hat sich verbessert.', 0);
            INSERT INTO okr_pitc.completed VALUES (1108, 1071, NULL, 0);
            INSERT INTO okr_pitc.completed VALUES (1110, 1083, NULL, 0);

            RAISE NOTICE 'Finished successfully!';
        END IF;
    END
$$;


