package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Quarter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuarterRepository extends CrudRepository<Quarter, Long> {
    Quarter findByQuarterLabel(@Param("quarter_label") String quarterLabel);
}
