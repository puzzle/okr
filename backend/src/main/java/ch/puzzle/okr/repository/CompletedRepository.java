package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Completed;
import org.springframework.data.repository.CrudRepository;

public interface CompletedRepository extends CrudRepository<Completed, Long> {
}
