package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.Team;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectiveRepository extends DeleteRepository<Objective, Long> {

    Integer countByTeamAndQuarterAndIsDeletedFalse(Team team, Quarter quarter);

    List<Objective> findObjectivesByTeamIdAndIsDeletedFalse(Long id);
}
