alter table team
    add column if not exists role_name varchar(250);

update team
set role_name = 'org_ausbildung'
where name = '/BBT';

update team
set role_name = 'org_wearecube'
where name = 'we are cube.³';

update team
set role_name = 'org_gl'
where name = 'Puzzle ITC';

update team
set role_name = 'org_mobility'
where role_name is null
   or role_name = '';

alter table team
    alter column role_name set not null;
