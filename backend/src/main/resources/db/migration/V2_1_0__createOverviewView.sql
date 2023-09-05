drop view if exists overview;
create view overview as
select t.id                as "team_id",
       t.name              as "team_name",
       coalesce(o.id, -1)  as "objective_id",
       o.title             as "objective_title",
       o.state             as "objective_state",
       q.id                as "quarter_id",
       q.label             as "quarter_label",
       coalesce(kr.id, -1) as "key_result_id",
       kr.title            as "key_result_title",
       kr.unit,
       kr.basis_value,
       kr.target_value,
       coalesce(m.id, -1)  as "measure_id",
       m."value"           as "measure_value",
       m.measure_date,
       m.created_on
from team t
         left join objective o on t.id = o.team_id
         left join quarter q on o.quarter_id = q.id
         left join key_result kr on o.id = kr.objective_id
         left join measure m on kr.id = m.key_result_id and m.measure_date = (select max(mm.measure_date)
                                                                              from measure mm
                                                                              where mm.key_result_id = m.key_result_id)
;