--
-- PostgreSQL database dump
--

-- Dumped from database version 14.13
-- Dumped by pg_dump version 14.11 (Ubuntu 14.11-0ubuntu0.22.04.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE IF EXISTS "pitc-okr-test";
--
-- Name: pitc-okr-test; Type: DATABASE; Schema: -; Owner: okr-test
--

CREATE DATABASE "pitc-okr-test" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'en_US.UTF-8';


ALTER DATABASE "pitc-okr-test" OWNER TO "okr-test";

\connect -reuse-previous=on "dbname='pitc-okr-test'"

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: okr_pitc; Type: SCHEMA; Schema: -; Owner: okr-test
--

CREATE SCHEMA okr_pitc;


ALTER SCHEMA okr_pitc OWNER TO "okr-test";

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: action; Type: TABLE; Schema: okr_pitc; Owner: okr-test
--

CREATE TABLE okr_pitc.action (
    id bigint NOT NULL,
    version integer NOT NULL,
    action character varying(4096) NOT NULL,
    priority integer NOT NULL,
    is_checked boolean NOT NULL,
    key_result_id bigint NOT NULL
);


ALTER TABLE okr_pitc.action OWNER TO "okr-test";

--
-- Name: alignment; Type: TABLE; Schema: okr_pitc; Owner: okr-test
--

CREATE TABLE okr_pitc.alignment (
    id bigint NOT NULL,
    aligned_objective_id bigint NOT NULL,
    alignment_type character varying(255) NOT NULL,
    target_key_result_id bigint,
    target_objective_id bigint,
    version integer NOT NULL
);


ALTER TABLE okr_pitc.alignment OWNER TO "okr-test";

--
-- Name: key_result; Type: TABLE; Schema: okr_pitc; Owner: okr-test
--

CREATE TABLE okr_pitc.key_result (
    id bigint NOT NULL,
    baseline double precision,
    modified_on timestamp without time zone,
    description character varying(4096),
    stretch_goal double precision,
    title character varying(250),
    created_by_id bigint NOT NULL,
    objective_id bigint NOT NULL,
    owner_id bigint NOT NULL,
    unit text,
    key_result_type character varying(255),
    created_on timestamp without time zone NOT NULL,
    commit_zone character varying(400),
    target_zone character varying(400),
    stretch_zone character varying(400),
    version integer NOT NULL
);


ALTER TABLE okr_pitc.key_result OWNER TO "okr-test";

--
-- Name: objective; Type: TABLE; Schema: okr_pitc; Owner: okr-test
--

CREATE TABLE okr_pitc.objective (
    id bigint NOT NULL,
    modified_on timestamp without time zone NOT NULL,
    description character varying(4096) NOT NULL,
    title character varying(250) NOT NULL,
    created_by_id bigint NOT NULL,
    quarter_id bigint NOT NULL,
    team_id bigint NOT NULL,
    state text NOT NULL,
    modified_by_id bigint,
    created_on timestamp without time zone NOT NULL,
    version integer NOT NULL
);


ALTER TABLE okr_pitc.objective OWNER TO "okr-test";

--
-- Name: quarter; Type: TABLE; Schema: okr_pitc; Owner: okr-test
--

CREATE TABLE okr_pitc.quarter (
    id bigint NOT NULL,
    label character varying(255) NOT NULL,
    start_date date,
    end_date date
);


ALTER TABLE okr_pitc.quarter OWNER TO "okr-test";

--
-- Name: team; Type: TABLE; Schema: okr_pitc; Owner: okr-test
--

CREATE TABLE okr_pitc.team (
    id bigint NOT NULL,
    name character varying(250) NOT NULL,
    version integer NOT NULL
);


ALTER TABLE okr_pitc.team OWNER TO "okr-test";

--
-- Name: alignment_selection; Type: VIEW; Schema: okr_pitc; Owner: okr-test
--

CREATE VIEW okr_pitc.alignment_selection AS
 SELECT o.id AS objective_id,
    o.title AS objective_title,
    t.id AS team_id,
    t.name AS team_name,
    q.id AS quarter_id,
    q.label AS quarter_label,
    COALESCE(kr.id, ('-1'::integer)::bigint) AS key_result_id,
    kr.title AS key_result_title
   FROM (((okr_pitc.objective o
     LEFT JOIN okr_pitc.team t ON ((o.team_id = t.id)))
     LEFT JOIN okr_pitc.quarter q ON ((o.quarter_id = q.id)))
     LEFT JOIN okr_pitc.key_result kr ON ((o.id = kr.objective_id)));


ALTER TABLE okr_pitc.alignment_selection OWNER TO "okr-test";

--
-- Name: check_in; Type: TABLE; Schema: okr_pitc; Owner: okr-test
--

CREATE TABLE okr_pitc.check_in (
    id bigint NOT NULL,
    change_info character varying(4096),
    created_on timestamp without time zone NOT NULL,
    initiatives character varying(4096),
    modified_on timestamp without time zone,
    value_metric double precision,
    created_by_id bigint NOT NULL,
    key_result_id bigint NOT NULL,
    confidence integer,
    check_in_type character varying(255),
    zone text,
    version integer NOT NULL
);


ALTER TABLE okr_pitc.check_in OWNER TO "okr-test";

--
-- Name: completed; Type: TABLE; Schema: okr_pitc; Owner: okr-test
--

CREATE TABLE okr_pitc.completed (
    id bigint NOT NULL,
    objective_id bigint NOT NULL,
    comment character varying(4096),
    version integer NOT NULL
);


ALTER TABLE okr_pitc.completed OWNER TO "okr-test";

--
-- Name: flyway_schema_history; Type: TABLE; Schema: okr_pitc; Owner: okr-test
--

CREATE TABLE okr_pitc.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE okr_pitc.flyway_schema_history OWNER TO "okr-test";

--
-- Name: overview; Type: VIEW; Schema: okr_pitc; Owner: okr-test
--

CREATE VIEW okr_pitc.overview AS
 SELECT tq.team_id,
    tq.team_version,
    tq.name AS team_name,
    tq.quater_id AS quarter_id,
    tq.label AS quarter_label,
    COALESCE(o.id, ('-1'::integer)::bigint) AS objective_id,
    o.title AS objective_title,
    o.state AS objective_state,
    o.created_on AS objective_created_on,
    COALESCE(kr.id, ('-1'::integer)::bigint) AS key_result_id,
    kr.title AS key_result_title,
    kr.key_result_type,
    kr.unit,
    kr.baseline,
    kr.stretch_goal,
    kr.commit_zone,
    kr.target_zone,
    kr.stretch_zone,
    COALESCE(c.id, ('-1'::integer)::bigint) AS check_in_id,
    c.value_metric AS check_in_value,
    c.zone AS check_in_zone,
    c.confidence,
    c.created_on AS check_in_created_on
   FROM (((( SELECT t.id AS team_id,
            t.version AS team_version,
            t.name,
            q.id AS quater_id,
            q.label
           FROM okr_pitc.team t,
            okr_pitc.quarter q) tq
     LEFT JOIN okr_pitc.objective o ON (((tq.team_id = o.team_id) AND (tq.quater_id = o.quarter_id))))
     LEFT JOIN okr_pitc.key_result kr ON ((o.id = kr.objective_id)))
     LEFT JOIN okr_pitc.check_in c ON (((kr.id = c.key_result_id) AND (c.modified_on = ( SELECT max(cc.modified_on) AS max
           FROM okr_pitc.check_in cc
          WHERE (cc.key_result_id = c.key_result_id))))));


ALTER TABLE okr_pitc.overview OWNER TO "okr-test";

--
-- Name: person; Type: TABLE; Schema: okr_pitc; Owner: okr-test
--

CREATE TABLE okr_pitc.person (
    id bigint NOT NULL,
    email character varying(250) NOT NULL,
    firstname character varying(50) NOT NULL,
    lastname character varying(50) NOT NULL,
    version integer NOT NULL,
    is_okr_champion boolean DEFAULT false
);


ALTER TABLE okr_pitc.person OWNER TO "okr-test";

--
-- Name: person_team; Type: TABLE; Schema: okr_pitc; Owner: okr-test
--

CREATE TABLE okr_pitc.person_team (
    is_team_admin boolean DEFAULT false NOT NULL,
    version integer NOT NULL,
    id bigint NOT NULL,
    person_id bigint,
    team_id bigint
);


ALTER TABLE okr_pitc.person_team OWNER TO "okr-test";

--
-- Name: sequence_action; Type: SEQUENCE; Schema: okr_pitc; Owner: okr-test
--

CREATE SEQUENCE okr_pitc.sequence_action
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE okr_pitc.sequence_action OWNER TO "okr-test";

--
-- Name: sequence_alignment; Type: SEQUENCE; Schema: okr_pitc; Owner: okr-test
--

CREATE SEQUENCE okr_pitc.sequence_alignment
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE okr_pitc.sequence_alignment OWNER TO "okr-test";

--
-- Name: sequence_check_in; Type: SEQUENCE; Schema: okr_pitc; Owner: okr-test
--

CREATE SEQUENCE okr_pitc.sequence_check_in
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE okr_pitc.sequence_check_in OWNER TO "okr-test";

--
-- Name: sequence_completed; Type: SEQUENCE; Schema: okr_pitc; Owner: okr-test
--

CREATE SEQUENCE okr_pitc.sequence_completed
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE okr_pitc.sequence_completed OWNER TO "okr-test";

--
-- Name: sequence_key_result; Type: SEQUENCE; Schema: okr_pitc; Owner: okr-test
--

CREATE SEQUENCE okr_pitc.sequence_key_result
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE okr_pitc.sequence_key_result OWNER TO "okr-test";

--
-- Name: sequence_objective; Type: SEQUENCE; Schema: okr_pitc; Owner: okr-test
--

CREATE SEQUENCE okr_pitc.sequence_objective
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE okr_pitc.sequence_objective OWNER TO "okr-test";

--
-- Name: sequence_organisation; Type: SEQUENCE; Schema: okr_pitc; Owner: okr-test
--

CREATE SEQUENCE okr_pitc.sequence_organisation
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE okr_pitc.sequence_organisation OWNER TO "okr-test";

--
-- Name: sequence_person; Type: SEQUENCE; Schema: okr_pitc; Owner: okr-test
--

CREATE SEQUENCE okr_pitc.sequence_person
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE okr_pitc.sequence_person OWNER TO "okr-test";

--
-- Name: sequence_person_team; Type: SEQUENCE; Schema: okr_pitc; Owner: okr-test
--

CREATE SEQUENCE okr_pitc.sequence_person_team
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE okr_pitc.sequence_person_team OWNER TO "okr-test";

--
-- Name: sequence_quarter; Type: SEQUENCE; Schema: okr_pitc; Owner: okr-test
--

CREATE SEQUENCE okr_pitc.sequence_quarter
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE okr_pitc.sequence_quarter OWNER TO "okr-test";

--
-- Name: sequence_team; Type: SEQUENCE; Schema: okr_pitc; Owner: okr-test
--

CREATE SEQUENCE okr_pitc.sequence_team
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE okr_pitc.sequence_team OWNER TO "okr-test";

--
-- Data for Name: action; Type: TABLE DATA; Schema: okr_pitc; Owner: okr-test
--

INSERT INTO okr_pitc.action VALUES (1369, 1, 'LinkedIn (extern)', 8, false, 1461);
INSERT INTO okr_pitc.action VALUES (1370, 1, 'Google Ads (extern)', 9, false, 1461);
INSERT INTO okr_pitc.action VALUES (1371, 0, 'tbd', 10, false, 1461);
INSERT INTO okr_pitc.action VALUES (1388, 1, 'Puzzle News (intern)', 0, false, 1470);
INSERT INTO okr_pitc.action VALUES (1051, 1, 'Workshop', 0, true, 1098);
INSERT INTO okr_pitc.action VALUES (1046, 1, 'Plan erstellt', 0, true, 1080);
INSERT INTO okr_pitc.action VALUES (1019, 1, 'Alle Retros haben eine Rubrik Wohlbefinden', 1, false, 1007);
INSERT INTO okr_pitc.action VALUES (1015, 2, 'Fajita Zmittag', 0, true, 1006);
INSERT INTO okr_pitc.action VALUES (1018, 2, 'Retro planen', 0, true, 1007);
INSERT INTO okr_pitc.action VALUES (1017, 2, 'Programmierabend', 2, true, 1006);
INSERT INTO okr_pitc.action VALUES (1023, 1, '/sys', 0, false, 1045);
INSERT INTO okr_pitc.action VALUES (1024, 1, '/mid/container', 1, false, 1045);
INSERT INTO okr_pitc.action VALUES (1025, 1, '/mid/cicd', 2, false, 1045);
INSERT INTO okr_pitc.action VALUES (1027, 0, '/ruby', 4, false, 1045);
INSERT INTO okr_pitc.action VALUES (1028, 0, '/zh', 5, false, 1045);
INSERT INTO okr_pitc.action VALUES (1029, 0, '/security', 6, false, 1045);
INSERT INTO okr_pitc.action VALUES (1030, 0, '/mobility', 7, false, 1045);
INSERT INTO okr_pitc.action VALUES (1031, 0, '/hitobito', 8, false, 1045);
INSERT INTO okr_pitc.action VALUES (1032, 0, '/ux', 9, false, 1045);
INSERT INTO okr_pitc.action VALUES (1036, 1, 'Uebersicht Kundenaufträge Indiv erstellen', 0, false, 1070);
INSERT INTO okr_pitc.action VALUES (1037, 1, 'Analyse Wartungskomplexität der einzelnen Umgebungen aufnehmen', 1, false, 1070);
INSERT INTO okr_pitc.action VALUES (1043, 0, 'Aktivität im April (geplant)', 4, false, 1087);
INSERT INTO okr_pitc.action VALUES (1044, 1, 'OKR Draft mit Team reviewed', 1, false, 1080);
INSERT INTO okr_pitc.action VALUES (1045, 1, 'Erfolgreiches im Teammeeting ingetriert (nicht überziehen, wichtige Themen besprechochen, sozializing)', 2, false, 1080);
INSERT INTO okr_pitc.action VALUES (1050, 0, 'Umsetzen der Massnahmen', 3, false, 1079);
INSERT INTO okr_pitc.action VALUES (1052, 0, 'Massnahmeplan (Backlog)', 1, false, 1098);
INSERT INTO okr_pitc.action VALUES (1053, 0, 'Prinzipien', 2, false, 1098);
INSERT INTO okr_pitc.action VALUES (1054, 0, 'Strukturen', 3, false, 1098);
INSERT INTO okr_pitc.action VALUES (1055, 0, 'Bereichsmap /dev/tre', 0, false, 1102);
INSERT INTO okr_pitc.action VALUES (1033, 2, 'Members wählen eine gewünschte Zertifizierung aus', 0, true, 1062);
INSERT INTO okr_pitc.action VALUES (1040, 1, 'Aktivität im Januar', 1, true, 1087);
INSERT INTO okr_pitc.action VALUES (1077, 1, 'Sales muss Tool gut vermarkten', 2, false, 1122);
INSERT INTO okr_pitc.action VALUES (1069, 2, 'Jeder Entwickler hat SonarLint Plugin bei sich Installiert', 0, true, 1120);
INSERT INTO okr_pitc.action VALUES (1070, 2, ' Es Existiert ein Sonar Repo bei Puzzle Sonar', 1, true, 1120);
INSERT INTO okr_pitc.action VALUES (1071, 2, 'Bei Mergen von Branches wird zuerst ein Sonar-Check durchgeführt', 2, true, 1120);
INSERT INTO okr_pitc.action VALUES (1072, 2, 'Grundfunktionalität mittels Unittests sicherstellen', 0, true, 1121);
INSERT INTO okr_pitc.action VALUES (1073, 2, 'Zusammenspiel von mehreren Systemen mit Integrationstests Sicherstellen', 1, true, 1121);
INSERT INTO okr_pitc.action VALUES (1074, 2, 'Funktionalität des Tools mit E2E-Tests sichern', 2, true, 1121);
INSERT INTO okr_pitc.action VALUES (1075, 2, 'Tool entwickeln', 0, true, 1122);
INSERT INTO okr_pitc.action VALUES (1076, 2, 'Tool Design überarbeiten', 1, true, 1122);
INSERT INTO okr_pitc.action VALUES (1090, 1, 'Preisstruktur ist erstellt ', 0, false, 1153);
INSERT INTO okr_pitc.action VALUES (1056, 1, 'Sills Claudia Asti', 1, true, 1102);
INSERT INTO okr_pitc.action VALUES (1057, 1, 'Skills Max Burri', 2, true, 1102);
INSERT INTO okr_pitc.action VALUES (1058, 1, 'Skills Christoph Lüthi', 3, true, 1102);
INSERT INTO okr_pitc.action VALUES (1063, 1, 'Skills Jan Walker', 8, true, 1102);
INSERT INTO okr_pitc.action VALUES (1064, 1, 'Skills Matthias Liechti', 9, true, 1102);
INSERT INTO okr_pitc.action VALUES (1039, 1, 'Auslegeorndung erstellt', 0, true, 1087);
INSERT INTO okr_pitc.action VALUES (1034, 2, 'Members besuchen den entsprechenden Kurs', 1, true, 1062);
INSERT INTO okr_pitc.action VALUES (1047, 1, 'Die MAG sind erfolgreich durchgeführt', 0, true, 1079);
INSERT INTO okr_pitc.action VALUES (1048, 1, 'Auswertung und konkrete Massnahmen Katalog erstellt', 1, true, 1079);
INSERT INTO okr_pitc.action VALUES (1041, 1, 'Aktivität im Februar', 2, true, 1087);
INSERT INTO okr_pitc.action VALUES (1016, 2, 'Spielabend', 1, true, 1006);
INSERT INTO okr_pitc.action VALUES (1059, 1, 'Skills Stephan Girod', 4, true, 1102);
INSERT INTO okr_pitc.action VALUES (1060, 1, 'Skills Mathis Hofer', 5, true, 1102);
INSERT INTO okr_pitc.action VALUES (1061, 1, 'Skills Lukas Nydegger', 6, true, 1102);
INSERT INTO okr_pitc.action VALUES (1062, 1, 'Skills Clara Lemm Llorente', 7, true, 1102);
INSERT INTO okr_pitc.action VALUES (1065, 1, 'Skills Simon Bühlmann', 10, true, 1102);
INSERT INTO okr_pitc.action VALUES (1042, 1, 'Aktivität im März', 3, true, 1087);
INSERT INTO okr_pitc.action VALUES (1049, 1, 'Bewertung der Massnahmen', 2, true, 1079);
INSERT INTO okr_pitc.action VALUES (1066, 1, 'Skills Daniel Tschan', 11, true, 1102);
INSERT INTO okr_pitc.action VALUES (1026, 1, '/devtre', 3, true, 1045);
INSERT INTO okr_pitc.action VALUES (1038, 1, 'Wartungsplanung für Q1 einhalten', 0, true, 1091);
INSERT INTO okr_pitc.action VALUES (1114, 1, 'Mit Arbeit an digitalen Lösungen prüfen', 2, false, 1199);
INSERT INTO okr_pitc.action VALUES (1035, 2, 'Members legen die entsprechende Prüfung ab', 2, true, 1062);
INSERT INTO okr_pitc.action VALUES (1092, 1, 'Vision ist verabschiedet', 0, false, 1164);
INSERT INTO okr_pitc.action VALUES (1093, 1, 'Definition von Massnahmen', 1, false, 1164);
INSERT INTO okr_pitc.action VALUES (1094, 1, 'Aktivitäten sind geplant', 2, false, 1164);
INSERT INTO okr_pitc.action VALUES (1096, 0, 'Wartbarkeit der Applikationen steigern', 4, false, 1164);
INSERT INTO okr_pitc.action VALUES (1097, 0, 'OpenShift Migration durchgeführt', 5, false, 1164);
INSERT INTO okr_pitc.action VALUES (1115, 2, 'Abstimmung mit ar und mw (25.3. geplant)', 0, true, 1144);
INSERT INTO okr_pitc.action VALUES (1086, 1, 'Ableiten des Verteilschlüssel', 1, false, 1150);
INSERT INTO okr_pitc.action VALUES (1087, 1, 'Analyse der Wartung der letzten 3 Jahre', 2, false, 1150);
INSERT INTO okr_pitc.action VALUES (1088, 2, 'Inhalt Betrieb/Wartung und Support', 3, false, 1150);
INSERT INTO okr_pitc.action VALUES (1110, 1, 'Event organisieren', 2, false, 1197);
INSERT INTO okr_pitc.action VALUES (1111, 1, 'Mit Guidelines Selbstorganisation prüfen', 0, false, 1199);
INSERT INTO okr_pitc.action VALUES (1089, 1, 'Wording und Vorgehen für Kundenkommunikation', 4, false, 1150);
INSERT INTO okr_pitc.action VALUES (1091, 3, 'Analyse von Techboard 5 - 7PT', 0, false, 1155);
INSERT INTO okr_pitc.action VALUES (1106, 2, 'Workshop, 23.4.2024', 0, true, 1194);
INSERT INTO okr_pitc.action VALUES (1085, 2, 'Analyse der Umgebungs-Komplexität', 0, true, 1150);
INSERT INTO okr_pitc.action VALUES (1107, 2, 'Workshop, 23.04.2024', 0, true, 1196);
INSERT INTO okr_pitc.action VALUES (1095, 1, 'Planung der Wartung', 3, true, 1164);
INSERT INTO okr_pitc.action VALUES (1101, 1, 'Open Space Leadership Monthly oder Kafi-Meeting für Members', 3, true, 1188);
INSERT INTO okr_pitc.action VALUES (1098, 1, 'Imagine-Blogpost inkl. Social Media', 0, true, 1188);
INSERT INTO okr_pitc.action VALUES (1100, 1, 'Direct Mailing an Email-Kontakte', 2, true, 1188);
INSERT INTO okr_pitc.action VALUES (1112, 2, 'Mit Fokusthemen Teamarbeit prüfen', 1, true, 1199);
INSERT INTO okr_pitc.action VALUES (1099, 1, 'Sponsoringauftritt KCD 13.06.2024', 1, true, 1188);
INSERT INTO okr_pitc.action VALUES (1108, 2, 'Ideen sammeln', 0, true, 1197);
INSERT INTO okr_pitc.action VALUES (1109, 2, 'Termin finden', 1, true, 1197);
INSERT INTO okr_pitc.action VALUES (1372, 0, 'Vorgehen und Roadmap festlegen', 0, false, 1455);
INSERT INTO okr_pitc.action VALUES (1117, 1, 'Im Leadership-Team kommunizieren', 3, false, 1199);
INSERT INTO okr_pitc.action VALUES (1118, 1, 'Erklärungen für die Punkte hinter dem Manifest erstellt', 4, false, 1199);
INSERT INTO okr_pitc.action VALUES (1123, 0, 'Review Saraina', 2, false, 1137);
INSERT INTO okr_pitc.action VALUES (1124, 0, 'Publish', 3, false, 1137);
INSERT INTO okr_pitc.action VALUES (1125, 0, 'Aktiv auf negativ Feedbacker zugehen und Meinung/Tipps abholen', 4, false, 1137);
INSERT INTO okr_pitc.action VALUES (1142, 1, 'Neues regelmässiges Abstimmungsformat /mobility - Sales Team einführen', 1, true, 1144);
INSERT INTO okr_pitc.action VALUES (1134, 1, 'Draft mit Nathi und Jöu besprechen (2.5. eingeplant)', 1, true, 1147);
INSERT INTO okr_pitc.action VALUES (1126, 1, 'Mit Jöu Umsetzungsplanung machen (Termin 25.4.)', 0, true, 1141);
INSERT INTO okr_pitc.action VALUES (1127, 1, 'Mit Jöu Zwischencheck machen', 1, true, 1141);
INSERT INTO okr_pitc.action VALUES (1122, 1, 'Draften', 1, true, 1137);
INSERT INTO okr_pitc.action VALUES (1140, 1, 'Validierung mit Sales extenr', 6, false, 1138);
INSERT INTO okr_pitc.action VALUES (1145, 1, 'Interne Projekte planen', 2, false, 1232);
INSERT INTO okr_pitc.action VALUES (1148, 0, 'Weiterbildungsangebote prüfen & an Teampool kommunizieren', 2, false, 1135);
INSERT INTO okr_pitc.action VALUES (1149, 0, 'Weiterbildung verbindlich planen', 3, false, 1135);
INSERT INTO okr_pitc.action VALUES (1150, 0, 'Bewerbungsgespräche führen & neue Members anstellen', 4, false, 1135);
INSERT INTO okr_pitc.action VALUES (1151, 0, 'Angebot auf Webseite mit Mobcoeur und Markom abstimmen', 5, false, 1135);
INSERT INTO okr_pitc.action VALUES (1152, 0, 'Markom publiziert Angebot auf Webseite', 6, false, 1135);
INSERT INTO okr_pitc.action VALUES (1158, 0, 'Ressourcen für Lehrlingsprojekt sind freigegeben und geplant', 5, false, 1139);
INSERT INTO okr_pitc.action VALUES (1159, 0, 'Kundenprojekt für 4-Lehrjahr-Lernender ist gewonnen und Lernender eingeplant', 6, false, 1139);
INSERT INTO okr_pitc.action VALUES (1164, 0, 'Diskussion Topics', 4, false, 1223);
INSERT INTO okr_pitc.action VALUES (1146, 1, 'Stelleninserat aufschalten', 0, true, 1135);
INSERT INTO okr_pitc.action VALUES (1153, 1, 'Stelleninserat für Betreuungsperson (Professional Java Dev) aufgeschaltet', 0, true, 1139);
INSERT INTO okr_pitc.action VALUES (1166, 0, 'Test', 0, false, 1236);
INSERT INTO okr_pitc.action VALUES (1167, 1, 'Bello', 1, false, 1236);
INSERT INTO okr_pitc.action VALUES (1168, 1, 'Findus ', 2, false, 1236);
INSERT INTO okr_pitc.action VALUES (1165, 1, 'Marktvalidierung mit CTO und FI sowie Members Platzierung am 23.4.', 0, true, 1140);
INSERT INTO okr_pitc.action VALUES (1141, 1, 'Vorgehen und Terminplanung mit CTO (Termin 27.3.)', 0, true, 1138);
INSERT INTO okr_pitc.action VALUES (1135, 2, 'Shortliste Technologie mit potentiellen Members erstellen', 1, true, 1138);
INSERT INTO okr_pitc.action VALUES (1121, 1, 'Abstimmung mit Saraine how to', 0, true, 1137);
INSERT INTO okr_pitc.action VALUES (1136, 2, 'Durchsprache mit CTO und jbl, nb', 2, true, 1138);
INSERT INTO okr_pitc.action VALUES (1131, 1, 'Mit Jölu weitere Tasks bestimmen', 2, true, 1141);
INSERT INTO okr_pitc.action VALUES (1119, 2, 'Workshop, 23.04.2024', 0, true, 1204);
INSERT INTO okr_pitc.action VALUES (1120, 2, 'Workshop, 23.04.2024', 0, true, 1221);
INSERT INTO okr_pitc.action VALUES (1160, 2, 'Abgleich Dänu (Nextcloud Collections) - (ehem 23.04.2024) - zu verschieben auf Freigabe Collections', 0, true, 1223);
INSERT INTO okr_pitc.action VALUES (1138, 3, 'Validierung mit Techboard', 3, true, 1138);
INSERT INTO okr_pitc.action VALUES (1174, 3, 'Kuchen ', 0, true, 1239);
INSERT INTO okr_pitc.action VALUES (1172, 3, 'Miaaaaau TV', 1, false, 1239);
INSERT INTO okr_pitc.action VALUES (1105, 2, '1 Open Space Leadership Monthly oder Kaffi-Meeting für Members', 3, true, 1193);
INSERT INTO okr_pitc.action VALUES (1128, 1, 'Draft erarbeiten ', 0, true, 1146);
INSERT INTO okr_pitc.action VALUES (1129, 1, 'Draft besprechen mit jbl, nb (25.4. gebucht)', 1, true, 1146);
INSERT INTO okr_pitc.action VALUES (1130, 1, 'Finalisieren und implementieren', 2, true, 1146);
INSERT INTO okr_pitc.action VALUES (1133, 1, 'Draft erarbeiten', 0, true, 1147);
INSERT INTO okr_pitc.action VALUES (1176, 0, 'Iwan bei Mobiliar einsetzen', 2, false, 1140);
INSERT INTO okr_pitc.action VALUES (1132, 1, 'Stand Tasks tracken', 3, true, 1141);
INSERT INTO okr_pitc.action VALUES (1171, 1, 'Neues Kanban Board implementiert', 2, true, 1144);
INSERT INTO okr_pitc.action VALUES (1161, 1, 'Danach Entscheid Tool', 1, true, 1223);
INSERT INTO okr_pitc.action VALUES (1154, 1, 'Interviews mit möglichen Kandidaten durchgeführt', 1, true, 1139);
INSERT INTO okr_pitc.action VALUES (1156, 1, 'Lehrlingsprojekt mit BBT und LST abgesprochen und bestimmt', 3, true, 1139);
INSERT INTO okr_pitc.action VALUES (1169, 1, 'Schulung mit 3-4 /mobility Members', 1, true, 1140);
INSERT INTO okr_pitc.action VALUES (1170, 2, 'Angebots/Marketing Story erstellen', 3, true, 1140);
INSERT INTO okr_pitc.action VALUES (1102, 3, '1 Imagine-Blogpost inkl. Social Media ', 0, true, 1193);
INSERT INTO okr_pitc.action VALUES (1175, 1, 'Mit CTO Roadmap abstimmen', 2, true, 1147);
INSERT INTO okr_pitc.action VALUES (1155, 1, 'Betreuungsperson angestellt', 2, true, 1139);
INSERT INTO okr_pitc.action VALUES (1143, 2, 'Ferienwünsche bei allen Members abgeholt', 0, true, 1232);
INSERT INTO okr_pitc.action VALUES (1104, 3, '1 Direct Mailing an Email-Kontakte ', 2, true, 1193);
INSERT INTO okr_pitc.action VALUES (1157, 1, 'Verantwortlichkeiten für Lehrlingsprojekt definiert', 4, true, 1139);
INSERT INTO okr_pitc.action VALUES (1144, 2, 'Ferien im Ptime und Kalender', 1, true, 1232);
INSERT INTO okr_pitc.action VALUES (1137, 3, 'Validierung mit Members', 4, true, 1138);
INSERT INTO okr_pitc.action VALUES (1139, 2, 'Validierung mit Sales intern', 5, true, 1138);
INSERT INTO okr_pitc.action VALUES (1147, 1, 'Interner Teampool bestimmen', 1, true, 1135);
INSERT INTO okr_pitc.action VALUES (1179, 1, 'Massnahmen & Roadmap für weiteres Vorgehen definiert', 2, false, 1262);
INSERT INTO okr_pitc.action VALUES (1180, 0, '/mobility Konzept für Einsatzplanung & Kundenkommunikation', 3, false, 1262);
INSERT INTO okr_pitc.action VALUES (1181, 0, 'Beratungsangebot mit potenziellen Kunden erstellt', 4, false, 1262);
INSERT INTO okr_pitc.action VALUES (1182, 0, 'Kundenfeedback zu Beratungsangebot eingeholt', 5, false, 1262);
INSERT INTO okr_pitc.action VALUES (1183, 1, 'Klärung Zusammenarbeit /sales & /mobility mit CSO', 0, false, 1266);
INSERT INTO okr_pitc.action VALUES (1103, 3, 'Sponsoringauftritt KCD 13.06.2024 ', 1, true, 1193);
INSERT INTO okr_pitc.action VALUES (1162, 1, 'Erstellen Grundstruktur', 2, true, 1223);
INSERT INTO okr_pitc.action VALUES (1163, 1, 'Briefing / Vorstellung Members', 3, true, 1223);
INSERT INTO okr_pitc.action VALUES (1186, 0, 'Definition von Massnahmen', 1, false, 1298);
INSERT INTO okr_pitc.action VALUES (1187, 0, 'Aktivitäten sind geplant', 2, false, 1298);
INSERT INTO okr_pitc.action VALUES (1189, 0, 'Wartbarkeit der Applikationen steigern', 4, false, 1298);
INSERT INTO okr_pitc.action VALUES (1190, 0, 'OpenShift Migration durchgeführt', 5, false, 1298);
INSERT INTO okr_pitc.action VALUES (1192, 0, 'Zwischengespräche durchgeführt', 1, false, 1300);
INSERT INTO okr_pitc.action VALUES (1185, 1, 'Vision ist verabschiedet', 0, true, 1298);
INSERT INTO okr_pitc.action VALUES (1191, 1, 'Zwischengespräche geplant', 0, true, 1300);
INSERT INTO okr_pitc.action VALUES (1184, 2, 'Im Thema "Delivery Pipeline / Kubernetes" sind Kooperationspotentiale mit CI/CD eruriert.', 3, false, 1275);
INSERT INTO okr_pitc.action VALUES (1193, 1, 'Membersplanung (Ruby Charta) ist aktualisiert', 0, true, 1302);
INSERT INTO okr_pitc.action VALUES (1177, 2, 'Kickoff', 0, true, 1262);
INSERT INTO okr_pitc.action VALUES (1194, 3, 'Vakanzen identifiziert', 1, true, 1302);
INSERT INTO okr_pitc.action VALUES (1188, 1, 'Planung der Wartung', 3, true, 1298);
INSERT INTO okr_pitc.action VALUES (1178, 4, 'Angebotsentwurf erstellt', 1, true, 1262);
INSERT INTO okr_pitc.action VALUES (1196, 1, 'Der Go-to Market Plan steht', 0, false, 1311);
INSERT INTO okr_pitc.action VALUES (1197, 1, 'Landing-Page “Platform Engineering” auf neuer Webseite ist live', 2, false, 1311);
INSERT INTO okr_pitc.action VALUES (1291, 1, 'Challenging durch sfa und ar', 4, true, 1261);
INSERT INTO okr_pitc.action VALUES (1199, 1, 'Plattformengineering Chapter Schweiz wurde gegründet', 4, false, 1311);
INSERT INTO okr_pitc.action VALUES (1285, 1, 'mit ar Vorgehen planen', 0, true, 1277);
INSERT INTO okr_pitc.action VALUES (1236, 2, 'Kundenumfrage ausgewertet und 3 potenzielle Massnahmen abgeleitet.', 0, true, 1343);
INSERT INTO okr_pitc.action VALUES (1206, 0, ' Analyse der Wartung der letzten 3 Jahre', 2, false, 1270);
INSERT INTO okr_pitc.action VALUES (1207, 0, 'Inhalt Betrieb/Wartung und Support', 3, false, 1270);
INSERT INTO okr_pitc.action VALUES (1208, 0, 'Wording und Vorgehen für Kundenkommunikation', 4, false, 1270);
INSERT INTO okr_pitc.action VALUES (1237, 2, 'Go-to-Market Plan finalisiert.', 1, true, 1343);
INSERT INTO okr_pitc.action VALUES (1209, 0, 'Sales Kampagne gestrartet auf Basis der marktanalyse https://www.srf.ch/kultur/gesellschaft-religion/schweizer-sportverbaende-immer-weniger-vereine-aber-immer-mehr-freiwilligenarbeit', 0, false, 1285);
INSERT INTO okr_pitc.action VALUES (1374, 0, 'P31 validieren und marktorientierter aufstellen', 2, false, 1455);
INSERT INTO okr_pitc.action VALUES (1375, 0, 'Entwicklung des Angebotsportfolios', 3, false, 1455);
INSERT INTO okr_pitc.action VALUES (1376, 1, 'Entwicklung der wichtigsten Angebote und Kernaufgaben / Massnahmen', 4, false, 1455);
INSERT INTO okr_pitc.action VALUES (1373, 1, 'Modelle bei Leadership Team / Bereichen einführen, wo notwendig ausbilden', 1, false, 1455);
INSERT INTO okr_pitc.action VALUES (1390, 1, 'Weihnachtsinfoanlass (intern)', 2, false, 1470);
INSERT INTO okr_pitc.action VALUES (1391, 0, 'Puzzle Blogpost (extern)', 3, false, 1470);
INSERT INTO okr_pitc.action VALUES (1392, 0, 'Business Lunch (extern)', 4, false, 1470);
INSERT INTO okr_pitc.action VALUES (1393, 0, 'Mate mit (extern)', 5, false, 1470);
INSERT INTO okr_pitc.action VALUES (1394, 0, 'Medienmitteilung (extern)', 6, false, 1470);
INSERT INTO okr_pitc.action VALUES (1247, 0, 'Aktionsplan', 2, false, 1302);
INSERT INTO okr_pitc.action VALUES (1195, 2, 'Kundenumfrage ist ausgewertet und 3 potenzielle Massnahmen sind daraus abgeleitet', 1, true, 1311);
INSERT INTO okr_pitc.action VALUES (1240, 1, 'Platform Engineering Chapter Schweiz gegründet.', 4, false, 1343);
INSERT INTO okr_pitc.action VALUES (1277, 1, 'Deep Dive Termin mit adi, anna und Chrigu aufsetzen', 0, true, 1275);
INSERT INTO okr_pitc.action VALUES (1276, 1, 'mit Ci/CD neuen Einsatz geplant', 4, false, 1263);
INSERT INTO okr_pitc.action VALUES (1248, 0, 'Verifikation der Ziele', 3, false, 1302);
INSERT INTO okr_pitc.action VALUES (1249, 0, 'Vorlage Entwicklungsplan', 2, false, 1300);
INSERT INTO okr_pitc.action VALUES (1267, 2, 'Konzept erstellen', 2, true, 1261);
INSERT INTO okr_pitc.action VALUES (1244, 2, 'Neuer Deal >TCHF100 gewonnen.', 8, false, 1343);
INSERT INTO okr_pitc.action VALUES (1282, 1, 'Ende Juli Stand bei ii und msc abholen', 1, true, 1276);
INSERT INTO okr_pitc.action VALUES (1225, 4, 'Wir organisieren Teamevents 🙂', 1, true, 1331);
INSERT INTO okr_pitc.action VALUES (1246, 1, 'Wir entwickeln unsere Prozesse und Kommunikation stetig weiter', 4, false, 1331);
INSERT INTO okr_pitc.action VALUES (1245, 2, 'Die Vision bzgl. Platform Engineering / mid3.0 ist Allen klar', 3, false, 1331);
INSERT INTO okr_pitc.action VALUES (1251, 1, 'Interne Stakeholder identifiziert und deren Bedürfnisse erfasst. 🙂', 0, false, 1344);
INSERT INTO okr_pitc.action VALUES (1252, 5, 'Gemeinsame Definition der Workpackages für das nächste Quartal mit Stakeholdern. 🙂', 1, false, 1344);
INSERT INTO okr_pitc.action VALUES (1253, 3, 'Aufbau eines MVPs basierend auf OpenShift Developer Hub (Backstage).🙂', 2, false, 1344);
INSERT INTO okr_pitc.action VALUES (1254, 3, 'Organisation eines PE Kafis. 🙂', 3, false, 1344);
INSERT INTO okr_pitc.action VALUES (1255, 4, 'Produktion eines “Ein Mate mit…” Videos zum Thema PE.', 4, false, 1344);
INSERT INTO okr_pitc.action VALUES (1256, 0, 'Einführung und Kommunikation einer einheitlichen PE-Lösung', 5, false, 1344);
INSERT INTO okr_pitc.action VALUES (1198, 2, 'Partner Programm Konzept steht', 3, true, 1311);
INSERT INTO okr_pitc.action VALUES (1271, 4, 'Massnahmen kommunzieren und schulen', 6, true, 1261);
INSERT INTO okr_pitc.action VALUES (1238, 1, 'Landing-Page “Platform Engineering” auf neuer Webseite ist live.', 2, false, 1343);
INSERT INTO okr_pitc.action VALUES (1278, 1, 'Next Steps definieren', 4, false, 1275);
INSERT INTO okr_pitc.action VALUES (1284, 0, 'bei Kanton Bern Implementierungssauftrag holen', 3, false, 1276);
INSERT INTO okr_pitc.action VALUES (1286, 0, 'monatliche Abstimmung ar und dbi einführen', 2, false, 1277);
INSERT INTO okr_pitc.action VALUES (1226, 4, 'Die SIGs erfüllen ihren Zweck und machen Spass 🙂', 2, true, 1331);
INSERT INTO okr_pitc.action VALUES (1270, 1, 'Abstimmungstermine planen', 0, true, 1261);
INSERT INTO okr_pitc.action VALUES (1273, 1, 'Termine mit CSO und CTO geplant', 0, true, 1263);
INSERT INTO okr_pitc.action VALUES (1288, 0, 'Codi Berti', 0, false, 1317);
INSERT INTO okr_pitc.action VALUES (1290, 0, 'Draft im August - Spiegeln mit Team', 2, false, 1317);
INSERT INTO okr_pitc.action VALUES (1281, 1, 'Termine planen', 0, true, 1276);
INSERT INTO okr_pitc.action VALUES (1202, 2, 'Kommunikationsplanung weiterführen', 0, true, 1315);
INSERT INTO okr_pitc.action VALUES (1266, 3, 'Kickoff durchführen mit msc und jbl für Zielklärung', 1, true, 1261);
INSERT INTO okr_pitc.action VALUES (1272, 3, 'Massnahmen priorisiert umsetzen', 7, false, 1261);
INSERT INTO okr_pitc.action VALUES (1241, 3, 'Pro Bereich/Pfeiler mindestens 1 Ressource vorhanden, die nicht älter als 2 Jahre ist (Blogbeitrag, Referenzbericht, Video). 🙂', 5, true, 1343);
INSERT INTO okr_pitc.action VALUES (1287, 1, 'Vorgehen und Mitarbeit mit Mäge klären', 0, true, 1267);
INSERT INTO okr_pitc.action VALUES (1242, 5, '2 Leads für den Aufbau von Plattformen bei neuen oder seit >3 Jahre inaktiven Kunden generiert.', 6, true, 1343);
INSERT INTO okr_pitc.action VALUES (1243, 3, '2 Leads für die Optimierung von Plattformen bei bestehenden Kunden generiert.', 7, true, 1343);
INSERT INTO okr_pitc.action VALUES (1280, 1, 'jbr dazu einladen', 2, true, 1275);
INSERT INTO okr_pitc.action VALUES (1274, 1, 'Marktvalidierung durchführen', 1, true, 1263);
INSERT INTO okr_pitc.action VALUES (1293, 1, 'Validierung und Priorisierung mit CSO und CTO', 2, true, 1263);
INSERT INTO okr_pitc.action VALUES (1275, 2, 'Priorisierte Shortlist ist erstellt', 3, true, 1263);
INSERT INTO okr_pitc.action VALUES (1279, 1, 'Kick Off mit Raaflaub & Adi durchführen für Delivery Pipeline', 1, true, 1275);
INSERT INTO okr_pitc.action VALUES (1289, 1, 'Simu Abgleich mit Pascou', 1, true, 1317);
INSERT INTO okr_pitc.action VALUES (1268, 2, 'Feedback msc und jba einholen', 3, true, 1261);
INSERT INTO okr_pitc.action VALUES (1283, 1, 'Blogs beauftragen', 2, true, 1276);
INSERT INTO okr_pitc.action VALUES (1200, 2, '2 Leads für den Aufbau von Plattformen bei neuen Kunden (oder Kunden, bei denen wir seit > 3 Jahre kein Projekt umgesetzt haben)', 5, true, 1311);
INSERT INTO okr_pitc.action VALUES (1201, 2, '2 Leads für die Optimierung von Plattformen bei bestehenden Kunden', 6, true, 1311);
INSERT INTO okr_pitc.action VALUES (1239, 2, 'Partnerprogramm Konzept entwickelt.', 3, true, 1343);
INSERT INTO okr_pitc.action VALUES (1269, 3, 'Konzept finalisieren & Dokumentieren', 5, true, 1261);
INSERT INTO okr_pitc.action VALUES (1292, 1, 'Workshop 3.9.', 1, true, 1267);
INSERT INTO okr_pitc.action VALUES (1224, 3, 'Wir arbeiten weiter gemeinsam in Projekten', 0, true, 1331);
INSERT INTO okr_pitc.action VALUES (1204, 2, 'Analyse der Umgebungs-Komplexität', 0, true, 1270);
INSERT INTO okr_pitc.action VALUES (1205, 1, ' Ableiten des Verteilschlüssel', 1, true, 1270);
INSERT INTO okr_pitc.action VALUES (1308, 1, 'Einsatzplannung mit Kunden abgestimmt', 0, false, 1405);
INSERT INTO okr_pitc.action VALUES (1309, 1, 'Kundenbestellung verarbeitet: Highrise auf won', 1, false, 1405);
INSERT INTO okr_pitc.action VALUES (1310, 1, 'Kundenbestellung verarbeitet: Ptime Auftrag erstellt & Position erfasst', 2, false, 1405);
INSERT INTO okr_pitc.action VALUES (1311, 0, 'Kundenbestellung verarbeitet: Bestellung in files abgelegt', 3, false, 1405);
INSERT INTO okr_pitc.action VALUES (1312, 0, 'Kundenbestellung verarbeitet: Rechnunggstellung im Wiki dokumentiert', 4, false, 1405);
INSERT INTO okr_pitc.action VALUES (1320, 0, 'Wording und Vorgehen für Kundenkommunikation', 4, false, 1416);
INSERT INTO okr_pitc.action VALUES (1321, 1, 'Gelungener Switch TobiH > SimoneG', 0, false, 1420);
INSERT INTO okr_pitc.action VALUES (1329, 1, 'SWOT', 1, false, 1437);
INSERT INTO okr_pitc.action VALUES (1330, 1, 'Vision, Zielbild definieren', 2, false, 1437);
INSERT INTO okr_pitc.action VALUES (1331, 0, 'Soll Konzept definieren, Strukturen, Rolle, Kompetenzen usw', 3, false, 1437);
INSERT INTO okr_pitc.action VALUES (1332, 0, 'Massnahmen festlegen', 4, false, 1437);
INSERT INTO okr_pitc.action VALUES (1333, 0, 'Umsetzung eher Q3''25', 5, false, 1437);
INSERT INTO okr_pitc.action VALUES (1334, 1, '1. Zielsegment ist definiert ', 0, false, 1439);
INSERT INTO okr_pitc.action VALUES (1335, 1, '2. Abgleich mit Highrise des Zielsegments', 1, false, 1439);
INSERT INTO okr_pitc.action VALUES (1336, 1, '3. Mailingtext ist vorbereitet', 2, false, 1439);
INSERT INTO okr_pitc.action VALUES (1395, 0, 'Webseite (extern)', 7, false, 1470);
INSERT INTO okr_pitc.action VALUES (1396, 0, 'LinkedIn (extern)', 8, false, 1470);
INSERT INTO okr_pitc.action VALUES (1397, 0, 'Google Ads (extern)', 9, false, 1470);
INSERT INTO okr_pitc.action VALUES (1398, 0, 'tbd', 10, false, 1470);
INSERT INTO okr_pitc.action VALUES (1389, 2, 'Leadership Monthly (intern)', 1, true, 1470);
INSERT INTO okr_pitc.action VALUES (1316, 1, 'Analyse der Umgebungs-Komplexität ', 0, true, 1416);
INSERT INTO okr_pitc.action VALUES (1317, 1, 'Ableiten des Verteilschlüssel mit Thom abgestimmt', 1, true, 1416);
INSERT INTO okr_pitc.action VALUES (1339, 0, 'Klären Wartungsaufwand pro Projekt', 0, false, 1444);
INSERT INTO okr_pitc.action VALUES (1340, 0, 'Klären Wartungsbudget pro Projekt', 1, false, 1444);
INSERT INTO okr_pitc.action VALUES (1341, 0, 'Planung von Members', 2, false, 1444);
INSERT INTO okr_pitc.action VALUES (1344, 1, 'Workshop /dev/tre, 15. Oktober 2024', 0, false, 1448);
INSERT INTO okr_pitc.action VALUES (1348, 2, 'Workshop /dev/tre, 22. Oktober 2024', 0, false, 1452);
INSERT INTO okr_pitc.action VALUES (1346, 1, 'Kundengespräch geführt', 0, false, 1450);
INSERT INTO okr_pitc.action VALUES (1347, 1, 'Planung und Auslastung gesichert', 1, false, 1450);
INSERT INTO okr_pitc.action VALUES (1342, 2, 'Workshop /dev/tre, 22. Oktober 2024', 0, false, 1446);
INSERT INTO okr_pitc.action VALUES (1351, 0, '/mid Week', 0, false, 1434);
INSERT INTO okr_pitc.action VALUES (1352, 0, '/mid Inside 3.12.', 1, false, 1434);
INSERT INTO okr_pitc.action VALUES (1353, 0, 'Secret Santa', 2, false, 1434);
INSERT INTO okr_pitc.action VALUES (1354, 0, 'Onboarding neue Members', 3, false, 1434);
INSERT INTO okr_pitc.action VALUES (1355, 0, 'Blogposts', 4, false, 1434);
INSERT INTO okr_pitc.action VALUES (1356, 0, 'Puzzle News (intern)', 0, false, 1461);
INSERT INTO okr_pitc.action VALUES (1357, 0, 'Leadership Monthly (intern)', 1, false, 1461);
INSERT INTO okr_pitc.action VALUES (1358, 0, 'Weihnachts Infoanlass (intern)', 2, false, 1461);
INSERT INTO okr_pitc.action VALUES (1359, 0, 'Puzzle Blogpost (extern)', 3, false, 1461);
INSERT INTO okr_pitc.action VALUES (1360, 0, 'Business Lunch (extern)', 4, false, 1461);
INSERT INTO okr_pitc.action VALUES (1361, 0, 'Mate mit (extern)', 5, false, 1461);
INSERT INTO okr_pitc.action VALUES (1362, 0, 'Medienmitteilung (extern)', 6, false, 1461);
INSERT INTO okr_pitc.action VALUES (1363, 0, 'Webseite (extern)', 7, false, 1461);
INSERT INTO okr_pitc.action VALUES (1364, 1, 'Readme/Steckbriefe fertig gestellt', 0, false, 1463);
INSERT INTO okr_pitc.action VALUES (1365, 1, 'Verantwortungen verteilt', 1, false, 1463);
INSERT INTO okr_pitc.action VALUES (1366, 1, 'Kein internes Arbeiten ohne Issue/Ticket/Auftrag', 2, false, 1463);
INSERT INTO okr_pitc.action VALUES (1367, 0, 'Tasks abgeschlossen (Finish something!)', 3, false, 1463);
INSERT INTO okr_pitc.action VALUES (1368, 0, 'Backlog Tickets definiert', 4, false, 1463);
INSERT INTO okr_pitc.action VALUES (1343, 2, 'Workshop /dev/tre, 22. Oktober 2024', 0, false, 1447);
INSERT INTO okr_pitc.action VALUES (1350, 2, 'Workshop /dev/tre, 22. Oktober 2024', 0, false, 1456);
INSERT INTO okr_pitc.action VALUES (1322, 2, 'Festlegen Staffing Weiterentwicklung wearecube.ch (Austritt TobiH)', 0, true, 1422);
INSERT INTO okr_pitc.action VALUES (1318, 1, 'SLA Vorlage erarbeitet ', 2, true, 1416);
INSERT INTO okr_pitc.action VALUES (1319, 1, 'Inhalt Betrieb/Wartung und Support', 3, true, 1416);
INSERT INTO okr_pitc.action VALUES (1328, 3, 'Ausganglage Ist-Situatiom identifizieren: Schwachstellen, ineffiziente Prozesse oder strukturelle Hindernisse erkennen.', 0, true, 1437);


--
-- Data for Name: alignment; Type: TABLE DATA; Schema: okr_pitc; Owner: okr-test
--



--
-- Data for Name: check_in; Type: TABLE DATA; Schema: okr_pitc; Owner: okr-test
--

INSERT INTO okr_pitc.check_in VALUES (1004, 'Kommunikation am Weihnachtsinfoanlass', '2023-12-11 19:41:43.318197', '', '2023-12-11 19:41:43.318202', 0.3, 26, 146, 2, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1005, 'Status quo', '2023-12-11 19:42:47.242092', '', '2023-12-11 19:42:57.372196', 0.1, 26, 145, 1, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1006, '', '2023-12-12 06:50:31.503447', '', '2023-12-12 06:50:31.503452', 0.9, 5, 167, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1015, 'Zwei weitere Blogposts in den entsprechenden Serien geschalten (Java String & Web Components).  Follower gewonnen: 52.', '2023-12-12 07:35:36.07316', 'Weiter so.', '2023-12-12 07:35:36.073165', 0.8, 49, 147, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1018, '13 von 19 Massnahmen umgesetzt, 3 Massnahmen in die Strategiearbeit Q3 überführt', '2023-12-12 07:51:04.828147', '', '2023-12-12 07:51:04.828151', 0.55, 5, 163, 1, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1063, '', '2023-12-14 14:33:09.306492', '', '2023-12-14 14:33:09.306508', 0.8, 5, 162, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (723, 'Positiver Austausch zwischen MCs am AUMC hat stattgefunden. Nächstes AUMC im Dez. zu diesem Thema geplant.', '2023-11-27 07:55:02.931763', '', '2023-12-15 09:40:46.234922', 0.7, 24, 132, 5, 'metric', NULL, 3);
INSERT INTO okr_pitc.check_in VALUES (1068, 'Kurz am Weihnachtsinfoanlass über den Workshop orientiert. Weitere Arbeiten dazu erfolgen im neuen Jahr.', '2023-12-18 06:31:56.742457', 'Zusammenfassung der Resultate und Pendenzen des Workshops durch abe.', '2023-12-18 06:31:56.742463', 0.3, 16, 138, 1, 'metric', NULL, 3);
INSERT INTO okr_pitc.check_in VALUES (1070, 'VSHN ist mit Workshop zu APPUiO einverstanden und Terminfindung läuft. Kurz über Stand am Weihnachtsinfoanlass orientiert.', '2023-12-18 06:38:03.488962', '', '2023-12-18 06:38:03.488969', 0.27, 16, 133, 3, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1112, 'Final Checkin: Puzzle Grundsätze ergänzt und kommuniziert. Bevor wir ein Self-Assessment durchführen, machen wir an den MAG eine Umfrage und holen Feedback ab.', '2023-12-19 06:41:10.119469', 'Umfrage an MAG durchführen und Feedback abholen.', '2023-12-19 06:41:10.119474', 0.6, 13, 140, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1199, 'Neue Tags:
Potenzielle Kunden Data Analytics / ML / MLOps -> Data-Analytics Neukunden
Teilnehmer oder Interessierte MLOpsLab -> MLOpsLab
Potenzielle Partner: MLOpsPartner', '2024-01-19 10:33:56.869257', '', '2024-01-19 10:33:56.869259', NULL, 51, 1021, 4, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1200, 'Bidu Schärz (GCP), Robin Bobst (AWS), Vale Klopfenstein (AWS)', '2024-01-22 06:47:22.256326', '', '2024-01-22 06:47:53.726327', NULL, 31, 1062, 8, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1125, 'wegen Festtage noch nichts passiert', '2024-01-03 14:48:28.512376', '', '2024-01-03 14:48:28.512379', 0, 67, 1029, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1126, '', '2024-01-03 14:51:29.22327', '', '2024-01-03 14:51:29.223273', 41, 67, 1030, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1133, '', '2024-01-08 09:10:06.73608', 'MAG planen mit Members', '2024-01-08 09:10:06.736082', NULL, 28, 1079, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1134, 'Aktuelle OKR wurden im Teammeeting vorgestellt. Inkl. diesem KR :-)', '2024-01-08 09:10:53.492956', '', '2024-01-08 09:10:53.492958', NULL, 28, 1080, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1135, '', '2024-01-08 09:11:08.948159', '', '2024-01-08 09:11:08.948161', NULL, 28, 1087, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1136, 'Ideen gesammelt im Codi
Besprochen mit Bruno', '2024-01-08 09:11:53.496921', 'Klären Stand', '2024-01-08 09:11:53.496924', NULL, 28, 1089, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1124, 'Erste Messung', '2023-12-27 09:00:30.539305', '', '2024-01-08 09:14:40.869817', 3726, 28, 1083, 3, 'metric', NULL, 5);
INSERT INTO okr_pitc.check_in VALUES (1141, '- bisher noch keine grossen Aktivitäten da Weihnachts-/Jahreswechsel-Ferien', '2024-01-09 07:32:17.07874', '', '2024-01-09 07:32:17.078743', NULL, 24, 1065, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1143, 'Noch nichts unternommen', '2024-01-10 12:05:14.007643', '', '2024-01-10 12:05:14.007646', NULL, 13, 1035, 4, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1152, 'Max, Chrigu & Girod alle in Mobi-Gaia-Team von Memebers-Liste entfernt
Claudia noch drauf gelassen. Nach wie vor sind Simu, Hupf und Lukas drauf', '2024-01-14 09:46:10.976201', 'Aufträge für Hupf, Lukas und Simu finden', '2024-01-14 09:46:30.186083', 2, 4, 1081, 6, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1161, 'Phippu besucht Ansible Meetup in Zürich', '2024-01-15 07:58:30.459404', '', '2024-01-15 07:58:30.459406', 1, 32, 1029, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1186, '', '2024-01-16 15:37:15.737691', '', '2024-01-16 15:37:15.737693', 987000, 60, 1093, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1187, '', '2024-01-17 12:11:39.096328', 'Im nächste LST Monthly wird der Entscheidprozess vorgestellt und vorangekündigt, dass im März das Commitment des LST abgeholt wird.', '2024-01-17 12:11:39.09633', NULL, 5, 1027, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1196, 'Nach konkreten Events in Bern und Zürich wurde recherchiert. Leider bis jetzt noch nicht viel zählbares. Partnerprogramme gibt es von unserem Stack nicht direkt. Wir gehen aber noch auf die Hersteller zu (geplant HP / iterative.ai)', '2024-01-18 07:05:24.231132', '', '2024-01-18 07:05:24.231135', NULL, 51, 1020, 4, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1221, 'unverändert', '2024-01-24 12:26:33.218771', '', '2024-01-24 12:26:33.218774', 0, 20, 1024, 9, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1226, 'BKD ux-Auftrag für Claudia in Aussicht, Ansonsten Status Quo', '2024-01-25 07:03:07.912653', 'Situation für Chrigu, Claudia, Hupf und Lukas klären = Sales-Aktivitäten', '2024-01-25 07:03:47.714116', 2, 4, 1081, 5, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1252, '', '2024-01-26 08:48:42.04856', '', '2024-01-26 08:48:42.048581', 1008026, 60, 1093, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1255, 'Im heutigen LST Monthly wird Prozess vorgestellt. Ticket erfasst', '2024-01-29 07:16:28.608355', '', '2024-01-29 07:16:28.608362', NULL, 5, 1027, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1288, 'Wöchentlich sync gehabt.', '2024-01-31 12:11:18.5245', '', '2024-01-31 12:11:18.524513', 4, 27, 1107, 8, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1289, 'Januar-Retro am 31.1. durchgeführt.', '2024-01-31 12:12:57.570389', '', '2024-01-31 12:12:57.570403', 1, 27, 1108, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1293, '24 / 32', '2024-02-01 15:39:57.257739', '', '2024-02-01 15:39:57.257749', 75, 29, 1026, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1294, 'Happinessumfrage erweitert um Confidence der Members abzufragen.', '2024-02-02 14:17:24.346629', '', '2024-02-02 14:17:24.346637', 0, 20, 1024, 9, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1295, 'Happinessumfrage erweitert um Commitment der Members abzufragen.', '2024-02-02 14:17:45.171054', '', '2024-02-02 14:17:45.171074', 0, 20, 1025, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1298, 'Treffen mit mTrail Marc Hoffman aufgesetzt. Treffen mit Creaholic, Rexult und Adfinis durchgeführt (noch ohne ein konkretes Angebot daraus ergeben)', '2024-02-05 07:03:50.606961', '', '2024-02-05 07:03:50.606968', NULL, 5, 1022, 5, 'ordinal', 'FAIL', 2);
INSERT INTO okr_pitc.check_in VALUES (1310, 'Die Wireframes sind eingetroffen (5.2.) und werden diese Woche inkl. Video von Jenny für das weitere Vorgehen aufbereitet.', '2024-02-06 07:45:03.168238', '', '2024-02-06 07:45:03.168246', NULL, 49, 1116, 6, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1215, 'Highrise Custom Field erarbeitet', '2024-01-23 05:54:56.623506', '', '2024-02-07 09:51:49.80217', NULL, 13, 1035, 4, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1314, 'Easyinsight Auswertung erarbeitet', '2024-02-07 09:52:16.911568', '', '2024-02-07 09:52:16.911572', NULL, 13, 1035, 2, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1315, 'Neue Leads
* Schweizerisches Rotes Kreuz
* SHKB
* PF
* Medidata
* Tailor IT
* Securitas', '2024-02-08 06:34:57.640176', '', '2024-02-08 06:34:57.64018', NULL, 24, 1065, 7, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1309, '', '2024-02-06 05:43:58.878047', '', '2024-02-08 08:17:57.671178', 2867877, 13, 1032, 7, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1325, 'Treffen mit Siemens stattgefunden
https://puzzleitc.highrisehq.com/notes/715862945. Mit Kanton Bern (EGI) findet 13.2. statt', '2024-02-12 07:04:14.396021', '', '2024-02-12 07:04:14.396026', NULL, 5, 1036, 4, 'ordinal', 'FAIL', 2);
INSERT INTO okr_pitc.check_in VALUES (1014, 'Programmierabend wurde während des OKR-Tools umgesetzt', '2023-12-12 07:32:01.146', '', '2024-02-26 09:50:21.518838', 3, 15, 1006, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1467, 'Immer noch gleich', '2024-02-26 12:04:44.284059', '', '2024-02-26 12:04:44.284061', 41, 32, 1030, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1007, 'Bleibt so final.', '2023-12-12 06:53:18.473944', '', '2023-12-12 06:53:18.473949', 0.5, 5, 169, 1, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1016, 'Wir sind bei +28.4%.', '2023-12-12 07:39:53.129536', '', '2023-12-12 07:39:53.12954', 28.4, 49, 148, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (108, 'Monat Juni 69,8369645622306%', '2023-07-19 14:23:51.155272', '', '2023-07-18 22:00:00', 0.7, 28, 102, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1064, '', '2023-12-14 14:33:49.445117', '', '2023-12-14 14:33:49.445131', 0.9, 5, 168, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1069, 'Am Weihnachtsinfoanlass kurz über den Stand orientiert. 4 Members stehen in den Startlöchern um die Zertifizierung in Angriff zu nehmen. Weitere Members, welche im neuen Jahr evtl. Auslastungslücken verzeichnen, können dazu kommen.', '2023-12-18 06:35:19.043414', '', '2023-12-18 06:35:19.043421', 0.25, 16, 130, 1, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1113, 'Final Checkin: Eigene Landing-Page erstellt, Kampagne gestartet. Ein Termin mit Bank-Now vereinbart.', '2023-12-19 06:45:57.49178', '', '2023-12-19 06:45:57.491785', 0.2, 13, 129, 1, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1114, 'Final Checkin: Aufbau nicht geglückt. Da es herausfordernd wurde die Auslastung hoch zu halten, suchen die Divisions zur Zeit keine neue Members. Kündigungen: Chrigu Schlatter, Livia, Micha, Marc Fehlmann', '2023-12-19 06:50:04.886643', 'Zumindest die Austritte sollen ersetzt werden. Dies wird in den nächsten 2 Quartalen im Rahmen der SUM weiter getracked.', '2023-12-19 06:50:04.886648', -2.4, 13, 131, 1, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1127, '', '2024-01-03 14:51:29.227229', '', '2024-01-03 14:51:29.227229', 41, 67, 1030, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1128, 'Nix wegen Festtage, wird aber diese Woche noch angegangen', '2024-01-03 14:53:15.290337', '', '2024-01-03 14:53:15.290339', 0, 67, 1037, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1137, 'Im Monat Dezember waren wir knapp darüber ', '2024-01-08 14:31:00.111277', '', '2024-01-08 14:31:00.111279', 96.5, 41, 1064, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1142, 'Wir sind auf Zielkurs und werden im Q4 voraussichtlich 1 Video veröffentlichen und ein weiters Video für Q1 24/25 bereits gedreht.', '2024-01-09 08:43:23.515683', '', '2024-01-09 08:43:23.515685', NULL, 48, 1046, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1256, '', '2024-01-29 07:37:39.200144', '', '2024-01-29 07:37:39.200151', 41, 67, 1030, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1153, 'Liima intensivieren: Angesprochen, aber noch nicht ongoing - SWOA: noch nicht nachgefragt', '2024-01-14 09:49:12.651884', '', '2024-01-14 09:49:12.651887', NULL, 4, 1082, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1162, '- 1. Übersicht über Eskalierte Tickets (und auch sonst eine Übersicht) (Done by LükuG)
- 2. Definition Eskalation (Zammad vs. SLA), bereinigung der SLA''s in Zammad, dass sie auch den richtigen SLA''s entsprechen
- 3. Punkt to be done', '2024-01-15 08:02:34.152582', '', '2024-01-15 08:02:34.152584', 1, 32, 1037, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1188, 'Termine mit Siemens, SBB, BLS und Kanton Bern (EGI) sind aufgesetzt.', '2024-01-17 12:14:48.919219', 'BernMobil Termin wird vereinbart', '2024-01-17 12:22:22.456815', NULL, 5, 1036, 5, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1197, 'Für die Anpassungen der Highrise Struktur haben ar und nbl morgen (19.1.) einen Workshop. Am Lab ist nach wie vor nur 1 Anmeldung. Mit der PF und Mobi ist noch ein Austausch zum Thema geplant.', '2024-01-18 07:27:01.830935', '', '2024-01-18 07:27:01.830937', NULL, 51, 1021, 4, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1202, 'Noch keinen Fortschritt erzielt', '2024-01-22 06:51:33.276878', '', '2024-01-22 09:10:19.876888', NULL, 31, 1067, 5, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1201, 'Noch keinen Fortschritt erzielt', '2024-01-22 06:51:20.779551', '', '2024-01-22 09:10:32.278097', NULL, 31, 1078, 5, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1216, '', '2024-01-23 05:57:45.495726', '', '2024-01-23 05:58:27.584948', 2793840, 13, 1032, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1218, 'MoserBär von /devtre im Layout erfasst und diese Woche zur finalen Freigabe an Kunde.
/zh: ca. 5 Referenzen in Abklärung Seitens Simu.
/security: 1 Referenz angedacht, Abklärungen laufen
/subscriptions: sje fragt nach
/sys: Ev. BFH-Referenz
/mid', '2024-01-23 08:33:25.253648', '', '2024-01-23 08:44:49.043719', 0, 26, 1045, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1222, 'unverändert', '2024-01-24 12:27:33.597168', '', '2024-01-24 12:27:33.59717', 0, 20, 1025, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1227, 'SWOA-Meeting für Mo. 29.1. geplant, ansonsten Status Quo', '2024-01-25 07:04:41.457527', '', '2024-01-25 07:04:41.457529', NULL, 4, 1082, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1230, 'An 7 von 7 identifizerten Events teilgenommen (Messung bis 24.01.24).', '2024-01-25 07:18:19.688789', '', '2024-01-25 07:18:19.688791', NULL, 16, 1011, 9, 'ordinal', 'STRETCH', 0);
INSERT INTO okr_pitc.check_in VALUES (1253, 'Wird erst im Februar ersichtlich', '2024-01-26 08:49:32.283567', '', '2024-01-26 09:00:35.25638', 0, 60, 1094, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1285, '/zh: In Abklärung bei Simu
/dev tre: Moser Bär geht diese Woche zur finalen Freigabe an Kunde.
/mid: Push an Vali
/security: sje fragt bei Mark Zeman nach / allenfalls eine Kombi mit /ruby-Referenz
/ruby: Allenfalls Referenz in Bezug auf Security
/UX: Push an Vali
/mobility: Ev. Referenzerweiterung zu Baustellentool. Ansonsten keine Referenz möglich.
/hitobito: Wanderwege in Kombination für OSS
/sys: sje fragt bei sys nach', '2024-01-30 08:13:28.075348', '', '2024-01-30 08:13:28.075364', 0, 26, 1045, 4, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1283, 'Mai: Publikation Ansible (Florian Studer)
März: Produktion "UX" geplant
Juli: Publikatoin UX

Planung weiterer Videos wird im März

', '2024-01-30 08:07:47.055997', '', '2024-01-30 09:56:24.626056', NULL, 55, 1046, 8, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1290, 'Noch keine neuen Issues erfasst, einiges an bestehenden gearbeitet. mze hat 2 Issues erfasst.', '2024-01-31 12:27:49.296154', '', '2024-01-31 12:28:16.678954', 2, 27, 1109, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1291, 'Noch nicht dazu gekommen.', '2024-01-31 12:28:46.439694', '', '2024-01-31 12:28:46.439709', NULL, 27, 1112, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1296, 'Aktuell hp und js auf Liste.', '2024-02-02 14:20:03.650874', '', '2024-02-02 14:20:03.65089', 2, 20, 1031, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1299, 'Mit SBB hat Austausch mit Einkauf stattgefunden. Ergebnisse hier notiert https://puzzleitc.highrisehq.com/notes/715211663
', '2024-02-05 07:10:02.500952', '', '2024-02-05 07:59:53.360575', NULL, 5, 1036, 4, 'ordinal', 'FAIL', 2);
INSERT INTO okr_pitc.check_in VALUES (1311, 'Mit UX das Testing besprechen und warten auf Testuser Inputs von Mark. ', '2024-02-06 07:48:51.705363', '', '2024-02-06 07:48:51.70537', NULL, 49, 1118, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1316, 'An 9 von 11 identifizerten Events teilgenommen (Messung bis 07.02.24).', '2024-02-08 07:27:10.081717', '', '2024-02-08 07:27:10.081723', NULL, 16, 1011, 9, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1317, 'Zusätzlicher Cilium Lead: LGT Financial Services

SwissRe dürfte bald abgeschlossen werden.', '2024-02-08 07:31:12.833522', '', '2024-02-08 07:31:12.833526', 0, 16, 1012, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1326, 'Mit bs werden die finanzielle Wirkung diese Woche gechallenged und validiert', '2024-02-12 07:05:41.871883', '', '2024-02-12 07:05:41.871888', NULL, 5, 1027, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1356, 'Chrigu ab April nicht mehr bei Puzzle, Simu bei Mobiliar gemeldet, UX-Konzept für Claudia in Aussicht (nebst Gaia), Lukas bei Swisscom onboarden, Hupf mehrere Optionen', '2024-02-13 09:05:12.949275', 'Alle Members für die nächsten 4 Monate definitiv verplanen', '2024-02-13 09:05:12.94928', 4, 4, 1081, 7, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1144, 'Sofort + Februar (ohne okr_pitcDE)', '2024-01-10 12:21:36.002296', 'Dev-Sales-Sprint läuft, zudem Workshop für Indiv-Projekt-Akquise', '2024-02-14 10:06:38.885464', 9, 13, 1034, 2, 'metric', NULL, 3);
INSERT INTO okr_pitc.check_in VALUES (1806, 'VisualisierungsPost auf WAC/Linkedin', '2024-04-29 10:50:10.464443', '', '2024-04-29 10:50:10.46445', 4, 3, 1210, 9, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1807, '', '2024-04-29 10:52:01.957026', '', '2024-04-29 10:52:01.957033', 530, 3, 1212, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1983, 'nächstes Relase ist in Planung ', '2024-05-27 07:46:56.77612', '', '2024-05-27 07:46:56.776124', 0.3, 41, 1151, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2376, 'Evaluation der SaaS Anbieter noch ausstehend.', '2024-08-08 05:49:19.899343', '', '2024-08-08 05:49:19.899347', NULL, 24, 1350, 4, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1008, '', '2023-12-12 06:55:11.760974', '', '2023-12-12 06:55:11.76098', 0, 5, 161, 1, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1017, 'Wir haben diese Woche mit Dänu die erste Bespechung für die neue Website und werden sie nächste Woche mit Mark und anschliessend den weiteren Stakeholdern besprechen. ', '2023-12-12 07:42:29.123757', '', '2023-12-12 07:42:29.123761', 0.5, 49, 149, 6, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1065, '', '2023-12-14 14:34:53.912249', '', '2023-12-14 14:34:53.912265', 0.4, 5, 170, 1, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1071, 'Uni Luzern, MediDate und Unblu haben bestellt. Ausblick: BERNMOBIL, PostFinance (Cilium), CSS, TailorIT, aity, etc. Subs im Umfang von TCHF 500 offeriert.', '2023-12-18 06:44:05.037717', '', '2023-12-18 06:44:05.037723', 85600, 16, 141, 1, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1318, '- Überarbeitung Cloud Angebot läuft, erster Draft vorhanden, noch in Review
- Erarbeitung Referenzen on-going
- Organisation für Puzzle Lunch on-going
- 2 weitere Members haben oder werden Zertifizierung starten
- weitere interessierte Members vorhanden', '2024-02-08 07:33:22.090596', '', '2024-02-08 07:33:22.0906', NULL, 16, 1013, 7, 'ordinal', 'FAIL', 2);
INSERT INTO okr_pitc.check_in VALUES (1072, 'Der Wert hat sich nochmals etwas verbessert. Wir sind ca. TCHF 100 unter dem Target von MCHF 3.4. Evtl. kommt noch der eine oder andere Auftrag fürs 2024 rein bis Ende Dezember.', '2023-12-18 06:47:39.924946', '', '2023-12-18 16:32:51.311248', 3296000, 16, 142, 10, 'metric', NULL, 3);
INSERT INTO okr_pitc.check_in VALUES (1115, 'Bei 1040% Kapa sind devinitiv verplant über Jan+Feb:
da 70%
cbe 80%
td 42%
rk 87%
ck 100%
nkr 45%
kk 95%
uro 100%
jsh 46%
dsi 70%
yt 98%
-> Total 833%', '2023-12-19 07:13:22.167454', 'Lücken bei aeu, nkr und jsh noch schliessen.', '2023-12-19 07:13:22.167459', 80.1, 33, 171, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1129, 'Nix wegen Festtage, wird aber diese Woche noch angegangen', '2024-01-03 14:53:15.29338', '', '2024-01-03 14:53:15.293381', 0, 67, 1037, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1138, 'noch nicht gesttartet ', '2024-01-08 14:32:36.751745', '', '2024-01-08 14:32:36.751747', NULL, 41, 1070, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1154, 'Noch keine Aktivitäten. Plan in d3-WS am 16.1. erstellen (= Commit)', '2024-01-14 10:36:35.400406', '', '2024-01-14 10:47:40.093458', NULL, 4, 1084, 3, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1163, 'ITPoint als erster Kunde der so richtig in Supportprozess integriert wird. Initiale Meetings mit ITPoint sind im Gange.', '2024-01-15 08:03:54.089011', '', '2024-01-15 08:03:54.089014', 15, 32, 1038, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1164, 'Initiales Meeting mit dem ganzen Team erfolgt, weiteres Doing erfolgt am Sys-Hackday', '2024-01-15 08:05:06.423026', '', '2024-01-15 08:05:06.423028', 20, 32, 1039, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1189, '6 Members haben am ersten Workshop teilgenommen (Nenner ist momentan 32, da ple von momentan 4 Kurzsessions ausgeht). ', '2024-01-17 12:28:47.702525', 'Nächsten Workshops sind geplant.', '2024-01-17 12:28:47.702528', 18.75, 5, 1026, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1190, 'Die Messeung erfolgt erst im März. Confidence nach erstem Workshop noch bei 5/10', '2024-01-17 12:36:58.337702', '', '2024-01-17 12:36:58.337704', 0, 5, 1024, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1198, 'Es sind bereits Networking Anlässe geplant, jedoch noch nicht abschliessend', '2024-01-18 07:29:55.422956', '', '2024-01-18 07:29:55.422958', NULL, 51, 1023, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1203, 'MAG fast vollständig geplant', '2024-01-22 07:59:41.654862', '', '2024-01-22 07:59:41.654865', NULL, 28, 1079, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1204, 'Keine Aktiväten', '2024-01-22 07:59:55.154603', '', '2024-01-22 07:59:55.154605', NULL, 28, 1080, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1145, '', '2024-01-10 12:28:01.183947', '', '2024-01-23 05:58:20.96304', 2727821, 13, 1032, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1219, 'Mai: Publikation Ansible (Florian Studer)
März: Produktion "UX" geplant
Juli: Publikatoin UX

Planung weiterer Videos wird im März
', '2024-01-23 08:38:12.329608', '', '2024-01-23 08:38:12.32961', NULL, 26, 1046, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1223, 'Austausch mit approppo (ar) für Feldtest Alliance Swiss Pass.', '2024-01-24 14:21:51.528181', '', '2024-01-24 14:21:51.528184', NULL, 20, 1022, 5, 'ordinal', 'FAIL', 2);
INSERT INTO okr_pitc.check_in VALUES (1228, 'Keine Änderung.', '2024-01-25 07:05:49.406192', 'Plan muss noch erstellt werden', '2024-01-25 07:05:49.406194', NULL, 4, 1084, 3, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1254, 'Grober Plan erstellt. ', '2024-01-26 13:17:42.428158', 'Interessierte abholen', '2024-01-26 13:17:42.428171', NULL, 28, 1080, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1257, '- Wir haben eine Übersicht
- Wir sind daran, SLAs im Zammad zu korrigieren.', '2024-01-29 07:38:51.429175', '', '2024-01-29 07:38:51.429181', 1.5, 67, 1037, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1286, 'Kernteam "Digitale Lösungen" trifft sich am 7.2.. Dort wird konzeptionell weiter geplant, allenfalls passen wir das KR gemäss aktueller Planung dann an. ', '2024-01-30 08:16:22.529124', '', '2024-01-30 08:16:22.529144', NULL, 26, 1047, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1292, 'fst konnte einen ersten Task abschliessen, alles andere ist ongoing.', '2024-01-31 12:37:42.180702', '', '2024-01-31 12:37:42.180715', 1, 27, 1110, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1297, 'Austausch mit mutoco und sf angefragt.', '2024-02-02 14:26:00.59412', '', '2024-02-02 14:26:00.594134', NULL, 20, 1022, 5, 'ordinal', 'FAIL', 2);
INSERT INTO okr_pitc.check_in VALUES (1300, 'Das LST Team wurde über Prozess informiert und wurde gutgeheissen', '2024-02-05 07:11:02.992321', '', '2024-02-05 07:11:02.992328', NULL, 5, 1027, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1327, 'Issues zu WAFs erfasst, Phil und Flo haben neue Issues.', '2024-02-12 07:37:58.970324', '', '2024-02-12 07:37:58.970329', 5, 27, 1109, 6, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1357, 'Clara ist am onBoarding, Liima ist der zusätzliche Aufwand geschätzt, SWOA-SLA ist mit Marc diskutiert', '2024-02-13 09:07:00.453646', 'SWOA: SLA finalisieren, Umsetzung forcieren - Liima: Offerte Zusatzaufwände forcieren - Acrevis klären', '2024-02-13 09:07:00.453651', NULL, 4, 1082, 6, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1312, 'März: Produktion "UX" (Simon Hirsbrunner)/ Veröffentlichung Security (Mark Zeman)
Mai: Publikation Ansible (Florian Studer)
Juli: Publikatoin UX

Planung weiterer Videos wird im März', '2024-02-06 08:40:46.060628', '', '2024-02-06 08:47:50.093276', NULL, 55, 1046, 8, 'ordinal', 'COMMIT', 4);
INSERT INTO okr_pitc.check_in VALUES (1358, 'Teilnahme an Events on going (Target)', '2024-02-13 09:08:20.987948', 'Plan rückwirkend erstellen, um Systematik reinzubrigen (Commit)', '2024-02-13 09:08:20.987953', NULL, 4, 1084, 4, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1468, 'Das Ziel kann aktuell nicht verfolgt werden (keine Knappheit) - eingecheckte Zahl entspricht Anteil BL an gesamt geleistet, nicht %-Wert auf 100%.', '2024-02-26 12:33:11.361326', '', '2024-02-26 13:10:30.545178', 28.48, 3, 1050, 0, 'metric', NULL, 8);
INSERT INTO okr_pitc.check_in VALUES (1808, '', '2024-04-29 10:52:55.373514', '', '2024-04-29 10:52:55.37352', 132, 3, 1214, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1988, 'Tendenz Mai wieder nicht gut ', '2024-05-27 07:47:55.803936', '', '2024-05-27 07:47:55.80394', NULL, 41, 1149, 2, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1812, 'Keine Änderung', '2024-04-29 11:00:47.18347', '', '2024-04-29 11:00:47.183479', NULL, 3, 1220, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1984, 'keine Veränderung', '2024-05-27 07:47:04.277238', '', '2024-05-27 07:47:04.277242', 92424, 36, 1191, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1987, 'keine Veränderung', '2024-05-27 07:47:39.032942', '', '2024-05-27 07:47:39.032945', NULL, 36, 1208, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1990, '', '2024-05-27 07:48:28.403061', '', '2024-05-27 07:48:28.403065', 153, 36, 1214, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2149, '', '2024-06-17 15:51:39.940056', 'Anpassung Confidence Level', '2024-06-17 15:51:39.940059', NULL, 20, 1135, 0, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2268, 'Roadmap erstellt, Market Research sind wir noch am finalisieren', '2024-07-15 08:09:36.529244', '', '2024-07-15 08:09:36.529248', NULL, 36, 1324, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2271, '', '2024-07-15 08:11:19.964563', '', '2024-07-15 08:11:19.964567', NULL, 36, 1316, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2283, '', '2024-07-16 06:45:50.937225', '', '2024-07-16 06:45:50.937227', NULL, 5, 1261, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1003, '', '2023-12-11 15:32:47.991206', '', '2023-12-11 15:32:47.99121', 0, 24, 144, 1, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1009, 'Bleibt so final. Strategiearbeit eingeleitet für Q3', '2023-12-12 07:00:37.131281', '', '2023-12-12 07:00:37.131285', 0.6, 5, 166, 1, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1010, 'Wir haben zusammen Fajitas gegessen', '2023-12-12 07:11:19.294535', 'Wir müssen noch Brettspiele für den Spielabend organisieren', '2023-12-12 07:11:19.29454', 1, 15, 1006, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1019, 'Blogpost wurde erstellt und wird veröffentlicht. Keine weiteren Aktivitäten geplant.', '2023-12-12 08:22:16.878935', '', '2023-12-12 08:22:16.878939', 70, 40, 198, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (185, 'Members für Mobility Tag eingeladen. 1. Termin am 31.8.', '2023-08-17 09:35:58.180985', '', '2023-08-16 22:00:00', 0, 20, 38, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (190, 'Ruby Upgrade und viele Lösungsstunden', '2023-08-21 10:53:36.260616', '', '2023-07-16 22:00:00', 48, 41, 125, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1066, '', '2023-12-14 14:40:21.707705', '', '2023-12-14 14:40:21.707721', 0.2, 5, 159, 1, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1067, '', '2023-12-14 14:42:18.3174', '', '2023-12-14 14:42:18.317416', 0.4, 5, 160, 1, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1073, 'Kein Update', '2023-12-18 07:07:01.325553', '', '2023-12-18 07:07:01.325561', 0.6, 13, 140, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1075, 'Kein Update', '2023-12-18 07:08:22.979573', '', '2023-12-18 09:47:19.645352', -2.4, 13, 131, 1, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1074, 'Kein Update', '2023-12-18 07:07:26.098953', '', '2023-12-19 06:45:20.87032', 0.2, 13, 129, 1, 'metric', NULL, 3);
INSERT INTO okr_pitc.check_in VALUES (1116, 'Erhöhung bei Julius Bär (Mandat Kolinski, das voraussichtlich von Kallies weitergeführt wird) ist realistisch. Angestrebt wird aufgrund der speziellen Ausgangslage +14%.

BJUB-ENG23 +14% (Gewicht 0.25)
IDIS-CID24 +1.2% (Gewicht 0.75)

Unveränderte Konditionen bei 5 Mandaten:
METSC-LE24 (Gewicht 1.0)
FZAG-ALE23 (Gewicht 1.0)
MOBI-GLU24 (Gewicht 1.7)
SNBA-AAPS (Gewicht 0.8)
SNBA-LU24 (Gewicht 0.4)

Durchschnittliche Erhöhung: 0.75%', '2023-12-19 07:24:50.907601', '', '2023-12-19 07:24:50.907605', 0.75, 33, 191, 1, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1117, 'Im Moment keine mit der Rentabilitätsrechnung verbundenen Massnahmen beschlossen, da momentan nicht nötig. Status Update in SUM mit mw gegeben.', '2023-12-19 07:27:17.863657', '', '2023-12-19 07:27:17.863663', 75, 33, 173, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1130, 'Nix wegen Festtage', '2024-01-03 14:53:43.921895', '', '2024-01-03 14:53:43.921897', 0, 67, 1038, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1131, 'Kickoff am Mittwoch 10.1', '2024-01-03 14:54:34.731862', '', '2024-01-03 14:54:34.731864', 0, 67, 1039, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1132, 'Wird am 10.1. das erste Mal erhoben', '2024-01-03 14:55:18.669259', '', '2024-01-03 14:55:18.669261', 0, 67, 1040, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1139, '67h im Monat Dezember, dies ist jedoch knapp zu hoch', '2024-01-08 14:52:22.648214', '', '2024-01-08 14:52:22.648216', NULL, 41, 1091, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1146, 'Positive Rückmeldung von Diehl', '2024-01-10 12:36:29.669977', '', '2024-01-10 12:36:29.66998', 1, 17, 1054, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1155, 'Kein neuer Lead', '2024-01-14 10:47:07.891611', 'Dran bleiben', '2024-01-14 10:47:21.999436', 0, 4, 1086, 3, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1165, 'Wir hatten im Januar zwei Sync-Meetings, wie geplant.', '2024-01-15 08:09:32.072204', '', '2024-01-15 08:09:32.072206', 2, 27, 1107, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1191, 'Die Messeung erfolgt erst im März. Confidence nach erstem Workshop noch bei 5/10', '2024-01-17 12:42:33.014323', '', '2024-01-17 12:42:33.014325', 0, 5, 1025, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1205, 'Gut ausgeplant', '2024-01-22 08:03:54.478348', 'Target ist wohl mit 4735h sehr unrealistisch', '2024-01-22 08:03:54.478352', 4078, 28, 1083, 6, 'metric', NULL, 5);
INSERT INTO okr_pitc.check_in VALUES (1206, 'Ruby Kafi im Januar durchgeführt
2 Meetups geplant (März und Mai)', '2024-01-22 08:04:39.211568', '', '2024-01-22 08:04:39.211571', NULL, 28, 1087, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1220, 'Es wird voraussichtlich das Praxisbeispiel "Wanderwege", das wir auch für die OSS nutzen. Wir definieren bis Ende Januar die Inhalte und Möglichkeiten mit Oli/Thomas.', '2024-01-23 08:44:15.731006', '', '2024-01-23 08:44:15.731008', NULL, 26, 1047, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1224, 'Treffen mit BLS-Management hat stattgefunden. Aktuell hat BLS keinen zusätzlichen Bedarf. Im April wird Situation neu beurteilt.', '2024-01-24 16:15:49.320729', '', '2024-01-24 16:15:49.320732', NULL, 20, 1036, 5, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1229, 'Aktuelle Lead-Liste: Kanton Aargau, S&P (SWICO),
(zudem potentielle Aufträge bei Bestandeskunden: Swisscom, BKD, Peerdom)', '2024-01-25 07:13:27.760761', 'Gezielter und intensiver Leads (= Neukunden) generieren', '2024-01-25 07:13:27.760764', 2, 4, 1086, 3, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1258, 'Liste ist erstellt, Stories fehlen noch', '2024-01-29 07:40:15.837788', '', '2024-01-29 07:40:15.837796', 20, 67, 1038, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1260, 'kein Wert unter 8', '2024-01-29 07:42:08.989859', '', '2024-01-29 07:42:08.989865', 100, 67, 1040, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1287, '', '2024-01-30 08:20:23.648215', '', '2024-01-30 09:55:58.704274', NULL, 26, 1116, 6, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1301, 'Immer noch nicht viel verändert - warten auf Bescheid IGE, HRM und SAC', '2024-02-05 14:28:24.886855', 'Sales! Sales! Sales', '2024-02-05 14:28:24.886862', 6120, 36, 1048, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1313, '- /sys => BFH-Referenz in Abklärung bei /sys. Sje fragt nach.
- /mid/container => Medidata-Projekt im Februar abgeschlossen. Referenz im Q2 publizierbar.
- /mid/cicd => Medidata
- /devtre => Moser Bär im GzD, Veröffentlichung im Q1
- /ruby => Security-Referenz Stadt ZH Decidim in Erarbeitung
- /zh => 5 Referenzen in Abklärung bei sfa
- /security => sje fasst bei mz nach
- /mobility => aktuell keine Referenz in Aussicht
- /hitobito => Schweizer Wanderwege in Erarbeitung im Zhang mit OSS
- /ux => 3 Referenzen in Aussicht von SBB', '2024-02-06 08:48:11.67118', '', '2024-02-06 08:48:11.671187', 0, 26, 1045, 3, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1319, 'Ergänzend zum letzten Check-in:
- mit Erarbeitung Konzept "Digitale Lösungen" gestartet
- Resultate aus Workshop sind nun geclustert
- Massnahmen "Beeinflusser" und "Nachhaltigkeit" am Laufen', '2024-02-08 07:36:46.563772', '', '2024-02-08 08:44:39.317885', NULL, 16, 1051, 4, 'ordinal', 'FAIL', 3);
INSERT INTO okr_pitc.check_in VALUES (1328, 'Erfahrungen von ITPoint (zB Monitoring) sind eingeflossen. Zudem viele Inputs in der Pipeline um in die Supportprozess Doku auf os.puzzle.ch einzufliessen. Damit höhere Wahrscheinlichkeit die Targetzone zu erreichen.', '2024-02-12 09:23:02.329376', '', '2024-02-12 09:24:34.721633', 25, 32, 1038, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1359, 'Swico (S&P) kommt nicht, BKD UX-Konzept neu, Mobi-Team neu, Kanton-Aargau bestehend, inova.Solutions.AG(Neu), ', '2024-02-13 09:10:30.401338', '', '2024-02-13 09:10:30.401342', 3, 4, 1086, 3, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1809, 'Aktuell noch keine Durchführung', '2024-04-29 10:53:40.077584', '', '2024-04-29 10:53:40.07759', NULL, 3, 1215, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1811, 'Keine Änderung', '2024-04-29 11:00:37.454923', '', '2024-04-29 11:00:37.454929', NULL, 3, 1218, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1985, 'zeitlich noch nicht dazu gekommen ', '2024-05-27 07:47:18.705226', '', '2024-05-27 07:47:18.705229', NULL, 41, 1150, 1, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1986, 'keine Veränderung', '2024-05-27 07:47:24.783818', '', '2024-05-27 07:47:24.783823', NULL, 36, 1206, 3, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1989, 'Post Visualisierungsworkshop', '2024-05-27 07:47:58.044899', '', '2024-05-27 07:47:58.044902', 8, 36, 1210, 9, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2151, 'Anpassung Confidence Level', '2024-06-17 15:52:43.948429', '', '2024-06-17 15:52:43.948431', NULL, 20, 1140, 0, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (2270, '', '2024-07-15 08:10:16.974845', '', '2024-07-15 08:10:16.974847', 1, 36, 1315, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2284, 'Austausch mit Alexandra von InnoArchitects zu Marktvalidierung mit KI', '2024-07-16 06:46:38.033626', '', '2024-07-16 06:46:38.03363', NULL, 5, 1263, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1012, 'Wir hatten bereits ein gemeinsames Zmorgä', '2023-12-12 07:13:51.748474', '', '2023-12-12 07:13:51.748478', 1, 15, 1008, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2377, '', '2024-08-08 07:29:12.929202', '', '2024-08-08 07:29:12.929206', NULL, 24, 1243, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1025, 'Migration abgeschlossen', '2023-12-12 09:03:52.88176', '', '2023-12-12 09:03:52.881765', 100, 31, 211, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1020, 'Bisher keine Features aus Cilium Enterprise zwingend identifiziert bei Kunden.', '2023-12-12 08:25:57.906154', 'Kampagne im 2024 geplant, geht nicht so schnell wie vermutet/geplant.', '2023-12-12 08:25:57.906159', 30, 40, 197, 4, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1076, 'Keine Veränderung', '2023-12-18 07:30:05.462946', '', '2023-12-18 07:30:05.462952', 0.1, 28, 184, 2, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1118, 'Absolute Verrechenbarkeit im November war 81.3% (bester Monat bisher).
Ø somit (75.3+78.8+81.3)/3=78.47%', '2023-12-19 07:31:45.716859', '', '2023-12-19 07:31:45.716863', 78.47, 33, 178, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1119, 'November-Ergebnis liegt leider noch nicht vor, es könnte den bisherigen Rekord aus dem September noch toppen. Nicht relevant, da Strecht-Ziel ohnehin erreicht.', '2023-12-19 07:33:38.733978', '', '2023-12-19 07:33:38.733983', 261337, 33, 190, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1140, 'Mirjam hat bei BLS gestartet', '2024-01-08 15:06:50.838077', 'Martin und Janik bei SBB und BLS eingereicht. Janik: Interview bei SBB am 15.1.', '2024-01-08 15:06:50.83808', 3, 20, 1031, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1147, 'Der erste Networking Anlass findet am 16. Januar statt.', '2024-01-10 20:17:08.546305', '', '2024-01-10 20:17:08.546307', NULL, 16, 1011, 8, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1156, 'Noch nichts in Sicht', '2024-01-14 10:48:24.754199', 'Dran bleiben', '2024-01-14 10:48:24.754201', NULL, 4, 1090, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1166, 'Keine Aktivitäten', '2024-01-15 08:21:18.076664', '', '2024-01-15 08:21:18.076667', NULL, 28, 1079, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1167, 'Keine Aktivitäten', '2024-01-15 08:21:32.837371', '', '2024-01-15 08:21:32.837373', NULL, 28, 1080, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1402, 'Die Wireframes sind nun für den finalen Schliff bei WLY. Als Nächstes gehen wir die Thematik Hosting, Content Creation sowie das Designbriefing an. Nächste Woche sollten wir die Targetzone erreichen.', '2024-02-21 15:02:19.540696', '', '2024-02-21 15:02:19.540698', NULL, 49, 1116, 9, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1192, 'Treffen mit Adfinis (A. Wittwer),  Creaholic (A. Haas) und Rexult (M. Novo) für Januar aufgesetzt.', '2024-01-17 17:01:13.703659', 'Liste von geeigneter Partner zusammentragen', '2024-01-18 07:12:01.043622', NULL, 20, 1022, 5, 'ordinal', 'FAIL', 2);
INSERT INTO okr_pitc.check_in VALUES (1207, 'SWUN zu obr übergeben
BAFU Entscheid noch offen
Hitobito: Aufteilung hat stattgefunden', '2024-01-22 08:05:34.210964', 'Entscheid BAFU herbeiführen', '2024-01-22 08:05:55.208867', NULL, 28, 1089, 7, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1225, 'je startet anfangs Februar bei SBB. ii ist mit ASP und BEMO bis im Mai ausgelastet. Ungewiss: js', '2024-01-24 16:19:33.14724', '', '2024-01-24 16:19:56.613894', 2, 20, 1031, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1231, 'BKD: UX-Konzept / Kanton Aargau: Erneuerung IUV und FHV Prozesse / Peerdom: Support Dev-Team / Swisscom: cADC 2024', '2024-01-25 07:24:30.230619', '', '2024-01-25 07:24:30.230622', NULL, 4, 1090, 4, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1259, 'Büro ist eingerichtet und aufgeräumt, erste Feedbacks bereits wieder umgesetzt. Bastelecken braucht noch sehr viel liebe', '2024-01-29 07:41:26.898884', '', '2024-01-29 07:41:26.898892', 80, 67, 1039, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1261, 'mgafner was an mind. 2 Meetups', '2024-01-29 07:43:15.521132', '', '2024-01-29 07:43:15.52114', 3, 67, 1029, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1302, 'Mayra noch bei einem weiteren Projekt offeriert', '2024-02-05 14:29:08.905153', '', '2024-02-05 14:29:08.90516', NULL, 36, 1049, 6, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1306, 'keine Veränderung seit letztem Checkin', '2024-02-05 14:34:06.988264', '', '2024-02-05 14:34:06.988271', NULL, 36, 1061, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1320, 'Interview mit Gesamt-GL und Kennenlernen Sales-Team aufgesetzt.', '2024-02-08 07:38:09.576584', '', '2024-02-08 07:38:09.576587', 0, 16, 1057, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1329, '- Weitere Analyse wie Tickets im Zammad genau eskalieren (nach pending reminder eskalieren diese nicht mehr)
- Hitobito SLA in Zammad an die effektiven Verträge angepasst.', '2024-02-12 09:26:43.944408', '', '2024-02-12 09:26:43.944412', 1.8, 32, 1037, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1330, 'Bastelecke immer noch nicht aufgeräumt, Kilu den Auftrag gegeben das zu machen.', '2024-02-12 09:27:41.400435', '', '2024-02-12 09:27:41.400443', 80, 32, 1039, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1360, 'BKD, UX-Konzept: Initalworkshop geplant / Kanton Aargau: Erneuerung IUV und FHV Prozesse: Angebot kurz vor Abschluss / Peerdom: Angular-Update aufgegleist / Swisscom: cADC 2024 in Vertragsverhandlungen ', '2024-02-13 09:12:07.979932', '', '2024-02-13 09:12:22.562843', NULL, 4, 1090, 4, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1365, 'Treffen mit Kanton Bern (EGI) hat stattgefunden. (Innvoationsfeatures werden umgesetzt).', '2024-02-14 07:35:07.774591', '', '2024-02-14 07:35:07.774595', NULL, 5, 1036, 5, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (1372, 'März: Produktion "UX" (Simon Hirsbrunner)/ Veröffentlichung Security (Mark Zeman)
Mai: Publikation Ansible (Florian Studer)
Juli: Publikatoin UX

Planung weiterer Videos im März', '2024-02-15 08:02:11.55501', '', '2024-02-15 08:02:11.555014', NULL, 55, 1046, 8, 'ordinal', 'COMMIT', 4);
INSERT INTO okr_pitc.check_in VALUES (1378, '', '2024-02-19 07:09:59.192491', 'Termin mit bs musste auf diesen Dienstag geschoben werden. Separate Feedback Termin mit mg und ODI werden aufgesetzt. LST Monthly Ticket wird erfasst', '2024-02-19 07:09:59.192495', NULL, 5, 1027, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1397, 'Die Testcases sind definiert. Sobald das Datum für die Testnavi sowie die Testseiten klar ist, werden die User fürs Testing eingeladen. Geplant ist das die Einladungen noch im Februar zu versenden. Testing wird dann im März sein.', '2024-02-20 07:04:36.833468', '', '2024-02-20 07:04:36.833471', NULL, 49, 1118, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1399, 'Teilnahme an Networkinganlässen ist passiert oder geplant, aufgrund Targetingstrategie (https://codimd.puzzle.ch/WPG5iDcWSl6Oi4qkygnRgA). Fokus für Q3 und Q4 sind Anlässe zum Thema MLOps.', '2024-02-20 15:02:09.892857', '', '2024-02-20 15:02:09.892861', NULL, 51, 1023, 1, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1400, '', '2024-02-21 06:32:18.139153', '', '2024-02-21 06:32:18.139156', 3568905, 13, 1032, 10, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (2272, 'noch keine Veränderung', '2024-07-15 08:11:37.118753', '', '2024-07-15 08:11:37.118756', NULL, 36, 1319, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1011, 'Wir haben nicht in jedem Retro über das Wohlbefinden diskutiert', '2023-12-12 07:12:02.48995', 'Wir legen mehr Wert auf diese Rubrik', '2024-02-26 09:50:54.8335', 4, 15, 1007, 0, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1810, 'Aktuell keine Änderung auf dem Papier (ich weiss nicht genau wo die Zahl herkommt)... In Fact ist ein bisschen was dazu gekommen.', '2024-04-29 10:54:21.914254', '', '2024-04-29 10:54:21.91426', 92424, 3, 1191, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1991, 'keine Veränderung', '2024-05-27 07:48:51.198159', '', '2024-05-27 07:48:51.198162', NULL, 36, 1215, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1992, 'Feedback von GL erhalten, nächste Woch werden wir es mit dem Team besprechen', '2024-05-27 07:50:18.454862', '', '2024-05-27 07:50:18.454865', NULL, 36, 1218, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2285, '', '2024-07-16 06:47:38.62983', '', '2024-07-16 06:47:38.629832', 0, 5, 1275, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2347, 'Noch keine Aktivitäten', '2024-07-29 09:40:10.681598', '', '2024-07-29 09:40:10.681604', NULL, 4, 1292, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2349, 'Noch keine Aktivitäten', '2024-07-29 09:41:02.931865', '', '2024-07-29 09:41:11.723474', NULL, 4, 1294, 10, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2350, 'Noch keine Aktivitäten', '2024-07-29 09:41:20.0505', '', '2024-07-29 09:41:20.050505', NULL, 4, 1295, 10, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2378, '16 Members mit Themenbereichen erfasst
Im GJ 24/25 bereits 8 Speaking Opportunities erfasst (5 eigene Events wie Puzzle up! / Sommerfest)
Die Liste im Redaktionsplan wird weiterhin geführt und aktualisiert.', '2024-08-08 12:45:30.77121', '', '2024-08-08 12:45:30.771216', NULL, 26, 1283, 10, 'ordinal', 'STRETCH', 0);
INSERT INTO okr_pitc.check_in VALUES (1813, 'Abgleich mit Lukas durchgeführt - noch keine freigeschaltete Version von Collections. ', '2024-04-29 11:03:03.330244', '', '2024-04-29 11:03:03.330249', NULL, 3, 1223, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1078, 'Keine Veränderung', '2023-12-18 07:32:14.809642', '', '2023-12-18 07:32:14.809648', 1.6, 28, 186, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1021, '77% der Members sind verplant im Januar. Wir haben zwei Members für welche wir noch Aufträge brauchen. Alle andern Verlängerungen kommen und neue Aufträge haben wir auch.', '2023-12-12 08:27:30.430143', '', '2023-12-12 12:49:00.217519', 100, 60, 200, 10, 'metric', NULL, 3);
INSERT INTO okr_pitc.check_in VALUES (1077, 'Keine Veränderung', '2023-12-18 07:30:57.29553', '', '2023-12-18 07:30:57.295536', 0.3, 28, 185, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1079, 'Nov 69%; Okt 66%; Sept 62%', '2023-12-18 07:35:55.027088', '', '2023-12-18 07:35:55.027094', 0.7, 28, 181, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1120, 'Situation gleich wie bei letztem Checkin. Etwas Entspannung bezügl. Auslastungssituation GJ/Q3 zeichnet sich ab, trotzdem planen wir den Ausrichtungsworkshop nicht mehr in diesem Quartal.', '2023-12-19 07:34:35.985929', '', '2023-12-19 07:34:35.985933', 0.1, 33, 192, 1, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1148, 'Leads: Universität Bern Rocky Linux, SwissRe Cilium', '2024-01-10 20:18:28.806385', '', '2024-01-10 20:18:28.806387', 0, 16, 1012, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (319, 'Keine', '2023-09-12 06:40:43.592175', '', '2023-09-11 22:00:00', 0.5, 30, 58, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1157, 'Messgrössen konkretisiert, Action Points erstellt', '2024-01-14 12:52:50.153522', 'Weiterarbeit gemäss ordinalen Messgrössen', '2024-01-14 12:52:50.153524', NULL, 4, 1098, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1158, 'Ordinale Messgrössen präzisiert', '2024-01-14 13:03:40.440547', 'Behandlung gemäss Massnahmen Key', '2024-01-14 13:03:40.440549', NULL, 4, 1100, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1168, 'Planung läuft', '2024-01-15 08:24:30.309317', '', '2024-01-15 08:24:30.309319', 3972, 28, 1083, 3, 'metric', NULL, 5);
INSERT INTO okr_pitc.check_in VALUES (1193, 'Zuerst müssen strategische Ziele festgelegt werden. Umfrage wird im Januar noch nicht durchgeführt', '2024-01-17 17:02:28.095376', '', '2024-01-17 17:02:28.095379', 0, 20, 1024, 9, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1208, 'Leads: SHKB, Securitas, MediData, RhB', '2024-01-22 09:01:56.968239', '', '2024-01-22 09:01:56.968242', NULL, 31, 1099, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1232, 'WS durchgeführt', '2024-01-25 07:25:18.206161', 'Masterplan als nächstes', '2024-01-25 07:25:18.206163', NULL, 4, 1098, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1233, 'Motto und Manifest im Team-WS kritisch betrachtet & diskutiert', '2024-01-25 07:26:19.018229', 'Finalisierung Motto & Manifest im Kernteam', '2024-01-25 07:26:19.018232', NULL, 4, 1100, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1262, '', '2024-01-29 08:02:04.38683', '', '2024-01-29 08:02:04.386848', NULL, 51, 1020, 4, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1263, '', '2024-01-29 08:02:18.554571', '', '2024-01-29 08:02:18.554578', NULL, 51, 1021, 4, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1264, '', '2024-01-29 08:02:42.208495', '', '2024-01-29 08:02:42.208503', NULL, 51, 1023, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1303, 'Keine Veränderung seit letztem Checkin', '2024-02-05 14:29:48.023128', 'Sales! Sales! Sales', '2024-02-05 14:29:48.023135', 1.95, 36, 1053, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1321, 'Mit Yoan und Mabel die Painpoints besprochen und in ihrer Arbeitsweise Action Items definiert, dass sie positiv an gagelige Aufgaben gehen koennen.', '2024-02-08 13:42:53.593275', '', '2024-02-08 13:42:53.593279', 100, 67, 1040, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1331, 'Lüku und Frank kümmern sich um die Members, die weniger als 8 notiert haben. Lüku redet mit Yoan und Mabel, Frank mit Kilu und Tim.', '2024-02-12 09:33:24.038731', '', '2024-02-12 09:33:24.038735', 100, 32, 1040, 9, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1332, 'keine weiteren Besuche, nachfragen wo Tinu Gafner war.', '2024-02-12 09:34:26.681346', '', '2024-02-12 09:34:26.68135', 3, 32, 1029, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1336, '', '2024-02-12 09:40:35.738119', '', '2024-02-12 09:40:35.738124', NULL, 28, 1089, 4, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1361, 'Keine Aktivitäten', '2024-02-13 09:12:57.085697', 'Weiterarbeit gemäss ordinalen Messgrössen', '2024-02-13 09:13:12.822657', NULL, 4, 1098, 5, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1362, 'Erstes Arbeitsmeeting im Kernteam (Claudia, Hupf, Mäge) durchgeführt, zwecks Finalisierung', '2024-02-13 09:14:28.118645', 'Finalisierung im Kernteam', '2024-02-13 09:14:28.11865', NULL, 4, 1100, 7, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1818, 'Ein Arbeitsvertrag wurde unterzeichnet!', '2024-04-30 13:04:51.394308', '', '2024-04-30 13:05:13.800663', 0.8, 17, 1238, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1819, 'Es wurde eine zusätzliche Stelle ausgeschrieben. Total: 5', '2024-05-01 11:39:11.73286', '', '2024-05-01 11:39:11.732877', NULL, 22, 1128, 7, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1366, 'Sofort + März (ohne okr_pitcDE)', '2024-02-14 10:06:31.017212', 'u.a. Offering Mobi Agiles Team', '2024-02-14 10:07:46.624773', 7.5, 13, 1034, 2, 'metric', NULL, 6);
INSERT INTO okr_pitc.check_in VALUES (1373, '/ruby: Security-Referenz in Erarbeitung bei Oli
/mid: Medidata-Referenz wird voraussichtlich im März erarbeitet
/mid: Adcubum-Referenz für Q2
/devtre: Moser Bär GzD beim Kunde
', '2024-02-15 08:12:31.905093', '', '2024-02-15 08:12:31.905097', 0, 26, 1045, 3, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1379, '9 von 11 MAG durchgeführt. Erste Massnahmen bereits angestossen', '2024-02-19 07:48:35.783937', 'MAG Abschliessen', '2024-02-19 07:48:49.688382', NULL, 28, 1079, 9, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1398, 'Diskussionen mit möglichen Partnern sind aufgegleist. Austausch mit HPE hat stattgefunden, Austausch mit iterative.ai ist geplant, Austausch mit AWS hat im Rahmen des Labaufbaus stattgefunden, allenfalls im Rahmen von Google Partnerschaft AI Themen anschauen (noch offen). Gleichzeitig Austausch mit Artifact hat stattgefunden (mögliche Zusammenarbeit wenn wir ML/AI Knowhow benötigen), Austausch mit Flock-Labs ist geplant für Donnerstag.', '2024-02-20 14:59:57.320584', '', '2024-02-20 14:59:57.320586', NULL, 51, 1020, 1, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1401, 'Keine Veränderung', '2024-02-21 06:32:54.365041', '', '2024-02-21 06:32:54.365043', NULL, 13, 1035, 2, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1403, 'Die Wireframes sind nun für den finalen Schliff bei WLY. Als Nächstes gehen wir die Thematik Hosting, Content Creation sowie das Designbriefing an. Nächste Woche sollten wir die Targetzone erreichen.', '2024-02-21 15:02:20.623479', '', '2024-02-21 15:02:20.623505', NULL, 49, 1116, 7, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1404, 'Die Wireframes sind nun für den finalen Schliff bei WLY. Als Nächstes gehen wir die Thematik Hosting, Content Creation sowie das Designbriefing an. Nächste Woche sollten wir die Targetzone erreichen.', '2024-02-21 15:02:34.223646', '', '2024-02-21 15:02:34.22365', NULL, 49, 1116, 9, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1405, 'Die Wireframes sind nun für den finalen Schliff bei WLY. Als Nächstes gehen wir die Thematik Hosting, Content Creation sowie das Designbriefing an. Nächste Woche sollten wir die Targetzone erreichen.', '2024-02-21 15:02:34.541928', '', '2024-02-21 15:02:34.541931', NULL, 49, 1116, 9, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1993, 'keine Veränderung', '2024-05-27 07:51:12.729168', '', '2024-05-27 07:51:12.729172', NULL, 36, 1220, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2286, 'Bei Kanton Bern vorgestellt. Ausstehend noch Rückmeldung vom Kanton zu Rahmenbedingungen der Data Goverance', '2024-07-16 06:48:47.980441', '', '2024-07-16 06:48:47.980444', NULL, 5, 1276, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2287, '', '2024-07-16 06:49:36.484655', '', '2024-07-16 06:49:36.484657', 0, 5, 1277, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2348, 'Noch keine Aktivitäten', '2024-07-29 09:40:46.938077', '', '2024-07-29 09:40:54.528231', NULL, 4, 1293, 7, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2351, 'Noch keine Aktivitäten', '2024-07-29 09:41:37.282257', '', '2024-07-29 09:41:37.282262', NULL, 4, 1296, 10, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2380, 'Wir erarbeiten das Know-How derzeit mit Kursen', '2024-08-08 19:05:45.336334', '', '2024-08-08 19:05:45.336339', NULL, 17, 1374, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2381, 'Abstimmung mit /mid Team, CHA baut eine Plattform für den Techworkshop auf', '2024-08-08 19:06:17.16319', '', '2024-08-08 19:06:17.163194', NULL, 17, 1375, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2433, '', '2024-08-15 07:56:47.387086', '', '2024-08-15 07:56:47.387088', NULL, 5, 1261, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1022, 'Verständnis im Team für DR ist da.
DR ist in den /mid Prozessen und Entscheidungsmechanismen verankert und wird als Entscheidungsgrundlage genutzt.', '2023-12-12 08:56:24.442562', '', '2023-12-12 08:56:24.442567', 50, 60, 201, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1080, 'SWUN aufgegleist, Aufgrund von Krankheit Ptime in Verzug', '2023-12-18 07:36:55.319987', 'Openshift noch immer offen', '2023-12-18 07:36:55.319997', 0.3, 28, 182, 3, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1121, 'Das letzte Team Forum wurde dafür verwendet, die MOs im Team zu challengen und Interesse abzuholen bzw. Umsetzungsvorschläge abzuholen. Das Bild des Teams auf die MOs hat sich dadurch geschärft.
Im Moment Aufbereitung und Adressierung der offenen Fragen.
Unterstützung der MO Cloud ist fast per se gegeben, Abstimmung mit MO-Owner (u.a. ich) implizit quasi gegeben, soll aber noch expliziter werden.', '2023-12-19 07:39:53.287025', 'Explizitere Abstimmung mit Cloud-MO-Team.', '2023-12-19 07:39:53.287029', 0.65, 33, 193, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1149, 'Pendenzen um Ziele zu erreichen definiert und verteilt am 08.01.24.', '2024-01-10 20:19:39.927438', '', '2024-01-10 20:19:39.927441', NULL, 16, 1013, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1150, 'Workshop um neue Wege in der Akquisition von Individual-Software-Projekten für am 18. Januar aufgesetzt.', '2024-01-10 20:21:05.46384', '', '2024-01-10 20:21:05.463853', NULL, 16, 1051, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (325, 'Zusage von Martin K.', '2023-09-13 06:13:08.720462', '', '2023-09-12 22:00:00', 0.2, 20, 35, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1159, 'Key Result genauer beschrieben, Action Plan erstellt', '2024-01-14 13:11:30.53215', 'Durchführung gemäss Action Plan', '2024-01-14 13:11:30.532152', 0, 4, 1102, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1169, 'Ruby Meetups geplant, ', '2024-01-15 08:24:53.924402', '', '2024-01-15 08:24:53.924404', NULL, 28, 1087, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1170, 'SWUN zu obr
BAFU Ideen gesammelt, gespräche geführt
Hitobito: Plan vorhanden; Kunden neu aufteilen', '2024-01-15 08:25:36.875108', '', '2024-01-15 08:25:36.87511', NULL, 28, 1089, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1194, 'Zuerst müssen strategische Ziele festgelegt werden. Umfrage wird im Januar noch nicht durchgeführt', '2024-01-17 17:03:22.097217', '', '2024-01-17 17:04:06.385593', 0, 20, 1025, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (326, 'Töfflitour-Event durchgeführt. Es war geil.', '2023-09-13 06:14:17.534125', '', '2023-09-12 22:00:00', 0.3, 20, 57, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1195, 'Gute Aussichten auf Auslastung von mka und je. Einsätze von ii und js ab März ungewiss.', '2024-01-17 17:07:13.865632', 'js bei BLS anbieten, ii Einsatz für BEMO möglich
', '2024-01-17 17:07:13.865634', 3, 20, 1031, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1209, 'Noch keinen Fortschritt erzielt.', '2024-01-22 09:10:25.927747', '', '2024-01-22 09:10:25.927749', NULL, 40, 1063, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1374, 'Anmeldung jbl an Innovationsworkshop its-ch', '2024-02-15 09:01:49.9055', '', '2024-02-15 09:01:49.905504', NULL, 20, 1023, 2, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1234, '- Cloud Angebot wird Anfang Februar überarbeitet
- Referenzen werden pro aktiv gesucht
- Organisation für Puzzle Lunch in Angriff genommen
- 3 Members haben oder werden Zertifizierung starten', '2024-01-25 07:26:24.641375', 'Bei Tech Board platzieren. Dev Bereiche nochmals anstossen. An Februar Monthly nehmen.', '2024-01-25 08:38:17.401437', NULL, 16, 1013, 7, 'ordinal', 'FAIL', 2);
INSERT INTO okr_pitc.check_in VALUES (1265, 'Es haben 4 Devs teilgenommen. Total 18 / 32', '2024-01-29 08:29:45.512266', '', '2024-01-29 08:29:45.512273', 56, 29, 1026, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1271, 'Bis anhing waren wir teilnehmern an 3 Events und es sind weitere geplant. Aus der Teilnahme hat sich ein konkreter Lead ergeben. Offerte geht diese Woche raus', '2024-01-29 08:43:49.044364', 'Events einplanen', '2024-01-29 08:43:49.04437', NULL, 36, 1058, 9, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1274, '3 Referenzen sind ready. Muss noch mit der SBB abklären ob wir sie veröffentlichen dürfen', '2024-01-29 08:46:23.968232', 'keine', '2024-01-29 08:46:23.968238', NULL, 36, 1105, 8, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1304, 'Newsletter geht diese Woche raus - höchstwahrscheinlich sogar mit einem zweiten Beitrag von WAC', '2024-02-05 14:30:44.776401', 'keine', '2024-02-05 14:30:44.776408', 1, 36, 1056, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1305, 'Bisher teilgenommene Events: Swiss ICT Innotalk, Dino Beerli Buchvernissage, ICT Networking Party, Swiss ICT VR/AR Event und diese Woche ein Webinar von Isolutions über ROI von der UX Transformation = 5 Events teilgenommen und 1 konkreter Lead kreiert', '2024-02-05 14:33:44.527785', '', '2024-02-05 14:33:44.527792', NULL, 36, 1058, 9, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1308, 'keine Veränderung seit letztem Checkin', '2024-02-05 14:42:52.420496', '', '2024-02-05 14:42:52.420503', NULL, 36, 1105, 8, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1322, 'Lab wurde durchgeführt mit 4 Teilnehmer. Jedoch waren nur zwei Kunden und zwei interne Personen. Diese sind jedoch sehr am Thema interessiert und haben sich selber gemeldet für eine Teilnahme. Aus diesem Grund auch auf Target.', '2024-02-08 15:34:58.142606', 'Weitere Angebote für MLOps werden geprüft', '2024-02-08 15:34:58.142611', NULL, 51, 1021, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1333, 'Keine Veränderung, Ferien', '2024-02-12 09:38:57.214285', '', '2024-02-12 09:38:57.214289', NULL, 28, 1079, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1334, 'Plan erstellt, sonst keine Veränderung, Ferien', '2024-02-12 09:39:36.649475', 'Brainstorming noch beim Team abholen.', '2024-02-12 09:39:36.6495', NULL, 28, 1080, 7, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1335, 'Keine Veränderung, Ferien', '2024-02-12 09:40:25.543685', 'Klären Helvetic Ruby', '2024-02-12 09:40:25.543689', NULL, 28, 1087, 4, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1363, '5 Skills/ CVs aktuell nach MAGs', '2024-02-13 10:07:22.342581', '', '2024-02-13 10:07:22.342585', 5, 4, 1102, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1364, 'Status Quo', '2024-02-13 10:08:07.066204', '', '2024-02-13 10:08:07.066208', 0, 4, 1103, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1375, 'Durch Mobi-Anfrage werden alle /mobility Members im Q3 ausgelastet sein: js, hp bei Mobi', '2024-02-15 09:04:11.43504', '', '2024-02-15 09:04:11.435044', 0, 20, 1031, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1367, 'Treffen mit EGI stattgefunden https://puzzleitc.highrisehq.com/notes/716102005', '2024-02-14 13:18:30.268707', '', '2024-02-14 15:32:18.762934', NULL, 5, 1036, 5, 'ordinal', 'COMMIT', 4);
INSERT INTO okr_pitc.check_in VALUES (1380, 'Aktuell läuft ein Brainstorming', '2024-02-19 07:49:30.413119', '', '2024-02-19 07:49:30.413123', NULL, 28, 1080, 7, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1383, 'Hitobito: Nachhaltig
SWUN: Läuft aktuell
BAFU: im Aufbau', '2024-02-19 07:54:43.653842', '', '2024-02-19 07:55:50.777896', NULL, 28, 1089, 7, 'ordinal', 'TARGET', 2);
INSERT INTO okr_pitc.check_in VALUES (1406, 'Die Wireframes sind nun für den finalen Schliff bei WLY. Als Nächstes gehen wir die Thematik Hosting, Content Creation sowie das Designbriefing an. Nächste Woche sollten wir die Targetzone erreichen.', '2024-02-21 15:02:35.167202', '', '2024-02-21 15:02:35.167204', NULL, 49, 1116, 9, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1814, 'Erledigt ist:
- Retromeeting der ersten 3 Monate ITPoint-Support
- Pikettplanung ist bis Ende Jahr definiert
- Erste zwei Retromeeting mit ITPoint sind aufgegleist
- Grafik des Supportprozesses ist erstellt und mit dem Kunden geteilt', '2024-04-29 11:29:17.14179', 'weiter so.', '2024-04-29 11:29:17.141797', 4, 32, 1160, 9, 'metric', NULL, 6);
INSERT INTO okr_pitc.check_in VALUES (1820, '', '2024-05-01 12:05:01.754768', '', '2024-05-01 12:05:01.75479', 0, 20, 1136, 3, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1994, 'Notwendiges ist erfasst - Dokument muss noch ins Reine geschrieben werden, damit es vorgelegt werden kann.', '2024-05-27 08:24:55.397411', '', '2024-05-27 08:24:55.397415', NULL, 27, 1185, 8, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1995, '', '2024-05-27 08:25:18.347621', 'Phil nimmt das Thema nun auf.', '2024-05-27 08:25:18.347624', NULL, 27, 1189, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1023, 'Kein Veränderung, da bereits erreicht.', '2023-12-12 08:58:17.191149', '', '2023-12-12 08:58:17.191154', 100, 31, 199, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1024, 'Aktuelles Zufriedenheitsniveau 8.3 (Zufriedenheitsniveau setzt sich aus den beiden Messkriterien "Zufriedenheit mit Puzzle" und "Zufriedenheit mit den Aufgaben" zusammen.', '2023-12-12 09:02:24.71407', '/mid Week', '2023-12-12 09:02:24.714074', 100, 31, 219, 8, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1081, 'Planung vorgenommen', '2023-12-18 07:38:33.970023', 'Planung SAC (betrifft jedoch Hitobito)', '2023-12-18 07:38:33.97003', 209351, 28, 183, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1122, 'Wir haben mit IT Point PostgreSQL Betrieb das Stretch Goal erreicht. Jedoch fehlt der Vermarktungsplan. Und beim Angebot fehlt noch das Finetuning. Deshalb Check-in mit einem "Mittelwert" von 0.5.', '2023-12-19 08:51:23.438781', '', '2023-12-19 08:51:23.438786', 0.5, 16, 133, 2, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1160, 'Noch nichts gemacht, auch Massnahmen sind noch nicht definiert hier in OKR!', '2024-01-14 13:13:41.663372', 'Workshop vom 16.1. nutzen, um Key Result zu beschreiben, Massnahmen zu definieren', '2024-01-14 13:13:41.663374', 0, 4, 1103, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1171, '', '2024-01-15 08:32:13.368366', '', '2024-01-15 08:32:13.368369', 77, 27, 1111, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1210, 'leider noch keine Veränderung', '2024-01-22 13:36:17.126918', '', '2024-01-22 13:36:17.126921', 0, 36, 1048, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1211, 'Beim IGE werden wir Phippu und Flavio offerieren / Berti als PL für BAFU in Stellung gebracht', '2024-01-22 13:37:08.649508', '', '2024-01-22 13:37:08.64951', NULL, 36, 1049, 6, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1212, 'leider noch keine Veränderung', '2024-01-22 13:37:48.010376', '', '2024-01-22 13:37:48.010378', 1.95, 36, 1053, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1235, 'Skills durch alle ausgefüllt (im WS)', '2024-01-25 07:27:20.539655', 'Bilaterale Besprechung Skills in den MAGs', '2024-01-25 07:27:20.539657', 5, 4, 1102, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1236, 'Status Quo', '2024-01-25 07:28:07.350529', 'Gemäss Key Result', '2024-01-25 07:28:07.350532', 0, 4, 1103, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1417, 'Momentan alle Members im nächsten Q ausgelastet', '2024-02-26 07:18:09.150703', '', '2024-02-26 07:18:09.150706', 1, 5, 1031, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1237, '- Der Workshop zu Individual-Software-Projekte wurde am 18.01.24 durchgeführt
- verschiedene Massnahmen definiert und einige sind bereits in Umsetzung
- Punkte und Fragen für Konzept wurden zusammengetragen
- als nächster Schritt wird das Konzept als Dokument eröffnet und ein nächstes Meeting mit dem Kernteam festgelegt.
- das Thema (oder Subthemen daraus) könnten sich als mögliche neue Marktopportunitäten eignen und anbieten', '2024-01-25 07:30:41.282248', '', '2024-01-25 07:31:20.608243', NULL, 16, 1051, 5, 'ordinal', 'FAIL', 2);
INSERT INTO okr_pitc.check_in VALUES (1151, 'Weiterer Austausch mit Kandidat hat am 9. Januar stattgefunden. Überlegungen zu weiteren Optionen stehen im Raum.', '2024-01-10 20:23:21.446976', '', '2024-01-25 07:33:42.623406', 0, 16, 1057, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1266, 'Mit Bernmobil findet nächster Austausch über mögliche Weiterentwicklungen erst nach der Weiterentwicklung im April/Mai statt.', '2024-01-29 08:37:34.455758', '', '2024-01-29 08:37:34.455764', NULL, 20, 1036, 5, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1272, 'Diskussion gestartet', '2024-01-29 08:44:21.042507', 'keine', '2024-01-29 08:44:21.042513', NULL, 36, 1061, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1267, 'Immer noch viele Deals offen, jedoch noch keine Veränderung konkret im Umsatz', '2024-01-29 08:38:32.239817', 'Sales! Sales! Sales!', '2024-01-29 09:38:53.550594', 0, 36, 1048, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1307, 'Visualisierungsworkshop als Angebot publiziert und wird im Newsletter kommuniziert', '2024-02-05 14:35:50.791127', '', '2024-02-05 14:35:50.791133', NULL, 36, 1076, 7, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (1323, 'Targeting für Networking festgelegt: https://codimd.puzzle.ch/WPG5iDcWSl6Oi4qkygnRgA?both
Anlässe für Q3/4 festgelegt, TN noch nicht überall fixiert.', '2024-02-08 15:38:52.452146', '', '2024-02-08 15:38:52.45215', NULL, 51, 1023, 2, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1324, 'Austausch mit möglichen Partnern laufen oder sind geplant. Angebotspräsentation an Anlässen aktuell keine Zusage und auch Möglichkeiten sind nicht so einfach. Eine Absage für den AI Leadership Day haben wir erhalten', '2024-02-08 15:40:48.281185', 'Weitere Anlässe anfragen', '2024-02-08 15:40:48.28119', NULL, 51, 1020, 4, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1337, '', '2024-02-12 09:43:43.242009', '', '2024-02-12 09:43:43.242014', 41, 32, 1030, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1338, 'Total 80% von Team ausgeplant', '2024-02-12 09:45:57.757142', 'Target ist sehr hoch gesetzt', '2024-02-12 09:45:57.757146', 3819, 28, 1083, 3, 'metric', NULL, 6);
INSERT INTO okr_pitc.check_in VALUES (1368, 'Die Wireframes werden gerade vom Projektteam geprüft. Wir geben am 21. Februar Feedback an WLY und konzentrieren uns dann auf die nächsten Punkte (Content Creation, Design & Bildwelten).', '2024-02-14 13:34:31.244965', '', '2024-02-14 13:34:31.24497', NULL, 49, 1116, 7, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1376, 'Confidence Level reduziert, da Confidence erst im März abgefragt wird.', '2024-02-15 09:28:34.958152', '', '2024-02-15 09:28:34.958156', 0, 20, 1024, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1381, 'BAFU neu bei OBR, mehr ausgeplant', '2024-02-19 07:54:09.879287', '', '2024-02-19 07:54:09.879291', 3946, 28, 1083, 3, 'metric', NULL, 6);
INSERT INTO okr_pitc.check_in VALUES (1382, '', '2024-02-19 07:54:28.160693', '', '2024-02-19 08:23:18.144817', NULL, 28, 1087, 4, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1407, '', '2024-02-21 15:37:41.301094', '', '2024-02-21 15:37:41.301097', 236000, 60, 1094, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1408, 'Neuer Lead SNB Gitlab: Nachsubskribierung, Reneval und Mehrjahresvertrag in Aussicht. Cilium Swiss Re und LGT warten wir auf Bestellung der Kunden. RockyLinux Uni Bern wird leider nichts.', '2024-02-22 07:05:20.078964', '', '2024-02-22 07:05:20.078966', 0, 16, 1012, 3, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1418, 'Alle A-Kundengespräche durchgeführt und dokumentiert. Im Moment keine Angebotsmöglichkeit, aber es sind auch alle Members ausgelastet', '2024-02-26 07:19:29.868693', '', '2024-02-26 07:19:29.868696', NULL, 5, 1036, 1, 'ordinal', 'COMMIT', 4);
INSERT INTO okr_pitc.check_in VALUES (1419, 'Umfrage erst im Laufe des März. Aber Confidence hoch.', '2024-02-26 07:21:06.766211', '', '2024-02-26 07:21:06.766214', 0, 5, 1024, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1420, 'Umfrage erst im Laufe des März. Aber Confidence hoch.', '2024-02-26 07:21:18.932779', '', '2024-02-26 07:21:18.932782', 0, 5, 1025, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1422, 'Es ist noch offen, ob eine weitere Kurzsession nötig ist oder eine letzte Abstimmung schriftlich erfolgen kann', '2024-02-26 07:24:17.156785', '', '2024-02-26 07:24:17.156787', 75, 5, 1026, 8, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1469, '', '2024-02-28 08:48:42.159439', '', '2024-02-28 08:48:42.15946', 3916480, 13, 1032, 10, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1472, 'Stand hat sich nicht geändert', '2024-03-01 15:41:27.284477', '', '2024-03-01 15:41:27.284487', NULL, 51, 1021, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1473, 'Übersicht mit relevanten Events auf unserer Wiki Seite: https://wiki.puzzle.ch/Puzzle/EventsOverview
Diese dient als Übersicht, welche Anlässe für uns wichtig sind und wer teilnimmt.', '2024-03-01 15:43:46.911539', '', '2024-03-01 15:43:46.91155', NULL, 51, 1023, 1, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1471, 'Austausch mit Xebia am 29.2.: Sie sind international 650MA, in der CH bis Ende Jahr 10 (aktuell 6), Interesse an einer Zusammenarbeit schien mir nicht gerade gross, sie haben bereits die besten Leute und stellen wenn nötig mehr an. Bewerbungen haben sie genug. Evtl. haben sie interessante Ausbildungen, die unsere Techis besuchen könnten (sind jedoch meist in A''dam)', '2024-03-01 15:40:42.8513', '', '2024-03-01 15:40:42.851314', NULL, 51, 1020, 1, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1474, '', '2024-03-04 08:36:53.413755', 'Zusammenfassung fürs Teammeeting erstellen', '2024-03-04 08:36:53.413762', NULL, 28, 1079, 9, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (1475, '', '2024-03-04 08:37:20.314886', 'Draft für Teammeeting erstellen', '2024-03-04 08:37:20.314893', NULL, 28, 1080, 7, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (1026, 'Unverändert. Wird in neuem Quartal wieder priorisiert.', '2023-12-12 09:12:15.417774', '', '2023-12-12 09:12:15.417779', 50, 31, 212, 3, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1082, 'Keine Veränderung', '2023-12-18 07:39:11.674187', 'Rückmeldung Mid ausstehend', '2023-12-18 07:39:11.674193', 0.7, 28, 179, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1085, '- Infoanlass Vertonung Stattgefunden
- Zammad Grafana Integration in Arbeit
- Team darauf sensibilisiert, dass sie regelmässiger Feedback in Tickets geben', '2023-12-18 07:49:39.445384', '', '2023-12-18 07:49:39.445393', 0.5, 67, 189, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1180, 'noch nichts unternommen', '2024-01-15 15:15:40.01972', '', '2024-01-15 15:15:40.019721', NULL, 36, 1061, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2379, 'Aktuell haben wir 14 Members dokumentiert
8 davon (Tru, JeanClaude, Oli, Fabien, Christoph, Dänu, Saraina, Andreas) Q1 GJ24/25 bereits einen Post publiziert. ', '2024-08-08 12:50:56.23249', '', '2024-08-08 12:51:21.276403', 8, 26, 1318, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2382, 'Bisher keine Zerfifizierungen vorhanden', '2024-08-08 19:06:40.715784', '', '2024-08-08 19:06:40.715791', 0, 17, 1376, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1213, 'Mayra und Tobi haben Blogpost bereit - Newsletter wird in einer Woche verschickt.', '2024-01-22 13:38:31.352143', '', '2024-01-22 13:38:31.352145', 0, 36, 1056, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1238, 'Ein bereits angesetztes Meeting musste ausgrund eines Terminkonflikts verschoben werden. Wir rechnen damit, dass sich die Situation in den kommenden Wochen klärt (grünes Licht oder nicht)

Weitere Optionen sind parallel in Prüfung.', '2024-01-25 07:33:29.683585', '', '2024-01-25 07:33:29.683588', 0, 16, 1057, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1239, 'Keine weiteren zusätzlichen Leads aktuell.

Am 25.01.24 findet ein Meeting zu einer Subscriptions Kampagne zwischen Sales+Marcom statt.', '2024-01-25 07:35:53.837794', '', '2024-01-25 07:35:53.837797', 0, 16, 1012, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1268, 'Phippu und Flavio bei IGE offeriert', '2024-01-29 08:39:10.180239', '', '2024-01-29 08:39:10.180245', NULL, 36, 1049, 6, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1339, 'Noch nicht gestartet', '2024-02-12 10:25:50.102863', '', '2024-02-12 10:26:23.871764', NULL, 41, 1070, 2, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (416, 'Erste Messungen zur Wirksamkeit in der Happiness Umfrage druchgeführt', '2023-10-05 08:12:43.114518', '', '2023-10-04 22:00:00', 0.1, 20, 164, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1369, 'Am gestrigen Meeting wurde entschieden, dass wir das Testing erst mit einer designten Navigation machen. Tobi H. und Jenny konkretisieren das Testing und bereiten alles vor. Die User werden Ende Februar für das Testing im März vorinformiert.', '2024-02-14 13:36:13.889371', '', '2024-02-14 13:36:13.889374', NULL, 49, 1118, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1377, 'Confidence Level reduziert, da Confidence erst im März abgefragt wird.', '2024-02-15 09:33:17.429554', '', '2024-02-15 09:33:17.429559', 0, 20, 1025, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1384, 'IGE gewonnen und HRM gewonnen', '2024-02-19 08:12:53.892992', 'versuchen Mobi Mandat zu verlängern', '2024-02-19 08:12:53.892994', 132996, 36, 1048, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1386, 'Phippu ist zurzeit noch auf der Bench - IGE Mandat fängt aber dann im März an. Tobi S. von der Liste genommen. Tobi H. hat noch Kappa.', '2024-02-19 08:15:52.175591', 'Sales! Sales! Sales', '2024-02-19 08:15:52.175594', 1.15, 36, 1053, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1409, 'An 9 von 12 identifizerten Events teilgenommen (Messung bis und mit 21.02.24).', '2024-02-22 07:16:04.575146', '', '2024-02-22 07:16:04.575148', NULL, 16, 1011, 9, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1421, '', '2024-02-26 07:22:20.891579', 'Separate bilaterale Termine aufgesetzt um Inputs/Feedback zu erhalten', '2024-02-26 07:22:20.891582', NULL, 5, 1027, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1476, 'Es wird kein weiteres Meeting geben, daher bleibt es im Target', '2024-03-04 08:38:08.523199', '', '2024-03-04 08:38:08.523209', 75, 29, 1026, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1477, 'Janiss, Olivier, Päscu und Pascal überplan', '2024-03-04 08:40:59.116409', '', '2024-03-04 08:40:59.116415', 4601, 28, 1083, 7, 'metric', NULL, 6);
INSERT INTO okr_pitc.check_in VALUES (1815, 'folgende Kunden wurden bereits angeschrieben:

- UniLu (gewonnen!)
- BAND-GENOSSENSCHAFT
', '2024-04-29 12:12:50.715456', 'Tinu fährt weiter mit Anschreiben', '2024-04-29 12:12:50.715462', 2, 32, 1158, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1821, '- Lehrlingsprojekte am LST monthly bestimmt: 1. Prio: UniLu, 2. Prio PCTS-Sheet
- Interviews mit möglichen Betreuungspersonen geführt', '2024-05-01 12:10:52.831527', '', '2024-05-01 12:10:52.831546', NULL, 20, 1139, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1996, '', '2024-05-27 08:25:30.039261', '', '2024-05-27 08:25:30.039264', NULL, 27, 1186, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2006, 'Die Migration der Blogpost sollte diese Woche abgeschlossen sein, dann legen wir mit dem SEO Thema los.', '2024-05-28 06:51:33.02133', '', '2024-05-28 06:51:33.021356', NULL, 49, 1179, 8, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2273, 'Die Änderung sind erfasst, Inputs aus Team eingeflossen, mit Tim Review gehalten, MR offen', '2024-07-15 11:04:08.564341', 'Auf Review MR durch Tim warten, anschliessend mergen und dem Team zeigen.', '2024-07-15 11:04:08.564343', NULL, 32, 1257, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2288, 'Kennenlernen mit Yup stattgefunden. Terminserie Abstimmung /mobility & Sales aufgesetzt', '2024-07-16 06:50:22.584064', '', '2024-07-16 06:50:22.584067', NULL, 5, 1266, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2352, 'Keine Veränderung', '2024-08-05 06:59:43.850572', '', '2024-08-05 06:59:43.850579', NULL, 13, 1245, 10, 'ordinal', 'TARGET', 3);
INSERT INTO okr_pitc.check_in VALUES (2383, 'Thema in Ausarbeitung', '2024-08-08 19:07:06.644021', '', '2024-08-08 19:07:06.644025', NULL, 17, 1377, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2412, 'Abstimmungstermin mit ar aufgesetzt', '2024-08-12 09:03:23.908974', '', '2024-08-12 09:03:23.908979', 0, 5, 1277, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2413, 'Termin am 14.8 mit Mäge', '2024-08-12 09:04:55.172125', '', '2024-08-12 09:04:55.172129', NULL, 5, 1267, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2452, 'Keine Veränderung', '2024-08-19 11:14:23.617325', '', '2024-08-19 11:14:23.617329', NULL, 13, 1245, 10, 'ordinal', 'TARGET', 3);
INSERT INTO okr_pitc.check_in VALUES (2453, 'Keine Veränderung', '2024-08-19 11:14:34.699701', '', '2024-08-19 11:14:34.699703', NULL, 13, 1247, 1, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2473, 'Aufbau Leads: Fischer Spindler, Inventx, Five Informatik
Update Leads: SNB, Mobi
Frequentis hat zugesagt / neuer Lead Inventx
', '2024-08-22 06:36:06.905337', '', '2024-08-22 06:36:06.905339', 2, 24, 1311, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2484, 'dsm: 322 von 739 Stunden verplant
yt:  479 von 1''056 Stunden verplant
dal: 361 von 739 Stunden verplant
45.9% Auslastung', '2024-08-22 11:21:11.229735', 'Gas geben im Verkauf. Offene Deals bei AXA (Simmen) und SBB (Yelan)', '2024-08-22 11:21:11.229738', 45.9, 33, 1304, 3, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2491, 'Partnerprogramm Konzept ist entwickelt und wird am MarCom und GL geokr_pitched! Kundenumfrage ist ausgewertet und Erkenntnisse abgeleitet. Bericht folgt. Go-to-Market Plan in Arbeit. Assets Pro Pfeiler fast alle gut.', '2024-08-22 12:02:06.648103', '', '2024-08-22 12:02:06.648105', 4, 31, 1343, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2497, 'unverändert', '2024-08-22 15:09:21.656627', '', '2024-08-22 15:09:21.656629', NULL, 20, 1262, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2501, 'Zeix Sommerfest (3 Personen) & UX-Meetup (SIH)', '2024-08-26 08:18:31.565998', '', '2024-08-26 08:18:31.566', 2, 3, 1314, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2502, 'Keine Veränderung', '2024-08-26 08:19:33.055044', '', '2024-08-26 08:19:33.055046', 6, 3, 1315, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2503, 'Noch keine Änderung, WAC-Morning im September', '2024-08-26 08:20:19.054021', '', '2024-08-26 08:20:19.054025', NULL, 3, 1316, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2368, 'Aktuell können wir aus Kapa-Gründen dieses Video nicht weiterverfolgen', '2024-08-06 07:14:22.26408', '', '2024-08-26 11:32:55.882618', NULL, 26, 1280, 0, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2521, '', '2024-08-27 13:00:37.63949', '', '2024-08-27 13:00:37.639506', NULL, 5, 1261, 8, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2525, 'ar möchte vorerst keinen regelmässigen Termin mit CTO und ple', '2024-08-27 13:04:23.586735', '', '2024-08-27 13:04:23.586751', 0, 5, 1277, 0, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2540, 'Status Quo', '2024-08-31 11:30:35.949678', '', '2024-08-31 11:30:35.949685', NULL, 4, 1278, 0, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1027, 'Gemäss Migrations-Team sind wir in der Target-Zone. Der Rest wird im nächsten Jahr angepackt.', '2023-12-12 10:41:53.50103', '', '2023-12-12 10:41:53.501035', 70, 31, 213, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1083, 'Keine Veränderung', '2023-12-18 07:39:55.479064', '', '2023-12-18 07:40:05.547835', 0.4, 28, 180, 5, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (2384, 'Mögliche Zusammenarbeit mit Tobias Feiffel (Leadplattform)', '2024-08-08 19:07:30.209903', '', '2024-08-08 19:07:30.209908', NULL, 17, 1378, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1281, 'Idee vorhanden', '2024-01-29 12:21:07.852512', '', '2024-01-29 12:21:07.852518', NULL, 28, 1089, 4, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2414, 'SwissLife ist unterschrieben - bei rund 20% pro Woche sind das im betrachteten Zeitraum rund 6 Wochen - 8160 CHF', '2024-08-12 11:07:59.959541', '', '2024-08-12 11:07:59.959545', 91161, 3, 1312, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2416, 'Keine Veränderung', '2024-08-12 11:08:32.298207', '', '2024-08-12 11:08:32.298212', NULL, 3, 1324, 5, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1214, '6 Members haben vor Ort Teilgenommen und 2 haben ihre Inputs vorgängig eingegeben. ', '2024-01-22 16:16:36.307462', '', '2024-01-22 16:16:36.307465', 44, 29, 1026, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (460, 'Einladungen versendet füren 16. November ', '2023-10-16 07:01:18.43019', 'Themen definitv bestimmen ', '2023-10-15 22:00:00', 0.3, 41, 156, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1240, 'Ein bereits angesetztes Meeting musste ausgrund eines Terminkonflikts verschoben werden. Wir rechnen damit, dass sich die Situation in den kommenden Wochen klärt (grünes Licht oder nicht)

Weitere Optionen sind parallel in Prüfung.', '2024-01-25 07:53:41.55665', '', '2024-01-25 07:55:12.157886', 0, 5, 1057, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1270, 'Tobis Blogpost im Puzzle Newsletter Ende Januar', '2024-01-29 08:41:43.864797', '', '2024-01-29 08:41:43.864802', 1, 36, 1056, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1269, 'immer noch Phippu, Tobi H. und Tobi S. auf der Bench', '2024-01-29 08:41:02.641393', 'Mandate verlängern und ausbauen!
Sales! Sales! Sales', '2024-01-29 09:39:31.135237', 1.95, 36, 1053, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1273, 'Angebot wird diese Woche auf Website publiziert', '2024-01-29 08:45:28.045126', 'Am Team Morgen alles fix finalisieren und dann publizieren', '2024-01-29 09:41:01.282201', NULL, 36, 1076, 7, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1340, '78h im Monat Januar ist zu hoch, für Feburar wurden bereits Masnahmen definiert ', '2024-02-12 10:29:57.64355', '', '2024-02-12 10:29:57.643555', NULL, 41, 1091, 4, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1370, 'Keine Veränderung bis zum vierten Termin im März. Confidence hoch, da im letzten Meeting nur noch 4 TN notwendig, damit wir in Target Zone bleiben.', '2024-02-14 14:27:35.902928', '', '2024-02-14 14:29:38.656126', 75, 29, 1026, 8, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1385, 'Berti und Mayra können evtl. bei SAC diese Woche verrechenbare Std. leisten', '2024-02-19 08:14:12.671009', '', '2024-02-19 08:14:12.671011', NULL, 36, 1049, 6, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1410, '- Überarbeitung Cloud Angebot noch in Review
- Erarbeitung Referenzen on-going
- Organisation für Puzzle Lunch on-going
- weitere Members haben oder werden Zertifizierung starten
- Account Planning mit AWS wird nach Ferien von Simu in Angriff genommen', '2024-02-22 07:17:57.829654', '', '2024-02-22 07:17:57.829656', NULL, 16, 1013, 5, 'ordinal', 'FAIL', 2);
INSERT INTO okr_pitc.check_in VALUES (1423, '', '2024-02-26 07:33:23.631466', '', '2024-02-26 07:33:23.63147', 7, 27, 1107, 8, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1478, '', '2024-03-04 08:41:29.817383', '', '2024-03-04 08:41:29.81739', NULL, 28, 1087, 4, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (1479, 'Neuer PL gefunden und bei Dev/tre angestellt', '2024-03-04 08:41:51.559712', '', '2024-03-04 08:41:51.559719', NULL, 28, 1089, 7, 'ordinal', 'TARGET', 2);
INSERT INTO okr_pitc.check_in VALUES (1480, '', '2024-03-04 08:52:47.800777', '', '2024-03-04 08:52:47.800784', 8, 27, 1107, 8, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1481, '', '2024-03-04 08:53:02.931284', '', '2024-03-04 08:53:02.931291', 3, 27, 1108, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1816, '', '2024-04-29 14:21:32.390982', '', '2024-04-29 14:21:32.391004', 4.5, 7, 1239, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1822, 'Wir starten in diesen Tagen mit der Bereinigung der SEO Probleme. Die Blogpost Datenbank wurde ab 2020 bis heute bereits bereinigt. Nun gehen wir an die SEO Tasks.', '2024-05-01 13:44:03.320744', '', '2024-05-01 13:44:03.320762', NULL, 49, 1179, 8, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1997, 'Leider keine Referenz.', '2024-05-27 08:25:56.23741', '', '2024-05-27 08:25:56.237413', NULL, 27, 1205, 9, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1998, 'Noch keine News.', '2024-05-27 08:26:08.735639', '', '2024-05-27 08:26:08.735643', NULL, 27, 1190, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2007, 'Struktur wird diese Woche abgeschlossen. erste Texte sind im Wordpress', '2024-05-28 06:52:22.235734', '', '2024-05-28 06:52:22.235765', 8, 49, 1180, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2274, 'Es hat ein erstens Gespräch stattgefunden.', '2024-07-15 11:04:45.378456', '', '2024-07-15 11:04:45.37846', NULL, 32, 1255, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2289, 'Einladung von Berti für Workshop im August', '2024-07-16 06:51:07.119896', '', '2024-07-16 06:51:07.119899', NULL, 5, 1267, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2353, 'Keine Veränderung', '2024-08-05 06:59:56.789297', '', '2024-08-05 06:59:56.789301', NULL, 13, 1247, 1, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2361, 'Keine Veränderung seit letzter Woche, Start diese Woche mit Pascou. ', '2024-08-05 07:10:03.403984', '', '2024-08-05 07:10:03.403988', NULL, 3, 1317, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2454, '', '2024-08-19 11:22:16.421458', '', '2024-08-19 11:22:16.421461', 2735564, 13, 1241, 2, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2459, 'Score aktuell bei 83', '2024-08-20 07:22:18.018567', '', '2024-08-20 07:22:18.01857', NULL, 49, 1281, 9, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2460, '', '2024-08-21 05:53:01.655401', '', '2024-08-21 05:53:01.655404', NULL, 5, 1261, 8, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2474, 'Hallo zäme

Gerne geben wir euch ein erstes Statusupdate hinsichtlich CRM Projekt.

Wir haben eine erneute Evaluation der möglichen CRM Lösungen vorgenommen und dabei auch mitberücksichtigt ob die Lösung auch zukunftsorientiert (ERP usw.) für uns passen könnte. Ebenfalls ein Kriterium war ob die Lösung auch von Partnerfirmen eingesetzt wird oder es ähnliche Referenzen gibt. Ebenso ob es in der Schweiz Provider und Implementationspartner für die Lösung gibt.
Bei der Evaluation wurd die Wahl von Odoo bestätigt. Spannende Alternativen wären ERPNext, vtier CRM oder SuiteCRM, welche im Gesamtranking aber alle hinter Odoo liegen.
Die ganze Evaluation https://files.puzzle.ch/f/7065688

Wie weiter:
Als nächste Schritte werden wir in die Anbieterevaulation für Odoo starten (Betrieb und Implementation).  Dazu findet im September ein Austausch mit CamptoCamp statt, welche sowohl Hosting wie auch Implementationsbegleitung bieten würden.', '2024-08-22 06:37:24.125236', '', '2024-08-22 06:37:46.063964', NULL, 24, 1350, 4, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2485, 'Vertrag mit Sven abgeschlossen. Start im November. Auslastung könnte über IPR Monatsbericht weitgehend sichergestellt werden. Einsatz in verrechenbarem Kundenprojekt bevorzugt. Betreuungsdetails noch nicht geklärt', '2024-08-22 11:24:14.056233', 'Betreuung klären (Gespräche mit dsm und da)', '2024-08-22 11:24:14.056235', NULL, 33, 1305, 7, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2492, '✅ Kundenumfrage ist ausgewertet und 3 potenzielle Massnahmen sind daraus abgeleitet.(Bericht folgt)
✅ Partner Programm Konzept steht (okr_pitch bei MarCom und in GL folgt)', '2024-08-22 13:18:28.744654', '', '2024-08-22 13:18:28.744656', 4, 24, 1311, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2504, 'Status Quo', '2024-08-26 08:26:57.677335', '', '2024-08-26 08:26:57.677339', NULL, 4, 1278, 0, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2505, 'Status Quo', '2024-08-26 08:27:34.176873', 'Gemäss Beschrieb', '2024-08-26 08:27:34.176875', NULL, 4, 1288, 10, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (2506, 'Abgleich mit /mobility, Dokument erstellt', '2024-08-26 08:28:08.81806', '', '2024-08-26 08:28:08.818062', NULL, 4, 1290, 0, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2507, 'Status Quo', '2024-08-26 08:28:30.874374', '', '2024-08-26 08:28:30.874384', NULL, 4, 1292, 0, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2508, 'Status Quo', '2024-08-26 08:28:44.09211', '', '2024-08-26 08:28:44.092113', NULL, 4, 1293, 7, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1028, '2 intere Projekte wurden mit Dagger ergänzt.
3 Contibutions wurden bei Dagger gemacht.
Viele interne Leads - nichts konkretes
Dagger Learnings aus /mid Week in Blogpost gepackt - Sollte noch in diesem Jahr raus. - Gehen das Thema im neuen Jahr etwas konkreter und mit Konzept nach. Diese Phase war eher chaotisch und wenig strukturiert. Learnings waren aber sehr gross.', '2023-12-12 11:05:32.618501', '', '2023-12-12 11:05:32.618504', 65, 31, 196, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1688, 'N/A', '2024-04-15 12:33:38.135283', '', '2024-04-15 12:33:38.135321', NULL, 4, 1224, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1084, '- Infoanlass Vertonung Stattgefunden
- Zammad Grafana Integration in Arbeit
- Team darauf sensibilisiert, dass sie regelmässiger Feedback in Tickets geben', '2023-12-18 07:49:39.426823', '', '2023-12-18 07:49:39.426833', 0.5, 67, 189, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1175, 'keine Veränderung', '2024-01-15 15:10:44.751821', '', '2024-01-15 15:10:44.751823', 0, 36, 1048, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1177, 'leider noch keine Veränderung', '2024-01-15 15:13:17.917032', '', '2024-01-15 15:13:17.917035', 1.95, 36, 1053, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1275, 'Neukategorisierung vorgenommen.

Neu Kategorie A:
Kantonsspital Graubünden

Neu nur noch Kategorie B (für Erreichung des KR als erledigt betrachtet):
TX Group AG (ehem. Tamedia)
SwissSign AG
Swisscom Health AG
Swiss Re Management Ltd', '2024-01-29 08:52:54.088794', '', '2024-01-29 08:52:54.088801', 4, 33, 1092, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1341, 'weiterhin viele Bugs... ', '2024-02-12 10:31:42.126896', 'Das Thema Qulaität udn nacharbeit wurde am, Retro angesrpocihen und die Members sensibilisert...
Möglichkeit eines tech. Audit prüfen -> odi', '2024-02-12 10:31:42.1269', 0.3, 41, 1066, 2, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1371, 'Austausch mit Marc Hoffmann mtrail https://puzzleitc.highrisehq.com/notes/716112574', '2024-02-14 15:30:58.253541', '', '2024-02-14 15:30:58.253545', NULL, 5, 1022, 5, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (1387, 'Newsletter ging raus mit dem Beitrag über den Visualisierungsworkshop und dem UX Bern Meetup Event', '2024-02-19 08:17:26.379098', 'Tobi S. Beitrag im nächsten Newsletter platzieren', '2024-02-19 08:17:26.379101', 2, 36, 1056, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1388, 'keine Veränderung. Diese Woche hosten wir allerdings noch den Event vom UX Meetup Bern', '2024-02-19 08:18:54.256657', '', '2024-02-19 08:18:54.25666', NULL, 36, 1058, 9, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1389, 'keine Veränderung. Wird am 23.02.24 diskutiert', '2024-02-19 08:20:34.341686', '', '2024-02-19 08:20:34.34169', NULL, 36, 1061, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1411, '- Konzept in Erarbeitung
- Go To Market Unterstützung von Phil Meier kann auf Konzept einzahlen (Blue print)', '2024-02-22 07:20:00.360834', '', '2024-02-22 07:20:00.360837', NULL, 16, 1051, 2, 'ordinal', 'FAIL', 3);
INSERT INTO okr_pitc.check_in VALUES (1424, 'Retro für 4.3. geplant', '2024-02-26 07:56:16.548046', '', '2024-02-26 07:56:16.548049', 2, 27, 1108, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1482, '', '2024-03-04 08:53:46.927782', '', '2024-03-04 08:53:46.927789', 85, 27, 1111, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1497, '', '2024-03-05 05:19:37.362525', '', '2024-03-05 05:19:37.362531', 3950836, 13, 1032, 10, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1505, 'An 12 von 15 identifizerten Events teilgenommen (Messung bis und mit 05.03.24).', '2024-03-06 07:07:23.413117', '', '2024-03-06 07:08:01.490966', NULL, 16, 1011, 10, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (1508, '- Konzept in Erarbeitung', '2024-03-06 07:20:16.348176', '', '2024-03-06 07:20:16.348181', NULL, 16, 1051, 1, 'ordinal', 'COMMIT', 3);
INSERT INTO okr_pitc.check_in VALUES (1514, 'Wir haben nun ein Script für die Wagon Komplexität ', '2024-03-07 12:29:42.658699', '', '2024-03-07 12:29:42.658706', NULL, 41, 1070, 2, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (514, 'SM möchte jbr wieder bestellen. Termin mit Poi (SM von Beat) aufgesetzt', '2023-10-25 14:20:32.339358', '', '2023-10-24 22:00:00', 0.25, 5, 167, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1515, 'kein Änderung', '2024-03-08 16:39:31.029482', '', '2024-03-08 16:39:41.934073', NULL, 20, 1022, 1, 'ordinal', 'COMMIT', 3);
INSERT INTO okr_pitc.check_in VALUES (1519, '', '2024-03-11 07:12:48.463724', '', '2024-03-11 07:12:48.463728', NULL, 5, 1036, 1, 'ordinal', 'COMMIT', 4);
INSERT INTO okr_pitc.check_in VALUES (1520, '', '2024-03-11 07:13:02.285262', '', '2024-03-11 07:13:02.285267', NULL, 5, 1027, 8, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1521, '', '2024-03-11 07:13:36.581574', '', '2024-03-11 07:13:36.581596', NULL, 5, 1027, 8, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1530, 'Leads
* Schweizerisches Rotes Kreuz
* SHKB
* PF
* Medidata
* Tailor IT
* Securitas
Gewonnen: Securitas', '2024-03-12 07:27:04.428651', '', '2024-03-12 07:27:04.428655', NULL, 24, 1065, 9, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1540, 'Sofort + April (ohne okr_pitcDE)', '2024-03-13 09:37:14.422655', '', '2024-03-13 09:37:14.422658', 2.1, 13, 1034, 9, 'metric', NULL, 6);
INSERT INTO okr_pitc.check_in VALUES (1541, '', '2024-03-13 09:38:23.720499', '', '2024-03-13 09:38:23.720501', 3972456, 13, 1032, 10, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1546, 'Wir sind daran, dass textliche und visuelle Grundgerüst zu erarbeiten. Der Designentwurf der Startseite ist freigegeben. ', '2024-03-14 09:51:18.923365', '', '2024-03-14 09:51:18.92337', NULL, 49, 1116, 9, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (1548, 'Moser Bär wird Ende März publiziert.
Ansonsten leider keine Referenzen.', '2024-03-14 09:54:41.708655', '', '2024-03-14 09:54:41.708657', 10, 26, 1045, 3, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1558, 'über 3 Moante sind wir übre dem Budget ', '2024-03-15 08:41:20.60263', '', '2024-03-15 08:41:20.602633', NULL, 41, 1091, 4, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1571, '', '2024-03-18 07:58:20.886814', '', '2024-03-18 07:58:20.886816', 10, 27, 1107, 8, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1573, '', '2024-03-18 07:59:03.041254', '', '2024-03-18 07:59:03.041256', 9, 27, 1110, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1596, 'Definierter Ablauf wird für GJ-Q4, also jetzt, erstmals umgesetzt.', '2024-03-19 06:18:09.127522', '', '2024-03-19 06:18:09.127524', NULL, 33, 1077, 7, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (1606, 'keine weiteren Besuche', '2024-03-25 10:17:32.275487', '', '2024-03-25 10:17:32.27549', 3.5, 32, 1029, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1614, '', '2024-03-27 08:24:09.160915', '', '2024-03-27 08:24:09.160924', NULL, 5, 1027, 10, 'ordinal', 'STRETCH', 0);
INSERT INTO okr_pitc.check_in VALUES (1615, 'in Erarbeitung, Fokustage eingeführt, internes Audit findet am 30. Mai statt', '2024-03-28 14:00:44.156854', '', '2024-03-28 14:00:44.156861', NULL, 13, 1129, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1616, '', '2024-03-28 14:02:10.435636', '', '2024-03-28 14:02:32.571415', 2471130, 13, 1134, 6, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1617, '', '2024-04-02 06:40:45.711575', '', '2024-04-02 06:40:45.71158', 92424, 36, 1191, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1618, 'Mögliche Aufträge für Mayra bei Mobi, für Berti evtl. punktuell bei BKD', '2024-04-02 06:41:14.232472', '', '2024-04-02 06:41:14.232477', NULL, 36, 1206, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1621, '', '2024-04-02 06:43:51.067297', '', '2024-04-02 06:43:51.067302', 66, 36, 1214, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1622, 'Termin für Workshop erstellt', '2024-04-02 06:44:38.066401', '', '2024-04-02 06:44:38.066405', NULL, 36, 1215, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1623, 'Termin und Roadmap für Bereichsstrategie ist erstellt', '2024-04-02 06:45:11.927657', '', '2024-04-02 06:45:11.927662', NULL, 36, 1218, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1631, 'Opportunitäten:
- BAFU: Daten und Digitalisierung (PDD) (Einladungsverfahren)', '2024-04-03 18:06:38.667367', '', '2024-04-03 18:06:38.667372', NULL, 16, 1176, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1632, 'Monat hat erst begonnen. Noch kein Umsatz.', '2024-04-03 18:09:27.097901', '', '2024-04-03 18:09:46.510331', 0, 16, 1177, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1635, 'Arbeiten noch nicht gestartet.
Bisher gibt es eine Analyse von P32/P14 durch MarCom.

Arbeitsstand Analyse: https://codimd.puzzle.ch/ZdHODTltQUSQPiu7AwR9EA', '2024-04-04 06:13:52.940812', 'Abstimmung mit den relevanten Stellen', '2024-04-04 11:35:52.329911', NULL, 24, 1131, 3, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1643, 'Wert um 2% gesteigen. nice.', '2024-04-08 07:01:20.545535', '', '2024-04-08 07:01:20.545539', 43, 32, 1157, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1657, 'Openshift migr ist ein plan vorhanden, Noch keine Aktivitäten', '2024-04-10 13:16:47.761471', '', '2024-04-10 13:16:47.761475', NULL, 28, 1164, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1641, 'Aufgrund Zeitmangel noch keine Arbeiten erfolgt', '2024-04-08 06:59:56.911499', 'Meine Zeit besser einteilen', '2024-04-08 08:37:03.328215', NULL, 32, 1159, 2, 'ordinal', 'FAIL', 2);
INSERT INTO okr_pitc.check_in VALUES (1029, 'Wert PTime für das gesamte Quartal bisher. Tiefe Auslastung mangels genügend Projekten führt trotz hoher Verrechenbarkeit zu tiefer abs. Verrechenbarkeit', '2023-12-12 12:39:26.16683', 'Kurzfristig keine Möglich', '2023-12-12 12:39:26.166834', 48, 3, 217, 1, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (554, 'Die Mitte angeschrieben ', '2023-10-30 11:35:30.518132', '', '2023-10-29 23:00:00', 0.1, 41, 154, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2114, '', '2024-06-13 12:19:37.515309', '', '2024-06-13 12:19:37.515323', 0, 5, 1144, 0, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1087, 'Laut Kilu/Reto wird diese Woche (KW51) das erste Modul veröffentlicht', '2023-12-18 09:52:49.029918', '', '2023-12-18 09:59:39.595275', 2.5, 32, 176, 5, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1086, 'Keine Veränderung, keine Anfragen', '2023-12-18 09:51:23.677419', '', '2023-12-18 14:22:11.75293', 0, 32, 175, 1, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1181, 'Angebot praktisch ready und TH wird es nach seinen Ferien auf unsere Website pushen', '2024-01-15 15:16:10.429033', '', '2024-01-15 15:16:10.429035', NULL, 36, 1076, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1176, 'Beim IGE können wir Phippu offerieren', '2024-01-15 15:11:39.303331', '', '2024-01-15 15:17:20.170418', NULL, 36, 1049, 6, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1242, 'Neue Leads
* Schweizerisches Rotes Kreuz
* SHKB
* PF

Aktuell fehlt noch ein Abschluss für Commit', '2024-01-25 09:00:27.807738', '', '2024-01-25 09:00:27.807742', NULL, 24, 1065, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1276, 'Securitas Offerte ist raus!', '2024-01-29 10:09:39.310564', '', '2024-01-29 10:09:39.310573', NULL, 31, 1099, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1342, 'Erste Gedanken habe nwir uns gemacht sowie Termin fixiert.
Fokusthema Security ', '2024-02-12 10:32:39.502541', '', '2024-02-12 10:32:39.502546', NULL, 41, 1085, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1390, 'keine Veränderung', '2024-02-19 08:22:28.751671', 'Sales für den Workshop!', '2024-02-19 08:22:28.751673', NULL, 36, 1076, 7, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (1391, 'SBB Referenzen von Jürgen seitens SBB bewilligen lassen', '2024-02-19 08:23:41.189195', '', '2024-02-19 08:23:41.189197', NULL, 36, 1105, 8, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1412, '- Interview und Kennenlernen Sales-Team hat mit einem Kandidat stattgefunden
- ein weiterer Kandidat wird noch eingeladen', '2024-02-22 07:21:09.564709', '', '2024-02-22 07:21:09.564711', 0, 16, 1057, 6, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1425, '', '2024-02-26 07:56:47.904563', '', '2024-02-26 07:56:47.904567', 8, 27, 1109, 6, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1426, '', '2024-02-26 07:57:01.632693', '', '2024-02-26 07:57:01.632695', 5, 27, 1110, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1483, 'keine Änderung', '2024-03-04 09:20:24.006838', 'Ausschreibungen prüfen', '2024-03-04 09:20:24.006844', 25, 17, 1059, 6, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1486, 'Diehl, Juris, MPI Mikro', '2024-03-04 09:24:41.436557', 'Bestandskunden anschreiben', '2024-03-04 09:24:41.436571', 20, 17, 1052, 3, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1498, 'Keine Veränderung', '2024-03-05 05:20:14.336379', '', '2024-03-05 05:20:14.336385', NULL, 13, 1035, 0, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1506, 'Nachsubskribierung durch SNB bestellt. Reneval und Mehrjahresvertrag bei SNB in Aussicht. Cilium Swiss Re und LGT nach wie vor offen.', '2024-03-06 07:12:29.571778', '', '2024-03-06 07:12:29.571785', 1, 16, 1012, 3, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1509, '- Interview mit weiterem Kandidaten am 7.3.24', '2024-03-06 07:21:12.472313', '', '2024-03-06 08:13:11.508519', 0, 16, 1057, 6, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1516, 'jbr auf Membersliste', '2024-03-08 16:41:14.071992', '', '2024-03-08 16:41:14.071998', 1, 20, 1031, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1522, 'Erste Massnahmen sind umgesetzt, andere aufgegleist. Gute Grundlage fürs nächste OKR Quartal.', '2024-03-11 07:53:50.592642', '', '2024-03-11 07:53:50.592646', NULL, 28, 1079, 9, 'ordinal', 'STRETCH', 1);
INSERT INTO okr_pitc.check_in VALUES (1531, 'BKD-Weiterentwicklungsprojekt dazu gekommen (nicht wirklich ein Lead, da von Bestandeskunde)', '2024-03-12 10:52:27.465992', 'Dran bleiben, um neue Leads zu generieren', '2024-03-12 10:52:27.465997', 4, 4, 1086, 0, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1542, 'Keine Veränderung', '2024-03-13 09:38:50.575873', '', '2024-03-13 09:38:50.575875', NULL, 13, 1035, 0, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1547, 'Das Testing wird in der ersten Aprilwoche stattfinden, alle Testuser haben zugesagt.', '2024-03-14 09:52:38.085146', '', '2024-03-14 09:52:38.085148', NULL, 49, 1118, 6, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1559, 'An 19 von 24 identifizerten Events teilgenommen (Messung bis und mit 15.03.24).', '2024-03-15 08:42:55.473134', '', '2024-03-15 08:42:55.473137', NULL, 16, 1011, 10, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (1572, '', '2024-03-18 07:58:36.445373', '', '2024-03-18 07:58:36.445375', 10, 27, 1109, 6, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1597, 'Entwicklungsworkshop fand in diesem vollen Quartal keinen Platz.', '2024-03-19 06:25:13.983924', 'Wir definieren im März-Teammeeting ob, wan und in welcher Form wie den Workshop durchführen. Tendenz zu "dezentraler" Besprechung der Ausrichtung mit den unterschiedlichen "Sub-Teams" (Dev, Sys, etvl. Cloud)', '2024-03-19 06:25:13.983927', NULL, 33, 1097, 1, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1607, 'keine Veränderung', '2024-03-25 10:17:46.068576', '', '2024-03-25 10:17:46.068579', 41, 32, 1030, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1608, 'Übersicht für Supportprozess ITPoint erstellen', '2024-03-25 10:18:25.578513', '', '2024-03-25 10:18:25.578517', 3, 32, 1037, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1610, 'Aufräumarbeiten sind erfolgt, damit fettiiiig', '2024-03-25 10:19:07.201907', '', '2024-03-25 10:19:07.20191', 100, 32, 1039, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1619, 'Termine und Roadmap erstellt', '2024-04-02 06:41:40.73829', '', '2024-04-02 06:41:40.738295', NULL, 36, 1208, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1634, '- die Marktanalyse ist noch in Erarbeitung
- Abklärungen Kampagne mit Carlos werden in Angriff genommen', '2024-04-03 18:12:57.457706', '', '2024-04-03 18:12:57.45771', NULL, 16, 1178, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1636, 'Es wurden 4 neue Stellen ausgeschrieben.', '2024-04-04 06:34:40.792811', '', '2024-04-04 06:34:40.792815', NULL, 22, 1128, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1642, 'Aufgrund mangelnder Zeit keine Arbeiten erfolgt', '2024-04-08 07:00:36.891633', '', '2024-04-08 08:40:47.145509', 0, 32, 1160, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1658, 'Keine Aktivitäten. Nicht so prio neben Tages/Projektgeschäft', '2024-04-10 13:17:23.036638', '', '2024-04-10 13:17:23.036642', NULL, 28, 1231, 2, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1817, 'Open Space am Leadership Monthly durchgeführt', '2024-04-29 15:23:22.379294', '', '2024-04-29 15:23:22.379314', NULL, 31, 1188, 10, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1823, 'Schulungstermin am 13. Mai für sje & jh. Anschliessend Teameinführung und Start mit bestehendem & freigegebenem Content', '2024-05-01 13:45:08.419793', '', '2024-05-01 13:45:08.419813', 5, 49, 1180, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1633, '- in GL erstes Review der MO und Ausblick neue MO vorgenommen
- Frühlings-LST Workshop mit Daniel Osterwalder vorbesprochen', '2024-04-03 18:11:44.828317', '', '2024-05-02 06:08:21.4669', NULL, 16, 1132, 8, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1999, '', '2024-05-27 09:27:18.09449', '', '2024-05-27 09:27:18.094493', NULL, 60, 1188, 10, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2008, 'Wir sind dran. ', '2024-05-28 07:18:15.932529', '', '2024-05-28 07:18:15.93255', 8, 49, 1200, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2009, '', '2024-05-28 07:25:52.894287', '', '2024-05-28 07:25:52.894308', NULL, 26, 1182, 8, 'ordinal', 'TARGET', 2);
INSERT INTO okr_pitc.check_in VALUES (2011, 'Give Away Konzept wird am Puzzle Inside Ende Juni vorgestellt
Nachhaltiges Give Away Puzzle up (Druckvelo) in Produktion
Interaktive Inszenierung am Puzzle up! sichergestellt (Rollup, Druckvelo)', '2024-05-28 07:30:59.102165', '', '2024-05-28 07:30:59.102186', NULL, 26, 1184, 7, 'ordinal', 'STRETCH', 1);
INSERT INTO okr_pitc.check_in VALUES (2017, 'Bedürfnisse Divisions wurden am Monthly abgeholt und wurden bereits besprochen. Integration und Leads noch offen.', '2024-05-29 06:44:41.137811', '', '2024-05-29 06:44:41.137824', NULL, 24, 1131, 5, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2019, '', '2024-05-30 04:52:24.495543', '', '2024-05-30 04:52:24.495552', 3318419, 13, 1134, 4, 'metric', NULL, 8);
INSERT INTO okr_pitc.check_in VALUES (2020, 'N/A', '2024-05-30 14:12:35.375084', 'Massnahmen und Messpunkte zu den 3 Fokusthemen definieren', '2024-05-30 14:12:35.375092', NULL, 4, 1194, 0, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2293, 'keine Veränderung', '2024-07-22 08:07:57.203061', '', '2024-07-22 08:07:57.203064', 83001, 36, 1312, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1030, 'Zu wenig Umfragen für Checkin seit letzter Messung (Messung am WACCAE ohne Eingabe im Tool)', '2023-12-12 12:49:10.411323', '', '2023-12-12 12:49:10.411327', 8, 3, 210, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1088, 'Entwicklung von 4 Modulen wird wohl nicht erreicht werden.', '2023-12-18 09:53:50.516596', '', '2023-12-18 09:59:46.391281', 2.5, 32, 176, 2, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1178, 'Wir haben bereits einige Blogpost und Referenzen vorproduziert. Jedoch noch nicht publiziert', '2024-01-15 15:14:13.053353', '', '2024-01-15 15:18:25.834127', 0, 36, 1056, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1243, 'Zuviele Bugs und Aufwand nach dem Bootstrap Update
Jubla hatten wir 1 Jahr nicht released', '2024-01-25 11:53:09.612356', 'Alle sind auf dem selben Stand
Testing von Kunden besser koordinieren', '2024-01-25 11:53:09.612359', 0.4, 41, 1066, 3, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (555, 'Erledigt; Grafana OnCall Evaluation, Grafana onCall Team von 3 Members', '2023-10-30 11:36:53.693189', '', '2023-10-29 23:00:00', 0.2, 24, 135, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (557, 'Erster Austausch am Monthly 30.10. findet statt.', '2023-10-30 11:37:56.299374', '', '2023-10-29 23:00:00', 0.2, 24, 132, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1277, 'MAG fast vollständig geplant', '2024-01-29 12:00:00.860191', 'Planung abschliessen, Massnahmenliste erstellen', '2024-01-29 12:00:00.860199', NULL, 28, 1079, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1343, 'Im Kernteam Meeting am 7. Februar wurde entschieden, dass der Fokus vorerst auf der Definition «Digitale Lösungen» und einem Konzept liegt. In einem Kleineren Kernteam werden nun entsprechende Punkte für ein Konzept erarbeitet und MarCom unterstützt dies mit jh im Kernteam. Vorerst ist kein Blogpost geplant. es gibt div. MarCom Massnahmen, die während der Konzeption erarbeitet werden. Deshalb wird dieses OKR nicht mehr wöchentlich eingechecked und gilt als Fail.
', '2024-02-12 10:58:13.193972', '', '2024-02-12 10:58:13.193976', NULL, 49, 1047, 0, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1393, 'Zusammen mit Markom erste Angebotsformulierung erfasst', '2024-02-19 11:34:48.144315', 'Abstimmung /mid und Markom organisieren', '2024-02-19 11:34:48.144317', NULL, 40, 1063, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1413, 'Wir sind auf Kurs aber keine grosse Veränderung zum letzten Checkin.', '2024-02-22 09:51:05.824212', '', '2024-02-22 09:51:05.824214', NULL, 24, 1065, 7, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1427, '', '2024-02-26 07:57:57.162111', '', '2024-02-26 07:57:57.162117', 82, 27, 1111, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1484, 'Keine Veränderung', '2024-03-04 09:20:44.876941', '', '2024-03-04 09:20:53.614094', 0, 17, 1060, 1, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1487, 'Juris, Popken', '2024-03-04 09:25:04.865936', '', '2024-03-04 09:25:04.865943', 3, 17, 1054, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1499, 'Die Wireframes sind abgenommen, das Designbriefing ist erfolgt. Wir werden diese Woche den ersten Entwurf des Designs anschauen und Feedback geben.', '2024-03-05 07:24:11.401705', '', '2024-03-05 07:24:11.401712', NULL, 49, 1116, 9, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (561, '>30% bearbeitet', '2023-10-30 11:46:30.6314', '', '2023-10-29 23:00:00', 30, 31, 212, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1574, '', '2024-03-18 07:59:53.457214', '', '2024-03-18 07:59:53.457216', 86, 27, 1111, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1507, '- Cloud Angebot überarbeitet (done)
- Erarbeitung Referenzen on-going
- Organisation für Puzzle Lunch gestartet (Durchführung GJQ4)
- Zertifierung abgeschlossen: 2024 Phil Matti (AWS) - Solution Architect (associate)
- Zertifierung von früher: 2021: Clara Llorente Lemm - Cloud Practitioneer
- weitere Members haben oder werden Zertifizierung starten (Chrigu Haller, Chrigu Fahrni, Martin Käser)
- Account Planning mit AWS noch offen', '2024-03-06 07:17:21.603643', '', '2024-03-06 07:19:15.295681', NULL, 16, 1013, 5, 'ordinal', 'COMMIT', 4);
INSERT INTO okr_pitc.check_in VALUES (1517, 'Umfrage wird am 11.3. im Zoo und Banditos Team durchgeführt. Confidence hoch.', '2024-03-08 16:44:58.341949', '', '2024-03-08 16:44:58.341954', 0, 20, 1024, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1518, 'Umfrage wird am 11.3. im Zoo und Banditos Team durchgeführt. Confidence hoch.', '2024-03-08 16:45:10.452765', '', '2024-03-08 16:45:10.45277', 0, 20, 1025, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1523, 'Ideen von Members eingebracht.', '2024-03-11 07:54:15.871937', '', '2024-03-11 07:54:15.871941', NULL, 28, 1080, 7, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (1532, 'Erhalten: cADC-Swisscom 2024 Fertigstellung/ Mobi JBat2>3-Migration/ BKD UX-Konzept/ BKD-Nacharbeiten/ Mobi Gebäudeversicherung (= Bereichsübergreifender Auftrag)/ Peerdom', '2024-03-12 10:56:30.136776', 'Keine weiteren Massnahmen.', '2024-03-12 10:56:30.13678', NULL, 4, 1090, 10, 'ordinal', 'STRETCH', 1);
INSERT INTO okr_pitc.check_in VALUES (1533, 'Clara noch auf Membersliste belassen', '2024-03-12 10:58:24.783875', 'Abklärung ob onboarding in bestehenden /dev/tre-Aufträgen Sinn macht, oder ob allenfalls Bereichsübergreifend gearbeitet wird', '2024-03-12 10:58:24.783881', 5, 4, 1081, 9, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1543, 'auch am letzten Sprintmeeting haben wir mit allen geredet, die weniger al eine 8 eingetragen haben .', '2024-03-13 13:10:55.932975', '', '2024-03-13 13:10:55.932979', 100, 32, 1040, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1549, 'März: Produktion "UX" (Simon Hirsbrunner)/ Veröffentlichung Security (Mark Zeman)
Mai: Publikation Ansible (Florian Studer)
Juli: Publikatoin UX', '2024-03-14 09:55:18.040659', '', '2024-03-14 09:55:18.040662', NULL, 26, 1046, 8, 'ordinal', 'COMMIT', 4);
INSERT INTO okr_pitc.check_in VALUES (1560, '- Cloud Angebot überarbeitet (done)
- Erarbeitung Referenzen on-going
- Organisation für Puzzle Lunch gestartet (Durchführung GJQ4)
- Zertifierung abgeschlossen: 2024 Phil Matti (AWS) - Solution Architect (associate)
- Zertifierung von früher: 2021: Clara Llorente Lemm - Cloud Practitioneer
- Zertifizierung abgeschlossen: 2024 Chrigu Fahrni (AWS) - Solution Architect (associate)
- weitere Members haben oder werden Zertifizierung starten (Chrigu Haller, Chrigu Fahrni, Martin Käser)
- Account Planning mit AWS noch offen', '2024-03-15 08:54:08.75991', '', '2024-03-15 08:54:08.759913', NULL, 16, 1013, 2, 'ordinal', 'COMMIT', 4);
INSERT INTO okr_pitc.check_in VALUES (1392, 'Members nutzen ihre Ausbildungstage.
cfahrni und challer machen ebenfalls AWS Solutions Architect', '2024-02-19 11:32:49.04015', '', '2024-03-18 09:42:37.345674', NULL, 40, 1062, 8, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (1598, 'Entwicklungsworkshop fand in diesem vollen Quartal keinen Platz.', '2024-03-19 06:25:13.995229', 'Wir definieren im März-Teammeeting ob, wan und in welcher Form wie den Workshop durchführen. Tendenz zu "dezentraler" Besprechung der Ausrichtung mit den unterschiedlichen "Sub-Teams" (Dev, Sys, etvl. Cloud)', '2024-03-19 06:25:13.995231', NULL, 33, 1097, 1, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1609, 'nicht dzau gekommen', '2024-03-25 10:18:38.049508', '', '2024-03-25 10:18:38.049512', 25, 32, 1038, 1, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1611, 'Gespräche sind erfolgt, resp waren nicht nötig', '2024-03-25 10:19:28.464833', '', '2024-03-25 10:19:28.464837', 100, 32, 1040, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1620, 'SoMe Plan erstellt. Fangen erst diese Woche an', '2024-04-02 06:42:07.976873', '', '2024-04-02 06:42:07.976878', 0, 36, 1210, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1637, '', '2024-04-04 12:57:02.648214', '', '2024-04-04 12:57:02.648218', NULL, 20, 1135, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1638, '', '2024-04-04 12:58:06.394121', '2 Inserate erstellt, eines auf Webseite aufgeschaltet.', '2024-04-04 12:58:06.394126', 0, 20, 1136, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1644, 'keine Veränderung seit letztem Checkin', '2024-04-08 07:27:04.443764', 'Sales! Sales! Sales', '2024-04-08 07:27:04.443768', 92424, 36, 1191, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1645, 'Neue Anfrage für Berti als Workshop Moderator. Muss aber zuerst noch geprüft und offeriert werden', '2024-04-08 07:27:48.055745', '', '2024-04-08 07:27:48.055748', NULL, 36, 1206, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1659, 'Erster sehr grober Entwurf', '2024-04-10 13:17:43.536582', '', '2024-04-10 13:17:43.536586', NULL, 28, 1165, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2010, '', '2024-05-28 07:28:22.010232', '', '2024-05-28 07:28:22.010254', NULL, 26, 1183, 7, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (2018, 'Keine Veränderungen.', '2024-05-29 06:51:30.866411', '', '2024-05-29 06:51:30.866437', NULL, 22, 1128, 8, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1031, 'Keine Veränderung', '2023-12-12 12:50:53.1035', '', '2023-12-12 12:50:53.103505', 0.3, 3, 218, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1089, 'Gespräch mit Saraina zum Platzieren des Angebotes auf Webseite hat stattgefunden, Referenzbericht mit BFH wird angepeilt.', '2023-12-18 09:56:40.198648', '', '2023-12-18 09:56:40.198663', 0.81, 32, 174, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1179, 'AB hat sich für 5 Events angemeldet. Die ersten finden diese Woche statt.', '2024-01-15 15:15:20.479234', '', '2024-01-16 12:29:23.615556', NULL, 36, 1058, 8, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1244, 'noch nicht gestartet ', '2024-01-25 11:54:28.566993', '', '2024-01-25 11:54:28.566995', NULL, 41, 1085, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (606, '-0.4 Livia Affolter', '2023-11-11 15:44:07.719862', '', '2023-11-10 23:00:00', -1.3, 13, 131, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1278, 'Geplant für nächstes Teammeeting', '2024-01-29 12:00:41.291717', '', '2024-01-29 12:04:29.522283', NULL, 28, 1080, 7, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1345, 'Wir haben die Bugs bereits behoben.', '2024-02-12 12:15:53.489378', 'Keine', '2024-02-12 12:15:53.489382', 5, 15, 1120, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1394, '', '2024-02-19 13:24:35.432125', '', '2024-02-19 13:24:35.432128', 1009018, 60, 1093, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1414, 'Zusammenarbeit & Ziel de Kampagne ist fixiert - Vorgehensweise besprochen und abgesegnet. Phase 1 "Marktanalyse" im Gange. Erste Resultate werden Anfangs März erwartet. Kampagnen-Draft dürfte sportlich werden - wir sind aber auf einem sehr guten Weg mit diesem Projekt einen Big-Bang zu landen!', '2024-02-22 09:56:55.620562', '', '2024-02-22 09:56:55.620565', NULL, 31, 1078, 4, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1428, 'Status quo', '2024-02-26 08:11:36.838309', '', '2024-02-26 08:11:36.838314', 0, 26, 1045, 3, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1344, 'Usertests haben gezeigt, dass User die OKRs selbständig erfassen können', '2024-02-12 12:15:10.69936', '', '2024-02-26 09:48:53.530207', NULL, 15, 1119, 0, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1485, 'Keiner Veränderung', '2024-03-04 09:21:07.834939', 'Gespräche mit Bestandskunden führen (v.a. wg. Preiserhöhung Red Hat im April)', '2024-03-04 09:21:50.93635', 0.9, 17, 1104, 7, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1500, '- /sys => nichts in Planung
- /mid/container => in Planung, bei Chrigu
- /mid/cicd => in Planung, bei Adi
- /devtre => Moser Bär beim Kunde für GzD
- /ruby => nicht Planung
- /zh => voraussichtlich Priot, Inputs folgen von Simu
- /security => keine Referenz möglich
- /mobility => keine Referenz in Planung
- /hitobito => Wanderwege in Planung
- /ux => SBB-Referenzen sind offen', '2024-03-05 08:12:09.754158', '', '2024-03-05 08:12:09.754165', 0, 26, 1045, 3, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1501, 'März: Produktion "UX" (Simon Hirsbrunner)/ Veröffentlichung Security (Mark Zeman)
Mai: Publikation Ansible (Florian Studer)
Juli: Publikatoin UX

Planung weiterer Videos im März', '2024-03-05 08:12:36.479275', '', '2024-03-05 08:12:36.479287', NULL, 26, 1046, 8, 'ordinal', 'COMMIT', 4);
INSERT INTO okr_pitc.check_in VALUES (1510, 'Wir haben nebst den Leads den 3rd Level für Rancher bei der Securitas gewonnen.', '2024-03-06 07:47:12.603177', '', '2024-03-06 07:47:12.603183', NULL, 24, 1065, 8, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1525, '', '2024-03-11 07:55:15.120641', '', '2024-03-11 07:55:15.120645', NULL, 28, 1089, 7, 'ordinal', 'TARGET', 2);
INSERT INTO okr_pitc.check_in VALUES (1524, 'Hackathon', '2024-03-11 07:55:07.198425', '', '2024-03-11 14:37:05.764156', NULL, 28, 1087, 8, 'ordinal', 'TARGET', 2);
INSERT INTO okr_pitc.check_in VALUES (1534, 'Ist nicht verlaufen gemäss Chronologie der Zonen (ordinale Messungen). Ergo "Commit" als "gefühltes" Resultat. ', '2024-03-12 11:03:20.226405', 'Keine weiteren, da Leute bis auf weiteres ausgelastet ', '2024-03-12 11:06:45.185592', NULL, 4, 1082, 0, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (1544, 'Abklärungen mit Michael Böbner bezüglich HMS Support erfolgt, Dokumenation für ITpoint überarbeitet und clarified für Supportet', '2024-03-13 13:12:03.956985', '', '2024-03-13 13:12:03.956988', 3, 32, 1037, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1626, 'Start im Mai', '2024-04-02 07:12:45.648744', '', '2024-04-02 07:12:45.648748', 0, 49, 1200, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1550, 'Noch einige Aufträge kurz vor Abschluss', '2024-03-14 10:01:40.315425', '', '2024-03-14 10:55:48.638913', 1073225, 60, 1093, 9, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1561, '- Konzept in Erarbeitung, Desk Research gestartet, Hypothesen definiert, nächste Schritte geklärt', '2024-03-15 08:55:30.112183', '', '2024-03-15 08:55:30.112186', NULL, 16, 1051, 0, 'ordinal', 'COMMIT', 3);
INSERT INTO okr_pitc.check_in VALUES (1575, 'Marktanalyse durchgeführt (Marktdaten, Trends, Mitbwerber, Positionierung & SWOT mit Fokus auf Opportunities). Mehr erreicht als gedacht, Kampagnen-Draft jedoch noch nicht vorhanden. Sind auf sehr gutem Weg.', '2024-03-18 08:37:01.252228', '', '2024-03-18 08:37:13.157198', NULL, 31, 1078, 3, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (1599, 'Lücke im März: 0.2 FTE von Dani. Total: 0.2 FTE -> 2.1% (-0.8 FTE Teamkapazität wg. Austritt Andres) ///
Kumuliert: -1.6 + 6.1 + 12.9 = 17.4', '2024-03-19 06:31:18.188549', '', '2024-03-19 06:31:18.188551', 17.4, 33, 1068, 6, 'metric', NULL, 5);
INSERT INTO okr_pitc.check_in VALUES (1612, 'Definitive Zahlen: CHF 292''966.- für Februar', '2024-03-25 11:10:46.649493', '', '2024-03-25 11:10:46.649498', 292966, 33, 1071, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1613, 'Definitives Februar-Ergebnis liegt vor: 13.9% Marge. Kumuliert: 15.0+10.1+13.9=39.0%', '2024-03-25 11:12:50.004839', '', '2024-03-25 11:12:50.004844', 39, 33, 1069, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1624, 'Startet ab Ende April. Zur Zeit sind wir an der finalen Freigabe für die Designs. ', '2024-04-02 07:10:38.401593', '', '2024-04-02 07:10:38.401598', NULL, 49, 1179, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1625, 'Start wahrscheinlich ab Mai', '2024-04-02 07:12:22.265489', '', '2024-04-02 07:12:22.265495', 0, 49, 1180, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1628, 'Sponsoring-Doku ist erstellt. Am 2. April werden im MarComSales-Weekly die möglichen Partner definiert. Anschliessend folgen die Anfragen via Partnermanager. ', '2024-04-02 07:14:09.09109', '', '2024-04-02 07:14:09.091095', NULL, 26, 1183, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1639, '', '2024-04-04 12:58:26.014455', '', '2024-04-04 12:58:26.014459', NULL, 20, 1139, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1646, 'keine Veränderung seit letztem Check-in', '2024-04-08 07:28:13.098649', '', '2024-04-08 07:28:13.098654', NULL, 36, 1208, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1660, 'Noch keine Aktivitäten', '2024-04-10 13:18:13.081928', '', '2024-04-10 13:18:13.081932', NULL, 28, 1232, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1824, 'Die ersten 6 Grafiken sind bei Jürgen in Bearbeitung. Mayra wird nächste Woche den Rest fertigstellen. Die Animationen sind bis spätestens Ende Mai fertig.
Fotograin: Zweite Fotografin angefragt für Büro- & Alltagssituationen', '2024-05-01 13:46:09.656711', '', '2024-05-01 13:46:09.656729', 5, 49, 1200, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2000, 'Keine Veränderungen', '2024-05-27 09:28:38.111606', '', '2024-05-27 09:29:29.929363', NULL, 60, 1207, 7, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (2012, '', '2024-05-28 07:36:28.289317', '', '2024-05-28 07:36:28.289338', NULL, 26, 1193, 8, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2021, 'N/A', '2024-05-30 14:20:29.3606', 'Erarbeiten der Massnahmen. Weiterarbeit Hand in Hand mit KR ".... Arbeit im Team.... 3 Fokusthemen..."', '2024-05-30 14:20:29.360611', NULL, 4, 1196, 0, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2275, 'Folgende Leads am Start:
- DV Bern
- HMS direkt (Stadt Bern)
- Erweiterung BFH', '2024-07-15 11:05:56.805331', '', '2024-07-15 11:05:56.805334', NULL, 32, 1258, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2294, 'keine Veränderung seit letztem Checkin', '2024-07-22 08:08:49.375259', '', '2024-07-22 08:08:49.375261', NULL, 36, 1322, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2290, 'Austausch mit interessierten Members fand bilateral statt. Folgende Members starten im neuen Branch: Anna, Tru, Pippo, Odi, Bruno. Bruno möchte Branch Owner übernehmen.', '2024-07-19 08:54:09.243354', '', '2024-07-19 13:17:04.361824', NULL, 13, 1245, 10, 'ordinal', 'TARGET', 3);
INSERT INTO okr_pitc.check_in VALUES (2354, '', '2024-08-05 07:00:57.196168', '', '2024-08-05 07:00:57.196174', 2603708, 13, 1241, 2, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1032, 'Keine Veränderung. Das Prinzip funktioniert. Wir überlegen uns Vorschläge zur Integration in PTime zu machen (reduktion Aufwand)', '2023-12-12 12:51:55.884709', '', '2023-12-12 12:51:55.884713', 100, 3, 214, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1091, 'Feedback ist abgeholt, an Weihnachtsanlass präsentiert, Massnahmen getroffen.', '2023-12-18 10:01:31.204529', '', '2023-12-18 10:01:31.204534', 1, 32, 188, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1183, 'JBoss EAP Subscriptionbestellung von Dataport', '2024-01-15 19:19:16.423919', '', '2024-01-15 19:19:38.186264', 25, 17, 1059, 6, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1090, 'Gespräch mit Saraina zum Platzieren des Angebotes auf Webseite hat stattgefunden, Referenzbericht mit BFH wird angepeilt. Seraina wir über Weihnachten / Neujahr in den Ferien sein, das heisst, das wir frühstens Januar veröffentlicht und damit werden wir das KR wohl nicht erreichen in dem Q', '2023-12-18 09:58:37.540291', '', '2023-12-18 09:58:37.540296', 0.45, 32, 172, 2, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1182, '3 Referenzen geschrieben und ready. Phippu ist auch noch eine am schreiben', '2024-01-15 15:16:50.520816', '', '2024-01-15 15:16:50.520818', NULL, 36, 1105, 8, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1245, 'Ab dem nächsten Teamtag jeweils Kurzupdate zu den aktuellen OKRs vorgesehen. Bisher noch nicht passiert.', '2024-01-25 13:16:06.684162', '', '2024-01-25 13:16:06.684178', NULL, 33, 1077, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1248, 'Im Dezember 15.0%. Kumuliert durchschnittlich 15.0%', '2024-01-25 13:26:28.750435', 'Taucher im Januar erwartet. Ab Februar wieder besser. Ab März voraussichtlich wieder gut.', '2024-01-29 09:12:29.655553', 15, 33, 1069, 4, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1600, 'Februar-Abschluss liegt noch nicht vor. Muss in diesem finalen Check-in von einer Prognose von 15% Marge ausgehen.
Im Februar 15%. Kumuliert: 15.0+10.1+15=40.1%', '2024-03-19 06:36:00.13267', 'Effektiver Wert in extra Checkin nach Publikation der Zahlen noch nachführen.', '2024-03-19 06:36:00.132672', 40.1, 33, 1069, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1576, 'Chrigu Fahrni: AWS Solutions Architect Associate
Bidu Schärz: Google Certified Associate Engineer', '2024-03-18 10:22:35.003746', '', '2024-03-19 08:54:01.504416', NULL, 40, 1062, 10, 'ordinal', 'TARGET', 3);
INSERT INTO okr_pitc.check_in VALUES (1247, 'Lücke im Januar: 0.8 FTE von Andres + 0.1 FTE von Dani + 0.3 FTE von Nils + 0.5 FTE von Christian. Total: 1.7 FTE -> 16.6%', '2024-01-25 13:24:40.35844', 'Schnelles Onboarding bei MeteoSchweiz und FZAG forcieren. Austritt von Andres wird März-Ergebnis entlasten.', '2024-01-29 09:44:43.005897', -1.6, 33, 1068, 3, 'metric', NULL, 3);
INSERT INTO okr_pitc.check_in VALUES (1279, 'Micha ausgetreten', '2024-01-29 12:03:05.063311', '', '2024-01-29 12:03:19.330276', 4104, 28, 1083, 6, 'metric', NULL, 6);
INSERT INTO okr_pitc.check_in VALUES (1347, 'Wir konnten weitere Issues abschliessen.', '2024-02-12 12:23:07.84394', '', '2024-02-12 12:23:07.843944', 3, 27, 1110, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1348, '', '2024-02-12 12:23:32.637437', '', '2024-02-12 12:23:32.637442', 5, 27, 1107, 8, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1395, 'Wird erst im Februar ersichtlich', '2024-02-19 13:25:05.343842', '', '2024-02-19 13:25:05.343844', 0, 60, 1094, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1415, 'Objective & KRs haben sich auf Grund der etwas umfänglicheren Kampagne mit Phil (Isovalent) geändert. Dagger-Landingpage auf Puzzle ist online (puzzle.ch/dagger) Use Cases werden aktuell identifiziert -> Danach fliesst dieses Objective in die andere Kampagne ein.', '2024-02-22 10:01:28.233221', '', '2024-02-22 10:01:28.233223', NULL, 31, 1067, 1, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1429, 'März: Produktion "UX" (Simon Hirsbrunner)/ Veröffentlichung Security (Mark Zeman)
Mai: Publikation Ansible (Florian Studer)
Juli: Publikatoin UX

Planung weiterer Videos im März', '2024-02-26 08:12:11.235835', '', '2024-02-26 08:12:11.235837', NULL, 26, 1046, 8, 'ordinal', 'COMMIT', 4);
INSERT INTO okr_pitc.check_in VALUES (1346, 'End to End Tests wurden implementiert, die Testabdeckung ansonsten ist bereits sehr gut.', '2024-02-12 12:22:56.086598', 'Noch weitere Rollenspezifische End To End Tests hinzufügen, die letzte Methode in den Controllern testen.', '2024-02-26 09:49:09.107298', NULL, 15, 1121, 10, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (1488, 'keine Veränderung seit letztem Check-in', '2024-03-04 12:35:11.773861', 'Mobi Deal und Buchknacker Deal gewinnen', '2024-03-04 12:35:11.773868', 129924, 36, 1048, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1490, 'keine Veränderung seit letztem Checkin', '2024-03-04 12:37:12.174326', '', '2024-03-04 12:37:12.174333', 28.48, 36, 1050, 0, 'metric', NULL, 8);
INSERT INTO okr_pitc.check_in VALUES (1502, '', '2024-03-05 10:45:02.904715', '', '2024-03-05 10:45:02.904721', NULL, 5, 1036, 1, 'ordinal', 'COMMIT', 4);
INSERT INTO okr_pitc.check_in VALUES (1504, '', '2024-03-05 10:47:08.066006', '', '2024-03-05 10:47:08.066012', NULL, 5, 1027, 8, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1511, 'oof so close
383''377.85 CHF
+ 15''419.75 CHF von Sylvain
', '2024-03-06 10:33:32.032646', '', '2024-03-06 10:34:12.642166', 398798, 40, 1094, 10, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1526, '', '2024-03-11 07:58:25.533794', '', '2024-03-11 07:58:25.533798', 4602, 28, 1083, 7, 'metric', NULL, 6);
INSERT INTO okr_pitc.check_in VALUES (1535, 'Das ist inzwischen eingelschlafen.', '2024-03-12 11:05:22.172854', 'Bewussterer Umgang damit im nächsten Quartal', '2024-03-12 11:05:34.699276', NULL, 4, 1084, 0, 'ordinal', 'FAIL', 3);
INSERT INTO okr_pitc.check_in VALUES (1545, 'Phippu als P35-Män hatte neben anderen Verpflichtungen zuwenig Zeit das umzusetzen.', '2024-03-13 13:13:09.600331', '', '2024-03-13 13:13:09.600334', 25, 32, 1038, 1, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1551, 'keine Veränderung (BKD', '2024-03-14 16:01:16.000018', 'Sales Aktivitäten hochhalten', '2024-03-14 16:01:16.00002', 129924, 36, 1048, 3, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1562, '- Interview mit Kandidat erfolgt. Ausstehend noch Kennenlernen Sales-Team (2.4.)
- Angebot unterbreitet. Mündlich grünes Licht erhalten. Letzter offener Punkt noch in Klärung.', '2024-03-15 09:00:49.701309', '', '2024-03-15 09:00:49.701312', 0, 16, 1057, 6, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1563, 'Dagger hat Functions und ihr Daggerverse veröffentlicht. Wir konnten bereits Use Cases besprechen und in unsere "Plattform Engineering"-Kampagne miteinbeziehen.', '2024-03-15 09:07:59.088133', '', '2024-03-15 09:07:59.088135', NULL, 31, 1067, 1, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1627, 'Anpassungen wurden mit DB besprochen, Ticket für April-Monthly wird noch erstellt.', '2024-04-02 07:13:02.29328', '', '2024-04-02 07:13:02.293283', NULL, 26, 1182, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1630, 'Austauschmeeting mit /mid am 4.4.2024 geplant.', '2024-04-02 07:17:47.394938', '', '2024-04-02 07:17:47.394943', NULL, 26, 1193, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1640, 'Erste Termine sind aufgesetzt', '2024-04-04 14:00:16.579184', '', '2024-04-04 14:00:16.579191', NULL, 29, 1142, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1647, 'Post und Kommentar über Unfold Event gemacht', '2024-04-08 07:28:42.358994', 'dem SoMe Plan folgen', '2024-04-08 07:28:42.359', 1, 36, 1210, 9, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1661, 'Erster Austausch mit Gerold stattgefunden. Sonst noch nichts.', '2024-04-10 13:18:44.123927', '', '2024-04-10 13:18:44.123933', NULL, 28, 1167, 2, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1662, '', '2024-04-10 13:22:10.190277', '', '2024-04-10 13:22:10.190282', 738694, 28, 1168, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1825, 'Bisher gibt es eine Analyse von P32/P14 durch MarCom sowie eine Gegenüberstellung zu den 5P durch mich.

Arbeitsstand Analyse: https://codimd.puzzle.ch/ZdHODTltQUSQPiu7AwR9EA', '2024-05-02 05:54:46.229356', 'Auslegeordnung mit Divisions planen', '2024-05-02 05:54:46.229371', NULL, 24, 1131, 3, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1826, 'Wir kommen nicht wie geplant vorwärts, da wir zu wenig Zeit finden. ', '2024-05-02 05:55:41.078133', 'Nicole Frey wird uns zusätzlich unterstützen', '2024-05-02 05:55:41.078145', NULL, 13, 1129, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1840, 'Keine Veränderung. Abstimmung mit Saraina heute', '2024-05-06 06:09:39.595429', '', '2024-05-06 06:09:39.595435', 0, 5, 1137, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1841, 'Von Sales kommt noch wenig aktiv.', '2024-05-06 06:10:30.945989', '', '2024-05-06 06:10:30.945996', 0, 5, 1144, 3, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1867, 'Es sind noch nicht alle Verlängerungen ab Juli unter Dach und Fach', '2024-05-07 11:53:20.515084', '', '2024-05-07 11:53:20.515092', 831561, 60, 1230, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1868, '- 75% durch Kündigung von Beat
+ 50% Neuanstellung G.B.', '2024-05-08 12:15:15.727991', '', '2024-05-08 12:15:55.488256', -0.25, 20, 1136, 3, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1879, 'Wir haben weitere Fokustage organisiert, damit wir grosse Schritte vorwärts kommen.', '2024-05-13 04:39:33.661716', '', '2024-05-13 04:39:33.661722', NULL, 13, 1129, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1033, 'Wir sind näher dran, haben aber immer noch keine konkrete Bestellung für die diversen Projekte (SwissLife, Mobi, BLS sind eigentlich klar und eingeplant). ', '2023-12-12 12:53:09.678884', '', '2023-12-12 12:53:09.678888', 0, 3, 215, 3, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1246, 'Team Forum im Februar wird als Mini-Teamentwicklungs-Workshop vorgesehen.', '2024-01-25 13:18:54.577877', '', '2024-01-25 13:18:54.577892', NULL, 33, 1097, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1280, 'Einige neue Events reingekommen', '2024-01-29 12:04:19.701342', 'Definieren wo wir teilnehmen, ev Meetup in den Herbst verschieben wegen Helvetic Ruby', '2024-01-29 12:04:19.70135', NULL, 28, 1087, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1349, 'Wir haben bereits ein sehr Ansprechendes Tool.', '2024-02-12 12:23:52.956063', 'Marketing muss gas geben.', '2024-02-12 12:23:52.956068', NULL, 15, 1122, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1577, '', '2024-03-18 10:33:30.956043', '', '2024-03-18 10:33:30.956045', 1083197, 40, 1093, 10, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1664, '2 Interviews geplant, Abstimmungstermin mit POs für Lehrlingsprojekt definiert.', '2024-04-11 12:51:48.759343', '', '2024-04-11 12:51:48.759348', NULL, 20, 1139, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1396, 'SHKB Offerte ist raus (Total zwei Offerten offen)', '2024-02-19 13:25:50.845275', '', '2024-02-19 13:25:50.845279', NULL, 60, 1099, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1416, 'Austausch mit Flock Labs hat ergeben, dass wir gemeinsam eine Berner ML Meetup Serie starten könnten. Sie sind sich sicher, dass es Bedarf gibt und sind auch gut vernetzt. Idee: März Austausch über weiteres Vorgehen, Planung Event für den Sommer, Hosting bei Puzzle', '2024-02-22 10:13:21.480894', '', '2024-02-22 10:13:21.480896', NULL, 51, 1020, 1, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1430, 'Austausch mit Andy (Solution Foundry), keine Angebotsmöglichkeit in Aussicht', '2024-02-26 08:32:33.019657', '', '2024-02-26 08:32:33.01966', NULL, 20, 1022, 1, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (1431, 'Stand ist noch gleich und wird sich voraussichtlich nicht ändern.', '2024-02-26 08:32:44.950927', '', '2024-02-26 08:32:44.950929', NULL, 51, 1021, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1355, 'Fast alle Teams nehmen am OKR-Framework teil, dazu haben wir noch drei freiwillige Teams zum Framework geholt.', '2024-02-12 12:41:34.049541', 'Jetzt muss das Marketing und unsere Entwicklungsabteilung Gas geben, damit wir das Tool extern vermarkten können.', '2024-02-26 09:48:53.479508', NULL, 15, 1122, 0, 'ordinal', 'COMMIT', 3);
INSERT INTO okr_pitc.check_in VALUES (1489, 'keine Veränderung seit letztem Check-in. ganzes Team leistet verrechenbare Stunden ausser Berti', '2024-03-04 12:36:10.759431', 'Möglichkeiten für Berti suchen um verrechenbar zu arbeiten', '2024-03-04 12:36:29.647529', NULL, 36, 1049, 2, 'ordinal', 'COMMIT', 3);
INSERT INTO okr_pitc.check_in VALUES (1491, 'Phippu startet mit Einarbeitung im März beim IGE und hat zudem das BesaQsys Mandat. Ab April ist er dann voll ausgelastet', '2024-03-04 12:39:16.615852', '', '2024-03-04 12:39:16.615862', 0.9, 36, 1053, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1493, 'keine Veränderung seit letztem Check-in. Diese Woche gehen wir an den WIAD und nächste Woche nimmt Tobi S. als Player beim Prompt Battle teil.', '2024-03-04 12:48:28.976349', '', '2024-03-04 12:48:28.976354', NULL, 36, 1058, 9, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (1503, 'Im LST vorgestellt. Mit GL und Einzelnen aus dem LST Feedback eingeholt, das grundsätzlich sehr positiv ist', '2024-03-05 10:46:29.967713', '', '2024-03-05 10:46:29.967719', NULL, 5, 1027, 8, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1512, 'Securitas 3rd Level ist unterschrieben!', '2024-03-06 10:34:46.141221', '', '2024-03-06 10:34:46.141227', NULL, 40, 1099, 10, 'ordinal', 'STRETCH', 0);
INSERT INTO okr_pitc.check_in VALUES (1527, 'Fast schon ufassbare 100%. Hat es so noch nicht gegeben', '2024-03-11 12:36:22.338843', '', '2024-03-11 12:36:22.338847', 100, 41, 1064, 8, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1536, 'Status Quo', '2024-03-12 11:06:28.449854', 'Übernahme in anderer Form in nächsten OKR-Zyklus', '2024-03-12 11:06:28.449857', NULL, 4, 1098, 0, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1537, 'Präsentation und Diskussion im Team-Meeting (Bereichs intern)', '2024-03-12 11:08:13.373122', 'Kommunikation intern (puzzelweit) und nach Aussen -> Nächster OKR-Zyklus', '2024-03-12 11:08:13.373124', NULL, 4, 1100, 0, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (688, 'Noch von keinen Rückmeldungen auf unsere Sales-Kampagne erfahren', '2023-11-24 06:04:38.676328', '', '2023-11-23 23:00:00', 0, 13, 129, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1552, 'keine Veränderung', '2024-03-14 16:15:57.188671', '', '2024-03-14 16:15:57.188673', NULL, 36, 1049, 1, 'ordinal', 'COMMIT', 3);
INSERT INTO okr_pitc.check_in VALUES (1553, 'keine Veränderung', '2024-03-14 16:16:27.468353', '', '2024-03-14 16:16:27.468355', 0.9, 36, 1053, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1554, 'keine Veränderung', '2024-03-14 16:18:30.453179', '', '2024-03-14 16:18:30.453181', 2, 36, 1056, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1673, 'in Arbeit', '2024-04-15 04:50:10.986944', '', '2024-04-15 04:50:10.986948', NULL, 13, 1129, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1564, 'Die mobility Members sind zuversichtlich, dass durch die strategischen Massnahmen die Ziele erreicht werden und bestätigen dies mit dem Durchschnittswert von 7.38 (Tiefster Wert von einzelnen Members 5).', '2024-03-15 12:53:08.604761', '', '2024-03-18 16:54:32.982518', 7.38, 20, 1024, 10, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1601, 'Februar-Zahlen liegen noch nicht vor. Deshalb bleibt Wert vorerst unverändert.', '2024-03-19 06:37:20.880977', 'Falls möglich, mit einem Extra-Checkin effektive Zahlen noch nachtragen.', '2024-03-19 06:37:20.880979', 269260, 33, 1071, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1568, 'Partnermap angepasst und weiteres Vorgehen besprochen: Quartalsweise Überprüfung und allfällig Anpssung der Partnermap.', '2024-03-15 12:59:44.26405', '', '2024-03-19 06:59:44.240108', NULL, 20, 1022, 0, 'ordinal', 'COMMIT', 4);
INSERT INTO okr_pitc.check_in VALUES (1565, '', '2024-03-15 12:55:08.273658', '', '2024-03-20 09:46:19.274884', 7.38, 20, 1024, 10, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1629, 'Give Away-Konzept ist in Erarbeitung. Definition Give Away Puzzle Up! erfolgt heute im Rahmen eines Meetings.', '2024-04-02 07:16:36.762408', '', '2024-04-02 07:16:36.762413', NULL, 26, 1184, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1648, 'läuft gut und wir haben mit den Post jetzt sicherlich auch mehr Sichtbarkeit', '2024-04-08 07:29:17.778005', '', '2024-04-08 07:29:17.77801', 84, 36, 1214, 8, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1649, 'mit Team besprochen und Tobi H. ist jetzt an der Umsetzung', '2024-04-08 07:29:56.963986', '', '2024-04-08 07:29:56.96399', NULL, 36, 1215, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1663, 'Keine Änderung ggü. letzer Woche', '2024-04-10 15:04:44.78086', '', '2024-04-10 15:04:44.780866', NULL, 29, 1142, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1675, 'keine Veränderung. SAC Verlängerung von Tobi steht an. Muss noch bestätigt werden', '2024-04-15 08:00:52.834693', '', '2024-04-15 08:00:52.834697', 92424, 36, 1191, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1692, '', '2024-04-16 06:42:12.397595', '', '2024-04-16 06:42:12.397598', NULL, 28, 1164, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1698, '3 spannende Professional Bewerbungen offen', '2024-04-17 07:10:09.113587', '', '2024-04-17 07:10:09.113592', 0, 20, 1136, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1708, '', '2024-04-18 06:50:31.813922', '', '2024-04-18 06:50:31.813926', 2687767, 13, 1134, 5, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1710, 'Consulting von Khôi bei HRM-Systems im Juni. Annahme 24h x CHF 90.- Mehrertrag = CHF 2160', '2024-04-19 14:04:22.845342', '', '2024-04-19 14:04:22.845347', 2160, 20, 1141, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1711, '', '2024-04-22 08:02:55.669789', '', '2024-04-22 08:02:55.669793', NULL, 28, 1164, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1712, '', '2024-04-22 08:03:04.572512', '', '2024-04-22 08:03:04.572516', NULL, 28, 1231, 2, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1713, '', '2024-04-22 08:03:11.791667', '', '2024-04-22 08:03:11.791671', NULL, 28, 1165, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1714, '', '2024-04-22 08:03:39.858849', '', '2024-04-22 08:03:39.858853', NULL, 28, 1232, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1092, '- Gespräche mit Hitobito Supportteam laufen, damit dort keine Tickets eskalieren.
- Team sensibilisiert, dass MA austritt innerhalb von 2 Arbeitstagen erfolgen muss.', '2023-12-18 10:04:33.022525', '', '2023-12-18 10:04:33.022531', 0.6, 32, 189, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (730, 'Minimale Veränderung', '2023-11-27 10:36:56.990266', 'keine', '2023-11-26 23:00:00', 8.5, 3, 210, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1184, 'JBoss Subscriptions Dataport', '2024-01-15 19:21:01.577648', '', '2024-01-15 19:21:23.517717', 0.9, 17, 1104, 4, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1249, 'Im Dezember "nur" 197''379.-', '2024-01-25 13:27:36.053286', '', '2024-01-25 13:27:36.053303', 197379, 33, 1071, 3, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1350, 'Mail und Newsbeitrag 1 sind raus.', '2024-02-12 12:23:56.561951', '', '2024-02-12 12:23:56.561958', NULL, 27, 1112, 5, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1432, 'keine Veränderung: Evtl. kommt noch Mobi Verlängerung hinzu, ansonsten diverse kleinere Deals und Leads', '2024-02-26 08:48:12.783145', 'Mobi Deal sichern, ansonsten Sales! Sales! Sales', '2024-02-26 08:48:12.783148', 129924, 36, 1048, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1433, 'Mayra leistet Verrechenbare Stunden beim SAC Projekt, Berti sucht noch ein entsprechendes Projekt', '2024-02-26 08:49:37.475398', '', '2024-02-26 08:50:07.207034', NULL, 36, 1049, 4, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (1492, 'keine Veränderung seit letztem Check-in', '2024-03-04 12:40:28.138895', '', '2024-03-04 12:40:28.138903', 2, 36, 1056, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1494, 'Werden wir am WAC Morning besprechen', '2024-03-04 12:53:22.276315', '', '2024-03-04 12:53:22.276322', NULL, 36, 1061, 4, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1513, 'Noch weitere Aufträge kurz vor Abschluss', '2024-03-06 10:36:49.820239', '', '2024-03-06 10:36:49.820246', 1020114, 40, 1093, 9, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1528, 'Wir sind mit  57.92 Stunden leicht unter Budget ', '2024-03-11 12:37:36.63652', '', '2024-03-11 12:37:36.636525', NULL, 41, 1091, 4, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1529, 'Das nächste Relase steht wieder an für Ende März. Der Merge auf der Testumgebung ist Ende Woche ', '2024-03-11 12:38:44.247384', '', '2024-03-11 12:38:44.247388', 0.3, 41, 1066, 2, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1538, 'Status Quo', '2024-03-12 11:09:13.710529', 'Bereichsmap aktualisieren (-> Nächster Zyklus)', '2024-03-12 11:09:13.710532', 11, 4, 1102, 0, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1555, 'WIAD und Prompt Battle waren ein voller Erfolg', '2024-03-14 16:20:24.450451', '', '2024-03-14 16:20:24.450455', NULL, 36, 1058, 9, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (1578, 'Kubernetes anywhere & everywhere
In kurzer Form bereits auf www.puzzle.ch/cloud publiziert, /mid-Journey wird noch ausgebaut.
Erste Anfrage bereits eingetroffen (Stretch), nicht über Webseite.', '2024-03-18 10:49:00.950625', '', '2024-03-18 10:55:09.384681', NULL, 40, 1063, 10, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (1566, 'Die mobility Members unterstützen die Strategiemassanhmen mit einem Durchschnittswert von 8.43 (von 10). Keine Voten unter 7.', '2024-03-15 12:56:21.811441', '', '2024-03-18 16:53:25.077546', 8.43, 20, 1025, 10, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1602, 'Prio 2 Kategorie wurde mir rbr zusammen durchgearbeitet. Die insgesamt 5 Prio-1-Kunden Account Plannings hatten jedoch noch keinen Platz.', '2024-03-19 06:48:40.666738', 'Termine für Prio-1-Kunden-Account-Plannings planen.', '2024-03-19 06:48:40.66674', 8, 33, 1092, 2, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1603, 'Mangels Zeit Keine weiteren Aktivitäten ggü. letztem Checkin.', '2024-03-19 06:51:38.37164', 'In GJ-Q4 abschliessen', '2024-03-19 06:51:38.371642', 1, 33, 1095, 1, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1650, 'Bereichsstrategie wurde von Berti und Simu besprochen und wird jetzt final niedergeschrieben', '2024-04-08 07:30:34.802382', '', '2024-04-08 07:30:34.802386', NULL, 36, 1218, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1665, '2 Interviews fix geplant, weitere Bewerbungen in Pipeline', '2024-04-11 12:52:49.156715', '', '2024-04-11 12:52:49.156719', 0, 20, 1136, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1674, '', '2024-04-15 04:51:08.497927', '', '2024-04-15 04:51:48.096285', 2491938, 13, 1134, 5, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1676, 'neue Anfrage für den Visualisierungsworkshop', '2024-04-15 08:13:38.65907', '', '2024-04-15 08:13:38.659074', NULL, 36, 1206, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1677, 'keine Veränderung seit letztem Checkin', '2024-04-15 08:13:59.193507', '', '2024-04-15 08:13:59.193511', NULL, 36, 1208, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1680, 'Anfrage für Visualisierungsworkshop erhalten und wir werden bei CH-Open den Workshop eingeben', '2024-04-15 08:17:22.650778', '', '2024-04-15 08:17:22.650782', NULL, 36, 1215, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1693, '', '2024-04-16 06:42:21.431336', '', '2024-04-16 06:42:21.43134', NULL, 28, 1231, 2, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1696, '', '2024-04-16 06:43:01.340147', '', '2024-04-16 06:43:01.34015', NULL, 28, 1167, 2, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1699, '- 1. Interview mit möglicher BBT Betreuungsperson hat stattgefunden.
- Abstimmung POs und BBT für Lehrlingsprojekt durchgeführt.
- Bei BLS angefragt für Einsatz von 4. Lehrjahr Lehrling.', '2024-04-17 07:12:44.120024', '', '2024-04-17 07:12:44.120028', NULL, 20, 1139, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1709, 'Keine zusätzliche Stellen ausgeschrieben.', '2024-04-18 08:17:14.157564', '', '2024-04-18 08:17:14.157569', NULL, 22, 1128, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1715, '', '2024-04-22 08:03:48.778313', '', '2024-04-22 08:03:48.778318', NULL, 28, 1167, 2, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1716, '', '2024-04-22 08:04:03.164805', '', '2024-04-22 08:04:03.16481', 738694, 28, 1168, 3, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2014, '- am 6.6. werden der erweiterten Kerngruppe Digitale Lösungen die Resultate aus der Marktanalayse vorgestellt.', '2024-05-28 08:42:40.521363', '', '2024-05-28 08:42:40.521385', NULL, 16, 1178, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1842, 'Schwangerschaftsvertetung und Verantwortlichkeit Innoprozess ist gelärt', '2024-05-06 06:11:57.354297', '', '2024-05-06 06:11:57.354308', NULL, 5, 1142, 4, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1869, 'Weiteres Kennenlernen für 29.5. abgemacht. Zudem Abstimmungstermin mit potenziellen Techleaders von /mobility', '2024-05-08 13:55:14.347287', '', '2024-05-08 13:55:14.347294', NULL, 20, 1135, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1880, '', '2024-05-13 04:41:42.101473', '', '2024-05-13 04:41:42.101535', 3038219, 13, 1134, 4, 'metric', NULL, 7);
INSERT INTO okr_pitc.check_in VALUES (1881, 'Keine Veränderung von den Massnahmen. Durch Weggang Nadia ca 16kchF kostenreduktion', '2024-05-13 06:10:36.825768', '', '2024-05-13 06:10:36.825774', 16000, 5, 1141, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1827, '- am Leadership-Team Workshop vom 30. April wurden die bestehenden und mögliche neue Marktopportunitäten besprochen
- am Monthly Mai (27. Mai) werden wir die finale Entscheidung über die Marktopportunitäten für das GJ 24/25 treffen', '2024-05-02 06:06:36.345525', '', '2024-05-15 11:16:45.064837', NULL, 16, 1132, 5, 'ordinal', 'FAIL', 2);
INSERT INTO okr_pitc.check_in VALUES (2001, 'Call mit SHKB am 29.5. ob 3rd Level ja oder neien', '2024-05-27 09:30:50.607788', '', '2024-05-27 09:30:50.607791', NULL, 60, 1217, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2013, '', '2024-05-28 08:41:15.008605', '- der Entscheid für die neuen Marktopportunitäten wird erst im Monthly Juni erfolgen.
- die Moals (Mittelfristsicht) sind erarbeitet. Offen sind noch letzten quantitativen Ziele.', '2024-05-28 09:10:25.191161', NULL, 16, 1132, 7, 'ordinal', 'FAIL', 3);
INSERT INTO okr_pitc.check_in VALUES (2022, 'N/A', '2024-05-30 14:33:48.01887', 'Gemäss Action Plan:', '2024-05-30 14:33:48.018878', 0, 4, 1197, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2023, 'Abstimmung mit dbi: Inserat aufgeschaltet lassen, sonst Thema nicht aktivi weiter treiben bis Person für Themenlead gefunden ist.', '2024-05-31 07:43:27.099065', '', '2024-05-31 07:43:27.099072', NULL, 20, 1135, 1, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2276, 'wir haben uns das noch nicht angehört.', '2024-07-15 11:06:29.690738', '', '2024-07-15 11:06:29.69074', NULL, 32, 1256, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2291, '', '2024-07-19 08:56:17.180664', '', '2024-07-19 08:56:34.53743', 2606285, 13, 1241, 4, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2295, 'keine Veränderung', '2024-07-22 08:10:08.574354', '', '2024-07-22 08:10:22.304798', NULL, 36, 1324, 5, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1093, 'mehr als 1 eskaliertes Ticket. Evt workflow ändern, wenn Supporteure nicht jeden Tag arbeiten und eine Antwort des Kunden kommt, eskaliert das Ticket ziemlich sicher...', '2023-12-18 10:12:01.927305', 'Eskalierung im Zammad überdenken', '2023-12-18 10:12:01.927311', 0.2, 32, 187, 3, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1185, 'Dataport', '2024-01-15 19:22:22.871794', '', '2024-01-15 19:22:22.871796', 1, 17, 1055, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1250, 'Anfrage bei Priot hängig. ', '2024-01-25 13:43:55.634399', '', '2024-01-25 13:43:55.634417', 1, 33, 1095, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1351, '', '2024-02-12 12:24:37.889752', '', '2024-02-12 12:24:37.889756', 81, 27, 1111, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1352, 'die Verrechenbarkeit der Kundenaufträge liegt bei 99.7 % und die absolute bei 82.1 %', '2024-02-12 12:32:48.011962', '', '2024-02-12 12:32:48.011967', 99.7, 41, 1064, 8, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1434, 'keine Veränderung seit letztem Check-in', '2024-02-26 08:56:59.344465', 'Sales! Sales! Sales', '2024-02-26 08:56:59.344468', 1.15, 36, 1053, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1435, 'MAG durchgeführt. Liste mit Massnahmen erstellt. ', '2024-02-26 08:58:18.518903', 'Prio und Umsetzung einplanen', '2024-02-26 08:58:18.518906', NULL, 28, 1079, 9, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (1495, 'keine Veränderung seit letztem Check-in', '2024-03-04 12:59:12.65264', 'Sales Aktivitäten und SoMe Posts ', '2024-03-04 12:59:12.652647', NULL, 36, 1076, 4, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (1539, 'Status Quo', '2024-03-12 11:09:38.716251', '', '2024-03-12 11:09:38.716254', 1, 4, 1103, 0, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1556, 'am WAC Morning mti Team brainstorming durchgeführt und Ideen gesammelt', '2024-03-14 16:21:46.925554', '', '2024-03-14 16:21:46.925556', NULL, 36, 1061, 0, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1557, 'mit Team besprochen wie wir den Visualisierungsworkshop attraktiver machen könnten und was Gründe sind warum wir keine Anmeldungen haben', '2024-03-14 16:24:42.336387', 'wir werden nochmals eine Iteration machen und verschiedene Action umsetzen (wird als OKR im nächstne Cycle bearbeitet werden)', '2024-03-14 16:24:42.33639', NULL, 36, 1076, 4, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (1567, 'unverändert', '2024-03-15 12:57:31.117528', '', '2024-03-15 12:57:31.117531', 1, 20, 1031, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1579, 'Wir gehen davon aus, dass am 25.3. mindestens eine qualifizierte Mehrheit zustimmen wird. Die bilateralen Gespräche mit der GL und ausgewählten Div. Owner deutet eigentlich darauf hin die Target Zone zu erreichen.', '2024-03-18 12:28:18.433559', '', '2024-03-18 12:28:18.433564', NULL, 5, 1027, 8, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1582, 'Keine Änderungen mehr im März', '2024-03-18 12:30:54.265248', '', '2024-03-18 12:30:54.26525', NULL, 5, 1021, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1604, 'In MAGs mit allen Members Möglichkeiten abgeklärt. Interesse einzelner Members vorhanden. Commitment bis Sommer aber zu kurzfristig.', '2024-03-19 06:54:01.07187', 'Für GJ24/25 Durchführung der Zertifizierungen mit den interessierten Members planen', '2024-03-19 06:54:01.071872', NULL, 33, 1101, 1, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1580, 'Alle A-Kundengespräche durchgeführt und dokumentiert. Im Moment keine Angebotsmöglichkeit -- auch bei BLS nicht, wo es noch lange offen war.', '2024-03-18 12:29:49.760733', '', '2024-03-19 07:00:20.277628', NULL, 5, 1036, 0, 'ordinal', 'COMMIT', 5);
INSERT INTO okr_pitc.check_in VALUES (1651, 'Robin hat die AWS Zertifizierung abgeschlossen', '2024-04-08 08:16:19.799037', '', '2024-04-08 08:16:19.799041', NULL, 60, 1207, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1666, 'Puzzle DE ist an spannendem Kandidaten mit Kafka Knowhow dran. Ev. könnte diese Person beim internen Knowhow Aufbau mithelfen.', '2024-04-11 12:55:04.136988', '', '2024-04-11 12:55:04.136992', NULL, 20, 1135, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1678, 'SoMe Post von Flavio plus Kommentar bei Beitrag von Zeix', '2024-04-15 08:14:26.8989', '', '2024-04-15 08:14:26.898904', 2, 36, 1210, 9, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1681, 'Bereichsstrategie ist niedergeschrieben und bei Simu zum überarbeiten. Nach den Ferien von Berti wird sie finalisiert', '2024-04-15 08:19:02.013217', '', '2024-04-15 08:19:02.01322', NULL, 36, 1218, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1694, 'Ausfall von PSI', '2024-04-16 06:42:39.592257', '', '2024-04-16 06:42:39.592261', NULL, 28, 1165, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1695, '', '2024-04-16 06:42:53.734966', '', '2024-04-16 06:42:53.73497', NULL, 28, 1232, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1700, 'SHKB 3rd Level Offerte pending', '2024-04-17 12:23:29.717497', '', '2024-04-17 12:23:29.717501', NULL, 40, 1217, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1717, 'Offerten draussen für Berti und Mayra (Mobi visualisierung). ', '2024-04-22 08:16:59.967317', '', '2024-04-22 08:16:59.967322', NULL, 3, 1206, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1742, '5/29 Tasks erledigt', '2024-04-23 15:40:15.880817', '', '2024-04-23 15:40:15.880825', 5, 33, 1233, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1751, 'unverändert', '2024-04-24 06:44:15.744511', '', '2024-04-24 06:44:15.744517', NULL, 20, 1135, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1730, 'Vorbereitungen für die Aufräumaktion sind für diese Woche eingeplant. ', '2024-04-23 06:27:18.98795', '', '2024-04-24 08:06:12.940862', NULL, 49, 1179, 8, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1759, 'Weggang Nadia Blatter ohne Ersetzung (Reduktion nicht verrechenbarer Pensen)', '2024-04-25 07:58:44.581579', '', '2024-04-25 07:58:44.581586', 8000, 5, 1141, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (55, '38.17', '2023-07-05 08:14:35.435436', '', '2023-07-04 22:00:00', 38, 26, 108, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (56, '0', '2023-07-05 08:15:27.679992', 'Feedback Sebastian Preisner ausstehend', '2023-07-04 22:00:00', 0, 26, 113, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (57, '1335', '2023-07-05 08:16:41.117668', '', '2023-07-04 22:00:00', 1345, 26, 109, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (58, 'Entwurf steht, Feedbackrunde', '2023-07-05 08:17:54.963221', '', '2023-07-04 22:00:00', 0.5, 26, 107, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (59, 'Yoga mit Pascal Simon', '2023-07-05 08:19:43.181628', '', '2023-07-04 22:00:00', 1, 26, 115, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (60, 'Newsbeitrag PWorkshop, Hinweis an Simu Mario Kart-Event', '2023-07-05 08:22:10.928752', '', '2023-07-04 22:00:00', 2, 26, 114, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (61, 'Kickoff hat stattgefunden', '2023-07-05 08:25:15.256532', '', '2023-07-04 22:00:00', 0.2, 26, 110, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (62, 'Board ist erstellt. Wird am MarComSales-Weekly und dann am BL-Weekly präsentiert.', '2023-07-05 08:30:10.93451', '', '2023-07-04 22:00:00', 0.75, 26, 117, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (63, 'Dagger-Video', '2023-07-05 08:30:48.809244', '', '2023-07-04 22:00:00', 1, 26, 118, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (64, 'Quartalsplanungen mit /sys, /mid, /zh, /mobility, /devtre, /bbt, /wac haben schon stattgefunden', '2023-07-05 08:33:20.442628', '', '2023-07-04 22:00:00', 0.75, 26, 116, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (67, 'Termin mit Jürgen von UX im August aufgesetzt für Auswertung letzte Memberumfrage. Happiness Umfrage für Teams ist ready. Planung erfolgt. ', '2023-07-05 08:48:04.819227', '', '2023-07-04 22:00:00', 0.1, 5, 36, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (54, 'Deep Dive AR und PLE für 22. August aufgesetzt. Challenging mit fse und mga aufgesetzt. Ferienbedingt läuft vorher nicht viel.', '2023-07-05 10:37:43.175256', '', '2023-07-04 22:00:00', 0.1, 5, 119, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (70, 'Erste Messung', '2023-07-05 13:51:22.219061', '', '2023-07-04 22:00:00', 1.4, 13, 45, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (71, 'Resultate aus Happiness Umfrage noch nicht vorhanden für Auswertung.', '2023-07-07 14:00:01.245679', '', '2023-07-06 22:00:00', 0, 16, 42, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (93, '-', '2023-07-31 15:08:16.778785', '', '2023-07-16 22:00:00', 34, 34, 53, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (73, 'Gemeinsames internes Verständnis geschafft, am Puzzle Workshop vorgestellt.', '2023-07-09 06:05:24.161629', '', '2023-07-08 22:00:00', 0.15, 13, 47, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (769, 'Leider immer noch keine Anmeldung (eine Kampagne startet morgen)', '2023-12-07 15:51:13.976322', '', '2023-12-06 23:00:00', 0.2, 51, 159, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1094, 'Umfrage fehlt.
Einzelne LST Member direkt befragt.
Ansonsten Ziel erreicht.', '2023-12-18 11:50:27.353239', '', '2023-12-18 11:50:27.353244', 0.8, 24, 132, 7, 'metric', NULL, 5);
INSERT INTO okr_pitc.check_in VALUES (1251, 'Commitment von Andres wieder zurückgezogen. Ansonsten bisher kein fixes Commitment.', '2024-01-25 13:50:36.688255', 'Commitments bei MAG abholen', '2024-01-29 09:59:38.435193', NULL, 33, 1101, 4, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1436, 'keine Veränderung seit letzten Check-in', '2024-02-26 08:58:21.023413', '', '2024-02-26 08:58:21.023416', 2, 36, 1056, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1438, 'UX Meetup bei uns gehostet -> somit an 7 Events teilgenommen und einen Lead generiert den wir für den SAC ausführen dürfen', '2024-02-26 09:01:31.393201', '', '2024-02-26 09:01:31.393204', NULL, 36, 1058, 9, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (1496, 'keine Veränderung seit letztem Check-in', '2024-03-04 12:59:50.786263', 'SBB muss die Referenzen von Jürgen bewilligen', '2024-03-04 12:59:50.78627', NULL, 36, 1105, 8, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1581, 'Keine Änderungen mehr im März', '2024-03-18 12:30:37.265848', '', '2024-03-19 06:58:49.681624', NULL, 5, 1020, 0, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1569, 'its-ch Workshop besucht. Spannende Kontakte kennengelernt, ohne direkte Aussichten auf Aufträge.', '2024-03-15 13:01:21.557568', '', '2024-03-19 06:59:59.630667', NULL, 20, 1023, 0, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1605, 'Aus Diskussion OKR Review. ', '2024-03-19 08:32:47.10851', '', '2024-03-19 08:32:47.108511', NULL, 24, 1051, 0, 'ordinal', 'FAIL', 3);
INSERT INTO okr_pitc.check_in VALUES (1652, '', '2024-04-08 08:34:58.62462', '', '2024-04-08 08:34:58.624624', NULL, 31, 1213, 6, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1667, 'viel zu hoch mit 104.92 Stunden', '2024-04-11 13:33:29.893202', 'Retro mit tel und Matthias', '2024-04-11 13:33:29.893207', NULL, 41, 1149, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1668, 'Erste Analyse gemacht und Ideen sind generiert', '2024-04-11 13:34:56.955409', 'wegen Daily Business und ISO 14001 ist meien Zuversicht sehr klein für dieses Quartal', '2024-04-11 13:34:56.955413', NULL, 41, 1150, 1, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1679, '', '2024-04-15 08:15:26.207401', '', '2024-04-15 08:15:26.207405', 100, 36, 1214, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1697, '', '2024-04-16 06:46:00.450125', '', '2024-04-16 06:46:00.450129', 738694, 28, 1168, 3, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1701, 'Blogpost, Direct Mailing & KCD Auftritt in Arbeit. Kommt gut.', '2024-04-17 12:23:46.166882', '', '2024-04-17 12:23:46.166885', NULL, 31, 1188, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1718, 'Keine Veränderung seit letztem Checkin (Planung gemacht) ', '2024-04-22 08:18:05.493526', '', '2024-04-22 08:18:05.49353', NULL, 3, 1208, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1731, 'Wir planen in den nächsten Wochen den Schulungstermin und können dann den def. Startschuss festlegen.', '2024-04-23 06:27:52.819785', '', '2024-04-23 06:27:52.819799', 0, 49, 1180, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1743, 'Planung der Workshops für das Teammeeting vom 24.4. geplant.', '2024-04-23 16:18:05.020677', '', '2024-04-23 16:18:05.020686', NULL, 33, 1235, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1744, 'März: 12.1% +++ Kumuliert: 12.1%', '2024-04-23 16:20:07.126596', 'Gas geben im April und Mail...', '2024-04-23 16:20:07.126603', 12.1, 33, 1161, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1752, 'unverändert', '2024-04-24 06:44:59.667966', '', '2024-04-24 06:44:59.667972', 0, 20, 1136, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1753, '- Aussicht auf Kundeneinsatz für 4. Lehrjahr Lehrling bei BLS. Wird sich im Mai klären
- Aussicht auf BBT Entwicklungsprojekt bei UniLu', '2024-04-24 06:46:33.759972', '', '2024-04-24 06:46:33.759979', NULL, 20, 1139, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1760, '', '2024-04-26 06:32:30.071534', '', '2024-04-26 06:32:36.180177', 4, 7, 1236, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1828, '- die Marktanalyse ist noch in Erarbeitung. Aufgrund Ferienabwesenheiten und vielen Offerings fehlte die Zeit schneller vorwärts zu kommen.
- Experten-Interview mit Gernot Hugl wird gepürft. Entscheid noch ausstehend.', '2024-05-02 06:10:11.428553', '', '2024-05-02 06:10:11.428565', NULL, 16, 1178, 4, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1829, 'Account Alignment mit GCP aufgesetzt am 27. Mai
Account Alignment mit AWS wird aktuell aufgesetzt
Zertifizierungen AWS -> erreicht
Zertifizierungen GCP -> noch offen
Opportunitäten:
- BAFU: Daten und Digitalisierung (PDD) (Einladungsverfahren) (leider verloren)', '2024-05-02 06:16:25.161542', '', '2024-05-02 06:24:23.08433', NULL, 16, 1176, 4, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1843, 'In Arbeitsgruppe die Fokusthemen "Ziele", "Verantwortung", "Beteiligung an Entscheiden" untersucht, besprochen und weitere Schritte definiert (https://codimd.puzzle.ch/d3-selbstorganisation-methoden#)', '2024-05-06 06:40:52.48897', 'Präsi zwecks Kenntnisnahme und Entscheid im Teammeeting, erarbeiten der Massnahmen', '2024-05-06 06:40:52.488977', NULL, 4, 1194, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1870, '- mündl. Zusage für Betreuungsperson.
- Abstimmung mit UniLu für BBT Kundenprojekt
- Betreuung im Q1 noch in Abklärung (Java & Basislehrjahr)', '2024-05-08 13:57:44.992228', '', '2024-05-08 13:58:04.246167', NULL, 20, 1139, 6, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1882, 'Keine', '2024-05-13 06:11:23.752646', '', '2024-05-13 06:11:23.752653', NULL, 5, 1146, 10, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (2002, '', '2024-05-27 09:55:56.576042', '', '2024-05-27 11:22:37.259697', 918773, 60, 1230, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2015, '- am 6.6. findet der Puzzle Lunch mit AWS definitiv statt.
- AWS und TD Synnex sind Partner am Puzzle up! vom Ende August.
- das geplante Account Alignment mit GCP vom 27. Mai wurde leider verschoben (neuer Termin wird gesucht).', '2024-05-28 08:44:21.592804', '', '2024-05-28 08:44:21.592827', NULL, 16, 1176, 3, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2024, 'unvewrändert', '2024-05-31 07:44:32.891575', '', '2024-05-31 07:44:32.891582', -0.25, 20, 1136, 3, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2292, 'Kickoff findet erst Ende August statt. Daher wird das Target vermutlich erst im nächsten Quartal erreicht.', '2024-07-19 13:16:02.539553', '', '2024-07-19 13:16:31.985709', NULL, 13, 1247, 1, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2296, 'noch keine Veränderung (Front wurde abgesagt)', '2024-07-22 08:11:08.356937', 'in den Bilas mit den Members diskutieren an welche UX Veranstaltungen sie gehen', '2024-07-22 08:11:08.356939', 0, 36, 1314, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2297, 'Post  Animationen Puzzle Website', '2024-07-22 08:11:34.525937', '', '2024-07-22 08:11:34.525939', 2, 36, 1315, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2355, 'Keine Veränderung', '2024-08-05 07:07:32.281715', '', '2024-08-05 07:07:32.281721', NULL, 3, 1324, 5, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2356, 'Neuer Member tritt die Stelle heute an, produktiv auf Mandat erst in zwei Wochen. ', '2024-08-05 07:07:59.022478', '', '2024-08-05 07:07:59.022484', NULL, 3, 1322, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2357, 'Keine Veränderung, Bestellung SwissLife noch ausstehend', '2024-08-05 07:08:18.067456', '', '2024-08-05 07:08:18.067461', 83001, 3, 1312, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2362, 'Keine Veränderung seit letzter Woche', '2024-08-05 07:10:21.141041', '', '2024-08-05 07:10:21.141046', NULL, 3, 1319, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2385, 'Angebot steht noch nicht zur Verfügung', '2024-08-08 19:07:55.934369', 'Workshop mit TF planen', '2024-08-08 19:07:55.934373', 0, 17, 1379, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2415, 'Keine änderung', '2024-08-12 11:08:21.085709', '', '2024-08-12 11:08:21.085713', NULL, 3, 1322, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2420, 'Konzept in Arbeit', '2024-08-12 11:10:45.085411', '', '2024-08-12 11:10:45.085415', NULL, 3, 1317, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2461, 'Kafka definitiv on hold', '2024-08-21 05:56:37.218657', 'Zusätzlich neu ein bi Weekly mit CTO und neu Captains für die Interest Groups am definieren', '2024-08-21 05:56:37.21866', NULL, 5, 1263, 10, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (2455, 'Team-Aktiviäten und Ziele derer sind definiert -> https://codimd.puzzle.ch/d3-teamaktivitaeten-2024-25#', '2024-08-19 12:18:23.661033', 'Sammeln von Aktivitäten, OK (Core-Team) erstellen, Plan erstellen', '2024-08-19 12:19:17.912271', NULL, 4, 1288, 10, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (2475, '', '2024-08-22 07:45:54.14791', '', '2024-08-22 07:45:54.147913', NULL, 28, 1298, 3, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2476, 'Erster generischer Draft', '2024-08-22 07:46:07.37441', '', '2024-08-22 07:46:07.374412', NULL, 28, 1299, 1, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1095, 'Offen: Für Cilium ist ein Konzept vorhanden für welche Use-Cases Cilium sinnvoll ist.
Offen: Für Cilium sind erste Offerings erstellt.
Offen: Für Dagger sind die ersten Offerings generiert.
Offen: Für Dagger sind erste Offerings erstellt.
Erreicht: Für Grafana OnCall haben wir die Evaluationsphase abgeschlossen.
Erreicht: Für Grafana sind Teams von jeweils 3 Members ausgebildet.
Offen: Für Grafana OnCall haben wir Umsätze bei Kunden generiert.
Erreicht: Für Event Driven Ansible haben wir die Evaluationsphase abgeschlossen.
Erreicht: Für EDA sind Teams von jeweils 3 Members ausgebildet.
Offen: Für Event Driven Ansible haben wir Kundenumsätze generiert.
Erreicht: Für ML Ops haben wir das Consulting und Schulungsangebot definiert.
Offen: Für ML Ops haben wir eine erste Schulung kostenpflichtig durchgeführt.
Offen: Für ML Ops haben wir ersten Kundenumsatz generiert. ', '2023-12-18 11:52:36.469653', '', '2023-12-18 11:52:36.46966', 0.5, 24, 135, 8, 'metric', NULL, 3);
INSERT INTO okr_pitc.check_in VALUES (1437, 'Erste Ideen im Codi gesammelt', '2024-02-26 08:59:48.948096', '', '2024-02-26 08:59:48.948099', NULL, 28, 1080, 7, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (1439, 'keine Veränderung', '2024-02-26 09:02:58.034691', '', '2024-02-26 09:02:58.034693', NULL, 36, 1061, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1570, 'SNB hat den Upsell sowie das Renewal für die nächsten drei Jahre Gitlab bestellt (Umfang CHF 662''400!). SwissRe Cilium Deal könnte in den nächsten 10 Tagen noch kommen. Mit Migros Bank und LGT ist erst im GJQ4 zu rechnen.', '2024-03-15 13:37:27.077568', '', '2024-03-15 13:37:27.07757', 1, 16, 1012, 1, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1583, 'Status Quo', '2024-03-18 15:22:15.987185', '', '2024-03-18 15:22:15.987187', 5, 4, 1081, 0, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1585, 'Es wurden zwar vermehrt Events besucht. Jedoch gibt es keine Übersicht, keinen Plan und keine Taktik.', '2024-03-18 15:25:06.339091', 'In OKRs des nächsten Quartals wieder aufnehmen.', '2024-03-18 15:25:06.339093', NULL, 4, 1084, 0, 'ordinal', 'FAIL', 3);
INSERT INTO okr_pitc.check_in VALUES (1653, 'Tinu hat vom Richmond Forum 4 Leads, die er in den nächsten Tagen anschreiben wird (Volvo Car, Band Genossenschaft, ein Institut in Genf und sonst noch was). Ist aber noch nicht erfolgt
', '2024-04-08 08:35:59.117873', '', '2024-04-08 08:36:31.918191', 0, 32, 1158, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1669, 'noch nicht gestartet ', '2024-04-11 13:35:21.874421', '', '2024-04-11 13:35:21.874425', NULL, 41, 1153, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1682, 'N/A', '2024-04-15 12:31:48.603428', '', '2024-04-15 12:31:48.603432', NULL, 4, 1194, 10, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1683, 'N/A', '2024-04-15 12:32:00.544092', '', '2024-04-15 12:32:00.544096', NULL, 4, 1196, 10, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1687, 'N/A', '2024-04-15 12:33:25.5621', '', '2024-04-15 12:37:06.040108', NULL, 4, 1221, 10, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1702, 'Keine Veränderung', '2024-04-17 12:24:34.748258', '', '2024-04-17 12:24:34.748262', NULL, 31, 1207, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1704, 'Keine Veränderung - Angebotsstory in Erarbeitung, Diskussion ist am 23.4. aufgesetzt.', '2024-04-17 12:26:01.939347', '', '2024-04-17 12:26:01.939351', NULL, 31, 1229, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1719, 'Brandbook vom Mayra auf WAC und verlinkung auf Linkedin', '2024-04-22 08:21:58.315227', '', '2024-04-22 08:21:58.315231', 3, 3, 1210, 9, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1732, '- Mayra hat den ersten Entwurf für die Animationen gemacht, warten auf V2. Jürgen wird anschliessend die Animationen übernehmen
- Briefing für Fotografin erfolgt in der KW12', '2024-04-23 06:28:48.639656', '', '2024-04-23 06:28:48.639673', 0, 49, 1200, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1745, 'März: CHF 289''513.- +++ Durchschnitt: CHF 289''513.-', '2024-04-23 16:23:40.289812', 'Weiter so...', '2024-04-23 16:23:40.289819', 289513, 33, 1163, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1754, 'Ticket fürs Leadership Monthly ist erstellt', '2024-04-24 08:03:21.696649', '', '2024-04-24 08:05:44.626593', NULL, 26, 1182, 7, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1761, 'Workshop durchgeführt. 3 Fokusthemen definiert: 1. Verantwortung, 2. Ziele, 3. Beteiligung an Entscheiden / Arbeitsgruppe erstellt, die das Thema weiter vertieft', '2024-04-26 15:47:17.677396', 'In Arbeitsgruppe erarbeiten, wie die drei Fokusthemen umgesetzt werden, inkl. Massnahmen und Messpunkte.', '2024-04-26 15:47:17.677402', NULL, 4, 1194, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1764, '', '2024-04-26 15:50:15.245703', '', '2024-04-26 15:50:15.245709', 0, 4, 1199, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1768, 'März ist schon besser als Februar. 75% aber noch nicht erreicht.', '2024-04-26 15:53:55.480625', '', '2024-04-26 15:53:55.480643', NULL, 4, 1225, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1769, 'BAFU SAM kommt (Hupf & Mehmet eingeplant)', '2024-04-26 15:55:17.190451', 'Offerte Kanton Aargau bis Ende Mai erstellen', '2024-04-26 15:55:17.190457', NULL, 4, 1227, 10, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (1771, 'Läuft alles, kommt gut', '2024-04-29 05:47:05.65933', '', '2024-04-29 05:47:05.659336', NULL, 31, 1188, 10, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1772, 'Keine Veränderung', '2024-04-29 05:47:41.820626', '', '2024-04-29 05:47:41.820633', NULL, 31, 1207, 7, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1830, 'Bestellungen von SHKB und RhB (beide OpenShift).', '2024-05-02 06:22:40.443292', '', '2024-05-02 06:22:40.443303', 56980, 16, 1177, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1844, 'In Arbeitsgruppe die Fokusthemen "Ziele", "Verantwortung", "Beteiligung an Entscheiden" untersucht, besprochen und weitere Schritte definiert (https://codimd.puzzle.ch/d3-selbstorganisation-methoden#)', '2024-05-06 06:42:27.044714', 'Präsi zwecks Kenntnisnahme und Entscheid im Teammeeting, erarbeiten der Massnahmen - Weiterarbeit Hand in Hand mit KR ".... Arbeit im Team.... 3 Fokusthemen..."', '2024-05-06 06:42:27.044744', NULL, 4, 1196, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1845, 'N/A', '2024-05-06 06:43:13.53317', 'siehe Action Plan', '2024-05-06 06:43:13.5332', 0, 4, 1197, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1765, 'Workshop durchgeführt, Verständnis geschafft, Ob "Digitale Lösungen" od "Indivisual Softwareentwicklung" spielt keine Rolle', '2024-04-26 15:51:55.972257', 'Mit /ux, /mobility & /ruby abgleichen.', '2024-05-06 06:55:55.686259', NULL, 4, 1204, 5, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1871, 'Direct Mailing in Arbeit, Sponsoringauftritt KCD ebenfalls in Planung', '2024-05-08 17:32:25.972228', '', '2024-05-08 17:32:25.972235', NULL, 31, 1188, 10, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1883, '', '2024-05-13 06:13:51.050279', 'Termin mit CTO aufgesetzt', '2024-05-13 06:13:51.050286', NULL, 5, 1147, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2003, 'keine Updates', '2024-05-27 10:03:50.471616', '', '2024-05-27 10:03:50.47162', NULL, 60, 1229, 6, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2004, 'Unverändert (Momentan keine Prio beim Mid-Lead)', '2024-05-27 10:04:34.137002', '', '2024-05-27 10:04:34.137008', NULL, 60, 1211, 0, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2016, 'Ypsomed neu dazu gekommen.', '2024-05-28 09:09:55.182605', '', '2024-05-28 09:09:55.182627', 61010, 16, 1177, 3, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2025, 'Projektentscheid: Entweder UniLu oder PTCS-Sheet, zudem OKR Tool', '2024-05-31 07:49:24.186037', '', '2024-05-31 07:49:24.186044', NULL, 20, 1139, 6, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2298, 'mit Jenny aufgegleist. Konzept muss noch erstellt werden', '2024-07-22 08:12:26.329719', '', '2024-07-22 08:12:26.329722', NULL, 36, 1316, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2299, 'mit jenny besprochen. Workshop geplant', '2024-07-22 08:13:04.639875', '', '2024-07-22 08:13:04.639878', NULL, 36, 1317, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2358, 'Aktuell keine Veränderung, Front Conf abgesagt. ', '2024-08-05 07:08:38.867136', '', '2024-08-05 07:08:38.86714', 0, 3, 1314, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2386, 'Kickoff durchgeführt und Auslegeordnung mit möglichem Angebot erstellt. --> wird weiter ausgearbeitet.
Teamszuammensetzung: mw, ek, apf, pg, jbl', '2024-08-09 10:31:45.976543', '', '2024-08-09 10:31:45.976549', NULL, 20, 1262, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2387, 'Rückmeldung auf Angebot bis Ende KW33.', '2024-08-09 10:32:57.681927', '', '2024-08-09 10:32:57.681932', -0.9, 20, 1259, 4, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (2388, 'Status quo', '2024-08-12 04:58:37.494772', '', '2024-08-12 04:58:37.494777', NULL, 4, 1278, 2, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2417, 'Keine Änderung', '2024-08-12 11:09:22.118602', '', '2024-08-12 11:09:22.118606', 0, 3, 1314, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1096, 'Offen: Für Cilium ist ein Konzept vorhanden für welche Use-Cases Cilium sinnvoll ist.
Offen: Für Cilium sind erste Offerings erstellt.
Offen: Für Dagger sind die ersten Offerings generiert.
Offen: Für Dagger sind erste Offerings erstellt.
Erreicht: Für Grafana OnCall haben wir die Evaluationsphase abgeschlossen.
Erreicht: Für Grafana sind Teams von jeweils 3 Members ausgebildet.
Offen: Für Grafana OnCall haben wir Umsätze bei Kunden generiert.
Erreicht: Für Event Driven Ansible haben wir die Evaluationsphase abgeschlossen.
Erreicht: Für EDA sind Teams von jeweils 3 Members ausgebildet.
Offen: Für Event Driven Ansible haben wir Kundenumsätze generiert.
Erreicht: Für ML Ops haben wir das Consulting und Schulungsangebot definiert.
Offen: Für ML Ops haben wir eine erste Schulung kostenpflichtig durchgeführt.
Offen: Für ML Ops haben wir ersten Kundenumsatz generiert. ', '2023-12-18 11:53:07.190293', '', '2023-12-18 11:53:07.190299', 0.7, 24, 135, 8, 'metric', NULL, 3);
INSERT INTO okr_pitc.check_in VALUES (1441, 'keine Veränderung seit letzten Check-in', '2024-02-26 09:03:56.490027', '', '2024-02-26 09:03:56.49003', NULL, 36, 1105, 8, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1442, '', '2024-02-26 09:04:23.146289', '', '2024-02-26 09:04:23.146291', 3828, 28, 1083, 3, 'metric', NULL, 6);
INSERT INTO okr_pitc.check_in VALUES (1440, 'keine Veränderung seit letztem Check-in', '2024-02-26 09:03:33.575685', '', '2024-02-26 09:53:52.123032', NULL, 36, 1076, 7, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (1584, 'Ist nicht so verlaufen wie in ordinalen Messwerten beschrieben. Dennoch sind alle Members ausgelastet, ausser Clara, d.h. nicht Stretch', '2024-03-18 15:24:00.293007', '', '2024-03-18 15:24:00.29301', NULL, 4, 1082, 0, 'ordinal', 'TARGET', 2);
INSERT INTO okr_pitc.check_in VALUES (1586, 'Nimmt man die Lead-Definition "Ein Kontakt mit einem potentiellen Kunden" und rechnet da auch andere bereiche von Grosskunden dazu waren es letztlich 5 Leads:
GemässKanton Aargau, Swico (via sieber&partner), inova-Solutions AG, Mobi Marzlii-Team, Mobi Rhone-Team - Bestehende Kunden (BKD, SWOA, Mobi-Teams) wurden nicht als Leads gezählt.', '2024-03-18 15:34:54.823886', '', '2024-03-18 15:34:54.823888', 5, 4, 1086, 0, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1654, 'Planung gemacht', '2024-04-08 09:05:16.053632', '', '2024-04-08 09:05:16.053636', NULL, 31, 1188, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1670, 'Noch kein Resultat, siehe auch Steerings Codi ', '2024-04-11 13:36:23.750029', 'Ext. Ressourcen auf Projekt nehmen', '2024-04-11 13:36:23.750051', NULL, 41, 1156, 4, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1684, 'N/A', '2024-04-15 12:32:22.938782', '', '2024-04-15 12:32:22.938786', 0, 4, 1197, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1703, 'In Arbeit', '2024-04-17 12:24:56.552453', '', '2024-04-17 12:24:56.552457', NULL, 31, 1211, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1720, 'Gemäss Batch auf Linkedin', '2024-04-22 09:33:19.140569', '', '2024-04-22 09:33:19.140589', 125, 3, 1214, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1721, 'Aktueller Stand (Besuche im April) gemäss Matomo', '2024-04-22 09:35:14.919728', '', '2024-04-22 09:35:14.91974', 430, 3, 1212, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1724, 'Abgleich mit Lüku - Nextcloud Collections wird zum ausprobieren zur Verfügung gestellt, alternative Github-Wiki zu prüfen, Fallback Gitlab Wiki', '2024-04-22 09:40:23.177101', '', '2024-04-22 09:40:23.177103', NULL, 3, 1223, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1725, 'Aktueller Stand unklar', '2024-04-22 09:44:44.341328', '', '2024-04-22 09:44:44.34133', NULL, 3, 1220, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1733, 'hat nicht sauber funktioniert. Wir mussten 4 Umgebungen anschliessend nochmals Releasen', '2024-04-23 09:00:53.167846', 'Umgang mit Sentry Issue im Team besprochen. Momentann sind wir bei 210', '2024-04-23 09:00:53.167868', 0.2, 41, 1151, 3, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1746, '', '2024-04-23 16:27:24.621936', '', '2024-04-23 16:27:24.621943', 766193, 60, 1230, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1747, 'Zwei Personen auf der Shortlist', '2024-04-23 16:31:31.645004', '', '2024-04-23 16:31:31.645012', NULL, 60, 1216, 5, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1748, '246	386	Dani
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
', '2024-04-23 16:36:55.7218', '', '2024-04-23 16:36:55.721808', 70.63, 33, 1174, 8, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1756, 'Feinheiten beim Give Away-Konzept werden ausgearbeitet. Anschliessend wird es dem Nachhaltigkeitsbranch präsentiert.
Abklärungen des Give Awaysa für den Puzzle Up! laufen', '2024-04-24 08:05:16.209273', '', '2024-04-24 08:05:28.469931', NULL, 26, 1184, 7, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1755, 'Sponsoringanfragen an GCP, Acend, Red Hat und Isovalent sind raus. Rückmeldungen ausstehend.', '2024-04-24 08:04:05.732178', '', '2024-04-24 08:06:30.65757', NULL, 26, 1183, 7, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1762, 'Workshop durchgeführt, Arbeitsgruppe erstellt, die das Thema weiterverfolgt', '2024-04-26 15:49:11.083901', 'In Arbeitsgruppe anhand bestehender SO-Frameworks/ -Methoden erarbeiten, wie wir die SO in /dev/tre in Guidelines verankern wollen', '2024-04-26 15:49:11.083908', NULL, 4, 1196, 10, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1763, '', '2024-04-26 15:49:43.336755', '', '2024-04-26 15:49:43.336761', 0, 4, 1197, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1766, 'Workshop durchgeführt & Thema besprochen, Übersicht noch nicht erstellt', '2024-04-26 15:52:44.07951', '', '2024-04-26 15:52:44.079517', NULL, 4, 1221, 10, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1767, 'N/A', '2024-04-26 15:53:16.020864', 'Gemäss KR', '2024-04-26 15:53:16.02087', NULL, 4, 1224, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1773, 'In Arbeit - braucht mehr als angenommen.', '2024-04-29 05:48:10.545727', '', '2024-04-29 05:48:10.545734', NULL, 31, 1211, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1774, 'In Arbeit - erste Diskussion geführt und nächste Schritte definiert.', '2024-04-29 05:49:19.318546', '', '2024-04-29 05:49:19.318552', NULL, 31, 1229, 4, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1777, 'Wird im Mai/Juni angegangen', '2024-04-29 05:52:48.398437', '', '2024-04-29 05:52:48.398443', NULL, 31, 1222, 8, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1778, 'Keine Veränderung - 2 zusätzliche interessierte Personen wurden abgelehnt.', '2024-04-29 05:53:36.82726', '', '2024-04-29 05:53:36.827267', NULL, 31, 1216, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1831, '', '2024-05-02 06:59:27.541742', '', '2024-05-02 06:59:27.54175', NULL, 26, 1182, 7, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1846, 'N/A', '2024-05-06 06:55:37.967358', 'siehe Action Plan', '2024-05-06 06:55:37.967366', 0, 4, 1199, 0, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1872, 'Bidu hat DevOps Engineer abgeschlossen', '2024-05-08 17:33:46.190554', '', '2024-05-08 17:33:46.190561', NULL, 40, 1207, 7, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1884, 'Gutes Angebot bei Mobi für ii gemacht. Beauftragung ausstehen.', '2024-05-13 06:19:56.653737', '', '2024-05-13 06:19:56.653745', NULL, 5, 1140, 6, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1885, 'Von Sales kommt wenig aktiv.', '2024-05-13 06:21:59.698801', '', '2024-05-13 06:21:59.698808', 0, 5, 1144, 2, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1906, 'Wir warten auf das finale Go on WLY, damit wir mit den SEO Korrekturen auf der neuen Website beginnen können. ', '2024-05-15 05:12:06.50115', '', '2024-05-15 05:12:06.501158', NULL, 49, 1179, 8, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1915, 'Status quo', '2024-05-16 06:28:22.303261', '', '2024-05-16 06:28:33.020904', NULL, 26, 1182, 8, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (1920, 'Einige Ideen sind aus RubyOps entstanden', '2024-05-21 06:54:15.193003', '', '2024-05-21 06:54:15.193009', NULL, 28, 1164, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1921, '', '2024-05-21 06:54:20.963808', '', '2024-05-21 06:54:20.963816', NULL, 28, 1231, 2, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1923, '', '2024-05-21 06:54:42.099462', '', '2024-05-21 06:54:42.099469', NULL, 28, 1232, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1924, '', '2024-05-21 06:54:52.063664', '', '2024-05-21 06:54:52.06368', NULL, 28, 1167, 2, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1939, 'Mit CTO abgestimmt', '2024-05-21 14:11:49.704266', '', '2024-05-21 14:11:49.70427', NULL, 5, 1147, 10, 'ordinal', 'STRETCH', 0);
INSERT INTO okr_pitc.check_in VALUES (2005, '', '2024-05-27 10:52:39.069966', '', '2024-05-27 10:52:39.06997', 893, 36, 1212, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2300, 'Einladung verschickt für Kickoff, Actionplan muss noch erstellt werden', '2024-07-22 08:14:03.072382', '', '2024-07-22 08:14:03.072385', NULL, 36, 1319, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1097, 'Ziel wurde im Oktober und November verfehlt', '2023-12-18 11:53:38.752973', '', '2023-12-18 11:53:38.752981', 0, 24, 144, 1, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1443, 'Auslegeordnung gemacht. Meetup Speaker gesetzt. Ruby Meetup bei Puzzle verschieben (zu nahe an der Helvetic Ruby)', '2024-02-26 09:09:44.98064', '', '2024-02-26 09:09:44.980643', NULL, 28, 1087, 4, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1587, 'Status Quo', '2024-03-18 15:35:17.788703', '', '2024-03-18 15:35:17.788709', NULL, 4, 1090, 10, 'ordinal', 'STRETCH', 1);
INSERT INTO okr_pitc.check_in VALUES (1655, 'Dävu Schneider involviert und seine Inputs aufgenommen. Wird am nächsten Austausch dabei sein.', '2024-04-08 09:06:29.84513', '', '2024-04-08 09:06:29.845134', NULL, 31, 1229, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1671, 'noch offen ', '2024-04-11 13:36:50.896976', '', '2024-04-11 13:36:50.896981', NULL, 41, 1152, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1685, '', '2024-04-15 12:32:48.153975', '', '2024-04-15 12:32:48.153978', 0, 4, 1199, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1686, 'N/A', '2024-04-15 12:32:59.132224', '', '2024-04-15 12:32:59.132228', NULL, 4, 1204, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1689, 'Absolute Verrechnebarkeit Februar 2024: 62.4% // März sieht nach knapp über 70% aus, d.h. "Commit"', '2024-04-15 12:35:51.975439', '', '2024-04-15 12:35:51.975443', NULL, 4, 1225, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2360, 'Konzept Auswertung noch nicht erstellt. ', '2024-08-05 07:09:38.05318', 'Evtl. jemand mit tiefer Auslastung beauftragen. ', '2024-08-05 07:09:38.053183', NULL, 3, 1316, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1690, 'Identifizierte Aufträge: BAFU SAM, UniBern Studentenanmeldung, SAC, Kanton Aargau', '2024-04-15 12:36:40.317677', '... weitere Aufträge identifizieren', '2024-04-16 14:46:23.855154', NULL, 4, 1227, 10, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (1705, 'Bereits eine interessierte Person', '2024-04-17 12:32:35.369377', '', '2024-04-17 12:32:35.369382', NULL, 40, 1216, 5, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1706, 'Noch nicht alle Verlängerungen abgeschlossen.', '2024-04-17 12:35:31.912394', '', '2024-04-17 12:35:31.912398', 673053, 40, 1230, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1722, 'Seite aktualisiert (neue Themen), eine Individuelle Offerte gestellt (OpenGIS). ', '2024-04-22 09:37:40.488625', '', '2024-04-22 09:37:40.488627', NULL, 3, 1215, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1734, 'ISO 9001 weisst doch einige Lücken auf die einen effizienten Fortschritt hindern....

Die Hauptprozesse  Infrastruktur wurde mit den Zuständigen besprochen und werden erarbeitet ', '2024-04-23 09:03:52.444103', '- Zusätzliche Unterstützung durch Nicole für die Aufarbeitung der relevanten GEsetzte für Puzzle ', '2024-04-23 09:03:52.44412', NULL, 41, 1154, 6, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1738, 'Saraina stellt zu recht in Frage, ob die Kommunikation nur schriftlich erfolgen soll oder nicht auch mündlich. Aber dann später am Sommerinfo Anlass.', '2024-04-23 09:26:24.925541', '', '2024-04-23 09:26:24.925559', 0, 5, 1137, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1749, 'Bisher 1 Techinterview (L.S.)', '2024-04-23 16:38:32.688558', '', '2024-04-23 16:38:32.688581', NULL, 33, 1175, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1757, 'Open Space Leadership Monthly ist geplant für Ende April (abr)
Blogpost in Erarbeitung
Sponsoringauftritt KCD in Erarbeitung
Direkt Mailing in Erarbeitung', '2024-04-24 08:07:47.887951', '', '2024-04-24 08:07:47.887958', NULL, 26, 1193, 8, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1770, 'Inserat geschrieben aber noch nicht publiziert. Erstgespräch mit Spontanbewerber durchgeführt, welcher passen könnte', '2024-04-26 15:55:59.081149', '', '2024-04-26 15:55:59.081155', NULL, 4, 1228, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1775, 'Keine Veränderung', '2024-04-29 05:49:33.507916', '', '2024-04-29 05:49:33.507924', NULL, 31, 1209, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1832, 'Aktuell warten wir auf Feedback von den Angefragten.', '2024-05-02 07:14:33.548046', '', '2024-05-02 07:14:33.548055', NULL, 26, 1183, 7, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1833, 'Status quo', '2024-05-02 07:16:41.090748', '', '2024-05-02 07:16:41.09076', NULL, 26, 1184, 7, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1834, 'Adi stellte imagine am monthly vor. ', '2024-05-02 07:17:31.554138', '', '2024-05-02 07:17:31.554153', NULL, 57, 1193, 8, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1847, 'N/A', '2024-05-06 06:56:16.491582', 'Mit /ux, /mobility & /ruby abgleichen.', '2024-05-06 06:56:16.491589', NULL, 4, 1204, 10, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1851, 'N/A', '2024-05-06 06:59:03.352468', 'Dran bleiben', '2024-05-06 06:59:03.352479', NULL, 4, 1227, 10, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (1873, 'Unverändert, in Arbeit', '2024-05-08 17:34:21.898721', '', '2024-05-08 17:34:21.898732', NULL, 40, 1211, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1886, 'Workshop im September mit OpenGIS', '2024-05-13 06:56:33.76382', '', '2024-05-13 06:56:33.763827', NULL, 3, 1215, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1907, 'Schulung absolviert. Diese und nächste Woche Fokus auf Seitenaufbau & Services & Solutions Inhalte.', '2024-05-15 05:12:55.049747', '', '2024-05-15 05:12:55.049754', 5, 49, 1180, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1908, 'Weitere Aufträge an Mayra haben sich durch die Schulung ergeben und können dann gleich im neuen Wordpress eingesetzt werden. Animationen werden getestet.', '2024-05-15 05:13:41.734952', '', '2024-05-15 05:13:41.734959', 5, 49, 1200, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1916, '', '2024-05-16 06:28:58.659802', '', '2024-05-16 06:28:58.659813', NULL, 26, 1183, 7, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1925, '', '2024-05-21 06:57:09.375025', '', '2024-05-21 06:57:09.375029', 1043679, 28, 1168, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1940, '', '2024-05-21 14:18:05.189451', '', '2024-05-21 14:18:05.189455', 826, 36, 1212, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1941, 'Erledigt ist:
- Retromeeting der ersten 3 Monate ITPoint-Support
- Pikettplanung ist bis Ende Jahr definiert
- Erste zwei Retromeeting mit ITPoint sind aufgegleist
- erste konkrete Supportfälle sind gesammelt, analysiert und mit dem Team geteilt
- Grafik des Supportprozesses ist erstellt und mit dem Kunden geteilt', '2024-05-22 06:28:55.482035', 'weiter so', '2024-05-22 06:28:55.482039', 5, 32, 1160, 9, 'metric', NULL, 6);
INSERT INTO okr_pitc.check_in VALUES (1922, '', '2024-05-21 06:54:31.589922', '', '2024-05-23 12:01:02.587063', NULL, 28, 1165, 3, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2026, 'Die im Workshop 23.4.3034 definierten Fokusthemen zu "Arbeit im Team", finden sich im Manifest wieder. Die Fokusthemen sind: Ziele, Verantwortung und Beteiligung an Entscheiden
"', '2024-05-31 09:38:41.344382', 'Weiterarbeit gemäss Action Plan', '2024-05-31 09:38:41.34439', 1, 4, 1199, 0, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2301, 'Am 29.7. Zweitgespräch, am 24.7. Erstgespräch', '2024-07-22 15:25:37.252868', '', '2024-07-22 15:25:37.25287', -0.9, 5, 1259, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2302, '', '2024-07-22 15:26:00.786357', '', '2024-07-22 15:26:00.78636', NULL, 5, 1261, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2304, 'je auch zum Kick-off dazugenommen', '2024-07-22 15:27:19.984323', '', '2024-07-22 15:27:19.984326', 0, 5, 1275, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2310, 'Status Quo', '2024-07-23 06:57:59.58275', '', '2024-07-23 06:57:59.58276', NULL, 49, 1279, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2359, 'Kein Post letzte Woche', '2024-08-05 07:09:02.263778', '', '2024-08-05 07:09:02.263782', 3, 3, 1315, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2363, 'Noch keine Veränderung', '2024-08-05 07:11:27.959652', 'Migration in aktives Nextcloud diese Woche. ', '2024-08-05 07:11:27.959657', NULL, 3, 1321, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2389, 'Status Quo', '2024-08-12 07:05:03.851363', '', '2024-08-12 07:05:03.851368', NULL, 4, 1288, 10, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2390, 'Status Quo', '2024-08-12 07:05:03.913956', '', '2024-08-12 07:05:03.913961', NULL, 4, 1288, 10, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2391, 'Status Quo', '2024-08-12 07:05:54.641672', '', '2024-08-12 07:05:54.641676', NULL, 4, 1290, 10, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2392, 'Status Quo', '2024-08-12 07:06:16.128172', '', '2024-08-12 07:06:16.128179', NULL, 4, 1292, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2418, 'welcome pascou-Interview', '2024-08-12 11:09:36.674772', '', '2024-08-12 11:09:36.674776', 4, 3, 1315, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2456, 'Der Promt ist bei Saraina und Franzi in der Vernehmlassung. Wir finalisieren ihn gemeinsam am 10. September', '2024-08-19 12:32:55.839646', '', '2024-08-19 12:32:55.839651', NULL, 49, 1279, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1098, 'Absolute Verrechenbarkeit November: 48.4% - Durchschnitt: 47.3%', '2023-12-18 12:14:41.538034', '', '2023-12-18 12:15:04.136792', 0, 22, 143, 1, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1444, '', '2024-02-26 09:10:01.154876', '', '2024-02-26 09:10:01.154879', NULL, 28, 1089, 7, 'ordinal', 'TARGET', 2);
INSERT INTO okr_pitc.check_in VALUES (1588, 'Keine Veränderung (Aufträge die nicht bei uns laufen BKD kommt noch hinzu). Ausserdem ist noch Budget offen bei BLS, da kennen wir die Planung noch nicht ganz von der BLS. Also kommt sicher noch einiges zu den 129K dazu', '2024-03-18 18:28:07.583954', '', '2024-03-18 18:28:07.583956', 129934, 36, 1048, 3, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1590, 'Grundsätzlich haben ab April alle zu tun und sind ziemlich ausgelastet. Es gibt einfach ein paar kleinere Löcher bei einzelnen Members die zusammen ca. 20-40% Kapa ausmachen. ', '2024-03-18 18:33:25.609059', 'Sales / Sales / Sales', '2024-03-18 18:33:25.609062', 0.4, 36, 1053, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1656, '', '2024-04-08 11:07:30.517459', '', '2024-04-08 11:07:30.517467', 464220, 60, 1230, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2125, 'OGZH Deal gewonnen', '2024-06-14 13:03:43.354514', '', '2024-06-14 13:04:05.835333', 968405, 31, 1230, 1, 'metric', NULL, 3);
INSERT INTO okr_pitc.check_in VALUES (2029, 'Übersicht erstellt, in Teammeeting besprochen, in Weekly integriert
', '2024-06-02 12:54:58.986993', 'Massnahmen: Events: Anwenden als fixer Bestandteil im Weekly / Sponsoring: T.B.D / Mit Markom besprechen', '2024-06-02 12:55:11.072871', NULL, 4, 1221, 0, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (1691, 'N/A', '2024-04-15 12:37:34.515178', '', '2024-04-15 12:37:34.515183', NULL, 4, 1228, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1707, 'Schätzungen werden beim nächsten Grooming gemacht.', '2024-04-17 12:57:51.576227', '', '2024-04-17 12:57:51.57623', NULL, 31, 1209, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1723, 'Keine Änderung (Mail bei Simu)', '2024-04-22 09:38:20.287439', '', '2024-04-22 09:38:20.287441', NULL, 3, 1218, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1672, 'wir sind auf einem guten Weg', '2024-04-11 13:37:29.314764', 'noch weitere Fokustage reservieren', '2024-04-23 09:01:25.845548', NULL, 41, 1154, 6, 'ordinal', 'FAIL', 2);
INSERT INTO okr_pitc.check_in VALUES (1735, 'Geburtstagsparty dsm/sfa: 7 TeilnehmerInnen // Pingpong Indoor Masters: 7 TeilnehmerInnen. Kumuliert: 7+7=14', '2024-04-23 09:06:16.447869', 'Monty Python Abend und Teamwanderung organisieren.', '2024-04-23 09:06:16.447886', 14, 33, 1173, 8, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1740, '', '2024-04-23 09:27:33.038108', '', '2024-04-23 09:27:33.038126', NULL, 5, 1147, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1750, 'Erstgespräche mit Interessenten sind geplant aber noch nicht durchgeführt.', '2024-04-23 16:39:54.326483', 'Erstgespräche durchführen, ggf. Fübi-Treffen auf Terrasse durchführen, anschliessend Tech-Interviews...', '2024-04-23 16:39:54.326491', NULL, 33, 1234, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1758, '', '2024-04-24 13:19:39.875461', '', '2024-04-24 13:19:39.875469', NULL, 5, 1138, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1776, 'Soweit gut.. Werden nach einem Monat eine Auslegeordnung machen.', '2024-04-29 05:51:52.394618', '', '2024-04-29 05:51:52.394625', NULL, 31, 1213, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1835, 'Controlling System ist implementiert. Einige Massnahmen schon getrackt.', '2024-05-02 15:39:45.562491', '', '2024-05-02 15:40:08.620847', NULL, 5, 1146, 10, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (1837, 'Validierung mit Techboard erstellt. ', '2024-05-02 15:43:14.369094', '', '2024-05-02 15:43:14.369102', NULL, 5, 1138, 7, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1848, 'N/A', '2024-05-06 06:57:04.801754', 'Übersicht erstellen, Besprechung in Weekly etablieren', '2024-05-06 06:57:04.801765', NULL, 4, 1221, 10, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1852, 'Mit Spontanberwerber gesprochen. Inserat noch nicht geschaltet. Habe mich (Mäge) entschieden, später auszuschreiben, damit die Bewerbende mit Mätthu Liechti besprochen und geprüft werden können', '2024-05-06 07:00:24.49276', 'Ausschreiben', '2024-05-06 07:00:34.93769', NULL, 4, 1228, 0, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1850, 'April könnte 75% erreichen, der Quartalsdurchschnitt liegt aber tiefer', '2024-05-06 06:58:25.821663', 'N/A', '2024-05-06 07:00:46.592448', NULL, 4, 1225, 0, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1875, 'Backlogs wurden aufgeräumt, OV Backlog ist quasi leer.', '2024-05-08 17:37:34.533088', '', '2024-05-08 17:37:34.533095', NULL, 40, 1209, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1887, 'Keine Änderung', '2024-05-13 06:58:20.005066', '', '2024-05-13 06:58:20.005074', 92424, 3, 1191, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1874, 'Dave, Pippo und Dänu haben Möglichkeiten für Angebot besprochen.
Kubernetes-nahe Entwicklung oder Go-Applikation.
Wir tendieren zu Ersterem. Iwan im Vorstellungs-Prozess bei der Mobi.', '2024-05-08 17:35:40.666306', '', '2024-05-13 07:27:32.602674', NULL, 40, 1229, 6, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1909, 'Es wurde eine zusätzliche Stelle ausgeschrieben. Total: 6
Es konnte ein Arbeitsvertrag abgeschlossen werden. Total: 1', '2024-05-15 09:25:56.824422', '', '2024-05-15 09:26:10.165219', NULL, 22, 1128, 8, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1917, 'Richtlinien des Nachhaltigkeitsbranch bekannt, Give Away Konzept wird am Puzzle Inside präsentiert.', '2024-05-16 06:30:05.150767', '', '2024-05-16 06:30:05.150774', NULL, 26, 1184, 7, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1926, 'RohrMax Backlog ist noch nicht priorisiert (dem Team fehlt die Zeit für Interne Themen)', '2024-05-21 11:17:57.629582', '', '2024-05-21 11:17:57.629586', NULL, 60, 1209, 2, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1927, 'Unverändert (Momentan keine Prio beim Mid-Lead)', '2024-05-21 11:18:57.382291', '', '2024-05-21 11:19:05.782297', NULL, 60, 1211, 0, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1942, 'Immer noch okay. Wollen wir diesen Wert überhaupt möglichst hoch haben?', '2024-05-22 06:32:58.769023', 'weiter so.', '2024-05-22 06:32:58.769026', 46, 32, 1157, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2027, 'N/A', '2024-05-31 09:59:40.051622', 'Mit /ux, /mobility & /ruby abgleichen. An Markom und im LST kommunizieren', '2024-05-31 09:59:40.05163', NULL, 4, 1204, 0, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2033, '', '2024-06-03 07:00:10.113294', '', '2024-06-03 07:00:10.113301', 96096, 36, 1191, 3, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2034, 'keine Veränderung', '2024-06-03 07:00:36.952373', '', '2024-06-03 07:00:36.952381', NULL, 36, 1206, 3, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2035, 'erarbeiten wir heute', '2024-06-03 07:00:54.792531', '', '2024-06-03 07:00:54.792538', NULL, 36, 1208, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2036, 'SoMe Post über Device Lab', '2024-06-03 07:01:20.027173', '', '2024-06-03 07:01:20.02718', 9, 36, 1210, 9, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2037, '', '2024-06-03 07:01:32.970878', '', '2024-06-03 07:01:32.970885', 155, 36, 1214, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2045, 'Sales intern Validierung aufgesetzt. Sales extern wird nicht durchgeführt. Dafür mit Yup zeitnah im Juli', '2024-06-03 14:34:32.703275', '', '2024-06-03 14:34:32.703283', NULL, 5, 1138, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2054, 'Es gibt noch einen Bug in den Blogposts bei der neuen Website. Daher Status Quo. ', '2024-06-04 07:15:07.400095', '', '2024-06-04 07:15:07.400102', NULL, 26, 1179, 8, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2055, '', '2024-06-04 07:16:03.254714', '', '2024-06-04 07:16:03.25472', 10, 26, 1180, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2061, 'Es konnte ein zusätzlicher Arbeitsvertrag abgeschlossen werden. Total: 2', '2024-06-06 09:34:44.739921', '', '2024-06-06 09:34:44.739927', NULL, 22, 1128, 10, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (2062, '65.5 h erreicht ', '2024-06-07 11:35:57.793436', '', '2024-06-07 11:35:57.793441', NULL, 41, 1149, 2, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2064, 'wir sind noch nicht weiter', '2024-06-07 11:37:01.6608', '', '2024-06-07 11:37:01.660806', NULL, 41, 1153, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2067, 'internes Audit war sehr zufriedenstellend, nur noch kleinere Anpassunge', '2024-06-07 11:38:55.715456', '', '2024-06-07 11:38:55.715461', NULL, 41, 1154, 8, 'ordinal', 'STRETCH', 2);
INSERT INTO okr_pitc.check_in VALUES (2068, 'Suricata läuft und ist brauchbar zumindest für forensische Auswertung.', '2024-06-10 07:00:38.577713', '', '2024-06-10 07:00:38.577718', NULL, 27, 1187, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2069, 'Phil arbeitet daran.', '2024-06-10 07:01:12.662588', '', '2024-06-10 07:01:12.662593', NULL, 27, 1189, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2070, '', '2024-06-10 07:01:27.325076', '', '2024-06-10 07:01:27.325082', NULL, 27, 1185, 8, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2090, '', '2024-06-11 09:19:01.147103', '', '2024-06-11 09:19:01.147107', NULL, 24, 1131, 1, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1099, 'haben einiges mit CHF 175 offeriert', '2023-12-18 13:47:39.878518', '', '2023-12-18 13:47:39.878525', 0.3, 36, 216, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1445, 'Mit zusätzlichen Mobi-Aufträgen, Zusatzbudget des Bundesarchivs, den Abgängen/ Abwesenheiten, zwei BKD-Aufträgen bis Mitte Jahr, Bedarf bei SAC, Peerdom-Intesivierung  sind die /dev/tre Members nun mittel-/ langfristig ausgebucht', '2024-02-26 09:47:33.931617', 'Clara nach Ausstieg bei Acrevis in ein andere Projekt (BUAR Siarde, MOBI Gaia, ...) onboarden', '2024-02-26 09:47:33.93162', 5, 4, 1081, 10, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1446, 'Liima- und SWOA-Angebot kurz vor Zustellung, SWOA wird schon mal angepackt. Bei Liima viel gemacht, Bundesarchiv hats Zusatzbudget gegeben - ergo: Die Auslastung mit "bestehenden" Aufträgen ist nicht mehr ganz so relevant', '2024-02-26 09:49:26.328879', 'Liima, SWOA klären', '2024-02-26 09:49:36.947266', NULL, 4, 1082, 8, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (1448, 'Status Quo. Kein neuer Lead dazu gekommen. Dennoch nun ausgelastet', '2024-02-26 09:53:04.521745', 'Dran bleiben, um neue Leads zu generieren', '2024-02-26 09:53:04.521747', 3, 4, 1086, 3, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1449, 'Kanton Aargau, Mobi (Gebäudeversicherung, JBat2>3-Migration)', '2024-02-26 09:54:20.962474', 'Offerte für Mobi noch einreichen', '2024-02-26 09:54:20.962477', NULL, 4, 1090, 7, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1589, 'Für Berti leider keinen verrechenbaren Einsatz gefunden', '2024-03-18 18:29:18.380997', '', '2024-03-18 18:29:18.380999', NULL, 36, 1049, 1, 'ordinal', 'COMMIT', 3);
INSERT INTO okr_pitc.check_in VALUES (1726, 'Wir sind mit UniLuzern nicht nur auf einen Neukunden zugegangen, sondern haben ihn auch gewonnen!', '2024-04-22 13:03:18.543334', 'Auf Ergebnisse von Tinu warten.', '2024-04-22 13:03:18.543336', 1, 32, 1158, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1736, 'Marktvalidierung erfolgt, nächste Action Points geplant', '2024-04-23 09:11:17.333759', '', '2024-04-23 09:11:42.184544', NULL, 5, 1140, 4, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1737, '', '2024-04-23 09:24:09.465429', '', '2024-04-23 09:24:09.465447', NULL, 5, 1138, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1739, '', '2024-04-23 09:27:05.172257', '', '2024-04-23 09:27:05.172273', NULL, 5, 1146, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1779, 'in Arbeit', '2024-04-29 06:14:42.253346', '', '2024-04-29 06:14:42.253353', NULL, 13, 1129, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1836, 'Die Stossrichtungen sind geplant auf die nächsten Quartale', '2024-05-02 15:40:55.970244', '', '2024-05-02 15:40:55.970252', NULL, 5, 1147, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1849, 'N/A', '2024-05-06 06:57:27.164475', 'Gemäss KR', '2024-05-06 06:57:27.164483', NULL, 4, 1224, 10, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1876, 'Im nächsten Team-Meeting Ideensammlung', '2024-05-08 17:39:46.179669', '', '2024-05-08 17:39:46.179676', NULL, 40, 1222, 8, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1877, 'Unverändert.', '2024-05-08 17:40:19.498087', '', '2024-05-08 17:40:19.498113', NULL, 40, 1216, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1878, 'Wir warten immer noch auf Romeo.
Medidata in Aussicht (3rd Level Plus).', '2024-05-08 17:41:08.824613', '', '2024-05-08 17:41:08.824621', NULL, 40, 1217, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1888, 'Aktuell keine Änderung', '2024-05-13 07:06:58.659283', '', '2024-05-13 07:06:58.659293', NULL, 3, 1206, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1889, 'Keine Änderung', '2024-05-13 07:07:12.009153', '', '2024-05-13 07:07:12.009161', NULL, 3, 1208, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1890, 'On Track (Phippu)', '2024-05-13 07:07:24.071774', '', '2024-05-13 07:07:24.071782', 6, 3, 1210, 9, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1910, '- Die Verteilung der MO in der GL ist erfolgt
- Für die Mittelfristsicht der Strategie sollen Moals (mid-term goals) verwendet werden (Info und Entscheid am Monthly vom 27.05.)
- die Inhalte der Moals werden aktuell ausgearbeitet', '2024-05-15 11:20:18.576311', '', '2024-05-15 11:20:18.576317', NULL, 16, 1132, 8, 'ordinal', 'FAIL', 2);
INSERT INTO okr_pitc.check_in VALUES (1918, 'Blogpost Imagine wurde veröffentlicht (inkl. SoMe). ', '2024-05-16 06:42:11.832574', '', '2024-05-16 06:42:11.83258', NULL, 57, 1193, 8, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1928, '', '2024-05-21 11:32:37.474241', '', '2024-05-21 11:32:37.474245', 16000, 5, 1141, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1943, 'Keine Actions von Seiten Tinu/Richmond Forum', '2024-05-22 06:41:34.515647', 'aus Tinu warten', '2024-05-22 06:41:34.515651', 2, 32, 1158, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1945, 'Präsi Erkenntnisse und Empfehlung in Teammeeting 14.5.2024 zwecks Kenntnisnahme und Entscheids zur Weiterwarbeit', '2024-05-22 06:48:35.024008', 'Erarbeiten der Massnahmen', '2024-05-22 06:48:35.024013', NULL, 4, 1194, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1948, 'N/A', '2024-05-22 06:50:38.884707', 'siehe Action Plan', '2024-05-22 06:50:38.884712', 0, 4, 1199, 0, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1959, 'Draft erstellt, wir sind ready fürs interne Audit.', '2024-05-23 08:07:30.378125', '', '2024-05-23 08:07:30.378128', NULL, 13, 1129, 8, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1961, 'Members mit mehr als 10 Tage ungeplante Ferien abgeholt', '2024-05-24 10:00:39.197438', '', '2024-05-24 10:00:39.197446', NULL, 28, 1232, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1962, '', '2024-05-27 06:01:31.906465', '', '2024-05-27 06:01:31.906469', 0, 5, 1137, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1963, 'Es fehlt noch die Validierung mit (einem) externen Spezialisten. ', '2024-05-27 06:03:36.446751', '', '2024-05-27 06:03:36.446756', NULL, 5, 1138, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2028, 'Internes Audit erfolgreich absolviert, ISO14001 in ISO9001 vollständig integriert, wir sind ready für die Zertifizierung.', '2024-05-31 15:26:26.996174', '', '2024-05-31 15:26:26.99618', NULL, 13, 1129, 10, 'ordinal', 'STRETCH', 0);
INSERT INTO okr_pitc.check_in VALUES (2030, ' Ideen gesammelt, kategorisiert und eingeordet - Ideen im Team besprochen (Weeklys)', '2024-06-02 12:56:06.448035', 'Liste der Sponsoring-Aktivitäten finalisiert. Abgleich mit /ux, /mobiity und /ruby – Bereichtsübergreifende Zusammenfassung & Kommunikation an LST und Markom ', '2024-06-02 12:56:06.448041', NULL, 4, 1224, 0, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2038, '', '2024-06-03 07:02:00.217048', '', '2024-06-03 07:02:00.217055', 955, 36, 1212, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2046, 'Wir haben entschieden, die Kommunikation nur mündlich am Puzzle Workshop zu machen. Dafür zu einem Open Space für Kritik und Input einzuladen', '2024-06-03 14:37:34.997244', '', '2024-06-03 14:37:34.997251', 0, 5, 1137, 0, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2056, '', '2024-06-04 07:16:11.869323', '', '2024-06-04 07:16:11.86933', 10, 26, 1200, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2057, 'Planung wird im Leadership-Monthly im Juni verabschiedet.', '2024-06-04 07:19:08.712755', '', '2024-06-04 07:19:08.712762', NULL, 26, 1182, 8, 'ordinal', 'TARGET', 2);
INSERT INTO okr_pitc.check_in VALUES (2063, 'wir sind noch nicht weiter', '2024-06-07 11:36:39.168141', '', '2024-06-07 11:36:39.168146', NULL, 41, 1150, 2, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2071, 'keine Veränderung seit letztem Checkin', '2024-06-10 08:08:45.727186', 'Swiss Life Bestätigung einholen und planen
Kt. Aargau follow up
WTO Kt. Zug follow up
WTO UVEK teilnehmen
BAFU UX mit Oli ', '2024-06-10 08:08:45.727191', 96098, 36, 1191, 2, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2091, 'unverändert --> Warten auf SHKB', '2024-06-11 12:12:50.369652', '', '2024-06-11 12:12:50.369655', NULL, 60, 1217, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (264, 'Offizielle Zahl Marcel', '2023-09-06 06:00:56.98941', '', '2023-09-05 22:00:00', 79.8, 5, 33, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2102, '', '2024-06-12 09:05:42.059323', '', '2024-06-12 09:05:42.059327', NULL, 28, 1231, 2, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2101, 'Openshift migr ist geplant.', '2024-06-12 09:05:31.937912', '', '2024-06-12 09:07:05.113514', NULL, 28, 1164, 1, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2104, '', '2024-06-12 09:07:27.155356', '', '2024-06-12 09:07:27.15536', NULL, 28, 1232, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2106, '', '2024-06-12 09:17:59.177891', '', '2024-06-12 09:17:59.177894', 1101566, 28, 1168, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2303, 'Mit Innoarchitects Vorgehen und Tools besprochen', '2024-07-22 15:26:39.831665', '', '2024-07-22 15:26:39.831668', NULL, 5, 1263, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2309, 'Keine weiteren Tätigkeiten von /mobilty stattgefunden', '2024-07-22 15:32:39.983177', '', '2024-07-22 15:32:39.983179', 0, 5, 1277, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2369, '', '2024-08-07 09:37:16.186955', '', '2024-08-07 09:37:16.186959', 4.6, 22, 1253, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1100, '', '2023-12-18 14:37:26.762973', '', '2023-12-18 14:37:26.762979', 113080, 36, 215, 3, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1447, 'Mehrere Events von mehreren Members besucht. Plan und Doku hinkt hinterher. Dennoch mehr Target als Commit', '2024-02-26 09:51:18.10557', 'Plan abbilden, mit Members besprochen, als Kommunikationswerkzeug etablieren', '2024-02-26 09:52:07.185941', NULL, 4, 1084, 6, 'ordinal', 'TARGET', 2);
INSERT INTO okr_pitc.check_in VALUES (1591, 'Tobis  Blogpost geht nächste Woche (Ende März) im Puzzle Newsletter raus', '2024-03-18 18:41:16.568434', '', '2024-03-18 18:41:16.568436', 3, 36, 1056, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1727, 'very nice', '2024-04-22 13:10:57.011394', '', '2024-04-22 13:10:57.011397', 49, 32, 1157, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2066, '', '2024-06-07 11:38:21.098907', 'Feature ready ist nun gemäss Roadmap MMP auf Ende August geplant', '2024-06-07 11:38:21.098913', NULL, 41, 1156, 4, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2072, 'keine Veränderung', '2024-06-10 08:09:25.138813', '', '2024-06-10 08:09:25.13882', NULL, 36, 1206, 3, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2092, 'Lead Frequentis aus /mid-Journey an Kubecon', '2024-06-11 13:02:50.063092', '', '2024-06-11 13:02:50.063096', NULL, 40, 1188, 10, 'ordinal', 'STRETCH', 0);
INSERT INTO okr_pitc.check_in VALUES (2094, 'Keine Veränderung, lassen es bleiben. Wird Platform Engineering Thema eingebettet.', '2024-06-11 13:04:56.874009', '', '2024-06-11 13:04:56.874013', NULL, 40, 1211, 0, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1728, 'Pikettplanung ist erfolgt und Support-Grafik ist erstellt und an ITPoint versendet und abgesegnet.
Ausserdem ist die Einladung für die ersten beiden Support-Retros an Michael Böbner ITPoint raus.
Die ersten Supportfälle ITPoint sind gesammelt und werden am kommenden DO mit dem Team geteilt.
BFH sind noch keine Retros möglich weil es noch kein Support-Fall gegeben hat :-)', '2024-04-22 13:13:01.420903', '', '2024-04-22 13:27:32.851331', 2, 32, 1160, 7, 'metric', NULL, 6);
INSERT INTO okr_pitc.check_in VALUES (1741, '', '2024-04-23 09:29:12.731241', '', '2024-04-23 09:29:12.73126', 0, 5, 1144, 3, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2103, 'Kickoff Hit Architektur Board hat stattgefunden. Noch nicht final geklärt. Komm noch offen.', '2024-06-12 09:06:39.023529', '', '2024-06-12 09:06:39.023533', NULL, 28, 1165, 3, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2305, 'Aufgrund von Ferienabwesenheiten noch keine Tätigkeiten', '2024-07-22 15:27:53.450101', '', '2024-07-22 15:27:53.450103', NULL, 5, 1262, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2306, 'ii für einen neuen Auftrag bei Mobi eingereicht. js bei SBB (ist aber nicht unbedingt durch neue Leads entstanden)', '2024-07-22 15:30:00.715178', '', '2024-07-22 15:30:00.715181', NULL, 5, 1266, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2307, 'Termin aufgesetzt mit Mäge', '2024-07-22 15:30:35.76184', '', '2024-07-22 15:30:35.761842', NULL, 5, 1267, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1780, '', '2024-04-29 06:15:36.573589', '', '2024-05-02 08:11:43.07251', 2998179, 13, 1134, 5, 'metric', NULL, 7);
INSERT INTO okr_pitc.check_in VALUES (1838, 'Schulungstermin ist 3. Juli. Iwan bei Mobiliar angeboten', '2024-05-02 15:45:54.803324', '', '2024-05-02 15:45:54.803333', NULL, 5, 1140, 6, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1853, 'Keine Änderung zur Vorwoche (updates ab nächster Woche)', '2024-05-06 07:22:53.790333', '', '2024-05-06 07:22:53.79034', 92424, 3, 1191, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1854, 'Keine Änderung zur Vorwoche', '2024-05-06 07:23:07.659813', '', '2024-05-06 07:23:07.65982', NULL, 3, 1206, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1891, 'kleine Steigerung - leichter Rückgang (vorab 100 pro Woche)', '2024-05-13 07:08:49.934359', '', '2024-05-13 07:08:49.934367', 662, 3, 1212, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1892, '', '2024-05-13 07:09:31.404304', '', '2024-05-13 07:09:31.404311', 143, 3, 1214, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1893, 'keine Änderung', '2024-05-13 07:09:52.580267', '', '2024-05-13 07:09:52.580274', NULL, 3, 1218, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1911, '- das Experten-Interview mit Gernot Hugl findet am 6.6. statt.
- die Telefonkampagne mit Carlos wird durchgeführt und ist in Planung.', '2024-05-15 11:23:47.419965', '', '2024-05-15 11:23:47.419971', NULL, 16, 1178, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1919, 'Auf Puzzle Workshop geplant und mit Saraina vorbesprochen', '2024-05-16 08:33:34.602015', '', '2024-05-16 08:33:34.602022', 0, 5, 1137, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1929, '', '2024-05-21 11:32:55.676059', '', '2024-05-21 11:32:55.676062', NULL, 5, 1146, 10, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (1930, '', '2024-05-21 11:34:09.998452', '', '2024-05-21 11:34:09.998456', 0, 5, 1144, 1, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1944, 'Herausgefunden: P35 wurde zielgerichtet für Acrevis formuliert. /sys-Support shizzle nicht hier reinpappen, sonst gefahr zu gross, dass was nötiges rausfliegt. /Sys-Support Anliegen in gitlab-Tasklisten einpflegen.', '2024-05-22 06:43:00.232547', 'Im Gitlab starten', '2024-05-22 06:43:00.232551', NULL, 32, 1159, 2, 'ordinal', 'FAIL', 2);
INSERT INTO okr_pitc.check_in VALUES (1949, 'N/A', '2024-05-22 06:50:59.056498', 'Mit /ux, /mobility & /ruby abgleichen.', '2024-05-22 06:50:59.056503', NULL, 4, 1204, 10, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1951, 'N/A', '2024-05-22 06:52:59.55829', 'Gemäss KR', '2024-05-22 06:52:59.558294', NULL, 4, 1224, 10, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1952, 'April mit 74.8% knapp verfehlt', '2024-05-22 06:54:07.762879', '', '2024-05-22 06:54:07.762883', NULL, 4, 1225, 0, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1960, '', '2024-05-23 08:08:43.848296', '', '2024-05-23 08:09:11.395508', 3067433, 13, 1134, 3, 'metric', NULL, 8);
INSERT INTO okr_pitc.check_in VALUES (1964, 'Die Bestellung für Iwan sollte nächstens kommen laut PO', '2024-05-27 06:04:29.114008', '', '2024-05-27 06:04:29.114011', NULL, 5, 1140, 7, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1967, 'ERstes monatliches Strategie Controlling Meeting diese Woche', '2024-05-27 06:08:12.612474', '', '2024-05-27 06:08:12.612478', NULL, 5, 1146, 10, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (2031, 'Mai sieht mit 65% wieder schlechter aus', '2024-06-02 12:57:43.261686', 'Wert überdenken. Massnahmen disktuieren, z.B. begrenzte Aktivitäten zu Ausbildung und Sales und diese über die Monate verteilen (nicht wirklich ein griffiger Ansatz)', '2024-06-02 12:57:43.261694', NULL, 4, 1225, 0, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2039, '', '2024-06-03 07:48:32.083506', '', '2024-06-03 07:48:32.083513', 46, 34, 1157, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2047, 'im Westen nix Neues', '2024-06-03 16:11:59.782285', '', '2024-06-03 16:11:59.782291', NULL, 32, 1159, 2, 'ordinal', 'FAIL', 2);
INSERT INTO okr_pitc.check_in VALUES (2058, 'Sponsorensuche ist abgeschlossen.', '2024-06-04 07:19:50.617535', '', '2024-06-04 07:19:50.617542', NULL, 26, 1183, 7, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (2065, 'abgeschlossen, kein Release mehr ', '2024-06-07 11:37:35.726732', '', '2024-06-07 11:37:35.726737', 0.3, 41, 1151, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2311, 'Termin mit Tru: Erste Stelle Kafka. Wir machen uns an ein Storyboard.', '2024-07-23 06:58:37.050753', '', '2024-07-23 06:58:37.050762', NULL, 49, 1280, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2364, 'GitOps Blueprint in Arbeit, DevEx Kickoff geplant', '2024-08-05 08:11:35.536825', '', '2024-08-05 08:11:35.536829', NULL, 31, 1326, 7, 'ordinal', 'FAIL', 2);
INSERT INTO okr_pitc.check_in VALUES (2370, '', '2024-08-07 09:37:54.955702', '', '2024-08-07 09:37:54.955705', NULL, 22, 1348, 7, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2393, 'Status Quo', '2024-08-12 07:06:29.936366', '', '2024-08-12 07:06:29.936372', NULL, 4, 1293, 7, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2394, 'Start mit Analyse der Zahlen GJ 2023/24', '2024-08-12 07:06:58.486082', '', '2024-08-12 07:06:58.486087', NULL, 4, 1294, 10, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2419, 'Konzept in Auswertung', '2024-08-12 11:10:12.633972', '', '2024-08-12 11:10:12.633977', NULL, 3, 1316, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2457, 'Aktuell verwenden wir die Liste für zukünftige Events (Beispiel DIN Bier etc.)', '2024-08-19 12:34:01.137147', '', '2024-08-20 07:22:09.461815', NULL, 49, 1283, 10, 'ordinal', 'STRETCH', 1);
INSERT INTO okr_pitc.check_in VALUES (2462, '', '2024-08-21 05:57:58.074136', 'Follow up mit Raaflaub und Adi am 4-9.', '2024-08-21 05:57:58.074138', 0, 5, 1275, 1, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2463, 'ii ist voll dran. msc hat weniger Zeit. Zwei Blogs dürfte schwierig werden', '2024-08-21 05:59:27.96892', '', '2024-08-21 05:59:27.968922', NULL, 5, 1276, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1101, 'Status Quo', '2023-12-18 15:35:28.397896', 'Definieren wie weiter - Zweck der einzelnen Tech-Elemente definieren - Besprechung im nächsten Team-Meeting ', '2023-12-19 08:32:35.750605', 0.2, 4, 202, 1, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1450, '', '2024-02-26 09:54:50.102456', 'Weiterarbeit gemäss ordinalen Messgrössen', '2024-02-26 09:54:50.102459', NULL, 4, 1098, 5, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1592, 'Insgesamt waren wir an 9 Events dabei (haben 1 UX Meetup gehostet, waren mit der Fachgruppe Swiss ICT User Experience Mitorganisator bei einem VR EVent, waren Sponsor beim WIAD und haben mit Tobi Stern aktiv beim Prompt Battle mitgebattled! Leider hatten wir keinen Talk gehalten. Deal hat sich aus den Eventbesuchen auch ergeben, den wir auch gleich umgesetzt und bereits abgeschlossen haben', '2024-03-18 18:44:46.226227', '', '2024-03-18 18:44:46.226229', NULL, 36, 1058, 9, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (1593, 'Wir haben im Team ein Brainstorming gemacht, ein konkretes Konzept hat sich noch nicht ergeben. ', '2024-03-18 18:45:28.928954', '', '2024-03-18 18:45:28.928956', NULL, 36, 1061, 0, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1729, 'Aus Zeitgründen noch keine Arbeiten dazu erfolgt. ', '2024-04-22 13:28:20.866668', '', '2024-04-22 13:28:20.866671', NULL, 32, 1159, 2, 'ordinal', 'FAIL', 2);
INSERT INTO okr_pitc.check_in VALUES (1857, 'Etwas tieferer Anstieg (rund 100 pro Woche)', '2024-05-06 07:24:51.472847', '', '2024-05-06 07:24:51.472857', 603, 3, 1212, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1781, 'Von der SHB warten wir auf eine Antwort. Weiter haben wir der RHB einen 3rd Level Light offeriert.', '2024-04-29 06:49:47.60432', '', '2024-04-29 06:49:47.604327', NULL, 60, 1217, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1782, 'getting there...', '2024-04-29 06:51:54.776068', '', '2024-04-29 06:51:54.776074', 771973, 60, 1230, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1855, 'Keine Änderung seit letztem Checkin', '2024-05-06 07:23:24.108807', '', '2024-05-06 07:23:24.108814', NULL, 3, 1208, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1860, 'keine änderung', '2024-05-06 07:27:17.612715', '', '2024-05-06 07:27:17.612721', NULL, 3, 1218, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1862, 'Collections scheint sich als gut zu bestätigen und ist im Test', '2024-05-06 07:28:00.264273', '', '2024-05-06 07:28:00.264281', NULL, 3, 1223, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1894, 'Keine Änderung', '2024-05-13 07:10:05.534778', '', '2024-05-13 07:10:05.534785', NULL, 3, 1220, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1912, '- Puzzle Cloud Lunch prov. für am 6.6. geplant ("Dev Experience auf AWS")
- AWS wird prov. Partner am Puzzle up! 2024
Opportunitäten:
- BAFU: Daten und Digitalisierung (PDD) (Einladungsverfahren) (leider verloren)
- SBB: Nachfolge Phil Matti, Senior System Engineer mit Fokus AWS', '2024-05-15 11:29:15.6891', '', '2024-05-15 11:29:15.689106', NULL, 16, 1176, 4, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1931, 'keine Veränderung im UX Umsatz selber (BKD und SAC Projekte laufen im PTime Forecast nicht über uns). SwissLife Bestätigung noch ausstehend und evtl. Polypoint', '2024-05-21 11:42:52.040148', 'Polypoint Deal reinholen, WTO''s gewinnen', '2024-05-21 11:42:52.040153', 92424, 36, 1191, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1932, 'keine Veränderung', '2024-05-21 11:43:27.619602', '', '2024-05-21 11:43:27.619609', NULL, 36, 1206, 3, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1933, 'keine Veränderung seit letztem Check-in. Wir warten auf die finalisierung der Bereichsstrategie', '2024-05-21 11:44:05.133703', '', '2024-05-21 11:44:05.133708', NULL, 36, 1208, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1936, 'keine Veränderung seit letzten Check-in', '2024-05-21 11:46:14.397143', '', '2024-05-21 11:46:14.397147', NULL, 36, 1215, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1938, 'keine Veränderung seit letztem Checkin', '2024-05-21 11:48:05.899665', '', '2024-05-21 11:48:05.89967', NULL, 36, 1220, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1946, 'Präsi Erkenntnisse und Empfehlung in Teammeeting 14.5.2024 zwecks Kenntnisnahme und Entscheids zur Weiterwarbei', '2024-05-22 06:49:23.79964', 'Erarbeiten der Massnahmen. Weiterarbeit Hand in Hand mit KR ".... Arbeit im Team.... 3 Fokusthemen..."', '2024-05-22 06:49:23.799644', NULL, 4, 1196, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1950, 'Übersicht erstellt, in Teammeeting besprochen, in Weekly integriert', '2024-05-22 06:52:37.199279', 'Events: Anwenden als fixer Bestandteil im Weekly / Sponsoring: T.B.D', '2024-05-22 06:52:37.199283', NULL, 4, 1221, 10, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1966, '', '2024-05-27 06:06:32.924781', '', '2024-05-27 06:06:32.924785', NULL, 5, 1147, 10, 'ordinal', 'STRETCH', 0);
INSERT INTO okr_pitc.check_in VALUES (1965, 'Offerings für Post health care und Eportrisikoversicherung sowie Entscheid Studis Applikation von UniBe offen.', '2024-05-27 06:05:55.714304', '', '2024-05-27 07:20:40.43502', 0, 5, 1144, 1, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2032, 'Girod versuchen in Peerdom zu platzieren, Für Hupf sieht es nach Überbuchung bis Ende Jahr aus. ', '2024-06-02 12:59:27.99809', 'Peerdom fixieren, BKD-Arbeiten für zweite Jahreshälfte andiskutieren, Zusatzbudget für Bundesarchiv auftreiben (-> Meeting Ende Juli vorgesehen)', '2024-06-02 12:59:27.998097', NULL, 4, 1227, 0, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (2040, 'Buchzentrum und SmartIT in the making', '2024-06-03 07:49:23.673761', '', '2024-06-03 07:49:23.673767', 4, 34, 1158, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2048, 'SWEB Migration durchgeführt', '2024-06-03 18:11:43.181442', '', '2024-06-03 18:11:43.181449', NULL, 28, 1164, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2049, '', '2024-06-03 18:11:59.672877', '', '2024-06-03 18:11:59.672884', NULL, 28, 1231, 2, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2059, 'Nächste Woche Sponsoringauftritt KCD', '2024-06-04 07:24:25.957445', '', '2024-06-04 07:24:25.957451', NULL, 57, 1193, 8, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2073, 'Actionplan erarbeitet und wird am Dienstag mit SUM Buddy besprochen', '2024-06-10 08:10:13.816945', '', '2024-06-10 08:10:13.81695', NULL, 36, 1208, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2074, 'Linkedin Post zum Visualisierungworkshop', '2024-06-10 08:10:54.448674', '', '2024-06-10 08:10:54.448679', 10, 36, 1210, 9, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2095, 'Deal bei Mobi wird wahrscheinlich nicht zu Stande kommen, keine Einflussmöglichkeiten.', '2024-06-11 13:06:02.651592', '', '2024-06-11 13:06:02.651597', NULL, 40, 1229, 2, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2097, 'Lukas Koller hat quasi ein neues Company OKR definiert.
Werden wir weiterverfolgen und als Vorschlag eingeben.', '2024-06-11 13:08:08.548484', '', '2024-06-11 13:08:30.608544', NULL, 40, 1222, 10, 'ordinal', 'STRETCH', 1);
INSERT INTO okr_pitc.check_in VALUES (2093, 'Vale dran, aber unsicher ob bis Ende Juni Prüfung abgelgt.', '2024-06-11 13:03:55.937198', '', '2024-06-11 13:09:02.146284', NULL, 40, 1207, 5, 'ordinal', 'COMMIT', 3);
INSERT INTO okr_pitc.check_in VALUES (2105, '', '2024-06-12 09:07:48.317861', '', '2024-06-12 09:07:48.317865', NULL, 28, 1167, 2, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2107, '', '2024-06-13 06:07:40.903698', '', '2024-06-13 06:07:40.903701', 3440219, 22, 1134, 3, 'metric', NULL, 8);
INSERT INTO okr_pitc.check_in VALUES (2115, '- diverse Leads aus Account Alignment mit GCP vom 13.06.24: Post am konkretesten', '2024-06-14 05:13:49.833461', '', '2024-06-14 05:13:49.833463', NULL, 16, 1176, 0, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2128, 'Team informiert, Feedback eingeholt und Massnahme Plan (siehe O1:KR3) erstellt', '2024-06-17 11:08:41.695573', '', '2024-06-17 15:32:33.149232', NULL, 36, 1218, 10, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (2152, 'April: 14.5% +++ Kumuliert: 12.1+14.5=26.6%', '2024-06-17 20:01:59.975727', '', '2024-06-17 20:01:59.97573', 26.6, 33, 1161, 8, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2154, 'Teamwanderung Aargau: 6. Kumuliert: 14+6 = 20', '2024-06-17 20:07:43.704734', '', '2024-06-17 20:07:43.704737', 20, 33, 1173, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2161, '', '2024-06-18 06:56:48.279135', '', '2024-06-18 06:56:48.279137', NULL, 27, 1198, 0, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2179, '', '2024-06-21 13:50:16.841118', '', '2024-06-21 13:51:11.32217', 614936, 28, 1301, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2181, '- Vorbereitungsarbeiten KR Q1 im Gange
- Treffen mit TD Synnex
- Abklärungen Sponsoring und/oder Teilnahme AWS Summit
- Arbeiten Abschluss AWS Partnerschaft', '2024-06-26 08:16:24.919815', '', '2024-06-26 08:16:24.919835', NULL, 16, 1250, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2184, 'SHKB hat endlich unterschrieben! ', '2024-06-26 19:31:41.426539', '', '2024-06-26 19:32:41.358688', NULL, 40, 1217, 10, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (2185, 'Noch nichts passiert', '2024-06-27 07:06:40.989977', '', '2024-06-27 07:06:40.989984', NULL, 24, 1243, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1102, 'Sales-Aktivitäten intensiviert. Manifest & Motto /dev/tre mit erstmals mit Markom besprochen', '2023-12-18 15:37:38.990409', 'Sales-Aktivitäten hoch behalten - Update Unser Motto erstellen - Manifest und  Motto /dev/tre  im nä. Teammeeting besprechen & verabschieden', '2023-12-19 08:33:00.357472', 0.6, 4, 204, 1, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (2395, 'Status Quo', '2024-08-12 07:07:13.604223', '', '2024-08-12 07:07:13.604226', NULL, 4, 1295, 10, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1103, 'Teammeeting 12.12 stattgefunden, OKR-Draft mit Team besprochen. Wünsche (verbesserungspotential) 2024 abgeholt', '2023-12-18 15:40:04.20456', 'OK Q3 GJ23/24 erstellen, Arbeit mit OKR intensivieren', '2023-12-19 08:33:38.08014', 0.8, 4, 205, 10, 'metric', NULL, 3);
INSERT INTO okr_pitc.check_in VALUES (1104, 'Thema erneut im Teammeeting 12.12. besprochen (High-/ Low-Lights/ Wishes)', '2023-12-18 15:41:35.798444', 'Themen konsolidieren, Weiteres Vorgehen definieren - Ansonsten Aktivitäten gemäss Beschreibung', '2023-12-19 08:34:04.291605', 0.2, 4, 206, 1, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1451, 'Im Kernteam (Claudia, Hupf, Mäge) in 2 Workshops ein finales Manifest erstellt und das Motto fallengelassen.', '2024-02-26 09:55:56.036176', 'Komunikation dem Team und nach aussen', '2024-02-26 09:55:56.036178', NULL, 4, 1100, 7, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1896, 'Fehlt noch Bestellung von Iwan', '2024-05-13 07:44:20.145975', '', '2024-05-13 07:44:20.145982', NULL, 5, 1140, 6, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1594, 'keine Veränderung', '2024-03-18 18:46:13.709277', 'wird im nächsten Cicle nochmals aufgenommen und das Angebot wird verbessert', '2024-03-18 18:46:13.70928', NULL, 36, 1076, 2, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (1783, 'Agenda ist versendet ', '2024-04-29 07:16:28.013982', '', '2024-04-29 07:16:28.013989', NULL, 41, 1152, 7, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1856, 'Boom-Summit checking', '2024-05-06 07:23:40.130865', '', '2024-05-06 07:23:40.130878', 5, 3, 1210, 9, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1861, 'keine änderung', '2024-05-06 07:27:27.499566', '', '2024-05-06 07:27:27.499575', NULL, 3, 1220, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1895, 'Nextcloud Collections eignet sich gut als Tool (noch auf der Test-Umgebung)', '2024-05-13 07:10:40.114785', '', '2024-05-13 07:10:40.114792', NULL, 3, 1223, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1913, 'Keine Veränderung gegenüber letztem Check-in.', '2024-05-15 11:30:53.621996', '', '2024-05-15 11:30:53.622003', 56980, 16, 1177, 4, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1934, 'Uphill Conf Post', '2024-05-21 11:44:30.235227', '', '2024-05-21 11:44:30.235231', 7, 36, 1210, 9, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1935, '', '2024-05-21 11:45:43.755282', '', '2024-05-21 11:45:43.755286', 151, 36, 1214, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1947, 'N/A', '2024-05-22 06:49:42.233736', 'siehe Action Plan', '2024-05-22 06:49:42.23374', 0, 4, 1197, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1968, 'Weggang Nadia. Verkleinerung Kernteam', '2024-05-27 06:09:24.010891', '', '2024-05-27 06:09:24.010895', 16000, 5, 1141, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2041, 'Erstes monatliche Strategie-Controlling durchgeführt. Jede Massnahme aktuaisiert', '2024-06-03 11:08:22.612112', '', '2024-06-03 11:08:22.612118', NULL, 5, 1146, 10, 'ordinal', 'STRETCH', 1);
INSERT INTO okr_pitc.check_in VALUES (2050, 'CTO Abgeholt. Gespräche geplant', '2024-06-03 18:12:19.85653', '', '2024-06-03 18:12:19.856536', NULL, 28, 1165, 3, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2051, 'Bis Sommer geplant', '2024-06-03 18:12:48.055284', '', '2024-06-03 18:12:48.055291', NULL, 28, 1232, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2053, '', '2024-06-03 18:14:56.381787', '', '2024-06-03 18:14:56.381793', 1101566, 28, 1168, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2396, 'Genügend Arbeit für alle bis Ende Oktober. Bei Clara (Junior) ist nicht alles verrechenbar.', '2024-08-12 07:08:54.225779', '', '2024-08-12 07:08:54.225783', NULL, 4, 1296, 10, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2182, '- wir haben am Puzzle Workshop 2024 eine Working Session angeboten und durchgeführt. Daraus entstand eine erste Auslegeordnung. An dieser wird nun weitergearbeitet.
- als nächsten Schritt organisieren wir uns als Team.', '2024-06-26 08:21:16.947025', '', '2024-06-26 08:21:16.947035', NULL, 16, 1251, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2186, 'Noch keine Veränderung', '2024-06-27 07:07:13.792764', '', '2024-06-27 07:07:13.792771', 0, 24, 1311, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2421, '', '2024-08-12 11:11:04.601712', 'Keine änderungen', '2024-08-12 11:11:04.601716', NULL, 3, 1319, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2193, 'Der Prompt wird im Juli kreiert (nach Go-Live der neuen Website).', '2024-07-01 04:24:51.954512', '', '2024-07-01 04:24:51.954516', NULL, 49, 1279, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2308, 'msc und ii sind dran', '2024-07-22 15:31:09.468235', '', '2024-07-22 15:31:09.468237', NULL, 5, 1276, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2312, 'Weiter Status Quo, beinahe alle Subsiten sind nun erfasst und live.', '2024-07-23 07:00:17.603397', '', '2024-07-23 07:00:17.603403', NULL, 49, 1281, 9, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2315, 'Aktuell haben wir 14 Members auf der Liste und fragen weiterhin.', '2024-07-24 04:39:51.042121', '', '2024-07-24 04:39:51.042129', 14, 49, 1318, 8, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2075, 'keine Veränderung', '2024-06-10 08:11:56.873024', '', '2024-06-10 08:11:56.873029', NULL, 36, 1215, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2096, 'Erste Resultate in sig-gitops erreicht, braucht noch mehr Zeit, kann weiter genommen werden.', '2024-06-11 13:07:13.773361', '', '2024-06-11 13:07:13.773365', NULL, 40, 1213, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2108, '- die Marktopportunitäten für das GJ 24/25 sind erarbeitet und werden ab 1.7.24 parat sein.
- der definitive Entscheid, welche verfolgt werden, wird am 24.6. am nächsten Monthly gefällt.
- die Moals (Mittelfrist-Strategie-Sicht) sind erarbeitet und werden ab 1.7.24 umgesetzt.
- der Prozess P11 wird mit den Moals ergänzt.', '2024-06-13 06:22:15.369725', '', '2024-06-13 06:22:15.369729', NULL, 16, 1132, 10, 'ordinal', 'STRETCH', 3);
INSERT INTO okr_pitc.check_in VALUES (2116, 'Planung wird im Leadership-Monthly End Juni verabschiedet.', '2024-06-14 07:47:09.209577', '', '2024-06-14 07:47:09.209579', NULL, 26, 1182, 8, 'ordinal', 'TARGET', 2);
INSERT INTO okr_pitc.check_in VALUES (2117, 'Sponsorensuche ist abgeschlossen.', '2024-06-14 07:47:24.629563', '', '2024-06-14 07:47:24.629566', NULL, 26, 1183, 7, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (2118, 'Give Away Konzept wird am Puzzle Inside Ende Juni vorgestellt
Nachhaltiges Give Away Puzzle up (Druckvelo) in Produktion
Interaktive Inszenierung am Puzzle up! sichergestellt (Rollup, Druckvelo)', '2024-06-14 07:47:41.767259', '', '2024-06-14 07:47:41.767261', NULL, 26, 1184, 7, 'ordinal', 'STRETCH', 1);
INSERT INTO okr_pitc.check_in VALUES (2129, '', '2024-06-17 11:09:07.462709', '', '2024-06-17 11:09:07.462711', 157, 36, 1214, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2153, 'März: CHF 264''099.- +++ Durchschnitt: (289''513+264''099)/2 = CHF 276''806.-', '2024-06-17 20:04:05.21133', '', '2024-06-17 20:04:05.211332', 276806, 33, 1163, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2162, 'Threatmodel ist nach STRIDE definiert, wir haben da ein paar Sachen zusammengetragen.', '2024-06-18 06:57:33.766093', '', '2024-06-18 06:57:33.766096', NULL, 27, 1186, 7, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2163, '', '2024-06-18 06:58:19.729863', '', '2024-06-18 06:58:19.729866', NULL, 27, 1205, 0, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2316, '', '2024-07-24 10:58:36.733766', '', '2024-07-24 10:58:36.733777', 738816, 60, 1328, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2318, 'Juni Teammeeting: 7.9', '2024-07-24 11:01:35.758533', '', '2024-07-24 11:01:35.75854', 7.9, 60, 1331, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2320, 'Frequentis hat zugesagt / neuer Lead Inventx', '2024-07-24 11:04:19.394239', '', '2024-07-24 11:04:19.394248', 1, 60, 1343, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2323, '10.9. DevOps Bier @Mobi (genug für zwei Talks), Ben & Seba
8.10. ROSA Techkaffi@Helvetia, Felix
22.8. Dagger 101 Cloud native MeetUp, Stöf
ACS, Felix (Termin noch offen)', '2024-07-24 14:41:20.713696', '', '2024-07-24 14:41:20.713701', NULL, 60, 1332, 9, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2321, 'Start im Juli/August. Puzzle-internen MVP soll bis Ende August stehen.
Als Grundlage soll das Generic Helm-Chart Deployment genutzt werden und als Frontend dafür Backstage eingesetzt werden.', '2024-07-24 11:13:26.978495', '', '2024-07-24 11:13:26.978502', 0, 40, 1344, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2319, 'Kick Off hat statt gefunden, Blue Prints sollten bis Ende August stehen.', '2024-07-24 11:02:31.11095', '', '2024-07-24 11:13:42.689014', NULL, 60, 1326, 7, 'ordinal', 'FAIL', 2);
INSERT INTO okr_pitc.check_in VALUES (2365, '', '2024-08-05 08:24:26.644791', 'In Arbeit', '2024-08-05 08:24:26.644802', 1, 31, 1343, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1105, 'Status Quo', '2023-12-18 15:49:01.969811', 'Möglichst die Leute auslasten, wo wir können. Aktuelle Absolute Verrechenbarkeit (Nov23) sieht besser aus. Aktueller Stand PTime 79%', '2023-12-19 08:34:43.310106', 0.3, 4, 207, 1, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1452, 'Weitere Skills-Listen erstellt und in MAGs geprüft', '2024-02-26 10:08:48.544283', '', '2024-02-26 10:08:48.544285', 10, 4, 1102, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1453, 'Bräteln/ Bier im März vorgesehen', '2024-02-26 10:09:27.761043', 'Einen Plan konkretisieren!', '2024-02-26 10:09:27.761045', 1, 4, 1103, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1595, 'keine Veränderung seit letztem Checkin. Prozess ist definiert und wir werden im nächsten Cycle neue Referenzen veröffentlichen', '2024-03-18 18:47:18.638678', '', '2024-03-18 18:47:18.638681', NULL, 36, 1105, 8, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1784, 'Es gibt nachwievor offenen Themen
- Prozess Kommunikation wurde nun auch gebrieft und sie werden Anpassungen vornehmen, inkl. Leitbild ', '2024-04-29 07:17:43.520013', 'Ziele in den Przessen werden schwierig sein zu definieren, bis jetzt haben wir noch keine ', '2024-04-29 07:18:31.991626', NULL, 41, 1154, 4, 'ordinal', 'FAIL', 2);
INSERT INTO okr_pitc.check_in VALUES (1914, 'Auslegeordnung abgeschlossen', '2024-05-15 11:44:25.327669', 'Auslegeordnung bzw. Bedürfnissklärung mit LST ist geplant am Mai Monthly', '2024-05-15 11:44:25.327675', NULL, 24, 1131, 3, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1937, 'Bereichsstrategie finalisiert und mit SUM Buddy besprochen. Warten auf Feedback von GL und dann besprechen wir es beim nächsten WAC Morning mit dem Team', '2024-05-21 11:47:17.890421', '', '2024-05-21 11:47:17.890426', NULL, 36, 1218, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1953, 'N/A', '2024-05-22 06:59:16.359499', 'Dran bleiben', '2024-05-22 06:59:16.359508', NULL, 4, 1227, 10, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (1954, 'N/A', '2024-05-22 06:59:48.621792', 'Ausschreiben', '2024-05-22 06:59:48.621796', NULL, 4, 1228, 0, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1969, '', '2024-05-27 06:34:47.77326', '', '2024-05-27 06:34:47.773263', NULL, 27, 1226, 4, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2042, 'Weiterhin von Mobiliar mündliche Zusage, Aber wir werden hingehalten mit unterschiedlichen Signalen...', '2024-06-03 11:26:00.158599', '', '2024-06-03 14:33:28.725133', NULL, 5, 1140, 4, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (2052, 'Rückmeldung aufgrund Ticket LST Meeting', '2024-06-03 18:13:18.9316', '', '2024-06-03 18:13:18.93161', NULL, 28, 1167, 2, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2076, 'Diese Woche erhalten wir Feedback vom Team', '2024-06-10 08:12:48.379869', '', '2024-06-10 08:12:48.379874', NULL, 36, 1218, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2077, 'Verständnis Division UX geschaffen und an Abstimmungsmeeting mit dev bereichen diskutiert. Werden aber erst im nächsten Cycle ein gemeinsames Statement erarbeiten', '2024-06-10 08:14:09.813583', '', '2024-06-10 08:14:09.813589', NULL, 36, 1220, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2098, 'Unverändert.', '2024-06-11 13:09:59.995098', '', '2024-06-11 13:09:59.995102', NULL, 40, 1216, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2109, '- die erste Marktanalyse ist erstellt und wurde der Kerngruppe Digitale Lösungen am 6. Juni vorgestellt.
- die Telefonkampagne mit Carlos ist gestartet und die Resultate werden in die Analyse eingearbeitet.', '2024-06-13 06:24:15.173703', '', '2024-06-13 06:24:15.173707', NULL, 16, 1178, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2119, '', '2024-06-14 07:50:12.19861', '', '2024-06-14 07:50:12.198613', NULL, 26, 1193, 8, 'ordinal', 'STRETCH', 0);
INSERT INTO okr_pitc.check_in VALUES (2130, 'Hybrider Usability Test und Referenz Landmatrix', '2024-06-17 11:11:09.537321', '', '2024-06-17 11:11:09.537324', 12, 36, 1210, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2155, '100. Lunch Explorer Jubi: 11 Teilnehmende. Kumuliert: 20+11 = 31

Die regulären Austragungen von LXZH und Chez Leu noch gar nicht eingerechnet...', '2024-06-17 20:14:00.221531', '', '2024-06-17 20:14:00.221534', 31, 33, 1173, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2164, 'Dokument mit Vorschlag wird heute der GL übergeben.', '2024-06-18 08:27:29.532426', '', '2024-06-18 08:27:46.116351', NULL, 27, 1185, 10, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (2165, 'Wir werden den Prozess nicht aufbauen, da die Resultate von Suricata nicht ausreichend verwaltbar sind.', '2024-06-18 08:28:39.534999', '', '2024-06-18 08:28:39.535002', NULL, 27, 1187, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2183, '- die Marktanalyse ist noch in Arbeit.
- am Puzzle Workshop wurde eine Working Session durchgeführt und mit interessierten Members an den Hypothesen gearbeitet.
- die Telefonkampagne von Carlos ist aktuell am Laufen. Bereits resultierten daraus Leads für drei Meetings.', '2024-06-26 08:30:39.47362', '', '2024-06-26 08:30:39.473632', NULL, 16, 1254, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2188, '', '2024-06-27 07:24:02.000523', '', '2024-06-27 07:24:02.000529', 2295853, 13, 1241, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2194, 'Diese Woche werden alle Meta-Tags für das Go-Live der Website überprüft.', '2024-07-01 04:26:25.028758', '', '2024-07-01 04:26:25.028762', NULL, 49, 1281, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2202, '', '2024-07-02 07:55:32.302629', '', '2024-07-02 07:55:32.302633', NULL, 26, 1280, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2226, '', '2024-07-08 15:34:18.770505', '', '2024-07-08 15:34:18.770509', NULL, 5, 1276, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2219, '', '2024-07-04 09:56:46.020096', '', '2024-07-04 09:56:46.020099', 2350345, 13, 1241, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2220, '', '2024-07-04 14:52:43.325568', '', '2024-07-04 14:52:43.325571', NULL, 5, 1261, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2222, 'Erste interessierte Members gefunden', '2024-07-05 06:43:07.61485', '', '2024-07-05 06:43:26.401117', NULL, 13, 1245, 10, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2223, 'Nicht Deep Dive sondern Kickoff für Erwartungsmanagement', '2024-07-08 06:24:59.104764', '', '2024-07-08 06:24:59.104768', 0, 5, 1275, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2229, 'Status Quo und Problematik mit ChatGPT Firmenaccount.', '2024-07-09 03:58:37.116109', '', '2024-07-09 03:58:37.116121', NULL, 49, 1279, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2233, 'KicKoff im Juli mit HR.', '2024-07-09 07:05:57.484909', '', '2024-07-09 07:05:57.484911', NULL, 49, 1280, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (436, '0.1', '2023-10-09 14:24:49.658437', 'Blogpost in arbeit, Einführung bei /sys noch ausstehend', '2023-10-08 22:00:00', 0.4, 34, 177, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2243, '100 h ', '2024-07-10 08:21:14.560577', '', '2024-07-10 08:21:14.560608', NULL, 41, 1268, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2244, 'noch nicht gestartet', '2024-07-10 08:21:46.413395', '', '2024-07-10 08:21:46.413421', NULL, 41, 1270, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2246, 'Ende August ist Stichtag für Inkasso, sidn aber nun auf einem guten Weg ', '2024-07-10 08:23:01.940198', '', '2024-07-10 08:23:01.940201', NULL, 41, 1273, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2248, 'Diesmal hat es super funktioniert und zum ersten mal ohne Bug seit 2 Jarhren, trotz den vielen Anpassungen durch den SAC,', '2024-07-10 10:05:50.352918', 'Team loben :-) ', '2024-07-10 10:05:50.352922', 1, 41, 1271, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2249, '- AWS Opportunities, welche wir online einreichen werden oder bereits gemacht haben: Brixel, Helvetia. Für den Partner-Level fehlt uns noch eine. Wir prüfen eine interne Referenz.
- Wir haben AWS Summit Zürich Sponsoring geprüft. Es ist mit TCHF 14 zu teuer. Wir werden als Teilnehmer dabei sein.
- Für den Vermarktungsplan starten wir mit einem Codi werden auf Basis von weiter daran arbeiten.
- die Mate-Video Planung wird in Angriff genommen.', '2024-07-10 19:41:06.33401', '', '2024-07-10 19:41:06.334012', NULL, 16, 1250, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2252, '', '2024-07-11 06:30:16.003315', '', '2024-07-11 06:30:16.003318', 74.83, 22, 1349, 1, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2256, 'Erfolgreiches Meeting mit /mid, gemeinsames Verständnis erreicht.', '2024-07-15 06:44:12.603848', 'Mark trägt mal Fragen für eine Auslegeordnung bei den Kunden zusammen', '2024-07-15 06:44:12.60385', NULL, 27, 1340, 9, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1106, 'Mobi-Gaia findet statt. Planungsmeeting 19.12. Bestellung muss noch kommen. Weiterentwicklungen Siard gibt es bis mindestens Mitte 2024 nicht (Ausgabestopp), Qaurkus-Schulung für Mobi in Nyon im Februar vorgesehen', '2023-12-18 15:52:23.21165', 'Mobi-Gaia Bestellung fixieren. Planung für mindestens ein Quartal vornehmen', '2023-12-19 08:35:12.683714', 0.3, 4, 208, 1, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (2221, '', '2024-07-04 14:59:07.355657', '', '2024-07-04 14:59:07.355665', NULL, 5, 1263, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1454, 'Lücke im Februar: 0.5 FTE von Andres + 0.26 FTE von Dani + 0.05 FTE von Nils + 0.1 FTE von Christian. Total: 0.91 FTE -> 8.9% ///
Kumuliert: -1.6 + 6.1 = 4.4', '2024-02-26 11:06:25.66204', 'Ausblick für März ist positiv.', '2024-02-26 11:19:18.339493', 4.4, 33, 1068, 6, 'metric', NULL, 5);
INSERT INTO okr_pitc.check_in VALUES (1785, 'bereits 100h', '2024-04-29 07:22:56.090236', '', '2024-04-29 07:22:56.09026', NULL, 41, 1149, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1788, 'Es wurden diverse Massnahmen getroffen inklusive Kostendacherhöhung sowie ext. Unterstützung aufs Projekt nehmen', '2024-04-29 07:24:58.282202', '', '2024-04-29 07:24:58.282208', NULL, 41, 1156, 4, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1858, '', '2024-05-06 07:26:50.632652', '', '2024-05-06 07:26:50.632659', 136, 3, 1214, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1859, 'Aktuell noch keine Durchführung', '2024-05-06 07:27:07.922212', '', '2024-05-06 07:27:07.922218', NULL, 3, 1215, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1897, 'Validierung mit Members beauftragt', '2024-05-13 07:50:29.291311', 'Sales Termine erstellen', '2024-05-13 07:50:29.291319', NULL, 5, 1138, 7, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1955, 'Gleicher Stand wie letztes Mal. Aktuell in Abklärungen mit Pippo, was es noch braucht für den Target-Teil. ', '2024-05-22 10:16:53.365567', '', '2024-05-22 10:16:53.365571', NULL, 29, 1142, 4, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1971, '', '2024-05-27 06:37:07.339592', '', '2024-05-27 06:37:07.339598', NULL, 27, 1226, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1970, 'Thema konnte gefunden werden, Blogpost wird ausgearbeitet.', '2024-05-27 06:36:49.169421', '', '2024-05-27 06:37:35.69721', NULL, 27, 1192, 8, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2043, 'Validierung mit Sales intern aufgesetzt. Sales extern offen bzw. mit wem?', '2024-06-03 11:30:51.988085', 'Wir haben 18 Validierungen mit Members. WEr fehlt noch? Stefan, Mättu Begert?', '2024-06-03 11:30:51.988092', NULL, 5, 1138, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2078, '', '2024-06-10 08:48:11.271499', '', '2024-06-10 08:48:11.271504', 1016, 36, 1212, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2099, 'OV Ok
Rohrmax hat die Hälfte aller Tickets bearbeitet - Genügt zur Erreichung vom Target, da eine 100%ige Bearbeitung nicht Zielführend wäre.', '2024-06-11 13:46:23.592146', '', '2024-06-11 13:46:23.592149', NULL, 31, 1209, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2110, 'Ziel hat sich geändert, aber würde dsagen wir haben mindestens Commit erreicht ', '2024-06-13 06:24:33.106254', '', '2024-06-13 06:24:33.106258', NULL, 41, 1156, 4, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2111, '- am 6.6. wurde der Puzzle Lunch mit AWS durchgeführt.
- das Account Alignment mit GCP findet am 13.6. statt.
- weitere Leads sind in der Zwischenzeit nicht dazu gekommen. Wir verbleiben bei zwei Opportunities.', '2024-06-13 06:26:33.725664', '', '2024-06-13 06:26:33.725667', NULL, 16, 1176, 0, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2120, 'Alle Bilder der bestehenden Seiten sind importiert und zugewiesen, die Visuals sind entsprechend den Seiten auch platziert. Bei einigen Lottie Animationen haben wir noch Darstellungsprobleme, diese werden asap behoben.', '2024-06-14 08:01:25.765682', 'Nach Start Jup und Erhalt Föteli Puzzle Bern können die restlichen Fotos eingefügt werden. ', '2024-06-14 08:01:25.765684', 80, 49, 1200, 9, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2121, 'Im Services&Solutions Part sind drei von fünf Themen abgebildet (und in Vernehmlassung). Der Rest wird momentan er-/überarbeitet. Ausser der Jobs-Seite sind die Texte überall enthalten und warten auf den Feinschliff.', '2024-06-14 08:06:52.16385', 'Die Texte sind soweit möglich eingepflegt. Es gibt noch div. technische und strukturelle Tasks und ein Brocken Schreibarbeit - aber wir haben ja noch Zeit. ', '2024-06-14 08:06:52.163852', 70, 49, 1180, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2131, 'keine Veränderung (Warten immer noch auf Bestellung Swiss Life)', '2024-06-17 11:13:06.399309', '', '2024-06-17 11:13:06.399311', 96096, 36, 1191, 0, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2132, 'keine Veränderung', '2024-06-17 11:15:22.353501', '', '2024-06-17 15:28:06.880661', NULL, 36, 1206, 0, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2156, 'Alder Daniel	394	526
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

0.911622453466217%', '2024-06-17 22:20:34.224095', '', '2024-06-17 22:20:34.224097', 91.16, 33, 1174, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2166, 'Phil war krank, eventuell reicht es noch diesen Monat, aber das hat verzögert.', '2024-06-18 08:29:10.765583', '', '2024-06-18 08:29:10.765585', NULL, 27, 1189, 0, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2167, 'Leider keine Referenz erhalten.', '2024-06-18 08:29:53.205623', '', '2024-06-18 08:29:53.205625', NULL, 27, 1205, 0, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2168, 'Blogpost auf nächstes Quartal verschoben.', '2024-06-18 08:30:28.636099', '', '2024-06-18 08:30:28.636101', NULL, 27, 1192, 0, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2189, 'Die erste Messung erfolgt mit dem Juli-Monatsabschluss im August.', '2024-06-27 07:41:39.682035', '', '2024-06-27 07:41:39.682042', 0, 22, 1253, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2195, 'Grundgerüst erstellt, erste Namen vermerkt. Vorgehen Dokumentation wird noch entschieden. ', '2024-07-01 04:35:49.578069', '', '2024-07-01 04:35:49.578073', NULL, 49, 1283, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2203, '', '2024-07-02 07:55:55.180854', '', '2024-07-02 07:55:55.18086', 0, 26, 1318, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2397, 'In Arbeit', '2024-08-12 08:26:38.678067', '', '2024-08-12 08:26:38.678071', NULL, 31, 1326, 7, 'ordinal', 'FAIL', 2);
INSERT INTO okr_pitc.check_in VALUES (2224, 'ar nochmals darauf aufmerksam gemacht, ein Status Update Meeting einzuführen mit dbi und mir. Und allenfalls auch Yup und jemand von /mid', '2024-07-08 06:38:38.660902', '', '2024-07-08 06:38:38.660912', 0, 5, 1277, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2254, '', '2024-07-11 07:17:15.463142', '', '2024-07-11 07:17:15.463144', NULL, 24, 1243, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2227, 'Inserat angepasst und neu auf jobs.ch aufgeschaltet.', '2024-07-08 16:49:54.497816', '', '2024-07-08 16:49:54.49782', -0.9, 20, 1259, 3, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2230, 'Go-Live am 4. Juli. SEO-Score liegt bei 83. Beim nächsten Check-in ziehen wir die ersten Vergleichszahlen (mind. 1 Woche live).', '2024-07-09 04:04:20.292339', '', '2024-07-09 04:04:20.292352', NULL, 49, 1281, 9, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2234, 'Info hat stattgefunden', '2024-07-09 07:57:42.474927', '', '2024-07-09 07:57:42.474931', NULL, 31, 1326, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2245, 'Nein ', '2024-07-10 08:22:09.133271', '', '2024-07-10 08:22:09.133273', NULL, 41, 1285, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2250, '- Absprache zwischen ek und mw hat stattgefunden.
- Kick-Off für MO Beratung mit passendem Teilnehmerkreis für nach den Sommerferien organisiert (7.8.24).
- ek hat erste Ideen für marktorientieres Angebot zusammengetragen (welches auch Themen der Beratung enthält).', '2024-07-10 19:47:07.360518', '', '2024-07-10 19:47:07.36052', NULL, 16, 1251, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2253, 'Projektteam definiert. Kickoff heute', '2024-07-11 07:16:48.765722', '', '2024-07-11 07:16:48.765725', NULL, 24, 1350, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2257, 'Phil hat bei sich einen PoC aufgebaut und peilt nun an, das im Staging aufzubauen. Damit können wir dann effizient planen.', '2024-07-15 06:51:45.153025', '', '2024-07-15 06:51:45.153028', NULL, 27, 1341, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2313, 'Commit erreicht und im Redaktonsplan detailliert vermerkt.', '2024-07-23 09:40:15.872963', '', '2024-07-23 09:40:15.87297', NULL, 49, 1283, 8, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2317, 'Keine Änderungen', '2024-07-24 10:59:03.949556', '', '2024-07-24 10:59:03.949565', NULL, 60, 1329, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1107, 'Status Quo', '2023-12-18 15:53:15.356462', 'Dran bleiben. Massnahmen wie gehabt', '2023-12-19 08:35:34.436966', 0.2, 4, 209, 1, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1455, 'Im Januar 10.1%. Kumuliert: 15.0+10.1=25.1%', '2024-02-26 11:22:42.209772', 'Target Zone wird voraussichtlich erreicht (Ausblick März ist relativ positiv).', '2024-02-26 11:22:42.209774', 25.1, 33, 1069, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1456, 'Im Januar: CHF 269''260.-', '2024-02-26 11:25:15.725287', '', '2024-02-26 11:25:15.725289', 269260, 33, 1071, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1786, 'kein Fortschritt', '2024-04-29 07:23:29.140324', '', '2024-04-29 07:23:42.423421', NULL, 41, 1150, 1, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1787, 'kein Fortschritt ', '2024-04-29 07:24:03.732261', '', '2024-04-29 07:24:03.732268', NULL, 41, 1153, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1863, 'Das schwank, ist aber in gutem Bereich', '2024-05-06 07:59:40.360346', '', '2024-05-06 07:59:40.360353', 47, 32, 1157, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1898, 'Termin bei Sales Puzzle angefragt, Termin bei Dave Kilchenmann angefragt', '2024-05-13 08:26:46.93511', '', '2024-05-13 08:26:46.935122', NULL, 5, 1138, 7, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1956, 'Gespräche für internen Teampool geführt. Abstimmung mit CTO geplant.', '2024-05-22 12:17:03.057707', '', '2024-05-22 12:17:03.057711', NULL, 20, 1135, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1972, 'Wir sind ready für Donnerstag :-) ', '2024-05-27 06:41:43.588057', '', '2024-05-27 06:41:43.588061', NULL, 41, 1154, 8, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (1899, 'Wir haben wieder über 123 Stunden ', '2024-05-13 09:27:47.180548', '', '2024-05-13 09:27:47.180554', NULL, 41, 1149, 2, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1973, 'Das Ziel hat sich nun etwas geändert und haben ein Kostendacherhöhung erhalten. Diverse Massnahmen sind eingeleitet', '2024-05-27 06:42:46.828046', '', '2024-05-27 06:42:46.82805', NULL, 41, 1156, 4, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2044, '03.06.2024: Die Aufteilung ist soweit geklärt. Nathalie 20% Members Coaching und LST, 10% Innoprozess, 40% verrechenbar PL / SM', '2024-06-03 12:46:13.276304', '', '2024-06-03 12:46:13.276312', NULL, 29, 1142, 4, 'ordinal', 'STRETCH', 0);
INSERT INTO okr_pitc.check_in VALUES (2079, '', '2024-06-10 13:36:18.418885', '', '2024-06-10 13:36:18.41889', NULL, 5, 1138, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2100, '', '2024-06-11 14:16:55.252106', '', '2024-06-11 14:16:55.252109', NULL, 27, 1186, 7, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2112, 'Mobiliar dazu gekommen.', '2024-06-13 06:32:10.855283', '', '2024-06-13 06:32:10.855286', 72959, 16, 1177, 3, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2122, 'Im Services&Solutions Part sind drei von fünf Themen abgebildet (und in Vernehmlassung). Der Rest wird momentan er-/überarbeitet. Ausser der Jobs-Seite sind die Texte überall enthalten und warten auf den Feinschliff.', '2024-06-14 08:06:52.182021', 'Die Texte sind soweit möglich eingepflegt. Es gibt noch div. technische und strukturelle Tasks und ein Brocken Schreibarbeit - aber wir haben ja noch Zeit. ', '2024-06-14 08:06:52.182023', 70, 49, 1180, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2123, 'Aufgrund des früheren Imports konnte die saubere Basis nicht vorher gemacht resp. übernommen werden. Wir optimieren nun direkt auf der neuen Seite. ', '2024-06-14 08:08:50.168806', 'Die Target Zone erreichen wir aber erst nach Livegang, wenn die Seiten indexiert sind und wir uns dort den Fokus auf SEO setzen. ', '2024-06-14 08:08:50.168808', NULL, 49, 1179, 8, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2133, 'Actionplan mit Sum Buddy besprochen, am LST Monthly vom 24. Juni wird es dem LST Team vorgestellt', '2024-06-17 11:29:13.877015', '', '2024-06-17 15:29:28.407631', NULL, 36, 1208, 0, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2157, 'Neues Techinterview geführt (B.R.). Er hat Angebot erhalten und wurde durch Team validiert. Er hat das Angebot aber noch nicht angenommen.', '2024-06-17 22:24:47.806719', 'Ob das Stellenangebot angenommen wird, entscheidet sich diese Woche und ist ziemlich offen.', '2024-06-17 22:24:47.806721', NULL, 33, 1175, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2169, 'Thema ist gefunden und Materialien auch, aber nicht validiert.', '2024-06-18 08:30:58.733322', '', '2024-06-18 08:31:06.302618', NULL, 27, 1190, 0, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2170, '', '2024-06-18 08:31:30.567695', '', '2024-06-18 08:31:30.567698', NULL, 27, 1226, 0, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2190, 'Die Jahreszielwerte sind definiert.', '2024-06-27 07:42:30.475005', '', '2024-06-27 07:42:30.475011', NULL, 22, 1348, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2196, 'Mittagessen mit SWUN geplant', '2024-07-01 07:31:08.39807', '', '2024-07-01 07:31:08.398074', NULL, 28, 1299, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2198, '', '2024-07-01 07:32:07.570997', '', '2024-07-01 07:32:07.571', NULL, 28, 1300, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2199, '', '2024-07-01 07:32:21.153205', '', '2024-07-01 07:32:21.153209', NULL, 28, 1302, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2204, '', '2024-07-02 08:34:35.186389', '', '2024-07-02 08:34:35.186394', 0, 36, 1312, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2225, 'Abstimmungstermin am 14.8. mit Mäge', '2024-07-08 06:40:35.820797', '', '2024-07-08 06:40:35.820802', NULL, 5, 1267, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2228, 'Kickoff am 7.8.2024', '2024-07-08 16:50:53.073812', '', '2024-07-08 16:50:53.073816', NULL, 20, 1262, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2231, 'Wir verfolgen die Thematik weiter und suchen fleissig Speakers.', '2024-07-09 04:17:20.214264', '', '2024-07-09 04:17:20.214267', NULL, 49, 1283, 8, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2232, 'Wir halten unsere Botschafter:innen im Redaktionsplan im Taiga fest.', '2024-07-09 04:21:14.85107', '', '2024-07-09 04:21:14.851073', 0, 49, 1318, 8, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2235, 'Umfrage läuft, erster Partner-okr_pitch geplant', '2024-07-09 07:58:13.365619', '', '2024-07-09 07:58:13.365622', 0, 31, 1343, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2236, 'Planung in Arbeit', '2024-07-09 07:58:43.996949', '', '2024-07-09 07:58:43.996951', 0, 31, 1344, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2247, 'nach wie vor in der Vorbereitung ', '2024-07-10 08:23:25.936929', '', '2024-07-10 08:23:25.936932', NULL, 41, 1274, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2255, '', '2024-07-11 07:17:57.44252', '', '2024-07-11 07:17:57.442522', 0, 24, 1311, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2258, 'Noch nicht vorangekommen.', '2024-07-15 06:52:06.75086', '', '2024-07-15 06:52:06.750862', NULL, 27, 1333, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2314, 'Überlegungen zur Hardware https://codimd.puzzle.ch/YtDN9NakSASpsoD3Kht0mQ', '2024-07-23 12:20:02.783535', '', '2024-07-23 12:20:02.783543', NULL, 17, 1375, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2322, 'Juni 2024 quasi gleich gut wie Juli 2023.
Juli 2024 noch offen, wird Durchschnitt eingetragen.', '2024-07-24 11:21:38.305296', '', '2024-07-24 11:22:33.65673', NULL, 40, 1346, 5, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (2324, 'Strateige ist neu unter https://wiki.puzzle.ch/Puzzle/DevRubyApps', '2024-07-24 14:57:05.786305', '', '2024-07-24 14:57:05.78631', NULL, 28, 1298, 3, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2325, '', '2024-07-24 14:57:23.074994', '', '2024-07-24 14:57:23.075001', NULL, 28, 1299, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2327, 'ZG in Planung', '2024-07-24 15:00:02.407634', '', '2024-07-24 15:00:02.407641', NULL, 28, 1300, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2371, '- AWS Opportunities: wir prüfen die Opportunity mit SBB (Auftrag mit Phil Matti, bei welchem wir unterstützen konnten). Weitere mögliche Opportunity mittelfristig: Taste Match
- GCP: gemeinsames Account Alignment läuft. Wir prüfen gemeinsam potentielle Kunden und sprechen diese an (Bsp. ASTRA, Stadt ZH, Rega, Kanton Bern, ...). Nächster Abgleich mit Google Cloud: 15.08.24
- wir nehmen am AWS Summit Zürich vom 4. September als Teilnehmer teil
- wir nehmen am Google Cloud Summit vom 26. September als Teilnehmer teil
- "Mate mit"-Video Kick-Off in den nächsten Wochen
- nächster Sync im Team: 12. August', '2024-08-07 11:56:57.108319', '', '2024-08-07 11:56:57.108324', NULL, 16, 1250, 4, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2366, 'Angebot an J.B. verschickt, per 1. September mit 100% Pesnum', '2024-08-05 12:25:30.711097', '', '2024-08-05 12:26:11.358699', -0.9, 20, 1259, 4, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1108, 'Keine Beauftragungen', '2023-12-18 18:02:17.286193', '', '2023-12-18 18:02:17.286198', 20, 17, 220, 1, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1457, 'Priorisierung der Accounts vorgenommen. Die Prio 2 Accounts werden diese Woche in einer gemeinsamen Session (rbr+sfa) geplant. Für die Prio 1 Accounts folgt eine Termineinladung an betroffene Divisions.

Potential eher gross (Prio 1):
Flughafen Zürich AG
Interdiscount AG
Migros-Genossenschafts-Bund
Schweizerische Nationalbank
Liechtensteinische Landesverwaltung, Amt für Informatik

Potential eher klein (Prio 2):
Aveniq Avectris AG
Bundesamt für Meteorologie und Klimatologie MeteoSchweiz
Zürcher Kantonalbank
Kantonsspital Graubünden
Bank Julius Bär & Co. AG', '2024-02-26 11:33:44.170444', 'Stretch werden wir kaum erreichen, Target sollte drinliegen.', '2024-02-26 11:33:44.170447', 4, 33, 1092, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1789, 'Nicht alle Tasks sind so beschrieben. ', '2024-04-29 08:54:13.948232', '', '2024-04-29 08:54:13.948238', NULL, 27, 1198, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1791, 'Arbeti daran ist fortgeschritten, aber noch nicht fertig.', '2024-04-29 08:55:12.311792', '', '2024-04-29 08:55:12.311798', NULL, 27, 1185, 8, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1793, '', '2024-04-29 08:55:31.926936', '', '2024-04-29 08:55:31.926942', NULL, 27, 1187, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1794, '', '2024-04-29 08:55:38.766838', '', '2024-04-29 08:55:38.766845', NULL, 27, 1189, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1795, '', '2024-04-29 08:55:44.035441', '', '2024-04-29 08:55:44.035448', NULL, 27, 1190, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1864, 'Überlegungen dazu angestellt: P35 eher nicht anpassen, da der komplett auf Acrevis ausgerichtet ist, und dort andere Bedürfnisse existieren als intern/bei anderen Kunden', '2024-05-06 08:23:09.093143', 'Inputs Tim und Reto wohl eher in eigenen Teil im gitlab.puzzle.ch customer part.', '2024-05-06 08:23:09.093152', NULL, 32, 1159, 2, 'ordinal', 'FAIL', 2);
INSERT INTO okr_pitc.check_in VALUES (1900, 'Zeitlich nicht weiter gemacht ', '2024-05-13 09:28:18.124109', '', '2024-05-13 09:28:18.124115', NULL, 41, 1150, 1, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1904, 'Neuer Prozess ist eigenführt sowie hat Andy Gurtner und Andy neu gestartet auf dem Projekt ', '2024-05-13 09:31:14.674236', '', '2024-05-13 09:31:14.674243', NULL, 41, 1156, 4, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1905, 'wir haben nun alles das erste mal gemerget', '2024-05-13 09:31:54.085971', '', '2024-05-13 09:31:54.085981', NULL, 41, 1154, 4, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (1957, 'unverändert', '2024-05-22 12:17:32.588671', '', '2024-05-22 12:17:32.588674', -0.25, 20, 1136, 3, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1974, '', '2024-05-27 06:54:45.147263', '', '2024-05-27 06:54:45.147266', NULL, 28, 1164, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1976, 'Austausch mit AMA und PZ hat stattgefunden', '2024-05-27 06:55:17.553205', '', '2024-05-27 06:55:17.553212', NULL, 28, 1165, 3, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2080, 'Antwort von Mobi zu Iwan immer noch ausständig', '2024-06-10 13:37:36.170857', '', '2024-06-10 13:37:36.170862', NULL, 5, 1140, 2, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (2113, 'Auf Input von Saraina Plan geändert und nicht schriftliche Info, sondern mündliche Info am Puzzle Workshop mit Open Space für Feedback', '2024-06-13 12:18:43.160035', '', '2024-06-13 12:18:43.160039', 0, 5, 1137, 0, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2124, 'Vale AWS cert.', '2024-06-14 12:52:33.992703', '', '2024-06-14 12:52:33.992706', NULL, 31, 1207, 10, 'ordinal', 'TARGET', 3);
INSERT INTO okr_pitc.check_in VALUES (2134, '', '2024-06-17 12:55:34.454306', '', '2024-06-17 15:30:11.695647', 1097, 36, 1212, 0, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2158, 'Stellenangebot ist draussen (für S.W.), wurde jedoch noch nicht definitiv angenommen, darum Verbleib auf "Fail" :-(', '2024-06-17 22:26:06.389025', '', '2024-06-17 22:26:06.389027', NULL, 33, 1234, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2159, '21/29 Tasks abgearbeitet', '2024-06-17 22:28:01.906772', '', '2024-06-17 22:28:01.906774', 22, 33, 1233, 8, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2160, 'Absolut keine Zeit gehabt für dieses Thema.', '2024-06-17 22:29:17.47805', 'Wir nehmen es ins nächste Quartal mit...', '2024-06-17 22:29:17.478052', NULL, 33, 1235, 0, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2191, 'Projektteam wird aktuell angefragt.', '2024-06-27 07:52:04.158235', '', '2024-06-27 07:52:04.158245', NULL, 13, 1350, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2197, 'Budget seitens Puzzle ist von Marcel kommuniziert worden', '2024-07-01 07:31:56.872876', '', '2024-07-01 07:31:56.87288', NULL, 28, 1298, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2200, '', '2024-07-01 07:32:27.127187', '', '2024-07-01 07:32:27.127192', NULL, 28, 1327, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2201, '', '2024-07-01 07:35:09.008205', '', '2024-07-01 07:35:09.008209', 658198, 28, 1301, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2205, 'Pascou hat bereits unterschrieben, nächste Woche unterschreibt Simone, dann haben wir alle Ausfälle ersetzt', '2024-07-02 08:35:59.452167', '', '2024-07-02 08:35:59.452174', NULL, 36, 1322, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2206, 'Wir sind dran - Roadmap Meeting mit Yup, Mark und Andreas am 9. Juli angesetzt', '2024-07-02 08:36:39.311004', '', '2024-07-02 08:36:39.311008', NULL, 36, 1324, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2218, 'Input Tim und Reto sind Erfasst. Termin mit Tim zum Review, anschliessend MR erfassen.', '2024-07-03 09:24:58.842097', 'Termin mit Tim', '2024-07-03 09:24:58.8421', NULL, 32, 1257, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2237, 'SHKB Unterschrieben', '2024-07-09 07:59:24.592818', '', '2024-07-09 07:59:24.592834', NULL, 31, 1329, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2259, '', '2024-07-15 06:52:12.495851', '', '2024-07-15 06:52:12.495854', NULL, 27, 1334, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2326, 'Planung Vorgenommen', '2024-07-24 14:59:22.389446', '', '2024-07-24 14:59:22.389452', 728928, 28, 1301, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2328, 'Erste Updates', '2024-07-24 15:00:41.336728', 'klären mit Odi nach Ferien', '2024-07-24 15:00:41.336734', NULL, 28, 1302, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2367, 'Kickoff am 8.8.', '2024-08-05 12:38:08.086911', '', '2024-08-05 12:38:08.086916', NULL, 20, 1262, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2372, '- der Kick-Off wurde um einen Tag auf den 8. August verschoben.
- weitere Arbeit an der Auslegeordnung/Angebot ist durch Yup erfolgt.', '2024-08-07 12:00:05.127452', '', '2024-08-07 12:00:05.127457', NULL, 16, 1251, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2399, 'In Arbeit', '2024-08-12 08:32:44.217193', '', '2024-08-12 08:32:44.217197', 0, 31, 1344, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2398, 'Aufbau Leads: Fischer Spindler, Inventx, Five Informatik
Update Leads: SNB, Mobi', '2024-08-12 08:31:04.781121', '', '2024-08-12 08:32:55.49917', 2, 31, 1343, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2422, 'Knowledge-Base live - noch nicht "mindestens" ein Beitrag pro Member', '2024-08-12 11:12:13.535631', '', '2024-08-12 11:12:13.535636', NULL, 3, 1321, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2458, 'Für nächste Events haben wir Member auf der Liste angefragt für Posts. Target Zone rückt näher + Simu H. hat heute einen Beitrag gepostet. ', '2024-08-19 12:36:07.08968', '', '2024-08-19 12:36:07.089683', 9, 49, 1318, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2464, 'Unsere intitialen Erwartungen sind mit dev/tre abgestimmt und definiert und der MO Leaders kommuniziret', '2024-08-21 06:04:25.998078', '', '2024-08-21 06:04:25.998081', NULL, 5, 1267, 8, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2251, '- wir haben das weitere Vorgehen für die MO Digitale Lösungen besprechen und festgelegt. Terminfindung für Workshop läuft.
- wir wollen die Marktanalyse, welche aktuell noch läuft, mit weiteren Experten-Interviews mit Architekten von Kunden ergänzen (wo es terminlich noch reicht).', '2024-07-10 19:49:27.073877', '', '2024-08-21 08:55:02.408038', NULL, 16, 1254, 5, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2477, 'Commit ist erst mit Abschluss ZG möglich', '2024-08-22 07:46:42.953106', '', '2024-08-22 07:46:42.953108', NULL, 28, 1300, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2493, '-', '2024-08-22 14:38:47.619987', '', '2024-08-22 14:38:47.61999', NULL, 41, 1268, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2509, 'Weiterarbeit mit Analyse, Abgleiche, Diskussionen', '2024-08-26 08:29:19.077843', '', '2024-08-26 08:29:19.077846', NULL, 4, 1294, 0, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1109, 'Keine weiteren positiven Rückmeldungen', '2023-12-18 18:03:11.703252', 'Erinnerungen senden', '2023-12-18 18:03:11.703257', 0, 17, 221, 2, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1458, 'Wurde im Februar nicht weiterverfolgt. Rückmeldung immer noch ausstehend. Nachfassen nötig.', '2024-02-26 11:35:24.6927', 'Gas geben. Zeit reservieren.', '2024-02-26 11:35:24.692703', 1, 33, 1095, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1459, 'Wird im Rahmen der MAGs angeschaut (die meisten werden in den kommenden 2 Wochen durchgeführt)', '2024-02-26 11:36:45.967526', 'Als Thema für MAGs vorsehen. ', '2024-02-26 11:36:45.967529', NULL, 33, 1101, 4, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1790, 'Andere Themen waren bisher dringender.', '2024-04-29 08:54:44.391402', '', '2024-04-29 08:54:44.391408', NULL, 27, 1226, 4, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1865, 'keine Weiteren Arbeiten erfolgt', '2024-05-06 08:23:32.461474', '', '2024-05-06 08:23:32.46148', 4, 32, 1160, 9, 'metric', NULL, 6);
INSERT INTO okr_pitc.check_in VALUES (1901, 'zeitlich nicht weitergemacht ', '2024-05-13 09:28:40.142806', '', '2024-05-13 09:28:40.142813', NULL, 41, 1153, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1958, 'Betreuungsperson BBT für Basislehrjahr (SBB) und Java Bereich angestellt. Betreuung während pe''s Abwesenheit: js oder cg.
Projekt UniLu', '2024-05-22 12:22:11.430781', '', '2024-05-22 12:22:11.430785', NULL, 20, 1139, 6, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1975, '', '2024-05-27 06:54:53.836505', '', '2024-05-27 06:54:53.836509', NULL, 28, 1231, 2, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2081, '', '2024-06-10 13:38:06.38461', '', '2024-06-10 13:38:06.384616', 16000, 5, 1141, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2082, '', '2024-06-10 13:38:17.191616', '', '2024-06-10 13:38:17.191634', NULL, 5, 1146, 10, 'ordinal', 'STRETCH', 1);
INSERT INTO okr_pitc.check_in VALUES (2083, '', '2024-06-10 13:38:27.712735', '', '2024-06-10 13:38:27.71274', NULL, 5, 1147, 10, 'ordinal', 'STRETCH', 0);
INSERT INTO okr_pitc.check_in VALUES (2085, 'Teampool definiert. Jedoch zurückgestellt bis externer Spezialist gefunden.', '2024-06-10 13:54:49.829232', '', '2024-06-10 13:54:49.829241', NULL, 20, 1135, 1, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2086, 'Confidence reduziert.', '2024-06-10 13:55:43.439313', '', '2024-06-10 13:55:43.439319', -0.25, 20, 1136, 1, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2127, 'unverändert', '2024-06-14 13:04:53.355808', '', '2024-06-14 13:04:53.355811', NULL, 31, 1216, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2135, 'Grundstruktur auf POC-Blattform erstellt, Briefing / Vorstellung Members am letzten WAC-Morning. Aktuell noch keine Partizipation durch Members. ', '2024-06-17 12:59:16.802215', '', '2024-06-17 12:59:16.802218', NULL, 3, 1223, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2192, '', '2024-06-27 09:29:03.710205', '', '2024-06-27 09:29:03.710214', 75.73, 22, 1349, 2, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2207, 'noch keine Veränderung. Werden eine Roadmap erstellen', '2024-07-02 08:37:49.443678', 'Roadmap erstellen', '2024-07-02 08:37:49.443682', 0, 36, 1314, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2208, 'keine Veränderung', '2024-07-02 08:38:28.416743', '', '2024-07-02 08:38:28.416747', 0, 36, 1315, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2209, 'keine Veränderung', '2024-07-02 08:38:51.090563', '', '2024-07-02 08:38:51.090567', NULL, 36, 1316, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2210, 'keine Veränderung', '2024-07-02 08:39:14.512815', '', '2024-07-02 08:39:14.512819', NULL, 36, 1317, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2238, 'Austausch mit Members ist geplant', '2024-07-09 08:00:19.661192', '', '2024-07-09 08:00:19.661194', NULL, 31, 1332, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2260, '', '2024-07-15 07:06:39.196763', '', '2024-07-15 07:06:49.938101', NULL, 28, 1298, 3, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2261, '', '2024-07-15 07:07:02.47789', '', '2024-07-15 07:07:02.477893', NULL, 28, 1299, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2329, 'Termin geplant', '2024-07-24 15:00:54.009216', '', '2024-07-24 15:00:54.009224', NULL, 28, 1327, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2330, 'Branch Kickoff findet am 29.8 statt', '2024-07-25 08:02:58.141067', '', '2024-07-25 08:02:58.141072', NULL, 13, 1245, 10, 'ordinal', 'TARGET', 3);
INSERT INTO okr_pitc.check_in VALUES (2331, 'Branch Kickoff findet am 29.8 statt', '2024-07-25 08:03:31.327782', '', '2024-07-25 08:03:31.327804', NULL, 13, 1247, 1, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2277, 'Vorschlag Prompt in KW 30, anschliessend Feinschliff mit Saraina und Vorstellung dem Team.', '2024-07-16 05:04:36.357297', '', '2024-07-29 04:27:37.992853', NULL, 49, 1279, 5, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2334, 'Prompt erstellt und mit Saraina gechallended. Wird im nächsten MarCom Austausch präsentiert.', '2024-07-29 04:52:44.349026', '', '2024-07-29 04:52:44.349033', NULL, 49, 1279, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2335, 'Status Quo', '2024-07-29 04:53:06.271826', '', '2024-07-29 04:53:06.271836', NULL, 49, 1280, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2336, 'Alle Seiten haben grünen oder orangen SEO Status.', '2024-07-29 06:37:01.027569', '', '2024-07-29 06:37:01.027575', NULL, 49, 1281, 9, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2373, '- Telefonkampagne mit Carlos ist noch on-going und die Einarbeitung der Resultate ist noch offen.
- der Workshop ist prov. am 3. September geplant. Definitiv entschieden, wird nach der Rückkehr von Berti aus seinen Ferien.
- Yup hat sich auch bereits Gedanken zu einem marktorientierten Angebot gemacht.', '2024-08-07 12:06:52.888427', '', '2024-08-07 12:06:52.888431', NULL, 16, 1254, 4, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2375, 'Leider noch keine Tasks komplett erledigt', '2024-08-08 05:36:17.568261', '', '2024-08-08 05:36:17.568266', 0, 24, 1311, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2400, 'In Aussicht: MediData', '2024-08-12 08:34:48.187447', '', '2024-08-12 08:34:48.187451', NULL, 31, 1329, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2423, 'Prompt muss noch etwas feingeschliffen werden. Anschliessend Challenge im Team.', '2024-08-12 12:47:40.887038', '', '2024-08-12 12:47:40.887043', NULL, 49, 1279, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2424, 'Keine Veränderungen (Diverse Deals noch offen)', '2024-08-13 08:52:20.520429', '', '2024-08-13 08:52:20.520436', 738816, 60, 1328, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2425, 'Anstellung von Joël', '2024-08-14 12:00:25.996301', '', '2024-08-14 12:00:25.996305', 0.1, 20, 1259, 4, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (2426, 'Wir haben 45.17 h auf das Wartungsbudget gebucht ', '2024-08-15 07:06:48.084593', '', '2024-08-15 07:06:48.084598', NULL, 41, 1268, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2427, 'noch nicht gestartet ', '2024-08-15 07:07:11.235482', '', '2024-08-15 07:07:11.235489', NULL, 41, 1270, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2429, 'ist eingetlich abgeschlossen', '2024-08-15 07:08:16.927913', '', '2024-08-15 07:08:16.927925', 1, 41, 1271, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2434, 'OpenShift Migration läuft. Stand Crypopus noch offen.', '2024-08-15 14:20:43.419565', '', '2024-08-15 14:20:43.41957', NULL, 28, 1298, 3, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2440, 'Aktuell keine Veränderung für den betrachteten Zeitraum', '2024-08-19 09:03:53.238391', '', '2024-08-19 09:03:53.238393', 91161, 3, 1312, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2441, 'Pascou ist ab heute produktiv für IGE unterwegs', '2024-08-19 09:04:12.180121', '', '2024-08-19 09:04:12.180125', NULL, 3, 1322, 5, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2442, 'Keine Veränderung', '2024-08-19 09:04:23.329389', '', '2024-08-19 09:04:23.329391', NULL, 3, 1324, 5, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2447, 'Keine Änderung', '2024-08-19 09:06:30.493098', ' ', '2024-08-19 09:06:30.493101', NULL, 3, 1319, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2448, 'Keine Veränderung ggü. Vorwoche', '2024-08-19 09:06:46.256194', '', '2024-08-19 09:06:46.256197', NULL, 3, 1321, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2465, 'ii bestätigt das Use Case auf Info Anlass hin abgeschlossen wird. Und Blogs kommen sollten', '2024-08-21 06:44:45.451744', '', '2024-08-21 06:44:45.451746', NULL, 5, 1276, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2466, 'Abstimmung mit ar am 22.8.', '2024-08-21 06:46:00.032961', '', '2024-08-21 06:46:00.032963', 0, 5, 1277, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2478, '', '2024-08-22 07:47:04.295973', '', '2024-08-22 07:47:04.295976', NULL, 28, 1302, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2479, 'Geplant für 03.09.24', '2024-08-22 07:47:36.230259', '', '2024-08-22 07:47:36.230262', NULL, 28, 1327, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2510, 'Status Quo', '2024-08-26 08:29:33.989582', '', '2024-08-26 08:29:33.989584', NULL, 4, 1295, 10, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1110, 'Subscription-Anfragen existieren', '2023-12-18 18:03:54.960276', 'Kontakte pflegen und ausbauen', '2023-12-18 18:03:54.96028', 2, 17, 222, 6, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1111, 'Zwei interessante Gespräche mit Kandidaten geführt', '2023-12-18 18:05:00.089251', '', '2023-12-19 08:29:00.200201', 19, 17, 223, 9, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (1461, 'Status quo. Team Forum steht diesen Donnerstag an. Somit fürs nächste Checkin dann Sprung mind. auf Commit vorgesehen. Ob es bis Ende März für viel mehr reicht - fraglich.', '2024-02-26 11:43:07.682717', '', '2024-02-26 11:45:22.484051', NULL, 33, 1097, 4, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (1460, 'Implementierung wird im Team-Meeting von dieser Woche ausprobiert. Team-Engagement wird ebenfalls diese Woche im Team Forum definiert. Somit Verbesserung auf "Target" fürs nächste Checkin erwartet.', '2024-02-26 11:41:04.322923', '', '2024-02-26 11:45:47.948339', NULL, 33, 1077, 7, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (1792, '', '2024-04-29 08:55:23.324522', '', '2024-04-29 08:55:23.324528', NULL, 27, 1186, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1797, 'Mark konnte bei einigen Projekten aushelfen und fragen beantworten: Von JWTs über Keycloak zu Vulns. Tatsächlich auch verrechenbar bei Brixel. Referenz wird aber noch abgeklärt, drum mal nur Commit.', '2024-04-29 08:57:24.374326', '', '2024-04-29 08:57:24.374333', NULL, 27, 1205, 9, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1866, 'keine weitere Kommunikation erfolgt.', '2024-05-06 09:02:20.89486', 'auf Tinu warten', '2024-05-06 09:02:20.894883', 2, 32, 1158, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1902, 'kein neues Relase geplant ', '2024-05-13 09:29:06.100737', '', '2024-05-13 09:29:06.100754', 0.25, 41, 1151, 3, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1977, 'Interne Projekt können wir noch nicht planen', '2024-05-27 06:55:56.083019', '', '2024-05-27 06:55:56.083023', NULL, 28, 1232, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1978, '', '2024-05-27 06:56:07.211322', '', '2024-05-27 06:56:07.211326', NULL, 28, 1167, 2, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2087, 'unverändert', '2024-06-10 13:56:29.119705', '', '2024-06-10 13:56:29.119716', NULL, 20, 1139, 6, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2084, '', '2024-06-10 13:42:52.962147', '', '2024-06-11 13:48:40.370942', 947829, 60, 1230, 2, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (2126, 'Unverändert', '2024-06-14 13:04:39.476592', '', '2024-06-14 13:04:39.476595', NULL, 31, 1217, 4, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2136, 'N/A', '2024-06-17 15:02:38.168633', 'In nächsten Zyklus nehmen', '2024-06-17 15:02:38.168636', NULL, 4, 1194, 0, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2137, 'N/A', '2024-06-17 15:03:10.777175', 'In nächsten Zyklus nehmen', '2024-06-17 15:03:10.777178', NULL, 4, 1196, 0, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2138, 'Termin 25. Juni 2024 (einfach ein Essen)', '2024-06-17 15:03:54.558573', '', '2024-06-17 15:04:33.677994', 1, 4, 1197, 10, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (2140, 'N/A', '2024-06-17 15:06:07.946044', 'In nächsten Zyklus nehmen', '2024-06-17 15:06:07.946047', NULL, 4, 1204, 0, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2141, 'N/A', '2024-06-17 15:06:26.807345', 'In nächsten Zyklus nehmen', '2024-06-17 15:06:26.807348', NULL, 4, 1221, 0, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (2142, 'N/A', '2024-06-17 15:06:54.928846', 'In nächsten Zyklus nehmen', '2024-06-17 15:06:54.928849', NULL, 4, 1224, 0, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2145, 'N/A', '2024-06-17 15:09:08.137627', 'In nächsten Zyklus verschoben', '2024-06-17 15:09:08.137629', NULL, 4, 1228, 0, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2211, 'keine Veränderung', '2024-07-02 08:39:31.990511', '', '2024-07-02 08:39:31.990515', NULL, 36, 1319, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2239, '', '2024-07-09 08:16:11.006017', '', '2024-07-09 08:16:11.006019', 751825, 60, 1328, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2262, '', '2024-07-15 07:07:18.454267', '', '2024-07-15 07:07:18.454269', NULL, 28, 1300, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2264, '', '2024-07-15 07:07:43.998382', '', '2024-07-15 07:07:43.998385', NULL, 28, 1327, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2278, 'Termin mit Tru am 23.7.24', '2024-07-16 05:07:11.31255', '', '2024-07-16 05:07:11.312553', NULL, 49, 1280, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2332, 'Keine Veränderung', '2024-07-25 08:04:40.582189', '', '2024-07-25 08:04:40.582198', 2606285, 13, 1241, 2, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2337, 'Swiss Life Bestellung sollte diese Woche kommen', '2024-07-29 07:55:22.232464', '', '2024-07-29 07:55:22.232474', 83001, 36, 1312, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2341, 'SoMe Post über Absage Front Conference und Push Puzzle Up', '2024-07-29 07:56:51.599292', '', '2024-07-29 07:56:51.599297', 3, 36, 1315, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2344, 'Actionplan erstellt', '2024-07-29 07:58:18.432855', '', '2024-07-29 07:58:18.432866', NULL, 36, 1319, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2374, '', '2024-08-07 12:40:04.575603', '', '2024-08-07 12:40:04.57561', 75.48, 22, 1349, 1, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2401, '', '2024-08-12 08:37:36.988425', '', '2024-08-12 08:38:10.364844', 8.1, 31, 1331, 8, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2428, 'wir warten noch auf den Entscheid per Ende August von EJV', '2024-08-15 07:07:50.13452', '', '2024-08-15 07:07:50.134539', NULL, 41, 1285, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2435, 'Kommt einfach nicht voran..', '2024-08-15 14:21:01.881489', '', '2024-08-15 14:21:01.881491', NULL, 28, 1299, 1, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2443, 'Aktuell keine Veränderung, diese Woche kommen UX-Meetup und Zeix Sommerfest', '2024-08-19 09:04:46.412466', '', '2024-08-19 09:04:46.412471', 0, 3, 1314, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2444, 'Twitter-Post für Mate mit UX', '2024-08-19 09:05:10.09505', '', '2024-08-19 09:05:10.095052', 5, 3, 1315, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2446, 'Analyse durch Pascou gemacht (Abgleich nächste Woche)', '2024-08-19 09:06:11.375463', '', '2024-08-19 09:06:11.375465', NULL, 3, 1317, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2467, '- AWS Opportunities: die Helvetia Opportunity wird aktuell bei AWS geprüft.
- AWS: Referenz mit Helvetia publiziert: https://www.puzzle.ch/success-stories/migration-eines-produktiven-openshift-clusters-zu-rosa
- GCP: Rega und ASTRA haben Interesse an einem Besuch bei/Austausch mit Google Nächster Abgleich mit Google Cloud: 11.09.24
- GCP: so lange wir nicht Partner sind, können wir noch keine Opportunities erfassen
- GCP: Sales Credentials in Angriff genommen (1 abgeschlossen). Technische Zertizierung bei Yelan am Laufen.
- GCP: Blog/Referenz zu Interdiscount möglich.
- nächster Sync im Team: 12. August', '2024-08-21 11:15:15.407291', '', '2024-08-21 11:15:15.407295', NULL, 16, 1250, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2468, '- Kick-Off für MO Beratung wurde am 8. August durchgeführt
- erste Auslegeordnung wurde erstellt und besprochen
- nächster Termin für Verfeinerung und weitere Ausarbeitung wird festgelegt', '2024-08-21 11:18:19.589386', '', '2024-08-21 11:18:19.589388', NULL, 16, 1251, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2469, '- der Workshop Markt-Research und Zukunftsbild Digitale Lösungen findet am 3. September statt.
- letzte Punkte von Carlos und Pascal Sieber fliessen noch in den Markt-Research ein. Diese wird nun abgeschlossen bis zum Workshop.
- die Schlussfolgerungen werden vor dem Workshop noch finalisiert.', '2024-08-21 11:20:32.080765', '', '2024-08-21 11:20:32.080767', NULL, 16, 1254, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2480, 'Acend, Uni Bern und Puzzle gibts eine Tripartnerschaft. Lab für Engineers deckt Uni Bern gut ab. Nun verschiedene ML Ops Labs im Angebot. Im Moment keine Labs gebucht und geplant. Erst wieder für November und Januar geplant. Bis jetzt wurden noch gar keine Werbekampagnen lanciert.
Die monatliche Abstimmung gibt es, aber dbi und ple noch nicht eingeladen.', '2024-08-22 07:48:47.277447', '', '2024-08-22 07:48:47.27745', 0, 5, 1277, 0, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2487, '24.1% Marge im Juli (unerwartet, fast schon unerklärbar hoch...)', '2024-08-22 11:34:23.569699', '', '2024-08-22 11:34:23.569702', 24.1, 33, 1310, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1462, 'Tinu geht ans Richmond Forum, ich rechne ihn als halben /Sysler ein', '2024-02-26 11:59:35.422901', '', '2024-02-26 11:59:35.422906', 3.5, 32, 1029, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1796, '', '2024-04-29 08:55:48.606419', '', '2024-04-29 08:55:48.606426', NULL, 27, 1192, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1903, 'wir haben ein erfolgreicehs Communitymeeting durchgeführt mit 20 Personen', '2024-05-13 09:30:09.624494', '', '2024-05-13 09:30:09.624501', NULL, 41, 1152, 7, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (1979, '', '2024-05-27 06:58:25.200436', '', '2024-05-27 06:58:25.20044', 1043166, 28, 1168, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2088, 'Stand wie 3.6.', '2024-06-10 14:14:59.011153', '', '2024-06-10 14:14:59.011158', NULL, 29, 1142, 10, 'ordinal', 'STRETCH', 0);
INSERT INTO okr_pitc.check_in VALUES (2139, 'N/A', '2024-06-17 15:05:03.591247', 'In nächsten Zyklus nehmen', '2024-06-17 15:05:03.59125', 1, 4, 1199, 0, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2143, 'Auch im Mai tiefer als 70%, da Eintritt und viel Sales-Aufwand', '2024-06-17 15:08:01.503585', '', '2024-06-17 15:08:01.503588', NULL, 4, 1225, 0, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2212, 'keine Veränderung', '2024-07-02 08:39:49.884998', '', '2024-07-02 08:39:49.885003', NULL, 36, 1321, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2240, 'noch keine Resultate', '2024-07-09 08:55:40.911738', '', '2024-07-09 08:55:40.911741', NULL, 60, 1346, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2263, '', '2024-07-15 07:07:35.943486', '', '2024-07-15 07:07:35.943488', NULL, 28, 1302, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2279, '', '2024-07-16 05:13:15.315452', 'Wir nutzen den Juli um weiterhin die Website Inhalte zu optimieren. Die organische Suchsichtbarkeit prüfen wir am 5. August (1. Monat live)', '2024-07-16 05:13:15.315454', NULL, 49, 1281, 9, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2333, 'Keine Aktivitäten bisher', '2024-07-25 11:41:52.466172', '', '2024-07-25 11:41:52.466177', NULL, 4, 1278, 2, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2338, 'keine Veränderung seit letzter Woche', '2024-07-29 07:55:43.823146', '', '2024-07-29 07:55:43.823154', NULL, 36, 1322, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2342, 'keine Veränderung seit letzter Woche', '2024-07-29 07:57:34.851061', '', '2024-07-29 07:57:34.851067', NULL, 36, 1316, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2402, '29.8. PE PuzzleUp! Dänu & Adi', '2024-08-12 08:39:26.547866', '', '2024-08-12 08:39:38.874323', NULL, 31, 1332, 10, 'ordinal', 'STRETCH', 1);
INSERT INTO okr_pitc.check_in VALUES (2430, 'wir sind nun bereit für das anstehende Audit vom 19. + 20. August', '2024-08-15 07:08:54.702119', '', '2024-08-15 07:08:54.702122', NULL, 41, 1274, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2436, 'Schon fast alle ZG geplant', '2024-08-15 14:21:57.346716', '', '2024-08-15 14:21:57.34673', NULL, 28, 1300, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2438, '', '2024-08-15 14:22:53.593456', '', '2024-08-15 14:22:53.59346', NULL, 28, 1327, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (165, 'Keine Veränderung', '2023-08-11 13:36:27.686844', '', '2023-08-10 22:00:00', 0.1, 28, 98, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2445, 'Analyse Matomo ist gemacht (TobiS) - aber noch nicht diskutiert.', '2024-08-19 09:05:39.584055', '', '2024-08-19 09:05:39.584057', NULL, 3, 1316, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2470, 'Die Quartalszahlen werden per Ende Q1 ausgewertet (im Oktober) und es wird neu beurteilt, ob Quartalsziele daraus abgeleitet und definiert werden sollen.', '2024-08-22 06:18:22.648031', '', '2024-08-22 07:32:34.974704', NULL, 22, 1348, 1, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2481, 'Keine Veränderung', '2024-08-22 07:50:12.410089', '', '2024-08-22 07:50:12.410092', 781427, 28, 1301, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2488, 'Bisher keien Anstellung. Einzelne Kandidaten in der Pipeline, jedoch noch niemand kurz vor Vertragsabschluss.', '2024-08-22 11:35:27.569446', '', '2024-08-22 11:35:27.569449', NULL, 33, 1307, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2494, 'wir warten nachwievor auf eine Antwort von EJV', '2024-08-22 14:39:39.041392', '', '2024-08-22 14:39:39.041395', NULL, 41, 1285, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2511, 'Status Quo', '2024-08-26 08:30:01.992953', '', '2024-08-26 08:30:01.992957', NULL, 4, 1296, 10, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2515, 'Zwei aussichtsreiche Kandidaten, einer davon der sehr cool gewesen wäre hat leider aus Lohngründen abgesagt', '2024-08-26 13:31:22.317003', '', '2024-08-26 13:32:12.862273', NULL, 34, 1255, 5, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2522, 'Noch den Members schulen in den kommenden Team Meetings', '2024-08-27 13:01:39.203547', '', '2024-08-27 13:01:39.203563', NULL, 5, 1261, 9, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2523, '', '2024-08-27 13:02:40.636166', 'Neue Termine mit CTO und abr aufgesetzt um zu schauen, wie wir im Q2 weiterfahren wollen', '2024-08-27 13:02:40.636183', 0, 5, 1275, 1, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2537, 'Erwartungen sind im Dokument "Digitale Lösungen - Mitwirken&Saleserwartung der Dev-Divisions.odt" () erfasst und unter den dev-Bereichen abgestimmt', '2024-08-31 10:49:18.124015', 'Informieren und Besprechen der /dev-Abteilung-Erwartungen im Workshop Digitale Lösungen am Di. 3. September 2024', '2024-08-31 10:57:05.692485', NULL, 4, 1290, 10, 'ordinal', 'TARGET', 2);
INSERT INTO okr_pitc.check_in VALUES (2542, 'keine Veränderung gegenüber der Woche vorher', '2024-09-02 09:02:35.306345', '', '2024-09-02 09:02:35.306353', 91161, 36, 1312, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (702, 'Keine Veränderung ', '2023-11-24 10:23:55.707086', '', '2023-11-23 23:00:00', 0.4, 28, 180, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2544, 'Beschaffungskonferenz und PuzzleUP und UX Nordics Konferenz', '2024-09-02 09:04:42.565913', '', '2024-09-02 10:50:17.889437', 5, 36, 1314, 4, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2546, 'Workshop morgen - sonst keine Änderung', '2024-09-02 12:18:31.907265', '', '2024-09-02 12:18:31.907272', NULL, 3, 1324, 5, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2548, 'Keine Änderung', '2024-09-02 12:19:27.909424', '', '2024-09-02 12:19:27.909431', NULL, 3, 1317, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2549, 'Keine änderung', '2024-09-02 12:19:49.325593', '', '2024-09-02 12:19:49.3256', NULL, 3, 1319, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2550, 'Umfrage über Chat, Diskussion am WAC Morning geplant. Folder Struktur im Nextcloud analysiert (Notwendigkeit Struktur zu verändern)', '2024-09-02 12:20:58.008231', '', '2024-09-02 12:20:58.008238', NULL, 3, 1321, 4, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2552, '', '2024-09-02 12:37:47.450659', '', '2024-09-02 12:37:47.450666', NULL, 27, 1340, 9, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2553, '', '2024-09-02 12:37:55.501116', '', '2024-09-02 12:37:55.501123', NULL, 27, 1339, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2555, '', '2024-09-02 12:38:14.304989', '', '2024-09-02 12:38:14.304997', NULL, 27, 1333, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2557, 'In den Teamm Meetings September einplaen', '2024-09-02 12:53:06.73347', '', '2024-09-02 12:53:06.733477', NULL, 5, 1261, 9, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2561, 'Status Quo bis am 10. September.', '2024-09-03 06:08:19.24381', '', '2024-09-03 06:08:19.243818', NULL, 49, 1279, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2563, 'Keien Grossen Verändeurngen', '2024-09-03 08:54:13.036815', '', '2024-09-03 08:54:13.036822', 740120, 60, 1328, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2564, '', '2024-09-03 14:26:07.992039', '', '2024-09-03 14:26:07.992043', NULL, 28, 1327, 5, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2566, 'Die Rahmenbedingungen und Ziele sind definiert und sind durch die GL verabschiedet.', '2024-09-05 07:51:45.658022', '', '2024-09-05 07:51:45.658026', NULL, 13, 1247, 1, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2567, 'Keine Veränderung', '2024-09-05 07:52:18.163822', '', '2024-09-05 07:52:18.163826', NULL, 13, 1245, 10, 'ordinal', 'TARGET', 3);
INSERT INTO okr_pitc.check_in VALUES (2569, 'Folgemeeting ist bisher nicht erfolgt. Aufgrund von Ferienabwesenheiten wird das Thema wohl nicht mehr im Q1 weiterentwickelt.', '2024-09-05 08:04:22.453893', '', '2024-09-05 08:04:49.018146', NULL, 20, 1262, 1, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2571, 'Zusammenarbeit verifiziert und erste Anpassungen vorgenommen. Salesunterstützung und Zusammenarbeiten zwischen /mobility und /sales soll weiter optimiert werden.', '2024-09-05 08:09:36.381411', '', '2024-09-05 08:09:36.381433', NULL, 20, 1266, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2575, '', '2024-09-05 14:54:48.235303', '', '2024-09-05 14:56:28.150128', 8.14, 31, 1331, 10, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (2576, 'Status Quo', '2024-09-06 09:03:51.937508', '', '2024-09-06 09:03:51.937512', NULL, 4, 1278, 0, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2577, 'Status Quo', '2024-09-06 09:05:15.313444', 'Kurzpräsi in Teammeeting vom 10. September 2024', '2024-09-06 09:05:15.313468', NULL, 4, 1288, 0, 'ordinal', 'COMMIT', 5);
INSERT INTO okr_pitc.check_in VALUES (1463, 'Zammad update auf 6 in Planung, damit Status "Waiting for Customer" verfügbar ist und möglichst keine falschen Eskalationen mehr da sind', '2024-02-26 12:01:10.954221', '', '2024-02-26 12:01:10.954224', 2, 32, 1037, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1798, '', '2024-04-29 10:32:01.249181', '', '2024-04-29 10:32:01.249187', NULL, 28, 1164, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1800, 'Erster Entwurf, verschiedene Inputs von PSI und ODI erhalten.', '2024-04-29 10:32:49.970252', '', '2024-04-29 10:32:49.970258', NULL, 28, 1165, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1980, '', '2024-05-27 07:03:18.116783', '', '2024-05-27 07:03:28.423602', NULL, 27, 1198, 7, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2089, '', '2024-06-10 14:15:25.552693', '', '2024-06-10 14:15:25.552698', 0, 29, 1144, 1, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2144, 'N/A', '2024-06-17 15:08:45.036831', 'Einige Möglichkeiten aber noch keine konkrete Pläne. Muss weiterverfolgt werden', '2024-06-17 15:08:45.036834', NULL, 4, 1227, 0, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (2213, '', '2024-07-02 10:55:31.7991', '', '2024-07-02 10:55:31.799104', 0, 17, 1237, 2, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2241, '', '2024-07-09 10:32:49.766128', '', '2024-07-09 10:32:49.76613', 2473356, 13, 1241, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2242, 'Noch nicht gestartet', '2024-07-09 10:33:49.788583', '', '2024-07-09 10:33:49.788586', NULL, 13, 1247, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2265, '', '2024-07-15 07:09:16.167167', '', '2024-07-15 07:09:16.167171', 670466, 28, 1301, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2280, 'Die ersten Divisionsaustausche finden statt - Commit wird wahrscheinlich nächste Woche erreicht.', '2024-07-16 05:24:13.400385', '', '2024-07-16 05:24:13.400387', NULL, 49, 1283, 8, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2339, 'keine Veränderung seit letzter Woche', '2024-07-29 07:56:09.191719', '', '2024-07-29 07:56:09.191727', NULL, 36, 1324, 5, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2343, 'keine Veränderung seit letzter Woche', '2024-07-29 07:57:55.789828', '', '2024-07-29 07:57:55.789838', NULL, 36, 1317, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2404, 'Feedback von Tim erhalten, diese noch einspeisen', '2024-08-12 08:51:40.760008', '', '2024-08-12 08:51:40.760015', NULL, 32, 1257, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2405, 'Feedback msc und jbl erfolgt heute. Feedback von sfa eingeholt. Feedback mit ar geplant', '2024-08-12 08:53:33.590431', '', '2024-08-12 08:53:33.590435', NULL, 5, 1261, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2406, 'Buchezentrum
SmartIT

BFH sieht schon mal sehr vielversprechend aus', '2024-08-12 08:53:50.242686', '', '2024-08-12 08:53:50.24269', NULL, 32, 1258, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2408, 'Deep Dive am 13.8.', '2024-08-12 08:55:41.727341', '', '2024-08-12 08:55:41.727346', 0, 5, 1275, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2409, 'noch nichts passiert', '2024-08-12 08:55:48.059637', '', '2024-08-12 08:55:48.059643', NULL, 32, 1256, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2410, 'Lüku hat das Thema angeschaut', '2024-08-12 08:56:12.468553', '', '2024-08-12 08:56:12.46856', NULL, 32, 1256, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2431, 'Marktvalidierung mit Trendanalyse in Perplexity erstellt und mit CSO und CTO validiet. Als Ergebnis gemeinsam entschieden, die Shortlist nicht zu verkürzen, sondern weiter Lean und mehrgleisig voranzugehen. Die nächsten Schritte sind definiert. Einige sind kurzfristig, andere werden im Q2 angegangen', '2024-08-15 07:41:48.851798', '', '2024-08-15 07:42:25.019598', NULL, 5, 1263, 10, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (2437, 'Ruby Charta mit Odi besprochen. Aktuell keine Vakanzen welche wir identifiziert haben.', '2024-08-15 14:22:43.813375', '', '2024-08-15 14:22:43.813377', NULL, 28, 1302, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2439, 'Philipp eingeplant', '2024-08-15 14:24:31.052013', '', '2024-08-15 14:24:31.052016', 781427, 28, 1301, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2449, 'Weiterentwicklung angestossen. Sonst unverändert', '2024-08-19 09:45:28.729392', '', '2024-08-19 09:45:28.729397', NULL, 20, 1262, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2450, '', '2024-08-19 09:47:48.138399', 'Status Quo', '2024-08-19 09:47:48.138401', NULL, 4, 1278, 1, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2471, '', '2024-08-22 06:25:34.790906', '', '2024-08-22 06:25:34.790909', 76.48, 22, 1349, 1, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2482, 'Mögliche weitere Verbesserung in Aussicht auf Grund Interessent für Untermiete.', '2024-08-22 07:54:11.564351', '', '2024-08-22 07:54:11.564354', 4.6, 22, 1253, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2489, 'Vertrag mit Sven abgeschlossen. Start im November. Auslastung könnte über IPR Monatsbericht weitgehend sichergestellt werden. Einsatz in verrechenbarem Kundenprojekt bevorzugt. Betreuungsdetails noch nicht geklärt', '2024-08-22 11:36:19.271131', '', '2024-08-22 11:36:19.271133', NULL, 33, 1308, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2495, '- Erste Klärung hat stattgefunden.
- Mit SERV und BBL-WTOs haben wir zwei Rahmenverträge gewonnen, an denen /mobility involviert sind.', '2024-08-22 15:08:02.343017', '', '2024-08-22 15:08:02.34302', NULL, 20, 1266, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2498, 'Aktuell noch nichts - wir sind dran bei Mobi usw. ', '2024-08-26 08:10:24.850606', '', '2024-08-26 08:10:24.850609', 91161, 3, 1312, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2499, 'Keine Änderung', '2024-08-26 08:13:52.287441', '', '2024-08-26 08:13:52.287443', NULL, 3, 1322, 5, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2512, 'Meeting findet im September statt', '2024-08-26 08:41:46.315348', '', '2024-08-26 08:41:46.315351', NULL, 3, 1317, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2403, 'Ein super Kandidat. Warten auf seine Antwort.', '2024-08-12 08:50:29.023466', '', '2024-08-26 13:31:36.789084', NULL, 32, 1255, 5, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2516, 'Kurz vor Vertragsabschluss bei der BFH =)
Lead bei Fischer Spindle Group, Buchzentrum ongoing, SmartIT aktuell noch keine Bewegung', '2024-08-26 14:44:23.492093', '', '2024-08-26 14:44:23.492097', NULL, 34, 1258, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2517, 'Bis hierher keine weiteren Aktivitäten', '2024-08-26 14:45:01.269211', '', '2024-08-26 14:45:01.269214', NULL, 34, 1256, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2518, 'Status Quo bis am 10. September,', '2024-08-27 06:45:56.635589', '', '2024-08-27 06:45:56.635605', NULL, 49, 1279, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (442, 'Keine', '2023-10-09 20:27:56.242529', 'Aktivitäten gemäss Beschreibung', '2023-10-08 22:00:00', 0, 4, 205, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2524, 'Draft für Vereinbarung an Kanton versendet', '2024-08-27 13:03:41.695032', '', '2024-08-27 13:03:41.695047', NULL, 5, 1276, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2526, '', '2024-08-27 13:05:37.973345', '', '2024-08-27 13:05:37.973362', NULL, 5, 1267, 8, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2527, 'Blogposts und Referenzberichte vorhanden.', '2024-08-27 14:50:05.11535', '', '2024-08-27 14:50:05.115367', 5, 40, 1343, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2528, 'Grobe Definition der Pfeiler in Arbeit', '2024-08-27 14:50:32.596826', '', '2024-08-27 14:50:32.596842', NULL, 31, 1326, 4, 'ordinal', 'FAIL', 2);
INSERT INTO okr_pitc.check_in VALUES (2529, 'Kickoff hat stattgefunden aber nicht so viel Progress wie erhofft', '2024-08-27 14:51:10.068623', '', '2024-08-27 14:51:10.068641', 0, 40, 1344, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2530, 'Keine Veränderungen (Diverse Deals noch offen)', '2024-08-27 14:51:53.300513', '', '2024-08-27 14:51:53.300529', 738816, 40, 1328, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2532, 'Juli 2024: 17,548', '2024-08-27 14:55:01.897855', '', '2024-08-27 14:55:19.648057', NULL, 40, 1346, 8, 'ordinal', 'STRETCH', 2);
INSERT INTO okr_pitc.check_in VALUES (2533, 'Unverändert', '2024-08-27 14:55:55.972979', '', '2024-08-27 14:55:55.972996', 8.1, 40, 1331, 8, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2534, 'Neben Gaia konnten zusätzliche Aufträge generiert werden: AVEG mit Zeilenwerk, Peerdom, BKD Weiterentwicklungen
Ausgelastet sind: Claudia, Max, Hupf, Lukas, Jan, Dani, Mätthu
Fragezeichen bei: Mehmet, Clara, Beri, Girod', '2024-08-31 10:43:23.073685', 'Klären ob Riedo kommt (Girod), Klären ob Kanton Aargau ab Januar 2025 eingeplant werden kann, Lösung für GAIA (Mobi) ab Januar finden', '2024-08-31 10:43:23.073691', NULL, 4, 1296, 0, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2538, 'Status Quo', '2024-08-31 10:58:30.922034', '', '2024-08-31 10:58:30.922041', NULL, 4, 1292, 0, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2536, 'Status Quo', '2024-08-31 10:44:47.374256', 'Weiterarbeit mit Analyse, Abgleiche, Diskussionen', '2024-08-31 10:45:21.158895', NULL, 4, 1294, 0, 'ordinal', 'COMMIT', 3);
INSERT INTO okr_pitc.check_in VALUES (1464, 'Keine neuen Arbeiten, warten auf Zeit um die bestehenden Inputs umzusetzen', '2024-02-26 12:02:22.325492', '', '2024-02-26 12:02:22.325494', 25, 32, 1038, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1799, 'Keine Kapa', '2024-04-29 10:32:18.939259', '', '2024-04-29 10:32:18.939265', NULL, 28, 1231, 2, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1802, 'Keine Kapa für dieses wichtige Vorhaben', '2024-04-29 10:33:38.665098', '', '2024-04-29 10:33:38.665104', NULL, 28, 1167, 2, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1981, 'Markom & Sales aus mobility Sicht Pensen und Zuständigkeiten geklärt', '2024-05-27 07:18:46.860535', '', '2024-05-27 07:18:46.860538', NULL, 20, 1142, 4, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2146, 'keine Veränderung', '2024-06-17 15:31:35.788883', '', '2024-06-17 15:31:55.221633', NULL, 36, 1220, 0, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2266, '', '2024-07-15 08:08:13.32055', '', '2024-07-15 08:08:13.320553', 83001, 36, 1312, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2281, 'Wir verfolgen dies weiter im Juli und halten die Botschafter:innen und allfällige Themen/Posts fest.', '2024-07-16 05:25:41.96709', '', '2024-07-16 05:25:41.967092', 2, 49, 1318, 8, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2340, 'keine Veränderung seit letzter Woche', '2024-07-29 07:56:26.546517', '', '2024-07-29 07:56:26.546523', 0, 36, 1314, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2407, 'Marktvalidierung für diese Woche eingeplant und Termin mit CSO und CTO eingeplant', '2024-08-12 08:54:39.758583', '', '2024-08-12 08:54:39.758587', NULL, 5, 1263, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2432, 'Es fand ein Austausch statt und Transparenz ist geschaffen, was /mobility Entwickler für Know-How und Skills haben im Thema. Aber neue oder greifbare Kooperationspotentiale sind nicht gefunden', '2024-08-15 07:45:33.385245', '', '2024-08-15 07:45:33.385248', 0, 5, 1275, 1, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2451, 'Typografie-Artikel ist erfasst und auf Linkedin geteilt. ', '2024-08-19 11:10:49.45825', '', '2024-08-19 11:10:49.458254', 6, 3, 1315, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2472, 'Der Fokus hat sich leicht verlagert, obwohl weiter am Training gearbeitet wird.
Neu wird der EGI Showcase aktiv weiterentwickelt und da etwas mehr Zeit investiert. ', '2024-08-22 06:30:07.093561', '', '2024-08-22 07:41:09.372001', NULL, 24, 1243, 7, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2483, 'Bestanden und nicht mal eine Abweichung ', '2024-08-22 08:16:38.231283', '', '2024-08-22 08:16:38.231285', NULL, 41, 1274, 7, 'ordinal', 'STRETCH', 0);
INSERT INTO okr_pitc.check_in VALUES (2490, 'Besprechung im Rahmen der ZG geplant', '2024-08-22 11:42:28.033986', 'Ab Mitte September (nach ZG) Konsolidierung der Inputs und Vorbereitung eines Zielbildes.', '2024-08-22 11:42:28.033989', NULL, 33, 1309, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2496, 'Vertrag mit Joël unterzeichnet.', '2024-08-22 15:08:43.475701', '', '2024-08-22 15:08:43.475704', 0.1, 20, 1259, 3, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (2500, 'Aktuell immer noch Fail - Workshop / gemeinsames Statement nächste Woche geplant. ', '2024-08-26 08:17:48.984292', '', '2024-08-26 08:17:48.984295', NULL, 3, 1324, 5, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2513, 'Keine Änderung', '2024-08-26 10:57:33.880047', '', '2024-08-26 10:57:33.880049', NULL, 3, 1319, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2514, 'Eintrag Pro Member fehlt', '2024-08-26 10:57:59.035342', 'Awareness schaffen für Einträge', '2024-08-26 10:57:59.035345', NULL, 3, 1321, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2519, 'Score weiterhin bei 83. Bis Ende August versuchen wir das SEO Issue duplicated Metadescription zu beheben und gitlab.puzzle.ch aus dem Index zu nehmen. ', '2024-08-27 07:01:24.957412', '', '2024-08-27 07:01:24.957426', NULL, 49, 1281, 9, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2520, 'Letzte Woche gab es mehrere Posts von Members zum Rails Höck und Cloud Native Computing Meetup. Somit haben wir unser Stretch Goal erreicht und das Thema hat Fahrt aufgenommen.', '2024-08-27 07:11:35.82344', '', '2024-08-27 07:11:35.823484', 12, 49, 1318, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2531, 'Aktuell keine Offerte offen', '2024-08-27 14:52:19.984229', '', '2024-08-27 14:52:19.984247', NULL, 40, 1329, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2535, 'Status Quo', '2024-08-31 10:44:05.015699', '', '2024-08-31 10:44:05.015707', NULL, 4, 1295, 0, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2556, '', '2024-09-02 12:38:21.969377', '', '2024-09-02 12:38:21.969384', NULL, 27, 1334, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2558, '', '2024-09-02 12:54:07.330308', '', '2024-09-02 12:54:07.330315', NULL, 5, 1276, 6, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2539, 'Durch Eigeninitiative von Mehmet fand gemeinsames Kochen/ Mittagessen "Sommerrollen" statt - Plan ist noch nicht vorhanden, d.h. nur Commit', '2024-08-31 11:30:03.542819', 'Plan erstellen', '2024-08-31 11:40:49.264947', NULL, 4, 1288, 10, 'ordinal', 'COMMIT', 5);
INSERT INTO okr_pitc.check_in VALUES (2543, 'keine Veränderung', '2024-09-02 09:03:34.62109', '', '2024-09-02 09:03:34.621102', NULL, 36, 1322, 5, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2545, 'keine Verändeurng', '2024-09-02 09:05:01.294895', '', '2024-09-02 09:05:01.294903', 6, 36, 1315, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2547, 'Noch keine Änderung, Diskussion Erkenntnisse nächste Woche Di', '2024-09-02 12:19:16.03021', '', '2024-09-02 12:19:16.030217', NULL, 3, 1316, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2551, 'Wir sind soweit, dass wir eine Staging-Umgebung haben. Noch können wir die Migration nicht beginnen, aber wir sind gut unterwegs.', '2024-09-02 12:37:33.469882', '', '2024-09-02 12:37:33.469893', NULL, 27, 1341, 7, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2554, '', '2024-09-02 12:38:01.825299', '', '2024-09-02 12:38:01.825307', NULL, 27, 1336, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2559, '', '2024-09-02 12:54:47.31149', '', '2024-09-02 12:54:47.311498', 0, 5, 1277, 0, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2560, '', '2024-09-02 12:55:20.386919', '', '2024-09-02 12:55:20.386927', NULL, 5, 1267, 8, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2562, 'Score bei 83, WLY prüft bei Google Console ob sie das beeinflussen können mit den gitlab.puzzle.ch oder wir das intern lösen müssen. ', '2024-09-03 06:40:24.179533', '', '2024-09-03 07:23:56.080247', NULL, 49, 1281, 9, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2565, '', '2024-09-03 14:26:43.900101', '', '2024-09-03 14:26:43.900107', NULL, 5, 1267, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2568, '', '2024-09-05 07:53:21.010382', '', '2024-09-05 07:53:21.010386', 3226915, 13, 1241, 2, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2570, 'Spannende Bewerbungen in Pipeline.', '2024-09-05 08:05:30.63805', '', '2024-09-05 08:05:30.638054', 0.1, 20, 1259, 4, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (2572, '', '2024-09-05 14:48:29.084869', 'GtM kann noch abgeschlossen werden. Rest ist unwahrscheinlich oder wird nicht mehr weiterverfolgt (PE Chapter Schweiz)', '2024-09-05 14:48:29.084874', 5, 31, 1343, 3, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2573, '', '2024-09-05 14:49:05.667747', 'Keine Veränderung.', '2024-09-05 14:49:05.667752', 0, 31, 1344, 3, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2574, '', '2024-09-05 14:49:49.372828', 'Commit können wir erreichen. Rest wird sehr schwierig.', '2024-09-05 14:49:49.372833', NULL, 31, 1326, 2, 'ordinal', 'FAIL', 2);
INSERT INTO okr_pitc.check_in VALUES (74, 'Mir sind noch keine Auswertung bekannt.', '2023-07-09 06:06:40.661812', '', '2023-07-08 22:00:00', 0, 13, 41, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (75, 'Bisher noch keine Blogposts zu Kubermatic, ML Themen und Cilium. Video zu Dagger erschienen.', '2023-07-10 05:47:46.120715', '', '2023-07-09 22:00:00', 0, 24, 49, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (76, 'Wir sind auf Kurs. Erste News zu Events (Puzzle Workshop) erschienen', '2023-07-10 05:49:20.120696', '', '2023-07-09 22:00:00', 0.1, 24, 40, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (274, 'Inputs aus Durchsprache im Team konsolidiert.', '2023-09-06 05:57:19.014945', 'Finale Durchsprache am 13.9.', '2023-09-05 22:00:00', 0.65, 5, 36, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (78, 'Erste Messung im neuen Jahr. Erster RH Subs Deal bereits abgeschlossen (Renewal). Cililum Subs für Post in Aussicht (Offeriert, TCHF 250, 10%=25 k). ', '2023-07-10 07:00:59.447002', '', '2023-07-09 22:00:00', 5000, 16, 51, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (79, 'In Ausarbeitung', '2023-07-10 07:41:15.810373', '', '2023-07-09 22:00:00', 0, 22, 46, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (96, 'Onboarding BFH in progress, Vertrag unterschrieben, erste verrechenbare Stunden kommen im Juli. Migration ITpoint bereits voll im Gang.', '2023-07-17 15:22:32.148649', '', '2023-07-16 22:00:00', 0.7, 34, 60, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (97, 'Stimmung bei /sys Members wird im Rahmen der Membersgespräche abgeholt. Beitrag zum Architekturentscheid im Brixel Projekt.', '2023-07-17 15:23:33.818885', '', '2023-07-16 22:00:00', 0.3, 34, 63, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (98, 'Noch keine Aktion in diesem Bereich', '2023-07-17 15:23:49.533411', '', '2023-07-16 22:00:00', 0, 34, 62, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (94, '-', '2023-07-17 15:24:11.826505', '', '2023-07-16 22:00:00', 177, 34, 54, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (80, '76 Prozent', '2023-07-10 09:34:36.610649', '', '2023-07-09 22:00:00', 0.3, 31, 81, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (82, 'Ticket Hitobito https://github.com/hitobito/hitobito/issues/1914 PSI und MVI sind dran
Ptime noch offen https://github.com/puzzle/puzzletime/pull/187', '2023-07-11 12:28:20.046791', 'Planung von Ptime vornehmen', '2023-07-10 22:00:00', 0.2, 28, 100, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (83, 'Noch keine Aktivitäten', '2023-07-11 12:28:52.181814', '', '2023-07-10 22:00:00', 0, 28, 98, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (84, 'Monat Mai 70,5534841432695%', '2023-07-11 12:31:44.940869', '', '2023-07-10 22:00:00', 0.3, 28, 102, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (85, 'SAC Zusage', '2023-07-11 12:32:23.762559', 'Fixieren von Brixel und BAFU', '2023-07-10 22:00:00', 0.3, 28, 103, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (86, 'Planung vorgenommen', '2023-07-11 12:34:10.983438', '', '2023-07-10 22:00:00', 11, 28, 101, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (87, 'Einige Bewerbungen eingegangen', '2023-07-11 12:35:17.462833', 'Netzwerk noch besser nutzen', '2023-07-10 22:00:00', 0.2, 28, 91, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (88, 'Umfrage noch mit bestehendem Tool gemacht', '2023-07-11 12:35:48.202605', '', '2023-07-10 22:00:00', 0.1, 28, 97, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (89, '0', '2023-07-12 13:10:40.859889', '', '2023-07-11 22:00:00', 0, 31, 104, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (90, '0', '2023-07-12 13:11:24.906084', '', '2023-07-11 22:00:00', 0, 31, 105, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (91, '0', '2023-07-12 13:11:35.251539', '', '2023-07-11 22:00:00', 0, 31, 79, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (92, '0', '2023-07-12 13:11:44.571004', '', '2023-07-11 22:00:00', 0, 31, 76, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (69, 'Erste Inputs von Team erhalten (Teammeeting Juli)', '2023-08-04 15:06:12.18436', '', '2023-07-04 22:00:00', 0.15, 31, 85, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (95, 'Stimmung im Team wird in Membersgesprächen abgeholt. Infos sammeln für Ops Angebot. Abstimmung mit Ops Team zu Zusammenarbeit mit VSHN erfolgt.', '2023-07-17 15:21:24.913542', '', '2023-07-16 22:00:00', 0.3, 34, 61, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (166, 'Keine Veränderung', '2023-08-11 13:37:33.323881', '', '2023-08-10 22:00:00', 11, 28, 101, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (99, '0', '2023-07-17 15:59:57.416401', 'Aktuell eine spannende Bewerbung offem, nächste Woche 2. Gespräch geplant.', '2023-07-16 22:00:00', 0, 20, 35, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (100, '0', '2023-07-17 16:05:38.319316', 'Members am Mobility Meetup zur Mitwirkung aufgefordert und Verfügbarkeiten abgeklärt.', '2023-07-16 22:00:00', 0, 20, 38, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (101, '0', '2023-07-17 16:29:02.729704', 'Organisation der einzelnen Events ist den Subteams übertragen und erste Umsetzungsideen sind vorhanden.
Teams und "Spielplan" für Mobility Töggeliturnier sind gemacht.', '2023-07-16 22:00:00', 0, 20, 57, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (102, 'Technologien MLflow und Kubeflow ausprobiert.Können nicht 1:1 verglichen werden. Für das Lab werden wir nun aber CML und DVC einsetzten. Erster Draft der Lab Struktur und Planung steht. Review der Arbeiten mit msc und rh erfolgt.', '2023-07-18 07:56:21.157844', '', '2023-07-17 22:00:00', 0.2, 30, 59, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (103, 'Technologien aktuell wie Lab (CML und DVC). Aktuelle Angebotsideen
- MLOpsLab
- Integration von ChatGPT bei Firmen / Suche über Unternehmensdaten mittels LLM (z.B. PrivateGPT)
- MLOps Beratung', '2023-07-18 08:03:28.41304', '', '2023-07-17 22:00:00', 0.3, 30, 58, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (104, 'Das Board ist erstellt und mit Sales besprochen. Es gibt noch einen Input betreffend der Events und dann können wir es dem bl-weekly präsentieren. ', '2023-07-18 15:01:04.037399', '', '2023-07-17 22:00:00', 0.8, 26, 117, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (105, 'weekly news neu jeweils mit Events, aufgrund der Sommerpause ist es etwas ruhiger', '2023-07-18 15:02:10.781888', '', '2023-07-17 22:00:00', 2, 26, 114, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (106, 'Post mit neuem Teambild und Hink zu unserer Kultur und den offenen Stellen', '2023-07-18 15:02:59.58015', '', '2023-07-17 22:00:00', 2, 26, 115, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (66, 'BBT Team noch nicht bei uns. Offizielle Zahl von Marcel', '2023-07-27 11:39:02.9258', '', '2023-07-26 22:00:00', 17021, 5, 32, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (72, 'Eintritt Mirjam Thomet und kleine Pensumanpassungen.
', '2023-07-31 04:41:31.538874', '', '2023-07-08 22:00:00', 0.85, 13, 43, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (68, 'Erste Happiness-Umfrage ausgewertet, zweite HU durchgeführt.', '2023-08-04 15:07:53.902649', '', '2023-07-04 22:00:00', 0.2, 31, 93, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (81, '68 Prozent', '2023-08-21 11:37:24.323393', '', '2023-07-10 22:00:00', 0, 31, 82, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (229, 'End of Summerparty ZH', '2023-08-25 07:27:36.889448', '', '2023-08-24 22:00:00', 3, 26, 115, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (362, 'Family Social Anlass', '2023-09-18 11:02:14.476301', '', '2023-09-17 22:00:00', 6, 26, 114, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (107, 'Julian kommt nächste Woche dazu, die Änderungen vorzunehmen. Anschliessend versuchen wir mit einer Linkedin Kampagne, etwas mehr Abonennten zu genierieren. ', '2023-07-18 15:04:28.896705', '', '2023-07-17 22:00:00', 0.5, 26, 113, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (109, '-3.2 Prozentpunkte gegenüber Vormonat', '2023-07-22 05:09:31.316639', '', '2023-07-21 22:00:00', 50.2, 22, 44, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (110, 'Monatsabschluss Juni. Prognose für Juli ähnlich. Ausblick Aug+Sept noch mit Unsicherheiten behaftet.', '2023-07-24 11:22:13.147836', 'Sales Kampagne', '2023-07-23 22:00:00', 75.6, 33, 99, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (111, 'Einvernehmliche Lösung mit FZAG zu Ablösung von Tiago gefunden. Gleichzeitig müssen wir bei SNB temporäre Reduktion der Velocity in Kauf nehmen. Dies ist allerdings ein Budgetthema, das nicht direkt mit Pensumsreduktion von Tiago in Verbindung steht.', '2023-07-24 11:28:57.986878', 'Vorgehen von SNB definitiv bestätigen lassen.', '2023-07-23 22:00:00', 0.4, 33, 80, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (146, 'leider tiefe abs. Verrechenbarkeit im Juni wegen Ferienabwesenheiten und Krankheit', '2023-07-31 07:12:10.906762', 'wir geben Gas im Juli und August', '2023-07-30 22:00:00', 0, 36, 71, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (388, 'keine Veränderung weil wir es erledigt haben', '2023-09-19 06:31:05.613322', 'keine', '2023-09-18 22:00:00', 1, 36, 67, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (112, 'Mündliche Zusage von Daniel Alder (70% Pensum). Er kann als Dev und Sys-ler eingesetzt werden und könnte die Pensumsreduktionen von Rémy und Tiago weitgehend kompensieren. Wert wird erst angepasst, wenn Vertrag unterschrieben vorliegt.', '2023-07-24 11:32:40.671967', 'Vertrag mit Dani Alder ins Trockene bringen. Müsste bis Ende Juli möglich sein (mündl. zugesagt)', '2023-07-23 22:00:00', -85, 33, 77, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (113, 'Immer noch Unsicherheit, ob Yelan überhaupt starten kann (Arbeitsbewilligung). Falls ja, haben wir mit Mobi Gaia Team einen vielversprechenden Ansatz mit Päscu Hurni vorbesprochen (Pairing mit David S., Einarbeitung Yelan gratis, dann verrechenbar).', '2023-07-24 11:37:57.744395', 'Arbeitsbewilligung für Yelan erhalten (Verfahren immer noch hängig; Ausgang ungewiss). Wenn Ausgang positiv, dann Ausgangslage bei Mobi nutzen.', '2023-07-23 22:00:00', 0.2, 33, 83, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (114, 'Bisher noch keine Aufträge. Vielversprechende Deals: Vontobel Volt, Peerdom DB, HRM DB Consulting', '2023-07-24 11:41:54.179135', 'Dran bleiben mit hoher Prio. Wird während meinen Ferien durch Remo weitergetrieben (ich würde bei Fragen zur Verfügung stehen).', '2023-07-23 22:00:00', 0.05, 33, 70, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (115, 'Situation für David Simmen entschärft mit Mobi Gluon 2 Migration, die seinen Vorstellungen entspricht. Carlo mit SAC-Zusage ebenfalls gut mit spannender Büez eingedeckt.', '2023-07-24 11:47:06.348672', 'Verbleibende Challenges: Andres (Varianten: Vontobel, PostAuto) und, falls er definitiv zusagt, ab Sept. Dani A. (BJUB, SAC, Swissmedic, Vontobel).', '2023-07-23 22:00:00', 6, 33, 72, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (116, 'Zweite Messung gegen Ende Juli. Cilium Subs Deal für Post ist gekommen. Deal Flughafen ZH in Ausssicht.', '2023-07-24 12:04:06.940739', '', '2023-07-23 22:00:00', 28600, 16, 51, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (117, 'Wir liegen 80% über dem Vorjahr in der selben Zeiteinheit (Juni 2022; CHF 197''678)', '2023-07-27 06:31:23.894166', '', '2023-07-26 22:00:00', 357716, 41, 120, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (119, 'Von Budget von CHF 111 800 wurde bereits 56 610 benutzt ', '2023-07-27 07:37:35.860928', '', '2023-07-26 22:00:00', 55190, 41, 122, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (120, 'Letztjährige Membersumfrage ausgewertet und Ansatzpunkte für Massnahmenpaket identifiziert. Bei Team Zoo neue Membersumfrage eingeführt', '2023-07-27 11:11:09.869964', '', '2023-07-26 22:00:00', 0.2, 5, 36, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (121, 'Da Jölu in den Ferien immer noch Stand wie beim letzten Checkin.', '2023-07-27 11:14:26.078392', '', '2023-07-26 22:00:00', 0, 20, 38, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (122, 'Da Jölu in den Ferien immer noch Stand wie beim letzten Checkin.', '2023-07-27 11:15:06.108113', '', '2023-07-26 22:00:00', 0, 20, 57, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (123, 'Hitobito: PR vorhanden, läuft auf Ruby 3.0 https://github.com/hitobito/hitobito/issues/1914 PSI und MVI sind dran
Ptime geplant: https://github.com/puzzle/puzzletime/pull/187 ', '2023-07-27 11:15:31.267352', 'Hitobito: Release vorbereiten
Ptime: Planung einhalten', '2023-07-26 22:00:00', 0.5, 28, 100, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (124, 'Termin 8.9.23 mit MVI geplant', '2023-07-27 11:16:30.657474', '', '2023-07-26 22:00:00', 0.1, 28, 98, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (125, 'Planung läuft', '2023-07-27 11:17:20.568529', '', '2023-07-26 22:00:00', 11, 28, 101, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (127, 'BAFU Prov. geplant. Vertraglich noch nicht geregelt; Brixel läuft bis ca Ende September mit bestehendem Vertrag', '2023-07-27 11:18:20.756479', '', '2023-07-26 22:00:00', 0.4, 28, 103, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (128, 'Zwei Kennenlerngespräche waren nicht viel versprechend. Ein Gespräch noch offen', '2023-07-27 11:19:43.12026', 'Weiter und noch mehr unser Inserat bewerben und streuen..', '2023-07-26 22:00:00', 0.1, 28, 91, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (129, 'Erste Ideen für Massnahmen generiert und mit mga und sfe gechallenged ergänzt.', '2023-07-27 11:20:08.214817', 'Massnahmen gilt es vertieft zu prüfen, zu priorisieren und auszuarbeiten', '2023-07-26 22:00:00', 0.2, 5, 119, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (130, 'nichts', '2023-07-27 11:20:24.960261', 'Neue Umfrage für nächstes Teammeeting vorbereiten', '2023-07-26 22:00:00', 0.1, 28, 97, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (65, 'Klar über Zielwert. Aber noch ohne BBT Team. Offizieller Wert von Marcel', '2023-07-27 11:23:47.158342', '', '2023-07-26 22:00:00', 75, 5, 33, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (133, 'Keine Veränderung', '2023-07-31 04:37:55.882196', '', '2023-07-30 22:00:00', 1.4, 13, 45, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (126, 'Super Kandidat hat für ein 80% Pensum zugesagt (aber noch nicht unterschrieben). Simu hat gekündigt (60%) Pensum', '2023-07-27 11:51:37.9145', '', '2023-07-26 22:00:00', 0.2, 20, 35, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (131, 'Die geplanten Arbeiten brauchen mehr Zeit als angenommen. Ein erster interner Testrun mit Adrian Bader und Khoi Tran sowie allenfalls einem nicht technischen Vertreter findet am 15.8 statt.', '2023-07-27 15:58:02.525048', '', '2023-07-26 22:00:00', 0.25, 30, 59, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (132, 'Angebot für MLOpsLab und MLOps Consulting in Erarbeitung. Für das MLOpsLab werden wir sofern wir Ressourcen von AWS benötigen ein Sandbox Funding beantragen. Dieses soll die Kosten für die AWS Ressourcen finanzieren.', '2023-07-27 16:05:23.999901', '', '2023-07-26 22:00:00', 0.4, 30, 58, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (134, 'Festanstellung Paco und Florian, Austritt Aaron und Dominic, div. Pensumanpassungen', '2023-07-31 04:40:22.296079', '', '2023-07-30 22:00:00', -0.35, 13, 43, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (136, 'Interviewfragen stehen bereit. Erste Termine mit RTEs sind vereinbart. ', '2023-07-31 06:59:20.772247', '', '2023-07-30 22:00:00', 0.2, 29, 37, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (135, 'DesignOps Worskhop durchgeführt und PainPoints aufgenommen', '2023-07-31 07:00:58.808657', 'konkrete Massnahmen im Team besprechen', '2023-07-30 22:00:00', 0.3, 3, 69, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (137, 'Strategie Draft wurde mit Stakeholdern diskutiert und nochmals von Simu und Berti überarbeitet', '2023-07-31 07:02:50.571158', 'finalisierte Fassung erstellen', '2023-07-30 22:00:00', 0.3, 3, 65, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (140, 'Definitin WAC Teamkultur ist erfolgt und eine Massnahme wurde definiert und umgesetzt. Eine weitere wurde geplant', '2023-07-31 07:05:49.890689', 'zweite Massnahme umsetzen', '2023-07-30 22:00:00', 0.7, 36, 67, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (141, 'Arbeit am Ops Angebot hat begonnen. Noch keine Abstimmung mit anderen Bereichen. Muss noch intensiviert werden.', '2023-07-31 07:06:18.671595', '', '2023-07-30 22:00:00', 0.3, 34, 61, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (142, 'Erste ITpoint Migrationen erfolgreich, SLA Verhandlungen aufgenommen. BFH Onboarding rollt an', '2023-07-31 07:06:55.350442', '', '2023-07-30 22:00:00', 0.7, 34, 60, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (143, 'aufgrund der Veränderung im Modus unseres Team Meetings, ist noch nichts passiert', '2023-07-31 07:08:17.518371', 'Happiness Umfrage im neuen Modus etablieren', '2023-07-30 22:00:00', 0, 36, 68, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (139, '4', '2023-08-28 10:01:45.426862', '', '2023-07-30 22:00:00', 177, 34, 54, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (138, '-', '2023-07-31 15:08:43.907911', '', '2023-07-30 22:00:00', 34, 34, 53, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (144, 'Gefühl aus Membersgesprächen: Thematik wird grundsätzlich positiv, aber mit wenig Potential eingeschätzt. Interesse der Members liegt eher auf Technologien als auf den grossen Providern mit proprietären Technologien. ', '2023-07-31 07:10:46.325486', '', '2023-07-30 22:00:00', 0.3, 34, 63, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (145, 'Noch keine Aktion in diesem Bereich', '2023-07-31 07:11:10.299176', '', '2023-07-30 22:00:00', 0, 34, 62, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (147, 'wir haben alle Members halten können', '2023-07-31 07:13:35.271844', 'Auslastung im Herbst von allen Members und neuen Members sicherstellen', '2023-07-30 22:00:00', 0.3, 36, 78, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (148, 'keine Veränderung', '2023-07-31 07:16:20.786844', 'Sales, Sales, Sales', '2023-07-30 22:00:00', 0, 36, 73, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (275, 'Keine Veränderung seit letzter Messung', '2023-09-06 05:58:57.015258', '', '2023-09-05 22:00:00', 0.6, 5, 119, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (149, 'Eingeführt bei /sys, /ux, /mid, /mobility. Auswertung und Zielwerte noch nicht bekannt.', '2023-07-31 11:29:04.558434', '', '2023-07-30 22:00:00', 0.2, 13, 41, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (150, 'Die weekly news machen gerade Sommerpause.', '2023-08-02 12:11:20.687644', 'Für das Züri Fübi & Montagsmaler nächsten Dienstag, habe ich Lukas Koller angefragt. Am #wecare  Schwumm nimmt unser Mats teil und eine Woche später ist das Aarebötle und der MarCom-Sales Teamevent, wo sich auch Gelegenheit bietet, etwas darüber zu erzählen. ', '2023-08-01 22:00:00', 4, 26, 114, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (153, 'Dani Tschan und Mätthu Liechti in /dev/tre integriert', '2023-08-03 11:58:32.178331', 'N/A', '2023-08-02 22:00:00', 0.3, 4, 92, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (154, 'N/A', '2023-08-03 11:59:43.210707', 'Aktivitäten gemäss Key Results', '2023-08-02 22:00:00', 0, 4, 89, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (151, 'N/A', '2023-08-03 12:00:11.840016', 'Aktivitäten gemäss Key Results', '2023-08-02 22:00:00', 0, 4, 94, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (152, 'N/A', '2023-08-03 12:00:22.388779', 'Aktivitäten gemäss Key Results', '2023-08-02 22:00:00', 0, 4, 86, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (155, 'N/A', '2023-08-03 12:00:48.489513', 'Aktivitäten gemäss Key Results', '2023-08-02 22:00:00', 0, 4, 87, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (156, 'Vertrag mit Simon Bühlmann abgeschlossen. Start als Software Engineer S4 zu 80% am 1. September 2023', '2023-08-03 12:02:38.982369', 'Keine weiteren Aktivitäten', '2023-08-02 22:00:00', 1, 4, 96, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (157, 'Wert Juni 2023 - + 3.9% im Vormonat, dennoch tiefer als Zielwert', '2023-08-03 12:08:41.199292', 'N/A', '2023-08-02 22:00:00', 72.1, 4, 74, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (179, 'Admin Team beauftragt für die Berechnung der Baseline (pro Senioritätsstufe). Massnahmepaket konsolidiert', '2023-08-16 14:19:57.439952', 'Einzelne Massnahmen werden noch weiter ausgearbeitet/geprüft.', '2023-08-15 22:00:00', 0.25, 5, 119, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (158, 'Inputs im nächsten August-Teammeeting umgesetzt', '2023-08-04 15:07:11.409739', '', '2023-08-03 22:00:00', 0.4, 31, 85, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (159, 'Dagger Kundenmapping durchgeführt und potenzial bewertet. Zusätzlich haben wir ein Fragenkatalog für die kommenden Gespräche mit potenziellen Kunden erstellt.', '2023-08-09 14:47:24.030512', '', '2023-08-08 22:00:00', 0.3, 31, 76, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (160, 'Erste Messung sämtlicher Grundpauschalen Operations der Divisions (Monat Juni 2023).', '2023-08-11 07:11:38.079814', '', '2023-08-10 22:00:00', 81481, 16, 50, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (161, 'RH Subs Deals bei Flughafen ZH und aity offeriert.', '2023-08-11 07:16:56.540463', '', '2023-08-10 22:00:00', 37800, 16, 51, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (163, 'kurzfristig nur 1.0 FTE, per nächsten Monat jedoch total 3 FTE (Anstellung Yelan und 2*0.5 freie Sys-Engineers in /zh)', '2023-08-11 12:12:12.78986', '', '2023-08-10 22:00:00', 1, 13, 45, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (164, 'Arbeiten am PTime laufen; Hitobito PR vorhanden', '2023-08-11 13:36:02.995093', '', '2023-08-10 22:00:00', 0.6, 28, 100, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (167, 'BAFU geplant, Bestellung noch ausstehend. Brixel läuft bis ca Ende September mit bestehendem Vertrag ', '2023-08-11 13:38:19.758966', '', '2023-08-10 22:00:00', 0.5, 28, 103, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (168, 'Aktuelle Zahl per 11.08.23 ist 63% (Monatsabschluss noch ausstehend)', '2023-08-11 13:39:33.347861', '', '2023-08-10 22:00:00', 0.7, 28, 102, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (169, 'Zwei Kandiaten für ein Techinterview', '2023-08-11 13:42:35.751886', '', '2023-08-10 22:00:00', 0.2, 28, 91, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (170, 'Happyness Umfrage am Teammeeting vom 10.8.23 erstmalig durchgeführt', '2023-08-11 13:43:24.943853', 'Auswertung und nächste Schritte durchführen', '2023-08-10 22:00:00', 0.3, 28, 97, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (171, 'Keine Veränderung, da keine Events', '2023-08-14 07:13:07.316482', '', '2023-08-13 22:00:00', 0.1, 24, 40, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (172, 'Bisher noch keine Blogposts zu Kubermatic, ML Themen und Cilium. Video zu Dagger erschienen.', '2023-08-14 07:14:25.016971', 'Auftrags-Reminder an Teams wurde versendet via Chat', '2023-08-13 22:00:00', 0.1, 24, 49, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (173, 'Draft des Ops Angebot von /sys ist erstellt. Austausch mit Tim hat stattgefunden. Weitere Arbeit am Angebot bis Ende August geplant.', '2023-08-14 12:55:10.80053', '', '2023-08-13 22:00:00', 0.3, 34, 61, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (174, 'ITpoint Migration bis Ende Monat abgeschlossen. Supportvertrag in Verhandlung.', '2023-08-14 12:55:58.767158', '', '2023-08-13 22:00:00', 0.7, 34, 60, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (175, 'Weitere Diskussionen mit Team und im /sys Lead. Noch zu wenig konkrete Projekte in der Pipeline um hier aktiv mit Knowhow zu beginnen.', '2023-08-14 12:57:12.031258', '', '2023-08-13 22:00:00', 0.3, 34, 63, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (176, 'Leider immer noch keine Aktion in diesem Bereich =(', '2023-08-14 12:57:33.253247', '', '2023-08-13 22:00:00', 0, 34, 62, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (177, 'Interview-Termine mit allen RTE stehen. Viedos werden wahrscheinlich in einem verpackt. --> Mehrere Erfolgsstories in einem verpackt', '2023-08-16 10:45:18.61138', 'Termine mit Kontakten ausserhalb der SBB vereinbaren', '2023-08-15 22:00:00', 0.25, 29, 37, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (178, 'Happiness Umfrage in allen /mobility Teams eingeführt. ', '2023-08-16 14:17:37.569162', 'Auswertung für die Baseline Bestimmung erfolgt Ende August und ist eingeplant.', '2023-08-15 22:00:00', 0.25, 5, 36, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (227, 'Alle Quartalsplanungen haben stattgefunden und die neuen Termine sind vereinbart fürs nächste Quartal', '2023-08-25 07:25:09.260022', '', '2023-08-24 22:00:00', 1, 26, 116, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (182, 'Mobility Event für September organisiert, Töggeliturnier initiert.', '2023-08-16 16:00:01.473138', '', '2023-08-15 22:00:00', 0.2, 20, 57, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (162, 'Umfrage durchgeführt, Auswertung vorgenommen, die richtigen Schlüsse müssen noch gezogen werden. Kick-Off mit Marcom, Sales und weiteren Division in Planung. Prüfung Peerdom als Kunde für GCP Account. Nächster Touchpoint m. GCP Ende Sept. vorgesehen.', '2023-08-17 11:27:31.031067', 'Da die Partnerschaft mit GCP wegen fehlender Zertifizierungen blockiert ist, müssen zeitnah die finanziellen und zeitl. Mittel für Members-Zertifizierungen geklärt werden.', '2023-08-10 22:00:00', 0.1, 16, 48, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (118, 'Die Auftragsverrechenbarkeit war im Juli sehr erfreulich', '2023-08-21 10:54:16.585038', '', '2023-08-16 22:00:00', 99, 41, 121, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (230, 'Videokonzept ist bei der GL fürs Review und wird anschliessend publiziert', '2023-08-25 07:30:23.407048', '', '2023-08-24 22:00:00', 1, 26, 110, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (231, 'Weiterhin keine Messung - unser Team-Meeting findet nur noch alle 6 Wochen statt', '2023-08-25 08:24:23.026984', 'Alternative definieren - evtl. im Bila', '2023-08-24 22:00:00', 0, 36, 68, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (272, 'Leider ein Rückzug von einem Kandiaten', '2023-09-04 08:42:58.082954', '', '2023-09-03 22:00:00', 0.2, 28, 91, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (232, 'Keine Anpassung seit letzter Messung', '2023-08-25 08:25:47.522134', 'Unklar, welche Massnahme umgesetzt wurde - evtl. sind wir mit wearemoving und dem neuen tactical bereits weiter. ', '2023-08-24 22:00:00', 0.7, 36, 67, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (263, 'Offizielle Zahl von Marcel höher als selbsterrechnete.', '2023-09-06 06:00:31.018376', 'Keine', '2023-09-05 22:00:00', 14803, 5, 32, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (236, 'TH Reduktion auf Ende November (-20%), Aktuell konnte die Auslastung gehalten werden. ', '2023-08-25 08:39:57.50632', 'Auslastung Herbst sicherstellen', '2023-08-24 22:00:00', 0.3, 36, 78, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (183, 'Der Testrun hat mit Marc Schmid, Max, Adrian , Khoi und Pippo stattgefunden. Iwan hatte den Lead Rebecca hat unterstützt. Sie wird nun auch als 2. Trainerin den Lead am Puzzle Up übernehmen.', '2023-08-16 19:09:21.463582', 'Iwan hat auf Anfrage von Mats ein Video zu ML erstellt.
Das Grundgerüst des Labs mit Step by Step Anleitung steht nun muss aber mit dem Feedback aus dem Testrun verfeinert werden. Auch Infrastrukturthemen sind noch offen. ', '2023-08-15 22:00:00', 0.4, 30, 59, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (184, 'Draft für Angebotstexte für die Website für MLOps Consulting und dem MLOps Lab sind erstellt. Validierung herausfordernd. Aktuell hat ein Gespräch mit der Mobi stattgefunden. Die sind aber schon sehr weit. Monitoring hat bei ihnen noch Potential.', '2023-08-16 19:17:03.255698', '', '2023-08-15 22:00:00', 0.5, 30, 58, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (186, 'Keine Änderung zum letzten Check-in. Spannende Bewerbungen offen.', '2023-08-17 09:36:50.354004', '', '2023-08-16 22:00:00', 0.2, 20, 35, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (187, 'Von Budget von CHF 111 800 wurde bereits 71060 benutzt ', '2023-08-21 10:28:35.598462', '', '2023-08-16 22:00:00', 71060, 41, 122, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (191, 'Wenig indirekter Aufwand wegen Ferienabwesenheiten', '2023-08-21 10:53:29.225721', '', '2023-08-16 22:00:00', 64, 41, 125, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (189, 'Elternzugang mussten wir einiges nachbessern', '2023-08-21 10:54:23.969957', '', '2023-07-16 22:00:00', 84, 41, 121, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (192, '74 Prozent', '2023-08-21 11:36:47.945967', '', '2023-08-20 22:00:00', 0.7, 31, 81, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (193, '71 Prozent', '2023-08-21 11:37:17.826866', '', '2023-08-20 22:00:00', 0.3, 31, 82, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (194, 'Von Chrigel während Teammeeting angesprochen und Interesse geprüft. Thema wird in der /mid Week (November 23) vertieft angeschaut. Ziel ist ein PoC.', '2023-08-21 12:40:47.05924', '', '2023-08-20 22:00:00', 0.7, 31, 105, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (195, 'Öffnungsrate vom August NL 34.7%', '2023-08-23 09:59:14.336763', 'Sommerferien. Wir prüfen die Entwicklung im September. ', '2023-08-22 22:00:00', 35, 26, 108, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (196, 'Task erledigt. ', '2023-08-23 10:00:01.257123', 'Allenfalls Linkedin Kampagne um neue Abonnent*innen zu gewinnen.', '2023-08-22 22:00:00', 1, 26, 113, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (197, 'Augustversand = 138 Empfänger*innen = -0.5%', '2023-08-23 10:03:32.160754', 'Straffe Adresskorrektur im Highrise. ', '2023-08-22 22:00:00', 1338, 26, 109, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2411, 'Wird morgen im Bila mit ii besprochen', '2024-08-12 08:57:37.904703', '', '2024-08-12 08:57:37.904707', NULL, 5, 1276, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (200, 'Haben bereits einige Interviews mit Personen im CI/CD Umfeld durchgeführt. Die Interviews sind dokumentiert und werden im September ausgewertet.', '2023-08-24 07:17:49.905876', '', '2023-08-23 22:00:00', 0.5, 31, 76, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (201, '+4.6 Prozentpunkte gegenüber Vormonat
', '2023-08-24 07:35:25.711195', '', '2023-08-23 22:00:00', 54.8, 22, 44, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (203, 'Keine: Aber alles geplant, dass Target Zone erreicht werden kann', '2023-08-24 07:51:30.423803', '- Task bei Jölu und Nathi zur Ablage ihrer Team Happiness
- Termin für Massnahmenpaket definieren ist am 31.8.', '2023-08-23 22:00:00', 0.25, 5, 36, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (204, 'Noch nicht gestartet ', '2023-08-24 07:57:06.571643', '', '2023-08-23 22:00:00', 0, 41, 126, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (205, 'März Stundensätze pro Senioritätslevel sind ausgewertet. Können wir als Baseline so verwenden.', '2023-08-24 07:59:48.585776', '- Am 13.9. im mobcoeur weekly ist das KR traktandiert für Massnahmenpaket Bestimmung', '2023-08-23 22:00:00', 0.5, 5, 119, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (206, 'noch nicht gestartet ', '2023-08-24 08:00:02.066646', '', '2023-08-23 22:00:00', 0, 41, 123, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (207, 'Keine', '2023-08-24 08:04:15.45546', '', '2023-08-23 22:00:00', 0.4, 30, 59, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (208, 'Keine', '2023-08-24 08:04:36.809448', '', '2023-08-23 22:00:00', 0.5, 30, 58, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (210, 'Freie Kapazität ist etwas gestiegen (PL Kapazität dazu gekommen). Ab September gibt es gegen 400% zusätzliche freie Kapazität bei unseren Members.', '2023-08-24 09:03:08.346575', 'Etliche Leads und Anfragen werden derzeit geprüft.', '2023-08-23 22:00:00', 1.5, 13, 45, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (202, 'kurz vor der Einführung', '2023-08-24 09:03:38.569888', '', '2023-08-23 22:00:00', 0.15, 22, 46, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (212, 'Deals von RhB, EveryWare und aity im August reingekommen. Leads: SHKB sowie erste Anfragen aus dem Rocky Linux Umfeld.', '2023-08-24 09:11:50.799325', '', '2023-08-23 22:00:00', 66500, 16, 51, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (213, 'Nächste Messung für Monat August wird im September folgen.', '2023-08-24 09:14:13.736541', '', '2023-08-23 22:00:00', 81481, 16, 50, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (209, 'Werte per 01.09.23: Festanstellung Simon Bühlmann, Tobias Stern, Daniel Alder, Yelan Tao, diverse Pensumsanpassungen, Austritt Simon Roth', '2023-09-11 04:26:47.725138', '', '2023-08-23 22:00:00', 1.95, 13, 43, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (215, 'Prüfung Swiss Unihockey als Kunde für GCP angestossen. Interner Kick-Off mit Marcom+Sales und potentiell weiteren Divisions geplant (2. September).', '2023-08-24 09:19:48.719295', '', '2023-08-23 22:00:00', 0.11, 16, 48, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (216, 'Stand 24.8.: 13 Anmeldungen', '2023-08-24 10:45:38.953258', '', '2023-08-23 22:00:00', 0, 20, 38, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (217, 'Zielwerte in Mobkernteam in Diskussion & Teamevent von Bandtis of the Latin Sea im Juli durchgeführt', '2023-08-24 10:47:34.877917', '', '2023-08-23 22:00:00', 0.2, 20, 57, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (218, 'keine Veränderung', '2023-08-24 10:48:32.898036', '', '2023-08-23 22:00:00', 0.2, 20, 35, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (219, 'Bestätigung des weiteren Vorgehens von beiden Kunden erhalten. Gute Lösung gefunden, jedoch werden die reduzierten Pensen zumindest in den nächsten Monaten noch nicht kompensiert werden können.', '2023-08-24 13:08:42.224997', '', '2023-08-23 22:00:00', 0.6, 33, 80, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (220, 'Vertrag von Dani Alder wurde unterzeichnet. Er ist Dev+Sysler gleichzeitig. Angenommen, er ist halb/halb tätig, können wir mit ihm kalkulatorisch 70%/2=35% der verlorenen Sys-Pensen kompensieren.', '2023-08-24 13:13:22.139287', 'Im Recruiting aktiv bleiben, sofern Bedarf vorhanden (im Moment eher on hold)', '2023-08-23 22:00:00', -50, 33, 77, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (221, 'Alles i.O.', '2023-08-24 13:28:43.865556', '', '2023-08-23 22:00:00', 0, 31, 104, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (222, 'OpenShift Cluster LPG1 wurde erfolgreich aufgebaut. Cilium PoC gestartet.', '2023-08-24 14:32:46.341097', '', '2023-08-23 22:00:00', 0.3, 40, 79, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (223, 'Meeting-Gefässe und -Zyklen /dev/tre überprüft, neu definiert und mit übergelagerten Firmen-Meetings und dem OKR-Prozess abgestimmt -> https://wiki.puzzle.ch/Puzzle/DevTreMeetings - Neue Termin-Serien erstellt', '2023-08-24 19:39:19.425451', 'Meetings planen und durchführen', '2023-08-23 22:00:00', 0.3, 4, 86, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (273, 'Keine Veränderung', '2023-09-04 08:43:16.574913', '', '2023-09-03 22:00:00', 0.3, 28, 97, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (276, '13 Teilnehmende am 1. Mobility Day vom 31.8.', '2023-09-06 11:50:10.878996', '', '2023-09-05 22:00:00', 0, 20, 38, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (224, 'Interne Version ist finalisiert und Rückmeldungen eingearbeitet. Erste Texte für externe Kommunikation sind vorhanden.', '2023-08-25 06:05:25.976562', 'Finalisieren Texte und Bilder für öffentliche Kommunikation. Vorstellen am Weekly, start Sales Kampagne.', '2023-08-24 22:00:00', 0.2, 13, 47, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (225, 'MLOps-Video', '2023-08-25 07:22:22.036945', '', '2023-08-24 22:00:00', 2, 26, 118, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (226, 'Das Board ist lanciert und kommuniziert', '2023-08-25 07:23:05.118068', '', '2023-08-24 22:00:00', 1, 26, 117, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (228, 'Lunch Explorer Zürich auf LinkedIn', '2023-08-25 07:26:31.008175', '', '2023-08-24 22:00:00', 5, 26, 114, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (188, 'Der Juli Umsatz ist unter Budget wegen vielen Ferienabwesenheiten ', '2023-09-11 12:33:06.903323', '', '2023-08-16 22:00:00', 391049, 41, 120, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (233, 'Fokussierung fehlt weiterhin - wir haben aber mittlerweile ein entsprechendes Projektbudget für das aufgleisen', '2023-08-25 08:27:26.415947', 'Priorisieren findings vom Workshop und Ansatz definieren', '2023-08-24 22:00:00', 0.3, 3, 69, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (234, 'Noch keine klarere Formulierung / definition - Strategie ist abgestimmt', '2023-08-25 08:28:19.149925', 'Strategie konkretisieren (Draft SIH)', '2023-08-24 22:00:00', 0.3, 3, 65, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (235, 'Juli: 54%, August 68% - die absolute Verrechenbarkeit ist wegen Abwesenheiten, Ferien usw. starken Schwankungen unterworfen. Die Verrechenbarkeit selber ist mit 100% hoch', '2023-08-25 08:38:13.554972', 'Überdenken, ob sich die Kennzahl eigenet (ob all den Schwankungen)', '2023-08-24 22:00:00', 2, 36, 71, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (237, 'Weiterhin keine Neukunden (Sales etwas tiefer am laufen)', '2023-08-25 08:40:42.399931', 'Sales, Sales, Sales', '2023-08-24 22:00:00', 0, 36, 73, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (238, '3 Happiness-Umfragen durchgeführt und ausgewertet. Massnahmenpaket für das nächste Quartal in Erarbeitung.', '2023-08-25 11:41:08.375944', '', '2023-08-24 22:00:00', 0.5, 31, 93, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (239, 'Inputs weiter implementiert und Massnahmen umgesetzt.', '2023-08-25 11:42:36.056468', '', '2023-08-24 22:00:00', 0.7, 31, 85, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (240, 'Simon Bühlmann: Integration in SIARD: Wartung und Support, SAP HANA. Allefalls Mobi-Gaia Grundhaltung: Java-Projekte
Chrigu Lüthi: Einsatz von Oktober bis Ende Jahr im Mobi-Gaia-Team-Auftrag. Mit Chrigu und Mobi abgesprochen.', '2023-08-26 13:44:24.994595', 'Intetragion und Einführungsprogramm für Clara erstellen. SWOA eine Idee, aber Clara möchte zum Start eher Back- statt Frontend (Java)

Beri: Arbeitsversuch am Laufen. Vorzu werden kleine Fortschritte erziehlt. Integration in Aufträge, noch offen.', '2023-08-25 22:00:00', 0.7, 4, 92, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (241, 'Bisher keine Zeit für Arbeit an diesem Key Result', '2023-08-26 13:45:49.802595', 'Aktivitäten gemäss Key Results ', '2023-08-25 22:00:00', 0, 4, 94, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (242, 'Draft "Arbeit im Team " und "Ambitionen" erstellt (https://wiki.puzzle.ch/Puzzle/DevTre), Arbeit an Moser-Baer-Blog-Artikel aufgegeist', '2023-08-26 13:51:15.707019', 'WIKI-Doku über /dev/tre mit Team verifizieren und präszisieren
Moser-Baer-Blog-Artikel ferstigstellen und publizieren', '2023-08-25 22:00:00', 0.3, 4, 89, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (243, 'Bisher keine Zeit für Arbeit an diesem Key Result', '2023-08-26 13:53:19.710419', 'Aktivitäten gemäss Key Results ', '2023-08-25 22:00:00', 0, 4, 87, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (244, 'Swisscom cADC erstellt, kommuniziert und 2x durchgeführt
Mobiliar Gaia erstellt, kommuniziert und 2x durchgeführt', '2023-08-26 13:55:37.115452', 'Aktivitäten gemäss Key Results ', '2023-08-25 22:00:00', 0.3, 4, 106, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (245, 'Das Konzept und das Drehbuch für das Video und den Blogartikel stehen und werden nun mit Markom abgestimmt. ', '2023-08-28 08:00:55.25972', '', '2023-08-27 22:00:00', 0.35, 29, 37, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (262, 'Baseline berechnet. Vorschlag für Zielwerte und Massnahmenpaket erstellt', '2023-08-30 09:19:20.635794', '', '2023-08-29 22:00:00', 0.6, 5, 36, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (247, 'Dagger Video, Cilium Blogpost, ML Ops Video', '2023-08-28 08:37:14.263633', '', '2023-08-27 22:00:00', 0.25, 24, 49, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (246, 'Aareböötle Post erschienen, Family Event hat stattgefunden', '2023-08-28 09:09:22.01945', '', '2023-08-27 22:00:00', 0.7, 24, 40, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (250, '+12', '2023-08-28 10:01:21.359411', '', '2023-08-13 22:00:00', 46, 34, 53, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (248, '-', '2023-08-28 10:01:27.81696', '', '2023-08-27 22:00:00', 46, 34, 53, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (249, '-', '2023-08-28 10:02:05.351032', '', '2023-08-27 22:00:00', 182, 34, 54, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (251, '182', '2023-08-28 10:02:15.913324', '+5', '2023-08-13 22:00:00', 182, 34, 54, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (252, 'Draft seitens /sys diese Woche fertig', '2023-08-28 10:11:09.26368', '', '2023-08-27 22:00:00', 0.3, 34, 61, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (253, 'Supportvertrag ITpoint beim Endkunden im Review, BFH Onboarding weiter fortgeschritten, Kubernetes (CKA) Schulung für Juniors geplant', '2023-08-28 10:12:18.206327', '', '2023-08-27 22:00:00', 0.7, 34, 60, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (254, '-', '2023-08-28 10:12:38.607531', '', '2023-08-27 22:00:00', 0.3, 34, 63, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (255, '-', '2023-08-28 10:12:57.974929', '', '2023-08-27 22:00:00', 0, 34, 62, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (258, 'Mobi Mandat für David und Yelan sollte klappen. Carlo mit SAC zufrieden. Kurzfristig etwas Verbesserung bei Andres (Team-Projekt bei Centris bis im Okt, nicht mehr alleine). Ungelöst: Dani Alder (Leads: Mobi, BJUB)', '2023-08-28 11:14:08.553093', 'Gutes Nachfolgemandat für Andres und Startmandat für Daniel Alder suchen, dann können wir auf 8 kommen.', '2023-08-27 22:00:00', 7, 33, 72, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (265, 'Massnahmen konsolidiert, Baseline bestimmt. Muss im MobCeur noch abgenommen werden.', '2023-08-31 12:47:32.562431', 'Zielwerte sind schwierig zu definieren. Braucht nochmals einen Effort.', '2023-08-30 22:00:00', 0.6, 5, 119, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (259, 'Neuer Auftrag: BKS (sehr klein). Deals: Kapo GR, UZH Frontend Dev, Mobi Gluon, Peerdom DB, HRM DB Consulting, BJUB', '2023-08-28 11:26:37.617476', 'Sales Prio bleibt hoch.', '2023-08-27 22:00:00', 0.3, 33, 70, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (260, 'Bei 8 Divisions eingeführt', '2023-08-28 13:54:17.816622', '', '2023-08-27 22:00:00', 0.7, 13, 41, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (261, 'Aktuell ist nicht definiert, wie die Resultate aus den Happiness Umfragen der Divisions auf Stufe Unternehmen zusammengezogen und ausgewertet werden. Die Auswertung und Umsetzung erfolgt direkt auf Stufe Division.', '2023-08-28 14:54:22.924565', 'Überprüfung des KR für die nächste OKR Iteration.', '2023-08-27 22:00:00', 0.15, 16, 42, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (266, 'Wie bisher', '2023-08-31 13:38:00.871096', '', '2023-08-30 22:00:00', 0.35, 29, 37, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (267, 'Keine Veränderung', '2023-09-04 08:39:10.009705', 'Ptime mit Thomas releasen', '2023-09-03 22:00:00', 0.6, 28, 100, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (268, 'Keine Veränderung', '2023-09-04 08:39:33.618153', '', '2023-09-03 22:00:00', 0.1, 28, 98, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (269, 'Keine Veränderung', '2023-09-04 08:39:51.256572', '', '2023-09-03 22:00:00', 11, 28, 101, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (270, 'SAC kommt gut, BAFU geplant, Bestellung noch ausstehend. Brixel läuft bis mindestens Ende September mit bestehendem Vertrag  ', '2023-09-04 08:40:34.86401', '', '2023-09-03 22:00:00', 0.7, 28, 103, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (271, 'Monat Juli: 62,660512250989%', '2023-09-04 08:42:17.710885', '', '2023-09-03 22:00:00', 0.7, 28, 102, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (256, 'Juli: 82%. Ausblick August: ähnlich wie Juli erwartet, dann ab Sept. tedentiell abnehmend', '2023-09-17 22:51:48.612592', 'Sales, Sales, Sales...', '2023-08-27 22:00:00', 82, 33, 99, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (361, 'Konzept immer noch bei der GL', '2023-09-18 11:01:24.854381', '', '2023-09-17 22:00:00', 1, 26, 110, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (363, 'Öffnungsrate vom NL August', '2023-09-18 11:03:32.544804', '', '2023-09-17 22:00:00', 36.1, 26, 108, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (364, 'NL-Abonennt*innen Stand 18.9. / Für Oktober SoMe-Kampagne geplant', '2023-09-18 11:05:06.893316', '', '2023-09-17 22:00:00', 1338, 26, 109, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (365, '-1', '2023-09-18 11:14:42.236497', 'keine', '2023-09-17 22:00:00', 45, 34, 53, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (366, '-2', '2023-09-18 11:15:58.039583', 'keine', '2023-09-17 22:00:00', 180, 34, 54, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (277, 'Indikatoren & Zielwerte sind definiert. Zusätzliche Frage in Membersumfrage der /mob Subteams ergänzt. Beurteilung & Auswertung von Teamzusammenhalt erfolgt spätestens ab Q2 in allen Subteams.', '2023-09-06 11:54:19.033827', '', '2023-09-05 22:00:00', 0.3, 20, 57, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (278, 'Aussichtsreiche Bewerbung (nach 2. Gespräch & Techinterview) am Start', '2023-09-06 12:56:34.29422', 'Im Mob Weekly entscheiden & ev. Angebot durch HR', '2023-09-05 22:00:00', 0.2, 20, 35, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (279, 'N/A', '2023-09-07 09:52:54.567623', 'Aktivitäten gemäss Key Results', '2023-09-06 22:00:00', 0, 4, 94, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (280, 'N/A', '2023-09-07 09:53:45.210247', 'Aktivitäten gemäss Key Results', '2023-09-06 22:00:00', 0.7, 4, 92, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (281, 'N/A', '2023-09-07 09:55:09.234594', 'Aktivitäten gemäss Key Results. Wird sich gemäss neu definiertem Zyklus und den Terminen ins nächste Quartal verschieben', '2023-09-06 22:00:00', 0.3, 4, 86, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (283, 'N/A', '2023-09-07 12:37:15.078339', 'Der Moser-Baer-Blogartikel wurde leicht angepasst. Er wird mit technischer Ausrichtung als erster von etwa 5 Blog-Artikeln erscheinen und ist in Arbeit.
Ausserhalb der OKRs wurde eine Referenzstory für BKD erstellt.
Wie wir arbeiten -> "Arbeit im Team" und Dienstleistungsangebot -> "Ambitionen" sind skitzziert im Wiki dokumentiert: https://wiki.puzzle.ch/Puzzle/DevTre', '2023-09-06 22:00:00', 0.5, 4, 89, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (282, 'Blogartikel ist in Arbeit und adaptiert. Neu teschnisch ausgerichet & erster von Artieklserie. Ausserhalb der OKRs wurde eine Referenzstory für BKD erstellt.  Arbeit im Team, Ambitionen sind skizziert: https://wiki.puzzle.ch/Puzzle/DevTre
', '2023-09-07 12:40:50.714529', 'Abschluss Blog-Artikel', '2023-09-06 22:00:00', 0.5, 4, 89, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (284, 'N/A', '2023-09-07 12:41:36.883236', 'Aktivitäten gemäss Key Results ', '2023-09-06 22:00:00', 0, 4, 87, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (285, 'Wert Juli 2023 - +12.6% höher als im Vormonat', '2023-09-07 12:46:18.459843', 'Auslastung der verrechenbaren Pensen hoch halten. Wird mit Projektabschlüssen und Eintritten nicht erreichbar sein.', '2023-09-06 22:00:00', 84.8, 4, 74, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (286, 'Verbesserung Mobi- und Swisscom-Controlling', '2023-09-07 12:47:50.428141', 'Aktivitäten gemäss Key Results ', '2023-09-06 22:00:00', 0.4, 4, 106, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (287, 'Deal von Centris im September. Etliche weitere Leads offen, in Bearbeitung oder bereits offeriert: Uni BE, SHKB, BLS, ETHZ, HPE, Centris, Honeywell, Flughafen ZH', '2023-09-08 07:30:29.308576', '', '2023-09-07 22:00:00', 77800, 16, 51, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (288, 'Keine Veränderung. Das KR wird nicht mehr aktiv weiterverfolgt.', '2023-09-08 07:34:06.978881', '', '2023-09-07 22:00:00', 0.15, 16, 42, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (289, 'Kick-Off zur Marktopportunität mit Marcom&Sales am 4. September durchgeführt. Klärung der Pendenzen und des weiteren Vorgehens. Google Billing ID von Peerdom an Google weitergeleitet zur Prüfung. Teilnahme an AWS Cloud Day vom 26. September sfa/mga.', '2023-09-08 07:37:15.018857', '', '2023-09-07 22:00:00', 0.15, 16, 48, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (290, 'Neue Messung für Monat August. Es wurden noch Aufträge angepasst/neu integriert.', '2023-09-08 08:40:27.756837', '', '2023-09-07 22:00:00', 99466, 16, 50, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (291, 'Freie Kapa durch Neuanstellungen massiv gestiegen.', '2023-09-11 04:22:33.202399', 'Wir starten einen Java-Sales-Sprint.', '2023-09-10 22:00:00', 3.7, 13, 45, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (292, 'Eintritt per 18.9.23 Clara Llorente Lemm', '2023-09-11 04:26:59.384089', '', '2023-09-10 22:00:00', 2.95, 13, 43, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (293, 'Kein Update, wir sind an der Finalisierung der Texte und Bilder.', '2023-09-11 04:28:13.277612', '', '2023-09-10 22:00:00', 0.2, 13, 47, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (294, 'Kein Update', '2023-09-11 05:31:01.598835', '', '2023-09-10 22:00:00', 0.7, 13, 41, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (295, 'Ptime muss noch Pipeline angepasst werden, Hitobito sollte per 31.8 (gemäss MVI) ready sein', '2023-09-11 06:01:25.040156', '', '2023-09-10 22:00:00', 0.7, 28, 100, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (296, 'Meeeting hat statt gefunden', '2023-09-11 06:01:56.26097', 'Klären Budget', '2023-09-10 22:00:00', 0.2, 28, 98, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (297, 'Keine Veränderung', '2023-09-11 06:03:47.179952', '', '2023-09-10 22:00:00', 11, 28, 101, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (298, 'Keine Veränderung', '2023-09-11 06:04:17.012057', '', '2023-09-10 22:00:00', 0.7, 28, 103, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (299, 'Aktuelle Zahl per 11.09.23 ist 65% (Monatasabschluss noch ausstehend)', '2023-09-11 06:06:06.911799', '', '2023-09-10 22:00:00', 0.7, 28, 102, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (300, 'Letzte Bewerbungen waren nicht vielversprechend', '2023-09-11 06:06:46.688902', '', '2023-09-10 22:00:00', 0.1, 28, 91, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (301, 'Keine Veränderung', '2023-09-11 06:07:01.995549', '', '2023-09-10 22:00:00', 0.3, 28, 97, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (302, 'Juli: -
Aug: Aareböötle, Family, Sommerinfoanlass,
Sept: TWS, Geburiapero, ', '2023-09-11 06:47:35.418407', '', '2023-09-10 22:00:00', 0.9, 24, 40, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (303, 'Mischwert: Wir haben bei Dagger und Cilium das Wissen bereits über mehrere Members verteilt. Ebenfalls bei ML Ops. Allerdings fehlt eine Komm zu Kubermatic', '2023-09-11 06:49:06.903171', '', '2023-09-10 22:00:00', 0.5, 24, 49, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (304, 'Meinung von 6/7 RTEs ist bekannt. Zwei Linkedin-Beiträge sind für September geplant. Austausch mit Mäge geplant', '2023-09-11 08:51:13.220694', '', '2023-09-10 22:00:00', 0.6, 29, 37, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (305, 'keine Veränderung', '2023-09-11 09:37:29.557502', 'siehe checkin vom 25.08.23', '2023-09-10 22:00:00', 0.3, 3, 69, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (306, 'keine Veränderung seit letztem Checkin', '2023-09-11 09:39:24.473084', 'Slot mit Simu am Freitag, 15.09.23 um die Strategie zu finalisieren', '2023-09-10 22:00:00', 0.3, 3, 65, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (307, 'WAC Dinner durchgeführt', '2023-09-11 09:46:13.242651', 'die entschiedenen Aktivitäten weiterführen', '2023-09-10 22:00:00', 1, 36, 67, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (308, 'Happiness Umfrage im Bila integriert', '2023-09-11 09:47:34.417462', 'Umfrage regelmässig durchführen und dann Zielwerte festlegen', '2023-09-10 22:00:00', 0.3, 36, 68, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (309, 'keine Veränderung seit letztem Checkin', '2023-09-11 09:51:52.819603', '', '2023-09-10 22:00:00', 2, 36, 71, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (310, 'Keine Veränderung seit letztem Checkin', '2023-09-11 11:46:26.7304', 'Auslastung Herbst sicherstellen', '2023-09-10 22:00:00', 0.3, 36, 78, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (352, '2. Durchführung am Techworkshop durch Iwan erfolgt (Rebecca krank)', '2023-09-18 08:15:14.050639', '', '2023-09-17 22:00:00', 0.5, 30, 59, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (311, 'sehr viele qualitative Leads (Visana, UZH, HRM Systems, FMH, Kanton Züri) aber noch keine fixe Bestätigun', '2023-09-11 12:00:08.4229', 'Close the Sale!!!!', '2023-09-10 22:00:00', 0, 36, 73, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (312, 'Von Budget von CHF 111 800 wurde bereits 78710 benutzt ', '2023-09-11 12:30:58.405421', '', '2023-09-10 22:00:00', 78710, 41, 122, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (313, 'Sehr guter August Umsatz von CHF 52''022', '2023-09-11 12:32:56.31526', '', '2023-09-10 22:00:00', 443070, 41, 120, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (315, 'Wartung und indirekter Aufwand war tief', '2023-09-11 12:39:17.831887', '', '2023-09-10 22:00:00', 70.3, 41, 125, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (316, 'Inhalt ist bekannt (?)', '2023-09-11 13:07:33.942933', '', '2023-09-10 22:00:00', 1, 65, 127, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (317, 'Datum steht noch nicht fest ', '2023-09-11 13:07:59.57807', '', '2023-09-10 22:00:00', 0, 65, 126, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (318, 'noch keines geplant ', '2023-09-11 13:08:32.159938', '', '2023-09-10 22:00:00', 0, 41, 123, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (320, 'Überarbeitung des Labs nach dem Feedback aus dem Testrun', '2023-09-12 06:42:47.159152', '', '2023-09-11 22:00:00', 0.5, 30, 59, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (321, 'Alles vorbereitet für Verabschiedung im Team', '2023-09-12 13:42:04.268342', '', '2023-09-11 22:00:00', 0.69, 5, 36, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (322, 'Massnahmepaket ist definiert. Aber Baseline kann Senioritätslevel nicht sinnvoll abbilden', '2023-09-12 14:44:09.000869', '', '2023-09-11 22:00:00', 0.4, 5, 119, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (314, 'SBVE hatten viele Nachbesserungen die nicht verrechent werden können', '2023-09-14 11:01:01.558731', '', '2023-09-10 22:00:00', 90.8, 41, 121, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (360, 'Keine Veränderung ', '2023-09-18 08:41:04.904905', 'Definieren was die Umfrage überhaupt bezwecken soll.', '2023-09-17 22:00:00', 0.3, 28, 97, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (327, 'keine Änderung', '2023-09-13 06:15:44.825369', '', '2023-09-12 22:00:00', 0, 20, 38, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (328, 'Erkenntnisse ausgewertet. Total mit 6 Kunden/Partner gesprochen', '2023-09-13 10:08:20.72237', '', '2023-09-12 22:00:00', 0.7, 31, 76, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (331, 'Kommunikation (interner Newsbeitrag, extern Blogpost mit Video und eigene Landingpage) folgen anfangs Oktober, daher Commit Ziel knapp nicht erreicht.', '2023-09-14 04:16:25.647593', '', '2023-09-13 22:00:00', 0.25, 13, 47, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (332, 'Ergänzung des SNB-Teams durch CK aufgegleist (Kompensation TD). Hier ab 2024 Aussicht auf leicht höheres Volumen. Pensum bei FZAG kann bis Ende Jahr knapp gehalten werden (Fokus von TD), danach Ablösung von TD als Challenge.', '2023-09-14 08:24:25.946787', 'Bereits im Herbst Planungssicherheit bei FZAG und SNB für 2024 anstreben.', '2023-09-13 22:00:00', 0.8, 33, 80, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (333, 'Status quo (siehe Checkin vom 24.8.).', '2023-09-14 08:27:42.975607', 'Recruitingbemühungen im Sys Engineering werden aufgrund aktueller Nachfrage etwas reduziert, jedoch nicht eingestellt.', '2023-09-13 22:00:00', -50, 33, 77, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (323, 'Nicht offizielle Zahl von Marcel. Selbst aus PTime errechnet', '2023-09-18 13:10:13.461295', '', '2023-09-17 22:00:00', 16240, 5, 32, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (257, 'Yelan startet definitiv (alle Bewilligungen liegen vor). Einsatzmöglichkeite für Mobi (Gluon Migration) zusammen mit DSM ist offeriert. Kunde hat Commitment zu dieser Combo abgegeben.', '2023-09-14 08:34:41.865426', 'Vertrag mit Mobi abschliessen. Fallback wäre das UZH Frontend-Dev-Mandat (Angebot eingereicht, Zuschlagsw''keit ~50%)', '2023-08-27 22:00:00', 0.5, 33, 83, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (335, 'Junior/Senior Pairing im Gluon2-Auftrag für Yelan erreicht (formelle Bestellung zwar noch aussthend). Verrechenbarkeit um die 75% für die nächsten Wochen erwartet. Intakte Chancen auf Zuschlag bei UZH (dann gegen 100% verrechenbar)', '2023-09-14 08:38:06.315176', 'Vertrag mit Mobi erhalten und UZH Dev-Mandat gewinnen (ist nicht mehr in unserer Hand, dies zu erreichen)', '2023-09-13 22:00:00', 0.8, 33, 83, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (336, 'Neukundenanteil nicht erreicht, dafür viele Leads und mehrere Aufträge (z.T. kleine)
Faktisch gewonnen: Mobi MongoDB, KaPo GR, SNB Ansible auf Windows, BKD.
Zudem neue Leads: SIX Satellite, SwissLife DepTrack, FZAG Monitoring RFP, BLKB Java.', '2023-09-14 08:46:33.314305', 'Faktisch gewonnene Deals ins Trockene bringen. Nicht nachlassen bei Aquise. Angelschnur gespannt halten...', '2023-09-13 22:00:00', 0.75, 33, 70, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (334, 'Mandat für David und Yelan bereits gestartet (Vertrag noch ausstehend). Carlo ok. Verbesserung bei Andres (Team-Projekt b. Centris). Zumindest kurzfristige Perspektive für Dani (Mobi).', '2023-09-14 08:47:22.041523', 'Nachfolgeaufträge für Andres und Dani suchen (kurzfristig ok).', '2023-09-13 22:00:00', 8, 33, 72, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (337, 'Umstellung auf Mecano mit Divisionsrentabilitätsrechnung. Messung nicht mehr möglich.', '2023-09-14 12:33:10.990919', '', '2023-09-13 22:00:00', 0.3, 31, 82, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (338, 'Umstellung auf Mecano mit Divisionsrentabilitätsrechnung. Messung nicht mehr möglich.', '2023-09-14 12:33:22.313066', '', '2023-09-13 22:00:00', 0.7, 31, 81, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (339, 'Alles i.O.', '2023-09-14 12:33:47.645011', '', '2023-09-13 22:00:00', 0, 31, 104, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (340, 'Cilium ist als CNI auf LPG1-Cluster installiert, die Verbindungen werden mit Hubble analysiert.', '2023-09-14 12:34:21.883211', 'PoC weiterführen', '2023-09-13 22:00:00', 0.7, 40, 79, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (341, 'In /mid-Week wird ein PoC durchgeführt', '2023-09-14 12:35:35.076404', 'PoC durchführen', '2023-09-13 22:00:00', 0.7, 40, 105, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (342, 'Massnahmenpaket in Form der aktuellen Teammeeting-Inhalte definiert und für das nächste Quartal geplant. Durchschnittliche Zufriedenheit der Members bei /mid jeden Monat gestiegen (Juni: 7.4, Juli: 7.6, August: 8)', '2023-09-14 12:41:10.263166', '', '2023-09-13 22:00:00', 1, 31, 93, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (343, 'Learnings: Nicht relevante Infos rausfiltern. Relevante Infos direkter und transparenter weiter geben. Die Members mitbestimmen lassen, welche Infos als relevante oder nicht relevant klassifiziert werden. Mitarbeit and Zielen motiviert.', '2023-09-14 12:45:30.838305', '', '2023-09-13 22:00:00', 1, 31, 85, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (344, 'Chrigu Schlatter hat die BAFU Pipeline in Dagger umgeschrieben (PoC). Learnings werden voraussichtlich im 3. Teil der Dagger-Blogbeitragsserie dargestellt.', '2023-09-14 13:33:48.849038', '', '2023-09-13 22:00:00', 0.9, 31, 76, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (345, 'Divisionsrentabilität wurde eingeführt und den Divisions vorgestellt', '2023-09-15 08:39:55.94165', '', '2023-09-14 22:00:00', 0.3, 22, 46, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (347, '4 Zusammenhaltsfördernde Aktivitäten sind durchgeführt und werden bis am 22.9. im Bandits of the Latin Sea Team ausgewertet.', '2023-09-15 09:43:20.349614', '', '2023-09-14 22:00:00', 0.7, 20, 57, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (349, 'Messung August: 82%. Ausblick: September tendentiell tiefer (wegen Neueintritten und ggf. Lücken)', '2023-09-17 22:52:30.925267', 'Sales, Sales, Sales...', '2023-09-17 22:00:00', 82, 33, 99, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (350, 'Es ist uns nicht bei allen Technologien gelungen unsere Kompetenz direkt sichtbar zu machen (Kubermatic fehlt). Für Cilium und Dagger wurde aber klar mehr als nur das Minimalziel erreicht. Auch im Thema ML Ops sind wir unterwegs.', '2023-09-18 05:35:53.68167', '', '2023-09-17 22:00:00', 0.5, 24, 49, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (351, 'Es wurde klar aktiver über die Puzzle internen Events kommuniziert und die Sichtbarkeit erhöht. Das KR wurde klar übertroffen (Stretch)', '2023-09-18 05:37:35.629369', '', '2023-09-17 22:00:00', 0.9, 24, 40, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1801, '', '2024-04-29 10:33:05.231027', '', '2024-04-29 10:33:05.231034', NULL, 28, 1232, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (353, 'Am Techworkshop wurde ein OpenSpace zu PrivateGPT durchgeführt. Das Angebot dazu ist aber noch nicht erstellt.', '2023-09-18 08:26:14.10592', '', '2023-09-17 22:00:00', 0.5, 30, 58, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (354, 'Final Check-in:  Ptime muss noch Pipeline angepasst werden, Hitobito sollte per 31.8 (gemäss MVI) ready sein. Verzögerung aufgrund von kleinem Pensum MVI.', '2023-09-18 08:36:30.724163', 'Beides weiter einplanen und vorwärts treiben. Thomas B. kann zusätzlich unterstützen.', '2023-09-17 22:00:00', 0.7, 28, 100, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (355, 'Auslegeorndung hat stattgefunden. Board aktualisiert. Budget ist nicht ausreichend. Planung ist ongoing.', '2023-09-18 08:37:26.653415', 'Budget klären.', '2023-09-17 22:00:00', 0.4, 28, 98, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (356, 'Keine Veränderung ', '2023-09-18 08:38:10.676157', 'Klären Bedaf BAFU ab 1.1.24', '2023-09-17 22:00:00', 11, 28, 101, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (357, 'Brixel pausiert. Genügend Aufträge vorhanden', '2023-09-18 08:39:10.920361', '', '2023-09-17 22:00:00', 0.8, 28, 103, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (358, 'Keine Veränderung', '2023-09-18 08:39:45.002029', '', '2023-09-17 22:00:00', 0.7, 28, 102, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (359, 'Keine Veränderung', '2023-09-18 08:40:28.988035', 'Prüfen von möglichen Kandidaten', '2023-09-17 22:00:00', 0.1, 28, 91, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (330, 'Baseline und Massnahmenpaket sind definiert und abgenommen', '2023-09-18 13:08:04.903086', '', '2023-09-17 22:00:00', 0.85, 5, 119, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (348, 'Martin hat Vertrag unterschrieben', '2023-09-18 13:17:57.096117', '', '2023-09-17 22:00:00', 1, 20, 35, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (346, 'Für den 2. Mobility Tag vom 18.9 waren es wieder 13 Member', '2023-09-18 13:15:51.455503', 'Zielwerte wurden festgelegt, bevor die Verfügbarkeit der Members an den einzelnen Tagen bekannt war. Aufgrund der Verfügbarkeit hätte im als bestmöglichen Wert nur Commit Zone erreicht werden können.', '2023-09-17 22:00:00', 0.2, 20, 38, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (367, 'Das Ops Angebot ist finalisiert, Kommunikation steht aus', '2023-09-18 11:28:22.926511', 'weiterziehen', '2023-09-17 22:00:00', 0.5, 34, 61, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (368, 'Weitere Kunden in der Pipeline', '2023-09-18 11:30:09.37364', '', '2023-09-17 22:00:00', 1, 34, 60, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (369, 'Meeting fand ohne /sys statt', '2023-09-18 11:33:00.41323', 'Wir konzentrieren uns auf die Marktoportunitaet Ops', '2023-09-17 22:00:00', 0, 34, 63, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (370, '-', '2023-09-18 11:33:30.169908', '-', '2023-09-17 22:00:00', 0, 34, 62, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (371, 'Der Blog + das Video werden noch im September veröffentlicht. Termin mit Mäge steht diese Woche an für den Austausch. Alle RTEs wurden befragt. Pippo und Jölu haben ihre Kunden befragt.', '2023-09-18 11:59:24.678055', '', '2023-09-17 22:00:00', 1, 29, 37, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (329, 'Massahmenpaket, Baseline, Zielwert definiert und abgenommen', '2023-09-18 13:01:28.018679', '', '2023-09-17 22:00:00', 0.85, 5, 36, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (324, 'js und pe bei Lehrlingen, jbr ganzer Monat in Ferien, ii und rhi für Marktopportunität. Nicht offizielle Zahl Marcel. Aus PTime gezogen', '2023-09-18 13:11:19.576769', '', '2023-09-17 22:00:00', 71, 5, 33, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (372, 'Eintritt Clara, Arbeitsaufnahme bei SWOA', '2023-09-18 13:45:57.036041', 'N/A. Beri Status Quo', '2023-09-17 22:00:00', 0.85, 4, 92, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (373, 'N/A', '2023-09-18 13:47:19.60709', 'Übernahme Key Result in Division OKRs GJ23/24, Q2 (nächster Zyklus)', '2023-09-17 22:00:00', 0, 4, 94, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (374, 'Erstes Teammeeting nach neuer Struktur durchgeführt, Workshop vorbesprochen', '2023-09-18 13:49:03.287967', 'Aktivitäten gemäss Key Results. Wird sich gemäss neu definiertem Zyklus und den Terminen ins nächste Quartal verschieben ', '2023-09-17 22:00:00', 0.6, 4, 86, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (375, 'Blog-Artikel Lit (Moser-Baer) erstellt, BKD Referenzstory publiziert', '2023-09-18 13:51:46.282099', 'Pendente Massnahmen in nächsten Zyklus übernehmen: Wie wir arbeiten -> "Arbeit im Team" und Dienstleistungsangebot -> "Ambitionen" sind skitzziert im Wiki dokumentiert: https://wiki.puzzle.ch/Puzzle/DevTre ', '2023-09-17 22:00:00', 0.7, 4, 89, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (376, 'Vorgehen für Findung bzgl. "Technologischer Ausrichtung" im Teammeeting besprochen', '2023-09-18 13:52:57.086474', 'Aktivitäten gemäss Key Results ', '2023-09-17 22:00:00', 0.2, 4, 87, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (377, 'Mobi- & Swisscom: Weitere Monatsrapporte erstellt, Controlling verbessert, Swisscom nur intern (will nicht für den Rapport bezahlen)', '2023-09-18 13:56:34.391499', 'Massnahmen gemäss Key Results', '2023-09-17 22:00:00', 0.6, 4, 106, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (378, 'Die freie Kapazität bei unseren Members ist im Vergleich zur letzten Messung nochmals etwas gestiegen (um 0.5 FTE).', '2023-09-18 15:00:45.220682', '', '2023-09-17 22:00:00', 4.3, 13, 45, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (379, '-6.1 Prozentpunkte gegenüber Vormonat', '2023-09-18 15:00:52.44142', '', '2023-09-17 22:00:00', 48.7, 22, 44, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (380, 'Weitere Eintritte von Members vor Ende September sind keine mehr zu verzeichnen. Nach dem 1.10. sind es fünf Members, die starten. (2x /mid/container, 2x /mobility, 1x /dev/tre).', '2023-09-18 15:08:36.56011', '', '2023-09-17 22:00:00', 2.95, 13, 43, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (381, 'Deal von Uni Bern im September. Weitere Deals offen. Zusätzlich zu letztem Checkin: MediData', '2023-09-18 15:24:59.511698', '', '2023-09-17 22:00:00', 78500, 16, 51, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (382, 'Zusammenarbeit im MO Hyperscaler Team etabliert, Meeting zum Stand am 18.09.23, Terminreihe für zukünftige Check-ins, Kick-Off mit GCP in Vorbereitung, Klärung Rahmenbedingungen Members Zertifizierung und weitere Themen werden angegangen.', '2023-09-18 15:31:34.411918', '', '2023-09-17 22:00:00', 0.2, 16, 48, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (383, 'Happiness Umfrage in GL durchgeführt.', '2023-09-18 15:46:00.92083', '', '2023-09-17 22:00:00', 0.8, 13, 41, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (384, 'Keine Veränderung gegenüber letzter Messung.', '2023-09-18 15:49:35.966723', '', '2023-09-17 22:00:00', 99466, 16, 50, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (385, 'Keine Veränderung. Das KR wird nicht mehr aktiv weiterverfolgt.', '2023-09-18 16:23:46.281168', '', '2023-09-17 22:00:00', 0.15, 16, 42, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (386, 'keine Veränderung seit 25.08.23', '2023-09-19 06:28:53.846642', 'werden wir im nächsten Quartal weiterverfolgen', '2023-09-18 22:00:00', 0.3, 3, 69, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (387, 'haben nochmals eine Session gemacht, jetzt müssen wir alles noch ins Reine schreiben und finalisieren', '2023-09-19 06:30:07.038977', 'Berti macht den finalen Wurf im nächsten Quartal', '2023-09-18 22:00:00', 0.3, 3, 65, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (389, 'Umfrage bei Bilas eingefügt', '2023-09-19 06:31:56.79753', 'nächstne Quartal noch die Zielwerte definieren', '2023-09-18 22:00:00', 0.3, 36, 68, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (390, 'September tiefe Auslastung', '2023-09-19 06:33:24.606374', ' nehmen wir mit für das nächste Quartal und haben einen Actionplan erstellt', '2023-09-18 22:00:00', 2, 36, 71, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (391, 'keine Veränderung seit letztem Checkin', '2023-09-19 06:34:39.473208', 'Auslastung sicherstellen', '2023-09-18 22:00:00', 0.3, 36, 78, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (392, 'konnten leider keinen Neukunden gewinnen - haben aber sehr viele gute Leads am laufen', '2023-09-19 06:35:43.192874', 'Leads im nächsten Quartal gewinnen', '2023-09-18 22:00:00', 0, 36, 73, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (393, 'Codimd mit Draft erstellt. Wir heute an Mark Waber und Daniel Binggeli übergeben für Inputs und Feedback. Nach den Ferien von Saraina und Mats (Mitte Oktober) treiben wir es weiter vorwärts.', '2023-09-21 08:15:17.519474', 'WIr sind auf gutem Weg', '2023-09-30 22:00:00', 0.1, 48, 146, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (394, 'N/A', '2023-09-23 15:37:56.241782', 'Dito letzter Check-In', '2023-09-22 22:00:00', 0.85, 4, 92, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (395, 'N/A', '2023-09-23 15:38:37.24614', 'Dito letzter Check-In', '2023-09-22 22:00:00', 0, 4, 94, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (396, 'N/A', '2023-09-23 15:39:09.617811', 'Dito letzter Check-In', '2023-09-22 22:00:00', 0.6, 4, 86, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (397, 'N/A', '2023-09-23 15:40:01.955732', 'Dito letzter Check-In', '2023-09-22 22:00:00', 0.7, 4, 89, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (398, 'N/A', '2023-09-23 15:40:56.73687', 'Dito letzter Check-In', '2023-09-22 22:00:00', 0.2, 4, 87, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (399, 'N/A', '2023-09-23 15:44:24.35659', 'N/A', '2023-09-22 22:00:00', 1, 4, 96, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (400, 'N/A', '2023-09-23 15:45:19.858172', 'Dito letzter Check-In', '2023-09-22 22:00:00', 0.6, 4, 106, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (401, 'Wert August 2023 -13.9% tiefer als im Vormonat - Durschntt Zyklus: 75.9', '2023-09-23 15:52:10.948581', '', '2023-09-22 22:00:00', 70.9, 4, 74, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (402, 'In Arbeit', '2023-09-25 06:14:17.106806', '', '2023-10-01 22:00:00', 0.3, 67, 172, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (403, 'In Arbeit', '2023-09-25 06:15:37.423982', '', '2023-09-30 22:00:00', 0.3, 67, 174, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (404, 'Erste Leads im Anflug', '2023-09-25 06:16:10.92492', '', '2023-09-30 22:00:00', 0, 32, 175, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (405, 'Module im /sys Sprint in Arbeit', '2023-09-25 06:17:16.839002', '', '2023-09-30 22:00:00', 2, 32, 176, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (406, 'Evaluation in Progress', '2023-09-25 06:17:47.551373', '', '2023-09-30 22:00:00', 0.3, 34, 177, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (407, 'Noch nicht implementiert', '2023-09-25 06:18:56.778686', '', '2023-09-30 22:00:00', 0, 32, 189, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (408, 'Noch keine Feedbacks abgeholt, in Progress', '2023-09-25 06:19:32.831102', '', '2023-09-30 22:00:00', 0.3, 67, 188, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (409, 'TVs haben und geben sich Mühe, bis jetzt aber in der Target Zone', '2023-09-25 06:20:23.383344', '', '2023-09-30 22:00:00', 0.7, 32, 187, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (459, 'SWW durchgeführt. ca. 500h für 2024 ', '2023-10-16 07:00:33.28488', '', '2023-10-15 22:00:00', 0.1, 41, 154, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (412, 'Grobplanung erstellt', '2023-09-28 09:40:54.293995', '', '2023-09-30 22:00:00', 0.1, 5, 167, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (410, 'Grobplanung erstellt zur Umsetzung der Massnahmen erstellt', '2023-10-05 08:06:14.645712', '', '2023-10-04 22:00:00', 0.1, 5, 163, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (411, 'Grobplanung erstellt', '2023-10-05 08:07:04.630885', '', '2023-10-04 22:00:00', 0.1, 5, 166, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (507, '1.-23.10. => +21.3 %', '2023-10-24 07:15:16.527331', '', '2023-10-23 22:00:00', 0, 49, 148, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (413, '67%: langsames aufbauen cg, unverrechenbares outfading sro, Techworkshop, Invest Marktopportunität mlops, relativer hoher Aufwand Lernende pe und js', '2023-10-02 07:14:38.773741', 'Aufwand Lernende überprüfen.', '2023-10-01 22:00:00', 0, 5, 161, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (414, '- Anpassung WS läuft (Ziel Mitte Okt)
- Kampagne geplant Ende Okt / Anfang Nov
- Sales (Fabien) über das Thema MLOps und die aktuellen Arbeiten informiert. Nimmt das Thema bei den /mid /container Kunden mit (Kennenlerngespräche im Nov).', '2023-10-05 06:27:38.319782', '- Besuch Team Meeting /mid /container für die Vorstellung von MLOps und gewinnen von Members
- Befragung von Kunden am PuzzleUp! zum aktuellen Angebot', '2023-10-04 22:00:00', 0.1, 51, 160, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (415, '- MLOps Lab kommt auf die Webseite puzzle.ch
- Pricing Ideen vorhanden (Idee Ansible Kickstart 1900.- für 2 Tage = 450.- für 4h)
', '2023-10-05 06:31:44.86542', '- Datum festlegen für Lab
- Bewerben des Labs sobald auf puzzle.ch (nach PuzzleUp!)
- Bedürfnis für weitere Labs am PuzzleUp! geklärt', '2023-10-04 22:00:00', 0.1, 51, 159, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (417, 'Erstes Meeting mit msc ist aufgesetzt für 9.10.', '2023-10-05 08:13:51.165616', '', '2023-10-04 22:00:00', 0.1, 5, 169, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (419, 'Bei aktuellen WTO ist das Vorgehen klar und auch wo möglich bereits Lead definiert. min. 3 Angebote sind aktuell in Arbeit und im Q2 werden sicherlich 2 eingereicht.', '2023-10-05 11:02:24.747452', '', '2023-10-04 22:00:00', 0.1, 30, 162, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (420, 'Erster Checkin', '2023-10-05 13:42:15.610925', '', '2023-10-04 22:00:00', 417838, 16, 142, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (422, 'Erster Checkin, noch keine Veränderung.', '2023-10-05 13:43:21.273926', '', '2023-10-04 22:00:00', 0, 13, 129, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (423, 'Erster Checkin, noch keine Veränderung.', '2023-10-05 13:44:14.353637', '', '2023-10-04 22:00:00', 0, 16, 133, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (424, 'Erster Checkin, noch keine Veränderung.', '2023-10-05 13:45:31.344084', '', '2023-10-04 22:00:00', 0, 16, 130, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (425, 'Erster Checkin, im Oktober noch keinen Handelsgeschäfts-Gewinn erzielt', '2023-10-05 13:47:54.958178', '', '2023-10-04 22:00:00', 0, 16, 141, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (426, 'Kickoff für facilitierten Workshop nach Marks Ferien am 26.10 geplant', '2023-10-05 13:49:21.283064', '', '2023-10-04 22:00:00', 0.1, 16, 138, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (421, 'Erster Checkin, noch keine Veränderung.', '2023-10-06 04:10:32.120269', '', '2023-10-04 22:00:00', 0, 13, 131, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (428, 'keine', '2023-10-09 06:40:36.097984', '', '2023-10-08 22:00:00', 0, 24, 144, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (429, 'keine', '2023-10-09 10:56:17.867638', '', '2023-10-08 22:00:00', 0, 24, 135, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (430, 'Wir haben den Oktober mit 1705 Follower gestartet, aktuell bei 1710. ', '2023-10-09 13:49:34.390365', 'Ein technischer Blogpost erfolgte Ende Sep, der nächste ist für diese Woche geplant. Im Oktober folgt die BKD Referenz und allenfalls weitere', '2023-10-08 22:00:00', 0.05, 26, 147, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (431, '
October 1 – 9, 2023 1,502 visits im Vgl. October 1 – 9, 2022 1,259 visit = +19.3%', '2023-10-09 13:53:41.251192', 'Content optimiert & auf Kurs. Setzen aber für die ersten neun Tage keinen neuen Wert.', '2023-10-08 22:00:00', 0, 49, 148, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (432, '0.3', '2023-10-09 14:21:13.510874', 'In Arbeit', '2023-10-08 22:00:00', 0.3, 67, 172, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (433, '0.4', '2023-10-09 14:22:24.20741', 'Onboarding BFH durchgeführt, Doku Vorlage ist ready, Team ist dabei, Secondary TV ist fly', '2023-10-08 22:00:00', 0.7, 67, 174, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (434, '0', '2023-10-09 14:23:26.998375', 'Deal mit HP leider geplatzt. Subscription Team arbeitet an weiteren Leads', '2023-10-08 22:00:00', 0, 32, 175, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (435, '-', '2023-10-09 14:24:12.702192', 'Erstes Modul ready', '2023-10-08 22:00:00', 2, 32, 176, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (437, '0', '2023-10-09 14:25:43.215883', 'Action Items werden definiert', '2023-10-08 22:00:00', 0, 32, 189, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (438, '0', '2023-10-09 14:26:10.223417', 'Feedback Prozess in Arbeit', '2023-10-08 22:00:00', 0.3, 67, 188, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (439, '0.7', '2023-10-09 14:26:50.52453', 'Keine Eskalationen', '2023-10-08 22:00:00', 0.7, 32, 187, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (440, 'Keine', '2023-10-09 20:26:31.52456', 'Aktivitäten gemäss Beschreibung', '2023-10-08 22:00:00', 0, 4, 204, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (441, 'Keine', '2023-10-09 20:27:24.45084', 'Aktivitäten gemäss Beschreibung', '2023-10-08 22:00:00', 0, 4, 202, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (443, 'Keine', '2023-10-09 20:28:23.161332', 'Aktivitäten gemäss Beschreibung', '2023-10-08 22:00:00', 0, 4, 206, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (444, 'Keine', '2023-10-09 20:29:01.812792', 'Aktivitäten gemäss Beschreibung', '2023-10-08 22:00:00', 0, 4, 209, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (445, 'Keine', '2023-10-09 20:29:30.67914', 'Aktivitäten gemäss Beschreibung', '2023-10-08 22:00:00', 0, 4, 208, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (446, 'September 59& (prov). Gründe: Eintritte ohne Aufträge, Gotte/ Götti, Techworkshop, Analyse SODB, Q&A-Session, Blog-Artikels, Referenzberichte, Hupf n.v.', '2023-10-09 20:46:17.283984', 'Simu: Endlich Onboarding in Mobi-Gaia-Auftrag ab KW45, Simu: Opportunität Spring Boot Training, Clara: Onboarding SWOA W&S, Clara: SWOA Athletenbiografie Offerte & Vertrag forcieren, Chrigu: Quarkus-Training', '2023-10-08 22:00:00', 0, 4, 207, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (447, 'Erster Workshop hat ergeben, dass eine Neukonzipierung nur Sinn macht (bzgl Wirtschaftlichkeit, Auslastung & Reaktionszeit) spezifisch auf die beiden Applikationen Haag Streit und Webtransfer. ', '2023-10-10 14:34:29.617456', 'Folge Workshop mit msc wird durch ple geplant', '2023-10-09 22:00:00', 0.3, 5, 169, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (448, 'Bei Team Zoo Wirksamkeit durch zwei Messpunkte (Sept und Okt) erkennbar', '2023-10-10 14:36:22.007659', '', '2023-10-09 22:00:00', 0.15, 20, 164, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (449, 'Verständnisfragen von ple sind mit mg geklärt', '2023-10-10 14:38:47.302708', 'Gemeinsames Verständnis und Fragen klären innerhalb LST /mobility', '2023-10-09 22:00:00', 0.2, 5, 166, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (450, 'Keine', '2023-10-10 14:48:43.32771', '', '2023-10-09 22:00:00', 0, 5, 161, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (451, 'Aufgrund Ferien jbl noch keine Tätigkeiten erfolgt', '2023-10-10 14:50:52.3996', '', '2023-10-09 22:00:00', 0, 20, 170, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (452, 'Mündliche Zusage für Bestellung Siemens 2024 für Marc, Etienne und neu Paco', '2023-10-11 15:07:40.152521', '', '2023-10-10 22:00:00', 0.2, 5, 167, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (453, 'Paco als Junio bei Siemens für 156CHF verkauft (mündliche Zusage)', '2023-10-11 15:09:08.673399', '', '2023-10-10 22:00:00', 0.15, 5, 163, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (454, 'Feedback Folien wurden dem Lab hinzugefügt für die Befragung am PuzzleUp!, Termin für den Austausch mit /cicd wurde vereinbart', '2023-10-12 06:30:18.204869', '', '2023-10-11 22:00:00', 0.2, 51, 160, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (455, 'Pricing kurz im Markt validiert und für einen ersten Run auf 450.- CHF / TN (4h) festgelegt. ', '2023-10-12 06:32:00.492041', '', '2023-10-11 22:00:00', 0.1, 51, 159, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (456, 'Aktuelle Arbeiten für WTO EJPD / BFE / OIZ laufen nach Plan', '2023-10-12 06:33:10.013707', '', '2023-10-11 22:00:00', 0.1, 30, 162, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (506, 'Codi mit Entwurf von Mark Waber und Daniel Binggeli abgenommen. ', '2023-10-24 07:12:23.693643', 'Wir starten nun mit einem Newsbeitrag und lancieren im Verlaufe der nächsten Wochen die ersten Termine.', '2023-10-23 22:00:00', 0.2, 48, 146, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (418, 'Erste Gespräche mit allen RTEs durchgeführt. Alle wollen bestehende Verträge verlängern. 2 Follow Up Meetings mit je einer RTEs bzgl. Stundensatzerhöhung MC Uno und MC Admin Team für Adrian und Khoi', '2023-10-12 14:01:35.317515', '', '2023-10-09 22:00:00', 0.3, 29, 168, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (457, 'Release deployed (SWW und Jubla noch offen) ', '2023-10-16 06:58:50.981194', '', '2023-10-15 22:00:00', 0.3, 65, 155, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (458, 'aus Prioritätsgründen verschoben ', '2023-10-16 06:59:52.602151', 'neuer Termin 24. Oktober 2023', '2023-10-15 22:00:00', 0, 41, 157, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (461, 'Rekordumsatz im September erreicht von CHF 82408', '2023-10-16 12:23:09.142082', '', '2023-10-15 22:00:00', 82408, 41, 152, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (464, 'Erste Notizen dazu gemacht ', '2023-10-16 12:29:47.288361', '', '2023-10-15 22:00:00', 0.1, 41, 194, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (465, 'Blog ist in Bearbeitung ', '2023-10-16 12:30:10.483688', '', '2023-10-15 22:00:00', 0.1, 41, 158, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (498, 'Eine Bewerberin', '2023-10-23 14:08:39.424793', '', '2023-10-22 22:00:00', 0.1, 28, 184, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (499, 'Keine Aktivität', '2023-10-23 14:08:53.388188', '', '2023-10-22 22:00:00', 0, 28, 185, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (463, 'Die Verrechenbarkeit der Kundenaufträge ist afu dem höchsten Stand mit 98.7% ', '2023-10-16 12:33:14.566931', '', '2023-10-15 22:00:00', 33, 41, 150, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (462, 'Die Absolute Verrechenbarkeit inkl Wartung und Lösung liegt bei 72.3 %', '2023-10-16 12:34:51.451789', '', '2023-10-15 22:00:00', 20, 41, 151, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (466, 'Vorbereitungen für das Kickoff vom 31. Oktober sind in vollem Gange', '2023-10-16 13:46:57.536417', '- Fragen erarbeiten
- Projektteam informieren', '2023-10-15 22:00:00', 0.1, 49, 149, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (467, 'Status quo aufgrund von Ferien', '2023-10-16 13:47:02.691283', '', '2023-10-15 22:00:00', 0, 26, 145, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (468, 'Teil 2 (Analyse) aus Blogserie wurde veröffentlicht. Teil 3 (Schlussfolgerung) ist in der Pipeline. Interner Dagger-Workshop mit 4! Entwicklern durchgeführt.', '2023-10-16 13:47:25.252339', '', '2023-10-15 22:00:00', 55, 59, 196, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (471, 'Grundsätze während Workshop im Team diskutiert', '2023-10-17 04:51:33.138736', 'Ergebnisse ausformulieren und anschliessend teilen mit SUM Buddy und am AUMC', '2023-10-16 22:00:00', 0.3, 3, 218, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (472, 'sehr tiefe abs. Verrechenbarkeit wegen Start Tobi, Herbstferien, Abwesenheiten, OKR Tool', '2023-10-17 04:59:33.184273', 'Auslastung bei allen erhöhen so schnell wie nur möglich', '2023-10-16 22:00:00', 0, 36, 217, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (473, 'FMH = CHF 170 / HRM Systems = CHF 160 / Swissrail = CHF 170', '2023-10-17 05:03:14.587353', '', '2023-10-16 22:00:00', 0.3, 36, 216, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (470, 'Erster Draft steht', '2023-10-17 05:04:34.841911', 'Finalisieren und Prozess definieren um damit zu arbeiten', '2023-10-16 22:00:00', 30, 3, 214, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (474, 'Noch keine Massnahmen umgesetzt. ', '2023-10-17 07:26:44.846965', '', '2023-10-16 22:00:00', 8.86, 3, 210, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (469, 'Bestellungen von P. Murkowsky für das ganze Jahr 2024 erhalten, Planwert Q1 ohne Berücksichtigung von Ferien', '2023-10-17 07:30:23.570138', '', '2023-10-16 22:00:00', 75954, 36, 215, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (475, 'mündliche Zusage für diverse Verlängerungen', '2023-10-18 08:29:05.576158', '', '2023-10-17 22:00:00', 30, 60, 200, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (476, 'Warten auf OK, dass wir die DR dem Team präsentieren dürfen. ', '2023-10-18 08:30:27.147867', '', '2023-10-17 22:00:00', 0, 60, 201, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (478, '106/239', '2023-10-18 13:03:37.895884', '', '2023-10-17 22:00:00', 44, 80, 211, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (479, 'Jenkins läuft (fast) einwandfrei und ist Teils gut dokumentiert https://gitlab.puzzle.ch/okr_pitc_members/guides/-/merge_requests/689', '2023-10-19 08:08:36.168803', '', '2023-10-18 22:00:00', 55, 78, 213, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (480, 'Durschnittswert für alle mobility Teams im September: 8.40: Members schätzen zusammenhaltsförderende Massnahmen.', '2023-10-19 08:38:15.241921', '', '2023-10-18 22:00:00', 0.2, 20, 164, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (481, 'FIS/KI-Bestellung für 2024 ist eingegangen', '2023-10-19 08:41:17.692144', '', '2023-10-18 22:00:00', 0.2, 20, 170, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (477, '18 Anmeldungen für mindestens einen Tag.', '2023-10-30 11:47:33.571753', '', '2023-10-17 22:00:00', 30, 31, 199, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (483, '-0.9 Christian Schlatter', '2023-10-19 12:08:17.336471', '', '2023-10-18 22:00:00', -0.9, 13, 131, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (484, 'Stand Mitte Oktober: bis jetzt nur RHEL für MediData', '2023-10-19 12:11:19.882851', '', '2023-10-18 22:00:00', 70, 16, 141, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (485, 'Vorhandene Bestellungen: Ramona, Andreas, Stefan', '2023-10-19 13:56:00.311793', '', '2023-10-18 22:00:00', 0.5, 29, 168, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (427, 'Puzzle Grundsätze ergänzt und am 4.10.23 kommuniziert', '2023-10-19 15:00:28.789266', '', '2023-10-04 22:00:00', 0.3, 13, 140, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (486, 'Absolute Verrechenbarkeit September: 45.3% - Durchschnitt: 45.3%', '2023-10-20 09:11:12.400317', '', '2023-10-19 22:00:00', 0, 22, 143, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (487, 'leider immer noch Status quo, bin noch nicht dazu gekommen', '2023-10-23 07:02:21.553648', '', '2023-10-22 22:00:00', 0, 26, 145, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (488, 'Stand 23.10.: 1721 Follower auf LinkedIn / 2 technische Blogposts / 1 Referenz Swisscom in der Pipeline für nächste Woche', '2023-10-23 07:34:00.702182', '', '2023-10-22 22:00:00', 0.2, 26, 147, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (489, 'nix action', '2023-10-23 14:00:40.51951', '-', '2023-09-29 22:00:00', 0, 34, 62, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (490, 'Ruby update fast umgesetzt', '2023-10-23 14:01:45.124235', 'Klären Status andere Migrationen', '2023-10-22 22:00:00', 0.2, 28, 179, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (491, 'Budget ist bekannt, erste auslegeorndung', '2023-10-23 14:02:08.960647', '', '2023-10-22 22:00:00', 0.2, 28, 180, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (492, 'Noch keine Aktivitäten', '2023-10-23 14:02:32.713582', '', '2023-10-22 22:00:00', 0, 28, 182, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (493, 'Noch nichts eingeplant', '2023-10-23 14:04:02.205373', '', '2023-10-22 22:00:00', 0, 28, 183, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (494, '-', '2023-10-23 14:05:34.12223', 'warten auf Rückfluss der Umfrage', '2023-10-22 22:00:00', 0, 32, 189, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (496, 'September 62%', '2023-10-23 14:08:09.730685', '', '2023-10-22 22:00:00', 0, 28, 181, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (497, 'Umfrage verschickt, warten auf Antworten', '2023-10-23 14:08:13.865145', 'warten', '2023-10-22 22:00:00', 0.4, 67, 188, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (500, 'Keine Aktivität', '2023-10-23 14:09:05.838589', '', '2023-10-22 22:00:00', 0, 28, 186, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (501, '0', '2023-10-23 14:09:09.835057', 'Leider keine Antwort mehr von Centris AG bezüglich Ascender', '2023-10-22 22:00:00', 0, 32, 175, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (502, 'noch nicht mehr Module, aber mehrere Sys Members sind daran', '2023-10-23 14:10:09.881866', 'OPNsense Weekly eng begleiten', '2023-10-22 22:00:00', 2, 32, 176, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (503, '0.2', '2023-10-23 14:11:00.405292', 'Blogpost ist weiter am voranschreiten', '2023-10-22 22:00:00', 0.4, 34, 177, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (504, 'Secondary TV sind verteilt und organisiert', '2023-10-23 14:11:48.975335', 'weiter so', '2023-10-22 22:00:00', 0.8, 67, 174, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (505, 'weiter daran', '2023-10-23 14:12:35.416948', '-', '2023-10-22 22:00:00', 0.3, 67, 172, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (508, 'Aktuell werden in der neben den WTOs verbleibenden Zeit bestehende Kontakte angegangen. EJPD WTO schon weit fortgeschritten und wird am 30.10 eingereicht. Fragen für BFE WTO eingereicht. ', '2023-10-25 13:04:53.480149', '', '2023-10-24 22:00:00', 0.15, 30, 162, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (510, 'Von 17 Massnahmen sind 2 umgesetzt.', '2023-10-25 13:49:25.752404', 'Nach dem mob-weekly vom 25.10. sind 6 von 17 Massnahmen umgesetzt', '2023-10-24 22:00:00', 0.15, 5, 163, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (511, 'Keine', '2023-10-25 14:13:22.856958', 'Gemeinsames Verständnis wird im /mob-coeur Team am 30.11. geschaffen + am 2.11. im LST Team Workshop', '2023-10-24 22:00:00', 0.2, 5, 166, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (512, 'Keine', '2023-10-25 14:16:20.029259', '', '2023-10-24 22:00:00', 0, 5, 161, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (513, 'Workshop mit msc und jbl für Supportorganisation Haag-Streit, Webtransfer ist aufgesetzt', '2023-10-25 14:18:28.322801', '', '2023-10-24 22:00:00', 0.4, 5, 169, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (515, 'Haltung des Coreteams kommuniziert, mobility-Aktivitäten ausgewertet und erste Anpassungen umgesetzt.', '2023-10-25 15:12:34.615366', '', '2023-10-24 22:00:00', 0.7, 20, 164, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (516, 'unverändert zur letzten Messung', '2023-10-25 15:47:59.42736', '', '2023-10-24 22:00:00', 0.2, 20, 170, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (517, 'Lab Angebot ist auf der Puzzle Webseite einsehbar', '2023-10-26 05:55:21.204027', '', '2023-10-25 22:00:00', 0.2, 51, 159, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (518, 'www.puzzle.ch/mlops ist verfügbar und gibt einen ersten Einblick in unser Angebot', '2023-10-26 05:56:00.920221', '', '2023-10-25 22:00:00', 0.2, 51, 160, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (519, 'Unverändert', '2023-10-27 11:40:23.008943', '', '2023-10-26 22:00:00', 0.3, 13, 140, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (520, 'Noch unverändert, nächste Woche werden wir aber die Puzzle Subseite online schalten und die Kampagne starten (inkl. Mailings an die persönlichen Kontakte) ', '2023-10-27 11:41:34.013788', '', '2023-10-26 22:00:00', 0, 13, 129, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (482, 'Zwischenstand Mitte Oktober 2023', '2023-10-27 11:44:26.144513', '', '2023-10-18 22:00:00', 592312, 16, 142, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (522, 'Draft finalisiert, Daten ausgefüllt, Prozess definiert. ', '2023-10-27 18:38:21.654319', 'Ausführen', '2023-10-26 22:00:00', 100, 3, 214, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (523, 'Wert in PTime immer noch gleich - zusätzliche mündliche Zusage für Mobi und SwissLife (jeweils eine Offerte versendet), Offerte BLS im November', '2023-10-27 18:39:29.958628', '', '2023-10-26 22:00:00', 75954, 36, 215, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (524, 'Auf Grund von WTO Aufwand stand das Thema still. ', '2023-10-27 18:43:20.616442', 'Siehe vorangehende Messung. ', '2023-10-26 22:00:00', 0.3, 3, 218, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (525, 'Aktuell nur Verlängerungen bisheriger Projekte im okr_pitch mit WTOs müssen wir die Stundensätze leider taktisch tiefer ansetzen (Stand SL: 170, Mobi: 190, OIZ 160-175)', '2023-10-27 18:45:11.361734', '', '2023-10-26 22:00:00', 0.3, 36, 216, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (565, 'cbe 70%, nkr 75% +++ Totale verrechenbare Kapa: 1040% +++ 13.9% der verrechenbaren Pensen sind für Jan/Feb definitiv geplant.', '2023-10-30 12:07:36.935283', 'Vollgas geben bei Sales und Akqui...', '2023-10-29 23:00:00', 13.9, 33, 171, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (566, 'Weitere Zusage', '2023-10-30 12:10:49.538091', '', '2023-10-29 23:00:00', 35, 60, 200, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (526, 'September 59.3& - Gründe: Eintritte ohne Aufträge, Gotte/ Götti, Techworkshop, Analyse SODB, Q&A-Session, Blog-Artikels, Referenzberichte, Hupf n.v. - Simu: In Mobi-Gaia-Auftrag onBoardet. Hupf: Start mit BKD', '2023-10-30 05:37:59.492509', 'Simu: Simu: Opportunität Spring Boot Training, Clara: Onboarding SWOA W&S und MOBI Liima / SWOA Athletenbiografie Offerte & Vertrag forcieren, Chrigu: Quarkus-Training ', '2023-10-29 23:00:00', 0, 4, 207, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (527, 'MOBI-Gaia: Frühstens im Nov bekannt ob Budget für 2024 - SWZH-cADC: Geht mind bis Jan24, danach noch offen, SWOA: Situation offen - BUAR: SAP-HANA inzwischen völlig offen wann', '2023-10-30 05:43:27.803912', 'MOBI: Hurni Anfang Nov kontaktieren - SWOA: Stephan Anfang Nov angehen - BUAR: Meeting vorbereiten und aktiv kommunizieren', '2023-10-29 23:00:00', 0.1, 4, 208, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (528, 'Aktiv in diversen WTOs - Individualsoftware-Projekte keine Anfragen', '2023-10-30 05:44:37.276495', 'Aktivitäten gemäss Beschreibung ', '2023-10-29 23:00:00', 0, 4, 209, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (529, 'N/A', '2023-10-30 05:45:40.59497', 'Aktivitäten gemäss Beschreibung ', '2023-10-29 23:00:00', 0, 4, 206, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (530, 'Quartals-Workshop und 1 Teammeeting durchgeführt - I will/nicht Sheet & Wortwolke generiert - Neue Version von Manifest (aka Ambitionen) geschrieben - Feedback zu Unser Motto (aka wie wir arbeiten) gesammelt', '2023-10-30 05:49:49.03166', '2 Teammeetings - Manifest und Unser Motto verabschieden - Agenda über mehrere Teammeetings erstellt', '2023-10-29 23:00:00', 0.4, 4, 205, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (531, 'Tech-Elemente diskutiert - Arbeit an Techradar begonnen - Grundsatzfragen gestellt', '2023-10-30 05:52:10.708597', 'Definieren wie weiter - Zweck der einzelnen Tech-Elemente definieren - Besprechung im nächsten Team-Meeting', '2023-10-29 23:00:00', 0.2, 4, 202, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (532, 'Ambitionen (neu Manifest) und Arbeit im Team (neu unser Motto) besprochen - Neue Verison von Manifest vorhanden', '2023-10-30 05:53:58.315177', 'Update Unser Motto erstellen - Manifest und Unser Motto im nä. Teammeeting besprechen & verabschieden', '2023-10-29 23:00:00', 0.2, 4, 204, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (521, 'Stand Ende Oktober 2023. Wir sind noch weit von den Zielwerten entfernt. Die Planungen stehen noch aus und es fehlen noch Aufträge für das neue Jahr.', '2023-10-30 07:37:59.413609', '', '2023-10-26 22:00:00', 601280, 16, 142, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (533, 'Noch keine weiteren wesentlichen Veränderungen. Der nächste Abgleich im Team findet am 7.11. statt. Marcom prüft bestehende Referenzen nach Cloud-Referenz-Eignung.', '2023-10-30 07:40:27.985989', '', '2023-10-29 23:00:00', 0, 16, 130, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (534, 'Noch keine wesentlichen Veränderungen. Abgleich mit /sys steht noch aus. Hinsichtlich APPUiO treffen wir uns intern am 7.11.', '2023-10-30 07:42:15.450577', '', '2023-10-29 23:00:00', 0, 16, 133, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (535, 'Puppet Subs für SHKB. Ausblick/noch offen: Tamedia, RhB, EveryWare, TailorIT, Lonza, Unblu, Flughafen ZH, ETH, Centris, Uni Bern.', '2023-10-30 07:50:28.994938', '', '2023-10-29 23:00:00', 1696, 16, 141, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (536, 'Abstimmung zwischen abe und mw am 26.10. vorgenommen. Weiteres Vorgehen und Workshop skizziert. Info an LST am 30.10. Workshop soll in der 2. oder 3. Dezember-Woche stattfinden.', '2023-10-30 07:53:08.027702', '', '2023-10-29 23:00:00', 0.11, 16, 138, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (537, 'Hit: Jenkis und Ruby 3 durchgeführt, SWEB jenkins geplant', '2023-10-30 09:19:14.721872', '', '2023-10-29 23:00:00', 0.5, 28, 179, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (538, 'Keine Veränderung', '2023-10-30 09:19:37.406853', 'Nächstes Abgleich geplant', '2023-10-29 23:00:00', 0.2, 28, 180, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (539, 'Auslegeornung erstellt, wird am Teammeting besprochen.', '2023-10-30 09:20:24.652354', '', '2023-10-29 23:00:00', 0.1, 28, 182, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (540, 'SWEB mündliche Zusage, BAFU geht Marc B. an. Brixel keine Planung möglich, SWUN geplant', '2023-10-30 09:22:51.90616', 'Zusagen einholen', '2023-10-29 23:00:00', 0, 28, 183, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (541, 'Keine Veränderung, Monatsabschluss noch ausstehend', '2023-10-30 09:40:27.938942', '', '2023-10-29 23:00:00', 0, 28, 181, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (542, 'Keine Veränderung', '2023-10-30 09:41:04.155078', 'Techinterview einplanen', '2023-10-29 23:00:00', 0.1, 28, 184, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (543, 'Ruby Chartä erstellt, Keine Aktivität', '2023-10-30 09:41:37.894306', '', '2023-10-29 23:00:00', 0.1, 28, 185, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (544, 'Keine Aktivität', '2023-10-30 09:41:56.326104', '', '2023-10-29 23:00:00', 0, 28, 186, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (545, 'Kickoff steht kurz bevor', '2023-10-30 09:49:55.914181', '', '2023-10-29 23:00:00', 0.1, 49, 149, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (546, 'Aktuell weiterhin tiefe Auslastung (non-billable projekte, abwesenheiten, Aufwand/Überzeit WTO) - stand 47%', '2023-10-30 10:59:53.295145', 'Auslastung erhöhen, nicht verrechenbaren Anteil vermindern.', '2023-10-29 23:00:00', 0, 36, 217, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (547, 'Noch keine Massnahmen umgesetzt. Aktuelle Messung basiert nur auf 3 Zahlen', '2023-10-30 11:04:24.839969', 'Messung verbessern', '2023-10-29 23:00:00', 8.33, 3, 210, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (548, 'Blog ist in Prüfung ', '2023-10-30 11:29:52.398037', '', '2023-10-29 23:00:00', 0.1, 41, 158, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (549, 'Aus Zeitgründne nicht weitergemacht ', '2023-10-30 11:30:42.290726', '', '2023-10-29 23:00:00', 0.1, 41, 194, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (550, 'Retro wurde durchgeführt und Q4 geplant und Massnahmen definiert ', '2023-10-30 11:31:36.161388', '', '2023-10-29 23:00:00', 0.3, 41, 157, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (551, 'Nächstes Teamforum am 24.11. ist reserviert um mit dem Team die Marktopportunitäten durchzugehen und zu besprechen, was für uns relevant ist und wo wir uns wie einbringen können.', '2023-10-30 11:33:39.966384', '', '2023-10-29 23:00:00', 0.1, 33, 193, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (553, 'September 1.2 Mio somit weiterhin 0', '2023-10-30 11:35:20.365247', '', '2023-10-29 23:00:00', 0, 24, 144, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (556, 'Aufgrund angespannter Auslastungssituation und noch grossen Unsicherheiten bezügl. Planung ab Anfang 2024, wird der Workshop mit dem ganzen Team noch nicht geplant. Je nach Planungsentwicklung wird er ins Q3 verschoben...', '2023-10-30 11:36:56.627061', 'Sales und Akquise first!', '2023-10-29 23:00:00', 0.01, 33, 192, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (558, 'Rentabilitätsrechnung von Marcel erhalten. Leider ohne Formeln. Voranalyse soweit möglich hinsichtlich LST-Workshop vorgesehen.', '2023-10-30 11:39:10.781882', '', '2023-10-29 23:00:00', 15, 33, 173, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (559, 'Ziel im September erreicht! Divisionsertrag für September: 294''413.- // Abzüglich Aufwand von Sublieferanten: 294''413 - 33''076 = 261''337.-', '2023-10-30 11:43:47.553607', 'Ausblick für Oktober jedoch schlechter. Daher dranbleiben...', '2023-10-29 23:00:00', 261337, 33, 190, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (560, 'Messwert 8.0', '2023-10-30 11:46:47.7935', '', '2023-10-29 23:00:00', 30, 31, 219, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (562, 'Absolute Verrechenbarkeit im September war 75.3%.', '2023-10-30 11:47:28.287133', '', '2023-10-29 23:00:00', 75.3, 33, 178, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (563, '20 Anmeldungen', '2023-10-30 11:47:45.453099', '', '2023-10-29 23:00:00', 40, 31, 199, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (564, 'Noch keine Mandate für 2024 fix verlängert. Daran bei FZAG, Centris, MeteoSchweiz, Interdiscount, KSGR, Mobi (ICT Support Engineer), SNB (AAP).

Reelle Chancen für Erhöhungen: KSGR (neu 200.- anstatt 180.-), Mobi (165.- auf 175.-)', '2023-10-30 11:54:23.467645', '', '2023-10-29 23:00:00', 0, 33, 191, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (552, 'Jubla ist verschoben auf Januar  (Kundenwunsch)
sehr viele Probleme SKV (Nextcloud), SWW (Suche), PBS diverses, SBV (Suisa) ', '2023-10-30 12:43:28.813451', 'Retro durchführen (dringende Nacharbeiten ausgelöst)', '2023-10-29 23:00:00', 0.3, 65, 155, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (567, 'Swisscom Referenz am 29. Oktober publiziert', '2023-10-31 08:15:54.592242', '', '2023-10-30 23:00:00', 0.5, 26, 147, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (568, 'Kickoff auf Mitte November verschoben, damit alle Stakeholder teilnehmen können.', '2023-10-31 08:16:42.072048', '', '2023-10-30 23:00:00', 0.1, 49, 149, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (570, 'Gemeinsames Verständnis im mobcoeur Team geschaffen', '2023-11-01 10:30:38.356329', '', '2023-10-31 23:00:00', 0.25, 5, 166, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (569, '10 von 18 Massnahmen sind umgesetzt', '2023-11-01 10:40:23.62532', '', '2023-10-31 23:00:00', 0.25, 5, 163, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (767, 'Neue Anfragen von BLS und aXenta', '2023-12-07 09:38:08.859787', '', '2023-12-06 23:00:00', 0.7, 30, 162, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (571, 'für alle bestehenden Members werden Mandate verlängert plus Paco mit kleinem Pensum bei Siemens  ', '2023-11-01 15:03:50.593635', '', '2023-10-31 23:00:00', 0.9, 5, 167, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (572, 'Eine Anmeldung zum Ansible++ Workshop', '2023-11-06 08:38:00.104003', '', '2023-11-05 23:00:00', 0.25, 84, 224, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (573, 'Ein Bewerber, dessen Fähigkeiten unseren Anforderungen genügen. Die Gehaltsforderungen können wir jedoch nicht erfüllen.', '2023-11-06 08:40:41.887643', 'Lohnmodell überprüfen, Anpassungen vornehmen (Inflationsausgleich, ...)', '2023-11-05 23:00:00', 10, 84, 223, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (574, 'Anfrage von der Deutschen Kautionskasse, noch nicht angeboten.', '2023-11-06 08:46:18.193221', 'Angebote erstellen, weitere Prospects identifizieren.', '2023-11-05 23:00:00', 2, 17, 222, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (575, 'Bisher keine positive Rückmeldung erhalten.', '2023-11-06 08:47:16.335714', 'Weitere Kunden mit Terminvorschlägen anschrieben', '2023-11-05 23:00:00', 0, 17, 221, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (576, 'Bisher keine Aufträge für 2024.', '2023-11-06 08:48:19.138046', 'Kunden auf Kapazität hinweisen', '2023-11-05 23:00:00', 20000, 17, 220, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (577, 'Keine Veränderung', '2023-11-06 15:05:23.667431', '', '2023-11-05 23:00:00', 0.5, 28, 179, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (578, ' Keine Veränderung ', '2023-11-06 15:05:40.441651', '', '2023-11-05 23:00:00', 0.2, 28, 180, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (579, 'Prov. für Oktober: 73% > Durchschnitt 67.5', '2023-11-06 15:07:36.951528', '', '2023-11-05 23:00:00', 1, 28, 181, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (580, 'Keine Veränderung', '2023-11-06 15:27:22.314373', '', '2023-11-05 23:00:00', 0.1, 28, 182, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (581, 'BAFU mündliche Aussage, sonst keine Veränderung', '2023-11-06 15:30:09.706303', '', '2023-11-05 23:00:00', 1224, 28, 183, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (582, '2. Gespräch geplant', '2023-11-06 15:30:34.276184', '', '2023-11-05 23:00:00', 0.1, 28, 184, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (583, ' Ruby Chartä überarbeitet', '2023-11-06 15:31:12.932284', 'PL-Sync geplant', '2023-11-05 23:00:00', 0.2, 28, 185, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (584, ' Keine Aktivität ', '2023-11-06 15:31:24.50995', '', '2023-11-05 23:00:00', 0, 28, 186, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (587, 'Status quo', '2023-11-07 07:46:41.354708', 'sf klärt am 9.11. mit db, was mit strategischem Ausbildungsbudget möglich ist und knüpft anschliessend ein "Päckli". Anschliessend werden potenzielle Members via Members Coach kontaktiert. ', '2023-11-06 23:00:00', 0.1, 16, 130, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (588, 'Leider Status quo', '2023-11-07 07:53:02.664095', 'Inhaltskonzept anschauen und an nächstem SUM mit db besprechen', '2023-11-06 23:00:00', 0, 26, 145, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (589, 'Status quo wegen Abwesenheit mt', '2023-11-07 08:42:11.359823', 'sje übernimmt das Thema und definiert die letzten Details mit der GL (via SUM db)', '2023-11-06 23:00:00', 0.2, 48, 146, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (590, 'Oktoberzahlen im Vergleich zu letztem Jahr sehr gut.', '2023-11-07 08:44:07.830018', 'Wir machen weiter so', '2023-11-06 23:00:00', 29.5, 49, 148, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2644, 'Unverändert', '2024-09-16 08:08:19.175347', '', '2024-09-16 08:08:19.175374', NULL, 28, 1302, 5, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (591, 'Web Components Blogpostserie veröffentlicht. In Planung: Referenz BKD, Blogpost String Templates Java', '2023-11-07 08:49:16.064416', 'wir sind auf Kurs', '2023-11-06 23:00:00', 0.6, 26, 147, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (593, 'Gemeinsames Verständnis geschaffen. ', '2023-11-08 10:29:07.429987', 'Kommunikation zu den Members ist geplant. Zuerst in Subteams, dann im Infoanlass /mobility. Allerdings folgt noch eine Puzzle weite Info', '2023-11-07 23:00:00', 0.25, 5, 166, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (586, 'Gemäss PTime betrug die absolute Verrechenbarkeit 71.0% deutlich unter Zielwert. Gründe: Jenkins Migration, Eingliederung Krankheitsfälle auf interne Projekte, Grösserer Aufwand Lernende, MLOps Lab (Marktopportunität)', '2023-11-08 11:44:05.262069', '', '2023-11-05 23:00:00', 0, 5, 161, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (592, '11 von 19 Massnahmen sind umgesetzt', '2023-11-08 11:57:15.017989', '', '2023-11-07 23:00:00', 0.25, 5, 163, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (594, 'MC Uno bestellt (alte Stundensätze). Noch offen cbr, nb, ktr, aba', '2023-11-08 12:28:53.39637', '', '2023-11-07 23:00:00', 0.6, 29, 168, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (595, 'EJPD wo wir massgeblich im Lead waren eingereicht. Ebenfalls OIZ und BIT wo wir mitgearbeitet haben. BFE am Laufen. Allenfalls Teilname BAG. Zuschlagsentscheid ewb sollte im Nov. erfolgen. Zudem wurden 9 Kontakte angegangen.', '2023-11-08 16:55:42.578552', '', '2023-11-07 23:00:00', 0.5, 30, 162, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (597, 'Abstimmung Lopas und BAUS stattgefunden. Abstimmung über gemeinsames BLS Team mit Daria nötig.', '2023-11-09 13:36:25.219525', '', '2023-11-08 23:00:00', 0.2, 20, 170, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (598, 'Kampagne geplant für Mitte November - Ende November', '2023-11-09 13:39:46.280469', '', '2023-11-08 23:00:00', 0.2, 51, 160, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (599, 'Lab ist für Februar geplant und wird noch in der Kampagne und im Newsletter kommuniziert', '2023-11-09 13:41:18.215618', '', '2023-11-08 23:00:00', 0.2, 51, 159, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (596, 'Massnahme: mobility Töggeliturnier nicht weitergeführt und an Members kommuniziert.
Happiness von Zoo & Bandits ausgewertet. Savvy Badgers folgt.', '2023-11-09 15:23:54.18872', '', '2023-11-08 23:00:00', 0.8, 20, 164, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (600, 'Alle bestehenden Bestellungen erfolgen fürs 2024', '2023-11-09 16:51:44.491586', 'Tracken, wann und ob mit den richtigen Rates die Bestellungen eingetroffen sind.', '2023-11-08 23:00:00', 0.8, 29, 168, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (601, 'Es fehlt nach wie vor sehr viel Umsatz im Forecast.', '2023-11-10 16:25:48.178823', 'Sales Sprint Auslastung 2024 am 7.11. gestartet.', '2023-11-09 23:00:00', 798257, 16, 142, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (602, 'Ausbildungspäckli sind definiert (3 PT strategisches Ausbildungsbudget in diesem GJ ergänzend zum persönlichen Ausbildungsbudget) und Members werden nun via MCs angesprochen.', '2023-11-10 16:28:24.017541', '', '2023-11-09 23:00:00', 0.15, 16, 130, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (603, 'Workshop geplant für am 07.12.23 an der Belpstrasse in Bern und Einladung erfolgt. Als nächster Schritt das Design des Workshops.', '2023-11-10 16:31:32.424131', '', '2023-11-09 23:00:00', 0.15, 16, 138, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (604, 'Subsite erstellt und aufgeschaltet, Sales Kampagne gestartet, jedoch noch kein neues agiles Team offeriert. ', '2023-11-11 15:40:28.78313', '', '2023-11-10 23:00:00', 0, 13, 129, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (605, 'Kickoff für Self Assessment organisiert (findet am 23.11 statt). Self Assessment wird voraussichtlich erst im nächsten Quartal statt finden.', '2023-11-11 15:41:42.065271', '', '2023-11-10 23:00:00', 0.4, 13, 140, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (608, 'MOBI: Wöchentlicher Austausch mit P. Hurni (GAIA), Situation verbessert, evtl. 2FTE für 2024 - BUAR: Vorbereitung November Austausch - SCZH: Abschluss zieht sich in Q1, 2024 - MOBA: Nix im 2024', '2023-11-12 08:19:07.602719', 'SWOA: Call mit Stephan durchführen, Planung 2024 mit Marc angehen', '2023-11-11 23:00:00', 0.2, 4, 208, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (629, 'Die absolute Verrechenbarkeit inkl Wartung und Lösung liegt im Oktober bei 67.3 % ', '2023-12-08 14:15:02.38032', '', '2023-11-19 23:00:00', 40, 41, 151, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (609, 'Verbesserte abs. Verrechenbarkeit im Oktober: 66% (Gemäs PTime), November aktuell 75%. Es geht aufwärts. Simu in Mobi onboardet. Clara auf Liima angesetzt', '2023-11-12 08:28:50.181533', 'Weiterhin Leute onboarden. Nicht verrechenbare Arbeit auf Monate verteilen. Pensenreduktion Clara. Unbezahlt Marc (Nov) und evtl. Clara (Dez)', '2023-11-11 23:00:00', 0.1, 4, 207, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (607, 'Weekly Sales-Abgleich mit Sales-Buddy /dev/tre (mw) etabliert, Swico ICT analysiert, Profile BIT-WTO eingegeben, Moser-Bäer abgeklärt (kommt nichts in 2024)', '2023-11-12 08:32:18.184801', 'Swico: Präsi nach Mitte Dezember verschieben - POST: Kontakt von Benji anschreiben - MOBI: Mit Pädu zum Kafi abmachen, Marcos schreiben', '2023-11-11 23:00:00', 0.1, 4, 209, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (610, 'Status Quo', '2023-11-12 08:33:11.564256', 'Aktivitäten gemäss Beschreibung ', '2023-11-11 23:00:00', 0, 4, 206, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (611, 'Status Quo. Nächstes Teammeeting am 14.11.2023', '2023-11-12 08:34:15.246473', '2 Teammeetings - Manifest und Unser Motto verabschieden - Agenda über mehrere Teammeetings erstellt ', '2023-11-11 23:00:00', 0.4, 4, 205, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (612, 'Wortwolke "I like/ i don''t like" erstellt, d.h. Präfernzen und Heterogenität sichtbar', '2023-11-12 08:36:03.387429', 'Definieren wie weiter - Zweck der einzelnen Tech-Elemente definieren - Besprechung im nächsten Team-Meeting ', '2023-11-11 23:00:00', 0.2, 4, 202, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (613, 'Status Quo', '2023-11-12 08:38:58.791585', ' Update Unser Motto erstellen - Manifest und Unser Motto im nä. Teammeeting besprechen & verabschieden ', '2023-11-11 23:00:00', 0.2, 4, 204, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (614, 'Keine Veränderung', '2023-11-13 07:56:25.85551', '', '2023-11-12 23:00:00', 0.5, 28, 179, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (615, 'Monatsabschluss noch ausstehend', '2023-11-13 07:58:38.660335', '', '2023-11-12 23:00:00', 1, 28, 181, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (616, 'Planung vorhanden', '2023-11-13 07:59:04.504392', '', '2023-11-12 23:00:00', 0.2, 28, 182, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (617, '- Abschluss Oktober abwartend', '2023-11-13 08:01:42.535005', '', '2023-11-12 23:00:00', 0, 24, 144, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (618, 'Erste Projekte fix eingeplant.', '2023-11-13 08:01:53.875384', 'Zusage SWEB am Herbstmeeting abholen, Vertrag BAFU klären', '2023-11-12 23:00:00', 75493, 28, 183, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (619, '2. Gespräch geplant ; Referenzen passen', '2023-11-13 08:02:37.610885', '', '2023-11-12 23:00:00', 0.5, 28, 184, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (620, 'Absprache findet bilateral statt. Noch kein gemeinsames Gefäss', '2023-11-13 08:03:04.886709', '', '2023-11-12 23:00:00', 0.2, 28, 185, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (621, 'Keine Veränderung aber auf Kurs. ', '2023-11-13 08:03:32.55082', '', '2023-11-12 23:00:00', 0.2, 24, 135, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (622, 'Gemeinsames Mittagessen vor dem letzten Teammeeting war gut', '2023-11-13 08:03:44.524061', '', '2023-11-12 23:00:00', 1, 28, 186, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (623, 'Monthly Einführung hat stattgefunden. AUMCs sind geplant', '2023-11-13 08:04:26.057226', '', '2023-11-12 23:00:00', 0.3, 24, 132, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (635, 'keine Veränderung', '2023-11-14 09:03:55.770823', '', '2023-11-13 23:00:00', 100, 3, 214, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (625, 'Strategie wurde definiert ', '2023-11-13 13:19:58.376878', 'wird am Communitymeeting vorgestellt ', '2023-11-12 23:00:00', 0.3, 41, 194, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2775, 'Keine Veränderung.', '2024-10-21 06:47:46.791839', '', '2024-10-21 06:47:46.791843', NULL, 3, 1423, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (627, 'SWW, die Mitte, SAC sind geplant ', '2023-11-13 13:22:34.037801', '', '2023-11-12 23:00:00', 0.2, 41, 154, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (631, 'Strechziel im Oktober erreicht mit Umsatz > 100''000', '2023-11-24 08:56:37.001782', '', '2023-11-19 23:00:00', 107761, 41, 152, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (624, 'Blogpost erstellt, Jubla hat bestellt ', '2023-11-13 13:28:19.46421', '', '2023-11-12 23:00:00', 0.7, 41, 158, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (626, 'Ziel ist fertig und kann nicht mehr erreicht werden', '2023-11-13 13:29:41.886836', 'Nacharbeiten sind ausgeführt -> Wartungsbudget wurde bei weitem überschossen
dringend Retro durchführen', '2023-11-12 23:00:00', 0.3, 65, 155, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (632, 'Centris, ETHZ, Aupri (TailorIT) und Flughafen ZH haben bestellt.', '2023-11-13 16:46:02.846886', '', '2023-11-12 23:00:00', 69328, 16, 141, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (633, 'Abgleich mit /sys noch offen. Bottleneck bei mw. Austausch hinsichtlich APPUiO hat stattgefunden.', '2023-11-13 16:47:33.470184', '', '2023-11-12 23:00:00', 0, 16, 133, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (634, 'Wir sind immernoch auf Kurs', '2023-11-14 08:27:14.593743', 'Weiter so', '2023-11-13 23:00:00', 27.4, 49, 148, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (636, 'haben Bestellung von SBB für P. Murkowsky verloren. SwissLife und Mobi definitive Zusagen noch ausstehend', '2023-11-14 09:06:16.315018', 'SwissLife und Mobi fixen', '2023-11-13 23:00:00', 0, 36, 215, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (637, 'keine Massnahmen umgesetzt', '2023-11-14 09:08:48.578208', '', '2023-11-13 23:00:00', 8.6, 3, 210, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (638, 'keine Veränderung seit letzter Messung', '2023-11-14 09:10:18.439252', '', '2023-11-13 23:00:00', 0.3, 3, 218, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (639, 'immer noch tiefe Auslastung (OKR Projekt, Abwesenheiten, Zukunftstag, Sales Aufwand)', '2023-11-14 09:13:28.007089', 'Interner Aufwand minimieren (OKR Projekt) und Auslastung so hoch wie möglich halten', '2023-11-13 23:00:00', 0, 36, 217, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (640, 'keine Veränderung seit letzter Messung', '2023-11-14 09:16:10.963976', 'für uns steht zur Zeit Auslastung im Fokus - Stundensatz an zweiter Stelle', '2023-11-13 23:00:00', 0.3, 36, 216, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (641, 'Keine Änderung', '2023-11-15 09:21:24.966394', '', '2023-11-14 23:00:00', 0, 5, 161, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (642, 'Keine Änderung', '2023-11-15 09:23:19.645419', '', '2023-11-14 23:00:00', 0.25, 5, 163, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (630, 'Die Verrechenbarkeit der Kundenaufträge ist im Oktober sehr gut mit 98.3%', '2023-11-24 08:57:11.732724', '', '2023-11-19 23:00:00', 33, 41, 150, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (643, 'Team Zoo und Team SBB sind informiert', '2023-11-15 09:25:14.820702', 'Team BLS als nächstes', '2023-11-14 23:00:00', 0.29, 5, 166, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (645, 'Abstimmungstermin mit Daria am 23.11.', '2023-11-16 07:36:58.71432', '', '2023-11-15 23:00:00', 0.2, 20, 170, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (646, 'NABI kleine Program Codes als Sublieferant von Garaio gewonnen', '2023-11-16 14:40:26.270689', '', '2023-11-15 23:00:00', 0.7, 30, 162, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (647, 'Alle Bestellungen sind zugesagt und bis auf Nathalie eingetroffen', '2023-11-16 15:03:03.535655', '', '2023-11-15 23:00:00', 0.9, 29, 168, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (648, 'Jenkis bei SWEB abgelöst', '2023-11-20 07:49:20.925367', 'Klären Open Shift Migration', '2023-11-19 23:00:00', 0.7, 28, 179, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (649, 'Issue 58 Ablösung Jenkins CentOS7 geschlossen', '2023-11-20 07:51:58.617216', '', '2023-11-19 23:00:00', 0.4, 28, 180, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (689, 'Umfrage wird im Rahmen der MAG durchgeführt. Info folgt.', '2023-11-24 06:05:45.842465', '', '2023-11-23 23:00:00', 0.6, 13, 140, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (650, 'In der /mid-Week Kubermatic auf mehreren Infrastrukturen aufgebaut und ausprobiert.', '2023-11-20 08:01:51.533691', 'Wird nicht weiterverfolgt, da als Alternaitive zu Rancher nicht geeignet. Blogpost bereits verfasst, muss noch reviewed und veröffentlicht werden.', '2023-11-19 23:00:00', 30, 40, 198, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (651, 'PoC ist abgeschlossen, Cilium läuft als aktives CNI auf dem LPG1-Cluster.', '2023-11-20 08:02:06.149982', 'Aktuell kein Use-Case für uns ersichtlich, um detaillierter zu vertiefen. Für OpenShift-Kunden unserer Meinung nach uninteressant. Cilium Enterprise Features für die meisten Kunden keine Killer-Features um Aufpreis rechtzufertigen.', '2023-11-19 23:00:00', 30, 40, 197, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (652, 'Nach Monatabschluss Oktober:  66.42% > Durchschnitt 64.21', '2023-11-20 08:02:42.452666', '', '2023-11-19 23:00:00', 0.4, 28, 181, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (653, ' Keine Veränderung ', '2023-11-20 08:06:49.693852', '', '2023-11-19 23:00:00', 0.2, 28, 182, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (654, 'Noch nicht fix eingeplant', '2023-11-20 08:08:57.704928', 'SWEB detail planung vornehmen. Zusage für 350k für das ganze Jahr vorhanden ', '2023-11-19 23:00:00', 75500, 28, 183, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (655, 'Gespräch war nicht erfolgreich. Zurück auf Feld 0', '2023-11-20 08:09:43.283022', '', '2023-11-19 23:00:00', 0, 28, 184, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (656, 'Keine Veränderung', '2023-11-20 08:10:03.408528', '', '2023-11-19 23:00:00', 0.2, 28, 185, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (657, ' Keine Aktivität ', '2023-11-20 08:10:17.644014', '', '2023-11-19 23:00:00', 1, 28, 186, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (658, '0.4', '2023-11-20 08:42:05.509731', 'Blogpost ist fertig, wartet auf Veröffentlichung', '2023-11-19 23:00:00', 0.8, 34, 177, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (659, '0.6', '2023-11-20 08:43:31.4127', 'Resultate der Umfragen sind eingegangen', '2023-11-19 23:00:00', 1, 67, 188, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (660, '0', '2023-11-20 08:48:01.030601', 'Leider bis jetzt keine neuen potenziellen Kunden, UniBe im Bereich HPC, aber Wahrscheinlichkeit gering', '2023-11-19 23:00:00', 0, 32, 175, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (661, '0.1', '2023-11-20 10:01:05.549651', 'SmartIT Offerte benutzen um Offering weiter zu verfeinern', '2023-11-19 23:00:00', 0.4, 67, 172, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (662, '0.5', '2023-11-20 10:02:15.072724', 'erstes Produktives Modul steht kurz vor der Veröffentlichung', '2023-11-19 23:00:00', 2.5, 32, 176, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (663, 'Retro wurde durchgeführt; - 4-5 Jahre technische Schuld abgebaut, inkl Pipline, Jenkins Technologie Wechsel,  neues Baseimage, GIT Hub Actions -> Kontext switch von M. Viehweger', '2023-11-20 10:21:32.107276', 'Quartalsplanung durchführen sowie Prüfung Betrieb

-> früher hat solches Middleware gemacht (Budget Betrieb)', '2023-11-19 23:00:00', 0.3, 65, 155, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (664, 'Planung wurde jsutiert und implementiert ', '2023-11-20 10:22:37.744636', '', '2023-11-19 23:00:00', 0.7, 41, 157, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (628, 'communitymeeting ist organisiert und slides sind erstellt ', '2023-11-20 10:23:17.853305', '', '2023-11-12 23:00:00', 0.3, 41, 156, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (665, 'Wir hatten 22 Teilneher, weit übre den letzten Communitymeetings. ', '2023-11-20 10:24:40.247108', 'Es sollten mehr Devs teilnehmen, wurde am Retro besprochen ', '2023-11-19 23:00:00', 1, 41, 156, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (666, 'Nächstes Teammeeting findet nach OKR Status Update statt.', '2023-11-20 12:36:11.316594', '', '2023-11-19 23:00:00', 30, 31, 219, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (667, 'Alle (23!) /mid Members haben an der /mid Week teilgenommen!!!!!', '2023-11-20 12:39:34.076434', '', '2023-11-19 23:00:00', 100, 31, 199, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (668, '0.1', '2023-11-20 13:09:05.288481', 'Wir sind dabei erste Ideen zu sammeln. z.B.: Infoblock am Weihnachtsanlass, Newsbeitrag', '2023-11-19 23:00:00', 0.1, 32, 189, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (669, 'Zu wenig Anmeldungen für Ansible EDA Schulung, daher Absage.', '2023-11-20 17:15:58.813006', 'Planung eigener Schulung', '2023-11-19 23:00:00', 0, 84, 224, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (670, 'Passende Bewerbung eingegangen', '2023-11-20 17:17:10.48193', '', '2023-11-19 23:00:00', 15, 84, 223, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (671, 'Angebot an dt. Kautionskasse kann nicht abgegeben werden, da Supportzeiten nicht abgedeckt werden können', '2023-11-20 17:18:10.0651', '', '2023-11-19 23:00:00', 2, 17, 222, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (672, 'Weitere Anfragen gesendet, noch keine Zusagen', '2023-11-20 17:19:06.611291', 'Interssenten flächig anschreiben', '2023-11-19 23:00:00', 0, 17, 221, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (673, 'Keine definitiven Aufträge erteilt, nur Absichten.', '2023-11-20 17:19:49.726318', '', '2023-11-19 23:00:00', 20000, 17, 220, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (674, 'Commit: Bereits erreicht und einige weitere technische Beitäge in der Pipeline bis zum Ende des Q2. Neue Follower bis jetzt: 35.', '2023-11-21 06:53:32.767057', 'BKD Referenz online. Diese Woche erscheinen zwei weitere techn. Beiträge: Java String & EDA. ', '2023-11-20 23:00:00', 0.7, 26, 147, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (675, 'Kickoff hat am 15.11. stattgefunden. Austausch technische Aspekte am 20.11. Weitere Vorgehen und Aufwandschätzungen sind definiert. Aktuell liegt der Fokus bei WLY für die Erarbeitung einer ersten Sitemap. ', '2023-11-21 06:57:05.428692', '-Dump für die Migration der Referenzen und Blogbeiträge, sobald entschieden ist, dass wie die Beiträge / Referenzen übernommen werden sollen.
- Anforderungen an Event-Mails an WLY schicken.', '2023-11-20 23:00:00', 0.2, 49, 149, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (676, 'Das finale Konzept inkl. next Steps ist bei der GL, Freigabe kurz bevor', '2023-11-21 07:34:08.525009', 'Nach GL-Freigabe: Vorstellung im Monthly, Terminplanung, Newsbeitrag', '2023-11-20 23:00:00', 0.3, 48, 146, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (677, 'Leider Status quo', '2023-11-21 07:34:35.74697', '', '2023-11-20 23:00:00', 0, 26, 145, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (680, 'Noch nicht alles ist fix, aber sieht gut aus.', '2023-11-21 10:51:32.611039', 'Planung muss jede Woche überarbeitet werden, da viele Kunden nur kleine Pensen 20%-40% wünschen. Sonst verlieren wir den Überblick. ', '2023-11-20 23:00:00', 65, 60, 200, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (679, 'DR wurde am Team vorgestellt und Fragen geklärt. DR wird bei Mid Austausch angeschaut aber noch kein KPIs definiert.', '2023-11-21 10:51:12.216788', '', '2023-11-20 23:00:00', 30, 60, 201, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (681, 'Alle Projekte erfasst. Ca die Hälfte aller Informationen sind vorhanden.', '2023-11-21 13:13:41.38978', '', '2023-11-20 23:00:00', 50, 31, 212, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (682, '221/239', '2023-11-21 13:31:25.836', '', '2023-11-20 23:00:00', 92, 31, 211, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (683, 'Unverändert', '2023-11-21 13:37:16.614646', '', '2023-11-20 23:00:00', 55, 59, 196, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (684, 'Keine Veränderung ggü dem letzten Mal', '2023-11-22 15:50:15.562337', '', '2023-11-21 23:00:00', 0.9, 29, 168, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (685, 'Wenig Zeit investiert seit dem letzten checkin. Müssen schauen, ob wir hier gross weiterkommen werden bis Ende Jahr.', '2023-11-23 11:54:05.149439', '', '2023-11-22 23:00:00', 60, 78, 213, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (678, '1.10.-21.11.2023 => 11''924 Visitors
1.10.-21.11.2022 => 8''343 Visitors', '2023-11-23 12:01:17.433836', '', '2023-11-20 23:00:00', 42.9, 49, 148, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (686, 'Lopas Bestellprozess am Laufen.
Budget für andere Projekte wird erst ab KW49 bekannt sein. Bestellungen werden dann wohl nicht gleich sofort folgen.', '2023-11-23 16:35:06.738951', '', '2023-11-22 23:00:00', 0.2, 20, 170, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (687, 'Keine Veränderung', '2023-11-24 06:03:42.35721', '', '2023-11-23 23:00:00', -1.3, 13, 131, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (690, 'POFI: CV Lukas für Angular eingegeben, S&P (Swico) Swico-Präsi (Vorgehen, Richkosten) eingegeben, Geht weiter, CSS: Kafka-Support 2024 soll kommen, WTOs: Bei 2 Leute eingegeben, Mobi: Nichts neues - ansonsten nix konkretes ', '2023-11-24 08:28:18.260639', 'S&P: Dran bleiben, CSS: Umfang klären, Vertrag aufsetzen - Ansonsten: Weiterarbeit mit den intensivierten Sales-Aktivitäten', '2023-11-23 23:00:00', 0.1, 4, 209, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (691, 'Mobi-Gaia: Bedarf 2FTE bestätigt, SWOA: Läuft aus ab 2025, lediglich Betrieb in 2024, BUAR: Weiterentwicklungsmassnahmen präsentiert & platziert', '2023-11-24 08:32:15.52454', 'MOBI-Gaia: 2FTEs für Lifecylce fixieren, BUAR: Für Weiterentwicklungen motivieren, Vertrag regeln, BKD: Abklären ob im 2024 was zu erwarten ist, MOBI-Quarkus: Bedarf 2024 klären', '2023-11-23 23:00:00', 0.2, 4, 208, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (692, 'Absolute Verrechenbarkeit Oktober: 48.2% - Durchschnitt: 46.7%', '2023-11-24 08:34:39.613569', '', '2023-11-23 23:00:00', 0, 22, 143, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (693, 'Blogpost veröffentlicht, am Community Meeting informiert, SAC wird Nexcloud auch integrieren', '2023-11-24 08:52:53.730971', '', '2023-11-23 23:00:00', 0.7, 41, 158, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (694, 'Rechnungsstrategie wurde am Communitymeeting vorgestellt und Feedback war positiv', '2023-11-24 08:54:00.433375', '', '2023-11-23 23:00:00', 0.7, 41, 194, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (695, 'Blasmusik ist auch geplant und Pfadi wurde angefragt ', '2023-11-24 08:54:55.479437', '', '2023-11-23 23:00:00', 0.25, 41, 154, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (696, 'Absolute Verrechenbarkeit Oktober 2023: 64.8% - Leicht gestiegen, noch far von gut. Viele Unverrechenbare Stunden geleistet: SWOA Analyse SODB, BUAR onboarding SImon', '2023-11-24 10:10:48.502557', 'Möglichst die Leute auslasten, wo wir können. Aktuelle Absolute Verrechenbarkeit (Nov23) sieht besser aus. Aktueller Stand PTime 77%', '2023-11-23 23:00:00', 0.1, 4, 207, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (697, 'Aufnahme und Diskussion der Themen (Pain Points, i Wish) im Teammeeting 14.11.2023', '2023-11-24 10:14:06.193162', 'Themen konsolidieren, Weiteres Vorgehen definieren - Ansonsten Aktivitäten gemäss Beschreibung', '2023-11-23 23:00:00', 0.1, 4, 206, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (698, 'Teammeeting 14.11. stattgefunden. Output zu Selbstorganisierten Teams als Retro vorhanden, Weiterarbeit an Manifest und Unser Motto an MarKom übergeben', '2023-11-24 10:17:36.769336', '1 Teammeeting, Verbesserung Daily, Überlegungen wie Retro zu integrieren ist (-> Ins Weekly, alle 2 Wochen im Weekly - Anforderungen an & Regeln für Retro)', '2023-11-23 23:00:00', 0.6, 4, 205, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (699, 'Status Quo', '2023-11-24 10:18:20.009718', 'Definieren wie weiter - Zweck der einzelnen Tech-Elemente definieren - Besprechung im nächsten Team-Meeting ', '2023-11-23 23:00:00', 0.2, 4, 202, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (700, 'Sales-Buddy/ Sales-Abgleich installiert - Mobi- und Agile-Teams-Sales-Streams  - Diverse Sales-Aktivitäten und Kundengespräche >> Alles Massnahmen, um Individualsoftwareentwicklung bei /dev/tre in Firma und nach aussen sichtbarer zu machen', '2023-11-24 10:21:56.433226', 'Sales-Aktivitäten hoch behalten - Update Unser Motto erstellen - Manifest und Unser Motto im nä. Teammeeting besprechen & verabschieden ', '2023-11-23 23:00:00', 0.5, 4, 204, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (701, 'Openshift sollte im nächsten Jahr starten.', '2023-11-24 10:23:27.599288', 'Klären für Zeitpunkt um Planung vorzunehmen', '2023-11-23 23:00:00', 0.7, 28, 179, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (703, 'Keine Veränderung ', '2023-11-24 10:24:35.220175', 'Klären Betriebskosten von Hitobito bezüglich WS23', '2023-11-23 23:00:00', 0.4, 28, 181, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (704, 'Keine Veränderung ', '2023-11-24 10:25:42.21092', 'Planung Projekte von Livia übernehmen', '2023-11-23 23:00:00', 0.2, 28, 182, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (705, 'Keine Veränderung', '2023-11-24 10:28:15.172989', 'Klären SWEB Planung', '2023-11-23 23:00:00', 75500, 28, 183, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (706, 'Keine Veränderung', '2023-11-24 10:28:57.003267', 'Helvetic Ruby für bekanntmachung', '2023-11-23 23:00:00', 0, 28, 184, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (707, 'PL Meeting wurde abgesagt, ev neu am Dienstag einplanen', '2023-11-24 10:29:38.913098', 'Rückmeldung einholen', '2023-11-23 23:00:00', 0.3, 28, 185, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (708, 'Erflogreiche Helvetic Ruby', '2023-11-24 10:30:15.782048', '', '2023-11-23 23:00:00', 1.5, 28, 186, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (709, 'Zahlen sind deutlich höher als bei letzter Messung, aber immer noch viel zu tief. Es fehlen noch 1.2 Mio bis zum Commit.', '2023-11-24 12:17:37.381546', 'Planung 2024 von Mobility und UX eintragen.', '2023-11-23 23:00:00', 1427000, 16, 142, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (710, 'Keine wesentlichen Veränderungen. Mit Nine als GCP Partner über Erfahrungen gesprochen. Nächster MO Team Sync am 30.11.23.', '2023-11-24 12:20:22.092013', '', '2023-11-23 23:00:00', 0.15, 16, 130, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (711, 'Keine wesentlichen Veränderungen gegenüber dem letzten Mal. APPUiO Thematik in GL besprochen. VSHN informiert, dass wir länger Zeit benötigen und es 2024 wird.', '2023-11-24 12:21:35.644476', '', '2023-11-23 23:00:00', 0, 16, 133, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (712, 'TX Group hat bestellt. Ausblick Cilium Subs PostFinance, RH Subs Quickline, TailorIT, BERNMOBIL, Mobiliar (längerfristig), SHKB, Uni Luzern, Kt. SO', '2023-11-24 12:27:16.454733', '', '2023-11-23 23:00:00', 70310, 16, 141, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (713, 'Eingeladene Personen können sich Workshop-Teilnahme am 07.12.23 einrichten. Vorbereitung Workshop läuft.', '2023-11-24 12:30:23.213689', '', '2023-11-23 23:00:00', 0.15, 16, 138, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (714, '71.7 offizielle Zahl Marcel', '2023-11-24 14:20:45.098351', '', '2023-11-23 23:00:00', 0, 5, 161, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (715, '14 von 18 umgesetzt 80% (wo von 3 im Rahmen der Strategieerarbeitung) ', '2023-11-24 14:26:21.077864', 'Sales Schulung vorbereitet', '2023-11-23 23:00:00', 0.4, 5, 163, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (716, 'Alle Teams sind informiert. Wesentliche Hebel sind identifiziert', '2023-11-24 14:28:16.619452', 'Strategie Erarbeitung wird aufgegleist', '2023-11-23 23:00:00', 0.5, 5, 166, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (585, 'Vertiefungsworkshop zu Haag Streit und Webtransfer. Ergebnis:eine dezidierte und teamübergreifende Supportorganisation bringt auch hier keinen signifikanten Mehrwert.', '2023-11-24 14:30:21.483656', 'Das Thema wird so abgeschlossen und nicht weitergeführt.', '2023-11-23 23:00:00', 0.5, 5, 169, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (644, 'unverändert', '2023-11-24 14:30:53.430157', '', '2023-11-23 23:00:00', 0.8, 20, 164, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (719, 'Kampagne läuft, Newsletter wurde verschickt', '2023-11-24 14:34:45.861605', '', '2023-11-23 23:00:00', 0.2, 51, 159, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (717, 'Kampagne wurde gestartet - läuft 2 Wochen. Kunden Mobiliar, SBB, BLS, PostFinance, Ypsomed wurden zum Angebot befragt.', '2023-11-24 14:33:53.924862', '', '2023-11-21 23:00:00', 0.3, 51, 160, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (720, 'EWB Entscheid erwartet verzögert sich aber. Allenfalls ergibt sich bei der Mobiliar im Bereich Schaden noch eine Möglichkeit. Budget ist jedoch knapp. OSS Auftrag von Janik für Janik der Bundeskanzlei. ', '2023-11-27 07:27:31.952483', '', '2023-11-26 23:00:00', 0.7, 30, 162, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (722, 'Durch MidWeek und Sys Bestrebungen (EDA & Grafana OnCall) haben wir riesen Schritte machen können', '2023-11-27 07:53:43.375993', '', '2023-11-26 23:00:00', 0.7, 24, 135, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (724, 'keine Veränderung seit letzter Messung', '2023-11-27 08:31:25.622803', '', '2023-11-26 23:00:00', 100, 3, 214, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (718, 'Keine', '2023-11-30 07:29:54.019466', '', '2023-11-29 23:00:00', 0.9, 5, 167, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (725, 'Das letzte Team Forum wurde dafür verwendet, die MOs im Team zu challengen und Interesse abzuholen bzw. Umsetzungsvorschläge abzuholen. Das Bild des Teams auf die MOs hat sich dadurch geschärft.', '2023-11-27 09:46:20.311887', 'Next up: Auswertung der Team Forum Ergebnisse und Kommunikation an MO-Owner.', '2023-11-26 23:00:00', 0.5, 33, 193, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (768, 'Stundensatzerhöung JBR bei Mobi abgelehnt (müsste man 6 Mte im Voraus anmelden!?)', '2023-12-07 11:51:54.014554', '', '2023-12-06 23:00:00', 0.5, 5, 163, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (748, 'Prov. Zahl Nov 66%', '2023-12-11 07:34:29.393817', '', '2023-12-03 23:00:00', 0.4, 28, 181, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (1803, 'SWEB und SAC geplant', '2024-04-29 10:36:55.519917', '', '2024-04-29 10:36:55.519923', 1054888, 28, 1168, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2147, 'Anpassung Confidence Level', '2024-06-17 15:49:57.855145', '', '2024-06-17 15:49:57.855147', -0.25, 20, 1136, 0, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (726, 'Situation gleich wie bei letztem Checkin. Etwas Entspannung bezügl. Auslastungssituation GJ/Q3 zeichnet sich ab, trotzdem planen wir den Ausrichtungsworkshop nicht mehr in diesem Quartal.', '2023-11-27 09:51:31.529484', 'Def. Entscheidung über Durchführung in diesem Q bis ca. am 10.12.2023. Falls Auslastungsforecast bis dann gut, dann können wir das letzte Team Forum vor Weihnachten für den Ausrichtungsworkshop nutzen.', '2023-11-26 23:00:00', 0.1, 33, 192, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (727, 'Absolute Verrechenbarkeit im Oktober war 78.8%.
Ø somit (75.3+78.8)/2=77.05%', '2023-11-27 09:55:10.524045', '', '2023-11-26 23:00:00', 77.05, 33, 178, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (728, 'Rentabilitätsrechnung ist analyisiert, verstanden und dem Team /zh in letztem (Extended-) Teammeeting vorgestellt.', '2023-11-27 09:58:26.06137', 'Fragen und Anregungen aus der Teamdiskussion verarbeiten und GL vorstellen', '2023-11-26 23:00:00', 50, 33, 173, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (729, 'keine Veränderung sichtbar - jedoch viele Leads und prov. geplant', '2023-11-27 10:34:29.272309', 'Deals schliessen und möglichst schnell im PTime eintragen', '2023-11-26 23:00:00', 0, 36, 215, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (731, 'keine Veränderung', '2023-11-27 10:37:30.395561', '', '2023-11-26 23:00:00', 0.3, 3, 218, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (732, 'Auslastung sehr tief', '2023-11-27 10:38:13.835256', 'Deals gewinnen', '2023-11-26 23:00:00', 0, 36, 217, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (733, 'keine Veränderung', '2023-11-27 10:38:53.020257', '', '2023-11-26 23:00:00', 0.3, 36, 216, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (734, 'September-Ergebnis wurde im Oktober knapp nicht übertroffen.
Oktober: 294851−34760 = CHF 260''091.-', '2023-11-27 11:33:44.364256', '', '2023-11-26 23:00:00', 261337, 33, 190, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (735, 'Definitiv abgeschlossene Verträge:
Interdiscount (Konstantin): 1.2% höher als bisher, Volumen TCHF 261

Es folgen weitere Verlängerungen, bei welchen mitunter eine Erhöhung ganz ausbleibt. Zielerreichung aus heutiger Sicht eher schwierig.', '2023-11-27 11:46:56.600634', 'Dran bleiben...', '2023-11-26 23:00:00', 1.2, 33, 191, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (736, 'Bei 1040% Kapa sind devinitiv verplant über Jan+Feb: td 20%, ck 90%, kk 90%, uro 10%, jsh 10% -> Total 220%', '2023-11-27 11:56:35.356615', 'Viele Aufträge mit mündlicher Zusage oder hoher Eintrittswahrscheinlichkeit. Diese sind noch ausgeklammert. Akut: Auftragssuche für nkr+aeu', '2023-11-26 23:00:00', 21.15, 33, 171, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (737, 'Letzte Woche wurden wiederum drei neue technische Blogposts aufgeschalten. Neue Follower bis jetzt: 42.', '2023-11-28 05:53:00.190944', 'In den nächsten Wochen sind weitere Inhalte geplant. ', '2023-11-27 23:00:00', 0.75, 26, 147, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (738, 'Letzte Woche hatten wir einen Peak wegen der MLOps Kampagne, sind aber trotzdem noch über dem Keyresult.', '2023-11-28 07:33:14.515275', 'Weiter wie bisher. ', '2023-11-27 23:00:00', 28.4, 49, 148, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (739, 'Feedback und Go GL ausstehend', '2023-11-28 07:33:33.83203', 'Feedback und Go GL ausstehend', '2023-11-27 23:00:00', 0.3, 26, 146, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (740, 'Inhaltskonzept in Review', '2023-11-28 07:33:52.714429', '', '2023-11-27 23:00:00', 0.1, 26, 145, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (741, 'Alle Bestellungen sind eingetroffen. ', '2023-11-29 16:33:50.006233', 'Dass wir eine weitere Person auf ein Mandat bringen können ist aktuell eher unrealistisch.', '2023-11-28 23:00:00', 0.9, 29, 168, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (743, 'Keine änderung', '2023-11-30 07:28:44.102355', '', '2023-11-29 23:00:00', 0, 5, 161, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (744, 'Meeting technische Aspekte und daraus erfolgte ToDos für uns sind erledigt. Momentan ist WLY in der Analysephase und macht für uns noch weitere Aufwandschätzungen betreffend dem Import der Referenzen/Blogposts sowie dem Hosting.', '2023-11-30 09:39:19.724119', 'Abwarten Antwort WLY, nach finalem GO von Dänu Entscheid festhalten für das weitere Vorgehen.', '2023-11-29 23:00:00', 0.3, 49, 149, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (745, 'Keine.', '2023-11-30 12:10:51.019987', '', '2023-11-29 23:00:00', 0.7, 30, 162, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (746, 'Keine Veränderung ', '2023-12-04 07:30:15.371661', '', '2023-12-03 23:00:00', 0.7, 28, 179, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (747, 'Keine Veränderung ', '2023-12-04 07:30:31.349625', '', '2023-12-03 23:00:00', 0.4, 28, 180, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (749, ' Keine Veränderung ', '2023-12-04 07:34:55.948512', '', '2023-12-03 23:00:00', 0.2, 28, 182, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (750, 'Einiges geplant', '2023-12-04 07:36:13.146407', '', '2023-12-03 23:00:00', 173924, 28, 183, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (751, 'Möglicher Kandidat prüfen', '2023-12-04 07:37:04.892318', 'Techinterview 07.12', '2023-12-03 23:00:00', 0.1, 28, 184, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (752, 'Keine Veränderung ', '2023-12-04 07:37:22.723057', '', '2023-12-03 23:00:00', 0.3, 28, 185, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (753, 'Advents Magic Karten', '2023-12-04 07:37:47.45734', '', '2023-12-03 23:00:00', 1.6, 28, 186, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (760, 'Zwei Blogposts geschalten im technischen Bereich (/mid week & Helvetic Ruby). Followerzahl gestiegen auf 46.', '2023-12-05 06:58:49.612959', 'Diese Woche sind zwei weitere technische Inhalte geplant. ', '2023-12-04 23:00:00', 0.8, 26, 147, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (754, '0', '2023-12-04 14:11:29.241556', 'No news, diese Woche wird Rockylinux Video gedreht, hoffentlich wird das noch etwas bewirken auf dem Markt', '2023-12-03 23:00:00', 0, 32, 175, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (755, '0', '2023-12-04 14:11:56.941549', 'Release erstes Modul steht kurz bevor', '2023-12-03 23:00:00', 2.5, 32, 176, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (756, '0', '2023-12-04 14:12:56.315541', 'Blogpost wurde veröffentlicht', '2023-12-03 23:00:00', 1, 34, 177, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (495, '0.2', '2023-12-04 14:14:36.32825', 'wieder vermehrt eskalierte Tickets. Bearbeitende Members sensibilisieren. Story erfasst die Sichtbarkeit der Eskalierten Tickets verbessert (Grafana Dashboard), dies Hilft hoffentlich auch für die Verbesserung', '2023-10-22 22:00:00', 0.6, 32, 187, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (757, '0.1', '2023-12-04 14:15:27.581966', 'Story erfasst für Darstellung einhaltung SLAs in einem Grafana Dashboard', '2023-12-03 23:00:00', 0.2, 32, 189, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (758, 'Der Wert hat sich seit der letzten Messung verdoppelt!', '2023-12-04 17:25:11.896036', '', '2023-12-03 23:00:00', 2963000, 16, 142, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (759, 'Erster Draft des Ops Angebots steht. Feinschliff kommt noch und ist geplant.', '2023-12-04 17:26:52.385053', '', '2023-12-03 23:00:00', 0.2, 16, 133, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (761, 'Unsere geplanten Inhalte funktionieren, die Websitebesuche sind weiterhin über dem geplanten Keyresult. ', '2023-12-05 07:32:08.493144', 'Weiter wie bisher. ', '2023-12-04 23:00:00', 26.4, 49, 148, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (762, 'Wir sind auf Standby und warten auf die Antwort von WLY für das weitere Vorgehen. ', '2023-12-05 07:32:57.976646', '', '2023-12-04 23:00:00', 0.3, 49, 149, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (763, 'Sales Schulung am Do', '2023-12-06 13:28:39.804629', '', '2023-12-05 23:00:00', 0.5, 5, 163, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (764, '68% (nicht offizielle Zahl). BFE Offering (Rebecca, Janik), Jenkins Migration, Onboarding Christoph BFH', '2023-12-06 14:16:24.010517', '', '2023-12-05 23:00:00', 0, 5, 161, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (742, 'Strategieprozess und Verkleinerung mobcoeur initiiert', '2023-12-06 14:17:49.043923', '', '2023-12-05 23:00:00', 0.6, 5, 166, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (765, 'Keine, bleibt so final', '2023-12-06 14:19:03.703611', '', '2023-12-05 23:00:00', 0.5, 5, 169, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (766, 'Bleibt so final', '2023-12-06 14:21:19.237639', '', '2023-12-05 23:00:00', 0.9, 5, 167, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (770, 'Bis anhin kein Lead aus der online Kampagne. Members aus den /mid /cicd Teams haben kein Interesse am Thema (zu weit weg von ihrem Business). Sie wissen nun zu mindest was es ist und würden bei Kundenbedarf auf uns zukommen.', '2023-12-07 11:59:27.658152', '', '2023-12-06 23:00:00', 0.3, 51, 160, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (771, 'Kein Update', '2023-12-08 06:57:27.831269', '', '2023-12-07 23:00:00', 0.6, 13, 140, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (773, '-1.1 Micha Lüdi, Marc Fehlmann', '2023-12-08 07:09:50.790157', '', '2023-12-07 23:00:00', -2.4, 13, 131, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (774, 'Ziel ist abgeschlossen', '2023-12-08 08:29:36.75649', '', '2023-12-07 23:00:00', 0.3, 65, 155, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (775, 'die mitte ist durchgeführt,', '2023-12-08 08:30:59.006154', 'GLP im Feburar, Jugenverbände sind angefragt ', '2023-12-07 23:00:00', 0.25, 41, 154, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (776, 'Ziel ist abgeschlossen', '2023-12-08 08:31:28.001594', '', '2023-12-07 23:00:00', 1, 41, 156, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (777, 'keien Änderung', '2023-12-08 08:32:12.334812', '', '2023-12-07 23:00:00', 0.7, 41, 157, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (778, 'Ziel ist abgeschlossen ', '2023-12-08 08:33:02.916824', '', '2023-12-07 23:00:00', 0.7, 41, 194, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (779, 'keine weitere Bestellung ', '2023-12-08 08:33:29.654983', '', '2023-12-07 23:00:00', 0.7, 41, 158, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (780, 'Die absolute Verrechenbarkeit inkl Wartung und Lösung liegt im Oktober bei 81.9 % ( Wartungsbudget wurde erhöht) ', '2023-12-08 14:16:20.452967', '', '2023-12-07 23:00:00', 80, 41, 151, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (781, 'Die Verrechenbarkeit der Kundenaufträge ist im November sehr gut mit 99.3% ', '2023-12-08 14:18:43.873983', '', '2023-12-07 23:00:00', 99, 41, 150, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (782, 'Strechziel im November erreicht mit Umsatz >100''000', '2023-12-08 14:19:54.528295', '', '2023-12-07 23:00:00', 115294, 41, 152, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (783, 'Der Wert hat sich nochmals etwas verbessert. Commit erreicht wir gut. Von Target sind wir noch etwas über TCHF 300 entfernt.', '2023-12-11 07:10:13.291955', '', '2023-12-10 23:00:00', 3062000, 16, 142, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (785, 'Kt. Solothurn und EveryWare haben bestellt. Ausblick: BERNMOBIL, PostFinance (Cilium), MediData, CSS, TailorIT, u.w.m. (siehe letzter Kommentar). Wir bleiben aktuell deutlich unter den Werten des Quartals des letzten Geschäftsjahres.', '2023-12-11 07:21:52.371221', '', '2023-12-10 23:00:00', 78930, 16, 141, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (786, 'Vorgehen hinsichtlich APPUiO und Zusammenarbeit mit VSHN ist definiert. Next step. Info an VSHN und Organistion Workshop gemeinsam mit VSHN.', '2023-12-11 07:23:25.343082', '', '2023-12-10 23:00:00', 0.25, 16, 133, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (788, 'Keine Veränderung ', '2023-12-11 07:32:42.294567', 'Nachfragen bei mid', '2023-12-10 23:00:00', 0.7, 28, 179, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (789, 'Keine Veränderung ', '2023-12-11 07:33:40.630105', '', '2023-12-10 23:00:00', 0.4, 28, 180, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (790, 'Prov. Zahl Nov 70%', '2023-12-11 07:36:14.750482', 'Abschluss Nov abwarten', '2023-12-10 23:00:00', 0.4, 28, 181, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (791, '177086', '2023-12-11 07:37:23.509642', '', '2023-12-10 23:00:00', 173924, 28, 183, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (792, 'Nicht erfolgreich', '2023-12-11 07:38:05.842273', 'Bedarf prüfen', '2023-12-10 23:00:00', 0.1, 28, 184, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (793, ' Keine Veränderung ', '2023-12-11 07:38:19.700727', '', '2023-12-10 23:00:00', 0.3, 28, 185, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (794, ' Keine Veränderung ', '2023-12-11 07:38:30.068259', '', '2023-12-10 23:00:00', 1.6, 28, 186, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (795, 'November 1''541''649 gemäss Time', '2023-12-11 08:01:41.253362', '- Keine', '2023-12-10 23:00:00', 0, 24, 144, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (798, 'Happiness von allen mob Teams ausgewertet bis Ende November', '2023-12-11 08:24:34.585478', '', '2023-12-10 23:00:00', 0.8, 20, 164, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (799, 'Lopas Support Bestellung erhalten', '2023-12-11 08:25:51.014356', '', '2023-12-10 23:00:00', 0.4, 20, 170, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (800, 'Drei weitere technische Interviews in dieser Woche', '2023-12-11 11:00:22.448698', '', '2023-12-10 23:00:00', 18, 84, 223, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (801, 'Kein technischer Workshop in Q2', '2023-12-11 11:00:53.753774', '', '2023-12-10 23:00:00', 0, 84, 224, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (802, 'Subscription-Angebot an Uni Tübingen', '2023-12-11 11:01:38.405409', '', '2023-12-10 23:00:00', 2, 17, 222, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (803, 'Kunden werden im Laufe der Woche angeschrieben', '2023-12-11 11:02:24.503074', '', '2023-12-10 23:00:00', 0, 17, 221, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (804, 'Bislang nur Budgetübernahmen in 2024', '2023-12-11 11:03:33.101993', '', '2023-12-10 23:00:00', 20000, 17, 220, 5, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (784, 'Gespräche mit 8 Members geführt. Diese können nun die Ausbildungen in Angriff nehmen. Um Angebot zu definieren/überarbeiten, hat es zeitlich noch nicht gereicht.', '2023-12-11 12:21:44.342281', '', '2023-12-11 12:24:37.559872', 0.25, 16, 130, 1, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (796, 'keine Veränderung bekannt.', '2023-12-11 08:02:10.084854', '- wir sind auf Kurs', '2023-12-15 09:41:11.796636', 0.7, 24, 135, 5, 'metric', NULL, 3);
INSERT INTO okr_pitc.check_in VALUES (772, 'Eine Rückmeldung auf unsere Sales-Kampagne eingegangen', '2023-12-08 06:58:34.502157', '', '2023-12-19 06:45:13.735621', 0.2, 13, 129, 5, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (2150, 'Anpassung Confidence Level', '2024-06-17 15:52:28.071703', '', '2024-06-17 15:52:28.071705', NULL, 20, 1138, 0, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1465, 'Tasks für die letzten Arbeiten sind verteilt:
Kilu&Lars Sofa und rundherum, Fabio Tisch mit 3D Drucker', '2024-02-26 12:03:05.202284', '', '2024-02-26 12:03:05.202287', 90, 32, 1039, 9, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (1466, 'Prozess hat sich etabliert', '2024-02-26 12:04:18.063148', 'Definition von 8 als Zahl war halbschlau ohne mit den Members zu diskutieren was eine 8 bedeutet.', '2024-02-26 12:04:18.06315', 100, 32, 1040, 9, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (797, '- keine', '2023-12-11 08:02:46.082384', '', '2023-12-15 09:40:38.4133', 0.7, 24, 132, 5, 'metric', NULL, 5);
INSERT INTO okr_pitc.check_in VALUES (787, 'Der Workshop hat am 07.12.23 stattgefunden. Es wurden mehrere Massnahmen definiert. Das Outcome und die Pendenzen werden noch dokumentiert von abe.', '2023-12-11 07:25:43.239779', '', '2023-12-15 09:40:57.66056', 0.3, 16, 138, 5, 'metric', NULL, 3);
INSERT INTO okr_pitc.check_in VALUES (1804, 'Mobi-Mandat für Mayra hat sich leider nicht erfüllt - offerten sind weiterhin offen. ', '2024-04-29 10:40:06.262937', '', '2024-04-29 10:40:06.262945', NULL, 3, 1206, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (1805, 'Keine Veränderungen seit letztem Checkin', '2024-04-29 10:40:23.827253', '', '2024-04-29 10:40:23.827259', NULL, 3, 1208, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (1982, 'Tim war leider oft besetzt, weshalb sich hier nicht mehr viel bewegt hat.', '2024-05-27 07:20:50.097011', '', '2024-05-27 07:20:50.097014', NULL, 27, 1187, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2148, 'Anpassung Confidence Level', '2024-06-17 15:50:20.735918', '', '2024-06-17 15:50:20.735921', NULL, 20, 1139, 0, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2267, 'Simone Götz hat letzten Dienstag den Vertrag unterschrieben und wird ab Oktober bei WAC arbeiten. Somit sind beide Abgänge ersetzt', '2024-07-15 08:09:02.964815', '', '2024-07-15 08:09:02.964818', NULL, 36, 1322, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2269, 'noch keine Veränderung, Plan erstellt', '2024-07-15 08:10:00.625468', '', '2024-07-15 08:10:00.625471', 0, 36, 1314, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2282, 'Interview 17.6 mit neuem Kandidaten', '2024-07-16 06:45:24.310831', '', '2024-07-16 06:45:24.310833', -0.9, 5, 1259, 3, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2345, 'Noch keine Aktivitäten', '2024-07-29 09:39:21.036324', '', '2024-07-29 09:39:21.03633', NULL, 4, 1288, 10, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2346, 'Noch keine Aktivitäten', '2024-07-29 09:39:53.185379', '', '2024-07-29 09:40:22.656257', NULL, 4, 1290, 10, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2578, 'Status Quo', '2024-09-06 09:06:00.060356', 'Übernahme in nächsten Zyklus. Mit WS am 3.9.2024 wurde Stretch hinfällig', '2024-09-06 09:06:00.06036', NULL, 4, 1290, 10, 'ordinal', 'TARGET', 2);
INSERT INTO okr_pitc.check_in VALUES (2579, 'Status Quo', '2024-09-06 09:06:55.230921', 'Allenfalls Übernahme in den nächsten OKR-Zyklus', '2024-09-06 09:06:55.230925', NULL, 4, 1292, 0, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2583, 'Target erreicht, Commit noch nicht, d.h. auf Commit gesetzt (1. Stufe) "CSS in 2024" von Max und "Rust Full Stack Development" von Dani Tschanam Techworkshop', '2024-09-06 15:29:57.430142', 'Massnahmeplan erstellen', '2024-09-06 15:29:57.430147', NULL, 4, 1293, 0, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2585, 'Wir haben den EJV gewonnen', '2024-09-09 06:51:56.718418', '', '2024-09-09 06:51:56.718423', NULL, 41, 1285, 5, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2586, 'Gemäss der Meilensteinplanung sind wir auf Kurs
Migrationsscript ist noch etwas im Verzug, der Rest passt ', '2024-09-09 06:52:43.275634', '', '2024-09-09 06:52:43.27564', NULL, 41, 1273, 7, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2587, 'nächster Release ist auf Track ', '2024-09-09 06:53:14.841096', '', '2024-09-09 06:53:14.8411', 1, 41, 1271, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2588, 'Sync betreffend Openshift migration geplant. Massnahmen nicht angegangen', '2024-09-09 06:53:20.382039', '', '2024-09-09 06:53:20.382044', NULL, 28, 1298, 3, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2589, 'Keine Aktivität', '2024-09-09 06:53:36.171832', '', '2024-09-09 06:53:36.171836', NULL, 28, 1299, 1, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2590, 'ZG laufen und Punkte wurden angesprochen', '2024-09-09 06:54:02.228623', '', '2024-09-09 06:54:02.228636', NULL, 28, 1300, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2591, 'zertifikat fehlt noch ', '2024-09-09 06:54:08.601722', '', '2024-09-09 06:54:08.601724', NULL, 41, 1274, 7, 'ordinal', 'STRETCH', 0);
INSERT INTO okr_pitc.check_in VALUES (2593, '', '2024-09-09 06:55:08.033516', '', '2024-09-09 06:55:08.033518', NULL, 28, 1327, 5, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2594, 'Ziel sehr ambitioniert, da Zusagen erst im Q2 wohl erfolgen', '2024-09-09 06:56:52.84438', '', '2024-09-09 06:56:52.844382', 820476, 28, 1301, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2595, 'Phippu hat am ersten Meeting zu MO PE teilgenommen, und trägt die Bedürfnisse der "PL" in den /sys-Lead und damit auch ins /sys-Team', '2024-09-09 07:12:48.419676', '', '2024-09-09 07:12:48.419679', NULL, 32, 1256, 5, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2596, 'Die Inputs sind alle in die /sys-Prozesse eingeflossen, von Tim reviewt,', '2024-09-09 07:14:18.205888', '', '2024-09-09 07:14:18.205891', NULL, 32, 1257, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2597, 'Noch keine weiteren Bewerber', '2024-09-09 07:15:24.195747', 'Inserat aufgeschaltet lassen', '2024-09-09 07:15:24.19575', NULL, 32, 1255, 5, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2599, 'BAFU geplant', '2024-09-09 07:19:42.081237', '', '2024-09-09 07:19:42.08124', 957074, 28, 1301, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2598, 'BFH IDS Team hat unterschrieben :-)', '2024-09-09 07:15:51.220623', '', '2024-09-09 07:24:37.346925', NULL, 32, 1258, 10, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (2600, 'Keine Veränderung', '2024-09-09 07:48:18.601233', '', '2024-09-09 07:48:18.601235', NULL, 3, 1322, 5, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2601, 'Anpassung Confidence... - sonst keine Änderung', '2024-09-09 07:50:03.742305', '', '2024-09-09 07:50:03.742307', NULL, 3, 1322, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2602, 'Artikel Strategisches UX', '2024-09-09 07:50:39.52377', '', '2024-09-09 07:50:39.523772', 7, 3, 1315, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2603, 'Keine Konferenzen letzte Woche', '2024-09-09 07:54:04.519854', '', '2024-09-09 07:54:04.519856', 5, 3, 1314, 4, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2604, 'Erkenntnisse werden diese Woche vorgestellt', '2024-09-09 07:54:51.492222', '', '2024-09-09 07:54:51.492225', NULL, 3, 1316, 10, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2605, 'Analyse durch Pascou gemacht - Diskussion / Vorstellung später. ', '2024-09-09 07:55:32.903338', '', '2024-09-09 07:55:32.903341', NULL, 3, 1317, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2606, 'Keine Änderung', '2024-09-09 07:55:50.826399', '', '2024-09-09 07:55:50.826402', NULL, 3, 1319, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2607, 'Aktuelle Hürde: "ein Beitrag pro Member" - Nextcloud ist live, Zielliste für Inhalt noch offen. ', '2024-09-09 07:58:12.954306', 'Members Abholen für Topics in Nextcloud', '2024-09-09 07:58:12.954309', NULL, 3, 1321, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2592, 'Aktuell keine weiteren Massnahmen', '2024-09-09 06:54:46.411228', '', '2024-09-09 09:54:23.842682', NULL, 28, 1302, 5, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2608, '', '2024-09-09 12:43:28.139619', '', '2024-09-09 12:43:28.139622', 74.83, 22, 1349, 0, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2609, 'Ypsomed', '2024-09-09 13:34:31.672984', '', '2024-09-09 13:34:31.672987', 97161, 3, 1312, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2610, 'Am Workshop erreicht', '2024-09-09 13:34:51.297877', '', '2024-09-09 13:34:51.29788', NULL, 3, 1324, 5, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (2611, 'LGT, Swisscom', '2024-09-09 14:40:38.33476', '', '2024-09-09 14:40:38.334763', 832611, 31, 1328, 0, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2612, 'Lead noch unschlüssig/abwesend (MediData) ', '2024-09-09 14:42:08.626955', '', '2024-09-09 14:42:08.626958', NULL, 31, 1329, 2, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2613, 'Name ok. Steckbriefe: 2/5 in Draft-Version bereit. Rest in Arbeit.', '2024-09-09 14:43:33.665571', '', '2024-09-09 14:43:49.834426', NULL, 31, 1326, 1, 'ordinal', 'FAIL', 3);
INSERT INTO okr_pitc.check_in VALUES (2614, '', '2024-09-09 15:20:17.314439', 'Noch Team Zoo schulen', '2024-09-09 15:20:17.314442', NULL, 5, 1261, 9, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2615, '', '2024-09-09 15:20:48.935163', '', '2024-09-09 15:20:48.935165', NULL, 5, 1263, 10, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (2616, 'Mündliches OK zur Vereinbarung erhalten', '2024-09-09 15:21:50.364915', '', '2024-09-09 15:21:50.364918', NULL, 5, 1276, 6, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2617, '', '2024-09-09 15:22:26.902682', '', '2024-09-09 15:22:26.902684', NULL, 5, 1267, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2618, '2 Monate hintereinander Wartungsbudget eingehalten, dies ist schon bal sehr guet ', '2024-09-10 06:34:05.071005', '', '2024-09-10 06:34:05.071008', NULL, 41, 1268, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2619, 'wir haben das Target schon fast erreicht und defineirt wer wieviel zahlt, aber dieses Ziel wird sich wohl bis Ende Jahr oder aogar Q1 hinziehen  ', '2024-09-10 06:36:00.641868', '', '2024-09-10 06:36:00.641871', NULL, 41, 1270, 7, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2620, 'Erste Sichtung mögliche Anbieter wurde gemacht. Ein erster Austausch mit CamptoCamp nächste Woche geplant.', '2024-09-10 07:20:33.863609', '', '2024-09-10 07:20:33.863611', NULL, 24, 1350, 4, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2621, 'Keine Veränderung zu letztem Check-in', '2024-09-10 07:21:14.54289', '', '2024-09-10 07:21:14.542893', NULL, 24, 1243, 7, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2623, 'Instanz gemeinsam finalisiert. Wir wenden diese nun an und speichern die erstellten Prompts in einer gemeinamen Bibliothek im Taiga Board ab.', '2024-09-10 07:59:49.403408', '', '2024-09-10 07:59:49.40341', NULL, 49, 1279, 5, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2624, 'Score weiterhin Status Quo. Bis am 20. September prüfen wir mit WLY noch die indexierten Seiten.', '2024-09-10 08:02:02.233916', '', '2024-09-10 08:02:02.233919', NULL, 49, 1281, 9, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2582, '- Markt-Research ist abgeschlossen
- Workshop zu Zukunftsbild Digitale Lösungen am Workshop vom 3.9. erarbeitet
- ein erster Entwurf des Zukunftsbilds steht
- die nächsten Schritte sind definiert', '2024-09-06 15:09:58.926363', '', '2024-09-10 11:40:55.175443', NULL, 16, 1254, 0, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (2625, '', '2024-09-10 08:28:47.790562', 'Das OKR hat in diesem Quartal volle Fahrt aufgenommen. Stretch ist erreicht und wir führen die Liste regulär weiter.', '2024-09-10 08:28:47.790564', NULL, 49, 1283, 10, 'ordinal', 'STRETCH', 1);
INSERT INTO okr_pitc.check_in VALUES (2626, '', '2024-09-10 09:09:35.166294', '', '2024-09-10 09:09:35.166297', NULL, 27, 1336, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2627, '', '2024-09-10 09:09:39.599046', '', '2024-09-10 09:09:39.599048', NULL, 27, 1339, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2628, 'Das OKR hat in diesem Quartal volle Fahrt aufgenommen. Stretch ist erreicht und wir führen die Liste regulär weiter. Letzte Woche wurden div. Post zu, Techworkshop etc. publiziert. ', '2024-09-10 09:10:26.933244', '', '2024-09-10 09:10:26.933247', 15, 49, 1318, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2629, 'Keine Veränderung', '2024-09-10 11:39:14.874034', '', '2024-09-10 11:39:14.87404', 4.6, 22, 1253, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2584, '', '2024-09-09 06:48:22.112789', '', '2024-09-10 11:39:59.259284', 3296429, 13, 1241, 0, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (2622, 'Kurz vor Abschluss Go-to Market Plan und Landing-Page', '2024-09-10 07:22:36.833242', '', '2024-09-10 11:40:17.115972', 4, 24, 1311, 0, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2630, 'Keine Veränderung', '2024-09-10 11:39:32.810496', '', '2024-09-10 11:40:17.488165', NULL, 22, 1348, 0, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (2541, '- Opportunities werden bei AWS im Portal geführt. Bei GCP können wir das noch nicht selber. Wir führen die Liste im Taiga.
- Interesse Robin Steiner an Google Zertifikat. Unterstützung geklärt.
- weitere Cloud Lunches werden geplant.
- bei AWS Opportunities ist die Freigabe von Brixel noch offen, obwohl approved. Nach wie vor fehlt uns eine Referenz. SBB würde passen aber wir erhalten die Referenz von den SBB noch nicht.
- AWS: Monthly recurring Revenue Anforderungen an Partnerschaften jetzt erfüllt.', '2024-09-02 06:54:24.622567', '', '2024-09-10 11:40:37.635066', NULL, 16, 1250, 0, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (2631, '- Codi mit erstem Wurf des möglichen Beratungsarbeit ist erarbeitet und Team wird nun Feedback geben', '2024-09-10 11:50:07.615545', '', '2024-09-10 11:50:07.615547', NULL, 16, 1251, 0, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2632, 'GtM ready!', '2024-09-12 14:36:28.29883', '', '2024-09-12 14:36:28.298832', 6, 31, 1343, 2, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2634, 'Weiterhin Status Quo.', '2024-09-15 19:36:45.910689', '', '2024-09-15 19:36:45.910692', NULL, 49, 1283, 10, 'ordinal', 'STRETCH', 1);
INSERT INTO okr_pitc.check_in VALUES (2635, 'Weiterhin volle Fahrt mit Züri Jubi und OSS Batch von Oli', '2024-09-15 19:39:37.612316', '', '2024-09-15 19:39:37.612318', 17, 49, 1318, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2636, 'Bitwarden läuft, aber es braucht noch Doku und Cryptopus-Export. Bitwarden sollte auch noch Argo-isiert werden.', '2024-09-16 06:41:11.887005', '', '2024-09-16 06:41:11.887025', NULL, 27, 1341, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2637, 'Kontakt mit Yup, aber nun Ferien.', '2024-09-16 06:41:57.337234', '', '2024-09-16 06:41:57.33725', NULL, 27, 1336, 0, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2638, 'Wie Beratung.', '2024-09-16 06:42:09.659887', '', '2024-09-16 06:42:09.659904', NULL, 27, 1339, 0, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2639, 'Mit /mid bereit.', '2024-09-16 06:42:25.214281', '', '2024-09-16 06:42:25.214298', NULL, 27, 1340, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2640, '', '2024-09-16 06:43:01.616543', '', '2024-09-16 06:43:01.616558', NULL, 27, 1334, 0, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2633, 'Wir speichern unsere Prompts weiterhin im Ticket ab. Aktuell werden wir Stretch nicht erreichen (Impressions vom letzten Quartal sind aktuell 33% tiefer, da nur halb so viele technische Inhalte gepostet wurden. Fokus lag auf Events. Wir checken den Stretch per Ende Quartal nochmals.)', '2024-09-15 19:35:10.0234', '', '2024-09-16 06:58:17.638054', NULL, 49, 1279, 8, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (2641, '', '2024-09-16 08:06:59.567992', '', '2024-09-16 08:06:59.56802', NULL, 28, 1298, 3, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2642, 'Template erstellt. Muss noch ausgefüllt werden.', '2024-09-16 08:07:18.313274', '', '2024-09-16 08:07:18.313291', NULL, 28, 1299, 1, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2645, 'Workshop hat stattgefunden', '2024-09-16 08:08:37.765104', '', '2024-09-16 08:08:37.765121', NULL, 28, 1327, 7, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2646, 'Planung vorgenommen inkl. Hitobito', '2024-09-16 08:09:58.700809', '', '2024-09-16 08:09:58.700826', 1006610, 28, 1301, 9, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2647, 'keine änderung', '2024-09-16 10:54:02.596109', '', '2024-09-16 10:54:02.596127', 97161, 3, 1312, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2648, 'Keine Änderung', '2024-09-16 10:54:15.430702', '', '2024-09-16 10:54:15.43072', NULL, 3, 1322, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2649, 'Keine Änderung', '2024-09-16 10:54:29.414278', '', '2024-09-16 10:54:29.414295', NULL, 3, 1324, 5, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (2650, 'Jubi-Sommer-Anlass', '2024-09-16 10:56:32.411076', '', '2024-09-16 10:56:32.411095', 6, 3, 1314, 0, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2651, 'Keine Veränderung', '2024-09-16 10:57:32.536074', '', '2024-09-16 10:57:32.536093', 7, 3, 1315, 0, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2652, '', '2024-09-16 10:57:52.529247', '', '2024-09-16 10:57:52.529277', 97161, 3, 1312, 0, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2653, 'Confidence', '2024-09-16 10:58:08.169513', '', '2024-09-16 10:58:08.16953', NULL, 3, 1324, 10, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (2654, 'Ergebnisse Diskutiert', '2024-09-16 11:00:03.365189', '', '2024-09-16 11:00:03.365203', NULL, 3, 1316, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2655, '', '2024-09-16 11:00:31.230126', 'Konsens ist seit Freitag erreicht.', '2024-09-16 11:00:31.230144', NULL, 3, 1317, 0, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2656, 'Kickoff durchgeführt,', '2024-09-16 11:06:12.595061', '', '2024-09-16 11:06:12.595076', NULL, 3, 1319, 0, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2657, 'Knowledge Base: nicht jeder Member einen Beitrag erfasst. ', '2024-09-16 11:06:48.815797', '', '2024-09-16 11:06:48.815821', NULL, 3, 1321, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2658, '', '2024-09-16 11:07:53.69007', '', '2024-09-16 11:07:53.690085', NULL, 3, 1321, 0, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2659, 'Wir ranken weiterhin mit einem SEO Score von 83. Werden die Inputs von WLY noch einpflegen, diese verändern aber den Score nicht. ', '2024-09-16 13:13:10.353846', '', '2024-09-16 13:13:10.35386', NULL, 49, 1281, 9, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2661, '', '2024-09-16 16:12:16.534439', '', '2024-09-16 16:12:16.534452', NULL, 5, 1263, 10, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (2660, 'Im Team Zoo kommuniziert', '2024-09-16 16:11:53.235517', '', '2024-09-16 16:13:47.43695', NULL, 5, 1261, 10, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (2662, 'Verspätung bei der Lokalen Infrastruktur. Ungeplante Knie Operation Iwan.', '2024-09-16 16:13:25.248897', '', '2024-09-16 16:14:21.887498', NULL, 5, 1276, 0, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2663, 'Wird frühestens im nächsten Quartal erfolgen', '2024-09-16 16:15:06.964794', '', '2024-09-16 16:15:06.96481', 0, 5, 1277, 0, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2664, 'Aufgrund der prekären Auslastung bei /mobility wurden Recruiting Aktivitäten reduziert.', '2024-09-16 16:48:30.319455', '', '2024-09-16 16:48:30.319466', 0.1, 20, 1259, 0, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (2665, 'Ein Angebotentwurf mit 12 Angebotsoptionen ist erstellt', '2024-09-16 20:06:45.059074', '', '2024-09-16 20:07:04.01895', NULL, 20, 1262, 0, 'ordinal', 'COMMIT', 2);
INSERT INTO okr_pitc.check_in VALUES (2666, 'Thema wird im nächsten Quartal weiterverfolgt.', '2024-09-16 20:08:04.338853', '', '2024-09-16 20:08:04.338866', NULL, 20, 1266, 0, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2667, 'dsm: 671 von 739 Stunden verplant
yt:  894 von 1''056 Stunden verplant
dal: 727 von 739 Stunden verplant
45.9% Auslastung', '2024-09-16 21:00:17.854505', '', '2024-09-16 21:00:17.854518', 90.4, 33, 1304, 10, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2668, 'Keine Veränderungen ggü. letztem Checkin', '2024-09-16 21:01:16.443469', '', '2024-09-16 21:01:16.443481', NULL, 33, 1305, 7, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2643, 'ZG laufen noch. Wird ende Woche abgeschlossen', '2024-09-16 08:08:01.955519', '', '2024-09-17 07:43:56.239409', NULL, 28, 1300, 8, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2709, 'Gemäss Sheet im PTime', '2024-10-08 10:55:39.606896', '', '2024-10-08 10:55:39.606903', 67.77, 3, 1417, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2486, 'WTO Zuschlag für SERV erhalten (Volumen 10''810''000.-, 5 Zuschlagsempfänger). Wir nehmen 1% des Volumens als New Business Wert, ein sehr konservative Schätzung. Ansonsten: Leider Absage von ewz, Gespräch über weitere Zusammenarbeit jedoch geplant. Ansonsten im Gespräch mit Santésuisee, Vontobel (aus Sicht /zh ebenfalls New Biz), Akros (eher Partner als Kunde).', '2024-08-22 11:31:07.784471', 'Akqui-Sprint (läuft)', '2024-09-16 21:17:06.843873', 108100, 33, 1306, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2669, 'Keine neuen Zuschläge. 2 spannende Deals mit Vontobel am Laufen. Wenn wir diese holen, wäre es nach unserer Definition aber kein New Biz, obschon wir seit längerem nicht mehr für Vontobel tätig waren.', '2024-09-16 21:27:49.599773', '', '2024-09-16 21:27:49.599785', 108100, 33, 1306, 10, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2670, '11.9% Marge im Juni
24.1% Marge im Juli (unerwartet, fast schon unerklärbar hoch...)
', '2024-09-16 21:31:23.476924', 'Mit 10% Marge im August (Abschluss noch nicht verfügbar) könnte das Stretch Goal erreicht werden. Realistisch...', '2024-09-16 21:31:23.476935', 35, 33, 1310, 7, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2671, 'Von Anstellung P.M. per 1.10. kann ausgegangen werden (mündl. und schriftl. Zusage, Vertrag in nächsten Tagen zu unterschreiben). Start in Q2, deshalb Target und Stretch unerreicht.', '2024-09-16 21:34:15.684808', '', '2024-09-16 21:34:15.684834', NULL, 33, 1307, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2672, 'Keine Neuerung ggü. letztem Checkin', '2024-09-16 21:34:45.839531', '', '2024-09-16 21:34:45.839545', NULL, 33, 1308, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2673, 'Themen im Rahmen der ZGs systematisch besprochen/identifiziert. ZGs noch im Gang, wird aber bis Ende Monat abgeschlossen sein.', '2024-09-16 21:36:23.863521', '', '2024-09-16 21:36:23.863533', NULL, 33, 1309, 2, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2674, 'Im nächsten Quartal ist ein KR geplant, um das Angebot zu schärfen', '2024-09-17 05:48:13.999715', '', '2024-09-17 05:48:13.999722', 0, 5, 1275, 0, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2675, 'Weitere Blueprints und Readmes in Arbeit aber noch so weit wie gewünscht.', '2024-09-17 05:59:17.326349', '', '2024-09-17 05:59:17.326355', NULL, 40, 1326, 0, 'ordinal', 'FAIL', 3);
INSERT INTO okr_pitc.check_in VALUES (2676, 'Landing-Page Inhalt ready, ist in Review aber noch nicht live, deshalb Target knapp verfehlt.', '2024-09-17 06:00:18.616756', '', '2024-09-17 06:00:18.616763', 6, 40, 1343, 0, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2677, 'Hat keine Priorität erhalten, entsprechend keine Actions erreicht.', '2024-09-17 06:01:11.701571', '', '2024-09-17 06:01:11.701581', 0, 40, 1344, 0, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2678, 'Kein Supportangebot in Aussicht.', '2024-09-17 06:03:54.992268', '', '2024-09-17 06:03:54.992274', NULL, 40, 1329, 0, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2679, 'Kein Supportangebot in Aussicht.', '2024-09-17 06:03:55.132892', '', '2024-09-17 06:03:55.132899', NULL, 40, 1329, 0, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2680, 'August Zahlen noch nicht verfügbar, wir gehen aber davon aus, dass sie ähnlich ausfallen wie Juli, entpsrechend wären wir sogar weit über dem Stretchgoal von TCHF 16.5 pro Monat und Member.', '2024-09-17 06:08:37.362705', '', '2024-09-17 06:08:37.362712', NULL, 40, 1346, 9, 'ordinal', 'STRETCH', 2);
INSERT INTO okr_pitc.check_in VALUES (2681, 'Members, die sich Zeit nehmen, an den SIGs zu arbeiten, kommen vorwärts und haben Spass.', '2024-09-17 06:09:49.523245', '', '2024-09-17 06:09:49.523252', 8.14, 40, 1331, 10, 'metric', NULL, 2);
INSERT INTO okr_pitc.check_in VALUES (2682, 'Unverändert zu letzem Checkin, alle Talks sind geplant, bzq. teilweise schon durchgeführt.', '2024-09-17 06:10:48.293284', '', '2024-09-17 06:10:48.293294', NULL, 40, 1332, 10, 'ordinal', 'STRETCH', 1);
INSERT INTO okr_pitc.check_in VALUES (2684, '', '2024-09-17 07:11:55.300735', '', '2024-09-17 07:11:55.300742', NULL, 4, 1295, 0, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2685, 'Status Quo', '2024-09-17 07:12:32.468682', '', '2024-09-17 07:12:32.468689', NULL, 4, 1294, 0, 'ordinal', 'COMMIT', 3);
INSERT INTO okr_pitc.check_in VALUES (2686, 'Status Quo', '2024-09-17 07:13:19.732201', 'Wird im Rahmen des Weeklys weiterverfolgt', '2024-09-17 07:13:19.732209', NULL, 4, 1293, 0, 'ordinal', 'COMMIT', 1);
INSERT INTO okr_pitc.check_in VALUES (2687, 'Status Quo', '2024-09-17 07:14:31.217104', '', '2024-09-17 07:14:31.217111', NULL, 4, 1292, 0, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2683, 'Mit Zusatzbudgets Mobi (Liima, JBat), Weiterentwicklung BKD, Auftrag für Zeilenwerk, Swisscom Budgeterhöhung, Paktetierung Bundesarchiv gibt es genügend Aufträge bis Ende Jahr', '2024-09-17 07:11:14.809612', 'Moglichst rasch Anfang 2025 klären. Januar 2025 ist noch offen', '2024-09-17 07:14:57.873794', NULL, 4, 1296, 10, 'ordinal', 'TARGET', 1);
INSERT INTO okr_pitc.check_in VALUES (2688, 'Status Quo', '2024-09-17 07:15:33.93523', '', '2024-09-17 07:15:33.935237', NULL, 4, 1278, 0, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2689, 'Status Quo', '2024-09-17 07:15:46.199875', '', '2024-09-17 07:15:46.199882', NULL, 4, 1288, 0, 'ordinal', 'COMMIT', 5);
INSERT INTO okr_pitc.check_in VALUES (2690, 'Status Quo', '2024-09-17 07:16:37.531574', '', '2024-09-17 07:16:37.53158', NULL, 4, 1290, 10, 'ordinal', 'TARGET', 2);
INSERT INTO okr_pitc.check_in VALUES (2691, 'Juli 2024: 17548
August 2024: 15987
Durschnitt: 16768
Stretch: 16500', '2024-09-18 15:40:18.797016', '', '2024-09-23 11:17:20.236551', NULL, 40, 1346, 10, 'ordinal', 'STRETCH', 3);
INSERT INTO okr_pitc.check_in VALUES (2692, 'Termine aufgegleist, erste Retros haben stattgefunden', '2024-09-26 12:01:06.06689', '', '2024-09-26 12:01:06.066913', NULL, 34, 1406, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2693, 'Tag und Location sind festgelegt. Vorbereitung des Workshops aufgegleist', '2024-09-26 12:01:39.056581', '', '2024-09-26 12:01:39.0566', NULL, 34, 1407, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2694, 'Weitere Erkenntnisse aus aktueller Ops Offerte gwonnen (wir müssen die pauschalen Leistungen erweitern, damit wir am Markt eine Chance haben)', '2024-09-26 12:04:37.908488', '', '2024-09-26 12:04:37.908505', NULL, 34, 1408, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2695, 'In Kontakt mit MO Team', '2024-09-26 12:05:03.841221', '', '2024-09-26 12:05:03.841239', NULL, 34, 1409, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2696, 'Bitwarden Test läuft stabil. Doku ist noch nicht ready für /sys.', '2024-10-02 09:12:56.869611', '', '2024-10-02 09:12:56.869617', NULL, 27, 1476, 4, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2697, 'Doku ist noch nicht ready, ich habe sie aber in Angriff genommen.', '2024-10-02 09:13:31.649407', '', '2024-10-02 09:13:31.649414', NULL, 27, 1477, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2698, 'Import von Passwörtern ist klar, Teams als Collections abbilden wird aufwändig.', '2024-10-02 09:14:07.090751', '', '2024-10-02 09:14:07.090757', NULL, 27, 1478, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2699, '', '2024-10-02 09:14:17.014596', '', '2024-10-02 09:14:17.014603', NULL, 27, 1479, 7, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2700, 'Inhalt definiert https://codimd.puzzle.ch/Ef-L_BOXQm-XAl9kslrlUg#', '2024-10-02 11:54:51.202888', '', '2024-10-02 11:54:51.202894', NULL, 17, 1377, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2701, 'Mögliche Vertriebswege, jedoch nicht etabliert.
Angebot alleine oder in Kooperation mit anderen Partnern?

    Vertriebspartner? (z.B. IHK, WIT)
    Technische Partner? (z.B. sodgeIT, AU)
    Fachfremder Partner? (z.B. Schreiner, Anwalt)
', '2024-10-02 11:55:40.750655', '', '2024-10-02 11:55:40.750662', NULL, 17, 1378, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2702, 'Noch nicht durchgeführt.', '2024-10-02 11:55:56.089176', '', '2024-10-02 11:55:56.089181', 0, 17, 1379, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2703, 'Gespräche mit Kunden geführt, Kenntnisse kommuniziert.', '2024-10-02 12:03:22.941574', '', '2024-10-02 12:03:22.941581', NULL, 17, 1374, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2704, 'Keine Plattform vorhanden, zurückgestellt aufgrund von parallelen Vorhaben bei /mid', '2024-10-02 12:03:54.893387', '', '2024-10-02 12:03:54.893394', NULL, 17, 1375, 6, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2705, 'Nichts vorhanden', '2024-10-02 12:04:04.545368', '', '2024-10-02 12:04:04.545375', 0, 17, 1376, 4, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2706, 'Am 15.10.24 erhalten wir von Benno eine Einführung in die Odoo Welt.', '2024-10-07 11:24:05.426222', '', '2024-10-07 11:24:05.426229', NULL, 49, 1453, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2707, 'Draft Subsite steht', '2024-10-08 07:08:33.880847', '', '2024-10-08 07:08:33.880854', NULL, 26, 1469, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2708, 'Saraina bespricht im SUM mit Dänu die Thematik Informationsarchitektur und dann starten wir. ', '2024-10-08 07:09:52.87371', '', '2024-10-08 07:09:52.873717', NULL, 49, 1410, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2710, '', '2024-10-08 10:58:29.082989', '', '2024-10-08 10:58:29.082999', NULL, 3, 1419, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2712, '', '2024-10-08 10:58:45.953111', '', '2024-10-08 10:58:45.953118', NULL, 3, 1421, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2713, 'Aktuell fehlt der Konsens noch', '2024-10-08 10:59:22.165017', '', '2024-10-08 10:59:22.165024', NULL, 3, 1422, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2711, 'Das Mandat verlängert (stretcht) sich konkret ins nächste Jahr. ', '2024-10-08 10:58:39.187958', '', '2024-10-08 11:02:01.619093', NULL, 3, 1420, 5, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2714, 'UX durchführung ongoing', '2024-10-08 11:06:51.744761', '', '2024-10-08 11:06:51.744767', NULL, 3, 1423, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2715, '', '2024-10-08 12:01:57.058552', '', '2024-10-08 12:01:57.058559', 1, 26, 1470, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2716, 'Basierend auf Skripts und Tests in Staging wird Target erfolgreich erreicht. Mit Ausnahme von "Alle Puzzler" und seinen Unterordnern kann sogar Stretch erreicht werden.

Check-In ist noch Fail, weil Ziel so formuliert ist, dass es erst mit der echten Migration erreicht ist.', '2024-10-09 11:04:56.233294', '', '2024-10-09 11:04:56.233301', NULL, 27, 1478, 10, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2717, 'Stand heute könnten wir im Notfall umsteigen. Für einen wirklich sauberen Betrieb fehlen noch ein paar Dinge, aber es *ginge*.', '2024-10-09 11:05:54.473738', '', '2024-10-09 11:05:54.473745', NULL, 27, 1479, 9, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2718, 'User-Guide ist rudimentär vorhanden. Admin-Guide fehlt noch.', '2024-10-09 11:06:33.816753', '', '2024-10-09 11:06:33.816759', NULL, 27, 1477, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2719, '- GCP Sell Parnerschaftslevel für DACH erreicht. Yuhuu!!
- GCP Service Partnerschaft noch offen. Es fehlt noch eine Zertifizierung. Yelan und Robin sind dran. Gemäss Yelan sollte ~Dezember machbar sein.
- GCP: alle weitere Anforderungen wie Due Dilligence erfüllt.
- GCP: Austausch mit Ekaterina Schirrmeister erfolgt. Versuchen Managed Partner Status für 2025 zu erreichen.
- GCP: weiterer Lead mit Fenaco.
- GCP: Kommunikation kann nun vorbereitet werden.
- AWS: nach wie vor zwei Referenzen offen (Brixel und SBB).
- allgemein: Austausch mit Red Hat und TD Synnex. Wir verstehen nun (besser) wie Subs Business bei den Hyperscalern weitergeführt werden kann.', '2024-10-09 14:28:12.35628', '', '2024-10-09 14:28:12.356288', NULL, 16, 1386, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2720, 'Noch keine Aktivität erfolgt. Sinnvollerweise gehen wir das Thema mit den von Benno am Monthly vorgestellten Werkzeugen an.', '2024-10-09 14:29:31.506027', '', '2024-10-09 14:29:31.506033', NULL, 16, 1388, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2721, 'Angebotsdefinition und One-Pager ist in Entstehung.', '2024-10-10 05:57:39.697286', '', '2024-10-10 05:57:39.697292', NULL, 1039, 1443, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2722, '', '2024-10-10 05:59:49.660235', 'In LST-Monthly Modelle und Konzepte abgeholt, auf welchen wir zukünftig Angebot entwickeln wollen. Start mit ersten Bereichen, damit als Beispiel weiter genutzt werden kann.', '2024-10-10 05:59:49.660241', NULL, 1039, 1455, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2724, 'Aktuell einiges am laufen, aber Commit noch nicht ganz erreicht.', '2024-10-14 07:07:14.745154', '', '2024-10-14 07:07:14.745161', NULL, 24, 1391, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2725, 'in Arbeit', '2024-10-14 07:07:32.45586', '', '2024-10-14 07:07:32.455867', NULL, 24, 1394, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2726, 'Leichte Erhöhung, Krank / Unterzeit / Start Simone', '2024-10-14 07:24:13.473824', '', '2024-10-14 07:24:13.473831', 77.92, 3, 1417, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2727, 'Termin in zwei Wochen', '2024-10-14 08:30:28.3024', '', '2024-10-14 08:30:28.302408', NULL, 3, 1419, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2728, 'Switch aufgegleist, Projekt geht weiter bis März (keine Verlängerung, aber stretch)', '2024-10-14 08:31:04.140151', '', '2024-10-14 08:31:04.140157', NULL, 3, 1420, 5, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2729, 'Aktuell noch keine Änderung (Bespinian weiterhin offen)', '2024-10-14 08:31:24.351516', '', '2024-10-14 08:31:24.351523', NULL, 3, 1421, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2730, 'Konsens noch nicht erreicht', '2024-10-14 08:32:13.953255', '', '2024-10-14 08:32:13.953262', NULL, 3, 1422, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2731, 'Keine News, weiterhin an der Research dran. ', '2024-10-14 08:32:32.674132', '', '2024-10-14 08:32:32.674139', NULL, 3, 1423, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2732, 'Draft Subsite Platform Engineering geht diese Woche zur Abnahme an Adi. Subsites Build-CI & GitOps in Bearbeitung und ebenfalls bis Ende Woche bei Adi. ', '2024-10-14 09:48:51.27924', '', '2024-10-14 09:48:51.279247', NULL, 26, 1469, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2733, 'Detaillierte Kommunikationsplanung ist erstellet. Special Mailing als neue Kommunikationsmassnahme erfasst. ', '2024-10-14 09:50:02.078779', '', '2024-10-14 09:50:02.07879', 2, 26, 1470, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2734, 'Startschuss heute, 15.10. mit Bedarfsabklärung Yup und morgen Value Proposition Mobility', '2024-10-15 05:42:24.604709', '', '2024-10-15 05:42:24.604715', NULL, 49, 1443, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2744, 'der Verteilschlüssel haben wir erstellt ', '2024-10-15 09:52:08.518136', '', '2024-10-15 09:52:08.518143', NULL, 41, 1416, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2745, 'ist noch offen ', '2024-10-15 09:52:40.724968', '', '2024-10-15 09:52:40.724975', NULL, 41, 1437, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2746, 'noch nicht gestartet', '2024-10-15 10:39:07.86568', '', '2024-10-15 10:39:07.865686', 0, 41, 1412, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2747, 'OIDC sowie Inkasso als Features sind noch offen aber auf gutem Weg.
SAC hat angefangen das System zu testen ', '2024-10-15 10:40:37.056715', '', '2024-10-15 10:41:00.680676', NULL, 41, 1413, 5, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2735, '', '2024-10-15 06:40:21.097298', '', '2024-10-15 06:41:19.151737', 5, 13, 1392, 2, 'metric', NULL, 6);
INSERT INTO okr_pitc.check_in VALUES (2736, '', '2024-10-15 06:42:20.614773', '', '2024-10-15 06:42:20.61478', 6.45, 13, 1384, 2, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2737, 'Noch nichts unternommen', '2024-10-15 06:43:03.129028', '', '2024-10-15 06:43:03.129035', NULL, 13, 1387, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2738, 'Keine Aktivitäten bisher', '2024-10-15 06:50:53.061887', '', '2024-10-15 06:50:53.061894', NULL, 4, 1446, 10, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2739, 'Keine Aktivitäten bisher', '2024-10-15 06:51:09.616203', '', '2024-10-15 06:51:09.61622', NULL, 4, 1447, 10, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2740, 'Keine Aktivitäten bisher', '2024-10-15 06:51:32.085489', '', '2024-10-15 06:51:32.085496', NULL, 4, 1448, 10, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2723, 'Zertifizierung/Partnerschaft in Arbeit bei AWS.
Angebot soll auf Plattform Engeneering aufbauen, dass Fortgeschritten ist.', '2024-10-10 06:00:22.514731', '', '2024-10-15 07:28:05.788213', NULL, 1039, 1471, 5, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2741, 'Keine Aktivitäten bisher', '2024-10-15 09:23:20.690093', '', '2024-10-15 09:23:20.6901', NULL, 4, 1452, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2742, 'Keine Aktivitäten bisher', '2024-10-15 09:23:29.75983', '', '2024-10-15 09:23:29.759837', NULL, 4, 1456, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2743, 'Im September mit 103 Stunden nicht erreicht, der Auslöser war der Relase der wegen der Migration nicht funktioneit hatte', '2024-10-15 09:51:30.18909', '', '2024-10-15 09:51:30.189097', NULL, 41, 1415, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2748, 'noch nicht gestaret ', '2024-10-15 10:41:23.410678', '', '2024-10-15 10:41:23.410704', NULL, 41, 1439, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2749, 'Status Quo', '2024-10-15 12:33:38.35799', 'Aufträge generieren, Members einplanen', '2024-10-15 12:33:38.357996', 4.5, 4, 1458, 0, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2750, 'Status Quo', '2024-10-15 12:34:59.605148', '"Dev Sales @ Puzzle"-Meeting für Do. 17.10.2024 14-16h aufgegleist.', '2024-10-15 12:34:59.605154', 0, 4, 1459, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2751, 'Präquali ist fast fertig. Partnerschaften (ohne Details: Rates etc.) sind geklärt. Für allfälliges Angebot muss Team nochmals gestaffed werden.', '2024-10-15 15:16:10.037104', '', '2024-10-15 15:16:10.03711', NULL, 5, 1399, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2752, 'Auftragsklärung stattgefunden mit Yup und Marketing. Timeline gesetzt', '2024-10-15 15:16:59.218784', '', '2024-10-15 15:16:59.218796', NULL, 5, 1397, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2753, 'Erste Gespräche erfolgt. Noch keine konkreten Bestellungen', '2024-10-15 15:18:56.837772', '', '2024-10-15 15:18:56.837777', 0, 5, 1405, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2754, 'Mit /sys Übernahme koordiniert, aber noch nicht durchgeführt.', '2024-10-16 06:51:55.4167', '', '2024-10-16 06:51:55.416706', NULL, 27, 1476, 8, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2755, 'User-Guide ist weit fortgeschritten, Admin-Guide fehlt noch.', '2024-10-16 06:52:38.00326', '', '2024-10-16 06:52:38.003266', NULL, 27, 1477, 8, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2756, 'Basierend auf Skripts und Tests in Staging wird Stretch erfolgreich erreicht.

Check-In ist noch Fail, weil Ziel so formuliert ist, dass es erst mit der echten Migration erreicht ist.', '2024-10-16 06:53:16.04706', '', '2024-10-16 06:53:16.047066', NULL, 27, 1478, 10, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2757, 'Wir könnten eine produktive BW-Umgebung hochfahren und loslegen. ', '2024-10-16 06:54:11.004996', '', '2024-10-16 06:54:11.005003', NULL, 27, 1479, 9, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2760, 'Erste Entwürfe sind erstellt. Abstimmung mit Divisions muss noch koordiniert und durchgeführt werden.', '2024-10-17 06:49:14.101055', '', '2024-10-17 06:49:14.101065', NULL, 1034, 1385, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2761, 'Website Inhalte sind in Arbeit und im Review.', '2024-10-17 06:52:25.625647', '', '2024-10-17 06:52:25.625653', NULL, 24, 1394, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2762, 'EGI Case ist in Arbeit und kommt voran. Wir konnten bisher enorm viel KnowHow aufbauen.', '2024-10-17 07:19:38.517987', '', '2024-10-17 07:19:38.517994', NULL, 24, 1391, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2758, 'Erste Liste für Fokuskunden ist in Erarbeitung, anschliessend Diskussion mit GL und BL.', '2024-10-17 06:45:20.596361', '', '2024-10-17 07:27:54.869503', NULL, 1034, 1381, 8, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2759, 'Versuchen die Pipeline gleich mit der ersten Version des neuen CRMs auf Odoo zu erstellen. Ansonsten als Notnagel eine Spreadsheet-Variante.', '2024-10-17 06:46:38.791027', '', '2024-10-17 07:28:45.356212', NULL, 1034, 1383, 6, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2763, 'Draft des Zielbildes erstellt', '2024-10-17 07:48:08.912866', '', '2024-10-17 07:48:08.912874', NULL, 13, 1389, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2764, 'PBS werden wir erst eines führen wenn neue Leute angestellt sind ', '2024-10-17 09:09:12.964171', '', '2024-10-17 09:09:12.964178', 1, 41, 1412, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2765, 'Ausschreibung noch nicht veröffentlicht', '2024-10-18 06:33:46.020242', '', '2024-10-18 06:33:46.020248', NULL, 20, 1403, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2766, 'Ideen für Gewinn von Neukunden sind definiert: Jubiläumsaktion für 25 PT', '2024-10-18 06:36:46.329748', '', '2024-10-18 06:36:46.329755', NULL, 20, 1398, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2767, 'Einsatz für cg bei Vontobel', '2024-10-18 06:38:34.137037', '', '2024-10-18 06:39:11.773577', 2.9, 20, 1404, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2768, 'Auswertung erstellt, MO:Owner orientiert.', '2024-10-18 12:13:50.958853', '', '2024-10-18 12:13:50.958861', NULL, 13, 1387, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2769, 'Angebot für Präquali Nachweise sind eingereicht. Rates noch nicht Gegenstand. Angebotsphase startet am 9.1., falls Präquali geschafft', '2024-10-18 15:12:06.437158', '', '2024-10-18 15:12:06.437167', NULL, 5, 1399, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2770, '', '2024-10-21 06:46:07.968745', '', '2024-10-21 06:46:07.968787', 71.86, 3, 1417, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2771, 'Keine Veränderung - Termin in einer Woche', '2024-10-21 06:46:23.278349', '', '2024-10-21 06:46:23.278365', NULL, 3, 1419, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2772, 'Keine Veränderung', '2024-10-21 06:46:38.299771', '', '2024-10-21 06:46:38.299775', NULL, 3, 1420, 5, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2773, 'Keine Veränderung, Bespinian weiter offen', '2024-10-21 06:46:51.311493', '', '2024-10-21 06:46:51.311498', NULL, 3, 1421, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2774, 'Abgleich mit Pascou gehabt, Weitereintwicklung WAC (HUGO) über Etienne möglich', '2024-10-21 06:47:26.303544', '', '2024-10-21 06:47:26.303548', NULL, 3, 1422, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2776, 'Mayra hat einen ersten, groben Entwurf erstellt. Diese Woche kurzes Review mit MarCom & Pippo, anschliessend verfolgen wir eine Variante weiter und schleifen am Inhalt.', '2024-10-21 08:58:32.173361', '', '2024-10-21 08:58:32.173365', NULL, 49, 1443, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2777, 'Meeting/Auslegeordnung zwischen Saraina, Benno und Jenny auf KW 43 verschoben.', '2024-10-21 09:00:56.38134', '', '2024-10-21 09:00:56.381343', NULL, 49, 1410, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2778, 'Feedback zu Subsite von db und bw erhalten und eingearbeitet. Drafts für 3 Subsites aktuell bei Adi, vier weitere Subsites ausstehend. Ziel: Alle Subsites bis Ende Oktober online. ', '2024-10-22 07:03:10.810273', '', '2024-10-22 07:03:10.810289', NULL, 26, 1469, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2779, 'Status quo, Ende Oktober / November folgt Lancierung', '2024-10-22 07:03:58.427479', '', '2024-10-22 07:03:58.427493', 2, 26, 1470, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2780, '', '2024-10-22 07:06:55.900471', '', '2024-10-22 07:06:55.900496', NULL, 26, 1453, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2781, 'P31 übernommen durch BW, Modelle festgelegt. Erster Bereich in Entwicklung (Mobility)', '2024-10-22 07:07:44.122364', '', '2024-10-22 07:07:44.122382', NULL, 1039, 1455, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2782, 'Angebot weiterhin in Entwicklung', '2024-10-22 07:09:02.446696', '', '2024-10-22 07:09:02.446713', NULL, 1039, 1471, 5, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2783, 'noch eine SIG fehlt für die Commit Zone', '2024-10-22 12:17:15.630105', '', '2024-10-22 12:17:15.630122', NULL, 60, 1432, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2785, '', '2024-10-22 12:22:52.974447', '', '2024-10-22 12:22:52.974464', NULL, 60, 1464, 5, 'ordinal', 'COMMIT', 0);
INSERT INTO okr_pitc.check_in VALUES (2786, '', '2024-10-22 12:23:02.678645', '', '2024-10-22 12:23:02.678662', NULL, 60, 1464, 5, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2790, 'Sync mit UX geplant. Läuft', '2024-10-22 12:31:34.039516', '', '2024-10-22 12:31:34.039531', NULL, 31, 1472, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2787, '', '2024-10-22 12:23:18.346348', '', '2024-10-22 12:24:32.644167', NULL, 60, 1464, 10, 'ordinal', 'TARGET', 2);
INSERT INTO okr_pitc.check_in VALUES (2784, 'Noch keine Updates', '2024-10-22 12:22:35.211701', '', '2024-10-22 12:25:02.093092', NULL, 60, 1463, 6, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2788, 'Draft für Seite steht und ist in Wordpress eingepflegt.', '2024-10-22 12:27:16.709384', '', '2024-10-22 12:27:16.709422', NULL, 31, 1436, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2789, 'In der Planung', '2024-10-22 12:30:34.224337', '', '2024-10-22 12:30:34.224352', 0, 31, 1461, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2791, 'Noch nichts passiert', '2024-10-22 12:32:31.492398', '', '2024-10-22 12:32:31.492413', 0, 60, 1434, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2792, '', '2024-10-22 12:32:40.493359', '', '2024-10-22 12:32:40.493374', 0, 60, 1460, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2793, 'Baby-steps', '2024-10-22 12:37:12.322953', '', '2024-10-22 12:37:12.32296', 13, 31, 1465, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2794, 'SWW haben wir durchgeführt ', '2024-10-25 05:58:21.804446', '', '2024-10-25 05:58:21.804453', 2, 41, 1412, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2795, 'nach dem letzten Steering sind der SAC und wir positiv gestimmt ', '2024-10-25 05:59:32.149167', '', '2024-10-25 05:59:32.149173', NULL, 41, 1413, 7, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2796, 'Im September hat der Relase 2x nicht funktioniert', '2024-10-25 06:15:51.09559', '', '2024-10-25 06:15:51.095598', NULL, 41, 1415, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2797, 'SLA Vorlage und Vertrag sind erstellt und in Prüfung ', '2024-10-25 06:16:22.938169', '', '2024-10-25 06:16:22.938176', NULL, 41, 1416, 5, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2798, 'noch offen', '2024-10-25 06:16:41.131089', '', '2024-10-25 06:16:41.131096', NULL, 41, 1439, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2799, 'Teilweise erledigt ', '2024-10-25 06:17:14.609912', '', '2024-10-25 06:17:14.609919', NULL, 41, 1437, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2800, '', '2024-10-25 08:54:10.799732', 'Noch keine Aktivitäten infolge Ferien stattgefunden', '2024-10-25 08:54:10.799739', NULL, 28, 1444, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2801, 'Noch keine Aktivitäten infolge Ferien stattgefunden', '2024-10-25 08:54:27.594012', 'Entwurf weiter ausarbeiten', '2024-10-25 08:54:27.594019', NULL, 28, 1445, 4, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2802, 'Erster entwurf, noch nicht am Teammeeting gezeigt, da noch vieles offen ist', '2024-10-25 08:55:02.080838', '', '2024-10-25 08:55:02.080845', NULL, 28, 1449, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2803, 'Planung hat begonnen. BAFU eingeplant, SWEB noch mündliche Zusage.', '2024-10-25 08:55:37.533498', '', '2024-10-25 08:55:37.533505', NULL, 28, 1450, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2804, 'Noch keine Aktivitäten infolge Ferien stattgefunden', '2024-10-25 08:55:46.038193', '', '2024-10-25 08:55:46.038202', NULL, 28, 1451, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2805, 'Die RTEs haben offenbar intern alle Jahresbestellungen wieder ausgelöst', '2024-10-28 06:49:58.235458', '', '2024-10-28 06:49:58.235466', 0, 5, 1405, 6, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2806, 'Mit ersten Workshops mit Benno und dem Value Propostion Ansatz geht Tendenz eher Richtung schärferers Mobility Angebot als Richtung generisches Angebot.', '2024-10-28 06:52:20.404084', '', '2024-10-28 06:52:20.404091', NULL, 5, 1397, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2807, 'Draft für allfällige Präsentation erstellt.', '2024-10-28 06:53:45.520877', '', '2024-10-28 06:53:45.520884', NULL, 5, 1399, 10, 'ordinal', 'TARGET', 0);
INSERT INTO okr_pitc.check_in VALUES (2808, 'Konkrete Ideen für Aufträge:
cb wird auch im 2025 im bisherigen SBB Team arbeiten.
jbo bei SBB eingereicht.
ii für AI-Aufgaben (intern) im Gespräch
gb: ev. BFH
jbr: ev. Mobi', '2024-10-28 07:16:25.426979', '', '2024-10-28 07:16:25.426985', 2.9, 20, 1404, 7, 'metric', NULL, 1);
INSERT INTO okr_pitc.check_in VALUES (2809, 'unverändert', '2024-10-28 07:17:35.446278', '', '2024-10-28 07:17:35.446284', NULL, 20, 1403, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2810, 'unverändert', '2024-10-28 07:17:51.008758', '', '2024-10-28 07:17:51.008765', NULL, 20, 1398, 7, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2811, 'Debriefing wegen Releas ', '2024-10-28 07:39:28.145555', 'So wie ich des sehe & Verstanden habe kamen da verschiedene Probleme zusammen:

    Wir haben sehr viele Änderungen auf einmal. (Umzug auf neue DB, Postgres)
    Postgres wurde von Lehrlingen umgesetzt (Sowohl PZ wie auch MVI haben hier erwähnt das dies nicht so schön und nicht so korrekt gemacht wurde und man das evtl. nicht mehr Lehlinge machen lassen sollte oder sie deutlich genauer reviewen müsste) (Man muss auch sehen: Ohne Lehrlinge hätte man Postgres nie gemacht... viel zu aufwändig)', '2024-10-28 07:39:28.145562', NULL, 41, 1415, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2812, 'Während loFi-Wireframes für Claim-Testing ready sind ist der Konsens oder Testing noch nicht gemacht. ', '2024-10-28 07:47:53.574604', '', '2024-10-28 07:47:53.57461', NULL, 3, 1422, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2813, 'Meeting stattgefunden - aktuell ist das IGE noch im Budgetierungs-Modus', '2024-10-28 08:27:35.515259', '', '2024-10-28 08:27:35.51528', NULL, 3, 1419, 5, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2814, 'da	343	von	1422
cbe	0	von	1360
rk	532	von	2134
ck	32	von	2032
nkr	56	von	1219
pmb	0	von	2032
uro	106	von	2235
jsh	74	von	1829
dsm	0	von	1422
jt	484	von	2032
Total	1627	von	17717
Auslastung 9.2%
', '2024-10-28 08:27:43.749769', 'Jahresend-Sales-Sprint....', '2024-10-28 08:27:43.749775', 9.2, 33, 1425, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2815, 'da	361	von	370
cbe	0	von	0
rk	403	von	554
ck	523	von	528
nkr	347	von	317
pmb	138	von	520
uro	598	von	581
jsh	475	von	475
dsm	375	von	370
jt	511	von	528
Total:	3731	von	4243
Auslastung:	87.9%
', '2024-10-28 08:34:26.305593', '', '2024-10-28 08:34:26.3056', 87.9, 33, 1424, 9, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2816, 'September: 14.8%', '2024-10-28 08:36:43.836828', 'Sieht gut aus...', '2024-10-28 08:36:43.836834', 14.8, 33, 1430, 8, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2817, 'Im Oktober bisher: 138 von 520h -> 26.5%', '2024-10-28 08:49:32.091898', 'SNB Zusage erhalten, jedoch Onboarding noch blockiert. Wird zahlen dann etwas verbessern...', '2024-10-28 08:49:32.091904', 26.5, 33, 1426, 5, 'metric', NULL, 0);
INSERT INTO okr_pitc.check_in VALUES (2818, 'Aussichtsreicher Kandidat (R.G.) hat leider abgesagt und geht Fa in der Nähe seines Wohnortes (Kt. GR)', '2024-10-28 08:50:33.933094', '', '2024-10-28 08:51:02.938639', NULL, 33, 1427, 4, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2819, 'Noch keine Referenzstory online. ID ist allerdings geschrieben. Warten auf Approval von Kd.', '2024-10-28 08:51:45.859639', '', '2024-10-28 08:52:07.017884', NULL, 33, 1428, 6, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2820, 'Noch nichts gemacht.', '2024-10-28 08:52:55.512179', 'Kurzfristig: Systematische Auswertung der ZGs', '2024-10-28 08:52:55.512186', NULL, 33, 1429, 3, 'ordinal', 'FAIL', 0);
INSERT INTO okr_pitc.check_in VALUES (2821, 'Aktuelle Einschätzung: Budget zu Boden bis Ende März, danach evtl. fertig - wir versuchen, da etwas zu verlängern. ', '2024-10-28 09:09:54.616384', '', '2024-10-28 09:09:54.61639', NULL, 3, 1420, 4, 'ordinal', 'FAIL', 1);
INSERT INTO okr_pitc.check_in VALUES (2822, 'Aktuell Miro-Board', '2024-10-28 09:15:57.226615', '', '2024-10-28 09:15:57.226622', NULL, 3, 1423, 5, 'ordinal', 'FAIL', 0);


--
-- Data for Name: completed; Type: TABLE DATA; Schema: okr_pitc; Owner: okr-test
--

INSERT INTO okr_pitc.completed VALUES (1000, 86, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1001, 83, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1002, 84, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1003, 104, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1004, 105, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1005, 103, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1006, 77, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1008, 75, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1009, 100, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1010, 76, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1012, 87, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1013, 78, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1014, 79, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1015, 101, 'Selbstorganisation zwar nicht erreicht, aber Learnings sind klar.', 0);
INSERT INTO okr_pitc.completed VALUES (1016, 81, '-> Release war nicht zufriedenstellend ', 0);
INSERT INTO okr_pitc.completed VALUES (1017, 82, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1018, 88, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1019, 102, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1020, 109, 'Zu wenig geplanter Umsatz mit nicht-Red Hat Kunden', 0);
INSERT INTO okr_pitc.completed VALUES (1021, 95, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1022, 97, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1023, 106, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1024, 110, 'Absage des Workshops, keine Einstellungen', 0);
INSERT INTO okr_pitc.completed VALUES (1025, 98, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1026, 99, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1027, 94, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1028, 90, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1029, 89, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1030, 91, 'Wir beschäftigen uns noch zu viel mit Ops und Updates', 0);
INSERT INTO okr_pitc.completed VALUES (1031, 92, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1032, 93, 'Anstellung hinfällig durch interne Members aus anderen Bereichen eingeplant.', 0);
INSERT INTO okr_pitc.completed VALUES (1033, 80, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1034, 1001, '', 0);
INSERT INTO okr_pitc.completed VALUES (1035, 1003, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1036, 1036, 'Allgemein geht es uns gut. MAGs waren gut, viele Massnahmen. OKR interessiert teilweise. Druck seitens SAC ist gross  ', 0);
INSERT INTO okr_pitc.completed VALUES (1037, 1037, 'Sehr viele Events, PL Nachfolge aufgegleist, BAFU noch a.i. bei OBR jedoch Lösung in Sicht', 0);
INSERT INTO okr_pitc.completed VALUES (1038, 1019, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1039, 1020, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1041, 1005, 'Wir konnten in einem kleinen Bereich unsere Erträge durch die MOs steigern, sehen aber klar noch Potential!
Betreffend Messbarkeit haben wir unsere Ziele nur teilweise erreicht, haben aber sicher eine bessere Basis für weitere Entscheide.', 0);
INSERT INTO okr_pitc.completed VALUES (1045, 1014, 'Bis Ende Geschäftsjahr ist die Auslastung gesichert. So gesehen erreicht.
Aber die Auslastung geht bis Aug/ Sep, für die meiseten sogar bis Ende 2024. Okt-Dez sind aber noch nicht klar. Events wurden nicht besucht und weniger Leads generiert als vorgesehen. Ergo: Nicht erreicht.', 0);
INSERT INTO okr_pitc.completed VALUES (1046, 1015, 'Vieles ist aufgegleist und Ongoing und wird in den nächsten OKR-Zyklus genommen. Hesst aber auch, Zeil nicht erreicht.', 0);
INSERT INTO okr_pitc.completed VALUES (1048, 1022, 'Obwohl es mit grossen Schritten in die richtige Richtung geht und wir vieles erreicht haben (Neukunden gewonnen IGE und HRM) und viel für das nächste Quartal vorgelegt habe, ist es immer noch zu wenig - wir wollen mehr, wir wollen besser werden und sind ambitioniert und zuversichtlich, dass für WAC noch mehr drin liegt. ', 0);
INSERT INTO okr_pitc.completed VALUES (1049, 1025, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1050, 1038, 'Umsetzung der KRs musste in diesem Quartal hinten anstehen bzw. hat zu wenig Priorität erhalten.', 0);
INSERT INTO okr_pitc.completed VALUES (1052, 1008, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1054, 1009, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1055, 1032, 'Ein KR nicht erreicht, daher wird Objective formell nicht erreicht. Effektiv kann Teamzufriedenheit nach den MAGs aber als hoch eingestuft werden.', 0);
INSERT INTO okr_pitc.completed VALUES (1056, 1030, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1057, 1031, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1058, 1040, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1060, 1042, 'Schlecht gewählt, weil nicht direkt von uns beeinflussbar.', 0);
INSERT INTO okr_pitc.completed VALUES (1061, 1035, '', 0);
INSERT INTO okr_pitc.completed VALUES (1062, 1029, 'Ziehen Objective im nächsten Q weiter', 0);
INSERT INTO okr_pitc.completed VALUES (1063, 1041, 'Hier müssen wir schrauben, und haben uns am sync auch schon Gedanken dazu gemacht. Wir werden die Thematik weiterverfolgen, aber wohl nicht mit genau dieser Metrik.', 0);
INSERT INTO okr_pitc.completed VALUES (1064, 1021, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1065, 1026, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1067, 1033, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1068, 1010, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1069, 42, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1070, 43, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1071, 44, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1072, 1058, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1073, 1061, '', 0);
INSERT INTO okr_pitc.completed VALUES (1074, 1060, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1075, 1057, 'Bin hin und her ob erreicht oder nicht, aber seien wir mal Positiv :-) Eine Steigerung kontnen wir feststellen ', 0);
INSERT INTO okr_pitc.completed VALUES (1076, 1056, 'Das Projekt Pauschalne kontne wir noch nicht angehen aus Zeitgründen
Relaseschulungen haben wir auch noch nicht angeboten ', 0);
INSERT INTO okr_pitc.completed VALUES (1077, 1051, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1079, 1050, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1080, 1053, 'Erstes KR Vorgehen gewechselt. Die anderen KRs ein bisschen zu wenig ambitioniert.', 0);
INSERT INTO okr_pitc.completed VALUES (1083, 1076, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1084, 1078, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1085, 1077, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1086, 1080, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1087, 1081, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1089, 1062, 'Nicht ganz erreichet im Sinne der "Weiterentwicklung" ist aber schon etwas gegangen.', 0);
INSERT INTO okr_pitc.completed VALUES (1090, 1063, 'Teilweise erreicht.', 0);
INSERT INTO okr_pitc.completed VALUES (1091, 1064, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1095, 1079, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1096, 1082, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1097, 1084, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1098, 1055, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1099, 1054, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1100, 1066, 'Keine Priorität im Q4 erhalten. Wird ins nächste Quartal übernommen.', 0);
INSERT INTO okr_pitc.completed VALUES (1101, 1067, 'Projekte und Ferien sind geplant. Ausbildung und andere Themen nicht. Architektur Thema noch nicht abgeschlossen', 0);
INSERT INTO okr_pitc.completed VALUES (1102, 1068, 'Als Objective erreicht. KR Digitale Lösungen ist noch nicht so weit wie gewünscht.', 0);
INSERT INTO okr_pitc.completed VALUES (1103, 1065, 'Mit zu erwartendem guten Mai-Abschluss, der noch nicht vorliegt, wird es voraussichtlich knapp für ein "erreicht" reichen.', 0);
INSERT INTO okr_pitc.completed VALUES (1104, 1074, 'Wir hatten keine Zeit für Strategiethemen. Auch blieben einige Teamanliegen noch unadressiert.', 0);
INSERT INTO okr_pitc.completed VALUES (1105, 1075, 'Zwei Bewerber haben ein Jobangebot von uns, beide sind aber noch hängig. Bei zwei Zusagen wäre Objective erreicht. Aus heutiger Sicht muss es als nicht erreicht taxiert werden.', 0);
INSERT INTO okr_pitc.completed VALUES (1106, 1072, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1107, 1073, 'Unsere Zusammenarbeit hat sich verbessert.', 0);
INSERT INTO okr_pitc.completed VALUES (1108, 1071, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1110, 1083, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1111, 1004, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1112, 1090, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1113, 1092, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1114, 1091, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1115, 1101, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1116, 1100, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1117, 1099, 'Besser als erwartet hane wir das KR Kosteneinsparung erreicht, vorallem Bezug auf EInhaltung der Wartungskosten was nun in den letzten 2 Monaten gelungen ist ', 0);
INSERT INTO okr_pitc.completed VALUES (1118, 1093, 'Leider kein Ersatz für Phil, mangelnder Einsatz beim Bewerbungsgespräche führen kann man uns aber nicht vorwerfen :D
Weiterer wichtiger Ops Kunde dazugewonnen', 0);
INSERT INTO okr_pitc.completed VALUES (1119, 1094, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1121, 1112, 'Zukunftsperspektive im Target (Schlagkräfiges Team und Digitale Lösungen), aktuelle Performance im Commit.', 0);
INSERT INTO okr_pitc.completed VALUES (1122, 1114, 'Die ersten zwei KR sind etwas eng formuliert (falsche Baseline) - Die SoMe-Kampagne zeigt Wirkung (+37% Visits auf WAC - wenn was gepostet wird zeigt''s Wirkung, der Inhalt ist nicht wirklich relevant), die Trennung Puzzle/WAC in der Argumentation ist konzeptionell aufgesetzt. ', 0);
INSERT INTO okr_pitc.completed VALUES (1123, 1115, 'Abgang TobiH im OKR-Cycle widerspricht "stabiles" Team, wir haben den gewünschten Level an Performanz noch nicht erreicht', 0);
INSERT INTO okr_pitc.completed VALUES (1124, 1095, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1125, 1096, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1126, 1097, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1127, 1098, 'Konnten zu wenig Zeit investieren in die einzelnen KRs. Wir sind in vielen Bereichen vorwärts gekommen, aber nicht so weit wie erhofft.', 0);
INSERT INTO okr_pitc.completed VALUES (1128, 1120, 'Unsere Members sind zufrieden und helfen uns aktiv dabei, das Team weiter zu bringen.', 0);
INSERT INTO okr_pitc.completed VALUES (1129, 1104, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1130, 1102, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1131, 1105, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1132, 1106, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1133, 1107, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1135, 1116, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1136, 1119, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1137, 1109, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1138, 1110, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1139, 1103, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1140, 1111, 'Das Erreichte hat noch zu wenig Substanz', 0);
INSERT INTO okr_pitc.completed VALUES (1141, 1108, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1142, 1118, 'Ziel für Umsatz über Jahreswechsel klar nicht erreicht.', 0);
INSERT INTO okr_pitc.completed VALUES (1143, 1154, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1144, 1146, NULL, 0);
INSERT INTO okr_pitc.completed VALUES (1145, 1152, NULL, 0);


--
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: okr_pitc; Owner: okr-test
--

INSERT INTO okr_pitc.flyway_schema_history VALUES (1, '1', '<< Flyway Baseline >>', 'BASELINE', '<< Flyway Baseline >>', NULL, 'user', '2023-12-11 12:22:04.261832', 0, true);
INSERT INTO okr_pitc.flyway_schema_history VALUES (2, '2.0.0', 'quarterAddStartEndDate', 'SQL', 'V2_0_0__quarterAddStartEndDate.sql', 544038146, 'user', '2023-12-11 12:22:04.5651', 99, true);
INSERT INTO okr_pitc.flyway_schema_history VALUES (3, '2.0.1', 'objectiveCeeatedByCreatedOnState', 'SQL', 'V2_0_1__objectiveCeeatedByCreatedOnState.sql', 303166416, 'user', '2023-12-11 12:22:04.753076', 33, true);
INSERT INTO okr_pitc.flyway_schema_history VALUES (4, '2.0.2', 'keyResultRefactoringDropAttrsAddNew', 'SQL', 'V2_0_2__keyResultRefactoringDropAttrsAddNew.sql', -54359160, 'user', '2023-12-11 12:22:04.846415', 34, true);
INSERT INTO okr_pitc.flyway_schema_history VALUES (5, '2.0.3', 'checkInRefactoringDropAttrsAddNew', 'SQL', 'V2_0_3__checkInRefactoringDropAttrsAddNew.sql', -2039253579, 'user', '2023-12-11 12:22:04.949235', 23, true);
INSERT INTO okr_pitc.flyway_schema_history VALUES (6, '2.0.4', 'createAlignmentTable', 'SQL', 'V2_0_4__createAlignmentTable.sql', -47379277, 'user', '2023-12-11 12:22:05.048607', 30, true);
INSERT INTO okr_pitc.flyway_schema_history VALUES (7, '2.0.5', 'createCompletedTable', 'SQL', 'V2_0_5__createCompletedTable.sql', 603612473, 'user', '2023-12-11 12:22:05.148529', 20, true);
INSERT INTO okr_pitc.flyway_schema_history VALUES (8, '2.0.6', 'changeOrdinalValidationLengthAndUseUnitEnum', 'SQL', 'V2_0_6__changeOrdinalValidationLengthAndUseUnitEnum.sql', -191660005, 'user', '2023-12-11 12:22:05.180161', 16, true);
INSERT INTO okr_pitc.flyway_schema_history VALUES (9, '2.0.7', 'createAuthorizationOrganisation', 'SQL', 'V2_0_7__createAuthorizationOrganisation.sql', 1438980338, 'user', '2023-12-11 12:22:05.268124', 31, true);
INSERT INTO okr_pitc.flyway_schema_history VALUES (10, '2.0.8', 'addForeignKeysAndIndexes', 'SQL', 'V2_0_8__addForeignKeysAndIndexes.sql', 1474907713, 'user', '2023-12-11 12:22:05.345952', 140, true);
INSERT INTO okr_pitc.flyway_schema_history VALUES (11, '2.0.9', 'addOptimisticLocking', 'SQL', 'V2_0_9__addOptimisticLocking.sql', -282349765, 'user', '2023-12-11 12:22:05.50742', 96, true);
INSERT INTO okr_pitc.flyway_schema_history VALUES (12, '2.0.10', 'createActionTable', 'SQL', 'V2_0_10__createActionTable.sql', 178406264, 'user', '2023-12-11 12:22:05.650409', 19, true);
INSERT INTO okr_pitc.flyway_schema_history VALUES (13, '2.0.11', 'updateSequences', 'SQL', 'V2_0_11__updateSequences.sql', -473016600, 'user', '2023-12-11 12:22:05.680562', 11, true);
INSERT INTO okr_pitc.flyway_schema_history VALUES (14, '2.1.0', 'createOverviewView', 'SQL', 'V2_1_0__createOverviewView.sql', -563749255, 'user', '2023-12-11 12:22:05.759674', 87, true);
INSERT INTO okr_pitc.flyway_schema_history VALUES (15, '2.1.1', 'createAlignmentSelectionView', 'SQL', 'V2_1_1__createAlignmentSelectionView.sql', 1376990314, 'user', '2023-12-11 12:22:05.856981', 6, true);
INSERT INTO okr_pitc.flyway_schema_history VALUES (16, '2.1.2', 'removeNotNullConstraintFromQuarterDates', 'SQL', 'V2_1_2__removeNotNullConstraintFromQuarterDates.sql', -215157812, 'user', '2024-01-29 15:43:54.038207', 95, true);
INSERT INTO okr_pitc.flyway_schema_history VALUES (17, '3.0.0', 'changeTeammanagement', 'SQL', 'V3_0_0__changeTeammanagement.sql', -1504914286, 'okr-test', '2024-10-28 16:44:25.264497', 233, true);


--
-- Data for Name: key_result; Type: TABLE DATA; Schema: okr_pitc; Owner: okr-test
--

INSERT INTO okr_pitc.key_result VALUES (1006, 0, NULL, 'Jeweils einmal im Monat organisiert ein Member vom BBT einen Event', 3, 'Einmal im Monat findet ein Teamevent statt.', 15, 1003, 15, 'NUMBER', 'metric', '2023-12-12 07:08:34.111147', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (201, 0, '2023-12-12 08:53:29.294078', '(Commit Zone): Das Konzept der DR wird im Team aktiv genutzt und von den Members verstanden.
(Target Zone): DR ist in den /mid Prozessen und Entscheidungsmechanismen verankert. Wir bestimmen verschiedene KPIs (absolute Verrechenbarkeit, Umsatz/Gewinn pro Member, etc.) die in Q3 als Messwerte eingesetzt werden können.
(Stretch Goal): Die Divisions-Planung für Q3 basiert auf der Divisionsrentabilität.', 100, 'Die Divisionsrentabilitätsrechnung ist implementiert und im Team existiert ein Verständnis dafür.', 60, 99, 60, 'PERCENT', 'metric', '2023-09-19 18:49:48.551845', NULL, NULL, NULL, 2);
INSERT INTO okr_pitc.key_result VALUES (1036, NULL, '2023-12-18 12:22:52.199538', '', NULL, 'Neuer Auftrag gewonnen durch intensivierte Kontaktpflege bestehender Kunden.', 5, 1009, 5, NULL, 'ordinal', '2023-12-18 12:07:40.794381', 'Physische Treffen mit allen unseren A-Kunden durchgeführt', 'Dadurch mindestens ein Angebot erstellen können (mind 1 Member für 6 Monate oder äquivalent)', '1 Neuer Auftrag (mind 1 Member für 6 Monate oder äquivalent) dadurch gewonnen', 4);
INSERT INTO okr_pitc.key_result VALUES (1008, 0, '2023-12-13 14:32:38.50288', 'Ein Zmorgen kann auch aus je einem Gipfeli bestehen', 1, 'Einmal pro Quartal bringt jemand ein Frühstück', 15, 1003, 15, 'NUMBER', 'metric', '2023-12-12 07:10:30.283963', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1020, NULL, '2023-12-19 07:12:36.966488', '', NULL, 'Die Bekanntheit von unserem MLOps Angebot ist gesteigert', 5, 1008, 51, NULL, 'ordinal', '2023-12-18 07:41:16.038377', 'Partnerprogramme sind überprüft, um Kooperationen und Präsentationen gezielt anzugehen', 'Mit 2 Partnern ist eine Kooperation initiiert und an 2 Anlässen ist oder wird das Angebot präsentiert', 'Aus den Akivitäten sind drei neue Leads entstanden', 3);
INSERT INTO okr_pitc.key_result VALUES (1186, NULL, NULL, '', NULL, 'Wir haben ein Bedrohungsmodell für Puzzle definiert.', 27, 1071, 27, NULL, 'ordinal', '2024-03-19 12:03:01.575285', 'Threat Model ist ad-hoc definiert.', 'Threat Model nach STRIDE definiert und in relevante Prozesse eingebettet.', 'Individuelle Models für ein oder mehrere Projekte zusammen mit den Devs entwickelt.', 0);
INSERT INTO okr_pitc.key_result VALUES (1092, 0, '2024-01-29 08:51:49.961275', 'In den Accounts von sfa und rbr gibt es total 13 A/A+ Kunden:
Aveniq Avectris AG
Bank Julius Bär & Co. AG
Bundesamt für Meteorologie und Klimatologie MeteoSchweiz
Flughafen Zürich AG
Interdiscount AG
Migros-Genossenschafts-Bund
Schweizerische Nationalbank
Zürcher Kantonalbank
Liechtensteinische Landesverwaltung, Amt für Informatik
TX Group AG (ehem. Tamedia)
SwissSign AG
Swisscom Health AG
Swiss Re Management Ltd', 13, 'Account Plannings für alle A und A+ Kunden von /zh  durchgeführt', 33, 1038, 33, 'NUMBER', 'metric', '2023-12-19 14:26:26.147973', NULL, NULL, NULL, 2);
INSERT INTO okr_pitc.key_result VALUES (1035, NULL, NULL, 'exkl. Subs -> wird bereits gemessen.', NULL, 'Wir messen das Potential aller Marktopportunitäten', 13, 1005, 13, NULL, 'ordinal', '2023-12-18 11:58:12.961831', 'Wir können alle Deals mittels Highrise messen und auswerten.', 'Wir können auch die Umsätze für alle Marktopportunitäten messen.', 'Wir steigern sowohl die Anzahl Deals sowie den gemessenen Umsatz per Februar 2024 im Vergleich zum Dezember 2023.', 0);
INSERT INTO okr_pitc.key_result VALUES (50, 0, '2023-06-20 06:21:05.06711', 'Stichdatum 15.9.', 100000, 'Operations: Wiederkehrende Erträge generiert durch neue Aufträge gemessen an den Pauschalen ', 16, 44, 16, 'CHF', 'metric', '2023-06-20 06:21:05.06711', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1141, 0, '2024-03-21 12:53:16.133836', 'Das Gesamtpotential dieser Massnahme ist im Nice Case +30kCHF pro Quartal bis Ende 2025 (ggü. Q1_23/24) , das ergibt über die verbleibenden 7 Quartale gemittelt +4.285 kCHF pro Quartal als Target.  Der Stretch liegt somit bei 6121. Wichtig: Der Cash-in Muss noch nicht in diesem Quartal erfolgen.', 6121, 'Erste strategischen Massnahmen, die keine Vertiefung benötigen, sind mit finanzieller Wirkung umgesetzt', 5, 1053, 5, 'CHF', 'metric', '2024-03-07 14:59:39.632218', NULL, NULL, NULL, 4);
INSERT INTO okr_pitc.key_result VALUES (1208, NULL, NULL, '', NULL, 'Wir überarbeiten unseren Actionplan zur Steigerung unserer Rentabilität mit konkreten Massnahmen', 36, 1079, 36, NULL, 'ordinal', '2024-03-19 13:06:55.492661', 'Actionplan überarbeitet und mindestens 5 neue konkrete Massnahmen eruiert und mit SUM Buddy abgesprochen', 'Actionplan mit Inputs Sum Buddy überarbeitet und vom LST Team abgenommen', 'Feedback von LST Team eingearbeitet und erste Massnahmen umgesetzt', 0);
INSERT INTO okr_pitc.key_result VALUES (1210, 0, NULL, '', 12, 'Wir posten jede Woche mind. 1 Beitrag auf SoMe', 36, 1082, 36, 'NUMBER', 'metric', '2024-03-19 13:08:06.359845', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1090, NULL, NULL, '', NULL, 'Wir aquirieren mit dem Verkauf zusammen 2 Aufträge', 4, 1014, 4, NULL, 'ordinal', '2023-12-19 14:24:23.607855', '2 Aufträge die in Frage kommen, für die wir uns interessieren', '2  Offerten/ Konzepte für Aufträge eingereicht, die passen', '2 Aufträge erhalten', 0);
INSERT INTO okr_pitc.key_result VALUES (1021, NULL, '2023-12-21 14:33:46.807044', '', NULL, 'Erste Umsätze mit dem MLOps Angebot sind erzielt', 5, 1008, 51, NULL, 'ordinal', '2023-12-18 07:41:16.043054', 'Umsatz und Deals sind messbar im Highrise mit spezifischen Tag', '1 Member im Einsatz (verrechenbar) ODER 1 Lab mit min. 4 Teilnehmern durchgeführt', '1 Member im Einsatz (verrechenbar) UND 1 Lab mit min. 4 Teilnehmern durchgeführt', 5);
INSERT INTO okr_pitc.key_result VALUES (1064, 92, '2024-01-08 14:31:43.320036', 'Commit (0.3): 1 Monat über 95%; Target 0.7: 2 Monate über 96%; Stretch (1.0): 3 Monate über 98%. Stichtag 31.3 (Monatsabschlüsse Jan, Feb, März) ', 99, 'wir halten die Kundenauftrags-Verrechenbarkeit auf dem Rekordhoch (Qualität)', 41, 1030, 41, 'PERCENT', 'metric', '2023-12-19 13:26:23.169839', NULL, NULL, NULL, 6);
INSERT INTO okr_pitc.key_result VALUES (1217, NULL, '2024-03-19 13:21:04.213815', '', NULL, 'Wir weiten das 3rd-Level Angebot weiter aus', 40, 1083, 40, NULL, 'ordinal', '2024-03-19 13:15:31.926178', '1 Offerten raus', '1x Unterschrieben', '2x Unterschrieben', 1);
INSERT INTO okr_pitc.key_result VALUES (1091, NULL, '2024-01-08 14:52:49.057202', 'Wir halten das monatliche Wartungsbduget von 63.5h (gemessen wird Jan,Feb und März 24) ', NULL, 'wir halten uns an das Wartungsbudget', 41, 1030, 41, NULL, 'ordinal', '2023-12-19 14:25:51.21142', '>63.5 h pro Monat aber für mich ist es Fail', '<63.5 h pro Monat', '< 50 h pro Monat ', 5);
INSERT INTO okr_pitc.key_result VALUES (1094, 300000, '2024-01-26 08:55:50.880922', '', 400000, 'Rekord-Umsatz für Februar 2024 (trotz kurzem Monat)!', 40, 1035, 60, 'CHF', 'metric', '2023-12-19 14:28:44.88853', NULL, NULL, NULL, 3);
INSERT INTO okr_pitc.key_result VALUES (1200, 0, '2024-04-02 07:10:53.41092', 'Commit: 80%
Target: 90 %
Stretch: 100 %', 100, 'Alle Bilder und Grafiken sin dim Wordpress eingepflegt. ', 26, 1077, 49, 'PERCENT', 'metric', '2024-03-19 12:41:20.415988', NULL, NULL, NULL, 2);
INSERT INTO okr_pitc.key_result VALUES (1174, 50, '2024-04-23 16:37:44.652675', 'Massgeblich ist der Anteil verplanter Stunden relativ zur den planbaren Stunden auf der Ansicht https://time.puzzle.ch/plannings/departments/15/multi_employees
(Baseline ist 50%)', 90, 'Auftragsbücher füllen: GJ25-Q1 Auslastung im Forecast erreicht 90% der Gesamtkapazität der verrechenbaren Pensen', 33, 1075, 33, 'PERCENT', 'metric', '2024-03-19 10:14:03.608472', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1147, NULL, '2024-05-02 15:41:25.067141', '', NULL, 'Die strategischen Stossrichtungen mit Vertiefungsphasen sind mit einer Roadmap geplant.', 5, 1053, 5, NULL, 'ordinal', '2024-03-07 15:10:04.657755', 'Alle strategischen Stossrichtungen sind geplant', 'Im Mobcoeur Team abgestimmt', 'Mit CTO abgestimmt', 4);
INSERT INTO okr_pitc.key_result VALUES (1007, 0, NULL, 'Paco nimmt diesen Punkt in die Retro auf und es wird von allen offen Kommuniziert.', 6, 'Mindestens einmal pro 2 Wochen halten wir eine Aussprache über das Wohlbefinden im Team.', 15, 1003, 15, 'NUMBER', 'metric', '2023-12-12 07:09:47.42594', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (216, 0, '2023-12-12 12:44:03.437601', 'Commit = CHF 160 / Target = CHF 175 / Stretch = CHF 190 - das KR ist eigentlich ein ordinales ;) ', 1, 'Stundensätze bei neuen Offerten erhöhen ', 36, 104, 36, 'PERCENT', 'metric', '2023-09-25 08:27:52.616003', NULL, NULL, NULL, 3);
INSERT INTO okr_pitc.key_result VALUES (1069, 0, '2024-01-29 09:11:20.622267', 'Trotz sich abzeichnenden Auslastungslücken wollen wir sehr rentabel bleiben.
Der Messwert ist kumuliert zu lesen (3mal Erreichung des Stretch-(Monats-)Ziels -> 3*15%=45%)', 45, 'Durchschnittlich 15% Marge in den Monatsabschlüssen (Dez-Feb)', 33, 1033, 33, 'PERCENT', 'metric', '2023-12-19 14:02:29.868478', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1037, 0, '2024-01-29 12:26:48.731005', 'Es wurden drei Action Items definiert und umgesetzt.', 6, 'Konkrete Verbesserungen am Supportprozess', 32, 1012, 32, 'NUMBER', 'metric', '2023-12-18 13:03:07.482642', NULL, NULL, NULL, 5);
INSERT INTO okr_pitc.key_result VALUES (1023, NULL, '2023-12-18 12:29:40.324755', '', NULL, 'Dank gezieltem Targeting an Networking Anlässen konnten wir konkrete Angebote einreichen.', 5, 1009, 51, NULL, 'ordinal', '2023-12-18 07:41:33.924624', 'Networking Anlässe im Q3 sind besucht, aufgrund klarer Auswahlkriterien und Targetingstrategie', 'Dank dieser Networking Anlässen konnten wir mindestens ein  konkretes Angebot machen (mind 1 Member für 6 Monate oder äquivalent)', 'Aus den Angeboten ist mindestens ein Auftrag gewonnen (mind 1 Member für 6 Monate oder äquivalent)', 6);
INSERT INTO okr_pitc.key_result VALUES (1022, NULL, '2023-12-18 13:12:13.136358', '', NULL, 'Durch reaktivierte Partnerkontakte haben wir einen neuen Auftrag gewonnen.', 5, 1009, 20, NULL, 'ordinal', '2023-12-18 07:41:33.924039', 'Mit mindestens 5 priorisierten Partnern / Sublieferanten haben wir physische Meetings durchgeführt.', 'Dadurch konnten wir mindestens ein konkretes Angebot machen (1 Member für mind. 6 Mte, oder äquivalent)', 'Dadurch ist 1 neuer Auftrag gewonnen (1 Member für mind. 6 Mte, oder äquivalent)', 3);
INSERT INTO okr_pitc.key_result VALUES (1045, 0, NULL, '10 Referenzen = 100 Prozent', 100, 'Wir erstellen pro Division mindestens eine Referenz, die wir dann im Q4 im neuen Websitedesign publizieren. ', 26, 1020, 26, 'PERCENT', 'metric', '2023-12-19 12:00:14.932033', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1105, NULL, NULL, '', NULL, 'Wir teilen unsere coolen Projekte mit der Welt und veröffentlichen neue Referenzen', 36, 1025, 36, NULL, 'ordinal', '2023-12-21 11:50:14.95926', 'Wir veröffentlichen auf der We Are Cube Website zwei neue WAC Referenzen', 'Wir veröffentlichen auf der We Are Cube Website zwei neue WAC Referenzen und haben den Referenzen Prozess klar definiert', 'Wir veröffentlichen auf der We Are Cube Website drei neue WAC Referenzen und haben den Referenzen Prozess klar definiert', 0);
INSERT INTO okr_pitc.key_result VALUES (1107, 9, NULL, 'Ein wöchentliches Meeting, um /security untereinander zu koordinieren. Auf 30'' beschränkt, um nicht zu viel Zeit zu verlieren.', 12, 'Wir haben regelmässige sync-meetings.', 27, 1040, 27, 'NUMBER', 'metric', '2024-01-15 08:07:37.213831', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1066, 0, '2024-01-25 11:53:50.231963', 'Commit (0.3): Release sind deployed und kommuniziert; ; Target (0.7): weniger als 3 Bugs (1.0): : weniger als 1 Bugs; ', 1, 'Hitobito Release für Q3 (Q1''24)', 41, 1031, 41, 'NUMBER', 'metric', '2023-12-19 13:55:54.978315', NULL, NULL, NULL, 2);
INSERT INTO okr_pitc.key_result VALUES (1116, NULL, '2024-01-30 09:50:53.970721', '', NULL, 'Wir treiben das interne Projekt für die neue Website voran und erarbeiten die Sitemap, die Wireframes sowie das Inhaltskonzept jeder Subsite.', 26, 1019, 26, NULL, 'ordinal', '2024-01-30 08:18:49.524768', 'Alle Divisions wurden mit der neuen Sitemap abgeholt und sehen sich darin vertreten. ', 'Die Seitenarchitektur steht und die Wireframes sind abgenommen.', 'Für jede Subsite steht das Inhaltskonzept sowie das textliche und visuelle Grundgerüst.', 2);
INSERT INTO okr_pitc.key_result VALUES (1119, NULL, '2024-02-26 09:22:27.591936', '', NULL, 'Das OKR wird zum Vorzeigeprojekt für hervorragende Usability', 15, 1001, 3, NULL, 'ordinal', '2024-02-12 12:09:53.1468', 'Wenn ein User die Applikation zum ersten mal benutzt, kann dieser ohne nachfragen seine Objectives und Key Results erfassen und scoren. ', 'Für interne Projekte wird das OKR-Tool als zu erreichender Standard angesehen, wir wollen mindestens 1 Anfrage erhalten, wie wir ein Frontendfeature umgesetzt haben. ', 'Das OKR-Tool wird von UX als Beispiel bei mindestens einem Kunde für ein perfektes Usererlebnis verwendet. Weiter richten sich die restlichen internen Projekte (PTime, Cryptopus und PSkills) nach den Icons und Buttonhirarchien, welche in unserem OKR-Tool implementiert sind. ', 1);
INSERT INTO okr_pitc.key_result VALUES (1149, NULL, '2024-03-19 11:56:27.871509', 'Wir halten das monatliche Wartungsbduget von 63.5h (gemessen wird März, April und Mai 24)  ', NULL, 'wir halten uns an das monatliche Wartungsbudget', 41, 1056, 41, NULL, 'ordinal', '2024-03-15 08:43:59.839807', '1 Monat < 63.5 h bedeutet Commit Zone ', '3 Monate < 63.5 h erreicht bedeutet Target Zone, ', '3 Monate < 60h bedeutet Stretch', 2);
INSERT INTO okr_pitc.key_result VALUES (1187, NULL, NULL, '', NULL, 'Wir haben ein Monitoring in unserem Netzwerk, um potenzielle Bedrohungen zu erkennen.', 27, 1071, 27, NULL, 'ordinal', '2024-03-19 12:07:20.112061', 'Suricata od. equiv. loggt Alerts.', 'Wir haben einen Prozess, um diese Resultate zu verarbeiten.', 'Es gibt automatisierte Alerts und Management dieser Information.', 0);
INSERT INTO okr_pitc.key_result VALUES (1157, 40, '2024-04-22 13:10:37.823908', 'Unsere Verrechenbarkeit soll sich gegenüber Q3 steigern
(ptime: Members -> Auslastung -> OE /Sys)', 50, 'Dank randvollen Auftragsbüchern geniessen wir unsere Sommerferien', 32, 1060, 32, 'PERCENT', 'metric', '2024-03-18 09:23:51.197317', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1128, NULL, '2024-03-19 10:35:44.31777', '', NULL, 'Wir haben zusätzliche verrechenbare Members gewonnen.', 22, 1050, 22, NULL, 'ordinal', '2024-03-07 09:55:24.832574', 'Wir schreiben fünf neue Stellen aus.', 'Wir schreiben fünf neue Stellen aus und schliessen zwei Arbeitsverträge ab.', 'Wir schreiben fünf neue Stellen aus und schliessen vier Arbeitsverträge ab.', 3);
INSERT INTO okr_pitc.key_result VALUES (1221, NULL, NULL, '', NULL, 'Wir haben einen Plan, wer wann warum an welchen Events teilnimmt', 4, 1063, 4, NULL, 'ordinal', '2024-03-19 13:17:36.759498', 'Workshop, Übersicht erstellen (/dev/tre-Wiki)
', 'In Workshop besprechen, Abgleich mit Markom,  Als Thema im Weekly etabliert
', 'Wir klären Sponsoring-Aktivitäten /dev/tre
', 0);
INSERT INTO okr_pitc.key_result VALUES (1213, NULL, '2024-03-19 13:56:09.2245', 'Zu allen unseren Fokus Themen haben wir eine Special Intrest Group (SIG).', NULL, 'SIG haben sich etabliert', 60, 1081, 60, NULL, 'ordinal', '2024-03-19 13:11:04.296225', 'Pro Fokus Thema ist eine SIG definiert', 'Pro SIG ist ein RocketChat-Channel definiert und die entsprechenden Members hinzugefügt. Die Rahmenbedingungen sind geklärt und akzeptiert.', 'SIGs verankert -> FIXME: besser schärfen', 1);
INSERT INTO okr_pitc.key_result VALUES (1191, 86609, '2024-05-13 06:58:08.509889', 'PTIME > Aufträge > Umsatz Juli bis September', 250000, 'Prognostizierter Umsatz im Q1 ist gesichert ', 36, 1079, 36, 'CHF', 'metric', '2024-03-19 12:16:38.579741', NULL, NULL, NULL, 4);
INSERT INTO okr_pitc.key_result VALUES (1214, 14, '2024-03-21 08:24:19.421798', '', 150, 'Auf unserer WAC Linkedin Seite erreichen wir 150 Linkedin Follower', 36, 1082, 36, 'NUMBER', 'metric', '2024-03-19 13:11:38.120287', NULL, NULL, NULL, 2);
INSERT INTO okr_pitc.key_result VALUES (218, 0, '2023-12-12 12:50:23.39201', 'Eigentlich ein ordinales Ziel
Commit = Diskutiert, Bestätigung, Lösungsansätze, Verbesserungsvorschläge erarbeitet im Team
Target = Teilen mit Sumbuddy & AUMC
Stretch = Aktive Teilnahme an der Umsatzung innerhalb Puzzle', 1, 'Puzzle Grundsätze sind bekannt und diskutiert', 3, 105, 3, 'PERCENT', 'metric', '2023-10-17 07:18:22.846575', NULL, NULL, NULL, 2);
INSERT INTO okr_pitc.key_result VALUES (1120, 3, NULL, 'Sonar untersucht den Code auf unsaubere Methoden (schlechte performance, security issues), und auf Code, welcher potentiell Bugs herbeiführen kann, dabei erstellt Sonar ein Rating mit 5 stufen, wobei E die schlechteste Stufe ist und A die beste.', 5, 'Das OKR-Tool verbessert den Sonar Bugscore von C auf A', 15, 1001, 15, 'NUMBER', 'metric', '2024-02-12 12:11:16.032896', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1109, 12, '2024-01-15 08:12:35.026931', 'Die Goals sind basierend auf 1 Issue pro Monat pro Person, Stretch sind 3.', 36, 'Jedes Mitglied von /security hat Issues erfasst.', 27, 1041, 27, 'NUMBER', 'metric', '2024-01-15 08:11:15.578337', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1175, NULL, NULL, '', NULL, 'Rekrutierung: Anstellung eines Senior System Engineers', 33, 1075, 33, NULL, 'ordinal', '2024-03-19 10:17:19.40042', '2 Kandidaten absolvieren Tech-Interview', 'Anstellung und vorgängige Validierung durch Team', 'Anstellung und mind. 50% Auslastung im Forecast für GJ25-Q1', 0);
INSERT INTO okr_pitc.key_result VALUES (1121, NULL, NULL, 'Um zukünftige Wartungsarbeiten oder Weiterentwicklungen am Tool sauber auszuführen und um die Funktionalität des Tools sicherzustellen, wird die Applikation mit verschieden Testsgetestet.', NULL, 'Das OKR-Tool weisst eine ausgezeichnete Testabdeckung auf', 15, 1001, 15, NULL, 'ordinal', '2024-02-12 12:13:03.249546', 'Unit Tests erreichen eine Testabdekung von 80% auf den Methoden und 80% auf den Branches, Controller sind mit IntegrationsTests getestet, einfache EndToEnd Tests sind mit Cypress umgesetzt. ', 'Unit Tests erreichen eine Testabdekung von 80% auf Methoden und Branches, Controller sind mit Integrationstests getestet und EndToEnd Tests, testen Rollenspezifisch die verschiedenen Ansichten der Applikation durch. ', 'Unit Tests erreichen eine Testabdekung von 85% auf Branches und Methoden, Controller sind zu 100% mit Integrationstests getestet, EndToEnd Tests, testen für jede Rolle, jede Ansicht, ausserdem überprüffen die End2End tests, ob jeweils die richtigen Ereignisse geschehen wenn eine Aktion ausgeführt wird. ', 0);
INSERT INTO okr_pitc.key_result VALUES (1046, NULL, '2023-12-19 13:01:08.531948', '', NULL, 'Wir stellen sicher, dass die Planung für das Videoformat «Ein Mate mit...» für Q4/2023 aufgegleist ist und wir technische Themen nach Aussen tragen.', 26, 1020, 26, NULL, 'ordinal', '2023-12-19 12:01:23.260576', '2 Videodreh geplant, 1 Video veröffentlicht.', '2 Videodreh geplant, 1 Video veröffentlicht, 1 Video produziert.', '2 Videodreh geplant, 2 Video veröffentlicht, 1 Video produziert.', 1);
INSERT INTO okr_pitc.key_result VALUES (1067, NULL, NULL, '', NULL, 'Wir haben die Story "Securing your pipeline" auf Basis von Dagger geschrieben', 40, 1029, 31, NULL, 'ordinal', '2023-12-19 13:59:21.694742', 'Wir idenfizieren mögliche Use Cases rund um Dagger', 'Wir definieren ein Lösungskonzept um ausgewählt Use Cases', 'Das Lösungskonzept ist in das CI/CD-Portfolio integriert (und publiziert)', 0);
INSERT INTO okr_pitc.key_result VALUES (1097, NULL, NULL, 'Aus Kapazitätsgründen aus Q2 übernommen.', NULL, 'Wir richten uns nach unseren Entwicklungszielen aus', 33, 1032, 33, NULL, 'ordinal', '2023-12-19 14:41:06.413051', 'Durchführung Ausrichtungsworkshop mit Team und Definition der Themenschwerpunkte.', 'Massnahmenkatalog zur Erreichung der Entwicklungsziele erstellt und mit Team besprochen.', 'Die Umsetzung der wichtigsten Massnahme ist fertig und konkret geplant und Umsetzung hat begonnen.', 0);
INSERT INTO okr_pitc.key_result VALUES (1024, 0, '2023-12-21 14:41:55.876906', 'Gemessen wird als Zusatzfrage für die monatliche Happiness Umfrage', 10, 'Die Confidence aller /mobility Members, dass die ausgewählten strategischen Massnahmen zu einer rentablen Division führen, ist mit einer 7 als Target (Skala 1-10) hoch.', 5, 1010, 20, 'NUMBER', 'metric', '2023-12-18 07:49:56.175737', NULL, NULL, NULL, 3);
INSERT INTO okr_pitc.key_result VALUES (1108, 1, NULL, 'Ein etwas längeres Meeting, um zu überprüfen, wie es im letzten Monat lief und uns zu verbessern.', 3, 'Wir haben regelmässig ein Retro-Meeting.', 27, 1040, 27, 'NUMBER', 'metric', '2024-01-15 08:08:42.693492', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1101, NULL, '2024-01-29 09:59:09.486533', 'Als relevant werden AWS und GCP Zertifizierungen (exkl. Accredidation Courses) gemäss https://codimd.puzzle.ch/3hze5seJQpSjOzaFO4rlSA# betrachtet.', NULL, 'Cloud Zertifizierungen abgeschlossen', 33, 1038, 33, NULL, 'ordinal', '2023-12-19 14:45:00.086456', '2 Commitments von Members, dass sie bis im Sommer eine relevante Zertifizierung abschliessen', '1 relevante Zertifizierung abgeschlossen', '2 relevante Zertifizierungen abgeschlossen', 1);
INSERT INTO okr_pitc.key_result VALUES (1038, 0, '2024-01-29 12:26:58.456491', 'Aus untenstehender List sind alle Punkte erledigt:
- nötige Stories im Sprint sind erfasst und umgesetzt.
- klare Dokumentation zum Onboarding eines generischen Kunden ist vorhanden und umsetzbar
- Überlegungen welche Tools (Backup, Monitoring, OnCall) pro Kunde ready sein müssen', 120, 'Onboarding von potenziellem Kunden ist standardisiert.', 32, 1012, 32, 'PERCENT', 'metric', '2023-12-18 14:53:02.173954', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1122, NULL, '2024-02-26 09:21:51.489966', 'Der erleichterte Umgang mit dem OKR-Framework soll weitere Members dazu Anregen, das Framework von OKRs zu nutzen.', NULL, 'Unser Tool macht das OKR-Framework beliebter', 15, 1001, 5, NULL, 'ordinal', '2024-02-12 12:14:05.023866', 'Mindestens ein Team von Puzzle, welches bis lang nicht am OKR-Framework teilnimmt, wird dank dem Tool neu auch am OKR Teilnehmen. Wir finden dies heraus anhand von Nachfragen wenn neue Teams dazustossen. ', 'Alle Teams von Puzzle nehmen am OKR-Framework Teil. ', 'Alle Teams von Puzzle nehmen am OKR-Framework Teil, zusätzlich haben wir mindestens eine externe Anfrage für unser Tool. ', 1);
INSERT INTO okr_pitc.key_result VALUES (1271, 0, '2024-06-17 07:45:51.109092', 'Commit (0.3): Release ist deployed und kommuniziert; Target (0.7): weniger als 5 Bugs (1.0): : weniger als 2 Bugs; ', 1, 'Hitobito Release für Q1 (Q3''24)', 41, 1100, 41, 'NUMBER', 'metric', '2024-06-17 06:46:15.176579', NULL, NULL, NULL, 2);
INSERT INTO okr_pitc.key_result VALUES (1129, NULL, '2024-03-07 10:51:43.645495', '', NULL, 'Wir haben unser Puzzle OS erweitert und das interne Audit zur Umweltzertifizierung bestanden.', 13, 1051, 13, NULL, 'ordinal', '2024-03-07 09:58:46.642752', 'Draft des erweiterten Puzzle OS erstellt.', 'Internes Audit erfolgreich bestanden.', 'ISO14001 in ISO9001 vollständig integriert.', 3);
INSERT INTO okr_pitc.key_result VALUES (1151, 0, '2024-03-19 13:23:15.946525', 'Commit (0.3): Release ist deployed und kommuniziert; Target (0.7): weniger als 5 Bugs (1.0): : weniger als 2 Bugs; ', 1, 'Hitobito Release für Q4 (Q2''24)', 41, 1057, 41, 'NUMBER', 'metric', '2024-03-15 08:44:18.222095', NULL, NULL, NULL, 2);
INSERT INTO okr_pitc.key_result VALUES (1158, 0, '2024-03-18 15:03:28.400744', 'Wir wollen soviele realistische Neukunden angehen  /anschreiben / zwingen', 5, 'Wir gehen auf potentielle Neukunden der Supportdienstleistung zu', 32, 1060, 32, 'NUMBER', 'metric', '2024-03-18 09:25:59.019966', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1305, NULL, NULL, '', NULL, 'Junior/Praktikant Betreuung und Planung fix', 33, 1109, 33, NULL, 'ordinal', '2024-06-17 23:42:08.88023', 'Junior/Praktikant angestellt (Vertrag unterschrieben)', '+ Betreuung sichergestellt und Planung zu 33% gesichert', '+ Planung zu 66% gesichert', 0);
INSERT INTO okr_pitc.key_result VALUES (1159, NULL, '2024-04-08 06:59:17.17884', '', NULL, 'Der Supportprozess (P35) im PuzzleOS wird überarbeitet', 32, 1061, 32, NULL, 'ordinal', '2024-03-18 09:30:51.054512', 'Inputs von Reto und Tim (Erfahrungen mit ITPoint) sind in den P35 eingeflossen', 'Die Bestehende Dokumentation wurde vollumfänglich überarbeitet und Unklarheiten (z.B. Definition Eskalation) sind beseitigt.', 'Zwei zusätzliche Personen haben P35 so als tipptopp abgesegnet.', 1);
INSERT INTO okr_pitc.key_result VALUES (1152, NULL, '2024-05-13 09:30:37.840891', 'wir führen ein erfolgreiches Communitymeeting durch', NULL, 'das nächste Communitymeeting ist erfolgreich durchgeführt', 41, 1057, 41, NULL, 'ordinal', '2024-03-15 08:44:18.222663', 'Agenda ist min. 2 Wochen vorher versendet', 'es melden sich 20 Teilnehmer an ', '> 20 Teilnehmer nehmen teil an Communitymeeting', 6);
INSERT INTO okr_pitc.key_result VALUES (214, 0, '2023-12-12 13:52:18.347716', 'Auslastungplanung Projekte ermöglicht uns eine bessere Übersicht über den Projekt Status-Quo mit Offerten (design ops)', 100, 'DesignOPS: wir verbessern die Auslastungsplanung', 3, 103, 3, 'PERCENT', 'metric', '2023-10-17 07:17:51.518054', NULL, NULL, NULL, 3);
INSERT INTO okr_pitc.key_result VALUES (1110, 12, NULL, 'Auch hier ist das Ziel ein Task pro Monat pro Person, Stretch sind 3 pro Monat.', 36, 'Wir bringen unsere Issues zum Abschluss.', 27, 1041, 27, 'NUMBER', 'metric', '2024-01-15 08:12:22.958027', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1047, NULL, '2023-12-19 13:05:16.909467', '', NULL, 'Wir erstellen einen Blogpost zu einem exemplarischen Individual-Software-Projekt (z.B. SAC) und schalten dazu eine LinkedIn-Kampagne.', 26, 1020, 26, NULL, 'ordinal', '2023-12-19 12:02:22.125914', 'Individual-Software-Projekt ist definiert und die Inhalte für den Blogpost stehen.', 'Blogpost ist publiziert, LinkedIn-Kampagne läuft.', 'Wir haben >10''000 Impressions auf dem LinkedIn-Post sowie einen generierten Lead.', 1);
INSERT INTO okr_pitc.key_result VALUES (1025, 0, '2023-12-21 14:42:47.438028', 'Zusatzfrage in der monatlichen Happiness Umfrage im März', 10, 'Das Commitment der /mobility Members für die ausgewählten strategischen Massnahmen ist mit einer durchschnittlichen 7 als Target (Skala 1-10) gross.', 5, 1010, 20, 'NUMBER', 'metric', '2023-12-18 07:55:56.968769', NULL, NULL, NULL, 3);
INSERT INTO okr_pitc.key_result VALUES (1098, NULL, '2024-01-14 12:52:12.307977', '', NULL, 'Der Begriff selbstorganisiert und die Relevanz für /dev/tre sind geklärt', 4, 1015, 4, NULL, 'ordinal', '2023-12-19 14:41:33.256793', 'Workshop durchgeführt um das Verständnis der, die Erwartungen an die und den Zweck der Selbstorganisation in /dev/tre zu ermitteln.', 'Gemeinsamer Massnahmeplan aufgrund bisherigen Diskussionen erstellt, inkl. Zeitachse und Zuständigkeiten.', 'Prinzipien und Erste konkrete Bereiche laufen selbstorganisiert. (Noch konkreter beschreiben)', 3);
INSERT INTO okr_pitc.key_result VALUES (45, 2, '2023-06-14 09:36:51.701417', 'Die FTE beziehen sich auf jene in der "Membersliste". Die Messungen erfolgen jeweils Mitte und Ende des Monats. Stichtag ist 15.9.', 1, 'Wir senken unsere durchschnittliche Auslastungslücke auf 1.0 FTE', 13, 43, 13, 'NUMBER', 'metric', '2023-06-14 09:36:51.701417', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1100, NULL, '2024-01-14 12:56:30.278868', '', NULL, 'Motto und Manifest /dev/tre sind verabschiedet', 4, 1015, 4, NULL, 'ordinal', '2023-12-19 14:42:15.211474', 'Aktuelle, vom Markom vorgeschlagene Versionen von Motte & Manifest im Team kritisch geprüft und besprochen.', 'Gemeinsamer Nenner für Motto und Manifest gefunden und finale Versionen erstellt.', 'Motto und Manifest Puzzle intern & extern (Kunden/ Bewerbende) kommuniziert und Feedback dazu abgeholt', 1);
INSERT INTO okr_pitc.key_result VALUES (1068, 0, '2024-01-29 09:44:13.02354', 'Betrachtet werden die verrechenbaren Pensen. Unter 10% heisst unter 1.02 FTE bei total 10.24 FTE.
Messung jeweils gegen Ende des Monats (Jan-Mar) vorgenommen (Schätzungsungenauigkeit ist dann jeweils klein).

Da KRs mit absteigenden Prozentwerten (Goal tiefer als Baseline) im Tool noch nicht richtig dargestellt werden, wird der monatliche Messwert von der Baseline subtrahiert (bei 10% Lücke -> 15-10=5%, bei 18% Lücke -> 15-18=-3%). Der eingcheckte Wert muss kumuliert interpretiert werden (bei 3maliger Erreichung des Stretch Goals: 3*5=15)', 15, 'Durchschnittliche Auslastungslücke bei den verrechenbaren Pensen liegt unter 10%', 33, 1033, 33, 'NUMBER', 'metric', '2023-12-19 14:00:47.398692', NULL, NULL, NULL, 5);
INSERT INTO okr_pitc.key_result VALUES (1039, 0, '2024-01-29 12:27:07.761193', 'Alle der folgendern Punkte sind erfüllt:

- Es ist definiert wie das neue Büro aussieht
- keine Veto Voten mehr
- Es ist umgesetzt
- Nach Implementations erneutes Review
- Umsetzung allfälliger Inputs aus Review', 120, 'Neues Büro ist schööööön', 32, 1013, 67, 'PERCENT', 'metric', '2023-12-18 14:56:20.975392', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1118, NULL, '2024-01-30 10:37:54.844844', '', NULL, 'Die neue Seitennavigation wird von mindestens fünf Testuser als gut strukturiert und die Keywords als verständlich empfunden. ', 26, 1019, 26, NULL, 'ordinal', '2024-01-30 10:36:05.643337', 'Das UX-Testing ist mit mindestens 5 Testuser geplant.', 'Das UX-Testing wurde mit mindestens 5 Testuser durchgeführt. ', 'Das UX-Testing wurde ausgewertet und die Ergebnisse sind in die Seitennavigation eingeflossen.', 0);
INSERT INTO okr_pitc.key_result VALUES (1131, NULL, '2024-03-07 10:51:32.486278', '', NULL, 'Die Rollen und Tätigkeiten im Marketing und Business Development sind in den Prozessen verankert. ', 24, 1051, 24, NULL, 'ordinal', '2024-03-07 10:08:12.083562', 'Analyse und Auslegeordnung der nötigen Tätigkeiten und Aufgaben im Bereich Marketing und Business Development gegenüber P32&P14.
Bedürfnisse der Divisions gesammelt und mit Auslegeordnung abgeglichen.', 'Wir haben die nötigen Aufgaben und Tätigkeiten in den Prozessen integriert.
Vorhandene Lücken sind erkannt und es ist geklärt wer den Lead übernimmt diese Lücken zu füllen.', 'Wir haben alle Aufgaben einer konkreten Person bzw. einem Team zugeordnet und die Aufgaben werden entsprechend wahrgenommen', 3);
INSERT INTO okr_pitc.key_result VALUES (1160, 0, '2024-03-18 15:02:49.445117', 'Wir verfeinern unseren  Supportprozess und bleiben motiviert die Verfeinerungen voranzutreiben:
- Retromeeting der ersten 3 Monate ITPoint-Support
- Pikettplanung ist bis Ende Jahr definiert
- Erste zwei Retromeeting mit ITPoint sind aufgegleist
- BFH-Retro (mit Kunde) ist durchgeführt
- erste konkrete Supportfälle sind gesammelt, analysiert und mit dem Team geteilt
- Grafik des Supportprozesses ist erstellt und mit dem Kunden geteilt', 6, 'Continuous Improvement / Continuous Dedication (CI/CD) ', 32, 1061, 32, 'NUMBER', 'metric', '2024-03-18 09:37:11.016403', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1177, 10000, '2024-05-02 09:15:47.724829', '', 160000, 'Realisierung von CHF 160''000.- Neugeschäft (Single Year Bookings) mit Red Hat Subscriptions.
', 22, 1076, 16, 'CHF', 'metric', '2024-03-19 11:27:13.028816', NULL, NULL, NULL, 6);
INSERT INTO okr_pitc.key_result VALUES (1176, NULL, NULL, '', NULL, 'Wir sind zertifizierte Partner eines Hyperscalers und generieren Opportunities für dieses Geschäftsfeld.
', 22, 1076, 16, NULL, 'ordinal', '2024-03-19 11:25:34.256824', 'Wir generieren 3 Opportunities für AWS oder GCP in diesem Quartal.
', 'Wir gewinnen einen Auftrag, der Monthly Recurring Revenue bringt.
', 'Die Partnerzertifizierung für AWS oder GCP ist abgeschlossen.
', 0);
INSERT INTO okr_pitc.key_result VALUES (1189, NULL, NULL, '', NULL, 'Puzzle hat einen Image Lifecycle Prozess.', 27, 1071, 27, NULL, 'ordinal', '2024-03-19 12:11:24.226837', 'Es gibt automatisierte Alerts wenn Images altern, welche die Verantwortlichen informieren.', 'Es gibt einen Prozess, welcher Teil des PuzzleOS oder QM ist, um Images aktuell zu halten.', 'Es gibt keine Images mehr, welche über 3 Monate alt sind.', 0);
INSERT INTO okr_pitc.key_result VALUES (1153, NULL, '2024-03-19 13:19:57.167526', 'Wir haben nun die Relases standartisiert und vesenden Releasenotes. Nun wollen wir das Angebot ausbauen und 4 Schulungen/Jahr anbieten', NULL, 'wir bieten Release Schulungen für unsere Kunden an-> Rentabilität, Verrechenbarkeit PL ', 41, 1056, 41, NULL, 'ordinal', '2024-03-15 09:02:44.952065', 'Intresse an Communitymeeting abgeholt ', 'Schulungskonzept ist vorhanden', 'Schulungseinladung ist bereits versendet ', 2);
INSERT INTO okr_pitc.key_result VALUES (1285, NULL, '2024-06-18 12:29:44.298538', '', NULL, 'Wir gewinnen einen Indiv. Neukunden für eine Umsetzung im Jahr 2025', 41, 1099, 41, NULL, 'ordinal', '2024-06-17 11:22:49.625032', 'Sales Kampagne erstellt,2 Angebote gestellt ', '1 Kunde gewonnen', '2 Kunden gewonnen', 3);
INSERT INTO okr_pitc.key_result VALUES (1070, NULL, '2023-12-19 14:10:51.130136', 'Unsere Wartungskosten sind nicht gedeckt und müssen deshalb ein Kosten Konzept erstellen und anschliessend neue Verträge erstellen und verhandeln', NULL, 'wir erhöhen den Ertrag der Pauschalen (Betrieb, Wartung und Support) -> Rentabilität', 41, 1030, 41, NULL, 'ordinal', '2023-12-19 14:04:23.948464', 'Konzept erarbeiten', 'Vertragsvorlage ist erstellt', '1 Kunde hat bereits unterschrieben', 1);
INSERT INTO okr_pitc.key_result VALUES (1178, NULL, '2024-03-19 12:16:27.458864', '', NULL, 'Wir kennen den potentiellen Zielmarkt für Digitale Lösungen (individual Software-Projekte).', 13, 1051, 16, NULL, 'ordinal', '2024-03-07 10:00:18.741243', 'Eine erste Marktanalyse in Form eines Desk-Research ist erstellt und der Kerngruppe digitale Lösungen präsentiert.', 'Die Resultate der Desk-Research Analyse werden erweitert durch eine Telefonkampagne und so geprüft und erweitert.', 'Ein erstes Konzept für die Marktbearbeitung ist erstellt.', 4);
INSERT INTO okr_pitc.key_result VALUES (1099, NULL, '2023-12-19 14:42:39.710231', '', NULL, 'Unser 3rd Level Support weitet sich aus', 31, 1035, 31, NULL, 'ordinal', '2023-12-19 14:41:47.43413', 'Wir identifizieren Kunden, bei denen wir unseren 3rd Level Support offerieren können', 'Wir haben eine 3rd Level Support Offerte versendet', 'Wir haben einen neuen 3rd Level Support Kunden gewonnen', 1);
INSERT INTO okr_pitc.key_result VALUES (1263, NULL, '2024-08-15 07:38:10.219921', '', NULL, 'Durch die Marktvalidierung ist die strategische Stossrichtung New Tech geschärft', 5, 1095, 5, NULL, 'ordinal', '2024-06-13 14:28:33.091655', 'Marktanalyse für die neuen Technologien ist erfolgt und mit CSO und CTO validiert.', 'Die Short List der neuen Technologien ist priorisiert und die nächsten Schritte sind definiert.', 'In einer der drei höchst priorisierten Technologien ist ein Member in einem bezahlten Auftrag (ohne GO).', 4);
INSERT INTO okr_pitc.key_result VALUES (1085, NULL, '2023-12-19 14:59:11.463546', '', NULL, 'das nächste Communitymeeting ist organisert ', 41, 1031, 41, NULL, 'ordinal', '2023-12-19 14:23:10.681474', 'Themen für Communtiymeeting sind klar ', 'Einladung ist verschickt inkl Agenda ', 'Wir werden überrannt von Anmeldungen > 23', 1);
INSERT INTO okr_pitc.key_result VALUES (1132, NULL, '2024-03-08 08:52:31.520158', '', NULL, 'Wir überprüfen und erweitern den Strategie-Prozess und definieren die Marktopportunitäten für das neue Geschäftsjahr.', 16, 1051, 16, NULL, 'ordinal', '2024-03-07 10:12:58.772802', 'Wir haben die bestehenden Marktoppounitäten gereviewed und uns entschieden, welche weiterverfolgt werden.', 'Die neuen Marktopportunitäten für das Geschäftsjahr 2024/2025 sind erarbeitet.', 'Der Strategie ist  mit einer Mittelfristsicht erweitert und die Prozesserweiterung ist im Puzzle OS dokumentiert.', 4);
INSERT INTO okr_pitc.key_result VALUES (1026, 0, '2023-12-21 14:44:54.257068', 'Wird nach jeder der 3-5 Kurzsession aktualisiert. 100% = in allen bisherigen Kurzsessions haben alle 8 Engineers teilgenommen. D.h. bei 5 Kurzsessions ist der Nenner 40, bei 3 Kurzsessions ist der Nenner 24.', 100, 'Die Partizipation der /mobility Members ist stark. Mit 8 Members arbeiten mindestens 1/3 unserer Engineers in jeder Kurzsession aktiv an der Strategie mit.', 5, 1010, 29, 'PERCENT', 'metric', '2023-12-18 08:22:58.918765', NULL, NULL, NULL, 2);
INSERT INTO okr_pitc.key_result VALUES (1111, 90, NULL, '', 100, 'Unsere Trainingskampagne wird von ganz Puzzle ausgefüllt.', 27, 1042, 27, 'PERCENT', 'metric', '2024-01-15 08:31:54.354851', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1048, 0, '2024-01-15 15:10:19.340819', '', 215000, 'Prognostizierter Umsatz GJ 23/24 Q4 gesichert', 36, 1022, 36, 'CHF', 'metric', '2023-12-19 12:37:00.906583', NULL, NULL, NULL, 3);
INSERT INTO okr_pitc.key_result VALUES (1040, 0, '2024-01-29 12:27:21.593725', 'Alle folgendne Punkte sind erfüllt:
- alle Member mit <8 Zufriedenheit werden abgeholt
- Es werden mit dem Member Action Items definiert
- Die Action Items wurden umgesetzt', 120, 'Members die in der Umfrage eine der beiden Zufreidenheiten mit weniger als 8 bewertet haben werden angehört.', 32, 1013, 32, 'PERCENT', 'metric', '2023-12-18 15:01:03.349679', NULL, NULL, NULL, 2);
INSERT INTO okr_pitc.key_result VALUES (1165, NULL, '2024-03-19 13:19:00.860819', '', NULL, 'Die Architekten Rolle ist im Ruby Team geklärt', 28, 1067, 28, NULL, 'ordinal', '2024-03-19 07:04:20.326964', 'Architekt/Dev Lead Hitobito ist geklärt', 'Rolle ist auf Ruby operalisiert, angepasst und mit CTO abgestimmt', '50% der Aufgaben sind Aktionen erfolgt', 2);
INSERT INTO okr_pitc.key_result VALUES (1229, NULL, '2024-03-19 13:39:31.016217', 'Coop-KR mit /mobilty', NULL, 'Weitere /mobility Members sind auf GO im Einsatz.', 40, 1080, 40, NULL, 'ordinal', '2024-03-19 13:39:04.029841', 'Angebotsstory in Kooperation mit CI/CD erarbeitet.', 'Mind. 1 zusätzlicher Member auf GO Auftrag fix eingeplant (Auftrag/Ressource ist vom Kunden bestellt)', 'Mind. 1 neuer Go Auftrag gewonnen.', 1);
INSERT INTO okr_pitc.key_result VALUES (1154, NULL, '2024-03-19 14:27:39.958847', 'Unser Ziel ist es das Zertifikat ISO 14001 bis im Herbst zur nächsten Lieferantenberuteilung dre SBB vorweisen zu können', NULL, 'Wir haben unser Puzzle OS erweitert und das interne Audit vom 30. Mai  2024 zur Umweltzertifizierung bestanden.', 41, 1058, 41, NULL, 'ordinal', '2024-03-15 09:23:14.212613', 'Draft des erweiterten Puzzle OS erstellt. ', 'Internes Audit erfolgreich bestanden', 'ISO14001 in ISO9001 vollständig in Puzzle OS integriert. ', 5);
INSERT INTO okr_pitc.key_result VALUES (1164, NULL, '2024-03-19 12:52:56.683345', '', NULL, 'Wir betreiben unsere Applikationen ideal und stellen eine gute Wartbarkeit sicher', 28, 1066, 28, NULL, 'ordinal', '2024-03-19 07:03:07.132238', 'Priorisierte Massnahmenliste nach Kosten/Nutzen ist definiert', 'Top 3 Prio Massnahmen sind umgesetzt', 'Top 5 Prio Massnahmen sind umgesetzt', 2);
INSERT INTO okr_pitc.key_result VALUES (1190, NULL, '2024-03-19 13:04:58.461589', '', NULL, 'Wir haben eine Trainingskampagne vorbereitet, welche den Devs einen Mehrwert bietet.', 27, 1072, 27, NULL, 'ordinal', '2024-03-19 12:13:59.94989', 'In Zusammenarbeit mit Devs und/oder Sec Champions haben wir ein Thema definiert und dazu passende Materialien oder Ausbildner gefunden.', 'Wir haben eine Trainingskampagne zusammengestellt und mit den Security Champions validiert.', 'Die Kampagne ist lanciert.', 1);
INSERT INTO okr_pitc.key_result VALUES (1135, NULL, '2024-03-28 17:00:29.658597', '', NULL, 'Technologieshift Kafka: Wir haben Knowhow durch Weiterbildung bestehender Members und Neuanstellung soweit gesteigert, dass wir Kafka Aufträge anbieten können.', 20, 1054, 20, NULL, 'ordinal', '2024-03-07 14:45:33.292335', 'Strategieziel definiert, Stelleninserat aufgeschaltet, internes Team definiert.', 'Schulung für internes Team geplant, 1 Kafka Spezialist angestellt und Angebot auf Webseite publiziert.', 'Mind. 1 Auftrag für Kafka Team gewonnen', 3);
INSERT INTO okr_pitc.key_result VALUES (1138, NULL, '2024-05-02 15:42:17.855525', 'Zusätzlich zu Go und Kafka ist geklärt, welche zusätzlichen Technologien bei /mobility gefördert und angeboten werden sollen.
https://codimd.puzzle.ch/UwQ1DyuyQFSXqMoI-h-M4w?both', NULL, 'Wir haben den Schwerpunkt für weitere Technologien definiert und Angebot ausgearbeitet.
', 20, 1054, 5, NULL, 'ordinal', '2024-03-07 14:50:34.285993', 'Eine Auswahl der Technologien ist mit dem Techboard getroffen und mit interessierte Members dafür gewonnen.', 'Die ausgewählten Technolgien sind am Markt validiert (mit unserem Sales und externen Spezialisten)', 'Eine Go To Market Strategie ist mit dem CTO definiert.', 9);
INSERT INTO okr_pitc.key_result VALUES (1228, NULL, '2024-04-16 14:57:00.027618', 'Wir suchen gezielt einen Software Engineer, den wir in Acrvis aufbauen können. Die Stufe muss mindestens Professional sein. Der Beschäftigungsgrad muss mindestens 80% sein.', NULL, 'Wir stellen eine zusätzlichen, geeignete Software entwickelndes Member an', 4, 1064, 4, NULL, 'ordinal', '2024-03-19 13:27:12.483246', 'Ausschreiben', 'Gespräche führen', 'Eine Anstellungung', 2);
INSERT INTO okr_pitc.key_result VALUES (1144, 0, '2024-04-23 09:33:21.862302', '', 2.4, 'Durch koordinierte Aktivitäten mit dem Salesteam gewinnen wir neue Aufträge. ', 29, 1055, 5, 'FTE', 'metric', '2024-03-07 15:05:05.436977', NULL, NULL, NULL, 7);
INSERT INTO okr_pitc.key_result VALUES (1011, NULL, '2023-12-12 13:24:29.670165', 'Das Sales-Team legt gemeinsam mit den VertreterInnen des Weekly fest, welche Events für uns in Frage kommen. Das GJQ3 soll unser Networking Quartal werden an welchem wir viel draussen sind.', NULL, 'Wir halten die Sales- und Networking-Aktivitäten hoch.', 16, 1004, 16, NULL, 'ordinal', '2023-12-12 13:21:23.941009', 'Wir sind an 60% der für uns relevanten externen Events mit mindestens einer Person vertreten.', 'Wir sind an 75% der für uns relevanten externen Events mit mindestens einer Person vertreten.', 'Wir sind an 90% der für uns relevanten externen Events mit mindestens einer Person vertreten.', 1);
INSERT INTO okr_pitc.key_result VALUES (1226, NULL, NULL, '', NULL, 'Es ist geklärt, wie Weiterbildungen im Security-Bereich angegangen werden.', 27, 1073, 27, NULL, 'ordinal', '2024-03-19 13:26:23.172564', 'Mark hat seine Wunschvorstellungen formuliert.', 'Mit den Member Coaches ist geklärt, was und wie das ablaufen soll.', 'Die Members konnten bereits ihre Wünsche äussern und einplanen.', 0);
INSERT INTO okr_pitc.key_result VALUES (1255, NULL, NULL, '', NULL, 'Ersatz Phil', 34, 1093, 34, NULL, 'ordinal', '2024-06-10 14:15:49.958675', 'Gespräche haben stattgefunden', 'Vertrag ist unterschrieben', 'Member hat bereits gestartet', 0);
INSERT INTO okr_pitc.key_result VALUES (1051, NULL, '2023-12-19 12:51:23.973805', '', NULL, 'Wir finden neue Wege um Individual-Software-Projekte zu akquirieren.', 13, 1021, 16, NULL, 'ordinal', '2023-12-19 12:50:53.230735', 'Wir haben ein Konzept für die Akquisition von Individual-Projekten erarbeitet.', 'Das Konzept ist erstellt und durch die Umsetzung sind drei Leads entstanden.', 'Wir gewinnen mindestens einen dieser Leads.', 1);
INSERT INTO okr_pitc.key_result VALUES (1054, 3, NULL, 'Vertriebstreffen mit Direktkunden. Direktkunden sind nicht (Partner)unternehmen, über die Abrechnung erfolgt.', 10, 'Anzahl der Kunden, die auf unsere Vetriebsanfragen positiv reagiert haben.', 17, 1024, 17, 'NUMBER', 'metric', '2023-12-19 12:52:27.340596', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1056, 0, NULL, '', 3, 'Wir schreiben für jeden Puzzle Newsletter einen Beitrag von WAC mit einem relevanten UX Thema.', 36, 1025, 36, 'NUMBER', 'metric', '2023-12-19 12:53:02.784489', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1060, 1, NULL, 'Ein Kunde kauft Support bei uns ein/schliesst einen Supportvertrag ab.', 2, 'Wir haben einen Supportvertrag abgeschlossen', 17, 1023, 17, 'NUMBER', 'metric', '2023-12-19 12:57:14.908974', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1055, 1, '2023-12-19 15:24:14.032067', 'Als Neukunde gilt ein Kunde, der nicht über Partnerunternehmen beauftragt. Die Abrechnung erfolgt direkt.', 6, 'Wir haben Neukunden gewonnen', 17, 1024, 17, 'NUMBER', 'metric', '2023-12-19 12:52:56.986195', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1049, NULL, '2023-12-19 14:12:09.33026', 'Wir wollen die Philosophie bei uns etablieren, dass alle Members (mind. zu einem Teil) verrechenbar werden. Das betrifft vorallem Berti und Mayra die beide ein unverrechenbares Pensum haben. Ziel ist es, dass auch sie beide auf Projekten oder Mandaten verrechenbar arbeiten könnten. Das würde unsere DR steigern', NULL, 'Alle Cubies sind verrechenbar!', 36, 1022, 36, NULL, 'ordinal', '2023-12-19 12:45:03.198043', 'Wir haben alle Members auf Projekte offeriert', 'Alle Members leisten verrechenbare Stunden', 'Berti und Mayra haben beide Projekte (bestätigt) mit mindestens 5 PT Einsatz', 1);
INSERT INTO okr_pitc.key_result VALUES (1050, 25, '2023-12-21 11:58:35.490795', 'Simu hat neben seinem 25% BL Pensum auch ein verrechenbares Pensum von 55%. Wir wollen das optimieren und versuchen zu erreichen, dass er so viel wie möglich verrechenbar arbeiten kann. Er wird aus privaten Gründen sein BL Pensum um 5% reduzieren.', 15, 'Wir optimieren die Bereichsleitung zu Gunsten der Verrechenbarkeit', 36, 1022, 3, 'PERCENT', 'metric', '2023-12-19 12:48:20.314797', NULL, NULL, NULL, 3);
INSERT INTO okr_pitc.key_result VALUES (1102, 0, '2024-01-14 13:10:37.859085', 'Eine (1) Bereichs-Map und elf (11) Updates der Member-Skills auf skills.puzzle.ch sind erstellt', 12, 'Die interne Struktur von /dev/tre und die technologischen Präferenzen sind geklärt', 4, 1015, 4, 'NUMBER', 'metric', '2023-12-19 14:45:14.165165', NULL, NULL, NULL, 3);
INSERT INTO okr_pitc.key_result VALUES (1112, NULL, NULL, '', NULL, 'Wir kommunizieren dazu und bewegen die Leute zum Abschluss des Trainings.', 27, 1042, 27, NULL, 'ordinal', '2024-01-15 08:34:23.139585', 'Es gibt ein Mail an die überfälligen User.', 'Mail und Newsbeitrag.', 'Zweiter Newsbeitrag nachdem ganz Puzzle die Trainings abgeschlossen hat.', 0);
INSERT INTO okr_pitc.key_result VALUES (1027, NULL, '2024-01-18 07:08:41.404181', 'Entscheidung gemäss DACI.
Driver: Division Owner
Approver: Marc Waber (CEO) / GL
Contributer: LST
Informed: /mobility Members', NULL, 'Der Support des Puzzle LST ist gross indem das ganze LST im März der Strategie zustimmt.', 5, 1010, 5, NULL, 'ordinal', '2023-12-18 08:40:16.214906', 'Eine qualifizierte Mehrheit stimmt der Strategie zu', 'Im März Monthly stimmen alle des LST der Strategie zu, obwohl es noch Vorbehalte gibt', 'Im März Monthly stimmen alle des LST ohne Vorbehalt der Strategie zu', 2);
INSERT INTO okr_pitc.key_result VALUES (1071, 200000, '2024-01-29 09:57:20.917352', 'Eingecheckt wird monatlich (Dez-Feb) der höchste bisher erreichte Wert eines Monats.', 300000, 'Wir knacken CHF 300''000.- Marke beim Umsatz', 33, 1033, 33, 'CHF', 'metric', '2023-12-19 14:05:47.093945', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1204, NULL, NULL, '', NULL, 'Wir formulieren unsere Anforderung/ Ansprüche bzgl. “Digitale Lösungen” ', 4, 1063, 4, NULL, 'ordinal', '2024-03-19 12:48:31.170053', 'Workshop, Verständnis «Digitale Lösungen» schaffen, Einodrnung «Individual-Software-Entwicklung» in dieses Thema
', 'Formulierung für /dev/tre erstellen - Mit /ux & /ruby abgeglichen - Gemeinsame Anforderung formulieren
', 'An Markom und im Leadeship-Team präsentiert
', 0);
INSERT INTO okr_pitc.key_result VALUES (1150, NULL, '2024-03-19 12:57:54.651258', 'Unsere Wartungskosten sind nicht gedeckt. es wird deshalb ein Konzept erstellt und anschliessend neue Verträge SLA ausgearbeitet und verhandelt', NULL, 'wir erhöhen den Ertrag der Pauschalen (Betrieb, Wartung und Support)
Jahresziel 2024 -> Rentabilität', 41, 1056, 41, NULL, 'ordinal', '2024-03-15 08:43:59.844382', 'Analyse der Umgebungs komplexität estellt
', 'Wartungskosten pro Kunde sind definiert', '1. Fassung SLA erarbeitet ', 4);
INSERT INTO okr_pitc.key_result VALUES (1179, NULL, '2024-03-21 10:05:23.74659', '', NULL, 'Wir setzen unseren Fokus bei der Content Creation von Anfang an auf SEO. ', 26, 1077, 49, NULL, 'ordinal', '2024-03-19 11:53:40.027952', 'Die duplizierten Meta-Titel und -Beschreibungen bei Blogpost und Referenzen sind korrigiert und bilden eine saubere Basis für den Wechsel in die neue Datenbank. ', 'Alle neuen Hauptseiten werden mit Yoast SEO auf orange (ok) oder grün (gut) in der SEO- und Lesbarkeitsanalyse gebracht. ', 'Die Keywords aus der SEO-Analyse sowie die Keywords der Divisions sind in den Content und das SEO-Tool eingeflossen.', 4);
INSERT INTO okr_pitc.key_result VALUES (1146, NULL, '2024-03-21 12:52:17.969467', '', NULL, 'Ein Controlling System für das Tracken der strategischen Massnahmen ist implementiert.', 5, 1053, 5, NULL, 'ordinal', '2024-03-07 15:03:39.297069', 'Das Controlling System ist definiert.', 'Das Controlling System ist implementiert.', 'Alle strategischen Massnahmen werden bereits aktiv darin getrackt', 3);
INSERT INTO okr_pitc.key_result VALUES (1161, 0, '2024-03-25 12:14:32.348038', 'Der Ausblick auf das GJQ4 ist positiv. Wir wollen die zur Verfügung stehenden Ressourcen möglichst effektiv nutzen und durchschnittlich 16% Marge in den Monatsabschlüssen ausweisen.
Der Messwert ist kumuliert zu lesen (3mal Erreichung des Stretch-(Monats-)Ziels -> 3*16%=48%)', 45, 'Durchschnittlich 15% Marge in den Monatsabschlüssen (März-Mai)', 33, 1065, 33, 'PERCENT', 'metric', '2024-03-19 06:37:37.284726', NULL, NULL, NULL, 2);
INSERT INTO okr_pitc.key_result VALUES (1237, 0, NULL, '', 30000, 'Wir füllen unsere Auftragsbücher für den Sommer mit Entwicklungsprojekten', 17, 1086, 17, 'EUR', 'metric', '2024-04-17 15:53:38.120777', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1238, 0, NULL, '', 1, 'Wir beschäftigen einen Member mit Dev-Skills', 17, 1086, 17, 'FTE', 'metric', '2024-04-17 15:55:07.133679', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1236, 1, '2024-04-23 06:23:30.946362', '', 5, 'Im Büro mehr Pflanzen', 7, 1085, 7, 'NUMBER', 'metric', '2024-04-08 08:40:08.844469', NULL, NULL, NULL, 2);
INSERT INTO okr_pitc.key_result VALUES (1012, 0, '2023-12-12 13:27:52.791281', 'Der Hauptumsatz wird aktuell mittels Red Hat Subscriptions erzielt. Wir möchten im GJQ3 auch Abschlüsse der neuen Partnerschaften Gitlab und CIQ sowie mit Isolvalent/Cilium erzielen.', 5, 'Wir holen uns 5 neue Deals im Handelsgeschäft für Cilium, Gitlab und CIQ.', 16, 1005, 16, 'NUMBER', 'metric', '2023-12-12 13:25:50.370576', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1459, 0, NULL, 'Per Mitte September gibt es 0 passende Aufträge, die von Sales kommen. Per Mitte September sollen dies 4 sein.', 4, 'Neue Aufträge 2025 sind zusammen mit Sales ermittelt', 4, 1160, 4, 'NUMBER', 'metric', '2024-09-17 13:03:51.521722', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1052, 0, NULL, 'Längerfristige Aufträge werden anteilig berücksichtigt', 75000, 'Beauftragungen (Auftragssumme) für bei nicht Red Hat-Kunden liegt über 40 TEUR', 17, 1024, 17, 'CHF', 'metric', '2023-12-19 12:51:05.685084', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1053, 1.95, NULL, 'Stichtag ist 19.03.2024', 0, 'Wir haben keine Cubies mehr auf der Bench!', 36, 1022, 36, 'FTE', 'metric', '2023-12-19 12:51:05.809796', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1059, 0, NULL, 'Mehrjährige Subscriptions werden auf anteilig auf das Jahr angerechnet', 120, 'Das Umsatzvolumen mit Subscriptions beträgt 60 TEUR', 17, 1023, 17, 'CHF', 'metric', '2023-12-19 12:56:09.977084', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1076, NULL, NULL, '', NULL, 'Visualisierungsworkshop als Angebot von WAC aufgenommen ', 36, 1025, 36, NULL, 'ordinal', '2023-12-19 14:10:47.371212', 'Angebot ausgearbeitet und auf Website ersichtlich', 'Visualisierungsworkshop geplant und mit Teilnehmenden gefüllt', 'Visualisierungworkshop mit zahlenden Gästen durchgeführt', 0);
INSERT INTO okr_pitc.key_result VALUES (1077, NULL, NULL, 'Vernehmlassung des Drafts im Teammeeting, Engagement-Möglichkeit schaffen für Interessierte, Rückmeldung nach Abschluss, ....', NULL, 'Team in OKR-Praxis einbinden', 33, 1032, 33, NULL, 'ordinal', '2023-12-19 14:13:04.660573', 'Ablauf des ganzen OKR-Cycles mit Team-Engagement-Möglichkeiten definieren.', 'Ablauf erfolgreich implementiert.', 'Ein pasendes Keyresult wird von Member eingebracht.', 0);
INSERT INTO okr_pitc.key_result VALUES (1081, 0, NULL, 'Check-In inkl. Notiz jeden Montag in sync mit LST-Weekly', 6, 'Die /dev/tre-Members werden im Verlauf des Quartals Aufträgen zugewiesen, bis niemand mehr auf der Membersliste ist', 4, 1014, 4, 'NUMBER', 'metric', '2023-12-19 14:21:07.911777', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1086, 0, NULL, '', 9, 'Wir generieren mit dem Verkauf zusammen 9 neue Leads', 4, 1014, 4, 'NUMBER', 'metric', '2023-12-19 14:23:32.181441', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1134, 2900000, '2024-05-29 07:47:51.317739', 'Forecast Wert des geplanten Dienstleistungsumsatzes aus PTime für das Q1 2024/25 (Pauschalen, Partnerumsätze, Subs sind nicht enthalten). Commit MCHF 3.2, Target: MCHF 3.6, Stretch MCHF 3.9. Stichdatum: 19.06.24', 3900000, 'Dank randvollen Auftragsbüchern geniessen wir unsere Sommerferien.', 13, 1050, 13, 'CHF', 'metric', '2024-03-07 10:14:49.928678', NULL, NULL, NULL, 6);
INSERT INTO okr_pitc.key_result VALUES (1256, NULL, '2024-08-26 14:45:49.124934', '', NULL, 'Plattform Engineering', 34, 1094, 67, NULL, 'ordinal', '2024-06-10 14:24:02.558497', '/sys "hat sich das mal angehört"', '/sys liefert Inputs und arbeitet mit', '/sys übernimmt eine aktive Rolle in der MO', 1);
INSERT INTO okr_pitc.key_result VALUES (1103, 0, '2024-01-14 13:12:00.850898', '5 Massnahmen TBD', 5, 'Die Teamarbeit ist aktiv gefödert', 4, 1015, 4, 'NUMBER', 'metric', '2023-12-19 14:46:14.234388', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1083, 3500, '2024-02-26 09:07:10.383905', 'Alle Ruby Members [14] sind im aktuellen +2 Monate definitiv im ptime geplant. Zählt direkt auf Company KR Memberliste von der GL ein.
Anzahl Stunden geplant im Puzzle Time https://time.puzzle.ch/plannings/departments/12/multi_employees Filter: Nächste 3 Monate
Berechnung von Stretch: 14 Members maximal Stunden = 5265 -> update auf 4750 (gemäss Berechnung im Calc)
Baseline: Annahme Stand 27.12.2023: 3500', 4750, 'Perfekte Planung für Members und Projekte. Keine Members auf der Kapa Liste.', 28, 1037, 28, 'NUMBER', 'metric', '2023-12-19 14:22:49.451548', NULL, NULL, NULL, 8);
INSERT INTO okr_pitc.key_result VALUES (1156, NULL, '2024-03-19 13:29:20.614635', 'MVP ist erfolgreich implementiert und der SAC ist äusserst zufrieden ', NULL, 'Wir sind im Projekt SAC Features Ready per 30.6.2024', 41, 1057, 41, NULL, 'ordinal', '2024-03-15 09:55:07.848663', 'SAC ist der Ansicht, dass wir unser Bestes geben und ist zufrieden.', 'Projekt ist in time sowie in budget ', '-', 4);
INSERT INTO okr_pitc.key_result VALUES (1205, NULL, '2024-03-19 13:15:14.194229', 'Ziel ist, /security als interne "Security-Consultants" zu etablieren, welche für Projekte angefragt werden können.', NULL, 'Wir unterstützen bestehende Kunden-Projekte mit Security Beratung.', 27, 1072, 27, NULL, 'ordinal', '2024-03-19 12:49:22.575428', 'Wir haben bei drei Projekten unsere Unterstützung angeboten.', 'Wir durften bei einem Projekt (potenziell unverrechenbar) mithelfen und haben dadurch eine Kundenreferenz.', 'Wir konnten bei zwei Projekten eine Referenz erhalten oder verrechenbar bei einem Projekt mithelfen.', 2);
INSERT INTO okr_pitc.key_result VALUES (1142, NULL, '2024-03-19 12:21:17.500831', 'Hinweis Target: darunter fällt auch, dass wir sagen, dass Joel und Pippo gar nicht verrechenbar sein müssen aufgrund meiner Vertretung. ', NULL, 'Wir haben einen Plan, wie wir uns längerfristig im Kernteam aufstellen wollen und entsprechende Massnahmen für die Umsetzung sind aufgegleist. ', 29, 1055, 29, NULL, 'ordinal', '2024-03-07 15:01:20.889002', 'Schwangerschaftsvertretung Nathalie (inkl. Unterstützung Pippo durch MarCom) sowie Verantwortlichkeit für den Innoprozess sind geklärt.  ', 'Langfristige Zusammenarbeit mit MarCom und Sales ist geklärt und allfälliger Wissenstransfer initiiert. Der Einsatz der verrechenbaren Pensen von jbl und ple für Q1 und Q2 GJ 24/25 ist geklärt. ', 'Wir haben Varianten definiert, wie wir uns ab Januar 2025 im Kernteam organisieren wollen. Dazu gehören die Aufteilung der verrechenbaren Pensen (PL Pensen) sowie mögliche neue Aufgaben von Nathalie. ', 4);
INSERT INTO okr_pitc.key_result VALUES (1193, NULL, '2024-03-21 10:06:14.336769', '', NULL, 'Wir kommunizieren die /mid-Journey intern und extern. ', 26, 1078, 57, NULL, 'ordinal', '2024-03-19 12:24:52.412284', 'Es wurden drei Massnahmen aus dem Action-Plan umgesetzt.', 'Es wurden alle Massnahmen aus dem Action-Plan umgesetzt.', 'Es wurde mindestens ein neuer Lead generiert.', 2);
INSERT INTO okr_pitc.key_result VALUES (1167, NULL, '2024-03-22 16:15:15.987482', '', NULL, 'Wir kennen unsere Bedürfnisse an "Digitale Lösungen"', 28, 1068, 28, NULL, 'ordinal', '2024-03-19 07:06:22.000913', 'Wir kennen unsere Bedürfnisse an Digitale Lösungen', 'Bedürfnisse sind mit Sales, Marcom, dev/* und ux abgestimmt', 'Neuer Lead aus Digitale Lösungen', 4);
INSERT INTO okr_pitc.key_result VALUES (1223, NULL, '2024-04-22 09:43:59.407865', '', NULL, 'Wir steigern unsere Qualitätsstandards und Effizienz', 36, 1084, 3, NULL, 'ordinal', '2024-03-19 13:18:37.434381', 'Knowledge Base Tool Frage ist geklärt', 'Knowledge Base PoC mit Grundstruktur ist erstellt', 'Members erfassen ihr Wissen ', 4);
INSERT INTO okr_pitc.key_result VALUES (1194, NULL, '2024-03-19 14:04:26.362443', '', NULL, 'Wir haben ein gemeinsames Verständnis der Arbeit im Team und legen 3 Fokusthemen fest
', 4, 1062, 4, NULL, 'ordinal', '2024-03-19 12:25:45.803587', 'Workshop, Verarbeitung «Retro 2024» ', 'Auslegeordnung vorhanden, 3 Fokusthemen definiert
', 'Massnahmen und Messpunkte zu den 3 Fokusthemen definiert', 1);
INSERT INTO okr_pitc.key_result VALUES (1180, 0, '2024-03-21 10:05:35.759224', 'Commit: 80%
Target: 90 %
Stretch: 100 %', 100, 'Alle Texte sind im Wordpress eingepflegt. ', 26, 1077, 49, 'PERCENT', 'metric', '2024-03-19 11:54:13.839631', NULL, NULL, NULL, 5);
INSERT INTO okr_pitc.key_result VALUES (1230, 800000, '2024-03-25 12:03:17.670101', '', 1200000, 'Wir haben einen guten Forecast für das Q1 2024/2025
', 40, 1083, 40, 'CHF', 'metric', '2024-03-19 13:16:22.381186', NULL, NULL, NULL, 3);
INSERT INTO okr_pitc.key_result VALUES (1136, 0, '2024-03-19 12:14:22.338744', '', 1, 'Durch angemessene Rekrutierungsbemühungen erreicht /mobility bis Ende Q4 GJ 23/24 ein Nettowachstum von 0.8 verrechenbaren FTE (exkl. Kafkastelle). ', 29, 1055, 20, 'FTE', 'metric', '2024-03-07 14:47:21.249126', NULL, NULL, NULL, 7);
INSERT INTO okr_pitc.key_result VALUES (1079, NULL, '2024-01-04 14:03:10.173581', 'Da im nächsten Quartal die MAG anstehen, hier das passende KR. Dabei sollen Massnahmen welche die Happiness fördern identifiziert, klassifiziert und umgesetzt werden
Ziel: Gemeinsam mit dem Members konkrete Massnahmen ableiten.', NULL, 'Massnahmen sind aus den MAGs identifiziert und implementiert', 28, 1036, 28, NULL, 'ordinal', '2023-12-19 14:19:30.079748', 'Alle MAG durchgeführt', 'Massnahmen Identifizierte', 'Erste Massnahmen sind bereits umgesetzt', 4);
INSERT INTO okr_pitc.key_result VALUES (1057, 0, NULL, 'Wir finden eine Lösung für unsere offene CSO Rolle (diverse Modelle denkbar).
', 1, 'Wir haben eine Lösung für die CSO Rolle.', 13, 1021, 16, 'NUMBER', 'metric', '2023-12-19 12:53:50.749925', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1029, 0, '2024-01-29 12:26:19.706362', 'Als Team sind wir an 4 Meetups/UserGroupMeetings/etc präsent', 8, 'aktives Networking', 32, 1011, 32, 'NUMBER', 'metric', '2023-12-18 09:48:54.294154', NULL, NULL, NULL, 2);
INSERT INTO okr_pitc.key_result VALUES (1013, NULL, '2023-12-19 13:34:10.900892', '- Wir haben unser Cloud-Angebot überprüft und überarbeitet.
- Wir haben zwei neue Cloud-Referenzen erarbeitet.
- Wir führen ein Accountplanning mit AWS durch.
- Wir organisieren einen ersten Cloud Lunch mit AWS oder GCP.
- Zwei Members haben eine neue Zertifizierung von AWS abgeschlossen.
- Zwei Members haben eine neue Zertifizierung von GCP abgeschlossen.', NULL, 'Wir bauen im Bereich Hyperscaler weiter Wissen auf, vernetzen uns und schliessen eine Partnerschaft ab.', 16, 1005, 16, NULL, 'ordinal', '2023-12-12 13:27:37.587364', '2 der unten stehenden Ziele sind erreicht.', '4 der unten stehenden Ziele sind erreicht.', '5 der unten stehenden Ziele sind erreicht und eine Partnerschaft mit AWS und/oder GCP abgeschlossen.', 1);
INSERT INTO okr_pitc.key_result VALUES (1058, NULL, '2023-12-19 14:18:20.190943', '', NULL, 'Wir nehmen an 6 UX relevanten Events teil und erweitern dadurch unser Netzwerk', 36, 1025, 36, NULL, 'ordinal', '2023-12-19 12:55:30.139152', 'Wir nehmen an 6 UX relevanten Events teil', 'Wir nehmen an 6 UX relevanten Events teil und generieren damit ein konkretes Angebot (Offering erstellt und versendet)', 'Wir nehmen an 6 UX relevanten Events teil und halten einen Talk und generieren ein konkretes Angebot (Offering erstellt und versendet)', 1);
INSERT INTO okr_pitc.key_result VALUES (1078, NULL, NULL, '', NULL, 'Kampagnen-Plan mit Isovalent steht', 40, 1029, 31, NULL, 'ordinal', '2023-12-19 14:18:47.075896', 'Wir haben uns mit Isovalent auf das Ziel der Kampagne geeinigt', 'Die Kampagne ist in einer Draft-Version vorhanden', 'Die Kampagne ist publiziert.', 0);
INSERT INTO okr_pitc.key_result VALUES (1104, 6, NULL, '', 10, 'Der Erlös aus dem Subscriptionverkauf beträgt 6 TEUR', 17, 1023, 17, 'CHF', 'metric', '2023-12-19 15:20:17.187077', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (188, 0, '2023-09-19 12:02:16.943177', 'Commit: Wir haben aus mind. 3 Divisions Feedback zu unseren Services abgeholt Target: Wir haben aus mind. 6 Divisions Feedback zu unseren Services abgeholt Stretch: Wir haben aus mind. 14 Divisions Feedback zu unseren Services abgeholt', 1, 'Sys Pain Pointing bei divisions abholen', 67, 94, 67, 'NUMBER', 'metric', '2023-09-19 12:02:16.943177', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1087, NULL, '2023-12-27 09:06:03.300394', 'Zählt auf Company KR Sales/Networking ein.', NULL, 'Wir sind als offene Ruby Bude schweizweit bekannt. ', 28, 1037, 28, NULL, 'ordinal', '2023-12-19 14:23:37.041367', 'Auslegeordnung ist gemacht, abgestimmt und Aktivitäten sind geplant', 'Action plan ist erfolgreich umgesetzt', 'Positive Resonanz welche zu einem Auftrag führt', 2);
INSERT INTO okr_pitc.key_result VALUES (1231, NULL, '2024-03-22 16:12:33.971223', 'Applikationen: socialweb, hitobito (ev pro Instanz), BAFU SAM, swissunihockey, decidim (ev pro Instanz), Brixel, PuzzleTime, Skills, Cryptopus
', NULL, 'Wir haben ein Application Lifecycle Management für unsere Applikationen', 28, 1066, 28, NULL, 'ordinal', '2024-03-19 14:16:25.933027', 'Erster Entwurf für ALM pro Applikation', 'ALM ist pro Applikation bekannt', 'Application Lifecycle Management ist mit allen Applikationen definiert und etabliert', 1);
INSERT INTO okr_pitc.key_result VALUES (1030, 37, '2024-01-03 14:52:28.879495', 'Unser Team ist ausgelastet zu:
40% Commit
45% Target
50% Stretch', 50, 'Unsere Members sind ausgelastet', 32, 1011, 32, 'PERCENT', 'metric', '2023-12-18 09:50:35.721474', NULL, NULL, NULL, 4);
INSERT INTO okr_pitc.key_result VALUES (1196, NULL, NULL, '', NULL, 'Guidelines für die Selbstorganisation sind erstellt', 4, 1062, 4, NULL, 'ordinal', '2024-03-19 12:29:06.193344', 'Workshop «Brainstorming Selbstorganisation 14.11.2023» verarbeitet
', 'Erstellen «Heatmap», Wo drückt uns der Schuh, wo gehen wir was an, Draft Guidelines vorhanden, Mit Manifest abgeglichen
', 'Guidelines sind vorhanden, im Team besprochen und dem GL-Coach vorgestellt
', 0);
INSERT INTO okr_pitc.key_result VALUES (1306, 0, NULL, 'Volumen abgeschlossener Deals mit Neukunden, für die wir bisher nicht gearbeitet haben.', 50000, 'New Business: Ein Neukunde', 33, 1109, 33, 'CHF', 'metric', '2024-06-17 23:44:05.106302', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1182, NULL, '2024-03-19 12:44:42.388359', '', NULL, 'Wir verabschieden ein Sponsoringkonzept, dass die Vielfältigkeit und Breite von Puzzle widerspiegelt und eine saubere Planung über das gesamte GJ sicherstellt.', 26, 1078, 26, NULL, 'ordinal', '2024-03-19 11:56:24.524661', 'Neues Vorgehen für Sponsorings ist definiert und am April-Monthly präsentiert.', 'Planungsmeeting mit DO und Sales hat stattgefunden.', 'Sponsoringplanung GJ 24/25 verabschiedet.', 1);
INSERT INTO okr_pitc.key_result VALUES (1206, NULL, NULL, '', NULL, 'Alle Cubies sind verrechenbar', 36, 1079, 36, NULL, 'ordinal', '2024-03-19 13:03:22.84727', 'Alle Cubies sind auf Projekte offeriert', 'Mayra leistet mind. 20 verrechenbare Stunden und Berti ist auf einem bestätigten Auftrag platziert', 'Zusätzlich zum Target Ziel leistet Berti verrechenbare Stunden', 0);
INSERT INTO okr_pitc.key_result VALUES (1137, 0.4, '2024-03-21 12:22:13.311461', 'Stretch Goal ist 9 "Daumen hoch" zu 1 "Daumen runter" mit neuen Feedback Prozess zu News (Target entspricht 0.75). Bei negativem Feedback wird aktiv beim Member nachgefragt, um Inputs zu holen.
', 0.9, 'Die Kommunikation zur /mobility Strategie ist Puzzle weit erfolgt und wird von den Members gestützt (gemessen an Ratio "Daumen hoch" / "Daumen runter")', 5, 1053, 5, 'NUMBER', 'metric', '2024-03-07 14:47:43.267556', NULL, NULL, NULL, 3);
INSERT INTO okr_pitc.key_result VALUES (1188, NULL, '2024-03-19 14:17:47.003644', 'Coop-KR mit /marcom', NULL, 'Wir kommunizieren die /mid Journey intern und extern', 40, 1080, 31, NULL, 'ordinal', '2024-03-19 12:07:53.421123', 'Es wurden drei Massnahmen aus dem Action-Plan umgesetzt', 'Es wurden alle Massnahmen aus dem Action-Plan umgesetzt', 'Es wurde mindestens ein neuer Lead generiert', 5);
INSERT INTO okr_pitc.key_result VALUES (1232, NULL, '2024-03-25 12:57:46.556444', 'Bedeutung von Ausgewogen:
- Keine Überplanung von Members
- Maximal zu 90% des Pensums geplant
- Uns ist klar, ob wir noch Kapa für zusätzliche Arbeit haben', NULL, 'Unsere Kundenvorhaben, interne Projekte, Events, Ausbildung, Ferienwünsche passen unter einen Hut.', 28, 1067, 28, NULL, 'ordinal', '2024-03-22 16:33:27.407701', 'Kundenvorhaben, interne Projekte, Events, Ausbildung, Ferienwünsche sind geplant', 'Wie haben eine ausgewogene Planung bis und mit Sommer', 'Planung geht bis Ende Jahr', 1);
INSERT INTO okr_pitc.key_result VALUES (1239, 4, '2024-04-29 14:21:39.676199', '', 6, 'Test', 7, 1085, 7, 'CHF', 'metric', '2024-04-25 05:33:22.649181', NULL, NULL, NULL, 3);
INSERT INTO okr_pitc.key_result VALUES (1184, NULL, '2024-05-16 06:38:13.198147', '', NULL, 'Wir setzen auf nachhaltige, lokale Give Aways bei unseren Eventauftritten. ', 26, 1078, 26, NULL, 'ordinal', '2024-03-19 11:57:43.952127', 'Wir erstellen ein Give Away-Konzept, das intern allen bekannt ist. ', 'Wir produzieren ein nachhaltiges Give Away für den Puzzle up! 2024.  ', 'Wir setzen das nachhaltige Give Away beim Puzzle up! interaktiv in Szene und motivieren die Teilnehmenden, sich mit der Nachhaltigkeit auseinander zu setzen.', 2);
INSERT INTO okr_pitc.key_result VALUES (1404, 3.8, '2024-09-17 13:14:07.489602', 'Stand 15.9. haben wir zum Start und sowie per Ende Q2 3.8 FTE auf der Bench (Membersliste): jbr (1.0), cg (0.9), jbo (1.0), ii (0.7), gb (0.2)  Die wollen wir auf 0 runter bringen (Target 1.1)', 0, 'Die Salesaktivitäten für /mobility führen zu neuen Aufträgen und erhöhen die kurzfristige Auslastung von /mobility ', 20, 1138, 20, 'FTE', 'metric', '2024-09-13 07:09:40.193087', NULL, NULL, NULL, 3);
INSERT INTO okr_pitc.key_result VALUES (1197, 0, NULL, '', 1, 'Wir führen einen Team-Event durch
', 4, 1062, 4, 'NUMBER', 'metric', '2024-03-19 12:29:47.520198', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1198, NULL, NULL, '', NULL, 'Wir dokumentieren unsere Arbeit so, dass Tasks bei Bedarf übergeben werden können.', 27, 1073, 27, NULL, 'ordinal', '2024-03-19 12:30:29.823536', 'Alle Tasks sind als Issues erfasst und beschrieben.', 'Es gibt wöchentliche Updates zu den Tasks.', 'Die Updates sind so formuliert, dass ein anderes Teammitglied übernehmen kann.', 0);
INSERT INTO okr_pitc.key_result VALUES (1061, NULL, NULL, '', NULL, 'Wir entwickeln ein neues Eventformat für WAC', 36, 1025, 36, NULL, 'ordinal', '2023-12-19 13:00:46.523138', 'Konzept für neuen Event steht und ist mit Marcom und GL Buddy besprochen', 'Konzept steht, Termin ist fixiert, Einladungen verschickt', 'Erstdurchführung des Events ist erfolgreich durchgeführt', 0);
INSERT INTO okr_pitc.key_result VALUES (1095, 0, NULL, 'Drei Referenzberichte verfassen (0.3), approven lassen (0.3) und publizieren (0.3).', 3, 'MO Cloud: Referenzstories publizieren', 33, 1038, 33, 'NUMBER', 'metric', '2023-12-19 14:29:40.32705', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1207, NULL, '2024-03-19 13:08:07.249113', 'Es werden nur Zertifizierungen gezählt, welche den Partnerstatus unterstützen.', NULL, 'Weitere Members von /mid erreichen eine Cloud-Zertifizierung', 40, 1080, 40, NULL, 'ordinal', '2024-03-19 13:05:46.580143', '+1', '+2', '+5', 1);
INSERT INTO okr_pitc.key_result VALUES (215, 0, '2023-09-25 08:25:39.969537', '', 250470, 'Aufträge 2024 Q1/ Prognostizierter Umsatz (Forecast des geplanten DL-Umsatzes 2024)', 36, 103, 36, 'CHF', 'metric', '2023-09-25 08:25:39.969537', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1093, 800000, '2023-12-20 08:46:37.130938', '', 1200000, 'Rekord-Forecast für Q4Y2023-24', 40, 1035, 60, 'CHF', 'metric', '2023-12-19 14:27:48.50192', NULL, NULL, NULL, 4);
INSERT INTO okr_pitc.key_result VALUES (1089, NULL, '2023-12-27 09:29:58.004069', 'Betroffen sind die Projekte von Micha, Marc B. und Livia', NULL, 'PL Nachfolgelösungen vorhanden', 28, 1037, 28, NULL, 'ordinal', '2023-12-19 14:24:06.225925', 'Folgeplanung von ist vorhanden', 'Planung umgesetzt, Kunde abgeholt', 'Projekte laufen nachhaltig und stabil. Keine vorübergehende Lösung.', 2);
INSERT INTO okr_pitc.key_result VALUES (1080, NULL, '2023-12-27 09:39:42.682707', 'Ziel: OKR als Mittel für ganz Ruby
Vernehmlassung des Drafts im Teammeeting, Engagement-Möglichkeit schaffen für Interessierte, Rückmeldung nach Abschluss, ....', NULL, 'Ruby Members sind mit OKR engagiert', 28, 1036, 28, NULL, 'ordinal', '2023-12-19 14:20:25.452358', 'Ablauf des ganzen OKR-Cycles mit Team-Engagement-Möglichkeiten definieren. ', 'Ablauf erfolgreich implementiert. ', 'Ein pasendes Keyresult wird von Member eingebracht. ', 3);
INSERT INTO okr_pitc.key_result VALUES (1031, 4, '2024-01-24 16:16:49.440139', 'Aktuell sind drei auf der Membersliste (Martin, Mirjam, Janik). Für Jonas ist zusätzlich ab März noch Auslastungslücke.
Alignement Company KR Membersliste', 1, 'Die kurzfristige Auslastung laufend erhöht, sodass per Ende Quartal (d.h. für den Folgemonat April) höchstens noch ein 1 /mobility Member auf der Membersliste ist.', 5, 1009, 20, 'NUMBER', 'metric', '2023-12-18 10:12:35.776759', NULL, NULL, NULL, 6);
INSERT INTO okr_pitc.key_result VALUES (1183, NULL, '2024-05-02 06:59:52.203704', '', NULL, 'Wir etablieren das Sponsoringmodell für den Puzzle up!.', 26, 1078, 55, NULL, 'ordinal', '2024-03-19 11:57:13.313459', 'Wir haben eine Zusage für ein Sponsoringpackage.', 'Wir haben zwei Zusagen für ein Sponsoringpackage. ', 'Wir haben drei Zusagen für ein Sponsoringpackage.', 2);
INSERT INTO okr_pitc.key_result VALUES (1224, NULL, NULL, '', NULL, 'Wir klären Sponsoring-Aktivitäten /dev/tre
', 4, 1063, 4, NULL, 'ordinal', '2024-03-19 13:23:26.253225', 'Ideen gesammelt, kategorisiert und eingeordet - Ideen im Team besprochen (Weeklys)
', 'Liste der Sponsoring-Aktivitäten finalisiert. Abgleich mit /ux, /mobiity und /ruby – Bereichtsübergreifende Zusammenfassung
', 'Kommunikation an LST und Markom
', 0);
INSERT INTO okr_pitc.key_result VALUES (1155, NULL, '2024-03-19 13:22:17.347132', 'im Rahmen unserer Strategie bezüglich Hitobito haben wir immer noch einen entscheidenden schwarzen Fleck, die Qualität der Software. Hier sind einige Indizien:

    Die Wartungsaufwände sind deutlich höher als das Budget vorsieht.
    Es gibt viele Bugs
    Die Nacharbeitsaufwände bei Releasen sind hoch und werden meist auf eigene Kosten durchgeführt.
    Entwicklungen sind tendenziell aufwändig und werden oft von Kunden hinterfragt.
    Schätzungen weichen häufig stark von den tatsächlich geleisteten Stunden pro Ticket ab, was schwer zu verstehen ist und auch Kunden im Nachhinein schwer zu verrechnen ist.
    Die Software ist bereits 10 Jahre alt.
    Die Architektur und die Wartungsstruktur sind sehr komplex. -> evtl. Grund für die hohen Kosten
    Performance Probleme zb bei Exporten
    Wir sind stark von zwei Entwicklern abhängig, die Hitobito und die Umgebung mehr oder weniger kennen.
', NULL, 'Wir steigern die Qualität von Hitobito', 41, 1059, 41, NULL, 'ordinal', '2024-03-15 09:46:20.41382', 'Kick off Techbaord und PL durchgeführt ', 'Analyse druch Techboard ist durchgeführt ', 'Erste Masnsahmen sind eingeleitet ', 5);
INSERT INTO okr_pitc.key_result VALUES (1209, NULL, '2024-03-19 13:49:15.656512', 'Backlog ist ready für Auslastungslücken zu füllen', NULL, 'OV & Rohrmax Backlogs sind bereinigt', 60, 1081, 40, NULL, 'ordinal', '2024-03-19 13:08:05.287415', 'Backlogs wurden aufgeräumt', 'Alle Tickets sind priorisiert, so dass sich die entsprechenden Members jeder Zeit ein Ticket schnappen können', 'Alle Tickets sind ready für Action
(Kategorie, Definition of done, Aufwandschätzung)', 1);
INSERT INTO okr_pitc.key_result VALUES (1234, NULL, '2024-03-25 11:48:39.045487', 'Wir wollen entweder eine Praktikumsstelle oder eine Junior-Position besetzen und bis zum Quartalsende deren Auslastung und Rentabilität sicherstellen.', NULL, 'Praktikum oder Junior Position besetzt, Betreuungsstrukturen geklärt und kostendeckender Einsatz gesichert.', 33, 1075, 33, NULL, 'ordinal', '2024-03-25 11:47:27.808255', 'Praktikums- oder Juniorstelle besetzt (Vertrag unterzeichnet) und Betreuungsstrukturen geklärt', 'Commit plus Auslastung des neuen Members für GJ25/Q1 zu 75% definitiv', 'Target plus eine Verrechenbarkeit für GJ25/Q1 sichergestellt, welche die Kosten (Lohn, Betreuung, Onboarding) übersteigt (Kalkulation in den Check-ins ausgeführt)', 1);
INSERT INTO okr_pitc.key_result VALUES (1216, NULL, '2024-03-19 13:58:01.447212', 'Wir stellen eine*n neuen Member ein', NULL, '/mid wächst', 40, 1083, 40, NULL, 'ordinal', '2024-03-19 13:14:15.54267', 'Stelle ausgeschrieben', 'Mindestens 1 Person auf der Shortlist', 'Unterschrift', 1);
INSERT INTO okr_pitc.key_result VALUES (1168, 676161, '2024-03-19 14:57:32.204315', 'Umsatz /ruby + /hitobito für Q1 und Q2
Total im 1.7.2023 bis 31.12.2023:
Hitobito: CHF 451229.- Ruby: CHF 493631.- Total: 944860

Messung nach https://time.puzzle.ch/reports/revenue von 01.07.24 bis 31.12.24
Initialwert:
Hitobito: CHF 293074.- Ruby: CHF 383087.- Total: 676161

Target ist 1 Mio', 1400000, 'Dank randvollen Auftragsbüchern geniessen wir unsere Sommer-, Herbst- und Weihnachtsferien.', 28, 1068, 28, 'CHF', 'metric', '2024-03-19 07:15:48.258141', NULL, NULL, NULL, 3);
INSERT INTO okr_pitc.key_result VALUES (1140, NULL, '2024-05-02 15:44:26.824731', 'https://codimd.puzzle.ch/GoNewTech
Zur Zeit ist bereits Martin Käser auf GO mit CI/CD im Einsatz.', NULL, 'Weitere /mobility Members sind auf GO im Einsatz.', 20, 1054, 5, NULL, 'ordinal', '2024-03-07 14:54:19.127344', 'Angebotsstory in Kooperation mit CI/CD erarbeitet.', 'Mind. 1 zusätzlicher Member auf GO Auftrag fix eingeplant (Auftrag/Ressource ist  vom Kunden bestellt)', 'Mind. 1 neuer Go Auftrag gewonnen.', 8);
INSERT INTO okr_pitc.key_result VALUES (1212, 0, '2024-05-13 14:00:31.302925', 'Messung Summe der Besuche gemäss Matomo im Q4 (1. April - Bis 30. Juni)', 2000, 'Wir erhöhen den Traffic in diesem Quartal auf der WAC Website auf 2000 Besuche', 36, 1082, 3, 'NUMBER', 'metric', '2024-03-19 13:10:29.866985', NULL, NULL, NULL, 5);
INSERT INTO okr_pitc.key_result VALUES (1032, 1140000, '2023-12-19 12:58:34.966483', 'Forecast Wert des geplanten Dienstleistungsumsatzes aus PTime für das Q4 2023/24 (Pauschalen, Partnerumsätze, Subs sind nicht enthalten). Metrisch: Commit: MCHF 2.5 Target: MCHF 3.3 Stretch: MCHF 3.8 Stichdatum: 19.03.24', 3800000, 'Unsere Auftragsbücher für das Q4 GJ2023/24 sind gut gefüllt', 13, 1004, 13, 'CHF', 'metric', '2023-12-18 11:55:22.480674', NULL, NULL, NULL, 2);
INSERT INTO okr_pitc.key_result VALUES (1082, NULL, NULL, '', NULL, 'Die /dev/tre-Members werden prioritär mit bestehenden Aufträgen ausgelastet.', 4, 1014, 4, NULL, 'ordinal', '2023-12-19 14:22:14.958101', 'Die Arbeit an bestehenden Wartungsverträgen konnte erfolgreich für die Umsetzung im Q1 intensiviert werden', 'Clara ist erfolgreich bei Acrevis onboardet und voll verrechenbar. Die Nachfolge von Marc in Swisscom ist onboarded.', 'Die notwendigen Neuerungen zusätzlich zum Wartungsvertrag mit Swiss Olmypics konnten bis Ende März realisiert werden.', 0);
INSERT INTO okr_pitc.key_result VALUES (54, NULL, '2023-06-20 12:19:44.56477', 'Commit: 170CHF/h
Target: 175CHF/h
Stretch: 180CHF/h', 170, 'Stundensatz goes brrrrr', 34, 45, 34, 'CHF', 'metric', '2023-06-20 12:19:44.56477', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (80, 0, '2023-06-20 13:22:15.926697', 'Zwei Leute, die ihr Pensum reduzieren (Tiago, Rémy). Mind. eine neue Person, die startet. Mehrere betroffene Kunden.
// Commit: Kundenbeziehungen können fortgesetzt werden
// Target: Pensumsreduktionen können bei allen betroffenen Kunden kompensiert werden // Stretch: Bei betroffenen Kunden ist zusätzliches Business möglich (duch zusätzliche Kapa nach Anstellung)', 1, 'Kundenbeziehungen und bestehendes Business erhalten', 33, 51, 33, 'NUMBER', 'metric', '2023-06-20 13:22:15.926697', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (96, 0, '2023-08-03 12:37:39.006376', 'Commit:
- Inserat überarbeitet und publiziert
Target:
 - mindestens 3 Gespräche durchgeführt
Stretch:
- 1 Vertrag unterschrieben', 1, 'Wir schliessen einen neuen Arbeitsvertrag mit einer verrechenbaren Person ab 60-80% *', 4, 56, 4, 'NUMBER', 'metric', '2023-08-03 12:37:39.006376', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (91, 0, '2023-06-20 13:38:48.230526', '	* Commit (0.3): valibler Bewerbung auf die Stelle
	* Target (0.7): Gestelltes Angebot
	* Stretch (1.0): Unterschriebener Vertrag
	* Stichtag: 15.9. ', 1, 'Wir können eine Anstellung tätigen', 28, 62, 28, 'NUMBER', 'metric', '2023-06-20 13:38:48.230526', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (89, 0, '2023-06-25 09:25:11.244925', 'Commit:
- Dienstleistungsangebot erstellt und im Wiki notiert
- “Wie wir arbeiten" erstellt und im Wiki notiert

Target:
- Blog-Artikel zu Moser-Baer publiziert (Output)
- Outcome!! Wie angekommen, wie messen?

Stretch:
- Eine Anfrage
', 1, 'Es ist bekannt, dass devtre für Individual-Software-Entwicklung steht', 4, 59, 4, 'NUMBER', 'metric', '2023-06-25 09:25:11.244925', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (79, 0, '2023-08-21 13:07:56.14477', '0.3 (Commit Zone): Wir haben unseren neuen OpenShift Staging Cluster aufgebaut;
0.7 (Target Zone): + Wir setzen einen Cilium PoC um;
1.0 (Stretch Goal): + Wir schreiben einen Blogpost über unsere Learnings;', 1, 'Cilium PoC auf interner Infrastruktur umgesetzt', 40, 57, 40, 'NUMBER', 'metric', '2023-08-21 13:07:56.14477', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (94, 0, '2023-06-25 09:31:56.561616', 'Commit:
- Wir kennen den Auftragsplan bis Ende 2023 und haben eine Vision für Q1, 2024
- Wir organisieren ein Coaching für den BL

Target:
- Wir klären die Frage mit welchem Instrument wir uns organisieren «Gurkensalat-Board"

Stretch:
- Report "Get things done" aus Gurkensalat als Beweis, dass Get Things Done besser geworden  ist (aber wie reporten??)

', 1, 'Wir leben die Selbstorganisation bewusster hinsichtlich Prioritäten und Terminen und get things done', 4, 61, 4, 'NUMBER', 'metric', '2023-06-25 09:31:56.561616', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (47, 0, '2023-06-20 09:05:48.287785', 'Commit (0.3): Gemeinsames internes Verständnis was ein "Agiles Team" ist wurde geschafft, Sales Kampagne gestartet; Target (0.7): Sales Kampagne gestartet und zwei Leads vorhanden; Stretch (1.0): Sales Kampagne gestartet und ein Team gewonnen. Stichdatum 15.9.
', 1, 'Agile Teams: Die Sales Kampagne ist gestartet und wir haben einen Auftrag für ein weiteres agiles Team gewonnen ', 13, 44, 13, 'NUMBER', 'metric', '2023-06-20 09:05:48.287785', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (109, 1345, '2023-06-26 07:53:25.953862', '', 1371, 'Wir erhöhen die Anzahl Newsletter Abonennt*innen um 2%', 26, 66, 26, 'NUMBER', 'metric', '2023-06-26 07:53:25.953862', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (68, 0, '2023-06-20 12:42:41.208914', 'Ordinales Ziel:
- Commit: Happinessumfrage ist etabliert
- Target: Zielwerte für die Happinessumfrage sind definiert
- Stretch: Für jedes Tactical ist die Happinessumfrage ausgefüllt und diskutiert', 1, 'Wir steigern die WAC Memberszufriedenheit', 36, 53, 36, 'NUMBER', 'metric', '2023-06-20 12:42:41.208914', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (44, NULL, '2023-07-22 05:10:55.954769', 'Commit (0.3): 1 Monat über 53%;  Target 0.7: 2 Monate über 53%; Stretch (1.0): 3 Monate über 53%. Stichtag 31.8 (Monatsabschlüsse Juni, Juli, August)
', 53, 'Die absolute Verrechenbarkeit liegt in jedem Monat über 53%', 22, 43, 22, 'PERCENT', 'metric', '2023-07-22 05:10:55.954769', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (36, 0, '2023-08-31 11:50:11.584735', '0.3 (Commit Zone): Monatliche Happiness Umfrage ausgewertet für die Baseline Bestimmung; 0.7 (Target Zone): Baseline und Zielwert Bestimmung + Massnahmenpaket aus letztjähriger Mitarbeiterbefragung für Mobility definiert; 1.0 (Stretch Goal): Baseline + Massnahmenpaket + 10% höhere Zufriedenheit als bei letztjähriger Mitarbeiterbefragung
', 1, 'Die Happiness Baseline, Massnahmen und allfälliger Zielwert für die folgenden Quartale sind definiert', 5, 40, 5, 'NUMBER', 'metric', '2023-08-31 11:50:11.584735', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (43, 0, '2023-06-14 09:31:27.101145', 'Ziewert von 4.5 bezieht sich auf FTE. Stichtag: 15.9.', 4.5, 'Wir erhöhen die verrechenbaren Pensen um 5% (4.5 FTE)', 13, 43, 13, 'NUMBER', 'metric', '2023-06-14 09:31:27.101145', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (74, NULL, '2023-06-25 09:15:44.152678', 'Überlegungen:
- Ambitiöses (stretch) Jahresziel ist 75%.
- Durchschnitt der letzten 5 Jahre (GJ 2018/19 bis GJ 2022/23): 68%
- Höchstwert der letzten 5 Jahre GJ 2018/19): 74.4%
- Durchschnitt GJ 2022/23 (Aug-Jun): 73.5%
- Durschnitt März bis April 2023: 71.4%

Für Q1, GJ 2023/24 sind viele Absenzen, Member-Gespräche, Onboardings, Offerings, Workshops und Rekrutierung geplant. Die 73% im Q1 GJ 2023/24 zu halten ist also schon ein Mega-Stretch.', 1, 'Die absolute Verrechenbarkeit ist in jedem Monat mindestens 73%', 4, 56, 4, 'PERCENT', 'metric', '2023-06-25 09:15:44.152678', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (87, 0, '2023-06-25 09:23:54.601536', 'Commit:
- Techmap devtre aktualisiert- Liste Technologie pro Auftrag aktualisiert und verabschiedet
- Techradar devtre erstellt

Target:
- Gewichtung FE, BE & CI/CD visualisiert
- Ermittelt mit welcher Technologie wir auf die Marktopportunität «New Tech» einzahlen wollen

Stretch:
- Statement geschrieben
- Mit GL-Coach & CTO verabschiedet
 ', 1, 'Wir kennen unsere technologiesche Ausrichtung', 4, 59, 4, 'NUMBER', 'metric', '2023-06-25 09:23:54.601536', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (53, NULL, '2023-06-26 11:32:16.887107', 'Commit: 35%
Target: 40%
Stretch: 45%', 35, 'Absolute Verrechenbarkeit', 34, 45, 34, 'PERCENT', 'metric', '2023-06-26 11:32:16.887107', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (51, 0, '2023-07-11 03:49:27.256405', 'Stichdatum 15.9.', 104700, 'Subscriptions: Handelsgeschäft-Gewinn verdreifacht zum Vorjahresquartal', 16, 44, 16, 'CHF', 'metric', '2023-07-11 03:49:27.256405', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (126, 0, '2023-08-24 07:57:53.27343', 'Commit (0.3): Termin ist agestimmt mit Community; Target (0.7): Themen sind bereits klar (1.0): Einladungen sind versendet; Stichtag 20.9. ', 1, 'Es nehmen min. 15 Teilnehmer an Community teil', 65, 72, 65, 'NUMBER', 'metric', '2023-08-24 07:57:53.27343', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (223, 9, '2023-10-16 17:27:57.281887', '', 20, 'Wir erhalten 20 qualifizierte Bewerbungen für technische Bereiche', 84, 110, 84, 'PERCENT', 'metric', '2023-10-16 17:27:57.281887', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1062, NULL, '2023-12-19 14:19:03.127739', '', NULL, 'Members von /mid sind Cloud-zertifiziert', 31, 1026, 40, NULL, 'ordinal', '2023-12-19 13:08:14.463524', 'Mindestens 3 Members haben sich dazu verpflichtet, bis im Sommer eine Cloud-Zertifizierung durchzuführen.', 'Mindestens 2 Members haben eine Cloud-Zertifizierung erreicht.', 'Mindestens 5 Members haben eine Cloud-Zertifizierung erreicht', 2);
INSERT INTO okr_pitc.key_result VALUES (86, 0, '2023-06-25 09:30:14.721095', 'Commit:
- Etabilieren eines regelmässigen (alle 3 Wochen) devtre-Biers
- Dabei Abwechslung reingebracht
- Roadmap der Gefässe erstellt und auf übergelagerte Meetings abgestimmt (SUM, LST-WS, OKR-Meetings, …)

Target:
- 3 Teammeetings durchgeführt (bisher)
- Erstmaliger Quartals-Teamworkshop durchgeführt

Stretch:
- Retro/ Umfrage durchgeführt und Verbesserungsmassnahmen definiert', 1, 'Wir bauen die regelmässigen devtre Team-Gefässe aus und steigern deren inhaltliche Qualiät', 4, 61, 4, 'NUMBER', 'metric', '2023-06-25 09:30:14.721095', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (60, 0, '2023-06-20 12:24:21.602948', 'Commit: Das Onboarding der aktuellen Kundenpipeline ist abgeschlossen (ITpoint, BFH)
Target: /sys erzielt Umsatz mit den Ops Kunden
Stretch: Weitere Ops Kunden in der Pipeline oder bereits vertraglich gebunden', 1, 'Der Operations Zug bei /sys beginnt zu rollen', 34, 50, 34, 'NUMBER', 'metric', '2023-06-20 12:24:21.602948', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (61, 0, '2023-06-20 12:25:07.143951', 'Commit: Ein Draft des Ops Angebots von /sys und Puzzle liegt vor
Target: Das Operations Angebot ist finalisiert und kommuniziert
Stretch: Es melden sich bereits erste potentielle Kunden', 1, 'Definition des Operations Angebots von /sys (und von Puzzle)', 34, 50, 34, 'NUMBER', 'metric', '2023-06-20 12:25:07.143951', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (62, 0, '2023-06-20 12:27:47.116608', 'Commit: Es findet ein regelmässiger Austausch von allen beteiligten Bereichen statt
Target: In den Hyperscaler-Projekten findet der interdisziplinäre Austausch zwischen Devs, Ops und Engineering statt
Stretch: Es gibt eine aktive und motivierte Gruppe an Engineers für die Hyperscaler Thematik bei Puzzle', 1, 'Das Alignment in der Hyperscaler Thematik zwischen allen Bereichen bei Puzzle steigt signifikant', 34, 52, 34, 'NUMBER', 'metric', '2023-06-20 12:27:47.116608', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (63, 0, '2023-06-20 12:35:58.862664', 'Commit: Das /sys Team ist aktiv involviert in der Hyperscaler Thematik (organisatorisch wie technisch)
Target: /sys leistet einen aktiven Beitrag in Hyperscaler Projekten
Stretch: /sys nimmt eine führende Rolle ein beim Pushen der Hyperscaler Marktopportunität', 1, 'Hyperscaler @/sys', 34, 52, 34, 'NUMBER', 'metric', '2023-06-20 12:35:58.862664', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (65, 0, '2023-06-20 12:39:56.273089', 'Ordinales Ziel:
- Commit: Wir haben die WAC Strategie mit Stakeholdern diskutiert
- Target: die WAC Strategie ist finalisiert
- Stretch: Die WAC Strategie ist publiziert', 1, 'Wir finalisieren die Divisionsstrategie von We Are Cube', 3, 55, 3, 'NUMBER', 'metric', '2023-06-20 12:39:56.273089', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (58, 0, '2023-06-20 12:46:14.198447', '0.3 (Commit Zone): Die Technologien für MLOps Beratung sind definiert.
0.7 (Target Zone): Drei Angebote (inkl. MLOps Tech-Lab) sind indentifiziert und als Angebot beschrieben. Davon ist ein Angebot mit 3 Kunden validiert.
1.0 (Strech Goal): Ein Kunde bestellt Dienstleistungen im Thema Data-Analytics.', 1, '3 Angebotsideen sind durch Einbezug der Members entworfen und können validiert werden.', 30, 47, 30, 'NUMBER', 'metric', '2023-06-20 12:46:14.198447', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (73, 0, '2023-06-20 12:53:02.495619', 'Ordinales Ziel
- Commit: 1 Neukunde
- Target: 2 (wovon 1 in Gesundheitsbereich)
- Stretch: 3 (wovon 1 im Gesundheitsbereich)', 1, 'WAC gewinnt zwei Neukunden (Ziel: Vertrag. Projekt muss noch nicht starten oder durchgeführt sein)', 36, 54, 36, 'NUMBER', 'metric', '2023-06-20 12:53:02.495619', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (70, 0, '2023-06-20 13:24:12.689433', 'Wir kommen mit aktiver Akquise an neue Aufträge und Kundenkontakte.
// Commit: 10 qualifizierte Leads, mind. 5 davon mit Neukunden
// Target: Akquise von zwei Mandaten oder einem Projekt
// Stretch: Akquise eines grösseren Dev-Projekts (>200k), das die /zh-Devs substantiell auslastet', 1, 'Akqui-Sprint trägt Früchte', 33, 49, 33, 'NUMBER', 'metric', '2023-06-20 13:24:12.689433', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (37, 0, '2023-06-20 13:25:35.616212', '0.3 (Commit Zone): Zwei Erfolgsstories sind auf Linkedin gepostet. Die Erfahrung aus anderen Teams ist in unser Konzept eingeflossen. Die Meinung von 3 Kontakten (RTEs) zu unserem Konzept ist uns bekannt.  0.7 (Target Zone):  Die Meinung eines weiteren /mobility Kontakt ist bekannt;  1.0 (Strech Goal: die Meinung aller RTEs sowie aller /mobility Kunden ist uns bekannt. ', 1, 'Wir haben ein Konzept wie wir Members als Team im PV oder Projekt zum Einsatz bringen', 29, 40, 29, 'NUMBER', 'metric', '2023-06-20 13:25:35.616212', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (69, 0, '2023-06-20 13:26:09.244617', 'Ordinales Ziel:
- Commit: DesignOps Workshop durchgeführt und Pain Points Members aufgenommen
- Target: konkrete Massnahme im Team besprochen und festgehalten
- Stretch: Zusätzlich zweite  Massnahmen besprochen und festgehalten', 1, 'Wir legen den Grundstein für die weitere Entwicklung und Professionalisierung von WAC', 3, 55, 3, 'NUMBER', 'metric', '2023-06-20 13:26:09.244617', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (92, 0, '2023-08-26 13:37:28.708989', 'Commit:
- racoon: Dani, Mätthu integriert

Target:
- Integrationsplan für Clara erstellt
- Integrationsplan für Simon erstellt

Stretch:
- Plan was Chrigu Lüthi ab Oktober: Aufträge, Technologien, Arbeitsweise
- Plan für Beri ab Oktober: Volumen, Aufträge, Entwicklung

', 1, 'Wir haben eine Vision für die neuen/ zurückkehrenden Members und integrieren diese', 4, 61, 4, 'NUMBER', 'metric', '2023-08-26 13:37:28.708989', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (85, 0, '2023-06-20 13:31:07.956171', '0.3 (Commit Zone): Wir identifizieren im Team Aspekte der Kommunikation, die als verbesserungswürdig wahrgenommen werden;
0.7 (Target Zone): + Wir definieren & implementieren Massnahmen zur Verbesserung der Kommunikation;
1.0 (Stretch Goal): + Wir evaluieren den Massnahmeneffekt und ziehen Schlüsse daraus.', 1, 'Massnahmen zur Verbesserung der wahrgenommenen Kommunikation sind umgesetzt', 31, 60, 31, 'NUMBER', 'metric', '2023-06-20 13:31:07.956171', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (83, 0, '2023-06-20 13:33:54.643812', 'Effiziente Einarbeitung Junior (Yelan) + Auftrag finden, welcher Tandem mit Junior ermöglicht // Commit: Junior kann 50% verrechenbar eingesetzt werden // Target: 75% Verrechenbarkeit // Stretch: Target + Auftrag mit Junior/Senior Pairing ermöglicht', 1, 'Pairing-Konstellation für Junior ermöglichen', 33, 49, 33, 'NUMBER', 'metric', '2023-06-20 13:33:54.643812', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (97, 0, '2023-06-20 13:37:38.190591', '	* Commit (0.3): Happiness Umfrage durchgeführt
	* Target (0.7): Zielwerte für nächstes Quartal sind definiert
	* Stretch (1.0): Erste Massnahmen abgeleitet.
	* Stichtag: 15.9. ', 1, 'Happiness Umfrage ist bei Ruby eingeführt, ausgewertet und Massnahmen definiert und löst die bestehende Memberumfrage ab.', 28, 62, 28, 'NUMBER', 'metric', '2023-06-20 13:37:38.190591', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (107, NULL, '2023-06-26 07:49:09.175261', '1=implementiert', 1, 'Wir implementieren das neue Layout passend zur neuen Startseite von puzzle.ch', 26, 66, 26, 'NUMBER', 'metric', '2023-06-26 07:49:09.175261', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (117, 0, '2023-06-27 10:53:54.522734', '', 1, 'Wir erstellen ein Kanban-Board, das die Kommunikationsaktivitäten der Divisions für alle sichtbar macht ', 26, 69, 26, 'NUMBER', 'metric', '2023-06-27 10:53:54.522734', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (150, 0, '2023-12-08 14:17:56.284723', 'Commit (0.3): 1 Monat über 92%; Target 0.7: 2 Monate über 92%; Stretch (1.0): 3 Monate über 92%. Stichtag 31.8 (Monatsabschlüsse Sep, Okt, November)

Messbarkeit: Durchschnittliche Verrechenbarkeit der laufenden Kundenauftrage (exkl. BWS) ', 92, 'Unsere Aufträge sind mit einer Verrechenbarkeit > 92% rentabel ', 41, 80, 41, 'PERCENT', 'metric', '2023-12-08 14:17:56.284723', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1307, NULL, NULL, 'Gemessen wird die durchschnittliche Auslastung pro Monat.', NULL, 'System Engineer angestellt und ausgelastet', 33, 1110, 33, NULL, 'ordinal', '2024-06-17 23:47:03.817687', 'Angestellt', 'Angestellt + 33% Auslastung im Q1', 'Angestellt + 66% Auslastung im Q1', 0);
INSERT INTO okr_pitc.key_result VALUES (145, 0, '2023-09-12 12:38:12.700258', 'Commit: Erarbeitung des Inhaltskonzepts für Puzzle.Docs bis Ende Q2.
Target: Erarbeitung und Freigabe des Inhaltskonzepts für Puzzle.Docs bis Ende Q2.
Stretch: Erarbeitung und Freigabe des Inhaltskonzepts für Puzzle.Docs bis Mitte Q2 und Planung der Wiki-Ablösung bis zum Ende Q3.', 1, 'Erarbeitung eines Inhaltskonzepts für Puzzle.Docs und Planung der Wiki-Ablösung. ', 26, 78, 26, 'NUMBER', 'metric', '2023-09-12 12:38:12.700258', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (142, 0, '2023-10-05 13:41:42.290074', 'Forecast Wert des geplanten Dienstleistungsumsatzes aus PTime für das Q1 2024 (Pauschalen, Partnerumsätze, Subs sind nicht enthalten). Commit: MCHF 2.6 Target: MCHF 3.4 Stretch: MCHF 4.0
Stichdatum: 19.12.23', 4000000, 'Unsere Auftragsbücher für das Q1 2024 sind gut gefüllt', 16, 75, 16, 'CHF', 'metric', '2023-10-05 13:41:42.290074', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (141, 0, '2023-09-15 13:55:22.976579', 'Wir nutzen nochmals dasselbe KR wie im GJQ1 diesmal mit Faktor 2 aufgrund des sehr grossen Centris Deals vor einem Jahr. Für das GJQ3 nehmen wir uns ein neues KR vor, welches die neuen Partnerschaften CIQ und Gitlab abbilden soll.', 273636, 'Subscriptions: Wir verdoppeln Handelsgeschäft-Gewinn im Vergleich zum Vorjahresquartal', 16, 76, 16, 'CHF', 'metric', '2023-09-15 13:55:22.976579', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (168, 0, '2023-09-18 08:25:01.911437', 'Commit Zone: Gespräche mit RTEs sind geführt und Wunsch nach Verlängerung der Mandate ist platziert. Target Zone: 100% der Mandate können bis Ende Jahr verlängert werden oder alternative Einsatzmöglichkeiten sind gefunden. Stretch Goal: Für einen weiteren Member konnte ein Einsatz bei der SBB vereinbart werden. ', 1, 'SBB Bestellungen innerhalb des Bahnproduktionsvertrages werden verlängert', 29, 86, 29, 'NUMBER', 'metric', '2023-09-18 08:25:01.911437', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (169, 0, '2023-09-18 08:35:53.117186', 'Commit: Der Mehrwert einer /mobility weiten Supportorganisation ist geprüft; Target: Die Supportorganisation ist konzipiert; Stretch: Die Supportorganisation ist eingeführt.', 1, 'Eine /mobility weite Supportorganisation ist konzipiert und eingeführt', 5, 83, 5, 'NUMBER', 'metric', '2023-09-18 08:35:53.117186', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (186, 0, '2023-09-18 12:40:09.755837', 'Anzahl sinnvolle Massnahmen ', 3, 'Wir definieren Massnahmen aus der Happiness Umfrage und setzten sie um.', 28, 93, 28, 'NUMBER', 'metric', '2023-09-18 12:40:09.755837', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (110, 0, '2023-08-25 07:30:51.004594', '', 2, 'Wir finalisieren und publizieren unser Videokonzept', 26, 68, 26, 'NUMBER', 'metric', '2023-08-25 07:30:51.004594', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (175, 0, '2023-09-19 12:25:45.308888', 'Commit: Wir wollen aus Leads in Zusammenhang mit der Partnerschaft mit CiQ Geld machen
Target: Wir verdienen 25k mit Leads aus dieser Partnerschaft
Stretch: Wir verdienen 50k mit Leads aus dieser Partnerschaft', 50000, 'CiQ Partnerschaft macht Geld', 32, 90, 32, 'CHF', 'metric', '2023-09-19 12:25:45.308888', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (185, 0, '2023-09-18 12:41:41.302691', 'Commit (0.3): Planung ist über die PLs abgesprochen; Target (0.7): Prozess ist etabliert; Stretch (1.0): Selbstorganisierte PLs. Stichtag 19.12 ', 1, 'Die Zusammenarbeit der PLs ist synchronisiert und flutschig', 28, 93, 28, 'NUMBER', 'metric', '2023-09-18 12:41:41.302691', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (198, 0, '2023-09-19 13:20:35.522647', '0.3 (Commit Zone): Ein PoC zu Kubermatic ist initiiert und läuft.
0.7 (Target Zone): Ein detaillierter Blogpost, der unsere Erkenntnisse und Erfahrungen mit Kubermatic darstellt, ist veröffentlicht.
1.0 (Stretch Goal): Ein klar strukturiertes Konzept ist erstellt, welches aufzeigt, in welchen Szenarien und Kontexten Kubermatic optimal eingesetzt werden kann.', 100, 'Erkundung von Kubermatic und dessen Anwendungspotenzial', 40, 97, 40, 'PERCENT', 'metric', '2023-09-19 13:20:35.522647', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (207, 0, '2023-09-19 13:28:47.720986', 'C, T, S
    • Ambitiöses (stretch) Jahresziel: 75%. Durchschnitt der letzten 5 Jahre: 68%. GJ 2018/19 mit Höchstwert der letzten 5 Jahren: 74.4%. Durchschnitt GJ 2022/23: 73.4%. Aktuell Eintritte, fehlend Aufträge, Verzögerungen ergo guter Wert: ->> 73%
    • Ein Monat über 73%; Target Zone: Zwei Monate über 73%; Stretch: Jeder Monat über 73%.
', 1, 'Die absolute Verrechenbarkeit ist in jedem Monat mindestens 73%', 4, 102, 4, 'NUMBER', 'metric', '2023-09-19 13:28:47.720986', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (197, 0, '2023-09-19 13:20:25.266187', '0.3 (Commit Zone): Der Cilium PoC wurde abgeschlossen und ausgewertet. Wir haben ein Konzept erstellt, für welche Use Cases Cilium Sinn macht.
0.7 (Target Zone): Es wurden 3 Kunden leads generiert.
1.0 (Stretch Goal): Ein Lead hat die Offer-Phase im /mid Sales funnel erreicht.', 100, 'Cilium Use-Case definiert und Kunden angegangen.', 40, 97, 40, 'PERCENT', 'metric', '2023-09-19 13:20:25.266187', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (116, 0, '2023-06-27 08:30:32.261977', '', 1, 'Wir führen mit jeder Division eine Kommunikationsplanung durch', 26, 69, 26, 'NUMBER', 'metric', '2023-06-27 08:30:32.261977', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (108, 38, '2023-07-05 08:14:03.764364', '', 40, 'Wir steigern die Öffnungsrate des Newsletters von 38% auf 40%', 26, 66, 26, 'PERCENT', 'metric', '2023-07-05 08:14:03.764364', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (114, 0, '2023-07-05 08:23:35.390507', '2 interne Events pro Monat', 6, 'Wir unterstützen die GL bei der internen Kommunikation von Events & Ereignissen', 26, 68, 26, 'NUMBER', 'metric', '2023-07-05 08:23:35.390507', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (118, 0, '2023-07-05 08:31:49.819983', '', 2, 'Wir produzieren und publizieren zwei Videos mit technischen Inhalten', 26, 69, 26, 'NUMBER', 'metric', '2023-07-05 08:31:49.819983', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (82, 0, '2023-07-10 09:34:18.882319', '0.3 (Commit Zone): Das Ziel wird 1x erreicht; 0.7 (Target Zone): Das Ziel wird 2x erreicht; 1.0 (Stretch Goal): Das Ziel wird 3x erreicht.

Es wird davon ausgegangen, dass die 3rd lvl Verträge zur Erreichung der Kennzahl beitragen.', 1, 'Die absolute Verrechenbarkeit von /mid/container ist in jedem Monat mindestens 71.0%', 31, 58, 31, 'NUMBER', 'metric', '2023-07-10 09:34:18.882319', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (104, NULL, '2023-07-12 13:10:32.846345', '', 0, 'Wir erzielen ein Nettowachstum von 0', 31, 58, 31, 'NUMBER', 'metric', '2023-07-12 13:10:32.846345', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1258, NULL, NULL, 'Auch wenn dies keine aktive MO mehr ist, wird /sys weiterhin sehr aktiv sein und am Markt weitere Kunden probieren zu aquirieren', NULL, 'Weitere Operations Kunden aquirieren', 34, 1093, 34, NULL, 'ordinal', '2024-06-10 14:40:23.828938', '3 Leads am Start', '1 Neuer Ops Kunde', '2 neue Ops Kunden', 0);
INSERT INTO okr_pitc.key_result VALUES (212, 0, '2023-10-18 13:02:24.193063', '(Commit Zone): 30% der Jenkins Pipelines & Projekte sind vollständig dokumentiert. (Target Zone): 70% der Jenkins Pipelines & Projekte sind vollständig dokumentiert. (Stretch Goal): Alle Jenkins Pipelines & Projekte sind vollständig dokumentiert.

Eine vollständige Dokumentation sollte die folgenden spezifischen Informationen enthalten: Team (Welches Team ist für die Pipeline verantwortlich?), SPOC (Wer ist der Hauptansprechpartner für die Pipeline?), Status (In welchem Zustand befindet sich das Projekt und die Pipeline? [z.B. in Entwicklung, Wartung, Abschluss etc.]), Benötigte Puzzle Infra (Welche Infrastruktur von Puzzle wird benötigt?), Wartungsvertrag (Gibt es einen Wartungsvertrag? [Ja/Nein]), Vertragsdetails (Details zu bestehenden Vertragsvereinbarungen)', 100, 'Wir dokumentieren alle aktiven Pipelines & Projekte', 31, 106, 31, 'PERCENT', 'metric', '2023-10-18 13:02:24.193063', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (41, 0, '2023-08-28 14:00:53.935079', 'Commit (0.3): Happiness Umfrage ist bei mind. 3 Divisions eingeführt und erste Auswertung ist vorhanden; Target (0.7): Happiness Umfrage ist bei mind. 7 der Divisions eingeführt; Stretch (1.0): Happiness Umfrage ist bei allen Divisions etabliert.
Stichtag: 15.9.
', 1, 'Die Happiness Umfrage ist in den Divisions eingeführt, ausgewertet und Zielwerte definiert.', 13, 42, 13, 'NUMBER', 'metric', '2023-08-28 14:00:53.935079', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (33, NULL, '2023-06-01 14:06:27.14655', 'Überlegungen:  Ambitiöses (“stretch”) Jahresziel müsste 76% sein (kein grosser strategischer Invest geplant; besser als die letzten drei Jahre, aber deutlich unter Spitzenjahr 19/20, in den Monaten Jan-April hatten wir im Schnitt 75.5%). Für Q1 und Q2 haben wir die Lehrlinge, darum 3% Abschlag (ca. 0.6 FTE für Lehrlingsbetreuung) --> 73%
', 73, 'Die absolute Verrechenbarkeit ist in jedem Monat über 73.0%', 5, 39, 5, 'PERCENT', 'metric', '2023-06-01 14:06:27.14655', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (42, 0, '2023-06-14 09:23:05.038915', 'Commit (0.3): Die Frage “Wie zufrieden bin ich mit meiner Arbeit” aus Happiness Umfrage ist als Grundlage für Konzept ausgewertet; Target (0.7): Commit + Konzept für stärkeres involvment der Members ist definiert; Stretch (1.0): Target + erste Massnahmen aus Konzept sind umgesetzt. Stichdatum 15.9.
', 1, 'Members und ihre Wünsche werden in der Akquise mehr involviert und die Zufriedenheit mit der Arbeit gesteigert', 16, 42, 16, 'NUMBER', 'metric', '2023-06-14 09:23:05.038915', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (32, NULL, '2023-06-07 07:45:08.822171', 'Überlegungen: Ambitiöses (“stretch”) Jahresziel ist 17.0kCHF  (kein grosser strategischer Invest geplant; besser als die letzten drei Jahre, aber deutlich unter Spitzenjahr 19/20.) Unter Berücksichtigung, dass Q1 [alle Quartalswerte mit einem Monat “Lag”] ca 12% weniger Ist Stunden hat, und wir wieder das BBT Team betreuen ist ein Abschlag von 15% auf die 17kCHF eingerechnet --> 14.5kCHF', 14500, 'Der Mitarbeiterumsatz pro FTE beträgt in jedem Monat mehr als 14.5kCHF', 5, 39, 5, 'CHF', 'metric', '2023-06-07 07:45:08.822171', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (40, 0, '2023-06-14 09:20:07.357072', 'Commit (0.3): zu 50% der internen Events (offiz. Fübi / Sommerinfo / Aareböötle usw.) gibt es einen Newsbeitrag mit Fotos;
Target (0.7): zu 80% der internen Events (offiz. Fübi / Sommerinfo / Aareböötle usw.) gibt es einen Newsbeitrag mit Fotos;
Stretch (1.0): Target + Es gibt mindestens 2 interne Events pro Monat.
Stichtag 15.9.', 1, 'Events & Ereignisse werden intern kommuniziert und zelebriert', 24, 42, 24, 'NUMBER', 'metric', '2023-06-14 09:20:07.357072', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (113, 0, '2023-06-27 08:21:37.401184', '', 1, 'Wir platzieren den Newsletter prominent auf unserer Startseite puzzle.ch', 26, 66, 26, 'NUMBER', 'metric', '2023-06-27 08:21:37.401184', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (101, 0, '2023-07-11 12:34:42.250956', 'Stichtag: 15.9. ', 13, 'Alle Members sind für die nächsten zwei Monaten geplant (Perspektive)', 28, 64, 28, 'NUMBER', 'metric', '2023-07-11 12:34:42.250956', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (46, 0, '2023-06-14 09:40:09.588376', 'Commit (0.3): Divisionsrentabilität ist eingeführt;
Target (0.7): Commit + Neue Zielwerte sind Ende Quartal vorhanden
Stretch (1.0): Commit + Neue Zielwerte sind auf den zweiten Monat bereits vorhanden; Stichtag 15.9.', 1, 'Die Divisionsrentabilitätsrechnung ist eingeführt und Zielwerte sind definiert', 22, 43, 22, 'NUMBER', 'metric', '2023-06-14 09:40:09.588376', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (48, 0, '2023-06-14 10:46:24.350383', 'Commit (0.3): Umfrage bei Members durchgeführt, AWS Partnerschaft abgeschlossen und kommuniziert; Target (0.7): GCP Partnerschaft abgeschlossen und kommuniziert und 1 neuer Kunde gewonnen (AWS oder GCP);  Stretch (1.0): 3 neue Kunden gewonnen. Stichtdatum 15.9.
', 1, 'Hyperscaler: Partnerschaften mit AWS und GCP sind abgeschlossen und wir gewinnen neue Kunden ', 16, 44, 16, 'NUMBER', 'metric', '2023-06-14 10:46:24.350383', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (49, 0, '2023-06-14 10:49:06.237495', 'Commit (0.3): Pro Technologie ist ein Blogpost vorhanden welcher unsere Expertise zeigt; Target (0.7): Commit + Wir haben pro Technologie mindestens 3 Members mit Wissen und mindestens ein Kunde mit Interesse. Stretch (1.0) Wir haben in jeder Technologie bereits Dienstleistungsumsätze generiert. Stichdatum 15.9.
', 1, 'New Tech: Puzzle ist für die gewählten Technologien am Markt bekannt ', 24, 44, 24, 'NUMBER', 'metric', '2023-06-14 10:49:06.237495', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (160, 0, '2023-11-16 09:34:29.549033', '0.3 (Commit Zone): Das Angebot wurde mittels einer Google Ads oder Linkedin Kampagne beworben. Gleichzeitig konnten 3 bestehende Kunden zum Angebot befragt werden.
0.7 (Target Zone): Aus der online Kampagne konnten 3 Leads für das Consulting Angebot gewonnen werden. 2 Members aus dem /mid /cicd Team konnten für MLOps Consulting begeistert werden und helfen bei den Pipeline Themen mit.
1.0 (Stretch Goal): Aus den Leads ist ein konkreter Deal resultiert

Fortschritt: https://codimd.puzzle.ch/LgAhVeD0SXiswTA8GlG1GQ?both', 1, 'Das neue Angebot MLOps Consulting ist gestaffed und validiert', 51, 83, 51, 'NUMBER', 'metric', '2023-11-16 09:34:29.549033', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (57, 0, '2023-06-20 13:10:44.568941', '0.3 (Commit Zone): Indikatoren sowie Mess- und Zielwerte für Beurteilung von Teamzusammenhalt sind definiert.
- 0.7 (Target Zone): Zusätzlich ist eine zusammenhaltsfördernde Aktivität durchgeführt und gemäss festgelegten Indikatoren ausgewertet.
- 1.0 (Strech Goal): Zusammenhaltsfördernde Aktivitäten sind etabliert und werden durch Initiative von Members iniziert und umgesetzt.', 1, 'Zusammenhaltsförderende Aktivitäten innerhalb /mobility oder Subteams unter Mitwirkung der Members durchgeführt.', 20, 40, 20, 'PERCENT', 'metric', '2023-06-20 13:10:44.568941', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (38, 0, '2023-06-20 13:12:14.921932', '0.3 (Commit Zone): Mindestens 50% aller /mobility Members (atkuell 14 Members) nehmen durchschnittlich am monatlichen /mobility Tag teil.
0.7 (Target Zone): Mindestens 65% aller /mobility Members (aktuell 18 Members) nehmen durchschnittlich am monatlichen /mobility Tag teil.
1.0 (Strech Goal): Mindestens 80% aller /mobility Members (aktuell 22 Members) nehmen durchschnittlich am monatlichen /mobility Tag teil.', 1, 'Der /mobility Tag erhöht die Identifikation und den Zusammenhalt unter /mobility Members mit Puzzle & /mobility.', 20, 40, 20, 'NUMBER', 'metric', '2023-06-20 13:12:14.921932', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (77, -85, '2023-06-20 13:13:02.914596', 'Pensumsverluste bei Sys Engineers (Tiago -65%, Rémy -20%) durch Anstellung eines neuen Sys Engineers eingrenzen/kompensieren
Commit: -50%, Target: 0%, Stretch: 10%', 0, 'Kompensation Pensumsverluste bei Sys Engineering', 33, 51, 33, 'PERCENT', 'metric', '2023-06-20 13:13:02.914596', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (78, 0, '2023-06-20 13:14:57.587783', 'Ordinales Ziel:
- Commit: Wir verlieren keine Members
- Target: Wir decken alle neuen Mandate mit vorhandenen und geplanten Members ab
- Stretch: Wir stellen ein zusätzlicher voll verrechenbarer Member an', 1, 'Wir kümmern uns um unsere FTE-Auslastung', 36, 54, 36, 'NUMBER', 'metric', '2023-06-20 13:14:57.587783', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (72, 6, '2023-08-28 11:14:55.983388', 'Zufriedenheit der Devs mit ihrer Auftrags-Perspektive auf Skala von 1-10.
Commit: 6, Target: 8, Stretch: 9', 10, 'Perspektive für Devs geschafft', 33, 49, 33, 'NUMBER', 'metric', '2023-08-28 11:14:55.983388', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (59, 0, '2023-06-20 12:53:24.885364', '0.3 (Commit Zone): Ein Pool von mindestens 5 interessierten Members besteht
0.7 (Target Zone): Das MLOps Tech-Lab wurde einmal durchgeführt und dabei einen weiteren Trainer eingeführt. Ein Blog Post zeigt unsere Entscheidung und Anwendung der gewählten Technologie auf.
1.0 (Stretch Goal): Zusätzlich wurde ein zweites MLOps Tech-Lab durch 2. Trainer/in durchgeführt. Technologiehersteller spricht Budget für Kommunikation.
', 1, 'Das MLOps Tech-Lab ist entwickelt und Kompetenzen aufgebaut. ', 30, 47, 30, 'NUMBER', 'metric', '2023-06-20 12:53:24.885364', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (35, 0, '2023-06-20 13:38:07.407737', 'Wir stellen zwei neue Member (Engineers) an (und keine verlieren) mit durchschnittlich 80% Pensum, um unser Verhältnis verrechenbare/nicht verrechenbare Pensen zu verbessern.', 1.6, 'Netto-Wachstum um 1.6 verrechenbarer FTE', 20, 39, 20, 'NUMBER', 'metric', '2023-06-20 13:38:07.407737', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (98, 0, '2023-06-20 13:40:25.498089', '	* Commit (0.3): Auslegeordnung
	* Target (0.7): Definiert und Dokumentation, Plannung von Members
	* Stretch (1.0): implementiert und etabliert
	* Stichtag: 15.9. ', 1, 'Der Prozess von RubyOps ist etabliert', 28, 63, 28, 'NUMBER', 'metric', '2023-06-20 13:40:25.498089', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (100, 0, '2023-06-20 13:42:06.472798', '	* Commit (0.3): Issue erstellt und konzipiert und geplant
	* Target (0.7): in bearbeitung, PR vorhanden
	* Stretch (1.0): auf allen Umgebungen deployed
	* Stichtag: 15.9. ', 1, 'Hitobito und PTime ist auf Ruby Version >3', 28, 63, 28, 'NUMBER', 'metric', '2023-06-20 13:42:06.472798', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (102, 0, '2023-06-20 13:48:38.453287', '	* Commit (0.3): 1 Monat über 65%
	* Target 0.7: 2 Monate über 65%
	* Stretch (1.0): 3 Monate über 65%
	* Stichtag 31.8. ', 1, 'Die absolute Verrechenbarkeit liegt in jedem Monat über 65%', 28, 64, 28, 'NUMBER', 'metric', '2023-06-20 13:48:38.453287', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (103, 0, '2023-06-20 13:49:54.059748', '	* Commit (0.3): Eine Verlängerung gesichert
	* Target 0.7: 2 Verlängerungen gesichert
	* Stretch (1.0): 3 oder mehr Verlängerung gesichert
	* Stichtag: 15.9. ', 1, 'Wir kommen gut durch das Sommerloch / Verlängerungen / weder zu viel noch zu wenig Brixel, SAC, BAFU', 28, 64, 28, 'NUMBER', 'metric', '2023-06-20 13:49:54.059748', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (76, 0, '2023-06-20 14:12:57.449485', '0.3 (Commit Zone): Wir führen ein Dagger Kundenmapping durch und verfügen über eine Liste potenzieller Leads;
0.7 (Target Zone): + Wir führen Gespräche mit 3 potenziellen Kunden und werten die Erkenntnisse aus;
1.0 (Stretch Goal): + Wir können mit einem Kunden eine Dagger-Pipeline umsetzen;', 1, 'Dagger-Marktresonanz ist untersucht', 31, 57, 31, 'NUMBER', 'metric', '2023-06-20 14:12:57.449485', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (71, 0, '2023-06-20 14:45:52.685949', 'Ordinales Ziel
Zielwert: 53%
- Commit: 1 Monat über Zielwert
- Target: 2 Monate über Zielwert
- Stretch: 3 Monate über Zielwert', 1, 'Wir halten die absolute Verrechenbarkeit in allen Monaten hoch', 36, 54, 36, 'NUMBER', 'metric', '2023-06-20 14:45:52.685949', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (105, 0, '2023-08-21 13:07:46.146106', '0.3 (Commit Zone): Wir organisieren ein Meeting mit den involvierten Members; 0.7 (Target Zone): + Wir machen eine Auslegeordnung als Basis für''s Q2; 1.0 (Stretch Goal): + Wir definieren erste Schritte und verteilen Tasks;', 1, 'Kubermatic Auslegeordnung gemacht', 40, 57, 40, 'NUMBER', 'metric', '2023-08-21 13:07:46.146106', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (93, 0, '2023-06-23 14:10:01.194713', '0.3 (Commit Zone): Monatliche Happiness Umfrage ausgewertet; 0.7 (Target Zone): + Massnahmenpaket für''s nächste Quartal definiert; 1.0 (Stretch Goal): + Zufriedenheit in jedem Monat etwas höher als im Vormonat. ', 1, 'Grundlage & Massnahmen zur Förderung sowie Messung der Members-Zufriedenheit definiert', 31, 60, 31, 'NUMBER', 'metric', '2023-06-23 14:10:01.194713', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (106, 0, '2023-06-25 09:19:59.841728', 'Commit:
. Swisscom cADC erstellt, kommuniziert und 2x durchgeführt
Target:
- Mobiliar Gaia erstellt, kommuniziert und 2x durchgeführt
- BUAR SAP HANA erstellt, kommuniziert und 1x durchgeführt
Stretch:
- Standard-Controlling-/ Reporting Vorlage vorhanden & dokumentiert
- F&A-Team instruiert für Durchführung beim Monatsabschluss', 1, 'Wir verbessern, vereinheitlichen und etablieren ein regelmässiges Controlling und Reporting', 4, 56, 4, 'NUMBER', 'metric', '2023-06-25 09:19:59.841728', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (99, 75, '2023-06-26 10:55:12.663594', 'Die absolute Verrechenbarkeit von /zh ist in jedem Monat mindestens auf 70% (Commit) bzw. 75% (Target) bzw. 80% (Stretch)', 80, 'Absolute Verrechenbarkeit', 33, 65, 33, 'PERCENT', 'metric', '2023-06-26 10:55:12.663594', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (81, 0, '2023-07-10 09:34:09.465359', '0.3 (Commit Zone): Das Ziel wird 1x erreicht; 0.7 (Target Zone): Das Ziel wird 2x erreicht; 1.0 (Stretch Goal): Das Ziel wird 3x erreicht. Es wird davon ausgegangen, dass die 3rd lvl Verträge zur Erreichung der Kennzahl beitragen.

Es wird davon ausgegangen, dass die 3rd lvl Verträge zur Erreichung der Kennzahl beitragen.', 1, 'Die absolute Verrechenbarkeit von /mid/cicd ist in jedem Monat mindestens 65.0%', 31, 58, 31, 'NUMBER', 'metric', '2023-07-10 09:34:09.465359', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (115, 0, '2023-06-27 08:28:56.233567', '', 3, 'Wir planen 3 Puzzleness-Inhalte, die wir in passender Form (Video, Social Media, Blogpost) publizieren', 26, 68, 26, 'NUMBER', 'metric', '2023-06-27 08:28:56.233567', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (119, 0, '2023-07-05 07:56:45.069795', '0.3 (Commit Zone): Baseline fürs nächste Quartal ist definiert (unter Berücksichtigung der Senioritätsstufen u.ä.); 0.7 (Target Zone): Baseline ist definiert + Massnahmenpaket zur Stundensatzerhöhung ist definiert; 1.0 (Stretch Goal): Baseline + Massnahmenpaket + Mittlerer Stundensatz zu aktuellem Wert um 2CHF erhöht auf 155CHF/h
', 1, 'Durchschnittliche Stundensätze um 2CHF erhöhen', 5, 39, 5, 'NUMBER', 'metric', '2023-07-05 07:56:45.069795', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (67, 0, '2023-07-06 13:51:08.637905', 'Ordinales Ziel:
- Commit: Zieldefinition WAC Teamkultur erfolgt
- Target: erste Massnahme zur Steigerung definiert und umgesetzt
- Stretch: zusätzliche zweite Massnahme zur Steigerung definiert und umgesetzt', 1, 'Wir definieren die WAC Teamkultur mit dem Team und leiten konkrete Massnahmen ab um diese zu stärken', 36, 53, 36, 'NUMBER', 'metric', '2023-07-06 13:51:08.637905', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (120, 0, '2023-07-27 06:38:54.157882', 'S: Bis zum Ende des laufenden Geschäftsjahres (31. Dezember 2023) streben wir an, den Umsatz zu halten oder leicht zu steigern im Vergleich zum Vorjahr
M: Der Fortschritt wird anhand der monatlichen Umsatzzahlen und Vergleichswerte aus dem Vorjahr gemessen.
A: Die geplante Umsatzsteigerung basiert auf den Kundenbudgets sowie den vorhanden Leads.
R: Der Umsatz ist von grossr Bedeutung für die finanzielle Stabilität von Hitobito
T:: Das Ziel soll bis zum 31. Dezember 2023 erreicht werden.', 460000, 'Wir erreichen und steigern den Umsatz', 41, 70, 41, 'CHF', 'metric', '2023-07-27 06:38:54.157882', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (157, 0, '2023-09-19 12:16:43.197533', 'Commit (0.3): Retro durchgeführt ; Target (0.7): Commit + Planung ist erstellt inkl. Tickets. Stretch (1.0): Wir leben den Przoess bereits nach Vorgaben und der KVP wird gelebt . Stichdatum 19.12.23. ', 1, 'Der Wartungsprozess ist etabliert und wir leben ihn', 41, 81, 41, 'NUMBER', 'metric', '2023-09-19 12:16:43.197533', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (129, 0, '2023-09-06 12:52:52.271892', 'Commit (0.3): Wir können ein neues agiles Team offerieren; Target (0.7): wir gewinnen einen neuen Auftrag; Stretch (1.0): Wir gewinnen mehrere Aufträge. Stichdatum 19.12.', 1, 'Agile Teams: Unsere Sales Kampagne ist erfolgreich und wir gewinnen neue Aufträge.
', 13, 76, 13, 'NUMBER', 'metric', '2023-09-06 12:52:52.271892', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (123, 0, '2023-08-21 11:19:20.772977', 'Commit (0.3): Termine sind abgestimmt mit 2 Accounts ; Target (0.7): Termine sind abgestimmt mit 4 Accounts (1.0): Termine sind abgestimmt mit 6 Accounts; Stichtag 20.10.

SAC
die Mitte
Wanderwege Schweiz
Pfadi
Jubla
Blasmusik

', 100, 'Accountgespräche sind geplant ', 41, 72, 41, 'PERCENT', 'metric', '2023-08-21 11:19:20.772977', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (125, NULL, '2023-08-21 10:46:51.265759', 'Commit (0.3): 1 Monat über 55%; Target 0.7: 2 Monate über 55%; Stretch (1.0): 3 Monate über 55%. Stichtag 31.8 (Monatsabschlüsse Juni, Juli, August)

Uner absoluter Verrechenbarkeit verstehen wir als Baisis die Verrechenbarkeit inkl die  inidirekte Stunden der Wartung( Budget) und  Lösungsbduget', 56, 'Die absolute Verrechenbarkeit liegt in jedem Monat über 55%', 41, 70, 41, 'PERCENT', 'metric', '2023-08-21 10:46:51.265759', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (121, NULL, '2023-08-21 10:53:58.212369', 'Commit (0.3): 1 Monat über 92%; Target 0.7: 2 Monate über 92%; Stretch (1.0): 3 Monate über 92%. Stichtag 31.8 (Monatsabschlüsse Juni, Juli, August)

Messbarkeit: Durchschnittliche Verrechenbarkeit der laufenden Kundenauftrage (exkl. BWS) ', 92, 'Unsere Aufträge sind mit einer Verrechenbarkeit > 92% rentabel ', 41, 70, 41, 'PERCENT', 'metric', '2023-08-21 10:53:58.212369', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1310, 0, NULL, 'Messung ist kumuliert zu lesen (3*15%, 15% pro Monat)', 45, '15% Marge', 33, 1109, 33, 'PERCENT', 'metric', '2024-06-17 23:57:47.193358', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (127, 0, '2023-08-24 07:58:01.986223', 'Commit (0.3): Inhalt ist bekannt ; Target (0.7): Release ist bereit  (1.0): Release sind deployed und kommuniziert; Stichtag 20.9. ', 1, 'Hitobito Release für Q1 (Q3''23)', 65, 72, 65, 'NUMBER', 'metric', '2023-08-24 07:58:01.986223', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (189, 0, '2023-09-19 12:16:45.490927', 'Commit: Wir haben 3 Action Items umgesetzt, welche den Sys-Service verbessern Target: Wir haben 8 Action Items umgesetzt, welche den Sys-Service verbessern Stretch: Wir haben 20 Action Items umgesetzt, welche den Sys-Service verbessern', 1, 'improvements im internen (und externen) Supportprozess implementieren', 32, 94, 32, 'PERCENT', 'metric', '2023-09-19 12:16:45.490927', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (130, 0, '2023-10-05 13:45:40.35018', 'Commit (0.3): Ausbildungspakete für die Members sind definiert und mind. 4 Members haben mit den Zertifizierungen bei AWS und GCP gestartet;
Target (0.7): Unser Cloud-Angebot ist definiert und wurde intern abgestimmt;
Stretch (1.0): Unser Cloud-Angebot ist kommuniziert und bei 20 potentiellen Kund*innen platziert.
Stichtag: 19.12.23', 1, 'Hyperscaler/Cloud: Der Wissensaufbau für die Erreichung der Zertifizierungsziele ist lanciert und verbindlich mit den Members vereinbart. Parallel dazu definieren und kommunizieren wir unser Cloud-Angebot.', 16, 76, 16, 'NUMBER', 'metric', '2023-10-05 13:45:40.35018', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (132, 0, '2023-09-08 07:09:39.882271', 'Commit (0.3): Das gesamte Leadership Team ist sich seiner Möglichkeiten bewusst und in passendem Rahmen aktiv auf seine Rolle hingewiesen.
Target (0.7): Das LST fördert aktiv das positive Denken innerhalb von Puzzle. Erfahrungen werden an Monthly oder AUMC Meetings geteilt.
Stretch (1.0): Eine Umfrage im LST gegen Ende der Zeitperiode zeigt eine wahrnehmbare, positive Wirkung.

Stichtag 19.12.', 1, 'Das Leadership-Team fördert eine Kultur des positiven Denkens', 24, 77, 24, 'NUMBER', 'metric', '2023-09-08 07:09:39.882271', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (138, 0, '2023-10-05 13:49:31.833401', 'Commit (0.3): Wir führen einen facilitierten Workshop mit ausgewählten Members zu den Resultaten aus dem Open Space "Innen-/Aussensicht/Vertrauen/Zusammenarbeit" des Puzzle Workshops 2023 durch und definieren Themenschwerpunkte sowie Massnahmen.
Target (0.7): wir setzen eine Massnahme aus dem Workshop um
Stretch (1.0): wir setzen drei oder mehr Massnahmen um
Stichdatum: 19.12.23', 1, 'Wir stärken das Miteinander, das Vertrauen und die Zusammenarbeit untereinander', 16, 77, 16, 'NUMBER', 'metric', '2023-10-05 13:49:31.833401', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (122, NULL, '2023-09-11 12:31:31.609673', 'Das Wartungs&Support Budget für Hitobito beträgt 111''800 CHF pro Kalenderjahr ', 111800, 'Wir halten das Wartungsbudget ein', 41, 70, 41, 'CHF', 'metric', '2023-09-11 12:31:31.609673', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (171, 0, '2023-09-18 23:38:17.616719', 'Ziel: 60% der verrechenbaren Pensen sind für Jan+Feb fix verplant (Commit) bzw. 70% (Target) bzw. 80% (Stretch).', 100, 'Hohe definitive Teamauslastung für Jan+Feb 2024', 33, 87, 33, 'PERCENT', 'metric', '2023-09-18 23:38:17.616719', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (133, 0, '2023-10-05 13:44:59.180785', 'Commit (0.3): Wir definieren ein geniales Ops Marktangebot hinter welchem wir intern alle stehen
Target (0.7): Wir stampfen einen umwerfenden Vermarktungsplan für 2024 aus dem Boden Stretch (1.0): Wir gewinnen mindestens einen weiteren spannenden Ops Kunden
Stichdatum: 19.12.23 ', 1, 'Operations: Wir definieren geniales Marktangebot und stampfen umwerfenden Vermarktungsplan aus dem Boden', 16, 76, 16, 'NUMBER', 'metric', '2023-10-05 13:44:59.180785', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (222, 2, '2023-09-28 14:54:19.064884', 'Als Neukunde gilt ein Kunde, der nicht über Partnerunternehmen beauftragt. Die Abrechnung erfolgt direkt.', 6, 'Wir haben Neukunden gewonnen', 17, 109, 17, 'NUMBER', 'metric', '2023-09-28 14:54:19.064884', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (224, 0, '2023-11-06 08:36:54.478686', 'Commit: Die technischen Workshops findet mit der Mindestanzahl Teilnehmenden statt
Target: Die technischen Workshops sind ausgebuch
Stretch Goal: Wir haben mehr Anfragen als wir im nächsten Quartal bewältigen können', 1, 'Die Auslastung der im Quartal angebotenen technischen Workshops', 84, 110, 84, 'PERCENT', 'metric', '2023-11-06 08:36:54.478686', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (148, 0, '2023-09-19 12:47:40.447763', '', 20, 'Erhöhung der monatlichen Besucherzahl der Webseite um 20% ggü. des Vorjahres mit SEO optimiertem Content', 49, 79, 49, 'PERCENT', 'metric', '2023-09-19 12:47:40.447763', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (144, 0, '2023-09-12 08:59:45.402234', 'In mindestens einem Monat erreicht =1; nicht erreicht = 0', 1, 'In einem Monat dieses Quartals machen wir einen Dienstleistungsumsatz (ohne Sublieferanten) von über 1.75 MCHF', 24, 75, 24, 'NUMBER', 'metric', '2023-09-12 08:59:45.402234', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (149, 0, '2023-09-19 11:54:20.0102', 'Commit: Erarbeitung und Freigabe der Sitemap für die neue Website
Target: Erarbeitung und Freigabe der Sitemap für die neue Website und Erstellen erster Wireframes
Stretch: Erarbeitung und Freigabe der Sitemap für die neue Website, erstellen erster Wireframes und Abfüllen von Inhalten (Text/Bild)', 1, 'Lancierung des internen Projekts zur neuen Website', 49, 79, 49, 'NUMBER', 'metric', '2023-09-19 11:54:20.0102', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (152, 0, '2023-10-16 12:23:50.872377', 'Commit (0.3): 1 Monat über 75''000; Target 0.7: 1 Monat über 80''000; Stretch (1.0): 1 Monat über 100''''000. Stichtag 19.12.23 (Sep-Nov)', 100000, 'Wir erreichen einen Rekordumsatz ', 41, 80, 41, 'CHF', 'metric', '2023-10-16 12:23:50.872377', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (154, 0, '2023-09-19 12:14:22.437931', 'Commit (0.3): 3 Gespräche sind durchgeführt; Target (0.7): 5 Gespräche sind durchgeführt; Stretch (1.0): 7 Gespräche sind durchgeführt; Stichtag 19.12.23
SAC
die Mitte
Wanderwege Schweiz (geplant)
Pfadi
Jubla
Blasmusik
GLP

(Kunden abholen) ', 7, 'Accountgespräche sind durchgeführt ', 41, 81, 41, 'NUMBER', 'metric', '2023-09-19 12:14:22.437931', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (151, 0, '2023-10-16 12:34:12.022542', 'Commit (0.3): 1 Monat über 60%; Target 0.7: 2 Monate über 60%; Stretch (1.0): 3 Monate über 60%. Stichtag 15.12 (Monatsabschlüsse Sep. Okt. Nov)

Unter absoluter Verrechenbarkeit verstehen wir als Basis die Verrechenbarkeit inkl die  inidirekte Stunden der Wartung (Budget) und  Lösungsbduget', 60, 'Wir erreichen eine hohe absolute Verrechenbarbeit', 41, 80, 41, 'PERCENT', 'metric', '2023-10-16 12:34:12.022542', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (161, 0, '2023-11-08 11:42:00.870663', 'Commit Zone: Ein Monat über 75%; Target Zone: Zwei Monate über 75%; Stretch: Jeder Monat über 75%.', 1, 'Wir erreichen mit 75% eine absolute Verrechenbarkeit, die höher ist als der beste Wert in den letzten 3 Jahren', 5, 84, 5, 'NUMBER', 'metric', '2023-11-08 11:42:00.870663', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (140, 0, '2023-10-05 13:51:10.934831', 'Commit (0.3): Wir überprüfen, überarbeiten und ergänzen (bei Bedarf) unsere Grundsätze. Anschliessend kommunizieren wir diese aktiv.
Target (0.7): Wir führen im Rahmen einer Umfrage ein Self Assessment zu den Grundsätzen durch und überprüfen damit, wie wir diese Leben
Stretch (1.0): ausgehend von den Resultaten überlegen wir uns, wie wir die Grundsätze besser verankern können und nehmen uns Themen vor, welche wir verbessern wollen. Stichdatum: 19.12.23', 1, 'Wir nehmen unsere Puzzle Grundsätze aus dem Keller und halten sie hoch', 13, 77, 13, 'NUMBER', 'metric', '2023-10-05 13:51:10.934831', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (131, 0, '2023-09-19 11:55:23.554334', 'Messung: Differenz unterschriebene Verträge Minus Kündigungen zwischen Oktober bis Dezember 2023.', 3, 'Wir erhöhen die verrechenbaren Pensen um 3 FTE', 13, 75, 13, 'NUMBER', 'metric', '2023-09-19 11:55:23.554334', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (156, 0, '2023-09-19 11:59:14.490305', 'Commit (0.3): Community Meeting ist organisiert; Target (0.7): Es nehmen 10 externe Teilnehmer teil, Stretch (1.0): Es nehmen 15 Teilnehmer teil; Stichtag 15.12.2023 ', 1, 'Wir führen ein erfolgreiches spannendes Community Meeting durch ', 41, 81, 41, 'NUMBER', 'metric', '2023-09-19 11:59:14.490305', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (155, 0, '2023-10-30 12:44:22.5887', 'Commit (0.3): Release sind deployed und kommuniziert; ; Target (0.7): weniger als 3 Bugs (1.0): : weniger als 1 Bugs; ', 1, 'Hitobito Release für Q1 (Q3''23)', 65, 81, 65, 'NUMBER', 'metric', '2023-10-30 12:44:22.5887', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (158, 0, '2023-09-19 12:12:21.906977', 'Commit (0.3): Nexcloud ist ein Blogpost vorhanden sowie Kundendemo an Community Meeting; Target (0.7): Commit + 1 Bestellung . Stretch (1.0) Wir haben 2 Bestellungen von Nextcloud Features. Stichdatum 01.01.2024. ', 1, 'Unser neues Features wie Nexcloud ist am Markt bekannt ', 41, 82, 41, 'PERCENT', 'metric', '2023-09-19 12:12:21.906977', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (147, 0, '2023-09-19 13:25:08.409028', 'Commit: 4 technische Blogposts und/oder Referenzen
Target: 6 technische Blogposts und/oder Referenzen
Stretch: 6 technische Blogposts und/oder Referenzen und 100 neue Follower', 1, 'Mit technischen Blogposts und Referenzen gewinnen wir auf LinkedIn neue Follower', 26, 79, 26, 'NUMBER', 'metric', '2023-09-19 13:25:08.409028', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (143, 0, '2023-09-19 13:35:53.661887', 'Commit (0.3): durchschnittlich mindestens 50.0%; Target (0.7): 52.0% Stretch (1.0): 53.5%. Stichtag 19.12 ', 1, 'Wir erreichen eine sehr gute absolute Verrechenbarkeit ', 22, 75, 22, 'NUMBER', 'metric', '2023-09-19 13:35:53.661887', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (146, 0, '2023-11-27 12:54:16.451724', 'Commit: Erarbeitung und Kommunikation der neuen internen Eventreihe.
Target: Durchführung von mindestens 2 Division-Events in Q2.
Stretch: Durchführung von mindestens 2 Division-Events in Q2 sowie ein Mittelwert von 8 auf einer 10er-Skala in einer Memberzufriedenheitsumfrage (qualitativ) nach Eventbesuch.', 1, 'Lancierung der Eventreihe «Puzzle Inside».', 26, 78, 26, 'NUMBER', 'metric', '2023-11-27 12:54:16.451724', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (167, 0, '2023-09-28 09:40:34.115823', 'Commit Zone: Gespräche mit RTEs/PLs sind geführt und Wunsch nach Verlängerung der Mandate ist platziert. Target Zone: 100% der Mandate können bis Ende Jahr verlängert werden oder alternative Einsatzmöglichkeiten sind gefunden. Stretch Goal: Für einen weiteren Member konnte ein Einsatz bei Siemens oder Mobiliar vereinbart werden. https://codimd.puzzle.ch/5ba4-O99T4aI2xkmLpljPA?both#', 1, 'Siemens und Mobiliar Bestellungen werden verlängert', 5, 86, 5, 'NUMBER', 'metric', '2023-09-28 09:40:34.115823', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (176, 0, '2023-09-19 12:24:15.401717', 'Commit: Das Beispielmodule ist in den Main gemerget
Target: Wir haben 4 Module entwickelt
Stretch: Wir haben 8 Module entwickelt', 8, 'Opnsense collection hat erste Module', 32, 90, 32, 'NUMBER', 'metric', '2023-09-19 12:24:15.401717', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (174, 0, '2023-09-19 13:56:44.75946', 'Commit: Unsere Kunden wissen wie sie uns erreichen
Target: Unsere Operations/Support-Zwiselis wissen wer unsere Kunden sind, welche SLAs diese haben und koennen ihnen Zeitnah helfen
Stretch: Unsere Interne Operations-Front ist so stark, dass wir fuer alle Kunden automatisierte/standardisierte Dokumentationen und Tools', 1, 'Unser Operations ist ins Rollen gekommen', 67, 89, 67, 'NUMBER', 'metric', '2023-09-19 13:56:44.75946', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (177, 0, '2023-09-18 11:59:51.807695', 'Commit: Grafana OnCall wurde evaluiert
Target: Grafana OnCall wurde bei /sys eingefuehrt und mit einem Blogpost in der Oeffentlichkeit abgefeiert
Stretch: Kunden die diesen Artikel suchten kauften auch Grafana OnCall', 1, 'Grafana OnCall', 34, 90, 34, 'NUMBER', 'metric', '2023-09-18 11:59:51.807695', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (164, 0, '2023-09-19 12:10:16.921829', 'Commit (0.3): Die Haltung des Coreteams bezüglich Teilnahme an /mobility Aktivätäten ist an die Members kommuniziert. Target (0.7): Die mobility-Aktivitäten sind monatlich durch die Happiness-Umfrage ausgewertet und allfällige Anpassungen durch Coreteam definiert. Stretch (1.0): Anpassungen umgesetzt und an Members kommuniziert.', 1, 'Wir steigern das Members Involvement bei /mobility', 20, 83, 20, 'NUMBER', 'metric', '2023-09-19 12:10:16.921829', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (173, 10, '2023-09-19 13:45:01.289131', 'Commit: Die Rentabilitätsrechnung ist analyisiert, verstanden und dem Team /zh vorgestellt; Target: Stellschrauben/Massnahmen für /zh sind abgeleitet und mit GL besprochen; Stretch: Umsetzung erster Massnahmen umgesetzt.', 100, 'Rentabilitätsrechnung analysiert und Massnahmen ergriffen', 33, 88, 33, 'PERCENT', 'metric', '2023-09-19 13:45:01.289131', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (170, 0, '2023-09-19 12:19:27.94443', 'Commit (0.3): Die Jahresplanung für alle BLS-Projekte ist mit den BLS-Auftragsverantwortlichen abgesprochen und provisorisch festgelegt. Target (0.7): Für die Applikationen FISZ/KI, Lopas, VBE und Baustellentool sind Budgets fürs 2024 bekannt und Bestellungen eingetroffen. Stretch (1.0): Mit ISCeco ist ein Team für ein neues Projekt im 2024 definiert.', 1, 'Wir haben die Bestellungen für BLS und  ISCeco für 2024 erhalten', 20, 86, 20, 'NUMBER', 'metric', '2023-09-19 12:19:27.94443', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (166, 0, '2023-09-28 09:24:15.470971', 'Commit: Die Rentabilitätsrechnung ist analyisiert und verstanden vom MobCoeur Team sowie /mobilty Members sind /mobilty relevante Themen informiert; Target: Stellhebel und Massnahmen für /mobility sind abgeleitet ; Stretch: Die wirkungsvollste Massnahme ist eingeleitet. https://codimd.puzzle.ch/2JC0gXPwRQWBfg4OgwBpoA#', 1, 'Erkenntnisse und Massnahmen aus der Divisionsrentabilitätsrechnung sind für Mobility abgeleitet', 5, 84, 5, 'NUMBER', 'metric', '2023-09-28 09:24:15.470971', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (172, 0, '2023-09-19 13:57:18.651701', 'Commit: Ansprechendes Ops-Angebot ist veroeffentlicht und kommuniziert
Target: Unser Angebot wird aktiv beworben
Stretch: Neue Kunden tanzen mit uns den Ops-Shuffle oder den gangnam-Ops', 1, 'Most Shiny Ops-Offering in the Hood', 67, 89, 67, 'NUMBER', 'metric', '2023-09-19 13:57:18.651701', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (163, 0, '2023-09-28 12:32:44.712135', 'Commit: 75% alle im Q1 definierten Massnahmen sind umgesetzt; Target: 90% aller definierten Massnahmen sind umgesetzt; Stretch: Der durchschnittliche Stundensatz ist per Ende Nov. auf 155CHF gestiegen.
https://codimd.puzzle.ch/UKbdoHV0Rd69P_oJUFvMgw?both', 1, 'Wir erhöhen die durchschnittlichen Stundensätze um 2 CHF auf 155 CHF', 5, 84, 5, 'NUMBER', 'metric', '2023-09-28 12:32:44.712135', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (193, 0, '2023-09-19 06:57:12.30293', 'Commit: Wir sprechen mit allen MO-Owner und klären im Team das Interesse und unseren konkreten Beiträg.
Target: Roadmap zur Unterstützung mind. einer MO erstellt und mit MO-Owner abgestimmt.
Stretch: Die wichtigste Massnahme aus der Roadmap ist umgesetzt.', 1, 'Wir klären unsere MO-Beiträge und positionieren uns entsprechend', 33, 95, 33, 'NUMBER', 'metric', '2023-09-19 06:57:12.30293', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (190, NULL, '2023-09-18 23:37:55.483772', 'Commit: bester Monat mit 200k Umsatz. Target: bester Monat mit 225k Umsatz. Stretch: Bester Monat mit 250k Umsatz.

Halbmonatliche Messungen werden auf Monatswerte extrapoliert.', 250000, 'Wir knacken die Viertelmillion Monatsumsatz', 33, 88, 33, 'CHF', 'metric', '2023-09-18 23:37:55.483772', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (191, 0, '2023-09-18 23:45:53.76229', 'Für die bis Ende 2023 auslaufenden und für 2024 verlängerten Mandate können wir höhere Stundensätze geltend machen.

Commit: 1% höher. Target: 2.5% höher. Stretch: 4% höher.

Gemessen wird die durchnittliche, nach Vertragsvolumen gewichtete Stundensatzerhöhung.', 4, 'Wir erhöhen die Rates bei bei den verlängerten Mandaten um 4%', 33, 87, 33, 'PERCENT', 'metric', '2023-09-18 23:45:53.76229', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (192, 0, '2023-09-19 00:03:12.680524', 'Commit: Durchführung Ausrichtungsworkshop mit Team und Definition der Themenschwerpunkte.
Target: Massnahmenkatalog zur Erreichung der Entwicklungsziele erstellt und mit Team besprochen.
Stretch: Die Umsetzung der wichtigsten Massnahme ist fertig und konkret geplant und Umsetzung hat begonnen.', 1, 'Wir richten uns nach unseren Entwicklungszielen aus', 33, 95, 33, 'NUMBER', 'metric', '2023-09-19 00:03:12.680524', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (178, 40, '2023-10-30 12:09:43.691939', 'Das GJ22/23 war bei /zh ein starkes Jahr (Ø 73.8% abs. Verrechenbarkeit). Wir wollen dies im Q2 auch unter schwierigeren Vorzeichen (Eintritte und drohenden Auslastungslücken) toppen.

1 Messung [in %] pro Monatshälfte, total 6 Messungen. Commit: Ø 68% (Level von Herbst 2022). Target: Ø 74% (> Ø GJ22/23). Stretch: Ø 82% (Top-Resultat aus Juli 2023)', 82, 'Wir erreichen trotz Eintritten und drohenden Lücken eine höhere abs. Verrechenbarkeit als im Ø GJ22/23', 33, 88, 33, 'PERCENT', 'metric', '2023-10-30 12:09:43.691939', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (194, 0, '2023-09-19 12:02:50.224896', 'Commit (0.3): Die Strategie ist definiert; Target (0.7): Commit + Die Strategie ist intern und extern kommuniziert. Stretch (1.0): Erste Umsetzungen in Hitobito sind erfolgt . Stichdatum 01.01.24. ', 1, 'Die Strategie Rechnungsmodul ist erfolgreich umgesetzt ', 41, 82, 41, 'NUMBER', 'metric', '2023-09-19 12:02:50.224896', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (183, 0, '2023-09-19 13:31:22.14235', 'AV und PL haben das Q1 2024 geplant. Forecast Wert des geplanten Dienstleistungsumsatzes aus PTime für das Q1 2024 (Pauschalen, Partnerumsätze, Subs sind nicht enthalten). Stichdatum: 19.12.23

https://time.puzzle.ch/reports/revenue 01.01.24 - 31.03.24', 225000, 'Unsere Auftragsbücher für das Q1 2024 sind gut gefüllt (Company)', 28, 92, 28, 'CHF', 'metric', '2023-09-19 13:31:22.14235', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (180, 0, '2023-09-19 12:24:41.660073', '* Commit (0.3): Planung & Budget  * Target (0.7): Zwei Issues als RubyOps umgesetzt * Stretch (1.0): implementiert und etabliert * Stichtag 19.12 ', 1, 'Der Prozess von RubyOps ist etabliert', 28, 91, 28, 'NUMBER', 'metric', '2023-09-19 12:24:41.660073', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (187, 0, '2023-10-09 14:28:08.347222', 'Commit: Es ist nicht mehr als 1 internes und kein "externes" Ticket eskaliert Target: Kein Ticket ist eskaliert Stretch: Wir haben keine offenen Tickets mehr, die aelter als 1 Tag sind', 1, 'Keine Tickets mehr im Zammad die Eskalieren', 32, 94, 32, 'NUMBER', 'metric', '2023-10-09 14:28:08.347222', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (179, 0, '2023-09-19 13:30:28.255329', 'Commit (0.3):1 umgesetzt, 2 geplant; Target (0.7): 2 umgesetzt, 1 geplant (1.0): 3 umgesetzt. Stichtag 19.12 ', 1, 'Die Migrationen (Ruby update, Jenkins und Openshift) sind geplant, durchgeführt und learnings aufgenommen und umgesetzt.', 28, 91, 28, 'NUMBER', 'metric', '2023-09-19 13:30:28.255329', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (184, 0, '2023-09-19 13:59:44.352106', '* Commit (0.3): Kandidat welche unsere Anforderungen erfüllt * Target (0.7): Kandidat welcher uns positiv überrascht * Stretch (1.0): Super-Expert * Stichtag: 31.12', 1, 'Wir können eine Anstellung tätigen', 28, 93, 28, 'NUMBER', 'metric', '2023-09-19 13:59:44.352106', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (182, 0, '2023-09-19 14:14:19.372556', 'Commit (0.3): Interne- und Kundenvorhaben sind geplant, Member geplant; Target (0.7): Planung wird eingehalten Stretch (1.0): Zufriedene Members und Kunden. Stichtag 19.12 ', 1, 'Die Interne- und Kundenvorhaben sind gut ausbalanciert', 28, 92, 28, 'NUMBER', 'metric', '2023-09-19 14:14:19.372556', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (162, 0, '2023-10-05 14:38:25.120183', '0.3 (Commit Zone) Die anstehenden Anfragen und Ausschreibungen sind priorisiert und es ist klar, wer den tech. Lead und Sales-Lead pro Anfrage hat. Wir haben mindestens ein Angebot eingereicht. 0.7 (Target Zone) Wir haben  2-3 (abhängig vom Volumen)  Angebote eingereicht. 1.0 (Stretch Goal) Wir gewinnen einen Auftrag.
Fortschritt: https://codimd.puzzle.ch/5ba4-O99T4aI2xkmLpljPA?both', 1, 'Wir gewinnen neue Aufträge für /mobility', 30, 86, 30, 'NUMBER', 'metric', '2023-10-05 14:38:25.120183', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (181, 0, '2023-10-30 09:39:12.394127', 'Commit (0.3): durchschnittlich mindestens 63.0%; Target (0.7): 65.0% Stretch (1.0): 66.5%. Stichtag 19.12
Massgebend ist die Zahl aus PTime (Gesamt Ruby)', 1, 'Wir erreichen eine hervorragende absolute Verrechenbarkeit (Company)', 28, 92, 28, 'NUMBER', 'metric', '2023-10-30 09:39:12.394127', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (199, 0, '2023-09-19 13:19:55.057224', '0.3 (Commit Zone): 13 Members nehmen an mindestens einem Tag der /mid Week teil
0.7 (Target Zone): 17 Members nehmen an mindestens einem Tag der /mid Week teil
1.0 (Stretch Goal): Alle Members, die nicht in den Ferien sind oder im Ausland arbeiten nehmen an mindestens einem Tag der /mid Week teil', 100, 'Wir stärken das Miteinander, das Vertrauen und die Zusammenarbeit untereinander', 31, 98, 31, 'PERCENT', 'metric', '2023-09-19 13:19:55.057224', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (196, 0, '2023-09-19 13:20:14.490916', '0.3 (Commit Zone): Analyse und Schlussfolgerungen aus dem Dagger PoC sind in einem Blog-Beitrag dokumentiert, in dem spezifisch definiert ist, in welchen Szenarien Dagger optimal eingesetzt werden kann.
0.7 (Target Zone): 3 Kunden leads (intern oder extern) sind identifiziert und es wurde ein interner Workshop für mindestens 3 Entwickler durchgeführt, um die Erkenntnisse aus dem PoC zu teilen.
1.0 (Stretch Goal): Ein Lead hat die Offer-Phase im /mid Sales funnel erreicht.', 100, 'Bestimmte Use-Cases für Dagger definieren und gezielte Kundenansprache durchführen', 59, 97, 59, 'PERCENT', 'metric', '2023-09-19 13:20:14.490916', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (159, 0, '2023-11-16 09:34:56.93216', '0.3 (Commit Zone): Eine Durchführung des MLOps Lab als kostenpflichtigen (Pricing ist geklärt) halbtags Kurs ist geplant und erste Anmeldungen (ab 1 Anmeldung) sind eingegangen. Das Bedürfnis für weitere Lab Angebote ist bekannt.
0.7 (Target Zone): Die Mindestanzahl von 4 Teilnehmer ist erreicht.
1.0 (Stretch Goal): Der Kurs ist ausgebucht (8 Teilnehmer)

Fortschritt: https://codimd.puzzle.ch/Hdr00LMXQfut7xPs_E3X9Q', 1, 'Das MLOps Lab Angebot wird ertragsgenerierend etabliert.', 51, 83, 51, 'NUMBER', 'metric', '2023-11-16 09:34:56.93216', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (204, 0, '2023-09-19 13:22:19.627024', 'C
    • Dienstleistungsangebot (aktuell «Ambitionen») /dev/tre ist diskutiert und verfasst
    • Eine treffendere Bezeichnung für «Ambitionen» ist gefunden auf der d3-Wiki-Seite notiert
    • “Wie wir arbeiten" ist diskutiert und verfasst
    • Eine treffendere Bezeichnung für “Wie wir arbeiten" ist gefunden auf der d3-Wiki-Seite notiert
T
    • Zwei weitere Blog-Artikel aus der Frontend-Serie ist publiziert (Output)
    • Ein Teaser-Text für das Teilen der Blog-Artikel und der Referenz-Stories BKD/ Moser-Baer auf LinkedIn ist erfasst
    • Der Blog-Artikel und die Referenz-Storis sind auf LinkedIn inkl. Teaser geteilt
S
    • Nutzungszahlen aus dem ersten Blog-Artikel (Lit) und den beiden Referenzzahlen, sind im Team besprochen und für zukünftige Massnahmen eingeordent (Outcome)
    • Ein erster Ideenkatalog «Networking d3 an Events» ist erstellt.
', 1, 'Es ist bekannt, dass devtre für Individual-Software-Entwicklung steht', 4, 100, 4, 'NUMBER', 'metric', '2023-09-19 13:22:19.627024', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (205, 0, '2023-09-19 13:23:42.512911', 'C
    • Wir haben definierte Outputs/ -comes aus den /dev/tre-Team-Meetings und des /dev/tre-Workshops
T
    • Output und -Comes aus den Team-Gefässen sind dokumentiert
    • Konkrete Massnahmen sind niedergeschrieben
S
    • Konkrete Massnahmen sind gestartet
    • Eine konkrete Massnahme ist umgesetzt
', 1, 'Wir haben die  regelmässigen devtre Team-Gefässe durchgeführt, deren Qualität gesteigert und Erkenntnisse dazu gesammlt', 4, 101, 4, 'NUMBER', 'metric', '2023-09-19 13:23:42.512911', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (206, 0, '2023-09-19 13:24:10.480212', 'C
    • Wir haben die zentralen Themen einer Selbstorganisation be- und verhandelt und niedergeschrieben
    • Wir klären die Frage mit welchem Instrument wir uns organisieren «Gurkensalat-Board"
T
    • Ein Briefing für ein externes Coaching ist erfasst. Dies basiert auf den zentralen Themen der SO.
    • Ein externes Coaching für den BL & und das Team, um die selbstorganisation zu verbessern ist organisiert.
S
    • Ein erster Report "Get things done" aus dem Gurkensalat-Board besteht,  als Beweis, dass Get Things Done besser geworden  ist (aber wie reporten??)
', 1, 'Wir leben die Selbstorganisation bewusster hinsichtlich Prioritäten und Terminen und get things done', 4, 101, 4, 'NUMBER', 'metric', '2023-09-19 13:24:10.480212', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (202, 0, '2023-09-26 06:24:46.06946', 'C
    • «Technologie pro Auftrag» aktualisiert und verabschiedet
    • «Tech-Map d3» aktualisiert und verabschiedet
T
    • «Techradar d3» erstellt
    • «Tech...»-Elemente d3 diskutiert und Bereich gültiges Statement verfasst
S
    • Statement kommuniziert
    • Relation technologische Ausrichtung d3 zu «Marktopportunität ‹New Tech›»  diskutiert
', 1, 'Wir kennen unsere technologische Ausrichtung', 4, 100, 4, 'NUMBER', 'metric', '2023-09-26 06:24:46.06946', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (200, 0, '2023-09-19 18:50:22.557881', '(Commit Zone): Für das Q3 sind mindestens 50% der verrechenbaren Pensen fest eingeplant.
(Target Zone): Für das Q3 sind mindestens 60% der verrechenbaren Pensen fest eingeplant.
Stretch Goal): Für das Q3 sind mindestens 70% der verrechenbaren Pensen fest eingeplant.', 100, 'Die Members-Planung für das nächste Quartal ist zu einem Grossteil sichergestellt.', 60, 99, 60, 'PERCENT', 'metric', '2023-09-19 18:50:22.557881', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1034, 12.45, '2024-01-10 12:25:28.174336', 'Messung: Die FTE beziehen sich auf jene in der “Membersliste”. Die Messungen erfolgen jeweils Mitte Monat (Summe der sofort + nächster Monat verfügbaren Members ohne okr_pitcDE). Stichtag ist 19.03.24', 2.9, 'Wir halten die freie Kapazität gemäss Memberliste auf unter 3 FTE', 13, 1004, 13, 'FTE', 'metric', '2023-12-18 11:56:13.437364', NULL, NULL, NULL, 2);
INSERT INTO okr_pitc.key_result VALUES (208, 0, '2023-09-19 13:31:51.726735', 'C (2), T, S (7)
    • Mobi: Budget für Lifecycle und Cloud-Enabling Themen (Gaia-Team) ist bekannt und gesprochen
    • Swisscom: Budget für cADC Umsetzungsauftrag und Wartung-&-Support-SLA ist bekannt und gesprochenInserat überarbeitet und publiziert
    • Bundesarchiv: SAP HANA Auftrag ist gestartet
    • Bundesarchiv: Empfehlungen für Weiterentwicklungen  SIARD-Suite sind dokumentiert und kommuniziert
    • Swiss Olympic: Aufbau und komplexität unserer entwickelten Individual-Lösung ist dokumentiert und kommuniziert
    • Swiss Olympic, Weiterentwicklung: Ideation- und Konzept-Workshop wurde durchgeführt
    • Bundesarchiv: Weiterentwicklungen sind bestellt
', 1, 'Die Aufträge von bestehenden Kunden sind für 2024 verlängert', 4, 102, 4, 'NUMBER', 'metric', '2023-09-19 13:31:51.726735', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (209, 0, '2023-09-19 13:32:19.958787', 'C
    • Zwei Neuaufträge sind identifiziert, Bedürfnisse und Anforderungen sind abgeklärt
T
    • Offerte inkl. Lösungsbeschrieb für zwei Neuaufträge sind erstellt und versendet
S
    • Wir haben die Zuschläge für zwei Neuaufträge erhalten
', 1, 'Wir haben zwei neue Aufträge für 2024 gewonnen', 4, 102, 4, 'NUMBER', 'metric', '2023-09-19 13:32:19.958787', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (211, 0, '2023-10-18 13:03:55.971041', '(Commit Zone): Ein Drittel (30%) der Jenkins Pipelines sind erfolgreich migriert, archiviert oder gelöscht. (Target Zone): Ein Grossteil (70%) der Jenkins Pipelines sind migriert, archiviert oder gelöscht. (Stretch Goal): Alle (100%) Jenkins Pipelines sind migriert, archiviert oder gelöscht.', 100, 'Wir bearbeiten einen Grossteil der Jenkins-Projekte', 31, 106, 31, 'PERCENT', 'metric', '2023-10-18 13:03:55.971041', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (135, 0, '2023-09-19 13:58:38.259038', 'Commit (0.3): 3 der unten stehenden Ziele sind erreicht.
Target (0.7): 5 der unten stehenden Ziele sind erreicht.
Stretch (1.0): 8 der unten stehenden Ziele sind erreicht.

Für Cilium ist ein Konzept vorhanden für welche Use-Cases Cilium sinnvoll ist.
Für Cilium sind erste Offerings erstellt.
Für Dagger sind die ersten Offerings generiert.
Für Dagger sind erste Offerings erstellt.
Für Grafana OnCall haben wir die Evaluationsphase abgeschlossen.
Für Grafana sind Teams von jeweils 3 Members ausgebildet.
Für Grafana OnCall haben wir Umsätze bei Kunden generiert.
Für Event Driven Ansible haben wir die Evaluationsphase abgeschlossen.
Für EDA sind Teams von jeweils 3 Members ausgebildet.
Für Event Driven Ansible haben wir Kundenumsätze generiert.
Für ML Ops haben wir das Consulting und Schulungsangebot definiert.
Für ML Ops haben wir eine erste Schulung kostenpflichtig durchgeführt.
Für ML Ops haben wir ersten Kundenumsatz generiert.



Stichtag 19.12.', 1, 'New Tech: Wir haben für Cilium, Dagger und ML Ops Consulting-Teams und bereiten Event Driven Ansible und Grafana OnCall als neue Themen vor.', 24, 76, 24, 'NUMBER', 'metric', '2023-09-19 13:58:38.259038', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (213, 0, '2023-09-20 13:52:58.370375', '0.3 (Commit Zone): Jenkins läuft auf der neuen Umgebung (OpenShift); 0.7 (Target Zone): Jenkins läuft einwandfrei und ist Teils gut dokumentiert; 1.0 (Stretch Goal): Jenkins ist durchautomatisiert und vollständig dokumentiert. ', 100, 'Wir beschreiben und stellen den neuen Jenkins umfassend', 78, 106, 78, 'PERCENT', 'metric', '2023-09-20 13:52:58.370375', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (217, 49, '2023-12-12 12:42:09.782475', 'Commit = 51% / Target = 52% / Stretch = 53%
Baseline (Q3: 49%)', 52, 'Wir haben eine hervorragende absolute Verrechenbarkeit', 36, 104, 36, 'PERCENT', 'metric', '2023-09-25 08:29:24.018451', NULL, NULL, NULL, 4);
INSERT INTO okr_pitc.key_result VALUES (1225, NULL, NULL, '', NULL, 'Wir steigern und behalten die absolute Verrechenbarkeit auf 75%', 4, 1064, 4, NULL, 'ordinal', '2024-03-19 13:25:49.176129', '70%', '75%', '80%', 0);
INSERT INTO okr_pitc.key_result VALUES (1227, NULL, NULL, '', NULL, 'Wir bringen die Auftragslage für Q1, GJ24/25 ans Trockene (Juli-Sep 2024)', 4, 1064, 4, NULL, 'ordinal', '2024-03-19 13:26:40.990644', 'Aufträge identifiziert
', 'Offerten rausgelassen', 'Mind. 1 Vertrag abgeschlossen', 0);
INSERT INTO okr_pitc.key_result VALUES (219, 0, '2023-09-25 09:48:42.542519', '0.3 (Commit Zone): Das Zufriedenheitsniveau bei /mid beträgt im Durchschnitt 7,3 Punkte; 0.7 (Target Zone): Das Zufriedenheitsniveau bei /mid beträgt im Durchschnitt 7,7 Punkte; (Stretch Goal): Das Zufriedenheitsniveau bei /mid beträgt im Durchschnitt 8,0 Punkte;

Die Zufriedenheit wird monatlich anhand der Happiness-Umfrage gemessen. Als Massnahme sollen bezüglich der Kommunikation nicht relevante Informationen weiterhin minimiert und relevante Informationen maximiert werden. Zusätzlich wird weiterhin Feedback von den Members eingeholt und es wird versucht, diese einzubinden.', 100, 'Wir arbeiten weiter an der Kommunikation und Informationstransparenz bei /mid', 31, 98, 31, 'PERCENT', 'metric', '2023-09-25 09:48:42.542519', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (220, 20000, '2023-09-28 14:12:49.028692', 'Gemeint ist die Auftragssumme von in GJ 23/24-Q2 eingehenden Aufträgen, die sich auf eine Umsetzung in GJ 23/24-Q3 beziehen (längerfristige Aufträge ggf. anteilig berücksichtigen).', 40000, 'Beauftragungen (Auftragssumme) für GJ 23/24-Q3 bei nicht Red Hat-Kunden liegt über 40 TEUR', 17, 109, 17, 'CHF', 'metric', '2023-09-28 14:12:49.028692', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (221, 0, '2023-09-28 14:21:24.249499', 'Direktkunden: Nicht (Partner)unternehmen, über die Abrechnung erfolgt.
Zu wievielen Direktkunden hat der Vertrieb Kontakt aufgenommen?
Anzahl der Kunden, die auf unsere Vetriebsanfragen positiv reagiert haben.
Commit: Target:  Stretch:', 8, 'Vertriebstreffen mit Direktkunden', 17, 109, 17, 'NUMBER', 'metric', '2023-09-28 14:21:24.249499', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (210, 8.38, '2023-10-17 07:25:45.690068', 'Wir definieren einen Massnahmenplan, Priorisieren und setzen Massnahmen um die Identifikation mit Puzzle und den Grundsätzen zu verbessern. Messwert ist die Zufriedenheit mit Puzzle aus den Membersumfragen (2-Wöchentlich)', 9, 'We are in Love', 3, 105, 3, 'NUMBER', 'metric', '2023-10-17 07:25:45.690068', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1222, NULL, '2024-03-19 13:57:07.99654', 'Inklusive Ownership der entsprechenden KR', NULL, 'Team hilft mit OKR zu definieren für Q1 2024/2025', 40, 1081, 40, NULL, 'ordinal', '2024-03-19 13:17:55.123749', '1 KR', '2 KR', '1 O inkl. min. 3 KR', 1);
INSERT INTO okr_pitc.key_result VALUES (1063, NULL, '2023-12-19 13:16:42.675633', '', NULL, 'Cloud-Marktangebot von /mid lanciert', 40, 1026, 40, NULL, 'ordinal', '2023-12-19 13:16:27.644394', 'Das neue Cloud-Marktangebot von /mid ist definiert', 'Das neue Cloud-Marktangebot von /mid ist auf der Webseite publiziert.', 'Erste Kundenanfragen auf Grund des neuen Marktangebots sind eingetroffen.', 1);
INSERT INTO okr_pitc.key_result VALUES (1065, NULL, '2023-12-19 13:53:38.460686', '', NULL, 'Wir generieren drei zusätzliche Leads für 3rd Level Support Verträge.', 24, 1005, 24, NULL, 'ordinal', '2023-12-18 11:55:24.518337', 'Wir generieren zwei zusätzlichen Lead für einen 3rd Level Support Vertrag.', 'Wir generieren 3 zusätzliche Leads für 3rd Level Support Verträge und schliessen einen Vertrag ab.', 'Wir gewinnen zwei neuen 3rd Level Support Vertrag.', 3);
INSERT INTO okr_pitc.key_result VALUES (1173, 0, NULL, 'Falls mehr als 3 Teamevents stattfinden, zählen die 3 meistbesuchten.
Teilnehmerzahl wird kumuliert, deshalb ist das Stretchgoal 3*10=30', 30, '3 Teamevents mit je 10 Teilnehmenden (es zählen auch team-externe Members)', 33, 1074, 33, 'NUMBER', 'metric', '2024-03-19 10:12:14.287478', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1084, NULL, '2024-01-09 14:41:27.422602', '', NULL, '/dev/tre Members besuchen gezielt Events für "Networking- und Partnerpflege" und berichten darüber', 4, 1014, 4, NULL, 'ordinal', '2023-12-19 14:23:09.070338', 'Plan und Übersicht vorhanden und besprochen. Sinn & Zweck klar', 'Total mindestens 5 Events im Flow-Board abgebildet, besucht und darüber berichtet', 'Es hat sich mindestens eine konkrete Anfrage ergeben', 2);
INSERT INTO okr_pitc.key_result VALUES (1211, NULL, '2024-03-19 13:09:21.101073', 'Wir wollen das aktuelle Marktangebot ausweiten und publizieren. Den Inhalt und die Form erarbeiten wir zusammen mit Markom.', NULL, 'Cloud-Marktangebot von /mid ausgeweitet und definiert', 40, 1080, 40, NULL, 'ordinal', '2024-03-19 13:08:31.245033', 'Inhalte für die neue Webseite ist erstellt', 'Bereit für Publishing auf der neuen Webseite', 'Inhalt ist auf der neuen Webseite publiziert', 4);
INSERT INTO okr_pitc.key_result VALUES (1185, NULL, NULL, '', NULL, 'Planung zu ISO 27k1 ist soweit, dass GL go/no-go entscheiden kann.', 27, 1071, 27, NULL, 'ordinal', '2024-03-19 12:01:06.519853', 'Notwendige Schritte sind dokumentiert.', 'Prozess ist aufgegleist und bei GL deponiert.', 'Zertifizierungsprozess ist angelaufen.', 0);
INSERT INTO okr_pitc.key_result VALUES (1215, NULL, NULL, '', NULL, 'Wir pimpen unser Visualisierungsworkshop-Angebot und führen einen Event mit zahlenden Teilnehmern durch', 36, 1082, 36, NULL, 'ordinal', '2024-03-19 13:13:59.057687', 'Unser überarbeitetes und verbessertes Angebot ist publiziert', 'Wir erreichen potenzielle Teilnehmer und haben für ein konkretes Datum mindestens 4 bestätigte Teilnehmer', 'Wir haben mindestens einen Visualiserungsworkshop mit 4 zahlenden Teilnehmer durchgeführt', 0);
INSERT INTO okr_pitc.key_result VALUES (1218, NULL, NULL, '', NULL, 'Wir finalisieren unsere Bereichsstrategie', 36, 1084, 36, NULL, 'ordinal', '2024-03-19 13:16:15.130716', 'Unsere Bereichsstrategie ist finalisiert und mit dem Team und unserem SUM Buddy besprochen', 'Bereichsstrategie ist mit Feedback von Team und GL erweitert und Massnahmeplan zur Umsetzung ist erstellt', 'Bereichsstrategie ist ready und einzelne Massnahmen wurden umgesetzt', 0);
INSERT INTO okr_pitc.key_result VALUES (1192, NULL, '2024-03-19 13:15:54.102868', 'Ziel ist es, Awareness hoch zu halten, und den Devs potenziell wichtiges Wissen zu vermitteln. Das Stretch Goal würde das dann auch gegen aussen präsentieren, um für Puzzle Werbung zu machen.', NULL, 'Wir machen Puzzle sicherer, in dem wir Security-Wissen teilen.', 27, 1072, 27, NULL, 'ordinal', '2024-03-19 12:20:00.545823', 'Wir haben ein Tech-Kafi oder einen Blogpost zu Security-Themen organisiert.', 'Wir haben zwei solche Aktivitäten organisiert.', 'Wir durften das bei einem Kunden oder an einem externen Event präsentieren.', 3);
INSERT INTO okr_pitc.key_result VALUES (1199, 0, '2024-03-19 14:08:12.960427', 'Metrische Messung gemäss Action Plan Items - Wir betrachten unser Manifest in allem was wir tun und überlegen uns dabei, tun wir es im Namen/ Rahmen des Manifests? So wollen wir gewährleisten, dass das Manifest bewusst wahrgenommen wird und das dessen Inhalt keine "heisse Luft" ist.', 5, 'Wir verankern unser Manifest und leben danach', 4, 1062, 4, 'NUMBER', 'metric', '2024-03-19 12:33:46.200824', NULL, NULL, NULL, 2);
INSERT INTO okr_pitc.key_result VALUES (1220, NULL, '2024-04-22 09:44:15.953885', '', NULL, 'Wir formulieren unsere Anforderung/Ansprüche bzgl. “Digitale Lösungen” ', 36, 1084, 36, NULL, 'ordinal', '2024-03-19 13:17:27.386995', 'Workshop und Verständnis schaffen, Einordnung «Individual-Software-Entwicklung» in dieses Thema', 'Formulierung für /ux erstellen - Mit /devtre, /mobility & /ruby abgeglichen - Gemeinsame Anforderung formulieren', 'An Markom und im Leadership-Team präsentiert', 4);
INSERT INTO okr_pitc.key_result VALUES (1139, NULL, '2024-03-28 17:05:33.135138', '', NULL, 'Bis Ende Q4 GJ 23/24 ist die Lehrlingsausbildung im Java Bereich fürs neue Semester organisiert.', 29, 1055, 20, NULL, 'ordinal', '2024-03-07 14:51:27.444282', 'Betreuungspersonen und  Projekte fürs erste Quartal sind definiert.', 'Fürs erste Semester sind Projekte und Verantwortlichkeiten   definiert und Ressourcen verbindlich eingeplant.', '4.Lj-Lernender ist auf Kundenprojekt für mind. 3 Monate eingeplant.', 6);
INSERT INTO okr_pitc.key_result VALUES (1233, 0, '2024-04-23 15:19:22.026003', 'Jeder Input, jeder Wunsch, jeder Verbesserungsvorschlag aus den MAGs ist entweder erfüllt, adressiert oder, falls beides nicht möglich, mit Absender besprochen sein. In letzterem Fall muss Vorgehen definiert und dem Member kommuniziert sein.

Liste von total 29 Inputs und Verbesserungsvorschlägen, die abgearbeitet wird (Zielerreichungsgrad linear)', 29, 'MAG-Inputs zu 100% verarbeitet', 33, 1074, 33, 'NUMBER', 'metric', '2024-03-25 11:31:02.37229', NULL, NULL, NULL, 2);
INSERT INTO okr_pitc.key_result VALUES (1235, NULL, '2024-04-23 15:42:06.014988', 'In Ausrichtigungsworkshops (ggf. verschiedene Workshops mit unterschiedlichen Interessengruppen) definieren, wie die mittelfristige Stossrichtung der Division /zh aussieht.

Wichtig dabei ist die Positionierung im kompetitiven (Java-)Softwareengineering und im traditionellen Linux System Engineering (mittelfristig weniger VMs, mehr Cloud, mehr SaaS).', NULL, 'Divisionstrategie 2026 entworfen.', 33, 1074, 33, NULL, 'ordinal', '2024-03-25 12:04:57.249721', 'Interessen des Teams in facilitierten Workshops (ggf. mehrere) erhoben und zusammen mit Team konsolidiert.', 'Commit plus Überführung der Ausrichtungsziele in eine Strategie, welche die Interessen von Puzzle, der Members und der Division zusammenbringt. Inkl. Massnahmenkatalog und grobem Ausbildungskonzept.', 'Target + Vernehmlassung und Approval der GL', 1);
INSERT INTO okr_pitc.key_result VALUES (1163, 250000, '2024-04-23 16:22:05.8037', 'Eingecheckt wird der kumulierte Durchnitt der monatlichen Umsätze (März-Mai, inkl. Sublieferanten) gemäss Divisionsrentabilitätsrechnung. Wenn der Durchschnitt am Schluss 296''296.- erreicht, ist das Stretch Ziel erreicht (3*296296=888888)', 296296, 'Wir erreichen einen Quartalsumsatz von CHF 888''888.- (durchschnittl. knapp TCHF 300 pro Mt.)', 33, 1065, 33, 'CHF', 'metric', '2024-03-19 06:37:37.288324', NULL, NULL, NULL, 3);
INSERT INTO okr_pitc.key_result VALUES (1460, 1, '2024-09-23 12:17:03.433915', 'Unsere Members generieren 5 neue Leads bei bestehenden oder neuen Kunden. Stretch Goal ist 7 neue Leads.', 7, 'Unsere Members generieren 5 Leads', 40, 1155, 40, 'NUMBER', 'metric', '2024-09-17 06:29:02.93471', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1275, 0, '2024-07-04 07:18:47.8253', '1 weiterer /mobility Member ist entweder auf GO oder Delivery Pipeline/Kubernetes Auftrag im Einsatz als Target (= 0.8FTE). Als Stretch zweiter Member. Ein Auftrag zählt wenn für mindestens 3 Monate Dauer geplant.', 1.1, 'Mit CI/CD sind weitere Kooperationspotentiale identifiziert und ein weiterer /mobility Member im Einsatz', 5, 1095, 5, 'FTE', 'metric', '2024-06-13 14:33:34.242254', NULL, NULL, NULL, 7);
INSERT INTO okr_pitc.key_result VALUES (1281, NULL, '2024-08-20 07:21:13.026833', '', NULL, 'SEO - Wir erhöhen die organische Suchsichtbarkeit der neuen Website. ', 26, 1103, 26, NULL, 'ordinal', '2024-06-17 11:10:35.044833', 'Wir optimieren die Website-Inhalte und die Meta-Tags, um die Platzierung in den organischen Suchergebnissen zu verbessern. ', 'Wir erhöhen den SEO-Score um 6 Punkte. (Score Ende Juni: 78)', 'Wir erhöhen den SEO-Score um 9 Punkte.', 1);
INSERT INTO okr_pitc.key_result VALUES (1472, NULL, '2024-09-30 09:05:02.243047', 'Falls der Ansatz nicht erfolgsversprechend ist, wird bei Commit-Zone abgebrochen.', NULL, 'Angebot UX / Platform Engineering etabliert', 40, 1154, 31, NULL, 'ordinal', '2024-09-23 12:53:44.432686', 'Die Idee und Zusammenarbeit ist getestet. (Workshop durchgeführt, internes Research Projekt abgeschlossen und Blogbeitrag über Key Findings geschrieben)', 'Ein Angebot ist definiert, Rollen sind klar verteilt.', '2 weitere Workshops sind verkauft inkl. UX Stunden', 1);
INSERT INTO okr_pitc.key_result VALUES (1253, 0, '2024-06-12 09:51:44.163327', 'Der übrige Betriebsaufwand (Miete, Ausgaben für Marketing- und Kommunikations-Aktionen, IT-Betrieb, Infrastruktur, Verwaltungsaufwand) konnte im Vergleich zum vorherigen Quartal um 3% reduziert werden.', 4.3, 'Der übrige Betriebsaufwand konnte um 3% reduziert werden.
', 22, 1090, 22, 'PERCENT', 'metric', '2024-06-04 13:21:10.641733', NULL, NULL, NULL, 3);
INSERT INTO okr_pitc.key_result VALUES (1266, NULL, '2024-06-18 12:49:12.245197', '', NULL, 'Salesunterstützung für /mobility führt zu neuen Aufträgen', 20, 1097, 20, NULL, 'ordinal', '2024-06-13 14:41:56.193694', 'Die Zusammenarbeit zwischen /sales und /mobility ist mit neuem CSO verifiziert und bei Bedarf angepasst.', 'Die koordinierte Salesunterstützung führt zu neuen Leads. Wir haben für alle /mobility Members verrechenbare Aufträge  im Q2.', 'Durch gezieltes strategisches Pricing gewinnen wir neue Aufträge (inkl. Rahmenverträge). Erreicht wenn Aufträge/ Rahmenverträge mit mind. CHF 300k Auftragsvolumen gewonnen.', 4);
INSERT INTO okr_pitc.key_result VALUES (1279, NULL, NULL, '', NULL, 'Social Media – Wir erstellen einen Prompt für LinkedIn-Posts auf ChatGPT in unserem Tone of Voice.  ', 26, 1103, 26, NULL, 'ordinal', '2024-06-17 11:09:29.453559', 'Prompt konzipiert und neue ChatGPT-Instanz erstellt.  ', 'Die Anwendung des Prompts ist im Team verankert und bei Daily Tasks im Einsatz. ', 'Technische LinkedIn-Posts erreichen 10% mehr Impressions im Vergleich zum vorderen Quartal.', 0);
INSERT INTO okr_pitc.key_result VALUES (1280, NULL, NULL, '', NULL, 'Employer Branding - Wir erweitern die Jobsseite mit Videoinhalten.    ', 26, 1103, 26, NULL, 'ordinal', '2024-06-17 11:10:03.541109', 'Wir erstellen ein Video-Konzept, für eine mögliche Ausschreibung und planen dies gemeinsam mit dem HR/Personalmarketing ein. ', '1 Stellenausschreibung mit Videocontent ist online.  ', '2 Stellenausschreibungen mit Videocontent ist online.', 0);
INSERT INTO okr_pitc.key_result VALUES (1294, NULL, '2024-06-19 07:25:28.760176', '', NULL, 'Wir haben ein Konzept, wie die absolute Verrechenbarkeit nachhaltig auf 75% steigern', 4, 1106, 4, NULL, 'ordinal', '2024-06-17 12:09:14.259086', 'Zieldefinition von Marcel aufnehmen
Hypothesen sind geschrieben und validiert
Hypothesen sind bewertet.
Prüfen, ob die absolute Verrechenbarkeit wirklich der "Taktgeber" von d3 ist.', 'Massnahmekatalog ist basierend auf Hypothesen erarbeitet
Massnahmen sind auf einen 2-3 Jahres Zeitplan gelegt
Es ist definiert, wie die Verrechenbarkeit zukünftig in OKR geführt werden soll', 'Massnahmen und Roadmap sind mit GL-Body besprochen und validiert sowie dem LST vorgestellt', 4);
INSERT INTO okr_pitc.key_result VALUES (1259, 0, '2024-06-18 12:24:29.410823', 'Vertragsunterzeichnung, nicht schon gestartet. ', 1, 'Durch angemessene Rekrutierungsbemühungen erreicht /mobility bis Ende Q1 GJ 24/25 ein Nettowachstum von 0.8 verrechenbaren FTE. Ausgangswert: 30.06.2024', 20, 1095, 20, 'FTE', 'metric', '2024-06-13 14:11:25.238775', NULL, NULL, NULL, 3);
INSERT INTO okr_pitc.key_result VALUES (1273, NULL, '2024-06-18 12:30:44.274657', '(Wir leisten die vordefnierten Stunden, aber der SAC bestimtm der Inhalt)

MVP (Minimal viable product) MMP (Minimal Marketable product) ist erfolgreich implementiert und der SAC ist äusserst zufrieden ', NULL, 'Wir  sind mit dem SAC Portal 1.0 erfolgreich und halten Meilensteine ein
', 41, 1100, 41, NULL, 'ordinal', '2024-06-17 06:46:15.178652', 'MVP, Machbarkeit bestätigt ', 'MMP Feature complete ', 'Ende Nachkorrekturen ', 3);
INSERT INTO okr_pitc.key_result VALUES (1465, 30, '2024-10-22 12:37:49.084968', 'Anhand nachstehender Massnahmen sorgen wir für einen guten Start ins neue Jahr.
- Verlängerungen fixen
- Neue Deals gewinnen

Die 65% werden nach unserem Division-Planungs-Sheet berechnet. Das Sheet wurde von Anna am 26.8. im LST Monthly präsentiert.', 80, 'Das /mid Team ist JAN-MÄR zu mindestens 65% in Aufträgen eingeplant.', 31, 1155, 60, 'PERCENT', 'metric', '2024-09-24 06:13:42.227891', NULL, NULL, NULL, 5);
INSERT INTO okr_pitc.key_result VALUES (1426, 25, '2024-09-17 12:59:05.990918', 'Basis ist die Auslastung von P.M. gemäss PuzzleTime-Planung.', 100, 'Auslastung des neuen System Engineers sicherstellen', 33, 1150, 33, 'PERCENT', 'metric', '2024-09-16 22:05:40.274939', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1274, NULL, '2024-06-17 06:48:26.640149', 'Unser Ziel ist es das Zertifikat ISO 14001 bis im Herbst zur nächsten Lieferantenberuteilung dre SBB vorweisen zu können', NULL, 'Wir bestehen das externe Audit vom 19.-20. August und erhalten die ISO 14001 Zertifizierung.', 41, 1101, 41, NULL, 'ordinal', '2024-06-17 06:46:21.36493', '-', 'Audit bestanden ', 'keine Abweichnung', 1);
INSERT INTO okr_pitc.key_result VALUES (1261, NULL, '2024-07-18 09:37:41.94327', 'Die strategischen Massnahmen 3 ("konsequentes Auftrags-  & Change-Management anwenden") und 4 ("Den effektiven Aufwand Stunden für Pauschalaufträge reduzieren" werden umgesezt. Dass nicht alle Support und Wartungsaufträge neu geführt werden können, wird im Konzept berücksichtigt.', NULL, 'Durch konsequenteres Auftragsmanagement sind die Aufwände für Support- & Wartungsaufträge reduziert', 5, 1095, 5, NULL, 'ordinal', '2024-06-13 14:21:26.42174', 'Konzept ist erstellt für die beide strategischen Massnahmen 3 & 4, dokumentiert und die Auftragsverantwortlichen sind  geschult', 'Alle Support und Wartungsaufträge werden nach dem neuen Konzept geführt.', 'Eine Wirkung von -6 kCHF Mehraufwand im Quartal ist bereits erzielt (entspricht der Hälfte des Nice Case beide Massnahmen zusammengenommen)', 6);
INSERT INTO okr_pitc.key_result VALUES (1300, NULL, '2024-06-21 11:59:17.919156', 'Wir haben passende Rollen und sind mit der Perspektive glücklich.', NULL, 'Rollen und Entwicklungsplan/Perspektiven sind geklärt', 28, 1108, 28, NULL, 'ordinal', '2024-06-17 19:54:27.127465', 'Aktuelle Rolle ist mit den Members in den ZG besprochen und Entwicklungswünsche sind aufgenommen.', 'Aus den ZG ist ein Entwicklungsplan, wo gewünscht vorhanden und mit den jeweiligen Members abgesprochen.', 'Konkreter Plan für die Entwicklung von allen Members vorhanden', 2);
INSERT INTO okr_pitc.key_result VALUES (1270, NULL, '2024-06-18 13:55:59.148846', 'Unsere Wartungskosten sind nicht gedeckt. es wird deshalb ein Konzept erstellt und anschliessend neue Verträge SLA ausgearbeitet und verhandelt', NULL, 'wir erhöhen den Ertrag der Pauschalen (Betrieb, Wartung und Support)
Jahresziel 2025 -> Rentabilität', 41, 1099, 41, NULL, 'ordinal', '2024-06-17 06:46:02.083382', 'Analyse der Umgebungs komplexität estellt ', 'Wartungskosten pro Kunde sind definiert ', '1. Fassung SLA erarbeitet ', 4);
INSERT INTO okr_pitc.key_result VALUES (1295, NULL, '2024-06-18 13:01:14.551329', '', NULL, 'Wir stellen einen zusätzlichen, geeigneten Software-Engineer an.', 4, 1106, 4, NULL, 'ordinal', '2024-06-17 12:09:35.093312', 'Inserat ist finalisiert und freigeschaltet', 'Bewerbungen und Gespräche finden statt', 'Ein Vertrag ist abgeschlossen', 1);
INSERT INTO okr_pitc.key_result VALUES (1245, NULL, '2024-06-04 10:04:42.439976', '', NULL, 'Der OrgDev Branch ist mit genügend und den passenden Members besetzt.', 13, 1092, 13, NULL, 'ordinal', '2024-06-04 09:15:13.402123', 'Wir finden neue Branch Members.', 'Wir finden eine neue Branch Owner:in.', 'Alle Branch Rollen sind besetzt und Members, Division Owner, Member Coach und GL sind im Branch vertreten.', 4);
INSERT INTO okr_pitc.key_result VALUES (1299, NULL, '2024-06-17 19:49:55.286637', 'Applikationen: socialweb, hitobito (ev pro Instanz), BAFU SAM, swissunihockey, decidim (ev pro Instanz), Brixel, PuzzleTime, Skills, Cryptopus
', NULL, 'Wir haben ein Application Lifecycle Management für unsere Applikationen', 28, 1107, 28, NULL, 'ordinal', '2024-06-17 19:47:22.556209', 'Erster Entwurf für ALM pro Applikation ', 'ALM ist pro Applikation bekannt ', 'Application Lifecycle Management ist mit allen Applikationen definiert und etabliert ', 1);
INSERT INTO okr_pitc.key_result VALUES (1312, 0, '2024-07-11 08:22:53.420412', 'Bei einem Maximalumsatz von 1.115 Mio pro Jahr erreichen wir 300k pro Quartal. Mit der Kurzfristigkeit (Jahreswechsel, Consulting), schätzen wir optimistisch 80% bereits zu Anfang planbar im PTime zu haben (Target: 187''500, Commit: 137''500)', 250000, 'Wir freuen uns über volle Auftragsbücher bis über den Jahreswechsel hinaus', 36, 1112, 36, 'CHF', 'metric', '2024-06-18 11:57:43.264306', NULL, NULL, NULL, 8);
INSERT INTO okr_pitc.key_result VALUES (1243, NULL, '2024-06-18 08:57:21.501447', '', NULL, 'MO AI/ML
Das Kernteam AI/ML hat sich gefunden und hat erste Resultate erzielt.', 24, 1091, 24, NULL, 'ordinal', '2024-06-04 09:11:46.501437', 'Das AI/ML Kernteam hat aktiv Zeit in das Thema investiert.', 'Erweitertes Training (Kombi: ML für Engineers und ML Ops Grundlagen) mit acend und Uni Bern ist aufgesetzt und fertig vorbereitet.', 'Erster bezahlter Auftrag ist gewonnen.
', 4);
INSERT INTO okr_pitc.key_result VALUES (1283, NULL, '2024-06-18 12:29:17.811568', '', NULL, 'Wir wissen, wer für Talks zu welchen Themen zur Verfügung steht, haben diese Information dokumentiert und evaluieren Speaking Opportunities. ', 26, 1104, 26, NULL, 'ordinal', '2024-06-17 11:13:06.922508', 'Dokumentation von mindestens 2 Members und ihren spezifischen Themenbereichen für Talks.', 'Dokumentation von mindestens 4 Members und ihren spezifischen Themenbereichen für Talks.', '2 Speaking Opportunities fürs GJ 24/25 ', 3);
INSERT INTO okr_pitc.key_result VALUES (1292, NULL, '2024-06-18 12:43:29.167228', 'Marktopportunitäten GJ 24/25 (Codi): https://codimd.puzzle.ch/0-eK0IubTRKlbTvSvAtwxQ', NULL, 'Wir klären in welchen Marktopportunitäten wir aktiv sein könnten', 4, 1105, 4, NULL, 'ordinal', '2024-06-17 12:08:11.833769', '- Die MOs GJ 24/24 sind studiert und hinsichtlich Unterstützung durch d3 validiert
', '- Falls Unterstützung durch d3 möglich, ist diese Niedergeschriebnen,', '- Die d3-Mitwirkung an dem MOs ist kommuniziert an/ besprochen mit:
- Sales (Yup)
- GL Coach (Dänu)', 1);
INSERT INTO okr_pitc.key_result VALUES (1247, NULL, '2024-06-18 12:53:45.322692', '', NULL, 'Die Rahmenbedingungen und Ziele für den nächsten Entwicklungsschritt unserer Organisation verabschiedet und die Erwartungen der Divisions an die Organisation der Zukunft sind abgeholt.', 13, 1092, 13, NULL, 'ordinal', '2024-06-04 09:16:14.499697', 'Die Rahmenbedingungen und Ziele sind definiert und sind durch die GL verabschiedet.', '80% der LST Members wurden zu den Erwartungen an die Organisation der Zukunft befragt und deren Rückmeldungen wurden ausgewertet.
', 'Aus den Auswertungen der Ziele und Erwartungen wurde ein erster Plan für Anpassungen an der Organisation von Puzzle erstellt.', 3);
INSERT INTO okr_pitc.key_result VALUES (1298, NULL, '2024-06-18 13:06:31.40928', 'Ziel von diesem KR sind nicht direkt finanzielle Einsparungen. Wartungsbudget soll sinnvoll eingesetzt werden um mögliche Techschulden zu vermeiden.', NULL, 'Wir betreiben unsere Applikationen ideal und stellen eine gute Wartbarkeit sicher', 28, 1107, 28, NULL, 'ordinal', '2024-06-17 19:47:22.552851', 'Priorisierte Massnahmenliste nach Kosten/Nutzen ist definiert', 'Top 3 Prio Massnahmen sind umgesetzt ', 'Top 5 Prio Massnahmen sind umgesetzt ', 2);
INSERT INTO okr_pitc.key_result VALUES (1321, NULL, '2024-06-18 13:15:43.632085', 'Wir finden unseren "Way of working" und teilen unser Wissen im Team. Wir lernen im Prozess und minimieren Doppelspurigkeiten. ', NULL, 'Way of Working - Wir werden effizienter in unseren Prozessen', 3, 1115, 3, NULL, 'ordinal', '2024-06-18 12:35:20.441083', 'Knowledge Base etabliert (nextcloud live).
Mindestens ein Beitrag pro Member.
Zielliste für Inhalt erfasst. ', 'Migration vorhandene, verteilte (files.puzzle) Prozessdefinition oder Vorgehensansätze sammeln. ', 'Jeder Member hat seine Knowledge-Base-Insel zum pflegen. ', 2);
INSERT INTO okr_pitc.key_result VALUES (1268, NULL, '2024-09-10 06:34:34.900903', 'Wir halten das monatliche Wartungsbduget von 63.5h (gemessen wird Juli, August und September 24)  ', NULL, 'wir halten uns an das monatliche Wartungsbudget', 41, 1099, 41, NULL, 'ordinal', '2024-06-17 06:46:02.080958', '1 Monat < 63.5 h ', ' 3 Monate < 63.5 h ', '3 Monate < 60h ', 2);
INSERT INTO okr_pitc.key_result VALUES (1455, NULL, '2024-09-26 15:04:48.706534', '', NULL, 'Unser Angebotsportfolio ist marktorientierter formuliert und die Marktbearbeitung entsprechend lanciert. ', 26, 1143, 1039, NULL, 'ordinal', '2024-09-17 12:50:17.487551', 'Es ist puzzleweit definiert, was wir als marktorientiertes Angebot verstehen (gemeinsame Sprache in etablierten Modellen) und es steht eine verbindliche Roadmap, wie das Angebotsportfolio weiterentwickelt/überarbeitet wird.', 'Die ca. 10 wichtigsten Puzzle-Angebote sind erfasst und dokumentiert aufbereitet  (MOs eingeschlossen). Für jedes dieser Angebote sind bis 3 Kernaufgaben / Massnahmen definiert, die unsere Schwerpunkte der Marktbearbeitung
für die nächsten 6 -12 Monate bilden.', 'P31 ist marktorientierter aufbereitet / überarbeitet und 25 % der Kernaufgaben / Massnahmen gemäss Target Zone sind angelaufen oder bereits umgesetzt.', 9);
INSERT INTO okr_pitc.key_result VALUES (1479, NULL, NULL, 'Read-Only kann auch als Anweisung an die Members gelten.', NULL, 'Cryptopus ist abgelöst', 27, 1164, 27, NULL, 'ordinal', '2024-10-02 09:12:24.344984', 'Parallelbetrieb ist möglich', 'Bitwarden ist primäres PW-Managementtool', 'Cryptopus wurde read-only gesetzt', 0);
INSERT INTO okr_pitc.key_result VALUES (1467, NULL, '2024-09-24 14:47:03.974156', 'Dieses KR wird evtl. depiorisiert. Abhängig davon, ob beide (SBB und BLS) WTOs kommen und uns ar von Sales zur Verfügung steht.', NULL, 'Die New Tech Themen von Mobility sind -- zusammen mit CTO, CSO und /mid -- als Angebotshypothesen formuliert', 5, 1161, 5, NULL, 'ordinal', '2024-09-24 14:42:12.018393', 'Die Kundenbedürfnisse sowie internen Stärken zu den New Tech Themen Delivery Pipeline Migrationen, Applikationsmodernisierung, AWS / Serverless, AI Integrationslösungen und GO sind analysiert und priorisiert ', ' Die New Tech Themen sind als Angebotshypothesen formuliert ', ' Jedes dieser Angebote ist validiert durch mindestens einem relevantem Kunde ', 2);
INSERT INTO okr_pitc.key_result VALUES (1302, NULL, '2024-06-21 13:03:02.072764', 'Hilft für Compnay KR3: Wir haben bei den verrechenbaren Rollen die Austritte ersetzt und zusätzliche Members gewonnen.', NULL, 'Optimierung der Verteilung der Teammitglieder auf Projekte und Identifikation von personellen Lücken', 28, 1108, 28, NULL, 'ordinal', '2024-06-17 19:57:17.13149', 'Ruby Charta aktualisiert, Abweichungen zu den Zielen sind identifiziert.', 'Ruby Charta ist vollständig, Lücken/Potenziale in Projekte identifiziert, Ziele eingehalten.', 'Wir haben einen konkreten Plan um mit den Lücken und Potentiale umzugehen', 6);
INSERT INTO okr_pitc.key_result VALUES (1301, 835000, '2024-07-01 07:37:29.718587', 'Umsatz /ruby + /hitobito für Q2 und Q3

Vorjahrestotal im 1.10.2023 bis 31.03.2024:
Hitobito: CHF 690971.- Ruby: CHF 493676.- Total: 1729127.-

Messung nach https://time.puzzle.ch/reports/revenue von 01.10.24 bis 31.03.25

Initialwert:
Hitobito: CHF 342980.- Ruby: CHF 271956.- Total: CHF 614''936.00

Target ist 1 Mio', 1275000, 'Wir erfreuen uns über volle Auftragsbücher bis 31.03.2025', 28, 1108, 28, 'CHF', 'metric', '2024-06-17 19:56:40.455253', NULL, NULL, NULL, 7);
INSERT INTO okr_pitc.key_result VALUES (1304, 50, '2024-08-22 11:19:58.926151', 'Die Aufträge von Yelan, David und Dani laufen im Verlauf des Septembers potentiell aus. Wir wollen eine kontinierliche Auslastung sicherstellen. Gemessen wird nach Soll-Stunden gewichtete Auslastung im Q1+Q2', 95, 'Kontinuierliche Auslastung der Dev-Pensen', 33, 1109, 33, 'PERCENT', 'metric', '2024-06-17 23:39:05.681914', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1461, 0, '2024-09-26 11:50:18.668602', 'Wir posaunen unsere Vision von Platform Engineering über die folgenden Kanäle raus.
Die Members kennen unser Marktangebot und identifizieren sich damit.', 11, 'Wir kommunizieren unsere PE Vision auf 8 verschiedenen Kanälen', 40, 1154, 31, 'NUMBER', 'metric', '2024-09-17 06:26:44.29706', NULL, NULL, NULL, 12);
INSERT INTO okr_pitc.key_result VALUES (1308, NULL, '2024-06-17 23:48:22.892735', 'Gemessen wird die durchschnittliche Auslastung pro Monat.', NULL, 'Dev Praktikant/Junior angestellt und ausgelastet', 33, 1110, 33, NULL, 'ordinal', '2024-06-17 23:48:05.095926', 'Angestellt', 'Angestellt + 33% Auslastung im Q1', 'Angestellt + 66% Auslastung im Q1', 1);
INSERT INTO okr_pitc.key_result VALUES (1315, 12, '2024-07-11 08:24:41.063487', 'In der Breite liegt die Wirkung - wir posten 15 Artikel auf Social Media (Target). Dazu gehören Blogposts (auf WAC, die wir streuen), Erfahrungsberichte von Konferenzen, Social Media Umfragen...', 16, 'Wir posten weiterhin jede Woche einmal relevanten Content auf SoMe.', 3, 1114, 3, 'NUMBER', 'metric', '2024-06-18 12:08:14.003851', NULL, NULL, NULL, 3);
INSERT INTO okr_pitc.key_result VALUES (1254, NULL, '2024-06-20 06:28:33.545396', 'Die Marktanalyse für die Digitalen Lösungen sind abgeschlossen und die Schlussforgerungen liegen vor. Wir führen einen Workshop durch, um Angebot und Schwerpunkte zum Thema Digitale Lösungen zu definieren. Ein Vermarktungsplan und eine Go-To-Market Strategie ist erarbeitet und hilft uns bei der weiteren Marktbearbeitung.', NULL, 'MO Digitale Lösungen
Die Schlussfolgerungen aus der Marktanalyse sind vorhanden und die Schwerpunkte und das Angebot sind definiert.', 16, 1091, 16, NULL, 'ordinal', '2024-06-04 14:32:47.053686', '- Die Marktanalyse ist abgeschlossen und die Resultate aus der Kampagne von Carlos sind eingeflossen.
- Die Schlussfolgerungen aus der Analyse sind vorhanden und wurden mit allen DO einer Software-Entwicklungsdivision diskutiert und abgenommen.', '- Wir definieren ein Angebot und Schwerpunkte der Neuausrichtung für das Thema Digitale Lösungen.', '- Wir haben einen Vermarktungsplan und eine Go-To-Market Strategie für die Digitalen Lösungen.
- Wir haben drei Leads für Digitale Lösungen.', 2);
INSERT INTO okr_pitc.key_result VALUES (1469, NULL, '2024-09-26 14:32:42.786794', 'Geteiltes KR mit /mid', NULL, 'Wir definieren 3 Angebotspakete im Platform Engineering', 26, 1162, 26, NULL, 'ordinal', '2024-09-26 14:23:42.323553', 'Die neue Landingpage für Platform Engineering ist publiziert. ', ' Drei konkrete Angebote wie bspw. «Enabling Team as a Service» und «Discovery Workshop 2.0» sind definiert, in unsere Slide Decks integriert und auf der Website publiziert. ', 'Wir haben 5 Leads generiert aufgrund dieser Angebote. ', 1);
INSERT INTO okr_pitc.key_result VALUES (1250, NULL, '2024-06-18 11:45:37.131116', 'Wir arbeiten weiter an der Basis der Marktopportunität mit dem Ziel mindestens eine der Partnerschaften zu den Hyperscalern abzuschliessen. Zudem haben wir einen Plan für die Vermarktung und Go-To-Market Aktivitäten bis Ende Geschäftsjahr.', NULL, 'MO Hyperscaler
Wir kommunizieren eine Partnerschaft mit einem Hyperscaler.', 16, 1091, 16, NULL, 'ordinal', '2024-06-04 09:19:42.872314', '- Wir führen eine Liste der Opportunities bei AWS und GCP.
- Wir prüfen und planen externe Cloud Lunches AWS und GCP.
- Wir prüfen Teilnahme und Sponsoring-Möglichkeit am AWS Summit Zürich vom 4.9.24.', '- Wir drehen ein "Mate mit"-Hyperscaler Video.
- Wir haben einen Vermarktungsplan und eine Go-To-Market Strategie mit Schwerpunkten bis Ende Geschäftsjahr.
- Wir kommunizieren die AWS Partnerschaft am Puzzle up! 2024.', '- Wir schliessen Partnerschaft mit GCP ab.', 7);
INSERT INTO okr_pitc.key_result VALUES (1311, 0, '2024-06-18 11:45:52.522219', 'Alle Action Items wurden erledigt.', 7, 'MO Plattform Engineering
Das go-to-market Konzept ist abgeschlossen und ein erster Platform MVP wurde intern aufgebaut.
', 24, 1091, 24, 'NUMBER', 'metric', '2024-06-04 09:18:13.055124', NULL, NULL, NULL, 3);
INSERT INTO okr_pitc.key_result VALUES (1241, 4050000, '2024-06-27 07:24:49.137156', 'Forecast Wert des geplanten Dienstleistungsumsatzes aus PTime für die Monate Okt-Jan (1.10.24-31.1.25) (Pauschalen, Partnerumsätze, Subs sind nicht enthalten). Commit MCHF 4.45, Target: MCHF 5.0, Stretch MCHF 5.4. Stichdatum: 16.09.24', 5400000, 'Wir erfreuen uns über volle Auftragsbücher bis über den Jahreswechsel hinaus.', 13, 1090, 13, 'CHF', 'metric', '2024-06-04 09:08:49.614671', NULL, NULL, NULL, 5);
INSERT INTO okr_pitc.key_result VALUES (1277, 0, '2024-07-04 07:23:47.19402', 'Lab mit Uni Bern: ML Ops for Engineers und ML Ops Grundlagen sind zwei neue Labs, die mit Acend aufgebaut werden sollen, diese werden aber noch nicht durchgeführt.
Die erfolgreich durchgeführten Labs beziehen sich auf das PuzzleUp und den CH Open Workshop.', 3, 'MO AI & MLOps: 2 MLOps Labs sind durchgeführt ', 5, 1096, 5, 'NUMBER', 'metric', '2024-06-13 14:40:06.702244', NULL, NULL, NULL, 4);
INSERT INTO okr_pitc.key_result VALUES (1251, NULL, '2024-08-21 11:20:58.342214', 'Das Team für die Marktopportunität Beratung hat sich formiert und die ersten Aktivitäten werden angegangen. Eine erste Auslegeordnung und ein erstes Angebot ist vorhanden.', NULL, 'MO Beratung
Wir starten mit der Marktopportunität Beratung und haben eine erste Auslegeordnung erstellt.', 16, 1091, 1034, NULL, 'ordinal', '2024-06-04 09:28:39.675608', '- Wir sind als Team organisiert und der Kick-Off zur Marktopportunität Beratung ist erfolgt.
- Eine erste Auslegeordung und ein mögliches Angebot für das Thema Beratung ist erstellt.', '- Wir haben eine Massnahmenplan und eine Roadmap für das weitere Vorgehen festgelegt.
- Wir haben mit ersten möglichen Kunden unser Angebot verifiziert und Feedback eingeholt.', '- Wir haben einen Lead für einen Auftrag für das Thema Beratung gewonnen.', 4);
INSERT INTO okr_pitc.key_result VALUES (1476, NULL, '2024-10-02 09:04:34.561289', '', NULL, 'Bitwarden ist sauber aufgesetzt', 27, 1164, 27, NULL, 'ordinal', '2024-10-02 09:03:19.967584', 'Bitwarden Prod ist stabil', '/sys hat Betrieb übernommen', 'Restore wurde erfolgreich getestet', 1);
INSERT INTO okr_pitc.key_result VALUES (1477, NULL, '2024-10-02 09:05:22.389104', '', NULL, 'Dokumentation ist vollständig', 27, 1164, 27, NULL, 'ordinal', '2024-10-02 09:03:19.968405', 'User- und Admin-Guides sind rudimentär vorhanden', 'User-Guide ist zufriedenstellend', 'Admin-Guide ist zufriedenstellend', 1);
INSERT INTO okr_pitc.key_result VALUES (1478, NULL, '2024-10-02 09:09:29.46423', '', NULL, 'Datenmigration ist erfolgreich', 27, 1164, 27, NULL, 'ordinal', '2024-10-02 09:03:19.968982', 'Alle Passwörter sind in Bitwarden', 'Die Zugriffsberechtigungen konnten grundsätzlich mit-abgebildet werden', 'Die Zugriffsberechtigungen sind identisch.', 1);
INSERT INTO okr_pitc.key_result VALUES (1453, NULL, '2024-10-07 11:23:12.96947', 'Damit wir mögliche Massnahmen im Hinblick auf eine vereinfachte Kommunikation festlegen können, möchten wir die strategische Plattform mit einbeziehen.', NULL, 'Wir evaluieren Odoo als Plattform zur Vereinheitlichung der internen Kommunikation. ', 26, 1144, 49, NULL, 'ordinal', '2024-09-17 12:48:22.052025', 'Wir kennen die Möglichkeiten von Odoo in der internen Kommunikation. ', 'Wir haben evaluiert, ob Odoo für die interne Kommunikation von Puzzle infrage kommt. ', 'Wir haben ein Grobkonzept für die mögliche Einführung von Odoo in der internen Kommunikation erstellt. ', 2);
INSERT INTO okr_pitc.key_result VALUES (1392, 5, '2024-10-09 13:03:01.156797', 'Die Messungen erfolgt jeweils Mitte Monat anhand der Members-Kapazitätsliste ohne okr_pitcDE: Anzahl Members sofort + nächster Monat, die nicht mind. 2 mögliche Aufträge haben.

', 0, 'Wir finden für alle Members mit freier Kapazität mindestens 2 mögliche Aufträge.', 13, 1134, 13, 'NUMBER', 'metric', '2024-09-03 09:41:39.227975', NULL, NULL, NULL, 4);
INSERT INTO okr_pitc.key_result VALUES (1458, 4.5, '2024-10-15 12:32:57.208972', 'Stand Mitte September 2024 sind ab Januar 2025 4.5 FTE auf der Membersplanung.
Bis Mitte Dezember 2024 sollen die FTE ab Januar 2025 auf 0 runter gebracht werden.

"Membersplanung.ods" siehe https://files.puzzle.ch/f/428988', 0, 'Es sind keine /dev/tre-Members mehr auf der Membersplanung', 4, 1160, 4, 'FTE', 'metric', '2024-09-17 13:01:28.340678', NULL, NULL, NULL, 3);
INSERT INTO okr_pitc.key_result VALUES (1480, NULL, NULL, '', NULL, 'Unsere aktuellen Portfolio-Schwerpunkte sind definiert und an unsere Members kommuniziert', 81, 1166, 17, NULL, 'ordinal', '2024-10-17 17:33:11.065958', 'TBD', 'TBD', 'TBD', 0);
INSERT INTO okr_pitc.key_result VALUES (1481, 3, NULL, '', 5, 'Unsere Members haben aktuelle Zertifizierungen zu unseren Technologieschwerpunkten abgelegt', 81, 1166, 84, 'NUMBER', 'metric', '2024-10-17 17:33:41.993301', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1482, NULL, NULL, '', NULL, 'Wir begeistern Externe für unsere aktuellen Portfolio-Schwerpunkte', 81, 1166, 81, NULL, 'ordinal', '2024-10-17 17:34:09.502901', 'TBD', 'TBD', 'TBD', 0);
INSERT INTO okr_pitc.key_result VALUES (1484, 3, NULL, '', 5, 'Wir haben die Finanzierung eingebrachter Ideen sichergestellt', 81, 1167, 17, 'NUMBER', 'metric', '2024-10-17 17:35:26.586961', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1318, 0, '2024-06-18 12:28:15.656737', 'Dokumentation von Anzahl Members, die regelmässig LinkedIn-Posts veröffentlichen sowie je ein publizierter Post.   ', 10, 'Wir wissen, wer gerne LinkedIn-Posts veröffentlicht, haben diese Information dokumentiert und bauen unsere Botschafter:innen zu spezifischen Themen auf. ', 26, 1104, 26, 'NUMBER', 'metric', '2024-06-17 11:12:14.190596', NULL, NULL, NULL, 3);
INSERT INTO okr_pitc.key_result VALUES (1290, NULL, '2024-06-18 12:33:23.611667', '- d3-Workshop 23.4.2024, Files: https://files.puzzle.ch/f/6357279
- MO "Ditigale Lösungen: Codi https://codimd.puzzle.ch/fGy0RNSzTVqVlUz80dw_Mw', NULL, 'Unser (d3) Mitwirken und unsere Sales-Erwartungen zur Marktopportunität (MO) "Digitale Lösungen" sind formuliert und kommuniziert sowie mit den /dev- & /ux-Divisions abgeglichen.', 4, 1105, 4, NULL, 'ordinal', '2024-06-17 12:07:45.504078', '- Verarbeitung Outcome d3-Workshop 23.4.2024
- d3-Mitwirken und -Sales-Erwartungen sind mit  MO "Ditigale Lösungen" abgeglichen und dokumentiert', '- d3-Mitwirken sind mit /mobility, /ruby & /ux abgestimmt
- allgemeine /dev- & /ux-Mitwirken sind formuliert
- d3-Sales-Erwartungen sind mit /mobility, /ruby & /ux abgestimmt
- allgemeine /dev- & /ux-Sales-Erwartungen sind mit /mobility, /ruby & /ux abgestimmt
', '- Inputs und Sales-Erwartungen seitens /dev- /ux-Divisions sind:
- an Sales (Yup) kommuniziert & besprochen
- dem Kernteam MO "Digitale Lösungen" präsentiert', 5);
INSERT INTO okr_pitc.key_result VALUES (1463, NULL, '2024-09-25 08:32:44.185731', 'Wir regeln, wie wir in den SIGs arbeiten.
Backlog-Tickets = Es gibt Tickets, die gut beschrieben und ready for action sind.', NULL, 'Unsere Arbeitsmethodik in den SIGs ist definiert', 40, 1153, 40, NULL, 'ordinal', '2024-09-17 06:19:16.532056', 'Readme ist fertiggestellt und die Verantwortlichkeiten sind definiert.', 'Bis auf die Backlog-Tickets, sind alle Actions umgesetzt.', 'Andere Divisions kopieren die definierte Arbeitsmethodik und gründen eine SIG.', 3);
INSERT INTO okr_pitc.key_result VALUES (1340, NULL, '2024-07-01 09:06:47.6577', '', NULL, 'MO Platform Engineering Beitrag definieren', 27, 1119, 27, NULL, 'ordinal', '2024-06-19 07:52:15.254026', 'Aktiver Austausch mit Lead/Kernteam ', 'Roadmap für /security-Beitrag ist definiert ', '/security ist bereit für Aufträge im MO ', 1);
INSERT INTO okr_pitc.key_result VALUES (1316, NULL, '2024-06-18 12:47:59.661365', 'Analyse der SoMe-Kampagne und Traffic Website von April-Juni - rollierend erweitert mit Erkenntnissen aus der neuen SoMe-Kampagne.', NULL, 'Wir wissen, wie wir unsere Kund:innen richtig ansprechen und welche Inhalte interessieren. ', 3, 1114, 3, NULL, 'ordinal', '2024-06-18 12:12:35.534198', 'Konzept Auswertung erstellt, Erkenntnisse gewonnen', 'Erkenntnisse sind im Team kommuniziert und diskutiert.', 'Wir wenden unser gewonnenes Wissen auf die neuen Artikel an. ', 1);
INSERT INTO okr_pitc.key_result VALUES (1317, NULL, '2024-07-08 08:31:48.917591', 'Wir sprechen Kund:innen anders über Puzzle und über WAC an. Die Puzzle-Website ist unsere "Feelgood"-Package (digitale Lösungen)-Kanal, we are cube ist stärker auf Consulting ausgerichtet. ', NULL, 'Wir kommunizieren gezielter mit UX Kunden über die WAC Website', 3, 1114, 3, NULL, 'ordinal', '2024-06-18 12:15:09.571382', 'Konsens über Informationen auf wearecube.ch/puzzle.ch', 'Texte und Struktur steht, Illustrationen usw. sind erstellt.', 'wearecube.ch angepasst', 2);
INSERT INTO okr_pitc.key_result VALUES (1319, NULL, '2024-07-22 08:13:34.028516', 'Wir hatten in der näheren Vergangenheit Änderungen in unserem kleinen Team. Daraus entstehende Unruhe möchten wir reduzieren ohne unsere Rentabilität zu torpedieren', NULL, 'Als Team wachsen wir zusammen, fühlen uns wohl und integrieren Neuzugänge', 3, 1115, 3, NULL, 'ordinal', '2024-06-18 12:33:04.248237', 'Kickoff mit gemeinsamen Zielen  - Action Plan für Teamzufriedenheit erstellen. ', 'Priorisierung der Massnahmen und Umsetzung Top 1 Massnahmen', 'Priorisierung der Massnahmen und Umsetzung Top 2 Massnahmen', 5);
INSERT INTO okr_pitc.key_result VALUES (1470, 0, '2024-09-26 15:01:58.55788', 'Wir posaunen unsere Vision von Platform Engineering über die folgenden Kanäle raus.
Die Members kennen unser Marktangebot und identifizieren sich damit.

Geteiltes KR mit /mid', 11, 'Wir kommunizieren unsere Platform Engineering Vision auf 8 verschiedenen Kanälen.', 26, 1162, 26, 'PERCENT', 'metric', '2024-09-26 14:25:36.261026', NULL, NULL, NULL, 2);
INSERT INTO okr_pitc.key_result VALUES (1485, 3, NULL, '', 5, 'Alle Members nehmen an unseren Angeboten teil', 81, 1167, 81, 'NUMBER', 'metric', '2024-10-17 17:35:59.621691', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1456, NULL, '2024-10-18 08:38:31.248852', '', NULL, 'Wir klären in welchen Marktopportunitäten GJ 24/25 wir aktiv sein könnten', 4, 1159, 4, NULL, 'ordinal', '2024-09-17 12:54:36.971554', 'Die MOs GJ 24/24 sind studiert und hinsichtlich Unterstützung durch d3 validiert ', 'Falls Unterstützung durch d3 möglich, ist diese Niedergeschriebnen, ', 'Die d3-Mitwirkung an dem MOs ist kommuniziert an/ besprochen mit: - Sales (Yup) - GL Coach (Dänu) ', 1);
INSERT INTO okr_pitc.key_result VALUES (1322, NULL, '2024-06-18 12:43:44.239125', 'Wir ersetzen den Austritt von Julia (80%), den Austritt von Flavio (80%) und die Reduktion von Tobi (20%) nachhaltig und verrechenbar. ', NULL, 'Wir haben bei den verrechenbaren Rollen die Austritte ersetzt.', 3, 1112, 3, NULL, 'ordinal', '2024-06-18 12:03:22.628908', 'Ausfälle sind ersetzt (d.h. Vertrag ist unterschrieben).', 'Mindestens 1 Member hat die Stelle bereits angetreten und arbeitet produktiv auf einem Mandat. ', 'Zwei Members sind verrechenbar angestellt. ', 1);
INSERT INTO okr_pitc.key_result VALUES (1464, NULL, NULL, 'Wir sind biz hintendrein, deshalb geben wir jetzt Guzzi.', NULL, 'Die Observability SIG startet durch', 40, 1153, 40, NULL, 'ordinal', '2024-09-23 13:15:27.468027', 'Die Members für die SIG Observability sind gefunden', 'Der Umfang und das Ziel der SIG Observability ist definiert', 'Es gibt ein Steckbrief/README', 0);
INSERT INTO okr_pitc.key_result VALUES (1278, NULL, '2024-06-18 12:50:23.599892', 'Kleine Bubble, die wir beeinflussen können', NULL, 'Unsere Wir vertiefen und etablieren die drei Fokusthemen “Ziele”, “Verantwortung” und “Beteiligung an Entscheiden”', 4, 1102, 4, NULL, 'ordinal', '2024-06-17 10:20:09.572591', 'Die 3 Fokusthemen wurden im Quartalsworkshop weiterentwickelt:
- Selbstorganisation mit OrgDev, bzw. Puzzle abstimmen, bzw. darauf aufbauen, z.B.
- Für die Fokusthemen etablierte Prozesse/ Modelle nehmen (Z.B. DACI für Entscheidungen)

 niedergeschrieben.', 'Die Regeln zu den 3 Fokusthemen sind erarbeitet und niedergeschrieben.

Massnahmen und Messpunkte zu den 3 Fokusthemen sind definiert ', 'Die Resultate sind dem GL-Body und dem LST präsentiert', 6);
INSERT INTO okr_pitc.key_result VALUES (1471, NULL, NULL, '', NULL, 'Unser Hyperscaler-Marktangebot ist geschärft und an unsere Kund:innen und Members kommuniziert.', 26, 1162, 1039, NULL, 'ordinal', '2024-09-26 14:26:14.382996', 'Unser Hyperscaler-Angebot ist geschärft und am Markt validiert. ', ' Wir haben das Hyperscaler-Angebot intern und extern (Website, SoMe, Newsletter, Video) kommuniziert. ', 'Mit einer Marketingkampagne generieren wir 3 qualifizierte Sales-Leads. ', 0);
INSERT INTO okr_pitc.key_result VALUES (1483, 5, '2024-10-21 09:33:54.999787', 'https://codimd.puzzle.ch/Ey_NylAMS0yNCd5PQxymcg', 10, 'Unsere Members beteiligen sich aktiv an der Ideenfindung für unser We-Care-Angebot', 81, 1167, 17, 'NUMBER', 'metric', '2024-10-17 17:35:02.203051', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1288, NULL, '2024-06-18 12:55:17.449217', '', NULL, 'Wir haben einen Plan “Team-Aktivitäten GJ 24/25”', 4, 1102, 4, NULL, 'ordinal', '2024-06-17 12:03:21.24326', 'Es ist definiert, was mit Team-Aktivitäten gemeint ist?
Es ist definiert, welches die Ziele und Inhalte der Aktivitäten (sachlich, sozial, etc.)
', 'Eine erste Team-Aktivität hat stattgefunden.', 'Die Team-Aktivitäten fürs GJ 24/25 sind geplant', 3);
INSERT INTO okr_pitc.key_result VALUES (1324, NULL, '2024-06-18 13:06:35.731107', '/ux ist für digitale Lösungen ein Zudiener (gemäss Absprache SUM) - als Puzzlestück helfen wir mit ein attraktives Angebot zu erstellen. ', NULL, 'Wir unterstützen die Initiative "Digitale Lösungen"', 3, 1112, 36, NULL, 'ordinal', '2024-06-18 13:03:03.257927', '- Wir schliessen die Marktanalyse Digitale Lösungen ab.
- Wir sind an jedem Digitale Lösungen Meeting dabei und bringen unsere Sicht ein.', 'Gemeinsames Statement zusammen mit Dev-Bereichen erreicht. ', 'Artefakte Konzeption / UX sind formuliert. ', 2);
INSERT INTO okr_pitc.key_result VALUES (1331, 7, '2024-06-25 15:07:08.653438', 'Zufriedenheit bleibt hoch. Gemessen wird die Zufriedenheit anhand der "Happiness Umfrage", die monatlich nach dem Teammeeting durchgeführt wird

Commit: 7.75
Target: 8

Mit 🙂 gekennzeichnete Actions geben wir an unsere Members weiter.', 8.5, 'Die Members bei /mid bleiben zufrieden.', 40, 1120, 40, 'NUMBER', 'metric', '2024-06-18 13:22:38.460131', NULL, NULL, NULL, 15);
INSERT INTO okr_pitc.key_result VALUES (1296, NULL, '2024-06-18 13:20:40.390659', '', NULL, 'Die /dev/tre-Members sind bis Ende 2024 eingeplant', 4, 1106, 4, NULL, 'ordinal', '2024-06-17 12:09:42.258426', 'Alle d3-Members per Ende Juli bis Ende Oktober eingeplant', 'Alle d3-Members per Ende August bis Ende Jahr', 'Alle d3-Members per Ende August bis Ende Januar 2024', 2);
INSERT INTO okr_pitc.key_result VALUES (1327, NULL, '2024-06-24 06:17:48.017819', 'MO "Digitale Lösungen: Codi https://codimd.puzzle.ch/fGy0RNSzTVqVlUz80dw_Mw', NULL, 'Mitwirken an der Marktopportunität (MO) "Digitale Lösungen"', 28, 1108, 28, NULL, 'ordinal', '2024-06-18 13:13:21.64572', 'Wir bringen unsere Erwartungen in de MO ein', 'Erwartungen sind mit /dev3, /mobility, /ux, sales, markom abgestimmt.', 'Wir sind Teil des Vermarktungsplan und der Go-To-Market Strategie. - Wir haben einen Lead für Digitale Lösungen. ', 4);
INSERT INTO okr_pitc.key_result VALUES (1329, NULL, '2024-06-25 14:14:49.433052', 'Wir wollen mehr Support-Verträge abschliessen, sei dies OpenShift/Rancher oder Pipeline-Support.', NULL, 'Das Support Angebot weitet sich aus 💰', 40, 1118, 40, NULL, 'ordinal', '2024-06-18 13:19:20.359097', '1 weiterer offeriert und SHKB abgeschlossen', '1 weiterer neben SHKB', '2 weitere neben SHKB', 3);
INSERT INTO okr_pitc.key_result VALUES (1336, NULL, '2024-06-19 07:50:27.816055', '', NULL, 'MO Beratung Beitrag definieren', 27, 1119, 27, NULL, 'ordinal', '2024-06-18 14:03:06.481582', 'Aktiver Austausch mit Lead/Kernteam', 'Roadmap für /security-Beitrag ist definiert', '/security ist bereit für Aufträge im MO', 1);
INSERT INTO okr_pitc.key_result VALUES (1328, 750000, '2024-07-09 08:14:29.145333', 'Vergleich 2023.10 - 2024.01 Ergebnis:
  - Total TCHF 1197,
  - Durchschnittlich pro Monat TCHF 299

Zielwert für 01.10.2024 - 31.01.2025 Ergebnis:
Commit: TCHF 1050
Target: TCHF 1200', 1350000, 'Wir erfreuen uns über volle Auftragsbücher bis über den Jahreswechsel hinaus', 40, 1118, 40, 'CHF', 'metric', '2024-06-18 13:15:24.365383', NULL, NULL, NULL, 5);
INSERT INTO okr_pitc.key_result VALUES (1389, NULL, '2024-09-17 09:32:23.4478', '', NULL, 'MO Digitale Lösungen
Wir haben ein attraktives Zielbild für die MO Digitale Lösungen und eine realistische Roadmap.', 1034, 1136, 1034, NULL, 'ordinal', '2024-09-03 09:23:50.638907', 'Es herrscht Einigkeit über das Zielbild der Digitalen Lösungen bei den Division Owner:innen. Unsere Expertisen und Lücken sind erfasst.', 'Erste Angebote von digitalen Lösungen sind formuliert.', 'Eine erste Digitale Lösung hat einen Deal generiert', 6);
INSERT INTO okr_pitc.key_result VALUES (1419, NULL, '2024-09-17 12:33:10.282853', 'Wir verlängern unser Mandat beim IGE oder weiten es sogar aus. ', NULL, 'Verlängerung Mandat IGE', 3, 1147, 36, NULL, 'ordinal', '2024-09-16 14:09:38.680074', 'Das Mandat ist verlängert', 'Das verlängerte Pensum entspricht 2024 = 120%', 'Mindestens eins trifft zu von
- Rates sind angepasst (Erhöhung Rates für Pascou)
- Pensum erhöht - onboarding weitere UXler', 1);
INSERT INTO okr_pitc.key_result VALUES (1420, NULL, '2024-09-17 12:33:27.734046', '', NULL, 'Verlängerung Mandat HRM', 3, 1147, 36, NULL, 'ordinal', '2024-09-16 14:11:06.661179', 'Das Mandat ist verlängert - egal wie', 'Das Pensum bleibt wie im 2024', 'Wir weiten das Pensum aus', 1);
INSERT INTO okr_pitc.key_result VALUES (1437, NULL, '2024-09-17 06:32:59.581656', '', NULL, 'Neuausrichtung der Divison Ruby/Hitobito', 41, 1152, 41, NULL, 'ordinal', '2024-09-17 06:28:56.528656', 'Sollkonzept ist definiert ', 'Massnahmen sind definiert ', 'erste Umsetzungen ', 1);
INSERT INTO okr_pitc.key_result VALUES (1388, NULL, '2024-09-17 09:29:47.096302', 'Wir überprüfen und schärfen unser bestehendes Hyperscaler Angebot und kommunizieren dieses an Kund:innen, Partner:innen und Members.', NULL, 'Unser Hyperscaler Marktangebot ist geschärft und an unsere Kund:innen und Members kommuniziert.', 16, 1135, 16, NULL, 'ordinal', '2024-09-03 09:23:21.215301', 'Unser Hyperscaler Angebot ist geschärft und intern kommuniziert.', 'Wir haben das Angebot gegenüber den Kund:innen kommuniziert.', 'Wir publizieren ein "Mate mit"-Hyperscaler Video.', 3);
INSERT INTO okr_pitc.key_result VALUES (1293, NULL, '2024-06-18 13:21:38.70695', '', NULL, 'Wir haben geklärt, an welchen Events im GJ 2024-25 /dev/tre einen Beitrag leistet', 4, 1105, 4, NULL, 'ordinal', '2024-06-17 12:08:29.409697', 'Wir erstellen einen Massnahmeplan zu d3 an Events. Wir estellen eine Arbeitsgruppe innerhalb d3 und klären die Verantwortlichkeit', 'Ein Beiträg an einem INTERNEN Events wurde geleistet.', 'Ein Beiträg an einem EXTERNEN Events wurde geleistet.', 2);
INSERT INTO okr_pitc.key_result VALUES (1326, NULL, '2024-08-27 14:53:54.006633', 'Pro Pfeiler ist ein Blueprint am entstehen
- Kubernetes
- Build-CI
- GitOps
- Observability
- Security
- (DevX)', NULL, 'Modulare PE Blueprints sind in Arbeit 🙂', 40, 1098, 40, NULL, 'ordinal', '2024-06-18 13:10:45.416404', 'Pfeiler haben passenden Namen & eine Definition (Steckbrief/Readme)', 'Pro Pfeiler ist ein Draft eines modularen Blueprints erstellt.
Die PO-Rolle ist besetzt.', 'Pro Pfeiler ist ein fertiger modularer Blueprint erstellt.', 13);
INSERT INTO okr_pitc.key_result VALUES (1333, NULL, NULL, '"Näher an ISO 27001" heisst spezifisch noch ohne grössere Änderungen an der Puzze-Kultur; grosse Schritte hängen von GL-Entscheid ab.', NULL, 'P16 ist so überarbeitet, dass er zur Strategie passt und näher an ISO27001 ist.', 27, 1116, 27, NULL, 'ordinal', '2024-06-18 13:50:23.658672', 'Probleme in P16 sind identifiziert und bestehende Tickets sind wieder in guten Zustand (oder geschlossen).', 'P16 ist grösstenteils überarbeitet, Rest ist in Tickets erfasst.', 'P16 ist komplett überarbeitet.', 0);
INSERT INTO okr_pitc.key_result VALUES (1334, NULL, NULL, 'Wichtig: Die Prozesse, welche am stärksten mit Risiken und Entwicklung zu tun haben.', NULL, 'Sicherheitsthemen sind in nicht-P16 Prozessen überarbeitet.', 27, 1116, 27, NULL, 'ordinal', '2024-06-18 13:53:04.102457', 'Problemstellen sind identifiziert.', 'Wichtige Prozesse sind überarbeitet.', 'Alle Prozesse sind überarbeitet.', 0);
INSERT INTO okr_pitc.key_result VALUES (1339, NULL, NULL, '', NULL, 'MO Digitale Lösungen Beitrag definieren', 27, 1119, 27, NULL, 'ordinal', '2024-06-19 07:51:31.874605', 'Aktiver Austausch mit Lead/Kernteam ', 'Roadmap für /security-Beitrag ist definiert ', '/security ist bereit für Aufträge im MO ', 0);
INSERT INTO okr_pitc.key_result VALUES (1341, NULL, NULL, '', NULL, 'Cryptopus wird abgelöst.', 27, 1116, 27, NULL, 'ordinal', '2024-06-19 07:53:06.96398', 'Wir haben einen Plan wie wir Cryptopus ablösen und was nötig ist. ', 'Wir haben die Alternative bereit, aber die Migration noch nicht durchgeführt. ', 'Migration erfolgreich durchgeführt. ', 0);
INSERT INTO okr_pitc.key_result VALUES (1262, NULL, '2024-06-24 07:24:26.559405', 'Formulierung entspricht Company OKR', NULL, 'MO Beratung: Wir starten mit der Marktopportunität Beratung und haben eine erste Auslegeordnung', 20, 1096, 20, NULL, 'ordinal', '2024-06-13 14:21:39.522447', 'Wir sind als Team organisiert und der Kick-Off zur Marktopportunität Beratung ist erfolgt.
Eine erste Auslegeordung und ein mögliches Angebot für das Thema Beratung ist erstellt.', 'Wir haben eine Massnahmenplan und eine Roadmap für das weitere Vorgehen festgelegt.
Für /mobility haben wir ein Konzept wie wir Members für Beratungseinsätze kurzfristig aus ihren Mandaten herausnehmen', 'Wir haben mit ersten möglichen Kunden unser Angebot verifiziert und Feedback eingeholt.', 1);
INSERT INTO okr_pitc.key_result VALUES (1343, 2, '2024-06-25 13:44:04.283298', 'Wir haben anhand des Action Plans eine Go-to-Market Strategie für Platform Engineering erarbeitet.

Commit: 5 Actions umgesetzt
Target: 7 Actions umgesetzt', 9, 'Go-to-Market Strategie erfolgreich erarbeitet (🙂)', 40, 1098, 40, 'NUMBER', 'metric', '2024-06-18 13:00:42.280522', NULL, NULL, NULL, 12);
INSERT INTO okr_pitc.key_result VALUES (1348, NULL, '2024-06-27 07:37:38.154735', '', NULL, 'In der Divisionsrentabilität haben wir die Zielwerte für Divisions definiert und eingeführt.', 22, 1090, 22, NULL, 'ordinal', '2024-06-04 09:09:17.565579', 'Die Jahreszielwerte sind definiert und eingeführt.', 'Die Quartalszielwerte sind definiert und eingeführt.', 'Die Quartalszielwerte werden von 70% der Divisions erreicht.', 4);
INSERT INTO okr_pitc.key_result VALUES (1344, 2, '2024-06-25 14:35:57.012724', 'Anhand des Action Plans definieren wir die interne Plattform.
Mit 🙂 gekennzeichnete Actions geben wir an unsere Members weiter

Commit: 4 Actions umgesetzt
Target: 5 Actions umgesetzt', 6, 'MVP ist erstellt, Stakeholder sind eingebunden und die Kommunikation ist sichergestellt (🙂)', 40, 1098, 40, 'NUMBER', 'metric', '2024-06-18 13:03:03.339119', NULL, NULL, NULL, 17);
INSERT INTO okr_pitc.key_result VALUES (1346, NULL, NULL, 'Der Durchschnitt des Ertrags aus Leistungsverrechnungen pro FTE pro Monat ist gleich gut wie vor einem Jahr.
Als Basis dienen Zahlen vom Q1 GJ2023/2024 (Juli 2023 bis August 2023). Durchschnitt: TCHF 16.1', NULL, 'Der Ertrag pro FTE pro Monat bleibt gleich gut', 40, 1118, 40, NULL, 'ordinal', '2024-06-25 15:01:03.794902', 'Der Ertrag ist <95% der Vergleichsperiode vor einem Jahr: TCHF 15.3', 'Der Ertrag ist gleich wie in der Vergleichsperiode vor einem Jahr: TCHF 16.1', 'Der Ertrag ist um 2.5% beser als in der Vergleichsperiode vor einem Jahr: TCHF 16.5', 0);
INSERT INTO okr_pitc.key_result VALUES (1332, NULL, '2024-06-25 15:15:15.997245', 'Im neuen Geschäftsjahr (2024/2025) halten x /mid-Members mindestens einen Talk. Dieses KR dient der Organisation dieser Talks.', NULL, '/mid-Talks sind geplant 🙂', 40, 1120, 40, NULL, 'ordinal', '2024-06-18 13:29:32.281768', 'Die Themen und Speaker für mögliche Talks sind definiert und potenzielle Events aufgelistet.', 'Commit erreicht & vier Talks sind definiert (Thema, Speaker, Event), wovon zwei bereits fix eingeplant sind.', 'Alle Talks sind definiert, wovon vier bereits fix eingeplant sind und ein zusätzlicher Talk bereits durchgeführt wurde.', 5);
INSERT INTO okr_pitc.key_result VALUES (1349, 77.225, '2024-06-27 07:40:36.966803', 'Kündigungen und Anstellungen die nach dem Quartal erfolgen werden bei der Messung auch miteinbezogen.', 80.225, 'Wir haben bei den verrechenbaren Rollen die Austritte ersetzt und zusätzliche Members gewonnen.', 22, 1090, 22, 'FTE', 'metric', '2024-06-04 09:16:36.160563', NULL, NULL, NULL, 3);
INSERT INTO okr_pitc.key_result VALUES (1350, NULL, '2024-06-27 07:52:30.699336', '', NULL, 'Das CRM Projekt hat Fahrt aufgenommen und kommt voran.', 13, 1092, 24, NULL, 'ordinal', '2024-06-27 07:50:59.301335', 'Das Projektteam ist definiert und eine erste Evaluation der möglichen Odoo SaaS Anbieter ist gemacht worden.
', 'Eine erste Installation unseres neuen CRM läuft und ein erstes Datenset aus Highrise wurde migriert.

', 'Die Anpassungen von PTime und anderen Umsystemen wurden für das Testsystem bereits testweise umgesetzt.

', 1);
INSERT INTO okr_pitc.key_result VALUES (1384, 10.4, '2024-10-17 07:30:44.95937', 'Die Messungen erfolgt jeweils Mitte Monat anhand der Members-Kapazitätenliste ohne okr_pitcDE. Summe der sofort + nächster Monat verfügbaren Members. Idee: die 3 freien FTE bringen unsere MO+IPR voran.', 2.9, 'Wir halten die freien Kapazitäten auf unter 3 FTE.', 13, 1134, 13, 'FTE', 'metric', '2024-09-03 09:16:57.426736', NULL, NULL, NULL, 6);
INSERT INTO okr_pitc.key_result VALUES (1424, 50, '2024-09-17 12:55:28.108934', 'Basis ist die Auslastung der verrechenbaren Pensen gemäss PuzzleTime-Planung.', 90, 'Teamauslastung auf für GJ/Q3 auf 90% erhöhen', 33, 1149, 33, 'PERCENT', 'metric', '2024-09-16 22:02:48.506837', NULL, NULL, NULL, 2);
INSERT INTO okr_pitc.key_result VALUES (1425, 0, '2024-10-28 08:28:20.032715', 'Wir wollen mit möglichst ganzjährigen Mandaten eine gute Auslastungs-Baseline für das 2025 erzielen. Basis ist die Auslastung der verrechenbaren Pensen gemäss PuzzleTime-Planung.', 50, 'Teamauslastung für das ganze 2025 auf 50% erhöhen.', 33, 1149, 33, 'PERCENT', 'metric', '2024-09-16 22:04:31.647541', NULL, NULL, NULL, 2);
INSERT INTO okr_pitc.key_result VALUES (1276, NULL, '2024-07-04 07:21:54.944384', '', NULL, 'MO AI & MLOps: Ein AI Use Case ist als MLOps Show Case umgesetzt', 5, 1096, 5, NULL, 'ordinal', '2024-06-17 07:02:23.912373', 'Use Case ist umgesetzt', '+ Zwei Blogs sind publiziert', '+ ein zahlender Kunde für den Use Case bzw. dessen Implementierung vorhanden', 1);
INSERT INTO okr_pitc.key_result VALUES (1314, 6, '2024-07-11 08:23:29.853144', 'Wir sind mindestens an 6 Events vor Ort (Target) mit Networking und Aufbau von Wissen. ', 8, 'Wir sind an wichtigen Events am Networken und bilden uns an Konferenzen weiter. ', 3, 1114, 3, 'NUMBER', 'metric', '2024-06-18 12:04:20.142148', NULL, NULL, NULL, 4);
INSERT INTO okr_pitc.key_result VALUES (1375, NULL, NULL, '', NULL, 'Wir verfügen über eine Test- und Demoplattform für kubevirt-basierte Projekte', 17, 1131, 84, NULL, 'ordinal', '2024-07-16 12:04:22.878689', 'Wir haben eine Plattform, auf der wir interne Tests mit kubevirt durchführen können.', 'Es existiert eine Plattform, die bei Kunden und Partnern für Demozwecke verwendet werden kann', 'Ein Kunde bestellt eine vergleichbare Plattform', 0);
INSERT INTO okr_pitc.key_result VALUES (1376, 0, '2024-07-16 12:05:38.89382', 'Zwei Members haben die Basis-Zertifizierung für einen Hyperscaler absolviert.', 5, 'Wir verfügen über hinreichend Members mit Basis-Zertifizierung für einen Hyperscaler', 17, 1131, 17, 'NUMBER', 'metric', '2024-07-16 12:05:22.158557', NULL, NULL, NULL, 1);
INSERT INTO okr_pitc.key_result VALUES (1378, NULL, NULL, '', NULL, 'Wir haben Vertriebswege für unser Angebot identifiziert und etabliert', 17, 1132, 17, NULL, 'ordinal', '2024-07-16 12:10:19.510668', 'Wir haben mit Partnern und Institutionen über das Angebot gesprochen und Kontakte geknüpft', 'Wir haben mindestens eine Institution gefunden, über die wir das Angebot lancieren können ', 'Das Angebot wurde aktiv vermarktet und es gibt Interessenten', 0);
INSERT INTO okr_pitc.key_result VALUES (1379, 0, NULL, 'Der AMM-Check wurde n-mal durchgeführt', 2, 'Wir haben das "AMM-Check"-Angebot bei einem Kunden durchgeführt', 17, 1132, 17, 'NUMBER', 'metric', '2024-07-16 12:11:27.903738', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1309, NULL, '2024-08-22 11:38:34.885131', '', NULL, 'Technologische Fokusthemen gesetzt', 33, 1111, 33, NULL, 'ordinal', '2024-06-17 23:53:55.517276', 'Themen identifiziert', '+ intern vorgestellt und Feedback geholt (SUM, ggf. LST-Monthly, CSO)', '+ Änderungen im Portfolio verankert', 1);
INSERT INTO okr_pitc.key_result VALUES (1267, NULL, '2024-08-19 05:59:03.123218', 'MO "Ditigale Lösungen: Codi https://codimd.puzzle.ch/fGy0RNSzTVqVlUz80dw_Mw
https://files.puzzle.ch/apps/files/files?dir=/swe/P32_Marketing_und_Verkauf/Digitale%20L%C3%B6sungen
', NULL, 'Unser (/mobility) Mitwirken und unsere Sales-Erwartungen zur Marktopportunität (MO) "Digitale Lösungen" sind formuliert und kommuniziert sowie mit den /dev- & /ux-Divisions abgeglichen.', 5, 1097, 5, NULL, 'ordinal', '2024-06-13 15:13:22.247492', '/mobility-Mitwirken und -Sales-Erwartungen sind mit MO "Digitale Lösungen" abgeglichen und dokumentiert', '- /mobility-Mitwirken sind mit devtre, /ruby & /ux abgestimmt
- allgemeine /dev- & /ux-Mitwirken sind formuliert
- /mobility-Sales-Erwartungen sind mit /devtre, /ruby & /ux abgestimmt
- allgemeine /dev- & /ux-Sales-Erwartungen sind mit /devtre, /ruby & /ux abgestimmt', 'Inputs und Sales-Erwartungen seitens /dev- /ux-Divisions sind:
an Sales (Yup) kommuniziert & besprochen
dem Kernteam MO "Digitale Lösungen" präsentiert
', 8);
INSERT INTO okr_pitc.key_result VALUES (1374, NULL, '2024-07-16 12:14:23.405542', '', NULL, 'Puzzle ist bei Kunden und Partnern als Know-How-Träger für kubevirt-basierte Technologien bekannt', 17, 1131, 81, NULL, 'ordinal', '2024-07-16 12:01:58.879407', 'Unsere Kunden und Partner wurden über das bestehende Know-How orientiert', 'Kunden und Partner denken beim Thema kubevirt an Puzzle', 'Wir haben Leads von Partnern/Auftäge von Kunden erhalten', 3);
INSERT INTO okr_pitc.key_result VALUES (1257, NULL, '2024-08-26 14:45:36.48457', 'Wir treiben das Ops Thema auch intern und prozessmässig weiter, das wir hier optimal und wirtschaftlich aufgestellt sind', NULL, 'Operations und Support: Inputs P35', 34, 1094, 32, NULL, 'ordinal', '2024-06-10 14:25:58.995305', 'Dokumentation überarbeitet, Inputs in P35 aus dem Team einfliessen lassen', 'Retro mit dem Team zum neuen Supportprozess', 'Alle Inputs (auch aus der Retro) sind eingeflossen und wir haben einen shiny new P35', 4);
INSERT INTO okr_pitc.key_result VALUES (1377, NULL, '2024-07-16 12:22:26.299987', '', NULL, 'Wir haben die Inhalte und den Umfang des Angebots definiert', 17, 1132, 17, NULL, 'ordinal', '2024-07-16 12:08:39.104393', 'Das Thema des Angebots ist definiert und es existieren Stichworte zum Inhalt.', 'Der Inhalt des Angebots ist schriftlich festgehalten, und ein erster Vertriebsweg ist definiert.', 'Das Beratungsangebot wurde von einem Interessenten beauftragt. ', 2);
INSERT INTO okr_pitc.key_result VALUES (1405, 70, NULL, '', 100, 'Alle Jahresbestellungen im PV für /mobility Aufträge sind vertraglich fixiert, fix im Ptime eingeplant und die Prozesse (Rechnunggstellung) im Wiki angepasst.', 20, 1138, 5, 'PERCENT', 'metric', '2024-09-13 07:12:18.077364', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1415, NULL, '2024-09-16 13:54:47.415884', 'Wir halten das monatliche Wartungsbduget von 63.5h (gemessen wird September, Oktober und November 24)  ', NULL, 'wir halten uns an das monatliche Wartungsbudget', 41, 1146, 41, NULL, 'ordinal', '2024-09-16 13:53:01.777632', '1 Monat < 63.5 h ', '2 Monate < 63.5 h ', '3 Monate < 63.5 h ', 1);
INSERT INTO okr_pitc.key_result VALUES (1383, NULL, '2024-09-17 12:26:58.483802', '', NULL, 'Sales Pipeline sichtbar machen.', 1034, 1134, 1034, NULL, 'ordinal', '2024-09-03 09:15:38.801617', 'Gewichtete Sales Pipeline für Accounts und Divisionen ist abgestimmt.', 'Sales Pipeline ist in einem ersten Draft mit Daten vorhanden.', 'Sales Pipeline ist im neuen CRM konzipiert.', 4);
INSERT INTO okr_pitc.key_result VALUES (1385, NULL, '2024-09-17 09:42:23.525035', '', NULL, 'Unser Angebotsportfolio ist marktorientierter formuliert.', 1034, 1135, 1034, NULL, 'ordinal', '2024-09-03 09:17:53.355467', 'Das Angebotsportfolio ist mit dem Leadership-Team abgestimmt.', 'Das Angebotsportfolio ist schriftlich formuliert.', 'Das Angebotsportfolio ist auf der Webseite verfügbar.', 2);
INSERT INTO okr_pitc.key_result VALUES (1393, 0, NULL, '70% der Members, kennen das Portolio und können es in 5 Minuten vorstellen. 10 Members werden befragt.
', 100, 'Unsere Members können das Puzzle-Portfolio in 5 Minuten vorstellen.
', 22, 1137, 22, 'PERCENT', 'metric', '2024-09-03 09:43:22.164021', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1394, NULL, '2024-09-17 09:30:20.421477', 'Alle Action Items wurden erledigt.', NULL, 'Das konkrete Marktangebot Platform Engineering ist formuliert und an Kund:innen und Members kommuniziert.', 24, 1135, 24, NULL, 'ordinal', '2024-09-03 09:14:41.166331', 'Das Angebot Platform Engineering ist auf der Website publiziert.', 'Konkrete (Produkt-)Angebote wie Enabling Team as a Service sind definiert und den Kunden kommuniziert. Die Zusammenarbeit über die Divisions hinweg ist definiert. Die Members kennen unser Marktangebot und identifizieren sich damit.', 'Wir haben 5 Deals generiert.', 5);
INSERT INTO okr_pitc.key_result VALUES (1407, NULL, '2024-09-17 13:33:54.818292', '', NULL, '/sys Day durchführen', 34, 1141, 34, NULL, 'ordinal', '2024-09-16 08:09:36.725703', 'Wir führen einen /sys Workshop (21.11.) zu kulturellen Themen und Operations Improvements durch', 'Die Outcomes sind festgehalten und das Team steht hinter diesen. Wir wissen wie wir unsere Zusammenarbeit in Zukunft gestalten wollen, und wie wir die Qualität für unsere Operations Kunden nachhaltig sicherstellen.', 'Die definierten Improvements zeigen erste Wirkung. Die Zufriedenheit steigt, das Knowhow wird besser verteilt im Team und wir erreichen eine höhere Qualität beim Abarbeiten von Supporttickets.', 1);
INSERT INTO okr_pitc.key_result VALUES (1413, NULL, '2024-10-15 10:39:33.300872', 'Vorgehen gemäss Meilensteinplanung ', NULL, 'Wir  gehen GO live mit dem SAC Portal 1.0
', 41, 1145, 41, NULL, 'ordinal', '2024-09-16 13:44:44.218519', '-', '1', '-', 2);
INSERT INTO okr_pitc.key_result VALUES (1391, NULL, '2024-09-18 11:35:16.494075', '', NULL, 'MO AI/ML
Wir haben eine weitere Referenz für AI Projekte und haben das Kundenangebot AI geklärt.  ', 24, 1136, 24, NULL, 'ordinal', '2024-09-03 09:25:32.130087', 'EGI Referenzcase mit OpenAI API ist fertig.
Abgleich AI Innovation mit bisheriger MO ist geklärt.', 'EGI Case mit eigener HW wurde umgesetzt.', 'Wir haben ein Kundenprojekt für einen weiteren AI Case gewonnen.', 5);
INSERT INTO okr_pitc.key_result VALUES (1403, NULL, NULL, 'Veröffentlichung und Einreichefrist für Angebot noch unklar. Allenfalls verschieben sich Ziele ins nächste Quartal.', NULL, 'Die Offerte für den Rahmenvertrag BLS IT Technologiepartner ist rechzeitig und mit hohen Erfolgsaussichten eingereicht.', 20, 1140, 20, NULL, 'ordinal', '2024-09-09 11:29:02.834163', 'Offertenteam, Aufgaben und Termine geklärt. Partnerschaften mit potenziellen Partnern abgesprochen.', 'Angebot ist fristgerecht und erfolgsversprechend einreicht.', 'Angebot bleibt auch mit höheren Rates  erfolgsversprechend.', 0);
INSERT INTO okr_pitc.key_result VALUES (1399, NULL, NULL, 'Noch unklar, ob es in diesem Quartal "nur" eine Präquali ist. Noch unklar, ob wir mit einem Partner einreichen.', NULL, 'Die SBB Software Engineering WTO haben wir fristgerecht und erfolgsversprechend eingereicht', 5, 1140, 5, NULL, 'ordinal', '2024-09-06 09:17:27.989186', 'Das Puzzle Team und deren Lead ist früh zusammenestellt und allfällige Partnerschaften geklärt', 'Das Angebot ist fristgerecht und erfolgsversprechend eingereicht.', 'Das Angebot ist fristgerecht und erfolgsversprechend eingereicht und die heutigen Rates nicht unterschritten', 0);
INSERT INTO okr_pitc.key_result VALUES (1406, NULL, NULL, '', NULL, '/sys Lead Aufgaben reviewen und optimieren', 34, 1141, 34, NULL, 'ordinal', '2024-09-16 07:53:19.113547', '/sys Lead Retro Workshop hat stattgefunden', 'Die konkreten Resultate und Veränderungen sind formalisiert dem Team kommuniziert', 'Die gewünschten Veränderungen zeigen bereits Wirkung', 0);
INSERT INTO okr_pitc.key_result VALUES (1398, NULL, '2024-09-06 09:41:20.952114', '', NULL, 'Durch geklärte Prozesse und Verantwortlichkeiten zwischen /mobility und /sales erhalten wir mehr Kraft in Offerings und gewinnen dadurch neue Aufträge.', 20, 1138, 20, NULL, 'ordinal', '2024-09-06 09:09:42.545969', 'Verantwortlichkeiten für Offerings sind geklärt.', '- Ressourcen sind eingeplant.
- Betreuung der Bestandeskunden ist definiert.
- Verantwortlichkeiten und Vorgehen für Gewinn von Neukunden ist festgelegt.', 'Sales Zusammenarbeit mit anderen Dev. Divisons ist optimiert....', 1);
INSERT INTO okr_pitc.key_result VALUES (1386, NULL, '2024-09-06 11:57:49.428718', 'Wir stehen mittlerweile vor dem Abschluss der Partnerschaften. In diesem Quartal wollen wir diese abschliessen und kommunizieren sowie weitere Leads dazu gewinnen.', NULL, 'MO Hyperscaler
Wir sind Partner von AWS und GCP und haben attraktive Leads generiert.', 16, 1136, 16, NULL, 'ordinal', '2024-09-03 09:19:47.592067', 'Wir schliessen die GCP Partnerschaft ab und kommunizieren diese.', 'Wir schliessen AWS Partnerschaft ab und kommunizieren diese. Wir überführen die Hyperscaler Themen in die relevanten Unternehmensprozesse (Z.B. Opportunity-Erfassung im Verkaufs- und/oder Auftragsabwicklung).', 'Pro Hyperscaler Partner erfassen wir je zwei Deals/Optys in den Partner-Portalen, je einen davon launchen wir. DL Deal Value: total CHF >30''000.-', 4);
INSERT INTO okr_pitc.key_result VALUES (1387, NULL, '2024-09-17 09:31:19.897161', 'Ziel wir erkennen auf Stufe Unternehmen, ob bei allen MO genügend Zeit investiert wird.', NULL, 'Der Investitionsbedarf unserer Marktopportunitäten ist bekannt und wird genutzt.', 13, 1136, 13, NULL, 'ordinal', '2024-09-03 09:22:35.230087', 'Alle Marktopportunitätsowner:innen überprüfen ihren Investitionsbedarf.', 'Wir überprüfen monatlich den Fortschritt unserer Marktopportunitäten.', 'Wir können in allen Marktopportunitäten mindestens einen Lead generieren.', 4);
INSERT INTO okr_pitc.key_result VALUES (1408, NULL, NULL, '', NULL, '/sys Ops besser positionieren', 34, 1142, 34, NULL, 'ordinal', '2024-09-16 08:44:00.924958', 'Wir überlegen uns wie wir das Operations Angebot noch besser kommunizieren und bekannt machen können', 'Mind. 1 Marketing Aktion in konkreter Planung', 'Marketing Aktion durchgeführt', 0);
INSERT INTO okr_pitc.key_result VALUES (1409, NULL, NULL, '', NULL, 'Inputs Platform Engineering Angebot', 34, 1142, 34, NULL, 'ordinal', '2024-09-16 08:45:44.279207', 'Wir lieferen unsere Inputs zur Formulierung des Platform Engineerin Angebots', 'Das /sys Angebot fliesst in die MO ein', 'Es kommen erste Anfragen für /sys via der MO', 0);
INSERT INTO okr_pitc.key_result VALUES (1416, NULL, '2024-09-16 13:58:51.904029', 'Unsere Wartungskosten sind nicht gedeckt. es wird deshalb ein Konzept erstellt und anschliessend neue Verträge SLA ausgearbeitet und verhandelt', NULL, 'wir erhöhen den Ertrag der Pauschalen (Betrieb, Wartung und Support)
Jahresziel 2025 -> Rentabilität', 41, 1146, 41, NULL, 'ordinal', '2024-09-16 13:53:01.77868', 'SLA Vorlage erarbeitet ', 'Wartungskosten pro Kunde sind definiert -', 'Wartungskosten und Strategie ist definiert
SLA erarbeitet', 1);
INSERT INTO okr_pitc.key_result VALUES (1397, NULL, '2024-09-17 11:59:04.580838', '', NULL, 'Das bestehende /mobility Know-How und Angebot ist überprüft und marktorientiert für Mobilitäts- und weitere Branchen beschrieben', 5, 1139, 5, NULL, 'ordinal', '2024-09-06 09:07:55.153441', 'Ein One Pager des neu beschriebenen Angebots ist mit Marketing und CSO erstellt', 'Der One Pager ist mit Feedback von je drei Branchen internen und Branchen fremden Kunden überarbeitet', 'Das neue beschriebene Angebot ist auf der Webseite ', 2);
INSERT INTO okr_pitc.key_result VALUES (1423, NULL, '2024-09-30 08:41:06.981984', 'Falls der Ansatz nicht erfolgsversprechend ist, wird bei Commit-Zone abgebrochen.
', NULL, 'Angebot UX / Platform Engineering etabliert', 3, 1148, 36, NULL, 'ordinal', '2024-09-16 14:28:12.140781', 'Die Idee und Zusammenarbeit ist getestet. (Workshop durchgeführt, internes Research Projekt abgeschlossen und Blogbeitrag über Key Findings geschrieben)', 'Ein Angebot ist definiert, Rollen sind klar verteilt.', '2 weitere Workshops sind verkauft inkl. UX Stunden', 5);
INSERT INTO okr_pitc.key_result VALUES (1427, NULL, NULL, 'Auslastung gemäss PTime-Planung', NULL, 'Neuerliche Anstellung eines System Engineers', 33, 1150, 33, NULL, 'ordinal', '2024-09-16 22:08:09.792281', 'Anstellung abgeschlossen', 'Commit + Auslastung von über 33% in ersten beiden Anstellungsmonaten', 'Commit + Auslastung von über 66% in ersten beiden Anstellungsmonaten', 0);
INSERT INTO okr_pitc.key_result VALUES (1428, NULL, NULL, 'Referenzberichte müssen publiziert sein.', NULL, '3 Referenzberichte verfassen und publizieren (mind. einer zu Hyperscaler)', 33, 1151, 33, NULL, 'ordinal', '2024-09-16 22:09:42.023463', '1 Referenzbericht', '2 Referenzberichte (mind. einer davon für MO Hypersclaer)', '3 Referenzberichte (mind. einer davon für MO Hypersclaer)', 0);
INSERT INTO okr_pitc.key_result VALUES (1429, NULL, NULL, '', NULL, 'Technologische Fokusthemen definiert', 33, 1151, 33, NULL, 'ordinal', '2024-09-16 22:12:18.618498', 'Themen identifiziert und Team vorgestellt', '+ intern vorgestellt und Feedback geholt (SUM, ggf. LST-Monthly, CSO) ', '+ Änderungen im Portfolio verankert ', 0);
INSERT INTO okr_pitc.key_result VALUES (1439, NULL, NULL, 'https://www.srf.ch/kultur/gesellschaft-religion/schweizer-sportverbaende-immer-weniger-vereine-aber-immer-mehr-freiwilligenarbeit', NULL, 'Marketingkampagne an föderalistische Vereine der Schweiz ist durchgeführt', 41, 1146, 41, NULL, 'ordinal', '2024-09-17 06:45:06.516464', 'Massnahme 1. + 2. sind umgsetzt', 'Mailing ist versendet', 'wir haben bereits 3 Responses ', 0);
INSERT INTO okr_pitc.key_result VALUES (1381, NULL, '2024-09-17 10:28:59.989782', '', NULL, 'Auswahl der Fokuskunden für Account Management 2.0 definiert.', 1034, 1134, 1034, NULL, 'ordinal', '2024-09-03 09:13:33.62746', 'Wir führen ein Account Management 2.0 ein und eine erste Auswahl der Kund:innen ist getroffen.', '12 Accounts sind mit der GL und den Divisions definiert.', 'Zu jedem Account ist die Account Manager:in definiert und es besteht ein Ausblick für das Jahr 2025.', 3);
INSERT INTO okr_pitc.key_result VALUES (1434, 2, '2024-09-23 12:09:33.672888', 'Unsere Members gestalten aktiv /mid, indem die folgenden 5 Möglichkeiten zur Mitarbeit genutzt werden. Als Strech Goal (7) dienen weitere, noch nicht definierte Möglichkeiten.', 7, 'Members nutzen 5 Möglichkeiten, /mid zu gestalten', 40, 1155, 40, 'NUMBER', 'metric', '2024-09-17 06:26:07.57214', NULL, NULL, NULL, 4);
INSERT INTO okr_pitc.key_result VALUES (1436, NULL, '2024-09-26 11:46:51.225159', 'Mit definieren meinen wir: ist auf Papier niedergeschrieben, alle beteiligten Personen kennen das Angebot und können es verkaufen.', NULL, 'Wir definieren 3 Angebotspakete im Platform Engineering', 40, 1154, 31, NULL, 'ordinal', '2024-09-17 06:28:13.309594', 'Die neue Landingpage für Platform Engineering ist auf der Website publiziert.', 'Drei konkrete Angebote wie bspw. "Enabling Team as a Service" und "Discovery Workshop 2.0" sind definiert und in unsere Slide Decks integriert & auf der Website publiziert.
', ' Wir haben 5 Leads generiert auf Grund dieser Angebote.', 4);
INSERT INTO okr_pitc.key_result VALUES (1443, NULL, '2024-09-17 12:07:24.817256', 'Geteiltes OKR mit Mobility (BW,JH)', NULL, 'Das bestehende /mobility Know-How und Angebot ist überprüft und marktorientiert für Mobilitäts- und weitere Branchen beschrieben', 1039, 1143, 1039, NULL, 'ordinal', '2024-09-17 12:06:13.575027', 'Ein One Pager des Angebots ist mit Marketing/CSO erstellt.', 'Der One Pager ist mit Feedback von je drei Branchen internen und Branchen fremden Kunden validiert.', 'Das Angebot ist auf der Webseite kommuniziert.', 1);
INSERT INTO okr_pitc.key_result VALUES (1422, NULL, '2024-09-17 12:19:01.039342', 'Im letzten Cycle definiert: Trennung Angebot wac.ch (Konzept basis erstellt) / puzzle.ch (digitale Lösungen) - wir gehen in die Umsetzung', NULL, 'Definition und Ausgestaltung Stossrichtung wearecube', 3, 1148, 3, NULL, 'ordinal', '2024-09-16 14:23:55.790215', 'Konsens über Claim, Angebot und Slogans', 'Wireframes für die evolutionäre Anpassung von WAC sind gemacht', 'Die neue Seite ist umgesetzt', 1);
INSERT INTO okr_pitc.key_result VALUES (1432, NULL, '2024-09-23 19:18:49.012461', 'Berücksichtigte SIGs:
- Kubernetes
- Build-CI
- GitOps
- Security
- DevX

Nicht berücksichtigte SIGs*:
- Observability
- Cloud

*hier sind wir noch nicht so weit', NULL, 'Jede SIG hat ein klar definiertes Ziel, das intern kommuniziert ist', 40, 1153, 40, NULL, 'ordinal', '2024-09-17 06:22:44.201739', 'Jede SIG hat ihr Ziel im README formuliert', 'Alle SIGs haben ein Grobkonzept geschrieben.', 'Alle SIGs haben ein Detailkonzept, welches eine detaillierte Implementation beschreibt.', 3);
INSERT INTO okr_pitc.key_result VALUES (1417, 44, '2024-10-08 10:52:18.316538', 'Gemäss PTime, es gilt der Wert der ab Start Zyklus, nur auf verrechenbaren Pensen (ohne Berti / Mayra / Simu BL-Pensum). Messung ab Start OKR-Cycle: 1. Oktober
', 75, 'Wir steigern unsere Auslastung der verrechenbaren Rollenanteile.', 3, 1130, 3, 'PERCENT', 'metric', '2024-09-16 13:58:09.236665', NULL, NULL, NULL, 3);
INSERT INTO okr_pitc.key_result VALUES (1430, 0, '2024-10-28 08:37:44.141911', 'Abschlüsse Sep-Nov gemäss Divisionsrentabilitätsrechnung. Baseline: durchschnittl. 10%. Kumulierte Messung (Strecht ist 3*18=54)', 54, 'Durchschnittl. 18% Marge im GJ/Q2', 33, 1149, 33, 'PERCENT', 'metric', '2024-09-16 22:20:33.933198', NULL, NULL, NULL, 2);
INSERT INTO okr_pitc.key_result VALUES (1410, NULL, '2024-10-07 11:22:50.477048', 'Aktuell gibt es viele interne Kommunikationskanäle (Wiki, Docs, Chat, Newsportal, Infoanlässe). Unser Ziel ist es, diese Kommunikationskanäle zu überprüfen, zu vereinfachen und zu bündeln.  ', NULL, 'Wir vereinfachen die interne Kommunikation.', 26, 1144, 49, NULL, 'ordinal', '2024-09-16 10:51:01.88022', 'Wir aktualisieren das Konzept der internen Kommunikation und zeigen konkrete Massnahmen zur Vereinheitlichung auf. ', 'Das Konzept zur Vereinheitlichung der internen Kommunikationskanäle wurde der GL präsentiert und von dieser verabschiedet. ', '50 Prozent der definierten Massnahmen zur Vereinheitlichung der internen Kommunikation sind geplant oder bereits umgesetzt. ', 4);
INSERT INTO okr_pitc.key_result VALUES (1449, NULL, '2024-09-17 12:45:11.417008', '', NULL, 'Wir sind ein super High-Performance Team', 28, 1158, 28, NULL, 'ordinal', '2024-09-17 12:38:07.550475', 'Wir haben unser Verständnis von High-Performance Team, Ausgangslage und Problemfelder sind identifiziert', 'Soll-Konzept ist definiert inkl. Massnahmen', 'Erste spürbare Umsetzungen sind erfolgt', 1);
INSERT INTO okr_pitc.key_result VALUES (1444, NULL, '2024-09-17 12:32:44.540505', 'Ziel von diesem KR sind nicht direkt finanzielle Einsparungen. Wartungsbudget soll sinnvoll eingesetzt werden um mögliche Techschulden zu vermeiden.', NULL, 'Minimieren Wartungsaufwand durch coole Massnahmen und maximieren Ertrag', 28, 1157, 28, NULL, 'ordinal', '2024-09-17 12:30:53.279474', 'Priorisierte Massnahmenliste nach Kosten/Nutzen ist definiert ', 'Top 3 Prio Massnahmen sind umgesetzt ', 'Top 5 Prio Massnahmen sind umgesetzt ', 1);
INSERT INTO okr_pitc.key_result VALUES (1445, NULL, '2024-09-17 12:33:33.928009', 'Applikationen: socialweb, hitobito (ev pro Instanz), BAFU SAM, swissunihockey, decidim (ev pro Instanz), Brixel, PuzzleTime, Skills, Cryptopus
', NULL, 'Wir haben ein Application Lifecycle Management für unsere Applikationen', 28, 1157, 28, NULL, 'ordinal', '2024-09-17 12:30:53.280198', 'Erster Entwurf für ALM pro Applikation ', 'ALM ist pro Applikation bekannt ', 'ALM ist mit Kunden abgesprochen', 1);
INSERT INTO okr_pitc.key_result VALUES (1421, NULL, '2024-09-17 12:33:43.282764', 'Der Forecast 2025 ist so gut, dass wir eine:n weitere:n UX Experten/Expertin anstellen könnten.
Zusätzlich zu den anderen Zielen haben wir eine gefüllte Pipeline bereits im 2024 - auf dieser Pipeline wollen wir aufbauen.
Baseline-Auftrag: Rahmenvertrag mit mindestens 50% Zeit bereits verplant. ', NULL, 'Solider Forecast bis Mitte 2025', 3, 1147, 36, NULL, 'ordinal', '2024-09-16 14:20:12.173007', 'Alle Members haben einen Baseline-Auftrag bis mindestens im Sommer 2025', 'Alle Members haben einen Baseline-Auftrag bis mindestens im Sommer, wir haben vielversprechende Offerten offen. ', 'Wir optimieren und müssen bereits im Q1 2025 eine Stelle ausschreiben', 3);
INSERT INTO okr_pitc.key_result VALUES (1448, NULL, NULL, 'Dev/tre Bild ist neu gezeichnet und wird verstanden
', NULL, '/dev/tre als Bereich ist visualisiert und verstanden', 4, 1156, 4, NULL, 'ordinal', '2024-09-17 12:36:30.989557', 'Einordnung der Rollen, Klären der Einsätze, Klären des Unterschieds Bereich-/ Projekt-Team, Begreifen eines agilen Teams, Zeichnen des Bildes
', 'Ab November wird devtre-Bild als Instrument im Weekly genutzt, Bild mit Sales Challengen
', 'Das Resultat sind dem GL-Body und dem LST präsentiert, ein News-Beitrag dazu ist geschrieben
', 0);
INSERT INTO okr_pitc.key_result VALUES (1452, NULL, '2024-10-18 08:11:27.136029', 'Wir haben geklärt in welchen Themen wir (/dev/tre) Expertisen/ Lücken haben rund um das Thema Digitale Lösungen (Individualsoftwareentwicklung) ', NULL, 'Unsere Expertisen und Lücken für die MO "Digitale Lösungen" sind erfasst', 4, 1159, 4, NULL, 'ordinal', '2024-09-17 12:45:04.195077', 'Ermitteln der Expertisen anhand der Sammlung "Was wir dazu beitragen" pro Member - Daraus Ableitung der Lücken - Dokumentation Expertisen & Lücken pro Bereich', 'Abgleich und Abstimmung der Expertisen & Lücken mit dev- & ux-Bereichen - Absprache mit Sales hinsichtlich Company-KR "Wir haben ein attraktives Zielbild für die MO Digitale Lösungen und eine realistische Roadmap."', 'Angebot für Bereich ist formuliert', 2);
INSERT INTO okr_pitc.key_result VALUES (1451, NULL, '2024-09-17 12:55:35.890576', 'Zählt auf GL Objective 2: Wir verstehen unser Portfolio und wissen welchen Mehrwert wir Kunden bringen.', NULL, 'Unser marktorientiertes Angebotsportfolio ist klar definiert und wird strategisch genutzt', 28, 1158, 28, NULL, 'ordinal', '2024-09-17 12:41:27.261335', 'Wir kennen unser eigenes Angebotsportfolio was wir heute machen', 'Wir definieren was wir zukünftig machen können/wollen', 'Unser Angebot ist in das Puzzle Angebot eingeflossen', 2);
INSERT INTO okr_pitc.key_result VALUES (1450, NULL, '2024-09-17 12:56:05.542201', 'Zählt auf GL Objective 1: Unsere Salespipeline ist voll und unsere Membersliste leer.', NULL, 'Ab ins Jahr 2025', 28, 1158, 28, NULL, 'ordinal', '2024-09-17 12:39:28.793646', 'Kundenaustausch mit wichtigsten Kunden hat stattgefunden.', 'Die wichtigsten Kunden sind geplant', 'Wir sind bereit für ein erfolgreiches Jahr 2025', 1);
INSERT INTO okr_pitc.key_result VALUES (1446, NULL, '2024-10-18 08:24:53.652607', 'Fokusthema “Ziele” ist vertieft behandelt und definiert.
', NULL, 'Die Ziele des Bereiches sind verhandelt und niedergeschrieben', 4, 1156, 4, NULL, 'ordinal', '2024-09-17 12:34:37.00393', '- Behandlung in Quartalsworkshop 22. Oktober 2024
- Mit /dev/tre-Manifest und Puzzle-Werten abgestimmt', 'Die Regeln zu «Ziele» sind erarbeitet und niedergeschrieben. Massnahmen und Messpunkte sind definiert
', 'Die Resultate sind dem GL-Body und dem LST präsentiert, ein News-Beitrag dazu ist geschrieben
', 2);
INSERT INTO okr_pitc.key_result VALUES (1447, NULL, '2024-10-18 08:25:31.373589', 'Fokusthema “Verantwortung” ist vertieft behandelt und definiert
', NULL, 'Die Verantwortlichkeiten und Rollen innerhalb /dev/tre sind verhandelt und niedergeschrieben', 4, 1156, 4, NULL, 'ordinal', '2024-09-17 12:35:26.530513', '- Behandlung in Quartalsworkshop 22. Oktober 2024
- Mit Manifest, Puzzelwerten, etc abgestimmt
', 'Die Regeln zu «Verantwortung» sind erarbeitet und niedergeschrieben. Massnahmen und Messpunkte sind definiert
', 'Die Resultate sind dem GL-Body und dem LST präsentiert, ein News-Beitrag dazu ist geschrieben
', 2);
INSERT INTO okr_pitc.key_result VALUES (1486, 2, NULL, '324', 2, '234', 6, 1159, 6, 'FTE', 'metric', '2024-11-01 11:00:01.898306', NULL, NULL, NULL, 0);
INSERT INTO okr_pitc.key_result VALUES (1412, 0, '2024-11-04 12:55:44.014153', 'Commit (0.3): 3 Gespräche sind durchgeführt; Target (0.7): 5 Gespräche sind durchgeführt; Stretch (1.0): alle Gespräche sind durchgeführt; Stichtag 19.12.24
INSI
PBSC -> gemäss PBS Gespräch keines durchführen in 2024
JUBL
CEVI
SJAS
JEMK
PRON
SBVE
GLP
SWW -> wurde am 24.10.24 durchgeführt
CVPS
SKVC
DSJ
SVSE
BFHI
(Kunden abholen) ', 15, 'Wir führen die Account Management Gespräche mit unseren Kunden', 41, 1145, 41, 'EUR', 'metric', '2024-09-16 13:44:44.213198', NULL, NULL, NULL, 5);


--
-- Data for Name: objective; Type: TABLE DATA; Schema: okr_pitc; Owner: okr-test
--

INSERT INTO okr_pitc.objective VALUES (54, '2023-06-20 12:33:17.632481', '', 'Wir steigern die Wirtschaftlichkeit von WAC', 36, 7, 24, 'ONGOING', NULL, '2023-06-20 12:33:17.632481', 1);
INSERT INTO okr_pitc.objective VALUES (39, '2023-06-09 09:28:58.865895', '', 'Wir erreichen eine gesunde Wirtschaftlichkeit', 5, 7, 15, 'ONGOING', NULL, '2023-06-09 09:28:58.865895', 1);
INSERT INTO okr_pitc.objective VALUES (1025, '2024-03-18 18:47:43.778239', 'Raus aus der Stube! Wir wollen am Markt und bei unseren Kunden präsent sein!', 'We Are Cube ist im Spotlight  - Wir erhöhen unsere Präsenz am Markt!', 36, 9, 24, 'SUCCESSFUL', 36, '2023-12-19 12:52:15.379968', 2);
INSERT INTO okr_pitc.objective VALUES (50, '2023-06-26 07:32:43.506323', '', '/sys stellt sich auf für Operationskunden', 34, 7, 22, 'ONGOING', NULL, '2023-06-26 07:32:43.506323', 1);
INSERT INTO okr_pitc.objective VALUES (44, '2024-05-29 05:35:38.61899', '', 'Die Marktopportunitäten sind etabliert und erreichen einen sichtbaren Fortschritt', 16, 7, 14, 'NOTSUCCESSFUL', 16, '2023-06-14 10:42:03.621643', 2);
INSERT INTO okr_pitc.objective VALUES (1090, '2024-09-10 11:40:38.025946', '', 'Durch Kosteneinsparungen und Wachstum verbessern wir die Wirtschaftlichkeit.', 5, 1001, 14, 'NOTSUCCESSFUL', 13, '2024-06-04 08:16:36.494054', 3);
INSERT INTO okr_pitc.objective VALUES (1005, '2024-03-18 12:27:25.364451', 'Die identifizierten Marktopportunitäten sollen wie bekannt zu zukünftigen Umsatztreibern werden. Einzelne davon tragen auch heute bereits wesentlich zum Umsatz bei. Damit wir im GJ Q4 eine Entscheidungsgrundlage haben, welche Marktopportunitäten wir weiter führen, möchten wir in diesem Quartal die Messgrundlage dazu schaffen. Dennoch wollen wir wo immer möglich auch den Umsatz durch die Marktopportunitäten steigern.', 'Wir messen und steigern die Erträge durch die Marktopportunitäten und schaffen Entscheidungsgrundlagen.', 16, 9, 14, 'SUCCESSFUL', 24, '2023-12-12 13:07:32.566823', 3);
INSERT INTO okr_pitc.objective VALUES (1021, '2024-03-19 08:33:03.043987', '', 'Wir begehen neue Wege bei der Akquisition von Aufträgen.', 24, 9, 14, 'NOTSUCCESSFUL', 24, '2023-12-19 12:27:19.521623', 5);
INSERT INTO okr_pitc.objective VALUES (104, '2023-12-18 14:34:09.179003', '', 'Wir steigern die Wirtschaftlichkeit von WAC (mittelfristig)', 36, 8, 24, 'NOTSUCCESSFUL', 36, '2023-09-19 13:42:06.37611', 2);
INSERT INTO okr_pitc.objective VALUES (101, '2023-12-19 08:53:58.386409', '', 'Wir formen und stabiliseren das Team devtre und fördern die Selbstorganisation', 4, 8, 20, 'SUCCESSFUL', 4, '2023-09-19 13:23:16.440378', 2);
INSERT INTO okr_pitc.objective VALUES (88, '2023-12-19 08:55:26.723704', 'Die Einführung der Rentabilitätsrechnung wird im Q2 Aufschlüsse darüber geben, wie rentabel die Division arbeitet. Wir analysieren und optimieren unsere Zahlen.', 'Wir tragen mit einer rentablen Division zum Geschäftserfolg bei', 33, 8, 23, 'SUCCESSFUL', 33, '2023-09-18 11:48:39.892218', 2);
INSERT INTO okr_pitc.objective VALUES (98, '2023-12-19 09:00:42.252945', '', 'Wir arbeiten weiter aktiv an der Zufriedenheit und Puzzleness bei /mid', 31, 8, 21, 'SUCCESSFUL', 40, '2023-09-22 08:26:47.815444', 2);
INSERT INTO okr_pitc.objective VALUES (90, '2023-12-19 09:03:55.929889', '', 'New Tech ist etabliert', 32, 8, 22, 'SUCCESSFUL', 67, '2023-09-19 13:53:05.943392', 2);
INSERT INTO okr_pitc.objective VALUES (1008, '2024-03-19 06:59:06.514904', 'Mit dem KR "Umsatz" zahlen wir direkt auf das Company Objective Marktopportitäten und insb. auf dessen KR "Potential messen" ein.', 'Wir generieren erste Erträge durch MLOps.', 5, 9, 15, 'SUCCESSFUL', 5, '2023-12-18 07:41:16.029954', 8);
INSERT INTO okr_pitc.objective VALUES (1091, '2024-09-10 11:51:12.368318', '', 'Die Marktopportunitäten für das neue Geschäftsjahr sind lanciert und die Basis ist gelegt.', 5, 1001, 14, 'SUCCESSFUL', 13, '2024-06-04 08:16:49.746375', 4);
INSERT INTO okr_pitc.objective VALUES (1093, '2024-09-16 08:49:07.600943', '', 'Durch Kosteneinsparungen und Wachstum verbessern wir die Wirtschaftlichkeit', 34, 1001, 22, 'SUCCESSFUL', 34, '2024-06-10 14:14:48.823466', 1);
INSERT INTO okr_pitc.objective VALUES (1032, '2024-03-19 07:03:02.249015', '', 'Hohe Teamzufriedenheit', 33, 9, 23, 'NOTSUCCESSFUL', 33, '2023-12-19 13:54:47.253957', 4);
INSERT INTO okr_pitc.objective VALUES (1094, '2024-09-16 08:49:23.386923', '', 'MO: /sys Inputs', 34, 1001, 22, 'SUCCESSFUL', 34, '2024-06-10 14:22:26.11855', 1);
INSERT INTO okr_pitc.objective VALUES (1098, '2024-09-17 06:02:25.393083', 'Wir starten durch im Thema Platform Engineering', 'Platform Engineering goes brrr 🚀', 40, 1001, 21, 'NOTSUCCESSFUL', 40, '2024-06-14 13:16:40.662286', 6);
INSERT INTO okr_pitc.objective VALUES (1040, '2024-03-19 08:19:18.042032', '', 'Wir finden uns als Team und etablieren unsere eigenen Arbeitsabläufe.', 27, 9, 18, 'SUCCESSFUL', 27, '2024-01-15 07:49:46.017731', 1);
INSERT INTO okr_pitc.objective VALUES (1103, '2024-09-17 07:35:51.249161', '', 'Wir bringen frischen Wind in unseren Online-Auftritt.', 26, 1001, 25, 'SUCCESSFUL', 26, '2024-06-17 11:08:49.869808', 2);
INSERT INTO okr_pitc.objective VALUES (1073, '2024-06-18 08:31:48.364712', 'Wir wollen so aufgestellt sein, dass auch bei Ferienabwesenheiten u.ä. die Tätigkeiten von /security weiterlaufen können.', 'Wir entwickeln /security weiter', 27, 1000, 18, 'SUCCESSFUL', 27, '2024-03-19 09:12:45.581936', 3);
INSERT INTO okr_pitc.objective VALUES (1077, '2024-06-14 08:12:00.689109', '', 'Unsere Website ist bereit für das Go-Live am 1. Juli 2024. ', 26, 1000, 25, 'SUCCESSFUL', 26, '2024-03-19 11:52:41.575107', 3);
INSERT INTO okr_pitc.objective VALUES (1081, '2024-06-14 13:05:44.70548', 'Wir arbeiten daran, dass wir effizienter werden in unserem Daily Business.', 'Wir werden effizienter ⚙️', 60, 1000, 21, 'SUCCESSFUL', 31, '2024-03-19 13:04:25.07092', 4);
INSERT INTO okr_pitc.objective VALUES (1050, '2024-06-13 06:34:31.759701', '', 'Wir haben mit den zusätzlichen Members volle Auftragsbücher für den Sommer.', 5, 1000, 14, 'SUCCESSFUL', 16, '2024-03-04 15:12:46.099519', 7);
INSERT INTO okr_pitc.objective VALUES (1085, '2024-04-08 08:39:51.880943', '', 'Wir möchten mehr Pflanzen', 7, 1000, 1000, 'ONGOING', 7, '2024-04-08 08:39:25.798978', 1);
INSERT INTO okr_pitc.objective VALUES (1086, '2024-04-17 15:59:19.195662', 'Wir schaffen die Voraussetzung, um zukünftig eigenständig Softwareentwicklungsprojekte durchführen zu können', 'okr_pitc:DE will Entwicklung!', 17, 1000, 28, 'ONGOING', 17, '2024-04-17 15:50:32.763781', 2);
INSERT INTO okr_pitc.objective VALUES (42, '2024-05-29 05:34:41.579936', '', 'Wir arbeiten gerne bei Puzzle und schaffen ein positives internes Bild', 24, 7, 14, 'SUCCESSFUL', 16, '2023-06-14 09:11:09.406672', 2);
INSERT INTO okr_pitc.objective VALUES (1058, '2024-06-07 11:39:03.580716', '', 'Wir verankern unser Umweltengagement mittels Zertifizierung ISO 14001 nachhaltig  ', 41, 1000, 26, 'SUCCESSFUL', 41, '2024-03-15 09:22:25.329866', 3);
INSERT INTO okr_pitc.objective VALUES (1060, '2024-06-13 06:18:25.433172', 'Wir wollen richtig gut ausgelastet sein.', 'Wir haben volle Auftragsbücher für den Sommer', 32, 1000, 22, 'SUCCESSFUL', 67, '2024-03-18 09:21:24.829786', 1);
INSERT INTO okr_pitc.objective VALUES (1056, '2024-06-13 06:26:20.389914', 'Jahresstrategie 2024', 'Wir erhöhen die Rentabilität', 41, 1000, 26, 'NOTSUCCESSFUL', 41, '2024-03-15 08:43:59.822526', 8);
INSERT INTO okr_pitc.objective VALUES (1053, '2024-06-13 13:03:31.500959', '', 'Wir haben die /mobility Strategie kommuniziert, deren Umsetzung geplant und das Controlling aufgesetzt.', 5, 1000, 15, 'SUCCESSFUL', 5, '2024-03-07 14:39:32.865826', 2);
INSERT INTO okr_pitc.objective VALUES (1078, '2024-06-14 07:51:33.731057', '', 'Wir optimieren unsere Sponsoring- und Eventauftritte.', 26, 1000, 25, 'SUCCESSFUL', 26, '2024-03-19 11:52:50.589197', 2);
INSERT INTO okr_pitc.objective VALUES (1036, '2024-03-13 09:36:21.20851', '', 'Happy Ruby Members', 28, 9, 19, 'SUCCESSFUL', 28, '2023-12-19 14:18:46.947564', 1);
INSERT INTO okr_pitc.objective VALUES (83, '2023-12-14 14:45:09.615067', '', 'Wir steigern die Attraktivität der Division /mobility', 51, 8, 15, 'NOTSUCCESSFUL', 5, '2023-09-19 12:07:45.999932', 2);
INSERT INTO okr_pitc.objective VALUES (55, '2023-06-20 12:34:11.120224', '', 'Harder, Better, Faster, Stronger', 3, 7, 24, 'ONGOING', NULL, '2023-06-20 12:34:11.120224', 1);
INSERT INTO okr_pitc.objective VALUES (84, '2023-12-14 14:49:23.255996', '', 'Wir bringen unsere Wirtschaftlichkeit auf das nächste Level', 5, 8, 15, 'NOTSUCCESSFUL', 5, '2023-09-19 12:40:20.664636', 2);
INSERT INTO okr_pitc.objective VALUES (1037, '2024-03-13 09:37:12.493206', '', 'Die Planung des Ruby Teams ist perfektioniert', 28, 9, 19, 'SUCCESSFUL', 28, '2023-12-19 14:20:44.889548', 2);
INSERT INTO okr_pitc.objective VALUES (1014, '2024-03-18 15:42:09.099692', '', 'Wir lasten alle /dev/tre-Members nachhaltig aus', 4, 9, 20, 'NOTSUCCESSFUL', 4, '2023-12-18 15:54:41.288827', 1);
INSERT INTO okr_pitc.objective VALUES (1082, '2024-06-17 15:30:27.282231', '', 'We Are Cube ist im Spotlight - Wir erhöhen unsere Präsenz am Markt', 36, 1000, 24, 'SUCCESSFUL', 36, '2024-03-19 13:07:26.109211', 4);
INSERT INTO okr_pitc.objective VALUES (1029, '2024-03-19 08:20:34.245245', 'Wir konkretisieren unser New Tech Angebot (Dagger & Cilium)', 'New-Tech Round 2 🥊', 40, 9, 21, 'NOTSUCCESSFUL', 40, '2023-12-19 13:20:25.698716', 5);
INSERT INTO okr_pitc.objective VALUES (87, '2023-12-19 08:53:02.759894', 'Im Herbst stellen sich die wichtigsten Weichen für die Auslastung im 2024. Wir wollen im Q2 bereits eine hohe Teamauslastung für den Jahresanfang 2024 erzielen.
', 'Wir füllen die Bücher für 2024', 33, 8, 23, 'SUCCESSFUL', 33, '2023-09-18 11:23:51.11679', 2);
INSERT INTO okr_pitc.objective VALUES (78, '2023-12-19 08:53:15.706087', '', 'Wir schaffen eine gemeinsame (Vertrauens-)Basis mit unseren Techies.', 26, 8, 25, 'SUCCESSFUL', 26, '2023-09-19 11:40:49.646833', 2);
INSERT INTO okr_pitc.objective VALUES (79, '2023-12-19 08:53:21.701216', '', 'Wir optimieren unsere Inhaltsstrategie, um noch mehr Leads zu generieren. ', 26, 8, 25, 'SUCCESSFUL', 26, '2023-09-19 11:39:57.437991', 2);
INSERT INTO okr_pitc.objective VALUES (49, '2023-06-20 12:23:16.695538', 'Im Q1 werden für mehrere Devs im Team (Andres, David, Yelan) neue Projekte oder spannende Mandate fällig. Der Salesfokus liegt entsprechend im Dev-Bereich.', 'Spannende nächste Etappe für Devs', 33, 7, 23, 'ONGOING', NULL, '2023-06-20 12:23:16.695538', 1);
INSERT INTO okr_pitc.objective VALUES (109, '2023-12-19 08:57:19.50951', 'Derzeit erhalten wir fast alle Aufträge von einem Auftraggeber/Partner. Diese Abhängigkeit soll auf weitere Auftraggeber verteilt werden.', 'Wir legen den Grundstein um von nur einem Auftraggeber unabhängiger zu werden ', 17, 8, 28, 'NOTSUCCESSFUL', 17, '2023-09-28 13:01:47.069105', 2);
INSERT INTO okr_pitc.objective VALUES (1022, '2024-03-18 18:40:24.113717', '', 'Wir steigern die Wirtschaftlichkeit von WAC', 36, 9, 24, 'NOTSUCCESSFUL', 36, '2023-12-19 12:34:30.900297', 2);
INSERT INTO okr_pitc.objective VALUES (1092, '2024-09-10 11:50:21.743791', 'Wir machen erste Schritte in den MOALs Organisation der Zukunft und CRM Ablösung', 'Wir reaktivieren OrgDev und starten mit dem CRM Projekt.', 5, 1001, 14, 'SUCCESSFUL', 13, '2024-06-04 09:10:55.060774', 4);
INSERT INTO okr_pitc.objective VALUES (1009, '2024-03-19 07:00:56.835314', '', 'Mit gezielten Sales Aktivitäten holen wir kurzfristig neue Aufträge und verbessern langfristig die Kunden- & Partnerbeziehung.', 5, 9, 15, 'NOTSUCCESSFUL', 5, '2023-12-18 07:41:33.919798', 6);
INSERT INTO okr_pitc.objective VALUES (1100, '2024-09-16 05:35:10.520191', '', 'Wir erhöhen die Kundenzufriedenheit', 41, 1001, 26, 'SUCCESSFUL', 41, '2024-06-17 06:46:15.168927', 4);
INSERT INTO okr_pitc.objective VALUES (1035, '2024-03-19 08:20:21.021694', 'Wir steuern mit Allem was wir haben und können einer gesunden Wirtschaftlichkeit bei.', 'Chöölleeeeee 🤑', 31, 9, 21, 'SUCCESSFUL', 40, '2023-12-19 14:18:41.580759', 7);
INSERT INTO okr_pitc.objective VALUES (1084, '2024-06-17 15:32:42.47863', '', 'We Are Calibrating', 36, 1000, 24, 'SUCCESSFUL', 36, '2024-03-19 13:14:17.045939', 2);
INSERT INTO okr_pitc.objective VALUES (1041, '2024-03-19 08:20:56.958627', '', 'Wir definieren und erledigen Tasks für unsere Ziele.', 27, 9, 18, 'NOTSUCCESSFUL', 27, '2024-01-15 07:51:17.538746', 1);
INSERT INTO okr_pitc.objective VALUES (1115, '2024-09-16 11:08:02.65325', 'Wir erreichen im Team Stabilität binden neue Members ein und erhalten die Zufriedenheit der bestehenden. Gleichzeitig werden wir effizienter in der Art wie wir arbeiten. ', 'Wir sind ein stabiles, performantes und zufriedenes Team', 3, 1001, 24, 'NOTSUCCESSFUL', 3, '2024-06-18 11:44:10.717314', 3);
INSERT INTO okr_pitc.objective VALUES (1001, '2024-02-26 09:51:25.043574', 'Mit der Weiterentwicklung wollen wir ein perfektes Tool für das OKR-Framework entwickeln, damit das volle Potential des Frameworks ausgenutzt werden kann.', 'The Puzzle OKR Tool will be the best application ever developed by Puzzle ITC', 15, 8, 1000, 'NOTSUCCESSFUL', 15, '2023-12-11 14:05:26.218217', 4);
INSERT INTO okr_pitc.objective VALUES (1003, '2024-02-26 09:51:36.344677', 'Wir wollen gerne zur Arbeit erscheinen.', 'Wir wollen eine grandiose Zusammenarbeit', 15, 8, 1000, 'SUCCESSFUL', 15, '2023-12-12 07:07:33.415285', 4);
INSERT INTO okr_pitc.objective VALUES (1074, '2024-06-17 22:36:34.016086', '', 'Teamanliegen adressiert, Teamentwicklung angestossen', 33, 1000, 23, 'NOTSUCCESSFUL', 33, '2024-03-19 10:09:48.989542', 4);
INSERT INTO okr_pitc.objective VALUES (1059, '2024-03-21 12:42:49.144084', 'Strategie 2024', 'Wir bringen Hitobito durch ein Techboard Review aufs nächste Level', 41, 999, 26, 'DRAFT', 41, '2024-03-15 09:43:41.686021', 2);
INSERT INTO okr_pitc.objective VALUES (1055, '2024-06-17 15:51:07.899125', '', 'Wir sind in den Bereichen BBT, /mobility und Anzahl FTE gut organisiert und haben die Basis für einen erfolgreichen Start in das neue Geschäftsjahr gelegt. ', 29, 1000, 15, 'NOTSUCCESSFUL', 20, '2024-03-07 14:43:59.102685', 6);
INSERT INTO okr_pitc.objective VALUES (1104, '2024-09-17 07:30:48.989958', '', 'Wir finden unsere Puzzle-Botschafter:innen.', 26, 1001, 25, 'SUCCESSFUL', 26, '2024-06-17 11:10:48.844395', 3);
INSERT INTO okr_pitc.objective VALUES (1106, '2024-09-17 07:32:35.844851', 'Themen: Wirtschaftlichkeit /dev/tre', 'Wir sind wirtschaftlich gesund aufgestellt', 4, 1001, 20, 'NOTSUCCESSFUL', 4, '2024-06-17 12:09:04.174114', 2);
INSERT INTO okr_pitc.objective VALUES (1010, '2024-03-27 08:24:28.78892', '', 'Wir haben eine Puzzle weit abgestützte Strategie, wie /mobility bis Ende 2025 rentabel wird.', 5, 9, 15, 'SUCCESSFUL', 5, '2023-12-18 07:44:21.665959', 6);
INSERT INTO okr_pitc.objective VALUES (1108, '2024-09-17 07:44:05.391103', 'Wir optimieren den Einsatz von unseren Members in den Projekten und stellen eine gute Auslastung sicher. Die Bedürfnisse, Stärken und Fähigkeiten sind abgeholt und eingeflossen.', 'Wir haben eine Symbiose zwischen Aufträgen und Members', 28, 1001, 19, 'SUCCESSFUL', 28, '2024-06-17 19:53:12.457546', 5);
INSERT INTO okr_pitc.objective VALUES (1061, '2024-06-13 06:17:59.359957', 'Wir wollen unsern neues Angebot (Supportdienstleistungen) weiter professionalisieren', 'Supportprozess wird weiter professionalisiert', 32, 1000, 22, 'NOTSUCCESSFUL', 67, '2024-03-18 09:27:22.247914', 2);
INSERT INTO okr_pitc.objective VALUES (1057, '2024-06-13 06:25:19.98919', '', 'Wir erhöhen die Kundenzufriedenheit', 41, 1000, 26, 'SUCCESSFUL', 41, '2024-03-15 08:44:18.216665', 2);
INSERT INTO okr_pitc.objective VALUES (1051, '2024-06-13 06:33:24.611386', '', 'Wir bringen Nachhaltigkeit, Marketing und Strategie aufs nächste Level.', 5, 1000, 14, 'SUCCESSFUL', 16, '2024-03-04 15:12:57.723467', 4);
INSERT INTO okr_pitc.objective VALUES (1079, '2024-06-17 15:29:57.731645', '', 'Wir steigern die Wirtschaftlichkeit von WAC', 36, 1000, 24, 'NOTSUCCESSFUL', 36, '2024-03-19 11:57:49.718649', 6);
INSERT INTO okr_pitc.objective VALUES (1065, '2024-06-17 22:35:29.885071', '', 'Chole hole...', 33, 1000, 23, 'SUCCESSFUL', 33, '2024-03-19 06:37:37.276552', 3);
INSERT INTO okr_pitc.objective VALUES (1011, '2023-12-18 09:45:24.500688', 'Unsere Members sollen ausgelastet sein, wir wollen finanziell rentieren.', 'Unsere Auftragsbücher sind gut gefüllt', 32, 9, 22, 'ONGOING', NULL, '2023-12-18 09:45:24.502302', 0);
INSERT INTO okr_pitc.objective VALUES (102, '2023-12-19 08:56:24.753177', '', 'Wir erreichen eine gesunde Wirtschaftlichkeit', 4, 8, 20, 'NOTSUCCESSFUL', 4, '2023-09-19 13:25:07.040731', 2);
INSERT INTO okr_pitc.objective VALUES (89, '2023-12-19 09:04:02.938412', '', '/sys gibt Gas mit Operationskunden', 67, 8, 22, 'SUCCESSFUL', 67, '2023-09-19 14:16:15.367273', 2);
INSERT INTO okr_pitc.objective VALUES (1015, '2024-03-18 15:43:06.427231', '', 'Wir bauen ein diverses, leistungsstarkes Team auf', 4, 9, 20, 'NOTSUCCESSFUL', 4, '2023-12-18 15:55:19.166815', 1);
INSERT INTO okr_pitc.objective VALUES (1023, '2023-12-19 12:40:45.834057', '', 'Wir sind unabhängiger vom Dienstleistungsgeschäft', 17, 9, 28, 'ONGOING', NULL, '2023-12-19 12:40:45.834964', 0);
INSERT INTO okr_pitc.objective VALUES (1132, '2024-07-16 13:02:32.752104', '', 'Wir erweitern unser Beratungsportfolio um niedrigschwellige Einstiegsangebote', 17, 1001, 28, 'ONGOING', 17, '2024-07-16 12:06:06.791954', 1);
INSERT INTO okr_pitc.objective VALUES (1004, '2024-09-03 09:17:49.27444', 'Das ist das Schlüssel-Objective für das nächste Quartal. Wir setzen alles daran, dass wir auch im neuen Jahr eine gute Auslastung erreichen und früh genug neue Aufträge gewinnen können. Bitte helft alle mit, dass wir in allen Disziplinen von Puzzle genügend passende Aufträge haben. Wie bereits im vergangenen Quartal heisst dies: kontaktiert eure Kunden, seit aktiv im Netzwerk und nehmt an Veranstaltungen teil, um an neue Leads und Deals zu kommen.', 'Wir schwimmen in allen Disziplinen in Aufträgen.', 16, 9, 14, 'SUCCESSFUL', 13, '2023-12-12 13:05:50.935435', 5);
INSERT INTO okr_pitc.objective VALUES (1038, '2024-03-19 06:58:12.992161', '', 'Portfolio Refining & Verkaufsförderung', 33, 9, 23, 'NOTSUCCESSFUL', 33, '2023-12-19 14:25:11.004541', 2);
INSERT INTO okr_pitc.objective VALUES (1030, '2024-03-19 08:15:06.826387', '', 'Wir sind rentabel ', 41, 9, 26, 'NOTSUCCESSFUL', 41, '2023-12-19 13:24:20.642158', 5);
INSERT INTO okr_pitc.objective VALUES (1042, '2024-03-19 08:20:19.078977', '', 'Puzzle absolviert die von uns ausgewählten Trainings.', 27, 9, 18, 'NOTSUCCESSFUL', 27, '2024-01-15 07:54:07.812028', 1);
INSERT INTO okr_pitc.objective VALUES (1083, '2024-06-26 19:32:58.124949', '', 'Unsere Rentabilität bleibt hoch 📈', 40, 1000, 21, 'SUCCESSFUL', 40, '2024-03-19 13:12:57.95764', 8);
INSERT INTO okr_pitc.objective VALUES (1109, '2024-09-17 07:33:41.610048', '', 'Auslastung & Sales', 33, 1001, 23, 'SUCCESSFUL', 33, '2024-06-17 23:33:29.26126', 2);
INSERT INTO okr_pitc.objective VALUES (1080, '2024-06-14 13:05:39.110394', 'Wir schärfen unser Portfolio und verbreite(r)n das Know-How.', '/mid journey continues 🚀', 40, 1000, 21, 'SUCCESSFUL', 31, '2024-03-19 12:04:33.346437', 7);
INSERT INTO okr_pitc.objective VALUES (60, '2023-06-20 13:36:22.441286', '', 'Wir fördern die Zufriedenheit im Team und verbessern die Kommunikationswahrnehmung', 31, 7, 21, 'ONGOING', NULL, '2023-06-20 13:36:22.441286', 1);
INSERT INTO okr_pitc.objective VALUES (57, '2023-06-20 14:08:48.956072', '', 'Wir pushen New Tech und machen uns auf dem Markt weiter bemerkbar', 31, 7, 21, 'ONGOING', NULL, '2023-06-20 14:08:48.956072', 1);
INSERT INTO okr_pitc.objective VALUES (1120, '2024-09-17 06:11:17.171072', 'Wir habens weiterhin lässig bei /mid', '/mid-Groove 💃🕺', 40, 1001, 21, 'SUCCESSFUL', 40, '2024-06-18 13:21:17.72944', 4);
INSERT INTO okr_pitc.objective VALUES (1101, '2024-09-16 05:34:59.875285', '', 'Wir verankern unser Umweltengagement mittels Zertifizierung ISO 14001 nachhaltig  ', 41, 1001, 26, 'SUCCESSFUL', 41, '2024-06-17 06:46:21.357351', 6);
INSERT INTO okr_pitc.objective VALUES (1099, '2024-09-16 05:38:04.002303', 'Jahresstrategie 2024', 'Durch Kosteneinsparungen und Wachstum verbessern wir die Wirtschaftlichkeit.', 41, 1001, 26, 'SUCCESSFUL', 41, '2024-06-17 06:46:02.057806', 11);
INSERT INTO okr_pitc.objective VALUES (1062, '2024-06-17 15:05:40.6071', 'Themen: Team, Zusammenarbeit, Selbstorganisation, Bereichsstruktur', 'Wir haben uns in den Bereichen Selbstorganisation und Arbeit im Team weiterentwickelt', 4, 1000, 20, 'SUCCESSFUL', 4, '2024-03-18 15:53:39.625582', 3);
INSERT INTO okr_pitc.objective VALUES (1054, '2024-06-17 15:52:55.871115', '', 'Wir haben die strategische Stossrichtung "Technologieshift" ausgearbeitet und vorhandene Opportunitäten umgesetzt.', 20, 1000, 15, 'NOTSUCCESSFUL', 20, '2024-03-07 14:40:45.156034', 7);
INSERT INTO okr_pitc.objective VALUES (1066, '2024-06-17 20:01:48.773744', '', 'Mehr Liebe für unsere Applikationen', 28, 1000, 19, 'NOTSUCCESSFUL', 28, '2024-03-19 06:55:17.852897', 3);
INSERT INTO okr_pitc.objective VALUES (1075, '2024-06-17 22:37:58.553863', '', 'Planung für den Q1 aufgefüllt und neue Members rekrutiert.', 33, 1000, 23, 'NOTSUCCESSFUL', 33, '2024-03-19 10:12:31.793629', 3);
INSERT INTO okr_pitc.objective VALUES (1071, '2024-06-18 08:32:00.523519', 'Wir arbeiten strukturiert und geben Infos weiter, indem wir Infos in Gitlab erfassen. Was wir tun, richten wir an unserer Strategie und den Zielen aus, die wir uns gesetzt haben. Insbesondere wollen wir Puzzle für eine etwaige ISO27k1 Zertifizierung vorbereiten.', 'Unsere Tätigkeiten folgen der Strategie und bereiten uns auf ISO 27k1 vor', 27, 1000, 18, 'SUCCESSFUL', 27, '2024-03-19 08:36:14.682794', 1);
INSERT INTO okr_pitc.objective VALUES (108, '2023-09-20 12:07:19.139688', '', 'Das /security-Team wächst zusammen', 27, 8, 18, 'ONGOING', NULL, '2023-09-20 12:07:19.139688', 1);
INSERT INTO okr_pitc.objective VALUES (63, '2023-06-20 13:32:42.203816', '', 'Wir betreiben erfolgreich unsere Applikationen mit einem aktuellen Techstack ', 28, 7, 19, 'ONGOING', NULL, '2023-06-20 13:32:42.203816', 1);
INSERT INTO okr_pitc.objective VALUES (64, '2023-06-20 13:32:26.431596', '', 'Ruby hat eine gesunde Wirtschaftlichkeit ', 28, 7, 19, 'ONGOING', NULL, '2023-06-20 13:32:26.431596', 1);
INSERT INTO okr_pitc.objective VALUES (1116, '2024-09-17 07:33:27.279288', '', 'Wir bringen die Security-Prozesse bei Puzzle weiter.', 27, 1001, 18, 'NOTSUCCESSFUL', 27, '2024-06-18 11:53:07.317901', 2);
INSERT INTO okr_pitc.objective VALUES (1150, '2024-09-17 13:07:36.009784', '', 'Team ausbauen', 33, 1002, 23, 'ONGOING', 33, '2024-09-16 22:04:45.233519', 1);
INSERT INTO okr_pitc.objective VALUES (1141, '2024-09-20 05:45:06.926705', 'Wo können wir uns als /sys Team und als /sys Lead weiter verbessern?', '/sys v2: Kulturelle und strukturelle Verbesserungen', 67, 1002, 22, 'ONGOING', 32, '2024-09-09 12:10:04.542336', 5);
INSERT INTO okr_pitc.objective VALUES (1142, '2024-09-20 05:41:18.874821', '', '/sys Marketing Improvements', 34, 1002, 22, 'ONGOING', 32, '2024-09-16 07:50:07.458823', 2);
INSERT INTO okr_pitc.objective VALUES (1148, '2024-09-20 14:27:11.151699', 'Wir schärfen und erweitern unser Angebot', 'Level Up Angebot', 3, 1002, 24, 'ONGOING', 3, '2024-09-16 14:21:14.401102', 1);
INSERT INTO okr_pitc.objective VALUES (1156, '2024-09-23 13:07:42.906911', 'Themen: Team, Selbstorganisation, Bereichsstruktur, Zusammenarbeit', 'Die /dev/tre-Members kennen die Bereichsausrichtung und ihre Rolle innerhalb /dev/tre', 4, 1002, 20, 'ONGOING', 4, '2024-09-17 12:22:32.366026', 1);
INSERT INTO okr_pitc.objective VALUES (1144, '2024-09-26 10:55:24.935857', '', 'Wir sorgen für eine einheitliche interne Informationsstrategie.
', 26, 1002, 25, 'ONGOING', 26, '2024-09-16 10:50:34.416808', 1);
INSERT INTO okr_pitc.objective VALUES (1155, '2024-09-30 11:35:21.212486', 'Unsere Members nutzen die Möglichkeiten zur Mitgestaltung und helfen uns mit beim Salesnen', 'Members gestalten /mid und denken dabei an die Wirtschaftlichkeit 😍💰', 40, 1002, 21, 'ONGOING', 31, '2024-09-17 06:25:07.461195', 6);
INSERT INTO okr_pitc.objective VALUES (86, '2023-12-14 14:39:14.774366', 'Wir können alle unsere PV Mandate fürs 2024 verlängern und neue Members auslasten', 'Wir legen den Grundstein für eine optimale Auslastung im Q3 GJ23/24', 30, 8, 15, 'SUCCESSFUL', 5, '2023-09-19 11:55:42.398737', 2);
INSERT INTO okr_pitc.objective VALUES (1012, '2023-12-18 13:01:30.346698', '', 'Unser Support ist von hoher Qualität', 32, 9, 22, 'ONGOING', NULL, '2023-12-18 13:01:30.347825', 0);
INSERT INTO okr_pitc.objective VALUES (53, '2023-06-20 13:15:13.236034', '', 'Wir arbeiten gerne bei Puzzle und schaffen ein positives internes Bild', 36, 7, 24, 'ONGOING', NULL, '2023-06-20 13:15:13.236034', 1);
INSERT INTO okr_pitc.objective VALUES (40, '2023-06-14 14:48:22.609337', '', 'Wir fördern die Happiness unserer Member und steigern die Identifikation mit /mobility', 20, 7, 15, 'ONGOING', NULL, '2023-06-14 14:48:22.609337', 1);
INSERT INTO okr_pitc.objective VALUES (45, '2023-06-26 07:33:29.312435', '', '/sys leistet seinen Beitrag zur stabilen Wirtschaftlichkeit von Puzzle', 34, 7, 22, 'ONGOING', NULL, '2023-06-26 07:33:29.312435', 1);
INSERT INTO okr_pitc.objective VALUES (70, '2023-07-27 06:23:08.507453', '', 'Wir tragen zum wirtschaftlichen Erfolg von Puzzle bei ', 41, 7, 26, 'ONGOING', NULL, '2023-07-27 06:23:08.507453', 1);
INSERT INTO okr_pitc.objective VALUES (61, '2023-06-25 09:25:50.371312', 'Das Büro, die Grundlage der Zusammenarbeit ist eingerichtet. Kürzliche Abgänge müssen kompensiert, Neueinstellungen integriert werden. /racoon wurde mit devtre fusioniert. Die technische Ausrichtung ist unklar. Die Auftragslage ändert sich, neue Aufträge kommen dazu. Die Divisionrentabilität wird eingeführt… Unter all diesen Gesichtspunkten ist es immanent wichtig, dass Team “neu” aufzustellen, damit es nicht verläddert.', 'Wir formen und stabiliseren das Team devtre und fördern die Selbstorganisation', 4, 7, 20, 'ONGOING', NULL, '2023-06-25 09:25:50.371312', 1);
INSERT INTO okr_pitc.objective VALUES (72, '2023-07-27 07:39:38.193792', '', 'Wir steigern die Kundenzufriedenheit', 41, 7, 26, 'ONGOING', NULL, '2023-07-27 07:39:38.193792', 1);
INSERT INTO okr_pitc.objective VALUES (47, '2023-06-20 10:55:46.475554', '', 'Wir werden Data Analytics / ML / MLOps Angebote verkaufen.', 30, 7, 15, 'ONGOING', NULL, '2023-06-20 10:55:46.475554', 1);
INSERT INTO okr_pitc.objective VALUES (56, '2023-06-20 12:47:46.393088', '', 'Wir erreichen eine gesunde Wirtschaftlichkeit', 4, 7, 20, 'ONGOING', NULL, '2023-06-20 12:47:46.393088', 1);
INSERT INTO okr_pitc.objective VALUES (52, '2023-06-26 07:30:38.191625', '', '/sys wird aktiv in der Hyperscalerthematik', 34, 7, 22, 'ONGOING', NULL, '2023-06-26 07:30:38.191625', 1);
INSERT INTO okr_pitc.objective VALUES (107, '2023-09-20 12:03:58.073071', '/security soll eine langfristige Zielsetzung erhalten, um Puzzle möglichst effektiv und effizient schützen zu können.', 'Strategie ist überarbeitet und Risiko-orientiert', 27, 8, 18, 'ONGOING', NULL, '2023-09-20 12:03:58.073071', 1);
INSERT INTO okr_pitc.objective VALUES (51, '2023-06-20 12:24:44.61702', 'Zwei Leute, die ihr Pensum anpsasen. Mind. eine neue Person, die startet. Mehr Diversity im Team. Mehrere direkt betroffene Kunden.', 'Embrace Change im Team', 33, 7, 23, 'ONGOING', NULL, '2023-06-20 12:24:44.61702', 1);
INSERT INTO okr_pitc.objective VALUES (62, '2023-06-20 13:31:24.653681', '', 'Wir als Ruby Team sind glücklich und gut am Markt aufgestellt', 28, 7, 19, 'ONGOING', NULL, '2023-06-20 13:31:24.653681', 1);
INSERT INTO okr_pitc.objective VALUES (1031, '2024-03-19 08:15:22.301247', '', 'Wir erhöhen die Kundenzufriedenheit', 41, 9, 26, 'NOTSUCCESSFUL', 41, '2023-12-19 13:54:45.424659', 1);
INSERT INTO okr_pitc.objective VALUES (1152, '2024-11-05 11:13:33.404564', '', 'Organisationsentwicklung Ruby/Hitobito ', 41, 1002, 26, 'SUCCESSFUL', 6, '2024-09-17 06:16:35.685994', 2);
INSERT INTO okr_pitc.objective VALUES (43, '2024-05-29 05:35:10.63066', '', 'Wir erreichen einen Quartalsgewinn und sind weiter verrechenbar gewachsen', 13, 7, 14, 'SUCCESSFUL', 16, '2023-06-20 14:04:58.817841', 2);
INSERT INTO okr_pitc.objective VALUES (65, '2023-06-26 10:53:50.28674', 'Die absolute Verrechenbarkeit von /zh ist in jedem Monat auf über 70.0%', 'Wir erreichen eine hohe absolute Verrechenbarkeit', 33, 7, 23, 'ONGOING', NULL, '2023-06-26 10:53:50.28674', 1);
INSERT INTO okr_pitc.objective VALUES (58, '2023-06-20 14:21:27.063055', '', 'Wir verzeichnen eine gesunde Wirtschaftlichkeit', 31, 7, 21, 'ONGOING', NULL, '2023-06-20 14:21:27.063055', 1);
INSERT INTO okr_pitc.objective VALUES (68, '2023-06-27 08:24:19.508338', '', 'Wir machen Puzzleness sichtbarer', 26, 7, 25, 'ONGOING', NULL, '2023-06-27 08:24:19.508338', 1);
INSERT INTO okr_pitc.objective VALUES (66, '2023-06-26 07:47:59.28265', '', 'Wir optimieren unseren Newsletter, damit noch mehr Menschen von Puzzle erfahren', 26, 7, 25, 'ONGOING', NULL, '2023-06-26 07:47:59.28265', 1);
INSERT INTO okr_pitc.objective VALUES (59, '2023-06-25 09:22:27.334997', 'Bewusstsein was wir machen', 'Wir definieren und kommunizieren unser Dienstleistungs- und Auftragsportfolio', 4, 7, 20, 'ONGOING', NULL, '2023-06-25 09:22:27.334997', 1);
INSERT INTO okr_pitc.objective VALUES (105, '2023-12-18 14:34:27.255953', 'Die Grundsätze sind bekannt, und diskutiert. Wir sind uns bewusst weshalb wir bei Puzzle sind und was wir an unserem Umfeld lieben. ', 'Wir kennen & leben die Grundsätze von Puzzle', 3, 8, 24, 'SUCCESSFUL', 36, '2023-09-19 13:42:58.700556', 2);
INSERT INTO okr_pitc.objective VALUES (103, '2023-12-18 14:41:17.477769', '', 'Wir steigern die Wirtschaftlichkeit von WAC (langfristig)', 36, 8, 24, 'SUCCESSFUL', 36, '2023-09-19 14:05:14.059905', 2);
INSERT INTO okr_pitc.objective VALUES (100, '2023-12-19 08:50:50.969683', '', 'Wir definieren und kommunizieren unser Dienstleistungs- und Auftragsportfolio ', 4, 8, 20, 'NOTSUCCESSFUL', 4, '2023-09-19 13:20:20.266448', 2);
INSERT INTO okr_pitc.objective VALUES (75, '2023-12-19 08:45:10.518604', '', 'Wir erreichen eine sehr gute Auslastung, erzielen einen Umsatzrekord und füllen die Auftragsbücher fürs 2024', 16, 8, 14, 'NOTSUCCESSFUL', 5, '2023-09-19 13:34:42.495332', 2);
INSERT INTO okr_pitc.objective VALUES (80, '2023-12-19 13:22:22.273341', '', 'Wir tragen zum wirtschaftlichen Erfolg von Puzzle bei ', 41, 8, 26, 'SUCCESSFUL', 41, '2023-09-14 09:36:33.150703', 4);
INSERT INTO okr_pitc.objective VALUES (76, '2023-12-19 08:51:36.60574', '', 'Mit den Marktopportunitäten starten wir durch und generieren neuen Umsatz', 16, 8, 14, 'NOTSUCCESSFUL', 16, '2023-09-06 12:43:59.904614', 4);
INSERT INTO okr_pitc.objective VALUES (81, '2023-12-19 08:54:19.2678', '', 'Wir erhöhen die Kundenzufriedenheit', 41, 8, 26, 'NOTSUCCESSFUL', 41, '2023-09-14 09:57:28.381143', 2);
INSERT INTO okr_pitc.objective VALUES (82, '2023-12-19 08:55:00.457331', '', 'Wir pushen neue Features und machen sie bekannt ', 41, 8, 26, 'SUCCESSFUL', 41, '2023-09-19 11:09:33.465094', 2);
INSERT INTO okr_pitc.objective VALUES (95, '2023-12-19 08:57:25.774876', 'Wir richten uns nach unseren Entwicklungszielen aus. Wir wollen im Team Angebots- und Themenschwerpunkte neu aushandeln und uns aligniert in die gewünschte Richtung weiterentwickeln.', 'Wir kalibrieren unseren Teamkompass neu', 33, 8, 23, 'NOTSUCCESSFUL', 33, '2023-09-19 00:01:58.464623', 2);
INSERT INTO okr_pitc.objective VALUES (97, '2023-12-19 08:59:15.020205', 'Zahlt auf Puzzle OKR Marktopportunitäten ein', 'Wir tragen zur Weiterentwicklung der Marktopportunitäten bei', 31, 8, 21, 'SUCCESSFUL', 40, '2023-09-19 13:09:43.17488', 2);
INSERT INTO okr_pitc.objective VALUES (106, '2023-12-19 08:59:44.731785', '', 'Wir treiben das Projekt "Migration Jenkins & Pipelines" erfolgreich und schmerzfrei voran', 31, 8, 21, 'SUCCESSFUL', 40, '2023-09-20 06:29:46.626596', 2);
INSERT INTO okr_pitc.objective VALUES (110, '2023-12-19 09:00:10.914194', '', 'Wir wollen unsere Attraktivität als Arbeitgeber für technisch herausragende Personen erhöhen', 84, 8, 28, 'NOTSUCCESSFUL', 17, '2023-10-16 16:50:33.803116', 2);
INSERT INTO okr_pitc.objective VALUES (99, '2023-12-19 09:01:52.117197', 'Zahlt auf Puzzle OKR Wirtschaftlichkeit ein', 'Wir tragen zu einer gesunden Wirtschaftlichkeit bei', 60, 8, 21, 'SUCCESSFUL', 40, '2023-09-25 15:29:12.751686', 2);
INSERT INTO okr_pitc.objective VALUES (94, '2023-12-19 09:03:51.253402', 'Wir wollen unsere Services fuer die Members auf die Pruefung stellen und verbessern', '/sys ist fuer die Members da', 67, 8, 22, 'SUCCESSFUL', 67, '2023-09-18 15:01:11.429816', 2);
INSERT INTO okr_pitc.objective VALUES (91, '2023-12-19 09:06:05.975383', '', 'Wir betreiben erfolgreich unsere Applikationen mit einem aktuellen Techstack', 28, 8, 19, 'NOTSUCCESSFUL', 28, '2023-09-18 12:18:32.657395', 2);
INSERT INTO okr_pitc.objective VALUES (92, '2023-12-19 09:06:12.675538', '', 'Ruby hat eine gesunde Wirtschaftlichkeit', 28, 8, 19, 'SUCCESSFUL', 28, '2023-09-18 12:18:20.251779', 2);
INSERT INTO okr_pitc.objective VALUES (1137, '2024-09-03 09:42:58.598486', '', 'Wir verstehen unser Portfolio und wissen welchen Mehrwert wir Kunden bringen.', 22, 999, 14, 'DRAFT', NULL, '2024-09-03 09:42:58.600521', 0);
INSERT INTO okr_pitc.objective VALUES (69, '2023-06-27 08:30:05.2284', '', 'Wir tragen technische Inhalte nach Aussen', 26, 7, 25, 'ONGOING', NULL, '2023-06-27 08:30:05.2284', 1);
INSERT INTO okr_pitc.objective VALUES (1110, '2024-09-17 07:34:07.351228', '', 'Recruiting', 33, 1001, 23, 'SUCCESSFUL', 33, '2024-06-17 23:44:32.09616', 2);
INSERT INTO okr_pitc.objective VALUES (1013, '2023-12-18 13:01:53.316024', '', 'Unsere Members sind glücklich', 32, 9, 22, 'ONGOING', 32, '2023-12-18 13:01:43.441926', 1);
INSERT INTO okr_pitc.objective VALUES (77, '2023-12-19 08:28:26.009651', '', 'Wir leben die Puzzle Grundsätze', 13, 8, 14, 'SUCCESSFUL', 5, '2023-09-06 12:15:04.231963', 2);
INSERT INTO okr_pitc.objective VALUES (93, '2023-12-19 09:06:49.220824', '', 'Wir als Ruby Team sind glücklich und gut am Markt aufgestellt', 28, 8, 19, 'SUCCESSFUL', 28, '2023-09-18 12:17:29.84897', 2);
INSERT INTO okr_pitc.objective VALUES (1063, '2024-06-17 15:07:14.36205', 'Themen: Inhalte, Marketing, Sales, Sponsoring, Events', 'Wir schärfen «Digitale Lösungen» sowie unsere Events- und Sponsoring-Aktivitäten', 4, 1000, 20, 'NOTSUCCESSFUL', 4, '2024-03-18 15:56:23.573056', 5);
INSERT INTO okr_pitc.objective VALUES (1064, '2024-06-17 15:09:21.669481', 'Themen: Auslastung, Rentabilität', 'Wir sind wirtschaftlich gesund aufgestellt
', 4, 1000, 20, 'NOTSUCCESSFUL', 4, '2024-03-18 15:56:49.027715', 3);
INSERT INTO okr_pitc.objective VALUES (1024, '2024-01-15 19:22:03.742635', 'Derzeit erhalten wir fast alle Aufträge von einem Auftraggeber/Partner. Diese Abhängigkeit soll auf weitere Auftraggeber verteilt werden.', 'Wir sind im Dienstleistungsgeschäft unabhängiger von nur einem Auftraggeber', 17, 9, 28, 'ONGOING', 17, '2023-12-19 12:49:44.057541', 1);
INSERT INTO okr_pitc.objective VALUES (1019, '2024-03-14 09:53:22.419874', '', 'Wir kreieren unsere neue Website.', 26, 9, 25, 'SUCCESSFUL', 49, '2023-12-19 11:55:19.66193', 3);
INSERT INTO okr_pitc.objective VALUES (1020, '2024-03-14 09:56:12.659785', '', 'Wir tragen die Disziplinen von Puzzle zu unseren Zielgruppen.   ', 26, 9, 25, 'NOTSUCCESSFUL', 57, '2023-12-19 11:58:36.150791', 2);
INSERT INTO okr_pitc.objective VALUES (1026, '2024-03-19 08:54:13.463206', 'Wir positionieren uns hinsichtlich der Hyperscaler Cloud-Angebote am Markt', '/mid goes Hypa Hypa 🌩️', 31, 9, 21, 'SUCCESSFUL', 40, '2023-12-19 12:56:15.142023', 5);
INSERT INTO okr_pitc.objective VALUES (1097, '2024-09-17 05:49:13.601409', '', 'Durch strategische Grundlagenarbeit zur Sales-Strategie Mobility (mit Yup) sowie zur MO Digitale Lösungen haben wir den Grundstein für neue lukrative Aufträge gelegt. ', 5, 1001, 15, 'NOTSUCCESSFUL', 5, '2024-06-13 14:07:05.750528', 4);
INSERT INTO okr_pitc.objective VALUES (1131, '2024-07-16 13:02:29.299875', '', 'Unsere Members sind bestens vorbereitet, um die nächsten spannenden Projektenanfragen zu bedienen
', 17, 1001, 28, 'ONGOING', 17, '2024-07-16 11:59:56.845083', 1);
INSERT INTO okr_pitc.objective VALUES (1114, '2024-09-16 11:05:25.926392', 'Wir sind auch diesen Monat kommunikativ am Markt aktiv. Wir ziehen unsere Social Media Kampagne weiter und Analysieren die Wirksamkeit der vergangenen Kampagne um Feinjustierungen vornehmen zu können. ', 'We are Top of Mind!', 3, 1001, 24, 'SUCCESSFUL', 3, '2024-06-18 11:43:10.532408', 3);
INSERT INTO okr_pitc.objective VALUES (1119, '2024-09-17 07:33:32.821586', '/security bleibt intern, aber trägt zu den MOs Beratung, Digitale Lösungen und Platform Engineering bei.', 'Beitrag zu MOs aufgleisen und planen', 27, 1001, 18, 'SUCCESSFUL', 27, '2024-06-18 13:19:34.330846', 4);
INSERT INTO okr_pitc.objective VALUES (1067, '2024-06-17 20:03:19.053313', '', 'Wir sind als Team optimal aufgestellt', 28, 1000, 19, 'NOTSUCCESSFUL', 28, '2024-03-19 06:55:24.674564', 2);
INSERT INTO okr_pitc.objective VALUES (1033, '2024-03-25 11:13:10.185117', '', 'Der Motor brummt', 33, 9, 23, 'SUCCESSFUL', 33, '2023-12-19 13:55:17.542397', 4);
INSERT INTO okr_pitc.objective VALUES (1105, '2024-09-17 07:32:27.081819', 'Themen: Angebot /dev/tre', 'Wir schärfen und kommunizieren unser Angebot', 4, 1001, 20, 'NOTSUCCESSFUL', 4, '2024-06-17 12:07:31.359535', 3);
INSERT INTO okr_pitc.objective VALUES (1068, '2024-06-17 20:13:54.060375', '', 'Wir haben tolle Aufträge und unsere Finanzzahlen machen Freude', 28, 1000, 19, 'SUCCESSFUL', 28, '2024-03-19 06:55:34.786893', 5);
INSERT INTO okr_pitc.objective VALUES (1133, '2024-08-15 06:28:29.445366', '', 'Hitobiot als internes Eventgtool nutzen ', 41, 999, 26, 'DRAFT', NULL, '2024-08-15 06:28:29.446571', 0);
INSERT INTO okr_pitc.objective VALUES (1076, '2024-06-14 05:14:04.031166', '', 'Wir erzielen Erfolge in den Marktopprtunitäten.', 22, 1000, 14, 'NOTSUCCESSFUL', 16, '2024-03-19 11:22:46.963368', 4);
INSERT INTO okr_pitc.objective VALUES (1072, '2024-06-18 08:31:18.477839', 'Wir wollen in anderen Projekten präsent sein und beraten/helfen um Security-Themen im Bewusstsein zu halten. Ebenso wollen wir über Newspost oder Techkafis sowie Trainings Security bei ganz Puzzle als Thema hochhalten.', '/security unterstützt den Rest von Puzzle', 27, 1000, 18, 'NOTSUCCESSFUL', 27, '2024-03-19 08:44:09.365113', 2);
INSERT INTO okr_pitc.objective VALUES (1151, '2024-09-17 13:07:41.227176', '', 'Angebot / Referenzen', 33, 1002, 23, 'ONGOING', 33, '2024-09-16 22:08:20.665233', 1);
INSERT INTO okr_pitc.objective VALUES (1136, '2024-09-17 09:30:36.830074', '', 'Wir begleiten unsere Kund:innen sicher und erfolgreich aus dem Jetzt in die Zukunft.', 5, 1002, 14, 'ONGOING', 13, '2024-08-27 12:00:30.271891', 3);
INSERT INTO okr_pitc.objective VALUES (1134, '2024-09-17 09:37:58.80856', '', 'Unsere Sales Pipeline ist voll und unsere Members-Kapazitätsliste leer.', 5, 1002, 14, 'ONGOING', 16, '2024-08-27 11:59:51.283588', 5);
INSERT INTO okr_pitc.objective VALUES (1112, '2024-09-16 10:58:46.18153', 'Wir arbeiten mit Hochdruck an unserer Rentabilität', 'Wir steigern die Wirtschaftlichkeit von WAC', 36, 1001, 24, 'SUCCESSFUL', 3, '2024-06-18 11:41:43.89273', 4);
INSERT INTO okr_pitc.objective VALUES (1111, '2024-09-17 07:42:40.262784', 'Angebot und Fokusthemen von /zh festlegen und pushen', 'Portfolio Refinement ', 33, 1001, 23, 'NOTSUCCESSFUL', 33, '2024-06-17 23:49:20.383198', 3);
INSERT INTO okr_pitc.objective VALUES (1135, '2024-09-17 09:28:50.548539', '', 'Wir verstehen unser Portfolio und wissen welchen Mehrwert wir unseren Kund:innen bringen.', 5, 1002, 14, 'ONGOING', 13, '2024-08-27 12:00:21.259411', 3);
INSERT INTO okr_pitc.objective VALUES (1102, '2024-09-17 07:32:11.234688', 'Themen: Struktur und Organisation /dev/tre', 'Wir festigen unsere selbstorganisierte Teamarbeit', 4, 1001, 20, 'NOTSUCCESSFUL', 4, '2024-06-17 10:18:55.315729', 2);
INSERT INTO okr_pitc.objective VALUES (1139, '2024-09-17 14:20:25.883337', 'Alternativ: Das jetzige und zukünftige (New Tech) /mobiity ist geschärft und am Markt validiert. ', 'Das /mobility Angebot ist FRESH und IN LOVE!', 5, 1002, 15, 'ONGOING', 5, '2024-09-06 08:50:43.742352', 2);
INSERT INTO okr_pitc.objective VALUES (1130, '2024-09-20 14:26:50.812384', 'Wir wollen unsere Wirtschaftlichkeit, wie in der Bereichsstrategie verankert, steigern und stabil halten.', 'Wir steigern unsere kurzfristige Wirtschaftlichkeit
', 3, 1002, 24, 'ONGOING', 3, '2024-07-05 09:53:29.957079', 2);
INSERT INTO okr_pitc.objective VALUES (1147, '2024-09-20 14:27:01.614671', 'Wir stellen den mittelfristigen Umsatz sicher und verlängern unsere Rahmenverträge. Ziel ist das geplante Wachstum für 2025 zu sichern.  ', 'Stabile Wirtschaftlichkeit für 1. HJ 2025', 3, 1002, 24, 'ONGOING', 3, '2024-09-16 14:02:42.22081', 1);
INSERT INTO okr_pitc.objective VALUES (1118, '2024-09-23 11:17:51.945901', 'Wir bleiben finanziell auf einem guten Level', 'Nöötli 💸', 40, 1001, 21, 'SUCCESSFUL', 40, '2024-06-18 13:13:13.63553', 5);
INSERT INTO okr_pitc.objective VALUES (1143, '2024-09-26 15:05:04.01453', '', 'Wir verstehen unser Portfolio und wissen welchen Mehrwert wir Kunden bringen.', 26, 1002, 25, 'ONGOING', 1039, '2024-09-16 10:50:03.384405', 1);
INSERT INTO okr_pitc.objective VALUES (1162, '2024-09-26 15:06:08.727524', '', 'Wir tragen unsere Marktopportunitäten nach Aussen. ', 26, 1002, 25, 'ONGOING', 1039, '2024-09-26 14:22:24.797926', 1);
INSERT INTO okr_pitc.objective VALUES (1164, '2024-10-02 09:12:28.32132', '', 'Cryptopus ist erfolgreich abgelöst durch Bitwarden', 27, 1002, 18, 'ONGOING', 27, '2024-10-02 09:03:19.953672', 1);
INSERT INTO okr_pitc.objective VALUES (1095, '2024-09-17 05:49:02.393329', 'Wie können wir Rentablitässteigerung messen? z.B. wenn eine weitere Kooperation zu Stande kommt mir ci/cd und wir einen Member zu einem höheren Stundensatz einsetzen können. Durch konsequentes Auftragsmangement können wir Kosten senken. ', 'Wir steigern unsere Rentabilität durch die Umsetzung gezielter Massnahmen in den strategischen Schwerpunkten gesundes Wachstum, Technologieshift und Auftragsmanagement. ', 5, 1001, 15, 'NOTSUCCESSFUL', 5, '2024-06-13 14:06:45.770843', 8);
INSERT INTO okr_pitc.objective VALUES (1096, '2024-09-17 05:49:08.122444', '', 'Wir haben eine Vertiefung für die strategischen Schwerpunkte Beratung und Schulungsangebot ausgearbeitet sowie zwei ML Ops Labs erfolgreich durchgeführt. ', 5, 1001, 15, 'NOTSUCCESSFUL', 5, '2024-06-13 14:06:56.922593', 4);
INSERT INTO okr_pitc.objective VALUES (1107, '2024-09-17 07:33:19.963236', 'Durch den effektiven Einsatz von Wartungsbudget und internen Vorhaben können wir die Aufwände für den Betrieb und Wartung nachhaltig senken. Dies gibt uns Möglichkeiten für weitere Optimierungen.
Wir kennen den Stand und die Strategie von unseren Applikationen.', 'Mehr Liebe für unsere Applikationen', 28, 1001, 19, 'NOTSUCCESSFUL', 28, '2024-06-17 19:47:22.53665', 5);
INSERT INTO okr_pitc.objective VALUES (1157, '2024-09-19 19:09:03.434733', 'Durch den effektiven Einsatz von Wartungsbudget und internen Vorhaben können wir die Aufwände für den Betrieb und Wartung nachhaltig senken. Dies gibt uns Möglichkeiten für weitere Optimierungen.
Wir kennen den Stand und die Strategie von unseren Applikationen.', 'Mehr Liebe für unsere Applikationen', 28, 1002, 19, 'ONGOING', 28, '2024-09-17 12:30:53.269328', 6);
INSERT INTO okr_pitc.objective VALUES (1158, '2024-09-19 19:09:07.229807', 'Definition High-Performance-Teams: https://codimd.puzzle.ch/TL1Rh5IGTAa399zw_zu3lw#', 'Wir sind ein High-Performance-Team und können gut ins Jahr 2025 starten', 28, 1002, 19, 'ONGOING', 28, '2024-09-17 12:36:04.987834', 2);
INSERT INTO okr_pitc.objective VALUES (1159, '2024-09-23 13:07:53.845189', 'Wir kennen unser Angebot - Themen: Inhalte, Marketing, Sales, Sponsoring, Events
', 'Wir kennen die Ausrichtung und Ziele der MO "Digitale Lösungen" sowie der Marktopportunitäten GJ 24/25 und unseren Beitrag für diese.', 4, 1002, 20, 'ONGOING', 4, '2024-09-17 12:37:22.050242', 3);
INSERT INTO okr_pitc.objective VALUES (1149, '2024-09-17 13:07:31.51737', '', 'Guter Jahresabschluss + gute Ausgangslage für 2025 schaffen', 33, 1002, 23, 'ONGOING', 33, '2024-09-16 22:00:32.590434', 2);
INSERT INTO okr_pitc.objective VALUES (1138, '2024-09-17 14:20:21.172588', '', 'Hohe Auslastung durch erfolgreiche Zusammenarbeit mit /sales', 20, 1002, 15, 'ONGOING', 5, '2024-09-06 08:49:52.447796', 1);
INSERT INTO okr_pitc.objective VALUES (1140, '2024-09-17 14:20:30.00904', 'Es ist noch unklar, wann die WTOs kommen, und welche Stufen (z.B. Präqualifiquation) bis Ende Jahr genommen werden müssen.', 'Für die SBB und BLS WTOs haben wir alle erforderlichen Verfahrensstufen gemeistert.', 5, 1002, 15, 'ONGOING', 5, '2024-09-06 09:12:30.754968', 1);
INSERT INTO okr_pitc.objective VALUES (1160, '2024-09-23 13:07:57.7423', 'Themen: Wirtschaftlichkeit /dev/tre', 'Die Auslastung bis Ende Geschäftsjahr 2024/25 ist gesichert', 4, 1002, 20, 'ONGOING', 4, '2024-09-17 12:56:56.849814', 1);
INSERT INTO okr_pitc.objective VALUES (1161, '2024-09-24 14:42:11.991951', 'Alternativ: Das jetzige und zukünftige (New Tech) /mobiity ist geschärft und am Markt validiert. ', 'Das /mobility Angebot ist FRESH und IN LOVE!', 5, 999, 15, 'DRAFT', NULL, '2024-09-24 14:42:11.993448', 2);
INSERT INTO okr_pitc.objective VALUES (1166, '2024-10-17 17:31:58.710976', '', 'Wir schärfen unser Portfolio und bilden unsere Members in Schwerpunktbereichen fort', 81, 1002, 28, 'DRAFT', NULL, '2024-10-17 17:31:58.712334', 0);
INSERT INTO okr_pitc.objective VALUES (1167, '2024-10-17 17:34:26.433644', '', 'Wir füllen "We Care" mit Leben und fördern die Work/Life-Balance unserer Members', 81, 1002, 28, 'DRAFT', NULL, '2024-10-17 17:34:26.435134', 0);
INSERT INTO okr_pitc.objective VALUES (1168, '2024-11-04 13:07:20.834809', 'asdf', 'aasdf', 6, 8, 26, 'ONGOING', NULL, '2024-11-04 13:07:20.901958', 0);
INSERT INTO okr_pitc.objective VALUES (1154, '2024-11-05 09:13:27.226164', 'Wir werfen die Werbetrommel für Platform Engineering an.', 'Platform Engineering ist in aller Munde 🗣️🗞️🎥', 40, 1002, 21, 'SUCCESSFUL', 6, '2024-09-17 06:24:05.137322', 7);
INSERT INTO okr_pitc.objective VALUES (1146, '2024-11-05 10:44:12.431802', 'Jahresstrategie 2024', 'Durch Kosteneinsparungen und Wachstum verbessern wir die Wirtschaftlichkeit.', 41, 1002, 26, 'SUCCESSFUL', 6, '2024-09-16 13:53:01.764084', 13);
INSERT INTO okr_pitc.objective VALUES (1153, '2024-11-05 09:13:04.705547', 'Wir definieren Prozesse und Technologien der SIGs', 'Platform Engineering mindset grows 🤯', 40, 1002, 21, 'DRAFT', 6, '2024-09-17 06:17:30.707618', 3);
INSERT INTO okr_pitc.objective VALUES (1145, '2024-11-05 12:38:20.600014', '', 'Wir erhöhen die Kundenzufriedenheitfasdf', 41, 1002, 26, 'DRAFT', 6, '2024-09-16 13:44:44.195804', 7);


--
-- Data for Name: person; Type: TABLE DATA; Schema: okr_pitc; Owner: okr-test
--

INSERT INTO okr_pitc.person VALUES (1000, 'rauch@puzzle.ch', 'Nils', 'Rauch', 0, false);
INSERT INTO okr_pitc.person VALUES (1001, 'liechti@puzzle.ch', 'Matthias', 'Liechti', 0, false);
INSERT INTO okr_pitc.person VALUES (1004, 'cgafner@puzzle.ch', 'Christian', 'Gafner', 0, false);
INSERT INTO okr_pitc.person VALUES (1007, 'roesch@puzzle.ch', 'Urs', 'Roesch', 0, false);
INSERT INTO okr_pitc.person VALUES (1008, 'hinderling@puzzle.ch', 'Tobias', 'Hinderling', 0, false);
INSERT INTO okr_pitc.person VALUES (1009, 'stark@puzzle.ch', 'Philip', 'Stark', 0, false);
INSERT INTO okr_pitc.person VALUES (1010, 'kaeser@puzzle.ch', 'Martin', 'Käser', 0, false);
INSERT INTO okr_pitc.person VALUES (1011, 'illi@puzzle.ch', 'Daniel', 'Illi', 0, false);
INSERT INTO okr_pitc.person VALUES (1014, 'buehlmann@puzzle.ch', 'Benjamin', 'Bühlmann', 0, false);
INSERT INTO okr_pitc.person VALUES (1015, 'bichsel@puzzle.ch', 'Roman', 'Bichsel', 0, false);
INSERT INTO okr_pitc.person VALUES (1016, 'burkhalter@puzzle.ch', 'Thomas', 'Burkhalter', 0, false);
INSERT INTO okr_pitc.person VALUES (1019, 'imhof@puzzle.ch', 'Fabien', 'Imhof', 0, false);
INSERT INTO okr_pitc.person VALUES (1020, 'schneider@puzzle.ch', 'David', 'Schneider', 0, false);
INSERT INTO okr_pitc.person VALUES (1021, 'seeger@puzzle.ch', 'Raphaela', 'Seeger', 0, false);
INSERT INTO okr_pitc.person VALUES (1022, 'raselli@puzzle.ch', 'Raeto', 'Raselli', 0, false);
INSERT INTO okr_pitc.person VALUES (1023, 'schaerz@puzzle.ch', 'Beat', 'Schärz', 0, false);
INSERT INTO okr_pitc.person VALUES (1024, 'tran@puzzle.ch', 'Khôi', 'Tran', 0, false);
INSERT INTO okr_pitc.person VALUES (1025, 'rotman@puzzle.ch', 'Stefan', 'Rotman', 0, false);
INSERT INTO okr_pitc.person VALUES (1026, 'ayas@puzzle.ch', 'Mehmet', 'Ayas', 0, false);
INSERT INTO okr_pitc.person VALUES (1027, 'brantschen@puzzle.ch', 'Jean-Claude', 'Brantschen', 0, false);
INSERT INTO okr_pitc.person VALUES (1028, 'krohn@puzzle.ch', 'Felix', 'Krohn', 0, false);
INSERT INTO okr_pitc.person VALUES (1029, 'galante@puzzle.ch', 'Reto', 'Galante', 0, false);
INSERT INTO okr_pitc.person VALUES (1030, 'weiss@puzzle-itc.de', 'Thomas', 'Weiss', 0, false);
INSERT INTO okr_pitc.person VALUES (1031, 'koster@puzzle.ch', 'Philipp', 'Koster', 0, false);
INSERT INTO okr_pitc.person VALUES (1032, 'schweizer@puzzle.ch', 'Simon', 'Schweizer', 0, false);
INSERT INTO okr_pitc.person VALUES (1033, 'geronimi@puzzle.ch', 'Pascal', 'Geronimi', 0, false);
INSERT INTO okr_pitc.person VALUES (1034, 'koc@puzzle.ch', 'Eyup', 'Koc', 0, false);
INSERT INTO okr_pitc.person VALUES (1035, 'urs.zollinger@safetycenter.ch', 'Urs', 'Zollinger', 0, false);
INSERT INTO okr_pitc.person VALUES (1036, 'andreas.karl@safetycenter.ch', 'Andreas', 'Karl', 0, false);
INSERT INTO okr_pitc.person VALUES (1037, 'hofer@puzzle.ch', 'Mathis', 'Hofer', 0, false);
INSERT INTO okr_pitc.person VALUES (1038, 'boschung@puzzle.ch', 'Joël', 'Boschung', 0, false);
INSERT INTO okr_pitc.person VALUES (1039, 'wyss@puzzle.ch', 'Bernhard', 'Wyss', 0, false);
INSERT INTO okr_pitc.person VALUES (1040, 'brandenberger@puzzle.ch', 'Giannin', 'Brandenberger', 0, false);
INSERT INTO okr_pitc.person VALUES (1041, 'bbertagna@puzzle.ch', 'Berivan', 'Bertagna', 0, false);
INSERT INTO okr_pitc.person VALUES (1042, 'girod@puzzle.ch', 'Stephan', 'Girod', 0, false);
INSERT INTO okr_pitc.person VALUES (1043, 'digennaro@puzzle.ch', 'Nevio', 'Di Gennaro', 0, false);
INSERT INTO okr_pitc.person VALUES (1044, 'burkhart@puzzle.ch', 'Andreas', 'Burkhart', 0, false);
INSERT INTO okr_pitc.person VALUES (1045, 'murbach@puzzle.ch', 'Pascal', 'Murbach', 0, false);
INSERT INTO okr_pitc.person VALUES (1047, 'goetz@puzzle.ch', 'Simone', 'Götz', 0, false);
INSERT INTO okr_pitc.person VALUES (1002, 'laffolter@puzzle.ch', 'Livia', 'Affolter', 0, false);
INSERT INTO okr_pitc.person VALUES (1005, 'alder@puzzle.ch', 'Daniel', 'Alder', 0, false);
INSERT INTO okr_pitc.person VALUES (1012, 'nydegger@puzzle.ch', 'Lukas', 'Nydegger', 0, false);
INSERT INTO okr_pitc.person VALUES (1017, 'beltrame@puzzle.ch', 'Carlo', 'Beltrame', 0, false);
INSERT INTO okr_pitc.person VALUES (2, 'hertle@puzzle.ch', 'Raffael', 'Hertle', 1, false);
INSERT INTO okr_pitc.person VALUES (3, 'hirsbrunner@puzzle.ch', 'Simon', 'Hirsbrunner', 1, false);
INSERT INTO okr_pitc.person VALUES (7, 'kleisa@puzzle.ch', 'Lias', 'Kleisa', 1, false);
INSERT INTO okr_pitc.person VALUES (8, 'kehrli@puzzle.ch', 'Lars', 'Kehrli', 1, false);
INSERT INTO okr_pitc.person VALUES (9, 'herren@puzzle.ch', 'Tim', 'Herren', 1, false);
INSERT INTO okr_pitc.person VALUES (10, 'owncloudadmin@puzzle.ch', 'Owncloud Admin', 'Admin', 1, false);
INSERT INTO okr_pitc.person VALUES (11, 'soltermann@puzzle.ch', 'Kilian', 'Soltermann', 1, false);
INSERT INTO okr_pitc.person VALUES (12, 'kupferschmid@puzzle.ch', 'Reto', 'Kupferschmid', 1, false);
INSERT INTO okr_pitc.person VALUES (14, 'schmid@puzzle.ch', 'Jonas', 'Schmid', 1, false);
INSERT INTO okr_pitc.person VALUES (15, 'egli@puzzle.ch', 'Marc', 'Egli', 1, false);
INSERT INTO okr_pitc.person VALUES (16, 'waber@puzzle.ch', 'Mark', 'Waber', 1, false);
INSERT INTO okr_pitc.person VALUES (17, 'kallies@puzzle-itc.de', 'Lukas', 'Kallies', 1, false);
INSERT INTO okr_pitc.person VALUES (18, 'ehrat@puzzle.ch', 'Mabel', 'Ehrat', 1, false);
INSERT INTO okr_pitc.person VALUES (19, 'preisig@puzzle.ch', 'Lukas', 'Preisig', 1, false);
INSERT INTO okr_pitc.person VALUES (20, 'baldinger@puzzle.ch', 'Joel', 'Baldinger', 1, false);
INSERT INTO okr_pitc.person VALUES (21, 'gafner@puzzle.ch', 'Martin', 'Gafner', 1, false);
INSERT INTO okr_pitc.person VALUES (22, 'groner@puzzle.ch', 'Marcel', 'Groner', 1, false);
INSERT INTO okr_pitc.person VALUES (23, 'baumgartner@puzzle.ch', 'Jürgen', 'Baumgartner', 1, false);
INSERT INTO okr_pitc.person VALUES (24, 'binggeli@puzzle.ch', 'Daniel', 'Binggeli', 1, false);
INSERT INTO okr_pitc.person VALUES (25, 'tschan@puzzle.ch', 'Daniel', 'Tschan', 1, false);
INSERT INTO okr_pitc.person VALUES (26, 'jenni@puzzle.ch', 'Saraina', 'Jenni', 1, false);
INSERT INTO okr_pitc.person VALUES (27, 'zeman@puzzle.ch', 'Mark', 'Zeman', 1, false);
INSERT INTO okr_pitc.person VALUES (28, 'brian@puzzle.ch', 'Olivier', 'Brian', 1, false);
INSERT INTO okr_pitc.person VALUES (29, 'bratschi@puzzle.ch', 'Nathalie', 'Bratschi', 1, false);
INSERT INTO okr_pitc.person VALUES (30, 'rava@puzzle.ch', 'Andreas', 'Rava', 1, false);
INSERT INTO okr_pitc.person VALUES (32, 'pschmid@puzzle.ch', 'Philippe', 'Schmid', 1, false);
INSERT INTO okr_pitc.person VALUES (33, 'fankhauser@puzzle.ch', 'Simon', 'Fankhauser', 1, false);
INSERT INTO okr_pitc.person VALUES (34, 'sellin@puzzle.ch', 'Frank', 'Sellin', 1, false);
INSERT INTO okr_pitc.person VALUES (35, 'rueger@puzzle.ch', 'Sascha', 'Rüger', 1, false);
INSERT INTO okr_pitc.person VALUES (36, 'berteletti@puzzle.ch', 'Adrian', 'Berteletti', 1, false);
INSERT INTO okr_pitc.person VALUES (37, 'gaechter@puzzle.ch', 'Christoph', 'Gaechter', 1, false);
INSERT INTO okr_pitc.person VALUES (38, 'klopfenstein@puzzle.ch', 'Valentin', 'Klopfenstein', 1, false);
INSERT INTO okr_pitc.person VALUES (39, 'daellenbach@puzzle.ch', 'Yannik', 'Dällenbach', 1, false);
INSERT INTO okr_pitc.person VALUES (40, 'haller@puzzle.ch', 'Christian', 'Haller', 1, false);
INSERT INTO okr_pitc.person VALUES (41, 'dietschi@puzzle.ch', 'Oliver', 'Dietschi', 1, false);
INSERT INTO okr_pitc.person VALUES (42, 'bobst@puzzle.ch', 'Robin', 'Bobst', 1, false);
INSERT INTO okr_pitc.person VALUES (43, 'hiller@puzzle.ch', 'Rebecca', 'Hiller', 1, false);
INSERT INTO okr_pitc.person VALUES (44, 'viehweger@puzzle.ch', 'Matthias', 'Viehweger', 1, false);
INSERT INTO okr_pitc.person VALUES (45, 'begert@puzzle.ch', 'Mathias', 'Begert', 1, false);
INSERT INTO okr_pitc.person VALUES (46, 'asti@puzzle.ch', 'Claudia', 'Asti', 1, false);
INSERT INTO okr_pitc.person VALUES (47, 'fehlmann@puzzle.ch', 'Marc', 'Fehlmann', 1, false);
INSERT INTO okr_pitc.person VALUES (48, 'mtroehler@puzzle.ch', 'Matthias', 'Tröhler', 1, false);
INSERT INTO okr_pitc.person VALUES (49, 'hashim@puzzle.ch', 'Jennifer', 'Hashim', 1, false);
INSERT INTO okr_pitc.person VALUES (50, 'walker@puzzle.ch', 'Jan', 'Walker', 1, false);
INSERT INTO okr_pitc.person VALUES (51, 'blatter@puzzle.ch', 'Nadia', 'Blatter', 1, false);
INSERT INTO okr_pitc.person VALUES (52, 'simon@puzzle.ch', 'Pascal', 'Simon', 1, false);
INSERT INTO okr_pitc.person VALUES (5, 'leimgruber@puzzle.ch', 'Philipp', 'Leimgruber', 2, true);
INSERT INTO okr_pitc.person VALUES (1, 'peggimann@puzzle.ch', 'Paco', 'Eggimann', 2, true);
INSERT INTO okr_pitc.person VALUES (13, 'santschi@puzzle.ch', 'Bruno', 'Santschi', 2, true);
INSERT INTO okr_pitc.person VALUES (4, 'mkrebs@puzzle.ch', 'Matthias', 'Krebs', 2, true);
INSERT INTO okr_pitc.person VALUES (31, 'brunner@puzzle.ch', 'Adrian', 'Brunner', 2, true);
INSERT INTO okr_pitc.person VALUES (53, 'schmocker@puzzle.ch', 'Aaron', 'Schmocker', 1, false);
INSERT INTO okr_pitc.person VALUES (54, 'meisser@puzzle.ch', 'Dominik', 'Meisser', 1, false);
INSERT INTO okr_pitc.person VALUES (55, 'fhashim@puzzle.ch', 'Franziska', 'Hashim', 1, false);
INSERT INTO okr_pitc.person VALUES (56, 'troehler@puzzle.ch', 'Tobias', 'Tröhler', 1, false);
INSERT INTO okr_pitc.person VALUES (57, 'scherwey@puzzle.ch', 'Valentina', 'Scherwey', 1, false);
INSERT INTO okr_pitc.person VALUES (58, 'pulfer@puzzle.ch', 'Jannik', 'Pulfer', 1, false);
INSERT INTO okr_pitc.person VALUES (59, 'schlatter@puzzle.ch', 'Christian', 'Schlatter', 1, false);
INSERT INTO okr_pitc.person VALUES (60, 'pfeifhofer@puzzle.ch', 'Anna', 'Pfeifhofer', 1, false);
INSERT INTO okr_pitc.person VALUES (61, 'baer@puzzle.ch', 'Christian', 'Bär', 1, false);
INSERT INTO okr_pitc.person VALUES (62, 'moeri@puzzle.ch', 'Manuel', 'Möri', 1, false);
INSERT INTO okr_pitc.person VALUES (63, 'lehmann@puzzle.ch', 'Miguel', 'Lehmann', 1, false);
INSERT INTO okr_pitc.person VALUES (64, 'gobeli@puzzle.ch', 'Etienne', 'Gobeli', 1, false);
INSERT INTO okr_pitc.person VALUES (65, 'ellenberger@puzzle.ch', 'Thomas', 'Ellenberger', 1, false);
INSERT INTO okr_pitc.person VALUES (66, 'raaflaub@puzzle.ch', 'Christoph', 'Raaflaub', 1, false);
INSERT INTO okr_pitc.person VALUES (67, 'grimm@puzzle.ch', 'Lukas', 'Grimm', 1, false);
INSERT INTO okr_pitc.person VALUES (68, 'boier@puzzle.ch', 'Carmen', 'Boier', 1, false);
INSERT INTO okr_pitc.person VALUES (69, 'roth@puzzle.ch', 'Simon', 'Roth', 1, false);
INSERT INTO okr_pitc.person VALUES (70, 'schober@puzzle.ch', 'Gertrud', 'Schober', 1, false);
INSERT INTO okr_pitc.person VALUES (71, 'gannon@puzzle.ch', 'Mirjam', 'Gannon', 1, false);
INSERT INTO okr_pitc.person VALUES (72, 'plattner@puzzle.ch', 'Sebastian', 'Plattner', 1, false);
INSERT INTO okr_pitc.person VALUES (73, 'stern@puzzle.ch', 'Tobias', 'Stern', 1, false);
INSERT INTO okr_pitc.person VALUES (74, 'fstuder@puzzle.ch', 'Florian', 'Studer', 1, false);
INSERT INTO okr_pitc.person VALUES (75, 'ymueller@puzzle.ch', 'Yoan', 'Müller', 1, false);
INSERT INTO okr_pitc.person VALUES (76, 'llorente@puzzle.ch', 'Clara', 'Llorente Lemm', 1, false);
INSERT INTO okr_pitc.person VALUES (77, 'fasnacht@puzzle.ch', 'Christian', 'Fasnacht', 1, false);
INSERT INTO okr_pitc.person VALUES (78, 'gilgen@puzzle.ch', 'Sylvain', 'Gilgen', 1, false);
INSERT INTO okr_pitc.person VALUES (79, 'strahm@puzzle.ch', 'Andreas', 'Strahm', 1, false);
INSERT INTO okr_pitc.person VALUES (80, 'luedi@puzzle.ch', 'Micha', 'Lüdi', 1, false);
INSERT INTO okr_pitc.person VALUES (81, 'kobras@puzzle-itc.de', 'Daniel', 'Kobras', 1, false);
INSERT INTO okr_pitc.person VALUES (82, 'sbuehlmann@puzzle.ch', 'Simon', 'Bühlmann', 1, false);
INSERT INTO okr_pitc.person VALUES (83, 'bianchi@puzzle.ch', 'Carina', 'Bianchi', 1, false);
INSERT INTO okr_pitc.person VALUES (84, 'proehl@puzzle-itc.de', 'Mark', 'Pröhl', 1, false);
INSERT INTO okr_pitc.person VALUES (85, 'steiner@puzzle.ch', 'Robin', 'Steiner', 1, false);
INSERT INTO okr_pitc.person VALUES (86, 'luethi@puzzle.ch', 'Christof', 'Lüthi', 1, false);
INSERT INTO okr_pitc.person VALUES (87, 'tao@puzzle.ch', 'Yelan', 'Tao', 1, false);
INSERT INTO okr_pitc.person VALUES (88, 'overney@puzzle.ch', 'Mayra', 'Overney-Falconi', 1, false);
INSERT INTO okr_pitc.person VALUES (89, 'egerber@puzzle.ch', 'Elias', 'Gerber', 1, false);
INSERT INTO okr_pitc.person VALUES (90, 'eugster@puzzle.ch', 'Andres', 'Eugster', 1, false);
INSERT INTO okr_pitc.person VALUES (91, 'macheleidt@puzzle.ch', 'Falko', 'Macheleidt', 1, false);
INSERT INTO okr_pitc.person VALUES (92, 'fahrni@puzzle.ch', 'Christian', 'Fahrni', 1, false);
INSERT INTO okr_pitc.person VALUES (93, 'kreutzer@puzzle.ch', 'Konstantin', 'Kreutzer', 1, false);
INSERT INTO okr_pitc.person VALUES (94, 'simmen@puzzle.ch', 'David', 'Simmen', 1, false);
INSERT INTO okr_pitc.person VALUES (95, 'dacostacova@puzzle.ch', 'Tiago', 'da Costa Cova', 1, false);
INSERT INTO okr_pitc.person VALUES (96, 'zumkehr@puzzle.ch', 'Pascal', 'Zumkehr', 1, false);
INSERT INTO okr_pitc.person VALUES (97, 'burri@puzzle.ch', 'Max', 'Burri', 1, false);
INSERT INTO okr_pitc.person VALUES (98, 'binder@puzzle.ch', 'Janiss', 'Binder', 1, false);
INSERT INTO okr_pitc.person VALUES (99, 'frey@puzzle.ch', 'Nicole', 'Frey', 1, false);
INSERT INTO okr_pitc.person VALUES (100, 'endtner@puzzle.ch', 'Janik', 'Endtner', 1, false);
INSERT INTO okr_pitc.person VALUES (101, 'matti@puzzle.ch', 'Philipp', 'Matti', 1, false);
INSERT INTO okr_pitc.person VALUES (102, 'bertagna@puzzle.ch', 'Fabio', 'Bertagna', 1, false);
INSERT INTO okr_pitc.person VALUES (103, 'jaeggi@puzzle.ch', 'Niklas', 'Jäggi', 1, false);
INSERT INTO okr_pitc.person VALUES (1003, 'mkunz@puzzle.ch', 'Mirjam', 'Kunz', 0, false);
INSERT INTO okr_pitc.person VALUES (1006, 'keil@puzzle.ch', 'Rémy', 'Keil', 0, false);
INSERT INTO okr_pitc.person VALUES (1013, 'koller@puzzle.ch', 'Lukas', 'Koller', 0, false);
INSERT INTO okr_pitc.person VALUES (1018, 'maierhofer@puzzle.ch', 'Andreas', 'Maierhofer', 0, false);
INSERT INTO okr_pitc.person VALUES (1046, 'pfammatter@puzzle.ch', 'Hannes', 'Pfammatter', 0, false);
INSERT INTO okr_pitc.person VALUES (6, 'minder@puzzle.ch', 'Yanick', 'Minder', 2, true);


--
-- Data for Name: person_team; Type: TABLE DATA; Schema: okr_pitc; Owner: okr-test
--

INSERT INTO okr_pitc.person_team VALUES (true, 0, 1, 6, 1003);
INSERT INTO okr_pitc.person_team VALUES (true, 0, 2, 5, 15);
INSERT INTO okr_pitc.person_team VALUES (false, 0, 3, 5, 1000);
INSERT INTO okr_pitc.person_team VALUES (true, 1, 4, 28, 26);


--
-- Data for Name: quarter; Type: TABLE DATA; Schema: okr_pitc; Owner: okr-test
--

INSERT INTO okr_pitc.quarter VALUES (1, 'GJ 22/23-Q3', '2023-01-01', '2023-03-31');
INSERT INTO okr_pitc.quarter VALUES (2, 'GJ 22/23-Q4', '2023-04-01', '2023-06-30');
INSERT INTO okr_pitc.quarter VALUES (3, 'GJ 22/23-Q2', '2022-10-01', '2022-12-31');
INSERT INTO okr_pitc.quarter VALUES (4, 'GJ 22/23-Q1', '2022-07-01', '2022-09-30');
INSERT INTO okr_pitc.quarter VALUES (5, 'GJ 21/22-Q4', '2022-04-01', '2022-06-30');
INSERT INTO okr_pitc.quarter VALUES (6, 'GJ 21/22-Q3', '2022-01-01', '2022-03-31');
INSERT INTO okr_pitc.quarter VALUES (7, 'GJ 23/24-Q1', '2023-07-01', '2023-09-30');
INSERT INTO okr_pitc.quarter VALUES (8, 'GJ 23/24-Q2', '2023-10-01', '2023-12-31');
INSERT INTO okr_pitc.quarter VALUES (9, 'GJ 23/24-Q3', '2024-01-01', '2024-03-31');
INSERT INTO okr_pitc.quarter VALUES (1000, 'GJ 23/24-Q4', '2024-04-01', '2024-06-30');
INSERT INTO okr_pitc.quarter VALUES (999, 'Backlog', NULL, NULL);
INSERT INTO okr_pitc.quarter VALUES (1001, 'GJ 24/25-Q1', '2024-07-01', '2024-09-30');
INSERT INTO okr_pitc.quarter VALUES (1002, 'GJ 24/25-Q2', '2024-10-01', '2024-12-31');
INSERT INTO okr_pitc.quarter VALUES (1003, 'GJ 24/25-Q3', '2025-01-01', '2025-03-31');


--
-- Data for Name: team; Type: TABLE DATA; Schema: okr_pitc; Owner: okr-test
--

INSERT INTO okr_pitc.team VALUES (1000, '/BBT', 0);
INSERT INTO okr_pitc.team VALUES (1002, 'Sales', 0);
INSERT INTO okr_pitc.team VALUES (20, '/dev/tre', 2);
INSERT INTO okr_pitc.team VALUES (26, '/hitobito', 2);
INSERT INTO okr_pitc.team VALUES (21, '/mid', 2);
INSERT INTO okr_pitc.team VALUES (15, '/mobility', 2);
INSERT INTO okr_pitc.team VALUES (19, '/ruby', 2);
INSERT INTO okr_pitc.team VALUES (18, '/security', 2);
INSERT INTO okr_pitc.team VALUES (22, '/sys', 2);
INSERT INTO okr_pitc.team VALUES (24, '/ux', 2);
INSERT INTO okr_pitc.team VALUES (23, '/zh', 2);
INSERT INTO okr_pitc.team VALUES (28, 'Deutschland', 2);
INSERT INTO okr_pitc.team VALUES (25, 'MarCom', 2);
INSERT INTO okr_pitc.team VALUES (14, 'Puzzle ITC', 2);
INSERT INTO okr_pitc.team VALUES (1003, 'test', 0);


--
-- Name: sequence_action; Type: SEQUENCE SET; Schema: okr_pitc; Owner: okr-test
--

SELECT pg_catalog.setval('okr_pitc.sequence_action', 1398, true);


--
-- Name: sequence_alignment; Type: SEQUENCE SET; Schema: okr_pitc; Owner: okr-test
--

SELECT pg_catalog.setval('okr_pitc.sequence_alignment', 1000, false);


--
-- Name: sequence_check_in; Type: SEQUENCE SET; Schema: okr_pitc; Owner: okr-test
--

SELECT pg_catalog.setval('okr_pitc.sequence_check_in', 2822, true);


--
-- Name: sequence_completed; Type: SEQUENCE SET; Schema: okr_pitc; Owner: okr-test
--

SELECT pg_catalog.setval('okr_pitc.sequence_completed', 1145, true);


--
-- Name: sequence_key_result; Type: SEQUENCE SET; Schema: okr_pitc; Owner: okr-test
--

SELECT pg_catalog.setval('okr_pitc.sequence_key_result', 1486, true);


--
-- Name: sequence_objective; Type: SEQUENCE SET; Schema: okr_pitc; Owner: okr-test
--

SELECT pg_catalog.setval('okr_pitc.sequence_objective', 1168, true);


--
-- Name: sequence_organisation; Type: SEQUENCE SET; Schema: okr_pitc; Owner: okr-test
--

SELECT pg_catalog.setval('okr_pitc.sequence_organisation', 1000, false);


--
-- Name: sequence_person; Type: SEQUENCE SET; Schema: okr_pitc; Owner: okr-test
--

SELECT pg_catalog.setval('okr_pitc.sequence_person', 1047, true);


--
-- Name: sequence_person_team; Type: SEQUENCE SET; Schema: okr_pitc; Owner: okr-test
--

SELECT pg_catalog.setval('okr_pitc.sequence_person_team', 4, true);


--
-- Name: sequence_quarter; Type: SEQUENCE SET; Schema: okr_pitc; Owner: okr-test
--

SELECT pg_catalog.setval('okr_pitc.sequence_quarter', 1003, true);


--
-- Name: sequence_team; Type: SEQUENCE SET; Schema: okr_pitc; Owner: okr-test
--

SELECT pg_catalog.setval('okr_pitc.sequence_team', 1003, true);


--
-- Name: action action_pkey; Type: CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.action
    ADD CONSTRAINT action_pkey PRIMARY KEY (id);


--
-- Name: alignment alignment_pkey; Type: CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.alignment
    ADD CONSTRAINT alignment_pkey PRIMARY KEY (id);


--
-- Name: check_in check_in_pkey; Type: CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.check_in
    ADD CONSTRAINT check_in_pkey PRIMARY KEY (id);


--
-- Name: completed completed_pkey; Type: CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.completed
    ADD CONSTRAINT completed_pkey PRIMARY KEY (id);


--
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- Name: key_result key_result_pkey; Type: CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.key_result
    ADD CONSTRAINT key_result_pkey PRIMARY KEY (id);


--
-- Name: objective objective_pkey; Type: CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.objective
    ADD CONSTRAINT objective_pkey PRIMARY KEY (id);


--
-- Name: person person_pkey; Type: CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.person
    ADD CONSTRAINT person_pkey PRIMARY KEY (id);


--
-- Name: person_team person_team_pkey; Type: CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.person_team
    ADD CONSTRAINT person_team_pkey PRIMARY KEY (id);


--
-- Name: quarter quarter_pkey; Type: CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.quarter
    ADD CONSTRAINT quarter_pkey PRIMARY KEY (id);


--
-- Name: team team_pkey; Type: CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.team
    ADD CONSTRAINT team_pkey PRIMARY KEY (id);


--
-- Name: person uk_person_email; Type: CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.person
    ADD CONSTRAINT uk_person_email UNIQUE (email);


--
-- Name: quarter uk_quarter_label; Type: CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.quarter
    ADD CONSTRAINT uk_quarter_label UNIQUE (label);


--
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: okr_pitc; Owner: okr-test
--

CREATE INDEX flyway_schema_history_s_idx ON okr_pitc.flyway_schema_history USING btree (success);


--
-- Name: idx_action_key_result; Type: INDEX; Schema: okr_pitc; Owner: okr-test
--

CREATE INDEX idx_action_key_result ON okr_pitc.action USING btree (key_result_id);


--
-- Name: idx_alignment_aligned_objective; Type: INDEX; Schema: okr_pitc; Owner: okr-test
--

CREATE INDEX idx_alignment_aligned_objective ON okr_pitc.alignment USING btree (aligned_objective_id);


--
-- Name: idx_alignment_target_key_result; Type: INDEX; Schema: okr_pitc; Owner: okr-test
--

CREATE INDEX idx_alignment_target_key_result ON okr_pitc.alignment USING btree (target_key_result_id);


--
-- Name: idx_alignment_target_objective; Type: INDEX; Schema: okr_pitc; Owner: okr-test
--

CREATE INDEX idx_alignment_target_objective ON okr_pitc.alignment USING btree (target_objective_id);


--
-- Name: idx_check_in_key_result; Type: INDEX; Schema: okr_pitc; Owner: okr-test
--

CREATE INDEX idx_check_in_key_result ON okr_pitc.check_in USING btree (key_result_id);


--
-- Name: idx_completed_objective; Type: INDEX; Schema: okr_pitc; Owner: okr-test
--

CREATE INDEX idx_completed_objective ON okr_pitc.completed USING btree (objective_id);


--
-- Name: idx_key_result_created_by_person; Type: INDEX; Schema: okr_pitc; Owner: okr-test
--

CREATE INDEX idx_key_result_created_by_person ON okr_pitc.key_result USING btree (created_by_id);


--
-- Name: idx_key_result_objective; Type: INDEX; Schema: okr_pitc; Owner: okr-test
--

CREATE INDEX idx_key_result_objective ON okr_pitc.key_result USING btree (objective_id);


--
-- Name: idx_key_result_owner_person; Type: INDEX; Schema: okr_pitc; Owner: okr-test
--

CREATE INDEX idx_key_result_owner_person ON okr_pitc.key_result USING btree (owner_id);


--
-- Name: idx_objective_created_by_person; Type: INDEX; Schema: okr_pitc; Owner: okr-test
--

CREATE INDEX idx_objective_created_by_person ON okr_pitc.objective USING btree (created_by_id);


--
-- Name: idx_objective_quarter; Type: INDEX; Schema: okr_pitc; Owner: okr-test
--

CREATE INDEX idx_objective_quarter ON okr_pitc.objective USING btree (quarter_id);


--
-- Name: idx_objective_team; Type: INDEX; Schema: okr_pitc; Owner: okr-test
--

CREATE INDEX idx_objective_team ON okr_pitc.objective USING btree (team_id);


--
-- Name: idx_objective_title; Type: INDEX; Schema: okr_pitc; Owner: okr-test
--

CREATE INDEX idx_objective_title ON okr_pitc.objective USING btree (title);


--
-- Name: check_in fk1ceh0ql85vexss212h0jlrctd; Type: FK CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.check_in
    ADD CONSTRAINT fk1ceh0ql85vexss212h0jlrctd FOREIGN KEY (created_by_id) REFERENCES okr_pitc.person(id);


--
-- Name: action fk_action_key_result; Type: FK CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.action
    ADD CONSTRAINT fk_action_key_result FOREIGN KEY (key_result_id) REFERENCES okr_pitc.key_result(id);


--
-- Name: alignment fk_alignment_aligned_objective; Type: FK CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.alignment
    ADD CONSTRAINT fk_alignment_aligned_objective FOREIGN KEY (aligned_objective_id) REFERENCES okr_pitc.objective(id);


--
-- Name: alignment fk_alignment_target_key_result; Type: FK CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.alignment
    ADD CONSTRAINT fk_alignment_target_key_result FOREIGN KEY (target_key_result_id) REFERENCES okr_pitc.key_result(id);


--
-- Name: alignment fk_alignment_target_objective; Type: FK CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.alignment
    ADD CONSTRAINT fk_alignment_target_objective FOREIGN KEY (target_objective_id) REFERENCES okr_pitc.objective(id);


--
-- Name: check_in fk_check_in_key_result; Type: FK CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.check_in
    ADD CONSTRAINT fk_check_in_key_result FOREIGN KEY (key_result_id) REFERENCES okr_pitc.key_result(id);


--
-- Name: completed fk_completed_objective; Type: FK CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.completed
    ADD CONSTRAINT fk_completed_objective FOREIGN KEY (objective_id) REFERENCES okr_pitc.objective(id);


--
-- Name: key_result fk_key_result_created_by_person; Type: FK CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.key_result
    ADD CONSTRAINT fk_key_result_created_by_person FOREIGN KEY (created_by_id) REFERENCES okr_pitc.person(id);


--
-- Name: key_result fk_key_result_objective; Type: FK CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.key_result
    ADD CONSTRAINT fk_key_result_objective FOREIGN KEY (objective_id) REFERENCES okr_pitc.objective(id);


--
-- Name: key_result fk_key_result_owner_person; Type: FK CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.key_result
    ADD CONSTRAINT fk_key_result_owner_person FOREIGN KEY (owner_id) REFERENCES okr_pitc.person(id);


--
-- Name: objective fk_objective_created_by_person; Type: FK CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.objective
    ADD CONSTRAINT fk_objective_created_by_person FOREIGN KEY (created_by_id) REFERENCES okr_pitc.person(id);


--
-- Name: objective fk_objective_quarter; Type: FK CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.objective
    ADD CONSTRAINT fk_objective_quarter FOREIGN KEY (quarter_id) REFERENCES okr_pitc.quarter(id);


--
-- Name: objective fk_objective_team; Type: FK CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.objective
    ADD CONSTRAINT fk_objective_team FOREIGN KEY (team_id) REFERENCES okr_pitc.team(id);


--
-- Name: person_team fk_person_team_person; Type: FK CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.person_team
    ADD CONSTRAINT fk_person_team_person FOREIGN KEY (person_id) REFERENCES okr_pitc.person(id);


--
-- Name: person_team fk_person_team_team; Type: FK CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.person_team
    ADD CONSTRAINT fk_person_team_team FOREIGN KEY (team_id) REFERENCES okr_pitc.team(id);


--
-- Name: check_in fkoiu1l4cagu6bsdwyfy90v4tvn; Type: FK CONSTRAINT; Schema: okr_pitc; Owner: okr-test
--

ALTER TABLE ONLY okr_pitc.check_in
    ADD CONSTRAINT fkoiu1l4cagu6bsdwyfy90v4tvn FOREIGN KEY (key_result_id) REFERENCES okr_pitc.key_result(id);


--
-- Name: DATABASE "pitc-okr-test"; Type: ACL; Schema: -; Owner: okr-test
--

REVOKE CONNECT,TEMPORARY ON DATABASE "pitc-okr-test" FROM PUBLIC;


--
-- PostgreSQL database dump complete
--

