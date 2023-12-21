ALTER TABLE person DROP COLUMN username;
ALTER TABLE person ADD CONSTRAINT UK_person_email UNIQUE (email);

DROP TABLE team_organisation;
DROP TABLE organisation;