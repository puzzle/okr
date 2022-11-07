package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Quarter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface QuarterRepository extends CrudRepository<Quarter, Long> {
}