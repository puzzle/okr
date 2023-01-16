package ch.puzzle.okr.repository;

import ch.puzzle.okr.helper.KeyResultMeasureValue;
import ch.puzzle.okr.models.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KeyResultRepository extends CrudRepository<KeyResult, Long> {
    List<KeyResult> findByObjective(@Param("objective") Objective objective);

    @Query(value = "select k.target_value as targetValue, k.basis_value as basisValue, m.value as value from key_result as k, measure as m, (select max(measure_date) as measureDate, key_result_id from measure where key_result_id = :keyResultId group by key_result_id) as t where t.key_result_id = m.key_result_id and t.measureDate = m.measure_date and k.id = m.key_result_id", nativeQuery = true)
    KeyResultMeasureValue getProgressValuesKeyResult(@Param("keyResultId") Long keyResultId);
}
