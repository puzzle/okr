package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Objective;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectiveRepository extends CrudRepository<Objective, Long> {

    @Query(value = "select coalesce(AVG((CAST(coalesce(value, kr2.basis_value, 0) as decimal) / CAST(coalesce(kr2.target_value, 0) as decimal))*100)) from key_result as kr2 LEFT JOIN"
            + "(select m.id, m.created_on, m.value, key_result_id from measure as m, (select max(m.created_on) as created_on, kr.id"
            + "      from key_result as kr LEFT JOIN measure m on kr.id = m.key_result_id"
            + "      group by kr.id) as sub where m.key_result_id = sub.id and m.created_on = sub.created_on) as sub3 on sub3.key_result_id = kr2.id where kr2.objective_id = :objective_id", nativeQuery = true)
    Double getProgressOfObjective(@Param("objective_id") Long objectiveId);

    List<Objective> findByTeamId(long id);
}
