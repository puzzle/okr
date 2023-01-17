package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Team;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends CrudRepository<Team, Long> {

    List<Team> findAllByNameNotOrderByNameAsc(String excludedTeamName);

    List<Team> findAllByIdInAndNameNotOrderByNameAsc(List<Long> ids, String excludedTeamName);

    Optional<Team> findByName(String name);
}
