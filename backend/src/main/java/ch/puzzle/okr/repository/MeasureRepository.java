package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Measure;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeasureRepository extends CrudRepository<Measure, Long> {
    List<Measure> findByKeyResult(@Param("keyResult") KeyResult keyResult);

    @Query(value = "select m.* from key_result as k, measure as m, (select max(created_on) as created_on, key_result_id from measure group by key_result_id) as t where t.key_result_id = m.key_result_id and t.created_on = m.created_on and k.id = m.key_result_id and k.objective_id = :objective_id", nativeQuery = true)
    List<Measure> findLastMeasuresOfKeyresults(@Param("objective_id") Long objectiveId);
}
