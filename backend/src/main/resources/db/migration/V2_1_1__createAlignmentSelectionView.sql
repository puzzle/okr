drop view if exists alignment_selection;
create view alignment_selection as
select o.id                as "objective_id",
       o.title             as "objective_title",
       t.id                as "team_id",
       t.name              as "team_name",
       q.id                as "quarter_id",
       q.label             as "quarter_label",
       coalesce(kr.id, -1) as "key_result_id",
       kr.title            as "key_result_title"
from objective o
         left join team t on o.team_id = t.id
         left join quarter q on o.quarter_id = q.id
         left join key_result kr on o.id = kr.objective_id;