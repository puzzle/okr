alter table alignment
    add column if not exists aligned_objective_title text,
    add column if not exists aligned_objective_quarter_id integer,
    add column if not exists aligned_objective_team_id integer,
    add column if not exists aligned_objective_team_name text,
    add column if not exists target_objective_title text,
    add column if not exists target_objective_team_id integer,
    add column if not exists target_objective_team_name text,
    add column if not exists target_key_result_title text,
    add column if not exists target_key_result_team_id integer,
    add column if not exists target_key_result_team_name text;

insert into public.alignment (id, aligned_objective_id, alignment_type, target_key_result_id, target_objective_id, version, aligned_objective_title, aligned_objective_quarter_id, aligned_objective_team_id, aligned_objective_team_name, target_objective_title, target_objective_team_id, target_objective_team_name, target_key_result_title, target_key_result_team_id, target_key_result_team_name)
values  (1, 4, 'objective', null, 6, 1, 'Build a company culture that kills the competition.', 2, 5, 'Puzzle ITC', 'Als BBT wollen wir den Arbeitsalltag der Members von Puzzle ITC erleichtern.', 4, '/BBT', null, null, null),
        (2, 9, 'keyResult', 8, null, 1, 'At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.', 2, 6, 'LoremIpsum', null, null, null, 'High employee satisfaction scores (80%+) throughout the year.', 5, 'Puzzle ITC');

DROP VIEW IF EXISTS ALIGNMENT_VIEW;
CREATE VIEW ALIGNMENT_VIEW AS
SELECT a.id, a.aligned_objective_id, a.aligned_objective_title, a.aligned_objective_team_id, aligned_objective_team_name, a.aligned_objective_quarter_id, a.alignment_type, a.target_key_result_id, a.target_key_result_title, a.target_key_result_team_id, a.target_key_result_team_name, a.target_objective_id, a.target_objective_title, a.target_objective_team_id, a.target_objective_team_name
FROM alignment a;