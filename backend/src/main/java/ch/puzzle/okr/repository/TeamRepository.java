package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Team;
import java.util.List;

public interface TeamRepository extends DeleteRepository<Team, Long> {

    List<Team> findTeamsByNameAndIsDeletedFalse(String name);
}
