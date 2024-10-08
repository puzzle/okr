package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends CrudRepository<Team, Long> {

    List<Team> findTeamsByName(String name);
}
