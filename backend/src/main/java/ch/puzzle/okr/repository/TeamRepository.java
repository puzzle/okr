package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Team;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends DeleteRepository<Team, Long> {

    List<Team> findTeamsByName(String name);
}
