package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.models.MeasureValue;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface MeasureRepository extends CrudRepository<Measure, Long> {
    List<Measure> findMeasuresByKeyResultIdOrderByMeasureDateDesc(@Param("keyResult_id") Long keyResultId);

    List<MeasureValue> findMeasuresByKeyResultId(@Param("keyResult_id") Long keyResultId);

    Measure findFirstMeasuresByKeyResultIdOrderByMeasureDateDesc(@Param("keyResult_id") Long keyResultId);

    @Query(value = "select m.* from key_result as k, measure as m, (select max(measure_date) as measure_date, key_result_id from measure group by key_result_id) as t where t.key_result_id = m.key_result_id and t.measure_date = m.measure_date and k.id = m.key_result_id and k.objective_id = :objective_id", nativeQuery = true)
    List<Measure> findLastMeasuresOfKeyresults(@Param("objective_id") Long objectiveId);

    List<Measure> findMeasuresByKeyResultIdAndMeasureDate(Long keyResultId, Instant measureDate);

}
