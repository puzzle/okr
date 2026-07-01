package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.team.Team;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, Long> {

    List<Team> findTeamsByName(String name);

    List<Team> findByMarkedAsArchivedAtIsNullOrMarkedAsArchivedAtGreaterThanEqual(LocalDate quarterStartDate);
}
