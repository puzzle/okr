package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Quarter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuarterRepository extends CrudRepository<Quarter, Long> {
    Optional<Quarter> findByLabel(@Param("label") String label);

    List<Quarter> getTop6ByOrderByStartDateDesc();

    @Query(value = "Select q from Quarter q where q.startDate <= :date and q.endDate >= :date")
    Quarter getActiveQuarter(@Param("date") LocalDate date);
}
