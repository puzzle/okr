package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.Team;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectiveRepository extends DeleteRepository<Objective, Long> {

    Integer countByTeamAndQuarter(Team team, Quarter quarter);

    List<Objective> findObjectivesByTeamId(Long id);
}
