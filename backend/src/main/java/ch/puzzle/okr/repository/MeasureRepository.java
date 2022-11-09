package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.models.KeyResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeasureRepository extends CrudRepository<Measure, Long> {
    List<Measure> findByKeyResult(@Param("keyResult") KeyResult keyResult);
}
