package ch.puzzle.okr.repository;

import ch.puzzle.okr.helper.KeyResultMeasureValue;
import ch.puzzle.okr.models.Objective;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectiveRepository extends CrudRepository<Objective, Long> {
    @Query(value = "select target_value as targetValue, basis_value as basisValue, value as value from (select key_result_id as krid, value from (select max(measure_date) as measure_date, k.id from measure as m, key_result as k where k.objective_id = :objectiveId and k.id = m.key_result_id group by k.id) as sub INNER JOIN measure as ms on sub.id = ms.key_result_id WHERE sub.measure_date = ms.measure_date) as sub RIGHT JOIN key_result as kr on kr.id = sub.krid where kr.objective_id = :objectiveId", nativeQuery = true)
    List<KeyResultMeasureValue> getCalculationValuesForProgress(@Param("objectiveId") Long objectiveId);

    List<Objective> findByTeamIdOrderByTitleAsc(long id);

    List<Objective> findByQuarterIdAndTeamIdOrderByTitleAsc(Long quarterId, Long teamId);
}
