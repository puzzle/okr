package ch.puzzle.okr.repository;

import java.util.List;

import ch.puzzle.okr.models.Team;

import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, Long> {

    List<Team> findTeamsByName(String name);
}
