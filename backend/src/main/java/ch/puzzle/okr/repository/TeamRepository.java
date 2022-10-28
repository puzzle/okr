package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Team;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, Long> {
}
