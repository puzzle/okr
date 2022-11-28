package ch.puzzle.okr.repository;

import java.util.List;
import ch.puzzle.okr.models.Objective;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectiveRepository extends CrudRepository<Objective, Long> {

    @Query(
            value = "select coalesce(AVG((CAST(coalesce(measure_value, 0) as decimal)/CAST(target_value as decimal))*100), 0) from\n" +
                    "(select max(created_on), max(value) as measure_value, max(target_value) as target_value\n" +
                    "from (select key_result.id, target_value, value, m.created_on, objective_id\n" +
                    "      from key_result LEFT JOIN measure m on key_result.id = m.key_result_id) as sub where objective_id = :objective_id group by id) as sub2",
            nativeQuery = true
    )
    Double getProgressOfObjective(@Param("objective_id") Long objectiveId);
}
