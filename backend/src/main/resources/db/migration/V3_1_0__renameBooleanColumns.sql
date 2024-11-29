ALTER TABLE action
    RENAME COLUMN is_checked to checked;

ALTER TABLE person_team
    RENAME COLUMN is_team_admin to team_admin;

ALTER TABLE person
    RENAME COLUMN is_okr_champion to okr_champion;