ALTER TABLE person
    DROP COLUMN IF EXISTS username;

ALTER TABLE person
    ADD CONSTRAINT UK_person_email UNIQUE (email);

DROP TABLE IF EXISTS team_organisation;
DROP TABLE IF EXISTS organisation;

CREATE SEQUENCE IF NOT EXISTS sequence_team;

CREATE TABLE person_team
(
    is_team_admin BOOLEAN DEFAULT FALSE NOT NULL,
    version       INTEGER               NOT NULL,
    id            BIGINT                NOT NULL,
    person_id     BIGINT,
    team_id       BIGINT,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS person_team
    ADD CONSTRAINT FK_person_team_team FOREIGN KEY (team_id) REFERENCES team;
ALTER TABLE IF EXISTS person_team
    ADD CONSTRAINT FK_person_team_person FOREIGN KEY (person_id) REFERENCES person;