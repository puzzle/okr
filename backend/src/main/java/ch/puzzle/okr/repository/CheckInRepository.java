package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.checkIn.CheckIn;
import ch.puzzle.okr.models.checkIn.CheckIn;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface CheckInRepository extends CrudRepository<CheckIn, Long> {
    List<CheckIn> findMeasuresByKeyResultIdOrderByMeasureDateDesc(@Param("keyResult_id") Long keyResultId);

    List<CheckIn> findMeasuresByKeyResultId(@Param("keyResult_id") Long keyResultId);

    CheckIn findFirstMeasureByKeyResultIdOrderByMeasureDateDesc(@Param("keyResult_id") Long keyResultId);

    @Query(value = "select m.* from key_result as k, measure as m, (select max(measure_date) as measure_date, key_result_id from measure group by key_result_id) as t where t.key_result_id = m.key_result_id and t.measure_date = m.measure_date and k.id = m.key_result_id and k.objective_id = :objective_id", nativeQuery = true)
    List<CheckIn> findLastMeasuresOfKeyresults(@Param("objective_id") Long objectiveId);

    List<CheckIn> findMeasuresByKeyResultIdAndMeasureDate(Long keyResultId, Instant measureDate);

}
