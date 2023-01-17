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
    @Query(value = "select k.target_value as targetValue, k.basis_value as basisValue, m.value as value from key_result as k, measure as m,(select max(measure_date) as measure_date, key_result_id from measure as m, key_result as k where k.objective_id = :objective_id and k.id = m.key_result_id group by key_result_id) as t where t.key_result_id = m.key_result_id and t.measure_date = m.measure_date and k.id = m.key_result_id and k.objective_id = :objective_id", nativeQuery = true)
    List<KeyResultMeasureValue> getCalculationValuesForProgress(@Param("objective_id") Long objectiveId);

    List<Objective> findByTeamId(long id);

    List<Objective> findByQuarterIdAndTeamId(Long quarterId, Long teamId);
}
