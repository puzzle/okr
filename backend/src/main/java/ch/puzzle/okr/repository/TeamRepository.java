package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Team;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TeamRepository extends CrudRepository<Team, Long> {

    List<Team> findTeamsByName(String name);
}
