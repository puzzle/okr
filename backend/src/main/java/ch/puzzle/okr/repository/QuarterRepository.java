package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Quarter;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuarterRepository extends CrudRepository<Quarter, Long> {

    @Query("SELECT q FROM Quarter q WHERE q.startDate IS NOT NULL ORDER BY q.startDate DESC LIMIT 6")
    List<Quarter> findTop6ByOrderByStartDateDescWithoutNullStartDate();

    Quarter findByLabel(String label);

    @Query(value = "Select q from Quarter q where q.startDate <= :date and q.endDate >= :date")
    Quarter getActiveQuarter(@Param("date") LocalDate date);
}
